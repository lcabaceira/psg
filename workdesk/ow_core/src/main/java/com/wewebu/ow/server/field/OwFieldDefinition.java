package com.wewebu.ow.server.field;

import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.ecm.OwPropertyClass;

/**
 *<p>
 * Base interface for property class and Search Fields. 
 * Used in the PropertyFieldManager to display and edit fields.
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
public interface OwFieldDefinition
{
    /** get the name of the class
     * @return class name
     */
    public abstract String getClassName();

    /** get the displayable name of the type as defined by the DMS System
     * can be identical to getClassName
     *
     * @param locale_p Locale to use
     * @return type displayable name of property
     */
    public abstract String getDisplayName(java.util.Locale locale_p);

    /** get the description defined by the DMS System
     *
     * @param locale_p Locale to use
     * @return type description of property
     */
    public abstract String getDescription(java.util.Locale locale_p);

    /** get the java class name of java object associated with this property
     * @return java class name
     */
    public abstract String getJavaClassName();

    /** get the native type which is defined by the underlying system
     *
     *  WARNING:    The returned object is opaque. 
     *              Using this object makes the client dependent on the underlying system
     *
     * @return Object native Type Object
     */
    public abstract Object getNativeType() throws Exception;

    /** check if property is a enum type  (see getEnums)
     * @return true if property is a enum type
     */
    public abstract boolean isEnum() throws Exception;

    /** get a list of enum objects for the enum type (see isEnum)
     * @return OwEnumCollection of OwEnum objects, which can be used in a select box.
     */
    public abstract com.wewebu.ow.server.field.OwEnumCollection getEnums() throws Exception;

    /** check if property is required, i.e. must be set by the user
     * @return true if property is required
     */
    public abstract boolean isRequired() throws Exception;

    /** get the max allowed value, or Integer len for String or null if not defined
     */
    public abstract Object getMaxValue() throws Exception;

    /** get the min allowed value, or Integer len for String or null if not defined
     */
    public abstract Object getMinValue() throws Exception;

    /** get the default value
     */
    public abstract Object getDefaultValue() throws Exception;

    /** check if property contains a list of values
     * @return true if property is array, false if property is scalar
     */
    public abstract boolean isArray() throws Exception;

    /** create a value for the field described by this class with the given XML Node serialization
     *
     * @param node_p the serialized value as a XML DOM Node
     * @return Object the value of the field 
     */
    public abstract Object getValueFromNode(org.w3c.dom.Node node_p) throws Exception;

    /** create a value for the field described by this class with the given String serialization
    *
    * @param text_p String the serialized value
    * @return Object the value of the field 
    */
    public abstract Object getValueFromString(String text_p) throws Exception;

    /** create a XML serialization of the given field value
     *
     * @param value_p Object with field value
     * @param doc_p DOM Document to add to
     *
     * @return DOM Node
     */
    public abstract org.w3c.dom.Node getNodeFromValue(Object value_p, org.w3c.dom.Document doc_p) throws Exception;

    /** get the formatter object for string representation 
     * 
     * @return OwFormat, can be null to use the default format
     */
    public abstract OwFormat getFormat();

    /** get a collection of possible filter / search operators for the field
     * 
     * @return Collection of operators as defined with OwSearchOperator.CRIT_OP_..., or null if no operators are defined
     */
    public abstract Collection getOperators() throws Exception;

    /** get child properties classes of a complex property class
     * 
     * @see OwPropertyClass#isComplex()
     * 
     * @return Collection of OwFieldDefinition or null if isComplex is false
     * @throws Exception 
     */
    public abstract List getComplexChildClasses() throws Exception;

    /** check if the property is a component that contains another child properties
     * 
     * @see OwPropertyClass#getComplexChildClasses()
     * 
     * @return true if the property can contains child properties
     */
    public abstract boolean isComplex();

}