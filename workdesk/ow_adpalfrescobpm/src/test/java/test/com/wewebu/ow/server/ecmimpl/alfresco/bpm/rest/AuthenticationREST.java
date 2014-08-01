package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.LoginBean;

/**
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
@Ignore
public class AuthenticationREST extends AlfrescoBPMNativeFixture
{
    @Test
    public void testSuccessLogin() throws Exception
    {
        ClientResource cr = new ClientResource(BASE_URI + "/service/api/login");
        try
        {
            LoginBean loginBean = new LoginBean("admin", "admin");
            Representation loginEntity = new JsonRepresentation(loginBean);
            Representation token = cr.post(loginEntity);

            JSONObject jsonToken = new JSONObject(token.getText());
            JSONObject jsonData = jsonToken.getJSONObject("data");
            String ticket = jsonData.getString("ticket");

            Assert.assertNotNull(ticket);
            System.err.println(ticket);
        }
        finally
        {
            cr.release();
        }
    }

    @Test
    public void testFailedLogin() throws Exception
    {
        ClientResource cr = new ClientResource(BASE_URI + "/service/api/login");
        try
        {
            LoginBean loginBean = new LoginBean("admin", "adminionus");
            Representation loginEntity = new JsonRepresentation(loginBean);
            cr.post(loginEntity);
            Assert.fail("Bad passwords should not be accepted.");
        }
        catch (ResourceException e)
        {
            //OK
        }
        finally
        {
            cr.release();
        }
    }
}
