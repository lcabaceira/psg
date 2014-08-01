package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwNotSupportedException;

/**
 *<p>
 * OwClass value for search templates. Used to specify a object class. see OwStandardClassSelectObjectClass.
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
public class OwClass
{
    private boolean m_fIncludeSubclasses;
    private boolean m_fEnabled;
    private int m_iObjectType;
    private String m_strClassName;
    private String m_strBaseClassName;
    private String m_strResourceName;

    /** construct a OwClass value
     * 
     * @param iObjectType_p
     * @param strClassName_p
     * @param strBaseClassName_p
     * @param strResourceName_p
     * @param fIncludeSubclasses_p
     */
    public OwClass(int iObjectType_p, String strClassName_p, String strBaseClassName_p, String strResourceName_p, boolean fEnabled_p, boolean fIncludeSubclasses_p)
    {
        m_strClassName = strClassName_p;
        m_fIncludeSubclasses = fIncludeSubclasses_p;
        m_fEnabled = fEnabled_p;
        m_strBaseClassName = strBaseClassName_p;
        m_strResourceName = strResourceName_p;
        m_iObjectType = iObjectType_p;
    }

    /** construct a OwClass value
     * 
     * @param iObjectType_p
     * @param strClassName_p
     * @param fIncludeSubclasses_p
     */
    public OwClass(int iObjectType_p, String strClassName_p, boolean fEnabled_p, boolean fIncludeSubclasses_p)
    {
        m_strClassName = strClassName_p;
        m_fIncludeSubclasses = fIncludeSubclasses_p;
        m_fEnabled = fEnabled_p;
        m_strBaseClassName = strClassName_p;
        m_strResourceName = null;
        m_iObjectType = iObjectType_p;
    }

    /** XML constructor */
    public OwClass(org.w3c.dom.Node folderNode_p) throws Exception
    {
        throw new OwNotSupportedException("OwClass: Constructor not implemented.");
    }

    /** string constructor */
    public OwClass(String strInitValue_p) throws Exception
    {
        m_strClassName = strInitValue_p;
        m_strBaseClassName = strInitValue_p;
        m_strResourceName = null;
        m_fIncludeSubclasses = true;
        m_fEnabled = true;

        // default searches for documents
        m_iObjectType = OwObjectReference.OBJECT_TYPE_DOCUMENT;
    }

    public boolean isIncludeSubclasses()
    {
        return m_fIncludeSubclasses;
    }

    public boolean isEnabled()
    {
        return m_fEnabled;
    }

    public int getObjectType()
    {
        return m_iObjectType;
    }

    public String getClassName()
    {
        return m_strClassName;
    }

    public String getResourceName()
    {
        return m_strResourceName;
    }

    public String getBaseClassName()
    {
        return m_strBaseClassName;
    }

    @Override
    public int hashCode()
    {
        return this.m_strClassName == null ? 0 : this.m_strClassName.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof OwClass)
        {
            OwClass classObj = (OwClass) obj;
            return (this.m_strClassName == null && classObj.m_strClassName == null) || (this.m_strClassName != null && this.m_strClassName.equals(classObj.m_strClassName));
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return m_strClassName;
    }
}