package com.wewebu.ow.server.ecmimpl.alfresco.bpm.util;

import java.util.Date;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;

/**
 *<p>
 * Filter will match only Workitems where a resubmission Date exist,
 * and that Date is defined in future. 
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
public class ResubmissionClientSideFilter implements ClientSideFilter
{
    private Date compareDate;

    public ResubmissionClientSideFilter()
    {
        this(new Date());
    }

    public ResubmissionClientSideFilter(Date compareDate)
    {
        this.compareDate = compareDate;
    }

    @Override
    public boolean match(OwObject obj) throws Exception
    {
        OwWorkitem item = (OwWorkitem) obj;
        Date resubmitDate = item.getResubmitDate(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        return null != resubmitDate && resubmitDate.after(getCompareDate());
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
