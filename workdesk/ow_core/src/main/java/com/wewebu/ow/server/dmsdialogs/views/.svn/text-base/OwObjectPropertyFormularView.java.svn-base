package com.wewebu.ow.server.dmsdialogs.views;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwJspConfigurable;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Form View for Object Properties. Can take JSP or HTML based forms.
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
public class OwObjectPropertyFormularView extends OwView implements OwFieldProvider, OwJspConfigurable
{
    private static final Logger LOG = OwLogCore.getLogger(OwObjectPropertyFormularView.class);

    /** name of the menu region */
    private static final String PLACEHOLDER_MENU = "ow_menu";

    /** batch index properties helper*/
    private OwBatchPropertiesHandler m_batchPropertiesHandler;

    /** changed properties, either through fieldmanager or external OwFormular interface. */
    private OwPropertyCollection m_changedProperties = new OwStandardPropertyCollection();

    /** Flag indication if user should be notified if nothing was saved
     * @since 3.1.0.3*/
    private boolean m_displayNoSaveMsg;

    /** Flag to enable save all button option when more than one documents are selected.<br>
     * The save all button will be rendered if this {@link #m_saveAllActive} is set to <code>true</code>,
     * the {@link #m_enablesaveall} is set to <code>true</code> and <code>OwObjectPropertyFormView.isReadOnly</code>
     * flag is <code>false</code>.
     * @since 3.1.0.0
     * */
    protected boolean m_enablesaveall;

    /** flag if all properties are read only type
     * @since 3.1.0.0
     **/
    private boolean m_fAllPropertiesReadonly = true;

    /** index of the save button */
    protected int m_iSaveButton;

    /**flag indicated that paste metadata feature should be enabled*/
    private boolean m_isPasteMetadataEnabled;

    private OwJspFormConfigurator m_jspConfigurator;

    /** the menu for the step processor */
    private OwSubMenuView m_menu;

    /** the BPM work item */
    private OwObject m_objectref;

    /**paste metadata handler*/
    protected OwPasteMetadataHandler m_pasteMetadataHandler;

    /** the properties from the work item */
    protected OwPropertyCollection m_properties;

    /** context to be used for the read-only property */
    private int m_ReadOnlyContext = OwPropertyClass.CONTEXT_NORMAL;
    /** read only dialog */
    private boolean m_readOnlyView;
    /**flag specify if save all feature is active
     * @since 3.1.0.0
     * */
    protected boolean m_saveAllActive;
    /** save all button index*/
    protected int m_saveAllButtonIndex = -1;

    /** the HTML content to use as a form */
    private String m_strHtmlContent;
    /** path to the JSP page to use as a form */
    private String m_strJspPage;

    /** instance of the property field class */
    protected OwFieldManager m_theFieldManager;

    /**
     * Add Save All button
     * @param saveAllText_p - the text to be displayed in the button
     * @throws Exception
     */
    protected void addSaveAllButton(String saveAllText_p) throws Exception
    {
        if (getMenu() != null)
        {
            // apply button
            if (m_enablesaveall)
            {
                m_saveAllButtonIndex = getMenu().addFormMenuItem(this, saveAllText_p, "SaveAll", null);
            }
        }
    }

    /**
     * Flag indicate if all properties are read only
     * @return boolean - true if all properties are read only
     * @since 3.1.0.0
     */
    private boolean areAllPropertiesReadOnly()
    {
        Iterator<OwProperty> it = m_properties.values().iterator();
        while (it.hasNext())
        {
            OwProperty property = it.next();
            try
            {

                OwPropertyClass propertyClass = property.getPropertyClass();
                boolean readOnly = property.isReadOnly(getReadOnlyContext());

                if (!propertyClass.isSystemProperty() && propertyClass.isHidden(getReadOnlyContext()))
                {
                    continue;
                }

                if (!readOnly)
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
     * Establish the enabled state for saveAll button
     * @since 3.1.0.0
     */
    protected void changeSaveAllButtonState()
    {
        if (getMenu() != null)
        {
            // disable apply button if not editable
            if (m_enablesaveall)
            {
                boolean visualyEnabled = !isReadOnly() && m_saveAllActive;
                getMenu().enable(m_saveAllButtonIndex, visualyEnabled);
            }
        }
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
        return new OwStandardPasteMetadataHandler((OwMainAppContext) getContext(), false, m_isPasteMetadataEnabled);
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        m_theFieldManager.detach();
    }

    /**
     * Display "no save" message.
     * By default this flag is set to false.
     * @return boolean flag
     * @since 3.1.0.3
     * @see #setDisplayNoSaveMsg(boolean)
     */
    public boolean displayNoSaveMsg()
    {
        return m_displayNoSaveMsg;
    }

    /**
     * Filter properties based on provided collection.
     * @param propertyInfos_p - can be null - no filter is applied.
     */
    @SuppressWarnings("unchecked")
    public void filterProperties(Collection<OwPropertyInfo> propertyInfos_p) throws Exception
    {
        if (propertyInfos_p != null && m_properties != null)
        {
            OwPropertyCollection filtered = new OwStandardPropertyCollection();
            for (OwPropertyInfo owPropertyInfo : propertyInfos_p)
            {
                String propName = owPropertyInfo.getPropertyName();
                if (propName != null)
                {
                    OwProperty prop = (OwProperty) m_properties.get(propName);
                    if (prop != null && !prop.isHidden(m_ReadOnlyContext))
                    {
                        filtered.put(propName, prop);
                    }
                }
            }
            m_properties = filtered;
        }
    }

    /** implementation for the OwFieldProvider interface
     */
    public OwField getField(String strFieldClassName_p) throws Exception
    {
        OwProperty prop = (OwProperty) m_properties.get(strFieldClassName_p);

        if (prop == null)
        {
            String msg = "OwObjectPropertyFormularView.getField: Property not found, strFieldClassName_p = " + strFieldClassName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return prop;
    }

    /** get a reference to the internal field manager */
    public OwFieldManager getFieldManager()
    {
        return m_theFieldManager;
    }

    /** get a name that identifies the field provider
     *
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        try
        {
            return m_strJspPage.toString();
        }
        catch (NullPointerException e)
        {
            return "htmlform";
        }
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return m_objectref;
    }

    /** implementation for the OwFieldProvider interface
     * get the type of field provider as defined with TYPE_...
     */
    public int getFieldProviderType()
    {
        switch (m_ReadOnlyContext)
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
        return m_properties.values();
    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return this.m_jspConfigurator;
    }

    /** get the menu
     */
    public OwMenuView getMenu()
    {
        return m_menu;
    }

    /** get the current object to use for property rendering
     * @return OwObject*/
    public OwObject getObjectRef()
    {
        return m_objectref;
    }

    /** get context to be used for the read-only property
     *
     * @return context_p int as defined with OwPropertyClass.CONTEXT_...
     */
    protected int getReadOnlyContext()
    {
        return m_ReadOnlyContext;
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
            OwField field = (OwField) m_properties.get(sName_p);
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get the index of the save button
     */
    protected int getSaveBtnIndex()
    {
        return m_iSaveButton;
    }

    /** get the status after the last update
     *
     * @return boolean true = update was OK, all fields are valid, false = update caused errors, one or more fields are invalid
     */
    public boolean getUpdateStatus()
    {
        return m_theFieldManager.getUpdateStatus();
    }

    /**
     * Post a message for informing user about a successfully save operation.
     * @since 3.1.0.0
     */
    protected void informUserOnSuccess()
    {
        // === info for user
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyFormularView.saved", "Changes have been saved."));
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === init field manager
        m_theFieldManager = ((OwMainAppContext) getContext()).createFieldManager();
        m_theFieldManager.setFieldProvider(this);
        m_theFieldManager.setExternalFormTarget(getFormTarget());

        // === init menu
        m_menu = new OwSubMenuView();
        addView(m_menu, null);

        // Save button
        m_iSaveButton = m_menu.addFormMenuItem(this, getContext().localize("owcore.OwObjectPropertyFormularView.savebtn", "Save"), "Save", null);

        m_menu.setDefaultMenuItem(m_iSaveButton);

        // initially disable save button
        getMenu().enable(m_iSaveButton, false);

        m_batchPropertiesHandler = createBatchPropertiesHandler();
        m_pasteMetadataHandler = createPasteMetadataHandler();
        setDisplayNoSaveMsg(false);
    }

    /** determine if region exists
     * @param strRegion_p name of the region to render
     *  of the following form:
     *
     *      [ow_err_ | ow_ro_]<PropertyNameIdentifier>[.<SubPropertyNameIdentifier>]
     *
     *      e.g.:
     *              Customer                : renders the property "Customer"
     *              ow_err_Customer         : renders the error string for the property "Customer"
     *              ow_ro_Custpmer          : renders the property "Customer" as read-only
     *              Customer.Address         : renders the property "Address" of the property "Customer", if Customer is of type OwObject
     *
     * @return true if region contains anything and should be rendered
     */
    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        if (super.isNamedRegion(strRegion_p))
        {
            return true;
        }

        // === check for menu
        if (strRegion_p.equals(PLACEHOLDER_MENU))
        {
            return true;
        }

        if (m_properties == null)
        {
            return false;
        }

        // scan the placeholder
        OwPropertyPlaceholder scanedPlaceholder = new OwPropertyPlaceholder(strRegion_p);
        return m_properties.containsKey(scanedPlaceholder.getIdentifier());
    }

    /**
     * Get the status of "paste metadata" feature.
     * @return -<code>true</code> if the JSP should render the Paste All and Paste buttons.
     * @since 3.1.0.0.
     */
    public boolean isPasteMetadataActivated()
    {
        return m_pasteMetadataHandler.isPasteMetadataActivated();
    }

    public boolean isReadOnly()
    {
        //if all properties are read only, set read only view
        if (areAllPropertiesReadOnly())
        {
            m_readOnlyView = true;
        }

        return m_readOnlyView;
    }

    private boolean isReadOnlyRendered(OwPropertyPlaceholder scanedPlaceholder_p, OwProperty property_p) throws Exception
    {
        boolean canSetProperties = m_objectref.canSetProperties(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        return scanedPlaceholder_p.isReadOnly() || (!canSetProperties) || property_p.isReadOnly(getReadOnlyContext());
    }

    /**
     * Always returns <code>false</code>.
     * @return <code>false</code>
     * @since 3.1.0.0
     */
    public boolean isSystemPropertyView()
    {
        return false;
    }

    /**
     * Update the specified properties (from owplugins->EditBatchPropertyList) with the
     * values from previous view.
     * @throws Exception
     */
    public void onBatchIndex() throws Exception
    {
        m_batchPropertiesHandler.onBatchIndex();
    }

    /**
     * called when user presses the paste all metadata button
     * @since 3.1.0.0
     */
    public void onPasteAllMetaData(HttpServletRequest request_p) throws Exception
    {
        m_pasteMetadataHandler.onPasteAllMetaData(request_p);
    }

    /**
     * called when user presses the paste this metadata button
     * @since 3.1.0.0
     */
    public void onPasteThisMetaData(HttpServletRequest request_p) throws Exception
    {
        m_pasteMetadataHandler.onPasteThisMetaData(request_p);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     * @throws Exception
     * @throws IOException
     */
    protected void onRender(Writer w_p) throws IOException, Exception
    {
        m_theFieldManager.reset();
        if (m_strJspPage == null)
        {
            m_strJspPage = m_jspConfigurator.getJspForm(getObjectRef().getObjectClass().getClassName());
        }
        w_p.write(m_theFieldManager.renderErrors());
        if (null != m_strHtmlContent)
        {
            renderHTMLFormular(w_p, m_strHtmlContent);
        }
        else if (null != m_strJspPage)
        {
            serverSideDesignInclude(m_strJspPage, w_p);
        }
        else
        {
            StringBuilder message = new StringBuilder();
            message.append("Cannot find a default JSP page (JspForm) for rendering. Please define a JSP page (JspForm) for the objectclass:  ");
            message.append(getObjectRef().getObjectClass().getClassName());
            w_p.write(message.toString());
            LOG.warn("Default <JspForm> tag not configured properly in owplugins.xml");
            throw new OwConfigurationException(message.toString());
        }

    }

    /** called when the user wants to save the modified properties
     *
     *  call getUpdateStatus() after save to check if fields are invalid
     *
     *  @return true = fields have been saved, false = nothing has been saved
     */
    public boolean onSave(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        if (onSaveInternal(request_p, oReason_p))
        {
            if (null != getDocument())
            {
                // inform other views, so they can display correctly
                getDocument().update(this, OwUpdateCodes.UPDATE_OBJECT_PROPERTY, null);
                getDocument().update(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY, null);
            }
            informUserOnSuccess();
            return true;
        }
        else
        {
            if (m_displayNoSaveMsg)
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.NotSaved", "Nothing was changed therefore the document was not saved.");
                ((OwMainAppContext) getContext()).postMessage(message);
            }
            //inform other views
            getDocument().update(this, OwUpdateCodes.OBJECT_PROPERTIES_NOT_CHANGED, null);
            // invalid fields, document not saved, see bug 4828
            return false;
        }
    }

    /** called to save the modified properties
     *
     *  call getUpdateStatus() after save to check if fields are invalid
     *
     *  @return true = fields have been saved, false = nothing has been saved
     */
    protected boolean onSaveInternal(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwPropertyCollection changedProperties = update(request_p);
        return save(changedProperties);
    }

    /** render the menu region */
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        // === render menu region
        m_menu.render(w_p);
    }

    /**
     * Checks if a field is mandatory or not.
     * 
     * @param strPlaceHolder_p The name of the field (usually this is the same with the name of the property). It has to be a fully qualified name (include the name of the class too);
     * @return true if this field is mandatory
     * @see #renderNamedRegion(Writer, String)
     * @since 4.2.0.0
     */
    public boolean isFieldMandatory(String strPlaceHolder_p) throws Exception
    {
        OwPropertyPlaceholder scanedPlaceholder = new OwPropertyPlaceholder(strPlaceHolder_p);
        OwProperty property = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());
        if (null != property)
        {
            return property.getPropertyClass().isRequired();
        }

        return false;
    }

    /** Called when the form parser finds a placeholder in the HTML / JSP input
      *
      * @param strPlaceHolder_p the placeholder string found
      *  of the following form:
      *
      *      [ow_err_ | ow_ro_]<PropertyNameIdentifier>[.<SubPropertyNameIdentifier>]
      *
      *      e.g.:
      *              Customer                : renders the property "Customer"
      *              ow_err_Customer         : renders the error string for the property "Customer"
      *              ow_ro_Customer          : renders the property "Customer" as read-only
      *              Customer.Address         : renders the property "Address" of the property "Customer", if Customer is of type OwObject
      *
      * @param w_p Writer object, write to replace placeholder
      */
    public void renderNamedRegion(Writer w_p, String strPlaceHolder_p) throws Exception
    {
        // === render a placeholder found in the form
        if (strPlaceHolder_p.equals(PLACEHOLDER_MENU))
        {
            // === render menu region
            renderMenuRegion(w_p);
            return;
        }

        if (m_properties == null)
        {
            return;
        }

        // scan the placeholder
        OwPropertyPlaceholder scanedPlaceholder = new OwPropertyPlaceholder(strPlaceHolder_p);

        if (scanedPlaceholder.isError())
        {
            // === placeholder for the error string
            // look up property by unique name
            OwProperty property = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());
            if (property != null)
            {
                // === property can not be edited
                w_p.write(m_theFieldManager.getSafeFieldError(property));
            }
            else
            {
                w_p.write("Property not found: " + scanedPlaceholder.getIdentifier());
            }
        }
        else if (scanedPlaceholder.isValidation())
        {
            // === placeholder for the validation target
            // look up property by unique name
            OwProperty property = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());
            if (property != null)
            {
                // insert empty HotInfo target
                w_p.write("<span class='OwPropertyError' id='HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(property) + "'></span>");
            }
            else
            {
                w_p.write("Property not found: " + scanedPlaceholder.getIdentifier());
            }
        }
        else
        {
            // === placeholder for the property
            // look up property by unique name
            OwProperty property = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());

            if (property != null)
            {
                if (scanedPlaceholder.hasSubIdentifier())
                {
                    // === get a sub property of a object type property
                    OwObject obj = (OwObject) property.getValue();
                    if (null != obj)
                    {
                        OwProperty subProperty = obj.getProperty(scanedPlaceholder.getSubIdentifier());
                        if (subProperty == null)
                        {
                            w_p.write("Property not found: " + scanedPlaceholder.getIdentifier() + "." + scanedPlaceholder.getSubIdentifier());
                        }
                        else
                        {
                            // === sub property can not be edited, so inserted as read-only always.
                            m_theFieldManager.insertReadOnlyField(w_p, subProperty);
                        }
                    }
                }
                else
                {
                    // === normal property
                    renderProperty(w_p, property, isReadOnlyRendered(scanedPlaceholder, property));
                }
            }
            else
            {
                w_p.write("Property not found: " + scanedPlaceholder.getIdentifier());
            }
        }
        if (getMenu() != null)
        {
            // disable apply button if not editable
            getMenu().enable(m_iSaveButton, !isReadOnly());
        }

    }

    /**
     * Utility method to render "paste all" symbol.
     * @param w_p - the {@link Writer} object
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderPasteAll(Writer w_p) throws Exception
    {
        m_pasteMetadataHandler.renderPasteAllMetadata(w_p, this);
    }

    /**
     * Utility method to render "paste" symbol.
     * @param w_p - {@link Writer} object
     * @param propName_p - the name of the property
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderPasteProperty(Writer w_p, String propName_p) throws Exception
    {
        // scan the placeholder
        OwPropertyPlaceholder scanedPlaceholder = new OwPropertyPlaceholder(propName_p);
        if (m_properties != null)
        {
            OwProperty prop = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());
            if (prop != null)
            {
                m_pasteMetadataHandler.renderPasteMetadata(w_p, prop, this);
            }
        }
    }

    /** overridable to render a property */
    protected void renderProperty(Writer w_p, OwProperty prop_p, boolean fReadOnly_p) throws Exception
    {
        if (fReadOnly_p)
        {
            m_theFieldManager.insertReadOnlyField(w_p, prop_p);
        }
        else
        {
            m_theFieldManager.insertEditField(w_p, prop_p);
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
            return false;
        }

        try
        {
            // === save only changed properties
            m_objectref.setProperties(changedProperties_p);

            // reset changed property map
            changedProperties_p.clear();
        }
        catch (Exception e)
        {
            String msg = "Error saving the properties to the object.";
            LOG.warn(msg, e);
            throw e;
        }
        finally
        {
            // get a copy of all properties from the object after setProperties
            // !!! otherwise we would have inconsistent properties
            m_properties = m_objectref.getClonedProperties(null);
            m_batchPropertiesHandler.setProperties(m_properties);
            m_pasteMetadataHandler.setProperties(m_properties);
        }
        return true;
    }

    /**
     * Set the collection of property names for batch feature.
     * @param batchIndexProperties_p
     * @since 3.0.0.0
     */
    public void setBatchProperties(Collection batchIndexProperties_p)
    {
        m_batchPropertiesHandler.setBatchIndexProperties(batchIndexProperties_p);
    }

    /**
     * Set the flag if "no save" message(s) should be displayed or not.
     * @param display_p boolean flag
     * @since 3.1.0.3
     */
    public void setDisplayNoSaveMsg(boolean display_p)
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
        OwField field = (OwField) m_properties.get(sName_p);

        if (null == field)
        {
            String msg = "OwObjectPropertyFormularView.setField: Field not found, name = " + sName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        field.setValue(value_p);

        m_changedProperties.put(field.getFieldDefinition().getClassName(), field);
    }

    /** set the JSP page to use as a form
     */
    public void setHTMLFormular(String strHTMLPage_p)
    {
        m_strHtmlContent = strHTMLPage_p;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        this.m_jspConfigurator = jspFormConfigurator_p;
    }

    /** set the optional JSP form to use in the render method
     *
     *  <br><br>In the JSP Form you can use the following statements to display and manipulate Properties<br><br>
     *
     *  // get a reference to the calling view<br>
     *  OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);<br><br>
     *
     *  <% m_View.renderNamedRegion(out,<FieldName>); %>                : renders the Property FieldName<br>
     *  <% m_View.renderNamedRegion(out,ow_err_<FieldName>); %>         : renders any validation error messages for the Property FieldName<br>
     *  <% m_View.renderNamedRegion(out,ow_ro_<FieldName>); %>          : renders the Property FieldName always read-only<br>
     *  <% m_View.renderNamedRegion(out,ow_menu); %>                    : renders a function menu to save changes<br>
     *
     * <br><br>You can also cast the m_View to OwFieldProvider to retrieve a OwField instance and manipulate or program a field directly.
     *
     * @param strJspPage_p
     * @throws Exception
     */
    public void setJspFormular(String strJspPage_p) throws Exception
    {
        m_strJspPage = strJspPage_p;
        if (getJspConfigurator() == null)
        {
            setJspConfigurator(new OwJspFormConfigurator(m_strJspPage));
        }
    }

    /**
     * @param objectRef_p The objectRef to set.
     */
    public void setObjectRef(OwObject objectRef_p) throws Exception
    {
        m_objectref = objectRef_p;
        if (getJspConfigurator() != null)
        {
            m_strJspPage = getJspConfigurator().getJspForm(m_objectref.getObjectClass().getClassName());
        }
        m_properties = m_objectref.getClonedProperties(null);
        m_batchPropertiesHandler.setProperties(m_properties);
        m_pasteMetadataHandler.setProperties(m_properties);
    }

    /**
     * Set the enabled status for paste metadata feature.
     * @param enabled_p
     * @since 3.1.0.0
     */
    public void setPasteMetadataEnabled(boolean enabled_p)
    {
        this.m_isPasteMetadataEnabled = enabled_p;
    }

    /** set context to be used for the read-only property
     *
     * @param context_p int as defined with OwPropertyClass.CONTEXT_...
     */
    public void setReadOnlyContext(int context_p)
    {
        m_ReadOnlyContext = context_p;
    }

    /**
     * Setter for saveAll button state.
     * @param saveAllEnabled_p
     * @since 3.1.0.0
     */
    public void setSaveAllActive(boolean saveAllEnabled_p)
    {
        m_saveAllActive = saveAllEnabled_p;
    }

    /** update view from request
     *  to save the changes call save
     *
     * @return OwPropertyCollection
     * @throws Exception
     */
    protected OwPropertyCollection update(HttpServletRequest request_p) throws Exception
    {
        // update the HTML form values
        if (m_theFieldManager.update(request_p, m_changedProperties, m_objectref.getProperties(null)))
        {
            // save for batch indexing
            m_batchPropertiesHandler.saveBatchIndexProperties();
            return m_changedProperties;
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
        return m_theFieldManager.update(request_p, changedProperties, m_objectref.getProperties(null));
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

    public void insertLabel(Writer w_p, String strPlaceHolder_p, String displayName, String styleClass) throws Exception
    {
        // scan the placeholder
        OwPropertyPlaceholder scanedPlaceholder = new OwPropertyPlaceholder(strPlaceHolder_p);
        if (m_properties == null)
        {
            w_p.write(displayName);
            return;
        }
        if (!scanedPlaceholder.isError() && !scanedPlaceholder.isValidation())
        {
            OwProperty property = (OwProperty) m_properties.get(scanedPlaceholder.getIdentifier());
            if (property != null)
            {
                boolean readOnlyRendered = isReadOnlyRendered(scanedPlaceholder, property);
                if (!isPropertyReadOnly(readOnlyRendered))
                {
                    int id = property.hashCode();
                    OwInsertLabelHelper.insertLabel(w_p, displayName, String.valueOf(id), styleClass);
                    return;
                }
            }
        }
        w_p.write(displayName);
    }

    public boolean isPropertyReadOnly(boolean readOnly)
    {
        return readOnly;
    }

    /** tuple after a successful placeholder scan
     * @since 2.5.3.0 is the inner class public*/
    public static class OwPropertyPlaceholder
    {
        /** start delimiter for the error string placeholders */
        private static final String PLACEHOLDER_ERROR_PREFIX = "ow_err_";
        /** start delimiter for the placeholders to be rendered as read-only always */
        private static final String PLACEHOLDER_READONLY_PREFIX = "ow_ro_";
        /** start delimiter for the placeholders */
        private static final String PLACEHOLDER_VALIDATION_PREFIX = "ow_validate_";

        /** flag indicating, that the error text for the property should be rendered*/
        private boolean m_fError;

        /** flag indicating, that the property should be rendered read only */
        private boolean m_fReadOnly;
        /** flag indicating, that the property is a sub property */
        private boolean m_fSubProperty;
        /** flag indicating, that the JS validation target for the property should be rendered*/
        private boolean m_fValidation;
        /** name of the property */
        private String m_strIdentifier;
        /** name of the sub property if namespace operator was used */
        private String m_strSubIdentifier;

        /** construct and scan the place holder
         *  of the following form:
         *
         *      [ow_err_ | ow_ro_]<PropertyNameIdentifier>[.<SubPropertyNameIdentifier>]
         *
         *      e.g.:
         *              Customer                : renders the property "Customer"
         *              ow_err_Customer         : renders the error string for the property "Customer"
         *              ow_ro_Custpmer          : renders the property "Customer" as read-only
         *              Customer.Address         : renders the property "Address" of the property "Customer", if Customer is of type OwObject
         */
        public OwPropertyPlaceholder(String strPlaceHolder_p)
        {
            m_strIdentifier = strPlaceHolder_p;

            int iQualifiedMarkerIndex = m_strIdentifier.indexOf('}');

            if (-1 != iQualifiedMarkerIndex)
            {
                m_strSubIdentifier = m_strIdentifier.substring(iQualifiedMarkerIndex + 1);
                m_strIdentifier = m_strIdentifier.substring(0, iQualifiedMarkerIndex);

                m_strIdentifier = m_strIdentifier.replaceAll("\\{", "");
                m_strIdentifier = m_strIdentifier.replaceAll("\\}", "");

                if (m_strSubIdentifier != null)
                {
                    if (m_strSubIdentifier.startsWith("."))
                    {
                        m_fSubProperty = true;
                        m_strSubIdentifier = m_strSubIdentifier.substring(1);
                        m_strSubIdentifier = m_strSubIdentifier.replaceAll("\\{", "");
                        m_strSubIdentifier = m_strSubIdentifier.replaceAll("\\}", "");
                    }
                    else
                    {
                        m_strSubIdentifier = null;
                    }
                }
            }
            else
            {
                // Split namespace operator if available
                int iNamespaceOperatorIndex = m_strIdentifier.indexOf('.');
                if (-1 != iNamespaceOperatorIndex)
                {
                    m_strSubIdentifier = m_strIdentifier.substring(iNamespaceOperatorIndex + 1);
                    m_strIdentifier = m_strIdentifier.substring(0, iNamespaceOperatorIndex);
                    m_fSubProperty = true;
                }
            }

            // read-only flag
            if (m_strIdentifier.startsWith(PLACEHOLDER_READONLY_PREFIX))
            {
                // === property should be rendered as read-only always
                m_strIdentifier = m_strIdentifier.substring(PLACEHOLDER_READONLY_PREFIX.length());
                m_fReadOnly = true;
            }

            // error flag
            if (m_strIdentifier.startsWith(PLACEHOLDER_ERROR_PREFIX))
            {
                // === placeholder for the error string
                m_strIdentifier = m_strIdentifier.substring(PLACEHOLDER_ERROR_PREFIX.length());
                m_fError = true;
            }

            // validation flag
            if (m_strIdentifier.startsWith(PLACEHOLDER_VALIDATION_PREFIX))
            {
                // === placeholder for the error string
                m_strIdentifier = m_strIdentifier.substring(PLACEHOLDER_VALIDATION_PREFIX.length());
                m_fValidation = true;
            }
        }

        /** name of the property */
        public String getIdentifier()
        {
            return m_strIdentifier;
        }

        /** name of the sub property if namespace operator was used */
        public String getSubIdentifier()
        {
            return m_strSubIdentifier;
        }

        /** flag indicating, that the property is a sub property */
        public boolean hasSubIdentifier()
        {
            return m_fSubProperty;
        }

        /** flag indicating, that the error text for the property should be rendered*/
        public boolean isError()
        {
            return m_fError;
        }

        /** flag indicating, that the property should be rendered read only */
        public boolean isReadOnly()
        {
            return m_fReadOnly;
        }

        /** flag indicating, that the JS validation target for the property should be rendered*/
        public boolean isValidation()
        {
            return m_fValidation;
        }

    }

}