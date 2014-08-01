package com.wewebu.ow.server.plug.owrecord;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMaxMinButtonControlView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * View to display a set of properties of the opened folder.
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
public class OwRecordPreviewPropertyView extends OwLayout implements OwFieldProvider
{
    private static final String AJAX_UPDATE_EVENT = "Update";
    /** the JSP page path to use */
    private static final String JSP_PAGE = "owrecord/OwRecordPreviewPropertyView.jsp";
    /** the JSP file used to render preview properties */
    private static final String JSP_PREVIEW_PROPERTIES_RENDERER = "owrecord/OwRecordPreviewPropertiesRenderer.jsp";
    /** a list of preview properties to display right away next to the record */
    protected List m_PreviewProperties;

    /** instance of the property field class */
    protected OwFieldManager m_theFieldManager;

    /** parameter name for the preview parameters to be displayed. */
    public static final String SETTINGS_PARAM_PREVIEW_PROPERTIES = "previewproperties";

    /** region of the min max view */
    public static final int MIN_MAX_REGION = 0;
    /** DIV id for property preview*/
    public static final String PREVIEW_PROPERTIES_DIV_ID = "OwRecordPreviewPropertyView";
    /** min max view module to display the minimize - maximize button */
    protected OwMaxMinButtonControlView m_minMaxView = new OwMaxMinButtonControlView(this, OwMaxMinButtonControlView.MODE_MAXIMIZE_BUTTON | OwMaxMinButtonControlView.MODE_MINIMIZE_BUTTON);

    /**flag specify if dynamic split is used */
    protected boolean m_isDynamicSplitUsed;

    /** init the target after the context is set.
      */
    protected void init() throws Exception
    {
        super.init();

        //register AJAX update event
        OwMainAppContext context = (OwMainAppContext) getContext();
        context.addAjaxUpdateContainer(PREVIEW_PROPERTIES_DIV_ID, getAjaxEventURL(AJAX_UPDATE_EVENT, null));

        // get preview property list from document
        m_PreviewProperties = (List) ((OwMasterDocument) getDocument()).getSafeSetting(SETTINGS_PARAM_PREVIEW_PROPERTIES, null);

        // add min max view, if required
        if ((m_PreviewProperties != null) && (m_PreviewProperties.size() > 0))
        {
            addView(m_minMaxView, MIN_MAX_REGION, null);
        }

        // get reference to the property field manager instance
        m_theFieldManager = ((OwMainAppContext) getContext()).createFieldManager();

        m_theFieldManager.setFieldProvider(this);

    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return getContext().localize("plug.owrecord.OwRecordPreviewPropertyView.title", "File Properties");
    }

    /** render only a region in the view, used by derived classes
    *
    * @param w_p Writer object to write HTML to
    * @param strRegion_p named region to render
    */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        OwProperty property = getProperty(strRegion_p);

        if (property != null)
        {
            m_theFieldManager.insertReadOnlyField(w_p, property);
        }
    }

    /** render the property
     * @param prop_p OwProperty
     * @param w_p Writer
    * @throws Exception 
     * */
    public void renderProperty(OwProperty prop_p, Writer w_p) throws Exception
    {
        m_theFieldManager.insertReadOnlyField(w_p, prop_p);
    }

    /** determine if region exists
     *
     * @param strRegion_p name of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        return m_PreviewProperties.contains(strRegion_p);
    }

    /** get the specified property from the current record
     * @param strPropertyName_p String property name
     * 
     * @return OwProperty or null if not available in record
     * */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwObject rootObject = ((OwRecordDocument) getDocument()).getCurrentRootFolder();
        OwObject folderObject = ((OwRecordDocument) getDocument()).getCurrentSubFolderObject();

        // lookup first in root folder, then in selected folder
        OwProperty property = null;

        try
        {
            property = rootObject.getProperty(strPropertyName_p);
        }
        catch (Exception e)
        {/* rootObject might be null or strPropertyName_p is undefined. */
        }

        if (property == null)
        {
            try
            {
                property = folderObject.getProperty(strPropertyName_p);
            }
            catch (Exception e)
            {/* folderObject might be null or strPropertyName_p is undefined. */
            }
        }

        // return property or null if not found    
        return property;
    }

    /** get the defined properties to display
     * @return List of String, or null if nothing is defined
     * */
    public List getPropertyNames()
    {
        return m_PreviewProperties;
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    public void onRender(Writer w_p) throws Exception
    {
        // reset field manager 
        m_theFieldManager.reset();

        // === render the defined set of properties for preview
        serverSideDesignInclude(JSP_PAGE, w_p);

    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT | OwFieldProvider.TYPE_SMALL;
    }

    public Object getFieldProviderSource()
    {
        return ((OwRecordDocument) getDocument()).getCurrentRootFolder();
    }

    public String getFieldProviderName()
    {
        return JSP_PAGE;
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return m_PreviewProperties;
    }

    /**
     * Setter for dynamic split flag.
     * @param isDynamicSplitUsed_p
     * @since 3.1.0.0
     */
    public void setDynamicSplitUsed(boolean isDynamicSplitUsed_p)
    {
        m_isDynamicSplitUsed = isDynamicSplitUsed_p;
    }

    /**
     * Getter for flag dynamic split
     * @return <code>true</code> if the dynamic splitter is used.
     * @since 3.1.0.0
     */
    public boolean isDynamicSplitUsed()
    {
        return m_isDynamicSplitUsed;
    }

    /**
     * Handler for AJAX request to update the content.
     * @param request_p - the AJAX request object.
     * @param response_p - the response
     * @throws Exception
     * @since 3.1.0.0
     */
    public void onAjaxUpdate(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        PrintWriter writer = response_p.getWriter();
        serverSideDesignInclude(JSP_PREVIEW_PROPERTIES_RENDERER, writer);
        writer.close();
    }
}