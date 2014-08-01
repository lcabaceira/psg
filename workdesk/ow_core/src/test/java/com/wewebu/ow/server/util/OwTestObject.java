package com.wewebu.ow.server.util;

import com.wewebu.ow.unittest.pojo.OwObjectAdaptor;

public class OwTestObject extends OwObjectAdaptor
{
    OwTestObjectClass m_owTestClass;

    public OwTestObject(OwTestObjectClass objectClass_p)
    {
        m_owTestClass = objectClass_p;
    }

    public OwTestObjectClass getObjectClass()
    {

        return m_owTestClass;
    }

    public void setObjectClass(OwTestObjectClass objectClass_p)
    {
        m_owTestClass = objectClass_p;

    }

}
