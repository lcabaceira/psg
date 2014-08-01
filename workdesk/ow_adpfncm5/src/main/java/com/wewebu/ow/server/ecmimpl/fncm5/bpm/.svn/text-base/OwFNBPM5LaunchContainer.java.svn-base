package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

import filenet.vw.api.VWStepElement;

/**
 *<p>
 * FileNet BPM Repository. Container for launch-step-wrapping work items.
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
public class OwFNBPM5LaunchContainer extends OwFNBPM5BaseContainer
{
    private static final String ID_PREFIX = "LC";

    private OwFNBPM5LaunchStepWorkItem m_launchItem;

    public OwFNBPM5LaunchContainer(OwFNBPM5Repository repository_p, VWStepElement stepElement_p) throws Exception
    {
        super(repository_p);
        this.m_launchItem = new OwFNBPM5LaunchStepWorkItem(this, stepElement_p);
    }

    protected OwObjectCollection getChildsInternal(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p, boolean lock_p) throws Exception
    {
        OwObjectCollection children = new OwStandardObjectCollection();
        for (int i = 0; i < objectTypes_p.length; i++)
        {
            if (objectTypes_p[i] == OwObjectReference.OBJECT_TYPE_WORKITEM)
            {
                children.add(m_launchItem);
                break;
            }
        }

        return children;
    }

    protected boolean isUserContainer()
    {
        return false;
    }

    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return getChildsInternal(objectTypes_p, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p, false);
    }

    public String getID()
    {
        return ID_PREFIX + m_launchItem.getID();
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/launch";
    }

    public String getName()
    {
        return getContext().localize("owlabel.OwFNBPMLaunchContainer.name", "Launch Step");
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_FOLDER;
    }

    public final OwFNBPM5LaunchStepWorkItem getLaunchWorkItem()
    {
        return m_launchItem;
    }

}
