package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses.OwOperatorHandler;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses.OwPropertyTypeMapping;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWExposedFieldDefinition;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWModeType;

/**
 *<p>
 * FileNet BPM Repository.<br/> 
 * Standard implementation of the work item property class.
 * Implements a single work item property description, which holds the information
 * about Settability and Modification of dependent work item property.
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
public class OwFNBPM5StandardWorkItemPropertyClass extends OwStandardPropertyClass implements OwFNBPM5WorkItemPropertyClass
{
    /** filter for MODE_TYPE_IN elements, which are used by the application itself*/
    protected static final Set<String> FILTER_OW_ELEMENTS = new TreeSet<String>();
    static
    {
        //see bug 2081
        FILTER_OW_ELEMENTS.add(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME);
        FILTER_OW_ELEMENTS.add(OwFNBPM5WorkItem.STEPPROCESSOR_JSP_PAGE_PROPERTY);
    }

    /** indicates that the property can be filtered */
    private boolean m_fFilterProperty;
    /** indicates weather the field is a pure DateField based in a queue definition = true, or a parameter which is only defined in the workflow object = false */
    private boolean m_fQueueDefinitionField;

    /** init property class description
     * 
     * @param strName_p
     * @param displayName_p
     * @param strJavaClassName_p
     * @param fArray_p
     * @param iMode_p
     * @param fSystem_p
     * @param iFieldType_p
     * @param fQueueDefinitionField_p
     * @param maxValue_p
     * @param combatiblefield_p a compatible field from DMS system to lookup choicelists
     * @throws Exception
     */
    public OwFNBPM5StandardWorkItemPropertyClass(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, int iMode_p, boolean fSystem_p, int iFieldType_p, boolean fQueueDefinitionField_p, Object maxValue_p,
            OwFieldDefinition combatiblefield_p) throws Exception
    {
        init(strName_p, displayName_p, strJavaClassName_p, fArray_p, iMode_p, fSystem_p, iFieldType_p, fQueueDefinitionField_p, maxValue_p, combatiblefield_p);
    }

    /** init property class description 
     * @throws Exception */
    public OwFNBPM5StandardWorkItemPropertyClass(VWExposedFieldDefinition definition_p, OwFieldDefinition combatiblefield_p) throws Exception
    {
        // only exposed fields can be filtered
        m_fFilterProperty = true;
        init(definition_p.getName(), new OwString(definition_p.getName()), getJavaClassName(definition_p.getFieldType()), false, VWModeType.MODE_TYPE_IN, true, definition_p.getFieldType(), true, null, combatiblefield_p);
    }

    /** get a collection of possible filter operators for a given field
     * 
     * @return Collection of operators as defined with OwSearchOperator.CRIT_OP_...
     */
    public Collection getOperators() throws Exception
    {
        if (this.m_operators == null)
        {
            this.m_operators = OwOperatorHandler.getMatchingOperator(getJavaClassName());
        }
        return this.m_operators;
    }

    private void init(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, int iMode_p, boolean fSystem_p, int iFieldType_p, boolean fQueueDefinitionField_p, Object maxValue_p, OwFieldDefinition combatiblefield_p)
            throws Exception
    {
        m_fQueueDefinitionField = fQueueDefinitionField_p;

        // set max value
        this.m_MaxValue = maxValue_p;

        this.m_DMSType = Integer.valueOf(iFieldType_p);
        this.m_fArray = fArray_p;
        this.m_fName = OwFNBPM5WorkItemObjectClass.NAME_PROPERTY.equals(strName_p);

        this.m_fRequired = iFieldType_p == VWFieldType.FIELD_TYPE_BOOLEAN;
        this.m_fSystem = fSystem_p;

        if (null != combatiblefield_p)
        {
            this.m_MaxValue = combatiblefield_p.getMaxValue();
            this.m_MinValue = combatiblefield_p.getMinValue();
            this.m_Enums = combatiblefield_p.getEnums();
        }
        else
        {
            this.m_Enums = null;
        }

        boolean fHidden = true;
        boolean fReadOnly = true;

        switch (iMode_p)
        {
            case VWModeType.MODE_TYPE_IN_OUT:
                fReadOnly = false;
                fHidden = false;
                break;

            case VWModeType.MODE_TYPE_IN:
                fReadOnly = true;
                //bug 2081
                fHidden = FILTER_OW_ELEMENTS.contains(strName_p);
                break;

            case VWModeType.MODE_TYPE_OUT:
                fReadOnly = false;
                fHidden = true;
                break;
            default:
                ;//will be hidden and read-only
        }

        for (int i = 0; i < OwPropertyClass.CONTEXT_MAX; i++)
        {
            this.m_fHidden[i] = fHidden;
            this.m_fReadOnly[i] = fReadOnly;
        }

        this.m_strClassName = strName_p;
        this.m_DisplayName = displayName_p;
        this.m_strJavaClassName = strJavaClassName_p;
    }

    /** indicates if field can be filtered */
    public boolean isFilterField()
    {
        return m_fFilterProperty;
    }

    /** indicates weather the field is a pure DateField based in a queue definition = true,
     or a parameter which is only defined in the workflow object = false 
     */
    public boolean isQueueDefinitionField()
    {
        return m_fQueueDefinitionField;
    }

    public static String getJavaClassName(int iVWFieldType_p) throws OwInvalidOperationException
    {
        return OwPropertyTypeMapping.getJavaClassName(iVWFieldType_p);
    }

}