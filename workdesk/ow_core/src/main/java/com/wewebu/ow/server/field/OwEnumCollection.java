package com.wewebu.ow.server.field;

/**
 *<p>
 * Collection of OwEnum elements.
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
public interface OwEnumCollection extends java.util.Collection
{
    /** retrieve a display name for the given object
     */
    public abstract String getDisplayName(java.util.Locale local_p, Object object_p);

}