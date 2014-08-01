package com.wewebu.expression.language;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *<p>
 * Unary conversion tables are array like structures holding information on how
 * an unary operator should perform type conversions.<br/>
 * Type indexes can be matched against the operand type index and 
 * the resulted conversion type is the array element found at the matched index. 
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
public class OwExprUnaryConversionTable
{
    /**Empty conversions set.*/
    private static final Set NO_CONVERSION = new HashSet();

    /**Conversion table array*/
    private OwExprType[] m_conversionTable = new OwExprType[OwExprType.ALL_TYPES.length];

    /**Type based conversions map : maps types to a set of possible conversions through this opertator.*/
    private Map m_typeConversions = new HashMap();

    /**
     * {@link Constructor}
     */
    public OwExprUnaryConversionTable()
    {
        Arrays.fill(m_conversionTable, OwExprType.NOTYPE);
    }

    /**
     * Conversion data add a helper.
     * Adds a conversion rule in the given conversionMap_p parameter.
     * If the holder operator of conversionMap_p is applied to type_p the a conversion_p typed value will be returned.      
     * @param conversionsMap_p conversion rules map
     * @param conversion_p the result type 
     * @param type_p operand type 
     */
    private static void addDirectionalConversion(Map conversionsMap_p, OwExprType conversion_p, OwExprType type_p)
    {
        Set conversionFromType = (Set) conversionsMap_p.get(conversion_p);
        if (conversionFromType == null)
        {
            conversionFromType = new HashSet();
            conversionsMap_p.put(conversion_p, conversionFromType);
        }
        conversionFromType.add(type_p);
    }

    /**
     * @param types_p a {@link Set} of {@link OwExprType}
     * @return a {@link Set} of all possible resulting types that can result when this operator would be applied to given types 
     */
    public Set getConversionsType(Set types_p)
    {
        Set conversions = new HashSet();
        for (Iterator i = types_p.iterator(); i.hasNext();)
        {
            OwExprType type = (OwExprType) i.next();
            conversions.addAll(getConversionsType(type));
        }

        return conversions;
    }

    /**
     * 
     * @param type_p an {@link OwExprType}
     * @return a {@link Set} of all possible resulting types that can result when this operator would be applied to given type
     */
    public Set getConversionsType(OwExprType type_p)
    {
        Set typeConversions = (Set) this.m_typeConversions.get(type_p);
        return typeConversions != null ? typeConversions : NO_CONVERSION;
    }

    /**
     * Adds a multiple operand types conversion rule 
     * @param types_p operand types
     * @param conversion_p type that results if this operator is applied to the any of the given types
     */
    public final void add(OwExprType[] types_p, OwExprType conversion_p)
    {
        for (int i = 0; i < types_p.length; i++)
        {
            add(types_p[i], conversion_p);
        }
    }

    /**
     * Adds conversion rule 
     * @param type_p operand type
     * @param conversion_p ype that results if this operator is applied to the given type
     */
    public final void add(OwExprType type_p, OwExprType conversion_p)
    {
        if (!conversion_p.equals(OwExprType.NOTYPE))
        {

            if (m_conversionTable[type_p.m_conversionIndex].equals(OwExprType.NOTYPE))
            {
                m_conversionTable[type_p.m_conversionIndex] = conversion_p;
                addDirectionalConversion(m_typeConversions, conversion_p, type_p);

            }
            else
            {
                throw new RuntimeException("The unary conversion table only supports NOTYPE-overrite !");
            }
        }
        else
        {
            throw new IllegalArgumentException("The binary conversion table does not support delete-conversion-by-NOTYPE!");
        }
    }

    /**
     * @param type_p
     * @return the type that results from applying this operator to a value of a given type 
     */
    public final OwExprType convert(OwExprType type_p)
    {
        return m_conversionTable[type_p.m_conversionIndex];
    }

    /* JAVADOC generator */
    //  public void jdPrint()
    //  {
    //      StringBuffer jdSB = new StringBuffer();
    //      jdSB.append("<table class=\"jd\">");
    //      jdSB.append("<tr>");
    //      
    //      jdSB.append("<td class=\"header\">&nbsp;Operand Type</td>");
    //   
    //      for (int i = 0; i < OwExprType.ALL_TYPE_TYPES.length; i++)
    //      {
    //          jdSB.append("<td class=\"header\">");
    //          jdSB.append("&nbsp;");
    //          jdSB.append(OwExprType.ALL_TYPE_TYPES[i].toString().toUpperCase());
    //          jdSB.append("</td>");
    //      }
    //      jdSB.append("</tr>");
    //      jdSB.append("<tr>");
    //      jdSB.append("<td class=\"header\">&nbsp;Result Type</td>");
    //      for (int i = 0; i < OwExprType.ALL_TYPE_TYPES.length; i++)
    //      {
    //          
    //              jdSB.append("<td>");
    //              jdSB.append("&nbsp;");
    //              if (!m_conversionTable[i].equals(OwExprType.NOTYPE))
    //              {
    //                  jdSB.append(m_conversionTable[i].toString().toUpperCase());
    //              }
    //              jdSB.append("&nbsp;");
    //              jdSB.append("</td>");
    //
    //          
    //      }
    //      jdSB.append("</tr>");
    //      jdSB.append("</table>");
    //      System.out.println(jdSB.toString());
    //  }
}
