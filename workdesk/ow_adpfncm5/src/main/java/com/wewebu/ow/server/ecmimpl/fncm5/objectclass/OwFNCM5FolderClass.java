package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwManagedSemiVirtualRecordConfiguration;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSemiVirtualRecordClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Folder;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SemiVirtualFolder;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5FolderClass.
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
public class OwFNCM5FolderClass extends OwFNCM5IndependentlyPersistableObjectClass<Folder, OwFNCM5ObjectStoreResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5FolderClass.class);
    private Set<String> recordClassNames;

    public OwFNCM5FolderClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p) throws OwException
    {
        super(declaration_p, resourceAccessor_p);
        this.recordClassNames = getResource().getNetwork().getRecordClassNames();
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_FOLDER;
    }

    @Override
    protected OwFNCM5EngineState<Folder> newSelf(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        OwFNCM5EngineState<Folder> newSelf = super.newSelf(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);

        Folder folder = newSelf.getEngineObject();
        if (permissions_p != null)
        {
            folder.set_Permissions(((OwFNCM5Permissions) permissions_p).getNativeObject());
        }

        Properties properties = folder.getProperties();
        properties.putObjectValue(PropertyNames.PARENT, parent_p);

        return newSelf;
    }

    @Override
    public OwFNCM5Object<? extends Folder> newObject(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5Object<? extends Folder> newObject = super.newObject(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p, factory_p);
        if (this.recordClassNames.contains(this.getClassName()))
        {
            createRecordStructure(newObject, resource_p);
        }
        return newObject;
    }

    /**
     * Will retrieve the template/source structure and start the recursive recreation of structure.
     * @param newFolderObject OwFNCM5Object current created object identified to have physical record structure
     * @param resource_p OwResource current resource/object store
     * @throws OwException
     */
    protected void createRecordStructure(OwFNCM5Object<? extends Folder> newFolderObject, OwResource resource_p) throws OwException
    {
        ObjectStore obStore = getResource().getObjectStore().getNativeObject();
        String strObjectClassName = newFolderObject.getClassName();
        String templatePath = "/ow_app/recordtemplates/" + strObjectClassName;
        try
        {
            Folder templateFolder = Factory.Folder.fetchInstance(obStore, templatePath, null);

            copyRecordStructure(templateFolder, newFolderObject.getNativeObject());
        }
        catch (EngineRuntimeException e)
        {
            LOG.error("The file structure could not be created.", e);
            OwNetworkContext context = getResource().getNetwork().getContext();
            if (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND)
            {
                throw new OwInvalidOperationException(context.localize1("OwFNCM5Network.failedtocreaterecord", "The file structure could not be created, maybe there is no file template:", templatePath), e);
            }

            throw new OwInvalidOperationException("The folder structure could not be created.", e);
        }
    }

    /**
     * Recursive traversal of source folder structure, attaching recreating it in the target folder object.
     * @param sourceFolder_p Folder structure definition
     * @param targetFolder_p Folder where to recreate the structure definition
     * @throws OwException
     */
    @SuppressWarnings("rawtypes")
    protected void copyRecordStructure(Folder sourceFolder_p, Folder targetFolder_p) throws OwException
    {
        FolderSet containees = sourceFolder_p.get_SubFolders();

        Iterator it = containees.iterator();
        while (it.hasNext())
        {
            Folder subSourceFolder = (Folder) it.next();

            // === copy folder by creating a new one with the same name
            Folder subTargetFolder = Factory.Folder.createInstance(sourceFolder_p.getObjectStore(), subSourceFolder.getClassName());
            subTargetFolder.set_FolderName(subSourceFolder.get_FolderName());
            //subTargetFolder.set_Permissions(subSourceFolder.get_Permissions());
            subTargetFolder.set_Parent(targetFolder_p);
            subTargetFolder.save(RefreshMode.REFRESH);

            // === recurse next subfolder
            copyRecordStructure(subSourceFolder, subTargetFolder);
        }
    }

    @Override
    protected void fileToParent(Folder parent_p, Folder persistableObject_p, String name_p)
    {
        //NO REFERENTIAL FILING
        //P8-API_DOC: Note Folders that are referentially contained cannot participate in hierarchy index searches.
    }

    @Override
    public OwFNCM5Object<Folder> from(Folder nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5ObjectStoreResource resource = getResource();
        OwFNCM5Network network = resource.getNetwork();

        String folderclassname = getClassName();
        OwManagedSemiVirtualRecordConfiguration virtualConfiguration = network.getManagedVirtualConfiguration();

        OwSemiVirtualRecordClass virtualFolderEntry = virtualConfiguration.semiVirtualFolderForObjectClass(folderclassname, network.getRoleManager(), network.getNetworkConfig().getConfigNode());

        OwFNCM5Folder folderObject = null;
        if (null != virtualFolderEntry)
        {
            folderObject = factory_p.create(OwFNCM5SemiVirtualFolder.class, new Class[] { Folder.class, OwFNCM5FolderClass.class, OwSemiVirtualRecordClass.class }, new Object[] { nativeObject_p, this, virtualFolderEntry });
            //            folderObject = new OwFNCM5SemiVirtualFolder(nativeObject_p, this, virtualFolderEntry);
        }
        else
        {
            folderObject = factory_p.create(OwFNCM5Folder.class, new Class[] { Folder.class, OwFNCM5FolderClass.class }, new Object[] { nativeObject_p, this });
            //            folderObject = new OwFNCM5Folder(nativeObject_p, this);
        }

        return OwFNCM5SoftObject.asSoftObject(folderObject);
    }

    @Override
    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        if (OwResource.m_ObjectPathPropertyClass.getClassName().equals(className_p))
        {
            OwFNCM5EnginePropertyClass<?, ?, ?> pathNameClass = enginePropertyClass(PropertyNames.PATH_NAME);
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ObjectPathPropertyClass, pathNameClass);
        }
        else
        {
            return super.virtualPropertyClass(className_p);
        }
    }

    @Override
    public List<String> getVirtualPropertiesClassNames() throws OwException
    {
        List<String> names = super.getVirtualPropertiesClassNames();
        names.add(OwResource.m_ObjectPathPropertyClass.getClassName());
        return names;
    }
}
