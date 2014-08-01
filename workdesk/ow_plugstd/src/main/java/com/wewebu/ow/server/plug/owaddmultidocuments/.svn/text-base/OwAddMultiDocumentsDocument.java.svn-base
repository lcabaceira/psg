package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Main document class for multi file upload, 
 * which also hold the skeleton object for meta-data handling and
 * methods for handling of DocumentImporter-Callbacks.
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
public class OwAddMultiDocumentsDocument extends OwDocument implements OwDocumentImporterCallback, OwObjectClassView.OwObjectClassViewListner, OwLocation
{

    private static final Logger LOG = OwLog.getLogger(OwAddMultiDocumentsDocument.class);
    /** the skeleton object to carry the properties of the new object */
    private OwObjectSkeleton m_skeletonObject;

    /** List of OwDocumentImportEntry objects!  
     * <p>A entry contains the DocumentImportItem and the corresponding DocumentImporter</p>*/
    private List m_importedDocumentsList;

    /** the resource used to search for document classes */
    protected OwResource m_resource;

    /** the parent folder, required for special handling of virtual folders */
    protected OwObject m_parentFolder;

    /** collection of OwPropertyInfo objects defining read only properties. Only required if parentFolder is a virtual folder */
    protected List<OwPropertyInfo> m_propertyInfos;

    /**flag notifying if a process should be shown for a Imported Document
     * <p>default value <code>false</code>*/
    private boolean showProcessView;

    /**flag notifying if a DocumentImportItem was added, which has no
     * ObjectClass predefined<p>default value <code>false</code>*/
    private boolean undefinedObjectClass;

    /**object class name which was defined by configuration (plugins.xml)
     * @since 2.5.2.0 */
    private String strClassName;

    /** object class processor, for extended handling of object class before skeleton creation
     * @since 4.1.1.0*/
    private OwObjectClassProcessor objectClassProcessor;

    /**the object class name which was selected by user action
     * @since 2.5.2.0 */
    protected String strUserSelectedClassName;

    /**
     * PropertyList configuration defines property filter and grouping
     * @since 4.2.0.0
     */
    private OwPropertyListConfiguration propertyListConfiguration;

    /** construct a save document with the resource to work on */
    public OwAddMultiDocumentsDocument(OwResource resource_p, OwObject parentFolder_p)
    {
        m_resource = resource_p;
        m_parentFolder = parentFolder_p;
        m_importedDocumentsList = new ArrayList();
        undefinedObjectClass = showProcessView = false;
        m_propertyInfos = new LinkedList<OwPropertyInfo>();
    }

    /**
     * Returns the <code>OwResource</code> where the new objects are created.
     * 
     * @return the <code>OwResource</code> where the new objects are created.
     */
    public OwResource getResource()
    {
        return m_resource;
    }

    /**
     * Collection of OwPropertyInfo objects defining read only properties. 
     * Non empty only if parentFolder is a virtual folder
     * @return Collection of OwPropertyInfo objects
     */
    public Collection<OwPropertyInfo> getPropertyInfos()
    {
        return m_propertyInfos;
    }

    /**
     * Set the ObjectClass for skeleton objects which are used
     * before creating a real instance of that object. Also
     * fire an update Event of type <code>OwUpateCodes.SET_NEW_OBJECT</code>.
     * @param objectClass_p OwObjectClass
     * @throws Exception
     * @throws OwInvalidOperationException if OwNetwork.createObjectSkeleton(OwbjectClass, OwResource} returns <code>null</code>
     */
    public void setObjectClass(OwObjectClass objectClass_p) throws Exception
    {
        OwMainAppContext context = (OwMainAppContext) getContext();
        OwObjectClass clazz = preProcessObjectClass(objectClass_p);
        m_skeletonObject = createSkeletonObject(clazz, context);

        if (m_skeletonObject == null)
        {
            //throw an exception, or there will be an endless recursion
            String msg = "OwAddMultiDocumentsDocument.setObjectClass: Fatal error, network returned null for object class named=" + objectClass_p.getClassName();
            LOG.fatal(msg);
            throw new OwInvalidOperationException(msg);
        }

        // if parent folder is a virtual folder, preset all properties defined by the virtual folder
        // and create a propertyInfo list.
        OwVirtualFolderObject parentVirtualFolder = null;
        try
        {
            parentVirtualFolder = (OwVirtualFolderObject) m_parentFolder;
        }
        catch (ClassCastException cce)
        {
            // do nothing here. parentVirtualFolder will still be null. 
        }
        if (parentVirtualFolder != null)
        {
            try
            {
                parentVirtualFolder.setFiledObjectProperties(objectClass_p, m_skeletonObject.getProperties(null));
            }
            catch (Exception e)
            {
                //delete the temp dir, else we lose the information of this folder
                if (context.getDragAndDropUploadDir() != null)
                {
                    context.clearDragDropUploadDir();
                }
                // throw the exception for error handling
                throw e;
            }
            // create m_propertyInfos
            m_propertyInfos = new LinkedList<OwPropertyInfo>();
            OwPropertyCollection tempFilledProperties = new OwStandardPropertyCollection();
            parentVirtualFolder.setFiledObjectProperties(objectClass_p, tempFilledProperties);
            Iterator<?> allPropertiesIterator = m_skeletonObject.getProperties(null).keySet().iterator();
            while (allPropertiesIterator.hasNext())
            {
                String propName = (String) allPropertiesIterator.next();
                if (tempFilledProperties.containsKey(propName))
                {
                    m_propertyInfos.add(new OwPropertyInfo(propName, Boolean.TRUE));
                }
            }
        }

        if (getUserSelectedClassName() != null && !getUserSelectedClassName().equals(m_skeletonObject.getObjectClass().getClassName()))
        {
            setUserSelectedClassName(m_skeletonObject.getObjectClass().getClassName());
        }
        /*update views only after a document was imported and class was defined.
         * This "if" stop update when a object class is predefined in plugins XML,
         * but no document is added, only after the first document was added
         * this method fire update events.*/
        if (getImportedDocumentsCount() > 0)
        {
            // update views
            update(this, OwUpdateCodes.SET_NEW_OBJECT, null);
        }
    }

    /**
     * overridable factory method
     * @param objectClass_p
     * @param context_p
     * @return OwObjectSkeleton
     * @throws Exception
     * @since 3.2.0.0
     */
    protected OwObjectSkeleton createSkeletonObject(OwObjectClass objectClass_p, OwMainAppContext context_p) throws Exception
    {
        return context_p.getNetwork().createObjectSkeleton(objectClass_p, m_resource);
    }

    /**
     * 
     * @param fRenew_p a <code>boolean</code>
     * @return an {@link OwObjectSkeleton}
     * @throws Exception 
     */
    public OwObjectSkeleton getSkeletonObject(boolean fRenew_p) throws Exception
    {
        if (fRenew_p)
        {
            // re-create all properties (will clear them all)
            m_skeletonObject.refreshProperties();
            // if parent folder is a virtual folder, preset all properties defined by the virtual folder
            OwVirtualFolderObject parentVirtualFolder = null;
            if (m_parentFolder != null)
            {
                try
                {
                    parentVirtualFolder = (OwVirtualFolderObject) m_parentFolder;
                    parentVirtualFolder.setFiledObjectProperties(m_skeletonObject.getObjectClass(), m_skeletonObject.getProperties(null));
                }
                catch (ClassCastException cce)
                {
                    // do nothing here. parentVirtualFolder will still be null. 
                }
            }
        }

        return m_skeletonObject;
    }

    /**
     * Return a OwDocumentImportItem of the given index.
     * <p>Can throw IndexOutOfBoundsException if <code>
     * <b>idx_p</b> &gt; {@link #getImportedDocumentsCount()}
     * </code> or less than 0 (zero)</p>
     * @param idx_p int index of item to be returned
     * @return OwDocumentImportItem
     * @see #getImportedDocumentsCount()
     */
    public OwDocumentImportItem getImportedDocument(int idx_p)
    {
        return ((OwDocumentImportEntry) m_importedDocumentsList.get(idx_p)).getItem();
    }

    /**
     * Return the amount of imported document item's
     * @return int amount of imported document item's
     */
    public int getImportedDocumentsCount()
    {
        return m_importedDocumentsList.size();
    }

    /**
     * This is a clean up method for a cached document item.
     * First the given document item is released {@link OwDocumentImportItem#release()}
     * and afterwards the caching entry from structure is cleared.
     * @param importedDocument_p {@link OwDocumentImportItem} - the document to be released
     * 
     * @throws Exception if release of OwDocumentImportItem creates a failure
     * @since 2.5.3.0
     */
    public void releaseImportedDocument(OwDocumentImportItem importedDocument_p) throws Exception
    {
        int removeItemIndex = -1;

        for (int i = 0; i < m_importedDocumentsList.size(); i++)
        {
            OwDocumentImportItem item = ((OwDocumentImportEntry) m_importedDocumentsList.get(i)).getItem();

            //=== check for equality based on MIME type & proposed name
            if (item.getContentMimeType(0).equals(importedDocument_p.getContentMimeType(0)) && item.getProposedDocumentName().equals(importedDocument_p.getProposedDocumentName()))
            {
                removeItemIndex = i;
                break;
            }
        }

        if (removeItemIndex != -1)
        {
            ((OwDocumentImportEntry) m_importedDocumentsList.get(removeItemIndex)).getItem().release();
            m_importedDocumentsList.remove(removeItemIndex);
        }
    }

    /**
     * This is a clean up method for cached document items.
     * First all document items are released {@link OwDocumentImportItem#release()}
     * and afterwards the caching structure is cleared.
     * @throws Exception if release of OwDocumentImportItem creates a failure
     */
    public void releaseImportedDocuments() throws Exception
    {
        for (int i = 0; i < m_importedDocumentsList.size(); i++)
        {
            ((OwDocumentImportEntry) m_importedDocumentsList.get(i)).getItem().release();
        }
        m_importedDocumentsList.clear();
    }

    public void onDocumentImported(OwDocumentImporter importer_p, OwDocumentImportItem importedDocument_p) throws Exception
    {
        if (importedDocument_p == null)
        {
            throw new NullPointerException("OwAddMultiDocumentsDocument.onDocumentImported: the DocumentImportItem should not be null");
        }

        m_importedDocumentsList.add(new OwDocumentImportEntry(importedDocument_p, importer_p));

        if (!showProcessView())
        {
            setShowProcessViewFlag(importer_p != null ? importer_p.hasPostProcessView(OwDocumentImporter.IMPORT_CONTEXT_NEW) : false);
        }

        if (!hasUndefinedObjectClass())
        {
            setUndefinedObjectClassFlag(importedDocument_p.getObjectClassName() == null);
        }
        //there is a predefined object class, but no documents were imported
        if (m_skeletonObject != null && getImportedDocumentsCount() == 1)
        {
            try
            {
                if (importedDocument_p.getObjectClassName() != null)
                {
                    setObjectClass(((OwMainAppContext) getContext()).getNetwork().getObjectClass(importedDocument_p.getObjectClassName(), getResource()));
                }
                else
                {
                    setObjectClass(m_skeletonObject.getObjectClass());
                }
            }
            catch (Exception e)
            {
                LOG.error("OwAddMultiDocumentsDocument.onDocumentImported: faild to reset object class", e);
            }
        }
    }

    /**
     * Returns the importer depending on the imported item, which is 
     * given as parameter <b>item_p</b>.
     * <p>If <code><b>item_p</b></code> is <code><b>null</b></code>
     * then <code><b>null</b></code> will be returned.<br />
     * Also <code><b>null</b></code> will be returned if
     * the DocumentImportItem was importer using DnD-functionality.</p>
     * @param item_p OwDocumentImportItem
     * @return OwDocumentImporter or <code><b>null</b></code>
     * @since 2.5.2.0
     */
    public OwDocumentImporter getDocumentImporter(OwDocumentImportItem item_p)
    {
        if (item_p != null)
        {
            for (int i = 0; i < m_importedDocumentsList.size(); i++)
            {
                OwDocumentImportEntry entry = (OwDocumentImportEntry) m_importedDocumentsList.get(i);
                if (entry.getItem() == item_p)
                {
                    return getDocumentImporter(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns the importer depending on the imported item, which is 
     * given as parameter <b>i_p</b>.
     * <p><code><b>i_p</b></code> should be &gt;=0 &amp; &lt; {@link #getImportedDocumentsCount()}
     * <br /><code><b>null</b></code> will be returned if the DocumentImportItem 
     * was importer using DnD-functionality.</p>
     * @param i_p int index of the imported document (zero-based)
     * @return OwDocumentImporter or <code><b>null</b></code>
     * @since 2.5.2.0
     */
    public OwDocumentImporter getDocumentImporter(int i_p)
    {
        return ((OwDocumentImportEntry) this.m_importedDocumentsList.get(i_p)).getImporter();
    }

    /**
     * If an document was added which DocumentImporter
     * has a post processing view, this method will return true.
     * @return boolean <code>true</code> only if a document with post process view was added
     * @since 2.5.2.0
     */
    public boolean showProcessView()
    {
        return this.showProcessView;
    }

    /**
     * If an document was added which {@link OwDocumentImportItem#getObjectClassName()}
     * has returned <code><b>null</b></code>.
     * @return boolean <code>true</code> if one document exist which is has not a predefined ObjectClass
     * @since 2.5.2.0
     */
    public boolean hasUndefinedObjectClass()
    {
        return this.undefinedObjectClass;
    }

    /**
     * Set the flag for undefined object class to 
     * given value.
     * <p>This flag notify if a document import item 
     * was added without an predefined ObjectClass/Document class.</p>
     * @param value_p boolean 
     * @since 2.5.2.0
     */
    protected void setUndefinedObjectClassFlag(boolean value_p)
    {
        this.undefinedObjectClass = value_p;
    }

    /**
     * Set the flag for post processing view of documents.
     * <p>This flag notify if there is a document-import-item
     * which should be post processed by a view</p>
     * @param value_p boolean
     * @since 2.5.2.0
     */
    protected void setShowProcessViewFlag(boolean value_p)
    {
        this.showProcessView = value_p;
    }

    /**
     * Get ObjectClass name which should be used
     * for objects by default.
     * <p>Return if an default ObjectClass was set in 
     * plugins definition</p>
     * @return String or <code>null</code>
     * @since 2.5.2.0
     */
    public String getClassName()
    {
        return this.strClassName;
    }

    /**
     * Set an ObjectClass name as default class.
     * <p>ATTENTION: This method set only the String reference,
     * you still have to call {@link #setObjectClass(OwObjectClass)}
     * to create an skeleton object, which can be used</p>
     * @param objectClassname_p String name of ObjectClass
     * @since 2.5.2.0
     */
    public void setClassName(String objectClassname_p)
    {
        this.strClassName = objectClassname_p;
    }

    /**
     * Return a String which represents the ObjectClass name
     * selected by user action.
     * @return String or <code>null</code>
     * @since 2.5.2.0
     */
    public String getUserSelectedClassName()
    {
        return this.strUserSelectedClassName;
    }

    /**
     * Method to set an ObjectClass name selected by user action.
     * <p>ATTENTION: This will only the set a String reference
     * to create a skeleton object a call of {@link #setObjectClass(OwObjectClass)}
     * is still needed</p>  
     * @param userSelectedObjectClass_p String ObjectClass name
     * @since 2.5.2.0
     */
    protected void setUserSelectedClassName(String userSelectedObjectClass_p)
    {
        this.strUserSelectedClassName = userSelectedObjectClass_p;
    }

    /**
     * Check if the given objectClass name is valid for current
     * context.
     * <p>Try to resolve the objectClass name to an ObjectClass instance.
     * if an exception is thrown during resolving, this method will return false</p>
     * @param objectClass_p String name (symbolic) of class to check
     * @return boolean true only if no exception is thrown when retrieve.
     * @since 2.5.2.0
     */
    public boolean isValidObjecClass(String objectClass_p)
    {
        try
        {
            OwMainAppContext context = (OwMainAppContext) getContext();
            context.getNetwork().getObjectClass(objectClass_p, getResource());
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    /**
     * Refactoring of handler for OwObjectClass selection by user action.
     * <p>before 2.5.2 OwCreateMultipleDocumentsDialog was listener of OwObjectClassView selection events.</p>
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView.OwObjectClassViewListner#onObjectClassViewSelectClass(OwObjectClass, String) OwObjectClassViewListener
     * @since 2.5.2.0
     */
    public void onObjectClassViewSelectClass(OwObjectClass classDescription_p, String strPath_p) throws Exception
    {
        setUserSelectedClassName(classDescription_p.getClassName());
        setObjectClass(classDescription_p);
    }

    /** (overridable)
     * PreCreation skeleton object handling of ObjectClass, delegated to current OwObjectClassProcessor instance.<br />
     * Will return the OwObjectClass which should be used as skeleton object class.
     * <p>By default if {@link #getObjectClassProcessor()} return null, the same OwObjectClass is returned as provided</p>
     * @param objCls_p OwObjectClass selected for skeleton creation
     * @return OwObjectClass is used for skeleton object
     * @throws OwException
     * @since 4.1.1.0
     */
    protected OwObjectClass preProcessObjectClass(OwObjectClass objCls_p) throws OwException
    {
        if (getObjectClassProcessor() != null)
        {
            return getObjectClassProcessor().process(objCls_p, this, getContext());
        }
        else
        {
            return objCls_p;
        }
    }

    /**
     * Setter for OwObjectClassProcessor
     * @param processor OwObjectClassProcessor (can be null)
     * @since 4.1.1.0
     */
    public void setObjectClassProcessor(OwObjectClassProcessor processor)
    {
        this.objectClassProcessor = processor;
    }

    /**
     * Getter of current OwObjectClassProcessor
     * @return OwObjectClassProcessor or null
     * @since 4.1.1.0
     */
    public OwObjectClassProcessor getObjectClassProcessor()
    {
        return this.objectClassProcessor;
    }

    @Override
    public OwObject getParent()
    {
        return this.m_parentFolder;
    }

    /**
     * Get the currently defined ProeprtyList configuration.
     * @return OwPropertyListConfiguration (or null of none available/defined)
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Set a specific PropertyList configuration
     * @param propertyListConfiguration OwPropertyListConfiguration
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propertyListConfiguration)
    {
        this.propertyListConfiguration = propertyListConfiguration;
    }

    @Override
    public void detach()
    {
        super.detach();
        if (this.propertyListConfiguration != null)
        {
            propertyListConfiguration = null;
        }
    }

    /**
     *<p>
     * INTERNAL helper class, for association of imported
     * documentItem and corresponding importer.
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
     *@since 2.5.2.0 
     */
    private static class OwDocumentImportEntry
    {
        private OwDocumentImportItem item;
        private OwDocumentImporter importer;

        public OwDocumentImportEntry(OwDocumentImportItem item_p, OwDocumentImporter importer_p)
        {
            this.item = item_p;
            this.importer = importer_p;
        }

        public OwDocumentImportItem getItem()
        {
            return this.item;
        }

        public OwDocumentImporter getImporter()
        {
            return this.importer;
        }
    }

}