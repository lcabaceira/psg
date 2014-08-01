package com.wewebu.ow.server.dmsdialogs;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;

import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Property view bridge interface.
 * Implementors define custom property view and behavior for create object dialogs. 
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
 *@since 3.0.0.0
 */
public interface OwPropertyViewBridge
{
    /**
     * 
     * @return the underlying property rendering {@link OwView}
     */
    OwView getView();

    /** set context to be used for the read-only property 
    *
    * @param context_p int as defined with OwPropertyClass.CONTEXT_...
    */
    void setReadOnlyContext(int context_p);

    /**
     * Sets the object whose properties will be displayed.
     * @param objectRef_p The objectRef to set.
     * @param showSystemProperties_p if true show only system properties.
     * @param propertyInfos_p collection of property info objects describing the properties to edit and their read only state, or null to edit all
     * @deprecated since 4.2.0.0 use {@link #setObjectRef(OwObject, boolean)} and {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    @Deprecated
    void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, Collection propertyInfos_p) throws Exception;

    /**
     * Sets the object whose properties will be displayed.
     * @param objectRef The objectRef to set.
     * @param showSystemProperties if true show only system properties.
     * @throws Exception
     * @since 4.2.0.0
     */
    void setObjectRef(OwObject objectRef, boolean showSystemProperties) throws Exception;

    /**
     * Return <code>true</code> if the view is used to render system properties.
     * @return <code>true</code> if the view is used to render system properties
     */
    boolean isSystemPropertyView();

    /**
     * called by the client when the indexes should be set from the previous values as defined with setBatchProperties.
     * @throws Exception
     */
    void onBatchIndex() throws Exception;

    /** 
     * Event called when user clicked Apply button in menu 
     *  
     *   @param request_p  HttpServletRequest
     *   @param reason_p Optional reason object submitted in addMenuItem
     *   
     * 
     *  @return true = fields have been saved, false  = one or more invalid fields
     */
    boolean onApply(HttpServletRequest request_p, Object reason_p) throws Exception;

    /**
     * Get the menu associated with this view.
     * @return the {@link OwMenuView} object associated with this view.
     */
    OwMenuView getMenu();

    /**
     * Get the status of the view.
     * @return <code>true</code> if the view is in read-only mode.
     */
    boolean isReadOnly();

    /**
     * Set the save all flag.
     * @param saveAllEnabled_p
     * @since 3.1.0.0
     */
    void setSaveAllActive(boolean saveAllEnabled_p);

    /**
     * Set the configuration for grouped properties.
     * @param groupPropertyConfiguration_p - the configuration object.
     * @since 3.1.0.0 
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    @Deprecated
    public void setGroupPropertiesConfiguration(com.wewebu.ow.server.dmsdialogs.OwGroupPropertiesConfiguration groupPropertyConfiguration_p);

    /**
     * Define a PropertyList configuration which allows a
     * @param propListConfiguration OwPropertyListConfiguration
     * @since 4.2.0.0
     */
    void setPropertyListConfiguration(OwPropertyListConfiguration propListConfiguration);
}
