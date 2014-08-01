package com.wewebu.ow.server.dmsdialogs.views;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * {@link OwPermissionCollection} facade document.
 * Keeps track of the target object (object owner of the edited/viewed permissions) and 
 * the current permissions (viewed or edited).    
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
 *@since 4.0.0.0
 */
public class OwPermissionsDocument extends OwDocument
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwPermissionsDocument.class);

    private OwPermissionCollection permissions;
    private OwObject object;

    private static OwPermissionCollection fetchPermissions(OwObject object_p) throws OwException
    {
        try
        {
            return object_p.getPermissions();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not retrieve perimissions.", e);
        }
    }

    /**
     * Constructor.
     * Object's permissions are the current permissions.
     * 
     * @param object_p
     * @throws OwException
     */
    public OwPermissionsDocument(OwObject object_p) throws OwException
    {
        this(object_p, fetchPermissions(object_p));
    }

    /**
     * Constructor
     * 
     * @param object_p target object
     * @param permissions_p current permissions
     */
    public OwPermissionsDocument(OwObject object_p, OwPermissionCollection permissions_p)
    {
        super();
        this.object = object_p;
        this.permissions = permissions_p;
    }

    /**
     * Saves the current permissions to the target object (see {@link OwObject#setPermissions(OwPermissionCollection)}).
     * Before returning the permissions are reloaded  (see see {@link OwObject#getPermissions()}).  
     * 
     * @throws OwException
     */
    public void savePermissions() throws OwException
    {
        try
        {
            this.object.setPermissions(this.permissions);
        }
        catch (OwException e)
        {
            LOG.error("Could not save permissions.", e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not save permissions.", e);
            throw new OwInvalidOperationException("Could not retrieve perimissions.", e);
        }
        finally
        {
            try
            {
                this.permissions = fetchPermissions(this.object);
            }
            catch (OwException e)
            {
                LOG.error("Could not reload permissions.", e);
                throw e;
            }
        }
    }

    /**
     * 
     * @return the target {@link OwObject}
     */
    public OwObject getObject()
    {
        return object;
    }

    /**
     * 
     * @return the current permissions 
     */
    public OwPermissionCollection getPermissions()
    {
        return permissions;
    }

}
