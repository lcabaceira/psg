package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.LinkedList;

import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyIdDefinitionImpl;

/**
 *<p>
 * Helper for handling Aspects in earlier CMIS versions, before CMIS 1.1. 
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
public class OwCMISAspectsPropertyDefinition extends PropertyIdDefinitionImpl
{
    /**generated serial Version UID*/
    private static final long serialVersionUID = -348628123708843907L;

    public static final String ID = "cmis:secondaryObjectTypeIds";

    public static final String LOCAL_NAME = "CMIS1.1_Simulation";

    public OwCMISAspectsPropertyDefinition()
    {
        super();
        setIsInherited(Boolean.FALSE);
        setIsOpenChoice(Boolean.FALSE);
        setIsOrderable(Boolean.FALSE);
        setIsQueryable(Boolean.FALSE);
        setIsRequired(Boolean.FALSE);
        /* These definition is incorrect, if reading CMIS 1.1 spec
         * but we want to hide that property definition, so user will not change/see values*/
        setUpdatability(Updatability.READONLY);

        setCardinality(Cardinality.MULTI);
        setDescription("Simulated Internal CMIS 1.1 handling");
        setDefaultValue(new LinkedList<String>());
        setId(ID);
        setLocalName(LOCAL_NAME);
        setDisplayName("NonCMIS1.1Handling");
        setPropertyType(PropertyType.ID);
    }

}
