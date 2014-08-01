package com.wewebu.ow.server.ecmimpl.alfresco.bpm.util;

import java.util.Date;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;

/**
 *<p>
 * Standard BPM type filter, will look if a resubmission exist
 * and filter only elements which resubmission date is defined in future. 
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
 *@since 4.2.0.0
 */
public class StandardBPMClientSideFilter implements ClientSideFilter
{
    private Date compareDate;

    public StandardBPMClientSideFilter()
    {
        this(new Date());
    }

    public StandardBPMClientSideFilter(Date compareDate)
    {
        this.compareDate = compareDate;
    }

    @Override
    public boolean match(OwObject obj) throws Exception
    {
        OwWorkitem item = (OwWorkitem) obj;
        Date resubmitDate = item.getResubmitDate(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        return (null == resubmitDate || resubmitDate.before(getCompareDate()));
    }

    public Date getCompareDate()
    {
        return this.compareDate;
    }

    public void setCompareDate(Date toCompare)
    {
        this.compareDate = toCompare;
    }

}
