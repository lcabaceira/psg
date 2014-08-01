package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * Pagination settings for a {@link BasicList}.
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
 *@since 4.2.0.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination
{
    @JsonProperty
    private long skipCount;
    @JsonProperty
    private long maxItems;
    @JsonProperty
    private long count;
    @JsonProperty
    private boolean hasMoreItems;
    @JsonProperty
    private long totalItems;

    /**
     * @return the skipCount
     */
    public long getSkipCount()
    {
        return skipCount;
    }

    /**
     * @return the maxItems
     */
    public long getMaxItems()
    {
        return maxItems;
    }

    /**
     * @return the count
     */
    public long getCount()
    {
        return count;
    }

    /**
     * @return the hasMoreItems
     */
    public boolean isHasMoreItems()
    {
        //This is due to a bug in the service implementation
        return (this.getSkipCount() + this.getCount()) < this.getTotalItems();
        //return hasMoreItems;
    }

    /**
     * @return the totalItems
     */
    public long getTotalItems()
    {
        return totalItems;
    }
}
