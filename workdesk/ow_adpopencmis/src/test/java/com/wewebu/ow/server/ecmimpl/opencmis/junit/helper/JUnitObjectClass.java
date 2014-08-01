package com.wewebu.ow.server.ecmimpl.opencmis.junit.helper;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISAbstractNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * JUnitObjectClass.
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
public class JUnitObjectClass extends OwCMISAbstractNativeObjectClass<ObjectType, TransientCmisObject>
{

    public JUnitObjectClass(ObjectType objectType_p, OwCMISNativeSession session_p)
    {
        super(objectType_p, session_p);
    }

    @Override
    public OwCMISNativeObject<TransientCmisObject> from(TransientCmisObject object, Map<String, ?> conversionParameters) throws OwException
    {
        return null;
    }

    @Override
    protected ObjectId createNativeObject(Map<String, Object> properties, ObjectId nativeParentFolder, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce)
    {
        return null;
    }

}
