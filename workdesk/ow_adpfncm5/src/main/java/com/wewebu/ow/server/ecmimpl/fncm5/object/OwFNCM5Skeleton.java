package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Set;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Specific P8 skeleton object.
 * Handle the permissions depending to the provided object class.
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwFNCM5Skeleton extends OwObjectSkeleton implements OwFNCM5Object<Object>
{
    private ClassDescription classDescription;
    private OwFNCM5Network network;
    private OwFNCM5Permissions permissions;

    public OwFNCM5Skeleton(OwFNCM5Network network_p, OwObjectClass objectClass_p, ClassDescription nativeDescription_p, OwXMLUtil initialValues_p) throws Exception
    {
        super(network_p, objectClass_p, initialValues_p);
        classDescription = nativeDescription_p;
        network = network_p;
    }

    public OwFNCM5Network getNetwork() throws OwException
    {
        return network;
    }

    public boolean hasWorkflowDescriptions()
    {
        return false;
    }

    public Set getWorkflowDescriptions()
    {
        return null;
    }

    @Override
    public String getDMSID() throws OwException
    {
        try
        {
            return super.getDMSID();
        }
        catch (Exception e)
        {
            throw new OwServerException("Internal problems while retrieving DMSID from Skeleton", e);
        }
    }

    @Override
    public Object getNativeObject() throws OwException
    {
        throw new OwInvalidOperationException(getNetwork().getContext().localize("OwFNCM5Skeleton.getNativeObject.invalidOperation", "Skeleton objects does not have native representation."));
    }

    public OwFNCM5Resource getResource() throws OwException
    {
        return getObjectClass().getResource();
    }

    @Override
    public OwFNCM5Class<?, ?> getObjectClass()
    {
        return (OwFNCM5Class<?, ?>) super.getObjectClass();
    }

    /**
     * Will return the default Instance permissions from OwObjectClass (ClassDescriptions)
     * if not set explicitly for this instance. Otherwise it will the defined 
     * permission collection which was defined through {@link #setPermissions(OwPermissionCollection)}.
     * <p>Calling setPermissions(null) this method will return again the default
     * instance permissions as defined by the back-end.</p>
     * @return OwPermissionCollection specific to OwFNCM5Permissions implementation
     */
    @Override
    public OwPermissionCollection getPermissions() throws Exception
    {
        if (permissions == null)
        {
            AccessPermissionList lst = OwFNCM5PermissionHelper.createCopy(getClassDescription().get_DefaultInstancePermissions());

            OwUserInfo user = getNetwork().getCredentials().getUserInfo();

            return new OwFNCM5Permissions(OwFNCM5PermissionHelper.consolidateCreatorRights(lst, user), getNetwork().getContext());
        }
        else
        {
            return permissions;
        }
    }

    /**
     * Set the permissions to a defined collection.
     * <p>ATTENTION: Provided parameter will explicitly be cast to OwFNCM5Permissions.</p>
     * @param permissions_p OwPermissionCollection (must represent a OwFNCM5Permissions)
     */
    @Override
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        permissions = (OwFNCM5Permissions) permissions_p;
    }

    protected ClassDescription getClassDescription()
    {
        return classDescription;
    }

    @Override
    public boolean canGetPermissions() throws Exception
    {
        return true;
    }

    @Override
    public boolean canSetPermissions() throws Exception
    {
        return true;
    }

    @Override
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        try
        {
            return super.getProperties(propertyNames_p);
        }
        catch (Exception e)
        {
            throw new OwServerException("Internal problems while retrieving properties from Skeleton", e);
        }
    }

    @Override
    public OwProperty getProperty(String strPropertyName_p) throws OwException
    {
        try
        {
            return super.getProperty(strPropertyName_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Internal problems while retrieving property from Skeleton", e);
        }
    }

    @Override
    public OwPropertyCollection getClonedProperties(Collection strPropertyNames_p) throws OwException
    {
        try
        {
            return super.getClonedProperties(strPropertyNames_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not clone properties", e);
        }
    }

    @Override
    public void delete() throws OwException
    {
        try
        {
            super.delete();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not delete object.", e);
        }
    }

    @Override
    public OwFNCM5Version<?> getVersion() throws OwException
    {
        try
        {
            return (OwFNCM5Version<?>) super.getVersion();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (ClassCastException e)
        {
            throw new OwServerException("Invalid object model.", e);
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve version.", e);
        }
    }
}
