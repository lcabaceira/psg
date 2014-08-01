package com.wewebu.ow.server.ecm.bpm;

import java.util.Collection;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Base interface for Workitem containers, or BPM Queues.<br/><br/>
 * To be implemented with the specific BPM system.
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
public interface OwWorkitemContainer extends OwObject
{
    // === filter types
    /** filter type for disabled filter, used in getChilds, see also setFilter */
    public static final int FILTER_TYPE_NONE = 0;
    /** filter type to retrieve only items which are resubmitted with the OwBPMWorkItem.resubmit() function, used in getChilds, see also setFilter */
    public static final int FILTER_TYPE_RESUBMISSION = 1;
    /** filter type to retrieve only items which are NOT resubmitted (default), used in getChilds, see also setFilter */
    public static final int FILTER_TYPE_NORMAL = 3;

    // === predefine properties
    /** (optional) property name for the groupboxes, can be used to distinguish groupboxes in cross views 
     */
    public static final String GROUPBOX_PROPERTY_NAME = "OW_GROUPBOX_NAME";

    /** check if resubmission is supported by the queue
     */
    public abstract boolean canResubmit() throws Exception;

    /** set a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @param iFilterType_p int as defined in FILTER_TYPE_...
     */
    public abstract void setFilterType(int iFilterType_p);

    /** get a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @return int as defined in FILTER_TYPE_...
     */
    public abstract int getFilterType();

    /** get the possible reassign container names used in reassignToPublicContainer
     * 
     * @return Collection of names or null if not defined
     * @throws Exception
     */
    public abstract Collection getPublicReassignContainerNames() throws Exception;

    /** get a display name for a reassign container name used in reassignToPublicContainer */
    public abstract String getPublicReassignContainerDisplayName(String sName_p);

    /** check if container supports work item pull, see pull
    * @param iContext_p int OwStatusContextDefinitions
    * 
    * @return boolean 
    * @throws Exception
    */
    public abstract boolean canPull(int iContext_p) throws Exception, OwStatusContextException;

    /** pulls the next available work item out of the container and locks it for the user
     * 
     * @param sort_p OwSort optional sorts the items and takes the first available one, can be null
     * @param exclude_p Set of work item DMSIDs to be excluded, can be null
     * @return OwWorkitem or OwObjectNotFoundException if no object is available or OwServerException if no object could be pulled within timeout
     * @throws Exception for general error or OwServerException if timed out or OwObjectNotFoundException if no work item is available
     */
    public abstract OwWorkitem pull(OwSort sort_p, Set exclude_p) throws Exception, OwObjectNotFoundException, OwServerException;

    /** get a collection of users that should be offered to the users for reassignment
     * 
     * @return Collection of OwUserInfo or null if no default list is available
     * @since 2.5.2.0
     */
    public Collection getDefaultUsers();
}