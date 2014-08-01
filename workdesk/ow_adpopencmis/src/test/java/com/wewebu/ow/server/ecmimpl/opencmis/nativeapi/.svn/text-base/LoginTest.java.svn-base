package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.chemistry.opencmis.client.bindings.CmisBindingFactory;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.spi.CmisBinding;

/**
 *<p>
 * Native OpenCMIS API test for Login.
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
public class LoginTest extends TestCase
{
    private Map<String, String> params;
    private CmisBinding binding;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        params = new HashMap<String, String>();
        params.put(SessionParameter.USER, "admin");
        params.put(SessionParameter.PASSWORD, "admin");
    }

    public void testLoginSOAP()
    {
        CmisBindingFactory factory = CmisBindingFactory.newInstance();
        params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/RepositoryService?wsdl");
        params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/NavigationService?wsdl");
        params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/ObjectService?wsdl");
        params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/VersioningService?wsdl");
        params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/DiscoveryService?wsdl");
        params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/RelationshipService?wsdl");
        params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/MultifilingService?wsdl");
        params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/PolicyService?wsdl");
        params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/AclService?wsdl");

        binding = factory.createCmisWebServicesBinding(params);

        verifyConnection("SOAP");
    }

    public void testLoginAtomPub()
    {
        CmisBindingFactory factory = CmisBindingFactory.newInstance();
        params.put(SessionParameter.ATOMPUB_URL, "http://abs-alfone.alfresco.com:8080/alfresco/cmisatom");
        binding = factory.createCmisAtomPubBinding(params);

        verifyConnection("AtomPub");
    }

    /**
     * Simply list available repositories of current connection
     */
    protected void verifyConnection(String conType)
    {
        System.out.println("=============START-" + conType + "==============");
        List<RepositoryInfo> repos = binding.getRepositoryService().getRepositoryInfos(null);
        for (RepositoryInfo repo : repos)
        {
            System.out.println("RepositoryName: " + repo.getName());
            System.out.println("RepositoryId: " + repo.getId());
            System.out.println("CMIS-Vendor: " + repo.getVendorName());
            System.out.println("CMIS-Version: " + repo.getCmisVersionSupported());
        }
        System.out.println("=============END-" + conType + "==============");
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (binding != null)
        {
            binding.clearAllCaches();
            binding.close();
        }
        if (params != null)
        {
            params.clear();
        }
        super.tearDown();
    }

}
