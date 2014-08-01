package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.ArrayList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Simulates a complex property.
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
 *@see OwDummyComplexPersonPropertyClass
 */
public class OwDummyComplexAddressPropertyClass extends OwStandardPropertyClass
{
    public OwDummyComplexAddressPropertyClass(String name_p, OwString displayname_p, boolean fArray_p)
    {
        m_strClassName = name_p;
        m_fArray = fArray_p;
        m_DisplayName = displayname_p;

        m_strJavaClassName = "java.lang.Object";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardPropertyClass#getComplexChildClasses()
     */
    public List getComplexChildClasses() throws Exception
    {
        // some fake address lines
        OwPropertyClass address = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.ADDRESS_PROPERTY);
        OwPropertyClass id = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.ID_PROPERTY);

        ArrayList ret = new ArrayList();

        // add complex types
        ret.add(address);
        ret.add(id);

        return ret;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwStandardPropertyClass#isComplex()
     */
    public boolean isComplex()
    {
        return true;
    }
}
