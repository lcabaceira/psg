package com.wewebu.ow.server.plug.owshortcut;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwStandardObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.std.log.OwLog;

/**
 *<p>
 * Implementation of a shortcut item for object references.
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
public class OwShortCutItemOwObject extends OwShortCutItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutDocument.class);

    /** delimiter used to build the object path in the object tree*/
    public static final String DEFAULT_PATH_DELIMITER = "/";

    /** delimiter for the subpath in the persist string */
    protected static final String SUBPATH_DELIMITER = "!#!";

    /** the wrapped object reference */
    private OwObjectReference m_objref;

    /** the wrapped subpath object reference */
    private OwObjectReference m_subpathObjectRef;

    /** subpath to the opened folder */
    private String m_subpath;

    /** displayable subpath to the opened folder */
    private String m_displaysubpath;

    /** maximum number of child nodes */
    protected int m_iMaxChildSize = 50;

    /** reconstruct the short cut from the given string
     * @see #getPersistString
     * @param persistString_p
     * @param context_p
     * @param maxChildSize_p number of maximum children to be searched when retrieving the pointed object reference
     */
    protected OwShortCutItemOwObject(String persistString_p, OwShortCutItemContext context_p, int maxChildSize_p) throws Exception
    {
        this.m_iMaxChildSize = maxChildSize_p;
        String rest = persistString_p;

        // get subpath
        int index = rest.indexOf(SUBPATH_DELIMITER);

        if (-1 != index)
        {
            m_subpath = rest.substring(0, index);
            if (m_subpath.length() == 0)
            {
                m_subpath = null;
            }

            rest = rest.substring(index + SUBPATH_DELIMITER.length());
        }

        // get displayable subpath
        index = rest.indexOf(SUBPATH_DELIMITER);

        if (-1 != index)
        {
            m_displaysubpath = rest.substring(0, index);
            if (m_displaysubpath.length() == 0)
            {
                m_displaysubpath = null;
            }

            rest = rest.substring(index + SUBPATH_DELIMITER.length());
        }

        // deserialize shortcut from given string
        OwNetwork network = ((OwMainAppContext) context_p.getContext()).getNetwork();
        m_objref = new OwStandardObjectReference(rest, network);

        m_subpathObjectRef = null;

        if (m_subpath != null)
        {
            if (this.m_displaysubpath != null && m_objref.getResourceID() != null)
            {
                m_subpathObjectRef = new OwDependentObjectReference(m_objref, this.m_displaysubpath, network);
            }
            else
            {
                m_subpathObjectRef = findPathReference(m_objref, m_subpath, DEFAULT_PATH_DELIMITER);
            }
        }
    }

    /** creates a short cut from the given OwObject Reference
     * 
     * @param ref_p
     */
    protected OwShortCutItemOwObject(OwObjectReference ref_p)
    {
        m_objref = ref_p;
    }

    /**
     * Constructor
     * @param ref_p the root object reference 
     * @param path_p relative path to the pointed object
     * @param subdisplaypath_p the path to be displayed for this shortcut in the shortcut views
     * @param maxChildSize_p number of maximum children to be searched when retrieving the pointed object reference
     * @deprecated since 4.1.1.1 use {@link #OwShortCutItemOwObject(OwObjectReference, OwObjectReference, String, String)} instead 
     */
    @Deprecated
    public OwShortCutItemOwObject(OwObjectReference ref_p, String path_p, String subdisplaypath_p, int maxChildSize_p)
    {
        this.m_iMaxChildSize = maxChildSize_p;
        this.m_objref = ref_p;
        this.m_subpath = path_p;

        this.m_subpathObjectRef = null;
        this.m_displaysubpath = subdisplaypath_p;
        if (m_subpath != null)
        {

            try
            {
                m_subpathObjectRef = findPathReference(ref_p, path_p, DEFAULT_PATH_DELIMITER);
            }
            catch (Exception e)
            {
                try
                {
                    LOG.error("OwShortCutItemOwObject.OwShortCutItemOwObject(): could not find path reference for path " + m_subpath + " with root DMSID " + m_objref.getDMSID(), e);
                }
                catch (Exception e1)
                {
                    LOG.error("OwShortCutItemOwObject.OwShortCutItemOwObject(): could not get object reference DMSID.", e);
                }
            }
        }
    }

    /**
     * Constructor for path specific definition 
     * @param root OwObjectReference which is the root of the path
     * @param target OwObjectReference which is descendant of root (level unknown)
     * @param displayPath String readable/displayable path (may be used for retrieval)
     * @param idPath String path based on Id's
     * @since 4.1.1.1
     */
    public OwShortCutItemOwObject(OwObjectReference root, OwObjectReference target, String displayPath, String idPath)
    {
        this.m_objref = root;
        this.m_subpath = idPath;

        this.m_subpathObjectRef = target;
        this.m_displaysubpath = displayPath;
    }

    /**
     * 
     * @param root_p the root object
     * @param path_p the relative path to search the reference in
     * @param pathDelimiter_p the delimiter used in constructing the path
     * @return an {@link OwObjectReference} for the object found under the given path relative to the given root object
     * @throws Exception
     * @deprecated since 4.1.1.1 searching by path must be done in upper level (business logic)
     */
    @Deprecated
    protected OwObjectReference findPathReference(OwObjectReference root_p, String path_p, String pathDelimiter_p) throws Exception
    {

        LOG.warn("!Performance! Retrieval of ShortCut item by path can slow down login.");
        StringTokenizer pathTokenizer = new StringTokenizer(path_p, pathDelimiter_p);

        OwObject pathObject = root_p.getInstance();

        while (pathTokenizer.hasMoreTokens())
        {
            OwObject pathObjectChild = null;
            String pathToken = pathTokenizer.nextToken();
            OwObjectCollection children = pathObject.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_CUSTOM }, null, null, m_iMaxChildSize, 0, null);
            Iterator childIt = children.iterator();
            while (childIt.hasNext())
            {
                OwObject child = (OwObject) childIt.next();
                if (child.getID().equals(pathToken))
                {
                    pathObjectChild = child;
                    break;
                }
            }

            if (pathObjectChild != null)
            {
                pathObject = pathObjectChild;
            }
            else
            {
                //no child with the given ID was found
                return null;
            }
        }
        return pathObject;
    }

    /** get the name of the shortcut 
     * 
     * @return the name <code>String</code>
     */
    public String getName()
    {
        if (m_subpath != null)
        {
            return m_objref.getName() + m_displaysubpath;
        }
        else
        {
            return m_objref.getName();
        }
    }

    /** Get the ID of the shortcut.
     * The ID is generated using the .hashCode() method,
     * if a folder is add to shortcut also the path to the
     * folder is hashed.
     * 
     * @return String defining the ID of current shortcut
     * @throws Exception 
     */
    public String getId() throws Exception
    {
        // we use the ID as AttributeBag name and the pure DMSID might be too long
        if (m_subpath != null)
        {
            return Integer.toString(m_objref.getDMSID().hashCode()) + m_subpath.hashCode();
        }
        else
        {
            return Integer.toString(m_objref.getDMSID().hashCode());
        }
    }

    /** create a collection of OwShortCutItem's from the given collection of OwObject's
     * 
     * @param objects_p Collection of {@link OwObjectReference}s
     * @return Collection of OwShortCutItem
     */
    public static synchronized Collection createShortCutItems(Collection objects_p)
    {
        Collection ret = new LinkedList();

        Iterator it = objects_p.iterator();
        while (it.hasNext())
        {
            OwObjectReference obj = (OwObjectReference) it.next();

            ret.add(new OwShortCutItemOwObject(obj));
        }

        return ret;
    }

    /** create a OwShortCutItem of OwShortCutItem from the given collection of OwObject
     * 
     * @param object_p
     * @param path_p
     * @param subdisplaypath_p 
     * @param maxChildSize_p 
     * @return the newly created {@link OwShortCutItem}
     * @deprecated since 4.1.1.1 caused by performance problems, use direct constructor {@link #OwShortCutItemOwObject(OwObjectReference, OwObjectReference, String, String)} instead
     */
    @Deprecated
    public static OwShortCutItem createShortCutItem(OwObjectReference object_p, String path_p, String subdisplaypath_p, int maxChildSize_p)
    {
        return new OwShortCutItemOwObject(object_p, path_p, subdisplaypath_p, maxChildSize_p);
    }

    /** get a string that persists the short cut. Used in string constructor. 
     * @throws Exception */
    protected String getPersistString() throws Exception
    {
        StringBuilder ret = new StringBuilder();

        if (null != m_subpath)
        {
            ret.append(m_subpath);
        }

        ret.append(SUBPATH_DELIMITER);

        if (null != m_displaysubpath)
        {
            ret.append(m_displaysubpath);
        }

        ret.append(SUBPATH_DELIMITER);

        ret.append(OwStandardObjectReference.getCompleteReferenceString(m_objref, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL));

        return ret.toString();
    }

    /** get the type of the short cut as defined in OwClipboard.CONTENT_TYPE_OW_... */
    public int getType()
    {
        return OwClipboard.CONTENT_TYPE_OW_OBJECT;
    }

    /** render a icon link for this short cut */
    public void insertIcon(OwShortCutItemContext context_p, Writer w_p) throws Exception
    {
        context_p.getMimeManager().insertIconLink(w_p, m_objref, m_subpath);
    }

    /** render a label link for this short cut */
    public void insertLabel(OwShortCutItemContext context_p, Writer w_p) throws Exception
    {
        context_p.getMimeManager().insertTextLink(w_p, getName(), m_objref, m_subpath);
    }

    /**
     * 
     * @return a reference of the pointed object
     */
    public OwObjectReference getObjRef()
    {
        if (m_subpath != null)
        {
            return m_subpathObjectRef;
        }
        else
        {
            return m_objref;
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            buffer.append("OwShortCutItemOwObject[");
            if (null != m_objref)
            {
                buffer.append("\n- m_objref:DMSID=").append(m_objref.getDMSID());
                buffer.append("\n- m_objref:Name =").append(m_objref.getName());
                buffer.append("\n- PersistString =");
                try
                {
                    buffer.append(getPersistString());
                }
                catch (Exception ex)
                {
                    buffer.append(ex.getMessage());
                }
            }
            else
            {
                buffer.append("\n- m_objref: null");
                buffer.append("\n]");
            }
        }
        catch (Exception e)
        {
            LOG.error("OwShortCutItemOwObject.toString():could not create shortcut string!", e);
        }
        return buffer.toString();
    }

    @Override
    public void refresh(OwRepository repository_p) throws OwException
    {
        try
        {
            //update the persistence id
            if (persistentId == null)
            {
                persistentId = getId();
            }
            //update object reference.
            if (m_subpath != null)
            {
                m_subpathObjectRef = updateObjectReference(repository_p, m_subpathObjectRef);
            }
            else
            {
                if (m_objref != null)
                {
                    OwObjectReference objectReference = updateObjectReference(repository_p, m_objref);
                    m_objref = objectReference;
                }
            }
        }
        catch (OwException owex)
        {
            throw owex;
        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Cannot refresh object, Invalid object reference", e);
            }
            throw new OwShortCutException("Invalid object reference", e);
        }
    }

    /**
     * Get the {@link OwStandardObjectReference} corresponding to the latest version of the {@link OwObject} 
     * @param repository_p
     * @param originalReference_p
     * @return the {@link OwStandardObjectReference} updated.
     * @throws Exception
     * @since 3.1.0.3
     */
    protected OwObjectReference updateObjectReference(OwRepository repository_p, OwObjectReference originalReference_p) throws Exception
    {
        OwObjectReference result = originalReference_p;
        if (result != null)
        {
            OwObject object = originalReference_p.getInstance();
            object = replaceWithLatestVersion(object);
            String refString = OwStandardObjectReference.getCompleteReferenceString(object, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
            result = new OwStandardObjectReference(refString, repository_p);
        }
        return result;
    }

    /**
     * Replace the given {@link OwObject} source object with its latest version 
     * @param source_p - the stored {@link OwObject} source.
     * @return - the latest version of the stored object, or the given {@link OwObject} if no version is available.
     * @throws OwException 
     * @since 3.1.0.3
     */
    protected OwObject replaceWithLatestVersion(OwObject source_p) throws OwException
    {
        OwObject result = source_p;

        try
        {
            if (result.hasVersionSeries())
            {
                OwVersionSeries versionSeries = result.getVersionSeries();
                if (versionSeries != null)
                {
                    OwVersion latestVersion = versionSeries.getLatest();
                    if (latestVersion != null)
                    {
                        result = versionSeries.getObject(latestVersion);
                    }

                }
            }
        }
        catch (OwObjectNotFoundException nfe)
        {
            throw nfe;
        }
        catch (Exception e)
        {
            LOG.error("Cannot get the last version", e);
        }
        return result;
    }

    /**
     *<p>
     * Helper class for handling dependent object reference.
     * Will use the parent/root object reference and display path
     * to resolve linked/favorited object.
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
     *@since 4.1.1.1
     */
    protected static class OwDependentObjectReference implements OwObjectReference
    {
        private OwObjectReference parentRef;
        private String subPath;
        private OwRepository repo;
        private OwObject instance;

        public OwDependentObjectReference(OwObjectReference parentRef, String subPath, OwRepository repo)
        {
            this.parentRef = parentRef;
            this.subPath = subPath;
            this.repo = repo;
        }

        @Override
        public String getResourceID() throws Exception
        {
            return parentRef.getResourceID();
        }

        @Override
        public OwObject getInstance() throws Exception
        {
            if (instance == null)
            {
                String parentPath = parentRef.getInstance().getPath();
                if (parentPath.endsWith("/"))
                {
                    instance = this.repo.getObjectFromPath(parentPath + this.subPath.substring(1), false);
                }
                else
                {
                    instance = this.repo.getObjectFromPath(parentPath + this.subPath, false);
                }
            }
            return instance;
        }

        @Override
        public String getName()
        {
            try
            {
                return getInstance().getName();
            }
            catch (Exception e)
            {
                String msg = "Cannot get name from referenced object, returning path info instead. path = " + this.subPath;
                if (LOG.isDebugEnabled())
                {
                    LOG.debug(msg, e);
                }
                else
                {
                    LOG.warn(msg);
                }
                return this.subPath;
            }
        }

        @Override
        public String getID()
        {
            try
            {
                return getInstance().getID();
            }
            catch (Exception e)
            {
                LOG.warn("Cannot get Id from referenced Object", e);
                return "";
            }
        }

        @Override
        public int getType()
        {
            try
            {
                return getInstance().getType();
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Could not get instance from repository, returning undefined type", e);
                }
                else
                {
                    LOG.info("Not able to get Type information from instance, returning undefined type");
                }
                return OwObjectReference.OBJECT_TYPE_UNDEFINED;
            }
        }

        @Override
        public String getDMSID() throws Exception
        {
            return getInstance().getDMSID();
        }

        @Override
        public int getPageCount() throws Exception
        {
            return 0;
        }

        @Override
        public String getMIMEType() throws Exception
        {
            return getInstance().getMIMEType();
        }

        @Override
        public String getMIMEParameter() throws Exception
        {
            return getInstance().getMIMEParameter();
        }

        @Override
        public boolean hasContent(int iContext_p) throws Exception
        {
            return getInstance().hasContent(iContext_p);
        }

    }
}