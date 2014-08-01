package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.Date;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;

/**
 *<p>
 * Dummy implementation of the history manager history entry interface to simulate history.
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
public class OwDummyHistoryEntry extends OwStandardHistoryEntry
{

    public OwDummyHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int type_p, int status_p, String strSummary_p, String strUser_p, OwObjectReference parent_p, Collection objects_p) throws Exception
    {
        super(context_p, time_p, id_p, type_p, status_p, strSummary_p, strUser_p, parent_p, objects_p);

    }

    public OwDummyHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int type_p, int status_p, String strSummary_p, String strUser_p, OwObjectReference object_p, OwFieldDefinitionProvider fielddefinitionprovider_p,
            String resource_p, Collection propertycardinalitiesandnames_p, Collection oldProperties_p, Collection newProperties_p) throws Exception
    {
        super(context_p, time_p, id_p, type_p, status_p, strSummary_p, strUser_p, object_p, fielddefinitionprovider_p, resource_p, propertycardinalitiesandnames_p, oldProperties_p, newProperties_p);

    }

    public OwDummyHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int iType_p, int iStatus_p, String strSummary_p, String strUser_p) throws Exception
    {
        super(context_p, time_p, id_p, iType_p, iStatus_p, strSummary_p, strUser_p);
    }

}