package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for history managers to collect and retrieve history information.<br/>
 * History Events are collected both in the EcmAdapter and in the Workdesk itself.<br/>
 * Most ECM Systems will write their own history and so the history manager needs only to read that information.
 * If the ECM System does not write a history, then the Adapter needs to write events
 * to a database and the history manager needs to read the events there.<br/>
 * For Workdesk events such as a clicked plugin the addEntry function needs 
 * to be implemented and the event needs to be written to a database.<br/><br/>
 * To be implemented with the specific ECM system.<br/><br/>
 * You get a instance of the HistoryManager by calling getContext().getHistoryManager().
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
public interface OwHistoryManager extends OwEventManager, OwRepository<OwHistoryEntry>
{
    /** init the manager, set context
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * @exception Exception
     */
    public abstract void init(OwHistoryManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception;

    /** set a reference to the network adapter 
     * 
     * @param network_p OwNetwork
     */
    public abstract void setNetwork(OwNetwork network_p);

    /** search for entries in the database for a specific ECM object
    *
    * @param object_p OwObjectReference to find entries for
    * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
    * @param sortCriteria_p OwSort to apply, or null
    * @param propertyNames_p Collection of properties to retrieve with the history entries
    * @param includeSubObjectTypes_p array of child OwObject types to be included in history, or null
    * @param iMaxSize_p max size of entries to retrieve
    *
    * @return Collection of OwHistoryEntry
    */
    public abstract OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception;
}