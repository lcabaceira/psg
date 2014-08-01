package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.lang.ref.SoftReference;

import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.Historized;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5IndependentlyPersistableObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5IndependentlyPersistableObject.
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
public abstract class OwFNCM5IndependentlyPersistableObject<P extends IndependentlyPersistableObject> extends OwFNCM5IndependentObject<P, OwFNCM5ObjectStoreResource>
{

    //    private static final Logger LOG = OwLog.getLogger(OwFNCM5IndependentlyPersistableObject.class);
    private SoftReference<Boolean> canDelete = new SoftReference<Boolean>(null);

    public OwFNCM5IndependentlyPersistableObject(P nativeObject_p, OwFNCM5IndependentlyPersistableObjectClass<P, OwFNCM5ObjectStoreResource> clazz_p)
    {
        super(nativeObject_p, clazz_p);
    }

    @Override
    public boolean canSetProperties(int iContext_p) throws OwException
    {
        return true;
    }

    public void setProperties(OwPropertyCollection properties_p) throws OwException
    {
        setProperties(properties_p, RefreshMode.REFRESH);
    }

    @Historized(type = OwEventManager.HISTORY_EVENT_TYPE_OBJECT, id = OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES)
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        OwFNCM5EngineState<P> mySelf = getSelf();
        mySelf.store(properties_p, mode_p);
    }

    @Override
    public boolean canDelete(int iContext_p) throws OwException
    {
        if (canDelete.get() == null || iContext_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            OwFNCM5Network network = getNetwork();
            OwFNCM5Credentials credentials = network.getCredentials();
            OwUserInfo userInfo = credentials.getUserInfo();

            OwFNCM5Permissions permissions = getPermissions();

            boolean b = permissions == null || OwFNCM5PermissionHelper.hasPermission(permissions, userInfo, AccessRight.DELETE_AS_INT);
            canDelete = new SoftReference<Boolean>(b ? Boolean.TRUE : Boolean.FALSE);
        }
        return canDelete.get().booleanValue();
    }

    @Override
    public void delete() throws OwException
    {
        getNativeObject().delete();
        getNativeObject().save(RefreshMode.NO_REFRESH);
    }

}
