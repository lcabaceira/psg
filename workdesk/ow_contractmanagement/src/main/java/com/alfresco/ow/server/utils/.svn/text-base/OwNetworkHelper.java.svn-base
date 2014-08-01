package com.alfresco.ow.server.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * OwNetworkHelper.<br/>
 * Helper class for OwNetwork related functions.
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
public class OwNetworkHelper
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwNetworkHelper.class);

    /**
     * Returns if object is a search template.
     * @param objectIdentifier_p
     * @return true if object is a search template, false else.
     */
    public static boolean isSearchTemplate(String objectIdentifier_p)
    {
        if (null != objectIdentifier_p)
        {
            return objectIdentifier_p.toLowerCase().startsWith("vf=");
        }
        return false;
    }

    /**
     * Executes and returns the search template results
     * @param context_p OW main application context
     * @param searchTemplate_p name of search template, has to start with "vf="
     * @return the search template results
     * @throws OwObjectNotFoundException
     */
    public static OwObjectCollection getSearchTemplateResults(OwMainAppContext context_p, String searchTemplate_p) throws OwObjectNotFoundException
    {
        if (!isSearchTemplate(searchTemplate_p))
        {
            String message = searchTemplate_p + " must identify a search template!";
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
        try
        {
            // get network
            OwNetwork network = context_p.getNetwork();

            // create search template
            OwSearchTemplate searchtemplate = (OwSearchTemplate) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, searchTemplate_p, false, false);
            searchtemplate.init(network);

            OwSearchNode search = searchtemplate.getSearch(true);

            // get property names to retrieve from column list
            List properties = new ArrayList();

            Iterator it = searchtemplate.getColumnInfoList().iterator();
            while (it.hasNext())
            {
                OwFieldColumnInfo columninfo = (OwFieldColumnInfo) it.next();
                properties.add(columninfo.getPropertyName());
            }

            // Search and get results
            return network.doSearch(search, searchtemplate.getSort(1), properties, 100, 0);
        }
        catch (Exception e)
        {
            String message = "Error creating or executing search template: " + searchTemplate_p;
            LOG.error(message, e);
            throw new OwObjectNotFoundException(message, e);
        }
    }

    /**
     * Returns object by path, DMSID or search template
     * @param context_p Main application context
     * @param objectIdentifier_p  Either a path starting with '/', a DMSID starting with "dmsid=" or a search template starting with "vf="
     * @param parameterName_p Name of a configuration parameter for the exception message or null
     * @return object by path, DMSID or search template
     */
    public static OwObject getObject(OwMainAppContext context_p, String objectIdentifier_p, String parameterName_p) throws OwObjectNotFoundException, OwConfigurationException
    {
        // check call parameters
        if (null == context_p)
        {
            String message = "OwMainAppContext context_p: is a required parameter!";
            throw new IllegalArgumentException(message);
        }

        OwObject owObj = null;
        OwNetwork network = context_p.getNetwork();

        // path?
        if (objectIdentifier_p.startsWith("/"))
        {
            // path
            try
            {
                owObj = network.getObjectFromPath(objectIdentifier_p, false);
            }
            catch (Exception e)
            {
                String message = "Error getting object from path " + objectIdentifier_p;
                LOG.error(message, e);
                throw new OwObjectNotFoundException(message, e);
            }
        }
        else if (objectIdentifier_p.toLowerCase().startsWith("dmsid"))
        {
            try
            {
                // DMSID
                owObj = network.getObjectFromDMSID(objectIdentifier_p, false);
            }
            catch (Exception e)
            {
                String message = "Error getting object from DMSID " + objectIdentifier_p;
                LOG.error(message, e);
                throw new OwObjectNotFoundException(message, e);
            }
        }
        else if (isSearchTemplate(objectIdentifier_p))
        {
            // Search and get results
            OwObjectCollection col = getSearchTemplateResults(context_p, objectIdentifier_p);
            if (col.size() != 1)
            {
                String sMessage = "The search template '" + objectIdentifier_p + "' didn't return one OwObject!";
                LOG.error(sMessage);
                throw new OwObjectNotFoundException(sMessage);
            }

            // retrieve object 
            owObj = (OwObject) col.get(0);
        }
        else
        {
            String objectName = parameterName_p != null ? parameterName_p : "objectIdentifier_p";
            String sMessage = objectName + " must contain either a path to an object, a DMSID of an object or a search template.";
            LOG.error(sMessage);
            throw new OwConfigurationException(sMessage);
        }
        return owObj;
    }

    /**
     * Returns if object is a folder.
     * @param owObject_p
     * @return true  if object is a folder, false else.
     */
    public static boolean isFolder(OwObject owObject_p)
    {
        if (null != owObject_p)
        {
            int type = owObject_p.getType();
            return (type & 0XF000) == OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS;
        }
        return false;
    }

    /**
     * Returns a folder by his DMSID
     * @param context_p OwMainAppContext
     * @param folderDmsId_p String: DMSID
     * @return OwObject:  a folder by his DMSID
     * @throws OwException
     */
    public static OwObject getFolderFromDmsId(OwMainAppContext context_p, String folderDmsId_p) throws OwException
    {
        // check required params first
        if (null == context_p)
        {
            String message = "OwMainAppContext context_p: is a required parameter!";
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
        if (null == folderDmsId_p || folderDmsId_p.isEmpty())
        {
            String message = "folderDmsId_p: is a required parameter!";
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }

        // retrieve object
        OwObject owObj = null;
        OwNetwork network = context_p.getNetwork();
        try
        {
            owObj = network.getObjectFromDMSID(folderDmsId_p, false);
        }
        catch (Exception e)
        {
            String message = "Error getting object from DMSID " + folderDmsId_p;
            LOG.error(message, e);
            throw new OwObjectNotFoundException(message, e);
        }

        // object must be a folder
        if (!isFolder(owObj))
        {
            String message = "Object with DMSID " + folderDmsId_p + " is not a folder object!";
            LOG.error(message);
            throw new OwInvalidOperationException(message);
        }

        return owObj;
    }

    /**
     * Returns the link objects from the folder. 
     * @param folder_p OwObject: folder object
     * @return OwObjectCollection: the link objects from the folder
     * @throws OwException 
     */
    public static OwObjectCollection getLinkObjects(OwObject folder_p) throws OwException
    {
        // check input params first
        if (null == folder_p || !isFolder(folder_p))
        {
            String message = "folder_p: is a required parameter which must reference a folder object!";
            LOG.error(message);
            throw new IllegalArgumentException(message);

        }

        OwObjectCollection links = null;
        try
        {
            int[] linkTypes = new int[] { OwObjectReference.OBJECT_TYPE_LINK };
            if (folder_p.hasChilds(linkTypes, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
            {
                links = folder_p.getChilds(linkTypes, null, null, 500, 0, null);
            }
            return links;
        }
        catch (OwException e)
        {
            LOG.error("Error retrieving link objects from folder.", e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Error retrieving link objects from folder.", e);
            throw new OwInvalidOperationException("Error retrieving link objects from folder.", e);
        }
    }

    /**
     * Conversion from String to boolean. 
     * @param sValue_p String value to convert
     * @param bDefault_p Default value if sValue is null or not boolean
     * @return boolean value
     */
    public static boolean convertBoolean(String sValue_p, boolean bDefault_p)
    {
        boolean bValue = bDefault_p;

        if (null != sValue_p)
        {
            String val = sValue_p.toLowerCase();
            if (val.equals("1") || val.equals("true"))
            {
                bValue = true;
            }
            else if (val.equals("0") || val.equals("false"))
            {
                bValue = false;
            }
        }

        return bValue;
    }
}
