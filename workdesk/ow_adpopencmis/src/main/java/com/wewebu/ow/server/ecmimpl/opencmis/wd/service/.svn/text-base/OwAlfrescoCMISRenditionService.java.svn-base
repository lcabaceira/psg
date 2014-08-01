package com.wewebu.ow.server.ecmimpl.opencmis.wd.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.app.OwGlobalParametersConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRendition;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwRequestContext;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.service.rendition.OwConfigurableRenditionService;

/**
 *<p>
 * Alfresco CMIS Implementation of Rendition service handling. 
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
 *@since 4.2.0.0
 */
public class OwAlfrescoCMISRenditionService extends OwCMISRenditionService implements OwConfigurableRenditionService
{
    private static final String TAG_VALUE = "value";

    private static final String TAG_LIST = "list";

    private static final String TAG_KEY = "key";

    private static final String TAG_ENTRY = "entry";

    private static final String TAG_MAP = "map";

    private static final String TAG_PROPERTY = "property";

    private static final String SERVICE_URI = "/service/wd/webscript/rendition";

    private static final Logger LOG = OwLog.getLogger(OwCMISRenditionService.class);

    /** Kind to thumbnail mapping */
    private Map<String, List<String>> kindToThumbnailNames;

    public OwAlfrescoCMISRenditionService()
    {
        kindToThumbnailNames = new HashMap<String, List<String>>();
    }

    public OwMainAppContext getContext()
    {
        final HttpServletRequest request_p = OwRequestContext.getLocalThreadRequest();
        final HttpSession Session = request_p.getSession();
        final OwMainAppContext context = (OwMainAppContext) Session.getAttribute(OwWebApplication.CONTEXT_KEY);
        return context;
    }

    @Override
    public void createRendition(final OwObject obj, final String type) throws OwException
    {
        OwGlobalParametersConfiguration util = getContext().getConfiguration().getGlobalParameters();

        String baseUri = util.getSafeString("EcmBaseUrl", null);

        if (baseUri != null)
        {
            final String id = obj.getID();
            final Map<String, List<String>> map = getRenditionKindMapping();

            // iterate and display values
            for (Map.Entry<String, List<String>> entry : map.entrySet())
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("title = " + entry.getKey());
                    LOG.debug("kinds = " + entry.getValue());
                }
                if (entry.getKey().equals(type))
                {
                    // create JSON object and populate it
                    JSONObject json = new JSONObject();

                    //TODO: To find a with an improvement to many post requests for each file, to sent entire kinds list once
                    //                try
                    //                {
                    //                    json.put("ID", id);
                    //                    json.put("REND_DEF", kinds);
                    //                }
                    //                catch (JSONException e)
                    //                {
                    //                    // TODO Auto-generated catch block
                    //                    e.printStackTrace();
                    //                }
                    //                LOG.debug("json = " + json.toString());
                    //                renditionServicePost(baseUri, id, json);

                    boolean success = false;
                    for (String rnd : entry.getValue())
                    {
                        try
                        {
                            json.put("ID", id);
                            json.put("REND_DEF", rnd);
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("json = " + json.toString());
                            }
                            renditionServicePost(baseUri, id, json);
                            success = true;
                        }
                        catch (final JSONException e)
                        {
                            LOG.warn("Unable to create JSON object", e);
                        }
                        catch (final OwServerException es)
                        {
                            LOG.warn("Failure to retrieve or create rendition.", es);
                        }
                    }

                    if (!success)
                    {
                        throw new OwServerException("All renditions have failed.");
                    }

                }
            }
        }
    }

    /**
     * Send  and  rendition request
     * @param baseUri Service URI
     * @param id Rendition object
     * @param json Rendition Type
     * @throws OwServerException
     */
    private void renditionServicePost(final String baseUri, final String id, final JSONObject json) throws OwServerException
    {
        String connectionUrl = baseUri + SERVICE_URI;
        if (LOG.isDebugEnabled())
        {
            LOG.debug(" ::renditionServicePost() [" + connectionUrl + "] " + json.toString());
        }
        OwRestletAuthenticationHandler restletAuthenticationHandler = null;
        try
        {
            restletAuthenticationHandler = (OwRestletAuthenticationHandler) getContext().getNetwork().getInterface(OwRestletAuthenticationHandler.class.getCanonicalName(), null);
        }
        catch (Exception e)
        {
            throw new OwServerException("Failed to retrieve the OwRestletAuthenticationHandler.", e);
        }

        final ClientResource cr = new ClientResource(connectionUrl);
        restletAuthenticationHandler.prepareCall(cr);

        try
        {
            final Representation jsonEntity = new JsonRepresentation(json);
            cr.post(jsonEntity);
        }
        catch (final ResourceException re)
        {
            throw new OwServerException("Could not create rendition of type for object with ID: " + id, re);
        }
        finally
        {
            final Representation responseEntity = cr.getResponseEntity();
            if (null != responseEntity)
            {
                try
                {
                    responseEntity.exhaust();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
                responseEntity.release();
            }
        }
    }

    @Override
    public boolean canCreateRendition(final OwObject obj, final String type) throws OwException
    {
        try
        {
            return obj.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        }
        catch (final OwException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            throw new OwInvalidOperationException("Could not check content.", e);
        }
    }

    private List<OwCMISRendition> getRenditions(final OwObject obj, String filter) throws OwException
    {
        if (filter == null)
        {
            filter = "*";
        }
        final OwCMISNativeObject<?> cmisObject = (OwCMISNativeObject<?>) obj;
        final HashSet<String> renditions = new HashSet<String>();
        renditions.add(filter);

        return cmisObject.retrieveRenditions(renditions, false);
    }

    @Override
    public void init(final OwXMLUtil serviceXMLUtil) throws OwException
    {
        if (LOG.isDebugEnabled())
        {
            String className = serviceXMLUtil.getSafeStringAttributeValue("class", null);
            LOG.debug("Read mapping parameter for " + className);
        }

        try
        {
            List<OwXMLUtil> props = serviceXMLUtil.getSafeUtilList(TAG_PROPERTY);
            if (!props.isEmpty())
            {
                for (OwXMLUtil prop : props)
                {
                    OwXMLUtil map = prop.getSubUtil(TAG_MAP);
                    List<OwXMLUtil> entries = map.getSafeUtilList(TAG_ENTRY);

                    for (final OwXMLUtil entry : entries)
                    {
                        String key = entry.getSafeStringAttributeValue(TAG_KEY, null);

                        List<OwXMLUtil> values = entry.getSafeUtilList(TAG_LIST, TAG_VALUE);
                        List<String> valueslst = new LinkedList<String>();
                        for (final OwXMLUtil value : values)
                        {
                            final String strValues = value.getSafeTextValue(TAG_VALUE);

                            valueslst.add(strValues);
                        }
                        kindToThumbnailNames.put(key, valueslst);
                    }
                }
            }
        }
        catch (final Exception e)
        {
            throw new OwConfigurationException("Unable to parse configuration, reading mapping");
        }
    }

    /**
     * Get rendition kind mapping.
     * 
     */
    private Map<String, List<String>> getRenditionKindMapping()
    {
        return kindToThumbnailNames;
    }
}
