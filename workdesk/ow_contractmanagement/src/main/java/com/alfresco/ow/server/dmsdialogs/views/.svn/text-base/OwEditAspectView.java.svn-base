package com.alfresco.ow.server.dmsdialogs.views;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.plug.owaddobject.OwAspectObjectClassProcessor;
import com.alfresco.ow.server.plug.owaddobject.OwAspectObjectClassProcessor.OwAspectGroupSelectionHelper;
import com.alfresco.ow.server.plug.owrecordext.OwCreateContractDialogAddAspects;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * OwEditAspectView.
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
public class OwEditAspectView extends OwLayout implements OwMultipanel
{
    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwEditAspectView.class);

    /** region of the tree view */
    public static final int CONTROL_REGION = 0;

    /** region of the tree view */
    public static final int MENU_REGION = 1;

    /** index of the next button in the menu */
    protected int m_iNextButtonIndex;

    /** Menu for buttons in the view */
    protected OwSubMenuView menuView;

    /** Stores the selector view */
    private OwAspectSelectView m_SelectorView;

    /** Stores the object reference to the skeleton object to create */
    private OwObject objectRef;

    /** Stores the object class processor */
    private OwAspectObjectClassProcessor objectClassProcessor;

    /** stores the parent dialog */
    private OwCreateContractDialogAddAspects dialog;

    /** stores the next active view */
    private OwView nextActivateView;

    /**
     * Constructor 
     * @param objectClassProcessor_p the object class processor to add the aspects to the object.
     * @param dialog_p the parent dialog of this view
     */
    public OwEditAspectView(OwAspectObjectClassProcessor objectClassProcessor_p, OwCreateContractDialogAddAspects dialog_p)
    {
        this.objectClassProcessor = objectClassProcessor_p;

        // check if processor is null
        if (null == objectClassProcessor_p)
        {
            String message = "Requried parameter 'objectClassProcessor_p' is null!";
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }

        // reset processor 
        objectClassProcessor_p.reset();

        this.dialog = dialog_p;

        // Check if dialog is null
        if (null == this.dialog)
        {
            String message = "Required parameter 'dialog_p' is null!";
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
        m_iNextButtonIndex = -1;
    }

    /**
     * Returns the object class processor.
     * @return OwAspectObjectClassProcessor
     */
    protected OwAspectObjectClassProcessor getObjectClassProcessor()
    {
        return this.objectClassProcessor;
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        menuView = new OwSubMenuView();
        // create and add the selector view
        this.m_SelectorView = createSelectorView();
        if (null != this.m_SelectorView)
        {
            this.addView(m_SelectorView, CONTROL_REGION, null);

            // === add menu
            addView(menuView, MENU_REGION, null);
        }
    }

    /**
     * Creates and returns the view used for the selector of the aspects.
     * @return the view used for the selector of the aspects.
     * @throws Exception 
     */
    protected OwAspectSelectView createSelectorView() throws Exception
    {
        OwAspectSelectView view = new OwAspectSelectView();
        view.setExternalFormTarget(view);

        updateAspectConfiguration();

        return view;
    }

    /**
     * Updates the object class based selection of the aspect configuration. 
     */
    protected void updateAspectConfiguration()
    {
        if (null != this.m_SelectorView)
        {
            String objectClassName = this.getObjectClassName();
            if (null != objectClassName && !objectClassName.isEmpty())
            {
                OwAspectGroupSelectionHelper availableAspects = this.getObjectClassProcessor().getConfiguredAspects(objectClassName);
                this.m_SelectorView.setCollections(availableAspects, null);
            }
        }
    }

    /**
     * Returns the class name of the object which will be edited or null if no object is present.
     * @return String object class name (or null if no object is present)
     */
    protected String getObjectClassName()
    {
        if (null != this.objectRef)
        {
            return this.objectRef.getClassName();
        }

        // no class name set. 
        return null;
    }

    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        this.nextActivateView = nextView_p;
        if (m_iNextButtonIndex < 0)
        {
            m_iNextButtonIndex = menuView.addMenuItem(this, getContext().localize("app.OwEditAspectView.next", "Next"), null, "Next", getNextActivateView(), null);
            menuView.setDefaultMenuItem(m_iNextButtonIndex);
        }
    }

    /**
     * Getter for view which should be next in order.
     * @return OwView (can be null if not set)
     */
    protected OwView getNextActivateView()
    {
        return this.nextActivateView;
    }

    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        // ignore
    }

    /** check if view has validated its data and the next view can be enabled
    *<p>Note: By default return always true, because aspects are not mandatory ...</p>
    * @return boolean true = can forward to next view, false = view has not yet validated
    */
    public boolean isValidated() throws Exception
    {
        return true;
    }

    /**
     * Stores the object reference to the object to create. 
     * @param owObject_p object reference 
     */
    public void setObjectRef(OwObject owObject_p)
    {
        this.objectRef = owObject_p;
        updateAspectConfiguration();
    }

    /** 
     * Event called when user clicked Next button in menu 
     * @param oReason_p Optional reason object submitted in addMenuItem
     * @param request_p a {@link HttpServletRequest}
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        this.dialog.updateObjectClass(this.objectClassProcessor, this.m_SelectorView.getSelectedAspects());
        m_iNextButtonIndex = -1;
        nextActivateView.activate();
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"OwEditAspectView\">");
        super.onRender(w_p);
        w_p.write("</div>");
    }

}
