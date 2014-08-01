package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwCompoundSelectList;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFromClause;
import com.wewebu.ow.csqlc.ast.OwSelectSublist;
import com.wewebu.ow.csqlc.ast.OwSimpleTable;
import com.wewebu.ow.csqlc.ast.OwTableReference;
import com.wewebu.ow.csqlc.ast.OwValueExpression;

public class OwSimpleTableTest extends TestCase
{

    public void testSimpleTable() throws Exception
    {
        OwCompoundSelectList list = new OwCompoundSelectList();
        OwValueExpression vExpr1 = new OwColumnReference(new OwColumnQualifier("cmis:document", "cmis:document"), "cmis:mycolumn1");
        OwValueExpression vExpr2 = new OwColumnReference(new OwColumnQualifier("cmis:document", "cmis:document"), "cmis:mycolumn2");
        OwSelectSublist subList1 = new OwSelectSublist(vExpr1, "OwnColumn");
        OwSelectSublist subList2 = new OwSelectSublist(vExpr2, "OwnColumn2");
        OwSelectSublist subList3 = new OwSelectSublist(vExpr1);
        list.add(subList1);
        list.add(subList2);
        list.add(subList3);

        OwTableReference tableReference = new OwCorrelatedTableName("cmis:document", new OwColumnQualifier("cmis:document", "cmis:document"));
        OwFromClause fromClause = new OwFromClause(tableReference);

        OwSimpleTable simpleTable = new OwSimpleTable(list, fromClause);

        assertEquals("SELECT cmis:mycolumn1 AS OwnColumn,cmis:mycolumn2 AS OwnColumn2,cmis:mycolumn1 FROM cmis:document", simpleTable.createSimpleTableSQLString().toString());

    }
}
