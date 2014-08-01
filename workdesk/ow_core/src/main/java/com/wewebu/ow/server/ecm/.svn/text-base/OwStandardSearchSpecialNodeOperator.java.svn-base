package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Standard implementation to scan a search node structure for
 * special nodes, like paths, object stores and classes.
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
 *@since 2.5.3.0
 */
public class OwStandardSearchSpecialNodeOperator
{
    private static final Logger LOG = OwLogCore.getLogger(OwStandardSearchSpecialNodeOperator.class);
    /** the selected classes*/
    private Object[] m_classobjects;
    /** merge type from template */
    private int m_mergeType;
    /** list of Strings representing full path to specified location */
    private LinkedList m_paths;

    public OwStandardSearchSpecialNodeOperator()
    {
        m_paths = new LinkedList();
    }

    /** Array of OwClass, representing the 
     * classes defined for the scanned search node 
     * @return Array of OwClass objects or null if nothing found
     */
    public Object[] getClasses()
    {
        return m_classobjects;
    }

    /**
     * get the merge type as string representation
     * <p>
     *  OwSearchOperator.MERGE_UNION = "union"
     *  OwSearchOperator.MERGE_INTERSECT = "intersection"
     *  OwSearchOperator.MERGE_NONE = "none" (default)
     * </p>
     * @return String representation of the {@link #getMergeType()} value
     */
    public String getMergeTypeRepresentation()
    {
        switch (this.m_mergeType)
        {
            case OwSearchOperator.MERGE_UNION:
                return "union";
            case OwSearchOperator.MERGE_INTERSECT:
                return "intersection";
            default: /* OwSearchOperator.MERGE_NONE */
                if (OwSearchOperator.MERGE_NONE != this.m_mergeType && LOG.isDebugEnabled())
                {
                    LOG.info("OwStandardSearchSpecialNodeOperator.getMergeType: merge operator not matching OwSearchOperator.MERGE_NONE value =" + m_mergeType);
                }
                return "none";
        }
    }

    /**
     * Get the int representation of the merge type. 
     * @return int
     * @see OwSearchOperator OwSearchOperator.MERGE_*
     */
    public int getMergeType()
    {
        return this.m_mergeType;
    }

    /**
     * Retrieve a List of scanned object stores/repositories that the given 
     * search node refers.
     * @return list of {@link OwSearchObjectStore}s representing the object stores/repositories
     *         that the scanned search node refers
     */
    public List getObjectStores()
    {
        LinkedList objectStores = new LinkedList();

        Iterator it = getPaths().iterator();
        while (it.hasNext())
        {
            OwSearchPath path = (OwSearchPath) it.next();

            OwSearchObjectStore objectStore = path.getObjectStore();
            if (!objectStores.contains(objectStore))
            {
                objectStores.add(objectStore);
            }
        }

        return objectStores;
    }

    /** 
     * Collection of String representing search-path objects referred by the 
     * scanned search node. 
     * @return Collection of {@link OwSearchPath}s
     */
    public Collection getPaths()
    {
        return m_paths;
    }

    /**
     * Returns a list of possible paths to
     * specified in the search node structure.
     * <p>
     * Just iterating over the scanned paths ({@link #getPaths()}
     * and removing the object store/repository only references.
     * @see OwSearchPath#isObjectStoreReference()
     * </p>
     * @return List of {@link OwSearchPath} subpaths
     */
    public List getSubpaths()
    {
        LinkedList subpaths = new LinkedList();

        Iterator it = m_paths.iterator();
        while (it.hasNext())
        {
            OwSearchPath path = (OwSearchPath) it.next();
            if (!path.isObjectStoreReference())
            {
                subpaths.add(path);
            }

        }

        return subpaths;
    }

    /** 
     * Traverse the search criteria tree, create and cache the special node 
     * information (namely referred paths within object-stores and object classes )<br/>.
     * @param searchNode_p {@link OwSearchNode} from where the scan should start
     * @throws Exception if problem occur requesting the FieldDefinition from OwSearchNode 
     */
    public void scan(OwSearchNode searchNode_p) throws Exception
    {
        if (!searchNode_p.isCriteriaNode())
        {
            List nodeChildren = searchNode_p.getChilds();
            Iterator itSearch = nodeChildren.iterator();
            while (itSearch.hasNext())
            {
                scan((OwSearchNode) itSearch.next());
            }
        }
        else
        {
            OwSearchCriteria criteria = searchNode_p.getCriteria();
            OwFieldDefinition fieldDefinition = criteria.getFieldDefinition();
            String fieldClass = fieldDefinition.getClassName();
            if (OwSearchPathField.CLASS_NAME.equals(fieldClass))
            {
                handleResourcepaths(searchNode_p);
                setMergeType(criteria.getOperator());
            }
            else if (OwStandardClassSelectObject.CLASS_NAME.equals(fieldClass))
            {
                handleClasses(searchNode_p);
            }
        }
    }

    /** (overridable)
     * Handling the paths search node to extract and add it to
     * {@link #getPaths()} collection.
     * <p>This method is called if a search criteria is found form path type.</p>
     * @param pathSearchNode_p OwSearchNode to use for paths extraction.
     */
    protected void handleResourcepaths(OwSearchNode pathSearchNode_p)
    {
        OwSearchCriteria pathCriteria = pathSearchNode_p.getCriteria();
        OwSearchPath path = (OwSearchPath) pathCriteria.getValue();

        if (null == path)
        {
            return;
        }

        m_paths.add(path);
    }

    /** (overridable) 
     * Handling for search node where the specified
     * classes are contained.
     * <p>This method is called by {@link #scan(OwSearchNode)}, if 
     * a node with class definition was found.</p>
     * @param classSearchNode_p SearchNode containing the classes
     */
    protected void handleClasses(OwSearchNode classSearchNode_p)
    {
        // === special treatment for objectclasses
        Object o = classSearchNode_p.getCriteria().getValue();
        setClasses((Object[]) o);
    }

    /**
     * Method to set the merge type, which is not
     * verifying if given parameter value is valid.
     * @param mergeType_p value for the merge type
     */
    protected void setMergeType(int mergeType_p)
    {
        m_mergeType = mergeType_p;
    }

    /**
     * Method to set the array of OwClass.
     * @param owClassArr_p array of OwClass or null
     */
    protected void setClasses(Object[] owClassArr_p)
    {
        m_classobjects = owClassArr_p;
    }
}