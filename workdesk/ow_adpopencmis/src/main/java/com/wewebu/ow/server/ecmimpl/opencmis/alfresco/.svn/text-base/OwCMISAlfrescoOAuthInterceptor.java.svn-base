package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.cmis.client.authentication.OAuthCMISAuthenticationProvider;
import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentials;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of AuthProviderFactory for OAuth handling.
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
public class OwCMISAlfrescoOAuthInterceptor extends OwCMISCredentialsAuthenticator
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAlfrescoOAuthInterceptor.class);

    /**Configuration node for CLIENT ID*/
    public static final String EL_CLIENT_ID = "ClientId";
    /**Configuration node for client secrete*/
    public static final String EL_CLIENT_SECRETE = "ClientSecrete";
    /**Configuration node for Client redirect/callback URL*/
    public static final String EL_CALLBACK_URL = "CallbackUrl";
    /**Configuration node of URL where to redirect to get GRANT from User*/
    public static final String EL_GRANT_URL = "GrantUrl";
    /**Configuration node of URL where to get AccessToken*/
    public static final String EL_ACCESS_URL = "AccessUrl";
    /**Configuration Node for refresh URL*/
    public static final String EL_REFRESH_URL = "RefreshUrl";
    /**(optional) Configuration node to define the value encoding URL-parameter, by default UTF-8*/
    public static final String EL_URL_ENCODING = "UrlEncoding";

    public static final String PARAM_CODE = "code";

    public static final String PARAM_REDIRECT_URI = "redirect_uri";

    public static final String PARAM_SCOPE = "scope";

    public static final String PARAM_RESPONSE_TYPE = "response_type";
    public static final String PARAM_CLIENT_ID = "client_id";

    private Map<String, String> conf;

    private String grantToken;

    public OwCMISAlfrescoOAuthInterceptor(OwBaseInitializer initializer)
    {
        super(initializer);
    }

    public OwCMISAlfrescoOAuthInterceptor(OwCMISNetwork network)
    {
        super(network);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(OwXMLUtil configNode) throws OwException
    {
        conf = new HashMap<String, String>();
        Iterator<Node> it = configNode.getSafeNodeList().iterator();
        while (it.hasNext())
        {
            Node n = it.next();
            conf.put(n.getNodeName(), n.getTextContent());
        }
    }

    @Override
    public synchronized AuthenticationProvider createAuthenticationProvider(Map<String, String> information) throws OwException
    {
        if (getConfigValue(EL_ACCESS_URL) == null)
        {
            return OAuthCMISAuthenticationProvider.alfrescoOAuthProvider(getConfigValue(EL_CLIENT_ID), getConfigValue(EL_CLIENT_SECRETE), getConfigValue(EL_CALLBACK_URL), getGrantToken());
        }
        else
        {
            if (LOG.isTraceEnabled())
            {
                LOG.trace("Creating OAuthCMISAuthenticationProvider with non-default configuration");
                LOG.trace("Callback-/Redirect-URL = " + getConfigValue(EL_CALLBACK_URL));
                LOG.trace("AccessToken-URL = " + getConfigValue(EL_ACCESS_URL));
                LOG.trace("RefreshToken-URL = " + getConfigValue(EL_REFRESH_URL));
                LOG.trace("Grant = " + this.grantToken);
            }
            return new OAuthCMISAuthenticationProvider(getConfigValue(EL_CLIENT_ID), getConfigValue(EL_CLIENT_SECRETE), getConfigValue(EL_CALLBACK_URL), getConfigValue(EL_ACCESS_URL), getConfigValue(EL_REFRESH_URL), getGrantToken());
        }
    }

    @Override
    public boolean onRequest(HttpServletRequest req, HttpServletResponse resp) throws OwException, IOException
    {
        String uri = req.getRequestURI();
        if (LOG.isTraceEnabled())
        {
            LOG.trace("OwCMISAlfrescoOAuthInterceptor.onRequest: request parameter:");
            Iterator<?> params = req.getParameterMap().entrySet().iterator();
            while (params.hasNext())
            {
                Entry param = (Entry) params.next();
                StringBuilder logTrace = new StringBuilder();
                String[] paramVals = (String[]) param.getValue();
                for (String value : paramVals)
                {
                    if (logTrace.length() > 0)
                    {
                        logTrace.append(",");
                    }
                    logTrace.append(value);
                }
                logTrace.insert(0, " ");
                logTrace.insert(0, param.getKey());
                LOG.trace(logTrace);
            }
        }
        if (req.getParameter(PARAM_CODE) != null)
        {//Step 2, we got response regarding grant, retrieve access token

            return stageTwo(req, resp);
        }
        else
        {//Step 1, redirect client to get Grant-Token
            return stageOne(req, resp);
        }
    }

    /**
     * Stage one of OAuth is to redirect to grant URL and wait for user grant token.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return boolean true
     * @throws OwException
     * @throws IOException 
     */
    protected boolean stageOne(HttpServletRequest req, HttpServletResponse resp) throws OwException, IOException
    {
        StringBuilder grantUrl = new StringBuilder(getConfigValue(EL_GRANT_URL));
        if (grantUrl.length() > 0)
        {
            //oauth/grant?client_id=your_api_key&redirect_uri=your_callback_url&scope=public_api&response_type=code
            appendParameter(grantUrl, PARAM_CLIENT_ID, getConfigValue(EL_CLIENT_ID));
            appendParameter(grantUrl, PARAM_REDIRECT_URI, getConfigValue(EL_CALLBACK_URL));
            appendParameter(grantUrl, PARAM_SCOPE, getConfigValue(PARAM_SCOPE));
            appendParameter(grantUrl, PARAM_RESPONSE_TYPE, getConfigValue(PARAM_RESPONSE_TYPE, PARAM_CODE));

            resp.sendRedirect(grantUrl.toString());
            return false;
        }
        return true;
    }

    /**
     * Stage two is to retrieve access token if needed.<br />
     * We will use the AuthenticationProvider factory method of Alfresco extension,
     * so that the refresh token handling will be done through the created AuthenticationProvider.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return boolean true process further sequence, false interrupt sequence and return
     * @throws OwException
     */
    protected boolean stageTwo(HttpServletRequest req, HttpServletResponse resp) throws OwException
    {
        this.grantToken = req.getParameter(PARAM_CODE);
        if (grantToken == null)
        {
            throw new OwAccessDeniedException("Grant for cloud access denied");
        }
        return true;
    }

    protected String getConfigValue(String configName)
    {
        return this.conf.get(configName);
    }

    protected String getConfigValue(String configName, String defaultValue)
    {
        String val = getConfigValue(configName);
        return val == null ? defaultValue : val;
    }

    /**
     * Helper method for creation of URL, will attach the parameter name and value
     * to it and escape the value using UTF-8 base.
     * @param url StringBuilder URL
     * @param paramName String
     * @param paramVal String
     * @throws UnsupportedEncodingException
     */
    protected void appendParameter(StringBuilder url, String paramName, String paramVal) throws UnsupportedEncodingException
    {
        if (paramName != null && paramVal != null)
        {
            if (url.indexOf("?") < 0)
            {
                url.append("?");
            }

            if (url.charAt(url.length() - 1) != '?')
            {
                url.append("&");
            }
            url.append(paramName);
            url.append("=");

            url.append(java.net.URLEncoder.encode(paramVal, "UTF-8"));
        }
    }

    /**
     * Get the grant token which is used for access token retrieval. 
     * @return String
     */
    protected String getGrantToken()
    {
        return this.grantToken;
    }

    @Override
    public OwRestletAuthenticationHandler createRestletAuthenticationHandler(OwCMISCredentials cred) throws OwException
    {
        OAuthCMISAuthenticationProvider oauthProv = (OAuthCMISAuthenticationProvider) cred.getAuthenticationProvider();
        return new OwCMISOAuthRestletHandler(oauthProv.getAccessTokenData().getAccessToken());
    }
}
