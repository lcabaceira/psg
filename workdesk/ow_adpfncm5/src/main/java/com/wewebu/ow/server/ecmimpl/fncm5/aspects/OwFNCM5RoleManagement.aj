package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.aspectj.lang.Signature;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Role management aspect.
 *</p>
 * Advises {@link Access} or {@link ObjectAccess} annotated methods with history role management
 * operations (like access granting or denying).
 * 
 *@see OwRoleManager
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
public aspect OwFNCM5RoleManagement
{
    private static final Map<Integer, OwString> accessDeniedMessageMap = new LinkedHashMap<Integer, OwString>();
    private static final OwString defaultAccessDeniedMessage = new OwString("fncm5.OwFNCMDocumentObject.accessdenied", "You do not have the necessary access authorization for this action.");
    static
    {
        accessDeniedMessageMap.put(OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW, new OwString("fncm5.OwFNCMNetwork.viewaccessdenied", "You do not have the necessary authorization to view this object."));
        accessDeniedMessageMap.put(OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE, new OwString("fncm5.OwFNCMNetwork.createaccessdenied", "You do not have the necessary authorization to create this object."));
        accessDeniedMessageMap.put(OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CHECKIN, defaultAccessDeniedMessage);
    }

    private OwString maskToMessage(int mask_p)
    {
        OwString message = accessDeniedMessageMap.get(mask_p);

        if (message == null)
        {
            Set<Integer> flags = accessDeniedMessageMap.keySet();
            for (Integer flag : flags)
            {
                if (0 != (mask_p & flag.intValue()))
                {
                    message = accessDeniedMessageMap.get(flag);
                    break;
                }
            }
        }

        if (message == null)
        {
            message = defaultAccessDeniedMessage;
        }

        return message;
    }

    private OwRoleManager getRoleManager()
    {
        OwFNCM5Network localNetwork = OwFNCM5Network.localNetwork();
        OwRoleManager roleManager = localNetwork.getRoleManager();
        return roleManager;
    }

    private boolean hasAccessMaskRight(OwRoleManager roleManager_p, int iCategory_p, String strResourceID_p, int requiredAccessMask_p) throws OwException
    {
        try
        {
            return roleManager_p.hasAccessMaskRight(iCategory_p, strResourceID_p, requiredAccessMask_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Event manager exception!", e);
        }
    }

    private boolean hasAccessMaskRight(int iCategory_p, String strResourceID_p, int requiredAccessMask_p) throws OwException
    {
        OwRoleManager roleManager = getRoleManager();
        return hasAccessMaskRight(roleManager, iCategory_p, strResourceID_p, requiredAccessMask_p);
    }

    private boolean hasAccessMaskRight(OwObject object_p, int requiredAccessMask_p) throws OwException
    {
        String className = object_p.getClassName();
        return hasAccessMaskRight(className, requiredAccessMask_p);
    }

    private boolean hasAccessMaskRight(String className_p, int requiredAccessMask_p) throws OwException
    {
        return hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, className_p, requiredAccessMask_p);
    }

    pointcut managedObjectMethod(ObjectAccess access, OwFNCM5Object object): execution(* (OwFNCM5Object || OwFNCM5Document).*(..) )  && @annotation(access) && target(object);

    pointcut managedNetworkMethod(Access access, OwFNCM5Network network): execution(* (OwFNCM5Network).*(..) )  && @annotation(access) && target(network);

    pointcut managedObjectReturn(ObjectAccess access): execution((OwObject|| OwFNCM5Object)  *.*(..) )  && @annotation(access);

    pointcut managedObjectCollectionReturn(ObjectAccess access): execution(OwObjectCollection  *.*(..) )  && @annotation(access);

    pointcut newObject() : execution(* *OwFNCM5Network.createNewObject(..) ) ;

    before(ObjectAccess access, OwFNCM5Object object) throws OwException : managedObjectMethod(access,object){
        int mask = access.mask();
        if (!hasAccessMaskRight(object, mask))
        {
            Signature signature = thisJoinPointStaticPart.getSignature();
            Logger logger = OwLog.getLogger(signature.getDeclaringType());
            logger.debug(signature + " : you do not have the necessary athirization to " + access.name());
            throw new OwAccessDeniedException(maskToMessage(mask));
        }
    }

    before(Access access, OwFNCM5Network network) throws OwException : managedNetworkMethod(access,network){
        int mask = access.mask();
        if (!hasAccessMaskRight(access.category(), access.resource(), mask))
        {
            Signature signature = thisJoinPointStaticPart.getSignature();
            Logger logger = OwLog.getLogger(signature.getDeclaringType());
            logger.debug(signature + " : you do not have the necessary athirization to " + access.name());
            throw new OwAccessDeniedException(maskToMessage(mask));
        }
    }

    before(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p, boolean fKeepCheckedOut_p) throws OwException : 
                newObject() && 
                args(fPromote_p,mode_p,resource_p,strObjectClassName_p,properties_p,permissions_p,content_p,parent_p,strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p)
   {
        if (!hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, strObjectClassName_p, OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE))
        {
            throw new OwAccessDeniedException(maskToMessage(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES));
        }
    }

    after(ObjectAccess access) returning(Object object) throws OwException : managedObjectReturn(access) {
        int mask = access.mask();
        if (!hasAccessMaskRight((OwObject) object, mask))
        {
            Signature signature = thisJoinPointStaticPart.getSignature();
            Logger logger = OwLog.getLogger(signature.getDeclaringType());
            logger.debug(signature + " : you do not have the necessary athirization to " + access.name());
            throw new OwAccessDeniedException(maskToMessage(mask));
        }
    }

    after(ObjectAccess access) returning(OwObjectCollection objectCollection) throws OwException : managedObjectCollectionReturn(access) {
        if (objectCollection != null)
        {
            int mask = access.mask();
            for (Iterator i = objectCollection.iterator(); i.hasNext();)
            {
                OwObject object = (OwObject) i.next();
                if (!hasAccessMaskRight(object, mask))
                {
                    i.remove();
                }
            }
        }
    }

}
