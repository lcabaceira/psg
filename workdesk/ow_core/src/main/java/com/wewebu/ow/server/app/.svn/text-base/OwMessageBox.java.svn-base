package com.wewebu.ow.server.app;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Generic Message Box with OK and Cancel button.
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
public class OwMessageBox extends OwStandardDialog
{
    private static final String NO_EVENT = "No";
    private static final String CANCEL_EVENT = "Cancel";
    /**the yes event*/
    private static final String YES_EVENT = "Yes";
    /**the ok event*/
    private static final String OK_EVENT = "OK";
    /** Logger for this class */
    private static final Logger LOG = OwLogCore.getLogger(OwMessageBox.class);
    // === type of message box buttons
    /** type of messagebox to create */
    public static final int TYPE_OK = 0;
    /** type of messagebox to create */
    public static final int TYPE_CANCEL = 1;
    /** type of messagebox to create */
    public static final int TYPE_OK_CANCEL = 2;
    /** type of messagebox to create */
    public static final int TYPE_YES_NO = 3;
    /** type of messagebox to create */
    public static final int TYPE_YES_NO_CANCEL = 4;

    // === type of message box icon / behavior 
    /** icon type of messagebox to create */
    public static final int ICON_TYPE_QUESTION = 0;
    /** icon type of messagebox to create */
    public static final int ICON_TYPE_EXCLAMATION = 1;
    /** icon type of messagebox to create */
    public static final int ICON_TYPE_WARNING = 2;
    /** icon type of messagebox to create */
    public static final int ICON_TYPE_INFO = 3;

    /** action style  link - render yes,no,ok or cancel as anchors */
    public static final int ACTION_STYLE_LINK = 1;
    /** action style  link - render yes,no,ok or cancel as buttons */
    public static final int ACTION_STYLE_BUTTON = 2;

    /** type of message box buttons */
    private int m_type;
    /** action rendering mode - one of {@link #ACTION_STYLE_LINK} or {@link #ACTION_STYLE_BUTTON}*/
    private int m_actionStyle;
    /** text to be displayed */
    private String m_strText;
    /** icon type*/
    private int m_icontype;

    /** construct a message box with given type 
     * @param type_p int type of message box buttons
     * @param icontype_p int type of message box icon / behavior 
     * @param strTitle_p String 
     * @param strText_p String 
     */
    public OwMessageBox(int type_p, int icontype_p, String strTitle_p, String strText_p)
    {
        this(type_p, ACTION_STYLE_LINK, icontype_p, strTitle_p, strText_p);
    }

    /** construct a message box with given type 
     * @param type_p int type of message box buttons
     * @param icontype_p int type of message box icon / behavior 
     * @param actionStyle_p link render style (one of {@link #ACTION_STYLE_LINK} or {@link #ACTION_STYLE_BUTTON}).
     *                      if invalid value {@link #ACTION_STYLE_LINK} is used 
     * @param strTitle_p String 
     * @param strText_p String 
     */
    public OwMessageBox(int type_p, int actionStyle_p, int icontype_p, String strTitle_p, String strText_p)
    {
        if (actionStyle_p == ACTION_STYLE_LINK || actionStyle_p == ACTION_STYLE_BUTTON)
        {
            m_actionStyle = actionStyle_p;
        }
        else
        {
            m_actionStyle = ACTION_STYLE_LINK;
        }

        m_type = type_p;
        m_icontype = icontype_p;
        m_strText = strText_p;

        setTitle(strTitle_p);
    }

    public void init() throws Exception
    {
        switch (m_icontype)
        {
            case ICON_TYPE_QUESTION:
                setInfoIcon(getContext().getDesignURL() + "/images/OwMessageBox/iconquestion.png");
                break;
            case ICON_TYPE_EXCLAMATION:
                setInfoIcon(getContext().getDesignURL() + "/images/OwMessageBox/iconexclamation.png");
                break;
            case ICON_TYPE_WARNING:
                setInfoIcon(getContext().getDesignURL() + "/images/OwMessageBox/iconwarning.png");
                break;
            case ICON_TYPE_INFO:
                setInfoIcon(getContext().getDesignURL() + "/images/OwMessageBox/iconinfo.png");
                break;
        }

    }

    /** render only a region in the view, used by derived classes
    *
    * @param w_p Writer object to write HTML to
    * @param iRegion_p ID of the region to render
    */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case MAIN_REGION:
                w_p.write("<div class=\"OwMessageBoxText\" >" + m_strText + "</div>");

                w_p.write("<div class=\"OwMessageBoxButton\" >");

                switch (m_type)
                {
                    case TYPE_OK_CANCEL:
                    {
                        String okEvent = OK_EVENT;
                        String okEventDisplayName = getContext().localize("app.OwMessageBox.ok", "&nbsp;&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;&nbsp;");
                        String cancelEvent = CANCEL_EVENT;
                        String cancelEventDisplayName = getContext().localize("app.OwMessageBox.cancel", "Cancel");

                        w_p.write(createEvent(okEvent, okEventDisplayName));
                        w_p.write("&nbsp;&nbsp;");
                        w_p.write(createEvent(cancelEvent, cancelEventDisplayName));

                        break;
                    }

                    case TYPE_OK:
                    {
                        String okEvent = OK_EVENT;
                        String okEventDisplayName = getContext().localize("app.OwMessageBox.ok", "&nbsp;&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;&nbsp;");

                        w_p.write(createEvent(okEvent, okEventDisplayName));

                        break;
                    }
                    case TYPE_CANCEL:
                    {
                        String cancelEvent = CANCEL_EVENT;
                        String cancelEventDisplayName = getContext().localize("app.OwMessageBox.cancel", "Cancel");

                        w_p.write(createEvent(cancelEvent, cancelEventDisplayName));

                        break;
                    }

                    case TYPE_YES_NO_CANCEL:
                    {
                        String yesEvent = YES_EVENT;
                        String yesEventDisplayName = getContext().localize("app.OwMessageBox.yes", "&nbsp;&nbsp;&nbsp;&nbsp;Yes&nbsp;&nbsp;&nbsp;&nbsp;");
                        String noEvent = NO_EVENT;
                        String noEventDisplayName = getContext().localize("app.OwMessageBox.no", "&nbsp;&nbsp;&nbsp;&nbsp;No&nbsp;&nbsp;&nbsp;&nbsp;");
                        String cancelEvent = CANCEL_EVENT;
                        String cancelEventDisplayName = getContext().localize("app.OwMessageBox.cancel", "Cancel");

                        w_p.write(createEvent(yesEvent, yesEventDisplayName));
                        w_p.write("&nbsp;&nbsp;");
                        w_p.write(createEvent(noEvent, noEventDisplayName));
                        w_p.write("&nbsp;&nbsp;");
                        w_p.write(createEvent(cancelEvent, cancelEventDisplayName));

                        break;
                    }

                    case TYPE_YES_NO:
                    {
                        String yesEvent = YES_EVENT;
                        String yesEventDisplayName = getContext().localize("app.OwMessageBox.yes", "&nbsp;&nbsp;&nbsp;&nbsp;Yes&nbsp;&nbsp;&nbsp;&nbsp;");
                        String noEvent = NO_EVENT;
                        String noEventDisplayName = getContext().localize("app.OwMessageBox.no", "&nbsp;&nbsp;&nbsp;&nbsp;No&nbsp;&nbsp;&nbsp;&nbsp;");

                        w_p.write(createEvent(yesEvent, yesEventDisplayName));
                        w_p.write("&nbsp;&nbsp;");
                        w_p.write(createEvent(noEvent, noEventDisplayName));
                        break;
                    }
                }

                w_p.write("</div>");
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    private String createEvent(String eventName_p, String displayName_p)
    {
        String action;
        String eventURL = getEventURL(eventName_p, null);
        if (m_actionStyle == ACTION_STYLE_LINK)
        {

            action = "<a class=\"OwMessageBoxButton\" href=\"" + eventURL + "\" ";
            action = addCancelBubbleSupport(eventName_p, action);
            action = action + ">" + displayName_p + "</a>";
        }
        else
        {
            action = "<input type=\"button\" value=\"" + displayName_p + "\" name=\"" + eventName_p + "\" onclick=\"document.location='" + eventURL + "'\" ";
            action = addCancelBubbleSupport(eventName_p, action);
            action = action + "/>";
        }
        if (YES_EVENT.equalsIgnoreCase(eventName_p) || OK_EVENT.equalsIgnoreCase(eventName_p))
        {
            try
            {
                getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, eventURL, null, displayName_p);
            }
            catch (Exception e)
            {
                LOG.error("OwMessageBox.createEvent: Cannot register key for event: " + eventName_p, e);
            }
        }

        return action;
    }

    /**
     * Add support for cancel bubble.
     * @param eventName_p
     * @param action_p
     * @return the cancel bubble HTML code.
     * @since 2.5.2.0
     */
    private String addCancelBubbleSupport(String eventName_p, String action_p)
    {
        if (NO_EVENT.equalsIgnoreCase(eventName_p) || CANCEL_EVENT.equalsIgnoreCase(eventName_p))
        {
            action_p = action_p + " onkeydown='event.cancelBubble=true' ";
        }
        return action_p;
    }

    /** called when user pressed the OK button
     */
    public void onOK(HttpServletRequest request_p) throws Exception
    {
        onOK();
        closeDialog();
    }

    /** called when user pressed the Cancel button
     */
    public void onCancel(HttpServletRequest request_p) throws Exception
    {
        onCancel();
        closeDialog();
    }

    /** called when user pressed the Yes anchor
     */
    public void onYes(HttpServletRequest request_p) throws Exception
    {
        onYes();
        closeDialog();
    }

    /** called when user pressed the No button
     */
    public void onNo(HttpServletRequest request_p) throws Exception
    {
        onNo();
        closeDialog();
    }

    /** overridable */
    public void onOK() throws Exception
    { /* do nothing, to be overwritten */
    }

    /** overridable */
    public void onCancel() throws Exception
    { /* do nothing, to be overwritten */
    }

    /** overridable */
    public void onYes() throws Exception
    { /* do nothing, to be overwritten */
    }

    /** overridable */
    public void onNo() throws Exception
    { /* do nothing, to be overwritten */
    }

}