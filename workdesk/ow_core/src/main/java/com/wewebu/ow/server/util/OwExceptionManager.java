package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import javax.servlet.ServletException;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Utility class for displaying exceptions in HTML.
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
public class OwExceptionManager
{
    /**
     * the ID of the HTML element that should be open on click 
     */
    public static final String OWEXCEPTION_ELEMENT_ID = "owexceptiondetails";

    /** display a exception as HTML 
     * @param e_p Throwable to be displayed
     */
    public static String getExceptionDisplayText(Throwable e_p)
    {
        return e_p.getLocalizedMessage();
    }

    /** display a exception as HTML 
     * @param locale_p Locale for localize the messages
     * @param e_p Throwable to be displayed
     * @param w_p Writer for HTML
     * @param strStyleClassName_p style class used to display the error message e.g. OwErrorStack, OwOwWebApplication_EXCEPTION
     */
    public static void PrintCatchedException(java.util.Locale locale_p, Throwable e_p, PrintWriter w_p, String strStyleClassName_p)
    {
        PrintCatchedException(locale_p, e_p, w_p, strStyleClassName_p, OwExceptionManager.OWEXCEPTION_ELEMENT_ID);
    }

    /** display an exception as HTML 
     * @param locale_p Locale for localize the messages
     * @param e_p Throwable to be displayed
     * @param w_p Writer for HTML
     * @param strStyleClassName_p style class used to display the error message e.g. OwErrorStack, OwOwWebApplication_EXCEPTION 
     * @param elementId_p the ID of the HTML element that should be open on click
     */
    public static void PrintCatchedException(java.util.Locale locale_p, Throwable e_p, PrintWriter w_p, String strStyleClassName_p, String elementId_p)
    {
        if (elementId_p == null)
        {
            elementId_p = OwExceptionManager.OWEXCEPTION_ELEMENT_ID;
        }
        // === catch error that occurred during request and was not handled
        if (e_p != null && w_p != null)
        {
            String strMessage = "";
            String strModulName = "";
            Throwable rootCause = null;

            if (e_p instanceof OwException) // handle OwExceptions
            {
                // === print module name
                strModulName = ((OwException) e_p).getModulName();
                strMessage = ((OwException) e_p).getMessage(locale_p);
            }
            else if (e_p instanceof InvocationTargetException) // handle InvocationTargetException
            {
                InvocationTargetException ite = (InvocationTargetException) e_p;

                Throwable targetException = ite;
                while (ite != null && ite.getMessage() == null) // Skip all InvocationTargetException to get the "real" Exception
                {
                    targetException = ite.getTargetException();
                    if (targetException instanceof InvocationTargetException)
                    {
                        ite = (InvocationTargetException) ite.getTargetException();
                    }
                    else
                    {
                        // further working only with the Throwable targetException which can be an Error or an Exception but no InvocationTargetException
                        break;
                    }
                }

                if (targetException != null) // the last InvocationTargetException has an TargetException
                {
                    if (targetException instanceof OwException)
                    {
                        // === print module name
                        strModulName = ((OwException) targetException).getModulName();
                        strMessage = ((OwException) targetException).getMessage(locale_p);
                    }
                    else
                    {
                        strMessage = targetException.getLocalizedMessage();
                        if (strMessage == null || strMessage.length() == 0)
                        {
                            strMessage = targetException.getMessage();
                        }
                    }
                }
                else
                {
                    // the last InvocationTargetException has no TargetException
                    // Try to get the message of the Parent-Exception
                    strMessage = e_p.getLocalizedMessage();
                    if (strMessage == null || strMessage.length() == 0)
                    {
                        strMessage = e_p.getMessage();
                    }
                }
            }
            else if (e_p instanceof ServletException)
            {
                strModulName = "JSP/Servlet Rendering Exception";
                strMessage = e_p.getLocalizedMessage();
                if (strMessage == null || strMessage.length() == 0)
                {
                    strMessage = e_p.getMessage();
                }
                rootCause = ((ServletException) e_p).getRootCause();
            }
            else
            // handle all other Throwable Exceptions then OwException and InvocationTargetException
            {
                strMessage = e_p.getLocalizedMessage();
                if (strMessage == null || strMessage.length() == 0)
                {
                    strMessage = e_p.getMessage();
                }
            }

            if (strMessage == null)
            {
                strMessage = OwString.localize(locale_p, "util.OwExceptionManager.unknownerror", "An unknown error occurred.");
            }

            //bug 3254 - the error message must begin with Error
            String messagePrefix = (new OwString("app.OwUILoginModul.error", "Error:")).getString(locale_p);
            if (messagePrefix != null)
            {
                strMessage = messagePrefix + " " + strMessage;

            }
            String toolTipMessage = OwString.localize(locale_p, "util.OwExceptionManager.TooltipForErrorLink", "Click here in order to display a detailed error message.");

            boolean oldStyle = true;

            if (oldStyle)
            {
                strModulName = OwString.localize1(locale_p, "util.OwExceptionManager.sourcemodul", "Error Source: %1", strModulName);

                w_p.write("\n<a title='" + toolTipMessage + "' href=\"javascript:toggleElementDisplay('" + elementId_p + "');\" class='" + strStyleClassName_p + "'>");
                try
                {
                    OwHTMLHelper.writeSecureHTML(w_p, strMessage);
                }
                catch (IOException e)
                {
                    w_p.write(strMessage);
                }
                w_p.write("</a>");

                w_p.write("\n<pre id='" + elementId_p + "' style='display: none;' class='" + strStyleClassName_p + "'>");

                w_p.write(strModulName + "<br>\n");

                e_p.printStackTrace(w_p);
                while ((rootCause != null))
                {
                    w_p.write("<br><br><b>Root Cause:</b><br><br>\n");
                    rootCause.printStackTrace(w_p);
                    if ((rootCause instanceof ServletException))
                    {
                        rootCause = ((ServletException) rootCause).getRootCause();
                    }
                    else
                    {
                        break;
                    }
                }
                w_p.write("\n</pre>");
            }
            else
            {

                strModulName = OwString.localize1(locale_p, "util.OwExceptionManager.sourcemodul", "Error Source: %1", strModulName);

                w_p.write("\n<a title='" + toolTipMessage + "' href=\"javascript:toggleElementDisplay('" + elementId_p + "');\" class='" + strStyleClassName_p + "'>");
                try
                {
                    OwHTMLHelper.writeSecureHTML(w_p, strMessage);
                }
                catch (IOException e)
                {
                    w_p.write(strMessage);
                }
                w_p.write("</a>");

                w_p.write("\n<pre id='" + elementId_p + "' style='display: none;' class='" + strStyleClassName_p + "'>");

                w_p.write("\n<a title='" + toolTipMessage + "' href=\"javascript:toggleElementDisplay('" + elementId_p + "_full');\"> " + strModulName + " (click here to display the full error StackTrace)</a>");
                w_p.write("\n<pre id='" + elementId_p + "_full' style='display: none;' class='" + strStyleClassName_p + "'>");
                e_p.printStackTrace(w_p);
                while ((rootCause != null))
                {
                    w_p.write("<br><br><b>Root Cause:</b><br><br>\n");
                    rootCause.printStackTrace(w_p);
                    if ((rootCause instanceof ServletException))
                    {
                        rootCause = ((ServletException) rootCause).getRootCause();
                    }
                    else
                    {
                        break;
                    }
                }
                w_p.write("\n</pre>");
                w_p.write("\n<br><br>");

                // === print error cause
                int i = 0;
                w_p.write(" Error caused by:");
                while (e_p != null)
                {
                    String message = e_p.getLocalizedMessage();
                    if (message == null)
                    {
                        message = e_p.getMessage();
                        if (message == null)
                        {
                            message = e_p.toString();
                        }
                    }

                    w_p.write("\n<a title='" + toolTipMessage + "' href=\"javascript:toggleElementDisplay('" + elementId_p + "" + i + "');\" class='" + strStyleClassName_p + "'> -> " + message + "</a>");

                    w_p.write("\n<pre id='" + elementId_p + "" + i + "' style='display: none;' class='" + strStyleClassName_p + "'>");

                    e_p.printStackTrace(w_p);

                    w_p.write("\n</pre>");
                    i++;
                    e_p = e_p.getCause();
                }
                w_p.write("\n</pre>");
            }
        }
    }

    /**
     * Get the localized message from the given exception.
     * @param e_p - the Exception
     * @param locale_p - the user {@link Locale} object
     * @return - the localized message.
     * @since 3.1.0.0
     */
    public static String getLocalizedMessage(Throwable e_p, Locale locale_p)
    {
        String message = null;
        Throwable msgException = e_p;
        if (e_p instanceof InvocationTargetException) // handle InvocationTargetException
        {
            InvocationTargetException ite = (InvocationTargetException) e_p;

            Throwable targetException = ite;
            while (ite != null && ite.getMessage() == null) // Skip all InvocationTargetException to get the "real" Exception
            {
                targetException = ite.getTargetException();
                if (targetException instanceof InvocationTargetException)
                {
                    ite = (InvocationTargetException) ite.getTargetException();
                }
                else
                {// further working only with the Throwable targetException which can be an Error or an Exception but no InvocationTargetException
                    break;
                }
            }

            if (targetException != null)
            {// the last InvocationTargetException has an TargetException
                msgException = targetException;
            }
        }

        if (msgException != null)
        {
            if (msgException instanceof OwException)
            {
                message = ((OwException) msgException).getMessage(locale_p);
            }
            else
            {
                message = msgException.getLocalizedMessage();
                if (message == null || message.length() == 0)
                {
                    message = msgException.getMessage();
                }
            }
        }

        if (message == null)
        {
            message = OwString.localize(locale_p, "util.OwExceptionManager.unknownerror", "An unknown error occurred.");
        }
        //bug 3254 - the error message must begin with Error
        String messagePrefix = (new OwString("app.OwUILoginModul.error", "Error:")).getString(locale_p);
        if (messagePrefix != null)
        {
            message = messagePrefix + " " + message;

        }
        return OwHTMLHelper.encodeJavascriptString(message, true);
    }

    /**
     * Get the relevant stack trace.
     * @param e_p - the exception
     * @return - a formatted {@link String} object, containing the stacktrace.
     * @since 3.1.0.0
     */
    public static String getRelevantStackTrace(Throwable e_p)
    {
        StringWriter result = new StringWriter();
        Throwable theThrowable = e_p;
        Throwable theRootCause = null;
        //theThrowable.printStackTrace(new PrintWriter(result));

        if (theThrowable instanceof InvocationTargetException)
        {
            InvocationTargetException ite = (InvocationTargetException) theThrowable;
            while (ite.getTargetException() != null && (ite.getTargetException() instanceof InvocationTargetException))
            {
                ite = (InvocationTargetException) ite.getTargetException();
            }
            theThrowable = ite;
        }
        else if (theThrowable instanceof ServletException)
        {
            theRootCause = ((ServletException) theThrowable).getRootCause();
        }
        if (theThrowable != null)
        {
            theThrowable.printStackTrace(new PrintWriter(result));
        }
        if (theRootCause != null)
        {
            result.write("<br>root cause: <br>");
            theRootCause.printStackTrace(new PrintWriter(result));
        }

        return OwHTMLHelper.encodeJavascriptString(result.toString());
    }
}