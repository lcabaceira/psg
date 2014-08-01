package com.wewebu.ow.server.field;

/**
 *<p>
 * Base class for priority rules implementation.<br/>
 * Rules Engine for Highlighting in Hit List.
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
 */
public abstract class OwPriorityRuleBase implements OwPriorityRule
{
    protected static final String ATTRIBUTE_NAME_CONTAINER = "container";
    protected static final String ATTRIBUTE_NAME_RESOURCE = "resource";
    protected static final String NODE_NAME_COLOR = "styleclass";

    protected String m_scontainer;
    protected String m_sresource;
    protected String m_sStyleClass;

    public OwPriorityRuleBase()
    {
        this("", "", "");
    }

    /**
     * Constructor
     * @param scontainer_p the container ID the rule applies to
     * @param sresource_p the resource ID the rule applies to
     * @param styleClass_p
     */
    public OwPriorityRuleBase(String scontainer_p, String sresource_p, String styleClass_p)
    {
        super();
        this.m_scontainer = scontainer_p;
        this.m_sresource = sresource_p;
        this.m_sStyleClass = styleClass_p;
    }

    /** Gets the container ID the rule applies to.
     * 
     * @return String
     */
    public String getContainer()
    {
        return m_scontainer;
    }

    /** Gets the style class the item should be highlighted if rule applies.
     * 
     * @return String style class string e.g. myStyle
     */
    public String getStylClass()
    {
        return m_sStyleClass;
    }

}