package com.wewebu.ow.server.plug.efilekey.pattern;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver;

/**
 *<p>
 * This class is an implementation for key pattern.<br/>
 * According with specification, a key pattern is represented in the following form: <br>
 * <b>Char</b> ::= [a-z] | [A-Z] | [0-9]  <br>
 * <b>specialChar</b> ::= ["_", "-", "/", "#", ".", ":", ";", "§", "$"] <br>
 * <b>stringSymbolic</b> ::= &lt;Char&gt;({"." | ":" | "_" | &lt;Char&gt;}) <br>
 * <b>propertyName</b> ::= &lt;stringSymbolic&gt; <br>
 * <b>staticRef</b> ::==["ow_today", "ow_userid", "ow_username", "ow_userlongname", "ow_usershortname", "ow_userdisplayname", "ow_sys_counter", "ow_sys_guid"] <br>
 * <b>propertyRef</b> ::= "{" &lt;propertyName&gt; | &lt;staticRef&gt; (, "\"" {&lt;Char&gt;|&lt;specialChar&gt;}"\"") "}" <br>
 * <b>keyPattern</b> ::= {&lt;propertyRef&gt; | &lt;Char&gt; | &lt;specialChar&gt;} <br>
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
 *@since 3.1.0.0
 */
public class OwKeyPatternImpl implements OwKeyPattern
{
    /** a list of patterns that compose the key*/
    private List m_keyPatterns = new LinkedList();

    /**
     * Constructor
     * @param keyPattern_p - the key patterns list
     */
    public OwKeyPatternImpl(List keyPattern_p)
    {
        m_keyPatterns = keyPattern_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.efilekey.pattern.OwKeyPattern#createStringImage(com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver)
     */
    public String createStringImage(OwKeyPropertyResolver resolver_p) throws OwInvalidOperationException
    {
        StringBuffer buff = new StringBuffer();
        for (Iterator patternIteratos = m_keyPatterns.iterator(); patternIteratos.hasNext();)
        {
            OwSingleKeyPattern pattern = (OwSingleKeyPattern) patternIteratos.next();
            buff.append(pattern.createStringImage(resolver_p));
        }
        return buff.toString();
    }

    /**
     * Get the property names involved in the key pattern. The returning {@link Set} object contains the system properties too.
     * @return - a {@link Set} of property names involved in the key pattern. 
     */
    public Set getPropertyNames()
    {
        Set result = new LinkedHashSet();
        for (Iterator patternIteratos = m_keyPatterns.iterator(); patternIteratos.hasNext();)
        {
            OwSingleKeyPattern pattern = (OwSingleKeyPattern) patternIteratos.next();
            if (pattern.getPropertyName() != null)
            {
                result.add(pattern.getPropertyName());
            }
        }
        return result;
    }

}
