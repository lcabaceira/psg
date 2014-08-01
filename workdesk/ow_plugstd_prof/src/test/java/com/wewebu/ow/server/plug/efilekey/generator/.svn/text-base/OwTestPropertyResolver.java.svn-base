package com.wewebu.ow.server.plug.efilekey.generator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OwTestPropertyResolver implements OwKeyPropertyResolver
{
    private Map properties = new HashMap();

    public void setPropertyValue(String property_p, Object value_p)
    {
        this.properties.put(property_p, value_p);
    }

    public Object getPropertyValue(String propertyName_p)
    {
        return this.properties.get(propertyName_p);
    }

    public Locale getLocale()
    {
        return Locale.ENGLISH;
    }

}
