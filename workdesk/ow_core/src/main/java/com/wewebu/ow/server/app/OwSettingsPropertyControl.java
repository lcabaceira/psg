package com.wewebu.ow.server.app;

import java.io.Writer;

import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Base class for a single settings property controls used in the OwSettings for the plugins. <br/>
 * Implements OwSettingsProperty and adds control UI functionality. <br/>
 * Unlike configuration node entries, the settings are stored for each user or for a site, 
 * where the configuration via getConfigNode is only set during startup.
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
public abstract class OwSettingsPropertyControl extends OwEventTarget implements OwSettingsProperty
{
    /** get optional help path for the control
     * 
     * @return JSP path or null if no help is available
     */
    public abstract String getHelpPath();

    /** get display name of property
     * @return String display name
     */
    public abstract String getDisplayName();

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected abstract Object createSingleClonedValue(Object oSingleValue_p);

    /** insert the property into a HTML form for editing
     *
     * @param w_p Writer to write HTML code to
     */
    public abstract void insertFormField(Writer w_p) throws Exception;

    /** gets a error message for this field, if update failed, clears the message automatically 
     *
     * @return String error message or an empty string
     */
    public abstract String getSafePropertyError();

    /** true = value was modified, false otherwise
     */
    public abstract boolean isModified();

    /** 
     * Check if this property has an error associated, without clear the error message
     * @since 2.5.2.0
     */
    public abstract boolean hasError();

    /** 
     * Gets a error message for this field 
     * @since 2.5.2.0
     * @return String error message or an empty string
     */
    public abstract String getPropertyError();

    public abstract void insertLabel(Writer w_p) throws Exception;

}
