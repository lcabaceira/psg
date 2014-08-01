package org.alfresco.wd.plug.rs.restlet;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 *<p>
 *Used to generate JSON Request looks like:
 *<code><pre>
 *{
 *     "actionedUponNode":"nodeRef",
 *     "actionDefinitionName":"action-definition",
 *     "parameterValues": {
 *      ...
 *      }
 *}
 *</pre></code>
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
 *@since 4.2.0.0
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class QueuedAction<P extends Object>
{
    @JsonSerialize(include = Inclusion.NON_NULL)
    private P parameterValues;

    private String actionedUponNode;
    private String actionDefinitionName;

    @JsonSerialize(include = Inclusion.NON_NULL)
    public P getParameterValues()
    {
        return parameterValues;
    }

    public void setParameterValues(P parameterValues)
    {
        this.parameterValues = parameterValues;
    }

    public String getActionedUponNode()
    {
        return actionedUponNode;
    }

    public void setActionedUponNode(String actionedUponNode)
    {
        this.actionedUponNode = actionedUponNode;
    }

    public String getActionDefinitionName()
    {
        return actionDefinitionName;
    }

    public void setActionDefinitionName(String actionDefinitionName)
    {
        this.actionDefinitionName = actionDefinitionName;
    }

}
