package com.wewebu.ow.csqlc;

import junit.framework.TestCase;

import com.wewebu.ow.csqlc.ast.OwBetweenPredicate;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwNullLiteral;
import com.wewebu.ow.csqlc.ast.OwSignedNumericLiteral;

public class OwBetweenPredicateTest extends TestCase
{
    private OwColumnReference aColumn;
    private OwSignedNumericLiteral v1;
    private OwSignedNumericLiteral v2;

    @Override
    protected void setUp() throws Exception
    {
        aColumn = new OwColumnReference(new OwColumnQualifier("table", "type"), "typeColumn");
        v1 = new OwSignedNumericLiteral(-11);
        v2 = new OwSignedNumericLiteral(67);
    }

    public void testDefalut() throws Exception
    {
        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, v1, v2);
            assertTrue(between.isValid());
            assertEquals("(typeColumn>=-11 AND typeColumn<=67)", between.createSearchConditionSQLString().toString());
        }
        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, v1, v2, true);
            assertTrue(between.isValid());
            assertEquals("(typeColumn<-11 OR typeColumn>67)", between.createSearchConditionSQLString().toString());
        }
    }

    public void testOneValue() throws Exception
    {
        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, v1, null);
            assertTrue(between.isValid());
            assertEquals("typeColumn>=-11", between.createSearchConditionSQLString().toString());
        }

        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, null, v2);
            assertTrue(between.isValid());
            assertEquals("typeColumn<=67", between.createSearchConditionSQLString().toString());
        }

        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, v1, null, true);
            assertTrue(between.isValid());
            assertEquals("typeColumn<-11", between.createSearchConditionSQLString().toString());
        }

        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, null, v2, true);
            assertTrue(between.isValid());
            assertEquals("typeColumn>67", between.createSearchConditionSQLString().toString());
        }
    }

    public void testNoValue() throws Exception
    {
        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, null, null, true);
            assertFalse(between.isValid());
        }

        {
            OwBetweenPredicate between = new OwBetweenPredicate(aColumn, new OwNullLiteral(), new OwNullLiteral(), true);
            assertFalse(between.isValid());
        }
    }
}
