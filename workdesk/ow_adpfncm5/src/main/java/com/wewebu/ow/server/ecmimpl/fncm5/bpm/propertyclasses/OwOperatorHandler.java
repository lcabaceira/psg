package com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses;

import java.util.Collection;
import java.util.LinkedList;

import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * Helper class to handle operation for different BPM property definitions.
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
public class OwOperatorHandler
{
    /**
     * Collection of operators which is used for
     * number fields (e.g. Integer, Float and Date)
     */
    public static final Collection<Integer> NUMBER_OPERATORS = new LinkedList<Integer>();

    /**
     * Collection of operators which is used for non-number fields. 
     */
    public static final Collection<Integer> DEFAULT_OPERATORS = new LinkedList<Integer>();

    static
    {
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
    }

    static
    {
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
    }

    private OwOperatorHandler()
    {

    }

    /**
     * Return a collection of operators based on the java class as parameter.
     * If the class is derived from java.lang.Number or java.util.Date
     * the return collection is {@link #NUMBER_OPERATORS}, else
     * the default collection {@link #DEFAULT_OPERATORS} is returned.
     * @param javaClass_p String representing the full qualified java class name
     * @return Collection of Integer
     * @throws ClassNotFoundException if given java class cannot be found
     */
    public static Collection<Integer> getMatchingOperator(String javaClass_p) throws ClassNotFoundException
    {
        Class<?> critClass = Class.forName(javaClass_p);
        if (java.lang.Number.class.isAssignableFrom(critClass) || java.util.Date.class.isAssignableFrom(critClass))
        {
            // === either a number or a date value
            return NUMBER_OPERATORS;
        }
        else
        {
            return DEFAULT_OPERATORS;
        }
    }

}
