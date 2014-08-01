package com.wewebu.ow.server.app;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Combo Box Model used when {@link OwFieldManager} needs to display combobox fields.
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
public class OwFieldManagerComboModel implements OwComboModel
{
    //the empty value display is ALT+255 char - because of extjs space height calculation problem.
    static final String EMPTY_VALUE_DISPLAY_VALUE = "\u00A0";
    /** the items*/
    private List m_items = new LinkedList();
    /** the selected item*/
    private OwComboItem m_selectedItem;
    /** flag indicating that there are items out of range*/
    private boolean m_hasItemOutOfRange;

    /**
     * Constructor
     * @param locale_p - the current {@link Locale} object. 
     * @param allowEmptyField_p - flag specifying that empty value is allowed.
     * @param selectedValue_p - the selected value.
     * @param displayNames_p - the display names array. 
     * @param values_p - the values array.
     */
    public OwFieldManagerComboModel(Locale locale_p, boolean allowEmptyField_p, String selectedValue_p, String[] displayNames_p, String[] values_p)
    {
        if (values_p == null || displayNames_p == null || values_p.length != displayNames_p.length)
        {
            throw new IllegalArgumentException();
        }
        if (allowEmptyField_p)
        {
            OwDefaultComboItem emptyValue = new OwDefaultComboItem(EMPTY_VALUE, EMPTY_VALUE_DISPLAY_VALUE);
            m_items.add(emptyValue);
            if (selectedValue_p == null || selectedValue_p.equals(EMPTY_VALUE))
            {
                m_selectedItem = emptyValue;
            }
        }
        for (int i = 0; i < values_p.length; i++)
        {
            OwDefaultComboItem owDefaultComboItem = new OwDefaultComboItem(values_p[i], displayNames_p[i]);
            m_items.add(owDefaultComboItem);
            if (selectedValue_p != null && values_p[i] != null && selectedValue_p.compareTo(values_p[i]) == 0)
            {
                m_selectedItem = owDefaultComboItem;
            }
        }
        // Render/add empty value to choicelist only if: the value is (null or "") and allowEmptyField -- OR -- the value is not (null or "")
        //        if (((value_p == null || value_p.equals("")) && fAllowEmptyField_p) || (value_p != null && !value_p.equals(""))) //Bug 2487

        if (m_selectedItem == null)
        {
            if (selectedValue_p != null && !selectedValue_p.equals(EMPTY_VALUE))
            {
                //the value was deleted from list, but still it is in the ecm
                OwDefaultComboItem outOfRangeItem = new OwDefaultComboItem(selectedValue_p, OwString.localize1(locale_p, "app.OwStandardFieldManager.outofrangelist.symbol", "(%1)", selectedValue_p));
                m_items.add(outOfRangeItem);
                m_selectedItem = outOfRangeItem;
                m_hasItemOutOfRange = true;
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
        m_selectedItem = item_p;
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
     * @see com.wewebu.ow.server.app.OwComboModel#hasItemOutOfRange()
     */
    public boolean hasItemOutOfRange()
    {
        return m_hasItemOutOfRange;
    }

}