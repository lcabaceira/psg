package com.wewebu.ow.server.ecm.bpm;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Base interface for Workitem containers, or BPM Queues.<br/><br/>
 * To be implemented with the specific BPM system.
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
public interface OwWorkitem extends OwObject
{
    /** get a step processor info class for the work item
     * @return OwWorkitemProcessorInfo
     */
    public abstract OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException;

    /** set a response
     */
    public abstract void setResponse(String strResponse_p) throws Exception;

    /** check if you can set a response
     * 
     * @return boolean 
     */
    public abstract boolean canResponse();

    /** get a response
     */
    public abstract String getResponse() throws Exception;

    /** get a list of possible responses
     * @return Collection of OwEnum
     */
    public abstract java.util.Collection getResponses() throws Exception;

    /** delete object and all references, regardless of any state
     */
    public abstract void forcedelete() throws Exception;

    /** check if deletion of object and all references, regardless of any state is possible / allowed
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return boolean 
     */
    public abstract boolean canForcedelete(int iContext_p) throws Exception;

    /** dispatch the workitem, move it to the next public queue
     */
    public abstract void dispatch() throws Exception;

    /** check if you can dispatch the workitem, move it to the next public queue
     * @param iContext_p OwStatusContextDefinitions
     */
    public abstract boolean canDispatch(int iContext_p) throws Exception;

    /** return the work item to the source from where it has been moved
     */
    public abstract void returnToSource() throws Exception;

    /** check if you can return the work item to the source from where it has been moved
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return boolean 
     */
    public abstract boolean canReturnToSource(int iContext_p) throws Exception;

    /**
     * reassign the workitem.
     * 
     * @param containername_p String 
     * @param delegateFlag_p A boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public abstract void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception;

    /**
     * reassign the workitem.
     * 
     * @param participant_p String 
     * @param delegateFlag_p A boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public abstract void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception;

    /** check if you can reassign the work item
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return boolean 
     */
    public abstract boolean canReassignToPublicContainer(int iContext_p) throws Exception;

    /** check if you can reassign the work item
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return boolean 
     */
    public abstract boolean canReassignToUserContainer(int iContext_p) throws Exception;

    /** resubmit the work item to the given date
     * @param date_p the date to resubmit, or null to clear resubmission
     */
    public abstract void resubmit(java.util.Date date_p) throws Exception;

    /** check if you can resubmit the work item to
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return boolean 
     */
    public abstract boolean canResubmit(int iContext_p) throws Exception;

    /** get the currently set resubmit date
     * @param iContext_p OwStatusContextDefinitions
     * 
     * @return Date or null if no resubmission is active 
     */
    public abstract java.util.Date getResubmitDate(int iContext_p) throws Exception;
}