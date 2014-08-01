package com.wewebu.ow.server.plug.owaddmultidocuments.cfg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Configuration helper class for SecondaryTypesProcessor. 
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
 *@since 4.1.1.0
 */
public class OwSecondaryTypesProcessorCfg
{
    public static final String EL_ASSOCIATION = "Association";

    @SuppressWarnings("unchecked")
    public static Map<String, String> getMapping(OwXMLUtil configuration)
    {
        Iterator<Node> itMap = configuration.getSafeNodeList().iterator();
        HashMap<String, String> retMap = new HashMap<String, String>();
        while (itMap.hasNext())
        {
            Node mapDef = itMap.next();
            String value = mapDef.getTextContent();
            if (value != null)
            {
                String[] map = value.split("=");
                if (map.length == 2)
                {
                    retMap.put(map[0], map[1]);
                }
            }
        }
        return retMap;
    }
}
