package com.wewebu.ow.server.util;

import com.wewebu.ow.unittest.pojo.OwObjectClassAdaptor;

public class OwTestObjectClass extends OwObjectClassAdaptor
{
    private String m_className;

    public OwTestObjectClass(String classname_p)
    {
        this.m_className = classname_p;
    }

    public OwTestObjectClass()
    {

    }

    public String getClassName()
    {
        return m_className;

    }

    public void setClassName(String classname_p)
    {
        this.m_className = classname_p;
    }

}
