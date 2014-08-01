package com.wewebu.ow.server.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility functions for working with the ECM system.
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
public class OwEcmUtil
{
    /** startup folder prefix */
    private static final String DMSID_PREFIX = "dmsid=";

    /** startup folder prefix */
    private static final String VIRTUALFOLDER_PREFIX = "vf=";

    /** package logger or class logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwEcmUtil.class);

    /**
     *</p>
     * A simple search clause tuple.
     * @see OwEcmUtil#createSimpleSearchNode(String, String, String, com.wewebu.ow.server.app.OwEcmUtil.OwSimpleSearchClause[], OwFieldDefinitionProvider)
     * @see OwEcmUtil#doSimpleSearch(String, String, String, com.wewebu.ow.server.app.OwEcmUtil.OwSimpleSearchClause[], OwRepository, OwSort, Collection, int, int)
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
    public static class OwSimpleSearchClause
    {
        private String m_name;
        private int m_operator;
        private Object m_value;

        /** create a search clause
         * 
         * @see OwEcmUtil#createSimpleSearchNode(String, String, String, com.wewebu.ow.server.app.OwEcmUtil.OwSimpleSearchClause[], OwFieldDefinitionProvider)
         * @see OwEcmUtil#doSimpleSearch(String, String, String, com.wewebu.ow.server.app.OwEcmUtil.OwSimpleSearchClause[], OwRepository, OwSort, Collection, int, int)
         * 
         * @param name_p the criteria name
         * @param operator_p the criteria operator as defined in OwSearchOperator.CRIT_OP_...
         * @param value_p the criteria value
         */
        public OwSimpleSearchClause(String name_p, int operator_p, Object value_p)
        {
            super();
            this.m_name = name_p;
            this.m_operator = operator_p;
            this.m_value = value_p;
        }

        /** get the criteria name
         * 
         * @return String
         */
        public String getName()
        {
            return m_name;
        }

        /** get the criteria operator
         * 
         * @return OwSearchOperator.CRIT_OP_...
         */
        public int getOperator()
        {
            return m_operator;
        }

        /** get the criteria value
         * 
         * @return Object
         */
        public Object getValue()
        {
            return m_value;
        }

        public String toString()
        {
            StringBuffer ret = new StringBuffer();

            ret.append("name = [");
            ret.append(m_name);
            ret.append("], operator = [");
            ret.append(String.valueOf(m_operator));
            ret.append(" - ");
            ret.append(OwSearchOperator.getOperatorDisplayString(Locale.ENGLISH, m_operator));
            ret.append("], value = [");
            ret.append(m_value);
            ret.append("]");

            return ret.toString();
        }
    }

    /** create a simple search node from a array of property values
     * 
     * @param strClassName_p the objectclass to search for
     * @param strResourceName_p the resource to search in, can be null to search the default resource
     * @param rootpath_p the root path to search in, can be null to search all object's
     * @param clauses_p an array of values operators and criteria names to apply to the search
     * @param fielddefinitionprovider_p the field definition provider to resolve the properties 
     * @return OwSearchNode the created search node that can be submitted to a repository doSearch method
     * @throws Exception
     */
    public static OwSearchNode createSimpleSearchNode(String strClassName_p, String strResourceName_p, String rootpath_p, OwSimpleSearchClause[] clauses_p, OwFieldDefinitionProvider fielddefinitionprovider_p) throws Exception
    {
        return createSimpleSearchNode(OwObjectReference.OBJECT_TYPE_DOCUMENT, strClassName_p, strResourceName_p, rootpath_p, clauses_p, fielddefinitionprovider_p);
    }

    /** create a simple search node from a array of property values
     * 
     * @param strClassName_p the object class to search for
     * @param strResourceName_p the resource to search in, can be null to search the default resource
     * @param rootpath_p the root path to search in, can be null to search all object's
     * @param clauses_p an array of values operators and criteria names to apply to the search
     * @param fielddefinitionprovider_p the field definition provider to resolve the properties
     * @param objectType_p - object type, as defined in {@link OwObjectReference} class. 
     * @return OwSearchNode the created search node that can be submitted to a repository doSearch method
     * @throws Exception
     * @since 3.0.0.0
     */
    public static OwSearchNode createSimpleSearchNode(int objectType_p, String strClassName_p, String strResourceName_p, String rootpath_p, OwSimpleSearchClause[] clauses_p, OwFieldDefinitionProvider fielddefinitionprovider_p) throws Exception
    {
        OwSearchObjectStore searchStore = createSearchStore(null, strResourceName_p);
        return createSimpleSearchNode(objectType_p, searchStore, strClassName_p, strResourceName_p, rootpath_p, clauses_p, fielddefinitionprovider_p);
    }

    /** create a simple search node from a array of property values
     *
     * @param searchObjectStore_p - the search object store.
     * @param strClassName_p the object class to search for
     * @param strResourceName_p the resource to search in, can be null to search the default resource
     * @param rootpath_p the root path to search in, can be null to search all object's
     * @param clauses_p an array of values operators and criteria names to apply to the search
     * @param fielddefinitionprovider_p the field definition provider to resolve the properties
     * @param objectType_p - object type, as defined in {@link OwObjectReference} class. 
     * @return OwSearchNode the created search node that can be submitted to a repository doSearch method
     * @throws Exception
     * @since 3.0.0.0
     */
    public static OwSearchNode createSimpleSearchNode(int objectType_p, OwSearchObjectStore searchObjectStore_p, String strClassName_p, String strResourceName_p, String rootpath_p, OwSimpleSearchClause[] clauses_p,
            OwFieldDefinitionProvider fielddefinitionprovider_p) throws Exception
    {
        return createSimpleSearchNode(objectType_p, searchObjectStore_p, strClassName_p, strResourceName_p, rootpath_p, clauses_p, fielddefinitionprovider_p, false);
    }

    /** create a simple search node from a array of property values
    *
    * @param searchObjectStore_p - the search object store.
    * @param strClassName_p the object class to search for
    * @param strResourceName_p the resource to search in, can be null to search the default resource
    * @param rootpath_p the root path to search in, can be null to search all object's
    * @param clauses_p an array of values operators and criteria names to apply to the search
    * @param fielddefinitionprovider_p the field definition provider to resolve the properties
    * @param objectType_p - object type, as defined in {@link OwObjectReference} class. 
    * @param searchSubFolders_p - if <code>true</code> the subfolders are included in search path
    * @return OwSearchNode the created search node that can be submitted to a repository doSearch method
    * @throws Exception
    * @since 3.0.0.0
    */
    public static OwSearchNode createSimpleSearchNode(int objectType_p, OwSearchObjectStore searchObjectStore_p, String strClassName_p, String strResourceName_p, String rootpath_p, OwSimpleSearchClause[] clauses_p,
            OwFieldDefinitionProvider fielddefinitionprovider_p, boolean searchSubFolders_p) throws Exception
    {
        // create root of the search tree
        OwSearchNode retnode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_COMBINATION);

        // add special search node, which contains class criteria and path criteria
        OwSearchNode specialNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
        retnode.add(specialNode);

        // add class criteria
        OwStandardClassSelectObject classselect = OwStandardClassSelectObject.CLASS_CLASS_NAME.newObject();
        classselect.addClass(objectType_p, strClassName_p, null, strResourceName_p, true);

        OwStandardClassSelectObject from = OwStandardClassSelectObject.CLASS_FROM.newObject();
        from.addClass(objectType_p, strClassName_p, null, strResourceName_p, true);

        specialNode.add(new OwSearchNode(classselect, OwSearchOperator.CRIT_OP_EQUAL, OwSearchCriteria.ATTRIBUTE_HIDDEN));
        specialNode.add(new OwSearchNode(from, OwSearchOperator.CRIT_OP_EQUAL, OwSearchCriteria.ATTRIBUTE_HIDDEN));

        // and resource / path criteria
        if (null != rootpath_p)
        {
            OwSearchPath searchPath = new OwSearchPath(null, rootpath_p, searchSubFolders_p, searchObjectStore_p);
            OwSearchNode searchPathNode = new OwSearchNode(OwSearchPathField.classDescription, OwSearchOperator.MERGE_NONE, searchPath, OwSearchCriteria.ATTRIBUTE_HIDDEN, "SimpleSearchPath", "SimpleSearchPathInstruction", null);
            specialNode.add(searchPathNode);
        }

        // add property search node, which contains property criteria
        OwSearchNode propNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
        retnode.add(propNode);

        // add some property criteria
        for (int i = 0; i < clauses_p.length; i++)
        {
            OwSimpleSearchClause clause = clauses_p[i];

            OwFieldDefinition fieldDefinition = fielddefinitionprovider_p.getFieldDefinition(clause.getName(), null);

            propNode.add(new OwSearchNode(fieldDefinition, clause.getOperator(), clause.getValue(), 0));
        }

        return retnode;
    }

    /**
     * Creates a {@link OwSearchObjectStore} object with the given parameters.
     * @param objectStoreName_p - the name of object store.
     * @param objectStoreId_p - the id of object store.
     * @return the newly created {@link OwSearchObjectStore} object.
     * @since 3.0.0.0
     */
    public static OwSearchObjectStore createSearchStore(String objectStoreId_p, String objectStoreName_p)
    {
        OwSearchObjectStore searchStore = new OwSearchObjectStore(objectStoreId_p, objectStoreName_p);
        return searchStore;
    }

    /** perform a simple search from a array of property values
     * 
     * @param strClassName_p the object class to search for
     * @param strResourceName_p the resource to search in, can be null to search the default resource
     * @param rootpath_p the root path to search in, can be null to search all object's
     * @param clauses_p an array of values operators and criteria names to apply to the search
     * @param repository_p OwRepository the repository to search in
     * @param sort_p the sort to apply, can be null
     * @param propertynames_p the column properties to retrieve, can be null
     * @param iMaxSize_p the max size of returned objects
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or (OwSearchTemplate.VERSION_SELECT_DEFAULT or 0) to use default version
     * @return OwObjectCollection
     * @throws Exception
     */
    public static OwObjectCollection doSimpleSearch(String strClassName_p, String strResourceName_p, String rootpath_p, OwSimpleSearchClause[] clauses_p, OwRepository repository_p, OwSort sort_p, Collection propertynames_p, int iMaxSize_p,
            int iVersionSelection_p) throws Exception
    {
        OwSearchNode search = createSimpleSearchNode(strClassName_p, strResourceName_p, rootpath_p, clauses_p, repository_p);
        return repository_p.doSearch(search, sort_p, propertynames_p, iMaxSize_p, iVersionSelection_p);
    }

    /** creates a OwObject from a string. Uses either a pathname or a dmsid. 
     *  Useful when working with XML configuration to set a predefined object.
     */
    public static OwObject createObjectFromString(OwMainAppContext context_p, String strObject_p) throws Exception
    {
        if (strObject_p.startsWith("/"))
        {
            // === physical Object is requested
            return (context_p).getNetwork().getObjectFromPath(strObject_p, true);
        }
        else if (strObject_p.startsWith(VIRTUALFOLDER_PREFIX))
        {
            // === virtual folder is requested	
            // remove virtual folder prefix
            strObject_p = strObject_p.substring(VIRTUALFOLDER_PREFIX.length());

            // get virtual folder from Network
            return (OwObject) (context_p).getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER, strObject_p, false, false);
        }
        else if (strObject_p.startsWith(DMSID_PREFIX))
        {
            // === a object via DMSID is requested
            // remove DMSID prefix
            strObject_p = strObject_p.substring(DMSID_PREFIX.length());

            // create object form DMSID 
            return (context_p).getNetwork().getObjectFromDMSID(strObject_p, false);
        }
        else
        {
            throw new OwInvalidOperationException("Unrecognized object: " + strObject_p + " use the following form [vf=<virtualfoldername> | dmsid=<dmsid> | /<path> ]");
        }
    }

    /** creates a simple subfolder using the default folder class from bootstrap
     * checks if folder exits already
     *
     * @param context_p OwMainAppContext instance
     * @param baseFolder_p OwObject where subfolder should be created
     * @param strName_p String name of new subfolder
     *
     * @return OwObject new folder
     */
    public static OwObject createSafeSubFolder(OwMainAppContext context_p, OwObject baseFolder_p, String strName_p) throws Exception
    {
        // === check if folder exists
        try
        {
            return context_p.getNetwork().getObjectFromPath(baseFolder_p.getPath() + "/" + strName_p, false);
        }
        catch (OwObjectNotFoundException e)
        {
            // folder does not exist, proceed and create it
        }

        // === create folder
        // Get folder class and name property
        String strFolderClassName = context_p.getConfiguration().getDefaultFolderClassName();
        OwObjectClass folderClass = context_p.getNetwork().getObjectClass(strFolderClassName, baseFolder_p.getResource());
        String strNameProperty = folderClass.getNamePropertyName();
        OwPropertyClass folderNameClass = folderClass.getPropertyClass(strNameProperty);

        // create name for folder
        com.wewebu.ow.server.ecm.OwProperty folderName = new com.wewebu.ow.server.ecm.OwStandardProperty(strName_p, folderNameClass);

        OwPropertyCollection properties = new com.wewebu.ow.server.ecm.OwStandardPropertyCollection();
        properties.put(folderName.getPropertyClass().getClassName(), folderName);

        // create folder
        String strFolderID = context_p.getNetwork().createNewObject(baseFolder_p.getResource(), strFolderClassName, properties, null, null, baseFolder_p, null, null);

        // return instance right away
        return context_p.getNetwork().getObjectFromDMSID(strFolderID, false);
    }

    /** 
     * Returned item from the function {@link OwEcmUtil#getParentPathOfClass(OwObject obj_p, Collection classNames_p)}
     */
    public static class OwParentPathInfo
    {
        /** construct a path info item
         * @param parent_p parent that matches a requested classname
         * @param path_p path from parent to the object
         */
        public OwParentPathInfo(OwObject parent_p, String path_p, String displaypath_p)
        {
            m_Parent = parent_p;
            m_strPath = path_p;
            m_strDisplayPath = displaypath_p;
        }

        /** parent that matches a requested classname */
        private OwObject m_Parent;
        /** path from parent to the object */
        private String m_strPath;
        /** display path from parent to the object */
        private String m_strDisplayPath;

        /** get the parent that matches a requested classname */
        public OwObject getParent()
        {
            return m_Parent;
        }

        /** get the path from parent to the object */
        public String getPath()
        {
            return m_strPath;
        }

        /** get the display path from parent to the object */
        public String getDisplayPath()
        {
            return m_strDisplayPath;
        }
    }

    /** get a collection of parents with paths for an object, which are of the specified classname
     *  used to find the records a object is filed in
     * 
     *  NOTE: The function will not just search the next parents, but the whole path of parents.
     * 
     *  @param obj_p to find parents for
     *  @param classNames_p Collection of classnames which must match the parents
     *
     *  @return List of OwParentPathInfo
     */
    public static List getParentPathOfClass(OwObject obj_p, Collection classNames_p) throws Exception
    {
        List retList = new ArrayList();

        iterateParentPathOfClass(obj_p, classNames_p, retList, "", "");

        return retList;
    }

    /** recursive iterator function used by {@link OwEcmUtil#getParentPathOfClass(OwObject obj_p, Collection classNames_p)}
     *  used to find the records a object is filed in
     * 
     *  NOTE: The function will not just search the next parents, but the whole path of parents.
     * 
     *  @param obj_p to find parents for
     *  @param classNames_p Collection of classnames which must match the parents
     *
     */
    private static void iterateParentPathOfClass(OwObject obj_p, Collection classNames_p, List retList_p, String strPath_p, String displaypath_p) throws Exception
    {
        List parents = obj_p.getParents();

        if (parents == null)
        {
            return;
        }

        Iterator it = parents.iterator();
        while (it.hasNext())
        {
            OwObject parent = (OwObject) it.next();

            boolean fMatch = false;
            Iterator itClassNames = classNames_p.iterator();
            while (itClassNames.hasNext())
            {
                String strClassName = (String) itClassNames.next();

                OwObjectClass objectClass = parent.getObjectClass();
                String parentClassname = objectClass.getClassName();

                if (parentClassname == null)
                {
                    LOG.error("OwEcmUtil.iterateParentPathOfClass: ParentClassname is null, classname = " + strClassName);
                    throw new OwInvalidOperationException("OwEcmUtil.iterateParentPathOfClass: Recursive iterator function used by getParentPathOfClass, used to find the records a object is filed in. ParentClassname is null, classname = "
                            + strClassName);
                }

                // match classname ? 
                if (parentClassname.equals(strClassName))
                {
                    retList_p.add(new OwParentPathInfo(parent, strPath_p, displaypath_p));
                    fMatch = true;
                    break;
                }
            }

            // didn't match so continue searching the next parent
            if (!fMatch)
            {
                iterateParentPathOfClass(parent, classNames_p, retList_p, OwObject.STANDARD_PATH_DELIMITER + parent.getID() + strPath_p, "/" + parent.getName() + displaypath_p);
            }

        }
    }

}