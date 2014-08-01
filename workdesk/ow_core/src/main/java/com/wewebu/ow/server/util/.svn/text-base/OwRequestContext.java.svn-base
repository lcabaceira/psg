package com.wewebu.ow.server.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p>
 * Helper for request context handling.
 * An implementation based on ThreadLocal holder, which are needed
 * for multiple/concurrent thread handling like in AJAX/Web 2.0 environments.
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
public class OwRequestContext
{
    /**
     * Holder for request associated with current thread.
     */
    private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    /**
     * Holder for response associated with current thread.
     */
    private static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>();

    /** 
     * Set the request associated with current thread.
     * @param request_p - the {@link HttpServletRequest} object
     */
    public static void setLocalThreadRequest(HttpServletRequest request_p)
    {
        requestHolder.set(request_p);
    }

    /** 
     * Set the response associated with current thread.
     * @param response_p - the {@link HttpServletResponse} object
     */
    public static void setLocalThreadResponse(HttpServletResponse response_p)
    {
        responseHolder.set(response_p);
    }

    /** 
     * Get the request associated with current thread.
     * @return the current {@link HttpServletRequest} object
     */
    public static HttpServletRequest getLocalThreadRequest()
    {
        return requestHolder.get();
    }

    /** 
     * Get the response associated with current thread.
     * @return the current {@link HttpServletResponse} object
     */
    public static HttpServletResponse getLocalThreadResponse()
    {
        return responseHolder.get();
    }

    /**
     * Clear the Request context and all dependent objects references.
     */
    public static void clear()
    {
        responseHolder.remove();
        requestHolder.remove();
    }
}
