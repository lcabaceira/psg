package com.wewebu.ow.server.settingsimpl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Settings Property for Clipboard behavior.
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
public class OwSettingsPropertyClipboardBehavior extends OwSettingsPropertyStringCombo
{
    /**clipboard behavior replace - replace the content of the clipboard each time copy/cut plugins are invoked*/
    public static final String OW_CLIPBOARD_BEHAVIOR_REPLACE = "OW_CLIPBOARD_BEHAVIOR_REPLACE";
    /**clipboard behavior append - append items to the content of the clipboard each time copy/cut plugins are invoked*/
    public static final String OW_CLIPBOARD_BEHAVIOR_APPEND = "OW_CLIPBOARD_BEHAVIOR_APPEND";
    /**behavior options*/
    private static String[] CLIPBOARD_BEHAVIOR_OPTIONS = { OW_CLIPBOARD_BEHAVIOR_APPEND, OW_CLIPBOARD_BEHAVIOR_REPLACE };

    /** 
     * create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected List createComboSelectList(Node valueNode_p) throws Exception
    {
        List valueList = new ArrayList();
        for (int i = 0; i < CLIPBOARD_BEHAVIOR_OPTIONS.length; i++)
        {
            OwDefaultComboItem item = new OwDefaultComboItem(CLIPBOARD_BEHAVIOR_OPTIONS[i], OwString.localizeLabel(getContext().getLocale(), CLIPBOARD_BEHAVIOR_OPTIONS[i]));
            valueList.add(item);
        }

        return valueList;
    }
}