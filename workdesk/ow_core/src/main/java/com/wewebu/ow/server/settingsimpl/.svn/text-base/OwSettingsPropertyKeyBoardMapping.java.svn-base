package com.wewebu.ow.server.settingsimpl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwPlugin;
import com.wewebu.ow.server.app.OwPluginKeyBoardMaping;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwKeyboardDescription;
import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 * Settings Property for Keyboard mappings (OwPluginKeyBoardMaping).
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
public class OwSettingsPropertyKeyBoardMapping extends OwSettingsPropertyBaseImpl
{
    private static final String ANY_OS_FAMILY = "ANY";
    private static final String OSF_PARAMETER_SUFFIX = "_osf";
    private static final String ID_PARAMETER_SUFFIX = "_id";
    private static final String KEY_PARAMETER_SUFFIX = "_key";
    private static final String CTRL_PARAMETER_SUFFIX = "_ctrl";

    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue()
    {
        // default returns zero Integer
        return new OwPluginKeyBoardMaping();
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p)
    {
        String strPluginID = request_p.getParameter(strID_p + ID_PARAMETER_SUFFIX);

        int iKeyCode = 0;
        String strKeyCode = request_p.getParameter(strID_p + KEY_PARAMETER_SUFFIX);

        if (strKeyCode.length() == 1)
        {
            // === character
            iKeyCode = strKeyCode.toUpperCase().charAt(0);
        }
        else if ((strKeyCode.length() == 2) && (strKeyCode.toLowerCase().charAt(0) == 'f'))
        {
            // === function key
            iKeyCode = OwAppContext.KEYBOARD_KEY_F1 + Integer.parseInt(strKeyCode.substring(1)) - 1;
        }
        else
        {
            // === key code
            try
            {
                iKeyCode = Integer.parseInt(strKeyCode);
            }
            catch (NumberFormatException e)
            {
                iKeyCode = '?';
            }
        }

        int iCtrlCode = Integer.parseInt(request_p.getParameter(strID_p + CTRL_PARAMETER_SUFFIX));
        String osFamilyValue = request_p.getParameter(strID_p + OSF_PARAMETER_SUFFIX);

        OwOSFamily osFamily = OwOSFamily.UNKNOWN;

        if (!ANY_OS_FAMILY.equals(osFamilyValue))
        {
            osFamily = OwOSFamily.valueOf(osFamilyValue);
        }

        return new OwPluginKeyBoardMaping(strPluginID, iKeyCode, iCtrlCode, osFamily);
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return new OwPluginKeyBoardMaping((OwPluginKeyBoardMaping) oSingleValue_p);
    }

    /** overridable to create a single value for the given node
     * @return Object with value
     */
    protected Object getSingleValue(Node valueNode_p)
    {
        // default implementation returns string from XML Text node child
        if (valueNode_p.getFirstChild() == null)
        {
            return null;
        }
        else
        {
            return new OwPluginKeyBoardMaping(valueNode_p);
        }
    }

    /** overridable, return the given value as a DOM Node for serialization
     *
     * @param valueRootNode_p root Node of the property
     * @param value_p Object to append as DOM Node
     *
     */
    protected void appendSingleValueNode(Node valueRootNode_p, Object value_p)
    {
        valueRootNode_p.appendChild(valueRootNode_p.getOwnerDocument().createTextNode(value_p.toString()));
    }

    /** overridable to insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        if (value_p == null)
        {
            return;
        }

        OwPluginKeyBoardMaping map = (OwPluginKeyBoardMaping) value_p;
        w_p.write("<div class=\"OwPluginKeyBoardMaping\" style=\"clear:both\">");
        // === render plugin
        OwPlugin selectedPlugin = null;

        w_p.write("<div>");

        List<?> pluginList = createPluginListItems();
        OwComboModel model = new OwDefaultComboModel(false, false, map.getPluginID(), pluginList);

        String commandDisplayName = getContext().localize2("OwSettingsPropertyKeyBoardMapping.commandNameIndexed", "Command name for item %1 from %2", "" + (iIndex_p + 1), getDisplayName());
        insertLabelValue(w_p, commandDisplayName, strID_p + ID_PARAMETER_SUFFIX, 0);
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p + ID_PARAMETER_SUFFIX, null, null, null);
        renderer.renderCombo(w_p);

        String selectedPluginId = model.getSelectedItem() == null ? null : model.getSelectedItem().getValue();
        if (selectedPluginId != null)
        {
            selectedPlugin = ((OwMainAppContext) getContext()).getConfiguration().getAllowedPluginInstance(selectedPluginId);
        }
        w_p.write("</div>");
        w_p.write("<div>");
        // === render Icon
        if ((selectedPlugin != null) && (selectedPlugin.getIcon() != null))
        {
            w_p.write("<img src=\"");
            w_p.write(selectedPlugin.getIcon());
            w_p.write("\"");
            String tooltip = "";
            if (selectedPlugin instanceof OwFunction)
            {
                tooltip = null == ((OwFunction) selectedPlugin).getTooltip() ? tooltip : ((OwFunction) selectedPlugin).getTooltip();
            }
            w_p.write(" alt=\"");
            w_p.write(tooltip);
            w_p.write("\" title=\"");
            w_p.write(tooltip);
            w_p.write("\"/>");
        }
        else
        {
            w_p.write("<span class=\"empty\">&nbsp;</span>");
        }
        w_p.write("</div>");
        // === render keycode
        String strKey = "";
        int keyCode = map.getKeyCode();
        if ((keyCode >= OwAppContext.KEYBOARD_KEY_F1) && (keyCode <= OwAppContext.KEYBOARD_KEY_F12))
        {
            strKey = "F" + String.valueOf(keyCode - OwAppContext.KEYBOARD_KEY_F1 + 1);
        }
        else if (keyCode < 100)
        {
            if (keyCode >= 32)
            {
                strKey += (char) keyCode;
            }
        }
        else
        {
            strKey = String.valueOf(keyCode);
        }
        w_p.write("<div>");
        String keyDisplayName = getContext().localize2("OwSettingsPropertyKeyBoardMapping.keyIndexed", "Key parameter for item %1 from %2", "" + (iIndex_p + 1), getDisplayName());
        insertLabelValue(w_p, keyDisplayName, strID_p + "_key", 0);
        w_p.write("<input class=\"OwInputControl\" size=\"3\" type=\"text\" name=\"");
        w_p.write(strID_p);
        w_p.write("_key\" id='" + strID_p + "_key' value=\"");
        w_p.write(strKey);
        w_p.write("\"></div>");

        w_p.write("<div>");
        // === render ctrl button
        List controlItems = createControlItems();
        OwComboModel ctrlModel = new OwDefaultComboModel(false, false, "" + map.getCtrlCode(), controlItems);
        String ctrlDisplayName = getContext().localize2("OwSettingsPropertyKeyBoardMapping.ctrlIndexed", "Control parameter for item %1 from %2", "" + (iIndex_p + 1), getDisplayName());
        insertLabelValue(w_p, ctrlDisplayName, strID_p + CTRL_PARAMETER_SUFFIX, 0);
        OwComboboxRenderer ctrlRenderer = ((OwMainAppContext) getContext()).createComboboxRenderer(ctrlModel, strID_p + CTRL_PARAMETER_SUFFIX, null, null, null);
        ctrlRenderer.renderCombo(w_p);

        //render OS family
        List osItems = createOSItems();
        String value = ANY_OS_FAMILY;
        if (OwOSFamily.UNKNOWN != map.getOSFamily())
        {
            value = map.getOSFamily().name();
        }
        OwComboModel osModel = new OwDefaultComboModel(false, false, value, osItems);
        OwComboboxRenderer osRenderer = ((OwMainAppContext) getContext()).createComboboxRenderer(osModel, strID_p + OSF_PARAMETER_SUFFIX, null, null, null);
        String osDisplayName = getContext().localize2("OwSettingsPropertyKeyBoardMapping.osIndexed", "Operating System for item %1 from %2", "" + (iIndex_p + 1), getDisplayName());
        insertLabelValue(w_p, osDisplayName, strID_p + OSF_PARAMETER_SUFFIX, 0);
        osRenderer.renderCombo(w_p);

        w_p.write("</div>");
        w_p.write("</div>");

    }

    /**
     * Create control items.
     * @return a list of {@link OwComboItem} objects
     * @throws Exception
     * @since 3.0.0.0
     */
    private List createControlItems() throws Exception
    {
        List result = new LinkedList();
        result.add(createCtrlItem(OwAppContext.KEYBOARD_CTRLKEY_NONE));
        result.add(createCtrlItem(OwAppContext.KEYBOARD_CTRLKEY_ALT));
        result.add(createCtrlItem(OwAppContext.KEYBOARD_CTRLKEY_CTRL));
        result.add(createCtrlItem(OwAppContext.KEYBOARD_CTRLKEY_SHIFT));
        result.add(createCtrlItem(OwAppContext.KEYBOARD_CTRLKEY_META));

        return result;
    }

    private List createOSItems() throws Exception
    {
        List result = new LinkedList();
        result.add(createOSItem(OwOSFamily.WINDOWS));
        result.add(createOSItem(OwOSFamily.OS_X));
        result.add(createOSItem(OwOSFamily.UNKNOWN));
        return result;
    }

    /**
     * Create a list of {@link OwComboItem} objects from plugins
     * @return a list of {@link OwComboItem} objects.
     * @since 3.0.0.0
     */
    private List createPluginListItems()
    {
        List result = new LinkedList();
        Iterator it = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunctionPlugins().iterator();
        addListItemsFromPlugins(result, it);

        // === render record plugins
        it = ((OwMainAppContext) getContext()).getConfiguration().getRecordFunctionPlugins().iterator();
        addListItemsFromPlugins(result, it);

        // === render master plugins
        it = ((OwMainAppContext) getContext()).getConfiguration().getMasterPlugins(true).iterator();
        addListItemsFromPlugins(result, it);

        return result;
    }

    /**
     * Helper method, to create and add items for plugins.
     * @param items_p - the existing list of items
     * @param pluginsIterator_p - the plugins iterator
     * @since 3.0.0.0
     */
    private void addListItemsFromPlugins(List items_p, Iterator pluginsIterator_p)
    {
        while (pluginsIterator_p.hasNext())
        {
            OwPlugin plug = (OwPlugin) pluginsIterator_p.next();
            String strID = plug.getPluginID();
            String displayValue = plug.getPluginTypeDisplayName() + ": " + plug.getPluginTitle();
            OwComboItem item = new OwDefaultComboItem(strID, displayValue);
            items_p.add(item);
        }
    }

    /**
     * Create a combo item.
     * @param iCtrlCode_p the keyCode
     * @return a {@link OwComboItem} object.
     * @throws Exception
     * @since 3.0.0.0
     */
    private OwComboItem createCtrlItem(int iCtrlCode_p) throws Exception
    {
        OwKeyboardDescription keyboardDescription = OwKeyboardDescription.getInstance();
        return new OwDefaultComboItem("" + iCtrlCode_p, keyboardDescription.getCtrlDescription(getContext().getLocale(), iCtrlCode_p));
    }

    private OwComboItem createOSItem(OwOSFamily osFamily) throws Exception
    {
        String displayName = (osFamily == OwOSFamily.UNKNOWN ? ANY_OS_FAMILY : osFamily.getFamilyName());
        String value = (osFamily == OwOSFamily.UNKNOWN ? ANY_OS_FAMILY : osFamily.name());
        return new OwDefaultComboItem(value, displayName);
    }

    @Override
    public Object getValue()
    {
        return super.getValue();
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        w_p.write(getDisplayName() + ":");
    }

    @Override
    protected void insertLabelValue(Writer w_p, String displayName, String strID_p, int iIndex_p) throws IOException
    {
        w_p.write("<label class=\"accessibility\" for=\"" + strID_p + "\">");
        w_p.write(displayName + ":");
        w_p.write("</label>");
    }
}