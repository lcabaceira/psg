package com.wewebu.ow.server.ao;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 * <p>
 * Manager for {@link OwSearchTemplate} application objects.<br/><br/>
 * Returns {@link OwSearchTemplate} objects.<br/>
 * Retrieves single objects and collection of objects.<br/>
 * Does not support parameterized retrieval through {@link #getApplicationObject(String, Object, boolean, boolean)}.
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
 *@since 3.2.0.0
 */
public class OwSearchTemplatesManager extends OwSupportBasedManager
{
    private static final String SINGLE_PATH_PARAMETER = "singlePath";

    private static final String MULTIPLE_PATH_PARAMETER = "multiplePath";

    private static final Logger LOG = OwLogCore.getLogger(OwSearchTemplatesManager.class);

    private OwSearchTemplateFactory searchTemplateFactory;

    private String singlePath = "";

    private String multiplePath = "";

    private OwRoleManager roleManager;

    public OwSearchTemplatesManager()
    {

    }

    /**
     * Constructor - an empty string base path will be used 
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param searchTemplateFactory_p search template factory implementation
     * 
     */
    public OwSearchTemplatesManager(OwAOSupport aoSupport_p, OwSearchTemplateFactory searchTemplateFactory_p)
    {
        this(aoSupport_p, "", searchTemplateFactory_p, "", "");
    }

    /**
     * Constructor
     * @param basePath_p path relative to the persistence support root of 
     *                   the managed objects' container  
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param searchTemplateFactory_p search template factory implementation
     * @param singlePath_p single objects relative path - influences only {@link #getApplicationObject(String, boolean, boolean)} and {@link #getApplicationObject(String, Object, boolean, boolean)}
     * @param multiplePath_p multiple objects relative path - influences only {@link #getApplicationObjects(String, boolean)}
     * @since 3.2.0.0
     */
    public OwSearchTemplatesManager(OwAOSupport aoSupport_p, String basePath_p, OwSearchTemplateFactory searchTemplateFactory_p, String singlePath_p, String multiplePath_p)
    {
        super(aoSupport_p, basePath_p);
        this.searchTemplateFactory = searchTemplateFactory_p;
        this.singlePath = singlePath_p;
        this.multiplePath = multiplePath_p;
    }

    private void checkDeniedAccess(OwSearchTemplate searchtemplate) throws OwAccessDeniedException
    {
        try
        {
            if (isAllowed(searchtemplate))
            {
                return;
            }
        }
        catch (OwException e)
        {
            LOG.error("", e);
        }

        String templateName = searchtemplate.getName();
        LOG.warn("OwSearchTemplatesManager.checkDeniedAccess : Access to the search template application object " + templateName + " was denied.");
        throw new OwAccessDeniedException(new OwString1("app.OwAOProvider.acces.denied", "Access to the application object %1 was denied", searchtemplate.getName()));

    }

    private boolean isAllowed(OwSearchTemplate searchtemplate) throws OwException
    {
        try
        {
            if (roleManager != null)
            {
                return roleManager.isAllowed(OwRoleManager.ROLE_CATEGORY_SEARCH_TEMPLATE, searchtemplate.getName());
            }
            else
            {
                return true;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Error at search template access check.", e);
        }
    }

    public OwSearchTemplate getApplicationObject(String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        if (param_p != null)
        {
            LOG.error("OwSearchTemplatesManager.getApplicationObject(): Invalid application object request!Could not process non null search template parameter - not implemented!");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"));
        }
        else
        {
            OwObject supportObject = getAOSupportObject(aoPath(singlePath, strName_p), forceUserSpecificObject_p, createIfNotExist_p);

            OwSearchTemplate searchtemplate = this.searchTemplateFactory.createSearchTemplate(supportObject);
            checkDeniedAccess(searchtemplate);

            return searchtemplate;
        }
    }

    private String aoPath(String cardinalityPath_p, String strName_p)
    {
        String aoPath = null;

        if (cardinalityPath_p != null && cardinalityPath_p.length() > 0)
        {
            aoPath = cardinalityPath_p + "/" + strName_p;
        }
        else
        {
            aoPath = strName_p;
        }

        return aoPath;
    }

    public OwSearchTemplate getApplicationObject(String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getApplicationObject(strName_p, null, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public List<OwSearchTemplate> getApplicationObjects(String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {

        OwObject[] supportObjects = getAOSupportObjects(aoPath(multiplePath, strName_p), forceUserSpecificObject_p, false);
        List<OwSearchTemplate> searchTemplatesCollection = new LinkedList<OwSearchTemplate>();

        for (int i = 0; i < supportObjects.length; i++)
        {
            if (supportObjects[i] != null)
            {
                OwSearchTemplate searchTemplate = this.searchTemplateFactory.createSearchTemplate(supportObjects[i]);
                if (isAllowed(searchTemplate))
                {
                    searchTemplatesCollection.add(searchTemplate);
                }
            }
        }

        return searchTemplatesCollection;
    }

    @Override
    public int getManagedType()
    {
        return OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE;
    }

    //    @Override  will be introduced together when AOW managers refactoring
    public OwAOType<?> getType()
    {
        return OwAOConstants.AO_SEARCHTEMPLATE;
    }

    protected OwSearchTemplateFactory initSearchTemplateFactory(OwAOContext context)
    {
        return (OwSearchTemplateFactory) context.getNetwork();
    }

    @Override
    public void init(OwAOType<?> type, OwManagerConfiguration configuration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException
    {
        super.init(type, configuration, supports, context);
        singlePath = configuration.getParameterValue(SINGLE_PATH_PARAMETER);
        multiplePath = configuration.getParameterValue(MULTIPLE_PATH_PARAMETER);
        searchTemplateFactory = initSearchTemplateFactory(context);
        roleManager = context.isRoleManaged() ? context.getNetwork().getRoleManager() : null;
    }

    @Override
    public String toString()
    {
        return "OwSearchTemplatesManager(searchTemplateFactory=" + searchTemplateFactory + ")->" + super.toString();
    }
}
