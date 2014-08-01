package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;

import org.apache.chemistry.opencmis.client.api.TransientDocument;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwCMISVersionSeries.
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
public interface OwCMISVersionSeries extends OwVersionSeries, OwCMISDocument<TransientDocument>
{
    String checkoutVersionSeries(Object mode_p) throws OwException;

    @Override
    OwVersion getLatest() throws OwException;

    @Override
    OwVersion getReservation() throws OwException;

    @Override
    OwVersion getReleased() throws OwException;

    @Override
    Collection getVersions(Collection properties_p, OwSort sort_p, int iMaxSize_p) throws OwException;

    @Override
    OwObject getObject(OwVersion version_p) throws OwException;
}
