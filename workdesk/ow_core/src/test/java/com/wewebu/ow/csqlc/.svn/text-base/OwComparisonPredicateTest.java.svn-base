package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwComparisonOperator;
import com.wewebu.ow.csqlc.ast.OwComparisonPredicate;

public class OwComparisonPredicateTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testPredicate() throws Exception
    {
        OwCharacterStringLiteral myName = new OwCharacterStringLiteral("myname");
        OwComparisonPredicate predicate1 = new OwComparisonPredicate(new OwColumnReference(new OwColumnQualifier("cmis:document", "cmis:document", "d"), "cmis:name"), OwComparisonOperator.EQ, myName);
        assertEquals("d.cmis:name='myname'", predicate1.createPredicateSQLString().toString());
        OwComparisonPredicate predicate2 = new OwComparisonPredicate(new OwColumnReference(new OwColumnQualifier(), "cmis:name"), OwComparisonOperator.NEQ, myName);
        assertEquals("cmis:name<>'myname'", predicate2.createPredicateSQLString().toString());
    }
}
