package com.wewebu.ow.server.ao;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 * <p>Manager for virtual folder {@link OwObject} application objects.</p>
 * 
 * <p>Returns {@link OwObject} objects.</p>
 * <p>Retrieves only single objects.</p>
 * <p>Does not support parameterized retrieval through {@link #getApplicationObject(String, Object, boolean, boolean)}.</p>
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
public class OwVirtualFoldersManager extends OwSupportBasedManager
{
    private static final Logger LOG = OwLogCore.getLogger(OwVirtualFoldersManager.class);

    public static final String DEFAULT_BASE_PATH = "other";

    private OwVirtualFolderFactory virtualFolderFactory;

    private OwRoleManager roleManager;

    public OwVirtualFoldersManager()
    {

    }

    /**
     * Constructor - {@link #DEFAULT_BASE_PATH} path will be used  
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param virtualFolderFactory_p virtual folder factory implementation
     * 
     */
    public OwVirtualFoldersManager(OwAOSupport aoSupport_p, OwVirtualFolderFactory virtualFolderFactory_p)
    {
        this(aoSupport_p, virtualFolderFactory_p, null, DEFAULT_BASE_PATH);
    }

    /**
     * Constructor - {@link #DEFAULT_BASE_PATH} path will be used  
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param virtualFolderFactory_p virtual folder factory implementation
     * @param roleManager_p role manager used for filtering allowed virtual folders (can be null if 
     *                      no role based filtering is required)
     * @since 4.0.0.0
     */
    public OwVirtualFoldersManager(OwAOSupport aoSupport_p, OwVirtualFolderFactory virtualFolderFactory_p, OwRoleManager roleManager_p, String basePath_p)
    {
        super(aoSupport_p, basePath_p != null ? basePath_p : DEFAULT_BASE_PATH);
        this.virtualFolderFactory = virtualFolderFactory_p;
        this.roleManager = roleManager_p;
    }

    private OwObject toVirtualFolder(OwObject supportObject_p, String strName_p) throws OwException
    {
        try
        {
            OwContentCollection contentCollection = supportObject_p.getContentCollection();
            OwContentElement firstElement = contentCollection.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
            InputStream contentStream = firstElement.getContentStream(null);
            org.w3c.dom.Document xmlDocument = null;
            xmlDocument = OwXMLDOMUtil.getDocumentFromInputStream(contentStream);
            Node xmlFirstChild = xmlDocument.getFirstChild();

            return this.virtualFolderFactory.createVirtualFolder(xmlFirstChild, strName_p, null);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.fatal("OwVirtualFolderManager.getApplicationObject():Could not create virtual folder application object!", e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"), e);
        }
    }

    public OwObject getApplicationObject(String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        if (param_p != null)
        {
            LOG.error("OwVirtualFolderManager.getApplicationObject():Could not process non null search template parameter - not implemented !");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"));
        }
        else
        {
            boolean allowed = false;
            try
            {
                if (roleManager != null)
                {
                    allowed = roleManager.isAllowed(OwRoleManager.ROLE_CATEGORY_VIRTUAL_FOLDER, strName_p);
                }
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not create application object.", e);
            }

            if (allowed)
            {
                OwObject supportObject = getAOSupportObject(strName_p, forceUserSpecificObject_p, createIfNotExist_p);
                return toVirtualFolder(supportObject, strName_p);
            }
            else
            {
                LOG.warn("OwVirtualFoldersManager.getApplicationObject : Access to virtual folder " + strName_p + " was denied.");
                throw new OwAccessDeniedException(new OwString1("app.OwAOProvider.acces.denied", "Access to the application object %1 was denied", strName_p));
            }

        }
    }

    public final OwObject getApplicationObject(String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getApplicationObject(strName_p, null, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public List<OwObject> getApplicationObjects(String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        List<OwObject> virtualFolders = new LinkedList<OwObject>();

        if (forceUserSpecificObject_p && roleManager != null)
        {
            try
            {
                Collection<String> allowedVirtualFolders = roleManager.getAllowedResources(OwRoleManager.ROLE_CATEGORY_VIRTUAL_FOLDER);
                for (String virtualFolderName : allowedVirtualFolders)
                {
                    OwObject virtualFolder = getApplicationObject(virtualFolderName, forceUserSpecificObject_p, false);
                    virtualFolders.add(virtualFolder);
                }
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not create application objects.", e);
            }
        }
        else
        {

            OwObject[] supportObjects = getAOSupportObjects("", forceUserSpecificObject_p, false);

            for (OwObject supportObject : supportObjects)
            {
                try
                {
                    OwObject virtualFolder = toVirtualFolder(supportObject, supportObject.getName());
                    virtualFolders.add(virtualFolder);
                }
                catch (OwInvalidOperationException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Creation of virtual folder faild", e);
                    }
                }
            }
        }
        return virtualFolders;
    }

    @Override
    public final int getManagedType()
    {
        return OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER;
    }

    //    @Override  will be introduced together when AOW managers refactoring
    public final OwAOType<?> getType()
    {
        return OwAOConstants.AO_VIRTUAL_FOLDER;
    }

    @Override
    public void init(OwAOType<?> type, OwManagerConfiguration configuration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException
    {
        super.init(type, configuration, supports, context);
        virtualFolderFactory = (OwVirtualFolderFactory) context.getNetwork();
        roleManager = context.isRoleManaged() ? context.getNetwork().getRoleManager() : null;
    }

    @Override
    public String toString()
    {
        return "OwVirtualFoldersManager(virtualFolderFactory=" + this.virtualFolderFactory + ")->" + super.toString();
    }
}
