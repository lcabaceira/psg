package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * View Module to display and edit search criteria.
 * The View must be enclosed by a form, or you can use the function setFormName to internally create a form in the view.
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
public class OwSearchCriteriaView extends OwView implements OwFieldProvider
{
    /** mask value to enable vertical rendering of criteria */
    public static final int VIEW_MASK_RENDER_VERTICAL = 0x0001;

    /** placeholder for the server base url */
    public static final String RUNTIME_PLACEHOLDER_BASE_URL = "ow_baseurl";
    /** placeholder for the server design url */
    public static final String RUNTIME_PLACEHOLDER_DESIGN_URL = "ow_designurl";

    /** start delimiter for the placeholders */
    public static final String PLACEHOLDER_ERROR_PREFIX = "ow_err_";
    /** start delimiter for the placeholders */
    public static final String PLACEHOLDER_INSTRUCTION_PREFIX = "ow_inst_";
    /** start delimiter for the placeholders */
    public static final String PLACEHOLDER_VALIDATION_PREFIX = "ow_validate_";
    /** errors region*/
    public static final int ERRORS_REGION = 9;

    /** instance of the property field class */
    protected OwFieldManager m_theFieldManager;

    /** list of the search criteria */
    protected List m_CriteriaList;

    /** optional HTML form */
    protected String m_strHtmlFormular;
    /** optional JSP form */
    protected String m_strJspFormular;

    /** map of criteria for fast access when using a layout */
    protected Map m_CriteriaMap;

    /** filters the views to be displayed*/
    protected int m_iViewMask = 0;

    /** determine the views to be displayed by masking them with their flag
    *
    * @param iViewMask_p bitmask according to VIEW_MASK_... flags
    */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === get reference to the property field manager instance
        m_theFieldManager = ((OwMainAppContext) getContext()).createFieldManager();
        m_theFieldManager.setExternalFormTarget(getFormTarget());
    }

    /** implementation for the OwFieldProvider interface
     */
    public OwField getField(String strFieldClassName_p) throws Exception
    {
        return m_theFieldManager.getFieldProvider().getField(strFieldClassName_p);
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return m_theFieldManager.getFieldProvider().getFieldProviderSource();
    }

    /** implementation for the OwFieldProvider interface
     * get the type of field provider as defined with TYPE_... 
     */
    public int getFieldProviderType()
    {
        return m_theFieldManager.getFieldProvider().getFieldProviderType();
    }

    /** remove view and all subviews from context
       */
    public void detach()
    {
        super.detach();

        // detach field manager manually
        m_theFieldManager.detach();
    }

    /** set an interface to a field provider interface
     * the field provider interface can be used by the fields to reference other fields.
     * setting the field provider is optional
     *
     * @param fieldProvider_p OwFieldProvider interface
     *
     * 
     */
    public void setFieldProvider(OwFieldProvider fieldProvider_p)
    {
        m_theFieldManager.setFieldProvider(fieldProvider_p);
    }

    /** set list of the search criteria 
     * @param criteriaList_p List with OwSearchCriteria to be edited in the view
     */
    public void setCriteriaList(List criteriaList_p)
    {
        m_CriteriaList = criteriaList_p;

        // clear corresponding map
        m_CriteriaMap = null;
    }

    /** set the optional HTML form to use in the render method
     * @param strFormular_p String optional HTML layout
     */
    public void setHTMLFormular(String strFormular_p)
    {
        m_strHtmlFormular = strFormular_p;
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
     *  <% m_View.renderNamedRegion(out,ow_inst_<FieldName>); %>        : renders the Property FieldName always read-only<br>
     *  <% m_View.renderNamedRegion(out,ow_menu); %>                    : renders a function menu to save changes<br>
     *
     * <br><br>You can also cast the m_View to OwFieldProvider to retrieve a OwField instance and manipulate or program a field directly.
     *
     * @param strJspUrl_p String optional HTML layout
     */
    public void setJspFormular(String strJspUrl_p)
    {
        m_strJspFormular = strJspUrl_p;
    }

    /** lookup a criteria by the unique name
     * @param strName_p 
     */
    public OwSearchCriteria lookupCriteria(String strName_p) throws Exception
    {
        // if we have a layout, we also need a unique name hashtable for fast access
        if ((m_CriteriaList != null) && (m_CriteriaMap == null))
        {
            // === create hashmap with unique names if not already done
            m_CriteriaMap = new HashMap();
            for (int i = 0; i < m_CriteriaList.size(); i++)
            {
                OwSearchCriteria SearchCriteria = (OwSearchCriteria) m_CriteriaList.get(i);
                m_CriteriaMap.put(SearchCriteria.getUniqueName(), SearchCriteria);
            }
        }

        if (m_CriteriaMap != null)
        {
            return (OwSearchCriteria) m_CriteriaMap.get(strName_p);
        }
        else
        {
            return null;
        }
    }

    /** try to render the placeholder, assuming it is a runtime placeholder
     *  If it is not a runtime placeholder it is ignored
     *
     *  @param w_p render target Writer
     *  @param  strPlaceholder_p runtime placeholder as defined with RUNTIME_PLACEHOLDER_...
     *
     *  @return true if placeholder was rendered, false if it was not a runtime placeholder
     */
    private boolean tryRenderRuntimePlaceholder(Writer w_p, String strPlaceholder_p) throws Exception
    {
        if (strPlaceholder_p.equals(RUNTIME_PLACEHOLDER_BASE_URL))
        {
            // === insert base URL 
            w_p.write(getContext().getBaseURL());
            return true;
        }

        if (strPlaceholder_p.equals(RUNTIME_PLACEHOLDER_DESIGN_URL))
        {
            // === insert design URL 
            w_p.write(getContext().getDesignURL());
            return true;
        }

        return false;
    }

    /** Called when the form parser finds a placeholder in the HTML input

     * @param strPlaceholder_p the placeholder string found
     * @param w_p Writer object, write to replace placeholder
     */
    public void renderNamedRegion(Writer w_p, String strPlaceholder_p) throws Exception
    {
        // look for runtime placeholder first
        if (tryRenderRuntimePlaceholder(w_p, strPlaceholder_p))
        {
            return;
        }

        // === render a criteria found in the layout
        if (strPlaceholder_p.startsWith(PLACEHOLDER_ERROR_PREFIX))
        {
            // === placeholder for the error string
            strPlaceholder_p = strPlaceholder_p.substring(PLACEHOLDER_ERROR_PREFIX.length());

            // look up criteria by unique name
            OwSearchCriteria SearchCriteria = lookupCriteria(strPlaceholder_p);
            if (SearchCriteria != null)
            {
                // insert error field
                w_p.write(m_theFieldManager.getSafeFieldError(SearchCriteria));
                if (SearchCriteria.isCriteriaOperatorRange())
                {
                    w_p.write(m_theFieldManager.getSafeFieldError(SearchCriteria.getSecondRangeCriteria()));
                }
            }
            else
            {
                w_p.write("Criteria not found: " + strPlaceholder_p);
            }
        }
        else if (strPlaceholder_p.startsWith(PLACEHOLDER_VALIDATION_PREFIX))
        {
            // === placeholder for the error string
            strPlaceholder_p = strPlaceholder_p.substring(PLACEHOLDER_VALIDATION_PREFIX.length());

            // look up criteria by unique name
            OwSearchCriteria SearchCriteria = lookupCriteria(strPlaceholder_p);
            if (SearchCriteria != null)
            {
                // insert empty HotInfo target
                w_p.write("<span class=\"OwPropertyError\" id=\"HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(SearchCriteria) + "\"></span>");
            }
            else
            {
                w_p.write("Criteria not found: " + strPlaceholder_p);
            }
        }
        else if (strPlaceholder_p.startsWith(PLACEHOLDER_INSTRUCTION_PREFIX))
        {
            // === placeholder for the instruction string
            strPlaceholder_p = strPlaceholder_p.substring(PLACEHOLDER_INSTRUCTION_PREFIX.length());

            // look up criteria by unique name
            OwSearchCriteria SearchCriteria = lookupCriteria(strPlaceholder_p);
            if (SearchCriteria != null)
            {
                // insert error field
                w_p.write(getSafeInstruction(SearchCriteria));
            }
            else
            {
                w_p.write("Criteria not found: " + strPlaceholder_p);
            }
        }
        else
        {
            // === placeholder for the criteria
            OwSearchCriteria SearchCriteria = lookupCriteria(strPlaceholder_p);

            if (SearchCriteria != null)
            {
                // insert edit field for the criteria
                renderEditCriteria(w_p, SearchCriteria);
            }
            else
            {
                w_p.write("Criteria not found: " + strPlaceholder_p);
            }
        }
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     */
    public void onRender(Writer w_p) throws Exception
    {
        // reset field manager
        m_theFieldManager.reset();

        if (m_strJspFormular != null)
        {
            // === render through JSP page
            serverSideDesignInclude(m_strJspFormular, w_p);
        }
        else
        {
            // === render the criteria 
            if (m_strHtmlFormular != null)
            {
                renderHTMLFormular(w_p, m_strHtmlFormular);
            }
            else
            {
                if (hasViewMask(VIEW_MASK_RENDER_VERTICAL))
                {
                    onRenderVertical(w_p);
                }
                else
                {
                    onRenderHorizontal(w_p);
                }
            }
        }
        // set focus 
        ((OwMainAppContext) getContext()).setFocusControlID(m_theFieldManager.getFocusControlID());
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#renderRegion(java.io.Writer, int)
     */
    public void renderRegion(Writer w_p, int region_p) throws Exception
    {
        if (region_p == ERRORS_REGION)
        {
            w_p.write(m_theFieldManager.renderErrors());
        }
        else
        {
            super.renderRegion(w_p, region_p);
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

    /** get a instruction for the criteria 
     * @param searchCriteria_p OwSearchCriteria
     *
     * @return String instruction or empty String
     * @throws Exception 
     */
    public String getSafeInstruction(OwSearchCriteria searchCriteria_p) throws Exception
    {
        String strInstruction = searchCriteria_p.getInstruction();
        String sWildCardInfo = getSafeWildCardInfo(searchCriteria_p);

        if (strInstruction == null)
        {
            return sWildCardInfo;
        }
        else if (sWildCardInfo.equals(""))
        {
            return strInstruction;
        }
        else
        {
            return strInstruction + ", " + sWildCardInfo;
        }
    }

    /** get the wildcard descriptions for the given field and operator
     * 
     * @param searchCriteria_p an {@link OwSearchCriteria}
     * @return wild card info string
     * 
     * @throws Exception
     */
    private String getSafeWildCardInfo(OwSearchCriteria searchCriteria_p) throws Exception
    {
        //return no wildcard description if it should not be shown
        if (!((OwMainAppContext) getContext()).doShowWildCardDescriptions())
        {
            return "";
        }

        Collection defs = searchCriteria_p.getWildCardDefinitions();
        if (defs == null)
        {
            return "";
        }

        StringBuffer ret = new StringBuffer();

        Iterator it = defs.iterator();
        while (it.hasNext())
        {
            OwWildCardDefinition def = (OwWildCardDefinition) it.next();

            ret.append(def.getDescription(getContext().getLocale()));

            if (it.hasNext())
            {
                ret.append(", ");
            }
        }

        return ret.toString();
    }

    /**
     * Renders a property control label on the given Writer.<br>
     * The property's read-only status and the value type (array or complex value) 
     * are considered at rendering time.
     * @param w_p
     * @param criteria_p
     * @param suffix_p
     * @throws Exception
     * @since 3.0.0.0`
     */
    protected void renderPropertyLabel(Writer w_p, OwSearchCriteria criteria_p, String suffix_p) throws Exception
    {
        OwFieldDefinition fieldDefinition_p = criteria_p.getFieldDefinition();

        String label = null;
        String strOperator = criteria_p.getOperatorDisplayName(getContext().getLocale());
        //don't show operatornames like "ist gleich" or "equals" => can be set in "oecmlocalize.jar", e.g. "field.OwSearchOperator.equal="
        if (strOperator.equalsIgnoreCase(""))
        {
            label = criteria_p.getFieldDefinition().getDisplayName(getContext().getLocale()) + ":&nbsp;";
        }
        else
        {
            label = criteria_p.getFieldDefinition().getDisplayName(getContext().getLocale()) + "&nbsp;" + strOperator + ":&nbsp;";
        }

        if (criteria_p.isReadonly() || (fieldDefinition_p.isArray() && OwInsertLabelHelper.fromFieldValue(criteria_p.getValue(), criteria_p.getFieldDefinition()) == null) || fieldDefinition_p.isComplex())
        {
            w_p.write(label);
            if (suffix_p != null)
            {
                w_p.write(suffix_p);
            }
        }
        else
        {
            w_p.write("<label for=\"");
            String fieldId = String.valueOf(fieldDefinition_p.hashCode());
            w_p.write(fieldId);
            w_p.write("\">");
            w_p.write(label);
            if (suffix_p != null)
            {
                w_p.write(suffix_p);
            }
            w_p.write("</label>");
        }

    }

    /** render the search template normal without a form

     * @param w_p Writer object, write to replace placeholder
     */
    protected void onRenderHorizontal(Writer w_p) throws Exception
    {
        // === render search template
        if (m_CriteriaList != null)
        {
            w_p.write("\n\n<div class=\"OwSearchCriteriaView OwNowrapLabel OwPropertyBlockLayout " + ((m_CriteriaList.size() > 4) ? "OwScrollable" : "") + "\">");
            for (int i = 0; i < m_CriteriaList.size(); i++)
            {
                OwSearchCriteria SearchCriteria = (OwSearchCriteria) m_CriteriaList.get(i);

                if (SearchCriteria.isReadonly() && !SearchCriteria.isHidden())
                {
                    // === read only
                    w_p.write("<div class=\"OwPropertyBlock\">\n");

                    w_p.write("<div class=\"OwPropertyLabel\">" + SearchCriteria.getFieldDefinition().getDisplayName(getContext().getLocale()) + "&nbsp;" + SearchCriteria.getOperatorDisplayName(getContext().getLocale()) + ":</div>\n");
                    w_p.write("<div class=\"OwPropertyValue\">\n");

                    renderReadOnlyCriteria(w_p, SearchCriteria);

                    // js validation column
                    w_p.write("<div class=\"OwPropertyError\" id=\"HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(SearchCriteria) + "\"></div>\n");

                    w_p.write("</div>\n");

                    w_p.write("</div>\n");
                }
                else if (!SearchCriteria.isHidden())
                {
                    // === editable
                    w_p.write("<div class=\"OwPropertyBlock\">\n");

                    w_p.write("<div class=\"OwPropertyLabel\">");
                    renderPropertyLabel(w_p, SearchCriteria, null);
                    w_p.write("</div>\n");

                    w_p.write("<div class=\"OwPropertyValue\">\n");
                    renderEditCriteria(w_p, SearchCriteria);

                    // instruction column
                    w_p.write("<div class=\"OwInstructionName\">");
                    w_p.write(getSafeInstruction(SearchCriteria));
                    w_p.write("</div>\n");
                    writeSearchCriteriaError(w_p, SearchCriteria);
                    // js validation column
                    w_p.write("<div class=\"OwPropertyError\" id=\"HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(SearchCriteria) + "\"></div>\n");

                    w_p.write("</div>\n");

                    w_p.write("</div>\n");
                }
            }

            w_p.write("</div>\n");
        }
    }

    /**
     * Write errors for a search criteria, if any. Treat special case when search criteria is a range.
     * @throws Exception - when something went wrong.
     */
    private void writeSearchCriteriaError(Writer w_p, OwSearchCriteria searchCriteria_p) throws Exception
    {
        // error column
        w_p.write("<div class=\"OwPropertyError\">");
        String searchCriteriaError = m_theFieldManager.getSafeFieldError(searchCriteria_p);
        w_p.write(searchCriteriaError);
        if (searchCriteria_p.isCriteriaOperatorRange())
        {
            if (searchCriteriaError != null && searchCriteriaError.trim().length() != 0)
            {
                w_p.write("<br>");
            }
            w_p.write(m_theFieldManager.getSafeFieldError(searchCriteria_p.getSecondRangeCriteria()));
        }
        w_p.write("</div>\n");
    }

    /** render the search template normal without a form

     * @param w_p Writer object, write to replace placeholder
     */
    protected void onRenderVertical(Writer w_p) throws Exception
    {
        // === render search template
        if (m_CriteriaList != null)
        {
            w_p.write("\n\n<table class=\"OwSearchCriteriaView OwNowrapLabel\">");

            String strOperator = "";
            for (int i = 0; i < m_CriteriaList.size(); i++)
            {
                OwSearchCriteria SearchCriteria = (OwSearchCriteria) m_CriteriaList.get(i);

                if (SearchCriteria.isReadonly() && !SearchCriteria.isHidden())
                {
                    // === read only
                    w_p.write("\n<tr><td class=\"OwPropertyName\" >" + SearchCriteria.getFieldDefinition().getDisplayName(getContext().getLocale()) + "&nbsp;" + SearchCriteria.getOperatorDisplayName(getContext().getLocale()) + ":</td></tr>");

                    w_p.write("\n<tr><td class=\"OwPropertyControl\">");
                    renderReadOnlyCriteria(w_p, SearchCriteria);
                    w_p.write("</td></tr>");

                    // space column
                    w_p.write("\n<tr><td>&nbsp;</td></tr>");
                }
                else if (!SearchCriteria.isHidden())
                {
                    // === editable
                    strOperator = SearchCriteria.getOperatorDisplayName(getContext().getLocale());
                    //don't show operatornames like "ist gleich" or "equals" => can be set in "oecmlocalize.jar", e.g. "field.OwSearchOperator.equal="
                    if (strOperator.equalsIgnoreCase(""))
                    {
                        w_p.write("\n<tr><td class=\"OwPropertyName\">" + SearchCriteria.getFieldDefinition().getDisplayName(getContext().getLocale()) + ":&nbsp;</td></tr>");
                    }
                    else
                    {
                        w_p.write("\n<tr><td class=\"OwPropertyName\">" + SearchCriteria.getFieldDefinition().getDisplayName(getContext().getLocale()) + "&nbsp;" + strOperator + ":</td></tr>");
                    }

                    w_p.write("\n<tr><td class=\"OwPropertyControl\">");
                    renderEditCriteria(w_p, SearchCriteria);
                    w_p.write("</td></tr>");

                    // error column
                    if (m_theFieldManager.getSafeFieldError(SearchCriteria).length() > 0)
                    {
                        w_p.write("\n<tr><td class=\"OwPropertyError\">&nbsp;&nbsp;");
                        w_p.write(m_theFieldManager.getSafeFieldError(SearchCriteria));
                        w_p.write("</td>");
                        if (SearchCriteria.isCriteriaOperatorRange())
                        {
                            String secondSearchCriteriaError = m_theFieldManager.getSafeFieldError(SearchCriteria.getSecondRangeCriteria());
                            if (secondSearchCriteriaError != null && secondSearchCriteriaError.trim().length() != 0)
                            {
                                w_p.write("\n<td class=\"OwPropertyError\">&nbsp;&nbsp;");
                                w_p.write(secondSearchCriteriaError);
                                w_p.write("\n</td>\n");
                            }
                        }
                        w_p.write("</tr>");
                    }

                    // js validation column
                    w_p.write("\n<tr><td class=\"OwPropertyError\" id=\"HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(SearchCriteria) + "\">&nbsp;</td></tr>");

                    String sInstr = getSafeInstruction(SearchCriteria);
                    if (sInstr.length() > 0)
                    {
                        // instruction column
                        w_p.write("\n<tr><td class=\"OwInstructionName\">");

                        w_p.write(sInstr);

                        w_p.write("</td></tr>");
                    }

                    // space column
                    w_p.write("\n<tr><td>&nbsp;</td></tr>");
                }
            }

            w_p.write("</table>\n");
        }
    }

    /** render a single editable criteria
     * 
     * @param w_p
     * @param crit_p
     * @throws Exception 
     */
    protected void renderEditCriteria(Writer w_p, OwSearchCriteria crit_p) throws Exception
    {
        if (crit_p.isCriteriaOperatorRange())
        {
            w_p.write("<table><tr><td>");
            m_theFieldManager.insertEditField(w_p, crit_p);
            w_p.write("</td><td>&nbsp;&nbsp;-&nbsp;&nbsp;</td><td>");
            m_theFieldManager.insertEditField(w_p, crit_p.getSecondRangeCriteria());
            w_p.write("</td></tr></table>");
        }
        else
        {
            if (crit_p.getFieldDefinition().isArray() && crit_p.getValue() == null)
            {//multi value properties should be displayed with at least on Input field
                crit_p.setValue(new Object[] { null });
            }
            m_theFieldManager.insertEditField(w_p, crit_p);
        }
    }

    /** render a single read only criteria
     * 
     * @param w_p
     * @param crit_p
     * @throws Exception 
     */
    protected void renderReadOnlyCriteria(Writer w_p, OwSearchCriteria crit_p) throws Exception
    {
        if (crit_p.isCriteriaOperatorRange())
        {
            m_theFieldManager.insertReadOnlyField(w_p, crit_p);
            w_p.write("&nbsp; - &nbsp;");
            m_theFieldManager.insertReadOnlyField(w_p, crit_p.getSecondRangeCriteria());
        }
        else
        {
            m_theFieldManager.insertReadOnlyField(w_p, crit_p);
        }
    }

    /**  event called when user submits the form
     *   updates the criteria, does the same as updateEditable
     *
     *   NOTE: Exceptions are handled and displayed automatically
     *
     *   @param request_p  HttpServletRequest
     *   @return true on success, false if errors occurred 
     */
    public boolean onSubmitSearch(HttpServletRequest request_p) throws Exception
    {
        return m_theFieldManager.update(request_p, null, null);
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
        return m_theFieldManager.update(request_p, null, null);
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return m_theFieldManager.getFieldProvider().getFieldProviderName();
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
        OwField field = getField(sName_p);
        field.setValue(value_p);
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
            OwField field = getField(sName_p);
            return field.getValue();
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
        return m_CriteriaList;
    }

    /**
     * Reset the errors from {@link OwFieldManager} object
     * @since 3.0.0.0
     */
    public void resetErrors()
    {
        if (m_theFieldManager != null)
        {
            m_theFieldManager.resetErrors();
        }
    }
}