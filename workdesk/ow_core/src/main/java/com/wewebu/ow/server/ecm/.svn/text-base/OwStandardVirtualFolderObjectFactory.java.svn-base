package com.wewebu.ow.server.ecm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.collections.OwAggregateIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwObjectCollectionIterableAdapter;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwHeaderFieldColumnInfo;
import com.wewebu.ow.server.field.OwHeaderFieldColumnInfoDecorator;
import com.wewebu.ow.server.field.OwPriorityRule;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base Class for virtual folder objects.<br/>
 * Folder Objects can contain other objects like documents and sub folders.
 * They can be real physical folders or virtual search based folders.
 * OwVitualFolderObject implements a search based virtual folder, which is feed by a XML description.
 * Both virtual and real folders are treated the same way.<br/>
 * Virtual Folders are made up of Searches. 
 * I.e. each node creates a OwSearchNode Object which result list acts as the nodes objects.<br/><br/>
 * To be implemented with the specific ECM system. 
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwStandardVirtualFolderObjectFactory implements OwVirtualFolderObjectFactory, OwVirtualFolderObject
{
    private static final Logger LOG = OwLogCore.getLogger(OwStandardVirtualFolderObjectFactory.class);

    /** the MIME type of the virtual folder */
    public static final String MIME_TYPE = OwMimeManager.MIME_TYPE_PREFIX_OW_DEFAULT + "virtual_folder";
    /** attribute in NODE to define an explicit MIME type
     * @since 4.2.0.0*/
    public static final String NODE_ATTRIBUTE_MIME_TYPE = "mimetype";
    /** name of the containing documents attribute flag */
    protected static final String NODE_ATTRIBUTE_CONTAINS_DOCUMENTS = "containsdocs";
    /** name of the for the name attribute */
    protected static final String NODE_ATTRIBUTE_NAME = "name";
    /** name of the node tag */
    protected static final String NODE_TAG_NAME = "node";

    /** node name of the search node */
    protected static final String SEARCH_NODE_TAG_NAME = "search";

    /** node name of the refine criteria node */
    protected static final String REFINE_CRITERIA_NODE_TAG_NAME = "refinecriteria";

    /** the one and only class description for the virtual folder objects */
    protected static final OwVirtualFolderObjectClass m_StandardClassDescription = new OwVirtualFolderObjectClass();
    /** cached child list */
    protected OwObjectCollection m_folderChilds;

    /** parent */
    protected OwStandardVirtualFolderObjectFactory m_parent;

    /** reference to the repository */
    protected OwRepository m_repository;

    private OwNetworkContext m_context;

    /** root node describing the virtual folder */
    protected Node m_rootNode;

    /** map containing the properties of the virtual folder object */
    protected OwPropertyCollection m_PropertyMap;

    /** the path to the currently opened sub folder */
    protected String m_nodePath = OwObject.STANDARD_PATH_DELIMITER;

    /** the path to the currently opened sub folder */
    protected String m_objectPath = OwObject.STANDARD_PATH_DELIMITER;

    /** the OwXMLUtil wrapped DOM node of the currently opened sub folder */
    protected OwXMLUtil m_OpenFolderNode;

    /** ID of virtual folder used in getDMSID to recreate the folder */
    protected String m_strDMSID;

    /** search template to use for this node, can be null */
    protected OwSearchTemplate m_SearchTemplate;

    /** a List of criteria to refine this node */
    protected List m_refinementCriteriaList;

    /** a set of criteria names to refine the search template */
    protected Set m_searchTemplateRefinementCriteriaNameSet;

    private Map searchCriteriaMap;

    private Map propagationMap;

    private String m_virtualFolderName;

    public void init(OwNetworkContext context_p, OwRepository repository_p, String strBaseDMSID_p, String virtualFolderName_p, Node rootNode_p) throws Exception
    {
        m_repository = repository_p;
        m_context = context_p;

        m_virtualFolderName = virtualFolderName_p;

        // set ID for getDMSID Function
        m_strDMSID = strBaseDMSID_p;

        // create a reference to the XML description of the virtual folder
        m_rootNode = rootNode_p;

        // set references to the currently opened sub folder
        m_OpenFolderNode = new OwStandardXMLUtil(getSubNode(m_nodePath));

        // create properties, use node name for the requested sub path
        createProperties(nameFromNode(m_OpenFolderNode));

    }

    private String nameFromNode(OwXMLUtil folderNode_p)
    {
        return folderNode_p.getNode().getAttributes().getNamedItem(NODE_ATTRIBUTE_NAME).getNodeValue();
    }

    private void validateSearchTemplate() throws Exception, OwConfigurationException
    {
        List refineCriteriaList = this.getRefineCriteriaList();
        for (Iterator iterator = refineCriteriaList.iterator(); iterator.hasNext();)
        {
            OwVirtualFolderRefineCriteria refCriteria = (OwVirtualFolderRefineCriteria) iterator.next();
            this.validateRefineCriteria(refCriteria.m_strCriteria);
        }
    }

    /** Empty default constructor. Use init-method to initialize */
    public OwStandardVirtualFolderObjectFactory()
    {

    }

    /** (overridable) create a virtual folder object, used internally in getChilds
     * @return OwStandardVirtualFolderObjectFactory
     */
    protected OwStandardVirtualFolderObjectFactory createNewSubfolderInstance() throws Exception
    {
        return new OwStandardVirtualFolderObjectFactory();
    }

    /** get context */
    public OwNetworkContext getContext()
    {
        return m_context;
    }

    /** get repository */
    public OwRepository getRepository()
    {
        return m_repository;
    }

    /** get the root DOM Node of the folder structure
     * @return DOM Node
     */
    protected Node getRootNode() throws Exception
    {
        return m_rootNode;
    }

    /** retrieve a sub DOM Node from the structure XML document using a string path
     * @param strPath_p String path to the sub node
     */
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
                if (ROOT_NODE_TAG_NAME.equals(n.getNodeName()) || NODE_TAG_NAME.equals(n.getNodeName()))
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

    /** (overridable) create all properties of the file. There are so little, we just load all at once
     * 
     * @param strName_p String the name to set the name property
     */
    protected void createProperties(String strName_p) throws Exception
    {
        if (m_PropertyMap == null)
        {
            m_PropertyMap = new OwStandardPropertyCollection();

            // localized name property
            String localizedName = OwString.localizeLabel(m_context.getLocale(), strName_p, strName_p);
            m_PropertyMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), new OwStandardProperty(localizedName, getObjectClass().getPropertyClass(OwResource.m_ObjectNamePropertyClass.getClassName())));
        }
    }

    /** check if this is the root folder */
    public boolean isRoot() throws Exception
    {
        return (this == getRootObject());
    }

    // === functions inherited from OwObject
    /** get Object name property string
     * @return the name property string of the object
     */
    public String getName()
    {
        try
        {
            // get name from map
            return ((OwStandardProperty) m_PropertyMap.get(OwResource.m_ObjectNamePropertyClass.getClassName())).getValue().toString();
        }
        catch (Exception e)
        {
            return "[undef]";
        }
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        return getName();
    }

    /** get from the given node, a list of criteria to refine the search template 
     *
     * @return Set of RefineCriteria
     */
    protected List getRefineCriteriaList() throws Exception
    {
        // is list already created from node ?
        if (m_refinementCriteriaList == null)
        {
            // === get refine criteria node if defined
            m_refinementCriteriaList = getRefineCriteriaListForNode(this.m_OpenFolderNode);
            OwStandardVirtualFolderObjectFactory bottomUpScan = this.m_parent;
            while (bottomUpScan != null && bottomUpScan.getRootNode() != this.getRootNode())
            {
                m_refinementCriteriaList.addAll(0, bottomUpScan.getRefineCriteriaList());
                bottomUpScan = bottomUpScan.m_parent;
            }
        }

        return m_refinementCriteriaList;
    }

    /** get from the whole search template, a list of criteria to refine the search template 
    *
    * @return Set of OwVirtualFolderRefineCriteria
    */
    protected Set getSearchTemplateRefineCriteriaNameSet() throws Exception
    {
        // is Set already created from node ?
        if (m_searchTemplateRefinementCriteriaNameSet == null)
        {
            // === get refine criteria node if defined
            m_searchTemplateRefinementCriteriaNameSet = new HashSet();
            scanSearchNode(this.m_rootNode);
            if (m_searchTemplateRefinementCriteriaNameSet == null || m_searchTemplateRefinementCriteriaNameSet.size() == 0)
            {
                if (getParent() != null && getParent().getSearchTemplate().equals(getSearchTemplate()))
                {
                    throw new OwConfigurationException("The virtual Searchtemplate seems to have no refine criteria!");
                }
            }
            // Avoid Nullpointer Exception
            if (propagationMap != null)
            {
                m_searchTemplateRefinementCriteriaNameSet.addAll(propagationMap.keySet());
            }

        }

        return m_searchTemplateRefinementCriteriaNameSet;
    }

    /** Recursively traverse the DOM nodes of the search template to collect refine criteria names.
     *
     * @param searchDOMNode_p node to traverse further
     */
    protected void scanSearchNode(Node searchDOMNode_p) throws Exception
    {
        if (searchDOMNode_p.getNodeType() == Node.ELEMENT_NODE && NODE_TAG_NAME.equals(searchDOMNode_p.getNodeName()))
        {
            List nodeList = getRefineCriteriaListForNode(new OwStandardXMLUtil(searchDOMNode_p));
            if (nodeList != null)
            {
                for (Iterator iterator = nodeList.iterator(); iterator.hasNext();)
                {
                    OwVirtualFolderRefineCriteria refineCriteria = (OwVirtualFolderRefineCriteria) iterator.next();
                    m_searchTemplateRefinementCriteriaNameSet.add(refineCriteria.m_strCriteria);
                }
            }
        }

        // traverse sub DOM nodes
        for (Node n = searchDOMNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeType() == Node.ELEMENT_NODE && NODE_TAG_NAME.equals(n.getNodeName()))
            {
                scanSearchNode(n);
            }
        }
    }

    /**
     * return a list of refine criteria for the given node
     * @param node_p OwXMLUtil parent node where to search
     * @return List of OwVirtualFolderRefineCriteria
     */
    private List getRefineCriteriaListForNode(OwXMLUtil node_p)
    {
        List refineCriteriaList = new LinkedList();
        List refineNodes = node_p.getSafeNodeList(REFINE_CRITERIA_NODE_TAG_NAME);
        if (refineNodes != null)
        {
            Iterator it = refineNodes.iterator();
            // iterate over the refinement criteria
            while (it.hasNext())
            {
                Node n = (Node) it.next();

                String strCriteriaName = n.getAttributes().getNamedItem("symname").getNodeValue();

                // get literal node
                Node l = null;
                if (n.hasChildNodes())
                {
                    l = n.getFirstChild();
                }

                // create new list item
                refineCriteriaList.add(new OwVirtualFolderRefineCriteria(strCriteriaName, l));
            }
        }
        return refineCriteriaList;
    }

    /**
     * validates that the given refine criteria name (in the node definition) exists also in where clause.
     * @param strCriteriaName_p
     */
    private void validateRefineCriteria(String strCriteriaName_p) throws OwConfigurationException
    {
        if (searchCriteriaMap == null)
        {
            try
            {
                OwSearchNode searchTree = this.getSearchTemplate().getSearch(false);
                searchCriteriaMap = searchTree.getCriteriaMap(OwSearchNode.FILTER_NONE);
            }
            catch (Exception e)
            {

                throw new OwConfigurationException("Could not get Searchtemplate to validate virtual search.", e);
            }
        }
        if (searchCriteriaMap.get(strCriteriaName_p) == null)
        {
            throw new OwConfigurationException("Invalid virtual Searchtemplate! RefineCriteria " + strCriteriaName_p + " not defined in WHERE clause!");
        }
    }

    /** refine a search according to the given refine criteria list
     * 
     * @param search_p OwSearchNode to refine
     * @param refineList_p List of OwVirtualFolderRefineCriteria for refinement
     *
     * @return OwSearchNode refined search
     */
    protected OwSearchNode getRefinedSearch(OwSearchNode search_p, List refineList_p) throws Exception
    {
        // do not refresh the search, because we want to access the cached one, which is displayed in the SearchView
        List criteriaList = search_p.getCriteriaList(OwSearchNode.FILTER_NONE);

        // reset all corresponding search criteria of the refine criteria!
        resetAllRefineCriteria(criteriaList);

        // iterate over the search criteria and do refinements
        Iterator searchit = criteriaList.iterator();
        while (searchit.hasNext())
        {
            OwSearchCriteria searchcriteria = (OwSearchCriteria) searchit.next();

            Iterator refineit = refineList_p.iterator();
            while (refineit.hasNext())
            {
                OwVirtualFolderRefineCriteria refineCriteria = (OwVirtualFolderRefineCriteria) refineit.next();

                if (refineCriteria.m_strCriteria.equals(searchcriteria.getUniqueName()))
                {
                    Object value = resolveLiteralValue(getName(), searchcriteria, refineCriteria.m_literalNode);

                    // === found criteria to refine
                    searchcriteria.setValue(value);
                }
            }
        }

        return search_p;
    }

    /**
     * iterates over all refine criteria and reset all corresponding search criteria (in WHERE clause) to null.
     * @param criteriaList_p list of OwSearchCriteria of the virtual search template
     * @throws Exception
     */
    private void resetAllRefineCriteria(List criteriaList_p) throws Exception
    {
        Set searchTemplateRefineCriteriaNameSet = getSearchTemplateRefineCriteriaNameSet();
        // iterate over criteria of the search template
        for (Iterator iterator = criteriaList_p.iterator(); iterator.hasNext();)
        {
            OwSearchCriteria criteria = (OwSearchCriteria) iterator.next();
            String critName = criteria.getClassName();
            String uniqueCritName = criteria.getUniqueName();
            if (searchTemplateRefineCriteriaNameSet.contains(critName) || searchTemplateRefineCriteriaNameSet.contains(uniqueCritName))
            {
                if (getPropagationMap() != null && !getPropagationMap().isEmpty())
                {//process propagation here, since it is not a "real" refinement
                    if (getPropagationMap().containsKey(critName))
                    {
                        criteria.setValue(getPropagationMap().get(critName));
                    }
                    else
                    {
                        if (getPropagationMap().containsKey(uniqueCritName))
                        {
                            criteria.setValue(getPropagationMap().get(uniqueCritName));
                        }
                        else
                        {
                            criteria.setValue(criteria.getDefaultValue());
                        }
                    }
                }
                else
                {//set value to default value if predefined, otherwise will be null
                    if (criteria.isHidden() || criteria.isReadonly())
                    {
                        criteria.setValue(criteria.getDefaultValue());
                    }
                }
            }
        }
    }

    /** resolve the given literal node to a property / criteria value
     * 
     * @param contextname_p
     * @param propClass_p
     * @param literal_p
     * @return an {@link Object}
     * @throws Exception
     */
    protected Object resolveLiteralValue(String contextname_p, OwFieldDefinition propClass_p, Node literal_p) throws Exception
    {
        if (literal_p == null)
        {
            return null;
        }

        // check if given literal is a placeholder
        String literal = literal_p.getNodeValue();

        if (null == literal)
        {
            return propClass_p.getValueFromNode(literal_p);
        }

        // Bug 1003: check for escaped parenthesis
        if (literal.startsWith("\\" + OwStandardSearchTemplate.LITERAL_PLACEHOLDER_LEFT_DELIMITER))
        {
            return propClass_p.getValueFromString(literal.substring(1));
        }

        if (literal.startsWith(OwStandardSearchTemplate.LITERAL_PLACEHOLDER_LEFT_DELIMITER) && literal.endsWith(OwStandardSearchTemplate.LITERAL_PLACEHOLDER_RIGHT_DELIMITER))
        {
            // Bug 1003: check for doc id's like {AE901EAC-BF45-449A-9572-B09F0466066A}
            if (OwStandardSearchTemplate.isDocId(literal))
            {
                return propClass_p.getValueFromNode(literal_p);
            }
            else
            {
                // === resolve placeholder
                return resolveLiteralPlaceholder(contextname_p, literal.substring(1, literal.length() - 1));
            }
        }
        else
        {
            return propClass_p.getValueFromNode(literal_p);
        }
    }

    /** (overridable) resolve the given literal placeholder name to a property / criteria value
     * 
     * @param contextname_p String name of calling context, e.g. search template name
     * @param placeholdername_p String name of placeholder to retrieve value for
     * @return an {@link Object}
     * @throws Exception
     */
    protected Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception
    {
        return getContext().resolveLiteralPlaceholder(contextname_p, placeholdername_p);
    }

    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public String getClassName()
    {
        return getObjectClass().getClassName();
    }

    /** get the class description of the object, the class descriptions are defined by the ECM System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        return m_StandardClassDescription;
    }

    /** get the root virtual folder object
     * @return OwStandardVirtualFolderObjectFactory
     */
    protected OwStandardVirtualFolderObjectFactory getRootObject() throws Exception
    {
        if (getParent() != null)
        {
            // ask parent
            return getParent().getRootObject();
        }
        else
        {
            // no parents, so this is the root
            return this;
        }
    }

    /** virtual folders have only one parent,
     *  easy access to it, rather than using getParents
     *
     *  @return OwObject
     */
    protected OwStandardVirtualFolderObjectFactory getParent() throws Exception
    {
        return m_parent;
    }

    /** get the containing parent of this object, does NOT cache returned objects
     * @return Parent Object, or null if object does not have any parents
     */
    public OwObjectCollection getParents() throws Exception
    {
        if (m_parent != null)
        {
            OwStandardObjectCollection parents = new OwStandardObjectCollection();
            parents.add(m_parent);
            return parents;
        }
        else
        {
            return null;
        }
    }

    /** get the children of the object.
     * 
     * NOTE: Physical folders do not cache the returned objects, virtual folders do,
     *       because they are all based on identical objects
     * 
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of sub folders
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort sort criteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        OwSearchTemplate searchtemplate = getSearchTemplate();
        if (searchtemplate != null)
        {
            checkResetState();
            return getChilds(searchtemplate.getSearch(false), iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        try
        {
            OwSearchTemplate searchtemplate = getSearchTemplate();
            if (searchtemplate != null)
            {
                checkResetState();
                return getChildren(searchtemplate.getSearch(false), loadContext);
            }
            else
            {
                return null;
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException(e.getMessage(), e);
        }
    }

    protected OwObjectCollection getFolderChilds() throws Exception
    {
        if (m_folderChilds == null)
        {
            m_folderChilds = new OwStandardObjectCollection();

            for (Node n = m_OpenFolderNode.getNode().getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeName().equals(NODE_TAG_NAME))
                {
                    String strSubPath = n.getAttributes().getNamedItem(NODE_ATTRIBUTE_NAME).getNodeValue();

                    OwStandardVirtualFolderObjectFactory newChild = createNewSubfolderInstance();

                    newChild.setPropagationMap(this.getPropagationMap());

                    // the one and only parent
                    newChild.m_parent = this;

                    OwStandardXMLUtil nodeUtil = new OwStandardXMLUtil(n);
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

                    newChild.init(m_context, m_repository, m_strDMSID, m_virtualFolderName, childRootNode);

                    if (searchSubUtil != null)
                    {
                        //in depth criteria value propagation
                        OwSearchTemplate thisSearchTemplate = getSearchTemplate();
                        OwSearchTemplate childTemplate = newChild.getSearchTemplate();
                        if (thisSearchTemplate != childTemplate)
                        {
                            OwSearchNode childSearch = childTemplate.getSearch(false);
                            OwSearchNode thisSearch = thisSearchTemplate.getSearch(false);

                            List refineCriteriaList = getRefineCriteriaList();
                            thisSearch = getRefinedSearch(thisSearch, refineCriteriaList);

                            deepCriteriaValuePropagation(childSearch, thisSearch);
                        }
                    }

                    m_folderChilds.add(newChild);
                }
            }
        }

        return m_folderChilds;
    }

    /**
     * Propagation of criteria values from top search.
     * @param childNode OwSearchNode representing sub search definition
     * @param parentNode OwSearchNode representing parent search to be used for propagation
     * @throws Exception
     * @since 4.1.2
     */
    protected void deepCriteriaValuePropagation(OwSearchNode childNode, OwSearchNode parentNode) throws Exception
    {
        Map thisCriteriaMap = parentNode.getCriteriaMap(OwSearchNode.FILTER_NONE);
        Set thisEntries = thisCriteriaMap.entrySet();

        Map childCriteriaMap = childNode.getCriteriaMap(OwSearchNode.FILTER_NONE);
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
                    OwSearchNode specialNode = childNode.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

                    if (specialNode == null)
                    {
                        specialNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
                        childNode.add(specialNode);
                    }

                    OwSearchNode searchPathNode = new OwSearchNode(OwSearchPathField.classDescription, OwSearchOperator.MERGE_NONE, value, OwSearchCriteria.ATTRIBUTE_HIDDEN, OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY,
                            "vfPathInstruction", null);
                    specialNode.add(searchPathNode);
                }
            }
        }
    }

    private OwIterable<OwObject> getFolderChildren() throws Exception
    {
        return new OwObjectCollectionIterableAdapter<OwObject>(getFolderChilds());
    }

    /** get the children of the object.
     * 
     * NOTE: Physical folders do not cache the returned objects, virtual folders do,
     *       because they are all based on identical objects
     * 
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of sub folders
     *  
     * @param search_p OwSearchNode to use for virtual search
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort sort criteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(OwSearchNode search_p, int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        OwStandardObjectCollection retCollection = new OwStandardObjectCollection();

        for (int iObjectTypeindex = 0; iObjectTypeindex < iObjectTypes_p.length; iObjectTypeindex++)
        {
            int iObjectType = iObjectTypes_p[iObjectTypeindex];

            switch (iObjectType)
            {
            // === get other folder nodes
                case OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OBJECT_TYPE_FOLDER:
                {
                    OwObjectCollection folderChilds = getFolderChilds();
                    retCollection.addAll(folderChilds, iMaxSize_p);
                }
                    break;

                // === retrieve the documents of the virtual folder node, by performing a search
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                {
                    if (m_OpenFolderNode.getSafeBooleanAttributeValue(NODE_ATTRIBUTE_CONTAINS_DOCUMENTS, false))
                    {
                        // === find child documents of node
                        // refine search
                        List refineCriteriaList = getRefineCriteriaList();
                        OwSearchNode search = getRefinedSearch(search_p, refineCriteriaList);

                        // search children
                        if (iVersionSelection_p == OwSearchTemplate.VERSION_SELECT_DEFAULT)
                        {
                            iVersionSelection_p = getSearchTemplate().getVersionSelection();
                        }

                        OwObjectCollection searchResult = m_repository.doSearch(search, sort_p, propertyNames_p, iMaxSize_p, iVersionSelection_p);
                        retCollection.addAll(searchResult, iMaxSize_p);
                    }
                }
                    break;

                default:
                    // Ignore
                    break;
            }
        }

        return retCollection;
    }

    private OwIterable<OwObject> getChildren(OwSearchNode search_p, OwLoadContext loadContext) throws Exception
    {
        List<OwIterable<OwObject>> retCollection = new ArrayList<OwIterable<OwObject>>();
        int[] iObjectTypes_p = loadContext.getObjectTypes();
        int iVersionSelection_p = loadContext.getVersionSelection();

        for (int iObjectTypeindex = 0; iObjectTypeindex < iObjectTypes_p.length; iObjectTypeindex++)
        {
            int iObjectType = iObjectTypes_p[iObjectTypeindex];

            switch (iObjectType)
            {
            // === get other folder nodes
                case OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OBJECT_TYPE_FOLDER:
                {
                    retCollection.add(getFolderChildren());
                }
                    break;

                // === retrieve the documents of the virtual folder node, by performing a search
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                {
                    if (m_OpenFolderNode.getSafeBooleanAttributeValue(NODE_ATTRIBUTE_CONTAINS_DOCUMENTS, false))
                    {
                        // === find child documents of node
                        // refine search
                        List refineCriteriaList = getRefineCriteriaList();
                        OwSearchNode search = getRefinedSearch(search_p, refineCriteriaList);

                        // search children
                        if (iVersionSelection_p == OwSearchTemplate.VERSION_SELECT_DEFAULT)
                        {
                            iVersionSelection_p = getSearchTemplate().getVersionSelection();
                        }

                        retCollection.add(m_repository.doSearch(search, loadContext));
                    }
                }
                    break;

                default:
                    // Ignore
                    break;
            }
        }

        return OwAggregateIterable.forList(retCollection);
    }

    /**
     * (overridable)
     * Checks whether two given class descriptions (an {@link OwClass} and an {@link OwObjectClass}) are compatible
     * in the context of this virtual folder. <br /> 
     * This default implementation compares the names of the two descriptions ignoring case.
     * @param classInfo_p
     * @param objectClass_p
     * @return <code>true</code> if the given {@link OwObjectClass} is virtually compatible with the given {@link OwClass}<br>
     *         <code>false</code> otherwise
     * @since 2.5.2.0
     */
    protected boolean compatibleObjectClasses(OwClass classInfo_p, OwObjectClass objectClass_p)
    {
        String strObjectClassName = objectClass_p.getClassName();
        if (classInfo_p.getClassName().equalsIgnoreCase(strObjectClassName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /** modify the properties of an object to meet the criteria list of this virtual folders search template
     * 
     *  This method is used to add documents to a virtual folder. Since the contents of a virtual folder is
     *  the result of a search template, a file in a virtual folder must meet all criteria of this search
     *  template.
     *  
     * @param objectClass_p OwObjectClass of the new document
     * @param properties_p OwPropertyCollection to be modified
     *  
     */
    public void setFiledObjectProperties(OwObjectClass objectClass_p, OwPropertyCollection properties_p) throws Exception
    {
        // get the search template of this virtual folder
        OwSearchTemplate searchtemplate = getSearchTemplate();

        // get the special node in this search template
        OwSearchNode specialNode = searchtemplate.getSearch(false).findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        // check if the object class of the new document is part of the search
        if (null != specialNode)
        {
            boolean classInSearchTemplate = false;
            Object[] classes = findSearchTemplateClassesList(specialNode);
            if (null != classes)
            {
                for (int i = 0; i < classes.length; i++)
                {
                    OwClass classinfo = (OwClass) classes[i];
                    if (compatibleObjectClasses(classinfo, objectClass_p))
                    {
                        classInSearchTemplate = true;
                    }

                }
                if (!classInSearchTemplate)
                {
                    String msg = getContext().localize("OwStandardVirtualFolderObjectFactory.classmismatch", "The class of the new object must comply with the class of the virtual folder.");
                    LOG.error("OwStandardVirtualFolderObjectFactory.setFiledObjectProperties: " + msg);
                    throw new OwInvalidOperationException(msg);
                }
            }
        }

        // get refined search
        OwSearchNode search = getRefinedSearch(searchtemplate.getSearch(false), getRefineCriteriaList());

        // iterate over the search criteria and set the values in the property collection
        setFiledObjectPropertiesIter(search, objectClass_p, properties_p);

        return;
    }

    private void setFiledObjectPropertiesIter(OwSearchNode searchnode_p, OwObjectClass objectClass_p, OwPropertyCollection properties_p) throws Exception
    {
        switch (searchnode_p.getNodeType())
        {
            case OwSearchNode.NODE_TYPE_PROPERTY:
                // check for <and> node and delegate control to the children, throw exception otherwise
                if (searchnode_p.getOperator() == OwSearchNode.SEARCH_OP_AND)
                {
                    List Childs = searchnode_p.getChilds();
                    if (Childs != null)
                    {
                        for (int i = 0; i < Childs.size(); i++)
                        {
                            setFiledObjectPropertiesIter((OwSearchNode) Childs.get(i), objectClass_p, properties_p);
                        }
                    }
                }
                else
                {
                    String msg = "OwStandardVirtualFolderObjectFactory.setFiledObjectPropertiesIter: If you want to add documents to virtual folders, the search criteria in the search template may only be connected by AND.";
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }
                break;
            case OwSearchNode.NODE_TYPE_SPECIAL:
                // ignore special nodes
                break;
            case OwSearchNode.NODE_TYPE_CBR:
                // no CBR nodes allowed in an virtual folder if you want to add document.
                String msg = "OwStandardVirtualFolderObjectFactory.setFiledObjectPropertiesIter: If you want to add documents to virtual folders, CBR conditions are not allowed in the search template.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            case OwSearchNode.NODE_TYPE_CRITERIA:
                OwSearchCriteria criteria = searchnode_p.getCriteria();
                if (null != criteria)
                {
                    String strPropertyName = criteria.getClassName();
                    if (OwSearchOperator.CRIT_OP_EQUAL != criteria.getOperator())
                    {
                        LOG.fatal("OwStandardVirtualFolderObjectFactory.setFiledObjectPropertiesIter: If you want to add documents to virtual folders, the search template may only contain EQUAL criterias.");
                        throw new OwConfigurationException(getContext().localize("fncm.OwFNCMNetwork.virtualfolder.AddDocuments.EQUAL", "If you want to add documents to virtual folders, the search template may only contain EQUAL criteria."));
                    }
                    Object searchTemplateDefinedValue = criteria.getValue();
                    if (null != searchTemplateDefinedValue)
                    {
                        // this property is set to a defined value in the search template. set it.
                        OwProperty prop = (OwProperty) properties_p.get(strPropertyName);
                        if (null == prop)
                        {
                            // this property is not already in the PropertyCollection. Create a new OwProperty
                            OwPropertyClass classDescription = objectClass_p.getPropertyClass(strPropertyName);
                            if (null == classDescription)
                            {
                                msg = "OwStandardVirtualFolderObjectFactory.setFiledObjectPropertiesIter: Property not found, PropertyName = " + strPropertyName;
                                LOG.error(msg);
                                throw new OwObjectNotFoundException(msg);
                            }
                            prop = new OwStandardProperty(searchTemplateDefinedValue, classDescription);
                            properties_p.put(strPropertyName, prop);
                        }
                        else
                        {
                            // this property is already in the PropertyCollection. just set its value.
                            prop.setValue(searchTemplateDefinedValue);
                        }
                    }
                }
                break;
            case OwSearchNode.NODE_TYPE_COMBINATION:
                // delegate to all children
                List childs = searchnode_p.getChilds();
                if (childs != null)
                {
                    for (int i = 0; i < childs.size(); i++)
                    {
                        setFiledObjectPropertiesIter((OwSearchNode) childs.get(i), objectClass_p, properties_p);
                    }
                }
                break;
            default:
                // unknown node type. throw exception.
                msg = "OwStandardVirtualFolderObjectFactory.setFiledObjectPropertiesIter: Invalid node. If you want to add documents, the search template may only consist of AND-nodes and EQUAL criterias.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
        }
    }

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception
    {
        return null;
    }

    /** get the version series object to this object, if the object is versionable
     * @return a list of object versions
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    /** check if a version series object is available, i.e. the object is versionable
     * @return true if object is versionable
     */
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    /** get the current version object 
     * 
     * @return OwVersion Object identifying the currently set version, or null if versions not supported
     */
    public OwVersion getVersion() throws Exception
    {
        return null;
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return getObjectClass().getType();
    }

    /** get the ECM specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     */
    public String getDMSID() throws Exception
    {
        // return <DMSID>,<subpath>
        return m_strDMSID + "," + m_nodePath;
    }

    /** retrieve the specified property from the object.
     * NOTE: If the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     * @return a property object or null if not found
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty prop = (OwProperty) m_PropertyMap.get(strPropertyName_p);

        if (prop == null && m_parent != null)
        {
            prop = m_parent.getProperty(strPropertyName_p);
        }
        else if (null == prop)
        {
            String msg = "OwStandardVirtualFolderObjectFactory.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        // get property from map
        return prop;
    }

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        OwStandardPropertyCollection allProperties = new OwStandardPropertyCollection();

        if (m_parent != null)
        {
            allProperties.putAll(m_parent.getProperties(propertyNames_p));
        }

        allProperties.putAll(m_PropertyMap);

        return allProperties;
    }

    /** retrieve the specified properties from the object as a copy
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param strPropertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getClonedProperties(java.util.Collection strPropertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, strPropertyNames_p);
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        m_PropertyMap.putAll(properties_p);
    }

    /** check if object allows to set / change properties
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if property retrieval is allowed 
     * @return true if allowed
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        // local documents do not support lock
        return false;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        // local documents do not support lock
        return false;
    }

    /** get the lock state of the object
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        // local documents do not support lock
        return false;
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p int value from {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p int value from {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    /** check if content can be set on this document with setContent
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true, if content can be set with setContent
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // folders don't have content
        return false;
    }

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true if allowed
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return true;
    }

    /** get the MIME type of the Object
     * @return MIME type as String
     */
    public String getMIMEType() throws Exception
    {
        return this.m_OpenFolderNode != null ? this.m_OpenFolderNode.getSafeStringAttributeValue(NODE_ATTRIBUTE_MIME_TYPE, MIME_TYPE) : MIME_TYPE;
    }

    /** get the additional MIME Parameter of the Object
     * @return MIME parameter as String
     */
    public String getMIMEParameter() throws Exception
    {
        // no additional MIME parameter
        return "";
    }

    /** delete object and all references from DB
     */
    public void delete() throws Exception
    {
        throw new OwNotSupportedException("OwStandardVirtualFolderObjectFactory.delete: Virtual folders do not support delete().");
    }

    /** check if object can be deleted
     * @return true, if delete operation works on object
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    /** removes the reference of the given object from this object (folder)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (folder)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {
        throw new OwNotSupportedException("OwStandardVirtualFolderObjectFactory.removeReference: Virtual Folders do not support removeReference().");
    }

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    private Object[] findSearchTemplateClassesList(OwSearchNode searchNode_p) throws Exception
    {
        if (!searchNode_p.isCriteriaNode())
        {
            Iterator itSearch = searchNode_p.getChilds().iterator();
            while (itSearch.hasNext())
            {
                Object[] o = findSearchTemplateClassesList((OwSearchNode) itSearch.next());
                if (null != o)
                {
                    return o;
                }
            }
        }
        else
        {
            if (searchNode_p.getCriteria().getFieldDefinition().getClassName().equals(OwStandardClassSelectObject.CLASS_NAME))
            {
                // === special treatment for object classes
                Object o = searchNode_p.getCriteria().getValue();
                return (Object[]) o;
            }
        }
        return null;
    }

    /** adds a object reference to this parent object / folder
     *  @param oObject_p OwObject reference to add to
     */
    public void add(OwObject oObject_p) throws Exception
    {
        // adding documents is only allowed if this folder contains documents
        if (!m_OpenFolderNode.getSafeBooleanAttributeValue(NODE_ATTRIBUTE_CONTAINS_DOCUMENTS, false))
        {
            throw new OwInvalidOperationException(getContext().localize("fncm.OwFNCMNetwork.virtualfoldernodocuments", "This virtual folder cannot contain documents."));
        }

        // get the class of the new object
        OwObjectClass objectClass = oObject_p.getObjectClass();

        // check the document class
        OwSearchNode specialNode = getSearchTemplate().getSearch(false).findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
        if (null != specialNode)
        {
            boolean classInSearchTemplate = false;
            Object[] classes = findSearchTemplateClassesList(specialNode);
            if (null != classes)
            {
                for (int i = 0; i < classes.length; i++)
                {
                    OwClass classinfo = (OwClass) classes[i];

                    if (compatibleObjectClasses(classinfo, objectClass))
                    {
                        classInSearchTemplate = true;
                    }
                }
                if (!classInSearchTemplate)
                {
                    throw new OwInvalidOperationException(getContext().localize("fncm.OwFNCMNetwork.virtualfolderclassmismatch", "Class of the new document must correspond to the class of the virtual folder."));
                }
            }
        }

        // create empty OwPropertyCollection and set all required properties
        OwPropertyCollection props = new OwStandardPropertyCollection();
        this.setFiledObjectProperties(objectClass, props);

        // save the changed properties to the object
        oObject_p.setProperties(props);
    }

    /** checks if object supports add function
     *
     * @param oObject_p OwObject reference to be added
     * @param iContext_p int representing one of the OwStatusContextDefinitions
     * @return true if object supports add function
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        // sanity check
        if (null == oObject_p)
        {
            return false;
        }

        // adding documents is only allowed if this folder contains documents
        if (!m_OpenFolderNode.getSafeBooleanAttributeValue(NODE_ATTRIBUTE_CONTAINS_DOCUMENTS, false))
        {
            return false;
        }

        // get the class of the new object
        OwObjectClass objectClass = oObject_p.getObjectClass();

        // check the document class
        OwSearchNode specialNode = getSearchTemplate().getSearch(false).findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
        if (null != specialNode)
        {
            boolean classInSearchTemplate = false;
            Object[] classes = findSearchTemplateClassesList(specialNode);
            if (null != classes)
            {
                for (int i = 0; i < classes.length; i++)
                {
                    OwClass classinfo = (OwClass) classes[i];
                    if (compatibleObjectClasses(classinfo, objectClass))
                    {
                        classInSearchTemplate = true;
                    }
                }
                if (!classInSearchTemplate)
                {
                    return false;

                }
            }
        }

        return true;
    }

    /** moves a object reference to this parent object (folder)
      *
      *  @param oObject_p OwObject reference to add to
      *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
      */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // add to this virtual folder
        this.add(oObject_p);
        // remove old reference
        oldParent_p.removeReference(oObject_p);
    }

    /** check if move operation is allowed
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     *  @param iContext_p int representing one of OwStatusContextDefinitions
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return this.canAdd(oObject_p, iContext_p);
    }

    /** get a search template associated with this Object
     *
     *  The search from the template can be used to refine the result in getChilds(...)
     *  ==> The search is automatically performed when calling getChilds(...)
     *
     *  The ColumnInfoList from the template can be used to format the result list of the children
     *  
     *
     *  NOTE: This function is especially used in virtual folders
     *
     *  @return OwSearchTemplate or null if not defined for the object
     */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        if (m_SearchTemplate == null)
        {
            // === create the search template upon the opened node
            Node searchNode = m_OpenFolderNode.getSubNode(SEARCH_NODE_TAG_NAME);

            if (searchNode != null)
            {
                String strResourceName = null;
                if (null != getResource())
                {
                    strResourceName = getResource().getID();
                }

                m_SearchTemplate = new OwSearchTemplateWrapper(createSearchTemplate(searchNode, strResourceName));
            }
            else
            {
                // === node does not contain a search, try to find one in the parent
                // get the one and only parent
                OwStandardVirtualFolderObjectFactory parent = getParent();
                if (parent != null)
                {
                    // === parent found
                    m_SearchTemplate = parent.getSearchTemplate();
                }
            }
        }
        return m_SearchTemplate;
    }

    /** overridable factory method
     * 
     * @param searchNode_p
     * @param sResourceName_p
     * @return an {@link OwSearchTemplate}
     * @throws Exception
     */
    protected OwSearchTemplate createSearchTemplate(Node searchNode_p, String sResourceName_p) throws Exception
    {
        OwSearchTemplate searchTemplate = new OwStandardSearchTemplate(m_context, searchNode_p, "VirtualFolder", sResourceName_p);
        searchTemplate.init(m_repository);

        return searchTemplate;
    }

    private String getColumnHeaderId() throws Exception
    {
        String headerId = m_virtualFolderName;

        OwStandardVirtualFolderObjectFactory parent = getParent();

        if (parent != null)
        {
            headerId = parent.getColumnHeaderId();
        }

        Node searchNode = m_OpenFolderNode.getSubNode(SEARCH_NODE_TAG_NAME);
        if (searchNode != null)
        {
            headerId += "." + nameFromNode(m_OpenFolderNode);
        }

        return headerId;
    }

    /** get the column info list that describes the columns for the child list
     * @return List of OwSearchTemplate.OwObjectColumnInfos, or null if not defined
     */
    public Collection getColumnInfoList() throws Exception
    {
        Collection<OwFieldColumnInfo> templateInfoList = getSearchTemplate().getColumnInfoList();
        if (m_virtualFolderName == null)
        {
            return templateInfoList;
        }
        else
        {

            String headerId = getColumnHeaderId();

            List<OwHeaderFieldColumnInfo> headerInfos = new LinkedList<OwHeaderFieldColumnInfo>();
            for (OwFieldColumnInfo templateInfo : templateInfoList)
            {
                headerInfos.add(new OwHeaderFieldColumnInfoDecorator(templateInfo, headerId));
            }
            return headerInfos;
        }
    }

    /** retrieve the number of pages in the objects
     * @return number of pages
     */
    public int getPageCount() throws Exception
    {
        return 1;
    }

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return null;
    }

    /** get the permissions object
     *
     * @return OwPermissionCollection of the object
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        throw new OwNotSupportedException("OwStandardVirtualFolderObjectFactory.getClonedPermissions: Not implemented.");
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    /** check if permissions can be set
     *
     * @return true = permissions can be set
     */
    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwNotSupportedException("OwStandardVirtualFolderObjectFactory.setPermissions: Not implemented.");
    }

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {

    }

    /** get the content of the object
     *<p>By default this method returns <code>null</code></p>
     * @return OwContentCollection
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    /** refresh the property cache  */
    public void refreshProperties() throws Exception
    {
    }

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public void refreshProperties(java.util.Collection props_p) throws Exception
    {
        refreshProperties();
    }

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardVirtualFolderObjectFactory.getNativeObject: Not implemented or Not supported.");
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    /** get the source object that originally provided the fields.
     * e.g. the field provider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p int value from {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if object has children
     *
     * @param iContext_p int value from {@link OwStatusContextDefinitions}
     * @param iObjectTypes_p
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        switch (iObjectTypes_p[0])
        {
        // === get other folder nodes
            case OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
            case OBJECT_TYPE_FOLDER:
                return getFolderChilds().size() > 0;
        }

        throw new OwStatusContextException("");
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the DMSID, the path is not necessarily unique,
     * but provides a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        return m_objectPath;
    }

    /** get a folder instance from the factory with the given DMSID part. 
     * The complete DMSID is strBaseDMSID_p + strDmsIDPart_p (see {@link #init(OwNetworkContext, OwRepository, String, String, Node)})
     * @param strDmsIDPart_p String DMSID part for the instance, or null to get a default virtual folder
     * @return  OwObject
     * */
    public OwVirtualFolderObject getInstance(String strDmsIDPart_p) throws Exception
    {
        // TODO: implement separate factory

        // validate SearchTemplate
        // we do it here because the init(...)-method is also called in the getChilds() Method
        // but we want to validate the search template just once.
        validateSearchTemplate();

        return this;
    }

    /** get the number of children
    * <p>By default an empty OwStatusContextException is thrown</p>
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p int value from {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        throw new OwStatusContextException("");
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p String
     * @param properties_p OwPropertyCollection  (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardVirtualFolderObjectFactory.changeClass: Not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    /** get a name that identifies the field provider, can be used to create ID's
     * 
     * @return String unique ID / Name of field provider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        try
        {
            return getResource().getID();
        }
        catch (NullPointerException e)
        {
            throw new OwObjectNotFoundException("OwStandardVirtualFolderObjectFactory.getResourceID: Resource Id not found for DMSID = " + getDMSID(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    public Map getPropagationMap()
    {
        return this.propagationMap;
    }

    public void setPropagationMap(Map propagationMap)
    {
        this.propagationMap = propagationMap;
    }

    public String getVirtualFolderName()
    {
        return m_virtualFolderName;
    }

    /**
     * Check if a reset was called,
     * and will process a propagation of values from parent search once again.
     * @throws Exception
     * @since 4.1.2
     */
    protected void checkResetState() throws Exception
    {
        if (getParent() != null)
        {
            if (getSearchTemplate() instanceof OwSearchTemplateWrapper)
            {
                OwSearchTemplateWrapper wrapedSearchTemplate = (OwSearchTemplateWrapper) getSearchTemplate();
                if (wrapedSearchTemplate.isReseted())
                {//multi-level possible
                    OwSearchTemplate parentSearch = findParentSearch(getParent());
                    if (parentSearch != null)
                    {
                        OwSearchNode parentNode = parentSearch.getSearch(false);
                        parentNode = getParent().getRefinedSearch(parentNode, getParent().getRefineCriteriaList());
                        deepCriteriaValuePropagation(wrapedSearchTemplate.getSearch(false), parentNode);
                        wrapedSearchTemplate.setReseted(false);
                    }
                }
            }
        }
    }

    /**
     * Find Parent search definition, useful for propagation of criteria
     * @param parent OwStandardVirtualFolderObjectFactory (can be null)
     * @return OwSearchTemplate (or null if provided parent is null)
     * @throws Exception
     * @since 4.1.2
     */
    protected OwSearchTemplate findParentSearch(OwStandardVirtualFolderObjectFactory parent) throws Exception
    {
        if (parent != null)
        {
            if (parent.getSearchTemplate() == getSearchTemplate())
            {
                return findParentSearch(parent.getParent());
            }
            return parent.getSearchTemplate();
        }
        return null;
    }

    /**
     *<p>
     * ObjectClass of virtual folder.
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
    public static class OwVirtualFolderObjectClass implements OwObjectClass
    {
        /** class name of the filename property, which is also the getName property of the Object */
        //public static final String NAME_PROPERTY            = "ow_virtual_folder_name";
        /** map containing the property class descriptions of the class */
        protected HashMap m_PropertyClassesMap = new HashMap();

        /** construct PropertyClass Object and set Property classes */
        public OwVirtualFolderObjectClass()
        {
            // === create the property classes
            // Name property
            m_PropertyClassesMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), OwResource.m_ObjectNamePropertyClass);
        }

        /** get Object type
         * @return the type of the object
         */
        public int getType()
        {
            return OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER;
        }

        /** get the child classes of this class if we deal with a class tree
         *
         * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
         *
         * @return List of child classes or null if no children are available
         */
        public java.util.List getChilds(OwNetwork network_p, boolean fExcludeHidden_p) throws Exception
        {
            return null;
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.ecm.OwObjectClass#getModes(int)
         */
        public List getModes(int operation_p) throws Exception
        {
            return null;
        }

        /** check if children are available
        *
        * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
        * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
        * @param context_p OwStatusContextDefinitions
        * 
        * @return boolean always false by this implementation
        */
        public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p)
        {
            return false;
        }

        /** get the child classes of this class if we deal with a class tree
        *
        * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
        * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
        *
        * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
        */
        public java.util.Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p)
        {
            return null;
        }

        /** get the name of the class
         * @return class name
         */
        public String getClassName()
        {
            return "OwVirtualFolder";
        }

        /** get the unique ID of the class
         *
         * @return class ID 
         */
        public String getID()
        {
            return getClassName();
        }

        /** get the displayable name of the type as defined by the ECM System
         * @return type displayable name of property
         */
        public String getDisplayName(Locale locale_p)
        {
            return getClassName();
        }

        /** get a map of the available property class descriptions 
         *
         * @param strClassName_p Name of class
         * @return OwPropertyClass instance
         */
        public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
        {
            OwPropertyClass propertyClassDescription = (OwPropertyClass) m_PropertyClassesMap.get(strClassName_p);
            if (null == propertyClassDescription)
            {
                throw new OwObjectNotFoundException("OwStandardVirtualFolderObjectFactory$OwVirtualFolderObjectClass.getPropertyClass: Cannot find the class for property = " + strClassName_p);
            }

            return propertyClassDescription;
        }

        /** get a list of the available property class descriptions names
         *
         * @return string array of OwPropertyClass Names
         */
        public java.util.Collection getPropertyClassNames() throws Exception
        {
            return m_PropertyClassesMap.keySet();
        }

        /** get the name of the name property
         * @return String name of the name property
         */
        public String getNamePropertyName() throws Exception
        {
            return OwResource.m_ObjectNamePropertyClass.getClassName();
        }

        /** check, if new object instances can be created for this class
         *
         * @return true, if object can be created
         */
        public boolean canCreateNewObject() throws Exception
        {
            return false;
        }

        public boolean hasVersionSeries() throws Exception
        {
            return false;
        }

        /** retrieve a description of the object class
         *
         * @return String Description of the object class
         */
        public String getDescription(Locale locale_p)
        {
            return OwString.localize(locale_p, "ecm.OwVirtualFolderObject.description", "Standard class for virtual folders / eFiles.");
        }

        /** check if class is visible to the user
         *
         * @return true if property is visible to the user
         */
        public boolean isHidden() throws Exception
        {
            return true;
        }

        /** get the parent class of this class 
         *
         * @return OwObjectClass parent or null if topmost class
         */
        public OwObjectClass getParent() throws Exception
        {
            return null;
        }
    }

    /**
     *<p>
     * Tuple of criteria name and value to refine a search criteria.
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
    protected static class OwVirtualFolderRefineCriteria
    {
        /** name of the criteria to refine */
        public String m_strCriteria;
        /** literal value to set */
        public Node m_literalNode;

        public OwVirtualFolderRefineCriteria(String strCriteria_p, Node node_p)
        {
            m_strCriteria = strCriteria_p;
            m_literalNode = node_p;
        }

        public boolean equals(Object obj_p)
        {
            if (obj_p == null)
            {
                return false;
            }
            if (obj_p instanceof OwVirtualFolderRefineCriteria)
            {
                OwVirtualFolderRefineCriteria secondObj = (OwVirtualFolderRefineCriteria) obj_p;
                //we assume no null value for m_strCriteria.
                return m_strCriteria.equals(secondObj.m_strCriteria);
            }
            if (obj_p instanceof OwSearchCriteria)
            {
                // test if this search criteria corresponds to the virtual folder refine criteria
                OwSearchCriteria criteria = (OwSearchCriteria) obj_p;
                //we assume no null value for m_strCriteria.
                return m_strCriteria.equals(criteria.getClassName());
            }
            else
            {
                return false;
            }
        }

        public int hashCode()
        {
            return m_strCriteria.hashCode();
        }
    }

    /**
     *<p>
     * Helper to identify if a refresh/reset of the searchtemplate was processed or not.
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
     *@since 4.1.2
     */
    protected static class OwSearchTemplateWrapper implements OwSearchTemplate
    {
        private OwSearchTemplate template;

        private boolean reseted;

        public OwSearchTemplateWrapper(OwSearchTemplate template)
        {
            this.template = template;
        }

        @Override
        public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
        {
            return this.template.getField(strFieldClassName_p);
        }

        @Override
        public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
        {
            this.template.setField(sName_p, value_p);
        }

        @Override
        public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
        {
            return this.template.getSafeFieldValue(sName_p, defaultvalue_p);
        }

        @Override
        public Collection getFields() throws Exception
        {
            return this.template.getFields();
        }

        @Override
        public int getFieldProviderType()
        {
            return this.template.getFieldProviderType();
        }

        @Override
        public Object getFieldProviderSource()
        {
            return this.template.getFieldProviderSource();
        }

        @Override
        public String getFieldProviderName()
        {
            return this.template.getFieldProviderName();
        }

        @Override
        public void init(OwFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
        {
            this.template.init(fieldDefinitionProvider_p);
        }

        @Override
        public Collection getColumnInfoList() throws Exception
        {
            return this.template.getColumnInfoList();
        }

        @Override
        public OwSearchNode getSearch(boolean fRefresh_p) throws Exception
        {
            if (fRefresh_p)
            {
                setReseted(fRefresh_p);
            }
            return template.getSearch(fRefresh_p);
        }

        @Override
        public String getHtmlLayout()
        {
            return template.getHtmlLayout();
        }

        @Override
        public boolean hasHtmlLayout()
        {
            return template.hasHtmlLayout();
        }

        @Override
        public String getJspLayoutPage()
        {
            return template.getJspLayoutPage();
        }

        @Override
        public boolean hasJspLayoutPage()
        {
            return template.hasJspLayoutPage();
        }

        @Override
        public String getIcon()
        {
            return template.getIcon();
        }

        @Override
        public String getName()
        {
            return template.getName();
        }

        @Override
        public String getDisplayName(Locale locale_p)
        {
            return template.getDisplayName(locale_p);
        }

        @Override
        public int getVersionSelection()
        {
            return template.getVersionSelection();
        }

        @Override
        public OwSort getSort(int iMinSortCriteria_p)
        {
            return template.getSort(iMinSortCriteria_p);
        }

        @Override
        public OwPriorityRule getPriorityRule()
        {
            return template.getPriorityRule();
        }

        @Override
        public boolean isInitalized()
        {
            return template.isInitalized();
        }

        @Override
        public int getDefaultMaxSize()
        {
            return template.getDefaultMaxSize();
        }

        @Override
        public boolean canSaveSearch()
        {
            return template.canSaveSearch();
        }

        @Override
        public boolean canDeleteSearch()
        {
            return template.canDeleteSearch();
        }

        @Override
        public boolean canUpdateSearch()
        {
            return template.canUpdateSearch();
        }

        @Override
        public void deleteSavedSearch(String name_p) throws Exception
        {
            template.deleteSavedSearch(name_p);
        }

        @Override
        public Collection getSavedSearches() throws Exception
        {
            return template.getSavedSearches();
        }

        @Override
        public void setSavedSearch(String name_p) throws Exception
        {
            template.setSavedSearch(name_p);
        }

        @Override
        public String getSavedSearch() throws Exception
        {
            return template.getSavedSearch();
        }

        @Override
        public void saveSearch(String name_p) throws Exception
        {
            template.saveSearch(name_p);
        }

        public boolean isReseted()
        {
            return this.reseted;
        }

        public void setReseted(boolean rested)
        {
            this.reseted = rested;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (super.equals(obj))
            {
                return true;
            }
            else
            {
                return template.equals(obj);
            }
        }

        public OwSearchTemplate getTemplate()
        {
            return this.template;
        }
    }

}