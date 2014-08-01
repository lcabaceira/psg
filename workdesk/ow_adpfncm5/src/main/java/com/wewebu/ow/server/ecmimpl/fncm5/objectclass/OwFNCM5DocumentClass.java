package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.List;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5DocumentState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Class representation of Document-based type definitions.
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
@SuppressWarnings("unchecked")
public class OwFNCM5DocumentClass<D extends Document> extends OwFNCM5IndependentlyPersistableObjectClass<D, OwFNCM5ObjectStoreResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5DocumentClass.class);

    public OwFNCM5DocumentClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_DOCUMENT;
    }

    @Override
    public OwFNCM5Object<D> from(D nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5Document<D> document = factory_p.create(OwFNCM5Document.class, new Class[] { Document.class, OwFNCM5DocumentClass.class }, new Object[] { nativeObject_p, this });
        return OwFNCM5SoftObject.asSoftObject(document);
    }

    //    public OwFNCM5Object<Document> from(Document nativeObject_p, boolean preserveVersion_p) throws OwException
    //    {
    //        OwFNCM5Document documentObject = null;
    //        if (preserveVersion_p)
    //        {
    //            documentObject = new OwFNCM5DocumentVersion(nativeObject_p, this);
    //        }
    //        else
    //        {
    //            Properties properties = nativeObject_p.getProperties();
    //            if (!properties.isPropertyPresent(PropertyNames.IS_CURRENT_VERSION))
    //            {
    //                nativeObject_p.refresh(new String[] { PropertyNames.IS_CURRENT_VERSION });
    //                properties = nativeObject_p.getProperties();
    //            }
    //            Boolean isCurrent = properties.getBooleanValue(PropertyNames.IS_CURRENT_VERSION);
    //
    //            if (isCurrent)
    //            {
    //                documentObject = new OwFNCM5CurrentDocument(nativeObject_p, this);
    //            }
    //            else
    //            {
    //                documentObject = new OwFNCM5DocumentVersion(nativeObject_p, this);
    //            }
    //        }
    //        OwFNCM5EngineObjectInterceptor<Document> protectionInterceptor = new OwFNCM5EngineObjectInterceptor<Document>(documentObject);
    //        OwFNCM5Object<Document> protectedDocument = (OwFNCM5Object<Document>) Proxy.newProxyInstance(OwFNCM5Object.class.getClassLoader(), new Class[] { OwFNCM5Object.class }, protectionInterceptor);
    //        return OwFNCM5SoftObject.asSoftObject(protectedDocument);
    //    }

    @Override
    public OwFNCM5EngineState<D> createSelf(D engineObject_p)
    {
        return new OwFNCM5DocumentState<D>(engineObject_p, this);
    }

    @Override
    protected OwFNCM5EngineState<D> newSelf(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        OwFNCM5EngineState<D> newDocumentSelf = super.newSelf(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);

        Document newDocument = newDocumentSelf.getEngineObject();

        if (permissions_p != null)
        {
            newDocument.set_Permissions(((OwFNCM5Permissions) permissions_p).getNativeObject());
        }

        setStorageArea(newDocument);

        newDocumentSelf.upload(content_p, strMimeType_p, strMimeParameter_p);

        AutoClassify autoClassify = AutoClassify.DO_NOT_AUTO_CLASSIFY;
        if (mode_p != null)
        {
            if (mode_p instanceof AutoClassify)
            {
                autoClassify = (AutoClassify) mode_p;
            }
            else
            {
                LOG.error("Invalid autoclassification mode object " + mode_p.getClass());
                throw new OwInvalidOperationException("Invalid autoclassification mod object " + mode_p.getClass());
            }
        }

        CheckinType checkinType = fPromote_p ? CheckinType.MAJOR_VERSION : CheckinType.MINOR_VERSION;

        newDocument.checkin(autoClassify, checkinType);

        if (fKeepCheckedOut_p)
        {
            newDocument.checkout(ReservationType.EXCLUSIVE, null, null, null);
        }

        return newDocumentSelf;
    }

    /**
     * Set a specific storage area during creation.
     * <p>By default empty, delegation to back-end configuration (StoragePolicy, DefaultStorage)</p>
     * @param document com.filenet.api.core.Document to set the storage area
     * @throws OwException if the problem occur assigning storage area
     */
    protected void setStorageArea(Document document) throws OwException
    {
        /*OwFNCM5ObjectStoreResource resource = getResource();
        ObjectStore objectStore = resource.getNativeObjectStore();

        StorageAreaSet storageAreas = objectStore.get_StorageAreas();
        StorageArea defaultStorageArea = (StorageArea) storageAreas.iterator().next();
        document.set_StorageArea(defaultStorageArea);*/
    }

    @Override
    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        if (OwResource.m_ObjectPathPropertyClass.getClassName().equals(className_p))
        {
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ObjectPathPropertyClass);
        }
        else if (OwResource.m_VersionSeriesPropertyClass.getClassName().equals(className_p))
        {
            return new OwFNCM5VirtualPropertyClass(OwResource.m_VersionSeriesPropertyClass);
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
        names.add(OwResource.m_VersionSeriesPropertyClass.getClassName());
        return names;
    }

    @Override
    protected String getContainmentName(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, Folder parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException
    {
        String name = super.getContainmentName(fPromote_p, mode_p, resource_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p);
        if (name != null && (strMimeParameter_p != null || content_p != null))
        {
            String ext = null;
            if (strMimeParameter_p != null)
            {
                ext = strMimeParameter_p;
            }
            else
            {
                try
                {
                    OwContentElement elem = content_p.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
                    ext = elem.getMIMEParameter();
                }
                catch (Exception e)
                {
                    LOG.debug("Content element could not be retrieved, ContainmentName will not have an extension", e);
                }
            }
            if (ext != null)
            {
                int idx = ext.lastIndexOf('.');
                if (idx >= 0 && !name.endsWith(ext.substring(idx)))
                {
                    name = name + ext.substring(idx);
                }
            }
        }
        return name;
    }
}
