package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwObjectCollectionIterableAdapter;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Abstract list view which contains a plugin cache utility class.
 * Providing setter and getter methods for derived classes and 
 * also some simplified factory methods for utility instance creation.
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
 *@since 3.1.0.4
 */
public abstract class OwObjectListViewPluginCache extends OwPageableListView
{

    private static final Logger LOG = OwLogCore.getLogger(OwObjectListViewPluginCache.class);

    /**the reference to the plugin cache utility*/
    private OwPluginStatusCachingUtility pluginCache;

    public OwObjectListViewPluginCache()
    {
        super();
    }

    public OwObjectListViewPluginCache(int viewMask)
    {
        super(viewMask);
    }

    /**
     * Simple getter method for the plugin cache utility.
     * @return OwPluginStatusCachingUtility which is currently set (can return null if not set)
     */
    protected OwPluginStatusCachingUtility getPluginCache()
    {
        return this.pluginCache;
    }

    /**
     * Simple setter for used cache instance.
     * @param newCache OwPluginStatusCachingUtility
     */
    protected void setPluginCache(OwPluginStatusCachingUtility newCache)
    {
        this.pluginCache = newCache;
    }

    /**
     * Create the plugin status cache.
     * Requesting all other information from extended class.
     * @param startIndex int the index of first object visible in list
     * @param endIndex int the index of last object visible in list
     * @param pluginEntries Collection of OwPluginEntry
     * @return a {@link OwObjectListViewPluginCache.OwPluginStatusCachingUtility} object.
     * @throws Exception
     * @deprecated since 4.2.0.0 use {@link #createPluginStatusCacheUtility(Collection)} instead
     */
    @Deprecated
    protected OwPluginStatusCachingUtility createPluginStatusCacheUtility(int startIndex, int endIndex, Collection<OwPluginEntry> pluginEntries) throws Exception
    {
        return createPluginStatusCacheUtility(startIndex, endIndex, getObjectList(), getParentObject(), pluginEntries, isEmptyPluginColumnRendered());
    }

    /**
     * Create the plugin status cache instance for provided information.
     * @param startIndex
     * @param endIndex
     * @param objectList
     * @param parentObject
     * @param pluginEntries
     * @param displayEmptyPluginColumn
     * @return throw not supported exception
     * @throws Exception
     * @deprecated since 4.2.0.0 use {@link #createPluginStatusCacheUtility(Collection)} instead
     */
    @Deprecated
    protected OwPluginStatusCachingUtility createPluginStatusCacheUtility(int startIndex, int endIndex, List objectList, OwObject parentObject, Collection<OwPluginEntry> pluginEntries, boolean displayEmptyPluginColumn) throws Exception
    {
        OwIterable<OwObject> page = new OwObjectCollectionIterableAdapter<OwObject>((OwObjectCollection) objectList);
        return new OwPluginStatusCachingUtility(page.skipTo(startIndex).getPage(endIndex - startIndex), parentObject, pluginEntries, displayEmptyPluginColumn);
    }

    /**
     * Simplified cache creation where nearly all information is requested through specific Getter methods.
     * @param pluginEntries Collection of OwPluginEntry
     * @return OwPluginStatusCachingUtility
     * @throws Exception
     * @since 4.2.0.0
     */
    protected OwPluginStatusCachingUtility createPluginStatusCacheUtility(Collection<OwPluginEntry> pluginEntries) throws Exception
    {
        return createPluginStatusCacheUtility(getDisplayedPage(), getParentObject(), pluginEntries, isEmptyPluginColumnRendered());
    }

    /**
     * Create a cache based on new paging interface.
     * @param page OwIterable containing OwObject
     * @param parentObject OwObject which is parent of the objects in page (can be null)
     * @param pluginEntries Collection of OwPluginEntry
     * @param displayEmptyPluginColumn displayEmptyPluginColumn boolean configuration if empty columns should be displayed
     * @return OwPluginStatusCachingUtility
     * @throws Exception if cache could not be created
     * @since 4.2.0.0
     */
    protected OwPluginStatusCachingUtility createPluginStatusCacheUtility(OwIterable<OwObject> page, OwObject parentObject, Collection<OwPluginEntry> pluginEntries, boolean displayEmptyPluginColumn) throws Exception
    {
        return new OwPluginStatusCachingUtility(page, parentObject, pluginEntries, displayEmptyPluginColumn);
    }

    /**
     *<p>
     * Utility class for caching the enabled/disabled status
     * for a plugin, for each visible object in list.
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
     *@since 3.0.0.0
     */
    protected static class OwPluginStatusCachingUtility
    {
        /**cached map between objects and plugins and their status*/
        private Map<String, Boolean> m_object2PluginStatusMap;
        /**flag indicating that empty plugin column should be displayed or not*/
        private boolean m_displayEmptyPluginColumn;
        /**list with plugins that are disabled for all objects*/
        private List<OwPluginEntry> m_disabledPlugins;
        /**maximum number of icons per plugin entry*/
        private Map<Integer, Integer> m_maxNumberOfIconsPerEntry;

        /**
         * Constructor
         * @param startIndex_p - the index of first object visible in list.
         * @param endIndex_p - the index of last object visible in list.
         * @param objectList_p - the list of objects.
         * @param parentObject_p - the parent object.
         * @param pluginEntries_p - the plugin entries.
         * @throws Exception - thrown when the caching cannot be created, usually when the DMSID cannot be obtain for an object in the list.
         * @deprecated since 4.2.0.0 use {@link #OwPluginStatusCachingUtility(OwIterable, OwObject, Collection, boolean)} instead
         */
        public OwPluginStatusCachingUtility(int startIndex_p, int endIndex_p, OwObjectCollection objectList_p, OwObject parentObject_p, Collection<OwPluginEntry> pluginEntries_p) throws Exception
        {
            m_displayEmptyPluginColumn = false;
            m_disabledPlugins = new LinkedList<OwPluginEntry>();
            m_maxNumberOfIconsPerEntry = new HashMap<Integer, Integer>();
            m_object2PluginStatusMap = new HashMap<String, Boolean>();
            OwIterable<OwObject> page = new OwObjectCollectionIterableAdapter<OwObject>(objectList_p);
            try
            {
                this.preparePluginInfo(page.skipTo(startIndex_p).getPage(endIndex_p - startIndex_p), parentObject_p, pluginEntries_p);
            }
            catch (Exception e)
            {
                LOG.error("OwPluginStatusCachingUtility.constructor: Cannot prepare plugin info. Reason:", e);
                throw e;
            }

        }

        /**
         * Constructor
         * @param page OwIterable with OwObjects
         * @param parent - the parent object.
         * @param pluginEntries - the plugin entries.
         * @param displayEmptyPluginColumn_p - should be empty plugin columns drawn
         * @throws Exception
         */
        public OwPluginStatusCachingUtility(OwIterable<OwObject> page, OwObject parent, Collection<OwPluginEntry> pluginEntries, boolean displayEmptyPluginColumn_p) throws Exception
        {
            m_displayEmptyPluginColumn = displayEmptyPluginColumn_p;
            m_disabledPlugins = new LinkedList<OwPluginEntry>();
            m_maxNumberOfIconsPerEntry = new HashMap<Integer, Integer>();
            m_object2PluginStatusMap = new HashMap<String, Boolean>();
            try
            {
                this.preparePluginInfo(page, parent, pluginEntries);
            }
            catch (Exception e)
            {
                LOG.error("OwPluginStatusCachingUtility.constructor: Cannot prepare plugin info. Reason:", e);
                throw e;
            }
        }

        /**
         * add plugin status for a given plugin and object.
         * @param object_p
         * @param pluginEntry_p
         * @param isEnabled_p
         */
        private void addPluginStatusForObject(OwObject object_p, OwPluginEntry pluginEntry_p, boolean isEnabled_p)
        {
            try
            {
                String key = createKey(object_p, pluginEntry_p);
                m_object2PluginStatusMap.put(key, isEnabled_p ? Boolean.TRUE : Boolean.FALSE);
            }
            catch (Exception e)
            {
                //nothing to do, just log
                LOG.error("Cannot add plugin status for object: " + object_p + " and for plugin entry: " + pluginEntry_p, e);
            }

        }

        /**
         * Create a key for the map. The key is created from object's DMSID and plugin index.
         * @param object_p - the object in the list.
         * @param pluginEntry_p - the plugin entry.
         * @return the key - the key.
         * @throws Exception - thrown when object DMSID cannot be obtained.
         */
        private String createKey(OwObject object_p, OwPluginEntry pluginEntry_p) throws Exception
        {
            String objectId = object_p.getDMSID();
            String key = objectId + "_" + pluginEntry_p.getIndex();
            return key;
        }

        private synchronized void preparePluginInfo(OwIterable<OwObject> page, OwObject parent, Collection<OwPluginEntry> instancePluginsList) throws Exception
        {
            StopWatch preparePluginInfoStopWatch = new Log4JStopWatch("preparePluginInfo");
            try
            {
                Iterator<OwPluginEntry> pluginsIterator = (new CopyOnWriteArraySet<OwPluginEntry>(instancePluginsList)).iterator();
                //Iterator pluginsIterator = instancePluginsList_p.iterator();
                m_disabledPlugins = new LinkedList<OwPluginEntry>();
                while (pluginsIterator.hasNext())
                {
                    OwPluginEntry entry = pluginsIterator.next();
                    processPluginEntry(page, parent, entry);
                }
            }
            finally
            {
                preparePluginInfoStopWatch.stop();
            }
        }

        private void processPluginEntry(OwIterable<OwObject> page, OwObject parent, OwPluginEntry entry) throws Exception
        {
            boolean isPluginEnabled = false;

            String pluginName = entry.getPlugin().getName();
            StopWatch pluginStopWatch = new Log4JStopWatch("plugin.isEnabled." + pluginName);

            try
            {
                for (OwObject obj : page)
                {
                    if (obj != null)
                    {
                        if (entry.m_Plugin.isEnabled(obj, parent, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
                        {
                            String icon = entry.m_Plugin.getIconHTML(obj, parent);
                            OwIcon owIcon = new OwIcon(icon);
                            int numberOfIcons = owIcon.getNumberOfIcons();
                            addNumberOfIconsForEntry(entry, numberOfIcons);
                            if (numberOfIcons > 0)
                            {
                                isPluginEnabled = true;
                                addPluginStatusForObject(obj, entry, true);
                            }
                            else
                            {
                                addPluginStatusForObject(obj, entry, false);
                            }
                        }
                        else
                        {
                            addPluginStatusForObject(obj, entry, false);
                        }
                    }
                }
            }
            finally
            {
                pluginStopWatch.stop();
            }
            if (!isPluginEnabled)
            {
                m_disabledPlugins.add(entry);
                addNumberOfIconsForEntry(entry, 0);
            }
        }

        private void addNumberOfIconsForEntry(OwPluginEntry entry_p, int numberOfIcons_p)
        {
            Integer key = Integer.valueOf(entry_p.getIndex());
            if (m_maxNumberOfIconsPerEntry.containsKey(key))
            {
                Integer existingNumberOfIcons = m_maxNumberOfIconsPerEntry.get(key);
                if (existingNumberOfIcons.intValue() < numberOfIcons_p)
                {
                    m_maxNumberOfIconsPerEntry.put(key, Integer.valueOf(numberOfIcons_p));
                }
            }
            else
            {
                m_maxNumberOfIconsPerEntry.put(key, Integer.valueOf(numberOfIcons_p));
            }
        }

        public int getMaximumNumberOfIconsForEntry(OwPluginEntry entry_p)
        {
            int result = 0;
            if (entry_p != null)
            {
                Integer resultAsInt = m_maxNumberOfIconsPerEntry.get(Integer.valueOf(entry_p.getIndex()));
                result = resultAsInt != null ? resultAsInt.intValue() : result;
            }
            return result;
        }

        public int getMaximumNumberOfIcons()
        {
            int result = 0;
            for (Integer value : m_maxNumberOfIconsPerEntry.values())
            {
                result += value.intValue();
            }
            return result;
        }

        /**
         * check if the given plugin is disabled for all visible objects.
         * @param entry_p
         * @return <code>true</code> if the plugin is disabled for all visible objects.
         */
        public boolean isPluginDisabledForAllObjects(OwPluginEntry entry_p)
        {
            boolean result = m_disabledPlugins.contains(entry_p);
            if (m_displayEmptyPluginColumn)
            {
                result = false;
            }
            return result;
        }

        /**
         * Returns the cached plugin status.
         * @param pluginEntry_p - plugin entry
         * @param obj_p - the object.
         * @return a {@link OwObjectListViewPluginCache.OwPluginStatus} instance.
         */
        public OwPluginStatus getCachedPluginState(OwPluginEntry pluginEntry_p, OwObject obj_p)
        {
            OwPluginStatus result;
            try
            {
                String key = createKey(obj_p, pluginEntry_p);
                Boolean status = m_object2PluginStatusMap.get(key);
                if (status == null)
                {
                    result = OwPluginStatus.NOT_CACHED;
                }
                else
                {
                    result = new OwPluginStatus(true, status.booleanValue());
                }
            }
            catch (Exception e)
            {
                result = OwPluginStatus.NOT_CACHED;
            }
            return result;
        }

        /**
         * Get the number of disabled plugins.
         * @return - the number of plugins that are disabled for all displayed objects.
         */
        public int getNumberOfDisabledPlugins()
        {
            return m_disabledPlugins.size();
        }
    }

    /**
     *<p>
     *  Class which holds the plugin status.
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
     *@since 3.0.0.0
     */
    protected static class OwPluginStatus
    {
        /**is cached flag*/
        private boolean isCached;
        /**is enabled flag*/
        private boolean isEnabled;
        /**NOT cached status*/
        public static final OwPluginStatus NOT_CACHED = new OwPluginStatus(false, false);

        /**constructor*/
        public OwPluginStatus(boolean isCached_p, boolean isEnabled_p)
        {
            this.isCached = isCached_p;
            this.isEnabled = isEnabled_p;
        }

        /**is cached */
        public boolean isCached()
        {
            return isCached;
        }

        /**is enabled */
        public boolean isEnabled()
        {
            return isEnabled;
        }
    }
}
