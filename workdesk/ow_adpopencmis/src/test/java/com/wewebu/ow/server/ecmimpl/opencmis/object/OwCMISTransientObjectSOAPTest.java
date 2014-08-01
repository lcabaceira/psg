package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

public class OwCMISTransientObjectSOAPTest extends OwCMISTransientObjectTest
{

    public OwCMISTransientObjectSOAPTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected <N extends TransientCmisObject> OwCMISAbstractTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        return new OwCMISBulkTransientObject<N>(transientCmisObject, creationContext, session);
    }

}
