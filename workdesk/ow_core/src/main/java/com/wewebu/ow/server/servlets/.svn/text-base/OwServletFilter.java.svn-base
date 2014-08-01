package com.wewebu.ow.server.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwRequestContext;

/**
 *<p>
 * This filter will setup all the boilerplate around the request/response, needed for Alfresco Workdesk to function properly.
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
 *@since 4.1.0.0
 */
public class OwServletFilter implements Filter
{
    private static final Logger LOG = OwLogCore.getLogger(OwServletFilter.class);

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
        //nothing to do here yet
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if (HttpServletRequest.class.isAssignableFrom(request.getClass()) && HttpServletResponse.class.isAssignableFrom(response.getClass()))
        {
            OwRequestContext.setLocalThreadRequest((HttpServletRequest) request);
            OwRequestContext.setLocalThreadResponse((HttpServletResponse) response);
            try
            {
                chain.doFilter(request, response);
            }
            finally
            {
                OwRequestContext.clear();
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        //nothing to do here yet
    }

}
