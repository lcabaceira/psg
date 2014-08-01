package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 *<p>
 * An instance of a variable as returned from the REST API.
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
public class Variable
{
    public static final String SCOPE_LOCAL = "local";
    public static final String SCOPE_GLOBAL = "global";

    @JsonProperty
    private String scope;//": "global",
    @JsonProperty
    private String name;//": "bpm_priority",
    @JsonProperty
    private Object value;//": 1,
    @JsonProperty
    private String type;//": "d_int"

    public String getScope()
    {
        return scope;
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

    public String getType()
    {
        return type;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getScope());
        sb.append("/");
        sb.append(this.getName());
        sb.append(" - ");
        sb.append(this.getValue());
        return sb.toString();
    }
}
