package com.wewebu.ow.server.plug.owbpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwPriorityRuleFactory;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfoString;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Main Document
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
public class OwBPMDocument extends OwMasterDocument
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMDocument.class);

    /**
     * @since 4.2.0.0
     */
    protected static final String CONFIG_NODE_PAGEABLE = "pageable";
    /** token that identifies a delimiter in configuration */
    public static final String QUEUE_DELIMITER = "delimiter";
    private static final String EL_QUEUES = "Queues";
    private static final String EL_QUEUE = "Queue";
    private static final String ATT_HIDE_IF_EMPTY = "hideIfEmpty";

    /** settings parameter name for the column info list for the node list view. */
    public static final String SETTINGS_PARAM_COLUMN_INFO = "columninfo";

    /** settings parameter name for the sorting. */
    public static final String SETTINGS_PARAM_SORT = "ColumnSortCriteria";

    /** the bpm repository */
    protected OwWorkitemRepository m_bpmrepository;

    /** list of OwFieldColumnInfo to be used for the child list columns */
    protected List m_columnInfoList;

    /** map of List of OwPriorityRule rules, keyed by container name
     */
    protected Map m_rulemap;

    private boolean usePaging;

    /** map of List of OwStandardPriorityRule rules, keyed by container name
     * 
     * @return Map
     * @throws Exception
     */
    public Map getRuleMap() throws Exception
    {
        if (null == m_rulemap)
        {
            OwPriorityRuleFactory prFactory = OwPriorityRuleFactory.getInstance();
            OwXMLUtil configNode = getConfigNode();
            OwWorkitemRepository fieldefinitionprovider = getBpmRepository();
            m_rulemap = prFactory.createRulesContainerMap(configNode, fieldefinitionprovider);
        }

        return m_rulemap;
    }

    /** get the default column info for the child list if no column info is defined in the opened folder
     *
     * @return List of OwFieldColumnInfo
     */
    protected List getDefaultColumnInfo() throws Exception
    {
        if (m_columnInfoList == null)
        {
            // create a column info list
            m_columnInfoList = new ArrayList();

            // try to get the default column info from the plugin settings
            List popertyNameList = (List) getSafeSetting(SETTINGS_PARAM_COLUMN_INFO, null);

            Iterator it = popertyNameList.iterator();

            while (it.hasNext())
            {
                String strPropertyName = (String) it.next();

                if (null != strPropertyName)
                {
                    // add column info
                    m_columnInfoList.add(new OwStandardFieldColumnInfoString(strPropertyName, OwFieldColumnInfo.ALIGNMENT_DEFAULT));
                }
            }
        }

        return m_columnInfoList;
    }

    /**
     * Get the column sort criteria.<br/>
     * Column sort criteria can be defined via <code>&lt;ColumnSortCriteria&gt;</code> 
     * tag in <code>owplugins.xml</code> or Settings plugin.<br/>
     * Double defined properties will be filtered out.
     * 
     * @return List of OwSortCriteria
     */
    protected List getColumnSortCriteria() throws Exception
    {
        List properties = (List) getSafeSetting(SETTINGS_PARAM_SORT, null);
        List sortCriterias = new ArrayList();
        HashSet duplicateDedectionSet = new HashSet();

        if (properties != null)
        {
            Iterator it = properties.iterator();
            while (it.hasNext())
            {
                OwSortCriteria sortCriteria = (OwSortCriteria) it.next();

                if (sortCriteria == null)
                {
                    StringBuffer buf = new StringBuffer();
                    buf.append("owplugins.xml - master plugin with id=");
                    buf.append(this.getPluginID());
                    buf.append(": tag <ColumnSortCriteria> is configured wrongly:");
                    buf.append(" one <property> tag contains no value.");
                    buf.append(" This tag was ignored");
                    LOG.warn(buf.toString());
                    //no property defined, just continue
                    continue;
                }
                if (duplicateDedectionSet.contains(sortCriteria))
                {
                    StringBuffer buf = new StringBuffer();
                    buf.append("owplugins.xml - master plugin with id=");
                    buf.append(this.getPluginID());
                    buf.append(": tag <ColumnSortCriteria> is configured wrongly:");
                    buf.append(" one <property> tag contains a value=");
                    buf.append(sortCriteria.getPropertyName());
                    buf.append(" that was defined before.");
                    buf.append(" This tag was ignored");
                    LOG.warn(buf.toString());
                    //property was defined twice, just continue
                    continue;
                }

                duplicateDedectionSet.add(sortCriteria);

                boolean sortAscending = sortCriteria.getAscFlag();

                //add to sort criteria list
                sortCriterias.add(new OwSortCriteria(sortCriteria.getPropertyName(), sortAscending));
            }
        }

        return sortCriterias;
    }

    /** a collection of OwBPMVirtualQueue for each queue
     * 
     * @return Collection of OwBPMVirtualQueue or null for delimiter
     * 
     * @throws Exception, OwConfigurationException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Collection getWorkQueues() throws Exception
    {
        List queueWrapperList = new ArrayList();

        // get configuration
        OwXMLUtil queues = getConfigNode().getSubUtil(EL_QUEUES);
        if (null == queues)
        {
            return Collections.emptyList();
        }
        Collection configqueuelist = queues.getSafeUtilList(EL_QUEUE);

        // === iterate over the configuration
        // flag to get rid of double delimiters, which make no sense
        boolean fCanAddDelimiter = false;

        Iterator it = configqueuelist.iterator();
        while (it.hasNext())
        {
            OwXMLUtil aQueue = (OwXMLUtil) it.next();
            String sQueueInfo = aQueue.getSafeTextValue(null);
            boolean bHideIfEmpty = aQueue.getSafeBooleanAttributeValue(ATT_HIDE_IF_EMPTY, false);

            if (null == sQueueInfo)
            {
                continue;
            }

            // check if it is a delimiter
            try
            {
                // scan queue info
                StringTokenizer queueinfo = new StringTokenizer(sQueueInfo, OwWorkitemRepository.PATH_DELIMITER);

                String sPrefix = queueinfo.nextToken();
                int iQueuetype = OwWorkitemRepository.m_containerprefixmap.getContainerType(sPrefix);
                String sQueuename = queueinfo.nextToken();

                if (sQueuename.endsWith("*"))
                {
                    // === get all queues of this type
                    Collection queuenames = getBpmRepository().getWorkitemContainerIDs(false, iQueuetype);

                    Iterator itCont = queuenames.iterator();
                    while (itCont.hasNext())
                    {
                        String name = (String) itCont.next();

                        String sThisContainerPath = OwWorkitemRepository.PATH_DELIMITER + sPrefix + OwWorkitemRepository.PATH_DELIMITER + name;

                        List rulelist = null;
                        try
                        {
                            // get the priority rules list for this queue/s
                            rulelist = (List) getRuleMap().get(sThisContainerPath);
                            if (null == rulelist)
                            {
                                rulelist = (List) getRuleMap().get(sQueueInfo);
                            }
                        }
                        catch (Exception e)
                        {
                            LOG.error("GetWorkQueus could not create priorityrules.", e);

                        }

                        if (rulelist == null)
                        {
                            rulelist = new Vector();
                        }

                        queueWrapperList.add(new OwBPMQueue(this, getBpmRepository().getWorkitemContainer(name, iQueuetype), rulelist, bHideIfEmpty));
                        fCanAddDelimiter = true;
                    }
                }
                else
                {
                    // === get just this queue
                    String sWildCardContainerPath = OwWorkitemRepository.PATH_DELIMITER + sPrefix + OwWorkitemRepository.PATH_DELIMITER + "*";

                    // get the priority rules list for this queue/s
                    List rulelist = (List) getRuleMap().get(sQueueInfo);
                    if (null == rulelist)
                    {
                        rulelist = (List) getRuleMap().get(sWildCardContainerPath);
                    }

                    queueWrapperList.add(new OwBPMQueue(this, getBpmRepository().getWorkitemContainer(sQueuename, iQueuetype), rulelist, bHideIfEmpty));
                    fCanAddDelimiter = true;
                }
            }
            catch (OwObjectNotFoundException e)
            {
                // === could be a delimiter
                if (sQueueInfo.equals(QUEUE_DELIMITER))
                {
                    // null means delimiter
                    if (fCanAddDelimiter)
                    {
                        queueWrapperList.add(null);
                        fCanAddDelimiter = false;
                    }
                }
                else
                {
                    throw new OwConfigurationException("Unknown queue info in plugin descriptor: " + sQueueInfo, e);
                }
            }

        }

        if ((queueWrapperList.size() > 0) && (queueWrapperList.get(queueWrapperList.size() - 1) == null))
        {
            // last one must not be a delimiter remove it
            queueWrapperList.remove(queueWrapperList.size() - 1);
        }

        return queueWrapperList;
    }

    /** the bpm repository */
    public OwWorkitemRepository getBpmRepository() throws Exception
    {
        if (null == m_bpmrepository)
        {
            if (!((OwMainAppContext) getContext()).getNetwork().hasInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository"))
            {
                String msg = "OwBPMDocument.getBpmRepository: The ECM adapter does not offer an interface for BPM Repository (OwWorkitemRepository).";
                LOG.info(msg);
                throw new OwConfigurationException(getContext().localize("owbpm.OwBPMDocument.noInterfaceSupport", "The ECM adapter does not offer an interface for BPM Repository (OwWorkitemRepository)."));
            }
            m_bpmrepository = (OwWorkitemRepository) ((OwMainAppContext) getContext()).getNetwork().getInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository", null);
        }
        return m_bpmrepository;
    }

    /** get the maximum number of items to retrieve in a queue
     */
    public int getMaxChildCount()
    {
        return getConfigNode().getSafeIntegerValue("MaxChildCount", 100);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwMasterDocument#init()
     */
    @Override
    protected void init() throws Exception
    {
        super.init();
        this.usePaging = getConfigNode().getSafeBooleanValue(CONFIG_NODE_PAGEABLE, false);
    }

    /**
     * @return the usePaging
     * @since 4.2.0.0
     */
    public boolean isUsePaging()
    {
        return usePaging;
    }

    protected OwLoadContext createLoadContext(OwBPMVirtualQueue queue) throws Exception
    {
        OwLoadContext loadContext = new OwLoadContext();

        //        Collection propertyNameList = m_DocumentListView.getRetrievalPropertyNames();
        //        loadContext.setPropertyNames(propertyNameList);

        //        int[] types;
        //        if (showFolderInResultList())
        //        {
        //            types = new int[4];
        //            types[3] = OwObjectReference.OBJECT_TYPE_FOLDER;
        //        }
        //        else
        //        {
        //            types = new int[3];
        //        }

        //        types[0] = OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS;
        //        types[1] = OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS;
        //        types[2] = OwObjectReference.OBJECT_TYPE_ALL_TUPLE_OBJECTS;
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS);

        OwSort sort = queue.getSort();
        loadContext.setSorting(sort);

        //        int maxChildSize = getMaxChildSize();
        //        loadContext.setMaxSize(maxChildSize);

        int versionSelectDefault = OwSearchTemplate.VERSION_SELECT_DEFAULT;
        loadContext.setVersionSelection(versionSelectDefault);

        //        OwSearchNode filterCriteria = queue.getFilterSearch();
        //        loadContext.setFilter(filterCriteria);
        return loadContext;

    }
}