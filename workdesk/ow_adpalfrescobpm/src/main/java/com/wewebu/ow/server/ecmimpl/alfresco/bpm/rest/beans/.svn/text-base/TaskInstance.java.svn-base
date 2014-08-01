package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 *<p>
 * TaskInstance.
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
public class TaskInstance
{
    @JsonProperty
    private String id;//": "127",
    @JsonProperty
    private String processId;//": "123",
    @JsonProperty
    private String processDefinitionId;//": "financialReport:1",
    @JsonProperty
    private String activityDefinitionId;//": "review" // the activity id of the usertask
    @JsonProperty
    private String name;//": "Handle vacation request",
    @JsonProperty
    private String description;//": "Vacation request by Kermit",
    @JsonProperty
    private String dueAt;//": "2010-10-13T14:54:26.750+02:00",
    @JsonProperty
    private String startedAt;//": "2010-10-13T14:54:26.750+02:00",
    @JsonProperty
    private String endedAt;//": "2010-10-13T14:54:26.750+02:00",
    @JsonProperty
    private String durationInMs;//": 982374, // expressed in millis
    @JsonProperty
    private String priority;//": 50,
    @JsonProperty
    private String owner;//": "Kermit",
    @JsonProperty
    private String assignee;//": "johndoe",
    @JsonProperty
    private String formResourceKey;//": "wf:submitAdhocTask
    @JsonProperty
    String state;
    @JsonProperty
    Variable variables[];

    public String getId()
    {
        return id;
    }

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public String getActivityDefinitionId()
    {
        return activityDefinitionId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDueAt()
    {
        return dueAt;
    }

    public String getStartedAt()
    {
        return startedAt;
    }

    public String getEndedAt()
    {
        return endedAt;
    }

    public String getDurationInMs()
    {
        return durationInMs;
    }

    public String getPriority()
    {
        return priority;
    }

    public String getOwner()
    {
        return owner;
    }

    public String getAssignee()
    {
        return assignee;
    }

    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }

    public String getFormResourceKey()
    {
        return formResourceKey;
    }

    public void setFormResourceKey(String formResourceKey)
    {
        this.formResourceKey = formResourceKey;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public Variable[] getVariables()
    {
        return variables;
    }
}