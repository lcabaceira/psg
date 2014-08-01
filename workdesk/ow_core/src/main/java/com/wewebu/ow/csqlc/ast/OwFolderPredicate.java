package com.wewebu.ow.csqlc.ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * SQL AST node : &lt;in folder&gt; syntax non-terminal as defined by the SQL grammar.<br/>
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
 *@since 3.2.0.0
 */
public class OwFolderPredicate extends OwPredicate
{

    private OwCharacterStringLiteral m_folderId;
    private boolean m_inTree = false;
    private OwColumnQualifier m_qualifier = new OwColumnQualifier();
    private OwFolderPredicateFormat m_format;

    /**
     * 
     * @param folderId_p <b>folder id</b> string terminal value
     * @param inTree_p <code>true</code> for an <i>in tree</i> search folder predicate (egg. IN_TREE(myFoderID)) <br>  
     *                 <code>false</code> for an  <i>in folder</i> search folder predicate (egg. IN_FOLDER(myFoderID)) <br>
     */
    public OwFolderPredicate(OwCharacterStringLiteral folderId_p, boolean inTree_p, OwFolderPredicateFormat format_p)
    {
        super();
        m_folderId = folderId_p;
        m_inTree = inTree_p;
        m_format = format_p;
    }

    @Override
    public StringBuilder createPredicateSQLString()
    {
        if (isValid())
        {
            StringBuilder idLiteral = m_folderId.createLiteralSQLString();
            return m_format.format(idLiteral.toString(), m_qualifier.getQualifierString(), m_inTree);
        }
        else
        {
            return new StringBuilder();
        }
    }

    /**
     * 
     * @return <code>true</code> if the given folder ID is not null and has a length greater than 0 <br>
     *         <code>false</code> otherwise
     */
    public boolean isValid()
    {
        return m_folderId != null && !m_folderId.isNull();
    }

    public List<OwColumnQualifier> getColumnQualifiers()
    {
        if (isValid())
        {
            return Arrays.asList(new OwColumnQualifier[] { this.m_qualifier });
        }
        else
        {
            return new LinkedList<OwColumnQualifier>();
        }
    }

}
