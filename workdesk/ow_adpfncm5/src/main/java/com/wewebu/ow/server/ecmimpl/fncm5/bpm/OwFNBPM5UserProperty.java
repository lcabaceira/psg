package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * FileNet BPM Plugin. <br/>
 * Property wrapper for F_Originator.
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
public class OwFNBPM5UserProperty implements OwProperty, Cloneable
{

    private OwPropertyClass m_PropertyClass;

    private long m_userid;
    private OwFNBPM5BaseContainer m_queue;

    /** resolved participant property  */
    private String m_value;

    /** construct from VWParameter
     */
    public OwFNBPM5UserProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass propclass_p, long userid_p)
    {
        m_PropertyClass = propclass_p;
        m_queue = queue_p;
        m_userid = userid_p;
    }

    /** construct from value
     */
    public OwFNBPM5UserProperty(OwFNBPM5UserProperty prop_p) throws Exception
    {
        m_PropertyClass = prop_p.m_PropertyClass;
        m_queue = prop_p.m_queue;
        m_value = prop_p.m_value;
        m_userid = prop_p.m_userid;

    }

    /** create a copy of the object
     */
    public Object clone()
    {
        try
        {
            return new OwFNBPM5UserProperty(this);
        }
        catch (Exception e)
        {
            return null;
        }
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
        return 0;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     <code>x</code>, <code>x.equals(x)</code> should return
     *     <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     *     should return <code>true</code> if and only if
     *     <code>y.equals(x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     <code>x</code>, <code>y</code>, and <code>z</code>, if
     *     <code>x.equals(y)</code> returns <code>true</code> and
     *     <code>y.equals(z)</code> returns <code>true</code>, then
     *     <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, multiple invocations of
     *     <tt>x.equals(y)</tt> consistently return <code>true</code>
     *     or consistently return <code>false</code>, provided no
     *     information used in <code>equals</code> comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     *     <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p>
     * The <tt>equals</tt> method for class <code>Object</code> implements 
     * the most discriminating possible equivalence relation on objects; 
     * that is, for any non-null reference values <code>x</code> and
     * <code>y</code>, this method returns <code>true</code> if and only
     * if <code>x</code> and <code>y</code> refer to the same object
     * (<code>x == y</code> has the value <code>true</code>).
     * <p>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the
     * general contract for the <tt>hashCode</tt> method, which states
     * that equal objects must have equal hash codes. 
     *
     * @param   obj_p   the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see     #hashCode()
     * @see     java.util.Hashtable
     */
    public boolean equals(Object obj_p)
    {
        Object value1 = null;
        Object value2 = null;

        try
        {
            value1 = getValue();
            value2 = ((OwProperty) obj_p).getValue();

            if (getFieldDefinition().isArray())
            {
                // === array
                if (((Object[]) value1).length != ((Object[]) value2).length)
                {
                    return false;
                }

                for (int i = 0; i < ((Object[]) value1).length; i++)
                {
                    if (!((Object[]) value1)[i].equals(((Object[]) value2)[i]))
                    {
                        return false;
                    }
                }

                return true;
            }
            else
            {
                // === scalar
                // compare the values
                return value1.equals(value2);
            }
        }
        catch (Exception e)
        {
            return ((value1 == null) && (value2 == null));
        }
    }

    public int hashCode()
    {
        if (m_value == null)
        {
            return 0;
        }
        else
        {
            return m_value.hashCode();
        }
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
        if (m_value == null)
        {
            try
            {
                m_value = m_queue.getVWSession().convertIdToUserNamePx(m_userid).getDisplayName();
            }
            catch (Exception e)
            {
                m_value = "?";
            }
        }

        return m_value;
    }

    /** set the value of the property. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of property if property is scalar, or a java.io.List of objects if Property is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        if (oValue_p == null)
        {
            try
            {
                m_userid = m_queue.getVWSession().convertUserNameToId((String) oValue_p);
            }
            catch (Exception e)
            {
                m_userid = 0;
            }
        }

        m_value = (String) oValue_p;
    }

    /** check if property is read only on the instance level
     *  NOTE:   isReadOnly is also defined in OwPropertyClass on the class level.
     *          I.e. A Property might be defined as readable on the class level, but still be write protected on a specific instance.
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     * @return true if property is read only
     */
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        // class level definition takes priority over Property level
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

    /** get the native object from the ECM system which is an array of VWParticipant
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        return Long.valueOf(m_userid);
    }

}
