package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyFileObject.OwDummyFilePropertyClass;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwStandardEnum;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation for the dummy BPM Repository.
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
public class OwDummyWorkitem extends OwFileObject implements OwWorkitem
{
    //private static final Logger LOG = OwLog.getLogger(OwDummyWorkitem.class);

    protected class OwDummyWorkitemProcessorInfo implements OwWorkitemProcessorInfo
    {
        private String m_jspFormPage = "demo/OwBPMDemo.jsp";

        public OwDummyWorkitemProcessorInfo()
        {

        }

        public OwDummyWorkitemProcessorInfo(String formPage_p)
        {
            super();
            m_jspFormPage = formPage_p;
        }

        public String getJspFormPage()
        {
            return m_jspFormPage;
        }

        public int getType()
        {
            return STEPPROCESSOR_TYPE_JSP_FORM;
        }

        public int getContextType()
        {

            return 0;
        }

        public String getDisplayName(Locale locale_p)
        {

            return null;
        }

        public int getHeight()
        {

            return 0;
        }

        public String getID()
        {

            return null;
        }

        public Object getNativeProcessor()
        {

            return null;
        }

        public String getScript() throws Exception
        {

            return null;
        }

        public String getURL() throws Exception
        {

            return null;
        }

        public int getWidth()
        {

            return 0;
        }

    }

    /**
     *<p>
     * Property class definition for the OwStandardProperty properties.
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
    protected static class OwDummyWorkitemPropertyClass extends OwStandardPropertyClass//implements OwPropertyClass
    {
        public OwDummyWorkitemPropertyClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, boolean fRequired_p, Collection operators_p)
        {
            m_strClassName = strClassName_p;
            m_strJavaClassName = strJavaClassName_p;
            m_DisplayName = displayName_p;
            m_fSystem = fSystem_p;
            m_fReadOnly[CONTEXT_NORMAL] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CREATE] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = fReadOnly_p;
            m_fName = fName_p;
            m_fRequired = fRequired_p;
            m_operators = operators_p;
        }
    };

    /**
     *<p>
     * File documents class definition for the OwFileDocument Object.
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
    public static class OwDummyWorkitemFileObjectClass extends OwFileObjectClass
    {

        public static final String SUBJECT_PROPERTY = "F_Subject";
        public static final String STEP_NAME_PROPERTY = "F_StepName";
        public static final String TIME_PROPERTY = "F_EnqueueTime";
        public static final String CREATE_TIME_PROPERTY = "F_CreateTime";
        public static final String COMMENT_PROPERTY = "F_Comment";
        public static final String ATTACHMENT_PROPERTY_1 = "Attachment_1";
        public static final String ATTACHMENT_PROPERTY_2 = "Attachment_2";
        public static final String ATTACHMENT_PROPERTY_3 = "Attachment_3";
        public static final String ATTACHMENT_PROPERTY_4 = "Attachment_4";
        public static final String INITIAL_ATTACHMENTS_PROPERTY = "Attachments_Initial";

        public Collection getFilterProperties(Collection propertynames_p) throws Exception
        {
            Collection ret = new Vector();

            ret.add(m_PropertyClassesMap.get(SUBJECT_PROPERTY));
            ret.add(m_PropertyClassesMap.get(CREATE_TIME_PROPERTY));
            ret.add(m_PropertyClassesMap.get(TIME_PROPERTY));

            return ret;
        }

        /** construct PropertyClass Object and set Property classes */
        public OwDummyWorkitemFileObjectClass()
        {
            super(false);

            // === create default file property classes
            m_PropertyClassesMap.put(SUBJECT_PROPERTY, new OwDummyFilePropertyClass(SUBJECT_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyWorkitemFileObjectClass.subject", "Subject"), false, true, true,
                    OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(STEP_NAME_PROPERTY, new OwDummyFilePropertyClass(STEP_NAME_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyWorkitemFileObjectClass.stepname", "Step Name"), false, true, false,
                    OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(TIME_PROPERTY, new OwDummyFilePropertyClass(TIME_PROPERTY, "java.util.Date", new OwString("owdummy.OwDummyWorkitemFileObjectClass.steptime", "Timestamp"), false, true, false,
                    OwDummyFileObject.m_operators.m_datenumoperators));
            m_PropertyClassesMap.put(CREATE_TIME_PROPERTY, new OwDummyFilePropertyClass(CREATE_TIME_PROPERTY, "java.util.Date", new OwString("owdummy.OwDummyWorkitemFileObjectClass.createtime", "Date created"), false, true, false,
                    OwDummyFileObject.m_operators.m_datenumoperators));
            m_PropertyClassesMap.put(COMMENT_PROPERTY, new OwDummyFilePropertyClass(COMMENT_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyWorkitemFileObjectClass.comment", "Comment"), false, false, false,
                    OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(ATTACHMENT_PROPERTY_1, new OwDummyFilePropertyClass(ATTACHMENT_PROPERTY_1, "com.wewebu.ow.server.ecm.OwObject", new OwString("owdummy.OwDummyWorkitemFileObjectClass.attachment1", "Text Attachment"), false, false,
                    false, OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(ATTACHMENT_PROPERTY_2, new OwDummyFilePropertyClass(ATTACHMENT_PROPERTY_2, "com.wewebu.ow.server.ecm.OwObject", new OwString("owdummy.OwDummyWorkitemFileObjectClass.attachment2", "Folder Attachment"), false,
                    false, false, OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(ATTACHMENT_PROPERTY_3, new OwDummyFilePropertyClass(ATTACHMENT_PROPERTY_3, "com.wewebu.ow.server.ecm.OwObject", new OwString("owdummy.OwDummyWorkitemFileObjectClass.attachment3", "Text Attachment read-only"),
                    false, true, false, OwDummyFileObject.m_operators.m_otheroperators));
            m_PropertyClassesMap.put(ATTACHMENT_PROPERTY_4, new OwDummyFileObject.OwFilePropertyArrayClass(ATTACHMENT_PROPERTY_4, "com.wewebu.ow.server.ecm.OwObject", new OwString("owdummy.OwDummyWorkitemFileObjectClass.attachment4",
                    "Multiple Text Attachment"), false, false, true, null, OwDummyFileObject.m_operators.m_otheroperators));

            m_PropertyClassesMap.put(INITIAL_ATTACHMENTS_PROPERTY, new OwDummyFileObject.OwFilePropertyArrayClass(INITIAL_ATTACHMENTS_PROPERTY, "com.wewebu.ow.server.ecm.OwObject", new OwString(
                    "owdummy.OwDummyWorkitemFileObjectClass.initialattachments", "Initial attachments"), false, false, true, null, OwDummyFileObject.m_operators.m_otheroperators));

        }
    }

    /** extension for the persistent properties for this object */
    private static final String PROPERTIES_EXTENSION = ".properties";

    private boolean m_fPropertiesLoaded;

    /** load all properties of the file. There are so little, we just load all at once
     */
    protected void loadProperties() throws Exception
    {
        if (m_fPropertiesLoaded)
        {
            return;
        }

        m_fPropertiesLoaded = true;

        // === load defaults
        super.loadProperties();

        // === load additional dummy properties
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.SUBJECT_PROPERTY, new OwStandardProperty(this.getName(), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.SUBJECT_PROPERTY)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.STEP_NAME_PROPERTY, new OwStandardProperty("Demo Step", getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.STEP_NAME_PROPERTY)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.TIME_PROPERTY, new OwStandardProperty(new Date(), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.TIME_PROPERTY)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.CREATE_TIME_PROPERTY, new OwStandardProperty(new Date(), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.CREATE_TIME_PROPERTY)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.COMMENT_PROPERTY, new OwStandardProperty("Comment...", getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.COMMENT_PROPERTY)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_1,
                new OwStandardProperty(this.getNetwork().getObjectFromPath("/Baufinanzierung.txt", false), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_1)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_2, new OwStandardProperty(this.getNetwork().getObjectFromPath("/", false), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_2)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_3,
                new OwStandardProperty(this.getNetwork().getObjectFromPath("/Depotverwaltung.txt", false), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_3)));
        m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_4, new OwStandardProperty(this.getNetwork().getApplicationObjects(OwNetwork.APPLICATION_OBJECT_TYPE_XML_DOCUMENT, "owsearchtemplates", false).toArray(), getObjectClass()
                .getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_4)));
    }

    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_WorkitemClassDescription = new OwDummyWorkitemFileObjectClass();

    /** get the class description of the object, the class descriptions are defined by the DMS System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        return m_WorkitemClassDescription;
    }

    /** get the class description of the object, the class descriptions are defined by the DMS System
     * @return class description name of object class
     */
    public static OwObjectClass getStaticObjectClass()
    {
        return m_WorkitemClassDescription;
    }

    private OwDummyWorkitemContainer m_container;

    public OwDummyWorkitem(OwDummyWorkitemRepository repository_p, File file_p, OwDummyWorkitemContainer container_p) throws Exception
    {
        super(repository_p.getNetwork(), file_p);

        m_container = container_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getType()
     */
    public int getType()
    {
        switch (m_container.getType())
        {
            case OBJECT_TYPE_ROSTER_FOLDER:
                return OwObjectReference.OBJECT_TYPE_ROSTERITEM;

            case OBJECT_TYPE_PROXY_QUEUE_FOLDER:
                return OwObjectReference.OBJECT_TYPE_WORKITEM_PROXY;

            case OBJECT_TYPE_TRACKER_QUEUE_FOLDER:
                return OwObjectReference.OBJECT_TYPE_WORKITEM_TRACKER;

            default:
                return OwObjectReference.OBJECT_TYPE_WORKITEM;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        switch (m_container.getType())
        {
            case OBJECT_TYPE_ROSTER_FOLDER:
                return "ow_workitem/roster";

            case OBJECT_TYPE_PROXY_QUEUE_FOLDER:
                return "ow_workitem/proxy";

            case OBJECT_TYPE_TRACKER_QUEUE_FOLDER:
                return "ow_workitem/tracker";

            default:
                return "ow_workitem/item";
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getLock(int)
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return (null != getLockUserID(iContext_p));
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getLockUserID(int)
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return (String) getPersistentProperties().get("LockUser");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getMyLock(int)
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return (((OwDummyNetwork) getNetwork()).getContext().getCurrentUser().getUserID().equals(getLockUserID(iContext_p)));
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#setLock(boolean)
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        String sLockUser = null;

        if (fLock_p)
        {
            sLockUser = ((OwDummyNetwork) getNetwork()).getContext().getCurrentUser().getUserID();
        }
        else
        {
            sLockUser = null;
        }

        if (null != sLockUser)
        {
            getPersistentProperties().put("LockUser", sLockUser);
        }
        else
        {
            getPersistentProperties().remove("LockUser");
        }

        return getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    }

    /** check if this object is a properties file to another object
     * 
     * @return a <code>boolean</code>
     */
    public boolean isPropertiesObject()
    {
        return m_File.getAbsolutePath().endsWith(PROPERTIES_EXTENSION);
    }

    /** application context key, where the attribute bags are stored */
    private static final String PROPS_APPLICATION_KEY = "OwDummyWorkItemProperties";

    /** get the map containing the attribute bags
     * 
     * @return Map
     */
    private Map getPersistentProperties()
    {
        String key = m_File.getAbsolutePath() + PROPS_APPLICATION_KEY;
        Map ret = (Map) ((OwDummyNetwork) getNetwork()).getContext().getApplicationAttribute(key);

        if (null == ret)
        {
            ret = new HashMap();
            ((OwDummyNetwork) getNetwork()).getContext().setApplicationAttribute(key, ret);
        }

        return ret;
    }

    private String m_sResponse;

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getDMSID()
     */
    public String getDMSID() throws Exception
    {
        return m_File.getCanonicalPath();
    }

    public boolean canDispatch(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canForcedelete(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canReassignToPublicContainer(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canReassignToUserContainer(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canResponse()
    {
        return true;
    }

    public boolean canResubmit(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canReturnToSource(int iContext_p) throws Exception
    {
        return true;
    }

    public void dispatch() throws Exception
    {
        if (m_sResponse == null)
        {
            throw new OwInvalidOperationException(OwString.localize(Locale.ENGLISH, "plug.owbpm.OwBPMStandardProcessorView.errorresponseselect", "Please select a response."));
        }

    }

    public void forcedelete() throws Exception
    {

    }

    public String getResponse() throws Exception
    {
        return m_sResponse;
    }

    public Collection getResponses() throws Exception
    {
        Collection ret = new Vector();

        ret.add(new OwStandardEnum("OK", "OK"));
        ret.add(new OwStandardEnum("Not OK", "Not OK"));

        return ret;
    }

    private Date m_resubmitdate = new Date();

    public Date getResubmitDate(int iContext_p) throws Exception
    {
        return m_resubmitdate;
    }

    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        return new OwDummyWorkitemProcessorInfo();
    }

    public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
    {

    }

    public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
    {

    }

    public void resubmit(Date date_p) throws Exception
    {
        m_resubmitdate = date_p;
    }

    public void returnToSource() throws Exception
    {

    }

    public void setResponse(String strResponse_p) throws Exception
    {
        m_sResponse = strResponse_p;
    }
}
