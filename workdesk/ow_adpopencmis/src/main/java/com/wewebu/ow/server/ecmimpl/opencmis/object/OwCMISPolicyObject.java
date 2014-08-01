package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.TransientPolicy;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPolicyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * CMIS base-type "cmis:policy" dependent implementation.
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
public class OwCMISPolicyObject extends OwCMISAbstractNativeObject<TransientPolicy, PolicyType, OwCMISPolicyClass> implements OwCMISPolicy
{

    public OwCMISPolicyObject(OwCMISNativeSession session_p, TransientPolicy nativeObject_p, OperationContext creationContext, OwCMISPolicyClass class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    @Override
    public final OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    @Override
    public final OwVersion getVersion() throws Exception
    {
        return null;
    }

    @Override
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        //void
    }

    @Override
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }
}
