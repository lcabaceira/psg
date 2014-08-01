package com.wewebu.ow.server.plug.owremote;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwMimeManager.OwOpenCommand;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwSecretEncryption;
import com.wewebu.ow.server.util.paramcodec.OwNetworkAttributeBagCodec;
import com.wewebu.ow.server.util.paramcodec.OwParameterMap;
import com.wewebu.ow.server.util.paramcodec.OwParameterMapCodec;
import com.wewebu.ow.server.util.paramcodec.OwTransientCodec;

/**
 *<p>
 * Remote Control Document Master Plugin listener for external requests.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwRemoteControlDocument extends OwMasterDocument implements OwRemoteConstants
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRemoteControlDocument.class);

    /** network */
    private static OwNetwork m_network;

    /**workitem repository interface*/
    private static final String BPM_WORKITEM_REPOSITORY_INTERFACE = "com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository";

    private static final String VIEW_MIME_CONTEXT = "OwObjectEditVersionsView";

    public static synchronized OwParameterMapCodec getUrlParameterCodec(OwMainAppContext mainAppContext_p) throws OwInvalidOperationException
    {
        //are network based attribute bags usable ?

        boolean canUseAttributeBags = false;

        m_network = mainAppContext_p.getNetwork();
        try
        {
            m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, "testBag", "testUser", true, true);
            canUseAttributeBags = true;
        }
        catch (Exception e)
        {
            //attribute bags are not available
            LOG.debug("OwRemoteControlDocument.getUrlParameterCodec(): attribute bags seem to be inaccessible! The OwTransientCodec will be used ...", e);
        }

        if (canUseAttributeBags)
        {
            return OwNetworkAttributeBagCodec.createConfiguredCodec(mainAppContext_p);
        }
        else
        {
            return OwTransientCodec.createConfiguredCodec(mainAppContext_p);
        }
    }

    /** overridable to receive request notifications from external sources / links
     *
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     */
    public void onExternalRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        OwParameterMap requestParameterMap = new OwParameterMap(request_p.getParameterMap());
        OwRemoteLinkConfiguration configuration = new OwRemoteLinkConfiguration(getConfigNode());
        OwMainAppContext context = (OwMainAppContext) getContext();
        OwParameterMapCodec codec = getUrlParameterCodec(context);

        if (codec.canDecode(requestParameterMap))
        {
            try
            {
                requestParameterMap = codec.decode(requestParameterMap, true);
            }
            catch (OwUserOperationException e)
            {
                LOG.debug("OwRemoteControlDocument.onExternalRequest(): user error for  [" + requestParameterMap.toRequestQueryString() + "]. Is this part of an expired url?", e);
                throw new OwUserOperationException(getContext().localize("owremote.OwRemoteControlDocument.expired.url", "The requested URL is no longer valid."), e);
            }
        }
        else
        {
            LOG.debug("OwRemoteControlDocument.onExternalRequest(): non ecoded request detected " + requestParameterMap.toRequestQueryString());
        }

        // === remote control event
        String strEventName = requestParameterMap.getParameter(configuration.linkElementFor(QUERY_KEY_EVENT_NAME));

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwRemoteControlDocument.onExternalRequest: CMD - strEventName=" + strEventName);
        }

        if (null == strEventName)
        {
            LOG.error("OwRemoteControlDocument.onExternalRequest: No event defined.");
            throw new OwRemoteException(getContext().localize("owremote.OwRemoteControlDocument.unrecognized_event", "Unrecognized remote event, please check http link."));
        }

        // === copy all properties and values to clipboard
        copyUpdatePropertiesToClipboard(requestParameterMap);

        boolean decrypt = false;

        if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_RECORD)) || strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_RECORD_ENCRYPTED)))
        {

            if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_RECORD_ENCRYPTED)))
            {
                decrypt = true;
            }
            onOpenRecord(requestParameterMap, decrypt);

        }
        else if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_MODIFY_PROPERTIES)))
        {
            onModifyProperties(requestParameterMap);

        }
        else if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_WORKITEM)) || strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_WORKITEM_ENC)))
        {
            if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_OPEN_WORKITEM_ENC)))
            {
                decrypt = true;
            }
            onOpenWorkItem(context, requestParameterMap, decrypt);

        }
        else if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_SEARCH)))
        {
            onSearch(requestParameterMap);
        }
        else if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_VIEW)) || strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_VIEW_CRYPT)))
        {
            if (strEventName.equals(configuration.linkElementFor(CONTROL_EVENT_VIEW_CRYPT)))
            {
                decrypt = true;
            }

            onOpenURL(response_p, decrypt, requestParameterMap);
        }
        else
        {
            // === unrecognized event
            LOG.error("OwRemoteControlDocument.onExternalRequest: Unrecognized remote command: " + strEventName);
            throw new OwRemoteException(getContext().localize("owremote.OwRemoteControlDocument.unrecognized_event", "Unrecognized remote event, please check http link."));
        }
    }

    private void onOpenURL(HttpServletResponse response_p, boolean decrypt, OwParameterMap parameterMap_p) throws OwRemoteException
    {

        OwObject obj;
        try
        {
            obj = getMappedObject(parameterMap_p, decrypt);
        }
        catch (Exception ex)
        {
            LOG.warn("Object not found, possible reasons: no access rights to open this object, invalid link or the object was already deleted.", ex);
            throw new OwRemoteException(getContext().localize("owremote.OwRemoteControlDocument.object_not_found", "Object not found, possible reasons: no access rights to open this object, invalid link or the object was already deleted."), ex);
        }

        try
        {
            String mimeContext = null;

            String latestVersion = parameterMap_p.getParameter(LATEST_VERSION_PARAMETER_NAME);
            if (latestVersion == null || !Boolean.parseBoolean(latestVersion))
            {
                mimeContext = VIEW_MIME_CONTEXT;
            }

            OwOpenCommand scmd = OwMimeManager.getOpenCommand((OwMainAppContext) getContext(), obj, mimeContext, null);

            if (!scmd.canGetURL())
            {
                if (scmd.canGetScript())
                {
                    String script = scmd.getScript(OwMimeManager.VIEWER_MODE_JAVASCRIPT, null, false);
                    ((OwMainAppContext) getContext()).addFinalScript(script);
                }
                else
                {
                    LOG.error("OwRemoteControlDocument.onOpenURL: Can not open script command.");
                    throw new OwRemoteException("Can not open script command.");
                }
            }
            else
            {

                // redirect browser
                response_p.sendRedirect(response_p.encodeRedirectURL(scmd.getURL()));
            }
        }
        catch (Exception e)
        {
            LOG.error("No viewservlet installed for this object.", e);
            throw new OwRemoteException(getContext().localize("owremote.OwRemoteControlDocument.no_viewservlet", "No viewservlet installed for this object."), e);
        }
    }

    private void onSearch(OwParameterMap requestParameterMap) throws OwRemoteException, Exception
    {
        // === perform a search
        String sTemplateName = requestParameterMap.getParameter(QUERY_KEY_SEARCH_TEMPLATE_NAME);
        String sSearchPluginID = requestParameterMap.getParameter(QUERY_KEY_PLUGIN_ID);
        String sMaxSize = requestParameterMap.getParameter(QUERY_KEY_SEARCH_MAX_SIZE);

        // get the search criteria parameters to set
        Map criterias = getPropertyMapFromQuery(requestParameterMap, QUERY_KEY_PROPERTY_PREFIX);

        OwMasterDocument searchplugin = (OwMasterDocument) getContext().getEventTarget(sSearchPluginID + OwConfiguration.MAIN_PLUGIN_DOCUMENT_ID_EXTENSION);
        if (null == searchplugin)
        {
            LOG.error("OwRemoteControlDocument.onExternalRequest: CONTROL_EVENT_SEARCH: No Masterplugin handler defined for given object in frontcontroller OwRemoteControlDocument.");
            throw new OwRemoteException("No Masterplugin handler defined for given object in frontcontroller OwRemoteControlDocument.");
        }

        // perform search
        searchplugin.dispatch(OwDispatchCodes.OPEN_OBJECT, sTemplateName + "," + sMaxSize, criterias);
    }

    private void onModifyProperties(OwParameterMap requestParameterMap) throws Exception, OwRemoteException
    {
        // === modify properties
        // get the properties and values to set
        Map properties = getPropertyMapFromQuery(requestParameterMap, QUERY_KEY_PROPERTY_PREFIX);

        // get the object
        OwObject obj = ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(this.getDMSIDfromRequest(requestParameterMap, false), true);

        // and its properties to modify
        OwPropertyCollection props = new OwStandardPropertyCollection();

        Iterator it = properties.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String sPropName = (String) entry.getKey();
            Object oValue = entry.getValue();

            // create modified property
            OwProperty newProp = new OwStandardProperty(oValue, obj.getObjectClass().getPropertyClass(sPropName));

            props.put(sPropName, newProp);
        }

        // save changes
        obj.setProperties(props);
    }

    private void onOpenWorkItem(OwMainAppContext context_p, OwParameterMap requestParameterMap_p, boolean useEcryptedDMIS_p) throws OwRemoteException, Exception
    {
        //        OwWorkitemRepository bpmrepo = getBpmRepository();
        //
        //        bpmrepo.getResourceIDs();
        //        bpmrepo.getDMSPrefix();
        //
        //        //  OwBPMRepository.getDMSID(getQueueElement().getWorkObjectNumber(), getResourceID());
        //
        //        OwObject owObject = null;
        //        if (useEcryptedDMIS_p)
        //        {
        //            String strDMSID = this.getBPMDMSIDfromRequest(requestParameterMap_p, useEcryptedDMIS_p);
        //
        //            owObject = bpmrepo.getObjectFromDMSID(strDMSID, true);
        //
        //        }
        //
        //        OwDocumentFunction workitemDocument = OwMimeManager.getHandlerDocumentPlugin((OwMainAppContext) getContext(), owObject);
    }

    /** the bpm repository */
    public OwWorkitemRepository getBpmRepository() throws Exception
    {

        OwWorkitemRepository bpmRepository = null;

        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();

        if (!network.hasInterface(BPM_WORKITEM_REPOSITORY_INTERFACE))
        {
            String msg = "OwRemoteControlDocument.getBpmRepository(): The ECM adapter does not offer an interface for work items repositories. This document function will be disabled!";
            LOG.warn(msg);
        }
        else
        {
            bpmRepository = (OwWorkitemRepository) network.getInterface(BPM_WORKITEM_REPOSITORY_INTERFACE, null);
            if (!bpmRepository.canLaunch())
            {
                String msg = "OwRemoteControlDocument.getBpmRepository(): The configured work item repository does not support the launching workflows. This document function will be disabled!";
                LOG.warn(msg);
                bpmRepository = null;
            }
        }
        return bpmRepository;
    }

    private void onOpenRecord(OwParameterMap requestParameterMap, boolean useEcryptedDMIS_p) throws Exception, OwRemoteException
    {
        OwObject recFolder = getMappedObject(requestParameterMap, useEcryptedDMIS_p);

        OwMasterDocument recordDocument = OwMimeManager.getHandlerMasterPlugin((OwMainAppContext) getContext(), recFolder);
        if (null == recordDocument)
        {
            LOG.error("OwRemoteControlDocument.onExternalRequest: CONTROL_EVENT_OPEN_RECORD: No Masterplugin handler defined for given object in frontcontroller OwRemoteControlDocument.");
            throw new OwRemoteException("No Masterplugin handler defined for given object in frontcontroller OwRemoteControlDocument.");
        }

        String subFolder = requestParameterMap.getParameter(QUERY_KEY_SUBPATH);

        recordDocument.dispatch(OwDispatchCodes.OPEN_OBJECT, recFolder, subFolder);
    }

    /**
     * Get Object from request parameter map (see {@link OwParameterMap}  
     * @param requestParameterMap
     * @param decrypt_p
     * @return OwObject
     * @throws Exception
     * @throws OwRemoteException
     */
    private OwObject getMappedObject(OwParameterMap requestParameterMap, boolean decrypt_p) throws Exception, OwRemoteException
    {
        return ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(this.getDMSIDfromRequest(requestParameterMap, decrypt_p), true);
    }

    /**
     * get latest DMSID version
     * @param dmsid_p current DMSID version
     * @return DMSID String
     * @since 3.1.0.0
     * @throws Exception
     */
    private String getLatestDMSID(String dmsid_p, boolean useLatestVersion_p) throws Exception
    {
        String dmsid = dmsid_p;

        OwObject obj = ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(dmsid, true);

        if (useLatestVersion_p && obj.hasVersionSeries())
        {
            OwVersionSeries versionsSeries = obj.getVersionSeries();
            OwVersion version = versionsSeries.getLatest();
            OwObject latestObject = versionsSeries.getObject(version);
            dmsid = latestObject.getDMSID();
        }

        return dmsid;
    }

    /** get a map of properties and values from the given query / request based parameter map
     *
     * @param parameterMap_p OwParameterMap
     * @param prefix_p String
     * @return Map
     */
    private Map getPropertyMapFromQuery(OwParameterMap parameterMap_p, String prefix_p)
    {
        // get the search criteria parameters to set
        Set params = parameterMap_p.getParameterNames();
        Map parameters = new HashMap();

        for (Iterator i = params.iterator(); i.hasNext();)
        {
            String sRequestParam = (String) i.next();

            if (sRequestParam.startsWith(prefix_p))
            {
                // === found a search criteria to set
                String sValue = parameterMap_p.getParameter(sRequestParam);
                String sSearchCriteria = sRequestParam.substring(prefix_p.length());

                parameters.put(sSearchCriteria, sValue);
            }

        }
        return parameters;
    }

    /**
     * Get the DMSID parameter from Request, encrypted or not encrypted
     * @param parameterMap_p
     * @param decrypt_p boolena flag if DMSID is encrypted in parameter map
     * @return plain DMSID
     */
    private String getDMSIDfromRequest(OwParameterMap parameterMap_p, boolean decrypt_p) throws OwRemoteException
    {
        OwRemoteLinkConfiguration configuration = new OwRemoteLinkConfiguration(getConfigNode());
        String dmsid = null;
        dmsid = parameterMap_p.getParameter(configuration.linkElementFor(QUERY_KEY_DMSID));
        if (null != dmsid)
        {
            try
            {
                if (decrypt_p)
                {
                    dmsid = OwSecretEncryption.decryptToString(OwSecretEncryption.stringToBytes(dmsid));
                }
                String latestVersion = parameterMap_p.getParameter(LATEST_VERSION_PARAMETER_NAME);
                if (latestVersion != null)
                {
                    dmsid = getLatestDMSID(dmsid, Boolean.parseBoolean(latestVersion));
                }
            }
            catch (OwInvalidOperationException e)
            {
                String mess = "Cannot decrypt the given text/url, possible cause: the text/url is invalid or was manipulated";
                LOG.error(mess, e);
                throw new OwRemoteException(mess, e);
            }
            catch (Exception e)
            {
                String mess = "Cannot get requested dmsid. Invalid dmsid.   ";
                LOG.error(mess, e);
                throw new OwRemoteException(mess, e);
            }
        }
        return dmsid;
    }

    /**
     * Copy all known properties definition from URL to the clipboard,
     * if a property is defined without value it's not inserted to
     * to the clipboard.
     * @param parameterMap_p OwParameterMap which contains the uprop_* definitions.
     * @throws Exception
     * @see #getPropertyMapFromQuery(OwParameterMap, String)
     * @see #QUERY_KEY_UPDATE_PROPERTY_PREFIX
     */
    private void copyUpdatePropertiesToClipboard(OwParameterMap parameterMap_p) throws Exception
    {
        //get map with update properties
        Map updateProperties = getPropertyMapFromQuery(parameterMap_p, QUERY_KEY_UPDATE_PROPERTY_PREFIX);
        //check if update properties defined
        if (!updateProperties.isEmpty())
        {
            OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
            OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
            if (clipboard != null)
            {
                //clear clipboard before inserting properties
                clipboard.clearContent();
                //create FieldDefinitions from symbolic property name
                for (Iterator it = updateProperties.entrySet().iterator(); it.hasNext();)
                {
                    //get symbolic property name
                    Map.Entry entry = (Map.Entry) it.next();
                    String propertyName = (String) entry.getKey();
                    Object value = entry.getValue();
                    try
                    {
                        OwFieldDefinition def = network.getFieldDefinition(propertyName, null);
                        //create an updatefield to add into the clipboard
                        OwUpdateField upField = new OwUpdateField(def);
                        //add only non-null values, else exception are thrown in other classes
                        if (value != null)
                        { //set value for current Field
                            upField.setValueFromString(value.toString());
                            //add updateField to the clipboard
                            clipboard.addContent(upField);
                        }
                    }
                    catch (OwObjectNotFoundException onfEx)
                    {
                        LOG.warn("Property could not be found, propertyName = [" + propertyName + "]", onfEx);
                    }
                    catch (ClassCastException ccEx)
                    {
                        LOG.warn("Property = [" + propertyName + "] could not be formated properly!", ccEx);
                    }
                }
            }
        }
    }

    /** public inner class for RemoteControl, to save Fields/Properties into the clipboard */
    public class OwUpdateField implements OwField
    {
        private OwFieldDefinition fieldDef;
        private Object value;

        /**can only be instantiated with a FieldDefinition */
        public OwUpdateField(OwFieldDefinition fieldDef_p)
        {
            this.fieldDef = fieldDef_p;
        }

        public OwFieldDefinition getFieldDefinition() throws Exception
        {
            return fieldDef;
        }

        public Object getValue() throws Exception
        {

            return value;
        }

        /**Set the value of the update field.
         * @param value_p Object which represents the value of the property
         */
        public void setValue(Object value_p) throws Exception
        {
            value = value_p;
        }

        /** Set the Value of this UpdateProperty using the
         * specification of the FieldDefinition. Convert the
         * given String-Value into Date, Integer etc...
         * @param value_p String which should be converted into native value
         * @throws Exception that indicate a parse or format exception in String <b><code>value_p</code></b>
         */
        public void setValueFromString(String value_p) throws Exception
        {
            value = fieldDef.getValueFromString(value_p);
        }

    }
}