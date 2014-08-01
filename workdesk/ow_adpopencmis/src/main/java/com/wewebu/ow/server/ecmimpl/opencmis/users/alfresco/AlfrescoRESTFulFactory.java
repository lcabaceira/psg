package com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.log4j.Logger;
import org.restlet.resource.ClientResource;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;

/**
 *<p>
 * Factory to create RESTful resources.
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
 *@since  4.1.1.0
 */
public class AlfrescoRESTFulFactory
{
    private static Logger LOG = OwLog.getLogger(AlfrescoRESTFulFactory.class);

    private String baseURL;
    private String findUserUri;
    private String findGroupsUri;
    private String userInfoUri;

    private OwRestletAuthenticationHandler authHandler;

    public AlfrescoRESTFulFactory(OwAuthenticationConfiguration conf, OwRestletAuthenticationHandler authHandler)
    {
        this.baseURL = conf.getConfiguration().getSafeTextValue("BaseURL", "");
        this.findGroupsUri = this.baseURL + conf.getConfiguration().getSafeTextValue("FindGroupsURI", "/service/api/groups?shortNameFilter=%s");
        this.findUserUri = this.baseURL + conf.getConfiguration().getSafeTextValue("FindUserURI", "/service/api/people?filter=%s");
        this.userInfoUri = this.baseURL + conf.getConfiguration().getSafeTextValue("UserInfoURI", "/service/api/people/%s?groups=true");
        this.authHandler = authHandler;
    }

    public ListGroupsResource listGroupsResource(String shortNameFilter) throws OwRestException
    {
        String resourceURI = String.format(this.findGroupsUri, encodeValue(shortNameFilter));
        if (LOG.isDebugEnabled())
        {
            LOG.debug("listGroupsResource call = " + resourceURI);
        }
        return createResourceFor(resourceURI, ListGroupsResource.class);
    }

    public ListPeopleResource getPeopleResource(String filterQuery) throws OwRestException
    {
        String resourceURI = String.format(this.findUserUri, encodeValue(filterQuery));
        if (LOG.isDebugEnabled())
        {
            LOG.debug("getPeopleResource call = " + resourceURI);
        }
        return createResourceFor(resourceURI, ListPeopleResource.class);
    }

    public GetPersonResource getPersonResource(String userName) throws OwRestException
    {
        String resourceURI = String.format(this.userInfoUri, encodeValue(userName));
        if (LOG.isDebugEnabled())
        {
            LOG.debug("getPersonResource call = " + resourceURI);
        }
        return createResourceFor(resourceURI, GetPersonResource.class);
    }

    private String encodeValue(String aStringValue) throws OwRestException
    {
        String encodeValue = null;
        try
        {
            encodeValue = URLEncoder.encode(aStringValue, "UTF-8");
            encodeValue = encodeValue.replaceAll("[+]", "%20");
        }
        catch (UnsupportedEncodingException encEx)
        {
            throw new OwRestException("Could not encode the filter!", encEx);
        }
        return encodeValue;
    }

    private <T> T createResourceFor(String resourceURI, Class<T> wrappedClass) throws OwRestException
    {
        ClientResource cr = new ClientResource(resourceURI);
        this.authHandler.prepareCall(cr);

        cr.getConverterService().setEnabled(true);

        T resource = cr.wrap(wrappedClass);

        //        final Uniform originalNext = cr.getNext();
        //        cr.setNext(new Uniform() {
        //
        //            public void handle(Request request, Response response)
        //            {
        //                //                request.getClientInfo().setAcceptedLanguages(acceptedLanguages);
        //                if (null != originalNext)
        //                {
        //                    originalNext.handle(request, response);
        //                }
        //            }
        //        });
        //
        //        //        cr.getClientInfo().setAcceptedLanguages(acceptedLanguages);
        return resource;
    }
}