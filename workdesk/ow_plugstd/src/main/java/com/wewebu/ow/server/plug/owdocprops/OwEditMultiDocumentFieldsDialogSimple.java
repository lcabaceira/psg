package com.wewebu.ow.server.plug.owdocprops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwFieldPropertyClassWrapper;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Edit multiple documents simultaneously, for a specific set of properties.
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
public class OwEditMultiDocumentFieldsDialogSimple extends OwStandardDialog
{
    /**
     *<p>
     * The edit field view.
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
    private static class OwEditFieldsView extends OwFieldsView
    {
        private OwEditMultiDocumentFieldsDialogSimple m_owEditMultiDocumentFieldsDialogSimple;

        public OwEditFieldsView(OwEditMultiDocumentFieldsDialogSimple owEditMultiDocumentFieldsDialogSimple_p)
        {
            m_owEditMultiDocumentFieldsDialogSimple = owEditMultiDocumentFieldsDialogSimple_p;
        }

        @SuppressWarnings("rawtypes")
        protected boolean saveFields(Map changedFields_p) throws Exception
        {
            m_owEditMultiDocumentFieldsDialogSimple.saveFields(changedFields_p);
            return m_owEditMultiDocumentFieldsDialogSimple.getFailedObjects().isEmpty();
        }

    }

    /**
     *<p>
     * A single edit field.
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
    public static class OwEditField implements OwField
    {

        private OwFieldDefinition fielddefinition;
        private Object value;

        public OwFieldDefinition getFieldDefinition() throws Exception
        {
            return fielddefinition;
        }

        public Object getValue() throws Exception
        {
            return value;
        }

        public void setValue(Object value_p) throws Exception
        {
            value = value_p;
        }

        public OwEditField(OwFieldDefinition fieldDefinition_p, Object value_p)
        {
            super();
            this.fielddefinition = fieldDefinition_p;
            this.value = value_p;
        }

    }

    /** flag to display the document list view */
    public static final int VIEW_MASK_SHOW_DOCUMENT_LIST = 0x00000001;

    /** flag to close the dialog upon save action */
    public static final int VIEW_MASK_CLOSE_ON_SAVE = 0x00000002;

    /** set of flags indicating the behavior of the view
     */
    protected int m_iViewMask;

    /**list of objects that cannot be changed*/
    protected List m_failedObjects = new LinkedList();
    /**the object in work*/
    protected OwObject m_processedObject;
    /** the objects to edit */
    private Collection m_objects;

    /** the property names to edit */
    private Collection m_fieldnames;

    /** the property classes to edit */
    private Map m_propertyclasses;

    /**
     *  create a multi document editing dialog
     * @param objects_p
     * @param parent_p
     * @param fieldnames_p
     * @throws Exception 
     */
    public OwEditMultiDocumentFieldsDialogSimple(Collection objects_p, OwObject parent_p, Collection fieldnames_p) throws Exception
    {
        m_objects = objects_p;
        m_fieldnames = fieldnames_p;
    }

    /** check if a certain view field is enabled 
     * @param iViewMask_p <code>int</code> view mask as defined with VIEW_field_...
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return (m_iViewMask & iViewMask_p) > 0;
    }

    /** set the view fields
     * @param iViewMask_p <code>int</code> view mask  (a combination of VIEW_field_... defined flags)
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** called when user saves the fields
     * save the modified fields to all objects
     * 
     * @param changedFields_p
     * @throws Exception 
     */
    public void saveFields(Map changedFields_p) throws Exception
    {
        // create property map for OwObject.setProperties method
        OwStandardPropertyCollection properties = new OwStandardPropertyCollection();

        Iterator it = changedFields_p.values().iterator();
        while (it.hasNext())
        {
            OwField field = (OwField) it.next();
            properties.put(field.getFieldDefinition().getClassName(), new OwStandardProperty(field.getValue(), new OwFieldPropertyClassWrapper(field.getFieldDefinition())));
        }

        // iterator over all objects
        it = m_objects.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            try
            {
                obj.setProperties(properties);
                this.m_processedObject = obj;
                getDocument().update(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY, null);
            }
            catch (Exception e)
            {
                m_failedObjects.add(obj);
            }
        }

        if (hasViewMask(VIEW_MASK_CLOSE_ON_SAVE))
        {
            // close dialog
            closeDialog();
        }
    }

    /** get the fields to edit
     * 
     * @return Collection of field names
     */
    protected Collection getFieldNames()
    {
        return m_fieldnames;
    }

    /** get the objects to edit
     * 
     * @return a {@link Collection} 
     */
    protected Collection getObjects()
    {
        return m_objects;
    }

    /** find the common property classes for the given names and objects
     *  throws an exception if the property classes are unequal
     *   
     * @param objects_p
     * @param fieldnames_p
     * @return Map of property names and OwPropertyClasses
     * @throws Exception 
     */
    private Map determinePropertyClasses(Collection objects_p, Collection fieldnames_p) throws Exception
    {
        Map ret = new HashMap();

        Iterator it = objects_p.iterator();

        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();

            OwObjectClass objclass = obj.getObjectClass();

            Iterator itfield = fieldnames_p.iterator();
            while (itfield.hasNext())
            {
                String name = (String) itfield.next();

                try
                {
                    OwPropertyClass propclass = objclass.getPropertyClass(name);

                    // already in map
                    OwPropertyClass mappropclass = (OwPropertyClass) ret.get(propclass.getClassName());

                    if (null == mappropclass)
                    {
                        // first item, put to map
                        ret.put(propclass.getClassName(), propclass);
                    }
                    else
                    {
                        // check that data type matches all object instances
                        if (!propclass.getJavaClassName().equals(mappropclass.getJavaClassName()))
                        {
                            // data type does not match
                            throw new OwInvalidOperationException(getContext().localize2("owplug.OwEditMultiDocumentFieldsDialogSimple.propertytypematcherror", "Different data types for the property [%1] in [%2].",
                                    propclass.getDisplayName(getContext().getLocale()), obj.getName()));
                        }
                    }
                }
                catch (OwObjectNotFoundException e)
                {
                    // property does not exist in object, can not proceed with editing
                    throw new OwInvalidOperationException(getContext().localize2("owplug.OwEditMultiDocumentFieldsDialogSimple.propertynotfound", "Could not find the property [%1] in [%2].", name, obj.getName()), e);
                }

            }
        }

        return ret;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // check all the objects for common property classes
        m_propertyclasses = determinePropertyClasses(m_objects, m_fieldnames);
        // === create the dialog views
        // === attached layout
        OwLayout m_Layout = new OwSubLayout();
        addView(m_Layout, MAIN_REGION, null);
        // create fields and definitions
        Map fields = new HashMap();
        List columninfo = new ArrayList();

        // name column first
        columninfo.add(new OwStandardFieldColumnInfo(OwResource.m_ObjectNamePropertyClass, OwFieldColumnInfo.ALIGNMENT_DEFAULT));

        Iterator it = getFieldNames().iterator();
        while (it.hasNext())
        {
            String name = (String) it.next();

            OwFieldDefinition def = (OwFieldDefinition) m_propertyclasses.get(name);

            // add to field map
            fields.put(name, new OwEditField(def, null));

            // also display as column
            columninfo.add(new OwStandardFieldColumnInfo(def, OwFieldColumnInfo.ALIGNMENT_DEFAULT));

        }

        // fields view
        OwFieldsView fieldsview = new OwEditFieldsView(this);
        // set possible masks
        int fieldsViewMask = 0;
        fieldsViewMask |= hasViewMask(OwFieldsView.VIEW_MASK_ENABLE_PASTE_METADATA) ? OwFieldsView.VIEW_MASK_ENABLE_PASTE_METADATA : 0;
        fieldsViewMask |= hasViewMask(OwFieldsView.VIEW_MASK_RENDER_VERTICAL) ? OwFieldsView.VIEW_MASK_RENDER_VERTICAL : 0;
        fieldsview.setViewMask(fieldsViewMask);

        m_Layout.addView(fieldsview, MAIN_REGION, null);
        fieldsview.setFields(fields);

        if (hasViewMask(VIEW_MASK_SHOW_DOCUMENT_LIST))
        {
            // objects view
            OwObjectListViewRow objectssview = new OwObjectListViewRow(0);
            m_Layout.addView(objectssview, OwSubLayout.NAVIGATION_REGION, null);

            objectssview.setColumnInfo(columninfo);
            OwStandardObjectCollection list = new OwStandardObjectCollection();
            list.addAll(getObjects());
            objectssview.setObjectList(list, null);
        }

    }

    /**
     * Get a list with objects that cannot be processed.
     * @return - object list that cannot be processed.
     * @since 2.5.2.0
     */
    public List getFailedObjects()
    {
        return m_failedObjects;
    }

    /**
     * Returns the object processed;
     * @return - the processed object.
     * @since 2.5.2.0
     */
    public OwObject getProcessedObject()
    {
        return m_processedObject;
    }
}