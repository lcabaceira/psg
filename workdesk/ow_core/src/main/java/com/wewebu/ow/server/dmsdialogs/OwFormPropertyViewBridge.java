package com.wewebu.ow.server.dmsdialogs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.OwPropertySubregion;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;

import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * JSP form based property view bridge. 
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
public class OwFormPropertyViewBridge implements OwPropertyViewBridge
{
    /** the view*/
    protected OwObjectPropertyFormularView view;

    /** constructor */
    public OwFormPropertyViewBridge(OwObjectPropertyFormularView view_p)
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

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#setObjectRefEx(com.wewebu.ow.server.ecm.OwObject, boolean, java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, Collection propertyInfos_p) throws Exception
    {
        this.view.setObjectRef(objectRef_p);
        this.view.filterProperties(propertyInfos_p);
    }

    @Override
    public void setObjectRef(OwObject objectRef, boolean showSystemProperties) throws Exception
    {
        this.view.setObjectRef(objectRef);
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
        return view.onSave(request_p, reason_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#getMenu()
     */
    public OwMenuView getMenu()
    {
        return view.getMenu();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return view.isReadOnly();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#setSaveAllActive(boolean)
     */
    public void setSaveAllActive(boolean saveAllEnabled_p)
    {
        view.setSaveAllActive(saveAllEnabled_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge#setGroupPropertiesConfiguration(com.wewebu.ow.server.dmsdialogs.OwGroupPropertiesConfiguration)
     */
    public void setGroupPropertiesConfiguration(OwGroupPropertiesConfiguration groupPropertyConfiguration_p)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPropertyListConfiguration(OwPropertyListConfiguration propListConfiguration)
    {
        if (propListConfiguration != null)
        {
            Map<String, OwPropertyInfo> map = new LinkedHashMap<String, OwPropertyInfo>();
            for (OwPropertySubregion<OwPropertyInfo> subregion : propListConfiguration.getSubregions())
            {
                for (OwPropertyInfo pi : subregion.getPropertyInfos())
                {
                    map.put(pi.getPropertyName(), pi);
                }
            }
            try
            {
                view.filterProperties(map.values());
            }
            catch (Exception e)
            {
                throw new OwInaccessibleException("Unable to set filter for properties", e, "owd.core");
            }
        }
        else
        {
            try
            {
                view.filterProperties(null);
            }
            catch (Exception e)
            {
                throw new OwInaccessibleException("Unable to set filter for properties", e, "owd.core");
            }
        }
    }
}
