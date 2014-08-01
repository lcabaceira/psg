package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL AST node : an invalid/missing literal implementation.<br/>
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
public class OwNullLiteral implements OwLiteral
{

    /**
     * Always throws a {@link NullPointerException}.
     * @return N/A
     * 
     */
    public StringBuilder createLiteralSQLString()
    {
        throw new NullPointerException("Can not create SQL strings for Null literals!");
    }

    /**
     * 
     * @return <code>true</code> always
     */
    public boolean isNull()
    {
        return true;
    }

}
