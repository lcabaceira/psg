package com.wewebu.expression.language;

import com.wewebu.expression.parser.ParseException;
import com.wewebu.expression.parser.TokenMgrError;

/**
 *<p>
 * OwExprUnparsableExpression.
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
public final class OwExprUnparsableExpression extends OwExprExpression
{

    public OwExprUnparsableExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, TokenMgrError tokenMgrError_p)
    {
        super(symbolTable_p, errorTable_p);
        errorTable_p.add(tokenMgrError_p);
    }

    public OwExprUnparsableExpression(OwExprSymbolTable symbolTable_p, OwExprErrorTable errorTable_p, ParseException parseException_p)
    {
        super(symbolTable_p, errorTable_p);
        errorTable_p.add(parseException_p);
    }

    public OwExprValue evaluate(OwExprScope scope_p) throws OwExprEvaluationException
    {
        throw new OwExprEvaluationException("Can not evaluate an unparsable expression!");
    }

    public OwExprExpressionType type() throws OwExprTypeMissmatchException
    {
        return OwExprExpressionType.NOTYPE;
    }

}
