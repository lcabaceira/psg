package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * Join type enumeration. 
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
public enum OwJoinType
{
    INNER(" INNER "), LEFT(" LEFT "), LEFT_OUTER(" LEFT OUTER "), DEFAULT_JOIN_TYPE("");
    private String m_sqlString;

    private OwJoinType(String sqlString_p)
    {
        m_sqlString = sqlString_p;
    }

    /**
     * 
     * @return the SQL String representation of this join type
     */
    public final String getSqlString()
    {
        return m_sqlString;
    }

}
