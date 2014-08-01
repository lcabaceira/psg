package com.wewebu.ow.server.ecmimpl.fncm5.viewer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.RealmSet;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory.EntireNetwork;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwWebApplication;

/**
 *<p>
 * Specific Daeja Viewer Annotation Security handler servlet.<br/>
 * Will parse the elements regarding the Daeja-P8
 * protocol and requests definitions.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwFNCM5AnnotSecurityServlet extends HttpServlet
{
    /**generated serialVersionUID   */
    private static final long serialVersionUID = -4417386052891205794L;

    private static final Logger LOG = OwLog.getLogger(OwFNCM5AnnotSecurityServlet.class);

    /**request suffix for available realms */
    protected static final String REQ_REALMS = "getRealms";
    /**request suffix for search  */
    protected static final String REQ_SEARCH = "getUsersGroups";

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String uri = req.getRequestURI();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwFNCM5AnnotSecurityServlet.doGet: URI = " + uri);
        }

        HttpSession session = req.getSession(false);
        if (session != null)
        {
            OwAppContext ctx = OwWebApplication.getContext(session);
            OwNetwork network = ctx.getRegisteredInterface(OwRoleManagerContext.class).getNetwork();
            OwFNCM5Credentials cred;
            try
            {
                cred = (OwFNCM5Credentials) network.getCredentials();
                User usr = com.filenet.api.core.Factory.User.fetchCurrent(cred.getConnection(), null);
                processMain(req, resp, ctx, usr);
            }
            catch (Exception e)
            {
                LOG.error("Could not retrieve credentials from network", e);
                sendError(resp, ctx.localize("OwFNCM5AnnotSecurityServlet.credentails.err", "Could not retrieve credentials"));
            }
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Your are not logged in or your Session is expired.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        LOG.warn("OwFNCM5AnnotSecurityServlet.doPost: Request not handled URI = " + req.getRequestURI());
    }

    protected void processMain(HttpServletRequest req, HttpServletResponse resp, OwAppContext ctx, User usr) throws ServletException, IOException
    {
        String uri = req.getRequestURI();
        if (uri.endsWith(REQ_REALMS))
        {
            processRealmsRequest(req, resp, ctx, usr);
        }
        else
        {
            if (uri.endsWith(REQ_SEARCH))
            {
                processSearchRequest(req, resp, ctx, usr);
            }
            else
            {
                processAnnotationRequest(req, resp, ctx, usr);
            }
        }
    }

    /**
     * Process the request and build an annotation dependent representation
     * of the permissions, with 
     * <pre>
     * &lt;accesslevel&gt;edit|view&lt;/accesslevel&gt;
     * &lt;permissions&gt;
     *     &lt;permission&gt;
     *       &lt;name&gt;#AUTHENTICATED-USERS&lt;/name&gt;
     *       &lt;inherited&gt;uninherited&lt;/inherited&gt;
     *       &lt;display_name&gt;#AUTHENTICATED-USERS&lt;/display_name&gt;
     *       &lt;type&gt;2001&lt;/type&gt; <-- Group
     *       &lt;accesstype&gt;1&lt;/accesstype&gt;
     *       &lt;owner_ctrl system_notes="Implicit Deny"&gt;none&lt;/owner_ctrl&gt;
     *       &lt;mod_content system_notes="Implicit Deny"&gt;none&lt;/mod_content&gt;
     *       &lt;view_content&gt;allow&lt;/view_content&gt;
     *    &lt;/permission&gt;
     *    &lt;permission&gt;
     *       &lt;name&gt;P8Admin-user@domain&lt;/name&gt;
     *       &lt;inherited&gt;uninherited&lt;/inherited&gt;
     *       &lt;display_name&gt;P8Admin&lt;/display_name&gt;
     *       &lt;type&gt;2000&lt;/type&gt;  <-- User
     *       &lt;accesstype&gt;1&lt;/accesstype&gt;
     *       &lt;owner_ctrl&gt;allow&lt;/owner_ctrl&gt;
     *       &lt;mod_content&gt;allow&lt;/mod_content&gt;
     *       &lt;view_content&gt;allow&lt;/view_content&gt;
     *    &lt;/permission&gt;
     * &lt/permissions&gt;
     * </pre>
     * @param req
     * @param resp
     * @param ctx
     * @param usr
     * @throws ServletException
     * @throws IOException
     */
    protected void processAnnotationRequest(HttpServletRequest req, HttpServletResponse resp, OwAppContext ctx, User usr) throws ServletException, IOException
    {
        String dmsid = req.getParameter("dmsid");
        String annotId = req.getParameter("annotationId");

        if (dmsid != null && annotId != null)
        {
            OwNetwork network = ctx.getRegisteredInterface(OwRoleManagerContext.class).getNetwork();
            try
            {
                OwFNCM5Object<?> obj = (OwFNCM5Object<?>) network.getObjectFromDMSID(dmsid, true);
                Document doc = (Document) obj.getNativeObject();
                Annotation annot = findAnnotation(req, doc, annotId);
                OwFNCM5NativeObjHelper.ensure(annot, PropertyNames.PERMISSIONS);
                AccessPermissionList perms = annot.get_Permissions();

                int mask = OwFNCM5PermissionHelper.getAccumulatedUserPermission(perms, network.getCredentials().getUserInfo());
                StringBuilder answer = new StringBuilder("<accesslevel>");
                if ((mask & AccessRight.WRITE_ACL_AS_INT) != 0)
                {
                    answer.append("edit");
                }
                else
                {
                    answer.append("view");
                }

                answer.append("</accesslevel><permissions>");
                Iterator<?> it = perms.iterator();
                String[] acls = { "owner_ctrl", "mod_content", "view_content" };
                int[] aclMasks = { AccessRight.WRITE_OWNER_AS_INT, AccessRight.WRITE_AS_INT, AccessRight.VIEW_CONTENT_AS_INT };
                while (it.hasNext())
                {
                    AccessPermission perm = (AccessPermission) it.next();
                    answer.append("<name>").append(perm.get_GranteeName()).append("</name>");
                    answer.append("<inherited>");
                    if (perm.get_PermissionSource() == PermissionSource.SOURCE_DIRECT)
                    {
                        answer.append("uninherited");
                    }
                    else
                    {
                        answer.append("inherited");
                    }
                    answer.append("</inherited><display_name>");
                    answer.append(perm.get_GranteeName());//TODO replace with real display name
                    answer.append("</display_name><type>");
                    int granteeType = perm.get_GranteeType().getValue();
                    answer.append(Integer.toString(granteeType));
                    answer.append("</type><accesstype>");
                    answer.append(Integer.toString(perm.get_AccessType().getValue()));
                    answer.append("</accesstype>");
                    for (int i = 0; i < acls.length; i++)
                    {
                        processMask(answer, acls[i], aclMasks[i], perm);
                    }
                }
                answer.append("</permissions>");

            }
            catch (Exception e)
            {
                LOG.error("Could not retrieve Annotation permissions", e);
                sendError(resp, "Error during request of permissions, see also server log");
            }
        }
        else
        {
            LOG.warn("OwFNCM5AnnotSecurityServlet.processAnnotationRequest: Could not be processed annotation or dmisd are not defined! dmsid = " + dmsid + " annotId = " + annotId);
        }
    }

    /**
     * Process mask is called by the {@link #processAnnotationRequest(HttpServletRequest, HttpServletResponse, OwAppContext, User)}
     * which is examine the access rights and write structure into the provided builder.
     * @param writer StringBuilder to add result
     * @param nodeName String name of the node to process
     * @param access int mask which is requested
     * @param perm AccessPermission to be analyzed
     */
    protected void processMask(StringBuilder writer, String nodeName, int access, AccessPermission perm)
    {
        writer.append("<").append(nodeName);
        if (perm.get_AccessType() == AccessType.ALLOW)
        {
            if ((perm.get_AccessMask() & access) != 0)
            {
                writer.append(">allow");
            }
            else
            {
                writer.append("system_notes=\"Implicit Deny\">none");
            }
        }
        else
        {
            if ((perm.get_AccessMask() & access) != 0)
            {
                writer.append(">denied");
            }
            else
            {
                writer.append(">none");
            }
        }
        writer.append("</").append(nodeName).append(">");
    }

    /**
     * Find corresponding Annotation from provided document, compare the Annotation through Id. 
     * Will return null if an Annotation could not be found.
     * @param req HttpServeltRequest
     * @param doc com.filenet.api.core.Document
     * @param annotId String Id which was extracted from request by default
     * @return Annotation or null if not found
     * @throws OwException
     */
    protected Annotation findAnnotation(HttpServletRequest req, Document doc, String annotId) throws OwException
    {
        OwFNCM5NativeObjHelper.ensure(doc, PropertyNames.ANNOTATIONS);
        AnnotationSet annoSet = doc.get_Annotations();
        Iterator it = annoSet.iterator();
        while (it.hasNext())
        {
            Annotation a = (Annotation) it.next();
            if (annotId.equalsIgnoreCase(a.get_Id().toString()))
            {
                return a;
            }
        }
        return null;
    }

    protected void processSearchRequest(HttpServletRequest req, HttpServletResponse resp, OwAppContext ctx, User usr) throws ServletException, IOException
    {
        /*type=users&query=Ad*&realm=o.__7sample */
        //        String realmId = req.getParameter("realm");
        //        if (realmId != null)
        //        {
        /* Unknown encoding ('!' == '.__f') 
         * .__f.__g.__7._Bl.__i.__j.__k.__t.__m.__n._Ci._C0._C6.__9
         * !   "   =   §   $   %   &   /   (   )   ä   ö    ü  ?
         */

        //            Iterator<Realm> it = getRealms(usr).iterator();
        //            Realm realm = null;
        //            while (it.hasNext())
        //            {
        //                realm = it.next();
        //                if (realm.)
        //            }

        //        }
    }

    /**
     * Return the available realms in a collection as XML structure.
     * <pre>
     * &lt;realms&gt;
     *  &lt;realm name="o=sample"/&gt;
     * &lt;/realms&gt;
     * </pre>
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if cannot send answer to requester
     */
    protected void processRealmsRequest(HttpServletRequest req, HttpServletResponse resp, OwAppContext ctx, User usr) throws IOException
    {
        Iterator<Realm> it = getRealms(usr).iterator();
        StringBuilder build = new StringBuilder("<?xml version=\"1.0\"?>");
        build.append("<realms>");
        while (it.hasNext())
        {
            Realm r = it.next();
            build.append("<realm name=\"");
            build.append(r.get_Name());
            build.append("\" />");
        }
        build.append("</realms>");
        byte[] answer = build.toString().getBytes();
        resp.setContentLength(answer.length);
        resp.setContentType("text/xml");
        resp.getOutputStream().write(answer);
        resp.getOutputStream().flush();
    }

    /**
     * Get all available Realms which can be found.
     * @param usr com.filenet.api.security.User
     * @return List of Realm's
     */
    protected List<Realm> getRealms(User usr)
    {
        RealmSet realmSet = EntireNetwork.getInstance(usr.getConnection()).get_AllRealms();
        LinkedList<Realm> lst = new LinkedList<Realm>();
        Iterator<?> it = realmSet.iterator();
        if (it.hasNext())
        {
            lst.add((Realm) it.next());
        }
        return lst;
    }

    protected String getEncoding()
    {
        return "UTF-8";
    }

    protected void sendError(HttpServletResponse resp, String msg) throws IOException
    {
        sendError(resp, msg, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    protected void sendError(HttpServletResponse resp, String msg, int httpStatus) throws IOException
    {
        if (httpStatus == HttpServletResponse.SC_OK)
        {
            resp.setStatus(httpStatus);
            StringBuilder msgToSend = new StringBuilder("<FAILED>NOT OK<DIALOG>");
            byte[] msgResp = msgToSend.append(msg).toString().getBytes(getEncoding());
            resp.setContentType("text/plain");
            resp.setContentLength(msgToSend.toString().length());
            resp.setCharacterEncoding(getEncoding());
            OutputStream out = resp.getOutputStream();
            out.write(msgResp);
            out.flush();
            out.close();
        }
        else
        {
            if (msg != null)
            {
                resp.sendError(httpStatus, msg);
            }
            else
            {
                resp.sendError(httpStatus);
            }
        }
    }
}