package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL AST node : undefined data type literal implementation.<br/> 
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
public class OwStringImageLiteral implements OwLiteral
{
    private String m_image;

    /**
     * Constructor
     * @param image_p {@link String} image of this literal as should appear in an  SQL string, can be null
     */
    public OwStringImageLiteral(String image_p)
    {
        super();
        this.m_image = image_p;
    }

    public StringBuilder createLiteralSQLString()
    {
        return new StringBuilder(m_image);
    }

    /**
     * 
     *@return <code>true</code> if the string image of this literal is null<br>
     *         <code>false</code> otherwise
     */
    public boolean isNull()
    {
        return m_image == null;
    }

}
