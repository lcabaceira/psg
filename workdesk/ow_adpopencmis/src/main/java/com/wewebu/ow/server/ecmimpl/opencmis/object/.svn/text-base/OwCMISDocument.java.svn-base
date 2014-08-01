package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.TransientDocument;

import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISDocument Interface.
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
public interface OwCMISDocument<D extends TransientDocument> extends OwCMISNativeObject<D>, OwVersion
{
    @Override
    OwCMISVersionSeries getVersionSeries() throws OwException;

    @Override
    void cancelcheckout() throws OwException;

    @Override
    boolean isMyCheckedOut(int context_p) throws OwException;
}
