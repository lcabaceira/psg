package com.wewebu.expression;

import java.io.StringReader;

import junit.framework.TestCase;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExpressionType;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprReflectiveScope;
import com.wewebu.expression.language.OwExprTypeMissmatchException;
import com.wewebu.expression.language.OwExprValue;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;

/**
*<p>
* OwExprAllTests. 
*</p>
*
*<p><font size="-2">
* Alfresco Workdesk<br/>
* Copyright (c) Alfresco Software, Inc.<br/>
* All rights reserved.<br/>
* <br/>
* For licensing information read the license.txt file or<br/>
* go to: http://wiki.alfresco.com<br/>
*</font></p>
*/
public abstract class OwExprTestBase extends TestCase
{
    protected OwExprExpression parse(String expressionString_p) throws ParseException
    {

        OwExprParser parser = new OwExprParser(new StringReader(expressionString_p));

        OwExprExpression expression = parser.ExprExpression();
        return expression;
    }

    protected OwExprExpressionType type(String expressionString_p) throws ParseException, OwExprTypeMissmatchException
    {
        OwExprExpression expression = parse(expressionString_p);
        return expression.type();
    }

    protected OwExprValue value(String expressionString_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        OwExprExpression expression = parse(expressionString_p);
        OwExprValue v = expression.evaluate();
        return v;
    }

    protected OwExprValue value(String expressionString_p, String name_p, Object scope_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        return value(expressionString_p, name_p, scope_p, true);
    }

    protected OwExprValue value(String expressionString_p, String name_p, Object scope_p, boolean checkVisibility_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        return value(expressionString_p, new OwExprExternalScope[] { new OwExprReflectiveScope(name_p, scope_p) }, checkVisibility_p);
    }

    protected OwExprValue value(String expressionString_p, OwExprExternalScope scope_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        return value(expressionString_p, scope_p, true);
    }

    protected OwExprValue value(String expressionString_p, OwExprExternalScope scope_p, boolean checkVisibility_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        return value(expressionString_p, new OwExprExternalScope[] { scope_p }, checkVisibility_p);
    }

    protected OwExprValue value(String expressionString_p, OwExprExternalScope[] scopes_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        return value(expressionString_p, scopes_p, true);
    }

    protected OwExprValue value(String expressionString_p, OwExprExternalScope[] scopes_p, boolean checkVisibility_p) throws ParseException, OwExprTypeMissmatchException, OwExprEvaluationException
    {
        OwExprExpression expression = parse(expressionString_p);
        if (expression.hasErrors())
        {
            throw new OwExprEvaluationException("Can't evalueate an eroneous expression : " + expression.getErrorTable());
        }
        if (checkVisibility_p)
        {
            if (!expression.symbolsVisibleInScopes(scopes_p))
            {
                throw new OwExprEvaluationException("Not all symbols are visible in scope !");
            }
        }
        OwExprValue v = expression.evaluate(scopes_p);
        return v;
    }

    protected void assertInvalidExpression(String expressionStr_p) throws ParseException
    {
        OwExprExpression e = parse(expressionStr_p);
        assertTrue("No errors found in : " + expressionStr_p, e.hasErrors());
    }

    protected void assertInvalidValue(String expressionStr_p) throws ParseException
    {
        OwExprExpression e = parse(expressionStr_p);
        assertFalse(expressionStr_p + " - should be error free : " + e.getErrorTable(), e.hasErrors());
        try
        {
            e.evaluate();
            fail("Should not be able to evalueate : " + expressionStr_p);
        }
        catch (OwExprEvaluationException ex)
        {
            ex.printStackTrace();
        }
    }
}
