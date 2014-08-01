package com.wewebu.ow.server.dmsdialogs.views;

/**
 *<p>
 * Defines the view of a single tool-item. 
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
public interface OwToolViewItem
{
    /** 
     * Get a title for the tool to display.
     * 
     * @return String
     */
    public abstract String getTitle();

    /** 
     * Get a description for the tool to display.
     * 
     * @return String
     */
    public abstract String getDescription();

    /** 
     * Get an icon for the tool to display.
     * 
     * @return String fully specified icon path
     */
    public abstract String getIcon();

    /** 
     * Get a big-icon for the tool to display.
     * 
     * @return String fully specified icon path
     */
    public abstract String getBigIcon();

    /**
     * Called when the user activates the tool item by clicking it. 
     * @throws Exception
     */
    public void onClickEvent() throws Exception;
}