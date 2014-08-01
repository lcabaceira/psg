package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;

import filenet.vw.api.VWQueueElement;

/**
 *<p>
 * Proxy Container.
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
public class OwFNBPM5ProxyContainer extends OwFNBPM5QueueContainer
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5ProxyContainer.class);

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return <code>int</code> number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        throw new OwStatusContextException("getChildCount");
    }

    public OwFNBPM5ProxyContainer(OwFNBPM5Repository repository_p) throws Exception
    {
        super(repository_p, repository_p.getVWSession().getQueue("Inbox(0)"));
    }

    public String getName()
    {
        return getContext().localize("ecmimpl.fncm.bpm.OwFNBPMProxyContainer.name", "Proxy");
    }

    public String getID()
    {
        return "DefaultProxy";
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/proxy";
    }

    /** overridable to create a new work item
     * @param workitem_p VWQueueElement
     * */
    protected OwFNBPM5WorkItem createWorkItem(VWQueueElement workitem_p) throws Exception
    {
        return new OwFNBPM5ProxyQueueWorkItem(this, workitem_p);
    }

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     *
     * @param iObjectTypes_p the requested object type (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     * @param fLock_p lock the returned objects
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChildsInternal(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p, boolean fLock_p) throws Exception
    {
        // === merge filter and absent filter node together to one single search node
        OwSearchNode absentfilternode = getAbsentFilterNode();

        if (absentfilternode.getChilds().size() == 0)
        {
            return null;
        }

        OwSearchNode mergenode = absentfilternode;

        // Merge search with filter criteria
        if (filterCriteria_p != null)
        {
            OwSearchNode filterpropertynode = filterCriteria_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY);

            // merge together in one merge node
            mergenode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
            mergenode.add(absentfilternode);
            mergenode.add(filterpropertynode);
        }

        return super.getChildsInternal(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, mergenode, fLock_p);
    }

    /** the cached filter node to search for the absent workitems the proxy (this user) is working for */
    private OwSearchNode m_absentfilternode;

    /** get the filter node to search for the absent workitems the proxy (this user) is working for 
     * @throws Exception 
     * @throws OwObjectNotFoundException */
    private OwSearchNode getAbsentFilterNode() throws OwObjectNotFoundException, Exception
    {
        if (null == m_absentfilternode)
        {
            m_absentfilternode = new OwSearchNode(OwSearchNode.SEARCH_OP_OR, OwSearchNode.NODE_TYPE_PROPERTY);

            // === iterate over the absents that this user is proxy for
            Set cyclepreventionset = new HashSet();
            recurseAbsentFilter(cyclepreventionset, getRepository().getNetwork().getCredentials().getUserInfo().getUserID(), m_absentfilternode);
        }

        return m_absentfilternode;
    }

    /** recurse through the absent person settings and collect filters
     * 
     * @param cyclepreventionset_p {@link Set} to store the already added proxy IDs to prevent cycles
     * @param proxyUserID_p String proxy to search the absent person filters for
     * @param filter_p property filter to append the filter to
     * 
     * @throws Exception
     */
    private void recurseAbsentFilter(Set cyclepreventionset_p, String proxyUserID_p, OwSearchNode filter_p) throws Exception
    {
        if (cyclepreventionset_p.contains(proxyUserID_p))
        {
            return;
        }

        cyclepreventionset_p.add(proxyUserID_p);

        Iterator it = getRepository().getAbsents(proxyUserID_p).iterator();
        while (it.hasNext())
        {
            OwFNBPM5ProxyStore.OwFNBPMProxyInfo info = (OwFNBPM5ProxyStore.OwFNBPMProxyInfo) it.next();

            // === check dates
            long currenttime = new Date().getTime();
            // bug 2368 - endTime can be null
            // consider only start time
            boolean endTimeCondition = true;
            if (info.getEndtime() != null)
            {
                endTimeCondition = info.getEndtime().getTime() >= currenttime;
            }
            if (info.getEnabled() && (info.getStarttime().getTime() <= currenttime) && endTimeCondition)
            {
                // === convert LDAP User ID to BPM User ID
                String sLDAPUserID = info.getAbsentPersonID();

                Integer iBPMUserID = null;
                try
                {
                    //Use UserLongName, because of P8 4.x API (bug 2441)
                    String sUserName = getRepository().getNetwork().getUserFromID(sLDAPUserID).getUserLongName();
                    iBPMUserID = Integer.valueOf(getRepository().getVWSession().convertUserNameToId(sUserName));
                }
                catch (Exception e)
                {
                    LOG.info("Could not resolve proxy by UserLongName, may be a P8 3.5 API! Person ID: " + sLDAPUserID, e);
                    try
                    {
                        //here retry with UserName for P8 3.5.x API
                        String sUserName = getRepository().getNetwork().getUserFromID(sLDAPUserID).getUserName();
                        iBPMUserID = Integer.valueOf(getRepository().getVWSession().convertUserNameToId(sUserName));
                    }
                    catch (Exception e2)
                    {
                        LOG.warn("Could not resolve proxy by UserName! Person ID: " + sLDAPUserID, e2);
                        continue;
                    }
                }

                // === create and append search node for this absent user
                OwSearchNode singleabsentfilternode = new OwSearchNode(getRepository().getFieldDefinition("F_BoundUser", getPath()), OwSearchOperator.CRIT_OP_EQUAL, iBPMUserID, 0);
                m_absentfilternode.add(singleabsentfilternode);

                // route of configured
                if (getRepository().getConfigNode().getSafeBooleanValue("ProxyRouting", false))
                {
                    // === call recursively
                    recurseAbsentFilter(cyclepreventionset_p, sLDAPUserID, filter_p);
                }
            }
        }
    }

}
