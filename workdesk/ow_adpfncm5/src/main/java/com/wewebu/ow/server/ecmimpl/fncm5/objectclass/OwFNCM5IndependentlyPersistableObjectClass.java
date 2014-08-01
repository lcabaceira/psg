package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import org.apache.log4j.Logger;

import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5PersistentState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Skeleton;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwFNCM5IndependentlyPersistableObjectClass.
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
public class OwFNCM5IndependentlyPersistableObjectClass<P extends IndependentlyPersistableObject, S extends OwFNCM5ObjectStoreResource> extends OwFNCM5IndependentlyInstantiableObjectClass<P, S> implements OwFNCM5SkeletonFactory
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5IndependentlyPersistableObjectClass.class);

    public OwFNCM5IndependentlyPersistableObjectClass(OwFNCM5EngineClassDeclaration<?, S> declaration_p, OwFNCM5ResourceAccessor<S> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    public OwFNCM5EngineState<P> createSelf(P engineObject_p)
    {
        return new OwFNCM5PersistentState<P>(engineObject_p, this);
    }

    @SuppressWarnings("unchecked")
    protected OwFNCM5EngineState<P> newSelf(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        OwFNCM5ObjectStoreResource resource = getResource();
        ObjectStore objectStore = resource.getNativeObjectStore();

        OwFNCM5EngineClassDeclaration<?, S> classDeclaration = getClassDeclaration();
        String symbolicName = classDeclaration.getSymbolicName();
        P newObject = (P) objectStore.createObject(symbolicName);

        //Folder newFolder = Factory.Folder.createInstance(objectStore, symbolicName);
        OwFNCM5EngineState<P> newSelf = createSelf(newObject);

        newSelf.set(properties_p);

        return newSelf;
    }

    @SuppressWarnings("unchecked")
    public OwFNCM5Skeleton createSkeleton(OwFNCM5Network network, OwObjectClass objClass) throws OwException
    {
        OwFNCM5EngineClassDeclaration<ClassDescription, S> classDeclaration = (OwFNCM5EngineClassDeclaration<ClassDescription, S>) getClassDeclaration();
        try
        {
            ClassDescription nativeDescription = classDeclaration.getNativeClass();
            String classSymbolicName = nativeDescription.get_SymbolicName();
            OwXMLUtil initialValues = network.getNetworkConfig().getCreationInitialValuesConfig(classSymbolicName);
            return new OwFNCM5Skeleton(network, this, nativeDescription, initialValues);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwFNCM5IndependentlyPersistableObjectClass.createSkeleton: Creation of skeleton object failed", e);
            throw new OwServerException(network.getContext().localize("OwFNCM5IndependentlyPersistableObjectClass.createSkeleton.error.skeletonInstance", "Could not instantiate Skeleton object."), e);
        }
    }
}
