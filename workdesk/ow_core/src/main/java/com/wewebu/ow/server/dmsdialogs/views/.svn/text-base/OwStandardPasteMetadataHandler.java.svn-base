package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwField;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * OwStandardPasteMetadataHandler - Used for plugins with JSPForm.
 * Provide support for paste operations inside properties view.
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
 *@since 3.1.0.0
 */
public class OwStandardPasteMetadataHandler implements OwPasteMetadataHandler
{
    /** query string for the property name */
    protected static final String QUERY_KEY_PROPNAME = "prop";
    /** event key paste metadata */
    private static final String EVENT_NAME_PASTE_THIS_META_DATA = "PasteThisMetaData";
    /** the context*/
    private OwMainAppContext m_context;

    /** properties from the object */
    protected OwPropertyCollection m_properties;
    /** flag for system view status*/
    private boolean m_isSystemView;
    /** flag indicating that paste feature is enabled*/
    private boolean m_hasPasteMetaDataEnabled;

    /**Constructor*/
    public OwStandardPasteMetadataHandler(OwMainAppContext context_p, boolean isSystemPropertiesView_p, boolean hasPasteMetadataEnabled_p)
    {
        m_context = context_p;
        m_isSystemView = isSystemPropertiesView_p;
        m_hasPasteMetaDataEnabled = hasPasteMetadataEnabled_p;
        m_properties = new OwStandardPropertyCollection();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    public void setProperties(OwPropertyCollection properties_p)
    {
        m_properties = properties_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#onPasteAllMetaData(javax.servlet.http.HttpServletRequest)
     */
    public void onPasteAllMetaData(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        switch (clipboard.getContentType())
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:
            {// === paste the metadata from Clipboard Object
                Iterator it = m_properties.values().iterator();
                while (it.hasNext())
                {
                    OwProperty prop = (OwProperty) it.next();
                    pasteMetaDataFromClipboardObject(prop);
                }
            }
                break;
            case OwClipboard.CONTENT_TYPE_OW_FIELD:
            {// === paste the metadata from Clipboard Fields
                Iterator it = m_properties.values().iterator();
                while (it.hasNext())
                {
                    OwProperty prop = (OwProperty) it.next();
                    pasteMetaDataFromClipboardField(prop);
                }
            }
                break;
            default://do nothing
        }

    }

    /** paste for the given Property the specific Value from Clipboard OwObject.
     * 
     * @param prop_p OwProperty which should be overwritten with MetaData from clipboard
     * @throws Exception
     */
    private void pasteMetaDataFromClipboardObject(OwProperty prop_p) throws Exception
    {//TODO implement paste MetaData from Clipboard Field to Property
        if (prop_p != null)
        {
            OwClipboard clipboard = getClipboard();
            OwObject clipboardObject = ((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject();
            try
            {
                OwProperty clipboardProperty = clipboardObject.getProperty(prop_p.getPropertyClass().getClassName());

                // filter only writable user properties
                if (isPropertyPasteable(prop_p, clipboardObject))
                {
                    prop_p.setValue(clipboardProperty.getValue());
                }
            }
            catch (OwObjectNotFoundException e)
            {
                // ignore
            }
        }
    }

    /** Paste data for the given Property the specific Value from Clipboard OwField.
     * 
     * @param prop_p OwProperty which should be overwritten with Metadata from clipboard.
     * @throws Exception
     */
    private void pasteMetaDataFromClipboardField(OwProperty prop_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        if (clipboard != null && !clipboard.getContent().isEmpty())
        {
            Iterator it = clipboard.getContent().iterator();
            while (it.hasNext())
            {
                OwField field = ((OwClipboardContentOwField) it.next()).getField();
                //search for the correct property <--> field association
                if (field.getFieldDefinition().getClassName().equals(prop_p.getFieldDefinition().getClassName()))
                {
                    OwPropertyClass info = prop_p.getPropertyClass();
                    //check if this property can be overwritten with Data from clipboard
                    if (!info.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && !info.isSystemProperty())
                    {
                        prop_p.setValue(field.getValue());
                    }
                    //exit loop
                    break;
                }
            }
        }
    }

    /** check if the property can be pasted from clipboard
     */
    private boolean isPropertyPasteable(OwProperty prop_p, OwObject clipboardObject_p) throws Exception
    {
        try
        {
            OwProperty clipboardProperty = clipboardObject_p.getProperty(prop_p.getPropertyClass().getClassName());

            return (clipboardProperty.getPropertyClass().getJavaClassName().equals(prop_p.getPropertyClass().getJavaClassName()) && (!prop_p.getPropertyClass().isSystemProperty())
                    && (!prop_p.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)) && (!prop_p.isHidden(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)) && (clipboardProperty.getValue() != null));
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#onPasteThisMetaData(javax.servlet.http.HttpServletRequest)
     */
    public void onPasteThisMetaData(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();
        String strPropertyName = request_p.getParameter(QUERY_KEY_PROPNAME);
        OwProperty prop = (OwProperty) m_properties.get(strPropertyName);
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

    /**
     * Get current Clipboard, often used in this class.
     * @return OwClipboard the current exist
     */
    protected OwClipboard getClipboard()
    {
        return m_context.getClipboard();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#isPasteMetadataActivated()
     */
    public boolean isPasteMetadataActivated()
    {
        if (m_isSystemView || (!m_hasPasteMetaDataEnabled))
        {
            return false;
        }

        OwClipboard clipboard = getClipboard();
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

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#renderPasteAllMetadata(java.io.Writer, com.wewebu.ow.server.ui.OwEventTarget)
     */
    public void renderPasteAllMetadata(Writer w_p, OwEventTarget view_p) throws Exception
    {
        String pasteMetadataTooltip = m_context.localize("app.OwObjectPropertyView.pasteallmeta", "Paste all metadata from clipboard.");
        w_p.write("<a title=\"" + pasteMetadataTooltip + "\" href=\"" + view_p.getEventURL("PasteAllMetaData", null) + "\"><img hspace=\"5\" border=\"0\" src=\"" + m_context.getDesignURL() + "/images/OwObjectPropertyView/paste_metadata.png\"");
        w_p.write(" alt=\"");
        w_p.write(pasteMetadataTooltip);
        w_p.write("\" title=\"");
        w_p.write(pasteMetadataTooltip);
        w_p.write("\"/></a>");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPasteMetadataHandler#renderPasteMetadata(java.io.Writer, com.wewebu.ow.server.ecm.OwProperty, int, com.wewebu.ow.server.ui.OwEventTarget)
     */
    public void renderPasteMetadata(Writer w_p, OwProperty prop_p, OwEventTarget view_p) throws Exception
    {
        OwClipboard clipboard = getClipboard();

        //render paste button in row for complete OwObject
        if (clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            OwObject clipboardObject = ((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject();
            if (isPropertyPasteable(prop_p, clipboardObject))
            {
                // look for same property and check data type
                try
                {
                    String pasteMetadataTooltip = m_context.localize("app.OwObjectPropertyView.pastethismeta", "Paste property from clipboard.");
                    w_p.write("<a title=\"" + pasteMetadataTooltip + "\" href=\"" + view_p.getFormEventURL(EVENT_NAME_PASTE_THIS_META_DATA, QUERY_KEY_PROPNAME + "=" + prop_p.getPropertyClass().getClassName()) + "\">");

                    w_p.write("<img src=\"" + m_context.getDesignURL() + "/images/OwObjectPropertyView/paste_metadata.png\"");
                    w_p.write(" alt=\"");
                    w_p.write(pasteMetadataTooltip);
                    w_p.write("\" title=\"");
                    w_p.write(pasteMetadataTooltip);
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
                if (field.getFieldDefinition().getClassName().equals(prop_p.getFieldDefinition().getClassName()))
                {
                    String pasteMetadataTooltip = m_context.localize("app.OwObjectPropertyView.pastethismeta", "Paste property from clipboard.");
                    w_p.write("<a title=\"" + pasteMetadataTooltip);
                    w_p.write("\" href=\"");
                    w_p.write(view_p.getFormEventURL(EVENT_NAME_PASTE_THIS_META_DATA, QUERY_KEY_PROPNAME + "=" + prop_p.getPropertyClass().getClassName()));
                    w_p.write("\"><img src=\"");
                    w_p.write(m_context.getDesignURL());
                    w_p.write("/images/OwObjectPropertyView/paste_metadata.png");
                    w_p.write("\" alt=\"");
                    w_p.write(pasteMetadataTooltip);
                    w_p.write("\" title=\"");
                    w_p.write(pasteMetadataTooltip);
                    w_p.write("\"/></a>");
                    break; //leave the for-loop, because the right property was found
                }
            }
        }

    }
}
