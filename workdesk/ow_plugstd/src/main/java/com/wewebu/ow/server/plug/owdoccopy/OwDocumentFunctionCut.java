package com.wewebu.ow.server.plug.owdoccopy;

import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation of the Document cut plugin.
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
public class OwDocumentFunctionCut extends OwDocumentFunctionAddToClipboard
{
    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdoccopy/cut.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdoccopy/cut_24.png");
    }

    /** check if plugin needs oParent_p parameter in onClick handler
     *
     * @return boolean true = enable plugin only, if a oParent_p parameter is available for the selected object, false = oParent_p can be null.
     */
    public boolean getNeedParent()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionAddToClipboard#onClickEventPostProcessing(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    protected void onClickEventPostProcessing(OwObject oObject_p, final OwObject oParent_p) throws Exception, OwInvalidOperationException
    {
        //just for preserving the old behavior, not necessary this is good
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwDocumentFunctionCut.invalidobject", "Item cannot be cut."));
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionAddToClipboard#createAddToClipboardCommand(com.wewebu.ow.server.ecm.OwObject, java.util.List)
     */
    protected OwCommandAddToClipboard createAddToClipboardCommand(OwObject oParent_p, List objects_p)
    {
        OwCommandAddToClipboard command = new OwCommandAddToClipboard(oParent_p, objects_p, getContext(), OwClipboard.CUT, this);
        return command;
    }

    @Override
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return oParent_p != null && oParent_p.getType() != OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER && super.isEnabled(objects_p, oParent_p, iContext_p);
    }

    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return oParent_p != null && oParent_p.getType() != OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER && super.isEnabled(oObject_p, oParent_p, iContext_p);
    }
}