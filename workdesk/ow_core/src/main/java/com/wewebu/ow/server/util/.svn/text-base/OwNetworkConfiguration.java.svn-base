package com.wewebu.ow.server.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Helper class to wrap the &lt;EcmAdapter&gt; element from owbootstrap.xml.
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
 * @since 3.2.0.0
 */
public class OwNetworkConfiguration
{
    public static final String EL_OBJECT_CLASS = "ObjectClass";
    public static final String EL_CREATION_INITIAL_VALUES = "CreationInitialValues";
    public static final String EL_PREFERREDPROPERTYTYPE = "PreferredPropertyType";
    public static final String EL_PROPERTY = "Property";

    public static final String EL_OWAPPLICATIONOBJECTBASEDIR = "OwApplicationObjectBaseDir";

    public static final String EL_VIRTUALFOLDERSCONTAINER = "VirtualFoldersContainer";

    public static final String EL_AUTHENTICATION = "Authentication";

    private static final Logger LOG = OwLogCore.getLogger(OwNetworkConfiguration.class);

    private OwXMLUtil config;
    private Map<String, OwStandardXMLUtil> m_CreateionInitialValuesConfigMap;

    /**
     * Will build this instance from the provided XML.
     * @param config the &lt;EcmAdapter&gt; element. 
     */
    public OwNetworkConfiguration(OwXMLUtil config)
    {
        this.config = config;
    }

    /**
     * Get a map between an object class name and the OwXMLUtil of its initial value configuration
     * @return a map object mapping a String (object class name) to a OwXMLUtil (its configuration node for initial values)
     */
    @SuppressWarnings("unchecked")
    private Map<String, OwStandardXMLUtil> getCreationInitialValuesConfigMap()
    {
        if (m_CreateionInitialValuesConfigMap == null)
        {
            // build it from config
            m_CreateionInitialValuesConfigMap = new HashMap<String, OwStandardXMLUtil>();
            // get the subnodes of <CreationInitialValues>
            List<Element> civSubnodes = getConfigNode().getSafeNodeList(EL_CREATION_INITIAL_VALUES);
            for (Element objectClassConfigElem : civSubnodes)
            {
                String className = OwXMLDOMUtil.getSafeStringAttributeValue(objectClassConfigElem, "name", null);
                if (objectClassConfigElem.getTagName().equals(EL_OBJECT_CLASS) && (className != null))
                {
                    try
                    {
                        m_CreateionInitialValuesConfigMap.put(className, new OwStandardXMLUtil(objectClassConfigElem));
                    }
                    catch (Exception e)
                    {
                        LOG.error("Cannot create OwStandardXMLUtil for CreateionInitialValuesConfigMap.", e);
                    }
                }
            }
        }
        return (m_CreateionInitialValuesConfigMap);
    }

    /**
     * Get the configuration node defining the initial values for new objects of a specific object class.
     * @param objectClassName_p The object class name to retrieve the initial values for
     * @return An OwXMUtil of the configuration node
     */
    public OwXMLUtil getCreationInitialValuesConfig(String objectClassName_p)
    {
        Map<String, OwStandardXMLUtil> civcm = getCreationInitialValuesConfigMap();
        if (civcm != null)
        {
            return civcm.get(objectClassName_p);
        }
        return (null);
    }

    /**
     * Return the current configuration node (owbootstrap.xml).
     * @return OwXMLUtil
     */
    public OwXMLUtil getConfigNode()
    {
        return this.config;
    }

    /**
     * 
     * @param defaultLocation_p
     * @return the configured OwApplicationObjectBaseDir value or the given default value 
     *         if no OwApplicationObjectBaseDir configuration is found 
     * @since 4.0.0.0 

     */
    public String getApplicationObjectBaseDir(String defaultLocation_p)
    {
        return config.getSafeTextValue(EL_OWAPPLICATIONOBJECTBASEDIR, defaultLocation_p);
    }

    /**
     * 
     * @param defaultContainer_p
     * @return the configured VirtualFoldersContainer value or the given default value 
     *         if no VirtualFoldersContainer configuration is found 
     * @since 4.0.0.0 
     */
    public String getVirtualFoldersContainer(String defaultContainer_p)
    {
        return config.getSafeTextValue(EL_VIRTUALFOLDERSCONTAINER, defaultContainer_p);
    }

    /**
     * Get a helper class which contains the configuration for authentication node.
     * @return {@link OwAuthenticationConfiguration} or null if none found
     * @since 4.1.0.0
     */
    public OwAuthenticationConfiguration getAuthenticationConfiguration()
    {
        OwXMLUtil util = null;
        try
        {
            util = getConfigNode().getSubUtil(EL_AUTHENTICATION);
        }
        catch (Exception e)
        {
            String msg = "Could not find/retrieve \"Authentication\" configuration.";
            if (LOG.isDebugEnabled())
            {
                LOG.warn(msg, e);
            }
            else
            {
                LOG.warn(msg);
            }
        }

        if (util != null)
        {
            return new OwAuthenticationConfiguration(util);
        }
        else
        {
            return null;
        }
    }
}
