package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Class object value for search templates to filter for object classes and object types.
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
public class OwStandardClassSelectObject implements OwField
{
    /** list of OwClass elements */
    protected java.util.List m_Classes = new java.util.ArrayList();

    /** 
     * Class name for the folder property field  
     * that designates class selection considering from and subclasses 
     * template entries.
     * */
    public static final String CLASS_NAME = "OwClassSelectObject";

    /** 
     * Class name for the folder property field  
     * that designates classes indicated by the <b>from</b> template entry.
     * @since 3.2.0.3
     * */
    public static final String FROM_NAME = "OwClassFrom";

    /** 
     * Class name for the folder property field  
     * that designates classes indicated by the <b>subclasses</b> template entry.
     * @since 3.2.0.3
     * */
    public static final String SUBCLASSES_NAME = "OwClassSubclasses";

    /**
     *<p>
     * Class description for on the fly created search fields.
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
    public static class OwStandardClassSelectObjectClass extends OwStandardPropertyClass
    {
        public OwStandardClassSelectObjectClass(String className_p)
        {

            m_strClassName = className_p;
            m_DisplayName = new OwString("ecm.OwStandardClassSelectObject.displayname", "Object Type");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwClass";

            m_fArray = true;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = false;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = false;
            m_fName = false;

            m_fRequired = false;
            m_DMSType = null;

        }

        public OwStandardClassSelectObject newObject()
        {
            return new OwStandardClassSelectObject(this);
        }

    }

    /** 
     * {@link OwClass} valued class for {@link OwStandardClassSelectObject#CLASS_NAME}
     * @since 3.2.0.3 
     * */
    public static final OwStandardClassSelectObjectClass CLASS_CLASS_NAME = new OwStandardClassSelectObjectClass(CLASS_NAME);

    /** 
     * {@link OwClass} valued class for {@link OwStandardClassSelectObject#FROM_NAME}
     * @since 3.2.0.3 
     * */
    public static final OwStandardClassSelectObjectClass CLASS_FROM = new OwStandardClassSelectObjectClass(FROM_NAME);

    /** 
     * {@link OwClass} valued class for {@link OwStandardClassSelectObject#SUBCLASSES_NAME}
     * @since 3.2.0.3 
     * */
    public static OwStandardClassSelectObjectClass CLASS_SUBCLASSES = new OwStandardClassSelectObjectClass(SUBCLASSES_NAME);

    private OwStandardClassSelectObjectClass m_classDescription;

    private OwStandardClassSelectObject(OwStandardClassSelectObjectClass classDescription_p)
    {
        this.m_classDescription = classDescription_p;
    }

    public OwStandardClassSelectObjectClass getClassDescription()
    {
        return m_classDescription;
    }

    /** add a new class entry
     */
    public void addClass(int iObjectType_p, String strClassName_p, String strBaseClassName_p, String strResourceName_p, boolean fIncludeSubclasses_p)
    {
        m_Classes.add(new OwClass(iObjectType_p, strClassName_p, strBaseClassName_p, strResourceName_p, true, fIncludeSubclasses_p));
    }

    /** get the value of the field. Can also be a list of values
      * @return Object value of field if field is scalar, or a java.io.List of objects if field is an array
      */
    public Object getValue() throws Exception
    {
        Object[] ret = m_Classes.toArray();
        return ret;
    }

    /** set the value of the field. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of field if field is scalar, or a java.io.List of objects if field is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        m_Classes.clear();

        if (oValue_p != null)
        {
            for (int i = 0; i < ((Object[]) oValue_p).length; i++)
            {
                m_Classes.add(((Object[]) oValue_p)[i]);
            }
        }
    }

    /** get the corresponding field definition of the field
     * @return OwFieldDefinition
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return m_classDescription;
    }
}