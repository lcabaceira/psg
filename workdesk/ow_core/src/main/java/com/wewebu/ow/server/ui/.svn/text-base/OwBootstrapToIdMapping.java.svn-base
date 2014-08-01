package com.wewebu.ow.server.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class to handle mappings between different owbootstrap.xml files and corresponding identifiers.
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
 *@since 3.2.0.0
 */
public class OwBootstrapToIdMapping
{
    /** the only instance of this class*/
    private static OwBootstrapToIdMapping instance = null;
    /** the class logger*/
    private static Logger LOG = OwLogCore.getLogger(OwBootstrapToIdMapping.class);
    /** the name of the properties file*/
    private static String BOOTSTRAP2ID = "owbootstrap2id.properties";
    /** the map between adaptor path and properties file.*/
    private Map<String, Properties> adapter2PropsMap = new HashMap<String, Properties>();

    /**The constructor of this class*/
    private OwBootstrapToIdMapping()
    {

    }

    /**
     * Get the only instance of this class.
     * @return - the only instance of this class.
     */
    public static synchronized OwBootstrapToIdMapping getInstance()
    {
        if (instance == null)
        {
            instance = new OwBootstrapToIdMapping();
        }
        return instance;
    }

    /**
     * Write the mappings between the <code>owbootstrap</code> file and the provided identifier,
     * @param adaptorFullPath_p - the full path of the adaptor
     * @param owBootstrapName_p - the name of the <code>owbootstrap</code> file
     * @param identifier_p - the value of the identifier.
     */
    public synchronized void writeMappings(String adaptorFullPath_p, String owBootstrapName_p, String identifier_p)
    {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try
        {
            Properties currentProperties = adapter2PropsMap.get(adaptorFullPath_p);
            File propFile = new File(getPropertiesFullPath(adaptorFullPath_p));
            if (currentProperties == null)
            {
                currentProperties = new Properties();
                if (propFile.exists())
                {
                    fileInputStream = new FileInputStream(propFile);
                    currentProperties.load(fileInputStream);
                }
            }
            currentProperties.put(owBootstrapName_p, identifier_p);
            fileOutputStream = new FileOutputStream(propFile);
            currentProperties.store(fileOutputStream, "Mappings between owbootstrap*.xml files and identifiers.");
            adapter2PropsMap.put(adaptorFullPath_p, currentProperties);
        }
        catch (Exception e)
        {
            LOG.error("Cannot write the mappings!", e);
        }
        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }
                catch (IOException e)
                {
                }
                fileInputStream = null;
            }
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                }
                fileOutputStream = null;
            }
        }
    }

    /**
     * Get the full path for the properties file where the mapping is stored.
     * @param adaptorFullPath_p - the full path to the adaptor configuration files
     * @return - the full path for the properties file where the mapping is stored.
     */
    private String getPropertiesFullPath(String adaptorFullPath_p)
    {
        return adaptorFullPath_p + "/" + BOOTSTRAP2ID;
    }

    /**
     * Get the associated identifier for the given <code>owBootstrapName_p</p>
     * @param adaptorFullPath_p - the adaptor full path.
     * @param owBootstrapName_p - the <code>owbootstrap</code> file name
     * @return - the associated identifier or <code>null</code> 
     */
    public synchronized String getIdentifier(String adaptorFullPath_p, String owBootstrapName_p)
    {
        String result = null;
        FileInputStream propStream = null;
        try
        {
            Properties currentProperties = adapter2PropsMap.get(adaptorFullPath_p);
            if (currentProperties == null)
            {
                currentProperties = new Properties();
                File propFile = new File(getPropertiesFullPath(adaptorFullPath_p));
                if (propFile.exists())
                {
                    propStream = new FileInputStream(propFile);
                    currentProperties.load(propStream);
                }
            }
            result = currentProperties.getProperty(owBootstrapName_p);
        }
        catch (Exception e)
        {
            LOG.error("Cannot read the mappings!", e);
        }
        finally
        {
            if (propStream != null)
            {
                try
                {
                    propStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
