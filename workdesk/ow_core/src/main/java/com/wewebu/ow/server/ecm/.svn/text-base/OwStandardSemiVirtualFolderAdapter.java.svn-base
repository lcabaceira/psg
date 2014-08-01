package com.wewebu.ow.server.ecm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwAggregateIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Standard semi-virtual-folder adapter implementation.
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
 *@since 3.0.0.0 class name was OwStandardSemiVrtualFolderAdapter
 *@since 3.1.0.0 class name was renamed to OwStandardSemiVirtualFolderAdapter
 */
public class OwStandardSemiVirtualFolderAdapter implements OwSemiVirtualFolderAdapter
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardSemiVirtualFolderAdapter.class);

    private OwVirtualFolderObject virtualFolderCache = null;
    private OwNetwork network;

    /**
     * Constructor
     * @param network_p 
     */
    public OwStandardSemiVirtualFolderAdapter(OwNetwork network_p)
    {
        super();
        this.network = network_p;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public OwObjectCollection getChildren(OwSemiVirtualFolder semiVirtualFolder_p, int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwObjectCollection children = null;
        for (int i = 0; i < objectTypes_p.length; i++)
        {
            OwObjectCollection result = null;
            if (OwStandardObjectClass.isContainerType(objectTypes_p[i]))
            {
                try
                {
                    // get the search criteria map from the virtual folder search template
                    OwVirtualFolderObject virtualFolder = getVirtualFolder(semiVirtualFolder_p);

                    //preserve the set-values/getChilds order to enable in-depth criteria value propagation
                    virtualFolder.setPropagationMap(propagateRootProperties(semiVirtualFolder_p, virtualFolder.getSearchTemplate().getSearch(false)));
                    // container type, delegate to virtual folder
                    result = virtualFolder.getChilds(new int[] { objectTypes_p[i] }, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);

                    // if configured, add all physical children (folders and their containing documents)
                    if (semiVirtualFolder_p.includesPhysicalChildren())
                    {
                        // add all physical children - do not sort yet
                        OwObjectCollection containerPhysicalChildren = semiVirtualFolder_p.getPhysicalChildren(new int[] { objectTypes_p[i] }, propertyNames_p, null, maxSize_p, versionSelection_p, filterCriteria_p);
                        result.addAll(containerPhysicalChildren);

                        // sort the overall collection
                        if (null != sort_p)
                        {
                            result.sort(sort_p);
                        }
                    }
                }
                catch (OwException ce)
                {
                    throw ce;
                }
                catch (Exception e)
                {
                    String msg = "OwStandardSemiVirtualFolderAdapter.getChilds():Error enumerating virtual folder contents!";
                    LOG.error(msg, e);
                    throw new OwServerException(new OwString("ecmimpl.OwStandardSemiVrtualFolderAdapter.children.enumeration.error", "Error enumerating virtual folder contents!"), e);
                }
            }
            else if (OwStandardObjectClass.isContentType(objectTypes_p[i]))
            {
                // if configured, add all physical children (documents of the root folder of the virtual file)
                if (semiVirtualFolder_p.includesPhysicalChildren())
                {
                    // content type, call parent getChilds() method
                    result = semiVirtualFolder_p.getPhysicalChildren(new int[] { objectTypes_p[i] }, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);
                }
            }
            else
            {//delegate for any other type requests, directly to ECM object
                result = semiVirtualFolder_p.getPhysicalChildren(new int[] { objectTypes_p[i] }, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);
            }

            //assign corresponding collection
            if (null == children)
            {
                children = result;
            }
            else
            {
                children.addAll(result);
            }
        }
        return children;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getChildren(OwSemiVirtualFolder semiVirtualFolder_p, OwLoadContext loadContext) throws OwException
    {
        List<OwIterable<OwObject>> childrenIterables = new ArrayList<OwIterable<OwObject>>();

        int[] objectTypes = loadContext.getObjectTypes();
        for (int i = 0; i < objectTypes.length; i++)
        {
            int iObjectType_p = objectTypes[i];
            OwLoadContext workingLoadContext = new OwLoadContext(loadContext);
            workingLoadContext.setObjectTypes(iObjectType_p);

            if (OwStandardObjectClass.isContainerType(iObjectType_p))
            {
                try
                {
                    // get the search criteria map from the virtual folder search template
                    OwVirtualFolderObject virtualFolder = getVirtualFolder(semiVirtualFolder_p);

                    //preserve the set-values/getChilds order to enable in-depth criteria value propagation
                    virtualFolder.setPropagationMap(propagateRootProperties(semiVirtualFolder_p, virtualFolder.getSearchTemplate().getSearch(false)));
                    // container type, delegate to virtual folder
                    OwIterable<OwObject> virtualChildren = virtualFolder.getChildren(workingLoadContext);
                    childrenIterables.add(virtualChildren);

                    // if configured, add all physical children (folders and their containing documents)
                    if (semiVirtualFolder_p.includesPhysicalChildren())
                    {
                        // add all physical children - do not sort yet
                        OwIterable<OwObject> containerPhysicalChildren = semiVirtualFolder_p.getPhysicalChildren(workingLoadContext);
                        childrenIterables.add(containerPhysicalChildren);
                    }
                }
                catch (OwException ce)
                {
                    throw ce;
                }
                catch (Exception e)
                {
                    String msg = "OwStandardSemiVirtualFolderAdapter.getChilds():Error enumerating virtual folder contents!";
                    LOG.error(msg, e);
                    throw new OwServerException(new OwString("ecmimpl.OwStandardSemiVrtualFolderAdapter.children.enumeration.error", "Error enumerating virtual folder contents!"), e);
                }
            }
            else if (OwStandardObjectClass.isContentType(iObjectType_p))
            {
                // if configured, add all physical children (documents of the root folder of the virtual file)
                if (semiVirtualFolder_p.includesPhysicalChildren())
                {
                    // content type, call parent getChilds() method
                    OwIterable<OwObject> containerPhysicalChildren = semiVirtualFolder_p.getPhysicalChildren(workingLoadContext);
                    childrenIterables.add(containerPhysicalChildren);
                }
            }
            else
            {
                //delegate for any other type requests, directly to ECM object
                OwIterable<OwObject> containerPhysicalChildren = semiVirtualFolder_p.getPhysicalChildren(workingLoadContext);
                childrenIterables.add(containerPhysicalChildren);
            }
        }

        return OwAggregateIterable.forList(childrenIterables);
    }

    @SuppressWarnings("unchecked")
    public synchronized OwVirtualFolderObject getVirtualFolder(OwSemiVirtualFolder semiVirtualFolder_p) throws OwException
    {
        if (null == this.virtualFolderCache)
        {
            try
            {
                this.virtualFolderCache = (OwVirtualFolderObject) this.network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER, semiVirtualFolder_p.getVirtualFolderName(), false, false);
                OwPropertyCollection folderProperties = semiVirtualFolder_p.getProperties(null);
                Set<Entry<String, OwProperty>> allProperties = folderProperties.entrySet();
                OwPropertyCollection virtualProperties = new OwStandardPropertyCollection();
                for (Entry<String, OwProperty> entry : allProperties)
                {
                    OwProperty property = entry.getValue();
                    OwPropertyClass pClass = property.getPropertyClass();

                    if (!pClass.getClassName().equals(OwResource.m_ObjectNamePropertyClass.getClassName()))
                    {
                        OwProperty propertyCopy = new OwStandardProperty(property.getValue(), pClass);
                        virtualProperties.put(entry.getKey(), propertyCopy);
                    }
                }

                this.virtualFolderCache.setProperties(virtualProperties);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("OwStandardSemiVirtualFolderAdapter.getVirtualFolder():Invalid virtual folder!", e);
                throw new OwInvalidOperationException(new OwString("app.OwStandardSemiVrtualFolderAdapter.invalid.virtual.folder", "Invalid virtual folder!"), e);

            }
        }

        return this.virtualFolderCache;
    }

    /** (overridable)
     * Convert the given property value to a value compatible with given search criteria.
     * The default implementation returns the same value if java classes are the same in
     * the property and the search criteria (see {@link OwSearchCriteria#getJavaClassName()} and {@link OwFieldDefinition#getJavaClassName()}). 
     * If the two classes don't match search a search-criteria based string conversion is performed    
     * on the properties value string representation.
     * 
     * @param searchCriteria_p
     * @param folderProperty_p
     * @return converted Object value  
     * @throws Exception
     */
    protected Object convertPropertyValue(OwSearchCriteria searchCriteria_p, OwProperty folderProperty_p) throws Exception
    {

        String criteriaJavaClass = searchCriteria_p.getJavaClassName();
        OwFieldDefinition propertyFieldDefinition = folderProperty_p.getFieldDefinition();
        String propertyJavaClass = propertyFieldDefinition.getJavaClassName();

        if (criteriaJavaClass.equals(propertyJavaClass))
        {
            return folderProperty_p.getValue();
        }
        else
        {
            // convert data type
            Object value = folderProperty_p.getValue();
            if (null == value)
            {
                return null;
            }

            return searchCriteria_p.getValueFromString(value.toString());
        }
    }

    /**
     * Called to propagate the defined set of properties from
     * &quot;physical&quot; parent to it's virtual children.
     * Will either return a map of propagated properties and depending values,
     * or an empty map if no propagation was done.
     * @param rootObj_p OwSemiVirtualFolder to use for propagation
     * @param search_p OwSearchNode where to map the properties
     * @return Map of propagated properties and depending values.
     * @throws Exception
     * @since 3.2.0.0
     */
    protected Map propagateRootProperties(OwSemiVirtualFolder rootObj_p, OwSearchNode search_p) throws Exception
    {
        Map searchCriteriaMap = search_p.getCriteriaMap(OwSearchNode.FILTER_NONE);

        // iterate through all virtual folder properties defined in owbootstrap.xml, retrieve
        // the corresponding folder property values and set them into the virtual folder
        // search criteria
        Map propertyMap = rootObj_p.getPropertyMap();
        Set virtualFolderProperties = propertyMap.entrySet();
        Iterator it = virtualFolderProperties.iterator();

        Map propagationMap = new HashMap();

        while (it.hasNext())
        {
            Map.Entry propMapEntry = (Map.Entry) it.next();
            String virtualFolderProperty = (String) propMapEntry.getKey();

            // get the corresponding folder property
            String sFolderProperty = (String) propMapEntry.getValue();
            OwProperty theProperty = null;
            if (sFolderProperty != null)
            {
                theProperty = rootObj_p.getProperty(sFolderProperty);
            }

            if (theProperty != null)
            {
                Object propagationValue = theProperty.getValue();

                if (virtualFolderProperty.equals(OwSearchPathField.CLASS_NAME))
                {

                    //add a new search path
                    String resourceId = rootObj_p.getResourceID();
                    OwSearchObjectStore searchStore = new OwSearchObjectStore(resourceId, null);
                    Object thePropertyValue = theProperty.getValue();
                    OwSearchPath searchPath = new OwSearchPath(null, thePropertyValue.toString(), rootObj_p.searchSubstructure(), searchStore);

                    OwSearchNode specialNode = search_p.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

                    if (specialNode == null)
                    {
                        specialNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
                        search_p.add(specialNode);
                    }

                    List specialChildren = specialNode.getChilds();
                    OwSearchNode searchPathNode = null;
                    for (Iterator k = specialChildren.iterator(); k.hasNext();)
                    {
                        OwSearchNode specialChild = (OwSearchNode) k.next();
                        OwSearchCriteria specialCriteria = specialChild.getCriteria();
                        if (VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY.equals(specialCriteria.getUniqueName()) && OwSearchPathField.CLASS_NAME.equals(specialCriteria.getClassName()))
                        {
                            searchPathNode = specialChild;
                            break;
                        }
                    }
                    if (searchPathNode == null)
                    {
                        //new OwSearchPathField.OwSearchPathFieldClass()
                        searchPathNode = new OwSearchNode(OwSearchPathField.classDescription, OwSearchOperator.MERGE_NONE, searchPath, OwSearchCriteria.ATTRIBUTE_HIDDEN, VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY, "vfPathInstruction", null);
                        specialNode.add(searchPathNode);
                    }
                    else
                    {
                        OwSearchCriteria searchPathNodeCriteria = searchPathNode.getCriteria();
                        searchPathNodeCriteria.setValue(searchPath);
                    }
                }
                else
                {
                    if (searchCriteriaMap.containsKey(virtualFolderProperty))
                    {
                        // if the configured virtual folder property also exists in the search template criteria map...

                        // set the value of the folder property into the corresponding search criteria of the virtual folder search template
                        OwSearchCriteria searchCriteria = (OwSearchCriteria) searchCriteriaMap.get(virtualFolderProperty);
                        Object theValue = convertPropertyValue(searchCriteria, theProperty);
                        searchCriteria.setValue(theValue);

                        propagationValue = theValue;
                    }

                    propagationMap.put(virtualFolderProperty, propagationValue);
                }
            }
            else
            {
                LOG.warn("OwStandardSemiVirtualFolderAdapter.getChilds(): No such property " + sFolderProperty + " in semi virtual folder");
            }
        }
        return propagationMap;
    }
}
