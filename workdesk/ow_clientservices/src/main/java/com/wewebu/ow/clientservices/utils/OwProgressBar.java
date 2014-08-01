package com.wewebu.ow.clientservices.utils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *<p>
 * Utility class for the Workdesk client services applet.
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
public class OwProgressBar extends Canvas
{
    private static final long serialVersionUID = 1L;

    private long mLevel;
    private long mMaximum;
    private Color mFrameColor;

    public OwProgressBar()
    {
        setForeground(Color.gray);
        mFrameColor = Color.black;
        setLevel(0);
    }

    public void setMaximum(int maximum_p)
    {
        mMaximum = maximum_p;
    }

    public void setLevel(long current_p)
    {
        mLevel = (current_p > mMaximum) ? mMaximum : current_p;
        repaint();
    }

    public void update(Graphics g_p)
    {
        paint(g_p);
    }

    public void paint(Graphics g_p)
    {
        Dimension d = getSize();
        double ratio = ((double) mLevel / (double) mMaximum);
        int x = (int) (d.width * ratio);

        g_p.setColor(mFrameColor);
        g_p.drawRect(0, 0, d.width - 1, d.height - 1);

        g_p.setColor(getForeground());
        g_p.fillRect(1, 1, x, d.height - 2);

        g_p.setColor(getBackground());
        g_p.fillRect(x + 1, 1, d.width - 2 - x, d.height - 2);
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(10, 1);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(100, 10);
    }
}