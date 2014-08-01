package com.wewebu.ow.server.ecmimpl.alfresco.bpm.util;

import java.util.Comparator;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkflowDescription;

/**
 *<p>
 * Compares two {@link OwAlfrescoBPMWorkflowDescription} instances based on their {@link OwAlfrescoBPMWorkflowDescription#getName()}. 
 * For equal names it falls back to comparing by {@link OwAlfrescoBPMWorkflowDescription#getId()}.
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
 *@since 4.0.0.0
 */
public class WorkflowDescriptionNameComparator implements Comparator<OwAlfrescoBPMWorkflowDescription>
{

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(OwAlfrescoBPMWorkflowDescription o1, OwAlfrescoBPMWorkflowDescription o2)
    {
        if (o1 == o2)
        {
            return 0;
        }

        if (null != o1 && null != o2)
        {
            String name1 = o1.getName();
            String name2 = o2.getName();
            if (name1.equals(name2))
            {
                return o1.getId().compareTo(o2.getId());
            }
            else
            {
                return name1.compareTo(name2);
            }
        }

        if (null == o1)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
