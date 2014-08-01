package com.wewebu.ow.unittest.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;

/**
 *<p>
 * OwSearchTestFieldDefinitionProvider. 
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
public class OwSearchTestFieldDefinitionProvider implements OwFieldDefinitionProvider
{
    private Map<String, OwSearchTestFieldDefinition> m_fields = new HashMap<String, OwSearchTestFieldDefinition>();

    public OwSearchTestFieldDefinitionProvider()
    {
    }

    public void add(OwSearchTestFieldDefinition definition_p)
    {
        m_fields.put(definition_p.getClassName(), definition_p);
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        return m_fields.get(strFieldDefinitionName_p);
    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int op_p) throws Exception
    {

        return null;
    }

}
