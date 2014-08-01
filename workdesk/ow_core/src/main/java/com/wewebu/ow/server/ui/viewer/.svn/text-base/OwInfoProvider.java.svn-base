package com.wewebu.ow.server.ui.viewer;

import java.io.IOException;
import java.io.OutputStream;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Information Provider Interface.
 * Implementing classes will be used to define
 * behavior and access rights on the fly.
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
public interface OwInfoProvider
{
    /**Constant for the DMSID value to retrieve from OwInfoRequest object*/
    public static final String PARAM_DMSID = "dmsid";
    /**Constant for the CONTEXT value to retrieve from OwInfoRequest object*/
    public static final String PARAM_CONTEXT = "ctx";

    /**
     * Returns registry context
     * on which this provider is registered on.
     * @return String context representation
     */
    String getContext();

    /**
     * Method called to be process a request.
     * @param request_p OwInformationRequest to process
     * @param answer_p OutputStream to write back answer
     * @throws IOException if problems with any I/O handling
     */
    void handleRequest(OwInfoRequest request_p, OutputStream answer_p) throws OwException, IOException;

}
