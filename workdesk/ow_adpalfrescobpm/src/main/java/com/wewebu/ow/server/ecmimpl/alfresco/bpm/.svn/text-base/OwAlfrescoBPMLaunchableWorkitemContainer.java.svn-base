package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Container for launchable WorkItems only.
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
public class OwAlfrescoBPMLaunchableWorkitemContainer extends OwAlfrescoBPMBaseContainer
{

    private List<OwAlfrescoBPMLaunchableWorkItem> workItems = new LinkedList<OwAlfrescoBPMLaunchableWorkItem>();

    public OwAlfrescoBPMLaunchableWorkitemContainer(OwNetwork network, OwAlfrescoBPMRepository bpmRepository)
    {
        super(network, bpmRepository);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.IfOwAlfrescoBPMWporkitemContainer#createContaineeMIMEType(com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem)
     */
    public String createContaineeMIMEType(OwAlfrescoBPMWorkItem item)
    {
        return "ow_workitem/launchitem";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.IfOwAlfrescoBPMWporkitemContainer#createContaineeMIMEParameter(com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem)
     */
    public String createContaineeMIMEParameter(OwAlfrescoBPMWorkItem item)
    {
        return "";
    }

    @SuppressWarnings("rawtypes")
    public OwAlfrescoBPMWorkItem createLaunchableWorkItem(OwAlfrescoBPMWorkflowDescription workflowDescription, Collection attachmentobjects) throws OwException
    {
        OwAlfrescoBPMLaunchableWorkItem workItem = new OwAlfrescoBPMLaunchableWorkItem(this, attachmentobjects, workflowDescription, this.bpmRepository);
        this.workItems.add(workItem);
        return workItem;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getID()
     */
    public String getID()
    {
        return "LC";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    @SuppressWarnings("unchecked")
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        OwStandardObjectCollection result = new OwStandardObjectCollection();
        result.addAll(this.workItems);
        return result;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getChildren(OwLoadContext filter) throws OwException
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#hasChilds(int[], int)
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return !this.workItems.isEmpty();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChildCount(int[], int)
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return this.workItems.size();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getName()
     */
    public String getName()
    {
        return "Launch Container";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getType()
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_FOLDER;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/launch";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#canResubmit()
     */
    public boolean canResubmit() throws Exception
    {
        return false;
    }
}
