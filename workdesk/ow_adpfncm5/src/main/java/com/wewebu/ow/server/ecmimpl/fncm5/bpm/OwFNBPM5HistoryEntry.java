package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardEnumCollection;
import com.wewebu.ow.server.field.OwStandardLocalizeableEnum;
import com.wewebu.ow.server.history.OwHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWException;
import filenet.vw.api.VWLogElement;

/**
 *<p>
 * FileNet BPM History Entry. Wraps a FileNet BPM event log element.
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
public class OwFNBPM5HistoryEntry implements OwHistoryEntry
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5HistoryEntry.class);

    /**
     *<p>
     * Property class description of history entry object properties.
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
    public static class OwFNBPMHistoryEntryPropertyClass extends OwStandardPropertyClass
    {
        /** init property class description */
        public OwFNBPMHistoryEntryPropertyClass(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, boolean fNameProperty_p, OwEnumCollection enums_p)
        {
            this.m_fArray = fArray_p;
            this.m_fName = fNameProperty_p;
            this.m_fRequired = false;
            this.m_fSystem = true;
            this.m_Enums = enums_p;

            for (int i = 0; i < OwPropertyClass.CONTEXT_MAX; i++)
            {
                this.m_fHidden[i] = false;
                this.m_fReadOnly[i] = true;
            }

            this.m_strClassName = strName_p;
            this.m_DisplayName = displayName_p;
            this.m_strJavaClassName = strJavaClassName_p;
        }
    }

    public static final int CUSTOM_EVENT_TYPE_REASSIGN = 9999;

    /**
     *<p>
     * Object class description of history entry.
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
    private static class OwFNBPMHistoryEntryObjectClass extends OwStandardObjectClass
    {
        /** property class name */
        public static final String USERID_PROPERTY = "F_UserId";
        /** property class name */
        public static final String EVENTTYPE_PROPERTY = "F_EventType";
        /** property class name */
        public static final String TIME_STAMP_PROPERTY = "F_TimeStamp";

        /** property class name */
        public static final String STEPNAME_PROPERTY = "F_StepName";
        /** property class name */
        public static final String WOBID_PROPERTY = "F_WorkFlowNumber";
        /** property class name */
        public static final String QUEUE_PROPERTY = "F_Queue";
        /** property class name */
        public static final String WORKFLOWNAME_PROPERTY = "F_WorkflowName";
        /** property class name */
        public static final String TEXT_PROPERTY = "F_Text";
        /** property class name */
        public static final String INSTSHEETNAME_PROPERTY = "F_InstSheetName";
        /** property class name */
        public static final String MAPNAME_PROPERTY = "F_MapName";
        /** property class name */
        public static final String WORKCLASSNAME_PROPERTY = "F_WorkClassName";
        /** property class name */
        public static final String WOBTAG_PROPERTY = "F_Tag";

        /** init object class description */
        private OwFNBPMHistoryEntryObjectClass()
        {
            m_fCanCreateNewObject = false;
            m_fHidden = true;
            m_fVersionable = false;
            m_iType = OwObjectReference.OBJECT_TYPE_HISTORY;
            m_parent = null;
            m_strClassName = "OwFNBPMHistoryEntryObjectClass";
            m_Description = new OwString("fncm.bpm.OwFNBPMHistoryEntryObjectClass.description", "Standard class description for FNBPM History Elements.");
            m_DisplayName = new OwString("fncm.bpm.OwFNBPMHistoryEntryObjectClass.displayname", "FNBPM History Element Class");
            m_strNamePropertyName = "Name";
            m_PropertyClassesMap = new HashMap();

            // === construct the property class descriptions
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.NAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.eventname", "Name"), "java.lang.String", false, true, null);
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype", "Event Type"), "java.lang.Integer", false, false, m_EventtypeEnumCollectionSingleton.getEnum());
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.username", "User Name"), "java.lang.String", false, false, null);
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.timestamp", "Timestamp"), "java.util.Date", false, false, null);
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.description", "Description"), "java.lang.String", false, false, null);
            addPropertyClass(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.modifiedproperties", "Modified Properties"),
                    "com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue", true, false, null);
            addPropertyClass(STEPNAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.stepname", "Workflow Name"), "java.lang.String", false, true, null);
            addPropertyClass(WOBID_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.wobid", "WorkObject ID"), "java.lang.String", false, false, null);
            addPropertyClass(QUEUE_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.queue", "Queue"), "java.lang.String", false, false, null);
            addPropertyClass(WORKFLOWNAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.workflowname", "Workflow Name"), "java.lang.String", false, false, null);
            addPropertyClass(TEXT_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.text", "Text"), "java.lang.String", false, false, null);
            addPropertyClass(INSTSHEETNAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.instsheetname", "Instruction sheet"), "java.lang.String", false, false, null);
            addPropertyClass(MAPNAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.mapname", "Workflow Map"), "java.lang.String", false, false, null);
            addPropertyClass(WORKCLASSNAME_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.workclassname", "WorkClass"), "java.lang.String", false, false, null);
            addPropertyClass(WOBTAG_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.objecttag", "Object Tag"), "java.lang.String", false, false, null);

            addPropertyClass(USERID_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.userid", "User"), "java.lang.String", false, false, null);
            addPropertyClass(EVENTTYPE_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype", "Event Type"), "java.lang.Integer", false, false, m_EventtypeEnumCollectionSingleton.getEnum());
            addPropertyClass(TIME_STAMP_PROPERTY, new OwString("fncm.bpm.OwFNBPMHistorEntry.timestamp", "Timestamp"), "java.util.Date", false, false, null);
        }

        /** add a new property class description */
        private void addPropertyClass(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, boolean fNameProperty_p, OwEnumCollection enums_p)
        {
            m_PropertyClassesMap.put(strName_p, new OwFNBPMHistoryEntryPropertyClass(strName_p, displayName_p, strJavaClassName_p, fArray_p, fNameProperty_p, enums_p));
        }
    }

    /**
     *<p>
     * OwFNBPMEventtypeEnumCollectionSingleton.
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
    private static class OwFNBPMEventtypeEnumCollectionSingleton
    {
        private OwStandardEnumCollection m_eventtypeenum = new OwStandardEnumCollection();

        public OwFNBPMEventtypeEnumCollectionSingleton()
        {
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(100), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.systemoperation", "System Operation")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(110), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.movetoserver", "Move to server")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(120), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.namechanged", "Name changed")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(130), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.childcreation", "Child creation")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(140), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.parentcreation", "Parent creation")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(150), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.childtermination", "Child termination")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(160), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.parenttermination", "Parent termination")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(170), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.exception", "Exception")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(172), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.deadline", "Deadline")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(174), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.reminder", "Reminder")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(180), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.forcedtoskipinstruction", "Forced to skip instruction")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(190), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.forcedtoterminate", "Forced to terminate")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(200), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.forcedtodelete", "Forced to delete")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(230), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.logvectorchanged", "LogVector changed")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(240), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.userlogin", "User log-in")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(250), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.userlogout", "User log-out")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(260), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.starttransfer", "Start transfer")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(270), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endtransfer", "End transfer")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(280), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.startcleanrepository", "Start clean repository")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(290), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endcleanrepository", "End clean repository")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(300), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.startinitregion", "Start init region")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(310), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endinitregion", "End init region")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(320), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.startfullinitialize", "Start full initialize")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(330), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endfullinitialize", "End full initialize")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(340), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.removedatabase", "Remove database")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(350), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.beginservice", "Begin service")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(352), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.workobjectqueued", "WorkObject queued")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(360), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endservicenormal", "End service normally")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(365), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.wobsavewithlock", "WOB save with lock")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(370), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endserviceabnormal", "End service abnormally")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(380), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endservicerelease", "End service release")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(382), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endservicereleasedelegate", "End service release delegate")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(384), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endservicereleasereassign", "End service release reassign")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(386), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endservicereleasereturn", "End service release return")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(390), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.endserviceabort", "Abort service")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(400), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.checkpoint", "Checkpoint")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(410), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user11", "User 1 1")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(420), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user12", "User 1 2")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(430), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user13", "User 1 3")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(440), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user14", "User 1 4")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(450), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user21", "User 2 1")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(460), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user22", "User 2 2")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(470), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user23", "User 2 3")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(480), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.user24", "User 2 4")));
            m_eventtypeenum.add(new OwStandardLocalizeableEnum(Integer.valueOf(CUSTOM_EVENT_TYPE_REASSIGN), new OwString("fncm.bpm.OwFNBPMHistorEntry.eventtype.reassign", "Reassign")));
        }

        public OwStandardEnumCollection getEnum()
        {
            return m_eventtypeenum;
        }
    }

    private static OwFNBPMEventtypeEnumCollectionSingleton m_EventtypeEnumCollectionSingleton = new OwFNBPMEventtypeEnumCollectionSingleton();

    private static OwFNBPMHistoryEntryObjectClass m_ClassDescription = new OwFNBPMHistoryEntryObjectClass();

    /** properties */
    private OwPropertyCollection m_properties = new OwStandardPropertyCollection();

    private VWLogElement m_logelement;

    //	private OwFNBPMHistoryManager m_historymanager;

    public OwFNBPM5HistoryEntry(OwFNBPM5HistoryManager manager_p, VWLogElement logElement_p) throws Exception
    {
        this(manager_p, logElement_p, String.valueOf(logElement_p.getEventType()), null, logElement_p.getEventType());
    }

    public OwFNBPM5HistoryEntry(OwFNBPM5HistoryManager manager_p, VWLogElement currentLE_p, String description_p, Object[] changedFields_p, int iEventType_p) throws VWException, Exception
    {
        m_logelement = currentLE_p;
        //		m_historymanager = manager;

        Object eventType = Integer.valueOf(iEventType_p);

        String name = m_EventtypeEnumCollectionSingleton.getEnum().getDisplayName(manager_p.getContext().getLocale(), eventType);
        String sDescription = name;
        if (null != description_p)
        {
            sDescription += ": ";
            sDescription += description_p;
        }

        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.NAME_PROPERTY, name);
        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, eventType);
        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY, m_logelement.getUserName());
        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY, m_logelement.getTimeStamp());
        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY, sDescription);
        setProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, changedFields_p);
        setProperty(OwFNBPMHistoryEntryObjectClass.STEPNAME_PROPERTY, m_logelement.getStepName());
        setProperty(OwFNBPMHistoryEntryObjectClass.WOBID_PROPERTY, m_logelement.getWorkObjectNumber());
        setProperty(OwFNBPMHistoryEntryObjectClass.QUEUE_PROPERTY, m_logelement.getQueueName());
        setProperty(OwFNBPMHistoryEntryObjectClass.WORKFLOWNAME_PROPERTY, m_logelement.getWorkflowName());
        setProperty(OwFNBPMHistoryEntryObjectClass.TEXT_PROPERTY, m_logelement.getText());
        setProperty(OwFNBPMHistoryEntryObjectClass.INSTSHEETNAME_PROPERTY, m_logelement.getInstructionSheetName());
        setProperty(OwFNBPMHistoryEntryObjectClass.MAPNAME_PROPERTY, m_logelement.getMapName());
        setProperty(OwFNBPMHistoryEntryObjectClass.WORKCLASSNAME_PROPERTY, m_logelement.getWorkClassName());
        setProperty(OwFNBPMHistoryEntryObjectClass.WOBTAG_PROPERTY, m_logelement.getWorkObjectTag());

        setProperty(OwFNBPMHistoryEntryObjectClass.USERID_PROPERTY, m_logelement.getUserName());
        setProperty(OwFNBPMHistoryEntryObjectClass.EVENTTYPE_PROPERTY, Integer.valueOf(m_logelement.getEventType()));
        setProperty(OwFNBPMHistoryEntryObjectClass.TIME_STAMP_PROPERTY, m_logelement.getTimeStamp());
    }

    /** set a property in the history object
     */
    private void setProperty(String strClassName_p, Object value_p) throws Exception
    {
        OwPropertyClass propClass = getObjectClass().getPropertyClass(strClassName_p);
        m_properties.put(propClass.getClassName(), new OwStandardProperty(value_p, propClass));
    }

    public void add(OwObject oObject_p) throws Exception
    {

    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return null;
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canLock() throws Exception
    {
        return false;
    }

    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canUndo() throws Exception
    {
        return false;
    }

    public void delete() throws Exception
    {

    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    public String getClassName()
    {
        return m_ClassDescription.getClassName();
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return null;
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
    }

    public Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    public String getDMSID() throws Exception
    {
        return null;
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT;
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    public boolean getLock(int iContext_p)
    {
        return false;
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p)
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p)
    {
        return null;
    }

    public String getMIMEParameter()
    {
        return "";
    }

    public String getMIMEType()
    {
        return "ow_history/" + m_ClassDescription.getClassName();
    }

    public String getName()
    {
        try
        {
            return (String) getProperty(getObjectClass().getNamePropertyName()).getValue();
        }
        catch (Exception e)
        {
            return "[undef]";
        }
    }

    public Object getNativeObject()
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwObjectClass getObjectClass()
    {
        return m_ClassDescription;
    }

    public static com.wewebu.ow.server.ecm.OwObjectClass getStaticObjectClass()
    {
        return m_ClassDescription;
    }

    public int getPageCount()
    {
        return 0;
    }

    public OwObjectCollection getParents()
    {
        return null;
    }

    public OwPermissionCollection getPermissions()
    {
        return null;
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p)
    {
        return m_properties;
    }

    public OwProperty getProperty(String strPropertyName_p) throws OwObjectNotFoundException
    {
        OwProperty prop = (OwProperty) m_properties.get(strPropertyName_p);
        if (null == prop)
        {
            String msg = "OwFNBPMHistoryEntry.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return prop;
    }

    public OwResource getResource()
    {
        return null;
    }

    public OwSearchTemplate getSearchTemplate()
    {
        return null;
    }

    public String getID()
    {
        return null;
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_HISTORY;
    }

    public OwVersion getVersion()
    {
        return null;
    }

    public OwVersionSeries getVersionSeries()
    {
        return null;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p)
    {
        return false;
    }

    public boolean hasVersionSeries()
    {
        return false;
    }

    public void move(OwObject oObject_p, OwObject oldParent_p)
    {
    }

    public void refreshProperties()
    {
    }

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     */
    public void refreshProperties(Collection props_p)
    {
        refreshProperties();
    }

    public void removeReference(OwObject oObject_p)
    {
    }

    public void setContentCollection(OwContentCollection content_p)
    {
    }

    public boolean setLock(boolean fLock_p)
    {
        return false;
    }

    public void setPermissions(OwPermissionCollection permissions_p)
    {
    }

    public void setProperties(OwPropertyCollection properties_p)
    {
    }

    public void undo()
    {
    }

    /** check if object has children
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p)
    {
        return false;
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the dmsid, the path is not necessarily unique,
     * but provids a readable information of the objects location.
     * @throws OwNotSupportedException 
     */
    public String getPath() throws OwNotSupportedException
    {
        throw new OwNotSupportedException("OwFNBPMHistoryEntry.getPath: Not implemented.");
    }

    /** get the number of children
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return <code>int</code> number of children or throws OwStatusContextException
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p)
    {
        return 0;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p <code>String</code> 
     * @param properties_p {@link OwPropertyCollection} (optional, can be null to set previous properties)
     * @param permissions_p {@link OwPermissionCollection}  (optional, can be null to set previous permissions)
     * @throws OwInvalidOperationException 
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("OwFNBPMHistoryEntry.changeClass: Not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass()
    {
        return false;
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws OwException
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws OwException
    {
        try
        {
            getProperty(sName_p).setValue(value_p);
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not set field value.", e);
        }
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     */
    public OwObject getInstance()
    {
        return this;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        try
        {
            return getResource().getID();
        }
        catch (NullPointerException e)
        {
            throw new OwObjectNotFoundException("OwFNBPMHistoryEntry.getResourceID: Resource Id not found for DMSID = " + getDMSID(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p)
    {
        setProperties(properties_p);
    }

}
