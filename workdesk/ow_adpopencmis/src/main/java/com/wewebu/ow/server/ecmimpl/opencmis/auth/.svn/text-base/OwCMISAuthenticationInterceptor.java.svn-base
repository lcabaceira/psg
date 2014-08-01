package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;

import com.wewebu.ow.server.auth.OwAuthenticator;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for authentication interception which is used for creation of OpenCMIS AuthenticationProvider.
 * The created AuthenticationProvider instance will be used in communication with CMIS back-end.
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
public interface OwCMISAuthenticationInterceptor extends OwAuthenticator
{

    /**
     * Initialization of AuthProviderFactory instance
     * with corresponding configuration.
     * @param configNode OwXMLUtil node which represents AuthProviderFactory configuration
     * @throws OwException
     */
    void init(OwXMLUtil configNode) throws OwException;

    /**
     * Called to create a new Authentication provider which should be used in 
     * communication between CMIS client and server.
     * @param information Map current available informations for OpenCMIS Session creation
     * @return AuthenticationProvider which should be used for session creation
     * @throws OwException
     */
    AuthenticationProvider createAuthenticationProvider(Map<String, String> information) throws OwException;

    /**
     * Called before login page is rendered, can be used for redirect or preparation
     * of this instance. The return value defines if the process sequence should be processed as
     * defined, or if it should stop because the response was created by this instance.
     * <p>If true is returned, by default a login is processed through network.
     * The login will call the {@link #createAuthenticationProvider(Map)} and
     * verify against back-end if login was successful.</p>
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return boolean true process sequence as defined, false stop processing
     * @throws OwException
     */
    boolean onRequest(HttpServletRequest req, HttpServletResponse resp) throws OwException, IOException;

    /**
     * Called after onRequest returns false, this method will control if
     * rendering should be processed afterwards or not.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return true process rendering, else request processing will be stopped
     * @since 4.2.0.0
     */
    boolean processRendering(HttpServletRequest req, HttpServletResponse resp);

    /**
     * Factory to create an OwCMISRestletAuthenticationHandler which can be used for non-CMIS calls.
     * @param cred OwCMISCredentials
     * @return OwCMISRestletAuthenticationHandler
     * @throws OwException if not possible to create instance
     * @since 4.2.0.0
     */
    OwRestletAuthenticationHandler createRestletAuthenticationHandler(OwCMISCredentials cred) throws OwException;
}
