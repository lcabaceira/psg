package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * Implementation of a list bean in a response received from a call to one of the Alfresco Workflow Public REST API services.
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
 * @param <E> The type of the elements in the list.
 * @see BasicListResponse
 * @since 4.2.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicList<E>
{
    @JsonProperty
    private Pagination pagination;
    @JsonProperty
    private Entry<E>[] entries;

    public Pagination getPagination()
    {
        return pagination;
    }

    public Entry<E>[] getEntries()
    {
        return entries;
    }
}
