package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Implementation of OwProperty for external attachment's from different repositories than P8.
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
public class OwFNBPM5ExternalAttachmentProperty implements OwProperty, Cloneable
{
    /** 
     * the embedded object references that are defined by the workflow string property.<br> 
     * for a single element property (non-array) the element at index 0 is considered only.
     * @see OwPropertyClass#isArray()
     * */
    private OwObjectReference[] m_ref;

    /** the property class description */
    private OwPropertyClass m_externalAttachmentPropClass;

    /** queue reference */
    private OwFNBPM5BaseContainer m_queue;

    /** create external attachment property class
     * 
     * @param queue_p
     * @param externalAttachmentPropClass_p
     * @param value_p
     * @throws Exception 
     */
    public OwFNBPM5ExternalAttachmentProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass externalAttachmentPropClass_p, Object value_p) throws Exception
    {
        m_externalAttachmentPropClass = externalAttachmentPropClass_p;

        m_queue = queue_p;

        if (m_externalAttachmentPropClass.isArray())
        {
            if ((value_p != null) && value_p.getClass().isArray())
            {
                Object[] arrayValue = (Object[]) value_p;
                //remove null and empty string references
                List validReferences = new ArrayList();
                for (int i = 0; i < arrayValue.length; i++)
                {
                    if (arrayValue[i] != null && arrayValue[i].toString().length() > 0)
                    {
                        validReferences.add(arrayValue[i]);
                    }
                }
                arrayValue = validReferences.toArray(new Object[validReferences.size()]);
                // convert from string values array 
                m_ref = new OwObjectReference[arrayValue.length];
                for (int i = 0; i < arrayValue.length; i++)
                {
                    OwStandardObjectReference reference = null;
                    if (arrayValue[i] != null && arrayValue[i].toString().length() > 0)
                    {
                        reference = new OwStandardObjectReference(arrayValue[i].toString(), m_queue.getNetwork());
                    }
                    m_ref[i] = reference;
                }
            }
            else
            {
                m_ref = new OwObjectReference[0];
            }
        }
        else
        {
            OwStandardObjectReference singleReference = null;
            if ((value_p != null) && (value_p.toString().length() != 0))
            {
                // convert from string value
                singleReference = new OwStandardObjectReference(value_p.toString(), m_queue.getNetwork());
            }
            m_ref = new OwObjectReference[] { singleReference };
        }
    }

    /** create external attachment property class
     * 
     * @param queue_p
     * @param externalAttachmentPropClass_p
     * @param ref_p
     */
    public OwFNBPM5ExternalAttachmentProperty(OwFNBPM5BaseContainer queue_p, OwPropertyClass externalAttachmentPropClass_p, OwObjectReference[] ref_p)
    {
        m_externalAttachmentPropClass = externalAttachmentPropClass_p;

        m_queue = queue_p;

        m_ref = new OwObjectReference[ref_p.length];
        System.arraycopy(ref_p, 0, m_ref, 0, ref_p.length);
    }

    /** get the original string value
     * 
     */
    public Object getNativeObject() throws Exception
    {
        // convert to string value
        if (null != m_ref)
        {
            if (m_externalAttachmentPropClass.isArray())
            {
                String[] nativeReferences = new String[m_ref.length];
                for (int i = 0; i < m_ref.length; i++)
                {
                    nativeReferences[i] = OwStandardObjectReference.getCompleteReferenceString(m_ref[i], OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                return nativeReferences;
            }
            else
            {
                if (m_ref[0] != null)
                {
                    return OwStandardObjectReference.getCompleteReferenceString(m_ref[0], OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                else
                {
                    return "";
                }
            }
        }
        else
        {
            return "";
        }
    }

    public OwPropertyClass getPropertyClass() throws Exception
    {
        return m_externalAttachmentPropClass;
    }

    /** overridden from java.lang.Object, make sure clone is implemented in subclasses
    *
    * @return OwProperty copy of this object
    */
    public Object clone() throws CloneNotSupportedException
    {
        return new OwFNBPM5ExternalAttachmentProperty(m_queue, m_externalAttachmentPropClass, m_ref);
    }

    public boolean isHidden(int context_p) throws Exception
    {
        return m_externalAttachmentPropClass.isHidden(context_p);
    }

    public boolean isReadOnly(int context_p) throws Exception
    {
        return m_externalAttachmentPropClass.isReadOnly(context_p);
    }

    public int compareTo(Object obj_p)
    {
        return 0;
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return m_externalAttachmentPropClass;
    }

    public Object getValue() throws Exception
    {
        if (m_externalAttachmentPropClass.isArray())
        {
            return m_ref;
        }
        else
        {
            return m_ref[0];
        }
    }

    public void setValue(Object value_p) throws Exception
    {
        if (m_externalAttachmentPropClass.isArray())
        {
            if (value_p != null)
            {
                if (value_p.getClass().isArray())
                {
                    Object[] arrayValue = (Object[]) value_p;
                    m_ref = new OwObjectReference[arrayValue.length];
                    for (int i = 0; i < arrayValue.length; i++)
                    {
                        m_ref[i] = (OwObjectReference) arrayValue[i];
                    }
                    //Emulate set like behavior 
                    HashSet valuesSet = new HashSet(Arrays.asList(m_ref));
                    m_ref = (OwObjectReference[]) valuesSet.toArray(new OwObjectReference[valuesSet.size()]);
                }
                else
                {
                    throw new OwInvalidOperationException("Invalid value for attachment array property!");
                }

            }
            else
            {
                m_ref = new OwObjectReference[0];
            }
        }
        else
        {
            m_ref[0] = (OwObjectReference) value_p;
        }

    }

}
