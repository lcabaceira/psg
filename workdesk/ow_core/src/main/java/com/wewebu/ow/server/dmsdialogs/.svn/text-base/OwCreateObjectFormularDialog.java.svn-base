package com.wewebu.ow.server.dmsdialogs;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwJspConfigurable;

/**
 *<p>
 * Dialog to create new objects. Uses a JSP Form.
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
public class OwCreateObjectFormularDialog extends OwStandardDialog implements OwJspConfigurable
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwCreateObjectFormularDialog.class);

    /** the new folder object */
    protected OwObjectSkeleton m_sceletonObject;

    /** the property view */
    protected OwObjectPropertyFormularView m_PropertyView;

    /** DMSID of the newly created object */
    private String m_strDmsID;

    /** selected object of record */
    protected OwObject m_folderObject;

    /** the initial class */
    protected String m_strObjectClass;

    /** parent object class from where the user can select, only useful if classes are structured in a tree */
    protected String m_strObjectClassParent;

    /** class to use for new folder */
    protected OwObjectClass m_folderClass;

    /** refresh context for callback */
    protected OwClientRefreshContext m_RefreshCtx;

    /** open with new record */
    protected boolean m_fOpenObject;

    /** URL of a form page to use */
    protected String m_strJspPage;

    /** a set of properties that should be set as default for the new object */
    protected Map m_ValuesMap;

    /** status of this dialog: nothing done yet */
    public static final int DIALOG_STATUS_NONE = 0;
    /** status of this dialog: checkin successful*/
    public static final int DIALOG_STATUS_OK = 1;
    /** status of this dialog: checkin failed */
    public static final int DIALOG_STATUS_FAILED = 2;

    /** status of this dialog needed for historization */
    protected int m_dialogStatus = DIALOG_STATUS_NONE;

    /**
     * Handler for multiple JSP form configuration
     * @since 3.1.0.0
     */
    private OwJspFormConfigurator m_jspConfigurator;

    /** create a record / folder create dialog
     *
     * @param folderObject_p OwObject parent folder to add to
     * @param strClassName_p class name to use for new folder, null = let user select a class
     * @param strObjectClassParent_p String parent class to let user browse
     * @param fOpenObject_p 
     * @param strJspFormConfig_p URL of a form page to use
     */
    public OwCreateObjectFormularDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean fOpenObject_p, OwJspFormConfigurator strJspFormConfig_p)
    {
        m_folderObject = folderObject_p;
        m_strObjectClass = strClassName_p;
        m_fOpenObject = fOpenObject_p;
        m_strObjectClassParent = strObjectClassParent_p;
        setJspConfigurator(strJspFormConfig_p);
        // add view to a new document
        setDocument(new OwDocument());
    }

    /**
     * get the current status of this dialog
     * @return the status
     */
    public int getStatus()
    {
        return (m_dialogStatus);
    }

    /** submit a set of values that should be set as default for the new object
     *
     * @param properties_p Map of values keyed by parameter names to be set initially
     */
    public void setValues(Map properties_p)
    {
        m_ValuesMap = properties_p;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === set document
        // register as event target
        getDocument().attach(getContext(), null);

        // === just one property view, no navigation needed
        //  === create properties view
        m_PropertyView = new OwObjectPropertyFormularView();

        // attach view to layout
        addView(m_PropertyView, MAIN_REGION, null);

        m_PropertyView.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);

        m_PropertyView.setJspConfigurator(getJspConfigurator());

        if (m_strObjectClass == null)
        {
            String msg = "OwCreateObjectFormularDialog.init: Please define a ObjectClass in the plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_folderClass = ((OwMainAppContext) getContext()).getNetwork().getObjectClass(m_strObjectClass, m_folderObject.getResource());
        updateObjectClass();
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach document
        getDocument().detach();
    }

    /** get the newly created object */
    public OwObject getNewObject() throws Exception
    {
        if (null == m_strDmsID)
        {
            return null;
        }
        else
        {
            return ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(m_strDmsID, false);
        }
    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        //////////////////////////////////////////////////////
        // NOTE:    Records structures are not created here, they are created in the ECM Adapter.
        //          Here we only select a class and create it.
        //          If the class represents just a folder or a whole structure is up to the Adapter.

        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_OBJECT_PROPERTY:
                // save was pressed on the property view, now we can create the folder
                m_strDmsID = create();
                // close the dialog
                closeDialog();

                // open the newly created record
                OwObject recordObject = getNewObject();
                if (m_fOpenObject)
                {
                    OwMasterDocument recordPlugin = OwMimeManager.getHandlerMasterPlugin((OwMainAppContext) getContext(), recordObject);

                    if (recordPlugin == null)
                    {
                        String msg = "OwCreateObjectFormularDialog.onUpdate: Recordplugin Id must be specified if a record is created.";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    recordPlugin.dispatch(OwDispatchCodes.OPEN_OBJECT, recordObject, null);
                }

                // refresh the context
                if (m_RefreshCtx != null)
                {
                    m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS, recordObject);
                    m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, recordObject);
                }

                break;
        }
    }

    /** set a plugin refresh callback interface
     *
     * @param pluginRefreshCtx_p OwClientRefreshContext
     */
    public void setRefreshContext(OwClientRefreshContext pluginRefreshCtx_p)
    {
        m_RefreshCtx = pluginRefreshCtx_p;
    }

    /** create the folder / record 
     * @return String DMSID of new object 
     */
    protected String create() throws Exception
    {
        // filter out read-only, hidden and null properties
        OwPropertyCollection docStandardPropertiesMap = m_sceletonObject.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        // fetch DocType
        String docType = m_sceletonObject.getObjectClass().getClassName();

        // === create a new object
        String newDmsid = null;
        try
        {
            newDmsid = ((OwMainAppContext) getContext()).getNetwork().createNewObject(m_folderObject.getResource(), docType, docStandardPropertiesMap, null, null, m_folderObject, null, null);
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }
        // flag success
        m_dialogStatus = DIALOG_STATUS_OK;
        // return DMSID 
        return newDmsid;
    }

    /** the object class for the folder was changed, update the skeleton
     */
    private void updateObjectClass() throws Exception
    {
        m_sceletonObject = ((OwMainAppContext) getContext()).getNetwork().createObjectSkeleton(m_folderClass, m_folderObject.getResource());

        // Merge with m_ValuesMap
        if (null != m_ValuesMap)
        {
            Iterator<?> it = m_ValuesMap.keySet().iterator();
            while (it.hasNext())
            {
                String strPropName = (String) it.next();

                try
                {
                    OwProperty skeletonProp = m_sceletonObject.getProperty(strPropName);

                    skeletonProp.setValue(m_ValuesMap.get(strPropName));
                }
                catch (OwObjectNotFoundException e)
                {
                    // Ignore
                }
            }
        }

        m_PropertyView.setObjectRef(m_sceletonObject);
    }

    /** overridden render the view
      * @param w_p Writer object to write HTML to
      */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("dmsdialogs/OwCreateObjectFormularDialog.jsp", w_p);
    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return this.m_jspConfigurator;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        this.m_jspConfigurator = jspFormConfigurator_p;
    }
}