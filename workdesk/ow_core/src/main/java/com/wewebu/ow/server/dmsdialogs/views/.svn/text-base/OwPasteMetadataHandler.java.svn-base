package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Provide support for paste operations inside properties view.
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
 *@since 3.1.0.0
 */
public interface OwPasteMetadataHandler
{

    /** called when user presses the paste all metadata button
     */
    void onPasteAllMetaData(HttpServletRequest request_p) throws Exception;

    /** called when user presses the paste this metadata button
     */
    void onPasteThisMetaData(HttpServletRequest request_p) throws Exception;

    /** 
     * check if paste metadata is active and should be displayed for user
     */
    boolean isPasteMetadataActivated();

    /**
     * Render the "paste all" metadata HTML code.
     * @param w_p - the writer
     * @param view_p - the view
     * @throws Exception
     */
    void renderPasteAllMetadata(Writer w_p, OwEventTarget view_p) throws Exception;

    /**
     * Render the "paste" metadata HTML code
     * @param w_p
     * @param prop_p
     * @param view_p
     * @throws Exception
     */
    void renderPasteMetadata(Writer w_p, OwProperty prop_p, OwEventTarget view_p) throws Exception;

    /**
     * Set the current properties.
     * @param properties_p - the properties.
     */
    public void setProperties(OwPropertyCollection properties_p);
}