package com.wewebu.ow.server.ecm.bpm;

import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * Base interface for case objects.
 * Case objects allow the adapter to find corresponding workflows to a OwObject.<br/><br/>
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
public interface OwCaseObject
{
    /** get the number of corresponding workitems to the case object
    * usually, case objects have one work item
    * @return int
    * */
    public abstract int getWorkitemCount() throws Exception;

    /** get the corresponding work item reference for the case object
    * @param iIndex_p int index of the requested workitem, (usually 0)
    * @return OwWorkitem or throws OwObjectNotFoundException
    * */
    public abstract OwObjectReference getWorkitem(int iIndex_p) throws Exception;
}