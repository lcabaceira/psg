package com.wewebu.expression.language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *<p>
 * Binary conversion tables are bidimensional-array like structures holding information on how
 * a binary operator should perform type conversions.<br>
 * Column and row type indexes can be matched against left and right operands type indexes and 
 * the resulted conversion type is the element found at the matched indexes in the table.    
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
public class OwExprBinaryConversionTable
{
    private static final Set NO_CONVERSION = new HashSet();

    private OwExprType[][] m_conversionTable = new OwExprType[OwExprType.ALL_TYPES.length][OwExprType.ALL_TYPES.length];

    private Map m_type1Conversions = new HashMap();

    private Map m_type2Conversions = new HashMap();

    /**
     * Constructor.
     * The {@link #m_conversionTable} internal table is filled with {@link OwExprType#NOTYPE} 
     */
    public OwExprBinaryConversionTable()
    {
        for (int i = 0; i < m_conversionTable.length; i++)
        {
            Arrays.fill(m_conversionTable[i], OwExprType.NOTYPE);
        }
    }

    /**
     * Adds a directional conversion to the desiganted conversions map.
     * <br>
     * The conversion rule states that the type_p type will be converted to the conversion_p type.
     * @param conversionsMap_p
     * @param conversion_p
     * @param type_p
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
     * 
     * @param types_p
     * @return all types of the left side operand that might result in the desiganted types_p type 
     *         set if conversion rules defined by this table are applyed  
     */
    public Set getConversionsType1(Set types_p)
    {
        Set conversions = new HashSet();
        for (Iterator i = types_p.iterator(); i.hasNext();)
        {
            OwExprType type = (OwExprType) i.next();
            conversions.addAll(getConversionsType1(type));
        }

        return conversions;
    }

    /**
     * 
     * @param types_p
     * @return all types of the right side operand that might result in the desiganted types_p type 
     *         set if conversion rules defined by this table are applyed  
     */
    public Set getConversionsType2(Set types_p)
    {
        Set conversions = new HashSet();
        for (Iterator i = types_p.iterator(); i.hasNext();)
        {
            OwExprType type = (OwExprType) i.next();
            conversions.addAll(getConversionsType2(type));
        }

        return conversions;
    }

    /**
     * 
     * @param type_p
     * @return all types of the left side operand that might result in the desiganted type_p type 
     *         if conversion rules defined by this table are applyed  
     */
    public Set getConversionsType1(OwExprType type_p)
    {
        Set type1Conversions = (Set) this.m_type1Conversions.get(type_p);
        return type1Conversions != null ? type1Conversions : NO_CONVERSION;
    }

    /**
     * 
     * @param type_p
     * @return all types of the right side operand that might result in the desiganted type_p type 
     *         if conversion rules defined by this table are applyed  
     */
    public Set getConversionsType2(OwExprType type_p)
    {
        Set type2Conversions = (Set) this.m_type2Conversions.get(type_p);
        return type2Conversions != null ? type2Conversions : NO_CONVERSION;
    }

    /**
     * Adds a conversion rule 
     * @param type1_p left side operand type 
     * @param types2_p right side operand types 
     * @param conversion_p resulted type
     * @throws RuntimeException if a conversion rule for the specified operands was already added
     * @throws IllegalArgumentException if the converted type is {@link OwExprType#NOTYPE}   
     */
    public final void add(OwExprType type1_p, OwExprType[] types2_p, OwExprType conversion_p)
    {
        for (int i = 0; i < types2_p.length; i++)
        {
            add(type1_p, types2_p[i], conversion_p);
        }
    }

    /**
     * Adds a conversion rule 
     * @param types1_p left side operand types 
     * @param type2_p right side operand type 
     * @param conversion_p resulted type
     * @throws RuntimeException if a conversion rule for the specified operands was already added
     * @throws IllegalArgumentException if the converted type is {@link OwExprType#NOTYPE}   
     */
    public final void add(OwExprType[] types1_p, OwExprType type2_p, OwExprType conversion_p)
    {
        for (int i = 0; i < types1_p.length; i++)
        {
            add(types1_p[i], type2_p, conversion_p);
        }
    }

    /**
     * Adds a conversion rule 
     * @param type1_p left side operand types 
     * @param type2_p right side operand type 
     * @param conversion_p resulted type
     * @throws RuntimeException if a conversion rule for the specified operands was already added
     * @throws IllegalArgumentException if the converted type is {@link OwExprType#NOTYPE}   
     */
    public final void add(OwExprType type1_p, OwExprType type2_p, OwExprType conversion_p)
    {
        if (!conversion_p.equals(OwExprType.NOTYPE))
        {

            if (m_conversionTable[type1_p.m_conversionIndex][type2_p.m_conversionIndex].equals(OwExprType.NOTYPE))
            {
                m_conversionTable[type1_p.m_conversionIndex][type2_p.m_conversionIndex] = conversion_p;
                addDirectionalConversion(m_type1Conversions, conversion_p, type1_p);
                addDirectionalConversion(m_type2Conversions, conversion_p, type2_p);
            }
            else
            {
                throw new RuntimeException("The binary conversion table only supports NOTYPE-override !");
            }
        }
        else
        {
            throw new IllegalArgumentException("The binary conversion table does not support delete-conversion-by-NOTYPE!");
        }
    }

    /**
     * 
     * @param type1_p
     * @param type2_p
     * @return the type that results from applying the current conversion table rules to the specified operand types 
     */
    public final OwExprType convert(OwExprType type1_p, OwExprType type2_p)
    {
        return m_conversionTable[type1_p.m_conversionIndex][type2_p.m_conversionIndex];
    }

    /* JAVADOC generator */
    //    public void jdPrint()
    //    {
    //        StringBuffer jdSB = new StringBuffer();
    //        jdSB.append("<table class=\"jd\">");
    //        jdSB.append("<tr>");
    //        jdSB.append("</td>");
    //        jdSB.append("<td class=\"header\">");
    //        jdSB.append("&nbsp;");
    //        jdSB.append("</td>");
    //        for (int i = 0; i < OwExprType.ALL_TYPE_TYPES.length; i++)
    //        {
    //            jdSB.append("<td class=\"header\">");
    //            jdSB.append("&nbsp;");
    //            jdSB.append(OwExprType.ALL_TYPE_TYPES[i].toString().toUpperCase());
    //            jdSB.append("</td>");
    //        }
    //        jdSB.append("</tr>");
    //        for (int i = 0; i < OwExprType.ALL_TYPE_TYPES.length; i++)
    //        {
    //            jdSB.append("<tr>");
    //            jdSB.append("<td class=\"header\">");
    //            jdSB.append("&nbsp;");
    //            jdSB.append(OwExprType.ALL_TYPE_TYPES[i].toString().toUpperCase());
    //            jdSB.append("</td>");
    //            for (int j = 0; j < OwExprType.ALL_TYPE_TYPES.length; j++)
    //            {
    //
    //                jdSB.append("<td>");
    //                jdSB.append("&nbsp;");
    //                if (!m_conversionTable[i][j].equals(OwExprType.NOTYPE))
    //                {
    //                    jdSB.append(m_conversionTable[i][j].toString().toUpperCase());
    //                }
    //                jdSB.append("&nbsp;");
    //                jdSB.append("</td>");
    //
    //            }
    //            jdSB.append("</tr>");
    //        }
    //        jdSB.append("</tr>");
    //        jdSB.append("</table>");
    //        System.out.println(jdSB.toString());
    //    }
}
