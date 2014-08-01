package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Implementation for the dummy BPM Repository.
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
public class OwDummyWorkitemRepository implements OwWorkitemRepository<OwDummyWorkitem>
{
    /**dummy launchable workflows IDs*/
    private static final OwDummyWorkflowDescription[] LAUNCHABLE_WORKFLOWS = new OwDummyWorkflowDescription[] { new OwDummyWorkflowDescription("Workflow_1"), new OwDummyWorkflowDescription("Workflow_2") };

    protected class OwDummyProxyInfo implements OwProxyInfo
    {

        private Date m_endtime;
        private Date m_starttime;
        private boolean m_fEnabled;
        private String m_sID;

        public boolean getEnabled()
        {
            return m_fEnabled;
        }

        public Date getEndtime()
        {
            return m_endtime;
        }

        public String getProxyPersonID()
        {
            return m_sID;
        }

        public Date getStarttime()
        {
            return m_starttime;
        }

        public void setEnabled(boolean fEnable_p)
        {
            m_fEnabled = fEnable_p;
        }

        public void setEndtime(Date endtime_p)
        {
            m_endtime = endtime_p;
        }

        public void setProxyPersonID(String sID_p)
        {
            m_sID = sID_p;
        }

        public void setStarttime(Date starttime_p)
        {
            m_starttime = starttime_p;
        }

    }

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "owdmbpm";

    private OwDummyNetwork m_network;

    public OwDummyWorkitemRepository(OwDummyNetwork network_p)
    {
        m_network = network_p;
    }

    public OwDummyNetwork getNetwork()
    {
        return m_network;
    }

    public boolean canProxy()
    {
        return false;
    }

    public OwProxyInfo createProxy() throws Exception, OwNotSupportedException
    {
        return new OwDummyProxyInfo();
    }

    public Collection getProxies(String absentpersonID_p) throws Exception, OwNotSupportedException
    {
        return new Vector();
    }

    public OwWorkitemContainer getWorkitemContainer(String sID_p, int iType_p) throws Exception
    {
        File file = new File(m_network.getContext().getBasePath() + m_network.getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + "/bpm/" + m_containerprefixmap.getContainerPrefix(iType_p) + "/" + sID_p);
        return new OwDummyWorkitemContainer(this, file, m_containerprefixmap.getContainerMimeType(iType_p), iType_p);
    }

    public OwWorkitemContainer getWorkitemLaunchContainer(Collection attachmentObjects_p) throws Exception
    {
        File file = new File(m_network.getContext().getBasePath() + m_network.getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + "/ow_launch/");
        return new OwDummyLaunchWorkitemContainer(this, file, "ow_workitemcontainer/launch", OwObjectReference.OBJECT_TYPE_FOLDER, attachmentObjects_p);
    }

    public Collection<String> getWorkitemContainerIDs(boolean fRefresh_p, int iType_p) throws Exception
    {
        // collect the queue names
        Collection<String> retNames = new ArrayList<String>();

        try
        {
            File[] Files = new File(m_network.getContext().getBasePath() + m_network.getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + "/bpm/" + m_containerprefixmap.getContainerPrefix(iType_p)).listFiles();

            // create Object for each file entry
            for (int i = 0; i < Files.length; i++)
            {
                if (Files[i].isDirectory())
                {
                    retNames.add(Files[i].getName());
                }
            }
        }

        catch (Exception e)
        {
            // ignore
        }

        return retNames;
    }

    public String getWorkitemContainerName(String sID_p, int iType_p) throws Exception
    {
        return sID_p;
    }

    public boolean hasContainer(int iType_p) throws Exception
    {
        return getWorkitemContainerIDs(false, iType_p).size() > 0;
    }

    public void setProxies(Collection proxies_p, String absentpersonID_p) throws Exception, OwNotSupportedException
    {

    }

    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        return new OwStandardObjectCollection();
    }

    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    public OwEventManager getEventManager()
    {
        return m_network.getEventManager();
    }

    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        return null;
    }

    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        return null;
    }

    public OwResource getResource(String strID_p) throws Exception
    {
        return null;
    }

    public Iterator getResourceIDs() throws Exception
    {
        return null;
    }

    public void refreshStaticClassdescriptions() throws Exception
    {
    }

    public void releaseResources() throws Exception
    {
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        try
        {
            return OwDummyWorkitem.getStaticObjectClass().getPropertyClass(strFieldDefinitionName_p);
        }
        catch (OwObjectNotFoundException e)
        {
            return m_network.getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
        }
    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canBatch()
     */
    public boolean canBatch()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#closeBatch(com.wewebu.ow.server.ecm.OwBatch)
     */
    public void closeBatch(OwBatch batch_p) throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#canLaunch()
     */
    public boolean canLaunch()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#createLaunchableItem(java.lang.String, java.util.Collection)
     */
    public OwWorkitem createLaunchableItem(OwWorkflowDescription workflowDescription_p, Collection attachmentobjects_p) throws Exception
    {
        OwWorkitemContainer launchContainer = getWorkitemLaunchContainer(attachmentobjects_p);
        return launchContainer.pull(null, null);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository#getLaunchableWorkflowIds(java.util.Collection)
     */
    public Collection getLaunchableWorkflowDescriptions(Collection attachmentobjects_p) throws Exception
    {
        return Arrays.asList(LAUNCHABLE_WORKFLOWS);
    }

    @Override
    public OwIterable<OwDummyWorkitem> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : Dummy BPM repository page search
        throw new OwNotSupportedException("The Dummy BPM repository does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }
}