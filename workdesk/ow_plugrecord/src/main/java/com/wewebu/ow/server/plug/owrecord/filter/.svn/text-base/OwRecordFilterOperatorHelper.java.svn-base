package com.wewebu.ow.server.plug.owrecord.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwStandardSearchSpecialNodeOperator;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * Helper class for FieldDefinition retrieval.
 * Creates wrapper classes which have basic
 * operators to be used with the requested FieldDefinition.
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
 *@since 3.2.0.0
 */
public class OwRecordFilterOperatorHelper
{
    /**
     * Collection of operators which is used for
     * number fields (e.g. Integer, Float and Date)
     */
    public static final Collection<Integer> NUMBER_OPERATORS = new LinkedList<Integer>();
    static
    {
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
        NUMBER_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
    }

    /**
     * Collection of operators which is used for non-number fields. 
     */
    public static final Collection<Integer> DEFAULT_OPERATORS = new LinkedList<Integer>();
    static
    {
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_LIKE));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        DEFAULT_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL));
    }

    public static final Collection<Integer> MULTIVAL_OPERATORS = new LinkedList<Integer>();
    static
    {
        MULTIVAL_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NOT_IN));
        MULTIVAL_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_IN));
    }
    public static final Collection<Integer> BOOLEAN_OPERATORS = new LinkedList<Integer>();
    static
    {
        BOOLEAN_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        BOOLEAN_OPERATORS.add(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL));
    }

    /**
     * Process the column info collection depending on the given object to 
     * retrieve field definitions which can be used for filtering.
     * @param obj_p OwObject which act as parent and define the resources to get field definitions
     * @param fieldDefProvider_p OwFieldDefinitionProvider from where the field definition of the properties can be requested
     * @param columnInfoList_p Collection of OwFieldColumnInfo object which should be checked for filtering
     * @return Collection of FieldDefinitions which can be used for filtering
     * @throws Exception if cannot retrieve filter properties from object, or field definitions.
     * @see #getResourceIdsFromObject(OwObject)
     * @see #getFieldDefinition(OwFieldDefinitionProvider, String, String)
     */
    @SuppressWarnings("unchecked")
    public Collection<OwFieldDefinition> collectFilterProperties(OwObject obj_p, OwFieldDefinitionProvider fieldDefProvider_p, Collection columnInfoList_p) throws Exception
    {
        Collection<OwFieldDefinition> filterDef = obj_p.getFilterProperties(columnInfoList_p);
        if (filterDef == null && columnInfoList_p != null && (obj_p.getType() == OwObjectReference.OBJECT_TYPE_FOLDER || obj_p.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER))
        {
            Iterator it = columnInfoList_p.iterator();
            filterDef = new LinkedList<OwFieldDefinition>();
            Iterator<String> idIt = getResourceIdsFromObject(obj_p).iterator();
            String id = idIt.hasNext() ? idIt.next() : null;
            while (it.hasNext())
            {
                OwFieldColumnInfo fci = (OwFieldColumnInfo) it.next();
                OwFieldDefinition fd = null;
                do
                {
                    try
                    {
                        fd = getFieldDefinition(fieldDefProvider_p, fci.getPropertyName(), id);

                        if (fd.isArray() || fd.isComplex())
                        {
                            continue;
                        }
                        filterDef.add(fd);
                    }
                    catch (Exception ex)
                    {
                        if (idIt.hasNext())
                        {
                            id = idIt.next();
                        }
                        else
                        {
                            throw ex;
                        }
                    }
                } while (fd == null && idIt.hasNext());
            }
        }
        return filterDef;
    }

    /**
     * Get a OwFieldDefintion which represents the given property name.
     * May create a wrapper to which provides basic operators.
     * @param fieldDefinitonProvider_p OwFieldDefinitionProvider
     * @param propName_p String symbolic or unique name of property
     * @param resourceId_p String resource id where to search for field definition
     * @return OwFieldDefinition for given property name
     * @throws Exception if cannot get OwFieldDefinition or operators of OwFieldDefinition
     */
    public OwFieldDefinition getFieldDefinition(OwFieldDefinitionProvider fieldDefinitonProvider_p, String propName_p, String resourceId_p) throws Exception
    {
        OwFieldDefinition def = fieldDefinitonProvider_p.getFieldDefinition(propName_p, resourceId_p);
        if (def != null && def.getOperators() == null)
        {
            def = new OwFieldDefWrapper(def);
        }
        return def;
    }

    /**
     * Return a list which contains the IDs from where the object retrieves it children.
     * <p>If the object is a virtual folder, the IDs will be retrieved from the
     * searchtemplate which is used by the object.</p>
     * @param obj_p OwObject from which to request the OwResource(s)
     * @return List of strings representing resource-IDs.
     * @throws Exception if resource ID could not be retrieved, or searchtemplate could not be parsed.
     */
    public List<String> getResourceIdsFromObject(OwObject obj_p) throws Exception
    {
        List<String> lst = new LinkedList<String>();
        if (obj_p.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
        {
            OwStandardSearchSpecialNodeOperator scanner = new OwStandardSearchSpecialNodeOperator();
            scanner.scan(obj_p.getSearchTemplate().getSearch(false));
            List objectStores = scanner.getObjectStores();
            for (Iterator i = objectStores.iterator(); i.hasNext();)
            {
                OwSearchObjectStore searchStore = (OwSearchObjectStore) i.next();
                String osIdentifier = searchStore.getId();
                if (osIdentifier == null)
                {
                    osIdentifier = searchStore.getName();
                }
                lst.add(osIdentifier);
            }
        }
        else
        {
            lst.add(obj_p.getResourceID());
        }
        return lst;
    }

    /**
     *<p>
     * OwFieldDefWrapper.
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
    protected static class OwFieldDefWrapper implements OwFieldDefinition
    {
        OwFieldDefinition nativeDef;

        public OwFieldDefWrapper(OwFieldDefinition nativeDef_p)
        {
            nativeDef = nativeDef_p;
        }

        public String getClassName()
        {
            return this.nativeDef.getClassName();
        }

        public String getDisplayName(Locale locale_p)
        {
            return this.nativeDef.getDisplayName(locale_p);
        }

        public String getDescription(Locale locale_p)
        {
            return this.nativeDef.getDescription(locale_p);
        }

        public String getJavaClassName()
        {
            return this.nativeDef.getJavaClassName();
        }

        public Object getNativeType() throws Exception
        {
            return this.nativeDef.getNativeType();
        }

        public boolean isEnum() throws Exception
        {
            return this.nativeDef.isEnum();
        }

        public OwEnumCollection getEnums() throws Exception
        {
            return this.nativeDef.getEnums();
        }

        public boolean isRequired() throws Exception
        {
            return this.nativeDef.isRequired();
        }

        public Object getMaxValue() throws Exception
        {
            return this.nativeDef.getMaxValue();
        }

        public Object getMinValue() throws Exception
        {
            return this.nativeDef.getMinValue();
        }

        public Object getDefaultValue() throws Exception
        {
            return this.nativeDef.getDefaultValue();
        }

        public boolean isArray() throws Exception
        {
            return this.nativeDef.isArray();
        }

        public Object getValueFromNode(Node node_p) throws Exception
        {
            return this.nativeDef.getValueFromNode(node_p);
        }

        public Object getValueFromString(String text_p) throws Exception
        {
            return this.nativeDef.getValueFromString(text_p);
        }

        public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
        {
            return this.nativeDef.getNodeFromValue(value_p, doc_p);
        }

        public OwFormat getFormat()
        {
            return this.nativeDef.getFormat();
        }

        public Collection getOperators() throws Exception
        {
            if (isArray())
            {
                return MULTIVAL_OPERATORS;
            }
            else
            {
                Class<?> critClass = Class.forName(getJavaClassName());
                if (java.lang.Number.class.isAssignableFrom(critClass) || java.util.Date.class.isAssignableFrom(critClass))
                {
                    // === either a number or a date value
                    return NUMBER_OPERATORS;
                }
                else
                {
                    if (Boolean.class.equals(critClass))
                    {
                        return BOOLEAN_OPERATORS;
                    }
                    else
                    {
                        return DEFAULT_OPERATORS;
                    }
                }
            }
        }

        public List getComplexChildClasses() throws Exception
        {
            return this.nativeDef.getComplexChildClasses();
        }

        public boolean isComplex()
        {
            return this.nativeDef.isComplex();
        }
    }
}
