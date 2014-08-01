package com.alfresco.ow.contractmanagement.virtualfolder;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.plug.owlink.OwReferencedObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwContractManagementVirtualFolderObjectFactory.
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
 **/
public class OwContractManagementVirtualFolderObjectFactory extends OwStandardVirtualFolderObjectFactory
{

    private static final Logger LOG = OwLog.getLogger(OwContractManagementVirtualFolderObjectFactory.class);

    protected OwObjectCollection m_linkObjectCollection = null;
    protected OwReferenceNodeHelper m_referenceNodeHelper = null;

    private List<String> aspectIds = null;

    /**
     * Returns link objects.
     * @return  link objects
     * @throws Exception
     */
    protected OwObjectCollection getLinkObjects() throws Exception
    {
        if (this.isRoot())
        {
            String strPath = this.getProperty("cmis:folder.cmis:path").getValue().toString();
            OwObject rootObject = this.getRepository().getObjectFromPath(strPath, false);

            Collection<String> propertyNames = null;
            OwSort sort = null;
            int maxSize = Integer.MAX_VALUE / 2;
            int versionSelection = 0;
            OwSearchNode filterCriteria = null;
            try
            {
                int[] linkTypes = new int[] { OwObjectReference.OBJECT_TYPE_LINK };
                if (rootObject.hasChilds(linkTypes, versionSelection))
                {
                    m_linkObjectCollection = rootObject.getChilds(linkTypes, propertyNames, sort, maxSize, versionSelection, filterCriteria);
                }
                return m_linkObjectCollection;
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not retrieve object links.", e);
            }
        }
        else
        {
            return ((OwContractManagementVirtualFolderObjectFactory) this.getRootObject()).getLinkObjects();
        }
    }

    /**
     * Checks if is reference
     * @return true if is reference
     */
    public boolean isReference()
    {
        return m_referenceNodeHelper != null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    @Override
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {

        if (this.isReference())
        {
            OwObjectCollection linkObjects = this.getLinkObjects();
            return this.m_referenceNodeHelper.getAssociatedObjects(linkObjects);
        }

        return super.getChilds(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory#getFolderChilds()
     */
    @Override
    protected OwObjectCollection getFolderChilds() throws Exception
    {
        if (m_folderChilds == null)//TODO refactor to avoid duplication of code
        {
            m_folderChilds = new OwStandardObjectCollection();

            for (Node n = m_OpenFolderNode.getNode().getFirstChild(); n != null; n = n.getNextSibling())
            {
                OwStandardXMLUtil nodeUtil = new OwStandardXMLUtil(n);

                if (n.getNodeName().equals(OwReferenceNodeHelper.REFERENCE_TAG_NAME))
                {
                    String directionStr = nodeUtil.getSafeStringAttributeValue(OwReferenceNodeHelper.REFERENCE_ATTRIBUTE_DIRECTION, "NONE");
                    OwObjectLinkRelation direction = OwObjectLinkRelation.valueOf(directionStr);

                    List<String> linkclasses = new LinkedList<String>();
                    List classNameUtilList = nodeUtil.getSafeUtilList(OwReferenceNodeHelper.REFERENCE_LINK_CLASSES, OwReferenceNodeHelper.REFERENCE_LINK_CLASSES_NAME);
                    for (Iterator<?> iterator = classNameUtilList.iterator(); iterator.hasNext();)
                    {
                        OwXMLUtil classNameUtil = (OwXMLUtil) iterator.next();

                        //check if virtual reference node rendering depends on an aspect
                        String aspectName = classNameUtil.getSafeStringAttributeValue("aspect", "");
                        if (!aspectName.isEmpty() && !hasAspect(aspectName))
                        {
                            continue;
                        }

                        String className = classNameUtil.getSafeTextValue(null);
                        if (className != null && !className.isEmpty())
                        {
                            linkclasses.add(className);
                        }
                    }

                    //no link classes defined, skip reference node
                    if (linkclasses.isEmpty())
                    {
                        continue;
                    }

                    OwReferenceNodeHelper refNodeHelper = new OwReferenceNodeHelper(direction, linkclasses);

                    String strSubPath = n.getAttributes().getNamedItem(NODE_ATTRIBUTE_NAME).getNodeValue();

                    OwContractManagementVirtualFolderObjectFactory newChild = (OwContractManagementVirtualFolderObjectFactory) createNewSubfolderInstance();

                    newChild.setPropagationMap(this.getPropagationMap());

                    // the one and only parent
                    newChild.m_parent = this;

                    newChild.m_referenceNodeHelper = refNodeHelper;

                    // sub path
                    Node childRootNode = null;

                    if (m_objectPath.length() == 1)
                    {
                        newChild.m_objectPath = OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }
                    else
                    {
                        newChild.m_objectPath = m_objectPath + OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }

                    if (m_nodePath.length() == 1)
                    {
                        newChild.m_nodePath = OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }
                    else
                    {
                        newChild.m_nodePath = m_nodePath + OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }
                    childRootNode = getRootNode();

                    newChild.init(this.getContext(), m_repository, m_strDMSID, getVirtualFolderName(), childRootNode);

                    m_folderChilds.add(newChild);
                }

                if (n.getNodeName().equals(NODE_TAG_NAME))
                {
                    String strSubPath = n.getAttributes().getNamedItem(NODE_ATTRIBUTE_NAME).getNodeValue();

                    OwContractManagementVirtualFolderObjectFactory newChild = (OwContractManagementVirtualFolderObjectFactory) createNewSubfolderInstance();

                    newChild.setPropagationMap(this.getPropagationMap());

                    // the one and only parent
                    newChild.m_parent = this;

                    OwXMLUtil searchSubUtil = nodeUtil.getSubUtil(OwStandardVirtualFolderObjectFactory.SEARCH_NODE_TAG_NAME);
                    // sub path
                    Node childRootNode = null;

                    if (m_objectPath.length() == 1)
                    {
                        newChild.m_objectPath = OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }
                    else
                    {
                        newChild.m_objectPath = m_objectPath + OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                    }

                    if (searchSubUtil == null)
                    {
                        if (m_nodePath.length() == 1)
                        {
                            newChild.m_nodePath = OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                        }
                        else
                        {
                            newChild.m_nodePath = m_nodePath + OwObject.STANDARD_PATH_DELIMITER + strSubPath;
                        }
                        childRootNode = getRootNode();
                    }
                    else
                    {
                        newChild.m_nodePath = OwObject.STANDARD_PATH_DELIMITER;
                        childRootNode = n;
                    }

                    newChild.init(this.getContext(), m_repository, m_strDMSID, getVirtualFolderName(), childRootNode);

                    if (searchSubUtil != null)
                    {
                        //in depth criteria value propagation

                        OwSearchTemplate childTemplate = newChild.getSearchTemplate();
                        OwSearchNode childSearch = childTemplate.getSearch(false);
                        Map childCriteriaMap = childSearch.getCriteriaMap(OwSearchNode.FILTER_NONE);

                        OwSearchTemplate thisSearchTemplate = getSearchTemplate();
                        OwSearchNode thisSearch = thisSearchTemplate.getSearch(false);

                        List refineCriteriaList = getRefineCriteriaList();
                        thisSearch = getRefinedSearch(thisSearch, refineCriteriaList);

                        Map thisCriteriaMap = thisSearch.getCriteriaMap(OwSearchNode.FILTER_NONE);
                        Set thisEntries = thisCriteriaMap.entrySet();
                        for (Iterator i = thisEntries.iterator(); i.hasNext();)
                        {
                            Entry criteriaEntry = (Entry) i.next();
                            Object criteriaKey = criteriaEntry.getKey();
                            if (!OwStandardClassSelectObject.CLASS_NAME.equals(criteriaKey) && !OwStandardClassSelectObject.FROM_NAME.equals(criteriaKey) && !OwStandardClassSelectObject.SUBCLASSES_NAME.equals(criteriaKey))
                            {
                                OwSearchCriteria criteria = (OwSearchCriteria) criteriaEntry.getValue();
                                Object value = criteria.getValue();
                                if (childCriteriaMap.containsKey(criteriaKey))
                                {
                                    OwSearchCriteria childCriteria = (OwSearchCriteria) childCriteriaMap.get(criteriaKey);
                                    //don't overwrite default values
                                    if (childCriteria.getDefaultValue() == null)
                                    {
                                        childCriteria.setValue(value);
                                    }
                                }
                                else if (OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY.equals(criteriaKey))
                                {
                                    OwSearchNode specialNode = childSearch.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

                                    if (specialNode == null)
                                    {
                                        specialNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
                                        childSearch.add(specialNode);
                                    }

                                    OwSearchNode searchPathNode = new OwSearchNode(OwSearchPathField.classDescription, OwSearchOperator.MERGE_NONE, value, OwSearchCriteria.ATTRIBUTE_HIDDEN,
                                            OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY, "vfPathInstruction", null);
                                    specialNode.add(searchPathNode);
                                }
                            }
                        }
                    }

                    m_folderChilds.add(newChild);
                }
            }
        }

        return m_folderChilds;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory#createNewSubfolderInstance()
     */
    @Override
    protected OwStandardVirtualFolderObjectFactory createNewSubfolderInstance() throws Exception
    {

        return new OwContractManagementVirtualFolderObjectFactory();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory#getSubNode(java.lang.String)
     */
    @Override
    protected Node getSubNode(String strPath_p) throws Exception
    {

        StringTokenizer tokenizer = new StringTokenizer(strPath_p, "/");

        Node node = getRootNode();

        while (tokenizer.hasMoreElements())
        {
            String strPathToken = tokenizer.nextToken();

            boolean fFound = false;
            for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling())
            {
                // node name matches the path token ?
                if (ROOT_NODE_TAG_NAME.equals(n.getNodeName()) || NODE_TAG_NAME.equals(n.getNodeName()) || OwReferenceNodeHelper.REFERENCE_TAG_NAME.equals(n.getNodeName()))
                {
                    String strName = n.getAttributes().getNamedItem("name").getNodeValue();
                    if (strName.equals(strPathToken))
                    {
                        // === found node for requested path token
                        // match next path token
                        node = n;
                        fFound = true;
                        break;
                    }
                }
            }

            // === path not resolved
            if (!fFound)
            {
                return null;
            }
        }

        return node;
    }

    /**
     * returns the value of VirtualFolderSearchPathProperty from a search template
     * @return the value of VirtualFolderSearchPathProperty from a search template
     * @throws Exception
     */
    public String getVirtualFolderSearchPath() throws Exception
    {
        OwSearchNode node = this.getSearchTemplate().getSearch(false);
        Object searchCriteria = node.getCriteriaMap(OwSearchNode.FILTER_HIDDEN).get("VirtualFolderSearchPathProperty");
        if (searchCriteria != null && searchCriteria instanceof OwSearchCriteria)
        {
            Object value = ((OwSearchCriteria) searchCriteria).getValue();
            if (value != null && value instanceof String)
            {
                return (String) value;
            }
        }

        return null;
    }

    /**
     * Check if the underlying object has an aspect id with given name.
     * 
     * @param aspectName aspect id
     * @return true if the underlying object has the aspect
     */
    protected boolean hasAspect(String aspectName)
    {
        if (aspectIds == null)
        {
            aspectIds = getAspectIDs();
        }

        return aspectIds.contains(aspectName);
    }

    /**
     * Get all aspect of the current underlying alfresco cmis object.
     * If the underlying object isn't an alfresco object an empty list will be returned.
     * 
     * @return List of aspect ids
     */
    private List<String> getAspectIDs()
    {
        List<String> aspects = new LinkedList<String>();

        Object nativeObject = null;
        //works only for cmis
        if (this.m_PropertyMap.containsKey("cmis:folder.cmis:path"))
        {
            try
            {
                String path = this.getProperty("cmis:folder.cmis:path").getValue().toString();
                OwObject physcialObject = this.getRepository().getObjectFromPath(path, false);
                nativeObject = physcialObject.getNativeObject();
            }
            catch (Exception e)
            {
                LOG.error("getAspectIDs: can't get aspect ids from native object.", e);
            }
        }
        //aspect support works only for alfresco
        if (nativeObject != null && nativeObject instanceof AlfrescoAspects)
        {
            Collection<ObjectType> aspectTypes = ((AlfrescoAspects) nativeObject).getAspects();
            for (ObjectType objectType : aspectTypes)
            {
                aspects.add(objectType.getId());
            }
        }
        return aspects;
    }

    private static class OwReferenceNodeHelper
    {
        protected static final String REFERENCE_TAG_NAME = "reference";
        protected static final String REFERENCE_ATTRIBUTE_DIRECTION = "direction";
        protected static final String REFERENCE_LINK_CLASSES = "linkclasses";
        protected static final String REFERENCE_LINK_CLASSES_NAME = "name";

        protected OwObjectLinkRelation m_direction;
        protected List<String> m_filterClassNames;

        public OwReferenceNodeHelper(OwObjectLinkRelation direction_p, List<String> filterClassNames_p)
        {
            this.m_direction = direction_p;
            this.m_filterClassNames = filterClassNames_p;
        }

        @SuppressWarnings("unchecked")
        protected OwObjectCollection getAssociatedObjects(OwObjectCollection linkObjects) throws Exception
        {
            OwObjectCollection associatedObjects = new OwStandardObjectCollection();
            if (linkObjects == null || linkObjects.isEmpty())
            {
                return associatedObjects;
            }

            for (Iterator<?> iterator = linkObjects.iterator(); iterator.hasNext();)
            {
                OwObjectLink linkObj = (OwObjectLink) iterator.next();

                if (filterOut(linkObj))
                {
                    continue;
                }

                OwReferencedObject obj;
                switch (m_direction)
                {
                    case INBOUND:
                        obj = new OwReferencedObject(linkObj, (OwObject) linkObj.getSource());
                        associatedObjects.add(obj);
                        break;
                    case OUTBOUND:
                        obj = new OwReferencedObject(linkObj, (OwObject) linkObj.getTarget());
                        associatedObjects.add(obj);
                        break;
                    case BOTH:
                        obj = new OwReferencedObject(linkObj, (OwObject) linkObj.getSource());
                        associatedObjects.add(obj);
                        obj = new OwReferencedObject(linkObj, (OwObject) linkObj.getTarget());
                        associatedObjects.add(obj);
                        break;
                    default://nothing to do;
                }
            }
            return associatedObjects;
        }

        protected boolean filterOut(OwObjectLink link)
        {
            return m_filterClassNames != null && !m_filterClassNames.isEmpty() && !m_filterClassNames.contains(link.getClassName());
        }
    }
}
