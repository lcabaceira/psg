package com.wewebu.ow.server.fieldimpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;

/**
 *<p>
 * Helper class to filter criteria node in a SearchNode structure.
 * Provide the possibility to return results in a Map or List.
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
 *@since 4.0.0.0
 */
public class OwSearchNodeFilter
{
    private int mask;
    private boolean nonProp;

    public OwSearchNodeFilter(int filterMask)
    {
        setFilter(filterMask);
    }

    public OwSearchNodeFilter()
    {
    }

    /**
     * Set the mask which will be used during filter process.
     * Mask is created from <code>OwSearchNode.FILTER_...</code> values.
     * @param filterMask int
     */
    public void setFilter(int filterMask)
    {
        mask = 0;
        if (0 != (OwSearchNode.FILTER_HIDDEN & filterMask))
        {
            mask = mask | OwSearchCriteria.ATTRIBUTE_HIDDEN;
        }

        if (0 != (OwSearchNode.FILTER_READONLY & filterMask))
        {
            mask = mask | OwSearchCriteria.ATTRIBUTE_READONLY;
        }

        nonProp = 0 != (OwSearchNode.FILTER_NONPROPERTY & filterMask);
    }

    public boolean filterNonProperties()
    {
        return nonProp;
    }

    protected boolean match(OwSearchCriteria crit)
    {
        return (crit.getAttributes() & mask) != 0;
    }

    /**
     * Return a map of criteria which will be add during 
     * filter process.
     * <p>Attention: This filtering is only correct if 
     * every criterion has its own unique name, otherwise
     * only the latest occurrence of specific criterion is contained.</p> 
     * @param node OwSearchNode to use for filter process
     * @return Map of Unique name to corresponding criteria
     */
    public Map<String, OwSearchCriteria> getCriteriaMap(OwSearchNode node)
    {
        MapContainer cont = new MapContainer();
        traverseNode(cont, node);
        Map<String, OwSearchCriteria> results = cont.getResults();
        cont.clean();
        return results;
    }

    /**
     * Return a list of specific filtered OwSearchCriteria.
     * @param node OwSearchNode which should be filtered
     * @return List of OwSearchCriteria
     */
    public List<OwSearchCriteria> getCriteriaList(OwSearchNode node)
    {
        ListContainer cont = new ListContainer();
        traverseNode(cont, node);
        List<OwSearchCriteria> results = cont.getResults();
        cont.clean();
        return results;
    }

    private void traverseNode(ResultContainer<?> v, OwSearchNode node)
    {
        // === add criteria if available 
        OwSearchCriteria crit = node.getCriteria();
        if (crit != null)
        {
            if (!match(crit))
            {
                v.add(crit);
            }
        }

        // === iterate over the children if available
        if ((node.getNodeType() == OwSearchNode.NODE_TYPE_PROPERTY) || (node.getNodeType() == OwSearchNode.NODE_TYPE_COMBINATION) || (!filterNonProperties()))
        {
            List<?> childs = node.getChilds();
            if (childs != null)
            {
                Iterator<?> it = childs.iterator();
                while (it.hasNext())
                {
                    traverseNode(v, (OwSearchNode) it.next());
                }
            }
        }
    }

    // Helper Classes
    /**
     *<p>
     *  Abstract result set container definition.
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
    private interface ResultContainer<T>
    {
        public abstract void add(OwSearchCriteria crit);

        public abstract T getResults();

        public abstract void clean();
    }

    /**
     *<p>
     * Map based result container implementation.
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
    private static class MapContainer implements ResultContainer<HashMap<String, OwSearchCriteria>>
    {
        private HashMap<String, OwSearchCriteria> results;

        public MapContainer()
        {
            results = new HashMap<String, OwSearchCriteria>();
        }

        public void add(OwSearchCriteria crit)
        {
            results.put(crit.getUniqueName(), crit);
        }

        public HashMap<String, OwSearchCriteria> getResults()
        {
            return results;
        }

        public void clean()
        {
            results = null;
        }
    }

    /**
     *<p>
     * List based result container implementation.
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
    private static class ListContainer implements ResultContainer<List<OwSearchCriteria>>
    {

        private LinkedList<OwSearchCriteria> results;

        public ListContainer()
        {
            results = new LinkedList<OwSearchCriteria>();
        }

        public void add(OwSearchCriteria crit)
        {
            results.add(crit);
        }

        public List<OwSearchCriteria> getResults()
        {
            return results;
        }

        public void clean()
        {
            results = null;
        }
    }
}
