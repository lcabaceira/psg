package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwCompoundSelectList;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFromClause;
import com.wewebu.ow.csqlc.ast.OwMergeType;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.csqlc.ast.OwRepositoryTarget;
import com.wewebu.ow.csqlc.ast.OwSelectSublist;
import com.wewebu.ow.csqlc.ast.OwSimpleTable;
import com.wewebu.ow.csqlc.ast.OwTableReference;
import com.wewebu.ow.csqlc.ast.OwValueExpression;

public class OwQueryStatementTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testStatement() throws Exception
    {
        OwCompoundSelectList list = new OwCompoundSelectList();
        OwValueExpression vExpr1 = new OwColumnReference(new OwColumnQualifier(), "cmis:mycolumn1");
        OwValueExpression vExpr2 = new OwColumnReference(new OwColumnQualifier(), "cmis:mycolumn2");
        OwSelectSublist subList1 = new OwSelectSublist(vExpr1, "OwnColumn");
        OwSelectSublist subList2 = new OwSelectSublist(vExpr2, "OwnColumn2");
        OwSelectSublist subList3 = new OwSelectSublist(vExpr1);
        list.add(subList1);
        list.add(subList2);
        list.add(subList3);

        OwTableReference tableReference = new OwCorrelatedTableName("cmis:document", new OwColumnQualifier("cmis:document", "cmis:document"));
        OwFromClause fromClause = new OwFromClause(tableReference);

        OwSimpleTable simpleTable = new OwSimpleTable(list, fromClause);

        OwQueryStatement statement = new OwQueryStatement(new OwRepositoryTarget("RES_ID", OwMergeType.NONE), simpleTable);

        assertEquals("SELECT cmis:mycolumn1 AS OwnColumn,cmis:mycolumn2 AS OwnColumn2,cmis:mycolumn1 FROM cmis:document", statement.createSQLString().toString());
        assertEquals("RES_ID", statement.getTargetRepositoryID());
    }

    public void testCharaterStringLiteral() throws Exception
    {
        OwCharacterStringLiteral l1 = new OwCharacterStringLiteral("foo");
        assertTrue(l1.contains('o', false));
        assertFalse(l1.contains('o', true));

        OwCharacterStringLiteral l2 = new OwCharacterStringLiteral("foo\\%");

        assertTrue(l2.contains('%', true));
        assertFalse(l2.contains('%', false));

        l1 = l1.replace(1, 3, "aa");
        assertEquals("'faa'", l1.createLiteralSQLString().toString());

        l1 = l1.append("%").insert(0, "%");
        assertEquals("'%faa%'", l1.createLiteralSQLString().toString());
    }

}
