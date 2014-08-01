package com.wewebu.ow.server.dmsdialogs;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;

import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * {@link OwObjectPropertyView} based property view bridge. 
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
public class OwStandardPropertyViewBridge implements OwPropertyViewBridge
{
    /**the view*/
    protected OwObjectPropertyView view;

    /**constructor*/
    public OwStandardPropertyViewBridge(OwObjectPropertyView view_p)
    {
        super();
        this.view = view_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#getView()
     */
    public OwView getView()
    {
        return this.view;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#setReadOnlyContext(int)
     */
    public void setReadOnlyContext(int context_p)
    {
        this.view.setReadOnlyContext(context_p);
    }

    @Deprecated
    public void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, @SuppressWarnings("rawtypes") Collection propertyInfos_p) throws Exception
    {
        this.view.setObjectRefEx(objectRef_p, showSystemProperties_p, propertyInfos_p);
    }

    public void setObjectRef(OwObject objectRef, boolean showSystemProperties) throws Exception
    {
        this.view.setObjectRef(objectRef, showSystemProperties);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#isSystemPropertyView()
     */
    public boolean isSystemPropertyView()
    {
        return this.view.isSystemPropertyView();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#onBatchIndex()
     */
    public void onBatchIndex() throws Exception
    {
        this.view.onBatchIndex();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#onApply(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    public boolean onApply(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        return this.view.onApply(request_p, reason_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#getMenu()
     */
    public OwMenuView getMenu()
    {
        return this.view.getMenu();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.view.isReadOnly();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#setSaveAllActive(boolean)
     */
    public void setSaveAllActive(boolean saveAllEnabled_p)
    {
        this.view.setSaveAllActive(saveAllEnabled_p);
    }

    /**
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    public void setGroupPropertiesConfiguration(OwGroupPropertiesConfiguration groupPropertyConfiguration_p)
    {
        this.view.setGroupPropertiesConfiguration(groupPropertyConfiguration_p);
    }

    @Override
    public void setPropertyListConfiguration(OwPropertyListConfiguration propListConfiguration)
    {
        this.view.setPropertyListConfiguration(propListConfiguration);
    }
}
