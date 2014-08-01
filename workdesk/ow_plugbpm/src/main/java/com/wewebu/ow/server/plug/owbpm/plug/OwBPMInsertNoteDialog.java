package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;

/**
 *<p>
 * Dialog for asking for a note to be attached to the workitem.
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
public class OwBPMInsertNoteDialog extends OwStandardDialog implements OwFieldProvider
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMInsertNoteDialog.class);

    /** named region for note text area */
    public static final int NOTE_REGION = STANDARD_DIALOG_REGION_MAX + 1;

    /** start of overridden regions */
    public static final int INSERT_NOTE_DIALOG_REGION_MAX = STANDARD_DIALOG_REGION_MAX + 3;

    /** the workitems which are to be handled */
    protected List m_workitems;

    /** menu for the buttons */
    protected OwSubMenuView m_MenuView;

    /** name of the property note */
    private String m_NotePropertyName;

    /** is the note property necessary, i.e. if true, than throw Exception when not set, if false,
     * than just ignore the missing note property
     */
    private boolean m_NotePropertyIsNecessary;

    /** inform listeners which is the current item in work*/
    protected OwWorkitem m_currentItem;
    /** the field manager*/
    protected OwFieldManager m_fieldManager;
    /** the note property */
    protected OwProperty m_noteProperty;

    /**
     * create a new instance of the dialog
     * @param workitems_p
     * @param notePropertyName_p if null  note property is ignored
     * @param notePropertyIsNecessary_p is the property note necessary. if true missing note property throws an exception
     * @throws OwException 
     */
    public OwBPMInsertNoteDialog(List workitems_p, String notePropertyName_p, boolean notePropertyIsNecessary_p) throws OwException
    {
        m_workitems = workitems_p;
        m_NotePropertyName = notePropertyName_p;
        m_NotePropertyIsNecessary = notePropertyIsNecessary_p;
    }

    /**
     * create a new instance of the dialog
     * @param workitem_p
     * @param notePropertyName_p
     * @param notePropertyIsNecessary_p is the property note necessary. if true missing note property throws an exception
     */
    public OwBPMInsertNoteDialog(OwWorkitem workitem_p, String notePropertyName_p, boolean notePropertyIsNecessary_p) throws OwException
    {
        // put the work item in a work item list, so that single and multiple workitems can be handled in the same way.
        m_workitems = new ArrayList();
        m_workitems.add(workitem_p);
        m_NotePropertyName = notePropertyName_p;
        m_NotePropertyIsNecessary = notePropertyIsNecessary_p;
    }

    /** enable form handling */
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /**
     * @see com.wewebu.ow.server.ui.OwView#init()
     */
    protected void init() throws Exception
    {
        super.init();
        OwMainAppContext context = (OwMainAppContext) getContext();
        if (null != m_NotePropertyName)
        {
            //	check if note property is defined for the workitems
            if (m_NotePropertyIsNecessary)
            {
                List itemsWithNoNoteProperty = arePropertiesDefined();
                if (itemsWithNoNoteProperty != null && itemsWithNoNoteProperty.size() > 0)
                {
                    throw new OwBPMNotePropertyException(itemsWithNoNoteProperty, getContext().localize("plug.owbpm.plug.OwBPMInsertNoteFunction.notenotdefined", "Note property had not been defined at plugin descriptor."));
                }
            }
            //init the field manager.
            m_fieldManager = context.createFieldManager();
            // init note edit field

            if (m_workitems.size() > 0)
            {
                try
                {
                    OwObject obj = (OwObject) m_workitems.get(0);
                    m_noteProperty = obj.getProperty(m_NotePropertyName);
                }
                catch (OwObjectNotFoundException e)
                {
                    m_noteProperty = null;
                }
            }
        }

        // === create menu for search form buttons
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, FOOTER_REGION, null);

        //      add OK button
        int okButtonIndex = m_MenuView.addFormMenuItem(this, getContext().localize("plug.owtask.ui.OwTaskFunctionBaseDialog.ok", "OK"), "OkDo", null);
        m_MenuView.setDefaultMenuItem(okButtonIndex);
        //      add cancel button
        m_MenuView.addFormMenuItem(this, getContext().localize("plug.owtask.ui.OwTaskFunctionBaseDialog.cancel", "Cancel"), "CancelDo", null);

    }

    /**
     * checks if for all workitems the note property is defined. 
     * @return list of the names of workitems which have no note property
     */
    private List arePropertiesDefined() throws Exception
    {
        List propertiesWithNoNoteProperty = new ArrayList();
        for (Iterator iter = m_workitems.iterator(); iter.hasNext();)
        {
            OwWorkitem item = (OwWorkitem) iter.next();

            // check if we have a note property
            try
            {
                item.getProperty(m_NotePropertyName);
            }
            catch (OwObjectNotFoundException e)
            {
                propertiesWithNoNoteProperty.add(item.getName());
            }

        }
        return propertiesWithNoNoteProperty;
    }

    /** 
     * event called when user clicked the cancel button  should be overloaded by specific Dialog
     *  @param request_p a {@link HttpServletRequest}
     *  @param reason_p
     */
    public void onCancelDo(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        closeDialog();
    }

    /** 
     * event called when user clicked the OK button  should be overloaded by specific Dialog
     *  @param request_p  a {@link HttpServletRequest}
     *  @param reason_p
     */
    public void onOkDo(HttpServletRequest request_p, Object reason_p) throws Exception
    {

        // set note
        setNote(request_p);

        closeDialog();

    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case MAIN_REGION:
                this.serverSideDesignInclude("owbpm/OwInsertNote.jsp", w_p);
            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * Render the note
     * @param w_p
     * @throws Exception
     * @since 2.5.2.0
     */
    public void renderNote(Writer w_p) throws Exception
    {
        if (m_fieldManager != null && m_noteProperty != null)
        {
            m_fieldManager.insertEditField(w_p, m_noteProperty);
        }
    }

    /**
     * set a note to all workitems of the list
     * @param request_p
     * @throws Exception 
     */
    public void setNote(HttpServletRequest request_p) throws Exception
    {
        // if note property name is not set we cannot set the not so just return
        // this can happen, if the property
        if (m_fieldManager != null)
        {
            m_currentItem = null;
            try
            {

                for (Iterator iter = m_workitems.iterator(); iter.hasNext();)
                {
                    // get work item
                    m_currentItem = (OwWorkitem) iter.next();

                    OwPropertyCollection changedProperties = new OwStandardPropertyCollection();
                    m_fieldManager.update(request_p, changedProperties, m_currentItem.getProperties(null));
                    if (changedProperties.size() > 0)
                    {
                        m_currentItem.setProperties(changedProperties);
                        getDocument().update(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY, m_currentItem);
                    }
                }

            }
            catch (Exception e)
            {
                LOG.error("Der Vorgang konnte nicht mit einer Notiz versehen werden", e);
                throw new OwInvalidOperationException(getContext().localize1("plug.owbpm.plug.OwBPMInsertNoteDialog.note.error", "Work item (%1) could not be provided with a note.", m_currentItem.getName()), e);
            }
        }
    }

    /**
     * unlock the workitems
     * 
     * @throws Exception
     */
    public void unlockAll() throws Exception
    {
        for (Iterator iter = this.m_workitems.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(false);
        }
    }

    /**
     * getter method for workitems 
     * @return Returns the workitems.
     */
    public Collection getWorkitems()
    {
        return m_workitems;
    }

    /**
     * Get the current processing work item
     * @return the current work item
     * @since 2.5.2.0
     */
    public OwWorkitem getCurrentItem()
    {
        return m_currentItem;
    }

    /**
     * @see com.wewebu.ow.server.app.OwStandardDialog#onRender(java.io.Writer)
     * @since 2.5.2.0
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (m_fieldManager != null)
        {
            m_fieldManager.reset();
        }
        super.onRender(w_p);

    }

    /**
     * @see com.wewebu.ow.server.ui.OwView#detach()
     * @since 2.5.2.0
     */
    public void detach()
    {
        super.detach();
        if (m_fieldManager != null)
        {
            m_fieldManager.detach();
        }
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getField(java.lang.String)
     * @since 2.5.2.0
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        OwProperty prop = null;
        if (m_currentItem != null)
        {
            try
            {
                prop = (OwProperty) m_currentItem.getProperties(null).get(strFieldClassName_p);
            }
            catch (Exception e)
            {
                // do nothing
            }
        }

        if (prop == null)
        {
            throw new OwObjectNotFoundException("OwObjectPropertyView.getField: Property not found, strFieldClassName_p = " + strFieldClassName_p);
        }

        return prop;
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderName()
     * @since 2.5.2.0
     */
    public String getFieldProviderName()
    {
        String name = null;
        if (m_currentItem != null)
        {
            name = m_currentItem.getName();
        }
        return name;
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderSource()
     * @since 2.5.2.0
     */
    public Object getFieldProviderSource()
    {
        return m_currentItem;
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderType()
     * @since 2.5.2.0
     */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFields()
     * @since 2.5.2.0
     */
    public Collection getFields() throws Exception
    {
        OwPropertyCollection result = new OwStandardPropertyCollection();
        if (m_currentItem != null && m_NotePropertyName != null)
        {
            OwProperty noteProp = m_currentItem.getProperty(m_NotePropertyName);
            result.put(noteProp.getPropertyClass().getClassName(), noteProp);
        }
        return result.values();
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#getSafeFieldValue(java.lang.String, java.lang.Object)
     * @since 2.5.2.0
     */
    public Object getSafeFieldValue(String name_p, Object defaultvalue_p)
    {
        Object result = defaultvalue_p;
        if (m_currentItem != null)
        {
            try
            {
                result = m_currentItem.getProperty(name_p).getValue();
            }
            catch (Exception e)
            {
                LOG.error("OwBPMInsertNoteDialog.getSafeFieldValue: Cannot get property value for property name: " + name_p);
            }
        }
        return result;
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldProvider#setField(java.lang.String, java.lang.Object)
     * @since 2.5.2.0
     */
    public void setField(String name_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        if (name_p != null && name_p.equalsIgnoreCase(m_NotePropertyName))
        {
            OwField prop = m_currentItem.getProperty(name_p);
            prop.setValue(value_p);
        }
    }
}