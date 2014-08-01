package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * A ProcessDefinition JSON instance.
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
public class ProcessDefinition
{
    @JsonProperty
    private String id;//": "financialReport:1",
    @JsonProperty
    private String key;//": "financialReport",
    @JsonProperty
    private Integer version;//": 1,
    @JsonProperty
    private String name;//": "April financial report",
    @JsonProperty
    private String category;//": "com.acme.financial",
    @JsonProperty
    private String deploymentId;//": "123",
    @JsonProperty
    private String startFormResourceKey;//": "wf:adhocTask",
    @JsonProperty
    private boolean graphicNotationDefined;//": true

    public String getId()
    {
        return id;
    }

    public String getKey()
    {
        return key;
    }

    public Integer getVersion()
    {
        return version;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public String getStartFormResourceKey()
    {
        return startFormResourceKey;
    }

    public boolean isGraphicNotationDefined()
    {
        return graphicNotationDefined;
    }

    @Override
    public String toString()
    {
        return String.format("Id: %s Name: %s Category: %s", this.id, this.name, this.category);
    }
}
