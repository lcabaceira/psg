package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ContainmentName;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5IndependentlyInstantiableObjectClass.
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
public abstract class OwFNCM5IndependentlyInstantiableObjectClass<P extends IndependentlyPersistableObject, S extends OwFNCM5ObjectStoreResource> extends OwFNCM5IndependentObjectClass<P, S> implements OwFNCM5Constructor<P, S>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5IndependentlyInstantiableObjectClass.class);

    public OwFNCM5IndependentlyInstantiableObjectClass(OwFNCM5EngineClassDeclaration<?, S> declaration_p, OwFNCM5ResourceAccessor<S> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    protected abstract OwFNCM5EngineState<P> newSelf(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException;

    /**
     * Create new Object, this will start the create process on p8 system
     * calling the specific save method.
     * @param fPromote_p boolean major/minor
     * @param mode_p Object if supported (can be null)
     * @param resource_p OwResource where it will be created
     * @param properties_p OwPropertyColletion which property will be set
     * @param permissions_p OwPermissionCollection (can be null)
     * @param content_p OwContentCollection
     * @param parent_p
     * @param strMimeType_p
     * @param strMimeParameter_p
     * @param fKeepCheckedOut_p boolean should be kept checked-out
     * @param factory_p
     * @return OwFNCM5Object
     * @throws OwException
     */
    public OwFNCM5Object<? extends P> newObject(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5EngineState<P> persistableState = newSelf(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);
        persistableState.save(RefreshMode.NO_REFRESH);

        P persistableObject = persistableState.refresh();

        if (parent_p != null)
        {
            String name = getContainmentName(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);
            fileToParent(parent_p, persistableObject, name);
        }

        persistableObject = persistableState.refresh();

        return from(persistableObject, factory_p);
    }

    /**
     * Return the containment name which will be provided to file operation, this name will maybe defined for containment ship.
     * By Default this will simply use the name property to define the containment property.
     * @param fPromote_p boolean promote
     * @param mode_p Object if specific mode is supported
     * @param resource_p OwResource to be used
     * @param properties_p OwPropertyCollection list of properties which will be set
     * @param permissions_p OwPermissionCollection
     * @param content_p OwContentCollection which will be provided for creation
     * @param parent_p OwObject parent of new created object used in file operation
     * @param strMimeType_p String a MIME type to be used
     * @param strMimeParameter_p String additional parameters
     * @param fKeepCheckedOut_p boolen is checked-out after creation
     * @return String or null if no specific containment name is available
     * @throws OwException
     */
    protected String getContainmentName(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        String namePropertyClassName = getNamePropertyName();
        OwProperty nameProperty = (OwProperty) properties_p.get(namePropertyClassName);
        String name = null;
        if (nameProperty != null)
        {
            try
            {
                Object nameValue = nameProperty.getValue();
                name = (nameValue != null) ? nameValue.toString() : null;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not get the name of the new object!", e);
            }
        }

        if (name == null)
        {
            LOG.debug("No name property found for the new object of class " + getClassName() + " with name property " + namePropertyClassName);
        }
        return name;
    }

    /**
     * File currently created object to provided parent.
     * <p>Will be called only if a parent for filing exist.</p> 
     * @param parent_p Folder parent to file in
     * @param persistableObject_p P currently created object
     * @param name_p String name for filing
     * @throws OwException if filing did not work
     */
    protected void fileToParent(Folder parent_p, P persistableObject_p, String name_p) throws OwException
    {
        OwFNCM5ContainmentName containmentName = new OwFNCM5ContainmentName(name_p);
        ReferentialContainmentRelationship rcr = parent_p.file(persistableObject_p, AutoUniqueName.AUTO_UNIQUE, containmentName.toString(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.save(RefreshMode.REFRESH);
    }

    public OwFNCM5Object<? extends P> newObject(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        Object nativeParent = null;
        if (parent_p != null)
        {
            try
            {
                nativeParent = parent_p.getNativeObject();
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Error retrieving native parent.", e);
            }
        }

        if (nativeParent instanceof Folder || nativeParent == null)
        {
            Folder nativeParentFolder = (Folder) nativeParent;

            return newObject(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, nativeParentFolder, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p, factory_p);
        }
        else
        {
            throw new OwInvalidOperationException("Invalid parent for new object. Expected native Folder based object but found a " + nativeParent.getClass() + " based " + parent_p.getClass());
        }
    }

    @Override
    public OwFNCM5Constructor<P, S> getConstructor() throws OwException
    {
        return this;
    }

}
