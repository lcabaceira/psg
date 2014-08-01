package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwComparisonOperator;
import com.wewebu.ow.csqlc.ast.OwComparisonPredicate;
import com.wewebu.ow.csqlc.ast.OwSearchCondition;
import com.wewebu.ow.csqlc.ast.OwSignedNumericLiteral;
import com.wewebu.ow.csqlc.ast.OwWhereClause;

public class OwWhereClauseTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testClause() throws Exception
    {
        OwColumnReference column = new OwColumnReference(new OwColumnQualifier(), "cmis:count");
        OwSignedNumericLiteral literal = new OwSignedNumericLiteral(-32123);
        OwSearchCondition searchCondition = new OwComparisonPredicate(column, OwComparisonOperator.LT, literal);
        OwWhereClause whereClause = new OwWhereClause(searchCondition);
        assertEquals("WHERE cmis:count<-32123", whereClause.createWhereClauseSQLString().toString());
    }
}
