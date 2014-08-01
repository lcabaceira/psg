package com.wewebu.ow.server.collections;

import java.util.Collection;

import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Interface about the context to load data/objects. 
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
public interface OwLoadContextInfo
{

    /**
     * Get an array of object types,
     * which should be retrieved.
     * @return array of OwObjectReference.OBJECT_TYPE_ contants.
     */
    int[] getObjectTypes();

    /**
     * List of property names/Id's which should be retrieved. 
     * @return List of property names/ids (String's)
     */
    Collection<String> getPropertyNames();

    /**
     * Sorting definition
     * @return OwSort or null;
     */
    OwSort getSorting();

    /**
     * Maximum retrieved elements
     * @return long if known, -1 if unknown
     */
    long getMaxSize();

    /**
     * Get a definition of a filter, which can be used to restrict result. 
     * @return OwSearchNode or null
     */
    OwSearchNode getFilter();

    /**
     * Definition of specific version restriction for data to retrieve.
     * @return int representing OwSearchTemplate.VERSION_SELECT_... constant
     */
    int getVersionSelection();

    /**
     * Skip count of results
     * @return long
     */
    long getSkipCount();

    /**
     * Get the size for pages (=Items per page), which are retrieved. 
     * @return integer size of page
     */
    int getPageSize();
}
