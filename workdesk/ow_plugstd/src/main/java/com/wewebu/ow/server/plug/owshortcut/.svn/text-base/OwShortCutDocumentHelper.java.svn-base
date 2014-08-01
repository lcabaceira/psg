package com.wewebu.ow.server.plug.owshortcut;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.plug.std.log.OwLog;

/**
 *<p>
 * Helper class for Short Cuts Document.<br/>
 * Stores and manage the OwShortCutItem list.
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
public class OwShortCutDocumentHelper
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutDocumentHelper.class);

    /** Map <Key=shortcut ID , Value=OwShortCutItem> */
    private Map<String, OwShortCutItem> m_shortcutsMap = null;

    public OwShortCutDocumentHelper()
    {
        this.m_shortcutsMap = new HashMap<String, OwShortCutItem>();
    }

    /**
     * Add a collection of shortcuts to shortcuts helper pool
     * @param oShortCuts_p Collection of type OwShortCutItem
     * @return boolean indicates if the list was changed
     */
    public boolean addShortCut(Collection oShortCuts_p)
    {
        boolean addedFlag = false;
        if (oShortCuts_p != null)
        {
            for (Iterator iterator = oShortCuts_p.iterator(); iterator.hasNext();)
            {
                OwShortCutItem object = (OwShortCutItem) iterator.next();
                if (addShortCut(object))
                {
                    addedFlag = true;
                }
            }
        }
        return addedFlag;
    }

    /**
     * Add a shortcut object to shortcuts helper pool
     * @param oObject_p OwShortCutItem
     * @return boolean indicates if the list was changed
     */
    public boolean addShortCut(OwShortCutItem oObject_p)
    {
        boolean addedFlag = false;
        try
        {
            String id = oObject_p.getId();
            if (!this.m_shortcutsMap.containsKey(id))
            {
                this.m_shortcutsMap.put(id, oObject_p);
                addedFlag = true;
            }
        }
        catch (Exception e)
        {
            LOG.warn("OwShortCutDocumentHelper.addShortCut(): Exception", e);
        }
        return addedFlag;
    }

    /**
     * Delete a collection of shortcuts from shortcuts helper pool 
     * @param oShortCuts_p Collection of type OwShortCutItem
     * @return boolean indicates if the list was changed
     */
    public boolean deleteShortCut(Collection oShortCuts_p)
    {
        boolean deletedFlag = false;
        if (oShortCuts_p != null)
        {
            for (Iterator iterator = oShortCuts_p.iterator(); iterator.hasNext();)
            {
                OwShortCutItem object = (OwShortCutItem) iterator.next();
                if (deleteShortCut(object))
                {
                    deletedFlag = true;
                }
            }
        }
        return deletedFlag;
    }

    /**
     * Delete shortcut document from shortcuts helper pool by shortcut ID 
     * @param shortcutId_p unique shortcut ID 
     * @return boolean indicates if the list was changed
     */
    public boolean deleteShortCut(String shortcutId_p)
    {
        boolean deletedFlag = false;
        if (shortcutId_p != null && !shortcutId_p.equals(""))
        {
            if (this.m_shortcutsMap.containsKey(shortcutId_p))
            {
                this.m_shortcutsMap.remove(shortcutId_p);
                deletedFlag = true;
            }
        }
        return deletedFlag;
    }

    /**
     * Delete a shortcut object from shortcuts helper pool
     * @param oObject_p OwShortCutItem
     * @return boolean indicates if the list was changed
     */
    public boolean deleteShortCut(OwShortCutItem oObject_p)
    {
        boolean deletedFlag = false;
        try
        {
            deletedFlag = deleteShortCut(oObject_p.getId());
        }
        catch (Exception e)
        {
            LOG.warn("OwShortCutDocumentHelper.deleteShortCut(): Exception", e);
        }
        return deletedFlag;
    }

    /**
     * Clear the shortcut list
     */
    public void cleanShortCut()
    {
        this.m_shortcutsMap.clear();
    }

    /**
     * Get Shortcuts from shortcuts helper pool
     * @return List with OwShortCutItem
     */
    public List getShortcuts()
    {
        return getShortcuts(new ShortCutComparator());
    }

    /**
     * Get Shortcuts sorted shortcuts helper pool
     * @param comparator_p Own Comparator
     * @return a {@link List} of shortcuts
     */
    public List getShortcuts(Comparator comparator_p)
    {
        Set keys = this.m_shortcutsMap.keySet();
        int count = 0;

        OwShortCutItem[] cutItemOwObjects = new OwShortCutItem[keys.size()];
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            String id = (String) iterator.next();
            cutItemOwObjects[count] = this.m_shortcutsMap.get(id);
            count++;
        }
        Arrays.sort(cutItemOwObjects, comparator_p);
        return Arrays.asList(cutItemOwObjects);
    }

    /**
     * Set a shortcuts list to shortcuts helper pool
     * @param shortcuts_p List of OwShortCutItem
     */
    public void setShortcuts(List shortcuts_p)
    {
        this.m_shortcutsMap = new HashMap();
        if (this.m_shortcutsMap != null)
        {
            for (int i = 0; i < shortcuts_p.size(); i++)
            {
                OwShortCutItem owObject = (OwShortCutItem) shortcuts_p.get(i);
                String keyId;
                try
                {
                    keyId = owObject.getId();
                    this.m_shortcutsMap.put(keyId, owObject);
                }
                catch (Exception e)
                {
                    LOG.warn("OwShortCutDocumentHelper.setShortcuts(): Exception", e);
                }
            }
        }
    }

    /** 
     * Sort for ShortCut List 
     *
     */
    private class ShortCutComparator implements Comparator
    {
        public int compare(Object o1_p, Object o2_p)
        {
            try
            {
                String name1 = ((OwShortCutItem) o1_p).getName();
                String name2 = ((OwShortCutItem) o2_p).getName();
                if (name1 == null && name2 == null)
                {
                    return 0;
                }
                if (name1 == null)
                {
                    return -1;
                }
                if (name2 == null)
                {
                    return 1;
                }
                name1 = name1.toUpperCase();
                name2 = name2.toUpperCase();
                return name1.compareTo(name2);
            }
            catch (Exception e)
            {
                LOG.error("Could not compare shrotcuts! Is this a deleted object reference ? ", e);
                return -1;
            }
        }
    }

    /**
     *  to String implementation, the List ist sorted
     */
    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        List shortcuts = getShortcuts();
        stringBuffer.append("Shortcut Documents:");
        for (int i = 0; i < shortcuts.size(); i++)
        {
            OwShortCutItem owObject = (OwShortCutItem) shortcuts.get(i);
            stringBuffer.append("\n" + i + " : " + owObject.getName());
        }
        return stringBuffer.toString();
    }

    /**
     * Refresh the shortcut list with latest version of the object
     * @since 3.1.0.3
     */
    public void refresh(OwRepository repository_p) throws Exception
    {
        Map<String, OwShortCutItem> temporaryShortcutsMap = new HashMap<String, OwShortCutItem>();
        Collection<OwShortCutItem> values = m_shortcutsMap.values();
        for (OwShortCutItem owShortCutItem : values)
        {
            owShortCutItem.refresh(repository_p);
            temporaryShortcutsMap.put(owShortCutItem.getId(), owShortCutItem);
        }
        m_shortcutsMap = temporaryShortcutsMap;
    }

    /**
     * Get the id used to persist the {@link OwShortCutItem} object
     * @param id_p - the current id of the {@link OwShortCutItem} object
     * @return the persistent id.
     * @since 3.1.0.3
     */
    public String getPersistentId(String id_p)
    {
        String result = id_p;
        if (m_shortcutsMap.containsKey(id_p))
        {

            try
            {
                result = m_shortcutsMap.get(id_p).getPersistentId();
            }
            catch (Exception e)
            {
                LOG.debug("Cannot find the persistent id, continue with current id.", e);
            }
        }
        return result;
    }
}