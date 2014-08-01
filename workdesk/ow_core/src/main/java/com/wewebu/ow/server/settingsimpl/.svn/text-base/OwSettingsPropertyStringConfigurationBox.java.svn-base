package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A List of String properties, which can be configured via two combo boxes.
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
public class OwSettingsPropertyStringConfigurationBox extends OwSettingsPropertyBaseImpl
{
    /** web key of the available attributes */
    private static final String WEBKEY_AVAILABLE_ATTRIBUTES = "available_";
    /** web key of the selected attributes */
    private static final String WEBKEY_SELECTED_ATTRIBUTES = "selected_";
    /** node name of a combo select item for list properties */
    protected static final String COMBO_SELECT_ITEM_NODE_NAME = "comboselect";

    /** attribute name of a combo select item display name for list properties */
    protected static final String COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME = "displayname";

    /** the combobox size */
    private static final int BOX_SIZE = 10;
    /** keep the latest selected Index on the right combobox */
    private int m_selectedIndex;
    /** attribute name to display label mapping */
    Map m_attributesDisplayNameMap;
    /** available attributes read from the plugin descriptor */
    List m_availableAttributes;

    /** 
     * @param w_p Writer
     */
    public void insertFormField(Writer w_p) throws Exception
    {
        /** list of the selected attributes */
        List selectedAttributes = getSelectedPropertiesList();

        /** get the currently available attributes */
        List currentlyAvailableAttributes = createCurrentlyAvailableAttributes(selectedAttributes);

        /** get the map of the labels to be displayed */
        Map displayNameMap = this.getAttributeDisplayNameMap();

        w_p.write("<TABLE class='OwTaskGeneralText'>"); // table 4 cols, 2 rows
        // render the titles
        w_p.write("<TR>\n");
        String displayName = getDisplayName();
        String availableId = WEBKEY_AVAILABLE_ATTRIBUTES + String.valueOf(this.hashCode());
        String selectedId = WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode());
        w_p.write("<TD class='OwSelectBoxTitle'>");
        w_p.write("<label for='" + availableId + "'>");
        w_p.write(getContext().localize1("settingsimpl.OwSettingsPropertyStringConfigurationBox.available.label", "Available &nbsp; %1", displayName));
        w_p.write("</label>");
        w_p.write("</TD>\n");
        w_p.write("<TD>&nbsp;</TD>\n");
        w_p.write("<TD class='OwSelectBoxTitle'>");
        w_p.write("<label for='" + selectedId + "'>");
        w_p.write(getContext().localize1("settingsimpl.OwSettingsPropertyStringConfigurationBox.selected.label", "Selected &nbsp; %1", displayName));
        w_p.write("</label>");
        w_p.write("</TD>\n");
        w_p.write("<TD>&nbsp;</TD>\n");
        w_p.write("</TR>\n");

        // render the select boxes
        w_p.write("<TR>\n");

        // render first select box

        w_p.write("<TD><SELECT class='OwInputfield_long' size='" + BOX_SIZE + "' name='" + availableId + "' id='" + availableId + "'>\n");
        for (int pos = 0; pos < currentlyAvailableAttributes.size(); pos++)
        {
            String attribute = (String) currentlyAvailableAttributes.get(pos);
            String attributeLabel = (String) displayNameMap.get(attribute);
            w_p.write("<OPTION value='" + attribute + "'>");
            w_p.write(attributeLabel != null ? attributeLabel : attribute);
            w_p.write("</OPTION>\n");
        }
        w_p.write("</SELECT>");
        w_p.write("</TD>\n");

        // render the toggle buttons
        String submitLeftShiftFunction = this.getFormEventURL("ShiftLeft", null);

        String submitRightShiftFunction = this.getFormEventURL("ShiftRight", null);

        w_p.write("<TD class='shiftButtons' width='60pt'>");
        String shiftLeftTooltip = getContext().localize("plug.owtask.ui.OwDisplayColumnsSettingsView.shiftleft", "Shift property left");
        w_p.write("<a href=\"" + submitLeftShiftFunction + "\"><img src='" + getContext().getDesignURL() + "/images/plug/pointer_left.png' width='32' height='32' border='0' alt='" + shiftLeftTooltip + "' title='" + shiftLeftTooltip + "'/></a>");
        w_p.write("<br>");
        w_p.write("<br>");
        String shiftRightTooltip = getContext().localize("plug.owtask.ui.OwDisplayColumnsSettingsView.shiftright", "Shift property right");
        w_p.write("<a href=\"" + submitRightShiftFunction + "\"><img src='" + getContext().getDesignURL() + "/images/plug/pointer_right.png' width='32' height='32' border='0' alt='" + shiftRightTooltip + "' title='" + shiftRightTooltip + "'/></a>");
        w_p.write("</TD>\n");

        // render the second select box
        w_p.write("<TD><SELECT id='" + selectedId + "' class='OwInputfield_long' size='" + BOX_SIZE + "' name='" + selectedId + "'>\n");
        for (int pos = 0; pos < selectedAttributes.size(); pos++)
        {
            String attribute = (String) selectedAttributes.get(pos);
            String attributeLabel = "";
            if (attribute != null)
            {
                attributeLabel = (String) displayNameMap.get(attribute);
            }
            w_p.write("<OPTION value='" + attribute + "' ");
            if (m_selectedIndex == pos)
            {
                w_p.write("selected");
            }
            w_p.write(">");
            w_p.write(attributeLabel != null ? attributeLabel : attribute);
            w_p.write("</OPTION>\n");
        }
        w_p.write("</SELECT>");
        w_p.write("</TD>\n");

        // render the up and down buttons
        String submitMoveUpFunction = this.getFormEventURL("MoveUp", null);
        String submitMoveDownFunction = this.getFormEventURL("MoveDown", null);

        w_p.write("<TD>");
        String moveUpTooltip = getContext().localize("plug.owtask.ui.OwDisplayColumnsSettingsView.shiftup", "Shift property up");
        w_p.write("<a href=\"" + submitMoveUpFunction + "\"><img src='" + getContext().getDesignURL() + "/images/plug/pointer_up.png' width='32' height='32' border='0' alt='" + moveUpTooltip + "' title='" + moveUpTooltip + "'></a>");
        w_p.write("<BR>");
        w_p.write("<BR>");
        String moveDownTooltip = getContext().localize("plug.owtask.ui.OwDisplayColumnsSettingsView.shiftdown", "Shift property down");
        w_p.write("<a href=\"" + submitMoveDownFunction + "\"><img src='" + getContext().getDesignURL() + "/images/plug/pointer_down.png' width='32' height='32' border='0' alt='" + moveDownTooltip + "' title='" + moveDownTooltip + "'></a>");

        w_p.write("</TD>\n");

        w_p.write("</TR>\n");
        w_p.write("</TABLE>\n");
    }

    /**
     * create currently available attributes by subtracting the selected attributes from the available ones
    * @param selectedAttributes_p
    * @return a {@link List}
    * @throws Exception 
    */
    private List createCurrentlyAvailableAttributes(List selectedAttributes_p) throws Exception
    {
        List availableAttributes = getAvailableAttributeList();
        List currentlyAvailableAttributes = new ArrayList();
        for (Iterator iter = availableAttributes.iterator(); iter.hasNext();)
        {
            String attr = (String) iter.next();
            if (!selectedAttributes_p.contains(attr))
            {
                currentlyAvailableAttributes.add(attr);
            }
        }
        return currentlyAvailableAttributes;
    }

    /**
     * get the map which maps attribute name to display label 
     * @return a {@link Map}
     * @throws Exception 
     */
    private Map getAttributeDisplayNameMap() throws Exception
    {
        if (null == m_availableAttributes)
        {
            m_attributesDisplayNameMap = new HashMap();

            /** list of the available attributes */
            m_availableAttributes = createAvailablePropertiesList(m_attributesDisplayNameMap);
        }

        return m_attributesDisplayNameMap;
    }

    /**
     * get available attribute list
     * @return a {@link List}
     * @throws Exception 
     */
    private List getAvailableAttributeList() throws Exception
    {
        if (null == m_availableAttributes)
        {
            m_attributesDisplayNameMap = new HashMap();

            /** list of the available attributes */
            m_availableAttributes = createAvailablePropertiesList(m_attributesDisplayNameMap);
        }

        return m_availableAttributes;

    }

    /** create a list of values, if property is a list 
     * @param attributesMap_p a {@link Map}
     * @return List of Objects representing values 
     */
    protected List createAvailablePropertiesList(Map attributesMap_p) throws Exception
    {
        List valueList = new ArrayList();

        // iterate over the child nodes
        for (Node n = getPropertyDefinitionNode().getFirstChild(); n != null; n = n.getNextSibling())
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
                        //check if display name from XML is a localized item
                        strDisplayName = ((OwMainAppContext) getContext()).localize(strDisplayName, strDisplayName);
                    }
                    attributesMap_p.put(strValue, strDisplayName);
                    valueList.add(strValue);
                }
            }
        }

        return valueList;
    }

    /** create a list of values, if property is a list 
     * @return List of Objects representing values 
     */
    protected List getSelectedPropertiesList() throws Exception
    {
        return (List) getValue();
    }

    /**
     * event called when user clicked Shift Left Button
     * 
     * @param request_p HttpServletRequest
     */
    public void onShiftLeft(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        // update the entries of the right box
        List selectedList = this.getSelectedPropertiesList();
        selectedList.remove(selectedAttribute);

        // deselect the currently selected attribute of the right box
        m_selectedIndex = -1;

    }

    /**
     * event called when user clicked Shift Right Button
     * 
     * @param request_p  HttpServletRequest
     */
    public void onShiftRight(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_AVAILABLE_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        //      update the entries of the right box
        List selectedList = this.getSelectedPropertiesList();
        selectedList.add(selectedAttribute);

        // move the selection of the right box to the last entry
        m_selectedIndex = selectedList.size() - 1;

    }

    /**
     * event called when user clicked Move Up Button
     * 
     * @param request_p  HttpServletRequest
     */
    public void onMoveUp(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        // update the entries of the right box
        List selectedList = this.getSelectedPropertiesList();

        int index = selectedList.indexOf(selectedAttribute);
        if (index == 0)
        {
            // first position in list cannot be moved up
            return;
        }
        selectedList.remove(index);
        selectedList.add(index - 1, selectedAttribute);

        // update the selection of the right box
        m_selectedIndex = index - 1;

    }

    /**
     * event called when user clicked Move Down Button
     * 
     * @param request_p HttpServletRequest
     */
    public void onMoveDown(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        //      update the entries of the right box
        List selectedList = this.getSelectedPropertiesList();
        int index = selectedList.indexOf(selectedAttribute);
        if (index == (selectedList.size() - 1))
        {
            // last position in list cannot be moved down
            return;
        }
        selectedList.remove(index);
        selectedList.add(index + 1, selectedAttribute);

        //      update the selection of the right box
        m_selectedIndex = index + 1;
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
        //nothing to do because the selected attributes are changed "on the fly"
        return true;
    }

}