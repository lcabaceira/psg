package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.ArrayList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Simulation of a complex property.
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
public class OwDummyComplexPersonPropertyClass extends OwStandardPropertyClass
{
    private static OwDummyComplexAddressPropertyClass m_address = new OwDummyComplexAddressPropertyClass("Address", new OwString("ecmimpl.owdummy.OwDummyComplexPersonPropertyClass.address", "Address"), false);
    private static OwDummyComplexAddressPropertyClass m_addresss = new OwDummyComplexAddressPropertyClass("AddressArray", new OwString("ecmimpl.owdummy.OwDummyComplexPersonPropertyClass.addresses", "Addresses"), true);

    public OwDummyComplexPersonPropertyClass(String name_p, OwString displayname_p, boolean fArray_p)
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
        OwPropertyClass firstname = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.FIRST_NAME_PROPERTY);
        //   OwPropertyClass lastname = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.LAST_NAME_PROPERTY);
        OwPropertyClass dateofbirth = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.DATE_OF_BIRTH_PROPERTY);
        OwPropertyClass notes = OwDummyFileObject.getStaticObjectClass().getPropertyClass(OwDummyFileObject.OwDummyFileObjectClass.NOTES_PROPERTY);

        ArrayList ret = new ArrayList();

        // add complex types
        ret.add(firstname);
        ret.add(m_address);
        ret.add(m_addresss);
        ret.add(dateofbirth);
        ret.add(notes);

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