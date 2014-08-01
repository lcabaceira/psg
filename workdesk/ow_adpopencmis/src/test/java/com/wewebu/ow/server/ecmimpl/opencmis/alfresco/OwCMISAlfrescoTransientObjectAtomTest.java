package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

/**
 *<p>
 * OwCMISAlfrescoBulkTransientObjectTest.
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
public class OwCMISAlfrescoTransientObjectAtomTest extends OwCMISAlfrescoTransientObjectTest
{

    public OwCMISAlfrescoTransientObjectAtomTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected String getConfigBootstrapName()
    {
        //SEE JIRA  issue  OWD-4651 : OwCMISAlfrescoBatchTransientObject are based on open CMIS API that does not work
        //                            with Alfresco SOAP implementation.
        //
        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
        //                return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_SOAP_XML;
    }

    @Override
    protected <N extends TransientCmisObject> OwCMISAlfrescoTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        return new OwCMISAlfrescoBulkTransientObject<N>(transientCmisObject, creationContext, session);
    }

}
