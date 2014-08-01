package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Principal;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlEntryImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlPrincipalDataImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OwCMISAclDiffTest
{
    protected Principal principal1, principal2;

    protected Ace ace1, ace2;

    @Before
    public void setUp()
    {
        principal1 = new AccessControlPrincipalDataImpl("principal1");
        principal2 = new AccessControlPrincipalDataImpl("principal2");

        List<String> perms = new LinkedList<String>();
        for (int i = 0; i < 10; i++)
        {
            perms.add("permission_" + i);
        }

        ace1 = new AccessControlEntryImpl(principal1, perms);
        ace2 = new AccessControlEntryImpl(principal2, new LinkedList<String>(perms));
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testAdd()
    {
        OwCMISAclDiffImpl diff = new OwCMISAclDiffImpl();
        Assert.assertNull(diff.getAdded());

        diff.add(ace1);
        Assert.assertNotNull(diff.getAdded());
        Assert.assertEquals(1, diff.getAdded().size());

        diff.add(ace2);
        Assert.assertTrue(1 < diff.getAdded().size());
    }

    @Test
    public void testDelete()
    {
        OwCMISAclDiffImpl diff = new OwCMISAclDiffImpl();
        Assert.assertNull(diff.getDeleted());

        diff.remove(ace2);
        Assert.assertNotNull(diff.getDeleted());
        Assert.assertEquals(1, diff.getDeleted().size());

        diff.remove(ace1);
        Assert.assertTrue(1 < diff.getDeleted().size());
    }

    @Test
    public void testMixture()
    {
        List<Ace> added = new LinkedList<Ace>();
        added.add(ace1);
        List<Ace> deleted = new LinkedList<Ace>();
        deleted.add(ace2);
        OwCMISAclDiffImpl diff = new OwCMISAclDiffImpl(added, deleted);

        Assert.assertNotNull(diff.getAdded());
        Assert.assertNotNull(diff.getDeleted());

        diff.remove(ace1);
        Assert.assertNotNull(diff.getAdded());
        Assert.assertEquals(0, diff.getAdded().size());

        diff.add(ace1);
        Assert.assertEquals(1, diff.getAdded().size());

        List<String> newPerms = new LinkedList<String>();
        newPerms.add("newPermission");
        newPerms.add("cmis:read");
        newPerms.add("cmis:write");

        int beforeAdd = ace1.getPermissions().size();
        diff.add(new AccessControlEntryImpl(principal1, newPerms));
        Assert.assertEquals(1, diff.getAdded().size());
        Assert.assertEquals(beforeAdd + newPerms.size(), diff.getAdded().get(0).getPermissions().size());

    }
}
