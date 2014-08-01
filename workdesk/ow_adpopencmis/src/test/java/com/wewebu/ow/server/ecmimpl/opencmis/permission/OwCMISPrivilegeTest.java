package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Principal;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlEntryImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlPrincipalDataImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OwCMISPrivilegeTest
{
    protected OwCMISPrivilege privilege;

    @Before
    public void setUp()
    {
        Principal principal = new AccessControlPrincipalDataImpl("principal");

        List<String> perms = new LinkedList<String>();
        for (int i = 0; i < 10; i++)
        {
            perms.add("permission_" + i);
        }

        Ace ace = new AccessControlEntryImpl(principal, perms);

        privilege = new OwCMISPrivilege(ace, principal.getId());
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testHasChilds()
    {
        Assert.assertNotNull(privilege);
        Assert.assertFalse(privilege.hasChilds());
    }

    @Test
    public void testGetChilds()
    {
        Assert.assertNotNull(privilege.getChilds(false));
        Assert.assertTrue(privilege.getChilds(false).isEmpty());

        Assert.assertNotNull(privilege.getChilds(true));
        Assert.assertTrue(privilege.getChilds(true).isEmpty());
    }

    @Test
    public void testGetNameDescription()
    {
        String name = privilege.getName();
        Assert.assertNotNull(name);

        String description = privilege.getDescription();
        Assert.assertNotNull(description);

        Assert.assertEquals(name, description);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertTrue(0 != privilege.hashCode());

        Assert.assertEquals(privilege.hashCode(), privilege.getName().hashCode());
    }

    @Test
    public void testEquals()
    {
        Assert.assertEquals(privilege, privilege);
        Assert.assertFalse(privilege.equals(null));

        OwCMISPrivilege compare = new OwCMISPrivilege(null, privilege.getName());
        Assert.assertTrue(privilege.equals(compare));

        compare = new OwCMISPrivilege(null, "otherName");
        Assert.assertFalse(privilege.equals(compare));

        compare = new OwCMISPrivilege(null, null);
        Assert.assertFalse(privilege.equals(compare));

        Assert.assertFalse(privilege.equals("otherName"));
    }
}
