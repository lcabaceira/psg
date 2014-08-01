package com.wewebu.ow.server.ecm;

import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * CBR value for search templates to perform cbr searches.
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
public class OwStandardContentBasedRetrivalObject implements OwField
{
    /** class name for the folder property field */
    public static final String CLASS_NAME = "OwContentBasedRetrivalObject";

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
    public static class OwStandardContentBasedRetrivalObjectClass extends OwStandardPropertyClass
    {
        public OwStandardContentBasedRetrivalObjectClass()
        {

            m_strClassName = CLASS_NAME;
            m_DisplayName = new OwString("ecm.OwStandardContentBasedRetrivalObject.displayname", "Full Text Search");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwStandardContentBasedRetrivalObject";

            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = false;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = false;
            m_fName = false;

            m_fRequired = false;
            m_DMSType = null;

        }
    }

    /** class description singleton */
    private static OwStandardContentBasedRetrivalObjectClass m_classDescription = new OwStandardContentBasedRetrivalObjectClass();

    public static OwStandardContentBasedRetrivalObjectClass getClassDescription()
    {
        return m_classDescription;
    }

    /** construct a resource value */
    public OwStandardContentBasedRetrivalObject(org.w3c.dom.Node folderNode_p) throws Exception
    {
        throw new OwNotSupportedException("OwStandardContentBasedRetrivalObject: Constructor not implemented.");
    }

    /** string constructor */
    public OwStandardContentBasedRetrivalObject(String strSearchString_p) throws Exception
    {
        m_strSearchString = strSearchString_p;
    }

    /** string constructor with zones */
    public OwStandardContentBasedRetrivalObject(String strSearchString_p, List zones_p) throws Exception
    {
        m_strSearchString = strSearchString_p;
        m_zones = zones_p;
    }

    /** search string to look for */
    protected String m_strSearchString;

    /** zones */
    protected List m_zones;

    /** get the zones as a string */
    public String getZonesString()
    {
        StringBuffer strRet = new StringBuffer();
        Iterator it = m_zones.iterator();
        while (it.hasNext())
        {
            strRet.append(it.next());

            if (it.hasNext())
            {
                strRet.append(", ");
            }
        }

        return strRet.toString();
    }

    /** get the zones as a list */
    public List getZones()
    {
        return m_zones;
    }

    /** get the search string to look for */
    public String getSearchString()
    {
        return m_strSearchString;
    }

    public String toString()
    {
        return m_strSearchString;
    }

    /** get the value of the field. Can also be a list of values
      * @return Object value of field if field is scalar, or a java.io.List of objects if field is an array
      */
    public Object getValue() throws Exception
    {
        return this;
    }

    /** set the value of the field. Can also be a list of values (see OwPropertyClass.isArray)
     * @param oValue_p value of field if field is scalar, or a java.io.List of objects if field is an array
     */
    public void setValue(Object oValue_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardContentBasedRetrivalObject.setValue: Cannot set value.");
    }

    /** get the corresponding field definition of the field
     * @return OwFieldDefinition
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return m_classDescription;
    }
}