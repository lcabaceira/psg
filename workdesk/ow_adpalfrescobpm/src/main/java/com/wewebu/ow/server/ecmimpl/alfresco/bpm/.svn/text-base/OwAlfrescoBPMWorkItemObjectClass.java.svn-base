package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMWorkItemTaskFormObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * {@link OwObjectClass} for {@link OwAlfrescoBPMWorkItem} instances.
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
public class OwAlfrescoBPMWorkItemObjectClass extends OwAlfrescoBPMObjectClass
{

    /**
     * @param wrappedObjectClass
     */
    protected OwAlfrescoBPMWorkItemObjectClass(OwObjectClass wrappedObjectClass)
    {
        super(wrappedObjectClass);
        addVirtualProperties();
    }

    private void addVirtualProperties()
    {
        Format dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'S");
        OwAlfrescoStandardPropertyClass cmCreatedPropertyClass = new OwAlfrescoStandardPropertyClass(PROP_CM_CREATED, Date.class, new OwAlfrescoBPMFormat(dateFormat));
        cmCreatedPropertyClass.setSystem(true);
        cmCreatedPropertyClass.setReadOnly(true);
        this.virtualProperties.put(cmCreatedPropertyClass.getClassName(), cmCreatedPropertyClass);

        //TODO get rid of this as soon as the CMIS adaptor implements Associations properties.
        OwAlfrescoStandardPropertyClass bpmPackagePropertyClass = new OwAlfrescoStandardPropertyClass(PROP_BPM_PACKAGE, String.class, null);
        bpmPackagePropertyClass.setSystem(true);
        bpmPackagePropertyClass.setReadOnly(true);
        this.virtualProperties.put(bpmPackagePropertyClass.getClassName(), bpmPackagePropertyClass);

        //TODO get rid of this as soon as the CMIS adaptor implements Associations properties.
        //        OwAlfrescoStandardPropertyClass cmOwnerPropertyClass = new OwAlfrescoStandardPropertyClass(PROP_CM_OWNER, String.class, null);
        //        cmOwnerPropertyClass.setSystem(true);
        //        cmOwnerPropertyClass.setReadOnly(true);
        //        this.virtualProperties.put(cmOwnerPropertyClass.getClassName(), cmOwnerPropertyClass);

        OwAlfrescoStandardPropertyClass owAttachmentsPropClass = new OwAlfrescoStandardPropertyClass(PROP_OW_ATTACHMENTS, OwObject.class, null);
        owAttachmentsPropClass.setSystem(false);
        owAttachmentsPropClass.setIsArray(true);
        this.virtualProperties.put(owAttachmentsPropClass.getClassName(), owAttachmentsPropClass);

        OwAlfrescoStandardPropertyClass owTitlePropClass = new OwAlfrescoStandardPropertyClass(PROP_OW_TASK_TITLE, String.class, null);
        owTitlePropClass.setSystem(true);
        owTitlePropClass.setReadOnly(true);
        owTitlePropClass.setIsArray(false);
        this.virtualProperties.put(owTitlePropClass.getClassName(), owTitlePropClass);

        OwAlfrescoStandardPropertyClass owAssigneePropClass = new OwAlfrescoStandardPropertyClass(PROP_OW_ASSIGNEE, String.class, null);
        owAssigneePropClass.setSystem(true);
        owAssigneePropClass.setReadOnly(true);
        owAssigneePropClass.setIsArray(false);
        this.virtualProperties.put(owAssigneePropClass.getClassName(), owAssigneePropClass);

        OwAlfrescoStandardPropertyClass statePropClass = new OwAlfrescoStandardPropertyClass(PROP_STATE, String.class, null);
        statePropClass.setSystem(true);
        statePropClass.setReadOnly(true);
        statePropClass.setIsArray(false);
        this.virtualProperties.put(statePropClass.getClassName(), statePropClass);

        OwAlfrescoStandardPropertyClass isReleasablePropClass = new OwAlfrescoStandardPropertyClass(PROP_IS_RELEASABLE, Boolean.class, null);
        isReleasablePropClass.setSystem(true);
        isReleasablePropClass.setReadOnly(true);
        isReleasablePropClass.setIsArray(false);
        this.virtualProperties.put(isReleasablePropClass.getClassName(), isReleasablePropClass);

        OwAlfrescoStandardPropertyClass isClaimablePropClass = new OwAlfrescoStandardPropertyClass(PROP_IS_CLAIMABLE, Boolean.class, null);
        isClaimablePropClass.setSystem(true);
        isClaimablePropClass.setReadOnly(true);
        isClaimablePropClass.setIsArray(false);
        this.virtualProperties.put(isClaimablePropClass.getClassName(), isClaimablePropClass);

        OwAlfrescoStandardPropertyClass isPooledPropClass = new OwAlfrescoStandardPropertyClass(PROP_IS_POOLED, Boolean.class, null);
        isPooledPropClass.setSystem(true);
        isPooledPropClass.setReadOnly(true);
        isPooledPropClass.setIsArray(false);
        this.virtualProperties.put(isPooledPropClass.getClassName(), isPooledPropClass);
    }

    protected void addBpmAssigneeVirtualProperty()
    {
        addUserInfoVirtualPropertyClass(PROP_BPM_ASSIGNEE);
    }

    protected void addUserInfoVirtualPropertyClass(String id)
    {
        OwAlfrescoStandardPropertyClass owBpmAssigneeClass = new OwAlfrescoStandardPropertyClass(id, OwUserInfo.class, null);
        owBpmAssigneeClass.setSystem(false);
        owBpmAssigneeClass.setReadOnly(false);
        owBpmAssigneeClass.setIsArray(false);
        owBpmAssigneeClass.setIsRequired(true);
        this.virtualProperties.put(owBpmAssigneeClass.getClassName(), owBpmAssigneeClass);
    }

    protected void addBpmAssigneesVirtualProperty()
    {
        OwAlfrescoStandardPropertyClass owBpmAssigneesClass = new OwAlfrescoStandardPropertyClass(PROP_BPM_ASSIGNEES, OwUserInfo.class, null);
        owBpmAssigneesClass.setSystem(false);
        owBpmAssigneesClass.setReadOnly(false);
        owBpmAssigneesClass.setIsArray(true);
        owBpmAssigneesClass.setIsRequired(true);
        this.virtualProperties.put(owBpmAssigneesClass.getClassName(), owBpmAssigneesClass);
    }

    protected void addBpmGroupAssigneeVirtualProperty()
    {
        OwAlfrescoStandardPropertyClass owBpmGroupAssigneeClass = new OwAlfrescoStandardPropertyClass(PROP_BPM_GROUP_ASSIGNEE, String.class, null);
        owBpmGroupAssigneeClass.setSystem(false);
        owBpmGroupAssigneeClass.setReadOnly(false);
        owBpmGroupAssigneeClass.setIsArray(false);
        owBpmGroupAssigneeClass.setIsRequired(true);
        this.virtualProperties.put(owBpmGroupAssigneeClass.getClassName(), owBpmGroupAssigneeClass);
    }

    protected void addBpmGroupAssigneesVirtualProperty()
    {
        OwAlfrescoStandardPropertyClass owBpmGroupAssigneesClass = new OwAlfrescoStandardPropertyClass(PROP_BPM_GROUP_ASSIGNEES, String.class, null);
        owBpmGroupAssigneesClass.setSystem(false);
        owBpmGroupAssigneesClass.setReadOnly(false);
        owBpmGroupAssigneesClass.setIsArray(true);
        owBpmGroupAssigneesClass.setIsRequired(true);
        this.virtualProperties.put(owBpmGroupAssigneesClass.getClassName(), owBpmGroupAssigneesClass);
    }

    public static OwAlfrescoBPMObjectClass forTask(OwAlfrescoBPMRepository bpmRepository, TaskInstance taskInstance) throws OwServerException
    {
        String taskClassName = "D:" + taskInstance.getFormResourceKey();
        try
        {
            OwNetwork<?> network = bpmRepository.getNetwork();
            OwObjectClass nativeClass = network.getObjectClass(taskClassName, null);

            OwAlfrescoBPMWorkItemObjectClass objectClass;
            if (Boolean.getBoolean("owd.adp.alfresco.bpm.checkFormModelForTask") || System.getProperty("owd.adp.alfresco.bpm.checkFormModelForTask") == null)
            {
                objectClass = new OwAlfrescoBPMWorkItemTaskFormObjectClass(nativeClass, bpmRepository, taskInstance.getId());
            }
            else
            {
                objectClass = new OwAlfrescoBPMWorkItemObjectClass(nativeClass);
            }
            return objectClass;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not get class " + taskClassName + " for task instance!", e);
        }
    }
}
