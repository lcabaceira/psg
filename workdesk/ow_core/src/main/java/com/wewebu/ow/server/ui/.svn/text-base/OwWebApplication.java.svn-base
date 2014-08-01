package com.wewebu.ow.server.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.servlets.OwInitialisingContextListener;
import com.wewebu.ow.server.util.OwExceptionManager;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Main Application Class, handles Request Response and maps to document view.<br/>
 * @see OwPortletApplication
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
@SuppressWarnings("rawtypes")
public class OwWebApplication
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwWebApplication.class);

    /** target ID for the login view */
    public static final String LOGIN_VIEW_KEY = "ow_LoginView";
    /** session key for the context */
    public static final String CONTEXT_KEY = "ow_Context";
    /** session key for the main view */
    private static final String VIEW_KEY = "ow_View";

    /** HTML element ID of the body tag */
    public static final String BODY_ID = "owbody";

    /** session attribute name for the header of the loggedout.jsp page */
    public static final String SESSION_KEY_LOGGEDOUT_HEADER = "ow_loggedout_header";
    /** session attribute name for the message of the loggedout.jsp page */
    public static final String SESSION_KEY_LOGGEDOUT_MESSAGE = "ow_loggedout_message";

    /** class definition for the login view class to be created for the web application */
    protected Class m_LoginClass = null;
    /** class definition for the context class to be created for the web application */
    protected Class m_ContextClass = null;
    /** class definition for the view class to be created for the web application */
    protected Class m_ViewClass = null;

    /** path the the main view page */
    protected String m_strMainJspPage;

    /** Main View Object */
    protected OwView m_MainView = null;

    /** Login View Object */
    protected OwView m_LoginView = null;

    /** Main Context Object */
    protected OwAppContext m_Context = null;

    /** Creates a new instance of WdWebApplication */
    public OwWebApplication()
    {

    }

    /** sets the Context class
     * @param docClass_p Class of the Context
     */
    public void setContextClass(Class docClass_p)
    {
        m_ContextClass = docClass_p;
    }

    /** sets the view class
     * @param viewClass_p Class of the view
     */
    public void setViewClass(Class viewClass_p)
    {
        m_ViewClass = viewClass_p;
    }

    /** sets the login view class
     * @param loginClass_p Class of the login view
     */
    public void setLoginClass(Class loginClass_p)
    {
        m_LoginClass = loginClass_p;
    }

    /** sets the path to the main JSP page
     *
     * @param strJspPath_p
     */
    public void setJspPath(String strJspPath_p)
    {
        m_strMainJspPage = strJspPath_p;
    }

    /** get the main view
     * 
     * @return OwView
     */
    public static OwView getMainView(HttpSession session_p)
    {
        return (OwView) session_p.getAttribute(VIEW_KEY);
    }

    /** get the login view
     * 
     * @return OwView
     */
    public static OwView getLoginView(HttpSession session_p)
    {
        return (OwView) session_p.getAttribute(LOGIN_VIEW_KEY);
    }

    /**
     * Get the context for current session.
     * Can return null if Session was invalidated, or not associated with an OwAppContext instance.
     * @return OwAppContext or null
     */
    public static OwAppContext getContext(HttpSession session_p)
    {
        return (OwAppContext) session_p.getAttribute(CONTEXT_KEY);
    }

    /** invalidates the session objects, recreates all */
    public static void invalidateSessionObjects(HttpServletRequest request_p)
    {
        // get session object
        HttpSession session = request_p.getSession(false);

        if (session != null)
        {
            session.setAttribute(VIEW_KEY, null);
            session.setAttribute(LOGIN_VIEW_KEY, null);
            session.setAttribute(CONTEXT_KEY, null);

            session.invalidate();
        }
    }

    /** log a session info for analysis
     * 
     * @param request_p HttpServletRequest
     * @param fAllSessions_p true = all sessions, false = only newly created sessions
     */
    public void logSessionInfo(HttpServletRequest request_p, boolean fAllSessions_p)
    {
        HttpSession session = request_p.getSession();

        if (session.isNew() || fAllSessions_p)
        {
            StringBuffer logBuf = new StringBuffer();

            logBuf.append("OW WEB-SESSION INFO ");

            if (session.isNew())
            {
                logBuf.append("(NEW) - ");
            }
            else
            {
                logBuf.append("(OLD) - ");
            }

            logBuf.append(" ID: ");
            logBuf.append(session.getId());
            logBuf.append(" CreateTime: ");
            logBuf.append(new java.util.Date(session.getCreationTime()).toString());

            logBuf.append(" LastAccessTime: ");
            logBuf.append(new java.util.Date(session.getLastAccessedTime()).toString());

            logBuf.append(" Age: ");
            logBuf.append((session.getLastAccessedTime() - session.getCreationTime()) / 1000);
            logBuf.append(" [sec.] ");

            logBuf.append(" MaxIntactiveInterval: ");
            logBuf.append(session.getMaxInactiveInterval());
            logBuf.append(" [sec.]");

            try
            {
                OwAppContext ctx = (OwAppContext) session.getAttribute(CONTEXT_KEY);
                logBuf.append(" Login: ");
                logBuf.append(ctx.getSessionDisplayName());
            }
            catch (Exception e)
            {
                // ignore
            }

            if (LOG.isDebugEnabled())
            {
                LOG.debug(logBuf.toString());
            }
        }

    }

    /** creates the session objects if not done already 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     * @param context_p ServletContext
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     */
    public void checkSessionObjects(ServletContext context_p, HttpServletRequest request_p, HttpServletResponse response_p, HttpSession session_p) throws Exception
    {
        OwAppContext context = (OwAppContext) session_p.getAttribute(CONTEXT_KEY);

        // check if session objects have been created yet
        if (context == null)
        {
            // === create main Context
            context = (OwAppContext) m_ContextClass.newInstance();

            // set the request parameters before init
            context.setRequest(context_p, request_p, response_p);
            // init the context before login
            context.init(m_Context);

            // keep it alive in the session
            session_p.setAttribute(CONTEXT_KEY, context);

            // === create login view
            m_LoginView = (OwView) m_LoginClass.newInstance();
            // keep it alive in the session
            session_p.setAttribute(LOGIN_VIEW_KEY, m_LoginView);
            m_LoginView.attach(context, OwAppContext.LOGIN_VIEW_TARGET_ID);

            // === main view will be created upon login
            m_MainView = null;
            session_p.setAttribute(VIEW_KEY, null);
        }
        else
        {
            // set the request parameters before init
            context.setRequest(context_p, request_p, response_p);

            m_MainView = (OwView) session_p.getAttribute(VIEW_KEY);
            m_LoginView = (OwView) session_p.getAttribute(LOGIN_VIEW_KEY);
        }

        // set as new context
        m_Context = context;
    }

    /** Main request handler for all AJAX based requests
     * <br />
     *
     * @param context_p ServletContext
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     * 
     * @throws Exception If any errors occurred
     */
    public void handleAjaxRequest(ServletContext context_p, HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        StopWatch handleAjaxRequestStopWatch = new Log4JStopWatch("handleAjaxRequest");
        try
        {
            request_p.setAttribute(OwAppContext.AJAX_REQUEST_TYPE, Boolean.TRUE);
            // === make sure session objects are created
            checkSessionObjects(context_p, request_p, response_p, request_p.getSession());
            if (m_Context != null && m_Context.getUserInfo() != null)
            {
                NDC.push(m_Context.getUserInfo().getUserName());
                // set the request parameters before init
                m_Context.setAjaxRequest(true);
                m_Context.handleAjaxRequest(request_p, response_p);
                m_Context.setAjaxRequest(false);
            }
        }
        catch (Exception e)
        {
            LOG.error("AJAX processing failed!", e);
            throw e;
        }
        finally
        {
            NDC.pop();
            NDC.remove();
            handleAjaxRequestStopWatch.stop();
        }
    }

    /** signal that request has started to prevent concurrent requests.
     * @throws OwPendingSessionException 
     */
    private void beginRequest(HttpSession session_p) throws OwPendingSessionException
    {
        OwAppContext context = (OwAppContext) session_p.getAttribute(CONTEXT_KEY);

        if (context != null)
        {
            context.beginRequest();
        }
    }

    /** Main request handler for all JSP based requests
     * allows only one request at a time
     * <br />
     * 
     * @param context_p ServletContext
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     * 
     * @throws Exception If any errors occurred 
     */
    public void handleRequest(ServletContext context_p, HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // === do request
        // put the whole request in a try block, we want to inform the user of exceptions in a convenient way
        try
        {
            StopWatch handleRequestLogicStopWatch = new Log4JStopWatch("handleRequest(Logic)");

            // === make sure session objects are created
            // set the request parameters upon each request
            checkSessionObjects(context_p, request_p, response_p, request_p.getSession());

            // === signal request begin to prevent concurrent requests
            beginRequest(request_p.getSession());

            // === disable caching at the browser.
            response_p.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
            response_p.setHeader("Pragma", "no-cache"); //HTTP 1.0
            //response.setDateHeader( "Expires", 0 ); //prevents caching at the proxy server
            response_p.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");

            //prepare request (create request prerequisite conditions - ex. time zone)
            if (m_Context.prepareRequest(request_p, response_p))
            {
                m_Context.endRequest();
                return;
            }

            // check if user is now logged in
            if (m_Context.isLogin())
            {
                // put the User Id to log file, use the %x option in your log4j.property file
                NDC.push(m_Context.getUserInfo().getUserName());

                // === handle request
                m_Context.handleRequest(request_p, response_p);
                // === handle logout in a container based authentication environment
                if ((!m_Context.isLogin()) && m_Context.isContainerBasedAuthenticated())
                {
                    // OK, we are no longer logged. So we have been logged out in m_Context.handleRequest
                    // and this login has been container based.
                    // set text messages in the session
                    request_p.getSession().setAttribute(SESSION_KEY_LOGGEDOUT_HEADER, m_Context.localize("ui.OwWebApplication.loggedout.header", "Goodbye"));
                    request_p.getSession().setAttribute(SESSION_KEY_LOGGEDOUT_MESSAGE, m_Context.localize("ui.OwWebApplication.loggedout.message", "You have successfully logged out from Workdesk. You may close this window."));
                    // send a redirect
                    response_p.sendRedirect(m_Context.getBaseURL() + "/loggedout.jsp");
                    // always end the request to prevent concurrent requests
                    m_Context.endRequest();
                    // and we are done
                    return;
                }
            }
            else
            {
                // === no credentials available, user must login first
                if (!m_Context.handleRequest(request_p, response_p))
                {
                    // always end the request to prevent concurrent requests
                    m_Context.endRequest();

                    return; // finish request
                }

                // === is user logged in now?
                if (m_Context.isLogin() && m_MainView == null)
                {
                    NDC.push(m_Context.getUserInfo().getUserName());
                    // === notify the context that the user is logged in now
                    m_Context.loginInit();

                    // === create main view with all application objects right after login
                    /*NOTE: We must not create the main view before we have valid credentials, 
                     *      because the main view creates all the application objects,
                     *      which might need valid credentials. */
                    m_MainView = (OwView) m_ViewClass.newInstance();
                    // keep it alive in the session
                    request_p.getSession().setAttribute(VIEW_KEY, m_MainView);
                    m_MainView.attach(m_Context, OwAppContext.MAIN_VIEW_TARGET_ID);

                    if (m_Context.isExternalTargetRequest(request_p))
                    {//we have a remote link to process
                        m_Context.handleExternalTargetRequest(request_p, response_p);
                    }
                }
            }
            // === again make sure session objects are created, somebody might have called invalidateSessionObjects
            checkSessionObjects(context_p, request_p, response_p, request_p.getSession());

            handleRequestLogicStopWatch.stop();

            StopWatch handleRequestRenderStopWatch = new Log4JStopWatch("handleRequest(Render)");
            // === render through view page
            RequestDispatcher rd = context_p.getRequestDispatcher("/" + m_strMainJspPage);
            rd.include(request_p, response_p);

            handleRequestRenderStopWatch.stop();
            // flush in main.jsp if necessary
            //response_p.flushBuffer();

            // === always end the request to prevent concurrent requests
            m_Context.endRequest();
        }
        catch (OwPendingSessionException e2)
        {
            if (LOG.isInfoEnabled())
            {
                // ignore requests when previous request is pending
                LOG.info("OwWebApplication.handleRequest: OwPendingSessionException, user clicked too fast, ignore second request.");
            }
            handleRequestPendingSessionException(context_p, request_p, response_p);
        }
        catch (SocketException e)
        {
            // catch SocketException for double click with Bea WebLogic
            // because rd.include(request_p,response_p); throws a SocketException
            // we ignore this and do not invalidate Session.
            if (m_Context != null)
            { // always end the request to prevent concurrent requests
                m_Context.endRequest();
            }

        }
        catch (Throwable e)
        {
            if (m_Context != null)
            {
                // always end the request to prevent concurrent requests
                m_Context.endRequest();
            }

            if (LOG.isDebugEnabled())
            {
                LOG.error("OwWebApplication.handleRequest: Request error, Thread = " + Thread.currentThread().getName() + ", Error = " + e.getMessage(), e);
            }

            printHandleRequestException(response_p.getWriter(), request_p.getRequestURI(), e, context_p);

            invalidateSessionObjects(request_p);
        }
        finally
        {
            NDC.pop();
            NDC.remove();
        }
    }

    /**
     * Prints the error page in case of an concurrent session to the given <code>PrintWriter</code>.<br>
     * Invoked by <code>{@link #handleRequest(ServletContext, HttpServletRequest, HttpServletResponse)}</code>.
     * 
     * @param out_p the <code>PrintWriter</code> to write the error page to

     * @deprecated since 4.1.1.0 replaced with <code>{@link #handleRequestPendingSessionException(ServletContext, HttpServletRequest, HttpServletResponse)}</code>
     */
    protected void printHandleRequestPendingSessionException(PrintWriter out_p, OwAppContext context_p)
    {
        out_p.write("<html><head>");
        // if JavaScript is disabled in the browser use meta tag
        out_p.write("<meta http-equiv=\"refresh\" content=\"2\">");
        out_p.write("</head>");
        out_p.write("<body>");
        out_p.write(context_p.localize("ui.OwWebApplication.concurrentrequest", "Server is still busy with current request, please wait ..."));
        out_p.write("</body>");
        out_p.write("</html>");
        out_p.flush();
    }

    /**
     * Forward to serverBusy.jsp in case of an concurrent session.<br>
     * Invoked by <code>{@link #handleRequest(ServletContext, HttpServletRequest, HttpServletResponse)}</code>.

     * @param srvContext ServletContext
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
    
     * @since 4.1.1.0 
     */
    protected void handleRequestPendingSessionException(ServletContext srvContext, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        RequestDispatcher dispatcher = request.getRequestDispatcher(m_Context.getDesignDir() + "/serverBusy.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Prints the error page in case of an Exception to the given <code>PrintWriter</code>.<br>
     * Invoked by <code>{@link #handleRequest(ServletContext, HttpServletRequest, HttpServletResponse)}</code>.
     * 
     * @param out_p the <code>PrintWriter</code> to write the error page to
     * @param resetUri_p URL where to jump for reset
     * @param t_p Exception to print out
     * 
     * @throws IOException write error
     */
    protected void printHandleRequestException(PrintWriter out_p, String resetUri_p, Throwable t_p) throws IOException
    {
        printHandleRequestException(out_p, resetUri_p, t_p, null);
    }

    /**
     * Prints the error page in case of an Exception to the given <code>PrintWriter</code>.<br>
     * Invoked by <code>{@link #handleRequest(ServletContext, HttpServletRequest, HttpServletResponse)}</code>.
     * 
     * @param out_p the <code>PrintWriter</code> to write the error page to
     * @param resetUri_p URL where to jump for reset
     * @param t_p Exception to print out
     * @param context_p ServletContext
     * 
     * @throws IOException write error
     */
    protected void printHandleRequestException(PrintWriter out_p, String resetUri_p, Throwable t_p, ServletContext context_p) throws IOException
    {
        Locale locale = Locale.getDefault();
        if (m_Context != null)
        {
            locale = m_Context.getLocale();
        }
        out_p.write("<html><head>\n");
        out_p.write("<title>" + OwString.localize(locale, "app.OwMainAppContext.apptitle", "Alfresco Workdesk") + "</title>\n\n");
        out_p.write("<style type=\"text/css\">");
        out_p.write("body {\n");
        out_p.write("background-color: #DADBFE;\n");
        out_p.write("font: normal 12px Arial, Helvetica, sans-serif;\n");
        out_p.write("}\n");

        out_p.write("a.OwErrorStack {\n");
        out_p.write("font-weight: bold;\n");
        out_p.write("display: block;\n");
        out_p.write("background-color: #FFF7B0;\n");
        out_p.write("color: #cc0000;\n");
        out_p.write("border-top: 1px solid #8f9bac;\n");
        out_p.write("border-bottom: 1px solid #787895;\n");
        out_p.write("padding: 4px 10px;\n");
        out_p.write("}\n");

        out_p.write("</style>\n");

        out_p.write("<script language=\"JavaScript\" type=\"text/javascript\">\n");
        out_p.write("function toggleElementDisplay(id)\n");
        out_p.write("{\n");
        out_p.write("   var details = document.getElementById(id);\n");
        out_p.write("   if ( details )\n");
        out_p.write("   {\n");
        out_p.write("       if (details.style.display == 'none')\n");
        out_p.write("       {\n");
        out_p.write("           details.style.display = '';\n");
        out_p.write("       }\n");
        out_p.write("       else\n");
        out_p.write("       {\n");
        out_p.write("           details.style.display = 'none';\n");
        out_p.write("       }\n");
        out_p.write("   }\n");
        out_p.write("}\n");
        out_p.write("</script>\n");
        out_p.write("</head><body>\n\n");

        out_p.write("<BR>\n\n");
        out_p.write("<B>" + OwString.localize(locale, "app.OwMainAppContext.apptitle", "Alfresco Workdesk") + "</B> - Handle Request Exception...");
        out_p.write("<BR><BR>\n\n");

        out_p.write("<a href=\"");
        OwHTMLHelper.writeSecureHTML(out_p, resetUri_p);
        out_p.write("\">" + OwString.localize(locale, "ui.OwWebApplication.reset", "Reset") + "</a>");

        out_p.write("\n<br><br><br>\n");

        out_p.write("<b>Request Error:</b><br>");

        OwExceptionManager.PrintCatchedException(locale, t_p, out_p, "OwErrorStack");

        out_p.write("\n<br>\n");

        if (context_p != null && context_p.getAttribute(OwInitialisingContextListener.STARTUP_ERROR_KEY) != null)
        {
            out_p.write("<br>");
            out_p.write("<b>Startup / Configuration Error</b><br>");
            out_p.write("\n\n");
            OwExceptionManager.PrintCatchedException(locale, (Exception) context_p.getAttribute(OwInitialisingContextListener.STARTUP_ERROR_KEY), out_p, "OwErrorStack", OwExceptionManager.OWEXCEPTION_ELEMENT_ID + "1");
        }

        out_p.write("</body>\n");
        out_p.write("</html>");
        out_p.flush();
    }

    /**
     * @return the m_Context
     * @since 4.1.1.0
     */
    public OwAppContext getContext()
    {
        return m_Context;
    }
}