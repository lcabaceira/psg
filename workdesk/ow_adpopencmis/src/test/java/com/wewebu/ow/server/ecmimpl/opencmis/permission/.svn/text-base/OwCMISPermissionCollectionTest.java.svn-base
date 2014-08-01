package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlListImpl;
import org.junit.Assert;
import org.junit.Test;

public class OwCMISPermissionCollectionTest
{
    @Test
    public void testFlags()
    {
        OwCMISPermissionCollectionImpl permCol = new OwCMISPermissionCollectionImpl(new AccessControlListImpl(), null);

        Assert.assertTrue(permCol.canGetPrivileges());
        Assert.assertTrue(permCol.canSetPrivileges());

        Assert.assertFalse(permCol.canDenyPrivileges());
        Assert.assertFalse(permCol.canAddMultiPolicy());
        Assert.assertFalse(permCol.canSetPolicies());
        Assert.assertFalse(permCol.canGetPolicies());
        Assert.assertTrue(permCol.canEditPermissions().isAllowed());
    }

    @Test
    public void testNullValues()
    {
        OwCMISPermissionCollectionImpl permCol = new OwCMISPermissionCollectionImpl(new AccessControlListImpl(), null);

        Assert.assertNull(permCol.getAvailableInheritanceDepths());
        Assert.assertNull(permCol.getAvailablePolicies(null));
        Assert.assertNull(permCol.getAppliedPolicies());
        Assert.assertNull(permCol.getSession());
    }
}
