package com.wewebu.ow.server.field;

import java.util.Collection;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Interface for objects that provide fields, like search templates or OwObjects.
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
public interface OwFieldProvider
{
    /** type of the fieldprovider is a object with metadata */
    public static final int TYPE_META_OBJECT = 0x0001;
    /** type of the fieldprovider is search object / template */
    public static final int TYPE_SEARCH = 0x0002;
    /** type of the fieldprovider is a create object dialog or view */
    public static final int TYPE_CREATE_OBJECT = 0x0010;
    /** type of the fieldprovider is a checkin object dialog or view */
    public static final int TYPE_CHECKIN_OBJECT = 0x0020;
    /** type of the fieldprovider is small, fields must not use a lot of space */
    public static final int TYPE_SMALL = 0x0040;
    /** type of the fieldprovider is a result list */
    public static final int TYPE_RESULT_LIST = 0x0080;
    /** type of the fieldprovider is a result list */
    public static final int TYPE_AJAX = 0x0100;

    /** get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public abstract OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException;

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public abstract void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException;

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public abstract Object getSafeFieldValue(String sName_p, Object defaultvalue_p);

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public abstract Collection getFields() throws Exception;

    /** get the type of field provider can be one or more of TYPE_... */
    public abstract int getFieldProviderType();

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer or null
     * */
    public abstract Object getFieldProviderSource();

    /** get a name that identifies the field provider, e.g. the name of the underlying JSP page
     * 
     * @return String unique ID / Name of fieldprovider, or null
     */
    public abstract String getFieldProviderName();
}