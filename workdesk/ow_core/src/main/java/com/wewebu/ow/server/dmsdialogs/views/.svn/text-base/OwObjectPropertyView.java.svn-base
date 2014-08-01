package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.OwPropertySubregion;
import org.alfresco.wd.ui.conf.OwSimplePropertySubregion;
import org.alfresco.wd.ui.conf.prop.OwPropertyGroup;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwViewUpdateException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * View Module to edit OwObject Properties.
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
@SuppressWarnings("rawtypes")
public class OwObjectPropertyView extends OwLayout implements OwMultipanel, OwFieldProvider
{
    /** attribute bag name for the saved batch index properties stored in the document */
    protected static final String BATCH_INDEX_PROPERTIES_ATTRIBTUE_BAG_NAME = "OwObjectPropertyViewBatchIndexProperties";

    /** layout region definition for the error rendering */
    public static final int ERRORS_REGION = 4;

    private static final String GROUP_HEADER_CLASS_NAME = "groupHeader";
    /**UI class for expanded group representation
     * @since 4.1.1.1*/
    public static final String GROUP_EXPANDED_CLASS_NAME = "OwGroupBodyExpanded";
    /**UI class for collapsed group representation
     * @since 4.1.1.1*/
    public static final String GROUP_COLLAPSED_CLASS_NAME = "OwGroupBodyCollapsed";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectPropertyView.class);

    /** layout region definition for the main (property list) region */
    public static final int MAIN_REGION = 1;

    /** layout region definition for the menu region */
    public static final int MENU_REGION = 2;

    /** layout region definition for the mode selection region */
    public static final int MODES_REGION = 3;
    /** ID for the modes select box */
    private static final String MODES_SELECT_ID = "owmodes";
    /** no mode @see {@link OwObjectPropertyView#m_icurrentmode} */
    private static final int NO_MODE = -1;
    /** query string for the property name */
    protected static final String QUERY_KEY_PROPNAME = "prop";

    /** flag to enable a internal menu and override getMenu() */
    public static final int VIEW_MASK_DISABLE_INTERNAL_MENU = 0x0000002;

    /** flag to activate the paste metadata buttons*/
    public static final int VIEW_MASK_ENABLE_PASTE_METADATA = 0x0000001;

    /** flag to force read-only mode */
    public static final int VIEW_MASK_READONLY = 0x0000008;

    /** flag to render vertical */
    public static final int VIEW_MASK_RENDER_VERTICAL = 0x0000004;

    /** contains the properties for batch-Import, set in owplugins.xml ==> <BatchPropertyList>  */
    protected Collection m_batchIndexProperties;

    /** batch index properties helper*/
    protected OwBatchPropertiesHandler m_batchPropertiesHandler;

    /** inform user on next edited object 
     * @since 3.1.0.3*/
    private boolean m_displayNoSaveMsg;

    /** Flag to enable save all button option when more than one documents are selected.<br>
     * The save all button will be rendered if this {@link #m_saveAllActive} is set to <code>true</code>,
     * the {@link #m_enablesaveall} is set to <code>true</code> and <code>OwObjectPropertyView.m_fReadOnly</code>
     * flag is <code>false</code>.
     * */
    protected boolean m_enablesaveall;

    /** flag indicating that all properties of object are read-only */
    protected boolean m_fAllPropertiesReadonly = true;

    /** flag indicating if object can be edited */
    protected boolean m_fReadOnly;
    /** flag indicating system properties. true = edit only system properties, false = edit only NON system properties*/
    protected boolean m_fSystem;
    /** the configuration for grouping properties
     * @deprecated since 4.2.0.0 OwPropertyListConfiguration should be used*/
    private com.wewebu.ow.server.dmsdialogs.OwGroupPropertiesConfiguration m_groupPropertyConfiguration;
    /**New Configuration/Filter reference, replacing previous filter and grouping handler
     * @since 4.2.0.0 */
    private OwPropertyListConfiguration propertyListConfiguration;
    /** menu ID of the apply button */
    protected int m_iAppyBtnIndex;
    /** currently selected setProperties mode */
    private int m_icurrentmode = NO_MODE;

    /** menu ID of the lock button */
    protected int m_iLockIndex;

    /** menu ID of the next button */
    protected int m_iNextButtonIndex = -1;

    /** menu ID of the unlock button */
    protected int m_iUnLockIndex;

    /** set of flags indicating the behavior of the view
     */
    protected int m_iViewMask;
    /** Menu for buttons in the view */
    protected OwSubMenuView m_MenuView;
    /** mode type to use for getModes method */
    private int m_modetype = OwObjectClass.OPERATION_TYPE_SET_PROPERTIES;

    /** object reference the view is working on */
    protected OwObject m_ObjectRef;

    /** paste metadata handler*/
    protected OwPasteMetadataHandler m_pasteMetadataHandler;

    /** properties from the object <br> 
     * <b>Do NOT access it directly use {@link #getFilteredClonedProperties()} instead</b>*/
    protected OwPropertyCollection m_Properties;

    /** collection of properties to show, or null to show and edit all */
    private Collection<String> m_propertyfilter;

    /** context to be used for the read-only property */
    protected int m_ReadOnlyContext = OwPropertyClass.CONTEXT_NORMAL;

    /** contains a map of properties to be rendered read only or null */
    protected Map<String, Boolean> m_readOnlyProperties;
    /**
     * Flag that indicates the active status of the Save All button.<br>
     * The save all button will be rendered if this {@link #m_saveAllActive} is set to <code>true</code>,
     * the {@link #m_enablesaveall} is set to <code>true</code> and <code>OwObjectPropertyView.m_fReadOnly</code>
     * flag is <code>false</code>.
     * */
    protected boolean m_saveAllActive;

    /** instance of the property field class */
    protected OwFieldManager m_theFieldManager;

    /**
     *  proceed with update calls even if no changes need to be saved boolean flag
     *  @since 3.2.0.3
     */
    private boolean m_updateNoChanges = false;

    /**
     *  post success message on success flag
     *  @since 3.2.0.3
     */
    private boolean m_informUserOnSuccess = true;

    /**
     *  Proceed with update calls even if no changes need to be saved boolean flag setter.
     *  @since 3.2.0.3
     */
    public void setUpdateNoChanges(boolean updateNoChanges)
    {
        this.m_updateNoChanges = updateNoChanges;
    }

    /**
     *  Post success message on success flag setter.
     *  @since 3.2.0.3
     */
    public void setInformUserOnSuccess(boolean informUserOnSuccess)
    {
        this.m_informUserOnSuccess = informUserOnSuccess;
    }

    /**
     * Flag indicate if all properties are read only
     * @return boolean - true if all properties are read only
     * @since 3.1.0.0
     */
    @SuppressWarnings("unchecked")
    private boolean areAllPropertiesReadOnly()
    {
        Iterator<OwProperty> it = null;
        try
        {
            it = getFilteredClonedProperties().values().iterator();
        }
        catch (Exception e1)
        {
            throw new OwInaccessibleException(getContext().localize("app.OwObjectPropertyView.err.getClonedProperties", "Unable to retrieve properties from reference object."), e1);
        }
        while (it.hasNext())
        {
            OwProperty property = it.next();
            try
            {
                OwPropertyClass propertyClass = property.getPropertyClass();

                if (m_fSystem != propertyClass.isSystemProperty())
                {
                    continue;
                }

                if (!isPropertyReadonly(property))
                {
                    m_fAllPropertiesReadonly = false;
                }

            }
            catch (Exception e)
            {
                LOG.error("Cannot read object properties " + e.getMessage());
            }

        }
        return m_fAllPropertiesReadonly;
    }

    /**
     * clear stored batch index data
     * @throws OwInvalidOperationException
     */
    public void clearBatchIndex() throws OwInvalidOperationException
    {
        m_batchPropertiesHandler.clearBatchIndex();
    }

    /**
     * Creates the batch properties handler
     * @return an {@link OwBatchPropertiesHandler} object
     * @since 3.1.0.0
     */
    protected OwBatchPropertiesHandler createBatchPropertiesHandler()
    {
        return new OwStandardBatchPropertiesHandler(getDocument());
    }

    /**
     * Create a {@link OwPasteMetadataHandler} object
     * @return the newly created handler
     * @since 3.1.0.0
     */
    protected OwPasteMetadataHandler createPasteMetadataHandler()
    {
        return new OwStandardPasteMetadataHandler((OwMainAppContext) getContext(), m_fSystem, hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA));
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach the field manager as well, this is especially necessary if we use it in a dialog
        m_theFieldManager.detach();
    }

    /**
     * Get current Clipboard, often used in this class.
     * @return OwClipboard the current exist
     */
    protected OwClipboard getClipboard()
    {
        return ((OwMainAppContext) getContext()).getClipboard();
    }

    /** implementation for the OwFieldProvider interface
     */
    public OwField getField(String strFieldClassName_p) throws Exception
    {
        OwProperty prop = (OwProperty) getFilteredClonedProperties().get(strFieldClassName_p);

        if (prop == null)
        {
            throw new OwObjectNotFoundException("OwObjectPropertyView.getField: Property not found, strFieldClassName_p = " + strFieldClassName_p);
        }

        return prop;
    }

    /** get a reference to the internal field manager */
    public OwFieldManager getFieldManager()
    {
        return m_theFieldManager;
    }

    /** get a name that identifies the field provider, can be used to create IDs
     *
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getObjectRef().getName();
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return getObjectRef();
    }

    /** implementation for the OwFieldProvider interface
     * get the type of field provider as defined with TYPE_...
     */
    public int getFieldProviderType()
    {
        switch (getReadOnlyContext())
        {
            case OwPropertyClass.CONTEXT_ON_CREATE:
                return TYPE_META_OBJECT | TYPE_CREATE_OBJECT;

            case OwPropertyClass.CONTEXT_ON_CHECKIN:
                return TYPE_META_OBJECT | TYPE_CHECKIN_OBJECT;

            default:
                return TYPE_META_OBJECT;
        }
    }

    /** get all the properties in the form
     *
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getFilteredClonedProperties().values();
    }

    /**
     * Get a property collection which is based on defined {@link #getPropertyListConfiguration()}
     * @return OwPropertyCollection
     * @throws Exception
     */
    protected OwPropertyCollection getFilteredClonedProperties() throws Exception
    {
        if (getObjectRef() != null)
        {
            if (m_Properties == null)
            {
                if (getPropertyListConfiguration() != null)
                {
                    m_propertyfilter = new LinkedList<String>();
                    m_readOnlyProperties = new HashMap<String, Boolean>();
                    // build property filter and fill readonly map
                    for (OwPropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> subregion : getPropertyListConfiguration().getSubregions())
                    {
                        for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo pi : subregion.getPropertyInfos())
                        {
                            try
                            {
                                //check object definition for properties
                                getObjectRef().getObjectClass().getPropertyClass(pi.getPropertyName());
                                m_propertyfilter.add(pi.getPropertyName());
                                if (pi.isReadOnly())
                                {
                                    m_readOnlyProperties.put(pi.getPropertyName(), Boolean.TRUE);
                                }
                            }
                            catch (OwObjectNotFoundException onfEx)
                            {
                                //ignore
                            }
                        }
                    }
                }

                //fetch all properties with one request
                OwPropertyCollection ret = getObjectRef().getClonedProperties(m_propertyfilter);

                if (null != m_propertyfilter)
                {
                    // === only get the properties among m_propertyfilter list
                    OwPropertyCollection retFiltered = new OwStandardPropertyCollection();
                    Iterator it = m_propertyfilter.iterator();

                    while (it.hasNext())
                    {
                        String sPropName = (String) it.next();
                        OwProperty prop = (OwProperty) ret.get(sPropName);

                        if (prop != null)
                        {
                            // === filter all hidden properties
                            // see Bug 1511
                            if (!prop.isHidden(getReadOnlyContext()))
                            {
                                retFiltered.put(sPropName, prop);
                            }
                        }
                    }

                    m_Properties = retFiltered;
                }
                else
                {
                    // === get all properties, but filter all hidden properties
                    OwPropertyCollection retFiltered = new OwStandardPropertyCollection();

                    Iterator it = ret.values().iterator();

                    while (it.hasNext())
                    {
                        OwProperty prop = (OwProperty) it.next();

                        // see Bug 1511
                        if (prop != null && !prop.isHidden(getReadOnlyContext()))
                        {
                            retFiltered.put(prop.getPropertyClass().getClassName(), prop);
                        }
                    }

                    m_Properties = retFiltered;
                }
            }

        }
        return m_Properties == null ? new OwStandardPropertyCollection() : m_Properties;
    }

    /** (overridable) get the menu of the view
     * you can add menu items or override to have your own menu for the view (see VIEW_MASK_DISABLE_INTERNAL_MENU)
     */
    public OwMenuView getMenu()
    {
        return m_MenuView;
    }

    /**
     * Getter of current referenced OwObject
     * @return OwObject (can return null)
     */
    public OwObject getObjectRef()
    {
        return m_ObjectRef;
    }

    /** overridable get the style class name for the row
     *
     * @param iIndex_p int row index
     * @param prop_p OwProperty current OwObject
     *
     * @return String with style class name
     */
    protected String getRowClassName(int iIndex_p, OwProperty prop_p)
    {
        return ((iIndex_p % 2) != 0) ? "OwObjectPropertyView_OddRow" : "OwObjectPropertyView_EvenRow";
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
            OwField field = (OwField) getFilteredClonedProperties().get(sName_p);
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /**
     * Get save button id index
     * @return int save btn index
     */
    public int getSaveBtnIndex()
    {
        return m_iAppyBtnIndex;
    }

    /** get the status after the last update
     *
     * @return boolean true = update was ok, all fields are valid, false = update caused errors, one or more fields are invalid
     */
    public boolean getUpdateStatus()
    {
        return m_theFieldManager.getUpdateStatus();
    }

    /** check if a certain view property is enabled
     * @param iViewMask_p int as defined with VIEW_PROPERTY_...
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return (m_iViewMask & iViewMask_p) > 0;
    }

    /**
     * Post a message for informing user about a successfully save operation.
     * @since 3.0.0.0
     */
    protected void informUserOnSuccess()
    {
        if (m_informUserOnSuccess)
        {
            // inform user
            ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyView.saved", "Changes have been saved."));
        }
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === get reference to the property fieldmanager instance
        m_theFieldManager = ((OwMainAppContext) getContext()).createFieldManager();
        m_theFieldManager.setExternalFormTarget(getFormTarget());

        // === create own menu
        if (!hasViewMask(VIEW_MASK_DISABLE_INTERNAL_MENU))
        {
            m_MenuView = new OwSubMenuView();
            addView(m_MenuView, MENU_REGION, null);
        }

        // === add buttons
        if (getMenu() != null)
        {
            // apply button
            m_iAppyBtnIndex = getMenu().addFormMenuItem(this, getContext().localize("app.OwObjectPropertyView.save", "Save"), "Apply", null);
            getMenu().setDefaultMenuItem(m_iAppyBtnIndex);
            // initially disable
            getMenu().enable(m_iAppyBtnIndex, false);
        }
        //initialize batchPropertyHelper
        m_batchPropertiesHandler = createBatchPropertiesHandler();
        m_pasteMetadataHandler = createPasteMetadataHandler();
        setDisplayNoSaveMsg(false);
    }

    /**
     * Check if the given properties group is with errors.
     * @param theGroup_p
     * @return - <code>true</code> if the group is with errors
     * @throws Exception
     * @since 3.1.0.0
     */
    protected boolean isGroupWithErrors(OwPropertyGroup<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> theGroup_p) throws Exception
    {
        List<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> props = theGroup_p.getProperties();
        boolean result = false;
        for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo prop : props)
        {
            OwField field = (OwField) getFilteredClonedProperties().get(prop.getPropertyName());
            if (field != null)
            {
                String errorString = m_theFieldManager.getSafeFieldError(field);
                if (errorString != null && errorString.length() > 0)
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /** (overridable)
     * Detect/Verify if this property has to be read only or editable.
     * Controlling UI how to render the provided property.
     * @param property_p OwProperty
     * @return boolean
     * @throws Exception
     */
    protected boolean isPropertyReadonly(OwProperty property_p) throws Exception
    {
        if (property_p.isReadOnly(getReadOnlyContext()))
        {
            return true;
        }
        if ((null != m_readOnlyProperties) && m_readOnlyProperties.containsKey(property_p.getFieldDefinition().getClassName()))
        {
            return true;
        }
        return false;
    }

    /**
     * Check if this view displays properties in a read only manner.
     * @return <code>true</code> if the properties are rendered in a read only way
     * @since 3.0.0.0
     */
    public boolean isReadOnly()
    {
        //if all properties are read only, set read only view
        if (areAllPropertiesReadOnly())
        {
            m_fReadOnly = true;
        }
        return m_fReadOnly;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwLayout#isRegion(int)
     */
    public boolean isRegion(int region_p)
    {
        switch (region_p)
        {
            case ERRORS_REGION:
                return true;
            case MODES_REGION:
                try
                {
                    if (m_modetype == OwObjectClass.OPERATION_TYPE_UNDEF)
                    {
                        return false;
                    }
                    else
                    {
                        return (null != getObjectRef().getObjectClass().getModes(m_modetype));
                    }
                }
                catch (Exception e)
                {
                    return false;
                }

            case MAIN_REGION:
                return true;

            default:
                return super.isRegion(region_p);
        }
    }

    /** overridable checks if lock should be supported
     */
    protected boolean isSupportLock()
    {
        return true;
    }

    /**
     *
     * @return <code>true</code> if this property view edits only system properties <br>
     *         <code>false</code>if this property view  edits only NON system properties\
     * @since 3.0.0.0
     */
    public boolean isSystemPropertyView()
    {
        return m_fSystem;
    }

    /** check if view has validated its data and the next view can be enabled
      *
      * @return boolean true = can forward to next view, false = view has not yet validated
      *
      */
    public boolean isValidated() throws Exception
    {
        OwPropertyCollection props = getFilteredClonedProperties();
        if (null == props)
        {
            return false;
        }

        Iterator it = props.values().iterator();

        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();
            OwPropertyClass propClass = prop.getPropertyClass();

            if ((!propClass.isSystemProperty()) && propClass.isRequired() && ((prop.getValue() == null) || (prop.getValue().toString().length() == 0)))
            {
                return false;
            }
        }

        return true;
    }

    public Boolean mapProperties(Map map_p, String name_p)
    {
        if (map_p.get(name_p) == null)
        {
            return null;
        }
        if (map_p.get(name_p).toString().equalsIgnoreCase("true"))
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    /**
     * Method called when property group is collapsed using AJAX.
     * Keep in synch the group collapsing status with visual info displayed.
     * @param request_p - the {@link HttpServletRequest} object
     * @param response_p - the {@link HttpServletResponse} object.
     * @since 3.1.0.0
     */
    public void onAjaxPropertyGroupCollapse(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        String groupId = request_p.getParameter("owGroupId");
        String isGroupCollapsed = request_p.getParameter("isGroupCollapsed");
        if (groupId != null && isGroupCollapsed != null && getPropertyListConfiguration() != null)
        {
            OwPropertySubregion subRegion = getPropertyListConfiguration().getGroupRegion(groupId);
            if (subRegion != null)
            {
                subRegion.getPropertyGroup().setCollapsed(Boolean.parseBoolean(isGroupCollapsed));
            }
        }
    }

    /** event called when user clicked Apply button in menu
     *
     *   call getUpdateStatus() after save to check if fields are invalid
     *
     *   @param request_p  HttpServletRequest
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *
     *
     *  @return true = fields have been saved, false  = one or more invalid fields
     */
    public boolean onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        if (onApplyInternal(request_p, oReason_p))
        {
            // inform other views, so they can display correctly
            safeUpdate(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY);
            safeUpdate(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY);

            informUserOnSuccess();

            return true;
        }
        else
        {
            if (m_displayNoSaveMsg)
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.NotSaved", "Nothing was changed therefore the document was not saved.");
                ((OwMainAppContext) getContext()).postMessage(message);
                safeUpdate(this, OwUpdateCodes.OBJECT_PROPERTIES_NOT_CHANGED);
            }
            // invalid fields, document not saved
            return false;
        }
    }

    /** event called to safe changes
    *
    *   call getUpdateStatus() after save to check if fields are invalid
    *
    *   @param request_p  HttpServletRequest
    *   @param oReason_p Optional reason object submitted in addMenuItem
    *
    *
    *  @return true = fields have been successfully saved, false = one or more invalid fields, or nothing was changed. Could not save anything
    */
    protected boolean onApplyInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // update form data
        OwPropertyCollection changedProperties = update(request_p);
        if (null == changedProperties)
        {
            return m_updateNoChanges;
        }
        return save(changedProperties);
    }

    /** called by the client when the indexes should be set from the previous values as defined with setBatchProperties
     * @throws Exception */
    public void onBatchIndex() throws Exception
    {
        m_batchPropertiesHandler.onBatchIndex();
    }

    /** event called when user clicked Lock button in menu
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p  HttpServletRequest
     */
    public void onLock(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        getObjectRef().setLock(true);

        // inform other views, so they can display the new lock state correctly
        safeUpdate(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY);
    }

    /** event called when user clicked Next button to switch to the next pane
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p  HttpServletRequest
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        if (onApplyInternal(request_p, null))
        {
            // activate the next view
            ((OwView) oReason_p).activate();
        }
    }

    /** called when user presses the paste all metadata button
     */
    public void onPasteAllMetaData(HttpServletRequest request_p) throws Exception
    {

        m_pasteMetadataHandler.onPasteAllMetaData(request_p);
    }

    /** called when user presses the paste this metadata button
     */
    public void onPasteThisMetaData(HttpServletRequest request_p) throws Exception
    {
        m_pasteMetadataHandler.onPasteThisMetaData(request_p);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // === render
        serverSideDesignInclude("OwObjectPropertyView.jsp", w_p);
        ((OwMainAppContext) getContext()).setFocusControlID(m_theFieldManager.getFocusControlID());
    }

    /** event called when user clicked UnLock button in menu
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p  HttpServletRequest
     */
    public void onUnLock(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        getObjectRef().setLock(false);

        // inform other views, so they can display the new lock state correctly
        safeUpdate(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY);
    }

    /** called by the framework to update the event target after the request was handled.
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender calls.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_OBJECT_PROPERTY:
            {
                // determine read only flag according to lock status, role and object capability
                if (m_fSystem || hasViewMask(VIEW_MASK_READONLY))
                {
                    m_fReadOnly = true;
                }
                else
                {
                    m_fReadOnly = (getObjectRef().canLock() && (!getObjectRef().getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))) || (!getObjectRef().canSetProperties(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
                }

                if (getMenu() != null)
                {
                    // check if object supports lock
                    if (isSupportLock() && getObjectRef().canLock())
                    {
                        // disable menu items that are not needed
                        if (getObjectRef().getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                        {
                            // === Object locked, so disable lock button
                            getMenu().enable(m_iLockIndex, false);
                            getMenu().enable(m_iUnLockIndex, true);
                            getMenu().setDefaultMenuItem(m_iAppyBtnIndex);
                        }
                        else
                        {
                            // === Object not locked, so disable unlock button
                            getMenu().enable(m_iLockIndex, true);
                            getMenu().enable(m_iUnLockIndex, false);
                            getMenu().setDefaultMenuItem(m_iLockIndex);
                        }
                    }
                }
                m_batchPropertiesHandler.setProperties(getFilteredClonedProperties());
                m_pasteMetadataHandler.setProperties(getFilteredClonedProperties());
            }
                break;
            case OwUpdateCodes.UPDATE_OBJECT_VERSION:
            case OwUpdateCodes.FILTER_CHANGED:
            {
                this.m_Properties = null;
                if (m_batchPropertiesHandler != null)
                {
                    m_batchPropertiesHandler.setProperties(getFilteredClonedProperties());
                }
                if (m_pasteMetadataHandler != null)
                {
                    m_pasteMetadataHandler.setProperties(getFilteredClonedProperties());
                }

            }
                break;
            default:/*nothing*/
        }
    }

    protected void renderErrorsRegion(Writer w_p) throws Exception
    {
        w_p.write(m_theFieldManager.renderErrors());
    }

    /** overridable to render additional columns
     *
     * @param w_p Writer
     */
    protected void renderExtraColumnHeader(java.io.Writer w_p) throws Exception
    {
        // === paste metadata column
        if (m_pasteMetadataHandler.isPasteMetadataActivated())
        {
            // Paste all metadata column
            w_p.write("<th class=\"OwPasteMetadata\">");
            m_pasteMetadataHandler.renderPasteAllMetadata(w_p, this);
            w_p.write("</th>");
        }
    }

    /**
    * overridable to render additional columns
    * @param w_p Writer
    * @param prop_p current OwProperty to render
    * @since 3.1.0.0
    */
    protected void renderExtraColumnRows(Writer w_p, OwProperty prop_p) throws Exception
    {
        // === paste metadata column
        if (m_pasteMetadataHandler.isPasteMetadataActivated())
        {
            w_p.write("\n<td class=\"OwPasteMetadata\">");

            m_pasteMetadataHandler.renderPasteMetadata(w_p, prop_p, this);

            w_p.write("</td>");
        }
    }

    /**
     * Render properties group header.
     * @param theGroup_p
     * @param w_p
     * @param strRowClassName_p
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void renderGroupHeader(OwPropertyGroup theGroup_p, Writer w_p, String strRowClassName_p) throws Exception
    {
        if (isGroupWithErrors(theGroup_p))
        {
            theGroup_p.setCollapsed(false);
        }
        // === draw the header
        w_p.write("<tr class=\"" + strRowClassName_p + "\" ");
        w_p.write("onClick=\"");
        w_p.write("OwPropGroup.toggleGroup('" + theGroup_p.getGroupId() + "','" + getAjaxEventURL("PropertyGroupCollapse", null) + "');");
        w_p.write("\">\n");
        w_p.write("<td id=\"td_" + OwHTMLHelper.encodeJavascriptVariableName(theGroup_p.getGroupId()) + "\" class=\"");
        if (!theGroup_p.isCollapsed())
        {
            w_p.write("OwGroupExpanded");
        }
        else
        {
            w_p.write("OwGroupCollapsed");
        }
        w_p.write("\">&nbsp;</td>");
        int numberOfColumns = this.m_pasteMetadataHandler.isPasteMetadataActivated() ? 3 : 2;
        w_p.write("<td colspan=\"" + numberOfColumns + "\" class=\"OwGroupName\">");
        w_p.write(OwHTMLHelper.encodeToSecureHTML(theGroup_p.getDisplayName(getContext())));
        w_p.write("<input type=\"hidden\" value =\"" + theGroup_p.isCollapsed() + "\" id=\"isCollapsed_" + theGroup_p.getGroupId() + "\"/>");
        // === required column
        w_p.write("</td>");

        w_p.write("</tr>\n");
    }

    /**
     * Render properties horizontally.
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void renderHorizontalProperties(Writer w_p) throws Exception
    {
        Iterator it = getFilteredClonedProperties().values().iterator();

        String strRowClassName = null;
        int iIndex = 0;

        while (it.hasNext())
        {
            OwProperty property = (OwProperty) it.next();
            OwPropertyClass propertyClass = property.getPropertyClass();

            // === filter for system / non system
            if (m_fSystem != propertyClass.isSystemProperty())
            {
                continue;
            }

            // === toggle even odd flag
            strRowClassName = getRowClassName(iIndex, property);

            renderHorizontalProperty(w_p, strRowClassName, property, propertyClass);

            iIndex++;
        }
    }

    /**
     * Render the property as a row in table.
     * @param w_p - the writer
     * @param strRowClassName_p - the CSS class name
     * @param property_p - the property to be rendered
     * @param propertyClass_p - the property class
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void renderHorizontalProperty(Writer w_p, String strRowClassName_p, final OwProperty property_p, OwPropertyClass propertyClass_p) throws Exception
    {
        // === draw the properties
        w_p.write("<tr class=\"" + strRowClassName_p + "\">\n");

        // === overridable to render additional columns
        renderExtraColumnRows(w_p, property_p);

        // === required column
        w_p.write("<td class=\"OwRequired\">");

        if (propertyClass_p.isRequired())
        {
            String requiredTooltip = getContext().localize("app.OwObjectPropertyView.required", "Required");
            w_p.write("<img src=\"" + getContext().getDesignURL() + "/images/OwObjectPropertyView/required.png\" alt=\"" + requiredTooltip + "\" title=\"" + requiredTooltip + "\"/>");
        }
        else
        {
            w_p.write("<img src=\"" + getContext().getDesignURL() + "/images/OwObjectPropertyView/notrequired.gif\" alt=\"\">");
        }

        w_p.write("</td>\n");

        w_p.write("<td class=\"OwPropertyName\" title=\"" + propertyClass_p.getClassName() + "\">");
        boolean propReadOnly = this.isPropertyReadonly(property_p);
        m_theFieldManager.insertLabel(w_p, m_fReadOnly, propReadOnly, property_p, ":&nbsp;&nbsp;", true);

        w_p.write("</td>\n<td class=\"DefaultInput\">\n");

        // property control
        w_p.write("<div class=\"OwPropertyControl\">");

        if (m_fReadOnly || propReadOnly)
        {
            // property can not be edited
            m_theFieldManager.insertReadOnlyField(w_p, property_p);
        }
        else
        {
            // property can be edited
            w_p.write("\n");
            m_theFieldManager.insertEditField(w_p, property_p);
            //at least one property is not read only
            m_fAllPropertiesReadonly = false;
        }

        w_p.write("</div>\n");

        // === display error column for this property
        w_p.write("<div class=\"OwPropertyError\">");
        w_p.write(m_theFieldManager.getSafeFieldError(property_p));
        w_p.write("</div>\n");

        // === display js validation HotInfo
        w_p.write("<div class=\"OwPropertyError\" id=\"HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(property_p) + "\"></div>\n");

        w_p.write("</td>\n");

        w_p.write("</tr>\n");
    }

    /**
     * Render the properties groups
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 will be removed soon, see {@link #renderPropertyListConfiguration(Writer)} and {@link #renderPropertyGroup(Writer, OwPropertyGroup, int, List)}
     */
    @SuppressWarnings("unchecked")
    protected void renderHorizontalPropertyGroups(Writer w_p) throws Exception
    {
        int index = 0;
        for (OwPropertyGroup owPropertyGroup : m_groupPropertyConfiguration.getGroups())
        {
            List<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> groupProperties = owPropertyGroup.getProperties();
            List<OwProperty> properties = new ArrayList<OwProperty>(groupProperties.size());
            for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo owPropertyInfo : groupProperties)
            {
                String propertyName = owPropertyInfo.getPropertyName();
                OwProperty prop = (OwProperty) getFilteredClonedProperties().get(propertyName);
                if (prop != null)
                {
                    properties.add(prop);
                }
                else
                {
                    String msg = String.format("Property %s, specified in group %s was not found in the current object!", propertyName, owPropertyGroup.getGroupId());
                    LOG.debug(msg);
                }
            }
            if (properties.isEmpty())
            {
                // skip empty groups
                continue;
            }

            renderPropertyGroup(w_p, owPropertyGroup, index, properties);
            index = index + properties.size();
        }

    }

    /**
     * Render a group based on provided information
     * @param w_p Writer
     * @param group OwPropertyGroup
     * @param propertyIndex int index to be rendered
     * @param properties List of OwProperty objects
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void renderPropertyGroup(Writer w_p, OwPropertyGroup<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> group, int propertyIndex, List<OwProperty> properties) throws Exception
    {
        String encodedGroupId = OwHTMLHelper.encodeJavascriptVariableName(group.getGroupId());
        w_p.write("<tbody id=\"groupHeader");
        w_p.write(encodedGroupId);
        w_p.write("\">");
        renderGroupHeader(group, w_p, GROUP_HEADER_CLASS_NAME);
        w_p.write("</tbody>");

        w_p.write("<tbody id=\"");
        w_p.write(encodedGroupId);
        w_p.write("\" class=\"");
        if (group.isCollapsed())
        {
            w_p.write(GROUP_COLLAPSED_CLASS_NAME);
        }
        else
        {
            w_p.write(GROUP_EXPANDED_CLASS_NAME);
        }
        w_p.write("\">");

        for (OwProperty prop : properties)
        {
            OwPropertyClass propertyClass = prop.getPropertyClass();
            // === toggle even/odd flag
            String strRowClassName = getRowClassName(propertyIndex, prop);
            renderHorizontalProperty(w_p, strRowClassName, prop, propertyClass);
            propertyIndex++;
        }
        w_p.write("</tbody>");
    }

    /** render the main region with the property list
     * @param w_p Writer object to write HTML to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // reset field manager
        m_theFieldManager.reset();

        OwPropertyCollection props = getFilteredClonedProperties();
        // properties available
        if ((props == null) || (props.size() == 0))
        {
            return;
        }

        //        w_p.write(m_theFieldManager.renderErrors());

        if (hasViewMask(VIEW_MASK_RENDER_VERTICAL))
        {
            renderVertically(w_p);
        }
        else
        {
            // === render horizontal
            // === draw table with properties listed
            w_p.write("<table class=\"OwObjectPropertyView_PropList\">\n");

            // title
            w_p.write("<thead>\n");
            w_p.write("<tr class=\"OwObjectPropertyView_Header\">\n");

            // === overridable to render additional columns
            renderExtraColumnHeader(w_p);

            // not required column
            w_p.write("<th class=\"OwRequired\"><img  alt=\"\" src=\"");
            w_p.write(getContext().getDesignURL() + "/images/OwObjectPropertyView/notrequired.gif\"></th>");
            // property name, value and error column
            w_p.write("<th class=\"OwPropertyName\">");
            w_p.write(getContext().localize("app.OwObjectPropertyView.propertycolumn", "Property"));
            w_p.write("&nbsp;&nbsp;</th><th>");
            w_p.write(getContext().localize("app.OwObjectPropertyView.valuecolumn", "Value"));
            w_p.write("&nbsp;&nbsp;</th>\n");
            w_p.write("</tr>\n");
            w_p.write("</thead>\n");
            if (getPropertyListConfiguration() != null)
            {
                getContext().renderJSInclude("/js/oweditproperties.js", w_p);

                renderPropertyListConfiguration(w_p);
            }
            else
            {
                w_p.write("<tbody>\n");
                renderHorizontalProperties(w_p);
                w_p.write("</tbody>\n");
            }

            w_p.write("</table>\n");
        }

        // determine read only flag according to lock status, role and object capability
        if (m_fSystem || hasViewMask(VIEW_MASK_READONLY) || m_fAllPropertiesReadonly)
        {
            m_fReadOnly = true;
        }
        else
        {
            m_fReadOnly = (getObjectRef().canLock() && (!getObjectRef().getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))) || (!getObjectRef().canSetProperties(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        }

        if (getMenu() != null)
        {
            // disable apply button if not editable
            getMenu().enable(m_iAppyBtnIndex, !m_fReadOnly);
        }
    }

    /** render the modes region with the set properties modes
     * @param w_p Writer object to write HTML to
     */
    protected void renderModesRegion(Writer w_p) throws Exception
    {
        if (m_modetype == OwObjectClass.OPERATION_TYPE_UNDEF || m_fReadOnly)
        {
            return;
        }
        List propmodes = getObjectRef().getObjectClass().getModes(m_modetype);

        if (null != propmodes)
        {
            List<OwComboItem> items = new LinkedList<OwComboItem>();
            for (int i = 0; i < propmodes.size(); i++)
            {
                OwEnum mode = (OwEnum) propmodes.get(i);
                OwComboItem item = new OwDefaultComboItem(String.valueOf(i), mode.getDisplayName(getContext().getLocale()));
                items.add(item);
            }
            OwComboModel model = new OwDefaultComboModel(false, false, "" + m_icurrentmode, items);
            OwString description = new OwString("app.OwObjectPropertyView.modes.title", "Modes");
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, MODES_SELECT_ID, null, null, description);
            String modesDisplayName = description.getString(getContext().getLocale());
            OwInsertLabelHelper.insertLabelValue(w_p, modesDisplayName, MODES_SELECT_ID);
            renderer.renderCombo(w_p);
        }
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        // render internal regions
        switch (iRegion_p)
        {
            case ERRORS_REGION:
                renderErrorsRegion(w_p);
                break;
            case MODES_REGION:
                renderModesRegion(w_p);
                break;

            case MAIN_REGION:
                renderMainRegion(w_p);
                break;

            default:
                // render attached views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** safe current properties for next object
     * @throws Exception */
    private void safeBatchIndexProperties() throws Exception
    {
        m_batchPropertiesHandler.saveBatchIndexProperties();
    }

    /** causes all attached views to receive an onUpdate event
     *
     *  @param target_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     */
    private void safeUpdate(OwEventTarget target_p, int iCode_p) throws Exception
    {
        OwDocument doc = getDocument();

        if (null != doc)
        {
            doc.update(target_p, iCode_p, null);
        }
    }

    /** save the properties to the object
     *
     * @param changedProperties_p OwPropertyCollection see update
     * @throws Exception
     *
     * @return true = fields have been saved, false = nothing has been saved
     */
    protected boolean save(OwPropertyCollection changedProperties_p) throws Exception
    {
        if ((changedProperties_p == null) || (changedProperties_p.size() == 0))
        {
            return m_updateNoChanges;
        }

        Object mode = null;

        // get selected mode for setproperties
        if (NO_MODE != m_icurrentmode)
        {
            List propmodes = getObjectRef().getObjectClass().getModes(m_modetype);
            OwEnum modeenum = (OwEnum) propmodes.get(m_icurrentmode);
            mode = modeenum.getValue();
        }

        try
        {
            // === save only changed properties
            getObjectRef().setProperties(changedProperties_p, mode);
        }
        catch (Exception e)
        {
            String msg = "Error saving the properties to the object.";
            LOG.error(msg, e);
            throw e;
        }
        finally
        {
            // get a copy of all properties from the object after setProperties
            // !!! otherwise we would have inconsistent properties
            m_Properties = null;
        }

        return true;
    }

    /**
     * setter method for m_batchPropertySet
     * @param set_p The propMap to set.
     */
    public void setBatchProperties(Collection set_p)
    {
        m_batchIndexProperties = set_p;
        m_batchPropertiesHandler.setBatchIndexProperties(set_p);
    }

    /**
     * Set the configuration if a message should be displayed 
     * when nothing was changed.
     * @param display_p boolean flag
     * @since 3.1.0.3
     */
    protected void setDisplayNoSaveMsg(boolean display_p)
    {
        this.m_displayNoSaveMsg = display_p;
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
        OwField field = (OwField) getFilteredClonedProperties().get(sName_p);

        if (null == field)
        {
            String msg = "OwObjectPropertyView.setField: Field not found, name = " + sName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        field.setValue(value_p);
    }

    /**
     * Set the configuration for grouped properties.
     * @param groupPropertyConfiguration_p - the configuration object.
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 will be removed in next version use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    @Deprecated
    public void setGroupPropertiesConfiguration(com.wewebu.ow.server.dmsdialogs.OwGroupPropertiesConfiguration groupPropertyConfiguration_p)
    {
        LOG.warn("OwGroupPropertiesConfiguration is deprected and should not be used, use EditPropertyList instead");
        OwPropertyListConfiguration propLstConf = null;
        if (groupPropertyConfiguration_p != null)
        {
            propLstConf = new OwPropertyListConfiguration();
            for (OwPropertyGroup group : groupPropertyConfiguration_p.getGroups())
            {
                OwSimplePropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> region = new OwSimplePropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo>(group);
                propLstConf.addRegion(region);
            }
        }
        setPropertyListConfiguration(propLstConf);
    }

    /** set mode type to use for getModes method
     *
     * @see OwObjectClass#getModes(int)
     *
     * @param modetype_p int as defined in OwObjectClass.OPERATION_TYPE_...
     */
    public void setModeType(int modetype_p)
    {
        m_modetype = modetype_p;
    }

    /** set the view that is next to this view, displays a next button to activate
     *
     * @param nextView_p OwView
     *
     */
    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        if (getMenu() != null)
        {
            // add next button
            m_iNextButtonIndex = getMenu().addFormMenuItem(this, getContext().localize("app.OwObjectPropertyView.next", "Next"), null, "Next", nextView_p, null);

            getMenu().enable(m_iNextButtonIndex, true);
        }
    }

    /**
     * @param objectRef_p The objectRef to set.
     * @param showSystemProperties_p if true show only system properties.
     */
    public void setObjectRef(OwObject objectRef_p, boolean showSystemProperties_p) throws Exception
    {
        // reset field manager
        m_theFieldManager.reset();

        // set new object reference
        m_ObjectRef = objectRef_p;

        m_theFieldManager.setFieldProvider(this);

        m_fSystem = showSystemProperties_p;

        // check if object supports lock
        if ((getMenu() != null) && (isSupportLock() && m_ObjectRef.canLock()))
        {
            // create lock object button
            m_iLockIndex = getMenu().addMenuItem(this, getContext().localize("app.OwObjectPropertyView.lock", "Lock document"), "Lock", null);
            // create unlock object button
            m_iUnLockIndex = getMenu().addMenuItem(this, getContext().localize("app.OwObjectPropertyView.unlcok", "Unlock document"), "UnLock", null);
        }

        // get a copy of all properties from the object
        m_Properties = null;

        // update right away
        onUpdate(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY, null);
    }

    /**
     * @param objectRef_p The objectRef to set.
     * @param showSystemProperties_p if true show only system properties.
     * @param propertyInfos_p collection of property info objects describing the properties to edit and their readonly state, or null to edit all
     * @deprecated since 4.2.0.0 use {@link #setObjectRef(OwObject, boolean)} instead, and for filtering/grouping {@link #setPropertyListConfiguration(OwPropertyListConfiguration)}
     */
    @Deprecated
    public void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, Collection propertyInfos_p) throws Exception
    {
        OwPropertyListConfiguration propLstConf = null;
        if (null != propertyInfos_p)
        {
            propLstConf = new OwPropertyListConfiguration();
            Iterator it = propertyInfos_p.iterator();
            LinkedList<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> propInfoLst = new LinkedList<org.alfresco.wd.ui.conf.prop.OwPropertyInfo>();
            while (it.hasNext())
            {
                org.alfresco.wd.ui.conf.prop.OwPropertyInfo pi = (org.alfresco.wd.ui.conf.prop.OwPropertyInfo) it.next();
                propInfoLst.add(pi);
            }
            propLstConf.addRegion(new OwSimplePropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo>(propInfoLst));
        }
        setPropertyListConfiguration(propLstConf);

        // reset field manager
        m_theFieldManager.reset();

        // set new object reference
        m_ObjectRef = objectRef_p;

        m_theFieldManager.setFieldProvider(this);

        m_fSystem = showSystemProperties_p;

        // check if object supports lock
        if ((getMenu() != null) && (isSupportLock() && m_ObjectRef.canLock()))
        {
            // create lock object button
            m_iLockIndex = getMenu().addMenuItem(this, getContext().localize("app.OwObjectPropertyView.lock", "Lock document"), "Lock", null);
            // create unlock object button
            m_iUnLockIndex = getMenu().addMenuItem(this, getContext().localize("app.OwObjectPropertyView.unlcok", "Unlock document"), "UnLock", null);
        }

        // update right away
        onUpdate(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY, null);
    }

    /** set the view that is prev to this view, displays a prev button to activate
      *
      * @param prevView_p OwView
      *
      */
    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        // ignore
    }

    /** set context to be used for the readonly property
     *
     * @param context_p int as defined with OwPropertyClass.CONTEXT_...
     */
    public void setReadOnlyContext(int context_p)
    {
        m_ReadOnlyContext = context_p;
    }

    /**
     * Getter for currently defined ReadOnly Context 
     * @return integer which is representing OwPropertyClass.CONTEXT_...
     * @since 4.2.0.0
     */
    public int getReadOnlyContext()
    {
        return this.m_ReadOnlyContext;
    }

    /**
     * {@link #m_saveAllActive} setter
     * @param active_p <code>true</code> if the <b>Save All</b> should be active.
     */
    public void setSaveAllActive(boolean active_p)
    {
        this.m_saveAllActive = active_p;
    }

    public void setSaveBtnIndex(int mIAppyBtnIndex_p)
    {
        m_iAppyBtnIndex = mIAppyBtnIndex_p;
    }

    /** set the view properties
     * @param iViewMask_p int a combination of VIEW_PROPERTY_... defined flags
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** update view from request
     *  to save the changes call save
     *
     * @return OwPropertyCollection or null if some fields are invalid
     * @throws Exception
     */
    protected OwPropertyCollection update(HttpServletRequest request_p) throws Exception
    {
        // update modes
        if (m_modetype != OwObjectClass.OPERATION_TYPE_UNDEF)
        {
            List propmodes = getObjectRef().getObjectClass().getModes(m_modetype);

            if (null != propmodes)
            {
                m_icurrentmode = Integer.parseInt(request_p.getParameter(MODES_SELECT_ID));
            }
        }

        // update properties list from form data
        OwPropertyCollection changedProperties = new OwStandardPropertyCollection();
        if (m_theFieldManager.update(request_p, changedProperties, getObjectRef().getProperties(null)))
        {
            // safe for batch indexing
            safeBatchIndexProperties();

            return changedProperties;
        }
        else
        {
            return null;
        }
    }

    /** update the target after a form event, so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        OwPropertyCollection changedProperties = new OwStandardPropertyCollection();
        if (getObjectRef() == null)
        {
            LOG.debug("OwObjectPropertyView.updateExternalFormTarget: The refer object (property, objectclass...) is missing. Please select one.");
            throw new OwInvalidOperationException(getContext().localize("app.OwObjectPropertyView.invalid.objectreferer", "The refer object (property, objectclass...) is missing. Please select one."));
        }
        else
        {
            OwPropertyCollection properties = getObjectRef().getProperties(null);
            return m_theFieldManager.update(request_p, changedProperties, properties);
        }
    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /**
     * Setter for OwPropertyListConfiguration, setting null will show all properties based on {@link #setReadOnlyContext(int)}.<br />
     * Can throw OwViewUpdateException if {@link #onUpdate(OwEventTarget, int, Object)} call fails
     * @param propListConfiguration OwPropertyListConfiguration (can be null)
     * @since 4.2.0.0
     * @see #getPropertyListConfiguration()
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propListConfiguration)
    {
        this.propertyListConfiguration = propListConfiguration;
        if (this.m_propertyfilter != null)
        {
            this.m_propertyfilter.clear();
            this.m_propertyfilter = null;
        }
        if (this.m_readOnlyProperties != null)
        {
            this.m_readOnlyProperties.clear();
            this.m_readOnlyProperties = null;
        }

        if (getContext() != null)
        {//if initialized fire update, otherwise exception is provoked
            try
            {
                onUpdate(this, OwUpdateCodes.FILTER_CHANGED, null);
            }
            catch (Exception e)
            {
                throw new OwViewUpdateException(getContext().localize("app.OwObjectPropertyView.err.setPropertyListConfiguration", "Failed to update view after Filter/Grouping definition."), e);
            }
        }
    }

    /**
     * Getter of OwPropertyListConfiguration which can return null, in such case all properties will be shown
     * matching based on defined {@link #setReadOnlyContext(int)} state.
     * @return OwPropertyListConfiguration or null.
     * @since 4.2.0.0
     * @see #setPropertyListConfiguration(OwPropertyListConfiguration)
     * @see #setReadOnlyContext(int)
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }

    /**
     * Called if properties should be rendered vertical, based on defined ViewMask
     * @param w_p Writer
     * @throws Exception
     * @see #hasViewMask(int)
     */
    protected void renderVertically(Writer w_p) throws Exception
    {/*extracted from renderMainRegion since 4.2.0.0*/
        // === render a vertical
        Iterator it = getFilteredClonedProperties().values().iterator();
        while (it.hasNext())
        {
            OwProperty property = (OwProperty) it.next();
            OwPropertyClass propertyClass = property.getPropertyClass();

            // === filter for system / non system
            if (m_fSystem != propertyClass.isSystemProperty())
            {
                continue;
            }

            w_p.write("<div class=\"OwPropertyName\" style=\"clear: both;\">");

            m_theFieldManager.insertLabel(w_p, m_fReadOnly, this.isPropertyReadonly(property), property, ":&nbsp;&nbsp;", true);

            w_p.write("</div>\n");

            w_p.write("<div>\n");

            w_p.write("<div class=\"OwPropertyControl\">");

            if (m_fReadOnly || this.isPropertyReadonly(property))
            {
                // property can not be edited
                m_theFieldManager.insertReadOnlyField(w_p, property);
            }
            else
            {
                // property can be edited
                m_theFieldManager.insertEditField(w_p, property);
                //at least one property is not read only
                m_fAllPropertiesReadonly = false;
            }

            w_p.write("</div>\n");

            // === display error column for this property
            w_p.write("<div class=\"OwPropertyError\">");
            w_p.write(m_theFieldManager.getSafeFieldError(property));
            w_p.write("</div>\n");

            // === display HotInfo target for JS validation errors
            w_p.write("<div class=\"OwPropertyError\" id=\"HotInfo" + m_theFieldManager.getFieldJSErrorCtrlID(property) + "\"></div>\n");

            w_p.write("<br><br>\n");
            w_p.write("</div>\n");

        }
    }

    /**
     * Will render based on defined OwPropertListConfiguration. 
     * @param writer Writer
     * @throws Exception 
     * @since 4.2.0.0
     * @see #getPropertyListConfiguration()
     */
    protected void renderPropertyListConfiguration(Writer writer) throws Exception
    {
        int idx = 0;
        for (OwPropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> subregion : getPropertyListConfiguration().getSubregions())
        {
            List<OwProperty> availableProperties = getSubregionAvailable(subregion);
            if (!availableProperties.isEmpty())
            {
                if (subregion.isGroup())
                {
                    renderPropertyGroup(writer, subregion.getPropertyGroup(), idx, availableProperties);
                }
                else
                {
                    writer.write("<tbody>\n");
                    for (OwProperty prop : availableProperties)
                    {
                        String rowClass = getRowClassName(idx++, prop);
                        renderHorizontalProperty(writer, rowClass, prop, prop.getPropertyClass());
                    }
                    writer.write("</tbody>\n");
                }
                idx += availableProperties.size();
            }
        }
    }

    /**
     * Get the Properties available to be rendered for current object reference.
     * @param subregion OwPropertySubregion
     * @return List of OwProperty objects to render, or empty list if none matching found
     * @throws Exception in case current {@link #getObjectRef()} properties are not accessible
     * @since 4.2.0.0
     */
    protected List<OwProperty> getSubregionAvailable(OwPropertySubregion<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> subregion) throws Exception
    {
        LinkedList<OwProperty> lst = new LinkedList<OwProperty>();
        OwPropertyCollection col = getFilteredClonedProperties();

        for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo propInfo : subregion.getPropertyInfos())
        {
            OwProperty prop = (OwProperty) col.get(propInfo.getPropertyName());
            if (prop != null)
            {
                lst.add(prop);
            }
        }

        return lst;
    }

    /**
     *<p>
     * Combination of property name and read only.
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
     *@deprecated since 4.2.0.0 use org.alfresco.wd.ui.conf.prop.OwPropertyInfo instead
     */
    @Deprecated
    public static class OwPropertyInfo extends org.alfresco.wd.ui.conf.prop.OwPropertyInfo
    {
        public OwPropertyInfo(String propertyName_p, boolean readOnly_p)
        {
            super(propertyName_p, readOnly_p);
        }
    }
}