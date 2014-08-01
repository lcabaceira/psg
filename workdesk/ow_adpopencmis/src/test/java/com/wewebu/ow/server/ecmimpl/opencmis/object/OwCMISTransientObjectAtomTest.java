package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

public class OwCMISTransientObjectAtomTest extends OwCMISTransientObjectTest
{

    public OwCMISTransientObjectAtomTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected String getConfigBootstrapName()
    {
        // TODO Auto-generated method stub
        return super.getConfigBootstrapName();
    }

    @Override
    protected <N extends TransientCmisObject> OwCMISAbstractTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session)
    {
        return new OwCMISBulkTransientObject<N>(transientCmisObject, creationContext, session);
    }

}
