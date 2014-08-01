package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFromClause;
import com.wewebu.ow.csqlc.ast.OwTableReference;

public class OwFromClauseTest extends TestCase
{
    public void testFromClause() throws Exception
    {
        OwTableReference tableReference = new OwCorrelatedTableName("cmis:document", new OwColumnQualifier("cmis:document", "cmis:document"));
        OwFromClause fromClause = new OwFromClause(tableReference);
        StringBuilder fromSQL = fromClause.createFromClauseSQLString();
        assertEquals("FROM cmis:document", fromSQL.toString());
    }
}
