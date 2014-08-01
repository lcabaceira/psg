package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans;

/**
 *<p>
 * Status Enum.
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
public enum Status
{
    ANY("any"), ACTIVE("active"), COMPLETED("completed");

    private String id;

    private Status(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return this.id;
    }
}
