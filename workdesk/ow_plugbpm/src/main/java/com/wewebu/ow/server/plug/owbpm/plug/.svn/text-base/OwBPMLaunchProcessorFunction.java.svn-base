package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Class for workflow launch document functions.<br/>
 * Depending on configuration, the workflow properties are rendered using a JSP file, or in a standard manner.
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
public class OwBPMLaunchProcessorFunction extends OwDocumentFunction
{
    /** the element name from configuration that specify the concrete JSP file to be used for rendering*/
    private static final String JSP_PAGE_ELEMENT_NAME = "JspForm";
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMLaunchProcessorFunction.class);
    /**workitem repository interface*/
    private static final String BPM_WORKITEM_REPOSITORY_INTERFACE = "com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository";
    /** the work item repository*/
    protected OwWorkitemRepository m_bpmRepository = null;

    /**
     * Factory interface used to create a {@link OwBPMWorkflowLauncher} object.
     * @since 3.1.0.0
     */
    protected interface OwBPMWorkflowLauncherFactory
    {
        /**
         * Create the {@link OwBPMWorkflowLauncher} object.
         * @param repository_p - the repository object.
         * @param targetObjects_p - collection with target objects.
         * @param context_p - the application context.
         * @param configNode_p - the config node object. 
         * @return the {@link OwBPMWorkflowLauncher} object.
         */
        OwBPMWorkflowLauncher createLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p);

        /**
         * Get the default icon path.
         * @return - the default icon path.
         */
        String getDefaultIcon();

        /**
         * Get the default big icon path.
         * @return - the default big icon path.
         */
        String getDefaulBigIcon();
    }

    /** 
     * Concrete factory use to create a standard workflow launcher.
     * @since 3.1.0.0
     */
    protected class OwStandardWorkflowLauncherFactory implements OwBPMWorkflowLauncherFactory
    {
        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#createLauncher(com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository, java.util.Collection, com.wewebu.ow.server.app.OwMainAppContext, com.wewebu.ow.server.util.OwXMLUtil)
         */
        public OwBPMWorkflowLauncher createLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p)
        {
            return new OwBPMStandardWorkflowLauncher(repository_p, targetObjects_p, context_p, configNode_p);
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#getDefaulBigIcon()
         */
        public String getDefaulBigIcon()
        {
            return "/images/plug/owbpm/gear_new_24.png";
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#getDefaultIcon()
         */
        public String getDefaultIcon()
        {
            return "/images/plug/owbpm/gear_new.png";
        }

    }

    /** 
     * Concrete factory use to create a JSP based  workflow launcher.
     * @since 3.1.0.0
     */
    protected class OwJSPWorkflowLauncherFactory implements OwBPMWorkflowLauncherFactory
    {
        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#createLauncher(com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository, java.util.Collection, com.wewebu.ow.server.app.OwMainAppContext, com.wewebu.ow.server.util.OwXMLUtil)
         */
        public OwBPMWorkflowLauncher createLauncher(OwWorkitemRepository repository_p, Collection targetObjects_p, OwMainAppContext context_p, OwXMLUtil configNode_p)
        {
            return new OwBPMJspWorkflowLauncher(repository_p, targetObjects_p, context_p, configNode_p);
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#getDefaulBigIcon()
         */
        public String getDefaulBigIcon()
        {
            return "/images/plug/owbpm/gear_new_form_24.png";
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMLaunchProcessorFunction.OwBPMWorkflowLauncherFactory#getDefaultIcon()
         */
        public String getDefaultIcon()
        {
            return "/images/plug/owbpm/gear_new_form.png";
        }

    }

    /**the workflow launcher factory object*/
    protected OwBPMWorkflowLauncherFactory m_workflowLauncherFactory = null;

    /**
     * Constructor.
     */
    public OwBPMLaunchProcessorFunction()
    {

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        //first create the correct workflow launcher factory
        m_workflowLauncherFactory = createWorkflowLauncherFactory(node_p);
        super.init(node_p, context_p);
        OwNetwork network = context_p.getNetwork();
        this.m_bpmRepository = null;
        if (!network.hasInterface(BPM_WORKITEM_REPOSITORY_INTERFACE))
        {
            String msg = "OwBPMLaunchProcessorFunction.init : The ECM adapter does not offer an interface for work items repsitories. This document function will be disabled!";
            LOG.warn(msg);
        }
        else
        {
            this.m_bpmRepository = (OwWorkitemRepository) network.getInterface(BPM_WORKITEM_REPOSITORY_INTERFACE, null);
            if (!this.m_bpmRepository.canLaunch())
            {
                String msg = "OwBPMLaunchProcessorFunction.init : The configured work item repository does not support the launching workflows. This document function will be disabled!";
                LOG.warn(msg);
                this.m_bpmRepository = null;
            }
        }
    }

    /**
     * Creates the concrete factory for workflow launcher.
     * @param node_p - the configuration node
     * @return - the concrete factory used to create a workflow launcher object.
     * @since 3.1.0.0
     */
    protected OwBPMWorkflowLauncherFactory createWorkflowLauncherFactory(OwXMLUtil node_p)
    {
        OwBPMWorkflowLauncherFactory launcherFactory = null;
        String jspForm = node_p.getSafeTextValue(JSP_PAGE_ELEMENT_NAME, null);

        if (jspForm != null)
        {
            launcherFactory = new OwJSPWorkflowLauncherFactory();
        }
        else
        {
            launcherFactory = new OwStandardWorkflowLauncherFactory();
        }
        return launcherFactory;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {

        if (m_bpmRepository != null)
        {
            return super.isEnabled(object_p, parent_p, context_p);
        }
        else
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public final void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(object_p, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwBPMLaunchProcessorFunction.onClickEvent():No workflow can be started for the given object!");
            throw new OwInvalidOperationException(getContext().localize("owbpm.OwBPMLaunchProcessorFunction.invalidObject", "No workflow can be started for the given object(s)!"));
        }
        List objects = new ArrayList();
        objects.add(object_p);

        onMultiselectClickEvent(objects, parent_p, refreshCtx_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public void onMultiselectClickEvent(final Collection objects_p, OwObject parent_p, final OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(objects_p, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwBPMLaunchProcessorFunction.onMultiselectClickEvent():No workflow can be started for the given objects!");
            throw new OwInvalidOperationException(getContext().localize("owbpm.OwBPMLaunchProcessorFunction.invalidObject", "No workflow can be started for the given object(s)!"));
        }

        OwBPMWorkflowLauncher launcher = createLauncher(objects_p);
        launcher.launch(refreshCtx_p);
        addHistoryEvent(objects_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Get the {@link OwWorkitemRepository} object
     * @return - the associated {@link OwWorkitemRepository} object.
     */
    protected OwWorkitemRepository getWorkitemRepository()
    {
        return m_bpmRepository;
    }

    /** (overridable) 
     *  Creates a custom workflow launcher for this document function
     *  
     * @param targetObjects_p
     * @return the custom {@link OwBPMWorkflowLauncher}
     * @throws Exception
     */
    protected OwBPMWorkflowLauncher createLauncher(Collection targetObjects_p) throws Exception
    {
        return m_workflowLauncherFactory.createLauncher(getWorkitemRepository(), targetObjects_p, getContext(), getConfigNode());
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    *  @since 3.1.0.0
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", m_workflowLauncherFactory.getDefaultIcon());
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL
    *  @since 3.1.0.0 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", m_workflowLauncherFactory.getDefaulBigIcon());
    }

}
