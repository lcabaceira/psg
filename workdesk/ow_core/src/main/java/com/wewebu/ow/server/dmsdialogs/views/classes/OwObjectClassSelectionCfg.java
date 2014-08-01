package com.wewebu.ow.server.dmsdialogs.views.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Configuration to be used for selecting the class of a newly created document.
 * Basically this is a filter for the classes to be shown to the user so he/she can choose upon.
 * It gets its data from the plugin's XML configuration.
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
public class OwObjectClassSelectionCfg
{
    private static final Logger LOG = Logger.getLogger(OwObjectClassSelectionCfg.class);

    /**
     * The root XML element for this configuration. 
     */
    public static final String EL_OBJECT_CLASS_SELECTION = "ObjectClassSelection";
    /**
     * The XML element to be used for each entry in the configuration.
     */
    public static final String EL_OBJECT_CLASS = "ObjectClass";
    public static final String ATT_INCLUDE_SUB_CLASS = "includeSubClasses";

    private Map<String, OwRootClassCfg> rootClasses = new HashMap<String, OwRootClassCfg>();

    private OwObjectClassSelectionCfg()
    {

    }

    /**
     * @return the rootClasses
     */
    public Set<OwRootClassCfg> getRootClasses()
    {
        return new HashSet<OwRootClassCfg>(rootClasses.values());
    }

    /**
     * @param rootClassName a class name
     * @return null if this class was not specified as a root class in this configuration
     */
    public OwRootClassCfg get(String rootClassName)
    {
        return this.rootClasses.get(rootClassName);
    }

    /**
     * @return true if only one root class was defined and it's {@link OwRootClassCfg#isIncludeSubclasses()} is false.
     */
    public boolean hasDefaultClass()
    {
        return null != getDefaultClass();
    }

    /**
     * @return null of there is no root class defined or there is more than 1 root class defined.
     * @see #hasDefaultClass() 
     */
    public OwRootClassCfg getDefaultClass()
    {
        if (1 == getRootClasses().size())
        {
            OwRootClassCfg defaultRootClass = getRootClasses().toArray(new OwRootClassCfg[] {})[0];
            if (!defaultRootClass.isIncludeSubclasses())
            {
                return defaultRootClass;
            }
        }
        return null;
    }

    /**
     * Factory method to create a configuration with single/default class name. 
     * @param defaultClassName String symbolic name or id of the default name
     * @return OwObjectClassSelectionCfg
     * @since 4.1.1.0
     */
    public static OwObjectClassSelectionCfg createSingleClassConfiguration(String defaultClassName)
    {
        OwObjectClassSelectionCfg cfg = new OwObjectClassSelectionCfg();
        addRootSelectionClass(cfg, defaultClassName, false);
        return cfg;
    }

    /**
     * Creates an instance of this configuration from the plugin's XML configuration.
     * <p>
     * Ex. <br/>
     * <code>
     * &lt;ObjectClasses&gt;<br/>
     *     &nbsp;&nbsp;&lt;ObjectClass includeSubClasses="true"&gt;class_name&lt;/ObjectClass&gt;<br/>
     * &lt;/ObjectClasses&gt;<br/>
     * </code>
     * </p>
     * @param pluginXMLCfg the plugin's XML configuration. 
     * @return an instance of myself.
     * @throws OwConfigurationException 
     */
    @SuppressWarnings("unchecked")
    public static OwObjectClassSelectionCfg fromPluginXml(OwXMLUtil pluginXMLCfg) throws OwConfigurationException
    {

        OwXMLUtil objectClassesXML;
        try
        {
            objectClassesXML = pluginXMLCfg.getSubUtil(OwObjectClassSelectionCfg.EL_OBJECT_CLASS_SELECTION);
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            OwString errMsg = new OwString("plug.owadddocument.cfg.missingCLassName", "Configuration error <ObjectClassSelection>, the object class is missing.");
            throw new OwConfigurationException(errMsg, e);
        }

        String strParentObjectClass = pluginXMLCfg.getSafeTextValue("ObjectClassParent", null);
        String strClassName = pluginXMLCfg.getSafeTextValue("ObjectClass", null);

        if (null == objectClassesXML && null == strParentObjectClass && null == strClassName)
        {
            return OwObjectClassSelectionCfg.createEmptyConfiguration();
        }

        if (null != strParentObjectClass || null != strClassName)
        {
            // show a warning
            LOG.warn("Usage of the <ObjectClassParent> and <ObjectClass> is deprecated. Please use the new <ObjectClassSelection>...</ObjectClassSelection> configuration element.");
        }

        OwObjectClassSelectionCfg cfg = new OwObjectClassSelectionCfg();
        if (null != objectClassesXML)
        {
            List<OwXMLUtil> xmlClasses = objectClassesXML.getSafeUtilList(OwObjectClassSelectionCfg.EL_OBJECT_CLASS);
            for (OwXMLUtil xmlClass : xmlClasses)
            {
                OwRootClassCfg rootClassCfg = OwRootClassCfg.fromXml(xmlClass);
                addRootClass(cfg, rootClassCfg);
            }
        }
        else
        {
            if (null != strClassName)
            {
                OwRootClassCfg rootClassCfg = new OwRootClassCfg(strClassName, false);
                addRootClass(cfg, rootClassCfg);
            }
            else if (null != strParentObjectClass)
            {
                OwRootClassCfg rootClassCfg = new OwRootClassCfg(strParentObjectClass, true);
                addRootClass(cfg, rootClassCfg);
            }
        }

        return cfg;
    }

    private static void addRootClass(OwObjectClassSelectionCfg cfg, OwRootClassCfg rootClassCfg)
    {
        cfg.rootClasses.put(rootClassCfg.getName(), rootClassCfg);
    }

    /**
     * Factory to create an empty OwObjectClassSelectionCfg instance.
     * @return OwObjectClassSelectionCfg
     * @since 4.1.1.0
     */
    public static OwObjectClassSelectionCfg createEmptyConfiguration()
    {
        return new OwObjectClassSelectionCfg();
    }

    /**
     * Add new root class configuration, and define if subclasses should be also visible.
     * @param cfg OwObjectClassSelectionCfg
     * @param rootClassName String
     * @param includeSubClasses boolean
     * @since 4.1.1.0
     * @see #createEmptyConfiguration()
     */
    public static void addRootSelectionClass(OwObjectClassSelectionCfg cfg, String rootClassName, boolean includeSubClasses)
    {
        OwRootClassCfg rootCfg = new OwRootClassCfg(rootClassName, includeSubClasses);
        addRootClass(cfg, rootCfg);
    }
}