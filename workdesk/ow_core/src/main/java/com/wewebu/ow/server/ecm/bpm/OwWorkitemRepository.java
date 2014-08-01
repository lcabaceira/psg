package com.wewebu.ow.server.ecm.bpm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

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
public interface OwWorkitemRepository<W extends OwWorkitem> extends OwRepository<W>
{

    /**
     *<p>
     * Proxy object information.
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
    public interface OwProxyInfo
    {
        public abstract String getProxyPersonID();

        public abstract java.util.Date getStarttime();

        public abstract java.util.Date getEndtime();

        public abstract boolean getEnabled();

        public abstract void setProxyPersonID(String sID_p);

        public abstract void setStarttime(java.util.Date starttime_p);

        public abstract void setEndtime(java.util.Date endtime_p);

        public abstract void setEnabled(boolean fEnable_p);

    };

    /** delimiter used to build paths and structure the containers into folders and subfolders,
     * see also m_containerprefixmap */
    public static String PATH_DELIMITER = "/";

    /**
     *<p>
     * Singleton that maps BPM container prefixes to types, 
     * used to organize the different container types for configuration, 
     * e.g. search templates or folder structures,
     * see also PATH_DELIMITER.
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
    public static class OwContainerPrefixMapSingleton
    {
        private Map m_prefixmap;
        private Map m_typemap;

        public OwContainerPrefixMapSingleton()
        {
            m_prefixmap = new HashMap();
            m_typemap = new HashMap();

            addContainerInfo("proxy", OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER);
            addContainerInfo("roster", OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER);
            addContainerInfo("tracker", OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER);
            addContainerInfo("user", OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
            addContainerInfo("public", OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER);
            addContainerInfo("system", OwObjectReference.OBJECT_TYPE_SYS_QUEUE_FOLDER);
            addContainerInfo("cross", OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER);
        }

        private void addContainerInfo(String sName_p, int iType_p)
        {
            m_typemap.put(sName_p, Integer.valueOf(iType_p));
            m_prefixmap.put(Integer.valueOf(iType_p), sName_p);
        }

        /** get the container prefix for the given type
         * 
         * @param iType_p
         * @return String BPM container prefix
         * 
         * @throws OwObjectNotFoundException 
         */
        public String getContainerPrefix(int iType_p) throws OwObjectNotFoundException
        {
            try
            {
                return "ow_" + ((String) m_prefixmap.get(Integer.valueOf(iType_p)));
            }
            catch (NullPointerException e)
            {
                throw new OwObjectNotFoundException("OwWorkitemRepository$OwContainerPrefixMapSingleton.getContainerPrefix: Could not find queue name for type = " + iType_p, e);
            }
        }

        /** get the container prefix for the given type
         * 
         * @param iType_p
         * @return String BPM container prefix
         * 
         * @throws OwObjectNotFoundException 
         */
        public String getContainerMimeType(int iType_p) throws OwObjectNotFoundException
        {
            try
            {
                return "ow_workitemcontainer/" + ((String) m_prefixmap.get(Integer.valueOf(iType_p)));
            }
            catch (NullPointerException e)
            {
                throw new OwObjectNotFoundException("OwWorkitemRepository$OwContainerPrefixMapSingleton.getContainerMimeType: Could not find mimetype for type = " + iType_p, e);
            }
        }

        /** get the container type for a given queuename prefix
         * 
         * @param sContainerPrefix_p
         * 
         * @return int OwObjectReference.OBJECT_TYPE_...
         * 
         * @throws OwObjectNotFoundException
         */
        public int getContainerType(String sContainerPrefix_p) throws OwObjectNotFoundException
        {
            try
            {
                return ((Integer) m_typemap.get(sContainerPrefix_p.substring(3))).intValue();
            }
            catch (NullPointerException e)
            {
                throw new OwObjectNotFoundException("OwWorkitemRepository$OwContainerPrefixMapSingleton.getContainerType: Could not find queue type for prefix = " + sContainerPrefix_p, e);
            }
        }

        public Collection getContainerTypes()
        {
            return m_typemap.values();
        }
    }

    /** singleton that maps queuecontainer prefixes to types, 
     * used to organize the different queue types for configuration, 
     * e.g. search templates or folder structures */
    public static OwContainerPrefixMapSingleton m_containerprefixmap = new OwContainerPrefixMapSingleton();

    /** get a collection of ID's for each container 
    *
    * @param fRefresh_p boolean true = update collection from server
    * @param iType_p int Type of the requested container names as defined in OwObjectReference.OBJECT_TYPE_...
    * 
    * @return Collection of String
    */
    public abstract java.util.Collection getWorkitemContainerIDs(boolean fRefresh_p, int iType_p) throws Exception;

    /** get a OwWorkitemContainer wrapper for the given container name 
    *
    * @param sID_p String ID of container
    * @param iType_p int Type of the requested container names as defined in OwObjectReference.OBJECT_TYPE_...
    * 
    * @return OwWorkitemContainer or throws OwObjectNotFoundException
    */
    public abstract OwWorkitemContainer getWorkitemContainer(String sID_p, int iType_p) throws Exception;

    /** get a displayname for the given container name 
    *
    * @param sID_p String ID of container
    * @param iType_p int Type of the requested container names as defined in OwObjectReference.OBJECT_TYPE_...
    * 
    * @return String display name
    */
    public abstract String getWorkitemContainerName(String sID_p, int iType_p) throws Exception;

    /** check if containers are available
    * @param iType_p int Type of the requested container names as defined in OwObjectReference.OBJECT_TYPE_...
    * */
    public abstract boolean hasContainer(int iType_p) throws Exception;

    /**
     * create a new proxy info used in setProxies
     */
    public abstract OwProxyInfo createProxy() throws Exception, OwNotSupportedException;

    /** set proxies, to receive the workitems
     * NOTE: All existing proxies for the given absent person will be deleted
     * 
     * @param proxies_p Collection of OwProxyInfo
     * @param absentpersonID_p String the person which is absent 
     * 
     * @throws Exception
     */
    public abstract void setProxies(java.util.Collection proxies_p, String absentpersonID_p) throws Exception, OwNotSupportedException;

    /** 
    * get proxy info for given absent person
    * 
    * @param absentpersonID_p String ID of absent person
    * 
    * @return Collection of OwProxyInfo
    * */
    public abstract java.util.Collection getProxies(String absentpersonID_p) throws Exception, OwNotSupportedException;

    /** check if repository allows proxy settings
     * 
     * @return boolean true = allows proxy users to be set
     */
    public abstract boolean canProxy();

    /** get a collection of {@link OwWorkflowDescription}s for the launchable workflows in the repository.
     *  The returned list describes only those workflows 
     *  the user has appropriate access right's / roles to launch the workflow.
     *  
     *  If attachmentobjects_p are submitted, the method will only return those workflow descriptions,
     *  which can accept the given OwObject's as initial attachments. 
     *   
     * @param attachmentobjects_p optional collection of attachment OwObject's, can be null
     * 
     * @return Collection of OwWorkflowDescription or null if no launch workflows are available
     * @throws Exception
     */
    public abstract Collection getLaunchableWorkflowDescriptions(Collection attachmentobjects_p) throws Exception;

    /** create a temporary work item for the given workflow ID that can be edited in a launch processor
     * NOTE: The item is not launched until its dispatch method is called.
     *  
     * @see OwWorkitem#dispatch()
     *  
     * @param workflowDescription_p the description of the workflow to launch
     * @param attachmentobjects_p optional collection of attachment OwObject's, can be null
     * @return OwWorkitem
     * 
     * @throws Exception
     */
    public abstract OwWorkitem createLaunchableItem(OwWorkflowDescription workflowDescription_p, Collection attachmentobjects_p) throws Exception;

    /** check if repository supports the creation of launchable items.<br>
     * The actual launching of items is performed by calling the dispatch method
     * of a launchable item.
     * @see #createLaunchableItem(OwWorkflowDescription, Collection)
     * 
     * @return boolean
     */
    public abstract boolean canLaunch();
}