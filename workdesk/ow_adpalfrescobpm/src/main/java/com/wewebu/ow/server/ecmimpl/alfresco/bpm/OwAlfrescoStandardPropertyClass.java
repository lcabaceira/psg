package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Alfresco BPM based implementation of {@link OwStandardPropertyClass}.
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
 *@since 4.0.0.0
 */
public class OwAlfrescoStandardPropertyClass extends OwStandardPropertyClass
{

    private OwAlfrescoBPMFormat format;

    public OwAlfrescoStandardPropertyClass(String className, Class<?> javaClass, OwAlfrescoBPMFormat format)
    {
        this.m_strClassName = className;
        this.m_DisplayName = new OwString(className);
        this.m_strJavaClassName = javaClass.getName();
        this.format = format;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardPropertyClass#getFormat()
     */
    @Override
    public OwFormat getFormat()
    {
        return this.format;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardPropertyClass#getValueFromString(java.lang.String)
     */
    @Override
    public Object getValueFromString(String text_p) throws Exception
    {
        if (null != this.format)
        {
            return this.format.parse(text_p);
        }
        else
        {
            return super.getValueFromString(text_p);
        }
    }

    public void setSystem(boolean system)
    {
        this.m_fSystem = system;
    }

    public void setReadOnly(boolean readonly)
    {
        for (int i = 0; i < this.m_fReadOnly.length; i++)
        {
            this.m_fReadOnly[i] = readonly;
        }
    }

    public void setHidden(boolean hidden)
    {
        for (int i = 0; i < this.m_fReadOnly.length; i++)
        {
            this.m_fHidden[i] = hidden;
        }
    }

    public void setIsArray(boolean isArray)
    {
        this.m_fArray = isArray;
    }

    public void setIsNameProperty(boolean isName)
    {
        this.m_fName = isName;
    }

    public void setIsRequired(boolean required)
    {
        this.m_fRequired = required;
    }
}
