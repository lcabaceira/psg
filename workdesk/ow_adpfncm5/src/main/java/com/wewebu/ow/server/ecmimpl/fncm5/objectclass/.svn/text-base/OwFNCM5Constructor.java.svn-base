package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Content object constructor.
 * Handles the creation of a new content objects.
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
public interface OwFNCM5Constructor<N, R extends OwFNCM5Resource>
{
    /**
     * Creates a new content object of the owner class (see {@link OwFNCM5Class#getConstructor()}.<br/>
     * Used as a delegate of {@link OwNetwork}'s createNewObject various implementations.
     * 
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param resource_p OwResource to add to
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     * @param fKeepCheckedOut_p true = create a new object that is checked out
     * @param factory_p non-null context dependent {@link OwFNCM5Object} factory @see {@link OwFNCM5Class#from(Object, OwFNCM5ObjectFactory)} 
     * @return the newly created {@link OwFNCM5Object}  
     * @throws OwException
     * @see OwNetwork#createNewObject(boolean, Object, OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String)
     * @see OwNetwork#createNewObject(boolean, Object, OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String, boolean)
     * @see OwNetwork#createNewObject(OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String)
     */
    OwFNCM5Object<? extends N> newObject(boolean fPromote_p, Object mode_p, OwResource resource_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p, OwFNCM5ObjectFactory factory_p) throws OwException;
}
