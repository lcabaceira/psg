package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.client.api.TransientFileableCmisObject;
import org.apache.chemistry.opencmis.client.api.TransientFolder;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.collections.OwFilteringIterable;
import com.wewebu.ow.server.collections.OwItemFilter;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositorySession;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISObjectIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * CMIS base-type "cmis:folder" dependent implementation.
 * Class representing a Folder in CMIS environments.
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
public class OwCMISFolderObject extends OwCMISAbstractNativeObject<TransientFolder, FolderType, OwCMISFolderClass> implements OwCMISFolder
{
    private static final Logger LOG = OwLog.getLogger(OwCMISFolderObject.class);

    public OwCMISFolderObject(OwCMISNativeSession session_p, TransientFolder nativeObject_p, OperationContext creationContext, OwCMISFolderClass class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwStandardObjectCollection children = new OwStandardObjectCollection();
        OwCMISNativeSession mySession = getSession();
        StopWatch stopWatchTotal = new Log4JStopWatch("OwCMISFolderObject.getChilds", "total");
        for (int iType = 0; iType < objectTypes_p.length && children.size() <= maxSize_p; iType++)
        {
            int pageSize;
            if (children.size() == 0 && maxSize_p == Integer.MAX_VALUE)
            {
                pageSize = maxSize_p;
            }
            else
            {
                pageSize = (maxSize_p - children.size()) + 1;
            }
            if (pageSize <= 0)
            {
                break;
            }
            switch (objectTypes_p[iType])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                {
                    OperationContext context = mySession.createOperationContext(propertyNames_p, sort_p, pageSize, getSession().getNativeObjectClass(ObjectType.FOLDER_BASETYPE_ID));
                    RepositoryCapabilities repoCap = mySession.getOpenCMISSession().getRepositoryInfo().getCapabilities();
                    if (repoCap.isGetFolderTreeSupported() && sort_p == null)
                    {
                        StopWatch stopWatch = new Log4JStopWatch();
                        List<Tree<FileableCmisObject>> folderTree = getNativeObject().getFolderTree(1, context);
                        stopWatch.lap("OwCMISFolderObject.getChilds", "getFolderTree(1, folder)");

                        for (Tree<FileableCmisObject> tree : folderTree)
                        {
                            FileableCmisObject item = tree.getItem();
                            OwCMISNativeObject<TransientFileableCmisObject> object = mySession.from((TransientFileableCmisObject) item.getTransientObject(), null);
                            children.add(object);
                        }
                        stopWatch.stop("OwCMISFolderObject.getChilds", "createOwObjects");
                    }
                    else
                    {
                        StopWatch stopWatch = new Log4JStopWatch("OwCMISFolderObject.getChilds", "folders");
                        ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                        boolean finished = false;
                        int count = 0;
                        while (!finished)
                        {
                            ItemIterable<CmisObject> currentPage = nativeChildren.skipTo(count).getPage();
                            for (CmisObject child : currentPage)
                            {
                                TransientCmisObject transinetChild = child.getTransientObject();
                                OwCMISNativeObjectClass<?, TransientCmisObject> childClass = mySession.classOf(transinetChild);
                                if (childClass.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
                                {
                                    OwCMISNativeObject<TransientCmisObject> childObject = mySession.from(transinetChild, null);

                                    children.add(childObject);
                                }
                                count++;
                            }
                            if (!currentPage.getHasMoreItems() || children.size() > maxSize_p)
                            {
                                finished = true;
                            }
                        }
                        stopWatch.stop();
                    }
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                {
                    OperationContext context = mySession.createOperationContext(propertyNames_p, sort_p, pageSize, getSession().getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));
                    StopWatch stopWatch = new Log4JStopWatch("OwCMISFolderObject.getChilds", "documents");
                    ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                    boolean finished = false;
                    int count = 0;
                    while (!finished)
                    {
                        ItemIterable<CmisObject> currentPage = nativeChildren.skipTo(count).getPage();
                        for (CmisObject child : currentPage)
                        {
                            if (child != null)
                            {
                                TransientCmisObject transinetChild = child.getTransientObject();
                                OwCMISNativeObjectClass<?, TransientCmisObject> childClass = mySession.classOf(transinetChild);
                                if (childClass.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
                                {
                                    OwCMISNativeObject<TransientCmisObject> childObject = mySession.from(transinetChild, null);
                                    children.add(childObject);
                                }
                            }
                            else
                            {
                                LOG.warn("Returned child object is null");
                            }
                            count++;
                        }
                        if (!currentPage.getHasMoreItems() || children.size() > maxSize_p)
                        {
                            finished = true;
                        }
                    }
                    stopWatch.stop();
                }
                    break;
                case OwObjectReference.OBJECT_TYPE_LINK:
                    children.addAll(super.getChilds(objectTypes_p, propertyNames_p, sort_p, pageSize, versionSelection_p, filterCriteria_p));
                    break;
                default:
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwCMISFolderObject.getChilds: Unsupported type requested, type = " + objectTypes_p[iType]);
                    }
            }
        }
        stopWatchTotal.stop();

        //FIXME Sorting
        OwStandardObjectCollection result = null;
        if (children.size() > maxSize_p)
        {
            result = new OwStandardObjectCollection();
            result.addAll(children.subList(0, maxSize_p));
            result.setComplete(false);
            try
            {
                result.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
                result.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, result.size());
            }
            catch (Exception e)
            {
                throw new OwServerException("Could not set " + OwObjectCollection.ATTRIBUTE_IS_COMPLETE + " attribute.", e);
            }
        }
        else
        {
            result = children;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPaginableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public OwIterable<OwCMISObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        OwCMISRepositorySession mySession = (OwCMISRepositorySession) getSession();
        if (loadContext.includeFolders() && loadContext.includeDocuments())
        {
            //All content in this folder
            OwSort sorting = loadContext.getSorting();
            OperationContext context = mySession.createOperationContext(loadContext.getPropertyNames(), sorting, loadContext.getPageSize(), getSession().getNativeObjectClass(ObjectType.FOLDER_BASETYPE_ID),
                    getSession().getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));
            ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
            return new OwCMISObjectIterable(nativeChildren, mySession);
        }
        else if (loadContext.includeDocuments())
        {
            OwSort sorting = loadContext.getSorting();
            OperationContext context = mySession.createOperationContext(loadContext.getPropertyNames(), sorting, loadContext.getPageSize(), getSession().getNativeObjectClass(ObjectType.FOLDER_BASETYPE_ID),
                    getSession().getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));
            ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
            OwCMISObjectIterable iterable = new OwCMISObjectIterable(nativeChildren, mySession);
            return new OwFilteringIterable<OwCMISObject>(iterable, new OwItemFilter<OwCMISObject>() {

                @Override
                public boolean accept(OwCMISObject obj)
                {
                    return obj.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT;
                }
            });
        }
        else if (loadContext.includeFolders())
        {
            OwSort sorting = loadContext.getSorting();
            OperationContext context = mySession.createOperationContext(loadContext.getPropertyNames(), sorting, loadContext.getPageSize(), getSession().getNativeObjectClass(ObjectType.FOLDER_BASETYPE_ID),
                    getSession().getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));
            ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
            OwCMISObjectIterable iterable = new OwCMISObjectIterable(nativeChildren, mySession);
            return new OwFilteringIterable<OwCMISObject>(iterable, new OwItemFilter<OwCMISObject>() {

                @Override
                public boolean accept(OwCMISObject obj)
                {
                    return obj.getType() == OwObjectReference.OBJECT_TYPE_FOLDER;
                }
            });
        }

        return super.getChildren(loadContext);
    }

    @Override
    public boolean hasChilds(int[] objectTypes_p, int context_p) throws OwException
    {
        if (Boolean.getBoolean("com.wewebu.owd.opencmis.folder.hasChilds"))
        {
            StopWatch stopWatch = new Log4JStopWatch();
            OwCMISNativeSession mySession = getSession();
            for (int iType = 0; iType < objectTypes_p.length; iType++)
            {
                switch (objectTypes_p[iType])
                {
                    case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                    case OwObjectReference.OBJECT_TYPE_FOLDER:
                    {
                        OperationContext context = mySession.createOperationContext(null, 10);
                        RepositoryCapabilities repoCap = mySession.getOpenCMISSession().getRepositoryInfo().getCapabilities();
                        if (repoCap.isGetFolderTreeSupported())
                        {
                            context.setMaxItemsPerPage(1);
                            List<Tree<FileableCmisObject>> folderTree = getNativeObject().getFolderTree(1, context);
                            stopWatch.stop("OwCMISFolderObject.hasChilds", "end getFolderTree");
                            return !folderTree.isEmpty();
                        }
                        else
                        {
                            ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                            boolean hasChild = nativeChildren.iterator().hasNext();
                            if (hasChild)
                            {
                                for (CmisObject child : nativeChildren)
                                {
                                    TransientCmisObject transinetChild = child.getTransientObject();
                                    OwCMISNativeObjectClass<?, TransientCmisObject> childClass = mySession.classOf(transinetChild);
                                    if (childClass.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
                                    {
                                        stopWatch.stop("OwCMISFolderObject.hasChilds", "end getChildren(Folder)");
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                        break;
                    case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                    case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                    {
                        OperationContext context = mySession.createOperationContext(null, 50, getSession().getNativeObjectClass(ObjectType.DOCUMENT_BASETYPE_ID));
                        context.setIncludeAcls(false);
                        context.setIncludeAllowableActions(false);
                        context.setIncludePathSegments(false);
                        context.setIncludeRelationships(IncludeRelationships.NONE);
                        ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                        boolean hasChild = nativeChildren.iterator().hasNext();
                        if (hasChild)
                        {
                            for (CmisObject child : nativeChildren)
                            {
                                TransientCmisObject transinetChild = child.getTransientObject();
                                OwCMISNativeObjectClass<?, TransientCmisObject> childClass = mySession.classOf(transinetChild);
                                if (childClass.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
                                {
                                    stopWatch.stop("OwCMISFolderObject.hasChilds", "end getChildren(Document)");
                                    return true;
                                }
                            }
                        }
                    }
                        break;
                    case OwObjectReference.OBJECT_TYPE_LINK:
                    {
                        if (super.hasChilds(objectTypes_p, context_p))
                        {
                            stopWatch.stop("OwCMISFolderObject.hasChilds", "end getChildren(Relationship)");
                            return true;
                        }

                    }
                        break;
                    default:
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwCMISFolderObject.hasChilds: Unsupported type requested, type = " + objectTypes_p[iType]);
                        }
                }
            }
            stopWatch.stop("OwCMISFolderObject.hasChilds", "endNoChild");
            return false;
        }
        return true;
    }

    @Override
    public final OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    @Override
    public final OwVersion getVersion() throws Exception
    {
        return null;
    }

    @Override
    public void removeReference(OwObject object_p) throws OwException
    {
        if (object_p instanceof OwCMISAbstractNativeObject)
        {
            OwCMISAbstractNativeObject<?, ?, ?> owNatObj = (OwCMISAbstractNativeObject<?, ?, ?>) object_p;
            TransientCmisObject natObj = owNatObj.getNativeObject();
            if (natObj instanceof TransientFileableCmisObject)
            {
                if (object_p.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
                {
                    TransientFileableCmisObject fileableObj = (TransientFileableCmisObject) natObj;
                    fileableObj.removeFromFolder(getNativeObject());
                }
                else
                {
                    Folder folder = (Folder) natObj;
                    String folderId = folder.getId();
                    List<String> faildToDelete = folder.deleteTree(true, UnfileObject.UNFILE, true);
                    if (faildToDelete != null && !faildToDelete.isEmpty())
                    {//maybe an exception have to be thrown, assuming CMIS back-end have more information and log 
                        LOG.warn("OwCMISFolderObject.removeReference: Not all objects could be deleted, failed to delete " + faildToDelete.size() + " objects in " + folderId);
                        if (LOG.isDebugEnabled())
                        {
                            StringBuilder missedObjs = new StringBuilder();
                            for (String id : faildToDelete)
                            {
                                if (missedObjs.length() > 0)
                                {
                                    missedObjs.append("; ");
                                }
                                missedObjs.append(id);
                            }
                            LOG.debug("OwCMISFolderObject.removeReference: ObjectId's which are not deleted = " + missedObjs);
                        }
                    }
                }
            }
            else
            {
                LOG.error("OwCMISFolderObject.removeReference: Unsupported/Invalid native object, java-class = " + natObj.getClass());
                throw new OwInvalidOperationException(new OwString1("opencmis.OwCMISFolderObject.removeRef.err.invalidNativeType", "The provided type %1 is not fileable.", natObj.getClass().getCanonicalName()));
            }
        }
        else
        {
            LOG.warn("OwCMISFolderObject.removeReference: Unsupported/Invalid OwObject, java-class = " + object_p.getClass());
            throw new OwNotSupportedException(new OwString("opencmis.OwCMISFolderObject.removeRef.err.unsupportedType", "The provided type is not supported by the Add-process."));
        }
    }

    @Override
    public void add(OwObject object_p) throws OwException
    {
        if (object_p instanceof OwCMISNativeObject)
        {
            OwCMISNativeObject<?> owNatObj = (OwCMISNativeObject<?>) object_p;
            TransientCmisObject natObj = owNatObj.getNativeObject();
            if (natObj instanceof TransientFileableCmisObject)
            {
                TransientFileableCmisObject fileableObj = (TransientFileableCmisObject) natObj;
                fileableObj.addToFolder(getNativeObject(), true);
            }
            else
            {
                LOG.error("OwCMISFolderObject.add: Process of unfileable object was ignored! native object java-class = " + natObj.getClass());
                throw new OwInvalidOperationException(new OwString1("opencmis.OwCMISFolderObject.add.err.invalidNativeType", "The provided type %1 is not fileable.", natObj.getClass().getCanonicalName()));
            }
        }
        else
        {
            LOG.error("OwCMISFolderObject.add: Invalid/unsupported object for add-operation! object java-class = " + object_p.getClass());
            throw new OwNotSupportedException(new OwString("opencmis.OwCMISFolderObject.add.err.unsupportedType", "The provided type is not supported by the Add-process."));
        }
    }

    public boolean canRemoveReference(OwObject object_p, int context_p) throws OwException
    {
        if (object_p.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
        {
            RepositoryInfo repoInfo = getSession().getOpenCMISSession().getRepositoryInfo();
            if (Boolean.TRUE.equals(repoInfo.getCapabilities().isMultifilingSupported()))
            {
                if (OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS == context_p)
                {
                    if (object_p instanceof OwCMISAbstractNativeObject)
                    {
                        OwCMISAbstractNativeObject<?, ?, ?> owNatObj = (OwCMISAbstractNativeObject<?, ?, ?>) object_p;
                        return owNatObj.getNativeObject() instanceof FileableCmisObject;
                    }
                }
                else
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return super.canRemoveReference(object_p, context_p);
        }
    }

    @Override
    public boolean canAdd(OwObject object_p, int context_p) throws OwException
    {
        if (context_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            String objID = object_p.getClassName();
            OwCMISProperty<?> prop = getProperty(PropertyIds.ALLOWED_CHILD_OBJECT_TYPE_IDS);
            String[] childIDs = (String[]) prop.getValue();
            if (childIDs != null && childIDs.length > 0)
            {
                try
                {
                    for (String id : childIDs)
                    {
                        if (id != null && isSubtypeOf(objID, id))
                        {
                            return true;
                        }
                    }
                }
                catch (OwObjectNotFoundException e)
                {
                    //id could not be found, maybe different repository
                }
                return false;
            }
            else
            {// we don't have any limitations, allow add operation
                return true;
            }
        }
        else
        {//it's time critical, so we allow filing and maybe get error on addObject call
            return true;
        }
    }

    /**
     * Helper Method which will recursively traverse bottom-up the
     * object-class/-type tree, searching for matching Id's.
     * @param typeId String current id/symbolic name of type
     * @param isParentId String parent type id/symbolic name
     * @return boolean true if both are equals, or typeId is sub type of parent id.
     * @throws OwException could not find object type or parent object type
     */
    protected boolean isSubtypeOf(String typeId, String isParentId) throws OwException
    {
        if (typeId.equals(isParentId))
        {
            return true;
        }
        else
        {
            OwCMISObjectClass clazz = getSession().getObjectClass(typeId);
            OwCMISObjectClass pClass = clazz.getParent();
            if (pClass == null)
            {
                return false;
            }
            else
            {
                return isSubtypeOf(pClass.getClassName(), isParentId);
            }
        }
    }

    @Override
    public void delete() throws OwException
    {
        TransientFolder nativeObject = getNativeObject();
        nativeObject.deleteTree(true, UnfileObject.DELETE, true);
        try
        {
            nativeObject.save();
        }
        catch (NullPointerException e)
        {
            //FIXME: Do Nothing bug in OpenCMIS 0.8.0 version, throwing NullPointerException if delete was successful (only if connection is AtomPub and using TransientFolder)
            //Transient-Object are deprecated since OpenCMIS 0.9.0-Beta1, therefore remove catch after upgrade/refactoring to newer OpenCMIS version
        }
        catch (CmisConstraintException e)
        {
            LOG.error("OwCMISFolderObject.delete(): could not delete the folder tree objects", e);
            throw new OwServerException(new OwString("opencmis.OwCMISFolderObject.err.delete.tree", "Could not delete all of the folder tree elements!"), e);
        }
    }

    @Override
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        //void
    }

    @Override
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public String getPath() throws OwException
    {
        StringBuilder path = new StringBuilder(OwObject.STANDARD_PATH_DELIMITER);
        path.append(getResourceID());
        path.append(getProperty(PropertyIds.PATH).getValue());
        return path.toString();
    }

    @Override
    public boolean canMove(OwObject object_p, OwObject oldParent_p, int context_p) throws OwException
    {
        try
        {
            if (getResourceID().equals(object_p.getResourceID()))
            {
                return canAdd(object_p, context_p);
            }
            else
            {
                return super.canMove(object_p, oldParent_p, context_p);
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Cannot get repository/resource id from object which should be moved", e);
            throw new OwServerException("Failed to retrieve repository id from object which should be moved", e);
        }
    }

    @Override
    public void move(OwObject object_p, OwObject oldParent_p) throws OwException
    {
        Object objToMove = null;
        try
        {
            objToMove = object_p.getNativeObject();
        }
        catch (Exception e)
        {
            throw new OwServerException("Cannot access object to move", e);
        }

        if (objToMove instanceof TransientFileableCmisObject)
        {
            FileableCmisObject obj = (FileableCmisObject) ((TransientFileableCmisObject) objToMove).getCmisObject();
            FileableCmisObject result = obj.move(getObjectAsId(oldParent_p), getObjectAsId(this));
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Moved object resulting id = " + result.getId());
            }
            if (object_p instanceof OwCMISAbstractNativeObject)
            {
                OwCMISAbstractNativeObject movedObject = ((OwCMISAbstractNativeObject) object_p);
                movedObject.replaceNativeObject(result.getId(), movedObject.getTransientObject().getTransientContext());
            }
        }
        else
        {
            super.move(object_p, oldParent_p);
        }
    }

    /**
     * Helper method to transform OwObject to native ObjectId representation.
     * @param object OwObject
     * @return ObjectId or null
     * @throws OwException if native object cannot be accessed or handled by method.
     * @since 4.1.1.1
     */
    protected ObjectId getObjectAsId(OwObject object) throws OwException
    {
        if (object != null)
        {
            Object nativeObj;
            try
            {
                nativeObj = object.getNativeObject();
            }
            catch (Exception e)
            {
                throw new OwServerException("Cannot access object to move", e);
            }
            if (nativeObj instanceof ObjectId)
            {
                return (ObjectId) nativeObj;
            }
            else
            {
                throw new OwInvalidOperationException("Invalid parameter object, native representation must implement ObjectId interface");
            }
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection getParents() throws OwException
    {
        OwObjectCollection col = null;
        if (null != getProperty(PropertyIds.PARENT_ID).getValue())
        {
            col = new OwStandardObjectCollection();
            List<Folder> folders = getNativeObject().getParents();
            for (Folder f : folders)
            {
                OwCMISNativeObject<TransientFolder> childObject = getSession().from(f.getTransientFolder(), null);
                col.add(childObject);
            }
        }
        return col;
    }

    /**
     * Helper to get info about requested types.
     * @param types int array of requested types
     * @param type int checking if such type is requested
     * @return boolean true only if contained in types collection
     * @since 4.1.1.1
     */
    protected boolean isRequested(int[] types, int type)
    {
        boolean contains = false;
        if (types != null)
        {
            for (int inTypes : types)
            {
                contains = (inTypes & 0xf000) == type;
            }
        }
        return contains;
    }

    @Override
    public int getChildCount(int[] objectTypes_p, int context_p) throws OwException
    {
        if (Boolean.getBoolean("com.wewebu.owd.opencmis.folder.getChildCount"))
        {
            StopWatch stopWatch = new Log4JStopWatch();
            OwCMISNativeSession mySession = getSession();
            int resultCount = 0;
            boolean folder, doc;
            folder = isRequested(objectTypes_p, OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS);
            doc = isRequested(objectTypes_p, OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS);

            OperationContext context = mySession.getOpenCMISSession().createOperationContext();
            context.setFilterString("cmis:name,cmis:objectId,cmis:objectTypeId,cmis:objectBaseTypeId");
            context.setIncludeAllowableActions(false);
            context.setMaxItemsPerPage(50);
            if (folder && doc)
            {
                ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                resultCount = (int) nativeChildren.getTotalNumItems();
                stopWatch.lap("OwCMISFolderObject.getChildCount", "getChildren(pageSize =" + context.getMaxItemsPerPage() + ", folder, doc)");
            }
            else
            {
                if (folder)
                {

                    RepositoryCapabilities repoCap = mySession.getOpenCMISSession().getRepositoryInfo().getCapabilities();
                    if (repoCap.isGetFolderTreeSupported())
                    {
                        context.setMaxItemsPerPage(25);
                        List<Tree<FileableCmisObject>> folderTree = getNativeObject().getFolderTree(1, context);
                        resultCount = folderTree.size();
                        stopWatch.lap("OwCMISFolderObject.getChildCount", "getFolderTree(1, folder)");
                    }
                    else
                    {
                        ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                        int skipCount = 0;
                        boolean hasMore;
                        do
                        {
                            ItemIterable<CmisObject> currentPage = nativeChildren.skipTo(skipCount).getPage();
                            for (CmisObject child : currentPage)
                            {
                                if (BaseTypeId.CMIS_FOLDER.equals(child.getBaseTypeId()))
                                {
                                    resultCount = resultCount + 1;
                                }
                                skipCount++;
                            }
                            hasMore = currentPage.getHasMoreItems();
                        } while (hasMore);
                        stopWatch.lap("OwCMISFolderObject.getChildCount", "getChildren(pageSize =" + context.getMaxItemsPerPage() + ", folder)");
                    }
                }
                else
                {
                    ItemIterable<CmisObject> nativeChildren = getNativeObject().getChildren(context);
                    int skipCount = 0;
                    boolean hasMore;
                    do
                    {
                        ItemIterable<CmisObject> currentPage = nativeChildren.skipTo(skipCount).getPage();
                        for (CmisObject child : currentPage)
                        {
                            if (BaseTypeId.CMIS_FOLDER.equals(child.getBaseTypeId()))
                            {
                                resultCount = resultCount + 1;
                            }
                            skipCount++;
                        }
                        hasMore = currentPage.getHasMoreItems();
                    } while (hasMore);
                    stopWatch.lap("OwCMISFolderObject.getChildCount", "getChildren(pageSize =" + context.getMaxItemsPerPage() + ", folder)");
                }
            }
            stopWatch.stop();
            return resultCount;
        }
        else
        {
            return super.getChildCount(objectTypes_p, context_p);
        }
    }
}