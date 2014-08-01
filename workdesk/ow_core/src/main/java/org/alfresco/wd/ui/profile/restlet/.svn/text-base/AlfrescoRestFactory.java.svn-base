package org.alfresco.wd.ui.profile.restlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Factory for change password handling, based on REST API. 
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
 *@since 4.2.0.0
 */
public class AlfrescoRestFactory
{
    protected static final String SERVICE_URI = "/service/api/person/changepassword/";

    private String baseUrl;
    private OwRestletAuthenticationHandler authHandler;

    public AlfrescoRestFactory(String baseUrl, OwRestletAuthenticationHandler authHandler)
    {
        this.baseUrl = baseUrl;
        this.authHandler = authHandler;
    }

    public void changePassword(String userName, String oldPassword, String newPassword) throws OwException
    {
        Client client = createClient();
        // call is based on something like /alfresco/service/api/person/changepassword/{userName}
        String service;
        try
        {
            service = SERVICE_URI + URLEncoder.encode(userName, "UTF8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwServerException("Unable to encode userName with UTF-8 encoding", e);
        }

        ClientResource cr = createClientResource(service, client);
        PasswordPostData data = createPasswordPostData(oldPassword, newPassword);

        try
        {
            cr.post(data);//Start connection send data
        }
        catch (ResourceException rex)
        {
            String msg = null;
            try
            {
                StringWriter cache = new StringWriter();
                cr.getResponseEntity().write(cache);
                /* { "success" : false,
                 *   "message" : "00240039 Do not have appropriate auth or wrong auth details provided."} */
                StringBuffer buf = cache.getBuffer();
                String keyWord = "\"message\"";
                int idx = buf.indexOf(keyWord);
                if (idx > 0)
                {
                    msg = buf.substring(idx);
                    idx = msg.indexOf('"', keyWord.length());
                    msg = msg.substring(idx, msg.indexOf('"', idx + 1));
                }
            }
            catch (IOException e)
            {
            }
            throw new OwServerException(new OwString1("profile.restlet.AlfrescoRestFactory.err.changePwd", "Unable to change password (%1).", msg == null ? rex.getMessage() : msg), rex);
        }
        finally
        {
            try
            {
                client.stop();//END connection
            }
            catch (Exception e)
            {
                throw new OwServerException("Failed to close connection", e);
            }
        }
    }

    /**
     * Factory method to create ClientResource.
     * @param uri String relative to base URL
     * @param client Client managing resource and connection
     * @return ClientResource
     * @throws OwException
     */
    protected ClientResource createClientResource(String uri, Client client) throws OwException
    {
        ClientResource cr = new ClientResource(this.baseUrl + uri);
        getAuthHandler().prepareCall(cr);
        cr.setNext(client);

        return cr;
    }

    /**
     * Factory to create base client for RESTFull call
     * @return org.restlet.Client
     */
    protected Client createClient()
    {
        List<Protocol> protocols = new LinkedList<Protocol>();
        protocols.add(Protocol.HTTPS);
        protocols.add(Protocol.HTTP);
        return new Client(protocols);
    }

    /**
     * Getter for provided AuthHandler
     * @return OwRestletAuthenticationHandler
     */
    protected OwRestletAuthenticationHandler getAuthHandler()
    {
        return this.authHandler;
    }

    /**
     * Factory to create POJO to be send in change requet.
     * @param oldPassword String
     * @param newPassword String
     * @return PasswordPostData
     */
    protected PasswordPostData createPasswordPostData(String oldPassword, String newPassword)
    {
        return new PasswordPostData(newPassword, oldPassword);
    }

    @JsonSerialize(include = Inclusion.NON_EMPTY)
    public static class PasswordPostData
    {
        private String newpw, oldpw;

        public PasswordPostData()
        {
        }

        public PasswordPostData(String newpw, String oldpw)
        {
            this.newpw = newpw;
            this.oldpw = oldpw;
        }

        public String getNewpw()
        {
            return newpw;
        }

        public void setNewpw(String newpw)
        {
            this.newpw = newpw;
        }

        public String getOldpw()
        {
            return oldpw;
        }

        public void setOldpw(String oldpw)
        {
            this.oldpw = oldpw;
        }
    }
}
