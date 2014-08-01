package com.wewebu.ow.server.ecmimpl.fncm5.viewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.security.Permission;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Specific Daeja Viewer annotation handler servlet.<br/>
 * Will work regarding the specific protocol of the 
 * Daeja Viewer.
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
public class OwFNCM5DaejaAnnotServlet extends HttpServlet
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5DaejaAnnotServlet.class);
    /**Generated serial version UID*/
    private static final long serialVersionUID = -6565112715059034231L;

    /**Encoding which is used to parse and communicate by default
     * @see #getEncoding()*/
    public static final String DEFAULT_ENCODING = "UTF-8";
    /**default annotation security type*/
    private static final String DEFAULT_ANNOTATION_SECURITY_TYPE = "InheritFromDocument";

    // some keys
    private static final String ADD = "add";
    private static final String CHANGE = "change";
    private static final String REMOVE = "remove";

    private static final String F_ANNOTATEDID = "F_ANNOTATEDID";
    private static final String F_ID = "F_ID";
    private static final String FNANNO = "FnAnno";
    private static final String PROPDESC = "PropDesc";
    private static final String PAGE_NUMBER_ATTR = "F_PAGENUMBER";
    private static final String STATE_ATTR = "STATE";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if (req.getSession(false) != null)
        {
            processRequest(req, resp);
        }
        else
        {
            sendErrorMessage(resp, "Forbidden Request", HttpServletResponse.SC_FORBIDDEN);
        }
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        OwMainAppContext context = (OwMainAppContext) session.getAttribute(OwWebApplication.CONTEXT_KEY);
        // get the DMSID of the document of the annotation
        String dmsId = req.getParameter(OwMimeManager.DMSID_KEY);

        try
        {
            // get the document of the annotation
            OwObject annotatedObj = context.getNetwork().getObjectFromDMSID(dmsId, false);
            Document fnDocument = (Document) annotatedObj.getNativeObject();

            // get the annotation XML out of the http request
            org.w3c.dom.Document annotationsDoc = OwFNCM5AnnotationXMLUtil.readAnnotationData(req); //read data

            // parse the document and extract a list of OwFNCMAnnotation
            List<OwFNCM5Annotation> processList = createAnnotationProcessLists(annotationsDoc, context);

            // now save the Annotations to P8
            String currentUser = context.getCredentials().getUserInfo().getUserLongName();
            Properties newAnnotations = processAnnotationList(processList, fnDocument, currentUser, context);

            // send response to viewer
            sendResponse(newAnnotations, resp);

            // signal event for history
            context.getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_UPLOAD, OwEventManager.HISTORY_STATUS_OK);
        }
        catch (EngineRuntimeException ex)
        {
            if (ex.getExceptionCode().getId() == ExceptionCode.E_ACCESS_DENIED.getId())
            {
                LOG.debug("The user has not sufficient permissions to save the annotations", ex);
                sendErrorMessage(resp, context.localize("fncm.OwFNCMDejaViewerAnnotationServlet.permError", "You do not have the authorization to save annotations."));
            }
            //TODO else part
        }
        catch (Exception e)
        {
            LOG.warn("Error occurred during saving of the Annotations", e);

            try
            {
                // signal event for history
                context.getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_UPLOAD, OwEventManager.HISTORY_STATUS_FAILED);
            }
            catch (Exception eh)
            {
                // Ignore history exceptions
            }

            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * create a list of {@link OwFNCM5Annotation} objects of the given XML annotation document
     * @param annotationsDoc_p
     * @param context_p 
     * @return a {@link List} of {@link OwFNCM5Annotation}
     */
    private List<OwFNCM5Annotation> createAnnotationProcessLists(org.w3c.dom.Document annotationsDoc_p, OwMainAppContext context_p)
    {
        List<OwFNCM5Annotation> annotationList = new LinkedList<OwFNCM5Annotation>();
        NodeList annotationElements = annotationsDoc_p.getElementsByTagName(FNANNO);
        int length = annotationElements.getLength();
        for (int i = 0; i < length; i++)
        {
            Element annotationElement = (Element) annotationElements.item(i);
            OwFNCM5Annotation annotObj = new OwFNCM5Annotation(annotationElement, context_p);
            annotationList.add(annotObj);
        }
        return annotationList;
    }

    /**
     * save the given annotations
     * @param annotLst
     * @param nativeDoc
     * @param ctx 
     * @return a {@link Properties} object
     * @throws Exception
     */
    private Properties processAnnotationList(List<OwFNCM5Annotation> annotLst, Document nativeDoc, String currentUser, OwAppContext ctx) throws Exception
    {
        Properties newAnnotationIDs = new Properties();
        // separate list for adding, modifying and deleted
        for (OwFNCM5Annotation a : annotLst)
        {
            String mode = a.getState(); // check whether it's being added, deleted or changed

            if (mode.equals(ADD))
            {
                String id = addNewAnnotsToCE(a, nativeDoc, currentUser, ctx);
                newAnnotationIDs.put(a.getID(), id);
            }
            else if (mode.equals(CHANGE))
            {
                if (a.getID() != null && a.getID().length() != 0)
                {
                    modifyAnnotsToCE(a, nativeDoc, ctx);
                }
            }
            else if (mode.equals(REMOVE)) //same with remove.  If removing a newly added annotation, need cleanup
            {
                if (a.getID() != null && a.getID().length() != 0)
                {
                    deleteAnnotsToCE(a, nativeDoc, ctx);
                }
            }
        }

        return newAnnotationIDs;
    }

    /**
     * add new annotations to P8
     * @param owdAnnot_p OwFNCM5Annotation object to create as native annotation
     * @param fnDoc_p com.filenet.api.core.Document to add annotation
     * @return String representing the Id of the new created Annotation
     * @throws UnsupportedEncodingException 
     * @throws TransformerException 
     */
    protected String addNewAnnotsToCE(OwFNCM5Annotation owdAnnot_p, Document fnDoc_p, String currentUser_p, OwAppContext context_p) throws UnsupportedEncodingException, TransformerException
    {
        //        Properties newAnnotationIDs = new Properties();
        //        Iterator it = list_p.iterator();
        //        while(it.hasNext())
        //        {
        //            OwFNCM5Annotation owdAnnot = (OwFNCM5Annotation) it.next();

        Annotation fnAnnot = createFileNetAnnotation(owdAnnot_p, fnDoc_p, currentUser_p, context_p);
        fnAnnot.set_AnnotatedObject(fnDoc_p);
        fnAnnot.save(RefreshMode.REFRESH);
        OwFNCM5NativeObjHelper.ensure(fnAnnot, PropertyNames.ID);
        owdAnnot_p.prepareContent(fnAnnot.get_Id().toString());
        setAnnotationContent(owdAnnot_p, fnAnnot);
        fnAnnot.save(RefreshMode.REFRESH);
        //            newAnnotationIDs.put(owdAnnot.getID(), fnAnnot.get_Id().toString());
        //        }
        //        return newAnnotationIDs;
        return fnAnnot.get_Id().toString();
    }

    /**
     * Create a new native Annotation object, regarding the provided values.
     * @param owdAnnot_p OwFNCM5Annotation
     * @param fnDoc_p com.filenet.api.core.Document document to be annotated
     * @param currentUser_p String representing current user
     * @param ctx_p OwAppContext
     * @return com.filenet.api.core.Annotation
     */
    protected Annotation createFileNetAnnotation(OwFNCM5Annotation owdAnnot_p, Document fnDoc_p, String currentUser_p, OwAppContext ctx_p)
    {
        return fnDoc_p.createAnnotation(0, ClassNames.ANNOTATION);
    }

    /**
     * Set the parsed content into the provided com.filenet.api.core.Annotation, 
     * will not call the save method of the annotation to persist the content.
     * @param owdAnnot_p OwFNCM5Annotation POJO representation 
     * @param fnAnnot_p com.filenet.api.core.Annotation native representation
     * @throws UnsupportedEncodingException 
     * @throws TransformerException 
     */
    @SuppressWarnings("unchecked")
    private void setAnnotationContent(OwFNCM5Annotation owdAnnot_p, Annotation fnAnnot_p) throws UnsupportedEncodingException, TransformerException
    {

        String content = owdAnnot_p.getContent();
        byte[] bytes = content.getBytes(getEncoding());
        ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
        ContentTransfer nativeStream = com.filenet.api.core.Factory.ContentTransfer.createInstance();
        ContentElementList contentLst = com.filenet.api.core.Factory.ContentElement.createList();
        nativeStream.setCaptureSource(inStream);
        nativeStream.set_RetrievalName("annotation.xml");
        contentLst.add(nativeStream);
        fnAnnot_p.set_ContentElements(contentLst);
    }

    /**
     * delete given Annotations in P8
     * @param owdAnnot_p OwFNCM5Annotation
     * @param fnDoc_p com.filenet.api.core.Document which was annotated
     * @param context_p OwAppContext
     */
    private void deleteAnnotsToCE(OwFNCM5Annotation owdAnnot_p, Document fnDoc_p, OwAppContext context_p)
    {
        String id = owdAnnot_p.getID();
        if ((id != null) && (id.length() > 0))
        {
            AnnotationSet setAnnot = fnDoc_p.get_Annotations();
            Iterator<?> it = setAnnot.iterator();
            while (it.hasNext())
            {
                Annotation fnAnnot = (Annotation) it.next();
                if (id.equalsIgnoreCase(fnAnnot.get_Id().toString()))
                {
                    fnAnnot.delete();
                    fnAnnot.save(RefreshMode.NO_REFRESH);
                }
            }
        }
        else
        {
            LOG.warn("OwFNCM5DejaViewerAnnotationServlet.deleteAnnotsToCE: Problem while deleting annotation: Invalid Annotation ID = " + id);
        }
    }

    /**
     * modify the given annotations in P8
     * @param owdAnnot_p OwFNCM5Annotation to be modified
     * @param fnDoc_p com.filenet.api.core.Document which was annotated
     * @param context_p OwAppContext
     * @throws UnsupportedEncodingException
     * @throws TransformerException 
     */
    private void modifyAnnotsToCE(OwFNCM5Annotation owdAnnot_p, Document fnDoc_p, OwAppContext context_p) throws UnsupportedEncodingException, TransformerException
    {
        String id = owdAnnot_p.getID();
        if (id != null && id.length() != 0)
        {
            Annotation note = null;
            Iterator it = fnDoc_p.get_Annotations().iterator();
            while (it.hasNext())
            {
                note = (Annotation) it.next();
                if (id.equalsIgnoreCase(note.get_Id().toString()))
                {
                    break;
                }
                else
                {
                    note = null;
                }
            }

            if (note != null)
            {
                Element root = owdAnnot_p.getRootElement();

                root.removeAttribute(STATE_ATTR);
                Element propDesc = OwXMLDOMUtil.getChildElement(root, PROPDESC);
                propDesc.setAttribute(F_ANNOTATEDID, id);
                propDesc.setAttribute(F_ID, id);

                setAnnotationContent(owdAnnot_p, note);

                AccessPermissionList perms = owdAnnot_p.getPermissions();

                // If the permissions come back empty, then it means that
                // security hasn't changed.  In this case, we'll just set
                // content and leave the security as is.
                if (perms != null && perms.size() != 0)
                {
                    // Workaround for security policies
                    //                    Permissions newPerms = ObjectFactory.getPermissions();
                    //                    Iterator permIT = perms.iterator();
                    //                    while (permIT.hasNext())
                    //                    {
                    //                        Permission item = (Permission) permIT.next();
                    //                        Permission newItem = ObjectFactory.getPermission(item.getAccess(), item.getAccessType(), item.getGranteeName(), item.getGranteeType());
                    //                        newPerms.add(newItem);
                    //                    }

                    note.set_Permissions(perms);
                }
                note.save(RefreshMode.NO_REFRESH);
            }
        }
        else
        {
            LOG.warn("OwFNCMDejaViewerAnnotationServlet.modifyAnnotsToCE: Invalid annotation ID, error occurred on deleting the annotation.");
        }

    }

    /**
     * Send response with the new Annotations ids.
     * @param newAnnotations_p
     * @param response_p
     * @throws IOException
     */
    protected void sendResponse(Properties newAnnotations_p, HttpServletResponse response_p) throws IOException
    {
        Set entries = newAnnotations_p.entrySet();
        StringBuffer sb = new StringBuffer();
        try
        {
            if (entries.size() != 0)
            {
                sb.append("RESPONSE\n");
                for (Iterator iterator = entries.iterator(); iterator.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    sb.append(key + "=" + value + "\n");
                }
            }
            else
            {
                sb.append("OK\n");
            }

            String responseText = sb.toString();

            //write entire response
            byte[] bytes = responseText.getBytes(getEncoding());
            response_p.setContentLength(bytes.length);
            OutputStream out = response_p.getOutputStream();
            out.write(bytes);
            out.close();
        }
        catch (Exception e) //send an error message to the viewer.  currently viewer does not do anything with it
        {
            sendErrorMessage(response_p, e.getMessage());
        }

    }

    /**
     * get the default permission of the annotation
     * @param currentUser_p
     * @param document_p
     * @return AccessPermissionList for the object
     */
    public static AccessPermissionList getDefaultAnnotationPermissions(String currentUser_p, Document document_p, ObjectStore objectStore_p, OwMainAppContext context_p)
    {
        OwXMLUtil bootstrapConf = context_p.getConfiguration().getBootstrapConfiguration();
        String annotationSecurityType = DEFAULT_ANNOTATION_SECURITY_TYPE;
        try
        {
            OwXMLUtil ecmXmlUtil = bootstrapConf.getSubUtil("EcmAdapter");
            OwXMLUtil annotationSecurityXMLUtil = ecmXmlUtil.getSubUtil("AnnotationSecurity");
            if (annotationSecurityXMLUtil != null)
            {
                annotationSecurityType = annotationSecurityXMLUtil.getSafeStringAttributeValue("type", DEFAULT_ANNOTATION_SECURITY_TYPE);
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("The <AnnotationSecurity/> element is not set in bootstrap.xml. We will use default annotation security type: " + DEFAULT_ANNOTATION_SECURITY_TYPE);
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Cannot load the annotation security type from xml, the default annotation security type will be used: " + DEFAULT_ANNOTATION_SECURITY_TYPE, e);
        }
        OwAnnotationPermissionsStrategy permissionStrategy = null;
        if (DEFAULT_ANNOTATION_SECURITY_TYPE.equals(annotationSecurityType))
        {
            permissionStrategy = new OwDocumentBasedAnnotationPermissionsStrategy();
        }
        else
        {
            permissionStrategy = new OwAnnotationClassBasedPermissionsStrategy(objectStore_p);
        }
        AccessPermissionList permissions = permissionStrategy.createAnnotationPermissions(currentUser_p, document_p);
        return permissions;
    }

    /**
     * Send a message to the Viewer with corresponding syntax,
     * which will display it in the UI.
     * <p>HTTP Status will be set to 200, it's a process error not communication</p>
     * @param resp HttpServletResponse response to use
     * @param msg String message to display
     * @throws IOException
     */
    protected void sendErrorMessage(HttpServletResponse resp, String msg) throws IOException
    {
        sendErrorMessage(resp, msg, HttpServletResponse.SC_OK);
    }

    /**
     * Send a message to the Viewer with corresponding syntax,
     * internal status will set to FAILED, HTTP status as defined by parameter. 
     * @param resp HttpServletResponse
     * @param msg String message to be send
     * @param httpStatus int representing HTTP status (see RFC 2616 section 10)
     * @throws IOException
     */
    protected void sendErrorMessage(HttpServletResponse resp, String msg, int httpStatus) throws IOException
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

    /**(overridable)
     * Method which defines which encoding should be used.
     * <p>By default this method return the {@link #DEFAULT_ENCODING}({@value #DEFAULT_ENCODING})</p>
     * @return String representing the encoding
     */
    protected String getEncoding()
    {
        return DEFAULT_ENCODING;
    }

    /**
     *<p>
     * Strategy for getting permissions..
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
    public static abstract class OwAnnotationPermissionsStrategy
    {
        /**
         * Creates the permission for current annotation object.
         * @param currentUser_p
         * @param document_p
         * @return the permissions.
         */
        public abstract AccessPermissionList createAnnotationPermissions(String currentUser_p, Document document_p);

        /**
         * Filter current permissions by user.
         * @param currentUser_p
         * @param permissions_p
         * @return the filtered permissions.
         */
        protected AccessPermissionList filterPermissionsByUser(String currentUser_p, AccessPermissionList permissions_p)
        {
            for (Iterator iterator = permissions_p.iterator(); iterator.hasNext();)
            {
                Permission permission = (Permission) iterator.next();
                String granteeName = permission.get_GranteeName();
                if (granteeName.equals(currentUser_p))
                {
                    iterator.remove();
                }
            }
            return permissions_p;
        }
    }

    /**
     *<p>
     * Provide permissions from the document.
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
    public static class OwDocumentBasedAnnotationPermissionsStrategy extends OwAnnotationPermissionsStrategy
    {

        public AccessPermissionList createAnnotationPermissions(String currentUser_p, Document document_p)
        {
            OwFNCM5NativeObjHelper.ensure(document_p, PropertyNames.PERMISSIONS);
            AccessPermissionList permissions = document_p.get_Permissions();
            permissions = filterPermissionsByUser(currentUser_p, permissions);
            //            int accessRight = Permission.RIGHT_WRITE_OWNER | Permission.RIGHT_WRITE_ACL | Permission.RIGHT_READ_ACL | Permission.RIGHT_DELETE | Permission.RIGHT_CREATE_INSTANCE | Permission.RIGHT_VIEW_CONTENT | Permission.RIGHT_READ
            //                    | Permission.RIGHT_WRITE;
            //
            //            Permission userPermission = ObjectFactory.getPermission(accessRight, Permission.TYPE_ALLOW, currentUser_p, BaseObject.TYPE_USER);
            //            permissions.add(userPermission);
            return permissions;
        }
    }

    /**
     *<p>
     * Provide default permissions from Annotation class.
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
    public static class OwAnnotationClassBasedPermissionsStrategy extends OwAnnotationPermissionsStrategy
    {
        /**the object store*/
        private ObjectStore nativeOs;

        /**
         * Constructor.
         * @param objectStore_p - the object store
         */
        public OwAnnotationClassBasedPermissionsStrategy(ObjectStore objectStore_p)
        {
            this.nativeOs = objectStore_p;
        }

        public AccessPermissionList createAnnotationPermissions(String currentUser_p, Document document_p)
        {
            ClassDescription objClassDesc = Factory.ClassDescription.fetchInstance(nativeOs, ClassNames.ANNOTATION, null);

            AccessPermissionList permissions = objClassDesc.get_DefaultInstancePermissions();
            /* New P8 5.0 API replace the user and creator_owner on it's own, code is kept if not working as assumed
            if (permissions != null)
            {
            // remove creator owner permissions, in order to have the same permissions as Workplace.
                AccessPermissionList filteredPermissions = filterPermissionsByUser(currentUser_p, permissions);
                boolean isCreatorOwnerReplaced = false;
                for (Iterator iterator = filteredPermissions.iterator(); iterator.hasNext();)
                {
                    Permission permission = (Permission) iterator.next();
                    String granteeName = permission.get_GranteeName();

                    if (granteeName != null && granteeName.equals(SpecialPrincipal.CREATOR_OWNER))
                    {
                        permission.set_GranteeName(currentUser_p);
                        isCreatorOwnerReplaced = true;
                    }
                }
                //handle situation when we have #CREATOR-OWNER in the permission list
                //in other way, return what we already read from Annotation class
                if (isCreatorOwnerReplaced)
                {
                    permissions = filteredPermissions;
                }
            }*/
            return permissions;
        }
    }
}
