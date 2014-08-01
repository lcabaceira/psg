package com.wewebu.ow.server.ui.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *<p>
 * Request object for OwInfoProvider.
 * An abstraction of a request, providing methods
 * to access different request informations. 
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
 *@since 3.1.0.0
 */
public interface OwInfoRequest
{
    /**
     * Return a list of parameter names
     * which exist in this request.
     * <p>Can return an empty list.</p>
     * @return List of Strings
     */
    List<String> getParameterNames();

    /**
     * Get the value for given parameter. 
     * @param paramName_p String name
     * @return String or null if parameter not exist
     */
    String getParameter(String paramName_p);

    /**
     * Return an InputStream which contains
     * the request body.
     * @return InputStream
     * @throws IOException if InputStream is not accessible
     */
    InputStream getRequestBody() throws IOException;

}
