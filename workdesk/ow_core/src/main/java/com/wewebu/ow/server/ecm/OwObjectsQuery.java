package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Implementors of this interface are objects query implementations.
 * An object query implementation holds all necessary information to retrieve at any time
 * a collection of objects upon its execution (see {@link #execute(OwSort)}.
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
 *@since 2.5.3.1
 */
public interface OwObjectsQuery
{
    /**
     * Executes this query 
     * @param sort_p the sort information to be applied when retrieving the object list
     * @return an {@link OwObjectCollection} 
     * @throws OwException if the query execution fails
     */
    OwObjectCollection execute(OwSort sort_p) throws OwException;
}
