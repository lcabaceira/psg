package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

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
public class OwCMISAbstractAlfrescoObjectClassTest extends OwCMISIntegrationTest
{
    static final Logger LOG = Logger.getLogger(OwCMISAbstractAlfrescoObjectClassTest.class);

    public OwCMISAbstractAlfrescoObjectClassTest(String name_p)
    {
        super("OwCMISAlfrescoObjectClassTest_Test", name_p);
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

    public void testObjectAspects() throws Exception
    {
        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;

        OwCMISNativeSession nativeSession = (OwCMISNativeSession) getNetwork().getSession(null);
        OwCMISNativeObject<TransientDocument> documentObject = nativeSession.from(integrationFixture.getDocument().getTransientDocument(), null);

        OwCMISProperty<?> title = documentObject.getProperty("cm:title");
        assertNotNull(title);
        assertEquals(OwCMISAlfrescoIntegrationFixture.DOCUMENT_TITLE, title.getValue());
    }

    public void testObjectSingleton() throws Exception
    {
        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;

        OwCMISNativeSession nativeSession = (OwCMISNativeSession) getNetwork().getSession(null);
        OwCMISNativeObjectClass<?, TransientDocument> documentClass = nativeSession.classOf(integrationFixture.getDocument().getTransientDocument());
        documentClass.from(integrationFixture.getDocument().getTransientDocument(), null);

        try
        {
            documentClass.from(integrationFixture.getDocument().getTransientDocument(), null);
        }
        catch (OwInvalidOperationException e)
        {
            LOG.warn("Duplicate object creation was sucessfully denied.");
        }

    }

}
