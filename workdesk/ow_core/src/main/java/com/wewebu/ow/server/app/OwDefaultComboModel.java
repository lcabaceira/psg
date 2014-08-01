package com.wewebu.ow.server.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * Default implementation for combo box model.
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
public class OwDefaultComboModel implements OwComboModel
{
    /** default display value*/
    private static final String DEFAULT_EMPTY_VALUE_DISPLAY_VALUE = "-";
    /** the items*/
    private List m_items;
    /** the selected item*/
    private OwComboItem m_selectedItem;
    /** flag that specify if empty item is allowed*/
    private boolean m_allowEmptyItem;
    /** flag that specify if items that are not item list are allowed*/
    private boolean m_allowNotInListElement;

    /** empty display value */
    private String m_emptyDisplayValue = DEFAULT_EMPTY_VALUE_DISPLAY_VALUE;

    /**
     * Constructor
     * @param value_p - the selected value. 
     * @param values_p - the array of available values. 
     * @param displayNames_p - the array of available display values.
     */
    public OwDefaultComboModel(String value_p, String[] values_p, String[] displayNames_p)
    {
        this(false, false, value_p, values_p, displayNames_p);
    }

    /**
     * Constructor
     * @param allowEmptyItem_p - flag that specify if empty item is allowed.
     * @param allowNotInList_p - flag that specify if items that are not item list are allowed.
     * @param value_p - the selected value. 
     * @param values_p - the array of available values. 
     * @param displayNames_p - the array of available display values.
     * @param emptyDisplayValue_p - the display value for empty item.
     */
    public OwDefaultComboModel(boolean allowEmptyItem_p, boolean allowNotInList_p, String value_p, String[] values_p, String[] displayNames_p, String emptyDisplayValue_p)
    {
        m_emptyDisplayValue = emptyDisplayValue_p;
        m_allowEmptyItem = allowEmptyItem_p;
        m_allowNotInListElement = allowNotInList_p;
        if (values_p == null || displayNames_p == null || values_p.length != displayNames_p.length)
        {
            throw new IllegalArgumentException();
        }
        List items = new LinkedList();
        for (int i = 0; i < values_p.length; i++)
        {
            items.add(new OwDefaultComboItem(values_p[i], displayNames_p[i]));
        }
        initialize(value_p, items);

    }

    /**
     * Constructor
     * @param allowEmptyItem_p - flag that specify if empty item is allowed.
     * @param allowNotInList_p - flag that specify if items that are not item list are allowed.
     * @param value_p - the selected value. 
     * @param values_p - the array of available values. 
     * @param displayNames_p - the array of available display values.
     */
    public OwDefaultComboModel(boolean allowEmptyItem_p, boolean allowNotInList_p, String value_p, String[] values_p, String[] displayNames_p)
    {
        m_allowEmptyItem = allowEmptyItem_p;
        m_allowNotInListElement = allowNotInList_p;
        if (values_p == null || displayNames_p == null || values_p.length != displayNames_p.length)
        {
            throw new IllegalArgumentException();
        }
        List items = new LinkedList();
        for (int i = 0; i < values_p.length; i++)
        {
            items.add(new OwDefaultComboItem(values_p[i], displayNames_p[i]));
        }
        initialize(value_p, items);
    }

    /**
     * Constructor
     * @param allowEmptyItem_p - flag that specify if empty item is allowed.
     * @param allowNotInList_p - flag that specify if items that are not item list are allowed.
     * @param value_p - the selected value. 
     * @param items_p - the list with {@link OwComboItem} objects.
     * @param emptyDisplayValue_p - the display value for empty item. 
     */
    public OwDefaultComboModel(boolean allowEmptyItem_p, boolean allowNotInList_p, String value_p, List items_p, String emptyDisplayValue_p)
    {
        m_allowEmptyItem = allowEmptyItem_p;
        m_allowNotInListElement = allowNotInList_p;
        m_emptyDisplayValue = emptyDisplayValue_p;
        initialize(value_p, items_p);
    }

    /**
     * Constructor
     * @param allowEmptyItem_p - flag that specify if empty item is allowed.
     * @param allowNotInList_p - flag that specify if items that are not item list are allowed.
     * @param value_p - the selected value. 
     * @param items_p - the list with {@link OwComboItem} objects. 
     */
    public OwDefaultComboModel(boolean allowEmptyItem_p, boolean allowNotInList_p, String value_p, List items_p)
    {
        m_allowEmptyItem = allowEmptyItem_p;
        m_allowNotInListElement = allowNotInList_p;
        initialize(value_p, items_p);
    }

    /**
     * Constructor
     * @param value_p - the selected value. 
     * @param items_p - the list with {@link OwComboItem} objects. 
     */
    public OwDefaultComboModel(String value_p, List items_p)
    {
        m_allowEmptyItem = true;
        m_allowNotInListElement = true;
        initialize(value_p, items_p);
    }

    /**
     * Utility method, used to initialize the model.
     * @param value_p - the selected value. 
     * @param items_p - the list with {@link OwComboItem} objects. 
     */
    private void initialize(String value_p, List items_p)
    {
        if (items_p == null)
        {
            throw new IllegalArgumentException();
        }
        m_items = new LinkedList();
        if (m_allowEmptyItem)
        {
            OwDefaultComboItem owDefaultComboItem = new OwDefaultComboItem(EMPTY_VALUE, m_emptyDisplayValue);
            if (value_p == null || value_p.compareTo(EMPTY_VALUE) == 0)
            {
                m_selectedItem = owDefaultComboItem;
            }
            m_items.add(owDefaultComboItem);
        }
        m_items.addAll(items_p);
        if (value_p != null && value_p.compareTo(EMPTY_VALUE) != 0)
        {
            for (Iterator iterator = items_p.iterator(); iterator.hasNext();)
            {
                OwComboItem item = (OwComboItem) iterator.next();
                if (value_p.equals(item.getValue()))
                {
                    m_selectedItem = item;
                }
            }
            if (m_selectedItem == null && m_allowNotInListElement)
            {
                OwDefaultComboItem owDefaultComboItem = new OwDefaultComboItem(value_p, value_p);
                m_items.add(owDefaultComboItem);
                m_selectedItem = owDefaultComboItem;
            }
        }

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#getAllItems()
     */
    public OwComboItem[] getAllItems()
    {
        return (OwComboItem[]) m_items.toArray(new OwComboItem[m_items.size()]);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#getItemAt(int)
     */
    public OwComboItem getItemAt(int position_p)
    {
        OwComboItem item = null;
        if (position_p < m_items.size() && position_p >= 0)
        {
            item = (OwComboItem) m_items.get(position_p);
        }
        return item;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#getSelectedItem()
     */
    public OwComboItem getSelectedItem()
    {
        return m_selectedItem;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#getSize()
     */
    public int getSize()
    {
        return m_items.size();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#hasItemOutOfRange()
     */
    public boolean hasItemOutOfRange()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#isSelectedItem(com.wewebu.ow.server.app.OwComboItem)
     */
    public boolean isSelectedItem(OwComboItem item_p)
    {
        return m_selectedItem != null && m_items.contains(item_p) && item_p.equals(m_selectedItem);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboModel#setSelectedItem(com.wewebu.ow.server.app.OwComboItem)
     */
    public void setSelectedItem(OwComboItem item_p)
    {
        this.m_selectedItem = item_p;
    }

}