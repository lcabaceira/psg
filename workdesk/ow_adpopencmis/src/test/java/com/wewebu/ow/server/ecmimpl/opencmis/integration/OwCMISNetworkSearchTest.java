package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwEcmUtil.OwSimpleSearchClause;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;

//Testing basic Search settings
public class OwCMISNetworkSearchTest extends OwCMISIntegrationTest
{

    public OwCMISNetworkSearchTest(String name_p)
    {
        super("OwCMISNetworkSearchTest", name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
    }

    public void testDoSearchSimple() throws Exception
    {
        OwSimpleSearchClause clause = new OwSimpleSearchClause("cmis:document.cmis:name", OwSearchOperator.CRIT_OP_LIKE, "*");
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:document", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[] { clause }, getNetwork());

        OwObjectCollection col = getNetwork().doSearch(search, null, null, 10, OwSearchTemplate.VERSION_SELECT_DEFAULT);
        assertNotNull(col);
        assertTrue(col.size() <= 10);

        Iterator<?> it = col.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, obj.getType());
        }
    }

    public void testDoSearchVersionALL() throws Exception
    {
        OwSimpleSearchClause clause = new OwSimpleSearchClause("cmis:document.cmis:name", OwSearchOperator.CRIT_OP_LIKE, "*");
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:document", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[] { clause }, getNetwork());

        OwObjectCollection col = getNetwork().doSearch(search, null, null, 10, OwSearchTemplate.VERSION_SELECT_ALL);
        assertNotNull(col);
        assertTrue(col.size() <= 10);

        Iterator<?> it = col.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, obj.getType());
        }
    }

    public void testDoSearchVersionCheckedOut() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:document", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());

        OwObjectCollection col = getNetwork().doSearch(search, null, null, 10, OwSearchTemplate.VERSION_SELECT_CHECKED_OUT);
        assertNotNull(col);
        assertTrue(col.size() <= 10);

        Iterator<?> it = col.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, obj.getType());
        }
    }

    public void testDoSearchInvalidColumns() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:folder", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());
        List<String> columns = new LinkedList<String>();
        columns.add("cmis:folder.test:NotAProperty");

        try
        {
            getNetwork().doSearch(search, null, columns, 10, OwSearchTemplate.VERSION_SELECT_DEFAULT);
            fail("Invalid column property defined, should throw exception!");
        }
        catch (OwObjectNotFoundException ex)
        {
            //OK
        }
    }

    public void testDoSearchPagingInvalidColumns() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:folder", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());
        List<String> columns = new LinkedList<String>();
        columns.add("cmis:folder.test:NotAProperty");

        OwLoadContext ctx = new OwLoadContext();
        ctx.setMaxSize(10l);
        ctx.setPropertyNames(columns);
        ctx.setVersionSelection(OwSearchTemplate.VERSION_SELECT_DEFAULT);

        try
        {
            getNetwork().doSearch(search, ctx);
            fail("Invalid column property defined, should throw exception!");
        }
        catch (OwObjectNotFoundException ex)
        {
            //OK
        }
    }

    public void testDoSearchPaging() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:folder", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());
        List<String> columns = new LinkedList<String>();
        columns.add("cmis:folder.cmis:name");
        columns.add("cmis:folder.cmis:baseTypeId");

        OwLoadContext ctx = new OwLoadContext();
        ctx.setMaxSize(10l);
        ctx.setPropertyNames(columns);
        ctx.setVersionSelection(OwSearchTemplate.VERSION_SELECT_DEFAULT);

        OwIterable<OwCMISObject> col = getNetwork().doSearch(search, ctx);
        assertNotNull(col);
        assertTrue(col.getTotalNumItems() <= 10l);

        Iterator<OwCMISObject> it = col.iterator();
        while (it.hasNext())
        {
            OwCMISObject obj = it.next();
            assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, obj.getType());
        }
    }

    public void testDoSearchNegativeMax() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:folder", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());
        try
        {
            getNetwork().doSearch(search, null, null, -10, OwSearchTemplate.VERSION_SELECT_DEFAULT);
            fail("Invalid maximum defined, must throw exception");
        }
        catch (OwInvalidOperationException ex)
        {
            //OK
        }
    }

    public void testDoSearchPagingNegativeMax() throws Exception
    {
        OwSearchNode search = OwEcmUtil.createSimpleSearchNode("cmis:folder", "Main Repository", "/JUnitTest", new OwSimpleSearchClause[0], getNetwork());
        OwLoadContext ctx = new OwLoadContext();
        ctx.setMaxSize(-10l);
        ctx.setVersionSelection(OwSearchTemplate.VERSION_SELECT_DEFAULT);

        OwIterable<OwCMISObject> result = getNetwork().doSearch(search, ctx);//max size is ignored for paging, no exception is thrown
        assertNotNull(result);
        assertTrue(result.getTotalNumItems() > 0l);
    }
}
