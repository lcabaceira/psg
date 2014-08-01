package com.wewebu.ow.server.plug.owdoccopy;

import java.util.List;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation of the Document copy plugin.
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
public class OwDocumentFunctionCopy extends OwDocumentFunctionAddToClipboard
{
    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {

        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdoccopy/copy.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdoccopy/copy_24.png");
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
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwDocumentFunctionCopy.invalidobject", "Item cannot be copied."));
        }
    }

    /**
     * Create add to clipboard command
     * @param oParent_p
     * @param objects_p
     * @return a {@link OwCommandAddToClipboard} object, configured for the needed operation
     */
    protected OwCommandAddToClipboard createAddToClipboardCommand(final OwObject oParent_p, List objects_p)
    {
        OwCommandAddToClipboard command = new OwCommandAddToClipboard(oParent_p, objects_p, getContext(), OwClipboard.COPY, this);
        return command;
    }

}