package com.wewebu.ow.server.field;

import java.text.Format;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 *<p>
 * Search criteria tuple containing property, operator and value.
 * Used to create SQL Statement or a search form entry.<br/>
 * Search are used with OwNetwork.doSearch(...) function.<br/><br/>
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
public class OwSearchCriteria implements OwField, OwFieldDefinition, OwFormat
{
    /** attribute bit flag used with m_iAttributes member: No attributes. */
    public static final int ATTRIBUTE_NONE = 0x00000000;
    /** attribute bit flag used with m_iAttributes member: A value that is hidden. */
    public static final int ATTRIBUTE_HIDDEN = 0x00000001;
    /** attribute bit flag used with m_iAttributes member: A value that can not be changed */
    public static final int ATTRIBUTE_READONLY = 0x00000002;
    /** attribute bit flag used with m_iAttributes member: A required value that must be filled in. */
    public static final int ATTRIBUTE_REQUIRED = 0x00000004;
    /** attribute bit flag used with m_iAttributes member: Allow the use of wildcards also for integer values, i.e. treat integers as string. */
    public static final int ATTRIBUTE_ALLOWWILDCARD = 0x00000008;
    /** attribute bit flag used with m_iAttributes member: Ignore the time part of a date value. */
    public static final int ATTRIBUTE_IGNORE_TIME = 0x00000010;
    /** attribute bit flag used with m_iAttributes member: Ignore the date part of a date value. */
    public static final int ATTRIBUTE_IGNORE_DATE = 0x00000020;

    /** field definition defining this search criteria */
    protected OwFieldDefinition m_FieldDefinition;

    /** criteria operator which should be applied to the value as defined in OwSearchCriteria */
    protected int m_iOp = OwSearchOperator.CRIT_OP_UNDEF;
    /** compare value for the search */
    protected Object m_oValue;

    /** the default value */
    protected Object m_oDefaultValue;

    /** optional second child criteria for range searches */
    protected OwSearchCriteria m_secondrangecriteria;

    /** flag indicating if the criteria can be edited in a search template view or if it is hidden */
    protected int m_iAttributes;

    /** the instruction to the search criteria */
    protected String m_strInstruction;

    /** Collection of OwWildCardDefinition wildcard definition for this criteria, or null if no wildcards are allowed */
    protected Collection m_wildcarddefinitions;

    /** get the wildcard definitions for this criteria
     * 
     * @return Collection of OwWildCardDefinition, or null if no wildcards are allowed
     */
    public Collection getWildCardDefinitions()
    {
        return m_wildcarddefinitions;
    }

    /** set the wildcard definitions for this criteria
     * 
     * @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     */
    public void setWildCardDefinitions(Collection wildcarddefinitions_p)
    {
        m_wildcarddefinitions = wildcarddefinitions_p;
    }

    /** get the instruction to the search criteria 
     */
    public String getInstruction()
    {
        return m_strInstruction;
    }

    /** a unique name that identifies this criteria */
    protected String m_strUniqueName;

    /** get the criteria operator which should be applied to the value as defined in OwSearchCriteria */
    public int getOperator()
    {
        return m_iOp;
    }

    /** set the criteria operator which should be applied to the value as defined in OwSearchCriteria */
    public void setOperator(int iOp_p)
    {
        m_iOp = iOp_p;
    }

    /** convert a operator ID to a displayable name
     * @param locale_p 
     * @return display name for given operator
     */
    public String getOperatorDisplayName(java.util.Locale locale_p)
    {
        return OwSearchOperator.getOperatorDisplayString(locale_p, getOperator());
    }

    /** get a optional second child criteria for range searches */
    public OwSearchCriteria getSecondRangeCriteria()
    {
        if (null == m_secondrangecriteria)
        {
            m_secondrangecriteria = new OwSearchCriteria();

            m_secondrangecriteria.m_iOp = this.m_iOp;
            m_secondrangecriteria.m_oValue = this.m_oValue;
            m_secondrangecriteria.m_strUniqueName = this.m_strUniqueName;
            m_secondrangecriteria.m_iAttributes = this.m_iAttributes;
            m_secondrangecriteria.m_FieldDefinition = this.m_FieldDefinition;
            m_secondrangecriteria.m_strInstruction = this.m_strInstruction;
            m_secondrangecriteria.m_oValue = this.m_oValue;
            m_secondrangecriteria.m_wildcarddefinitions = this.m_wildcarddefinitions;
        }

        return m_secondrangecriteria;
    }

    /** check if criteria has a second range criteria fo ranges
     * 
     * @return boolean
     */
    public boolean isCriteriaOperatorRange()
    {
        return OwSearchOperator.isCriteriaOperatorRange(getOperator());
    }

    /** get the flag indicating if the criteria can be edited in a search template view or if it is hidden */
    public int getAttributes()
    {
        return m_iAttributes;
    }

    /** check if the criteria can be edited in a search template view or if it is hidden */
    public boolean isHidden()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_HIDDEN));
    }

    /** check if the criteria is visible but read-only */
    public boolean isReadonly()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_READONLY));
    }

    /** check if the criteria must be set */
    public boolean isRequired()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_REQUIRED));
    }

    /** check if the criteria ignores the time part */
    public boolean isIgnoreTime()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_IGNORE_TIME));
    }

    /** check if the criteria ignores the date part */
    public boolean isIgnoreDate()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_IGNORE_DATE));
    }

    /** check if the criteria allows wildcard even if underlying type is number */
    public boolean isAllowWildcard()
    {
        return (0 != (m_iAttributes & ATTRIBUTE_ALLOWWILDCARD));
    }

    /** check if criteria allows the use of wildcards
     * */
    public boolean canWildCard()
    {
        return (m_wildcarddefinitions != null);
    }

    /** get the compare value for the search */
    public Object getValue()
    {
        return m_oValue;
    }

    /** set the compare value for the search */
    public void setValue(Object oValue_p)
    {
        m_oValue = oValue_p;
    }

    /** set the initial/default value for the search
     * 
     * @param oValue_p
     */
    public void setInitialAndDefaultValue(Object oValue_p)
    {
        if (isCriteriaOperatorRange())
        {
            // two values
            if (null != oValue_p)
            {
                // first criteria
                m_oValue = ((Object[]) oValue_p)[0];
                // clone default value, it must be independent
                m_oDefaultValue = cloneValue(m_oValue);

                // second criteria
                getSecondRangeCriteria().m_oValue = ((Object[]) oValue_p)[1];
                // clone default value, it must be independent
                getSecondRangeCriteria().m_oDefaultValue = cloneValue(getSecondRangeCriteria().m_oValue);
            }
        }
        else
        {
            // one value
            m_oValue = oValue_p;
            m_oDefaultValue = oValue_p;
        }
    }

    /** create a clone of given value
     * 
     * @param value_p
     * @return an {@link Object}
     */
    private Object cloneValue(Object value_p)
    {
        /////////////////////////////////////////////////////////////////////
        // TODO:
        // right now this is not a big problem, since values are cloned upon update in fieldmanage anyway, 
        // but for a robust architecture it should be implemented.

        return value_p;
    }

    /** get the corresponding field definition of the field
     * @return OwFieldDefinition
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        // definition and field are identical for search criteria
        return this;
    }

    /** get a unique name that identifies this criteria 
     *
     * @return String unique name for the criteria
     */
    public String getUniqueName()
    {
        return m_strUniqueName;
    }

    public String getClassName()
    {
        return m_FieldDefinition.getClassName();
    }

    /** get the default search value
     * 
     * @return Object for single criteria, or Object[2] for range criteria
     */
    public Object getDefaultValue() throws Exception
    {
        return m_oDefaultValue;
    }

    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_FieldDefinition.getDisplayName(locale_p);
    }

    /** get the formatter object for string representation 
     * 
     * @return Format, can be null to use the default format
     */
    public OwFormat getFormat()
    {
        return this;
    }

    public com.wewebu.ow.server.field.OwEnumCollection getEnums() throws Exception
    {
        return m_FieldDefinition.getEnums();
    }

    public String getJavaClassName()
    {
        if (isAllowWildcard())
        {
            return "java.lang.String"; // treat all data types as String, so wildcards can be used
        }
        else
        {
            return m_FieldDefinition.getJavaClassName();
        }
    }

    /** get the original class name, in case data type was overridden by allow wildcard
     * 
     * @return a {@link String}
     */
    public String getOriginalJavaClassName()
    {
        return m_FieldDefinition.getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {
        return m_FieldDefinition.getMaxValue();
    }

    public Object getMinValue() throws Exception
    {
        return m_FieldDefinition.getMinValue();
    }

    public Object getNativeType() throws Exception
    {
        return m_FieldDefinition.getNativeType();
    }

    public org.w3c.dom.Node getNodeFromValue(Object value_p, org.w3c.dom.Document doc_p) throws Exception
    {
        return m_FieldDefinition.getNodeFromValue(value_p, doc_p);
    }

    public Object getValueFromNode(org.w3c.dom.Node node_p) throws Exception
    {
        if (isAllowWildcard())
        {
            // === treat as string
            try
            {
                return node_p.getNodeValue();
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else
        {
            return m_FieldDefinition.getValueFromNode(node_p);
        }
    }

    /** create a value for the field described by this class with the given String serialization
    *
    * @param text_p String the serialized value
    * @return Object the value of the field 
    */
    public Object getValueFromString(String text_p) throws Exception
    {
        if (isAllowWildcard())
        {
            // === treat as string
            return text_p;
        }
        else
        {
            return m_FieldDefinition.getValueFromString(text_p);
        }
    }

    public boolean isArray() throws Exception
    {
        return m_FieldDefinition.isArray();
    }

    public boolean isEnum() throws Exception
    {
        return m_FieldDefinition.isEnum();
    }

    public String getDescription(java.util.Locale locale_p)
    {
        return m_FieldDefinition.getDescription(locale_p);
    }

    public Collection getOperators() throws Exception
    {
        return m_FieldDefinition.getOperators();
    }

    /** check if given java class name is base type
     * 
     * @param base_p a {@link Class}
     * @return boolean
     * @throws ClassNotFoundException 
     */
    public boolean isType(Class base_p) throws ClassNotFoundException
    {
        return (base_p.isAssignableFrom(Class.forName(getJavaClassName())));
    }

    public boolean ignoreTime()
    {
        if (isIgnoreTime())
        {
            return true;
        }
        else if (null != m_FieldDefinition.getFormat())
        {
            return m_FieldDefinition.getFormat().ignoreTime();
        }
        else
        {
            return false;
        }
    }

    /** get the optional formatter object for string representation, according to the given context type 
     * 
     * @param iFieldProviderType_p int as defined in OwFieldProvider.TYPE_...
     * @return OwFormat, can be null to use the default format
     */
    public Format getTextFormat(int iFieldProviderType_p)
    {
        if (null != m_FieldDefinition.getFormat())
        {
            return m_FieldDefinition.getFormat().getTextFormat(iFieldProviderType_p);
        }
        else
        {
            return null;
        }
    }

    /** check if criteria is a date criteria 
    * 
    * @return a boolean
    */
    public boolean isDateType()
    {
        try
        {
            // is date ?
            return java.util.Date.class.isAssignableFrom(Class.forName(getJavaClassName()));
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    public String validate(int fieldProviderType_p, Object object_p, Locale locale_p)
    {
        // no special validation
        return null;
    }

    public boolean canValidate()
    {
        return false;
    }

    public boolean ignoreDate()
    {
        if (isIgnoreDate())
        {
            return true;
        }
        else if (null != m_FieldDefinition.getFormat())
        {
            return m_FieldDefinition.getFormat().ignoreDate();
        }
        else
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinition#isComplex()
     */
    public boolean isComplex()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinition#getComplexChildClasses()
     */
    public List getComplexChildClasses() throws Exception
    {
        // not complex, so return null
        return null;
    }

    @Override
    public String toString()
    {
        StringBuilder build = new StringBuilder("OwSearchCriteria[unique-name: ");
        build.append(getUniqueName());
        build.append(", fieldName: ");
        build.append(getClassName());
        build.append(", value: ");
        build.append(getValue());
        build.append(", default-value: ");
        build.append(m_oDefaultValue);
        build.append(", field-def: ");
        build.append(m_FieldDefinition);
        build.append("]");
        return build.toString();
    }
}