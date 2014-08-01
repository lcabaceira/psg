package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Arrays;
import java.util.LinkedHashSet;

import com.filenet.api.core.Document;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.field.OwFieldDefinition;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWAttachmentType;
import filenet.vw.api.VWDataField;
import filenet.vw.api.VWLibraryType;
import filenet.vw.api.VWParameter;

/**
 *<p>
 * FileNet BPM Plugin. <br/>
 * A FileNet queue wrapper
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
public class OwFNBPM5AttachmentProperty implements OwProperty, Cloneable
{
    private VWParameter m_parameter;
    private VWDataField m_datafield;
    private OwPropertyClass m_PropertyClass;
    private OwFNBPM5BaseContainer m_queue;

    private Object m_value;

    /** construct from VWParameter
     */
    public OwFNBPM5AttachmentProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass propertyClass_p, VWParameter parameter_p)
    {
        m_queue = queue_p;
        m_PropertyClass = propertyClass_p;
        m_parameter = parameter_p;
    }

    /** construct from VWDataField
     */
    public OwFNBPM5AttachmentProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass propertyClass_p, VWDataField datafield_p)
    {
        m_queue = queue_p;
        m_PropertyClass = propertyClass_p;
        m_datafield = datafield_p;
    }

    /** construct from value
     */
    public OwFNBPM5AttachmentProperty(OwFNBPM5AttachmentProperty prop_p) throws Exception
    {
        m_PropertyClass = prop_p.getPropertyClass();
        m_value = prop_p.getValue();
    }

    /** create a copy of the object
     */
    public Object clone()
    {
        try
        {
            return new OwFNBPM5AttachmentProperty(this);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**  implementation of java.lang.Comparable interface Compares this object with the specified object for order. Returns a negative integer, 
     * zero, or a positive integer as this object is less than, equal to, or greater than the specified object.<br />
     * In the foregoing description, the notation sgn(expression) designates the mathematical signum function, which is defined to return one of -1, 0, or 1 
     * according to whether the value of expression is negative, zero or positive. The implementor must ensure
     * <code> sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) </code> for all x and y. <br /> 
     * (This implies that x.compareTo(y) must throw an exception if y.compareTo(x) throws an exception.)
     * <br />
     * The implementor must also ensure that the relation is transitive: <code>(x.compareTo(y)>0 && y.compareTo(z)>0) </code> implies x.compareTo(z)>0.
     * <br />
     * Finally, the implementer must ensure that x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z.
     * <br />
     * It is strongly recommended, but not strictly required that (x.compareTo(y)==0) == (x.equals(y)). Generally speaking, any class that implements the Comparable interface and violates this condition should clearly indicate this fact. The recommended language is "Note: this class has a natural ordering that is inconsistent with equals." 
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

    public final int hashCode()
    {
        return super.hashCode();
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
                VWAttachment[] att = (m_parameter != null) ? (VWAttachment[]) m_parameter.getValue() : (VWAttachment[]) m_datafield.getValue();
                if (att != null)
                {
                    // === resolve objects
                    java.util.List resolvedObjects = new java.util.ArrayList();

                    for (int i = 0; i < att.length; i++)
                    {
                        if (att[i].getId() != null)
                        {
                            OwFNBPM5Repository repository = m_queue.getRepository();
                            resolvedObjects.add(repository.getFNCMObject(att[i]));
                        }
                    }

                    m_value = resolvedObjects.toArray();
                }
            }
            else
            {
                // resolve attachment and create OwObject
                VWAttachment att = (m_parameter != null) ? (VWAttachment) m_parameter.getValue() : (VWAttachment) m_datafield.getValue();
                if (att != null && att.getId() != null)
                {
                    m_value = m_queue.getRepository().getFNCMObject(att);
                }

            }
        }

        return m_value;
    }

    /** set the value of the property. Can also be an array of values (see OwPropertyClass.isArray)
     * Duplicated values are eliminated - emulates a Set-like behavior.
     * @param oValue_p value of property if property is scalar, or a java.io.List of objects if Property is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        setUniqueValue(oValue_p);
    }

    /** set the value of the property. Can also be an array of values (see OwPropertyClass.isArray)<br>
     *  Duplicated values are eliminated - emulates a Set-like behavior. 
    * @param oValue_p value of property if property is scalar, or a java.io.List of objects if Property is an array
     * @throws Exception 
    */
    private void setUniqueValue(Object oValue_p) throws Exception
    {
        m_value = oValue_p;
        if (m_value != null && getPropertyClass().isArray())
        {
            Object[] arrayValue = (Object[]) m_value;
            LinkedHashSet setValues = new LinkedHashSet(Arrays.asList(arrayValue));
            m_value = setValues.toArray(new Object[setValues.size()]);
        }
    }

    /** 
     * get the VWAttachmentType object type for a given CE object type
     * @see OwObjectReference
     */
    private static int getVWObjectTypeFromReferenceType(int iObjectType_p)
    {
        switch (iObjectType_p)
        {
            case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                return VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT;

            case OwObjectReference.OBJECT_TYPE_CUSTOM:
                return VWAttachmentType.ATTACHMENT_TYPE_CUSTOM_OBJECT;

            case OwObjectReference.OBJECT_TYPE_FOLDER:
                return VWAttachmentType.ATTACHMENT_TYPE_FOLDER;

            case OwObjectReference.OBJECT_TYPE_STORED_SEARCH:
                return VWAttachmentType.ATTACHMENT_TYPE_STORED_SEARCH;

            default:
                return VWAttachmentType.ATTACHMENT_TYPE_UNDEFINED;
        }
    }

    /** 
     * set an VW Attachment from the given P8 CE Object 
     * 
     * @param att_p
     * @param obj_p
     * @param useObjectStoreId_p
     * 
     * @throws Exception
     */
    public static void setAttachment(VWAttachment att_p, OwFNCM5Object obj_p, boolean useObjectStoreId_p) throws Exception
    {
        String objstoreid = null;

        if (useObjectStoreId_p)
        {
            //            objstoreid = obj_p.getFileNetObject().getObjectStoreId();
            objstoreid = obj_p.getResourceID();
        }
        else
        {
            objstoreid = ((OwFNCM5ObjectStoreResource) obj_p.getResource()).getName();
        }

        att_p.setAttachmentName(obj_p.getName());

        if (obj_p.getType() != OwObjectReference.OBJECT_TYPE_DOCUMENT)
        {
            // === other classes
            att_p.setId(obj_p.getID());
            att_p.setLibraryName(objstoreid);
            att_p.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
            att_p.setType(getVWObjectTypeFromReferenceType(obj_p.getType()));
            att_p.setVersion(null);
        }
        else
        {
            // === document
            att_p.setId(((Document) obj_p.getNativeObject()).get_VersionSeries().get_Id().toString());
            att_p.setLibraryName(objstoreid);
            att_p.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
            att_p.setType(getVWObjectTypeFromReferenceType(obj_p.getType()));
            att_p.setVersion(null);
        }
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

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        Object oValue = getValue();

        // === create FileNet attachment object
        if (getFieldDefinition().isArray())
        {
            // === array
            try
            {
                VWAttachment[] att = new VWAttachment[((Object[]) oValue).length];

                for (int i = 0; i < att.length; i++)
                {
                    try
                    {
                        // unresolved 
                        att[i] = ((OwStandardUnresolvedAttachmentReference) ((Object[]) oValue)[i]).getNativeAttachment();
                    }
                    catch (ClassCastException e)
                    {
                        att[i] = new VWAttachment();

                        setAttachment(att[i], (OwFNCM5Object) ((Object[]) oValue)[i], true);
                    }
                }

                return att;
            }
            catch (NullPointerException e)
            {
                return null;
            }
        }
        else if (oValue != null)
        {
            // === scalar
            VWAttachment att = null;
            try
            {
                // unresolved 
                att = ((OwStandardUnresolvedAttachmentReference) oValue).getNativeAttachment();
            }
            catch (ClassCastException e)
            {
                att = new VWAttachment();
                setAttachment(att, (OwFNCM5Object) oValue, true);
            }

            return att;
        }
        else
        {
            return new VWAttachment();
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("OwFNBPMAttachmentProperty [");
        buffer.append("\n- m_value          = ").append(this.m_value == null ? "[null]" : "[attachment set]");
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