package com.wewebu.ow.server.plug.owbpm;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewFilterRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.field.OwPriorityRule;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * A BPM queue wrapper.
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
public class OwBPMQueue implements OwBPMVirtualQueue
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMQueue.class);

    /** the sort object to use for this queue */
    private OwSort m_sort;

    /** filter for the object list view */
    private OwObjectListViewFilterRow.OwFilter m_filter;

    /** reference to the document */
    private OwBPMDocument m_doc;

    /** the folder object from the repository */
    private OwWorkitemContainer m_queuefolder;

    /** flag for filter enable */
    private boolean m_fIsFilter;

    /** list of priority rules to apply to this queue */
    private Collection m_rulelist;

    private boolean hideIfEmpty = false;

    /** creates a wrapper for the given queue name
     * 
     * @param doc_p
     * @param queueFolder_p
     * @param rulelist_p Collection of OwPriorityRule or null of no rules are defined
     */
    public OwBPMQueue(OwBPMDocument doc_p, OwWorkitemContainer queueFolder_p, Collection rulelist_p)
    {
        m_rulelist = rulelist_p;
        m_doc = doc_p;
        m_queuefolder = queueFolder_p;
    }

    public OwBPMQueue(OwBPMDocument doc_p, OwWorkitemContainer queueFolder_p, Collection rulelist_p, boolean hideIfEmpty)
    {
        m_rulelist = rulelist_p;
        m_doc = doc_p;
        m_queuefolder = queueFolder_p;
        this.hideIfEmpty = hideIfEmpty;
    }

    /** check if the rules apply to this object so it should be displayed with a color
     * 
     * @param obj_p OwObject work item to check for
     * @return String style class or null if no rule matches
     */
    public String applyRules(OwObject obj_p)
    {
        Iterator it = getRules().iterator();
        while (it.hasNext())
        {
            OwPriorityRule rule = (OwPriorityRule) it.next();
            try
            {
                if (rule.appliesTo(obj_p))
                {
                    return rule.getStylClass();
                }
            }
            catch (Exception e)
            {
                LOG.warn("Failed to process priority rule !", e);
            }
        }
        return null;
    }

    /** get a list of priority rules for this queue
     * 
     * @return Collection of OwPriorityRule or null of no rules are defined
     */
    private Collection getRules()
    {
        return m_rulelist;
    }

    /** the folder object from the repository */
    public OwWorkitemContainer getQueueFolder()
    {
        return m_queuefolder;
    }

    /** check if resubmission is supported by the queue
     */
    public boolean canResubmit() throws Exception
    {
        return m_queuefolder.canResubmit();
    }

    /** get the context
     */
    protected OwMainAppContext getContext()
    {
        return (OwMainAppContext) m_doc.getContext();
    }

    /** get the workitems from the queue */
    public OwObjectCollection getWorkItems(OwSearchNode filterNode_p) throws Exception
    {
        return getQueueFolder().getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS }, null, getSort(), m_doc.getMaxChildCount(), 0, filterNode_p);
    }

    /** the sort object to use for this queue 
     * @throws Exception */
    public OwSort getSort() throws Exception
    {
        if (null == m_sort)
        {
            List sortCriterias = m_doc.getColumnSortCriteria();

            // initialize sort with sort from user preferences
            if (sortCriterias != null)
            {
                m_sort = new OwSort(getContext().getMaxSortCriteriaCount(), true);

                /* first criteria defined in owplugins.xml should have highest priority, but in
                 * OwSort the latest added criteria have highest priority. So it is necessary to
                 * add criteria in reverse order.
                */
                for (int i = sortCriterias.size() - 1; i >= 0; i--)
                {
                    m_sort.addCriteria((OwSortCriteria) sortCriterias.get(i));
                }
            }
            else if (null != getSearchTemplate())
            {
                m_sort = getSearchTemplate().getSort(getContext().getMaxSortCriteriaCount());
            }
        }

        return m_sort;
    }

    /** get the filter setting
    *
    * @return OwObjectListViewFilterRow.OwFilter
    */
    public OwObjectListViewFilterRow.OwFilter getObjectListFilter() throws Exception
    {
        if (m_filter == null)
        {
            // === create default filter
            Collection fprops = m_queuefolder.getFilterProperties(getColumnInfo());
            if (fprops != null)
            {
                m_filter = OwObjectListView.createFilter(fprops, m_doc.getBpmRepository(), null, getName(), m_doc);
            }
            else
            {
                m_filter = OwObjectListView.createFilter(getName());
            }
        }

        return m_filter;
    }

    /** get the column info for the child list columns
     *
     * @return List of OwFieldColumnInfo
     */
    public List getColumnInfo() throws Exception
    {
        return m_doc.getDefaultColumnInfo();
    }

    /** get the name of the queue
     */
    public String getName() throws Exception
    {
        return getQueueFolder().getID();
    }

    /** get the number of items in the queue
     * 
     * @param context_p int on of OwStatusContextDefinitions.STATUS_CONTEXT_...
     * @return count or throws OwStatusContextException
     * @throws Exception,OwStatusContextException
     */
    public int getItemCount(int context_p) throws Exception
    {
        return getQueueFolder().getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, context_p);
    }

    /** get the name of the queue
     */
    public String getDisplayName() throws Exception
    {
        OwWorkitemContainer queueFolder = getQueueFolder();
        String id = queueFolder.getID();
        String name = queueFolder.getName();

        return getContext().localize("plug.owbpm.queue." + id, name);
    }

    /** get the search template for the queue */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return getQueueFolder().getSearchTemplate();
    }

    /** get the path / URL to the icon for the queue */
    public String getIcon() throws Exception
    {
        return OwMimeManager.getMimeIcon(getContext(), getQueueFolder());
    }

    /** switch resubmission view on or off
     */
    public void toggleResubmitFilter()
    {
        if (isResubmitFilter())
        {
            getQueueFolder().setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        }
        else
        {
            getQueueFolder().setFilterType(OwWorkitemContainer.FILTER_TYPE_RESUBMISSION);
        }
    }

    /** check if resubmission filter is on
     */
    public boolean isResubmitFilter()
    {
        return (getQueueFolder().getFilterType() != OwWorkitemContainer.FILTER_TYPE_NORMAL);
    }

    /** switch filter on or off
     */
    public void toggleFilter()
    {
        m_fIsFilter = !m_fIsFilter;
    }

    /** switch filter on or off
     */
    public void enableFilter(boolean fEnable_p)
    {
        m_fIsFilter = fEnable_p;
    }

    /** check if filter is enabled */
    public boolean isFilter()
    {
        return m_fIsFilter;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.OwBPMVirtualQueue#hideIfEmpty()
     */
    @Override
    public boolean isHideIfEmpty()
    {
        return this.hideIfEmpty;
    }
}