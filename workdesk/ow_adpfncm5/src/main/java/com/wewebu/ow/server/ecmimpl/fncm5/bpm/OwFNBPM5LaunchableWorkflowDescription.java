package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;

import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;

/**
 *<p>
 * The description of a workflow that can be launched in an {@link OwFNBPM5Repository}.
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
public interface OwFNBPM5LaunchableWorkflowDescription extends OwWorkflowDescription
{
    /**
     * Creates a launchable {@link OwWorkitem} for the given FNBPM repository
     * @param fnbpmRepository_p
     * @param attachmentobjects_p
     * @return the launchable {@link OwWorkitem}
     * @throws Exception
     */
    OwWorkitem createLaunchableItem(OwFNBPM5Repository fnbpmRepository_p, Collection attachmentobjects_p) throws Exception;
}
