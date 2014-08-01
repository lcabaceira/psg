package com.wewebu.ow.server.ecmimpl.fncm5.viewer.info;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.filenet.api.constants.AccessRight;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.ui.viewer.OwAbstractInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwDocInfoEnum;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwInfoRequest;

/**
 *<p>
 * OwInfoProvider for P8 5.0 adaptor.<br/>
 * Used for advanced viewing, to identify the possible actions
 * which can be executed on a document.
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
public class OwFNCM5InfoProvider extends OwAbstractInfoProvider
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5InfoProvider.class);

    private OwFNCM5Network network;
    private OwFNCM5Object<?> obj;

    public OwFNCM5InfoProvider(OwFNCM5Network network_p)
    {
        this.network = network_p;
    }

    public String getContext()
    {
        return OwFNCM5Network.DMS_PREFIX;
    }

    public void handleRequest(OwInfoRequest request_p, OutputStream answer_p) throws IOException
    {
        String dmsid = request_p.getParameter(OwInfoProvider.PARAM_DMSID);
        try
        {
            obj = getNetwork().getObjectFromDMSID(dmsid, false);
            super.handleRequest(request_p, answer_p);
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Object could not be retrieved by DMSID = " + dmsid, e);
        }
        finally
        {
            this.obj = null;//release object every time
        }
    }

    protected String process(Node item_p)
    {
        Node sub = item_p.getFirstChild();
        String result = null;
        if (sub.getNodeType() == Node.TEXT_NODE)
        {
            String nodeValue = sub.getNodeValue();
            try
            {
                OwFNCM5Permissions perms = (OwFNCM5Permissions) obj.getPermissions();
                OwBaseUserInfo usr = getNetwork().getCredentials().getUserInfo();
                switch (OwDocInfoEnum.getEnumFromName(nodeValue))
                {
                    case HAS_PERM_TO_CHECKOUT:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && !obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            boolean acc = OwFNCM5PermissionHelper.hasPermission(perms, usr, AccessRight.MAJOR_VERSION_AS_INT | AccessRight.MINOR_VERSION_AS_INT);
                            result = Boolean.toString(acc);
                        }
                    }
                        break;
                    case HAS_PERM_TO_CHECKIN:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            boolean acc = OwFNCM5PermissionHelper.hasPermission(perms, usr, AccessRight.CHANGE_STATE_AS_INT);
                            result = Boolean.toString(acc);
                        }
                    }
                        break;
                    case HAS_PERM_TO_CANCEL_CHECKOUT:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            boolean canCancelCheckOut = obj.getVersion().canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && obj.getVersion().isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                            result = Boolean.toString(canCancelCheckOut);
                        }
                    }
                        break;
                    case HAS_PERM_TO_CREATE_ANNOTATIONS:
                    {
                        result = Boolean.FALSE.toString();
                        boolean cAnnot = getNetwork().getObjectClass("Annotation", obj.getResource()).canCreateNewObject();
                        if (cAnnot)
                        {
                            cAnnot = OwFNCM5PermissionHelper.hasPermission(perms, usr, AccessRight.LINK_AS_INT);
                        }
                        result = Boolean.toString(cAnnot);
                    }
                        break;
                    case NUMBER_OF_PARENTS:
                    {
                        result = Integer.toString(obj.getParents().size());
                    }
                        break;
                    case HAS_PERM_TO_CREATE_DOCUMENT:
                    {
                        result = "" + obj.getObjectClass().canCreateNewObject();
                    }
                        break;
                    case IS_CHECKOUT:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            result = Boolean.TRUE.toString();
                        }
                    }
                        break;
                    case IS_VERSIONABLE:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries())
                        {
                            result = Boolean.TRUE.toString();
                        }
                    }
                        break;
                    case HAS_PERM_TO_MOD:
                    {
                        result = Boolean.FALSE.toString();
                        String mimeType = obj.getMIMEType();
                        result = Boolean.valueOf("image/tiff".equals(mimeType) || "application/pdf".equals(mimeType)).toString();
                    }
                        break;
                    default:
                        ;
                }
            }
            catch (Exception e)
            {
                LOG.error("Cannot compute the value of the " + nodeValue + " property.", e);
            }
        }
        return result;
    }

    /**
     * Getter for internal used network
     * @return OwFNCM5Network
     */
    protected OwFNCM5Network getNetwork()
    {
        return network;
    }

}
