package com.wewebu.ow.server.app;

/**
 *<p>
 * Interface for combo box model.
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
public interface OwComboModel
{
    /** empty value */
    public static final String EMPTY_VALUE = "";

    /**
     * Get all items.
     * @return - an array of {@link OwComboItem} objects. 
     */
    public OwComboItem[] getAllItems();

    /**
     * Return the selected {@link OwComboItem} object. Can be null.
     */
    public OwComboItem getSelectedItem();

    /**
     * Return the {@link OwComboItem} object at the specified position. Can be null.
     * @param position_p - the position in the list
     */
    public OwComboItem getItemAt(int position_p);

    /**
     * Set the given item as selected.
     * @param item_p - the {@link OwComboItem}
     */
    public void setSelectedItem(OwComboItem item_p);

    /**
     * Get the number of items in this model.
     * @return the number of items.
     */
    public int getSize();

    /**
     * Check if the given item is the selected one.
     * @param item_p - the given {@link OwComboItem} object 
     * @return - <code>true</code> if the given item is selected.
     */
    public boolean isSelectedItem(OwComboItem item_p);

    /**
     * Specify if the model has an item that is out of range. 
     * E.g: if a choice list has 3 values in the ECM, and one of the values is stored as a 
     * property for an object, and than the stored value is removed from choice list, 
     * that value is no longer in the choice range. 
     * @return <code>true</code> if this model has an item out of range.
     */
    public boolean hasItemOutOfRange();

}