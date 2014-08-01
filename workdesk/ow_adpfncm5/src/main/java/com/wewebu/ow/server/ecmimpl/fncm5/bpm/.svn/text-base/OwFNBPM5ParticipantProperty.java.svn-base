package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwFieldDefinition;

import filenet.vw.api.VWDataField;
import filenet.vw.api.VWParameter;
import filenet.vw.api.VWParticipant;

/**
 *<p>
 * FileNet BPM Plugin.<br/>
 * A FileNet queue wrapper.
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
public class OwFNBPM5ParticipantProperty implements OwProperty, Cloneable
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5ParticipantProperty.class);

    private VWParameter m_parameter;
    private VWDataField m_datafield;
    private OwPropertyClass m_PropertyClass;
    /**
     * participant property should always be of type array
     */
    private Object[] m_value;

    /** construct from VWParameter
     */
    public OwFNBPM5ParticipantProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass propertyClass_p, VWParameter parameter_p)
    {
        m_PropertyClass = propertyClass_p;
        m_parameter = parameter_p;
    }

    /** construct from VWDataField
     */
    public OwFNBPM5ParticipantProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass propertyClass_p, VWDataField datafield_p)
    {
        m_PropertyClass = propertyClass_p;
        m_datafield = datafield_p;
    }

    /** construct from value
     */
    public OwFNBPM5ParticipantProperty(OwFNBPM5ParticipantProperty prop_p) throws Exception
    {
        m_PropertyClass = prop_p.getPropertyClass();
        m_value = (Object[]) prop_p.getValue();
    }

    public Object clone()
    {
        try
        {
            return new OwFNBPM5ParticipantProperty(this);
        }
        catch (Exception e)
        {
            return null;
        }
    }

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
            if ((value1 == null) && (value2 == null))
            {
                return true;
            }
            else
            {
                return false;
            }
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
        if ((m_value == null) && ((m_parameter != null) || (m_datafield != null)))
        {
            if (getPropertyClass().isArray())
            {
                VWParticipant[] participants = (m_parameter != null) ? (VWParticipant[]) m_parameter.getValue() : (VWParticipant[]) m_datafield.getValue();
                if (participants != null)
                {
                    // === resolve objects
                    ArrayList<String> resolvedObjects = new ArrayList<String>();

                    for (int i = 0; i < participants.length; i++)
                    {
                        String sName = participants[i].getParticipantName();
                        if (sName.length() != 0)
                        {
                            resolvedObjects.add(sName);
                        }
                    }

                    m_value = resolvedObjects.toArray();
                }
            }
            else
            {
                //should never happen
                String msg = "OwFNBPMParticipantProperty.getValue: Workflow Groups / Participants should always be an array.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
        }

        return m_value;
    }

    public int hashCode()
    {
        if (m_value == null)
        {
            return 0;
        }
        else
        {
            return Arrays.hashCode(m_value);
        }
    }

    /** set the value of the property. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of property if property is scalar, or a java.io.List of objects if Property is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        m_value = (Object[]) oValue_p;
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
        if (m_value == null)
        {
            return null;
        }

        VWParticipant[] part = new VWParticipant[m_value.length];
        for (int i = 0; i < part.length; i++)
        {
            part[i] = new VWParticipant((String) m_value[i]);
        }
        return part;
    }
}