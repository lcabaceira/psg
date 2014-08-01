package com.wewebu.ow.server.ecm.eaop;

/**
 * Boolean result {@link OwrReturnTypeCollector}.
 * Can merge results using OR or AND operations.
 * See {@link #newAND(boolean)} and {@link #newOR(boolean)}. 
 *    
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */
public class OwBooleanCollector extends OwrReturnTypeCollector
{
    private static int OR = 1;
    private static int AND = 2;

    public static OwBooleanCollector newOR(boolean default_p)
    {
        return new OwBooleanCollector(OR, default_p);
    }

    public static OwBooleanCollector newAND(boolean default_p)
    {
        return new OwBooleanCollector(AND, default_p);
    }

    private int op = AND;
    private boolean result;

    private OwBooleanCollector()
    {
        this(AND, false);
    }

    private OwBooleanCollector(int op_p, boolean default_p)
    {
        super();
        this.result = default_p;
        if (op_p != AND && op_p != OR)
        {
            throw new RuntimeException("Invalid booleqan collector operation " + this.op);
        }

        this.op = op_p;
    }

    public void collect(boolean result_p)
    {
        result = (op == AND) ? result && result_p : result || result_p;
    }

    public Boolean getResult()
    {
        return result;
    }

}
