package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.HashMap;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses.OwFNBPM5WorkflowPropertyClass;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWExposedFieldDefinition;
import filenet.vw.api.VWFieldDefinition;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWModeType;
import filenet.vw.api.VWWorkflowDefinition;

/**
 *<p>
 * FileNet BPM Repository. Standard implementation of the work item class description.<br/>
 * A single workitem.
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
public class OwFNBPM5WorkItemObjectClass extends OwStandardObjectClass
{
    /** the size for F_Comment property*/
    private static final int COMMENT_SIZE = 4000;

    /** property class name of the property used to name the work item */
    public static final String NAME_PROPERTY = "F_Subject";

    /** property class name for a system field, that is created internally and is not based upon a PE table attribute */
    public static final String STEP_NAME_PROPERTY = "F_StepName";
    /** property class name for a system field, that is created internally and is not based upon a PE table attribute */
    public static final String COMMENT_PROPERTY = "F_Comment";
    /** property class name for a system field, that is created internally and is not based upon a PE table attribute */
    public static final String QUEUE_NAME_PROPERTY = "F_QueueName";
    /** property class name for a system field, that is created internally and is not based upon a PE table attribute */
    public static final String DESCRIPTION_PROPERTY = "F_Description";
    /** property class name for a system field, that is created internally and is not based upon a PE table attribute */
    public static final String STEP_PROCESSOR_ID = "F_StepProcId";

    public static final HashMap<String, OwPropertyClass> BASIC_PROPS = new HashMap<String, OwPropertyClass>();

    /** init object class description 
     * @throws Exception */
    public OwFNBPM5WorkItemObjectClass() throws Exception
    {
        m_fCanCreateNewObject = false;
        m_fHidden = true;
        m_fVersionable = false;
        m_iType = OwObjectReference.OBJECT_TYPE_WORKITEM;
        m_parent = null;
        m_strClassName = "OwFNBPMWorkItem";
        m_Description = new OwString("owfnbpm.OwFNBPMWorkItemObjectClass.description", "Class description for FileNet BPM work items");
        m_DisplayName = new OwString("owfnbpm.OwFNBPMWorkItemObjectClass.displayname", "FileNet BPM workitem");
        m_strNamePropertyName = NAME_PROPERTY;

        m_PropertyClassesMap = new HashMap<String, OwPropertyClass>();
    }

    /**
     * init class definition from given native workflow definition
     * @param def_p VWWorkflowDefinition native structure definition object
     * @param repo_p OwFNBPMRespository to be used for PE to CE field mapping
     * @throws Exception
     */
    public OwFNBPM5WorkItemObjectClass(VWWorkflowDefinition def_p, OwFNBPM5Repository repo_p) throws Exception
    {
        this();
        m_DisplayName = new OwString(def_p.getName());
        m_Description = new OwString(new StringBuilder(OwString.LABEL_PREFIX).append(def_p.getName()).append(".description").toString(), def_p.getDescription());
        m_strClassName = def_p.getName();
        VWFieldDefinition[] arr = def_p.getFields();
        String resource = repo_p.getConfigNode().getSafeTextValue("DefaultObjectStoreLookup", null);
        for (int i = 0; i < arr.length; i++)
        {
            addPropertyClass(arr[i], repo_p, resource);
            //            addPropertyClass(field.getName(), new OwString(field.getName()), javaClass, -1, field.getFieldType(), field.getCanAssign(), false, null);
        }
    }

    private void addPropertyClass(VWFieldDefinition field_p, OwFNBPM5Repository repo_p, String resource_p) throws Exception
    {
        OwFieldDefinition def = null;
        if (field_p.getFieldType() != VWFieldType.FIELD_TYPE_ATTACHMENT && field_p.getFieldType() != VWFieldType.FIELD_TYPE_PARTICIPANT && resource_p != null)
        {
            try
            {
                //request from CE == repo.getNetwork()
                def = repo_p.getNetwork().getFieldDefinition(field_p.getName(), resource_p);
            }
            catch (OwObjectNotFoundException e)
            {
                //no problem we don't have a representation in CE for that field
            }
        }
        m_PropertyClassesMap.put(field_p.getName(), new OwFNBPM5WorkflowPropertyClass(field_p, def));
    }

    /** add a new system property class description
     * @param definition_p VWExposedFieldDefinition to be add to properties map 
     * @throws Exception */
    public void addPropertyClass(VWExposedFieldDefinition definition_p) throws Exception
    {
        OwPropertyClass propclass = createPropertyClass(definition_p, null);
        m_PropertyClassesMap.put(propclass.getClassName(), propclass);
    }

    /**
     * create a property class form given native VWExposedFieldDefinition
     * @param definition_p VWExposedFieldDefinition native field definition
     * @param combatiblefield_p OwFieldDefiniton a CE field definition to use for additional rendering, can be null 
     * @return OwPropertyClass for given native field definition
     * @throws Exception
     */
    public static synchronized OwPropertyClass createPropertyClass(VWExposedFieldDefinition definition_p, OwFieldDefinition combatiblefield_p) throws Exception
    {
        if (OwFNBPM5BaseContainer.getUserProperties().contains(definition_p.getName()))
        {
            return new OwFNBPM5UserWorkItemPropertyClass(definition_p, combatiblefield_p);
        }
        else
        {
            OwPropertyClass propClass = getBasicProperties().get(definition_p.getName());
            if (propClass == null)
            {
                propClass = new OwFNBPM5StandardWorkItemPropertyClass(definition_p, combatiblefield_p);
            }
            return propClass;
        }
    }

    /** create a property class
     * 
     * @param strName_p String PropertyClass name
     * @param iVWFieldType_p int representing the native field type
     * @param iMode_p int native mode of field (IN, OUT, IN_OUT)
     * @param fIsSystem_p boolean to is field type system
     * @param isArray_p boolean flag for array handling
     * @param fQueueDefinitionField_p boolean is field defined by queue (false for workflow defined field)
     * @param combatiblefield_p a compatible field from DMS system to lookup choicelists
     * @return an {@link OwPropertyClass}
     * @throws Exception
     */
    public static OwPropertyClass createPropertyClass(String strName_p, int iVWFieldType_p, int iMode_p, boolean fIsSystem_p, boolean isArray_p, boolean fQueueDefinitionField_p, OwFieldDefinition combatiblefield_p) throws Exception
    {
        if (OwFNBPM5BaseContainer.getUserProperties().contains(strName_p))
        {
            // === user property class, with integer user info
            return new OwFNBPM5UserWorkItemPropertyClass(strName_p, new OwString(strName_p), isArray_p, iMode_p, fIsSystem_p, iVWFieldType_p, fQueueDefinitionField_p, null, combatiblefield_p);
        }
        else
        {
            // === normal property class
            String strJavaClassName = OwFNBPM5StandardWorkItemPropertyClass.getJavaClassName(iVWFieldType_p);

            if (OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME.equals(strName_p))
            {
                fIsSystem_p = true;
            }

            return new OwFNBPM5StandardWorkItemPropertyClass(strName_p, new OwString(strName_p), strJavaClassName, isArray_p, iMode_p, fIsSystem_p, iVWFieldType_p, fQueueDefinitionField_p, null, combatiblefield_p);
        }
    }

    public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
    {
        if (getBasicProperties().containsKey(strClassName_p))
        {
            return getBasicProperties().get(strClassName_p);
        }
        return super.getPropertyClass(strClassName_p);
    }

    /**
     * Helper Method to lazy initialize a basic map (cache) of 
     * general used property classes.
     * @return HashMap of name to property class
     * @throws Exception
     */
    protected static synchronized HashMap<String, OwPropertyClass> getBasicProperties() throws Exception
    {
        if (BASIC_PROPS.isEmpty())
        {
            BASIC_PROPS.put(NAME_PROPERTY, new OwFNBPM5StandardWorkItemPropertyClass(NAME_PROPERTY, new OwString("owlabel.F_Subject", "Title"), "java.lang.String", false, VWModeType.MODE_TYPE_IN_OUT, false, VWFieldType.FIELD_TYPE_STRING, false,
                    Integer.valueOf(128), null));
            BASIC_PROPS.put(STEP_NAME_PROPERTY, new OwFNBPM5StandardWorkItemPropertyClass(STEP_NAME_PROPERTY, new OwString("owlabel.F_StepName", "Step Name"), "java.lang.String", false, VWModeType.MODE_TYPE_IN, false, VWFieldType.FIELD_TYPE_STRING,
                    true, Integer.valueOf(64), null));
            BASIC_PROPS.put(COMMENT_PROPERTY, new OwFNBPM5StandardWorkItemPropertyClass(COMMENT_PROPERTY, new OwString("owlabel.F_Comment", "Notation for processing"), "java.lang.String", false, VWModeType.MODE_TYPE_IN_OUT, false,
                    VWFieldType.FIELD_TYPE_STRING, false, Integer.valueOf(COMMENT_SIZE), null));
            BASIC_PROPS.put(QUEUE_NAME_PROPERTY, new OwFNBPM5StandardWorkItemPropertyClass(QUEUE_NAME_PROPERTY, new OwString("owlabel.F_QueueName", "Name of Queue"), "java.lang.String", false, VWModeType.MODE_TYPE_IN, false,
                    VWFieldType.FIELD_TYPE_STRING, false, Integer.valueOf(64), null));
            BASIC_PROPS.put(DESCRIPTION_PROPERTY, new OwFNBPM5StandardWorkItemPropertyClass(DESCRIPTION_PROPERTY, new OwString("owlabel.F_Description", "Instruction"), "java.lang.String", false, VWModeType.MODE_TYPE_IN, false,
                    VWFieldType.FIELD_TYPE_STRING, false, Integer.valueOf(1024), null));
            BASIC_PROPS.put(STEP_PROCESSOR_ID, new OwFNBPM5StandardWorkItemPropertyClass(STEP_PROCESSOR_ID, new OwString("owlabel.F_StepProcId", "Step Processor ID"), "java.lang.Integer", false, VWModeType.MODE_TYPE_IN, false,
                    VWFieldType.FIELD_TYPE_INT, true, Integer.valueOf(64), null));

            BASIC_PROPS.put("F_LockUser", createPropertyClass("F_LockUser", new OwString("owlabel.F_LockUser", "Locked User"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_LockTime", createPropertyClass("F_LockTime", new OwString("owlabel.F_LockTime", "Time Locked"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_BindPending", createPropertyClass("F_BindPending", new OwString("owlabel.F_BindPending", "Locked User"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_BoundUser", createPropertyClass("F_BoundUser", new OwString("owlabel.F_BoundUser", "Bound User"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_BoundMachine", createPropertyClass("F_BoundMachine", new OwString("owlabel.F_BoundMachine", "Bound Machine"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_Tag", createPropertyClass("F_Tag", new OwString("owlabel.F_Tag", "Tag"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_UniqueId", createPropertyClass("F_UniqueId", new OwString("owlabel.F_UniqueId", "Unique ID"), VWFieldType.FIELD_TYPE_FLOAT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_OperationId", createPropertyClass("F_OperationId", new OwString("owlabel.F_OperationId", "Operation ID"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WorkClassId", createPropertyClass("F_WorkClassId", new OwString("owlabel.F_WorkClassId", "WorkClass ID"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_QueueWPClassId", createPropertyClass("F_QueueWPClassId", new OwString("owlabel.F_QueueWPClassId", "QueueWPClassID"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_EnqueueTime", createPropertyClass("F_EnqueueTime", new OwString("owlabel.F_EnqueueTime", "Enqueue Time"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_CreateTime", createPropertyClass("F_CreateTime", new OwString("owlabel.F_CreateTime", "Create Time"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_InstrSheetId", createPropertyClass("F_InstrSheetId", new OwString("owlabel.F_InstrSheetId", "Instr. Sheet ID"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WorkOrderId", createPropertyClass("F_WorkOrderId", new OwString("owlabel.F_WorkOrderId", "Work Order ID"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Class", createPropertyClass("F_Class", new OwString("owlabel.F_Class", "Class"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_Operation", createPropertyClass("F_Operation", new OwString("owlabel.F_Operation", "Operation"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WorkFlowNumber", createPropertyClass("F_WorkFlowNumber", new OwString("owlabel.F_WorkFlowNumber", "Workflow Number"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Originator", createPropertyClass("F_Originator", new OwString("owlabel.F_Originator", "Originator"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_ParentWobNum", createPropertyClass("F_ParentWobNum", new OwString("owlabel.F_ParentWobNum", "Parent WobNum"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_StartTime", createPropertyClass("F_StartTime", new OwString("owlabel.F_StartTime", "Start time"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_TimeOut", createPropertyClass("F_TimeOut", new OwString("owlabel.F_TimeOut", "Timeout"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_SplitLeg", createPropertyClass("F_SplitLeg", new OwString("owlabel.F_SplitLeg", "SplitLeg"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_TrackerStatus", createPropertyClass("F_TrackerStatus", new OwString("owlabel.F_TrackerStatus", "Tracker Status"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Overdue", createPropertyClass("F_Overdue", new OwString("owlabel.F_Overdue", "Overdue"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Response", createPropertyClass("F_Response", new OwString("owlabel.F_Response", "Response"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_TransferUser", createPropertyClass("F_TransferUser", new OwString("owlabel.F_TransferUser", "Transfer User"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_MinMilestone", createPropertyClass("F_MinMilestone", new OwString("owlabel.F_MinMilestone", "Min. Milestone"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_MaxMilestone", createPropertyClass("F_MaxMilestone", new OwString("owlabel.F_MaxMilestone", "Max. Milestone"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Occurrence", createPropertyClass("F_Occurrence", new OwString("owlabel.F_Occurrence", "Occurrence"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_StepDescription", createPropertyClass("F_StepDescription", new OwString("owlabel.F_StepDescription", "Step Description"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_LastErrorNumber", createPropertyClass("F_LastErrorNumber", new OwString("owlabel.F_LastErrorNumber", "Last Error Number"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_LastErrorText", createPropertyClass("F_LastErrorText", new OwString("owlabel.F_LastErrorText", "Last Error Text"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_InstrSheetName", createPropertyClass("F_InstrSheetName", new OwString("owlabel.F_InstrSheetName", "Instr. Sheet Name"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Deadline", createPropertyClass("F_Deadline", new OwString("owlabel.F_Deadline", "Deadline"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_Reminder", createPropertyClass("F_Reminder", new OwString("owlabel.F_Reminder", "Reminder"), VWFieldType.FIELD_TYPE_TIME, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_WsPortType", createPropertyClass("F_WsPortType", new OwString("owlabel.F_WsPortType", "Ws Port Type"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WsOperation", createPropertyClass("F_WsOperation", new OwString("owlabel.F_WsOperation", "Ws Operation"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WsCorrelation", createPropertyClass("F_WsCorrelation", new OwString("owlabel.F_WsCorrelation", "Ws Correlation"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WorkClassRevision", createPropertyClass("F_WorkClassRevision", new OwString("owlabel.F_WorkClassRevision", "Work Class Revision"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WFDeadline", createPropertyClass("F_WFDeadline", new OwString("owlabel.F_WFDeadline", "WF Deadline"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN_OUT, true, false, true));

            BASIC_PROPS.put("F_WFReminder", createPropertyClass("F_WFReminder", new OwString("owlabel.F_WFReminder", "WF Reminder"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN_OUT, true, false, true));
            BASIC_PROPS.put("F_Responses", createPropertyClass("F_Responses", new OwString("owlabel.F_Responses", "Responses"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN_OUT, true, true, true));
            BASIC_PROPS.put("F_ResponseCount", createPropertyClass("F_ResponseCount", new OwString("owlabel.F_ResponseCount", "Response Count"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN_OUT, true, false, true));
            BASIC_PROPS.put("F_SourceDoc", createPropertyClass("F_SourceDoc", new OwString("owlabel.F_SourceDoc", "Source Doc"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN_OUT, true, false, true));
            BASIC_PROPS.put("F_WorkSpaceId", createPropertyClass("F_WorkSpaceId", new OwString("owlabel.F_WorkSpaceId", "WorkSpaceId"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));

            BASIC_PROPS.put("F_Locked", createPropertyClass("F_Locked", new OwString("owlabel.F_Locked", "Locked"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_LockMachine", createPropertyClass("F_LockMachine", new OwString("owlabel.F_LockMachine", "Lock Machine"), VWFieldType.FIELD_TYPE_INT, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_SortOrder", createPropertyClass("F_SortOrder", new OwString("owlabel.F_SortOrder", "Sort Order"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, true));
            BASIC_PROPS.put("F_WobNum", createPropertyClass("F_WobNum", new OwString("owlabel.F_WobNum", "WorkflowNumber"), VWFieldType.FIELD_TYPE_STRING, VWModeType.MODE_TYPE_IN, true, false, false));
            //            BASIC_PROPS.put("F_WobNum", new OwFNBPMStandardWorkItemPropertyClass("F_WobNum", new OwString("owlabel.F_WobNum", "WorkflowNumber"), "java.lang.Integer", false, VWModeType.MODE_TYPE_IN, false,
            //                    VWFieldType.FIELD_TYPE_STRING, true, Integer.valueOf(128), null));
        }
        return BASIC_PROPS;
    }

    private static OwPropertyClass createPropertyClass(String strName_p, OwString localization_p, int iVWFieldType_p, int iMode_p, boolean fIsSystem_p, boolean isArray_p, boolean fQueueDefinitionField_p) throws Exception
    {
        return createPropertyClass(strName_p, localization_p, null, iVWFieldType_p, iMode_p, fIsSystem_p, isArray_p, fQueueDefinitionField_p);
    }

    private static OwPropertyClass createPropertyClass(String strName_p, OwString localization_p, String clazz_p, int iVWFieldType_p, int iMode_p, boolean fIsSystem_p, boolean isArray_p, boolean fQueueDefinitionField_p) throws Exception
    {
        if (OwFNBPM5BaseContainer.getUserProperties().contains(strName_p))
        {
            // === user property class, with integer user info
            return new OwFNBPM5UserWorkItemPropertyClass(strName_p, localization_p, isArray_p, iMode_p, fIsSystem_p, iVWFieldType_p, fQueueDefinitionField_p, null, null);
        }
        if (clazz_p != null)
        {
            return new OwFNBPM5StandardWorkItemPropertyClass(strName_p, localization_p, clazz_p, isArray_p, iMode_p, fIsSystem_p, iVWFieldType_p, false, null, null);
        }
        else
        {
            String clazz = OwFNBPM5StandardWorkItemPropertyClass.getJavaClassName(iVWFieldType_p);
            return new OwFNBPM5StandardWorkItemPropertyClass(strName_p, localization_p, clazz, isArray_p, iMode_p, fIsSystem_p, iVWFieldType_p, false, null, null);
        }
    }
}