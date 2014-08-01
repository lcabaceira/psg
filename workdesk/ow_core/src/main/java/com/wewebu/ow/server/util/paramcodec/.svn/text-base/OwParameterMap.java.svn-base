package com.wewebu.ow.server.util.paramcodec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *<p>
 * Encapsulates a {@link Map} containing parameter names as keys and 
 * parameter values as map values. 
 * The keys in the parameter map are of type String. 
 * The values in the parameter map are of type String array.<br/>
 * It is used to handle HTTP servlet request parameter maps. 
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
public class OwParameterMap
{
    private Map m_parameters = new HashMap();

    /**
     * Constructor
     */
    public OwParameterMap()
    {
        //void
    }

    /**
     * Deep copy constructor
     * @param parameterMap_p
     */
    public OwParameterMap(OwParameterMap parameterMap_p)
    {
        if (parameterMap_p.m_parameters != null)
        {
            m_parameters.putAll(parameterMap_p.m_parameters);
        }
    }

    /**
     * Constructor
     * @param parameterMap_p {@link Map} containing parameter names as keys and 
     *                       parameter values as map values. 
     *                       The keys in the parameter map must be of type String. 
     *                       The values in the parameter map must be of type String array. 
     */
    public OwParameterMap(Map parameterMap_p)
    {
        m_parameters.putAll(parameterMap_p);
    }

    /**
     * 
     * @return a {@link Set} of String parameter names 
     */
    public synchronized Set getParameterNames()
    {
        return m_parameters.keySet();
    }

    /**
     * 
     * @param parameterName_p
     * @return an array of String values for the given parameter or <code>null</code> no parameter 
     *         with the given name is found
     */
    public synchronized String[] getParameterValues(String parameterName_p)
    {
        return (String[]) m_parameters.get(parameterName_p);
    }

    /**
     * Returns the value of a request parameter as a String, or null if the parameter does not exist.
     * @param parameterName_p
     * @return a String representing the single value of the parameter
     */
    public synchronized String getParameter(String parameterName_p)
    {
        String[] values = (String[]) m_parameters.get(parameterName_p);
        if (values != null && values.length > 0)
        {
            return values[0];
        }
        else
        {
            return null;
        }
    }

    /**
     * Adds the given value to the already existing values of the specifed parameter
     * @param parameterName_p
     * @param value_p 
     */
    public synchronized void setParameter(String parameterName_p, String value_p)
    {
        String[] values = (String[]) m_parameters.get(parameterName_p);
        if (values == null)
        {
            values = new String[] { value_p };
        }
        else
        {
            String[] newValues = new String[values.length + 1];
            System.arraycopy(values, 0, newValues, 0, values.length);
            newValues[values.length] = value_p;
            values = newValues;
        }
        m_parameters.put(parameterName_p, values);
    }

    /**
     * 
     * @return a {@link Map}  containing parameter names as keys and 
     *          parameter values as map values. 
     *          The keys in the parameter map are of type String. 
     *          The values in the parameter map are of type String array. 
     */
    public Map toRequestParametersMap()
    {
        return m_parameters;
    }

    /**
     * 
     * @return the a HTTP query string representation of the encapsulated parameter and values
     */
    public synchronized String toRequestQueryString()
    {
        Set entries = m_parameters.entrySet();
        StringBuffer queryString = new StringBuffer();
        for (Iterator i = entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry) i.next();
            String parameterName = (String) entry.getKey();
            String[] parameterValues = (String[]) entry.getValue();
            if (parameterName != null && parameterValues != null)
            {
                for (int j = 0; j < parameterValues.length; j++)
                {
                    if (parameterValues[j] != null)
                    {
                        if (queryString.length() > 0)
                        {
                            queryString.append("&");
                        }
                        queryString.append(parameterName);
                        queryString.append("=");
                        queryString.append(parameterValues[j]);
                    }
                }
            }
        }

        return queryString.toString();
    }

    /**
     * Adds all values of the given {@link Map}.
     * The keys in the parameter map must be of type String. 
     * The values in the parameter map must be of type String array. 
     * @param cookieMap_p
     */
    public synchronized void addAll(Map cookieMap_p)
    {
        Set entries = cookieMap_p.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry) i.next();
            String paramName = (String) entry.getKey();
            String[] paramValues = (String[]) entry.getValue();

            if (paramValues != null)
            {
                String[] currentValues = (String[]) m_parameters.get(paramName);
                if (currentValues == null)
                {
                    m_parameters.put(paramName, paramValues);
                }
                else
                {
                    String[] newValues = new String[currentValues.length + paramValues.length];
                    System.arraycopy(currentValues, 0, newValues, 0, currentValues.length);
                    System.arraycopy(paramValues, 0, newValues, currentValues.length, paramValues.length);
                    m_parameters.put(paramName, newValues);
                }
            }
        }
    }

    public synchronized boolean equals(Object obj_p)
    {
        if (obj_p == null)
        {
            return false;
        }
        synchronized (obj_p)
        {
            if (obj_p instanceof OwParameterMap)
            {
                OwParameterMap pmObj = (OwParameterMap) obj_p;
                Set objeKs = pmObj.m_parameters.keySet();
                Set thisKs = m_parameters.keySet();
                if (objeKs.size() != thisKs.size())
                {
                    return false;
                }
                else
                {
                    for (Iterator i = objeKs.iterator(); i.hasNext();)
                    {
                        String parameterName = (String) i.next();
                        String[] thisValues = (String[]) m_parameters.get(parameterName);
                        String[] objValues = (String[]) pmObj.m_parameters.get(parameterName);
                        if (objValues == null || thisValues == null)
                        {
                            if (objValues != thisValues)
                            {
                                return false;
                            }
                        }
                        else
                        {
                            if (thisValues.length != objValues.length)
                            {
                                return false;
                            }
                            else
                            {
                                for (int j = 0; j < objValues.length; j++)
                                {
                                    if (objValues[j] == null && null != thisValues[j])
                                    {
                                        return false;
                                    }
                                    else
                                    {
                                        if (!objValues[j].equals(thisValues[j]))
                                        {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }

                    }
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public String toString()
    {
        return toRequestQueryString();
    }

}
