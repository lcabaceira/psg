package com.wewebu.ow.server.field.filter;

import com.wewebu.ow.server.field.OwSearchCriteria;

/**
 *<p>
 * Compare Interface for filtering.
 * Simple filter action where a {@link OwSearchCriteria} is representing
 * the filter specification.
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
 *@since 3.2.0.0
 */
public interface OwComparator<T>
{
    /**
     * Matching check, where the filter specifies the
     * criteria value and operation to be used for filtering.
     * @param filter OwSearchCriteria
     * @param value T to verify against filter
     * @return true only if value is matching the filter, else false
     */
    boolean match(OwSearchCriteria filter, T value);
}
