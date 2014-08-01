package com.wewebu.ow.server.field;

/**
 *<p>
 * Pair of enum value and display name strings for each enum entry.
 * Used in getEnums of OwFieldDefinition.
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
public interface OwEnum
{
    /** get the enum value
     */
    public abstract Object getValue();

    /** get a display name
     */
    public abstract String getDisplayName(java.util.Locale local_p);

    /** check if enum has a contained child collection */
    public abstract boolean hasChildEnums();

    /** get the contained child collection
     * 
     * @return OwEnumCollection or null if item has no children
     */
    public abstract OwEnumCollection getChilds();
}