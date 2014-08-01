package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * This will be used during a POST request to the "/processes" service.
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
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartProcessBody
{
    @JsonProperty
    private String processDefinitionId;
    @JsonProperty
    private String processDefinitionKey;
    @JsonProperty
    private String businessKey;
    @JsonProperty
    private Map<String, Object> variables = new HashMap<String, Object>();
    @JsonProperty
    private List<String> items = new ArrayList<String>();

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public void setProcessDefinitionKey(String processDefinitionKey)
    {
        this.processDefinitionKey = processDefinitionKey;
    }

    public void addItem(String itemId)
    {
        this.items.add(itemId);
    }

    public void putVariable(String name, Object value)
    {
        this.variables.put(name, value);
    }

    public void setVariables(Map<String, Object> variables)
    {
        this.variables = variables;
    }
}
