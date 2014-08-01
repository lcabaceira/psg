package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Standard Implementation for object properties.<br/>
 * A property contains the name and value of a object property.
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
public class OwStandardProperty implements OwProperty, Cloneable
{
    /** value of the property */
    protected Object m_Value;
    /** class description of the property */
    protected OwPropertyClass m_PropertyClass;

    /** construct Property Object and set members */
    public OwStandardProperty(Object value_p, OwPropertyClass propertyClass_p)
    {
        m_Value = value_p;
        m_PropertyClass = propertyClass_p;
    }

    /** create a copy of the object
     */
    public Object clone()
    {
        try
        {
            // === array clone
            return new OwStandardProperty(((Object[]) m_Value).clone(), m_PropertyClass);
        }
        catch (Exception e)
        {
            // not an array
        }

        try
        {
            // === try to call the clone method
            java.lang.reflect.Method method = m_Value.getClass().getMethod("clone", null);
            return new OwStandardProperty(method.invoke(m_Value, null), m_PropertyClass);
        }
        catch (Exception e)
        {
            // ignore, it is a scalar that is cloned by call by value calling convention
        }

        // scalar clones with call by value automatically
        return new OwStandardProperty(m_Value, m_PropertyClass);
    }

    /**  implementation of java.lang.Comparable interface Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
        In the foregoing description, the notation sgn(expression) designates the mathematical signum function, which is defined to return one of -1, 0, or 1 according to whether the value of expression is negative, zero or positive. The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y. (This implies that x.compareTo(y) must throw an exception iff y.compareTo(x) throws an exception.)

        The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) implies x.compareTo(z)>0.

        Finally, the implementer must ensure that x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z.

        It is strongly recommended, but not strictly required that (x.compareTo(y)==0) == (x.equals(y)). Generally speaking, any class that implements the Comparable interface and violates this condition should clearly indicate this fact. The recommended language is "Note: this class has a natural ordering that is inconsistent with equals." 
     * @param obj_p the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object. 
    */
    public int compareTo(Object obj_p)
    {
        return OwPropertyComparator.legacyCompare(this, obj_p);
    }

    /** get the class description of the property
     */
    public OwPropertyClass getPropertyClass() throws Exception
    {
        return m_PropertyClass;
    }

    /** get the corresponding field definition of the field
     * @return OwFieldDefinition
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return getPropertyClass();
    }

    /** get the value of the property. Can also be a list of values (see OwPropertyClass.isArray)
     * @return Object value of property if property is scalar, or a java.io.List of objects if Property is an array
     */
    public Object getValue() throws Exception
    {
        return m_Value;
    }

    /** set the value of the property. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of property if property is scalar, or a java.io.List of objects if Property is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        m_Value = oValue_p;
    }

    /** check if property is read only on the instance level
     *  NOTE:   isReadOnly is also defined in OwPropertyClass on the class level.
     *          I.e. A Property might be defined as readable on the class level, but still be write protected on a specific instance.
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     * @return true if property is read only
     */
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return m_PropertyClass.isReadOnly(iContext_p);
    }

    /** check if property is visible to the user
     *
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     *
     * @return true if property is visible to the user
     */
    public boolean isHidden(int iContext_p) throws Exception
    {
        return m_PropertyClass.isHidden(iContext_p);
    }

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardProperty.getNativeObject: Not implemented or Not supported.");
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("OwStandardProperty [");
        buffer.append("\n- m_value          = ").append(this.m_Value);
        try
        {
            buffer.append("\n- isSystemProperty = ").append(this.m_PropertyClass.isSystemProperty());
        }
        catch (Exception e)
        {
        }
        try
        {
            buffer.append("\n- ClassName = ").append(this.m_PropertyClass.getClassName());
        }
        catch (Exception e)
        {
        }
        buffer.append("\n- m_PropertyClass  = ").append(this.m_PropertyClass);
        buffer.append("\n]");
        return buffer.toString();
    }
}