package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * Type.
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
public class Type
{
    @JsonProperty
    private String name;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private String url;

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }
}
