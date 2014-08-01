package com.wewebu.ow.server.servlets;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwUserOperationDispatch;
import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwWebApplication;

/**
 *<p>
 * ServletRequestListener.
 * Specific Listener used to notify the framework for pre- and postprocessing of a request.
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
 *@since 3.2.0.0
 */
public class OwRequestListener implements ServletRequestListener, HttpSessionAttributeListener
{
    private static final Logger LOG = OwLogCore.getLogger(OwRequestListener.class);
    /**Attribute name which is set if prepare was executed*/
    protected static final String PREPARE = "owd.prepare";
    /**Attribute name which contains the start time of request (in milliseconds Long)*/
    protected static final String REQ_PROC_START = "owd.req.start";

    private static List<String> lst = new LinkedList<String>();
    static
    {
        lst.add("/js");
        lst.add("/designs");
        lst.add("/applets");
        lst.add("/help");
    }

    public void requestDestroyed(ServletRequestEvent sre)
    {
        if (sre.getServletRequest() instanceof HttpServletRequest)
        {
            HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
            if (req.getSession(false) != null && Boolean.TRUE.equals(req.getAttribute(PREPARE)))
            {
                OwAppContext context = ((OwAppContext) req.getSession(false).getAttribute(OwWebApplication.CONTEXT_KEY));
                dispatch(OwUserOperationType.STOP, context, req.getContextPath());
            }
        }
        if (LOG.isTraceEnabled())
        {
            Long startTime = ((Long) sre.getServletRequest().getAttribute(REQ_PROC_START));
            if (startTime != null)//check for null since initialization could change the LOG level
            {
                LOG.trace(new StringBuilder("OwRequestListener.requestDestroyed: Request process time (ms) = ").append(System.currentTimeMillis() - startTime.longValue()));
            }
        }
    }

    public void requestInitialized(ServletRequestEvent sre)
    {
        if (sre.getServletRequest() instanceof HttpServletRequest)
        {
            HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
            if (req.getSession(false) != null && isNotificationPath(req.getServletPath()))
            {
                sre.getServletRequest().setAttribute(PREPARE, Boolean.TRUE);
                OwAppContext context = ((OwAppContext) req.getSession(false).getAttribute(OwWebApplication.CONTEXT_KEY));
                dispatch(OwUserOperationType.START, context, req.getContextPath());
            }
            else
            {
                sre.getServletRequest().setAttribute(PREPARE, Boolean.FALSE);
            }
        }
        if (LOG.isTraceEnabled())
        {
            sre.getServletRequest().setAttribute(REQ_PROC_START, Long.valueOf(System.currentTimeMillis()));
        }
    }

    private void dispatch(OwUserOperationType opType, OwAppContext ctx, String appCtx)
    {
        if (ctx != null)
        {
            try
            {
                OwNetwork network = ctx.getRegisteredInterface(OwRoleManagerContext.class).getNetwork();
                OwUserInfo user = null;
                if (network != null)
                {
                    OwCredentials credentials = network.getCredentials();
                    if (credentials != null)
                    {
                        user = credentials.getUserInfo();
                    }
                }
                opType.fireAndForget(ctx.getRegisteredInterface(OwUserOperationDispatch.class), user, appCtx);
            }
            catch (Exception e)
            {
                LOG.error("Could not dispatch operation-started event.", e);
            }
        }
    }

    /**(overridable)
     * Check if current request path is a framework dependent processing.
     * <p>This method filter out the calls which are simple resource
     * request for images, CSS and so on.</p>
     * @param servletPath String Servlet path
     * @return boolean checking the provided path.
     */
    protected boolean isNotificationPath(String servletPath)
    {
        if (servletPath != null)
        {
            for (String skip : lst)
            {
                if (servletPath.startsWith(skip))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbEv)
    {
        if (OwWebApplication.CONTEXT_KEY.equals(sbEv.getName()))
        {
            OwAppContext context = (OwAppContext) sbEv.getValue();
            context.getHttpRequest().setAttribute(PREPARE, Boolean.TRUE);
            dispatch(OwUserOperationType.START, context, "");
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbEv)
    {
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbEv)
    {
    }
}
