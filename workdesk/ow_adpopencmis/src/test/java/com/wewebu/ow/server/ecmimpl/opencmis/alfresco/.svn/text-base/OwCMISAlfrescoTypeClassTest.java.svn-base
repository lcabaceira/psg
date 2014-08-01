package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Basic OwObjectClass test cases.
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
public class OwCMISAlfrescoTypeClassTest extends OwCMISIntegrationTest
{
    private static final String D_OWD_HRDOCUMENT = "D:owd:hrdocument";
    private static final String OWD_DISPATCHDATE = "owd:DispatchDate";

    public OwCMISAlfrescoTypeClassTest(String name_p)
    {
        super("OwCMISAlfrescoTypeClassTest_Test", name_p);
    }

    @Override
    protected OwCMISTestFixture createFixture()
    {
        return new OwCMISAlfrescoIntegrationFixture(this);
    }

    @Override
    protected String getConfigBootstrapName()
    {
        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
    }

    public void testAspectProperties() throws Exception
    {
        OwCMISObjectClass hrDocument = getNetwork().getObjectClass(D_OWD_HRDOCUMENT, null);

        OwCMISPropertyClass<?> dispatchDate = hrDocument.getPropertyClass(OWD_DISPATCHDATE);
        assertNotNull(dispatchDate);
    }

    public void testShortNameDuplication() throws OwException
    {
        OwCMISObjectClass hrDocument = getNetwork().getObjectClass(D_OWD_HRDOCUMENT, null);

        Map<String, OwCMISPropertyClass<?>> propertyClasses = hrDocument.getPropertyClasses();

        Set<String> cmisShortNames = new HashSet<String>();
        Set<String> duplicateShortNames = new HashSet<String>();

        Collection<OwCMISPropertyClass<?>> classes = propertyClasses.values();

        for (OwCMISPropertyClass<?> propertyClass : classes)
        {
            if (propertyClass instanceof OwCMISNativePropertyClass)
            {
                OwCMISNativePropertyClass nativePropertyClass = (OwCMISNativePropertyClass) propertyClass;
                String cmisName = nativePropertyClass.getNonQualifiedName();
                if (cmisShortNames.contains(cmisName))
                {
                    duplicateShortNames.add(cmisName);
                }
                else
                {
                    cmisShortNames.add(cmisName);
                }
            }
        }

        if (!duplicateShortNames.isEmpty())
        {
            fail("Duplicat properties have bee found inc class " + D_OWD_HRDOCUMENT + " : " + duplicateShortNames.toString());
        }
    }

}
