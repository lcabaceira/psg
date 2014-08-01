package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwCompoundSelectList;
import com.wewebu.ow.csqlc.ast.OwSelectAll;
import com.wewebu.ow.csqlc.ast.OwSelectList;
import com.wewebu.ow.csqlc.ast.OwSelectSublist;
import com.wewebu.ow.csqlc.ast.OwValueExpression;

public class OwSelectListTest extends TestCase
{

    public void testSelectListAll() throws Exception
    {
        OwSelectList list = new OwSelectAll();
        assertEquals("*", list.createSelectListSQLString().toString());
    }

    public void testSelectListSublist() throws Exception
    {
        OwCompoundSelectList list = new OwCompoundSelectList();
        OwValueExpression vExpr1 = new OwColumnReference(new OwColumnQualifier("tt", "tt_T", "a"), "cmis:mycolumn1");
        OwValueExpression vExpr2 = new OwColumnReference(new OwColumnQualifier("zz", "zz_T", "b"), "cmis:mycolumn2");
        OwSelectSublist subList1 = new OwSelectSublist(vExpr1, "OwnColumn");
        OwSelectSublist subList2 = new OwSelectSublist(vExpr2, "OwnColumn2");
        OwSelectSublist subList3 = new OwSelectSublist(vExpr1);
        list.add(subList1);
        list.add(subList2);
        list.add(subList3);

        assertEquals("a.cmis:mycolumn1 AS OwnColumn,b.cmis:mycolumn2 AS OwnColumn2,a.cmis:mycolumn1", list.createSelectListSQLString().toString());
    }

    public void testSelectListSublistContainsColumnReference() throws Exception
    {
        OwCompoundSelectList list = new OwCompoundSelectList();
        OwValueExpression vExpr0 = new OwColumnReference(new OwColumnQualifier("tt", "tt_T", "a"), "cmis:mycolumn0");
        OwValueExpression vExpr1 = new OwColumnReference(new OwColumnQualifier("tt", "tt_T", "b"), "cmis:mycolumn1");
        OwValueExpression vExpr2 = new OwColumnReference(new OwColumnQualifier("zz", "zz_T", "c"), "cmis:mycolumn2");
        OwValueExpression vExpr3 = new OwColumnReference(new OwColumnQualifier("zz", "zz_T", "d"), "cmis:mycolumn1");
        OwSelectSublist subList0 = new OwSelectSublist(vExpr0, "OwnColumn0");
        OwSelectSublist subList1 = new OwSelectSublist(vExpr1, "OwnColumn1");
        OwSelectSublist subList2 = new OwSelectSublist(vExpr2, "OwnColumn2");
        OwSelectSublist subList3 = new OwSelectSublist(vExpr3, "OwnColumn3");
        list.add(subList0);
        list.add(subList1);
        list.add(subList2);
        list.add(subList3);

        assertTrue(list.containsColumnReference("tt", "cmis:mycolumn1"));
        assertFalse(list.containsColumnReference("tt", "cmis:mycolumn2"));
        assertFalse(list.containsColumnReference("zz", "cmis:mycolumn0"));
        assertTrue(list.containsColumnReference("zz", "cmis:mycolumn1"));
    }
}
