package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Containable;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.PropertyFilter;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5IndependentlyPersistableObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Base class for containable objects.<br />
 * Containable:[Document,Folder,CustomObject]<br />
 * Is extended from IndependentlyPersistableObject for easier polymorphism concept.
 * This class checks in the Constructor if the provided native object implements
 * the com.filenet.api.core.Containable.
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
public abstract class OwFNCM5ContainableObject<O extends IndependentlyPersistableObject> extends OwFNCM5IndependentlyPersistableObject<O> implements OwFNCM5Containee
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ContainableObject.class);
    protected static final String[] LOCK_PROPERTIES = new String[] { PropertyNames.LOCK_TIMEOUT, PropertyNames.LOCK_TOKEN, PropertyNames.DATE_LAST_MODIFIED };
    protected SoftReference<OwObjectCollection> parents;

    public OwFNCM5ContainableObject(O nativeObject_p, OwFNCM5IndependentlyPersistableObjectClass<O, OwFNCM5ObjectStoreResource> clazz_p) throws OwInvalidOperationException
    {
        super(nativeObject_p, clazz_p);
        if (!(nativeObject_p instanceof Containable))
        {
            LOG.error("OwFNCM5ContainableObject.OwFNCM5ContainableObject: Containable on non containable engine object " + nativeObject_p == null ? "<null>" : nativeObject_p.getClass().toString());
            throw new OwInvalidOperationException("Containable on non containable engine object.");
        }
        parents = new SoftReference<OwObjectCollection>(null);
    }

    @Override
    public String getName()
    {
        try
        {
            OwFNCM5EngineState<O> myself = getSelf();
            Containable containable = (Containable) myself.ensure(PropertyNames.NAME);
            return containable.get_Name();
        }
        catch (OwException e)
        {
            LOG.error("Could not retrieve name !", e);
            return "N/A";
        }
    }

    @Override
    public OwObjectCollection getParents() throws OwException
    {
        if (parents.get() == null)
        {
            parents = new SoftReference<OwObjectCollection>(retrieveParentsCollection());
        }
        return parents.get();
    }

    @Override
    public OwFNCM5Permissions getPermissions() throws OwException
    {
        if (getNativeObject().getProperties().find(PropertyNames.PERMISSIONS) == null)
        {
            //deep level fetch Permissions represents an object with additional information
            PropertyFilter propFilter = new PropertyFilter();
            propFilter.setMaxRecursion(5);
            propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSIONS, null);
            getNativeObject().fetchProperties(propFilter);
        }
        return new OwFNCM5Permissions(((Containable) getNativeObject()).get_Permissions(), getNetwork().getContext());
    }

    @Override
    public void setPermissions(OwPermissionCollection perms_p) throws OwException
    {
        ((Containable) getNativeObject()).set_Permissions(((OwFNCM5Permissions) perms_p).getNativeObject());
        getNativeObject().save(RefreshMode.NO_REFRESH);
    }

    @Override
    public boolean canGetPermissions() throws OwException
    {
        return true;
    }

    @Override
    public boolean canSetPermissions() throws OwException
    {
        return true;
    }

    public void refreshProperties() throws OwException
    {
        if (parents.get() != null)
        {
            parents = new SoftReference<OwObjectCollection>(null);
        }
        super.refreshProperties();
    }

    public void refreshProperties(Collection properties_p) throws OwException
    {
        if (parents.get() != null)
        {
            parents = new SoftReference<OwObjectCollection>(null);
        }
        super.refreshProperties(properties_p);
    }

    /**
     * The Interface method {@link #getParents()} implements a central caching,
     * so in case of ECM base request this method will be called.
     * @return OwObjectCollection, or null if no parents exist.
     * @throws OwException if retrieval of parents fail
     */
    protected abstract OwObjectCollection retrieveParentsCollection() throws OwException;

    public void removeReferenceFrom(OwFNCM5Folder folder_p) throws OwException
    {
        try
        {
            OwFNCM5EngineState<O> myself = getSelf();

            Containable nativeContainable = (Containable) myself.refresh();
            Folder folderNative = folder_p.getNativeObject();

            ReferentialContainmentRelationshipSet containers = nativeContainable.get_Containers();
            Iterator iterator = containers.iterator();
            while (iterator.hasNext())
            {

                ReferentialContainmentRelationship containmentRelationship = (ReferentialContainmentRelationship) iterator.next();
                IndependentObject tail = containmentRelationship.get_Tail();
                String tailId = tail.getObjectReference().getObjectIdentity();
                String thisId = folderNative.getObjectReference().getObjectIdentity();
                if (tailId != null && tailId.equals(thisId))
                {
                    ReferentialContainmentRelationship re = folderNative.unfile(containmentRelationship.get_ContainmentName());
                    re.save(RefreshMode.NO_REFRESH);
                }
            }

            myself.refresh();
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not remove reference!", e);
        }

    }

}
