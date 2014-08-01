package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyEngineObject;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ContentObjectModel;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5FolderClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ObjectStoreClass;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwObject based on native FileNet P8 ObjectStore.
 * Some parts of network interface are implemented directly here
 * to divide responsibility.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwFNCM5ObjectStore extends OwFNCM5IndependentObject<ObjectStore, OwFNCM5ObjectStoreResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ObjectStore.class);
    private OwFNCM5Object<?> rootFolder = null;

    public OwFNCM5ObjectStore(ObjectStore nativeObject_p, OwFNCM5ObjectStoreClass clazz_p) throws OwException
    {
        super(nativeObject_p, clazz_p);

    }

    @Override
    public String getName()
    {
        OwFNCM5EngineState<ObjectStore> myself = getSelf();
        ObjectStore objectStore = myself.getEngineObject();

        return objectStore.get_Name();
    }

    public OwFNCM5Object getObjectFromPath(String path_p) throws OwException
    {
        if (path_p == null)
        {
            throw new OwInvalidOperationException("null paths are not valid!");
        }

        String trimmedPath = path_p.trim();
        while (trimmedPath.contains("//"))
        {
            trimmedPath = trimmedPath.replaceAll("//", "/");
        }
        if (!trimmedPath.startsWith("/"))
        {
            trimmedPath = "/" + trimmedPath;
        }
        if ("/".equals(trimmedPath))
        {
            return this;
        }
        else
        {
            return nativeObjectRetrieval(Arrays.asList(ClassNames.FOLDER, ClassNames.DOCUMENT, ClassNames.CUSTOM_OBJECT), trimmedPath, OwFNCM5DefaultObjectFactory.INSTANCE);
        }
    }

    /**
     * Retrieve object by Id, will dynamically request the native
     * type of the object to retrieve. In worst case this 
     * method will create up to three roundtrips to ECM system before 
     * throwing a OwObjectNotFoundException.<br />
     * Default retrieve order is: <b> Document, CustomObject, Folder </b>.
     * <p>To reduce round trip use {@link #getObjectFromId(String, String)}
     * for specific type retrieval.</p>
     * @param objId_p String Id of the object
     * @return OwFNCM5Object specific to it's type
     * @throws OwException if cannot found or retrieve object
     */
    public OwFNCM5Object<?> getObjectFromId(String objId_p) throws OwException
    {
        return nativeObjectRetrieval(Arrays.asList(ClassNames.DOCUMENT, ClassNames.FOLDER, ClassNames.CUSTOM_OBJECT), objId_p, OwFNCM5DefaultObjectFactory.INSTANCE);
    }

    /**
     * Retrieve an object by given Id and type.
     * If type is unknown use {@link #getObjectFromId(String)} instead.
     * @param objId_p String of the object to retrieve 
     * @param nativeType_p String native API type of the object.
     * @return OwFNCM5Object based on given Id and type
     * @throws OwException if error occur while retrieving
     */
    public OwFNCM5Object<?> getObjectFromId(String objId_p, String nativeType_p) throws OwException
    {
        return nativeObjectRetrieval(Arrays.asList(nativeType_p), objId_p, OwFNCM5DefaultObjectFactory.INSTANCE);
    }

    /**
     * Based on a list of types, it will try to retrieve the object from the
     * ECM system, will try the next type based on native ExceptionCode.E_NULL_OR_INVALID_PARAM_VALUE.
     * @param types List of types the object could be
     * @param pathOrId_p String representing the Id of the object or the path to the object
     * @return OwFNCM5Object specific object 
     * @throws OwException will throw an OwObjectNotFound if object could not be found
     */
    protected OwFNCM5Object<?> nativeObjectRetrieval(List<String> types, String pathOrId_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        StopWatch stopWatch = new Log4JStopWatch("OwFNCM5ObjectStore.nativeObjectRetrieval");
        if (getID().equals(pathOrId_p))
        {
            stopWatch.stop();
            return this;
        }
        else
        {
            StopWatch stopWatchEx = new Log4JStopWatch("OwFNCM5ObjectStore.nativeObjectRetrieval ex");
            OwFNCM5EngineState<ObjectStore> myself = getSelf();
            ObjectStore objectStore = myself.getEngineObject();

            Iterator<String> itType = types.iterator();
            OwFNCM5ObjectStoreResource myResource = getResource();
            OwFNCM5ContentObjectModel om = myResource.getObjectModel();
            Exception[] exceptions = new Exception[types.size()];
            while (itType.hasNext())
            {
                String objType = itType.next();
                try
                {
                    IndependentObject root = objectStore.fetchObject(objType, pathOrId_p, null);
                    OwFNCM5Class<IndependentObject, ?> rootClass = om.classOf(root);
                    stopWatch.stop();
                    return rootClass.from(root, factory_p);
                }
                catch (EngineRuntimeException ex)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug(" object [" + pathOrId_p + "] as type " + objType + " from " + getName());
                    }
                    int idEx = ex.getExceptionCode().getId();
                    if (idEx == ExceptionCode.E_NULL_OR_INVALID_PARAM_VALUE.getId() || idEx == ExceptionCode.E_OBJECT_NOT_FOUND.getId())
                    {
                        /*mean that the object is not from that type, re-try with next type*/
                    }
                    else
                    {
                        if (idEx == ExceptionCode.E_ACCESS_DENIED.getId())
                        {
                            throw new OwAccessDeniedException(OwString.localize(getNetwork().getLocale(), "OwFNCM5ObjectStore.retrieve.exception.denied", "You don't have the permission to access the object"), ex);
                        }
                        if (idEx == ExceptionCode.E_INVALID_REQUEST.getId())
                        {//Thrown if malformed request, id/path not matching some criteria
                            throw new OwInvalidOperationException(getNetwork().getContext().localize1("OwFNCM5ObjectStore.retrieve.exception.invalid", "The request is invalid for parameter = %1", pathOrId_p), ex);
                        }
                        else
                        {
                            throw new OwServerException("Unhandled exception during object retrieval", ex);
                        }
                    }
                }
            }
            stopWatchEx.stop();
            if (exceptions.length > 0 && exceptions[0] != null)
            {
                throw new OwObjectNotFoundException("Could not find object by path (or Id): " + pathOrId_p, exceptions[0]);
            }
            else
            {
                throw new OwObjectNotFoundException("Could not find object by path (or Id): " + pathOrId_p);
            }
        }
    }

    public synchronized OwFNCM5Object<?> getRootFolder() throws OwException
    {
        if (this.rootFolder == null)
        {
            OwFNCM5ObjectStoreResource myResource = getResource();
            OwFNCM5ContentObjectModel om = myResource.getObjectModel();
            OwFNCM5FolderClass folderClass = (OwFNCM5FolderClass) om.objectClassForName(ClassNames.FOLDER);

            OwFNCM5EngineState<ObjectStore> myself = getSelf();
            ObjectStore objectStore = myself.getEngineObject();

            //            Folder root = objectStore.get_RootFolder();
            Properties osProperties = objectStore.getProperties();
            PropertyEngineObject rootFolderProperty = (PropertyEngineObject) osProperties.get(PropertyNames.ROOT_FOLDER);
            Folder root = (Folder) rootFolderProperty.getObjectValue();
            if (root == null)
            {
                throw new OwServerException("Object store root is null!");
            }
            this.rootFolder = folderClass.from(root, OwFNCM5DefaultObjectFactory.INSTANCE);
        }

        return this.rootFolder;
    }

    @Override
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwStandardObjectCollection children = new OwStandardObjectCollection();

        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_TUPLE_OBJECTS:
                case OBJECT_TYPE_CUSTOM:
                {
                    //TODO: filter custom object from root folder
                    break;
                }
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OBJECT_TYPE_DOCUMENT:
                {
                    OwFNCM5EngineState<ObjectStore> myself = getSelf();
                    Folder root = myself.getEngineObject().get_RootFolder();
                    if (root != null)
                    {
                        root.refresh();
                        DocumentSet docs = root.get_ContainedDocuments();
                        Iterator<?> it = docs.iterator();
                        while (it.hasNext())
                        {
                            Document doc = (Document) it.next();
                            if (doc instanceof Versionable)
                            {
                                Versionable versionable = doc;
                                switch (iVersionSelection_p)
                                {
                                    case OwSearchTemplate.VERSION_SELECT_MINORS:
                                        if (versionable.get_MinorVersionNumber() == 0)
                                        {
                                            continue;
                                        }
                                        break;
                                    case OwSearchTemplate.VERSION_SELECT_MAJORS:
                                        if (versionable.get_MinorVersionNumber() != 0)
                                        {
                                            continue;
                                        }
                                        break;

                                    case OwSearchTemplate.VERSION_SELECT_IN_PROCESS:
                                        if (!VersionStatus.IN_PROCESS.equals(versionable.get_VersionStatus()))
                                        {
                                            continue;
                                        }
                                        break;
                                    case OwSearchTemplate.VERSION_SELECT_CHECKED_OUT:
                                        if (!VersionStatus.RESERVATION.equals(versionable.get_VersionStatus()))
                                        {
                                            continue;
                                        }
                                        break;
                                    case OwSearchTemplate.VERSION_SELECT_CURRENT:
                                        if (!versionable.get_IsCurrentVersion())
                                        {
                                            continue;
                                        }
                                        break;
                                    case OwSearchTemplate.VERSION_SELECT_RELEASED:
                                        if (!VersionStatus.RELEASED.equals(versionable.get_VersionStatus()))
                                        {
                                            continue;
                                        }
                                        break;
                                    case OwSearchTemplate.VERSION_SELECT_ALL:
                                    case OwSearchTemplate.VERSION_SELECT_DEFAULT:
                                    default:
                                        break;
                                }
                            }
                            OwFNCM5Object docObject = createOwObject(doc);
                            children.add(docObject);
                        }
                    }

                    break;
                }
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OBJECT_TYPE_FOLDER:
                {
                    OwFNCM5EngineState<ObjectStore> myself = getSelf();
                    ObjectStore objectStore = myself.getEngineObject();

                    FolderSet folders = objectStore.get_TopFolders();
                    Iterator folderIt = folders.iterator();
                    while (folderIt.hasNext())
                    {
                        Folder folder = (Folder) folderIt.next();
                        OwFNCM5Object folderObject = createOwObject(folder);
                        children.add(folderObject);
                    }
                    break;
                }
                default:
                    LOG.debug("OwFNCM5ObjectStore.getChilds(): Unknown object type will be ignored for retrieval. objetType = " + iObjectTypes_p[i]);
            }
        }

        return children;
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        return true;
    }

    public String getMIMEType() throws OwException
    {
        return "ow_root/filenet_cm_obst";
    }

    public OwFNCM5Version<?> getVersion() throws OwException
    {
        return null;
    }

    public boolean hasVersionSeries() throws OwException
    {
        return false;
    }

    public OwVersionSeries getVersionSeries() throws OwException
    {
        return null;
    }

    public String getID()
    {
        OwFNCM5EngineState<ObjectStore> myself = getSelf();
        ObjectStore objectStore = myself.getEngineObject();

        Id id = objectStore.get_ObjectStoreId();
        return id.toString();
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (getID().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj) || obj == null)
        {
            return false;
        }
        if (!(obj instanceof OwFNCM5ObjectStore))
        {
            return false;
        }
        OwFNCM5ObjectStore other = (OwFNCM5ObjectStore) obj;
        return getID().equals(other.getID());
    }

    /**
     * 
     * @return P8 Symbolic Name of this object store 
     * @since 3.2.0.1
     */
    public String getSymbolicName()
    {

        OwFNCM5EngineState<ObjectStore> myself = getSelf();
        ObjectStore objectStore = myself.getEngineObject();

        return objectStore.get_SymbolicName();
    }

}
