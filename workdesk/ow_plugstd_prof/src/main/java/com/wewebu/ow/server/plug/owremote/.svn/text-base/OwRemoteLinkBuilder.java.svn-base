package com.wewebu.ow.server.plug.owremote;

import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.CONTROL_EVENT_OPEN_RECORD_ENCRYPTED;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.CONTROL_EVENT_OPEN_WORKITEM_ENC;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.CONTROL_EVENT_VIEW;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.CONTROL_EVENT_VIEW_CRYPT;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.LATEST_VERSION_PARAMETER_NAME;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.QUERY_KEY_DMSID;
import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.QUERY_KEY_EVENT_NAME;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwSecretEncryption;
import com.wewebu.ow.server.util.paramcodec.OwParameterMap;
import com.wewebu.ow.server.util.paramcodec.OwParameterMapCodec;

/**
 *<p>
 * Helper class to create links for remote control functions. 
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
 *@since 3.2.0.1
 */
public class OwRemoteLinkBuilder
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRemoteLinkBuilder.class);

    /**
     * Create URL/URI for viewer remote invocation, representing/viewing a specific object.
     * @param context_p OwMainAppContext 
     * @param configuration_p OwRemoteLinkConfiguration current remote configuration
     * @param owObject_p OwObject for viewing
     * @param crypt_p boolean should object id be encrypted
     * @param useLatestVersion_p boolean should be the latest version of object retrieved
     * @return String URL/URI for processing
     * @throws OwRemoteException if any error occur durring c
     */
    public String createViewEventURL(OwMainAppContext context_p, OwRemoteLinkConfiguration configuration_p, OwObject owObject_p, boolean crypt_p, boolean useLatestVersion_p) throws OwRemoteException
    {
        try
        {
            StringBuilder eventUrl = new StringBuilder();

            eventUrl.append(context_p.getBaseURL());
            eventUrl.append("?");
            eventUrl.append("owappeid=");
            eventUrl.append(URLEncoder.encode("com.wewebu.ow.RemoteControl.Doc", "UTF-8"));
            eventUrl.append("&");
            OwParameterMap eventParameterMap = new OwParameterMap();

            int objectType = owObject_p.getType();

            if (objectType == OwObjectReference.OBJECT_TYPE_FOLDER)
            {
                //  send folders url
                eventParameterMap.setParameter(configuration_p.linkElementFor(QUERY_KEY_EVENT_NAME), URLEncoder.encode(configuration_p.linkElementFor(CONTROL_EVENT_OPEN_RECORD_ENCRYPTED), "UTF-8"));
            }
            else if (objectType == OwObjectReference.OBJECT_TYPE_WORKITEM)
            {
                //  send workitems eventtypes
                eventParameterMap.setParameter(configuration_p.linkElementFor(QUERY_KEY_EVENT_NAME), URLEncoder.encode(configuration_p.linkElementFor(CONTROL_EVENT_OPEN_WORKITEM_ENC), "UTF-8"));
            }
            else
            {
                eventParameterMap.setParameter(configuration_p.linkElementFor(QUERY_KEY_EVENT_NAME),
                        URLEncoder.encode((crypt_p ? configuration_p.linkElementFor(CONTROL_EVENT_VIEW_CRYPT) : configuration_p.linkElementFor(CONTROL_EVENT_VIEW)), "UTF-8"));
            }

            eventParameterMap.setParameter(configuration_p.linkElementFor(QUERY_KEY_DMSID), URLEncoder.encode((crypt_p ? OwSecretEncryption.bytesToString(OwSecretEncryption.encrypt(owObject_p.getDMSID())) : owObject_p.getDMSID()), "UTF-8"));
            OwParameterMapCodec parameterCodec = OwRemoteControlDocument.getUrlParameterCodec(context_p);
            OwParameterMap encodedParameterMap = parameterCodec.encode(eventParameterMap);
            eventUrl.append(encodedParameterMap.toRequestQueryString());

            if (owObject_p.hasVersionSeries() && useLatestVersion_p)
            {
                eventUrl.append("&");
                eventUrl.append(LATEST_VERSION_PARAMETER_NAME);
                eventUrl.append("=" + useLatestVersion_p);
            }
            return (eventUrl.toString());
        }
        catch (UnsupportedEncodingException e)
        {
            String msg = "UTF-8 encoding for URLs is not available";
            LOG.error(msg, e);
            throw new OwRemoteException(msg, e);
        }
        catch (Exception e)
        {
            String msg = "Error getting DMSID from object";
            LOG.error(msg, e);
            throw new OwRemoteException(msg, e);
        }
    }

    /**
     * Create JavaScript call for object collection to be opened as mailto reference.
     * @param objects_p Collection of objects to handle
     * @param appContext_p OwMainAppContext 
     * @param configuration_p OwRemoteLinkConfiguration current configuration
     * @param location_p String Javascript lcoation parameter associated with mailto call
     * @param useLatestVersion_p boolean use latest version for object refrence
     * @return String
     * @throws OwRemoteException
     */
    @SuppressWarnings("rawtypes")
    public String createJScript(Collection objects_p, OwMainAppContext appContext_p, OwRemoteLinkConfiguration configuration_p, String location_p, boolean useLatestVersion_p) throws OwRemoteException
    {
        // sanity check for empty objects
        if (objects_p == null)
        {
            return "";
        }
        // create mail body
        StringBuffer mailBody = new StringBuffer();
        for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
        {
            OwObject owObject = (OwObject) iterator.next();
            mailBody.append(owObject.getName());
            mailBody.append("\n");
            //            mailBody.append(OwRemoteControlDocument.getViewEventURL(appContext_p, owObject, true, useLatestVersion_p));
            mailBody.append(createViewEventURL(appContext_p, configuration_p, owObject, true, useLatestVersion_p));
            mailBody.append("\n\n");
        }
        // assemble mail to URL
        StringBuffer mailtoUrl = new StringBuffer();
        mailtoUrl.append("mailto:?subject=");
        try
        {
            String encodedSubject = OwAppContext.encodeURL(appContext_p.localize("plugin.com.wewebu.ow.owdocemaillink.mail.subject", "Document Link"));
            mailtoUrl.append(encodedSubject);
        }
        catch (UnsupportedEncodingException uee)
        {
            // append subject unencoded
            mailtoUrl.append(appContext_p.localize("plugin.com.wewebu.ow.owdocemaillink.mail.subject", "Document Link"));
        }
        mailtoUrl.append("&body=");
        try
        {
            String encodedBody = OwAppContext.encodeURL(mailBody.toString());
            mailtoUrl.append(encodedBody);
        }
        catch (UnsupportedEncodingException uee)
        {
            // append body unencoded
            mailtoUrl.append(mailBody.toString());
        }
        // assemble JavaScript command
        StringBuffer javaScriptCommand = new StringBuffer();
        javaScriptCommand.append(location_p);
        javaScriptCommand.append(" = '");
        javaScriptCommand.append(OwHTMLHelper.encodeJavascriptString(mailtoUrl.toString()));
        javaScriptCommand.append("';");
        // return JavaScript command
        return (javaScriptCommand.toString());
    }

}
