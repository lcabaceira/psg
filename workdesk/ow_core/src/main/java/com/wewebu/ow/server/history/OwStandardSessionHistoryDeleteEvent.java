package com.wewebu.ow.server.history;

/**
 *<p>
 * Standard implementation of the {@link OwSessionHistoryDeleteEvent} interface.<br/>
 * <b>Important:</b> the contained DMSID belongs to a deleted object, do not try to obtain the object from the DMSID provided by this event.
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
 * @since 3.1.0.0
 */
public class OwStandardSessionHistoryDeleteEvent implements OwSessionHistoryDeleteEvent
{
    /**
     * The DMSID of the deleted object.
     */
    private String dmsId;

    /**
     * Constructor
     * @param dmsId_p
     */
    public OwStandardSessionHistoryDeleteEvent(String dmsId_p)
    {
        this.dmsId = dmsId_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.history.OwSessionHistoryDeleteEvent#getAffectedDmsId()
     */
    public String getAffectedDmsId()
    {
        return dmsId;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.event.OwEvent#getSummary()
     */
    public String getSummary() throws Exception
    {
        return dmsId;
    }

}
