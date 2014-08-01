package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.definitions.PermissionDefinition;
import org.apache.chemistry.opencmis.commons.enums.Action;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwPolicy;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwReason;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwStandardReason;
import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwRuntimeException;

/**
 *<p>
 * Simple Implementation of OwPermissionCollection.
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
@SuppressWarnings("rawtypes")
public class OwCMISPermissionCollectionImpl implements OwCMISPermissionCollection
{
    private static final Logger LOG = OwLog.getLogger(OwCMISPermissionCollectionImpl.class);

    private Acl acl;
    private boolean setPrivileges;
    //    private boolean setPolicies;
    private OwCMISAclDiffImpl diff;
    private CmisObject nativeObject;
    private Session session;

    public OwCMISPermissionCollectionImpl(CmisObject natObj, Session session)
    {
        setPrivileges = natObj.getAllowableActions().getAllowableActions().contains(Action.CAN_APPLY_ACL);
        //        setPolicies = natObj.getAllowableActions().getAllowableActions().contains(Action.CAN_APPLY_POLICY);
        this.nativeObject = natObj;
        this.session = session;
        if (getNativeObject().getAcl() == null)
        {
            reset(session.getAcl(getNativeObject(), false));
        }
        else
        {
            reset(getNativeObject().getAcl());
        }
    }

    public OwCMISPermissionCollectionImpl(Acl acl, Session session)
    {
        setPrivileges = true;
        //        setPolicies = natObj.getAllowableActions().getAllowableActions().contains(Action.CAN_APPLY_POLICY);
        this.session = session;
        this.acl = acl;
    }

    @Override
    public boolean canGetPrivileges()
    {
        return true;
    }

    @Override
    public boolean canSetPrivileges()
    {
        return setPrivileges;
    }

    @Override
    public boolean canDenyPrivileges()
    {
        return false;
    }

    @Override
    public Collection getAvailablePrivileges(OwUserInfo principal_p)
    {
        LinkedList<OwCMISPrivilege> privileges = new LinkedList<OwCMISPrivilege>();
        LinkedList<String> filter = new LinkedList<String>();
        if (principal_p != null)
        {
            String principalId = extractId(principal_p);

            for (Ace ace : getAcl().getAces())
            {
                if (ace.getPrincipalId().equals(principalId))
                {
                    filter.addAll(ace.getPermissions());
                }
            }
        }
        for (PermissionDefinition def : this.session.getRepositoryInfo().getAclCapabilities().getPermissions())
        {
            if (!filter.contains(def.getId()))
            {
                privileges.add(new OwCMISPrivilege(null, def.getId()));
            }
        }
        return privileges;
    }

    @Override
    public Map getAvailableInheritanceDepths()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwPrivilegeSet addPrivilegeSet(OwUserInfo principal_p, Collection privileges_p, boolean deny_p, int inheritancedepth_p) throws OwException
    {
        List<String> permissions = new LinkedList<String>();
        Iterator<OwPrivilege> it = privileges_p.iterator();
        while (it.hasNext())
        {
            permissions.add(it.next().getName());
        }

        Ace added = getSession().getObjectFactory().createAce(extractId(principal_p), permissions);

        getDiff().add(added);

        return new OwCMISPrivilegeSet(principal_p, added);
    }

    @Override
    public Collection getAppliedPrivilegeSets()
    {
        Acl defLst = getAcl();
        List<OwCMISPrivilegeSet> sets = new LinkedList<OwCMISPrivilegeSet>();
        List<Ace> base = new LinkedList<Ace>(defLst.getAces());
        if (this.diff != null)
        {
            if (this.diff.getDeleted() != null)//remove delete first
            {
                Iterator<Ace> it = base.iterator();
                while (it.hasNext())
                {
                    Ace in = it.next();
                    if (in.isDirect())
                    {
                        Iterator<Ace> delIt = diff.getDeleted().iterator();
                        while (delIt.hasNext())
                        {
                            Ace del = delIt.next();
                            if (in.getPrincipalId().equals(del.getPrincipalId()))
                            {
                                List<String> inLst = in.getPermissions();
                                List<String> delLst = del.getPermissions();
                                if (inLst.size() == delLst.size())
                                {
                                    Collections.sort(inLst);
                                    Collections.sort(delLst);
                                    if (inLst.equals(delLst))
                                    {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (diff.getAdded() != null)//add newly created
            {
                base.addAll(diff.getAdded());
            }
        }
        //Transform into OwPrivilegeSet objects
        for (Ace ace : base)
        {
            OwCMISUserInfo usr = new OwCMISUserInfo(ace.getPrincipalId(), null);
            OwCMISPrivilegeSet set = new OwCMISPrivilegeSet(usr, ace);
            sets.add(set);
        }
        return sets;
    }

    @Override
    public void removePrivilegeSet(OwPrivilegeSet privilegeSet_p) throws OwException
    {
        if (privilegeSet_p instanceof OwCMISPrivilegeSet)
        {//only mark as deleted
            Ace delete = ((OwCMISPrivilegeSet) privilegeSet_p).nativeObject();
            getDiff().remove(delete);
        }
        else
        {
            if (privilegeSet_p == null)
            {
                throw new OwInvalidOperationException("The to be deleted PrivilegeSet cannot be null");
            }
            else
            {
                throw new OwInvalidOperationException("The provided PrivilegeSet is not supported, type = " + privilegeSet_p);
            }
        }
    }

    @Override
    public boolean canGetPolicies()
    {
        return false;
    }

    @Override
    public boolean canSetPolicies()
    {
        return false;
    }

    @Override
    public boolean canAddMultiPolicy()
    {
        return false;
    }

    @Override
    public Collection getAvailablePolicies(OwUserInfo principal_p)
    {
        return null;
    }

    @Override
    public void addPolicy(OwPolicy policy_p) throws OwException
    {

    }

    @Override
    public Collection getAppliedPolicies()
    {
        return null;
    }

    @Override
    public void removePolicy(OwPolicy policy_p) throws OwException
    {

    }

    @Override
    public OwReason canEditPermissions()
    {
        return OwStandardReason.ALLOWED;
    }

    @Override
    public CmisObject getNativeObject()
    {
        return nativeObject;
    }

    @Override
    public OwCMISAclDiff getDiff()
    {
        if (this.diff == null)
        {
            diff = new OwCMISAclDiffImpl();
        }
        return diff;
    }

    @Override
    public void reset(Acl newAcl_p)
    {
        this.acl = newAcl_p;
        if (diff != null)
        {
            if (diff.getAdded() != null)
            {
                diff.getAdded().clear();
            }
            if (diff.getDeleted() != null)
            {
                diff.getDeleted().clear();
            }
        }
        this.diff = null;
    }

    /**
     * Getter for current native ACL object. 
     * @return org.apache.chemistry.opencmis.commons.data.Acl (or null if explicitly reseted or not available)
     */
    public Acl getAcl()
    {
        return acl;
    }

    @Override
    public Session getSession()
    {
        return session;
    }

    /**(overridable)
     * Helper to extract id from given OwUserInfo object.
     * @param usr OwUserInfo
     * @return String id
     * @throws OwRuntimeException
     */
    protected String extractId(OwUserInfo usr) throws OwRuntimeException
    {
        try
        {
            return usr.getUserName();
        }
        catch (Exception e)
        {
            LOG.error("Cannot extract Id for current user info object", e);
            throw new OwCMISRuntimeException("Unable to extract Id from UserInfo", e);
        }
    }
}
