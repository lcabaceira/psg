package com.wewebu.ow.server.plug.owdocprops;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwField;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.plug.owdocprops.OwEditMultiDocumentFieldsDialogSimple.OwEditField;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * OwFieldsView.
 * 
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
public abstract class OwFieldsView extends OwLayout implements OwMultipanel, OwFieldProvider
{
    /** event key metadata */
    private static final String EVENT_KEY_PASTE_THIS_META_DATA = "PasteThisMetaData";
    /** layout region definition for the main (fieldlist) region */
    public static final int MAIN_REGION = 1;
    /** layout region definition for the menu region */
    public static final int MENU_REGION = 2;

    /** flag to render vertical */
    public static final int VIEW_MASK_RENDER_VERTICAL = 0x0000004;
    /** flag to activate the paste metadata buttons*/
    public static final int VIEW_MASK_ENABLE_PASTE_METADATA = 0x0000080;

    /** query string for the fieldname */
    protected static final String QUERY_KEY_PROPNAME = "prop";

    /** set of flags indicating the behavior of the view
     */
    protected int m_iViewMask;

    /** instance of the field field class */
    protected OwFieldManager m_theFieldManager;

    /** fields to edit */
    protected Map m_fields;
    /** original fields for comparision */
    private Map m_clonedfields;

    /** Menu for buttons in the view */
    protected OwSubMenuView m_MenuView;

    /** menu ID of the apply button */
    protected int m_iAppyBtnIndex;

    /** (overridable) get the menu of the view
     * you can add menu items or override to have your own menu for the view (see VIEW_MASK_DISABLE_INTERNAL_MENU) 
     */
    public OwMenuView getMenu()
    {
        return m_MenuView;
    }

    public void detach()
    {
        super.detach();

        m_theFieldManager.detach();
    }

    /** set the fields to edit 
     * 
     * @param fields_p map of OwField
     */
    public void setFields(Map fields_p) throws Exception
    {
        m_fields = fields_p;

        m_clonedfields = createCloneFromFields(fields_p);
    }

    /**
     * Create a map with clones of editable fields.
     * @param fields_p - the original map
     * @return the map with cloned objects.
     * @since 3.0.0.0
     */
    protected Map createCloneFromFields(Map fields_p) throws Exception
    {
        Map result = new HashMap();
        Set keys = fields_p.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            String name = (String) iterator.next();
            OwEditField value = (OwEditField) fields_p.get(name);
            OwEditField clonedValue = new OwEditField(value.getFieldDefinition(), value.getValue());
            result.put(name, clonedValue);
        }
        return result;
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

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === get reference to the fieldfield manager instance
        m_theFieldManager = ((OwMainAppContext) getContext()).createFieldManager();
        m_theFieldManager.setExternalFormTarget(getFormTarget());

        m_theFieldManager.setFieldProvider(this);

        // create menu
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, MENU_REGION, null);

        // === add buttons
        if (getMenu() != null)
        {
            // apply button
            m_iAppyBtnIndex = getMenu().addFormMenuItem(this, getContext().localize("plug.owdocprops.OwFieldView.save", "Save"), "Apply", null);
            getMenu().setDefaultMenuItem(m_iAppyBtnIndex);

        }
    }

    /** 
     * event called when user clicked Apply button in menu, 
     *   
     * @param request_p a {@link HttpServletRequest}
     * @param oReason_p Optional reason object submitted in addMenuItem
     * 
     * @return true = fields have been saved, false  = one or more invalid fields
     */
    public boolean onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        Map changedFields = new HashMap();
        if (m_theFieldManager.update(request_p, changedFields, m_clonedfields))
        {
            // call client save method
            if (saveFields(changedFields))
            {
                succeed();
                return true;
            }
            else
            {
                failed();
            }
        }

        return false;
    }

    /**
     * Signal success for handling save fields. 
     * @since 4.2.0.0 
     */
    protected void succeed()
    {
        // inform user
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("plug.owdocprops.OwFieldView.saved", "The changes have been applied."));
    }

    /**
     * Signal problem handling saving fields.
     * @since 4.2.0.0
     */
    protected void failed()
    {
        //overwrite
    }

    /** save the fields
     *  to be implemented
     * 
     * @param changedFields_p
     */
    protected abstract boolean saveFields(Map changedFields_p) throws Exception;

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        // render internal regions
        switch (iRegion_p)
        {
            case MAIN_REGION:
                renderMainRegion(w_p);
                break;

            default:
                // render attached views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** check if a certain view field is enabled 
     * @param iViewMask_p <code>int</code> view mask as defined with VIEW_field_...
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return (m_iViewMask & iViewMask_p) > 0;
    }

    /** set the view fields
     * @param iViewMask_p <code>int</code> view mask (a combination of VIEW_field_... defined flags)
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** check if paste metadata is active and should be displayed for user
     */
    protected boolean activatePasteMetadata()
    {
        if (!hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA))
        {
            return false;
        }

        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
        switch (clipboard.getContentType())
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:
                return clipboard.getContent().size() == 1;

            case OwClipboard.CONTENT_TYPE_OW_FIELD:
                return clipboard.getContent().size() > 0;

            default:
                return false;
        }

    }

    /** overridable to render additional columns 
    *
    * @param w_p Writer
    */
    protected void renderExtraColumnHeader(java.io.Writer w_p) throws Exception
    {
        // === paste metadata column
        if (activatePasteMetadata())
        {
            // Paste all metadata column
            String pasteTooltip = getContext().localize("plug.owdocprops.OwFieldView.pasteallmeta", "Paste all metadata from the clipboard.");
            w_p.write("<th align=\"center\"><a title=\"" + pasteTooltip + "\" href=\"" + getEventURL("PasteAllMetaData", null) + "\">");
            w_p.write("<img hspace=\"5\" border=\"0\" src=\"" + getContext().getDesignURL() + "/images/OwObjectPropertyView/paste_metadata.png\"");
            w_p.write(" alt=\"");
            w_p.write(pasteTooltip);
            w_p.write("\" title=\"");
            w_p.write(pasteTooltip);
            w_p.write("\"/></a></th>");
        }
    }

    /** overridable get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param prop_p an {@link OwField} (current OwObject)
    *
    * @return String with style class name
    */
    protected String getRowClassName(int iIndex_p, OwField prop_p)
    {
        return ((iIndex_p % 2) != 0) ? "OwObjectPropertyView_OddRow" : "OwObjectPropertyView_EvenRow";
    }

    /** render the main region with the field list
     * @param w_p Writer object to write HTML to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // reset field manager
        m_theFieldManager.reset();

        // fields availalbe
        if ((m_fields == null) || (m_fields.size() == 0))
        {
            return;
        }

        if (hasViewMask(VIEW_MASK_RENDER_VERTICAL))
        {
            // === render a vertical	
            Iterator it = m_fields.values().iterator();
            while (it.hasNext())
            {
                OwField field = (OwField) it.next();
                OwFieldDefinition fielddefinition = field.getFieldDefinition();

                w_p.write("<div class='OwPropertyName' style='clear: both;'>");

                w_p.write(fielddefinition.getDisplayName(getContext().getLocale()));

                w_p.write("</div>\n");

                w_p.write("<div>\n");

                w_p.write("<div class='OwPropertyControl'>");

                m_theFieldManager.insertEditField(w_p, field);

                w_p.write("</div>\n");

                // === display error column for this field
                w_p.write("<div class='OwPropertyError'>");
                w_p.write(m_theFieldManager.getSafeFieldError(field));
                w_p.write("</div>\n");

                // === display HotInfo target for JS validation errors
                w_p.write("<div class='OwPropertyError' id='HotInfo" + m_theFieldManager.getFieldJSErrorCtrlID(field) + "'></div>\n");

                w_p.write("<br><br>\n");
                w_p.write("</div>\n");
            }
        }
        else
        {
            // === render horizontal	
            // === draw table with fields listed
            w_p.write("<table class='OwObjectPropertyView_PropList'>\n");

            // title
            w_p.write("<thead>\n");
            w_p.write("<tr class='OwObjectPropertyView_Header'>\n");

            // === overridable to render additional columns
            renderExtraColumnHeader(w_p);

            // not required column
            w_p.write("<th class='OwRequired'><img hspace='5' src='" + getContext().getDesignURL() + "/images/OwObjectPropertyView/notrequired.gif' alt='' title=''></th>");
            // field name, value and error column
            w_p.write("<th>" + getContext().localize("plug.owdocprops.OwFieldView.fieldcolumn", "Property") + "&nbsp;&nbsp;</th><th>" + getContext().localize("plug.owdocprops.OwFieldView.valuecolumn", "Value") + "&nbsp;&nbsp;</th>\n");
            w_p.write("</tr>\n");
            w_p.write("</thead>\n");
            w_p.write("<tbody>\n");

            Iterator it = m_fields.values().iterator();

            String strRowClassName = null;
            int iIndex = 0;

            while (it.hasNext())
            {
                OwField field = (OwField) it.next();
                OwFieldDefinition fielddefinition = field.getFieldDefinition();

                // === toggle even odd flag
                strRowClassName = getRowClassName(iIndex, field);

                // === draw the fields
                w_p.write("<tr class='" + strRowClassName + "'>\n");

                // === overridable to render additional columns
                renderExtraColumnRows(w_p, field, iIndex);

                // === required column
                w_p.write("<td class=\"OwRequired\">");

                if (fielddefinition.isRequired())
                {
                    String tooltip = getContext().localize("app.OwObjectPropertyView.required", "Required");
                    w_p.write("<img src='" + getContext().getDesignURL() + "/images/OwObjectPropertyView/required.png' alt='" + tooltip + "' title='" + tooltip + "'/>");
                }
                else
                {
                    w_p.write("<img src='" + getContext().getDesignURL() + "/images/OwObjectPropertyView/notrequired.gif' alt='' title=''/>");
                }

                w_p.write("</td>\n");

                w_p.write("<td nowrap class='OwPropertyName' title='" + fielddefinition.getClassName() + "'>");

                w_p.write(fielddefinition.getDisplayName(getContext().getLocale()) + ":&nbsp;&nbsp;");

                w_p.write("</td>\n<td class='DefaultInput'>\n");

                // property control
                w_p.write("<div class='OwPropertyControl'>");
                m_theFieldManager.insertEditField(w_p, field);
                w_p.write("</div>\n");

                // === display error column for this field
                w_p.write("<div class='OwPropertyError'>");
                w_p.write(m_theFieldManager.getSafeFieldError(field));
                w_p.write("</div>\n");

                // === display js validation HotInfo
                w_p.write("<div class='OwPropertyError' id='HotInfo_" + m_theFieldManager.getFieldJSErrorCtrlID(field) + "'></div>\n");

                w_p.write("</td>\n");

                w_p.write("</tr>\n");

                iIndex++;
            }

            w_p.write("</tbody>\n");
            w_p.write("</table>\n");
        }
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // === render 
        w_p.write(m_theFieldManager.renderErrors());

        w_p.write("<div style=\"clear:both\" class=\"OwObjectPropertyView\">");

        w_p.write("<table class='OwObjectPropertyView'>");

        w_p.write("<tr><td>");

        renderRegion(w_p, MAIN_REGION);

        w_p.write("</td></tr>");

        w_p.write("<tr><td class='OwObjectPropertyView_MENU OwInlineMenu'>");

        renderRegion(w_p, MENU_REGION);

        w_p.write("</td></tr>");

        w_p.write("</table>");

        w_p.write("</div>");
    }

    /** check if the property can be pasted from clipboard
     */
    private boolean isfieldPasteable(OwField prop_p, OwObject clipboardObject_p) throws Exception
    {
        try
        {
            OwField clipboardfield = clipboardObject_p.getProperty(prop_p.getFieldDefinition().getClassName());

            return (clipboardfield.getFieldDefinition().getJavaClassName().equals(prop_p.getFieldDefinition().getJavaClassName()) && (clipboardfield.getValue() != null));
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }
    }

    /** called when user presses the paste this metadata button
     */
    public void onPasteThisMetaData(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        String strfieldName = request_p.getParameter(QUERY_KEY_PROPNAME);
        OwField prop = (OwField) m_fields.get(strfieldName);
        switch (clipboard.getContentType())
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:// === paste the metadata
                pasteMetaDataFromClipboardObject(prop);
                break;

            case OwClipboard.CONTENT_TYPE_OW_FIELD: // === paste metadata from clipboard field
                pasteMetaDataFromClipboardField(prop);
                break;

            default://do nothing
        }
    }

    /** called when user presses the paste all metadata button
     */
    public void onPasteAllMetaData(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        switch (clipboard.getContentType())
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:
            {// === paste the metadata from Clipboard Object
                Iterator it = m_fields.values().iterator();
                while (it.hasNext())
                {
                    OwField prop = (OwField) it.next();
                    pasteMetaDataFromClipboardObject(prop);
                }
            }
                break;
            case OwClipboard.CONTENT_TYPE_OW_FIELD:
            {// === paste the metadata from Clipboard Fields
                Iterator it = m_fields.values().iterator();
                while (it.hasNext())
                {
                    OwField prop = (OwField) it.next();
                    pasteMetaDataFromClipboardField(prop);
                }
            }
                break;
            default://do nothing
        }

    }

    /**
     * Get current Clipboard, often used in this class.
     * @return OwClipboard the current exist
     */
    protected OwClipboard getClipboard()
    {
        return ((OwMainAppContext) getContext()).getClipboard();
    }

    /** paste for the given field the specific Value from Clipboard OwObject.
     * 
     * @param prop_p OwField which should be overwritten with MetaData from clipboard
     * @throws Exception
     */
    private void pasteMetaDataFromClipboardObject(OwField prop_p) throws Exception
    {//TODO implement paste MetaData from Clipboard Field to field
        if (prop_p != null)
        {
            OwClipboard clipboard = getClipboard();
            OwObject clipboardObject = ((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject();
            try
            {
                OwField clipboardfield = clipboardObject.getProperty(prop_p.getFieldDefinition().getClassName());

                // filter only writable user fields
                if (isfieldPasteable(prop_p, clipboardObject))
                {
                    prop_p.setValue(clipboardfield.getValue());
                }
            }
            catch (OwObjectNotFoundException e)
            {
                // ignore
            }
        }
    }

    /** Paste data for the given field the specific Value from Clipboard OwField.
     * 
     * @param prop_p OwField which should be overwritten with Metadata from clipboard.
     * @throws Exception
     */
    private void pasteMetaDataFromClipboardField(OwField prop_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        if (clipboard != null && !clipboard.getContent().isEmpty())
        {
            Iterator it = clipboard.getContent().iterator();
            while (it.hasNext())
            {
                OwField field = ((OwClipboardContentOwField) it.next()).getField();
                //search for the correct field <--> field association
                if (field.getFieldDefinition().getClassName().equals(prop_p.getFieldDefinition().getClassName()))
                {
                    prop_p.setValue(field.getValue());

                    //exit loop
                    break;
                }
            }
        }
    }

    /** overridable to render additional columns 
    *
    * @param w_p Writer
    * @param field_p current OwField to render
    * @param iIndex_p int row of field
    *
    */
    protected void renderExtraColumnRows(Writer w_p, OwField field_p, int iIndex_p) throws Exception
    {
        // === paste metadata column
        if (activatePasteMetadata())
        {
            OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
            w_p.write("\n<td align='center'>");

            //render paste button in row for complete OwObject
            if (clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT)
            {
                OwObject clipboardObject = ((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject();
                if (isfieldPasteable(field_p, clipboardObject))
                {
                    // look for same field and check data type
                    try
                    {
                        String pasteMetadataTooltip = getContext().localize("app.OwFieldView.pastethismeta", "Paste property from clipboard.");
                        w_p.write("<a title=\"" + pasteMetadataTooltip + "\" href=\"" + getFormEventURL(EVENT_KEY_PASTE_THIS_META_DATA, QUERY_KEY_PROPNAME + "=" + field_p.getFieldDefinition().getClassName()) + "\">");
                        w_p.write("<img hspace=\"5\" border=\"0\" src=\"" + getContext().getDesignURL() + "/images/OwObjectPropertyView/paste_metadata.png\"");
                        w_p.write(" alt=\"");
                        String pasteTooltip = getContext().localize1("app.OwFieldView.pastethismetaforfield", "Paste property from clipboard for field %1.", field_p.getFieldDefinition().getDisplayName(getContext().getLocale()));
                        w_p.write(pasteTooltip);
                        w_p.write("\" title=\"");
                        w_p.write(pasteTooltip);
                        w_p.write("\"/></a>");
                    }
                    catch (OwObjectNotFoundException e)
                    {
                    }
                }
            }
            //render paste button only for fields, contained in clipboard
            if (clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_FIELD)
            {
                for (Iterator it = clipboard.getContent().iterator(); it.hasNext();)
                {
                    OwField field = ((OwClipboardContentOwField) it.next()).getField();
                    if (field.getFieldDefinition().getClassName().equals(field_p.getFieldDefinition().getClassName()))
                    {
                        w_p.write("<a title=\"" + getContext().localize("app.OwFieldView.pastethismeta", "Paste property from clipboard."));
                        w_p.write("\" href=\"");
                        w_p.write(getFormEventURL(EVENT_KEY_PASTE_THIS_META_DATA, QUERY_KEY_PROPNAME + "=" + field_p.getFieldDefinition().getClassName()));
                        w_p.write("\"><img hspace=\"5\" border=\"0\" src=\"");
                        w_p.write(getContext().getDesignURL());
                        w_p.write("/images/OwObjectPropertyView/paste_metadata.png\")");
                        String pasteTooltip = getContext().localize1("app.OwFieldView.pastethismetaforfield", "Paste property from clipboard for field %1.", field_p.getFieldDefinition().getDisplayName(getContext().getLocale()));
                        w_p.write(" alt=\"");
                        w_p.write(pasteTooltip);
                        w_p.write("\" title=\"");
                        w_p.write(pasteTooltip);
                        w_p.write("\"/></a>");
                        break; //leave the for-loop, because the right field was found
                    }
                }
            }

            w_p.write("</td>");
        }
    }

    public boolean isValidated() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public OwField getField(String name_p) throws Exception, OwObjectNotFoundException
    {
        OwField field = (OwField) m_fields.get(name_p);

        if (null == field)
        {
            throw new OwObjectNotFoundException("OwFieldsView.getField: " + name_p);
        }

        return field;
    }

    public String getFieldProviderName()
    {
        return "OwFieldsView";
    }

    public Object getFieldProviderSource()
    {
        return this;
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT;
    }

    public Collection getFields() throws Exception
    {
        return m_fields.values();
    }

    public Object getSafeFieldValue(String name_p, Object defaultvalue_p)
    {
        OwField field = (OwField) m_fields.get(name_p);

        if (null == field)
        {
            return defaultvalue_p;
        }

        try
        {
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    public void setField(String name_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        OwField field = (OwField) m_fields.get(name_p);
        field.setValue(value_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwEventTarget#updateExternalFormTarget(javax.servlet.http.HttpServletRequest, boolean)
     */
    public boolean updateExternalFormTarget(HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        OwPropertyCollection changedProperties = new OwStandardPropertyCollection();
        return m_theFieldManager.update(request_p, changedProperties, m_clonedfields);
    }

}