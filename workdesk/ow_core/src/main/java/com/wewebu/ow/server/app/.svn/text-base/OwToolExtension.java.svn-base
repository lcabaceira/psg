package com.wewebu.ow.server.app;

import com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An extension of the tool item.<br/>
 * Defines the actions performed when a tool item is clicked.
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
public interface OwToolExtension extends OwToolViewItem
{
    /** 
     * Called when user invokes the extension.
     * 
     * @throws Exception
     */
    public abstract void onClickEvent() throws Exception;

    /** 
     * Initializes the extension.
     *  
     * @param context_p the current context 
     * @param confignode_p the configuration node
     * @throws Exception
     */
    public abstract void init(OwXMLUtil confignode_p, OwMainAppContext context_p) throws Exception;

    /**
     * Get the extension id.
     * @return the extension id.
     * @since 3.1.0.0
     */
    public String getId();
}