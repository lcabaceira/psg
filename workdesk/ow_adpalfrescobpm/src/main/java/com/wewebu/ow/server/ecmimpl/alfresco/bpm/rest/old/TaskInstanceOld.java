package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 *<p>
 * Old bean for task instance.
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
 *@since 4.2.0
 *@deprecated This is for temporary internal use only and will be removed as soon as all the information exposed by the old API will be available through the new <strong>Alfresco Workflow Public Rest API</strong>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_EMPTY)
@Deprecated
public class TaskInstanceOld
{
    @JsonProperty
    private String id;

    @JsonProperty
    private boolean isPooled;

    @JsonProperty
    private boolean isEditable;

    @JsonProperty
    private boolean isReassignable;

    @JsonProperty
    private boolean isClaimable;

    @JsonProperty
    private boolean isReleasable;

    public String getId()
    {
        return id;
    }

    public boolean isPooled()
    {
        return isPooled;
    }

    public boolean isEditable()
    {
        return isEditable;
    }

    public boolean isReassignable()
    {
        return isReassignable;
    }

    public boolean isClaimable()
    {
        return isClaimable;
    }

    public boolean isReleasable()
    {
        return isReleasable;
    }
}