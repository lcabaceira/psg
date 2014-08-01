package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Vector;

import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWExposedFieldDefinition;

/**
 *<p>
 * FileNet BPM Repository. <br/>
 * Implementation of the user ID work item property class, like F_BoundUser. <br/>
 * The user IDs need to be converted from string to ID and vice versa.
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
public class OwFNBPM5UserWorkItemPropertyClass extends OwFNBPM5StandardWorkItemPropertyClass
{

    public OwFNBPM5UserWorkItemPropertyClass(VWExposedFieldDefinition definition_p, OwFieldDefinition combatiblefield_p) throws Exception
    {
        super(definition_p, combatiblefield_p);

        // force string representation, because we convert the P8 BPM IDs to names
        m_strJavaClassName = "java.lang.String";
    }

    public OwFNBPM5UserWorkItemPropertyClass(String strName_p, OwString displayName_p, boolean fArray_p, int iMode_p, boolean fSystem_p, int iFieldType_p, boolean fQueueDefinitionField_p, Object maxValue_p, OwFieldDefinition combatiblefield_p)
            throws Exception
    {
        // force string representation, because we convert the P8 BPM IDs to names
        super(strName_p, displayName_p, "java.lang.String", fArray_p, iMode_p, fSystem_p, iFieldType_p, fQueueDefinitionField_p, maxValue_p, combatiblefield_p);
    }

    /** collection of selectable operators for user values */
    private static Collection m_useroperators = new Vector();
    static
    {
        m_useroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
    }

    /** get a collection of possible filter operators for a given field
     * 
     * @return Collection of operators as defined with OwSearchOperator.CRIT_OP_...
     * 
     */
    public Collection getOperators() throws Exception
    {
        return m_useroperators;
    }
}
