package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString2;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Settings Property Combobox for Windows Position.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 * @since 3.2.0.0
 *</font></p>
 */
public class OwSettingsPropertyDisplayCombo extends OwSettingsPropertyStringCombo
{

    /** create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected List createComboSelectList(Node valueNode_p) throws Exception
    {
        List valueList = new ArrayList();

        // iterate over the child nodes
        for (Node n = valueNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals(COMBO_SELECT_ITEM_NODE_NAME))
            {
                // create list entry
                OwXMLUtil nodeWrapper = new OwStandardXMLUtil(n);

                String strDisplayName = null;
                String strValue = nodeWrapper.getSafeTextValue(null);

                if (null != strValue)
                {
                    // there is a label defined for the value
                    strDisplayName = ((OwMainAppContext) getContext()).localizeLabel(strValue);
                    if (strDisplayName.equals(strValue))
                    {
                        // no label defined for value so get display name
                        strDisplayName = nodeWrapper.getSafeStringAttributeValue(COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME, "?");
                    }
                    valueList.add(new OwDefaultComboItem(strValue, strDisplayName));
                }
            }
        }

        return valueList;
    }

    /**
     *  Display value localized 
     * @param mode_p mode
     * @return String localized value
     */
    protected String displayValue(String mode_p)
    {
        Locale locale = ((OwMainAppContext) getContext()).getLocale();

        if (mode_p.equals("DISPLAY_MODE_SPLIT"))
        {
            return OwString.localize(locale, "settingsimpl.OwSettingsPropertyDisplayMode.mode1", "Split Screen Mode - Use same screen for main window and viewer");
        }

        if (mode_p.equals("DISPLAY_MODE_FULL"))
        {
            return OwString.localize(locale, "settingsimpl.OwSettingsPropertyDisplayMode.mode4", "Full Screen Mode - Main Window on one screen and viewer on the other screen");
        }

        return mode_p;

    }

    /** 
     * create a list of values, if property is a list 
     * @return List of Objects representing values 
     */
    protected List createComboList() throws Exception
    {

        String[] DISPLAY_SETTINGS_MODE = { displayValue("DISPLAY_MODE_SPLIT"), displayValue("DISPLAY_MODE_FULL") };

        List<OwDefaultComboItem> valueList = new ArrayList<OwDefaultComboItem>();
        for (int i = 0; i < DISPLAY_SETTINGS_MODE.length; i++)
        {
            OwDefaultComboItem item = new OwDefaultComboItem(DISPLAY_SETTINGS_MODE[i], OwString.localizeLabel(getContext().getLocale(), DISPLAY_SETTINGS_MODE[i]));
            valueList.add(item);
        }

        return valueList;
    }

    /** insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
     * @param iIndex_p int Index of item if it is a list
     */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        // === read the combo select items: OwComboSelectItem
        List comboItems = createComboList();
        String selValue = null;
        if (value_p != null)
        {
            selValue = displayValue(value_p.toString());
        }

        OwComboModel comboModel = new OwDefaultComboModel(selValue == null ? null : selValue, comboItems);
        String indexId = extractIDIndex(strID_p, ITEM_PREFIX, 1);

        String indexString = "";
        if (indexId != null)
        {
            indexString = getContext().localize1("settingsimpl.OwSettingsPropertyStringCombo.index.title", " number %1", indexId);
        }
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, strID_p, null, null, new OwString2("settingsimpl.OwSettingsPropertyStringCombo.index.combo.title", "%1 %2", getDisplayName(), indexString));
        renderer.renderCombo(w_p);

    }

}