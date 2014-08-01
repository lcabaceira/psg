package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.ReservationType;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwStandardLocalizeableEnum;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Generic (non P8 5.0 Engine) class base.
 * Allows further non-engine content-object-class hierarchy extension.
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
//NON-Thread-safe Implementation
public abstract class OwFNCM5GenericClass<N, R extends OwFNCM5Resource> implements OwFNCM5Class<N, R>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5GenericClass.class);

    private static final List<OwStandardLocalizeableEnum> DEFAULT_CHECKIN_MODES = new LinkedList<OwStandardLocalizeableEnum>();
    static
    {
        DEFAULT_CHECKIN_MODES.add(new OwStandardLocalizeableEnum(AutoClassify.DO_NOT_AUTO_CLASSIFY, new OwString("fncm.OwFNCMDocumentObject.checkinmodenormal", "Normal")));
        DEFAULT_CHECKIN_MODES.add(new OwStandardLocalizeableEnum(AutoClassify.AUTO_CLASSIFY, new OwString("fncm.OwFNCMDocumentObject.checkinmodeautoclassify", "Autoclassify")));
    }

    private static final List<OwStandardLocalizeableEnum> DEFAULT_CHECKOUT_MODES = new LinkedList<OwStandardLocalizeableEnum>();
    static
    {
        DEFAULT_CHECKOUT_MODES.add(new OwStandardLocalizeableEnum(Integer.valueOf(ReservationType.OBJECT_STORE_DEFAULT_AS_INT), new OwString("fncm.OwFNCMDocumentObject.checkoutmodedefault", "As set in object store")));
        DEFAULT_CHECKOUT_MODES.add(new OwStandardLocalizeableEnum(Integer.valueOf(ReservationType.COLLABORATIVE_AS_INT), new OwString("fncm.OwFNCMDocumentObject.checkoutmodecollaborative", "Collaborative")));
        DEFAULT_CHECKOUT_MODES.add(new OwStandardLocalizeableEnum(Integer.valueOf(ReservationType.EXCLUSIVE_AS_INT), new OwString("fncm.OwFNCMDocumentObject.checkoutmodeexclusive", "Exclusive")));
        DEFAULT_CHECKOUT_MODES.add(new OwStandardLocalizeableEnum(Integer.valueOf(ReservationType.REPLICATED_AS_INT), new OwString("fncm.OwFNCMDocumentObject.checkoutmodereplicated", "Replicated")));
    }

    private OwFNCM5ResourceAccessor<R> resourceAccessor;

    public OwFNCM5GenericClass(OwFNCM5ResourceAccessor<R> resourceAccessor_p)
    {
        this.resourceAccessor = resourceAccessor_p;
    }

    public R getResource() throws OwException
    {
        return this.resourceAccessor.get();
    }

    public boolean canCreateNewObject() throws OwException
    {
        R resource = getResource();
        OwFNCM5Network network = resource.getNetwork();
        OwRoleManager roleManager = network.getRoleManager();
        try
        {
            return roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_OBJECT_CLASSES, getClassName(), OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.debug("Could not verify access rights.", e);
            throw new OwServerException("Could not verify access rights.", e);
        }
    }

    public Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        return null;
    }

    public List getModes(int operation_p) throws OwException
    {
        switch (operation_p)
        {
            case OwObjectClass.OPERATION_TYPE_CREATE_NEW_OBJECT:
            case OwObjectClass.OPERATION_TYPE_CHECKIN:
                return new LinkedList<OwStandardLocalizeableEnum>(DEFAULT_CHECKIN_MODES);

            case OwObjectClass.OPERATION_TYPE_CHECKOUT:
                return new LinkedList<OwStandardLocalizeableEnum>(DEFAULT_CHECKOUT_MODES);

            default:
                return null;
        }
    }

    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws OwException
    {
        return false;
    }

}
