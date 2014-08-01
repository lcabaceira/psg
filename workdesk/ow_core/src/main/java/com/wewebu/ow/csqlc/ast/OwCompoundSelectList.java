package com.wewebu.ow.csqlc.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *<p>
 * SQL AST node : an container of &lt;select list&gt;s implementation of the 
 * &lt;select list&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
 *@see OwSelectSublist
 *@since 3.2.0.0
 */
public class OwCompoundSelectList implements OwSelectList
{
    private List<OwSelectSublist> m_sublists = new ArrayList<OwSelectSublist>();

    /**
     * Adds the given &lt;select list&gt; to the list of &lt;select list&gt;s that 
     * this select list composed of.
     * @param sublist_p  
     */
    public void add(OwSelectSublist sublist_p)
    {
        m_sublists.add(sublist_p);
    }

    public StringBuilder createSelectListSQLString()
    {
        if (m_sublists.size() > 0)
        {

            OwSelectSublist zeroSublist = m_sublists.get(0);
            StringBuilder zeroBuilder = zeroSublist.createSelectListSQLString();
            Set<String> uniqueSublists = new LinkedHashSet<String>();
            uniqueSublists.add(zeroBuilder.toString());

            for (int i = 1; i < m_sublists.size(); i++)
            {
                OwSelectSublist sublist = m_sublists.get(i);
                StringBuilder subString = sublist.createSelectListSQLString();
                uniqueSublists.add(subString.toString());
            }

            StringBuilder builder = new StringBuilder();
            Iterator<String> ui = uniqueSublists.iterator();
            String firstSub = ui.next();
            builder.append(firstSub);
            while (ui.hasNext())
            {
                String sub = ui.next();
                builder.append(",");
                builder.append(sub);
            }

            return builder;
        }
        else
        {
            return new StringBuilder();
        }
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        List<OwColumnQualifier> qualifiers = new LinkedList<OwColumnQualifier>();
        for (OwSelectSublist sub : m_sublists)
        {
            qualifiers.addAll(sub.getColumnQualifiers());
        }
        return qualifiers;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.csqlc.ast.OwSelectList#contains(java.lang.String, java.lang.String)
     */
    @Override
    public boolean containsColumnReference(String tableName, String columnReferenceName)
    {
        for (OwSelectSublist sublist : this.m_sublists)
        {
            if (sublist.containsColumnReference(tableName, columnReferenceName))
            {
                return true;
            }
        }
        return false;
    }
}
