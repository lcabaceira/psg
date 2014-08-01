package com.wewebu.ow.server.plug.owemailsend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owremote.OwRemoteException;
import com.wewebu.ow.server.plug.owremote.OwRemoteLinkBuilder;
import com.wewebu.ow.server.plug.owremote.OwRemoteLinkConfiguration;
import com.wewebu.ow.server.servlets.OwMultifileDownload;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * eMail creation function plugin.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 * @since 3.2.0.0
 *</font></p>
 */
public class OwEmailCreationFunction extends OwDocumentFunction
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwEmailCreationFunction.class);

    /**  mail message body    */
    public static final String MESSAGE_BODY_PARAM = "msg_body";

    private static final String EMAIL_BODY = "EmailBody";

    private static final String LANG = "lang";

    private static final String EMAIL_SUBJECT = "EmailSubject";

    private static final String MAX_ATTACHMENTS = "MaxAttachments";

    private static final String EMAIL_ATTACHMENT = "EmailAttachment";

    private static final String MAX_LINKS = "MaxLinks";

    private static final String EMAIL_LINK = "EmailLink";

    /** how many links you can send per email */
    private int m_maxLinks = 0;

    /** how many attachments you can send per email */
    private int m_maxAttachments = 0;

    /** use latest document version in remote links */
    private boolean m_alwaysUseLatestVersion = false;

    /** link enabled */
    private boolean m_linkSendEnabled = false;

    /** attachment enabled */
    private boolean m_attachmentSendEnabled = false;

    /** get the URL to the info icon
    * @return String URL
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocemailattachement/sendattachement.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocemailattachement/sendattachement_24.png");
    }

    /**
     * read configuration
     * @throws Exception
     */
    private void initLinkFct() throws Exception
    {
        List<?> props = null;
        OwXMLUtil enabledDocumentFunctionsUtil = getConfigNode().getSubUtil(EMAIL_LINK);

        if (enabledDocumentFunctionsUtil != null)
        {
            m_linkSendEnabled = enabledDocumentFunctionsUtil.getSafeBooleanAttributeValue("enable", false);
            if (m_linkSendEnabled)
            {
                props = enabledDocumentFunctionsUtil.getSafeNodeList();

                Iterator<?> it = props.iterator();
                while (it.hasNext())
                {
                    OwXMLUtil propertyConfig = new OwStandardXMLUtil((Node) it.next());
                    String node = propertyConfig.getNode().getNodeName();

                    if (node.compareTo(MAX_LINKS) == 0)
                    {
                        m_maxLinks = Integer.valueOf(propertyConfig.getSafeTextValue(null));
                    }

                    if (node.compareTo("AlwaysUseLatestVersion") == 0)
                    {
                        m_alwaysUseLatestVersion = Boolean.valueOf(propertyConfig.getSafeTextValue(null));
                    }
                }
            }
        }

    }

    /**
     * read configuration
     * @throws Exception
     */
    private void initAttachmentFct() throws Exception
    {
        List<?> props = null;
        OwXMLUtil enabledDocumentFunctionsUtil = getConfigNode().getSubUtil(EMAIL_ATTACHMENT);

        if (enabledDocumentFunctionsUtil != null)
        {
            m_attachmentSendEnabled = enabledDocumentFunctionsUtil.getSafeBooleanAttributeValue("enable", false);
            if (m_attachmentSendEnabled)
            {
                props = enabledDocumentFunctionsUtil.getSafeNodeList();

                Iterator<?> it = props.iterator();
                while (it.hasNext())
                {
                    OwXMLUtil propertyConfig = new OwStandardXMLUtil((Node) it.next());
                    String node = propertyConfig.getNode().getNodeName();
                    if (node.compareTo(MAX_ATTACHMENTS) == 0)
                    {
                        m_maxAttachments = Integer.valueOf(propertyConfig.getSafeTextValue(null));
                    }
                }
            }
        }
    }

    /**
     *  isSendLinksEnabled
     * @return true is send link function is enabled
     */
    private boolean isSendLinksEnabled()
    {
        return m_linkSendEnabled;
    }

    /**
     *
     * @return true is send attachments function is enabled
     */
    private boolean isSendAttachmentsEnabled()
    {
        return m_attachmentSendEnabled;
    }

    /**
    *
    * @return true for use always latest version in remote links
    */
    private boolean isAlwaysUseLatestVersion()
    {
        return m_alwaysUseLatestVersion;
    }

    /**
     * get localized mail subject
     * @return string localized mail subject
     * @throws Exception
     */
    private String getCustomEmailSubject() throws Exception
    {
        String localizedSubject = getContext().localize("server.plug.owdocsend.subject", "E-Mail with attachment exported from Workdesk");
        Node subNode = null;

        try
        {
            subNode = getConfigNode().getSubNode(EMAIL_SUBJECT);
        }
        catch (Exception e)
        {
            subNode = null;
        }
        if (subNode != null)
        {
            Collection<?> propertyConfigNodes = getConfigNode().getSafeNodeList(EMAIL_SUBJECT);
            Iterator<?> it = propertyConfigNodes.iterator();
            while (it.hasNext())
            {
                OwXMLUtil propertyConfig = new OwStandardXMLUtil((Node) it.next());

                Locale locale = getContext().getLocale();
                localizedSubject = propertyConfig.getSafeStringAttributeValue(LANG, locale.getLanguage());
                if (localizedSubject.compareTo(locale.getLanguage()) == 0)
                {
                    localizedSubject = propertyConfig.getSafeTextValue(null);
                    return localizedSubject;

                }
            }
        }
        return localizedSubject;

    }

    /**
     *
     * @return maximum number of attachments that can be send
     */
    private int getMaxAttachments()
    {
        return m_maxAttachments;
    }

    /**
     *
     * @return maximum number of links that can be send
     */
    private int getMaxLinks()
    {
        return m_maxLinks;
    }

    /**
     * get customized body content
     * @return string localized mail subject
     * @throws Exception
     */
    private String getCustomEmailBody() throws Exception
    {

        String localizedEmailBody = getContext().localize("server.plug.owdocsend.body", "See attached file...");

        Node subNode = null;
        try
        {
            subNode = getConfigNode().getSubNode(EMAIL_BODY);
        }
        catch (Exception e)
        {
            subNode = null;
        }
        if (subNode != null)
        {
            Collection<?> propertyConfigNodes = getConfigNode().getSafeNodeList(EMAIL_BODY);
            Iterator<?> it = propertyConfigNodes.iterator();
            while (it.hasNext())
            {
                OwXMLUtil propertyConfig = new OwStandardXMLUtil((Node) it.next());

                Locale locale = getContext().getLocale();
                localizedEmailBody = propertyConfig.getSafeStringAttributeValue(LANG, locale.getLanguage());
                if (localizedEmailBody.compareTo(locale.getLanguage()) == 0)
                {
                    localizedEmailBody = propertyConfig.getSafeTextValue(null);
                    return localizedEmailBody;

                }
            }
        }
        return localizedEmailBody;

    }

    /**
     * initialize plugin
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        //read configuration
        initAttachmentFct();
        initLinkFct();

    }

    /** check if function is enabled for the given object parameters (called only for single select operations)
    *
    *  @param object_p OwObject where event was triggered
    *  @param parent_p Parent which listed the Object
    *  @param context_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */

    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        if (!super.isEnabled(object_p, parent_p, context_p))
        {
            return false;
        }
        int objectType = object_p.getType();

        if ((objectType == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)) //|| (objectType != OwObjectReference.OBJECT_TYPE_DOCUMENT))// || !object_p.hasContent(context_p))
        {
            return false;
        }

        return true;
    }

    /** event called when user clicked the plugin label / icon
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param refreshCtx_p OwClientRefreshContext call back interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        //don't use isEnabled, check if object type and object class is supported
        if (!isObjectTypeSupported(oObject_p.getType()) || !isObjectClassSupported(oObject_p.getObjectClass().getClassName()))
        {
            throw new OwInvalidOperationException(getContext().localize1("", "Object '%1' is not a document which can be send by e-mail.", oObject_p.getClassName()));
        }

        //don't use isEnabled, check if object is versionable
        int objectType = oObject_p.getType();

        if (objectType == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
        {
            throw new OwInvalidOperationException(getContext().localize1("", "Object '%1' is not a document which can be send by e-mail.", oObject_p.getClassName()));
        }

        Collection<OwObject> coll_Objects_p = new ArrayList<OwObject>();
        coll_Objects_p.add(oObject_p);
        this.onMultiselectClickEvent(coll_Objects_p, oParent_p, refreshCtx_p);
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext call back interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        if (isSendAttachmentsEnabled())
        {
            addAttachments(objects_p, oParent_p, refreshCtx_p);
        }
        StringBuilder mailBody = new StringBuilder();
        //create mail body

        mailBody.append(getCustomEmailBody());
        mailBody.append("\n\n");

        if (isSendLinksEnabled())
        {
            mailBody.append(linkBuilder(objects_p, oParent_p));
        }

        String strScript_p = createJScript(mailBody.toString(), getCustomEmailSubject());

        this.m_MainContext.addFinalScript(strScript_p);

        addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);

    }

    /**
     * Create the JavaScript needed to open the email client
     * @return the JavaScript as <code>String</code>
     * @throws Exception
     */
    private String createJScript(String mailBody_p, String mailSubject_p) throws Exception
    {
        StringBuilder appletDialogURL = new StringBuilder();
        appletDialogURL.append(getContext().getDesignURL() + "/OwSendMail.jsp");
        StringBuilder scriptBuilder = new StringBuilder();

        scriptBuilder.append("canAttach='");
        scriptBuilder.append(isSendAttachmentsEnabled());
        scriptBuilder.append("';\n");

        scriptBuilder.append("mailSubject='");
        scriptBuilder.append(OwHTMLHelper.encodeJavascriptString(mailSubject_p));
        scriptBuilder.append("';\n");

        scriptBuilder.append("mailBody='");
        scriptBuilder.append(OwHTMLHelper.encodeJavascriptString(mailBody_p));
        scriptBuilder.append("';\n");

        scriptBuilder.append("openCenteredDialogWindow('");
        scriptBuilder.append(appletDialogURL.toString());
        scriptBuilder.append("', 'Attaching' , 200, 80);");
        return scriptBuilder.toString();

    }

    /**
     * add attachments
     * @param objects_p OwObject
     * @param parent_p OwObject parent
     * @param refreshCtx_p Context
     * @throws IOException
     * @throws Exception
     */
    private void addAttachments(Collection<OwObject> objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws IOException, Exception
    {

        // filter objects, which are no documents or don't have content.
        List<OwObject> filesToZip = new ArrayList<OwObject>();

        // sanity check for empty objects
        if (null != objects_p && (objects_p.size() <= getMaxAttachments()))
        {
            for (Iterator<OwObject> iterator = objects_p.iterator(); iterator.hasNext();)
            {
                OwObject object = iterator.next();
                if (!isEnabled(object, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                {
                    String escapedName = OwHTMLHelper.encodeToSecureHTML(object.getName());
                    this.m_MainContext.postMessage(this.m_MainContext.localize1("server.plug.owdocsend.OwSendAsAttachmentFunction.nodocument", "Object %1 is not a Document.", escapedName));
                    LOG.debug("OwSendAsAttachmentFunction.onMultiselectClickEvent: Object " + escapedName + " is not a Document.");
                    continue;
                }

                filesToZip.add(object);
            }
        }
        else
        {
            addHistoryEvent(objects_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwRemoteException(getContext().localize1("plugin.com.wewebu.ow.owdocemailattach.exception.too_many_elements_to_send", "It is not allowed to send more than %1 documents as e-mail attachments.",
                    Integer.toString(getMaxAttachments())));

        }

        //check for valid files
        if (filesToZip == null || filesToZip.size() == 0)
        {
            m_MainContext.postMessage(this.m_MainContext.localize("server.plug.owdocsend.OwSendAsAttachmentFunction.nofile", "No valid files found to download!"));
            LOG.debug("OwSendAsAttachmentFunction.onMultiselectClickEvent: No valid files found to download!");
            return;
        }

        // store filesToZip list into session for servlet.
        (getContext()).getHttpSession().setAttribute(OwMultifileDownload.ATTRIBUTE_NAME_FILES_TO_DOWNLOAD, filesToZip);

    }

    /**
     * create mailbody
     * @param objects_p
     * @return mailbody containing links
     * @throws Exception
     */
    private String linkBuilder(Collection<OwObject> objects_p, OwObject oParent_p) throws Exception
    {
        OwMainAppContext appContext_p = getContext();
        // create mail body
        StringBuilder mailBody = new StringBuilder();

        // sanity check for empty objects
        if (null != objects_p && (objects_p.size() <= getMaxLinks()))
        {

            for (Iterator<OwObject> iterator = (objects_p).iterator(); iterator.hasNext();)
            {
                OwObject owObject = iterator.next();

                if (owObject.getType() != OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
                {
                    mailBody.append(owObject.getName());
                    mailBody.append("\n");
                    OwRemoteLinkBuilder builder = new OwRemoteLinkBuilder();
                    OwRemoteLinkConfiguration linkConfiguration = new OwRemoteLinkConfiguration(getConfigNode());
                    mailBody.append(builder.createViewEventURL(appContext_p, linkConfiguration, owObject, true, isAlwaysUseLatestVersion()));
                    mailBody.append("\n\n");
                }
            }
        }
        else
        {
            addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwRemoteException(getContext().localize1("plugin.com.wewebu.ow.owdocemaillink.exception.too_many_elements_to_send", "It is not allowed to send more than %1 documents as e-mail link.", Integer.toString(getMaxLinks())));
        }
        return mailBody.toString();
    }

}