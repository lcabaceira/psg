package com.wewebu.ow.server.plug.owdms;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView.OwObjectClassViewListner;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owdms.log.OwLog;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Dialog that help to change the object class for a given object.
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
public class OwChangeClassDialog extends OwStandardDialog implements OwObjectClassViewListner
{
    /**navigation view*/
    private OwSubNavigationView m_SubNavigation;
    /**layout*/
    private OwSubLayout m_Layout;
    /**the object for which the object class will be changed*/
    private OwObject m_object;
    /**the class selector view*/
    private OwObjectClassView m_classView;
    /**the property view*/
    private OwObjectPropertyView m_PropertyView;
    /**the document for this dialog*/
    private OwChangeClassDialogDocument m_document;
    /**refresh context - inform the parent view that the class of the object was changed*/
    private OwClientRefreshContext m_refreshContext;
    private int m_indexClassView;
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwChangeClassDialog.class);

    /**
     * Constructor.
     * @param object_p - the object
     * @param dialogListener_p - dialog listener.
     * @throws Exception
     */
    public OwChangeClassDialog(OwObject object_p, OwDialogListener dialogListener_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        m_refreshContext = refreshCtx_p;
        setListener(dialogListener_p);

        m_Layout = new OwSubLayout();
        m_SubNavigation = new OwSubNavigationView();
        OwObject theObjectToChangeClass = object_p;
        if (object_p.hasVersionSeries())
        {
            OwVersionSeries versSeries = object_p.getVersionSeries();
            theObjectToChangeClass = versSeries.getObject(versSeries.getLatest());
        }
        m_object = theObjectToChangeClass;
        m_document = new OwChangeClassDialogDocument(m_object);
        setDocument(m_document);
    }

    /**
     * Initialize the inner views.
     * @see com.wewebu.ow.server.ui.OwView#init()
     */
    protected void init() throws Exception
    {
        super.init();

        m_document.attach(getContext(), null);

        // === attached layout
        addView(m_Layout, MAIN_REGION, null);

        // === navigation
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // enable validation of panels
        m_SubNavigation.setValidatePanels(true);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        m_classView = new OwObjectClassView(m_object.getResource(), m_object.getType());

        m_indexClassView = m_SubNavigation.addView(m_classView, getContext().localize("owsavedialog.impl.OwSaveDlgDialog.class_title", "Select class"), null, getContext().getDesignURL() + "/images/plug/owdocprops/class.png", null, null);

        m_classView.navigateToClass(m_object.getObjectClass());
        m_classView.setSelectedItemStyle("OwSaveDlgObjectClassTextSelected");
        m_classView.setItemStyle("OwSaveDlgObjectClassText");

        m_classView.setEventListner(this);
        // m_docImportView.setNextActivateView(m_classView);
        m_PropertyView = new OwObjectPropertyView();
        m_PropertyView.setUpdateNoChanges(true);
        m_PropertyView.setInformUserOnSuccess(false);

        // disable internal modes of property view, we display a own mode box here
        //-----< compute viewmask >-----
        int iViewMask = 0;

        m_PropertyView.setViewMask(iViewMask);

        // attach view to layout
        m_SubNavigation.addView(m_PropertyView, getContext().localize("owsavedialog.impl.OwSaveDlgDialog.properties_title", "Properties"), null, getContext().getDesignURL() + "/images/plug/owdocprops/properties.png", null, null);
        // NOTE: Initially no object is set, will set it upon class selection
        m_PropertyView.setObjectRef(m_object, false);
        // set as next view of previous one
        m_classView.setNextActivateView(m_PropertyView);

        // === activate the first view
        m_SubNavigation.navigate(m_indexClassView);

    }

    /**
     * Called when user selected a different object class. Only then the "next" button is visible.
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView.OwObjectClassViewListner#onObjectClassViewSelectClass(com.wewebu.ow.server.ecm.OwObjectClass, java.lang.String)
     */
    public void onObjectClassViewSelectClass(OwObjectClass classDescription_p, String strPath_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwChangeClassDialog.onObjectClassViewSelectClass: Selected class: " + classDescription_p.getClassName());
        }
        m_document.updateObjectClass(classDescription_p);
    }

    /**
     * Called after user selected the new class. Here the properties view is filled with
     * the new properties.
     * @see com.wewebu.ow.server.ui.OwView#onUpdate(OwEventTarget, int, Object)
     * @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        if (iCode_p == OwUpdateCodes.SET_NEW_OBJECT)
        {
            OwObject obj = m_document.getSkeletonObject();

            // bug 1726
            if (m_PropertyView != null)
            {
                // new set the reference
                m_PropertyView.setObjectRef(obj, false);
            }
        }
        switch (iCode_p)
        {
            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
            case OwUpdateCodes.OBJECT_PROPERTIES_NOT_CHANGED:
                performChangeClass(caller_p, iCode_p);
        }
    }

    /**
     *
     * @param caller_p
     * @throws Exception
     * @throws OwInvalidOperationException
     */
    private void performChangeClass(OwEventTarget caller_p, int iCode_p) throws Exception, OwInvalidOperationException
    {
        if (caller_p instanceof OwObjectPropertyView)
        {
            try
            {
                m_document.changeClass();
                //getDocument().update(this,iCode_p , null);
                closeDialog();
                if (m_refreshContext != null)
                {
                    m_refreshContext.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS, null);
                    m_refreshContext.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_FOLDER_CHILDS, null);
                    m_refreshContext.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
                }
            }
            catch (OwInvalidOperationException e)
            {
                m_SubNavigation.navigate(m_indexClassView);
                throw e;
            }

        }
        //class changed, inform user about it
        //remove previous messages
        Collection messages = ((OwMainAppContext) getContext()).getMessages();
        if (messages != null)
        {
            messages.clear();
            ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyView.saved", "Changes have been saved."));
        }

    }

    /**
     * Detach the attached document.
     * @see com.wewebu.ow.server.ui.OwView#detach()
     */
    public void detach()
    {
        super.detach();
        // detach document
        getDocument().detach();
    }

}