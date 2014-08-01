package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.TransientRelationship;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISRelationshipClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;

/**
 *<p>
 * CMIS base-type "cmis:relationship" dependent implementation.
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
public class OwCMISRelationshipObject extends OwCMISAbstractNativeObject<TransientRelationship, RelationshipType, OwCMISRelationshipClass> implements OwCMISRelationship
{
    private static final Logger LOG = OwLog.getLogger(OwCMISRelationshipObject.class);

    public OwCMISRelationshipObject(OwCMISNativeSession session_p, TransientRelationship nativeObject_p, OperationContext creationContext, OwCMISRelationshipClass class_p) throws OwException
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
        // void
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

    @Override
    public OwObjectReference getTarget()
    {
        try
        {
            return (OwObjectReference) getProperty(PropertyIds.TARGET_ID).getValue();
        }
        catch (OwException e)
        {
            LOG.warn("faild to get target as reference object", e);
            throw new OwCMISRuntimeException("faild to get target as reference object", e);
        }
    }

    @Override
    public OwObjectReference getSource()
    {
        try
        {
            return (OwObjectReference) getProperty(PropertyIds.SOURCE_ID).getValue();
        }
        catch (OwException e)
        {
            LOG.warn("faild to get source as reference object", e);
            throw new OwCMISRuntimeException("faild to get source as reference object", e);
        }
    }

    @Override
    public OwObjectLinkRelation getRelation(OwObject obj)
    {
        String sourceId = getNativeObject().getPropertyValue(PropertyIds.SOURCE_ID);
        String targetId = getNativeObject().getPropertyValue(PropertyIds.TARGET_ID);
        String objId = OwObjectIDCodeUtil.decode(obj.getID());
        if (sourceId.equals(objId))
        {
            if (targetId.equals(objId))
            {
                return OwObjectLinkRelation.BOTH;
            }
            return OwObjectLinkRelation.OUTBOUND;
        }
        else
        {
            if (targetId.equals(objId))
            {
                return OwObjectLinkRelation.INBOUND;
            }
            return OwObjectLinkRelation.NONE;
        }
    }
}
