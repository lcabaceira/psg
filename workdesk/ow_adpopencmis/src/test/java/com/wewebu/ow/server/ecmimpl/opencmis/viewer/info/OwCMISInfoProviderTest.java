package com.wewebu.ow.server.ecmimpl.opencmis.viewer.info;

import java.io.IOException;
import java.util.HashMap;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.viewer.OwInfoProvider;
import com.wewebu.ow.unittest.mock.OwMockInfoRequest;

public class OwCMISInfoProviderTest extends OwCMISIntegrationTest
{
    private OwInfoProvider infoProv;
    private String dmsid;

    public OwCMISInfoProviderTest(String name_p)
    {
        super(name_p);
    }

    public void testHandleRequest() throws OwException, IOException
    {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("dmsid", dmsid);
        StringBuilder testString = new StringBuilder("<docInfo><prop>isVersionable</prop>");
        testString.append("<prop>").append("PermToCheckinDoc").append("</prop>");
        testString.append("<prop>").append("PermToCheckoutDoc").append("</prop>");
        testString.append("<prop>").append("PermToCancelCheckout").append("</prop>");
        testString.append("<prop>").append("PermToCopyFromDoc").append("</prop>");
        testString.append("<prop>").append("isCheckout").append("</prop>");
        testString.append("<prop>").append("PermToModify").append("</prop>");
        testString.append("<prop>").append("NumParents").append("</prop>");
        testString.append("<prop>").append("PermToCopyFromDoc").append("</prop>");
        testString.append("<prop>").append("HasExternalResources").append("</prop>");
        testString.append("<prop>").append("PermToCreateAnnotations").append("</prop>");
        testString.append("<prop>").append("PermToViewAnnotations").append("</prop>");
        testString.append("<prop>").append("PermToCreateDoc").append("</prop>");
        testString.append("</docInfo>");
        OwMockInfoRequest req = new OwMockInfoRequest(param, testString.toString());
        infoProv.handleRequest(req, System.out);
        System.out.println();//just create a line end
    }

    public void testException()
    {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("dmsid", dmsid);
        OwMockInfoRequest req = new OwMockInfoRequest(param, "<docInfo><prop>HandledNullpointer</prop></docInfo>");
        try
        {
            infoProv.handleRequest(req, System.out);
        }
        catch (OwException e)
        {
            fail("Unsupported definitions should be handled correct");
        }
        catch (IOException e)
        {
            fail("Unsupported definitions should be handled correct");
        }
    }

    public void testInvalidDmsid()
    {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("dmsid", "NotADmsid!");
        OwMockInfoRequest req = new OwMockInfoRequest(param, "<docInfo><prop>Should not be touched</prop></docInfo>");
        try
        {
            infoProv.handleRequest(req, System.out);
        }
        catch (OwException e)
        {
            fail("Unknown object/dmsid will just return nothing.");
        }
        catch (IOException e)
        {
            fail("Unknown object/dmsid should not result in IO exception.");
        }
    }

    public void testGetDmsPrefix()
    {
        assertEquals(getNetwork().getDMSPrefix(), infoProv.getContext());
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
        infoProv = (OwInfoProvider) getNetwork().getInterface(OwInfoProvider.class.getCanonicalName(), null);
        OwObject obj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + "Documents.xml", true);
        dmsid = obj.getDMSID();
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (this.getNetwork() != null)
        {
            getNetwork().logout();
        }
        super.tearDown();
    }
}
