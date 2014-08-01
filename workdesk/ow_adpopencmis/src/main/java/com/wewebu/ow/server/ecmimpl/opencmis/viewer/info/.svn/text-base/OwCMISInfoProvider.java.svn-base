package com.wewebu.ow.server.ecmimpl.opencmis.viewer.info;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ui.viewer.OwAbstractInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwDocInfoEnum;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwInfoRequest;

/**
 *<p>
 * OwInfoProvider for CMIS adaptor.<br/>
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
public class OwCMISInfoProvider extends OwAbstractInfoProvider
{
    private static final String APPLICATION_PDF_MIME = "application/pdf";

    private static final String IMAGE_TIFF_MIME = "image/tiff";

    private static final Logger LOG = OwLog.getLogger(OwCMISInfoProvider.class);

    private OwCMISNetwork network;
    private OwCMISObject obj;

    public OwCMISInfoProvider(OwCMISNetwork network_p)
    {
        this.network = network_p;
    }

    public String getContext()
    {
        return this.network.getDMSPrefix();
    }

    public void handleRequest(OwInfoRequest request_p, OutputStream answer_p) throws IOException
    {
        String dmsid = request_p.getParameter(OwInfoProvider.PARAM_DMSID);
        try
        {
            obj = (OwCMISObject) getNetwork().getObjectFromDMSID(dmsid, false);
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

                switch (OwDocInfoEnum.getEnumFromName(nodeValue))
                {
                    case HAS_PERM_TO_CHECKOUT:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && !obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            boolean canCheckOut = obj.getVersion().canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                            result = Boolean.toString(canCheckOut);
                        }
                    }
                        break;
                    case HAS_PERM_TO_CHECKIN:
                    {
                        result = Boolean.FALSE.toString();
                        if (obj.hasVersionSeries() && obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            boolean canCheckIn = obj.getVersion().canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                            result = Boolean.toString(canCheckIn);
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
                        //TODO: implement after annotations cmis-object model is designed
                        result = Boolean.FALSE.toString();
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
                        result = Boolean.valueOf(IMAGE_TIFF_MIME.equals(mimeType) || APPLICATION_PDF_MIME.equals(mimeType)).toString();
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
    protected OwCMISNetwork getNetwork()
    {
        return network;
    }

}
