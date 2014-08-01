package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import org.apache.chemistry.opencmis.commons.spi.CmisBinding;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;

public class OwCMISServiceAccessTest extends OwCMISIntegrationTest
{

    public OwCMISServiceAccessTest(String name_p)
    {
        super("SERVICE_ACCESS", name_p);
    }

    public void testCMISServices() throws Exception
    {
        loginAdmin();
        //RepositoryService indirect test through getResource call
        OwCMISSession session = this.getNetwork().getResource(null).createSession();
        assertNotNull(session);

        assertTrue(session instanceof OwCMISNativeSession);
        OwCMISNativeSession natSes = (OwCMISNativeSession) session;
        CmisBinding binding = natSes.getOpenCMISSession().getBinding();
        assertNotNull(binding.getNavigationService());
        assertNotNull(binding.getObjectService());
        assertNotNull(binding.getDiscoveryService());
        assertNotNull(binding.getMultiFilingService());
        this.getNetwork().logout();
    }
}
