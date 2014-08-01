package com.wewebu.ow.server.plug.owdms;

import java.util.Iterator;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Save Dialog Document class handling import item's and skeleton object for further processing.
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
public class OwSaveDlgDocument extends OwDocument
{

    /** the object skeleton that carries the new properties to be set */
    private OwObjectSkeleton m_skeletonObject;

    /** the resource used to search for document classes */
    protected OwResource m_resource;

    /** the imported document */
    protected OwDocumentImportItem m_importedDocument;

    /** the importer used for imported document item
     * @since 2.5.2.0 
     */
    protected OwDocumentImporter m_documentImporter;

    /** a object to be used as a property template */
    private OwObject m_propertyTemplateObject;

    /**
     * Create a new <code>OwSaveDlgDocument</code> for a given resource
     * 
     * @param resource_p the resource used to search for document classes
     */
    public OwSaveDlgDocument(OwResource resource_p)
    {
        m_resource = resource_p;
    }

    /**
     * Returns the resource where this dialog saves the content to.
     * 
     * @return OwResource current set resource 
     */
    public OwResource getResource()
    {
        return m_resource;
    }

    /** set a object to be used as a property template
     *  the object skeleton will take the parameters from the template
     * @param obj_p OwObject
     */
    public void setObjectTemplate(OwObject obj_p)
    {
        m_propertyTemplateObject = obj_p;
    }

    /**
     * @param objectClass_p OwObjectClass
     */
    public void setObjectClass(OwObjectClass objectClass_p) throws Exception
    {
        // create also the skeleton object
        m_skeletonObject = getCurrentContext().getNetwork().createObjectSkeleton(objectClass_p, m_resource);

        if (m_propertyTemplateObject != null)
        {
            // === copy parameters
            OwPropertyCollection templateProperties = m_propertyTemplateObject.getProperties(null);
            OwPropertyCollection skeletonProperties = m_skeletonObject.getProperties(null);

            Iterator it = skeletonProperties.values().iterator();
            while (it.hasNext())
            {
                OwProperty skeletonProp = (OwProperty) it.next();
                if (skeletonProp.getPropertyClass().isSystemProperty())
                {
                    continue;
                }

                OwProperty templateProp = (OwProperty) templateProperties.get(skeletonProp.getFieldDefinition().getClassName());
                if (templateProp == null)
                {
                    continue;
                }

                skeletonProp.setValue(templateProp.getValue());
            }
        }

        // update views
        update(this, OwUpdateCodes.SET_NEW_OBJECT, null);
    }

    /**
     * Return the current skeleton object.
     * @return OwObjectSkeleton 
     * @since 2.5.2.0
     */
    public OwObjectSkeleton getSkeletonObject()
    {
        return m_skeletonObject;
    }

    /**
     * return the template object - used for the properties
     */
    public OwObject getObjectTemplate()
    {
        return this.m_propertyTemplateObject;

    }

    public void setImportedDocument(OwDocumentImportItem importedDocument_p) throws Exception
    {
        // release current imported document
        if (m_importedDocument != null)
        {
            m_importedDocument.release();
            m_importedDocument = null;
        }
        // set new imported document
        m_importedDocument = importedDocument_p;

        if (hasValidPredefinedObjectClass())
        {
            setObjectClass(getCurrentContext().getNetwork().getObjectClass(m_importedDocument.getObjectClassName(), this.getResource()));
        }
        else
        {
            //is importedDocument null then a clean up is called
            if (importedDocument_p != null)
            {
                setObjectClass(getObjectTemplate().getObjectClass());
            }
        }
    }

    public OwDocumentImportItem getImportedDocument()
    {
        return (m_importedDocument);
    }

    /**
     * Check if the current imported document item ({@link #getImportedDocument()})
     * has predefined a valid ObjectClass.
     * @return boolean true only if document item has a predefined ObjectClass which is valid for current context
     * @since 2.5.2.0
     */
    public boolean hasValidPredefinedObjectClass()
    {
        if (getImportedDocument() != null && getImportedDocument().getObjectClassName() != null)
        {
            try
            {
                getCurrentContext().getNetwork().getObjectClass(getImportedDocument().getObjectClassName(), this.getResource());
                return true;
            }
            catch (Exception ex)
            {

            }
        }
        return false;
    }

    /**
     * Helper returns the context as OwMainAppContext.
     * @return OwMainAppContext
     * @since 2.5.2.0
     */
    protected OwMainAppContext getCurrentContext()
    {
        return (OwMainAppContext) getContext();
    }

    /**
     * Get the {@link OwDocumentImporter}
     * which was set.
     * @return {@link OwDocumentImporter}
     * @since 2.5.2.0
     */
    public OwDocumentImporter getDocumentImporter()
    {
        return m_documentImporter;
    }

    /**
     * Set here the {@link OwDocumentImporter} 
     * which was used for import of document item
     * @param importer_p {@link OwDocumentImporter}
     * @since 2.5.2.0
     */
    public void setDocumentImporter(OwDocumentImporter importer_p)
    {
        this.m_documentImporter = importer_p;
    }
}