package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5CustomObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * {@link CustomObject} representation.
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
public class OwFNCM5CustomObject extends OwFNCM5ContainableObject<CustomObject>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5CustomObject.class);

    public OwFNCM5CustomObject(CustomObject nativeObject_p, OwFNCM5CustomObjectClass clazz_p) throws OwInvalidOperationException
    {
        super(nativeObject_p, clazz_p);
    }

    public String getMIMEType() throws OwException
    {
        return OwMimeManager.MIME_TYPE_PREFIX_OW_CUSTOMOBJECT + getClassName();
    }

    @Override
    protected OwObjectCollection retrieveParentsCollection() throws OwException
    {
        OwObjectCollection col = null;
        OwFNCM5EngineState<CustomObject> myself = getSelf();
        CustomObject customObject = myself.ensure(PropertyNames.CONTAINERS);

        ReferentialContainmentRelationshipSet rcrSet = customObject.get_Containers();
        if (rcrSet != null && !rcrSet.isEmpty())
        {
            col = new OwStandardObjectCollection();
            Iterator it = rcrSet.iterator();
            OwFNCM5ObjectStore os = (getResource()).getObjectStore();
            while (it.hasNext())
            {
                ReferentialContainmentRelationship rcr = (ReferentialContainmentRelationship) it.next();
                String id = rcr.get_Tail().getObjectReference().getObjectIdentity().toString();
                try
                {
                    OwFNCM5Object folder = os.getObjectFromId(id, "Folder");
                    col.add(folder);
                }
                catch (Exception ex)
                {
                    //Ignore the exception, but keep available for debug reasons
                    LOG.debug("Cannot retrieve by or invalid Folder id = " + id, ex);
                }
            }
        }
        return col;
    }

    @Override
    public boolean setLock(boolean fLock_p) throws Exception
    {
        OwFNCM5EngineState<CustomObject> myself = getSelf();
        CustomObject customObject = myself.getEngineObject();
        String owner = getUserShortName();
        final int timeout = Integer.MAX_VALUE;
        if (fLock_p)
        {
            customObject.lock(timeout, owner);
        }
        else
        {
            customObject.unlock();
        }

        return customObject.isLocked();
    }

    @Override
    public boolean getLock(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<CustomObject> myself = getSelf();
        CustomObject customObject = myself.getEngineObject();
        return customObject.isLocked();
    }

    @Override
    public boolean getMyLock(int iContext_p) throws OwException
    {
        String lockOwner = getLockUserID(iContext_p);
        String userName = getUserShortName();
        return lockOwner != null && lockOwner.equals(userName);
    }

    @Override
    public String getLockUserID(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<CustomObject> myself = getSelf();
        CustomObject customObject = myself.getEngineObject();
        String lockOwner = customObject.get_LockOwner();
        return lockOwner;
    }
}
