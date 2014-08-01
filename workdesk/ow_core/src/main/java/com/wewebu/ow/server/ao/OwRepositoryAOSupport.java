package com.wewebu.ow.server.ao;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 *  Simple Repository based OwAOSupport.
 *  Will retrieve ApplicationObjects (Searchtemplates,...)
 *  from a repository, using the provided OwNetwork.
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
public class OwRepositoryAOSupport implements OwAOSupport, OwConfigurableSupport
{
    private static final char PATH_SEPARATOR = '/';

    private static final Logger LOG = OwLogCore.getLogger(OwRepositoryAOSupport.class);

    private OwNetwork<?> network;
    private String path;
    private int[] objectTypes;
    private int versionSelection;
    private String aoClassName;
    private String pathSeparator = "" + PATH_SEPARATOR;
    private int maxSize;

    public OwRepositoryAOSupport()
    {

    }

    /**
     * Create an instance with a max size of Integer.MAX_VALUE for object retrieval.
     * @param network_p OwNetwork to use
     * @param path_p String path where to search
     * @param objectTypes_p types to request
     * @param versionSelection_p int version selection see OwSearchtTemplate
     * @param aoClassName_p String objectclass new to create new instances
     */
    public OwRepositoryAOSupport(OwNetwork<?> network_p, String path_p, int[] objectTypes_p, int versionSelection_p, String aoClassName_p)
    {
        this(network_p, path_p, objectTypes_p, versionSelection_p, aoClassName_p, Integer.MAX_VALUE);
    }

    /**
     * Create an instance with a max size of Integer.MAX_VALUE,
     * for object retrieval.
     * @param network_p OwNetwork to use
     * @param path_p String path where to search
     * @param objectTypes_p types to request
     * @param versionSelection_p int version selection see OwSearchtTemplate
     * @param aoClassName_p String objectclass new to create new instances
     * @param maxSize_p int number of maximum children to retrieve
     */
    public OwRepositoryAOSupport(OwNetwork<?> network_p, String path_p, int[] objectTypes_p, int versionSelection_p, String aoClassName_p, int maxSize_p)
    {
        super();
        initialize(network_p, path_p, objectTypes_p, versionSelection_p, aoClassName_p, maxSize_p);
    }

    private void initialize(OwNetwork<?> network_p, String path_p, int[] objectTypes_p, int versionSelection_p, String aoClassName_p, int maxSize_p)
    {
        this.network = network_p;
        this.path = path_p;
        this.objectTypes = objectTypes_p;
        this.versionSelection = versionSelection_p;
        this.aoClassName = aoClassName_p;
        this.maxSize = maxSize_p;
    }

    @Override
    public void init(OwSupportConfiguration configuration, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            String pathParameter = configuration.getParameterValue("path");
            String confPath = OwString.replaceAll(pathParameter, "{defaultResourceID}", context.getNetwork().getResource(null).getID());

            String objectTypesParameter = configuration.getParameterValue("objectTypes");
            String[] objectTypeValues = objectTypesParameter.split(",");
            int[] confObjectTypes = new int[objectTypeValues.length];
            for (int i = 0; i < objectTypeValues.length; i++)
            {
                Integer type = (Integer) OwObjectReference.class.getField(objectTypeValues[i]).get(null);
                confObjectTypes[i] = type;
            }

            Integer confVersionSelection = (Integer) OwSearchTemplate.class.getField(configuration.getParameterValue("versionSelection")).get(null);

            String confAOClassName = configuration.getParameterValue("aoClassName");
            int confMaxSize = Integer.parseInt(configuration.getParameterValue("maxSize", "" + Integer.MAX_VALUE));
            initialize(context.getNetwork(), confPath, confObjectTypes, confVersionSelection, confAOClassName, confMaxSize);
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not initialize repository AO support.", e);
        }

    }

    public OwObject getSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {

        String name = strName_p;
        if (forceUserspecificObject_p)
        {
            try
            {
                name += "_";
                OwCredentials credentials = this.network.getCredentials();
                OwUserInfo userInfo = credentials.getUserInfo();
                String userID = userInfo.getUserID();
                name += userID;
            }
            catch (Exception e)
            {
                String message = "Could not retrieve user support info for object named " + strName_p + " in " + this.path;
                LOG.error(message, e);
                throw new OwObjectNotFoundException(message, e);
            }
        }

        String objectPath = this.path + this.pathSeparator + name;

        try
        {
            return this.network.getObjectFromPath(objectPath, true);
        }
        catch (OwObjectNotFoundException e)
        {
            if (createIfNotExist_p)
            {
                try
                {

                    OwObject aoParent = this.network.getObjectFromPath(this.path, true);
                    OwResource resource = aoParent.getResource();

                    OwObjectClass aoClass = this.network.getObjectClass(this.aoClassName, resource);
                    String namePropertyName = aoClass.getNamePropertyName();
                    OwPropertyClass namePropertyClass = aoClass.getPropertyClass(namePropertyName);

                    OwStandardPropertyCollection properties = new OwStandardPropertyCollection();
                    OwStandardProperty nameProperty = new OwStandardProperty(name, namePropertyClass);
                    properties.put(namePropertyName, nameProperty);

                    String newAODMSID = this.network.createNewObject(true, null, resource, this.aoClassName, properties, null, null, aoParent, null, null);

                    OwObject newApplicationObject = this.network.getObjectFromDMSID(newAODMSID, true);

                    LOG.debug("New AO object created with DMSID " + newAODMSID + " for " + objectPath);

                    return newApplicationObject;
                }
                catch (OwException owe)
                {
                    throw owe;
                }
                catch (Exception ce)
                {
                    String message = "Could not create support object at " + objectPath;
                    LOG.error(message, ce);
                    throw new OwServerException(message, ce);
                }
            }
            else
            {
                throw e;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String message = "Could not retrieve support object at " + objectPath;
            LOG.error(message, e);
            throw new OwObjectNotFoundException(message, e);
        }
    }

    public OwObject[] getSupportObjects(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        String folderPath = this.path + "/" + strName_p;
        try
        {
            OwObject folder = this.network.getObjectFromPath(folderPath, true);
            OwObjectCollection children = folder.getChilds(this.objectTypes, null, null, getMaxSize(), this.versionSelection, null);
            return (OwObject[]) children.toArray(new OwObject[children.size()]);

        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve support object at " + folderPath, e);
        }

    }

    /**
     * Get maximum retrieval size.
     * @return int 
     */
    public int getMaxSize()
    {
        return this.maxSize;
    }

    /**
     * Set max size to be used during retrieval.
     * @param maxSize_p int amount
     */
    public void setMaxSize(int maxSize_p)
    {
        this.maxSize = maxSize_p;
    }
}
