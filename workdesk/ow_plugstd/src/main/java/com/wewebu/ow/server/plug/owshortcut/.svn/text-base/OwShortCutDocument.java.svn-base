package com.wewebu.ow.server.plug.owshortcut;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * Short Cuts Document. Master plugin that displays and executes shortcuts.
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwShortCutDocument extends OwMasterDocument
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutDocument.class);

    /** general ID in the plugin ID to be configure the plugin IDs for attachments */
    public static final String PLUGIN_CONFIG_SHORTCUT_FUNCTIONS = "ShortcutDocumentFunctions";

    public static final String PLUGIN_CONFIG_MAXCHILDSIZE = "MaxChildSize";

    /** settings parameter name for the column info list for the node list view. */
    public static final String SETTINGS_PARAM_COLUMN_INFO = "columninfo";

    /** settings parameter name for the sorting. */
    public static final String SETTINGS_PARAM_SORT = "ColumnSortCriteria";

    /** list of OwShortCutItem */
    private OwShortCutDocumentHelper m_shortcutsHelper;

    /** list of document function IDs to be applied to object pointing shortcuts*/
    private List m_shortcutFunctionIDs;

    /** maximum number of child nodes used in fetching relative path shortcuts*/
    protected int m_iMaxChildSize;

    /**configured default column list*/
    private List m_defaultColumnInfoList;

    /** document event listeners list - see {@link OwShortcutDocumentEventListner} */
    private List<OwShortcutDocumentEventListner> m_eventListeners;

    /** default sort configuration */
    private OwSort m_defaultObjectListSort;

    public OwShortCutDocument()
    {
        m_iMaxChildSize = 50;
        m_eventListeners = new LinkedList<OwShortcutDocumentEventListner>();
    }

    /**
     * Add new ShortCuts to you ShrotCut(Favorites) List
     * 
     * @param objects_p Collection of OwObject's
     * @throws OwShortCutException
     */
    public void addOwObjectShortCuts(Collection objects_p) throws OwShortCutException
    {
        // create wrapper short cut items
        List objectReferences = new LinkedList();
        OwMainAppContext mainAppContext = (OwMainAppContext) getContext();
        OwNetwork network = mainAppContext.getNetwork();

        try
        {
            for (Iterator i = objects_p.iterator(); i.hasNext();)
            {
                OwObjectReference reference = (OwObjectReference) i.next();
                OwStandardObjectReference stdReference;
                String refString = OwStandardObjectReference.getCompleteReferenceString(reference, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
                stdReference = new OwStandardObjectReference(refString, network);

                objectReferences.add(stdReference);
            }

            Collection oShortCuts = OwShortCutItemOwObject.createShortCutItems(objectReferences);
            addShortCuts(oShortCuts);
        }
        catch (Exception e)
        {
            throw new OwShortCutException("Error while adding shortcuts !", e);
        }
    }

    /** add new ShortCut to you ShrotCut(Favorites) List
     * 
     * @param object_p
     * @param path_p
     * @param subdisplaypath_p 
     * @throws OwShortCutException
     */
    public void addOwObjectShortCut(OwObject object_p, String path_p, String subdisplaypath_p) throws OwShortCutException
    {
        // create wrapper short cut items
        OwMainAppContext mainAppContext = (OwMainAppContext) getContext();
        OwNetwork network = mainAppContext.getNetwork();

        OwObject target;
        try
        {
            String parentPath = object_p.getPath();
            if (parentPath.endsWith("/"))
            {
                target = network.getObjectFromPath(parentPath + subdisplaypath_p.substring(1), false);
            }
            else
            {
                target = network.getObjectFromPath(parentPath + subdisplaypath_p, false);
            }
        }
        catch (Exception e)
        {
            throw new OwShortCutException("Error while adding shortcut !", e);
        }

        OwShortCutItem item = new OwShortCutItemOwObject(object_p, target, subdisplaypath_p, path_p);
        List shortcuts = new LinkedList();
        shortcuts.add(item);
        addShortCuts(shortcuts);

    }

    /**
     * Add new ShortCuts to you ShrotCut(Favorites) List
     * 
     * @param shortcuts_p Collection of OwShortCutItem's
     * @throws OwShortCutException
     */
    public void addShortCuts(Collection shortcuts_p) throws OwShortCutException
    {
        OwShortCutDocumentHelper helper = getShortCutDocumentHelper();

        if (helper.addShortCut(shortcuts_p))
        {
            // save
            try
            {
                OwAttributeBagWriteable bag = getPersistentAttributeBagWriteable();
                for (Iterator iterator = shortcuts_p.iterator(); iterator.hasNext();)
                {
                    OwShortCutItem owShortCutItem = (OwShortCutItem) iterator.next();
                    String persistString = owShortCutItem.saveShortCut();
                    bag.setAttribute(owShortCutItem.getId(), persistString);
                }
                bag.save();
                fireShortcutDocumentChaged();
            }
            catch (Exception e)
            {
                String msg = getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.errorsaving", "Error saving the shortcuts to AttributeBag");
                LOG.error(msg, e);
                throw new OwShortCutException(msg, e);
            }
        }
    }

    public void deleteShortCut(String id_p) throws OwShortCutException
    {
        deleteShortCuts(Arrays.asList(new String[] { id_p }));
    }

    /**
     * Delete ShortCuts from your ShortCut(Favorites) List by indexes found in the given
     * {@link List} as {@link String}s
     *  
     * @param shortCutIds_p lsit of  shortcut IDs to delete 
     * @throws OwShortCutException
     * @since 2.5.3.0
     */
    public void deleteShortCuts(List shortCutIds_p) throws OwShortCutException
    {
        OwShortCutDocumentHelper helper = getShortCutDocumentHelper();

        for (Iterator i = shortCutIds_p.iterator(); i.hasNext();)
        {
            String id = (String) i.next();
            String persistentId = helper.getPersistentId(id);
            if (helper.deleteShortCut(id))
            {
                // save
                try
                {
                    OwAttributeBagWriteable bag = getPersistentAttributeBagWriteable();
                    bag.remove(persistentId);
                    bag.save();

                }
                catch (Exception e)
                {
                    String msg = "Error deleting ShortCuts from AttributeBag";
                    LOG.error(msg, e);
                    throw new OwShortCutException(msg, e);
                }
            }
        }

        fireShortcutDocumentChaged();
    }

    /** get list of OwShortCutItem 
     * @throws OwShortCutException 
     */
    public List getShortCutItems() throws OwShortCutException
    {
        return getShortCutDocumentHelper().getShortcuts();
    }

    /**
     * init the shortcut pool with elements from the PersistentAttributeBagWriteable
     * @throws OwShortCutException 
     */
    private void initList() throws OwShortCutException
    {
        this.m_shortcutsHelper = createShortCutHelper();
        try
        {
            // load persistent short cuts
            OwAttributeBagWriteable bag = getPersistentAttributeBagWriteable();

            Collection attributeName = bag.getAttributeNames();
            OwShortCutItemContext ctx = getShortCutItemContext();
            for (Iterator iterator = attributeName.iterator(); iterator.hasNext();)
            {
                String persistString = (String) bag.getAttribute((String) iterator.next());
                OwShortCutItem item = deserializeShortCut(persistString, ctx);
                this.m_shortcutsHelper.addShortCut(item);
            }
        }
        catch (OwShortCutException e)
        {
            throw e;
        }
        catch (Exception e1)
        {
            String msg = "Could not init owShortCutDocument, there are problems with the PersistentAttributeBagWriteable..";
            LOG.error(msg, e1);
            throw new OwShortCutException(msg, e1);
        }
    }

    /**
     * Getter to get helper for ShortCut handling.
     * @return OwShortCutDocumentHelper
     * @throws OwShortCutException
     * @since 4.2.0.0
     */
    protected OwShortCutDocumentHelper getShortCutDocumentHelper() throws OwShortCutException
    {
        if (null == this.m_shortcutsHelper)
        {
            initList();
        }
        return this.m_shortcutsHelper;
    }

    /**
     * 
     * @return a {@link List} of document function IDs to be used with shortcuts pointing to {@link OwObject}s 
     * @since 2.5.3.0
     */
    public List getShortcutFunctionIDs()
    {
        return m_shortcutFunctionIDs;
    }

    /**
     * Get the column sort criteria.<br/>
     * Column sort criteria can be defined via <code>&lt;ColumnSortCriteria&gt;</code> 
     * tag in <code>owplugins.xml</code> or Settings plugin.<br/>
     * Double defined properties will be filtered out.
     * 
     * @return List of OwSortCriteria
     * @since 2.5.3.0
     */
    public OwSort getColumnSortCriteria() throws Exception
    {
        if (m_defaultObjectListSort == null)
        {
            List properties = (List) getSafeSetting(SETTINGS_PARAM_SORT, null);
            LinkedList sortCriterias = new LinkedList();
            HashSet duplicateDedectionSet = new HashSet();

            if (properties != null)
            {
                Iterator it = properties.iterator();
                while (it.hasNext())
                {
                    OwSortCriteria sortCriteria = (OwSortCriteria) it.next();

                    if (sortCriteria == null)
                    {
                        StringBuilder buf = new StringBuilder();
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
                        StringBuilder buf = new StringBuilder();
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

            //  initialize sort with sort from user preferences
            if (sortCriterias != null)
            {
                OwMainAppContext context = (OwMainAppContext) getContext();

                m_defaultObjectListSort = new OwSort(context.getMaxSortCriteriaCount(), true);

                /* first criteria defined in owplugins.xml should have highest priority, but in
                 * OwSort the latest added criteria have highest priority. So it is necessary to
                 * add criteria in reverse order.*/
                while (!sortCriterias.isEmpty())
                {
                    m_defaultObjectListSort.addCriteria((OwSortCriteria) sortCriterias.removeLast());
                }
            }
        }
        return m_defaultObjectListSort;
    }

    /**
     *  get the default column info for the list of children if no column info is defined in the opened folder
     * @since 2.5.3.0
     */
    public Collection getDefaultColumnInfo() throws Exception
    {
        if (null == m_defaultColumnInfoList)
        {
            // try to get the default column info from the plugin settings
            List defaultPropertyNameList = (List) getSafeSetting(SETTINGS_PARAM_COLUMN_INFO, null);
            if (defaultPropertyNameList != null)
            {
                // create a column info list
                m_defaultColumnInfoList = new LinkedList();

                Iterator it = defaultPropertyNameList.iterator();

                while (it.hasNext())
                {
                    String strPropertyName = (String) it.next();

                    // get display name
                    OwFieldDefinition fielddef = null;
                    try
                    {
                        fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(strPropertyName, null);
                    }
                    catch (OwObjectNotFoundException e)
                    {
                        // === property not found
                        // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                        LOG.warn("Could not resolve property for contentlist: " + strPropertyName);

                        // remove invalid property
                        it.remove();

                        // try next one
                        continue;
                    }

                    // add column info
                    m_defaultColumnInfoList.add(new OwStandardFieldColumnInfo(fielddef));
                }
            }
            else
            {
                // === create empty default lists
                m_defaultColumnInfoList = new LinkedList();
            }
        }

        return m_defaultColumnInfoList;
    }

    protected void init() throws Exception
    {
        super.init();
        m_shortcutFunctionIDs = getConfigNode().getSafeStringList(OwShortCutDocument.PLUGIN_CONFIG_SHORTCUT_FUNCTIONS);

        m_iMaxChildSize = getConfigNode().getSafeIntegerValue(OwShortCutDocument.PLUGIN_CONFIG_MAXCHILDSIZE, 200);

    }

    /**
     * 
     * @param listener_p listener to be added 
     * @since 2.5.3.0
     */
    public void addEventListener(OwShortcutDocumentEventListner listener_p)
    {
        synchronized (m_eventListeners)
        {
            m_eventListeners.add(listener_p);
            //listener_p.onShortcutDocumentChaged(this);//not needed at registration time
        }
    }

    /**
     * 
     * @param listener_p listener to be removed
     * @since 2.5.3.0
     */
    public void removeEventListener(OwShortcutDocumentEventListner listener_p)
    {
        synchronized (m_eventListeners)
        {
            m_eventListeners.remove(listener_p);
        }
    }

    /**
     * Triggers the {@link OwShortcutDocumentEventListner#onShortcutDocumentChaged(OwShortCutDocument)}
     * event for all registered listeners.
     * @since 2.5.3.0
     */
    protected void fireShortcutDocumentChaged()
    {
        synchronized (m_eventListeners)
        {
            for (Iterator i = m_eventListeners.iterator(); i.hasNext();)
            {
                OwShortcutDocumentEventListner listener = (OwShortcutDocumentEventListner) i.next();
                listener.onShortcutDocumentChaged(this);
            }
        }

    }

    /** overridable, create a map of repositories that can be searched
     */
    protected Map getRepositories()
    {
        // === create map of repositories to search on 
        Map repositories = new HashMap();

        repositories.put("OW_HISTORY", ((OwMainAppContext) getContext()).getHistoryManager());
        repositories.put("OW_ECM_ADAPTER", ((OwMainAppContext) getContext()).getNetwork());

        return repositories;
    }

    /**
     * Refresh the list with shortcuts.
     * @throws OwShortCutException 
     * @since 3.1.0.3
     */
    public void refreshShortcuts() throws OwShortCutException
    {
        OwShortCutDocumentHelper helper = getShortCutDocumentHelper();
        try
        {
            helper.refresh(((OwMainAppContext) getContext()).getNetwork());
        }
        catch (Exception e)
        {
            LOG.error("Cannot refresh shortcut list", e);
            throw new OwShortCutException("Cannot refresh shortcut list", e);
        }
    }

    /**(overridable)
     * Factory method for the Helper.
     * @return OwShortCutDocumentHelper
     * @since 3.2.0.4
     */
    protected OwShortCutDocumentHelper createShortCutHelper()
    {
        return new OwShortCutDocumentHelper();
    }

    /**(overridable)
     * Factory method called to create an OwShortCutItem object from provided String.<br />
     * If OwShortCut item cannot be instantiated from string a exception will be thrown.
     * @param persistString String representing short cut item (favorite)
     * @param context OwShortCutItemContext with references to AppContext and MIME manager
     * @return OwShortCutItem
     * @throws OwException
     * @since 3.2.0.4
     */
    protected OwShortCutItem deserializeShortCut(String persistString, OwShortCutItemContext context) throws OwException
    {
        return OwShortCutItem.loadShortCut(persistString, context, m_iMaxChildSize);
    }

    /**
     * Get/Create OwShortCutItemContext
     * @return OwShortCutItemContext
     * @since 4.2.0.0
     */
    protected OwShortCutItemContext getShortCutItemContext()
    {
        OwShortCutItemContext ctx = ((OwShortCutView) getMasterView()).getItemView();
        if (ctx == null || ctx.getContext() == null)
        {
            ctx = new OwSimpleShortCutContext(getContext());
        }
        return ctx;
    }

    /**
     *<p>
     * Simple implementation of OwShortCutItemContext interface.
     * Can provide only AppContext instance, unable to provide manager.
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
     *@since 4.2.0.0
     */
    protected static class OwSimpleShortCutContext implements OwShortCutItemContext
    {
        private OwAppContext context;

        public OwSimpleShortCutContext(OwAppContext ctx)
        {
            this.context = ctx;
        }

        @Override
        public OwAppContext getContext()
        {
            return context;
        }

        @Override
        public OwMimeManager getMimeManager()
        {
            return null;
        }
    }
}