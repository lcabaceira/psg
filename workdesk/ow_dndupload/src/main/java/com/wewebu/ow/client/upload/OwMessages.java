package com.wewebu.ow.client.upload;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.JApplet;

/**
 *<p>
 * A bunch of keys used for localization.
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
public class OwMessages
{
    public static final String PREFIX = "upload.Messages.";

    public static final String UPLOAD_MESSAGES_ERRMULTIFILENOTSUPPORTED = "upload.Messages.errMultiFileNotSupported";
    public static final String UPLOAD_MESSAGES_MSGERRGENERAL = "upload.Messages.msgErrGeneral";
    public static final String UPLOA_MESSAGES_ERRTOOMANYFILES = "upload.Messages.errTooManyFiles";
    public static final String UPLOAD_MESSAGES_ERRINVALIDFILESSIZE = "upload.Messages.errInvalidFilesSize";
    public static final String UPLOAD_MESSAGES_ERRINVALIDBATCHSIZE = "upload.Messages.errInvalidBatchSize";
    public static final String UPLOAD_MESSAGES_ERRINVALIDFILES = "upload.Messages.errInvalidFiles";
    public static final String UPLOAD_MESSAGES_QUESTIONSKIPCONTINUE = "upload.Messages.questionSkipContinue";
    public static final String UPLOAD_MESSAGES_ERR_NO_FILES = "upload.Messages.errNoFiles";
    public static final String UPLOAD_MESSAGES_ERRINVALIDFILESEXTENSION = "upload.Messages.errInvalidFilesExtension";
    public static final String UPLOAD_MESSAGES_TLTUPLOAD = "upload.Messages.tltUpload";
    public static final String UPLOAD_MESSAGES_LBLOVERALL = "upload.Messages.lblOverall";
    public static final String UPLOAD_MESSAGES_LBLTIMEESTIMATES = "upload.Messages.lblTimeEstimates";
    public static final String UPLOAD_MESSAGES_LBLUPLOADING = "upload.Messages.lblUploading";
    public static final String UPLOAD_MESSAGES_BTNCLOSE = "upload.Messages.btnClose";
    public static final String UPLOAD_MESSAGES_BTNCANCEL = "upload.Messages.btnCancel";

    public static final String UPLOAD_MESSAGES_ERRUPLOAD = "upload.Messages.errUpload";
    public static final String UPLOAD_MESSAGES_ERRNOFILES = "upload.Messages.errNoFiles";
    public static final String UPLOAD_MESSAGES_ERRFILENOTFOUND = "upload.Messages.errFileNotFound";
    public static final String UPLOAD_MESSAGES_PASTEMENUITEM = "upload.Messages.pasteMenuItem";

    private Properties properties;

    private JApplet applet;

    private OwMessages(Properties properties_p, JApplet applet_p)
    {
        this.properties = properties_p;
        this.applet = applet_p;
    }

    /**
     * Read all applet parameters starting with {@link #PREFIX} and consider them as localization messages.
     * @param applet_p
     * @return an instance of {@link OwMessages} ready for using.
     * @throws IOException
     */
    public static OwMessages loadFrom(JApplet applet_p) throws IOException
    {
        Properties properties = new Properties();
        return new OwMessages(properties, applet_p);
    }

    public String localize(String key_p, String defaultMessage_p, Object... arguments_p)
    {
        String message = this.properties.getProperty(key_p);
        if (null == message)
        {
            // Try to search through the applet's parameters
            message = this.applet.getParameter(key_p);
        }

        if (null == message)
        {
            message = defaultMessage_p;
        }

        if (0 != arguments_p.length)
        {
            message = MessageFormat.format(message, arguments_p);
        }
        return message;
    }
}
