package com.wewebu.ow.server.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwKeySetting;
import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting;
import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode;
import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 * Single keyboard mapping for a plugin.
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
public class OwPluginKeyBoardMaping
{
    private static final Logger LOG = OwLogCore.getLogger(OwPluginKeyBoardMaping.class);

    private static final String CMD_STRING_SEPARATOR = ",";

    public static Map<String, OwKeySetting> toKeySettings(Collection<OwPluginKeyBoardMaping> mappings)
    {
        Map<String, List<OwOSFamilyKeyCode>> keyCodes = new HashMap<String, List<OwOSFamilyKeyCode>>();
        for (OwPluginKeyBoardMaping mapping : mappings)
        {
            OwOSFamilyKeyCode keyCode = new OwOSFamilyKeyCode(mapping.getOSFamily(), mapping.getKeyCode(), mapping.getCtrlCode());
            String pluginId = mapping.getPluginID();

            List<OwOSFamilyKeyCode> codes = keyCodes.get(pluginId);
            if (codes == null)
            {
                codes = new ArrayList<OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode>();
                keyCodes.put(pluginId, codes);
            }

            codes.add(keyCode);
        }

        Map<String, OwKeySetting> keySettings = new HashMap<String, OwKeySetting>();
        Set<Entry<String, List<OwOSFamilyKeyCode>>> codeEntries = keyCodes.entrySet();
        for (Entry<String, List<OwOSFamilyKeyCode>> pluginCodes : codeEntries)
        {
            OwOSFamilyKeyCode[] codesArray = pluginCodes.getValue().toArray(new OwOSFamilyKeyCode[] {});
            String pluginId = pluginCodes.getKey();
            keySettings.put(pluginId, new OwOSFamilyKeyCodeSetting(pluginId, codesArray));
        }

        return keySettings;
    }

    private String m_cmdstring;

    public OwPluginKeyBoardMaping(OwPluginKeyBoardMaping map_p)
    {
        init(map_p.toString());
    }

    public OwPluginKeyBoardMaping(Node valueNode_p)
    {
        init(valueNode_p.getFirstChild().getNodeValue());
    }

    public OwPluginKeyBoardMaping()
    {

    }

    public OwPluginKeyBoardMaping(String strPluginID, int iKeyCode, int iCtrlCode)
    {
        this(strPluginID, iKeyCode, iCtrlCode, OwOSFamily.UNKNOWN);
    }

    public OwPluginKeyBoardMaping(String strPluginID, int iKeyCode, int iCtrlCode, OwOSFamily osFamily)
    {
        String family = (OwOSFamily.UNKNOWN != osFamily ? CMD_STRING_SEPARATOR + osFamily.name() : "");
        m_cmdstring = String.valueOf(iKeyCode) + CMD_STRING_SEPARATOR + String.valueOf(iCtrlCode) + CMD_STRING_SEPARATOR + strPluginID + family;
    }

    public String toString()
    {
        return m_cmdstring == null ? "" : m_cmdstring;
    }

    private void init(String strValue_p)
    {
        m_cmdstring = strValue_p;
    }

    public String getPluginID()
    {
        if (m_cmdstring != null)
        {
            String[] split = m_cmdstring.split(CMD_STRING_SEPARATOR);
            if (split.length > 2)
            {
                return split[2];
            }
        }
        return "";
    }

    public int getKeyCode()
    {
        if (m_cmdstring != null)
        {
            String[] split = m_cmdstring.split(CMD_STRING_SEPARATOR);
            if (split.length > 0)
            {
                try
                {
                    return Integer.parseInt(split[0]);
                }
                catch (NumberFormatException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Propvided value is not a number, value = " + split[0], e);
                    }
                }
            }
        }
        return 0;
    }

    public int getCtrlCode()
    {
        if (m_cmdstring != null)
        {
            String[] split = m_cmdstring.split(CMD_STRING_SEPARATOR);
            if (split.length > 1)
            {
                try
                {
                    return Integer.parseInt(split[1]);
                }
                catch (NumberFormatException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Propvided value is not a number, value = " + split[1], e);
                    }
                }
            }
        }
        return 0;
    }

    public OwOSFamily getOSFamily()
    {
        if (m_cmdstring == null)
        {
            return OwOSFamily.UNKNOWN;
        }
        StringTokenizer tokens = new StringTokenizer(m_cmdstring, CMD_STRING_SEPARATOR);
        tokens.nextToken();
        tokens.nextToken();
        if (tokens.hasMoreTokens())
        {
            tokens.nextToken();
            if (tokens.hasMoreTokens())
            {
                String osFamilyValue = tokens.nextToken();
                return OwOSFamily.from(osFamilyValue);
            }
            return OwOSFamily.UNKNOWN;
        }
        else
        {
            return OwOSFamily.UNKNOWN;
        }

    }

}
