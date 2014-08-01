package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Collection;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Builds dialog used to display workflows
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
 * @since 4.2.0.0
 */
public class OwBPMDisplayWorkflowDialogBuilder
{
    private Collection<OwObject> items;
    private int index;
    private OwObject parentObject;
    private int maxElementSize;
    private Collection<String> batchIndexProperties;
    private Map<Integer, Map<String, String>> layoutRegionAttributes;

    public OwBPMDisplayWorkflowDialog build()
    {
        return new OwBPMDisplayWorkflowDialog(this);
    }

    public OwBPMDisplayWorkflowDialogBuilder items(Collection<OwObject> items)
    {
        this.items = items;
        return this;
    }

    public OwBPMDisplayWorkflowDialogBuilder index(int index)
    {
        this.index = index;
        return this;
    }

    public OwBPMDisplayWorkflowDialogBuilder parentObject(OwObject parentObject)
    {
        this.parentObject = parentObject;
        return this;
    }

    public OwBPMDisplayWorkflowDialogBuilder maxElementSize(int maxElementSize)
    {
        this.maxElementSize = maxElementSize;
        return this;
    }

    public OwBPMDisplayWorkflowDialogBuilder layoutRegionAttributes(Map<Integer, Map<String, String>> attributes)
    {
        this.layoutRegionAttributes = attributes;
        return this;
    }

    Collection<OwObject> getItems()
    {
        return items;
    }

    int getIndex()
    {
        return index;
    }

    OwObject getParentObject()
    {
        return parentObject;
    }

    int getMaxElementSize()
    {
        return maxElementSize;
    }

    Collection<String> getBatchIndexProperties()
    {
        return batchIndexProperties;
    }

    Map<Integer, Map<String, String>> getLayoutRegionAttributes()
    {
        return layoutRegionAttributes;
    }

}
