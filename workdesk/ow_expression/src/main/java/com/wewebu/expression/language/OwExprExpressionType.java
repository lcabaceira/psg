package com.wewebu.expression.language;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *<p>
 * Compiled expression-type class.<br/>
 * Compiled expressions might result in multiple value types (see {@link OwExprType}) (the resulted type 
 * depends on the properties and function types which are discovered at evaluation time). Objects of 
 * {@link OwExprExpressionType} are a collection of possible value types - called inferred types - 
 * (see {@link OwExprType}) an expression might result in.<br/>
 * Expression types are produced during compilation time and they change (that is to say they are 
 * regressed) as sub expression or operands are parsed and added to the holding expression.<br/>
 * The expression types are regressed based on operator conversion tables and sub expression types.<br/>
 * Example:<br/>
 * <b><code>1+a=c</code><br/></b>
 * In the above expression the operator <b><code>=</code></b> is applied to operands <b><code>1+a</code></b> and <b><code>c</code></b>.<br/>
 * According to <b><code>+</code></b> conversion table <b><code>1+a</code></b> can only result in {STRING,NUMERIC,TIME,DATE}.<br/>
 * According to the  <b><code>=</code></b> conversion table this operator results in a BOOLEAN value always so 
 * the resulted expression-type will be a BOOLEAN.<br/>
 * The  same conversion table the operands must be the same or at least one should be a STRING or NULL
 * so the expression-type of <b><code>1+a</code></b> and <b><code>c</code></b> should be {STRING,NUMERIC,TIME,DATE}.
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
public class OwExprExpressionType
{
    public static final OwExprExpressionType NOTYPE = new OwExprExpressionType(OwExprType.NOTYPE);

    /**
     * Pretty printer of types
     * @param types_p
     * @return a pretty print String representation of the specified expression types
     */
    public static final String printTypes(OwExprExpressionType[] types_p)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        if (types_p.length > 0)
        {
            sb.append(types_p[0].toString());
        }
        for (int i = 1; i < types_p.length; i++)
        {
            sb.append(",");
            sb.append(types_p[i].toString());
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * Types that can be inferred from this expression type.
     * The holding expression can result in any of this types.
     */
    protected Set m_inferringTypes = new HashSet();

    /**
     * Constructor
     * @param type_p the only inferred type
     */
    public OwExprExpressionType(OwExprType type_p)
    {
        this(new OwExprType[] { type_p });
    }

    /**
     * Constructor
     * @param inferringTypes_p array of initially inferred types
     */
    public OwExprExpressionType(OwExprType[] inferringTypes_p)
    {
        this(Arrays.asList(inferringTypes_p));
    }

    /**
     * Constructor
     * @param inferringTypes_p collection of initially inferred types
     */
    public OwExprExpressionType(Collection inferringTypes_p)
    {
        this.m_inferringTypes.addAll(inferringTypes_p);
    }

    /**
     * Reduces the inferred type to the intersection of the passed types and the current inferred types.
     * @param types_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise
     * @throws OwExprTypeMissmatchException if the current inferred types set intersection with the types_p type set is void 
     *         -the regression/subset is not possible  
     */
    private boolean subsetTypes(Set types_p) throws OwExprTypeMissmatchException
    {
        if (types_p.isEmpty())
        {
            throw new OwExprTypeMissmatchException("Incompatible types for expression " + printTypes(m_inferringTypes));
        }

        Set typesCopy = new HashSet(m_inferringTypes);
        boolean regressed = false;
        for (Iterator i = typesCopy.iterator(); i.hasNext();)
        {
            OwExprType type = (OwExprType) i.next();
            if (!types_p.contains(type))
            {
                i.remove();
                regressed = true;
            }
        }
        if (typesCopy.isEmpty())
        {
            throw new OwExprTypeMissmatchException("Incompatible types " + printTypes(m_inferringTypes) + " and " + printTypes(types_p));
        }
        else
        {
            m_inferringTypes = typesCopy;
        }

        return regressed;
    }

    /**
     * Reduces the current inferred types to the intersection of 
     * the inferred types of type_p and the current inferred types set.
     * @param type_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise
     * @throws OwExprTypeMissmatchException if the current inferred types set intersection with the types_p type set is void 
     *         -the regression/subset is not possible  
     */
    final boolean regressTo(OwExprType type_p) throws OwExprTypeMissmatchException
    {
        HashSet typeSet = new HashSet();
        typeSet.add(type_p);
        return regressTo(typeSet);

    }

    /**
     * Reduces the current inferred types to the intersection of 
     * the  types  types_p and the current inferred types set.
     * @param types_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise
     * @throws OwExprTypeMissmatchException if the current inferred types set intersection with the types_p type set is void 
     *         -the regression/subset is not possible  
     */
    final boolean regressTo(OwExprType[] types_p) throws OwExprTypeMissmatchException
    {
        HashSet typeSet = new HashSet();
        typeSet.addAll(Arrays.asList(types_p));
        return regressTo(typeSet);
    }

    /**
     * Reduces the current inferred types to the intersection of 
     * the  types set  regressedTypes_p and the current inferred types set.
     * @param regressedTypes_p
     * @return <code>true</code> if the current inferred types were changed in any way , <code>false</code> otherwise
     * @throws OwExprTypeMissmatchException if the current inferred types set intersection with the types_p type set is void 
     *         -the regression/subset is not possible  
     */
    boolean regressTo(Set regressedTypes_p) throws OwExprTypeMissmatchException
    {
        return subsetTypes(regressedTypes_p);
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprExpressionType)
        {
            OwExprExpressionType typeObj = (OwExprExpressionType) obj_p;

            if (typeObj.m_inferringTypes.size() != this.m_inferringTypes.size())
            {
                return false;
            }
            else
            {
                for (Iterator i = this.m_inferringTypes.iterator(); i.hasNext();)
                {
                    OwExprType type = (OwExprType) i.next();
                    if (!typeObj.m_inferringTypes.contains(type))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return new Integer(m_inferringTypes.size()).hashCode();
    }

    /**
     * Pretty printer of this expression type.
     * @param types_p
     * @return a pretty-print String representation of this expression type.
     */
    private String printTypes(Set types_p)
    {
        StringBuffer sb = new StringBuffer();

        if (types_p.size() != 1)
        {
            sb.append("{");
        }
        Iterator it = types_p.iterator();
        if (it.hasNext())
        {
            sb.append(it.next());
        }
        for (; it.hasNext();)
        {
            Object typeObject = it.next();
            sb.append(",");
            sb.append(typeObject);
        }
        if (types_p.size() != 1)
        {
            sb.append("}");
        }
        return sb.toString();
    }

    /**
     * Regresses (diminishes the possible types represented by this {@link OwExprExpression} )
     * this expression type to the types represented the argument  <code>expressionType_p</code>
     * @param expressionType_p types to regress this expression type to 
     * @throws OwExprTypeMissmatchException if the regression process fails (type incompatibility- 
     *                                      the expressionType_p types can't be combined in a subset of 
     *                                      the types represented by this expression type )
     */
    public void regressTo(OwExprExpressionType expressionType_p) throws OwExprTypeMissmatchException
    {
        regressTo(expressionType_p.m_inferringTypes);
    }

    /**
     * 
     * @param type_p
     * @return true if the holder expression of this expression type can result in value have the type_p type
     */
    public boolean canInfer(OwExprType type_p)
    {
        return m_inferringTypes.contains(type_p) || type_p.equals(OwExprType.SCOPE) || type_p.equals(OwExprType.NULL);
    }

    public String toString()
    {
        return printTypes(m_inferringTypes);
    }

    /**
     * 
     * @return the inferred types set
     */
    public final Set getInferringTypes()
    {
        return m_inferringTypes;
    }

}
