package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Language;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OwRestException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactory;

/**
 *<p>
 * Factory to create RESTful resources for the old <b>Alfresco REST API</b>.
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
 *@since  4.2.0.0
 *@deprecated This is for temporary internal use only and will be removed as soon as all the information exposed by the old API will be available through the new <strong>Alfresco Workflow Public Rest API</strong>. 
 */
@Deprecated
public class AlfrescoRESTFulFactoryOld
{
    private static final String REST_API_PATH = "/service/api";

    private String baseURL;
    private NativeValueConverterFactory valueConverterFactory;
    private ArrayList<Preference<Language>> acceptedLanguages;
    private Client clientConnector;
    private Map<String, String> callUrls;
    private OwRestletAuthenticationHandler authHandler;

    /**
     * Constructor for REST-Connection handler
     * @param baseURL String base part of URL
     * @param authHandler OwRestletAuthenticationHandler
     * @param currentLocale Locale
     * @param converterFactory NativeValueConverterFactory
     */
    public AlfrescoRESTFulFactoryOld(String baseURL, OwRestletAuthenticationHandler authHandler, Locale currentLocale, NativeValueConverterFactory converterFactory)
    {
        this.baseURL = baseURL;
        this.authHandler = authHandler;
        this.callUrls = new HashMap<String, String>();
        this.valueConverterFactory = converterFactory;

        Language language = Language.valueOf(currentLocale.getLanguage());

        Preference<Language> currentLanguage = new Preference<Language>(language);
        this.acceptedLanguages = new ArrayList<Preference<Language>>();
        acceptedLanguages.add(currentLanguage);

        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTPS);
        protocols.add(Protocol.HTTP);
        this.clientConnector = new Client(protocols);
    }

    public TaskInstanceResourceOld taskInstanceResource(String taskId) throws OwRestException
    {
        String taskInstanceIdEncoded = null;
        try
        {
            taskInstanceIdEncoded = URLEncoder.encode("activiti$" + taskId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceUri = String.format(getCallUrl("/task-instances/%s"), taskInstanceIdEncoded);
        return createResourceFor(resourceUri, TaskInstanceResourceOld.class);
    }

    /**
     * Returns a full URL based on provided action URI,
     * will cache the full URL and return it again if the action URI is requested again. 
     * @param actionUri String URI of the action 
     * @return String full URL for requested action URI
     */
    protected String getCallUrl(String actionUri)
    {
        String callUrl = callUrls.get(actionUri);
        if (callUrl == null)
        {
            callUrl = this.baseURL + REST_API_PATH + actionUri;
            callUrls.put(actionUri, callUrl);
        }
        return callUrl;
    }

    private <T> T createResourceFor(String resourceURI, Class<T> wrappedClass) throws OwRestException
    {
        if (null == this.clientConnector)
        {
            throw new OwRestException("RESTFull Factory was already released.");
        }
        ClientResource cr = new ClientResource(resourceURI);
        this.authHandler.prepareCall(cr);
        cr.getConverterService().setEnabled(true);
        cr.setNext(this.clientConnector);

        T resource = cr.wrap(wrappedClass);

        final Uniform originalNext = cr.getNext();
        cr.setNext(new Uniform() {

            public void handle(Request request, Response response)
            {
                request.getClientInfo().setAcceptedLanguages(acceptedLanguages);
                if (null != originalNext)
                {
                    originalNext.handle(request, response);
                }
            }
        });

        cr.getClientInfo().setAcceptedLanguages(acceptedLanguages);
        return resource;
    }

    /**
     * Call this when you have finished working with this factory.
     * It will release all Restlet client connectors.
     */
    public void release()
    {
        try
        {
            this.clientConnector.stop();
            this.clientConnector = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}