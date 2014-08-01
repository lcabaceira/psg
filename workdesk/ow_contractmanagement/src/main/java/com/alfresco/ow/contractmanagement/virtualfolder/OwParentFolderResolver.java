package com.alfresco.ow.contractmanagement.virtualfolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwParentFolderResolver.
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
public class OwParentFolderResolver
{
    private static final Logger LOG = OwLog.getLogger(OwParentFolderResolver.class);

    /**
     * Use the common root folder of all selected object. If more than one root folder exists an exception will be thrown.
     * If a single object has more then one parent an exception will be thrown.
     * If a object has no parent folder an exception will be thrown.
     */
    public static final String PLUGIN_PARAM_SINGLE_ROOT = "UseSingleRoot";
    /**
     * Physical root folder of an semi virtual folder.
     * Uses the defined VirtualFolderProperty OwSearchPath of semi virtual folder SemiVirtualRecordClass.
     * @deprecated since 4.2 use {@link #PLUGIN_PARAM_VF_SEARCH_PATH} instead
     */
    @Deprecated
    public static final String PLUGIN_PARAM_VF_SEARCH_PATRH = "VirtualFolderSearchPath";
    /**
     * Physical root folder of an semi virtual folder.
     * Uses the defined VirtualFolderProperty OwSearchPath of semi-virtual folder SemiVirtualRecordClass.
     */
    public static final String PLUGIN_PARAM_VF_SEARCH_PATH = "VirtualFolderSearchPath";

    /**
     * The search path property name of semi virtual folder search path
     */
    protected static final String VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY = "VirtualFolderSearchPathProperty";

    private enum RootFolderMode
    {
        DefinedRootFolder, UseSingleRoot, VirtualFolderSearchPath, Default
    }

    private OwObject rootFolder = null;
    private OwMainAppContext context;
    private RootFolderMode rootFolderMode;

    /** initialization of the plugin 
     * @param context_p OwMainAppContext
     */
    public void init(String strRootFolder, OwMainAppContext context_p) throws Exception
    {
        this.context = context_p;

        if (strRootFolder == null || strRootFolder.isEmpty())
        {
            StringBuilder msg = new StringBuilder();
            msg.append("No root folder is configured. ");
            msg.append("Use an object path or ");
            msg.append(PLUGIN_PARAM_SINGLE_ROOT);
            msg.append(", ");
            msg.append(PLUGIN_PARAM_VF_SEARCH_PATH);
            msg.append(".");
            msg.append(" Original parent folder(s) will be used.");
            LOG.info(msg.toString());

            rootFolderMode = RootFolderMode.Default;
            return;
        }

        if (strRootFolder.startsWith(PLUGIN_PARAM_SINGLE_ROOT))
        {
            rootFolderMode = RootFolderMode.UseSingleRoot;
            return;
        }

        if (strRootFolder.startsWith(PLUGIN_PARAM_VF_SEARCH_PATH))
        {
            rootFolderMode = RootFolderMode.VirtualFolderSearchPath;
            return;
        }

        //only physical folders are allowed as root folder
        if (strRootFolder.startsWith("vf="))
        {
            throw new OwConfigurationException("Unrecognized object: " + strRootFolder + ". Prefix 'vf=' not allowed. Use the following form dmsid=<dmsid> | /<path> ]");
        }

        rootFolder = OwEcmUtil.createObjectFromString(context, strRootFolder);
        rootFolderMode = RootFolderMode.DefinedRootFolder;
    }

    /**
     * Returns the root folder as configured.
     * If no folder could be found the original parent will be returned.
     * 
     * @param objects_p
     * @param parent_p
     * @return the root folder as configured
     * @throws Exception
     */
    public OwObject getRootFolder(@SuppressWarnings("rawtypes") Collection objects_p, OwObject parent_p) throws Exception
    {
        OwObject returnedObj = null;

        switch (rootFolderMode)
        {
            case DefinedRootFolder:
                returnedObj = rootFolder;
                break;

            case UseSingleRoot:
                returnedObj = getSingleRootFolder(objects_p);
                break;

            case VirtualFolderSearchPath:
                returnedObj = getVirtualFolderSearch(objects_p);
                break;

            case Default:
                returnedObj = parent_p;

        }

        if (returnedObj != null)
        {
            return returnedObj;
        }

        return parent_p;
    }

    /**
     * Returns the root folder
     * @param object_p
     * @param parent_p
     * @return the root folder
     * @throws Exception
     */
    public OwObject getRootFolder(OwObject object_p, OwObject parent_p) throws Exception
    {
        List<OwObject> objList = new ArrayList<OwObject>(1);
        objList.add(object_p);
        return getRootFolder(objList, parent_p);
    }

    /**
     * Checks if the root folder was defined by config  
     * @return if the root folder was defined by config 
     */
    public boolean isExplicitDefinedRootFolder()
    {
        return rootFolderMode.equals(RootFolderMode.DefinedRootFolder);
    }

    /**
     * Returns the single common root folder of all objects in collection.<br>
     * Conditions:
     * <ul>
     *  <li>Every object in collection has exactly one root folder</li>
     *  <li>All objects in collection have the same root folder</li>
     * </ul>
     * In all other cases an exception is thrown.
     * 
     * @param objects_p Collection of {@link OwObject}
     * @return Common root folder of all objects in collection
     * @throws Exception If objects in collection have no common root folder.
     */
    @SuppressWarnings("rawtypes")
    protected OwObject getSingleRootFolder(Collection objects_p) throws Exception
    {
        OwObject parent = null;
        if (objects_p != null)
        {
            Iterator iterator = objects_p.iterator();
            while (iterator.hasNext())
            {
                OwObject object = (OwObject) iterator.next();
                OwObjectCollection parents = object.getParents();

                if (parents == null)
                {
                    throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.noParent", "Error during processing of the function: No parent found for the object."));
                }
                //object has more than one parent, no single root folder
                if (parents.size() > 1)
                {
                    throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.multiParents",
                            "Error during processing of the function: Object has more than one parent, but only one parent is allowed."));
                }
                //object has another parent than a already defined parent of an object in collection, unable to cut.
                if (parent != null && !parent.equals(parents.get(0)))
                {
                    throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.differentParents",
                            "Error during processing of the function:  Objects have different parents. The same parent for all objects is necessary."));
                }
                if (parent == null)
                {
                    parent = (OwObject) parents.get(0);
                }
            }
        }

        if (parent == null)
        {
            throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.noParent", "Error during processing of the function: No parent found for the object."));
        }

        return parent;
    }

    /**
     * Find root folder by executing a search template
     * @param objects_p
     * @return root folder by executing a search template
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    protected OwObject getVirtualFolderSearch(Collection objects_p) throws Exception
    {
        OwObject parent = null;

        if (objects_p != null)
        {
            Iterator iterator = objects_p.iterator();
            while (iterator.hasNext())
            {
                OwObject object = (OwObject) iterator.next();
                if (object.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
                {
                    OwVirtualFolderObject rootObject = getRootObject((OwVirtualFolderObject) object);
                    OwSearchTemplate searchTemplate = rootObject.getSearchTemplate();

                    if (searchTemplate == null)
                    {
                        throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.virtualFolderSearchPath",
                                "Error during processing of the function: No virtual folder search path found for the object."));
                    }

                    OwSearchPath searchPath = null;
                    List childNodes = searchTemplate.getSearch(false).findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL).getChilds();
                    for (Iterator it = childNodes.iterator(); it.hasNext();)
                    {
                        OwSearchNode child = (OwSearchNode) it.next();

                        if (!child.isCriteriaNode())
                        {
                            continue;
                        }

                        OwSearchCriteria criteria = child.getCriteria();
                        if (criteria.getUniqueName().equals(VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY))
                        {
                            searchPath = (OwSearchPath) criteria.getValue();
                        }
                    }

                    OwObject objectParent = null;
                    if (searchPath != null)
                    {
                        objectParent = OwEcmUtil.createObjectFromString(context, searchPath.getPathName());
                    }

                    if (objectParent == null)
                    {
                        throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.noParent", "Error during processing of the function: No parent found for the object."));
                    }
                    if (parent != null && !parent.equals(objectParent))
                    {
                        throw new OwInvalidOperationException(new OwString("com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver.error.differentParents",
                                "Error during processing of the function:  Objects have different parents. The same parent for all objects is necessary."));
                    }

                    if (parent == null)
                    {
                        parent = objectParent;
                    }
                }
            }
        }

        return parent;
    }

    /** get the root virtual folder object
     * @return OwStandardVirtualFolderObjectFactory
     */
    protected OwVirtualFolderObject getRootObject(OwVirtualFolderObject object_p) throws Exception
    {
        OwObjectCollection parentsList = object_p.getParents();
        if (parentsList != null && parentsList.get(0) != null && parentsList.get(0) instanceof OwVirtualFolderObject)
        {
            // ask parent
            return getRootObject((OwVirtualFolderObject) parentsList.get(0));
        }
        else
        {
            // no parents, so this is the root
            return object_p;
        }
    }

    /**
     * returns the defined root folder object
     * @return the defined root folder object
     */
    protected OwObject getDefinedRootFolder()
    {
        return rootFolder;
    }
}
