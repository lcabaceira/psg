package com.wewebu.ow.server.plug.owdemo.owmain;

import com.wewebu.ow.server.app.OwMasterDocument;

/**
 *<p>
 * OwDemoDocument.
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
public class OwDemoDocument extends OwMasterDocument
{
    /** enum for red constant */
    public static final int RED = 1;
    /** enum for green constant */
    public static final int GREEN = 2;
    /** enum for blue constant */
    public static final int BLUE = 3;

    /** color variable */
    protected int m_iColor = 0;

    /** called to set color value of m_iColor
     * @param iColor_p color value
     */
    public void setColor(int iColor_p) throws Exception
    {
        m_iColor = iColor_p;

        update(this, 0, null);
    }

    /** called to get color value of m_iColor
     */
    public int getColor()
    {
        return m_iColor;
    }
}