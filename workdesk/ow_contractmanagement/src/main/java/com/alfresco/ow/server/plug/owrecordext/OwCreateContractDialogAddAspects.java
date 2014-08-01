package com.alfresco.ow.server.plug.owrecordext;

import java.util.List;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.OwContractGroupPropertyView;
import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.dmsdialogs.views.OwEditAspectView;
import com.alfresco.ow.server.plug.owaddobject.OwAspectObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owrecordext.OwCreateContractDialog;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwCreateContractDialogAddAspects.<br/>
 * Extended dialog class to append an aspect selector to the create object dialog.
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
public class OwCreateContractDialogAddAspects extends OwCreateContractDialog
{
    /** stores if the user can modify the object class processor settings who allows to add aspects or not. */
    private boolean m_CanEdit = false;

    /** Stores the edit aspects view if created */
    protected OwEditAspectView m_EditAspectsView = null;

    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwCreateContractDialogAddAspects.class);

    /**
     * Constructor 
     * @param folderObject_p    
     *              Parent folder object
     *              
     * @param classSelectionCfg
     *              Configuration 
     *              
     * @param fOpenObject_p
     *              Flag indicates if to open folder after creation
     */
    public OwCreateContractDialogAddAspects(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p)
    {
        super(folderObject_p, classSelectionCfg, fOpenObject_p);
    }

    /**
     * Constructor - deprecated!
     * @param folderObject_p
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param fOpenObject_p
     * @deprecated will be replaced by {@link #OwCreateContractDialogAddAspects(OwObject, OwObjectClassSelectionCfg, boolean)}
     */
    @Deprecated
    public OwCreateContractDialogAddAspects(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean fOpenObject_p)
    {
        super(folderObject_p, strClassName_p, strObjectClassParent_p, fOpenObject_p);
    }

    @Override
    protected void init() throws Exception
    {
        /* first read MY config otherwise it won't work
         * check if additional configuration is set
         */
        OwXMLUtil handler = getConfigNode().getSubUtil("AspectsAssociations");
        if (handler != null)
        {
            String strEdit = handler.getSafeStringAttributeValue("edit", "false");
            this.m_CanEdit = (strEdit != null && strEdit.toLowerCase().equals("true"));
        }

        // init super class
        super.init();
    }

    /**
     * Returns if the user can modify the object class processor settings who allows to add aspects or not.
     * @return if the user can modify the object class processor settings who allows to add aspects or not.
     */
    public boolean canEdit()
    {
        return this.m_CanEdit;
    }

    @Override
    protected void initTabOrder() throws Exception
    {
        super.initTabOrder();
        // check if aspect selector type is set and add view, if
        if (this.canEdit())
        {
            // create and append tab 
            OwObjectClassProcessor processor = this.getObjectClassProcessor();
            int editAspectTabIdx = -1;
            if (processor instanceof OwAspectObjectClassProcessor)
            {
                m_EditAspectsView = createEditAspectView((OwAspectObjectClassProcessor) processor);
                editAspectTabIdx = m_SubNavigation.addView(m_EditAspectsView, getContext().localize("owrecordext.OwCreateContractDialogAddAspects.addaspects_title", "Add Aspects"), null, getContext().getDesignURL()
                        + "/images/ContractManagement/add_aspect.png", null, null);

            }
            else
            {
                String message = "getObjectClassProcessor() must return an instance of OwObjectClassProcessor!";
                LOG.error(message);
                throw new OwInvalidOperationException(message);
            }

            int idx = m_SubNavigation.getTabList().indexOf(propertiesTab);
            if (m_keyPatternPropertyBridge != null)
            {
                idx--;
                m_SubNavigation.enable(idx, true);
                this.m_EditAspectsView.setNextActivateView(m_keyPatternPropertyBridge.getView());
            }
            else
            {
                // set successor of m_EditAspectsView
                this.m_EditAspectsView.setNextActivateView(this.delegateView);
            }

            OwTabInfo key = (OwTabInfo) m_SubNavigation.getTabList().get(editAspectTabIdx);
            m_SubNavigation.getTabList().remove(key);
            m_SubNavigation.getTabList().add(idx, key);
            m_SubNavigation.enable(idx, true);

            /* 1. View: m_classView --> may be null
             * 2. View: accessRightsView --> may be null
             * 3. View: m_EditAspectsView -> always exists
             * 4. View: keyPattern --> may be null
             * 5. View: delegateView -> always exists*/
            // set successor of m_classView 
            if (null != this.m_classView && null == this.accessRightsView)
            {
                this.m_classView.setNextActivateView(this.m_EditAspectsView);
            }

            // set successor of accessRightsView
            if (null != this.accessRightsView)
            {
                this.accessRightsView.setNextActivateView(this.m_EditAspectsView);
            }

        }

    }

    /**
     * Factory method to create EditAspectView.
     * @param processor OwAspectObjectClassProcessor for retrieval/construction of final OwObjectClass
     * @return OwEditAspectView
     * @throws OwException
     */
    protected OwEditAspectView createEditAspectView(OwAspectObjectClassProcessor processor) throws OwException
    {
        return new OwEditAspectView(processor, this);
    }

    /**
     * Update object class and notify embedded view to configure aspects
     */
    @Override
    protected void updateObjectClass() throws Exception
    {
        super.updateObjectClass();

        if (null != this.m_EditAspectsView)
        {
            this.m_EditAspectsView.setObjectRef(super.m_sceletonObject);
        }
    }

    /** 
     * Updates the object class by using the aspects object class processor.
     * @throws Exception
     */
    public void updateObjectClass(OwAspectObjectClassProcessor processor_p, List<String> selectedAspects_p) throws Exception
    {
        // m_objectClassName may contain aspects already if dialog was processed and user steps back .... so remove the aspects here ....
        int pos = this.m_objectClassName.indexOf(',');
        String cleanedObjectClassName = pos > 0 ? this.m_objectClassName.substring(0, pos) : this.m_objectClassName;
        processor_p.setFinalAspects(cleanedObjectClassName, selectedAspects_p);
        super.updateObjectClass();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owrecordext.OwCreateContractDialog#createObjectPropertyView()
     */
    @Override
    protected OwObjectPropertyView createObjectPropertyView() throws Exception
    {
        //generate key support & CMG auditing
        return new OwContractGroupPropertyView(this.m_propertyPatternConfiguration);
    }
}
