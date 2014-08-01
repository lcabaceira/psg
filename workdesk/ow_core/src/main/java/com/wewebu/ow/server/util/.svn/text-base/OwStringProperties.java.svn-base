package com.wewebu.ow.server.util;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Class for the string text mapped by key.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwStringProperties
{
    private static final String[] DEFAULT_RESOURCE_NAMES = new String[] { "oecmlocalize", "zioilocalize", "oecmcustomlocalize" };

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStringProperties.class);

    private static OwStringProperties instance;

    private ConcurrentHashMap<String, Hashtable> m_locals = new ConcurrentHashMap<String, Hashtable>();

    private String[] resourceNames;

    // load the properties from resource file
    private OwStringProperties()
    {
        this.resourceNames = DEFAULT_RESOURCE_NAMES;
    }

    /** get the names of the resource files
     * 
     * @return String[]
     */
    private String[] getResourceNames()
    {
        return resourceNames;
    }

    /** get the properties map with locale strings from cash
     * 
     * @param local_p Locale
     * @throws OwDebugModeException 
     */
    public Hashtable getProperties(Locale local_p) throws OwDebugModeException
    {
        Hashtable ret = m_locals.get(local_p.toString());
        if (ret == null)
        {
            ret = createProperties(local_p.toString());
        }

        return ret;
    }

    /** create synchronized the missing property map
    *
    * @param locale_p String ID of the locale e.g.: "de"
    *
    * @return Hashtable
    * @throws OwDebugModeException 
    */
    private Hashtable createProperties(String locale_p) throws OwDebugModeException
    {
        if (locale_p.equalsIgnoreCase("debugmode"))
        {
            // === debug mode language
            throw new OwDebugModeException();
        }

        return createPropertiesSync(locale_p);
    }

    /** create synchronized the missing property map
     *
     * @param locale_p String ID of the locale e.g.: "de"
     *
     * @return Hashtable
     */
    private synchronized Hashtable createPropertiesSync(String locale_p)
    {
        Hashtable ret = m_locals.get(locale_p);
        if (null != ret)
        {
            return ret; // already loaded by previous thread
        }

        // text maps to resolve localization for given locale ID 
        ret = new Hashtable();

        // === load from resource
        // load the text maps
        String[] resourceNames = getResourceNames();
        for (int i = 0; i < resourceNames.length; i++)
        {
            loadProperties(locale_p, resourceNames[i], ret);
        }

        m_locals.put(locale_p, ret);

        return ret;
    }

    /** load localize text maps into the given map, add to it.
     *
     * @param locale_p String ID of the locale e.g.: "de"
     * @param name_p String name of the text map, will be extended with locale and Hashtable
     * @param props_p Hashtable to be added to
     */
    private void loadProperties(String locale_p, String name_p, Hashtable props_p)
    {
        String mapname = name_p + "_" + locale_p + ".properties";

        // load text maps
        try
        {
            OwResourceProperties temp = new OwResourceProperties();
            // load the localize text map
            temp.load(getClass().getResource("/" + mapname));
            props_p.putAll(temp);
        }
        catch (Exception e)
        {
            LOG.debug("OwString.loadProperties: Textmap [" + mapname + "] was not found.");
        }
    }

    /**
     * initialize custom localization files
     * @param bootConfig
     */
    public static synchronized void initialize(OwXMLUtil bootConfig)
    {
        if (instance == null)
        {
            instance = new OwStringProperties();
        }
        instance.init(bootConfig);
    }

    /**
     * Create list with resource files
     * @param bootstrapConfig_p
     */
    private void init(OwXMLUtil bootstrapConfig_p)
    {
        String strDisplayName = "";

        //reinitialize the list of resource names
        List<String> resourceFileNames = new LinkedList<String>();

        //add default names;
        resourceFileNames.addAll(Arrays.asList(DEFAULT_RESOURCE_NAMES));

        //read from bootstrap;
        List fileList = bootstrapConfig_p.getSafeNodeList("availablelocalizeFiles");
        Iterator it = fileList.iterator();

        while (it.hasNext())
        {
            org.w3c.dom.Node langNode = (org.w3c.dom.Node) it.next();

            strDisplayName = OwXMLDOMUtil.getSafeStringAttributeValue(langNode, "fileName", "");
            resourceFileNames.add(strDisplayName);
        }

        this.resourceNames = resourceFileNames.toArray(new String[resourceFileNames.size()]);
    }

    /**
     * Get instance
     * @return OwStringProperties 
     */
    public static synchronized OwStringProperties getInstance()
    {
        if (instance == null)
        {
            instance = new OwStringProperties();
        }
        return instance;
    }

    /**
     * Adds the given key mapped localization properties to the current text mappings. 
     * 
     * @param locale_p
     * @param properties_p
     * @throws OwDebugModeException
     * @since 3.1.0.5
     */
    public void putAll(Locale locale_p, Map properties_p) throws OwDebugModeException
    {
        getProperties(locale_p).putAll(properties_p);
    }

    /**
     * Adds the given key mapped label to the current text mappings.
     * The {@link OwString#LABEL_PREFIX} is added to the given key.
     * 
     * @param local_p
     * @param key_p
     * @param label_p
     * @throws OwDebugModeException
     * @since 3.1.0.5
     */
    public void putLabel(Locale local_p, String key_p, String label_p) throws OwDebugModeException
    {
        getProperties(local_p).put(OwString.LABEL_PREFIX + key_p, label_p);
    }
}
