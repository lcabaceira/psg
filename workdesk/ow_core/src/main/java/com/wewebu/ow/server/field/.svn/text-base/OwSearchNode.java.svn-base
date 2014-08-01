package com.wewebu.ow.server.field;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.fieldimpl.OwSearchNodeFilter;

/**
 *<p>
 * Class for searches<br/>
 * Searches are trees of OwSearchNode Objects.<br/>
 * Each node can contain either a list of child OwSearchNode Objects of a search criteria element.
 * The nodes are combined with SEARCH_OP_... operators "and" and "or".
 * The Criteria is by the {@link OwSearchCriteria}.<br/>
 * Search are used with {@link OwNetwork#doSearch} Function.<br/><br/>
 * To be implemented with the specific DMS system.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwSearchNode
{
    /** 
     * A search iterator which lists the criteria ONLY
     * @deprecated since 4.0.0.0
     */
    @Deprecated
    public class OwSearchList extends LinkedList
    {
        private static final long serialVersionUID = 1L;
        /** filter */
        private int m_iFilter;

        private boolean hasFilter(int iFlag_p)
        {
            return (m_iFilter & iFlag_p) != 0;
        }

        /** constructs a list form the given OwSearchNode tree
         * 
         * @param iFilter_p int any combination of OwSearchNode.FILTER_...
         * @param search_p the OwSearchNode to iterate
         */
        public OwSearchList(OwSearchNode search_p, int iFilter_p)
        {
            m_iFilter = iFilter_p;
            traverseSearchTree(search_p);
        }

        /** recursively traverse the OwSearchNode tree
         * @param search_p current OwSearchNode object node
         */
        private void traverseSearchTree(OwSearchNode search_p)
        {
            // === add criteria if available 
            OwSearchCriteria criteria = search_p.getCriteria();
            if (criteria != null)
            {
                if (!(hasFilter(FILTER_HIDDEN) && criteria.isHidden()))
                {
                    super.add(criteria);
                }
            }

            // === iterate over the children if available
            if ((search_p.getNodeType() == OwSearchNode.NODE_TYPE_COMBINATION) || (search_p.getNodeType() == OwSearchNode.NODE_TYPE_PROPERTY) || (!hasFilter(FILTER_NONPROPERTY)))
            {
                List Childs = search_p.getChilds();
                if (Childs != null)
                {
                    for (int i = 0; i < Childs.size(); i++)
                    {
                        traverseSearchTree((OwSearchNode) Childs.get(i));
                    }
                }
            }
        }
    }

    /**
     * A search map which maps the criteria among their unique name
     * @deprecated since 4.0.0.0 don't use this class anymore
     */
    @Deprecated
    public class OwSearchMap extends HashMap
    {
        private static final long serialVersionUID = 1L;

        private int m_iFilter;

        private boolean hasFilter(int iFlag_p)
        {
            return (m_iFilter & iFlag_p) != 0;
        }

        /** constructs a list form the given OwSearchNode tree
         * @param iFilter_p int any combination of OwSearchNode.FILTER_...
         * @param search_p the OwSearchNode to iterate
         */
        public OwSearchMap(OwSearchNode search_p, int iFilter_p)
        {
            m_iFilter = iFilter_p;
            traverseSearchTree(search_p);
        }

        /** recursively traverse the OwSearchNode tree
         * @param search_p current OwSearchNode object node
         */
        private void traverseSearchTree(OwSearchNode search_p)
        {
            // === add criteria if available 
            OwSearchCriteria Criteria = search_p.getCriteria();
            if (Criteria != null)
            {
                if (!(hasFilter(FILTER_HIDDEN) && Criteria.isHidden()))
                {
                    super.put(Criteria.getUniqueName(), Criteria);
                }
            }

            // === iterate over the children if available
            if ((search_p.getNodeType() == OwSearchNode.NODE_TYPE_CRITERIA) || (search_p.getNodeType() == OwSearchNode.NODE_TYPE_PROPERTY) || (!hasFilter(FILTER_NONPROPERTY)))
            {
                List Childs = search_p.getChilds();
                if (Childs != null)
                {
                    for (int i = 0; i < Childs.size(); i++)
                    {
                        traverseSearchTree((OwSearchNode) Childs.get(i));
                    }
                }
            }
        }
    }

    /**
     *<p>
     * Field provider interface for searches.
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
    public static class OwSearchNodeFieldProvider implements OwFieldProvider
    {
        /** map for fast access to the search fields */
        private Map m_FieldMap;

        /** create a field provider for the given search
         * @param search_p OwSearchNode
         */
        public OwSearchNodeFieldProvider(OwSearchNode search_p)
        {
            // === create map with fields from given search
            // iterate all fields from the search tree
            Iterator it = search_p.getCriteriaList(OwSearchNode.FILTER_HIDDEN).iterator();

            m_FieldMap = new HashMap();

            while (it.hasNext())
            {
                OwSearchCriteria criteria = (OwSearchCriteria) it.next();
                m_FieldMap.put(criteria.getUniqueName(), criteria);
            }
        }

        /** get the source object that originally provided the fields.
         * e.g. the fieldprovider might be a template pattern implementation like a view,
         *      where the original provider would still be an OwObject
         *      
         * @return Object the original source object where the fields have been taken, can be a this pointer
         * */
        public Object getFieldProviderSource()
        {
            return this;
        }

        /** get a name that identifies the field provider, can be used to create IDs 
         * 
         * @return String unique ID / Name of fieldprovider
         */
        public String getFieldProviderName()
        {
            return "OwSearchNode";
        }

        /** get a field with the given field definition class name
         *
         * @param strFieldClassName_p String class name of requested fields
         *
         * @return OwField or throws OwObjectNotFoundException
         */
        public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
        {
            OwField field = (OwField) m_FieldMap.get(strFieldClassName_p);
            if (field == null)
            {
                throw new OwObjectNotFoundException("OwSearchNode$OwSearchNodeFieldProvider.getField: Property not found, strFieldClassName_p = " + strFieldClassName_p);
            }

            return field;
        }

        /** get the type of field provider as defined with TYPE_... */
        public int getFieldProviderType()
        {
            return TYPE_SEARCH;
        }

        /** modify a Field value, but does not save the value right away
         * 
         * @param sName_p
         * @param value_p
         * @throws Exception
         * @throws OwObjectNotFoundException
         */
        public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
        {
            getField(sName_p).setValue(value_p);
        }

        /** retrieve the value of a Field
         * 
         * @param sName_p
         * @param defaultvalue_p
         * @return Object the value of the Field of defaultvalue_p
         */
        public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
        {
            try
            {
                OwField field = getField(sName_p);
                return field.getValue();
            }
            catch (Exception e)
            {
                return defaultvalue_p;
            }
        }

        /** get all the properties in the form
         * 
         * @return Collection of OwField
         * @throws Exception
         */
        public Collection getFields() throws Exception
        {
            return m_FieldMap.values();
        }
    }

    // === filter types for criteria traversing
    /** filter type for criteria traversing used in getCriteriaList */
    public static final int FILTER_NONE = 0x0000;
    /** filter type for criteria traversing used in getCriteriaList */
    public static final int FILTER_HIDDEN = 0x0001;
    /** filter type for criteria traversing used in getCriteriaList */
    public static final int FILTER_NONPROPERTY = 0x0002;
    /** filter type for criteria traversing used in getCriteriaList 
     * @since 4.0.0.0*/
    public static final int FILTER_READONLY = 0x0004;

    /** node type searches for properties like the metadata of documents */
    public static final int NODE_TYPE_PROPERTY = 1;
    /** node type searches for special properties like resource path or class criteria */
    public static final int NODE_TYPE_SPECIAL = 2;
    /** node type searches for content based criteria */
    public static final int NODE_TYPE_CBR = 3;
    /** a criteria node */
    public static final int NODE_TYPE_CRITERIA = 4;
    /** node, which combines other nodes */
    public static final int NODE_TYPE_COMBINATION = 5;

    /** the type of the branch can be one of the NODE_TYPE_... definitions
     *  used to distinguish the search branches and make search creation easier*/
    protected int m_iNodeType = 0;

    /** combination operator for the OwSearchNode child nodes undefined */
    public static final int SEARCH_OP_UNDEF = 0;
    /** combination operator for the OwSearchNode child nodes of this OwSearchNode Node */
    public static final int SEARCH_OP_AND = 1;
    /** combination operator for the OwSearchNode child nodes of this OwSearchNode Node */
    public static final int SEARCH_OP_OR = 2;

    /** combination operator which should be applied to the child OwSearchNode elements as defined in OwSearchNode */
    protected int m_iOp = SEARCH_OP_UNDEF;

    /** criteria instance for this search node, null if OwSearchNode has children */
    protected OwSearchCriteria m_Criteria;

    /** the children of this search node which are combined using the m_iOp Operator */
    protected LinkedList m_Childs;

    /** dump the search tree */
    public void dump(Writer w_p) throws Exception
    {
        // dump info
        w_p.write("[Combination]        node that may contain any other node<br>");
        w_p.write("[Property]           node that contains only property criteria<br>");
        w_p.write("[CBR]                node that contains only content based library criteria<br>");
        w_p.write("[Special]            node that contains only special criteria<br>");
        w_p.write("[Criteria]           criteria node with no further nodes<br>");

        // dump tree
        dump(w_p, this, 0, true);
    }

    /** dump a line block-quote with the given level */
    private static void dumpLevelLine(Writer w_p, int iLevel_p, String strText_p, boolean fHtml_p) throws Exception
    {
        if (fHtml_p)
        {
            w_p.write("<br>");
        }
        else
        {
            w_p.write("\r\n");
        }

        for (int i = 0; i < iLevel_p; i++)
        {
            if (fHtml_p)
            {
                w_p.write("&nbsp;&nbsp;&nbsp;&nbsp;");
            }
            else
            {
                w_p.write("  ");
            }
        }

        w_p.write(strText_p);
    }

    /** get a string representation of the operator type for dump 
     *
     * @param locale_p Locale to use
     */
    private static String getOperatorDisplayName(java.util.Locale locale_p, int iOP_p)
    {
        switch (iOP_p)
        {

            case SEARCH_OP_AND:
                return "AND";
            case SEARCH_OP_OR:
                return "OR";
            default:
                return "undef";
        }
    }

    /** get a string representation fo the node type for dump */
    private static String getDumpNodeTypeString(int iNodeType_p)
    {
        switch (iNodeType_p)
        {
            case NODE_TYPE_PROPERTY:
                return "Property";
            case NODE_TYPE_SPECIAL:
                return "Special";
            case NODE_TYPE_CBR:
                return "CBR";
            case NODE_TYPE_CRITERIA:
                return "Criteria";
            case NODE_TYPE_COMBINATION:
                return "Combination";
            default:
                return "undef";
        }
    }

    /** dump java class name as string */
    private static String dumpJavaClassName(String strJCalss_p)
    {
        return strJCalss_p.substring(strJCalss_p.lastIndexOf('.') + 1);
    }

    /** dump attributes as string */
    private static String dumpAttributes(int iAttributes_p)
    {
        String strRet = "";
        if (0 != (iAttributes_p & OwSearchCriteria.ATTRIBUTE_HIDDEN))
        {
            strRet += "ATTRIBUTE_HIDDEN ";
        }

        if (0 != (iAttributes_p & OwSearchCriteria.ATTRIBUTE_READONLY))
        {
            strRet += "ATTRIBUTE_READONLY ";
        }

        if (0 != (iAttributes_p & OwSearchCriteria.ATTRIBUTE_REQUIRED))
        {
            strRet += "ATTRIBUTE_REQUIRED ";
        }

        if (0 != (iAttributes_p & OwSearchCriteria.ATTRIBUTE_ALLOWWILDCARD))
        {
            strRet += "ATTRIBUTE_ALLOWWILDCARD ";
        }

        return strRet;
    }

    /** dump the search tree
     * 
     * @param w_p
     * @param search_p
     * @param iLevel_p
     * @param fHtml_p true = HTML dump, false = text dump
     * @throws Exception
     */
    private static void dump(Writer w_p, OwSearchNode search_p, int iLevel_p, boolean fHtml_p) throws Exception
    {
        if (search_p.isCriteriaNode())
        {
            // === dump criteria node
            OwSearchCriteria crit = search_p.getCriteria();
            StringBuffer buff = new StringBuffer("[");
            buff.append(getDumpNodeTypeString(search_p.getNodeType()));
            buff.append(": ");
            buff.append(crit.getDisplayName(Locale.getDefault()));
            buff.append("] - ");
            buff.append(crit.getDisplayName(Locale.getDefault()));
            buff.append(": <b>\"");

            try
            {
                if (crit.isArray())
                {
                    boolean fDelimiter = false;
                    Object[] arrVal = (Object[]) crit.getValue();
                    for (int i = 0; i < arrVal.length; i++)
                    {
                        if (fDelimiter)
                        {
                            buff.append(", ");
                        }

                        if (arrVal[i] == null)
                        {
                            buff.append("<null>");
                        }
                        else
                        {
                            buff.append(arrVal[i].toString());
                        }

                        fDelimiter = true;
                    }
                }
                else
                {
                    if (crit.getValue() == null)
                    {
                        buff.append("<null>");
                    }
                    else
                    {
                        buff.append(crit.getValue().toString());
                    }
                }
            }
            catch (NullPointerException e)
            {
                // ignore
            }
            buff.append("\"</b> CLASS: ");
            buff.append(search_p.getCriteria().getClassName());
            buff.append(" J-CLASS: ");
            buff.append(dumpJavaClassName(search_p.getCriteria().getJavaClassName()));
            buff.append(" UNIQUE: ");
            buff.append(crit.getUniqueName());
            buff.append(" ATTR: ");
            buff.append(dumpAttributes(search_p.getCriteria().getAttributes()));

            //            dumpLevelLine(w_p, iLevel_p, "[" + getDumpNodeTypeString(search_p.getNodeType()) + ": " + crit.getDisplayName(Locale.getDefault()) + "] - " + crit.getOperatorDisplayName(Locale.getDefault()) + ": <b>\"" + value + "\"</b> CLASS: "
            //                    + search_p.getCriteria().getClassName() + " J-CLASS: " + dumpJavaClassName(search_p.getCriteria().getJavaClassName()) + " UNIQUE: " + search_p.getCriteria().getUniqueName() + " ATTR: "
            //                    + dumpAttributes(search_p.getCriteria().getAttributes()), fHtml_p);
            dumpLevelLine(w_p, iLevel_p, buff.toString(), fHtml_p);
        }
        else
        {
            // === dump tree node
            dumpLevelLine(w_p, iLevel_p, "[" + getDumpNodeTypeString(search_p.getNodeType()) + "] - " + getOperatorDisplayName(Locale.getDefault(), search_p.getOperator()), fHtml_p);

            // recurse
            Iterator it = search_p.getChilds().iterator();
            while (it.hasNext())
            {
                dump(w_p, (OwSearchNode) it.next(), iLevel_p + 1, fHtml_p);
            }
        }
    }

    /** criteria constructor
     * 
     * @param criteria_p
     */
    public OwSearchNode(OwSearchCriteria criteria_p)
    {
        m_Criteria = criteria_p;
        m_iNodeType = NODE_TYPE_CRITERIA;
    }

    /** create a search node with a criteria
     *
     *  @param fieldDefinition_p OwFieldDefinition of criteria
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param oInitialAndDefaultValue_p initial and default value, for range operators it is a two field array, first field for first range criteria, second field for second range criteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     *  @param strUniqueName_p String a unique name that identifies this criteria 
     *  @param strInstruction_p String instruction to be displayed, can be null
     *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     */
    public OwSearchNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p)
    {
        // === create criteria node
        m_Criteria = new OwSearchCriteria();

        m_Criteria.m_iOp = iOp_p;
        m_Criteria.m_strUniqueName = strUniqueName_p;
        m_Criteria.m_iAttributes = iAttributes_p;
        m_Criteria.m_FieldDefinition = fieldDefinition_p;
        m_Criteria.m_strInstruction = strInstruction_p;
        m_Criteria.m_wildcarddefinitions = wildcarddefinitions_p;
        m_iNodeType = NODE_TYPE_CRITERIA;

        m_Criteria.setInitialAndDefaultValue(oInitialAndDefaultValue_p);
    }

    /** decorator pattern to and override a search node with a criteria
     *
     *  @param fieldDefinition_p OwFieldDefinition
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param oInitialAndDefaultValue_p initial and default value, for range operators it is a two field array, first field for first range criteria, second field for second range criteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     *  @param strUniqueName_p String a unique name that identifies this criteria 
     *  @param strInstruction_p String instruction to be displayed, can be null
     *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     *  
     *  @param minValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param maxValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param defaultValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param sJavaClassName_p String value to override the inherited FieldDefinition with custom values
     */
    public OwSearchNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p, Object minValue_p, Object maxValue_p,
            Object defaultValue_p, String sJavaClassName_p) throws Exception
    {
        // === create criteria node
        m_Criteria = new OwSearchCriteriaFieldDefOverride();

        ((OwSearchCriteriaFieldDefOverride) m_Criteria).setMinValue(minValue_p);
        ((OwSearchCriteriaFieldDefOverride) m_Criteria).setMaxValue(maxValue_p);
        ((OwSearchCriteriaFieldDefOverride) m_Criteria).setDefaultValue(defaultValue_p);
        ((OwSearchCriteriaFieldDefOverride) m_Criteria).setJavaClassName(sJavaClassName_p);

        m_Criteria.m_iOp = iOp_p;
        m_Criteria.m_strUniqueName = strUniqueName_p;
        m_Criteria.m_iAttributes = iAttributes_p;
        m_Criteria.m_FieldDefinition = fieldDefinition_p;
        m_Criteria.m_strInstruction = strInstruction_p;
        m_Criteria.m_wildcarddefinitions = wildcarddefinitions_p;
        m_iNodeType = NODE_TYPE_CRITERIA;

        m_Criteria.setInitialAndDefaultValue(oInitialAndDefaultValue_p);
    }

    /** create a search node with a criteria
     *
     *  @param field_p OwField criteria as field
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     *  @param strUniqueName_p String a unique name that identifies this criteria 
     *  @param strInstruction_p String instruction to be displayed, can be null
     *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     */
    public OwSearchNode(OwField field_p, int iOp_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p) throws Exception
    {
        // === create criteria node
        m_Criteria = new OwSearchCriteria();

        m_Criteria.m_iOp = iOp_p;
        m_Criteria.m_oValue = field_p.getValue();
        m_Criteria.m_strUniqueName = strUniqueName_p;
        m_Criteria.m_iAttributes = iAttributes_p;
        m_Criteria.m_FieldDefinition = field_p.getFieldDefinition();
        m_Criteria.m_strInstruction = strInstruction_p;
        m_Criteria.m_wildcarddefinitions = wildcarddefinitions_p;
        m_iNodeType = NODE_TYPE_CRITERIA;
    }

    /** create a search node with a criteria
     *
     *  @param fieldDefinition_p OwFieldDefinition of criteria
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param oInitialAndDefaultValue_p initial and default value, for range operators it is a two field array, first field for first range criteria, second field for second range criteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     */
    public OwSearchNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p)
    {
        // === create criteria node
        m_Criteria = new OwSearchCriteria();

        m_Criteria.m_iOp = iOp_p;
        m_Criteria.m_strUniqueName = fieldDefinition_p.getClassName();
        m_Criteria.m_iAttributes = iAttributes_p;
        m_Criteria.m_FieldDefinition = fieldDefinition_p;
        m_Criteria.m_strInstruction = null;
        m_iNodeType = NODE_TYPE_CRITERIA;

        m_Criteria.setInitialAndDefaultValue(oInitialAndDefaultValue_p);
    }

    /** create a search node with a criteria
     *
     *  @param field_p OwField criteria as field
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     */
    public OwSearchNode(OwField field_p, int iOp_p, int iAttributes_p) throws Exception
    {
        // === create criteria node
        m_Criteria = new OwSearchCriteria();

        m_Criteria.m_iOp = iOp_p;
        m_Criteria.m_oValue = field_p.getValue();
        m_Criteria.m_strUniqueName = field_p.getFieldDefinition().getClassName();
        m_Criteria.m_iAttributes = iAttributes_p;
        m_Criteria.m_FieldDefinition = field_p.getFieldDefinition();
        m_Criteria.m_strInstruction = null;
        m_iNodeType = NODE_TYPE_CRITERIA;
    }

    /** create combination search with children but no criteria
     * @param iOp_p combination operator which should be applied to the child OwSearchNode elements as defined in OwSearchNode
     * @param iNodeType_p the type of the branch can be one of the NODE_TYPE_... definition
     */
    public OwSearchNode(int iOp_p, int iNodeType_p)
    {
        m_Childs = new LinkedList();
        m_iOp = iOp_p;
        m_iNodeType = iNodeType_p;
    }

    /** clone the search tree
     * @return OwSearchNode copy
     */
    public Object clone() throws CloneNotSupportedException
    {
        try
        {
            return new OwSearchNode(this);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** create a copy with child tree
    */
    public OwSearchNode(OwSearchNode search_p) throws Exception
    {
        // === create copy
        m_iOp = search_p.getOperator();
        m_iNodeType = search_p.getNodeType();

        if (search_p.isCriteriaNode())
        {
            OwSearchCriteriaFieldDefOverride overrideCriteria = null;
            try
            {
                overrideCriteria = (OwSearchCriteriaFieldDefOverride) search_p.m_Criteria;
            }
            catch (ClassCastException cce)
            {
                // not a override criteria
                overrideCriteria = null;
            }
            if (overrideCriteria == null)
            {
                m_Criteria = new OwSearchCriteria();
                m_Criteria.m_iOp = search_p.m_Criteria.m_iOp;
                m_Criteria.m_strUniqueName = search_p.m_Criteria.m_strUniqueName;
                m_Criteria.m_iAttributes = search_p.m_Criteria.m_iAttributes;
                m_Criteria.m_FieldDefinition = search_p.m_Criteria.m_FieldDefinition;
                m_Criteria.m_strInstruction = search_p.m_Criteria.m_strInstruction;
                m_Criteria.m_wildcarddefinitions = search_p.m_Criteria.m_wildcarddefinitions;
                m_Criteria.m_oValue = search_p.m_Criteria.m_oValue;
                m_Criteria.m_oDefaultValue = search_p.m_Criteria.m_oDefaultValue;
                if (search_p.m_Criteria.m_secondrangecriteria != null)
                {
                    m_Criteria.getSecondRangeCriteria().m_oValue = search_p.m_Criteria.m_secondrangecriteria.m_oValue;
                    m_Criteria.getSecondRangeCriteria().m_oDefaultValue = search_p.m_Criteria.m_secondrangecriteria.m_oDefaultValue;
                }
            }
            else
            {
                m_Criteria = new OwSearchCriteriaFieldDefOverride();
                ((OwSearchCriteriaFieldDefOverride) m_Criteria).setMinValue(overrideCriteria.getMinValue());
                ((OwSearchCriteriaFieldDefOverride) m_Criteria).setMaxValue(overrideCriteria.getMaxValue());
                ((OwSearchCriteriaFieldDefOverride) m_Criteria).setDefaultValue(overrideCriteria.getDefaultValue());
                ((OwSearchCriteriaFieldDefOverride) m_Criteria).setJavaClassName(overrideCriteria.getJavaClassName());
                m_Criteria.m_iOp = overrideCriteria.m_iOp;
                m_Criteria.m_strUniqueName = overrideCriteria.m_strUniqueName;
                m_Criteria.m_iAttributes = overrideCriteria.m_iAttributes;
                m_Criteria.m_FieldDefinition = overrideCriteria.m_FieldDefinition;
                m_Criteria.m_strInstruction = overrideCriteria.m_strInstruction;
                m_Criteria.m_wildcarddefinitions = overrideCriteria.m_wildcarddefinitions;
                m_Criteria.m_oValue = overrideCriteria.m_oValue;
                m_Criteria.m_oDefaultValue = overrideCriteria.m_oDefaultValue;
                if (overrideCriteria.m_secondrangecriteria != null)
                {
                    m_Criteria.getSecondRangeCriteria().m_oValue = overrideCriteria.m_secondrangecriteria.m_oValue;
                    m_Criteria.getSecondRangeCriteria().m_oDefaultValue = overrideCriteria.m_secondrangecriteria.m_oDefaultValue;
                }
            }
        }
        else
        {
            m_Childs = new LinkedList();

            // recursive call
            Iterator it = search_p.getChilds().iterator();
            while (it.hasNext())
            {
                add(new OwSearchNode((OwSearchNode) it.next()));
            }
        }
    }

    /** get the criteria operator which should be applied to the subnodes as defined in OwSearchCriteria */
    public int getOperator()
    {
        return m_iOp;
    }

    /** get a list which contains all the criteria in the OwSearchNode Tree
     * @param iFilter_p int any combination of OwSearchNode.FILTER_...
     * @return List which contains all the criteria in the OwSearchNode Tree
     */
    public List getCriteriaList(int iFilter_p)
    {
        OwSearchNodeFilter filter = new OwSearchNodeFilter(iFilter_p);
        return filter.getCriteriaList(this);
    }

    /** get a map which maps all the criteria in the OwSearchNode Tree to their unique name
     * 
     * @param iFilter_p int any combination of OwSearchNode.FILTER_...
     * @return Map which contains all the criteria in the OwSearchNode Tree
     */
    public Map getCriteriaMap(int iFilter_p)
    {
        OwSearchNodeFilter filter = new OwSearchNodeFilter(iFilter_p);
        return filter.getCriteriaMap(this);
    }

    /** clear all criteria and set to default values
     */
    public void reset() throws Exception
    {
        Iterator it = getCriteriaList(FILTER_HIDDEN).iterator();
        while (it.hasNext())
        {
            OwSearchCriteria criteria = (OwSearchCriteria) it.next();

            if (criteria.isCriteriaOperatorRange())
            {
                // two values
                criteria.setValue(criteria.getDefaultValue());
                criteria.getSecondRangeCriteria().setValue(criteria.getSecondRangeCriteria().getDefaultValue());
            }
            else
            {
                // single value
                criteria.setValue(criteria.getDefaultValue());
            }
        }
    }

    /** get the criteria of this node
     * @return OwSearchCriteria or null if node contains children
     */
    public OwSearchCriteria getCriteria()
    {
        return m_Criteria;
    }

    /** get children of this search node which are combined using the m_iOp Operator
     * @return List of OwSearchNode Objects, or null if node contains a criteria
     *
     */
    public List getChilds()
    {
        return m_Childs;
    }

    /** get the type of the branch can be one of the NODE_TYPE_... definitions
     *  used to distinguish the search branches and make search creation easier
     *
     * @return int 
     */
    public int getNodeType()
    {
        return m_iNodeType;
    }

    /** check if any criteria are set 
     * @return true = no criteria are set, false = at least one criteria is set
     */
    public boolean isEmpty()
    {
        Iterator it = getCriteriaList(FILTER_NONE).iterator();
        while (it.hasNext())
        {
            OwSearchCriteria criteria = (OwSearchCriteria) it.next();

            if ((criteria.getValue() != null) && (criteria.getValue().toString().length() != 0))
            {
                return false;
            }
        }

        return true;
    }

    /** check if search node is a criteria node
     * @return true = criteria node, false = branch containing other OwSearchNode nodes.
     */
    public boolean isCriteriaNode()
    {
        return (m_iNodeType == NODE_TYPE_CRITERIA);
    }

    /** add a search node. This node must not contain a criteria
     * @param search_p the search to add
     */
    public void add(OwSearchNode search_p) throws Exception
    {
        if (getCriteria() != null)
        {
            throw new Exception("Can not add a search to a criteria search node.");
        }

        m_Childs.add(search_p);
    }

    /** default constructor for overridden classes
     * 
     */
    protected OwSearchNode()
    {

    }

    /** set a XML node that persists the current state of the criteria 
     * @param persistentNode_p org.w3c.dom.Node XML node
     */
    public void setPersistentNode(org.w3c.dom.Node persistentNode_p) throws Exception
    {
        List criteriaList = getCriteriaList(FILTER_HIDDEN);

        // org.w3c.dom.NodeList childNodes = persistentNode_p.getChildNodes();

        // === read nodes for each criteria
        // criteria length must match node length

        int i = 0;
        for (Node n = persistentNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (i >= criteriaList.size())
            {
                throw new OwInvalidOperationException("OwSearchNode.setPersistentNode: Die persistenten Suchkriterien passen nicht zum Suchobjekt.");
            }
            OwSearchCriteria criteria = (OwSearchCriteria) criteriaList.get(i);

            if (n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
            {
                // get operator attribute
                int iOperator = Integer.parseInt(n.getAttributes().getNamedItem(PERSIST_OPERATOR_ATTR_NAME).getNodeValue());
                criteria.setOperator(iOperator);

                // === set the persistent criteria value
                if (criteria.isCriteriaOperatorRange())
                {
                    // === two values
                    for (Node subvalue = n.getFirstChild(); subvalue != null; subvalue = subvalue.getNextSibling())
                    {
                        if (subvalue.getNodeName().equals(PERSIST_START_RANGE_NAME))
                        {
                            criteria.setValue(criteria.getValueFromNode(subvalue.getFirstChild()));
                        }

                        if (subvalue.getNodeName().equals(PERSIST_END_RANGE_NAME))
                        {
                            criteria.getSecondRangeCriteria().setValue(criteria.getSecondRangeCriteria().getValueFromNode(subvalue.getFirstChild()));
                        }
                    }
                }
                else
                {
                    // === one value
                    criteria.setValue(criteria.getValueFromNode(n.getFirstChild()));
                }
            }
            // advance position in criteria list
            i++;
        }
        if (i < criteriaList.size())
        {
            throw new OwInvalidOperationException("OwSearchNode.setPersistentNode: Die persistenten Suchkriterien passen nicht zum Suchobjekt.");
        }
    }

    /** DOM name for persistence see get/setPersistentNode() */
    protected static final String PERSIST_SEARCH_NODE_NAME = "SN";

    /** DOM name for persistence see get/setPersistentNode() */
    protected static final String PERSIST_UNIQUE_NAME__ATTR_NAME = "un";

    /** DOM name for persistence see get/setPersistentNode() */
    protected static final String PERSIST_OPERATOR_ATTR_NAME = "op";

    /** DOM name for persistence see get/setPersistentNode() */
    protected static final String PERSIST_START_RANGE_NAME = "v1";
    /** DOM name for persistence see get/setPersistentNode() */
    protected static final String PERSIST_END_RANGE_NAME = "v2";

    /** get a XML node that persists the current state of the criteria 
     * and that can be used with setPersistentNode() to recreate the state
     */
    public org.w3c.dom.Node getPersistentNode(org.w3c.dom.Document doc_p) throws Exception
    {
        // === create a node for each criteria
        org.w3c.dom.Element retNode = doc_p.createElement(PERSIST_SEARCH_NODE_NAME);

        List criteriaList = getCriteriaList(FILTER_HIDDEN);

        for (int i = 0; i < criteriaList.size(); i++)
        {
            OwSearchCriteria criteria = (OwSearchCriteria) criteriaList.get(i);

            // === criteria node with class name
            org.w3c.dom.Element criteriaNode = doc_p.createElement(criteria.getClassName());
            // add uniquename attribute
            criteriaNode.setAttribute(PERSIST_UNIQUE_NAME__ATTR_NAME, criteria.getUniqueName());
            // add operator attribute
            criteriaNode.setAttribute(PERSIST_OPERATOR_ATTR_NAME, String.valueOf(criteria.getOperator()));

            // === value node
            org.w3c.dom.Node valueNode = criteria.getNodeFromValue(criteria.getValue(), doc_p);

            if (criteria.isCriteriaOperatorRange())
            {
                // === two values
                org.w3c.dom.Node valueNode2 = criteria.getSecondRangeCriteria().getNodeFromValue(criteria.getSecondRangeCriteria().getValue(), doc_p);

                org.w3c.dom.Element subnode1 = doc_p.createElement(PERSIST_START_RANGE_NAME);
                org.w3c.dom.Element subnode2 = doc_p.createElement(PERSIST_END_RANGE_NAME);

                if (null != valueNode)
                {
                    subnode1.appendChild(valueNode);
                }

                if (null != valueNode2)
                {
                    subnode2.appendChild(valueNode2);
                }

                criteriaNode.appendChild(subnode1);
                criteriaNode.appendChild(subnode2);
            }
            else
            {
                // === one value
                if (null != valueNode)
                {
                    criteriaNode.appendChild(valueNode);
                }
            }

            // append to return node
            retNode.appendChild(criteriaNode);
        }

        return retNode;
    }

    /** get a field provider interface for the search
     *
     * @return OwFieldProvider
     */
    public OwFieldProvider getFieldProvider()
    {
        return new OwSearchNodeFieldProvider(this);
    }

    /** find the first occurrence of a search node
     * @return OwSearchNode or null if not found
     */
    public OwSearchNode findSearchNode(int iNodeType_p)
    {
        return findSearchNodeInternal(this, iNodeType_p);
    }

    /** find the first occurrence of a search node
     * @return OwSearchNode or null if not found
     */
    private OwSearchNode findSearchNodeInternal(OwSearchNode searchNode_p, int iNodeType_p)
    {
        if (searchNode_p.getNodeType() == iNodeType_p)
        {
            return searchNode_p;
        }

        if (searchNode_p.isCriteriaNode())
        {
            return null;
        }

        Iterator it = searchNode_p.getChilds().iterator();
        while (it.hasNext())
        {
            OwSearchNode search = findSearchNodeInternal((OwSearchNode) it.next(), iNodeType_p);
            if (null != search)
            {
                return search;
            }
        }

        return null;
    }

    /** (overridable) check if search tree is valid, can be used to perform a search,
     * or if required fields are missing, or if no criteria where submitted.
     * @return true if valid
     * @throws Exception 
     */
    public boolean isValid() throws Exception
    {
        boolean fOneIsSet = false;

        // check if first criteria has been set in case of a virtual folder
        // at least one criteria must have been set

        Collection criterialist = getCriteriaList(OwSearchNode.FILTER_NONPROPERTY | OwSearchNode.FILTER_HIDDEN);

        if (criterialist.size() == 0)
        {
            return true;
        }

        Iterator it = criterialist.iterator();
        while (it.hasNext())
        {
            OwSearchCriteria crit = (OwSearchCriteria) it.next();

            // check if value is set
            if ((null != crit.getValue()) && (crit.getValue().toString().length() != 0))
            {
                // check for a default value
                if (crit.isCriteriaOperatorRange())
                {
                    if (null == crit.getDefaultValue())
                    {
                        fOneIsSet = true;
                    }
                }
                else
                {
                    if ((null == crit.getDefaultValue()) || (crit.getDefaultValue().toString().length() == 0) || (!crit.getDefaultValue().equals(crit.getValue())))
                    {
                        fOneIsSet = true;
                    }
                }
            }
            else
            {
                // now check that all required criteria are set
                if (crit.isRequired())
                {
                    return false;
                }
            }
        }

        return fOneIsSet;
    }

    public String toString()
    {
        StringWriter writer = new StringWriter();

        try
        {
            dump(writer, this, 0, false);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }

        return writer.toString();
    }
}