package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Container for all launching work items.
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
public class OwDummyLaunchWorkitemContainer extends OwDummyWorkitemContainer
{

    /**
     *A launching work item. 
     */
    public class OwDummyLaunchWorkitem extends OwDummyWorkitem
    {

        public OwDummyLaunchWorkitem(OwDummyWorkitemRepository repository_p, File file_p, OwDummyWorkitemContainer container_p) throws Exception
        {
            super(repository_p, file_p, container_p);
        }

        protected void loadProperties() throws Exception
        {
            super.loadProperties();
            m_PropertyMap.put(OwDummyWorkitemFileObjectClass.STEP_NAME_PROPERTY, new OwStandardProperty("Launch Step", getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.STEP_NAME_PROPERTY)));

            m_PropertyMap.remove(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_1);
            m_PropertyMap.remove(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_3);

        }

        public void setInitialAttachments(Collection attachmentObjects_p) throws Exception
        {
            Collection initialAttachments = null;
            if (attachmentObjects_p != null)
            {
                initialAttachments = attachmentObjects_p;
            }
            else
            {
                initialAttachments = new ArrayList();

            }
            OwStandardProperty initialTextAttachemntsProperty = new OwStandardProperty(initialAttachments.toArray(), getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.INITIAL_ATTACHMENTS_PROPERTY));
            OwStandardProperty attachemnt4Property = new OwStandardProperty(new Object[] {}, getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_4));
            OwStandardProperty attachemnt2Property = new OwStandardProperty(null, getObjectClass().getPropertyClass(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_2));
            m_PropertyMap.put(OwDummyWorkitemFileObjectClass.INITIAL_ATTACHMENTS_PROPERTY, initialTextAttachemntsProperty);
            m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_4, attachemnt4Property);
            m_PropertyMap.put(OwDummyWorkitemFileObjectClass.ATTACHMENT_PROPERTY_2, attachemnt2Property);
        }

        public boolean canResponse()
        {
            return false;
        }

        public String getResponse() throws Exception
        {
            return null;
        }

        public Collection getResponses() throws Exception
        {
            return null;
        }

        public void setResponse(String strResponse_p) throws Exception
        {

        }

        public boolean canForcedelete(int context_p) throws Exception
        {
            return false;
        }

        public boolean canReassignToPublicContainer(int context_p) throws Exception
        {
            return false;
        }

        public boolean canReassignToUserContainer(int context_p) throws Exception
        {
            return false;
        }

        public boolean canResubmit(int context_p) throws Exception
        {
            return false;
        }

        public boolean canReturnToSource(int context_p) throws Exception
        {
            return false;
        }

        public void forcedelete() throws Exception
        {

        }

        public Date getResubmitDate(int context_p) throws Exception
        {
            return null;
        }

        public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
        {

        }

        public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
        {

        }

        public void resubmit(Date date_p) throws Exception
        {

        }

        public void returnToSource() throws Exception
        {

        }

        public boolean canDelete(int context_p) throws Exception
        {
            return false;
        }

        public boolean canLock() throws Exception
        {
            return false;
        }

        public void delete() throws Exception
        {
            // TODO Auto-generated method stub

        }

        public boolean getLock(int context_p) throws Exception
        {
            return true;
        }

        public String getLockUserID(int context_p) throws Exception
        {
            OwDummyNetwork newtwork = m_repository.getNetwork();
            OwNetworkContext context = newtwork.getContext();

            OwBaseUserInfo currentUser = context.getCurrentUser();
            return currentUser.getUserName();
        }

        public boolean getMyLock(int context_p) throws Exception
        {
            return false;
        }

        public boolean setLock(boolean lock_p) throws Exception
        {
            return false;
        }

        public String getMIMEType() throws Exception
        {
            return "ow_workitem/item";
        }

        public int getType()
        {
            return OBJECT_TYPE_WORKITEM;
        }

        protected boolean isAutoSave()
        {
            return false;
        }

        public void dispatch() throws Exception
        {

        }

        public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
        {
            return new OwDummyWorkitemProcessorInfo("demo/OwBPMLaunchDemoDummy.jsp");
        }

    }

    private Collection m_attachementObjects;

    public OwDummyLaunchWorkitemContainer(OwDummyWorkitemRepository repository_p, File file_p, String mimeType_p, int type_p, Collection attachementObjects_p) throws Exception
    {
        super(repository_p, file_p, mimeType_p, type_p);

        this.m_attachementObjects = attachementObjects_p;
    }

    /**
     * 
     * @param file_p
     * @return a launching {@link OwDummyLaunchWorkitem} object
     * @throws Exception
     */
    protected OwFileObject createFileObject(File file_p) throws Exception
    {
        OwDummyLaunchWorkitem dummyLaunchItem = new OwDummyLaunchWorkitem(m_repository, file_p, this);
        dummyLaunchItem.setInitialAttachments(m_attachementObjects);
        return dummyLaunchItem;
    }

}