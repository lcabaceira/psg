package com.wewebu.ow.clientservices.utils;

import java.awt.GridLayout;
import java.awt.Panel;

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
public class OwProgressBarPanel extends Panel implements OwProgressMonitorListener
{
    private static final long serialVersionUID = 1L;
    private OwProgressBar pb;
    private OwProgressMonitor progressMonitor;

    public OwProgressBarPanel(OwProgressMonitor progressMonitor_p)
    {
        this.progressMonitor = progressMonitor_p;
        this.progressMonitor.addListener(this);

        setLayout(new GridLayout(1, 1));
        pb = new OwProgressBar();
        add(pb);
    }

    public void progressChanged(int current_p)
    {
        pb.setLevel(current_p);
        repaint();
    }

    public void targetChanged(int newTarget_p)
    {
        pb.setMaximum(newTarget_p);
        repaint();
    }
}