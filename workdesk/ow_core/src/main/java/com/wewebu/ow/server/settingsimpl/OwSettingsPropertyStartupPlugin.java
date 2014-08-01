package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Settings Property for the startup plugin selection.
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
public class OwSettingsPropertyStartupPlugin extends OwSettingsPropertyBaseImpl
{
    /** overridable to insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        String strCurrentPluginID = "";
        if (value_p != null)
        {
            strCurrentPluginID = value_p.toString();
        }

        // === get the main plugins IDs to build the combobox
        List mainPluginsList = ((OwMainAppContext) getContext()).getConfiguration().getAllowedPlugins(OwBaseConfiguration.PLUGINTYPE_MASTER);
        if (mainPluginsList != null)
        {
            List pluginsItems = createPluginItems(mainPluginsList);
            OwComboModel model = new OwDefaultComboModel(false, false, strCurrentPluginID, pluginsItems);
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p, null, null, null);
            renderer.renderCombo(w_p);
        }
        else
        {
            // === nothing to select
            w_p.write("<span class='OwPropertyName'>" + getContext().localize("settingsimpl.OwSettingsPropertyStartupPlugin.nopluginsdefined", "No plugins defined.") + "</span>");
        }
    }

    /**
     * Create a list of {@link OwComboItem} objects.
     * @param mainPluginsList_p - the initial list with plugins
     * @return a list of {@link OwComboItem} objects.
     * @since 3.0.0.0
     */
    private List createPluginItems(List mainPluginsList_p)
    {
        List result = new LinkedList();
        Iterator it = mainPluginsList_p.iterator();
        while (it.hasNext())
        {
            OwXMLUtil pluginDescriptorNode = (OwXMLUtil) it.next();
            String strPluginID = pluginDescriptorNode.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "undef");

            // check if master plugin contains a view or only a document
            if (null == pluginDescriptorNode.getSafeTextValue("ViewClassName", null))
            {
                continue;
            }
            String displayName = ((OwMainAppContext) getContext()).getConfiguration().getLocalizedPluginTitle(pluginDescriptorNode);
            result.add(new OwDefaultComboItem(strPluginID, displayName));
        }
        return result;
    }
}