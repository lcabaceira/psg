package com.wewebu.ow.server.field;

/**
 *<p>
 * Base interface for fields, like object properties or search criteria.
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
public interface OwField
{
    /** get the value of the field. Can also be a list of values
     * @return Object value of field if field is scalar, or a java.io.List of objects if field is an array
     */
    public abstract Object getValue() throws Exception;

    /** set the value of the field. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of field if field is scalar, or a java.io.List of objects if field is an array
     */
    public abstract void setValue(Object oValue_p) throws Exception;

    /** get the corresponding field definition of the field
     * @return OwFieldDefinition
     */
    public abstract OwFieldDefinition getFieldDefinition() throws Exception;

}