package com.wewebu.ow.server.ui.viewer;

/**
 *<p>
 * Enumeration of Results.
 * This class represent the possible results
 * for annotation info requests.
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
 *@since 3.2.0.0
 */
public enum OwAnnotResultsEnum
{
    /**Value to allow a specified request*/
    VALUE_ALLOW("allow"),
    /**Value to deny a specified request*/
    VALUE_DENY("deny"),
    /**Value to represent a default setting*/
    DEFAULT_ID("default");

    private String value;

    private OwAnnotResultsEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
