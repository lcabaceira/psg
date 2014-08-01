package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ContentObjectModel;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.ObjectAccess;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ContainmentName;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5FolderClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwFNCM5Folder.
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
public class OwFNCM5Folder extends OwFNCM5ContainableObject<Folder>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Folder.class);

    public OwFNCM5Folder(Folder nativeObject_p, OwFNCM5FolderClass clazz_p) throws OwInvalidOperationException
    {
        super(nativeObject_p, clazz_p);
    }

    @Override
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        return super.getChildCount(iObjectTypes_p, iContext_p);
    }

    @ObjectAccess(mask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW, name = " view this object ")
    @Override
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwFNCM5Network network = getNetwork();
        OwXMLUtil configuration = network.getConfiguration();

        boolean evaluateReferentialContainmentRelationship = configuration.getSafeBooleanValue("EvaluateReferentialContainmentRelationship", false);

        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder nativeObject = myself.getEngineObject();

        nativeObject.refresh();

        OwObjectCollection retCol;
        StopWatch stopWatch;
        if (evaluateReferentialContainmentRelationship)
        {
            stopWatch = new Log4JStopWatch("OwFNCM5Folder.getChilds(RCR)");
            retCol = retrieveChildrenReferences(nativeObject, iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
        }
        else
        {
            stopWatch = new Log4JStopWatch("OwFNCM5Folder.getChilds(STD)");
            retCol = retrieveChildren(nativeObject, iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
        }
        stopWatch.stop();
        return retCol;
    }

    protected OwObjectCollection retrieveChildrenReferences(Folder folder_p, int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwStandardObjectCollection children = new OwStandardObjectCollection();//TODO replace with paging collection
        ReferentialContainmentRelationshipSet containees = folder_p.get_Containees();

        boolean folders = false;
        boolean content = false;
        boolean custom = false;

        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                {
                    folders = true;
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                {
                    content = true;
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_CUSTOM:
                {
                    custom = true;
                }
                    break;
                default:
                    ;
            }
        }
        Iterator ci = containees.iterator();
        OwFNCM5ObjectStoreResource resource = getResource();
        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();
        List<String> folderIds = new LinkedList<String>();
        while (ci.hasNext())
        {
            //TODO: reuse folder creation hook ?!?!? 
            ReferentialContainmentRelationship reference = (ReferentialContainmentRelationship) ci.next();
            OwFNCM5Class<ReferentialContainmentRelationship, ?> referenceClass = objectModel.classOf(reference);
            OwFNCM5Object<ReferentialContainmentRelationship> referenceObject = referenceClass.from(reference, OwFNCM5DefaultObjectFactory.INSTANCE);
            OwFNCM5ReferentialContainee containee = new OwFNCM5ReferentialContainee(referenceObject);
            int type = containee.getType();

            if ((folders && OwObjectReference.OBJECT_TYPE_FOLDER == type) || (content && OwObjectReference.OBJECT_TYPE_DOCUMENT == type) || (custom && OwObjectReference.OBJECT_TYPE_CUSTOM == type))
            {
                children.add(containee);
                if (OwObjectReference.OBJECT_TYPE_FOLDER == type)
                {
                    IndependentObject nativeFolder = containee.getNativeObject();
                    ObjectReference objectReference = nativeFolder.getObjectReference();
                    folderIds.add(objectReference.getObjectIdentity());
                }
            }

        }

        if (folders)
        {
            FolderSet subFolders = folder_p.get_SubFolders();
            Iterator fi = subFolders.iterator();
            while (fi.hasNext())
            {
                Folder folder = (Folder) fi.next();
                String folderId = folder.get_Id().toString();
                if (!folderIds.contains(folderId))
                {
                    OwFNCM5Object<?> folderObject = createOwObject(folder);
                    children.add(folderObject);
                }
            }
        }
        if (sort_p != null && !sort_p.getCriteriaCollection().isEmpty())
        {
            doClientSorting(children, sort_p);
        }
        return children;
    }

    protected OwObjectCollection retrieveChildren(Folder folder_p, int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwStandardObjectCollection children = new OwStandardObjectCollection();//TODO replace with paging collection

        for (int i = 0; i < iObjectTypes_p.length; i++)
        {

            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                {
                    FolderSet subFolders = folder_p.get_SubFolders();
                    PageIterator subFoldersIterator = subFolders.pageIterator();
                    fill(children, subFoldersIterator, iMaxSize_p, iVersionSelection_p);
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                {
                    DocumentSet containedDocuments = folder_p.get_ContainedDocuments();
                    PageIterator containedDocumentsIterator = containedDocuments.pageIterator();
                    fill(children, containedDocumentsIterator, iMaxSize_p, iVersionSelection_p);
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_CUSTOM:
                {
                    ReferentialContainmentRelationshipSet containees = folder_p.get_Containees();
                    //TODO : extract relationship head and select select CUSTOM objects 
                    PageIterator containeesIterator = containees.pageIterator();
                    fill(children, containeesIterator, iMaxSize_p, iVersionSelection_p);
                }
                    break;
                default:
                    LOG.debug("OwFNCM5Folder.retreiveChildren(): Unknown object type will be ignored for retrieval. objetType = " + iObjectTypes_p[i]);
            }
        }
        if (sort_p != null && !sort_p.getCriteriaCollection().isEmpty())
        {
            doClientSorting(children, sort_p);
        }
        return children;
    }

    /**
     * Sort the Result list of {@link #getChilds(int[], Collection, OwSort, int, int, OwSearchNode)},
     * since it is not possible to sort natively in ECM API.
     * @param col OwObjectCollection to sort
     * @param sortOrder OwSort sort order definition
     */
    protected void doClientSorting(OwObjectCollection col, OwSort sortOrder)
    {
        StopWatch stopWatch = new Log4JStopWatch("OwFNCM5Folder.doClientSorting");
        try
        {
            col.sort(sortOrder);
        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Sorting of collection failed", e);
            }
            else
            {
                LOG.debug("OwFNCM5Folder.doClientSorting: Could not sort collection, ex.msg = " + e.getMessage());
            }
        }
        stopWatch.stop();
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder = myself.getEngineObject();

        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                {
                    OwFNCM5NativeObjHelper.ensure(getNativeObject(), PropertyNames.SUB_FOLDERS);
                    FolderSet subFolders = folder.get_SubFolders();
                    return !subFolders.isEmpty();
                }
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                {
                    OwFNCM5NativeObjHelper.ensure(getNativeObject(), PropertyNames.CONTAINED_DOCUMENTS);
                    DocumentSet containedDocuments = folder.get_ContainedDocuments();
                    return !containedDocuments.isEmpty();
                }
                default: /*ignore*/
                    ;
            }
        }
        return false;
    }

    protected final void fill(OwObjectCollection collection_p, PageIterator pageIterator_p, int maxSize_p) throws OwException
    {
        fill(collection_p, pageIterator_p, maxSize_p, OwSearchTemplate.VERSION_SELECT_DEFAULT);
    }

    /**
     * Use the page iterator to select all the elements and create OwObject
     * and adding them to the given collection. 
     * @param collection_p OwObjectCollection to add the new OwObject
     * @param pageIterator_p PageIterator to use for browsing
     * @param maxSize_p int max size of collection
     * @throws OwException
     */
    protected void fill(OwObjectCollection collection_p, PageIterator pageIterator_p, int maxSize_p, int versionSelection_p) throws OwException
    {
        while (pageIterator_p.nextPage() && (maxSize_p <= 0 || collection_p.size() < maxSize_p))
        {
            Object[] children = pageIterator_p.getCurrentPage();
            for (int child = 0; collection_p.size() <= maxSize_p && child < children.length; child++)
            {
                IndependentlyPersistableObject ipObject = (IndependentlyPersistableObject) children[child];
                if (ipObject instanceof Versionable)
                {
                    Versionable versionable = (Versionable) ipObject;
                    switch (versionSelection_p)
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

                OwFNCM5Object<?> owObject = createOwObject(ipObject);
                collection_p.add(owObject);
            }
        }

    }

    public String getMIMEType() throws OwException
    {
        return OwMimeManager.MIME_TYPE_PREFIX_OW_FOLDER + getObjectClass().getClassName();
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

    @Override
    public OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException
    {
        //        if (OwResource.m_ObjectPathPropertyClass.getClassName().equals(propertyClass_p.getClassName()))
        //        {
        //            try
        //            {
        //                return new OwFNCM5SimpleProperty(getProperty(PropertyNames.PATH_NAME).getValue(), propertyClass_p);
        //            }
        //            catch (OwException owEx)
        //            {
        //                throw owEx;
        //            }
        //            catch (Exception e)
        //            {
        //                throw new OwServerException("Cannot retrieve path value for object", e);
        //            }
        //        }
        return super.createVirtualProperty(propertyClass_p);
    }

    @Override
    protected OwObjectCollection retrieveParentsCollection() throws OwException
    {
        OwObjectCollection parents = null;
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder = myself.ensure(PropertyNames.PARENT);

        Folder parent = folder.get_Parent();

        if (parent != null)
        {
            parents = new OwStandardObjectCollection();
            parents.add(createOwObject(parent));
        }
        return parents;
    }

    @Override
    public boolean setLock(boolean fLock_p) throws Exception
    {
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder = myself.getEngineObject();

        String owner = getUserShortName();
        final int timeout = Integer.MAX_VALUE;
        if (fLock_p)
        {
            folder.lock(timeout, owner);
        }
        else
        {
            folder.unlock();
        }

        return folder.isLocked();
    }

    @Override
    public boolean getLock(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder;
        if (iContext_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            folder = myself.refresh(LOCK_PROPERTIES);
        }
        else
        {
            folder = myself.ensure(LOCK_PROPERTIES);
        }
        return folder.isLocked();
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
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder;
        if (iContext_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            folder = myself.refresh(new String[] { PropertyNames.LOCK_OWNER });
        }
        else
        {
            folder = myself.ensure(PropertyNames.LOCK_OWNER);
        }

        String lockOwner = folder.get_LockOwner();
        return lockOwner;
    }

    @Override
    public String getPath() throws OwException
    {
        OwFNCM5EngineState<Folder> myself = getSelf();
        Folder folder = myself.getEngineObject();
        OwFNCM5NativeObjHelper.ensure(folder, PropertyNames.PATH_NAME);
        StringBuilder path = new StringBuilder("/");
        path.append(getResource().getSymbolicName());
        path.append(folder.get_PathName());
        return path.toString();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#move(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    @Override
    public void move(OwObject oObject_p, OwObject oldParent_p) throws OwException
    {
        // do not move if source and destination are identical
        if (this.equals(oldParent_p))
        {
            return;
        }
        try
        {
            // move it depending on type
            if (oObject_p.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
            {
                // === use FileNet move function for folders
                Folder subjectFolder = (Folder) oObject_p.getNativeObject();
                subjectFolder.move(this.getNativeObject());
                subjectFolder.save(RefreshMode.REFRESH);
            }
            else
            {
                // === do manual move on documents
                add(oObject_p);
                oldParent_p.removeReference(oObject_p);
            }
        }
        catch (EngineRuntimeException ere)
        {
            if (ere.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("It already exists an object with this name = " + oObject_p.getName(), ere);
                }
                throw new OwInvalidOperationException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.moveexists", "There already exists an object with this name:") + oObject_p.getName(), ere);
            }
            else if (ere.getExceptionCode() == ExceptionCode.SECURITY_INVALID_CREDENTIALS)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("You do not have the necessary access authorization to move this object, name = " + oObject_p.getName(), ere);
                }
                throw new OwAccessDeniedException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.movenoaccess", "You do not have the necessary authorization to move this object:") + oObject_p.getName(), ere);
            }
            else
            {
                throw ere;
            }
        }
        catch (OwException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            String msg = "Could not move object to folder.";
            LOG.error(msg, e);
            throw new OwServerException(msg, e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#add(com.wewebu.ow.server.ecm.OwObject)
     */
    @Override
    public void add(OwObject oObject_p) throws OwException
    {
        if (oObject_p.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFNCMFolderObject.add: You can only add documents to a folder/file.");
            }
            throw new OwInvalidOperationException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.addwrongtype", "You can only add documents to a folder/eFile."));
        }

        try
        {
            IndependentlyPersistableObject nativeObject = (IndependentlyPersistableObject) oObject_p.getNativeObject();
            OwFNCM5ContainmentName containmentName = new OwFNCM5ContainmentName(oObject_p.getName());
            ReferentialContainmentRelationship rcr = getNativeObject().file(nativeObject, AutoUniqueName.AUTO_UNIQUE, containmentName.toString(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
            rcr.save(RefreshMode.NO_REFRESH);
        }
        catch (EngineRuntimeException ere)
        {
            if (ere.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("It already exists an object with this name = " + oObject_p.getName(), ere);
                }
                throw new OwInvalidOperationException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.addexists", "There already exists an object with this name:") + oObject_p.getName(), ere);
            }
            else if (ere.getExceptionCode() == ExceptionCode.SECURITY_INVALID_CREDENTIALS)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("You do not have the necessary access authorization to add this object, name = " + oObject_p.getName(), ere);
                }
                throw new OwAccessDeniedException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.addnoaccess", "You do not have the necessary authorization to add this object:") + oObject_p.getName(), ere);
            }
            else
            {
                throw ere;
            }
        }
        catch (OwException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            String msg = "Could not add object to folder.";
            LOG.error(msg, e);
            throw new OwServerException(msg, e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#removeReference(com.wewebu.ow.server.ecm.OwObject)
     */
    @Override
    public void removeReference(OwObject oObject_p) throws OwException
    {
        if (oObject_p.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFNCM5FolderObject.removeReference: You can only remove documents from a folder/file. Can not remove references to folders.");
            }
            throw new OwInvalidOperationException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.removereferencewrongtype", "You can only remove documents from a folder/eFile."));
        }

        if (oObject_p instanceof OwFNCM5Containee)
        {
            OwFNCM5Containee containaee = (OwFNCM5Containee) oObject_p;
            containaee.removeReferenceFrom(this);
        }
        else
        {
            LOG.debug("OwFNCM5FolderObject.removeReference: You can only remove documents from a folder/file. Can not remove non-containees.");
            throw new OwInvalidOperationException(OwString.localize(getNetwork().getLocale(), "fncm.OwFNCMFolderObject.removereferencewrongtype", "You can only remove documents from a folder/eFile."));
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#canAdd(com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws OwException
    {
        return (oObject_p.getType() != OwObjectReference.OBJECT_TYPE_FOLDER);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#canMove(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws OwException
    {
        // only move if source and destination folders are different and oldParent_p is not null
        return ((null != oldParent_p) && (!equals(oldParent_p)));
    }

    @Override
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws OwException
    {
        return (oObject_p.getType() != OwObjectReference.OBJECT_TYPE_FOLDER);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else
        {
            if (obj instanceof OwFNCM5Folder)
            {
                OwFNCM5Folder f = (OwFNCM5Folder) obj;
                return getID().equals(f.getID());
            }
            else
            {
                return super.equals(obj);
            }
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 31;
        try
        {
            hash += getResourceID().hashCode();
        }
        catch (OwException e)
        {
            LOG.error("Hash cannot be calculated correct missing resource Id", e);
        }
        return hash + getID().hashCode();
    }
}
