package com.wewebu.ow.server.app;

import com.wewebu.ow.server.app.OwClipboard.OwClipboardContent;
import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Single clipboard item for OwObject types.
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
public class OwClipboardContentOwObject implements OwClipboardContent
{
    /** construct a clipboard item
     * @param content_p the clipboard content object
     * @param parent_p the parent object used for cut operation, can be null
     */
    public OwClipboardContentOwObject(OwObject content_p, OwObject parent_p)
    {
        m_clipobject = content_p;
        m_Parent = parent_p;
    }

    /** the object in the clipboard */
    public OwObject getObject()
    {
        return m_clipobject;
    }

    /** the parent of the object for cut operations, can be null */
    public OwObject getParent()
    {
        return m_Parent;
    }

    /** the contained clipboard object */
    private OwObject m_clipobject;

    /** the parent object of the content object, used for cut operation */
    private OwObject m_Parent;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int result = -1;
        if (m_clipobject != null)
        {
            result = m_clipobject.hashCode();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object clipboardObject_p)
    {
        boolean result = clipboardObject_p != null;
        if (result)
        {
            try
            {
                OwClipboardContentOwObject toBeCompared = (OwClipboardContentOwObject) clipboardObject_p;
                // 2 objects with all members <null> are equal
                result = (m_clipobject == null && toBeCompared.m_clipobject == null) && (m_Parent == null && toBeCompared.m_Parent == null);
                if (!result)
                {
                    result = m_clipobject != null && toBeCompared.m_clipobject != null && m_clipobject.equals(toBeCompared.m_clipobject);
                    if (result)
                    {
                        //maybe the parents are both null
                        result = m_Parent == null && toBeCompared.m_Parent == null;
                        if (!result)
                        {
                            result = m_Parent != null && toBeCompared.m_Parent != null && m_Parent.equals(toBeCompared.m_Parent);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                //never should be here - m_clipobject should be not null;
                result = false;
            }
        }
        return result;
    }
}