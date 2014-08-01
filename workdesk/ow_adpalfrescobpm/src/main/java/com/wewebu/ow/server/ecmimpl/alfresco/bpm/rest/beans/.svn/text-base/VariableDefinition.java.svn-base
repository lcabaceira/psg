package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import java.util.List;

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
public class VariableDefinition
{
    @JsonProperty
    private String dataType;
    @JsonProperty
    private String title;
    @JsonProperty
    private String qualifiedName;
    @JsonProperty
    private String name;
    @JsonProperty
    private boolean required;
    @JsonProperty
    private String defaultValue;
    @JsonProperty
    private List<String> allowedValues;

    public String getDataType()
    {
        return dataType;
    }

    public String getTitle()
    {
        return title;
    }

    public String getQualifiedName()
    {
        return qualifiedName;
    }

    public String getName()
    {
        return name;
    }

    public boolean isRequired()
    {
        return required;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * List of values the variable can have,
     * indirect definition of ChoiceList,
     * @return List of Strings (can be null);
     */
    public List<String> getAllowedValues()
    {
        return this.allowedValues;
    }
}
