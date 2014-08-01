package com.wewebu.ow.server.dmsdialogs.views.classes;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Only used in conjunction with {@link OwObjectClassSelectionCfg}.
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
 *@since 4.1.0.0
 */
public class OwRootClassCfg
{
    private String name;
    private boolean includeSubclasses = false;

    protected OwRootClassCfg(String name, boolean includeSubclasses)
    {
        this.name = name;
        this.includeSubclasses = includeSubclasses;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the includeSubclasses
     */
    public boolean isIncludeSubclasses()
    {
        return includeSubclasses;
    }

    /**
     * @param xmlClass
     * @return an instance of myself configured from this XML snippet.
     * @throws OwConfigurationException 
     */
    public static OwRootClassCfg fromXml(OwXMLUtil xmlClass) throws OwConfigurationException
    {
        String className = xmlClass.getSafeTextValue(null);
        if (null == className)
        {
            OwString errMsg = new OwString("plug.owadddocument.cfg.missingCLassName", "Configuration error <ObjectClassSelection>, the object class is missing.");
            throw new OwConfigurationException(errMsg);
        }

        String includeSubclassesStr = xmlClass.getSafeStringAttributeValue(OwObjectClassSelectionCfg.ATT_INCLUDE_SUB_CLASS, "false");
        boolean includeSubclasses = Boolean.parseBoolean(includeSubclassesStr);

        return new OwRootClassCfg(className, includeSubclasses);
    }
}