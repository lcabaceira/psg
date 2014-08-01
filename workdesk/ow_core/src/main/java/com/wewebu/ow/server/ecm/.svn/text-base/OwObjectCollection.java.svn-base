package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwAttributeBag;

/**
 *<p>
 * Base interface for object class descriptions.
 * Interface for objects lists from the network. Implements the SUN value list pattern.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwObjectCollection extends java.util.List, OwAttributeBag
{
    /** predefined attribute for attribute bag in object list */
    public static final String ATTRIBUTE_SIZE = "ow_size";

    /** predefined attribute for attribute bag in object list */
    public static final String ATTRIBUTE_IS_COMPLETE = "ow_complete";

    /** predefined attribute for attribute bag in object list 
        
        returns the SQL statement used to generate the list
        !!! FOR DEBUGGING ONLY !!! 
     */
    public static final String ATTRIBUTE_SQL = "ow_sql";

    /** check if object list has retrieved all objects
     *  If false, there are more objects available, but it is not guaranteed that you can retrieve them with the next / prev functions.
     *  I.e. hasNext / Prev might still return false. 
     *
     * @return boolean true = all available objects have been added to the list 
     */
    public abstract boolean isComplete() throws Exception;

    /** check if object list has access to more next objects, than currently added.
     *  I.e. if it could do another SQL Query and obtain more objects from the ECM System.
     *
     * @return boolean true = there are more objects available, call getNext() to retrieve additional objects.
     */
    public abstract boolean hasNext() throws Exception;

    /**  check if object list has access to more previous objects, than currently added.
     *  I.e. if it could do another SQL Query and obtain more objects from the ECM System.
     *
     * @return boolean true = there are more objects available, call getPrev() to retrieve additional objects.
     */
    public abstract boolean hasPrev() throws Exception;

    /** retrieve further objects, than currently added.
     *  I.e. submit another SQL Query and obtain more objects from the ECM System.
     *
     *  NOTE: The retrieved objects will replace the current objects in the list
     *
     */
    public abstract void getNext() throws Exception;

    /** retrieve further objects, than currently added.
     *  I.e. submit another SQL Query and obtain more objects from the ECM System.
     *
     *  NOTE: The retrieved objects will replace the current objects in the list
     */
    public abstract void getPrev() throws Exception;

    /** sort the list by the given criteria.
     *  The default implementation sorts on the cached objects in the application server.
     *  The function may be overloaded to sort by the ECM System with SQL sort statements.
     *
     * @param sortCriteria_p list of sort criteria
     */
    public abstract void sort(OwSort sortCriteria_p) throws Exception;
}