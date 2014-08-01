package com.wewebu.ow.clientservices.applets;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.desktop.Message;
import org.jdesktop.jdic.desktop.internal.impl.WinAPIWrapper;

import com.wewebu.ow.clientservices.utils.OwHttpFileDownload;
import com.wewebu.ow.clientservices.utils.OwProgressBarPanel;
import com.wewebu.ow.clientservices.utils.OwProgressMonitor;

/**
 *<p>
 * File Download Applet, used for e.g. to send documents as email attachment.
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
public class OwFileDownloadApplet extends Applet
{
    private static final Logger LOGGER = Logger.getLogger(OwFileDownloadApplet.class.getName());

    private static final String OUTLOOK_EXPRESS = "Outlook Express";
    private static final String MICROSOFT_OUTLOOK = "Microsoft Outlook";

    private static final long serialVersionUID = -7720871873968263724L;

    private static final String SEPERATOR = ";";
    private String m_httpSession = null;
    private String m_downloadURL = null;

    /** DMSID is passed in case if we have only one file not in zipped form */
    private String m_dmsid = null;

    private OwProgressMonitor m_progressMonitor;

    private OwProgressBarPanel m_progressBarPanel;

    // Messages to be translated and transmitted from outside as applet
    // parameters
    private String msg_exception = "Exception: {0} \nPlease see the Java console.";
    private String m_msgsubject = "E-Mail with attachment exported from Workdesk";
    private String m_msgbody = "See attached file...";

    /** attachment enabled */
    private boolean m_attachmentSendEnabled = false;

    private JSObject jsWin = null;

    public void init()
    {
        m_httpSession = getParameter("jSession");
        m_downloadURL = getParameter("downloadURL");
        m_dmsid = getParameter("dmsid");
        m_msgsubject = getParameter("msg_subject");
        m_msgbody = getParameter("msg_body");
        // read translated messages
        String message = getParameter("msg_exception");
        if (null != message)
        {
            msg_exception = message;
        }

        Object isAttachmentDwnEnabledObj = null;

        jsWin = JSObject.getWindow(this);

        isAttachmentDwnEnabledObj = jsWin.call("isAttachmentDwnEnabled", new Object[] {}); // Call script in HTML page

        m_attachmentSendEnabled = Boolean.parseBoolean((String) isAttachmentDwnEnabledObj);

        if (m_attachmentSendEnabled)
        {
            m_progressMonitor = new OwProgressMonitor();
            setLayout(new BorderLayout());
            m_progressBarPanel = new OwProgressBarPanel(m_progressMonitor);
            add(m_progressBarPanel, BorderLayout.CENTER);

        }

        LOGGER.log(Level.INFO, "Applet initialization finished...");

    }

    /**
     * draw progress bar
     */
    public void paint(Graphics g_p)
    {
        if (!m_attachmentSendEnabled)
        {
            setBackground(Color.gray);
            g_p.drawRect(0, 0, getSize().width, getSize().height);
        }
    }

    public void start()
    {
        super.start();
        download();
    }

    /** called from java script to download flies from Alfresco Workdesk
     *
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void download()
    {
        try
        {
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run()
                {
                    File result = null;
                    try
                    {
                        result = downloadFile();
                    }
                    catch (Throwable e)
                    {
                        logErrorAndClose(e);
                        return null;
                    }
                    return result;
                }
            });
        }
        catch (Exception e)
        {
            logErrorAndClose(e);
        }
    }

    private File downloadFile() throws JSException
    {
        //create mail message
        MailMessage mailmessage = new MailMessage();
        File fileDownloaded = null;
        String downloadedFile = null;

        jsWin = JSObject.getWindow(this);

        if (m_attachmentSendEnabled)
        {
            LOGGER.log(Level.INFO, "Download files...");
            OwHttpFileDownload fileDownload = new OwHttpFileDownload(this.m_downloadURL, this.m_httpSession);

            String acceptInsecureCerts = getParameter("accept_insecure_certificates");
            fileDownload.setIsAcceptInsecureCertificates((acceptInsecureCerts != null ? Boolean.valueOf(acceptInsecureCerts) : false));
            fileDownload.setProgressMonitor(m_progressMonitor);

            if (m_dmsid != null)
            {
                fileDownloaded = fileDownload.downloadFile(m_dmsid);
            }
            else
            {
                fileDownloaded = fileDownload.downloadFile(null);
            }

            if (fileDownloaded != null)
            {
                List<String> attachList = new ArrayList<String>();
                downloadedFile = fileDownloaded.getAbsolutePath();
                attachList.add(downloadedFile);
                mailmessage.setAttachments(attachList);
            }
        }

        if (m_msgsubject == null)
        {
            Object object = jsWin.call("getEmailSubject", null); // Call script in HTML page
            m_msgsubject = (String) object;
        }

        if (m_msgbody == null)
        {
            Object object = jsWin.call("getEmailBody", null); // Call script in HTML page
            m_msgbody = (String) object;
        }

        mailmessage.setSubject(m_msgsubject);
        mailmessage.setBody(m_msgbody);

        if (mailmessage != null)
        {
            String defMailer = getDefaultMailer();

            if (MICROSOFT_OUTLOOK.equalsIgnoreCase(defMailer) || OUTLOOK_EXPRESS.equalsIgnoreCase(defMailer))
            {
                sendMail(mailmessage);
                // the case that Outlook or Outlook Express is the default mailer

                JSObject win = JSObject.getWindow(this);
                win.eval("self.close();");
            }
            else
            {
                // call javascript method to instantiate the email client with attachment
                // it use mailto: protocol, by using this protocol some mail clients does not support attachments
                // due to security constrains
                callJsFuntion("openEmailClient", new String[] { downloadedFile });
            }
        }

        return fileDownloaded;
    }

    /**
     * send mail message
     * 
     * @param message_p
     */
    public void sendMail(MailMessage message_p)
    {
        Message message = new Message();

        List<?> toList = new ArrayList<Object>();
        message.setToAddrs(toList);

        List<?> ccList = new ArrayList<Object>();
        message.setCcAddrs(ccList);

        message.setSubject(message_p.getSubject());

        String body = message_p.getBody();

        message.setBody(body);
        LOGGER.log(Level.INFO, "The mail Body is: \n " + body);

        List<String> attachList = message_p.getAttachments();

        try
        {
            message.setAttachments(attachList);
            Desktop.mail(message);
        }
        catch (IOException e)
        {
            logErrorAndClose(e);
        }
        catch (DesktopException e)
        {
            logErrorAndClose(e);
        }
    }

    protected boolean callJsFuntion(String functionName_p, Object[] param_p)
    {

        if (jsWin == null)
        {
            jsWin = JSObject.getWindow(this);
        }
        jsWin.call(functionName_p, param_p);
        return true;
    }

    public String getSeperator()
    {
        return OwFileDownloadApplet.SEPERATOR;
    }

    public void setSessionToken(String token_p)
    {
        this.m_httpSession = token_p;
    }

    private void logErrorAndClose(Throwable e_p)
    {
        LOGGER.log(Level.SEVERE, "Got exception: ", e_p);
        logErrorAndClose(MessageFormat.format(msg_exception, new Object[] { e_p.getMessage() }));
    }

    /**
     * Log error and close window
     * 
     * @param errorMessage_p
     */
    private void logErrorAndClose(String errorMessage_p)
    {
        String showErrorJS = "showErrorAndClose";
        try
        {
            callJsFuntion(showErrorJS, new String[] { errorMessage_p });
        }
        catch (Exception e1)
        {
            // We are in a very bad position
            e1.printStackTrace();
        }
    }

    /**
     * Get the Windows default system mailer.
     *
     * @return Name of the default system mailer
     */
    private String getDefaultMailer()
    {
        String defaultMailer = null;
        //read from windows registry default mailer
        defaultMailer = WinAPIWrapper.WinRegQueryValueEx(WinAPIWrapper.HKEY_LOCAL_MACHINE, "SOFTWARE\\Clients\\Mail", "");

        return defaultMailer;
    }

    /**
     * Check if the simple MAPI is supported in the current system.
     *
     * @return true if simple MAPI is supported.
     */
    static boolean isMapiSupported()
    {
        String regMapi = WinAPIWrapper.WinRegQueryValueEx(WinAPIWrapper.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows Messaging Subsystem", "MAPI");
        if (regMapi != null)
        {
            if (regMapi.equals("1"))
            {
                return true;
            }
        }
        return false;
    }
}
