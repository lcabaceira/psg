package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

public class OwCMISAlfrescoTransientObjectSOAPTest extends OwCMISAlfrescoTransientObjectTest
{

    public OwCMISAlfrescoTransientObjectSOAPTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected <N extends TransientCmisObject> OwCMISAlfrescoTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        return new OwCMISAlfrescoBulkTransientObject<N>(transientCmisObject, creationContext, session);
    }
}
