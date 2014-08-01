package com.wewebu.ow.server.plug.owdocprops;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Dialog to edit the document properties with a form.
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
public class OwEditPropertiesFormularDialog extends OwStandardSequenceDialog
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEditPropertiesFormularDialog.class);

    /** mask value to use preview mode for auto-open */
    public static final int VIEW_MASK_PREVIEW_AUTOOPEN = 0x0001;

    /** mask value to use auto-open object */
    public static final int VIEW_MASK_AUTOOPEN = 0x0002;

    private OwObjectPropertyFormularView m_propertyView;

    /**
     * @since 3.1.0.0
     */
    private OwJspFormConfigurator m_jspConfigurator;

    /**
     * @param objects_p collection of {@link OwObject}s that the user clicked to open
     * @param parentObject_p the parent {@link OwObject}s that listed the objects, or null if no parent is available
     * @param jspConfigurator_p Url to the Jsp Page to use as a form
     */
    public OwEditPropertiesFormularDialog(Collection objects_p, OwObject parentObject_p, OwJspFormConfigurator jspConfigurator_p)
    {
        // simple document object to generate update events among the attached views, does not need to be registered as an event target itself 
        setDocument(new OwDocument());

        m_items = new ArrayList();
        m_items.addAll(objects_p);

        m_jspConfigurator = jspConfigurator_p;

    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach document
        getDocument().detach();
    }

    /** init the target after the context is set.*/
    protected void init() throws Exception
    {
        super.init();

        // === set document
        // register as event target
        getDocument().attach(getContext(), null);

        // === add properties view
        m_propertyView = new OwObjectPropertyFormularView();

        // attach view to layout
        addView(m_propertyView, MAIN_REGION, null);

        // set the HTML form to use
        m_propertyView.setJspConfigurator(m_jspConfigurator);

        initNewItem();
    }

    /** overridden render the view
      * @param w_p Writer object to write HTML to
      */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("dmsdialogs/OwEditPropertiesFormularDialog.jsp", w_p);
    }

    // === multiselect functionality

    /** list of items to work on */
    protected List m_items;

    /** current item index */
    protected int m_iIndex = 0;

    /** filters the views to be displayed*/
    protected int m_iViewMask = 0;

    /** determine the views to be displayed by masking them with their flag
    *
    * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
    */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    public int getViewMask()
    {
        return m_iViewMask;
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /** init the dialog with the current item
     */
    protected void initNewItem() throws Exception
    {
        m_propertyView.setObjectRef(getItem());

        // open
        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            if (OwMimeManager.isObjectDownloadable((OwMainAppContext) getContext(), getItem()))
            {
                if (hasViewMask(VIEW_MASK_PREVIEW_AUTOOPEN))
                {
                    OwMimeManager.openObjectPreview(((OwMainAppContext) getContext()), getItem(), null, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
                else
                {
                    OwMimeManager.openObject(((OwMainAppContext) getContext()), getItem(), null, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
            }
        }
    }

    /** the work item to work on */
    private OwObject getItem()
    {
        return (OwObject) m_items.get(m_iIndex);
    }

    /** called when the Dialog needs to know if there is a next item
    *
    */
    public boolean hasNext() throws Exception
    {
        return (m_iIndex < (m_items.size() - 1));
    }

    /** called when the Dialog needs to know if there is a prev item
     *
     */
    public boolean hasPrev() throws Exception
    {
        return (m_iIndex > 0);
    }

    /** get the number of sequence items in the dialog */
    public int getCount()
    {
        return m_items.size();
    }

    /** move to prev item and roll over, i.e. start at the end one if first one is reached 
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the dialog
     */
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwEditPropertiesFormularDialog.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            m_iIndex--;
        }
        else
        {
            m_iIndex = (m_items.size() - 1);
        }

        // init the dialog with the current work item
        initNewItem();
    }

    /** move to next item and roll over, i.e. start at the first one if end is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the next item, if this is the last item, closes the dialog
     */
    public void next(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            // === remove the current item and move to the next
            if (getCount() == 1)
            {
                // === only one item left
                // close dialog
                super.closeDialog();
                return;
            }
            else
            {
                m_items.remove(m_iIndex);
                if (m_iIndex >= m_items.size())
                {
                    m_iIndex = 0;
                }
            }
        }
        else
        {
            // === move to the next item
            if (hasNext())
            {
                m_iIndex++;
            }
            else
            {
                m_iIndex = 0;
            }
        }

        // === init the dialog with the current work item
        initNewItem();
    }

    /** visually close the Dialog. The behavior depends on usage
     *  If this view is a child of a DialogManager, the View gets removed from it.
     */
    public void closeDialog() throws Exception
    {
        super.closeDialog();

        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            // === close viewer as well
            if (((OwMainAppContext) getContext()).getWindowPositions().getPositionMainWindow())
            {
                ((OwMainAppContext) getContext()).addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(((OwMainAppContext) getContext()), OwMimeManager.VIEWER_MODE_DEFAULT));
            }
        }
    }
}