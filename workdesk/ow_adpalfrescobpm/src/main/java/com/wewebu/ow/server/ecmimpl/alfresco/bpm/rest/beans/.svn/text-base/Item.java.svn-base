package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 *<p>
 * Item (attachement) details.
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
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Item
{
    @JsonProperty
    private String id;//": "42eef795-857d-40cc-9fb5-5ca9fe6a0592",
    @JsonProperty
    private String name;//" : "FinancialResults.pdf"
    @JsonProperty
    private String title;//" : "FinancialResults"
    @JsonProperty
    private String description;//" : "the description"
    @JsonProperty
    private String mimeType;//" : "application/pdf"
    @JsonProperty
    private String createdBy;//" : "johndoe"
    @JsonProperty
    private String createdAt;//" : "2010-10-13T14:53:25.950+02:00"
    @JsonProperty
    private String modifiedBy;//" : "johndoe"
    @JsonProperty
    private String modifiedAt;//" : "2010-10-13T14:53:25.950+02:00"
    @JsonProperty
    private long size;//" : 28973

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public String getModifiedAt()
    {
        return modifiedAt;
    }

    public long getSize()
    {
        return size;
    }
}
