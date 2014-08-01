package com.wewebu.ow.server.ecmimpl.opencmis.util;

import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

/**
 *<p>
 *  Helper to compare two PropertyDefinition instances.
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
public class OwPropertyDefinitionHelper
{

    /**
     * Compare base attributes of the property definition, if it is not returning on main checks 
     * the {@link #isInheritanceDifferent(PropertyDefinition, PropertyDefinition)} is invoked to
     * compare deeper. Compared are the attributes:<br />
     * <code>
     *  a.Id == b.Id;<br />
     *  a.type == b.type;<br />
     *  a.cardinality == b.cardinality;<br />
     *  a.queryName == b.queryName;
     * </code>
     * 
     * @param a PropertyDefinition
     * @param b PropertyDefinition
     * @return true if different
     */
    public static boolean isDifferent(PropertyDefinition<?> a, PropertyDefinition<?> b)
    {
        if (a.getId().equals(b.getId()))
        {
            if (a.getPropertyType().equals(b.getPropertyType()))
            {
                if (a.getCardinality().equals(b.getCardinality()))
                {
                    if (a.getQueryName() != null && b.getQueryName() != null)
                    {
                        if (a.getQueryName().equals(b.getQueryName()))
                        {
                            return isInheritanceDifferent(a, b);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Assuming that the property definitions are linked somehow, or represent
     * an inheritance dependency, this method will check the other attributes
     * and tell if there are differences.
     * @param a PropertyDefinition
     * @param b PropertyDefinition
     * @return true if definition's are different, false else
     */
    public static boolean isInheritanceDifferent(PropertyDefinition<?> a, PropertyDefinition<?> b)
    {
        if (a.getUpdatability().equals(b.getUpdatability()))
        {
            if (a.isOrderable().equals(b.isOrderable()))
            {
                if (a.isRequired().equals(b.isRequired()))
                {
                    if ((a.getChoices() == null || a.getChoices().isEmpty()) && (b.getChoices() == null || b.getChoices().isEmpty()))
                    {
                        List<?> alst = a.getDefaultValue();
                        List<?> blst = b.getDefaultValue();
                        if (alst == null && blst == null)
                        {
                            return false;
                        }
                        else
                        {
                            if (alst != null && blst != null)
                            {
                                if (alst.size() == blst.size())
                                {
                                    return false;
                                }
                            }
                        }
                    }
                    else
                    {
                        if (a.getChoices() != null && b.getChoices() != null)
                        {
                            if ((a.isOpenChoice() != null && b.isOpenChoice() != null) && a.isOpenChoice().equals(b.isOpenChoice()))
                            {
                                List<?> aChoice = a.getChoices();
                                List<?> bChoice = b.getChoices();
                                if (aChoice.size() == bChoice.size())
                                {//Maybe incorrect since choices can be hierarchical
                                    return false;
                                }
                            }
                            else
                            {
                                if (a.isOpenChoice() == null && b.isOpenChoice() == null)
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}
