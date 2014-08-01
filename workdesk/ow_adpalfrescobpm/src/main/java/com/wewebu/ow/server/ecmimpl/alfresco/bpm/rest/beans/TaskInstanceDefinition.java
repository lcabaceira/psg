package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * TaskInstanceDefinition.
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
 *@since 4.0.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskInstanceDefinition
{
    @JsonProperty
    private String id;
    @JsonProperty
    private String url;
    @JsonProperty
    private Type type;

    /**
     * @return the id
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return the type
     */
    public Type getType()
    {
        return this.type;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return this.url;
    }
}
