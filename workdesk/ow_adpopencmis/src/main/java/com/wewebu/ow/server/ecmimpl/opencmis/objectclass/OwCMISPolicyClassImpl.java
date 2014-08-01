package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientPolicy;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISPolicy;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISPolicyObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISPolicyClassImpl.
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
public class OwCMISPolicyClassImpl extends OwCMISAbstractNativeObjectClass<PolicyType, TransientPolicy> implements OwCMISPolicyClass
{

    public OwCMISPolicyClassImpl(PolicyType policyType, OwCMISNativeSession session)
    {
        super(policyType, session);
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    @Override
    public OwCMISPolicy from(TransientPolicy object, Map<String, ?> conversionParameters) throws OwException
    {
        OperationContext creationContext = createContext(conversionParameters);
        OwCMISPolicyClass clazz = getParameterValue(conversionParameters, OwCMISConversionParameters.OBJECT_CLASS, this);
        return new OwCMISPolicyObject(getSession(), object, creationContext, clazz);
    }

    @Override
    protected ObjectId createNativeObject(Map<String, Object> properties, ObjectId nativeParentFolder, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce)
    {
        Session nativeSession = getSession().getOpenCMISSession();
        ObjectId newId = nativeSession.createPolicy(properties, nativeParentFolder, policies, addAce, removeAce);
        return newId;
    }
}
