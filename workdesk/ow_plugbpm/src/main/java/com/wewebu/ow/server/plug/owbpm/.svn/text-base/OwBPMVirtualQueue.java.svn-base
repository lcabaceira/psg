package com.wewebu.ow.server.plug.owbpm;

import java.util.List;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewFilterRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Interface for all virtual queues.
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
public abstract interface OwBPMVirtualQueue
{
    /** check if resubmission is supported by the queue
     */
    public abstract boolean canResubmit() throws Exception;

    /** get the path / URL to the icon for the queue */
    public abstract String getIcon() throws Exception;

    /** check if the rules apply to this object so it should be displayed with a color
     * 
     * @param obj_p OwObject work item to check for
     * @return String style class or null of no rule matches
     */
    public abstract String applyRules(OwObject obj_p);

    /** get the workitems from the queue */
    public abstract OwObjectCollection getWorkItems(OwSearchNode filterNode_p) throws Exception;

    /** the sort object to use for this queue 
     * @throws Exception */
    public abstract OwSort getSort() throws Exception;

    /** get the column info for the child list columns
     *
     * @return List of OwFieldColumnInfo
     */
    public abstract List getColumnInfo() throws Exception;

    /** get the filter setting
    *
    * @return OwObjectListViewFilterRow.OwFilter
    */
    public abstract OwObjectListViewFilterRow.OwFilter getObjectListFilter() throws Exception;

    /** get the name of the queue
     */
    public abstract String getName() throws Exception;

    /** get the number of items in the queue
     * 
     * @param context_p int on of OwStatusContextDefinitions.STATUS_CONTEXT_...
     * @return count or throws OwStatusContextException
     * @throws Exception,OwStatusContextException
     */
    public abstract int getItemCount(int context_p) throws Exception;

    /** get the name of the queue
     */
    public abstract String getDisplayName() throws Exception;

    /** get the search template for the queue */
    public abstract OwSearchTemplate getSearchTemplate() throws Exception;

    /** switch resubmission view on or off
     */
    public abstract void toggleResubmitFilter();

    /** check if resubmission filter is on
     */
    public abstract boolean isResubmitFilter();

    /** switch filter on or off
     */
    public abstract void toggleFilter();

    /** switch filter on or off
     */
    public abstract void enableFilter(boolean fEnable_p);

    /** check if filter is enabled */
    public abstract boolean isFilter();

    /** get the work item container of that queue */
    public abstract OwWorkitemContainer getQueueFolder();

    /**
     * Tells if this queue should be hidden if empty.
     * @return true if this queue should be hidden if empty.
     * @since 4.2.0.0
     */
    public boolean isHideIfEmpty();
}