package com.wewebu.ow.server.app;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwExceptionManager;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base Class for field User Interface.<br/>
 * Displays field as HTML and creates HTML Form Elements for editing properties,
 * also performs Validation. <br/>
 * You get a instance of the FieldManager by calling getContext().createFieldManager().
 * Use this instance with all fields in your view. <br/>
 * To be implemented with the specific DMS system.
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
public abstract class OwFieldManager extends OwEventTarget
{
    /**Parameter to disable validation handling of values.
     * @since 4.2.0.0
     */
    public static final String FLAG_DISABLE_VALIDATION = "wddV";
    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;

    /**  flag signal for debug reasons, to throw an error if client forgets to call reset */
    private boolean m_fDEBUG_ResetCalled = false;

    /** map which keeps the objects to be edited / displayed and create links upon */
    protected Map m_ObjectMap = new HashMap();

    /** list of strings, which designate errors on fields */
    protected Map m_FieldErrors;
    /**map between fields display names and their render Id, used for better error reporting */
    protected Map m_ids2displayNames;
    /** reference to field manager configuration*/
    private OwXMLUtil m_ConfigNode;

    /**
     * Get configuration node for current manager instance.
     * @return OwXMLUtil
     */
    protected OwXMLUtil getConfigNode()
    {
        return m_ConfigNode;
    }

    protected void init() throws Exception
    {
        m_MainContext = (OwMainAppContext) getContext();
    }

    /** init the field manager upon creation
     *
     * @param configNode_p OwXMLUtil node with configuration information
     */
    public void init(OwXMLUtil configNode_p) throws Exception
    {
        m_ConfigNode = configNode_p;
    }

    /** get the Field from the given ID
     *
     * @param strID_p String ID
     *
     * @return OwField
     */
    public OwField getField(String strID_p)
    {
        return (OwField) m_ObjectMap.get(new Integer(strID_p));
    }

    /** the field provider interface can be used by the fields to reference other fields. */
    private OwFieldProvider m_FieldProvider;

    /** set reference to a field provider interface<br />
     * the field provider interface can be used by the fields to reference other fields.
     * setting the field provider is optional
     *
     * @param fieldProvider_p OwFieldProvider interface
     */
    public void setFieldProvider(OwFieldProvider fieldProvider_p)
    {
        m_FieldProvider = fieldProvider_p;
    }

    /** check if has given type
     *
     * @param iFieldProviderType_p int type to query
     *
     * @return boolean true if field provider is of given type
     */
    public boolean isFieldProviderType(int iFieldProviderType_p)
    {
        if (getFieldProvider() != null)
        {
            return (getFieldProvider().getFieldProviderType() & iFieldProviderType_p) != 0;
        }
        else
        {
            return false;
        }
    }

    /** check if has given name
     *
     * @param sName_p String name to query
     *
     * @return boolean true if field provider is of given type
     */
    public boolean isFieldProviderName(String sName_p)
    {
        if (getFieldProvider() != null)
        {
            if (getFieldProvider().getFieldProviderName() != null)
            {
                return getFieldProvider().getFieldProviderName().equals(sName_p);
            }
        }
        return false;
    }

    /** get an interface to a field provider interface
     * the field provider interface can be used by the fields to reference other fields.
     *
     * @return OwFieldProvider or null if no field provider is available
     *
    */
    public OwFieldProvider getFieldProvider()
    {
        return m_FieldProvider;
    }

    /** external form view overrides internal form */
    protected OwEventTarget m_externalFormEventTarget = this;

    /** get the target, that is used for form date and renders form
     */
    public OwEventTarget getFormTarget()
    {
        return m_externalFormEventTarget;
    }

    /** Format and displays the value attached to the fieldClass in HTML
     * @param w_p Writer object to write HTML to
     * @param field_p OwField Value to be displayed
     */
    public void insertReadOnlyField(Writer w_p, OwField field_p) throws Exception
    {
        // delegate to derived field manager to generate HTML Code
        insertReadOnlyFieldInternal(w_p, field_p.getFieldDefinition(), field_p.getValue());
    }

    /** Format and displays the value attached to the fieldClass in HTML
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object Value to be displayed
     */
    protected abstract void insertReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception;

    /** control ID of the control that should receive the input focus, usually the first field */
    private String m_strFocusControlID;

    /**
     * if true, we do not reset the focus for the next call to {@link #reset()}.
     */
    private boolean m_keepCurrentFocus = false;

    /** retrieve the control ID of the control that should receive the input focus, usually the first field */
    public String getFocusControlID()
    {
        return m_strFocusControlID;
    }

    /**
     * @param strFocusControlID_p the m_strFocusControlID to set
     * @since 3.1.0.4
     */
    protected void setFocusControlID(String strFocusControlID_p)
    {
        this.m_strFocusControlID = strFocusControlID_p;
        this.m_keepCurrentFocus = true;
    }

    /** Format and displays the value attached to the fieldClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param field_p OwField Value to be displayed
     */
    public void insertEditField(Writer w_p, OwField field_p) throws Exception
    {
        // === check if client calls the Reset Function
        if (!m_fDEBUG_ResetCalled)
        {
            throw new OwInvalidOperationException("OwFieldManager.insertEditField: Used OwFieldManager without reseting the map. Did you call OwFieldManager.Reset() in your onRender method?");
        }

        // add to the map
        int hash = field_p.hashCode();
        m_ObjectMap.put(Integer.valueOf(hash), field_p);

        if (m_strFocusControlID == null)
        {
            m_strFocusControlID = String.valueOf(hash);
        }

        // delegate to derived field manager to render the field
        insertEditFieldInternal(w_p, field_p.getFieldDefinition(), field_p, String.valueOf(hash));
    }

    /** Format and displays the value attached to the fieldClass in HTML for use in a HTML form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    protected abstract void insertEditFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception;

    /** check if both values equal, works on null pointers as well
     * @param obj1_p first object to compare, can be null
     * @param obj2_p second object to compare, can be null
     * @return true if both values match or both are null, false otherwise
     */
    protected static boolean safeEqual(Object obj1_p, Object obj2_p)
    {
        if ((obj1_p == null) && (obj2_p == null))
        {
            return true;
        }

        if ((obj1_p != null) && (obj2_p != null))
        {
            //for BigDecimal equals implementation does not return true if the scale is not the same in both objects
            //thus 2.0 is not equal to 2.00 when compared by this method - see Java API documentation
            //we have to use compareTo
            if (obj1_p instanceof BigDecimal && obj2_p instanceof BigDecimal)
            {
                BigDecimal bigDecimalObj1 = (BigDecimal) obj1_p;
                BigDecimal bigDecimalObj2 = (BigDecimal) obj2_p;
                return bigDecimalObj1.compareTo(bigDecimalObj2) == 0;
            }
            else
            {
                if (obj1_p.getClass().isArray())
                {
                    boolean result = true;
                    try
                    {
                        Object[] obj1Array = (Object[]) obj1_p;
                        Object[] obj2Array = (Object[]) obj2_p;
                        result = obj1Array.length == obj2Array.length;
                        if (result)
                        {
                            for (int i = 0; i < obj1Array.length; i++)
                            {
                                result = safeEqual(obj1Array[i], obj2Array[i]);
                                if (!result)
                                {
                                    break;
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        result = false;
                    }
                    return result;
                }
                else
                {
                    return obj1_p.equals(obj2_p);
                }
            }

        }
        else
        {
            return false;
        }
    }

    /** compare to field maps and return the changed fields
     *
     * @param originalFields_p the original fields to compare
     * @param compareFields_p the compare map
     * @param changedProperties_p Map of returned changed fields
     */
    public static void computeChangedFields(Map originalFields_p, Map compareFields_p, Map changedProperties_p) throws Exception
    {
        // === client wants to know about changed fields
        Iterator it = originalFields_p.values().iterator();
        while (it.hasNext())
        {
            OwField field = (OwField) it.next();
            String strClassName = field.getFieldDefinition().getClassName();

            OwField compareProp = (OwField) compareFields_p.get(strClassName);

            if (null == compareProp)
            {
                continue;
            }

            if (!safeEqual(field.getValue(), compareProp.getValue()))
            {
                // return changed field in return map
                changedProperties_p.put(strClassName, compareProp);
            }
        }
    }

    /** update the field values upon request and validates the new values.
     *  displays error message if fields could not be updated
     *
     * @param request_p  HttpServletRequest
     * @param changedFields_p optional return map of changed fields, can be null. If compareMap_p is null, compares to the previous value
     * @param compareMap_p optional map with original map to compare to, can be null
     *
     * @return true = all fields are valid, false = one or more fields are invalid
     */
    public boolean update(HttpServletRequest request_p, Map changedFields_p, Map compareMap_p) throws Exception
    {
        return update(request_p, changedFields_p, compareMap_p, Boolean.parseBoolean(request_p.getParameter(FLAG_DISABLE_VALIDATION)));
    }

    /** update the field values upon request and validates the new values.
     *  displays error message if fields could not be updated
     *
     * @param request_p  HttpServletRequest
     * @param changedFields_p optional return map of changed fields, can be null. If compareMap_p is null, compares to the previous value
     * @param compareMap_p optional map with original map to compare to, can be null
     * @param fIgnoreErrors_p boolean disable error collection for pure updates
     *
     * @return true = all fields are valid, false = one or more fields are invalid
     */
    protected boolean update(HttpServletRequest request_p, Map changedFields_p, Map compareMap_p, boolean fIgnoreErrors_p) throws Exception
    {
        boolean fRet = true;

        m_FieldErrors = null;

        // === iterate over the stored fields and call updateField
        Iterator it = m_ObjectMap.values().iterator();

        while (it.hasNext())
        {
            OwField field = (OwField) it.next();
            OwFieldDefinition definition = field.getFieldDefinition();

            // try to update, catch and store error for later info
            try
            {
                // === update the property from form data
                // delegate to derived field manager
                Object newValue = updateFieldInternal(request_p, definition, field.getValue(), String.valueOf(field.hashCode()));

                // return changed fields map to client
                if (changedFields_p != null)
                {
                    // === client wants to know about changed fields
                    if (compareMap_p != null)
                    {
                        // === client submitted a compare map
                        String className = field.getFieldDefinition().getClassName();
                        OwField compareProp = (OwField) compareMap_p.get(className);
                        if (!safeEqual(newValue, compareProp.getValue()))
                        {
                            // return changed field in return map
                            changedFields_p.put(definition.getClassName(), field);
                        }
                    }
                    else
                    {
                        // === compare to the previous value
                        if (!safeEqual(newValue, field.getValue()))
                        {
                            // return changed field in return map
                            changedFields_p.put(definition.getClassName(), field);
                        }
                    }
                }

                // set value
                field.setValue(newValue);
            }
            catch (Exception e)
            {
                if (!fIgnoreErrors_p)
                {

                    // save error to inform user
                    String strError = OwExceptionManager.getExceptionDisplayText(e);
                    if ((strError == null) || (strError.length() == 0))
                    {
                        strError = getContext().localize("app.OwFieldManager.unknownerrorinfield", "The inserted value is invalid.");
                    }

                    setFieldError(field, strError);
                }

                fRet = false;
            }
        }

        //check for external control field errors

        return fRet && (m_FieldErrors == null || m_FieldErrors.isEmpty());
    }

    /** get the ID that should be used for the HTML element to display JavaScript errors
     *  keep the ID in sync, so that JS Errors can be displayed as well.
     *
     * @param field_p
     * @return a {@link String}
     */
    public String getFieldJSErrorCtrlID(OwField field_p)
    {
        return String.valueOf(field_p.hashCode());
    }

    /** writes a error message for the requested field, if the field contained an invalid value after user edited it.
     * @param field_p the requested field
     * @return error string or an empty string if field was valid
     */
    public String getSafeFieldError(OwField field_p) throws Exception
    {
        if (m_FieldErrors != null)
        {
            String strErr = (String) m_FieldErrors.get(Integer.valueOf(field_p.hashCode()));
            if (strErr != null)
            {
                return strErr;
            }
            else
            {
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * Clear the error maps
     * @since 3.0.0.0
     */
    public void resetErrors()
    {
        if (m_FieldErrors != null)
        {
            m_FieldErrors.clear();
        }
        if (m_ids2displayNames != null)
        {
            m_ids2displayNames.clear();
        }
    }

    /**
     * Render all errors collected by this {@link OwFieldManager} object
     * @return - a {@link String} object representing the HTML code for errors rendering
     * @throws Exception
     * @since 3.0.0.0
     */
    public String renderErrors() throws Exception
    {
        StringBuffer result = new StringBuffer();
        if (m_FieldErrors != null && m_FieldErrors.size() > 0)
        {
            result.append("<div name=\"Errors\" class=\"OwAllFieldErrors\">");
            Iterator fieldIds = m_FieldErrors.keySet().iterator();
            boolean isFirstLine = false;
            while (fieldIds.hasNext())
            {
                Integer fieldId = (Integer) fieldIds.next();
                if (m_ObjectMap != null)
                {
                    if (!isFirstLine)
                    {
                        if (m_FieldErrors.size() > 1)
                        {
                            result.append(getContext().localize("app.OwFieldManager.errors", "Errors:"));
                            result.append("<br/>");
                        }
                        isFirstLine = true;
                    }
                    else
                    {
                        result.append("<br/>");
                    }
                    StringWriter writer = new StringWriter();
                    if (m_ids2displayNames.containsKey(fieldId))
                    {
                        result.append(getContext().localize1("app.OwFieldManager.errorForField", "Error for field %1:", (String) m_ids2displayNames.get(fieldId)));
                        result.append("&nbsp;");
                    }
                    OwHTMLHelper.writeSecureHTML(writer, (String) m_FieldErrors.get(fieldId));
                    result.append(writer.toString());
                }

            }
            result.append("</div>");
        }
        return result.toString();
    }

    /**
     * Clears the errors associated with the given field.<br>
     * If no errors remain the {@link #m_FieldErrors} map is set to <code>null</code>
     * @param field_p
     * @since 2.5.2.0
     */
    public void clearFieldError(OwField field_p)
    {
        Integer hash = Integer.valueOf(field_p.hashCode());
        if (m_FieldErrors != null)
        {
            m_FieldErrors.remove(hash);
            if (m_FieldErrors.isEmpty())
            {
                m_FieldErrors = null;
            }
        }
        if (m_ids2displayNames != null)
        {
            m_ids2displayNames.remove(hash);
            if (m_ids2displayNames.isEmpty())
            {
                m_ids2displayNames = null;
            }
        }
    }

    /**
     * Sets the given error message for the given field.
     * @param field_p
     * @param strError_p
     * @since 2.5.2.0
     */
    public void setFieldError(OwField field_p, String strError_p)
    {
        if (m_FieldErrors == null)
        {
            m_FieldErrors = new LinkedHashMap();
        }
        if (m_ids2displayNames == null)
        {
            m_ids2displayNames = new LinkedHashMap();
        }
        Integer hash = Integer.valueOf(field_p.hashCode());
        m_FieldErrors.put(hash, strError_p);
        try
        {
            m_ids2displayNames.put(hash, field_p.getFieldDefinition().getDisplayName(getContext().getLocale()));
        }
        catch (Exception e)
        {
            //do nothing
        }
    }

    /** get the status after the last update
     *
     * @return boolean true = update was OK, all fields are valid, false = update caused errors, one or more fields are invalid
     */
    public boolean getUpdateStatus()
    {
        return (m_FieldErrors == null);
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    protected abstract Object updateFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception;

    /** attach a field control to the field manager
     *
     * @param strFieldClassName_p class name of the field that uses the control
     * @param control_p OwFieldManagerControl that renders the field
     * @param configNode_p DOM Node to the configuration XML for the control, or null to use defaults
     */
    public abstract void attachFieldControlByClass(String strFieldClassName_p, OwFieldManagerControl control_p, Node configNode_p) throws Exception;

    /** attach a field control to the field manager
     *
     * @param strJavaClassName_p java type of the field that uses the control
     * @param control_p OwFieldManagerControl that renders the field
     * @param configNode_p DOM Node to the configuration XML for the control, or null to use defaults
     */
    public abstract void attachFieldControlByType(String strJavaClassName_p, OwFieldManagerControl control_p, Node configNode_p) throws Exception;

    /** get a reference to a MIME manager that can be used to render objects
     *
     * @return OwMimeManager
     */
    public abstract OwMimeManager getMimeManager();

    /**
     * clear the map before you call insert... methods, otherwise the map would increase to infinite
     *
     * <p>NOTE: Never forget to call this function in your onRender Method</p>
     * @since 3.2.0.0 
     */
    public void reset()
    {
        m_ObjectMap.clear();

        // signal for debug reasons, to throw an error if client forgets to call reset
        m_fDEBUG_ResetCalled = true;

        if (m_keepCurrentFocus)
        {
            this.m_keepCurrentFocus = false;
        }
        else
        {
            // clear focus ID
            m_strFocusControlID = null;
        }
    }

    /** update the view after a view event (caused by getEditableViewEventURL), so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        return update(request_p, null, null, true);
    }

    /** override the internal OwEditable with an external one,
     *  must be called BEFORE view is attached.
     *
     * <br>NOTE:    By default, view will render its own form,
     *              unless you call setEditable
     *              When setting an external OwEditable,
     *              the view will not render a own form,
     *              but use the form name of the given OwEditable.
     *
     *              ==> Several form views can update each other.
     *
     * @param eventtarget_p OwEventTarget
     */
    public void setExternalFormTarget(OwEventTarget eventtarget_p) throws Exception
    {
        m_externalFormEventTarget = eventtarget_p;
    }

    /** get the form used for the template edit fields
     *  Returns the internal render form or the external one
     *  if you called setFormName.
     *
     * @return String form name
     */
    public String getFormName()
    {
        if (this == m_externalFormEventTarget)
        {
            return null;
        }
        else
        {
            return m_externalFormEventTarget.getFormName();
        }
    }

    /**
     * Renders a property control label on the given Writer
     * @param w_p Writer
     * @param readOnlyView_p Read only view flag
     * @param readOnly_p Read only property flag
     * @param field_p Property to be rendered
     * @param suffix_p String
     * @param writeLabel_p boolean Write Label
     * @throws Exception
     */
    public abstract void insertLabel(Writer w_p, boolean readOnlyView_p, boolean readOnly_p, OwField field_p, String suffix_p, boolean writeLabel_p) throws Exception;

    /**
     * Will extract the value from request, and transform it into dependent type representation defined by OwFieldDefinition.
     * @param request_p HttpServletRequest
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object old/current value of field
     * @param strID_p String id of field
     * @return Object reference to specific type representation (can return null)
     * @throws OwException if unable to convert value
     * @since 4.2.0.0
     */
    public abstract Object convertValue(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws OwException;

}