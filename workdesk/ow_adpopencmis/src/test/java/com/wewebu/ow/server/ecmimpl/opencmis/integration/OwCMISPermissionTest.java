package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.Ace;

import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollectionImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPrivilege;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.unittest.util.OwTestUserInfo;

public class OwCMISPermissionTest extends OwCMISIntegrationTest
{
    OwPermissionCollection testObject;

    public OwCMISPermissionTest(String name_p)
    {
        super("CMISPermissionTest", name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();

        Session s = getCmisSession();
        OperationContext ctx = s.createOperationContext();
        ctx.setCacheEnabled(false);
        ctx.setIncludeAcls(true);
        CmisObject obj = getCmisSession().getObjectByPath("/JUnitTest", ctx);
        boolean contains = false;
        for (Ace ace : obj.getAcl().getAces())
        {
            if (ace.isDirect() && ace.getPermissions().contains("cmis:all"))
            {
                contains = true;
            }
        }
        if (!contains)
        {
            List<String> allPerm = new LinkedList<String>();
            allPerm.add("cmis:all");
            Ace ace = s.getBinding().getObjectFactory().createAccessControlEntry(this.adminUser, allPerm);
            obj.getAcl().getAces().add(ace);
        }
        testObject = new OwCMISPermissionCollectionImpl(obj.getAcl(), s);
        assertNotNull(testObject);
    }

    @Override
    protected void tearDown() throws Exception
    {
        getNetwork().logout();
        super.tearDown();
    }

    public void testConstructor() throws OwException
    {
        Session s = getCmisSession();
        OperationContext ctx = s.createOperationContext();
        ctx.setCacheEnabled(false);
        ctx.setIncludeAllowableActions(true);
        ctx.setIncludeAcls(false);
        CmisObject obj = getCmisSession().getObjectByPath("/JUnitTest", ctx);
        //ACL not fetched, no Exception
        new OwCMISPermissionCollectionImpl(obj, s);

        ctx.setIncludeAcls(true);
        obj = getCmisSession().getObjectByPath("/JUnitTest", ctx);
        //ACL prefetched, process different branch
        new OwCMISPermissionCollectionImpl(obj, s);
    }

    public void testGetAvailablePrivileges() throws Exception
    {
        Collection<?> privileges = testObject.getAvailablePrivileges(getNetwork().getCredentials().getUserInfo());
        assertNotNull(privileges);
    }

    public void testAddPrivilegeSet() throws Exception
    {

        OwCMISPrivilege priv = new OwCMISPrivilege(null, "cmis:all");
        List<OwPrivilege> privileges = new LinkedList<OwPrivilege>();
        privileges.add(priv);

        int before = testObject.getAppliedPrivilegeSets().size();

        OwPrivilegeSet newSet = testObject.addPrivilegeSet(getNetwork().getCredentials().getUserInfo(), privileges, false, 0);

        assertNotNull(newSet);
        assertTrue(before < testObject.getAppliedPrivilegeSets().size());
    }

    @SuppressWarnings("unchecked")
    public void testRemovePrivilegeSet() throws Exception
    {
        try
        {
            testObject.removePrivilegeSet(null);
            fail("Null values are not allowed for removePrivilegeSet Call");
        }
        catch (OwInvalidOperationException opEx)
        {
            //OK
        }

        try
        {
            testObject.removePrivilegeSet(new OwPrivilegeSet() {
                @Override
                public OwUserInfo getPrincipal()
                {
                    return null;
                }

                @Override
                public boolean isDenied()
                {
                    return false;
                }

                @SuppressWarnings("rawtypes")
                @Override
                public Collection getPrivileges()
                {
                    return null;
                }

                @Override
                public int getInheritanceDepth()
                {
                    return 0;
                }

            });
            fail("Non-CMIS Privilege Set is not allowed for removePrivilegeSet Call");
        }
        catch (OwInvalidOperationException opEx)
        {
            //OK
        }

        OwCMISPrivilege priv = new OwCMISPrivilege(null, "cmis:all");
        List<OwPrivilege> privileges = new LinkedList<OwPrivilege>();
        privileges.add(priv);

        List<OwPrivilegeSet> colPrivSet = (List<OwPrivilegeSet>) testObject.getAppliedPrivilegeSets();
        assertNotNull(colPrivSet);

        int before = colPrivSet.size();
        assertTrue(before > 0);

        OwPrivilegeSet toRemove = null;
        for (OwPrivilegeSet part : colPrivSet)
        {
            if (part.getInheritanceDepth() == 0)
            {
                toRemove = part;
                break;
            }
        }
        assertNotNull(toRemove);

        testObject.removePrivilegeSet(toRemove);

        assertTrue(before > testObject.getAppliedPrivilegeSets().size());
    }

    public void testExtractId()
    {
        OwUserInfo info = new OwTestUserInfo(null) {
            public String getUserName() throws Exception
            {
                return super.getUserName().substring(5);
            }
        };

        try
        {
            testObject.getAvailablePrivileges(info);
        }
        catch (OwCMISRuntimeException runtimeEx)
        {

        }
    }
}
