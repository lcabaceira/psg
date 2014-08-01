package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Search template criteria-field for path entries.
 * Each path criteria field holds an {@link OwSearchPath} value.
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
 *@since 3.0.0.0
 */
public class OwSearchPathField implements OwField
{
    /** class name for the path criteria-field */
    public static final String CLASS_NAME = "OwSearchPath";

    /**
     *<p>
     * OwSearchPathFieldClass.
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
    public static class OwSearchPathFieldClass extends OwStandardPropertyClass
    {
        private OwSearchPathFieldClass()
        {
            m_strClassName = CLASS_NAME;
            m_DisplayName = new OwString("ecm.OwSearchPath.displayname", "Search path");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwSearchPath";

            m_fArray = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = false;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = false;
            m_fName = false;

            m_fRequired = false;
            m_DMSType = null;
        }
    }

    /**The one and only search path field property-class instance*/
    public static final OwSearchPathFieldClass classDescription = new OwSearchPathFieldClass();

    private OwSearchPath path;

    /**
     * Constructor
     * @param path_p initial path value
     */
    public OwSearchPathField(OwSearchPath path_p)
    {
        this.path = path_p;
    }

    public OwSearchPathField()
    {
        //void
    }

    public void setPath(OwSearchPath path_p)
    {
        this.path = path_p;
    }

    public OwSearchPath getPath()
    {
        return this.path;
    }

    public Object getValue() throws OwException
    {
        return getPath();
    }

    public void setValue(Object oValue_p) throws OwException
    {
        setPath((OwSearchPath) oValue_p);
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return OwSearchPathField.classDescription;
    }
}