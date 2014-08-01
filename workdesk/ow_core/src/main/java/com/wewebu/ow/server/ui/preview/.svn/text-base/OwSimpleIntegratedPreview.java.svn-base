package com.wewebu.ow.server.ui.preview;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.service.rendition.OwRenditionService;
import com.wewebu.service.rendition.OwRenditionServiceProvider;

/**
 *<p>
 * Simple implementation of OwIntegratedPreview.
 * Will handle rendering of preview corresponding to the preview MIME type,
 * and contains a default handling if no explicit handler available.
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
public class OwSimpleIntegratedPreview extends OwIntegratedPreview implements OwPreviewContext
{
    private static final Logger LOG = OwLogCore.getLogger(OwSimpleIntegratedPreview.class);

    private static final String AT_VALUE = "value";
    private static final String AT_NAME = "name";
    private static final String AT_TYPE = "type";
    private static final String AT_CLASS = "class";
    private static final String ANY_CONTENT_TYPE = "*";
    private static final String EL_CONFIGURATION = "configuration";
    private static final String EL_FILTER = "filter";
    private static final String EL_MIME = "mime";
    private static final String EL_PARAMERTER = "parameter";
    private static final String EL_VIEWER = "viewer";

    private OwXMLUtil configuration;
    private OwObject object;
    private Map<String, OwPreviewRenderer> cachedRenderer;
    private Map<String, Map<String, String>> cachedConfig;
    private String renditionType;
    private String fullscreen;

    public OwSimpleIntegratedPreview()
    {
        cachedRenderer = new HashMap<String, OwPreviewRenderer>();
        cachedConfig = new HashMap<String, Map<String, String>>();
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        renditionType = getConfiguration().getSafeStringAttributeValue("rendition", "application/x-shockwave-flash");
        fullscreen = getConfiguration().getSafeStringAttributeValue("fullScreen", "true");
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        OwRenditionServiceProvider provider = getRenditionServiceProvider();
        if (getObject() != null)
        {
            OwRenditionService service = provider.getRendtionService(getObject());
            OwPreviewRenderer renderer = getNoPreviewRenderer();
            boolean available = service.hasRendition(getObject(), getRenditionType());
            if (available || service.canCreateRendition(getObject(), getRenditionType()))
            {
                List<String> mimeTypes = null;
                try
                {
                    if (!available)
                    {
                        service.createRendition(getObject(), getRenditionType());
                    }
                    mimeTypes = service.getRenditionMimeType(getObject(), getRenditionType());
                }
                catch (OwException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.warn("Unable to get/create Rendition for preview", e);
                    }
                    else
                    {
                        LOG.warn("OwSimpleIntegratedPreview.onRender(): Unable to get/create Rendition for preview. (" + e.getMessage() + ")");
                    }
                }
                if (mimeTypes != null)
                {
                    try
                    {
                        renderer = getPreviewRenderer(mimeTypes);
                    }
                    catch (OwException ex)
                    {
                        renderer = null;
                        if (LOG.isDebugEnabled())
                        {
                            LOG.warn("Unable to get renderer for preview", ex);
                        }
                        else
                        {
                            LOG.warn("OwSimpleIntegratedPreview.onRender(): Unable to get renderer for preview. (" + ex.getMessage() + ")");
                        }
                    }
                }

            }
            if (renderer == null)
            {//no renderer available
                w_p.write(getContext().localize("OwSimpleIntegratedPreview.no.renderer.label", "No preview handler available."));
            }
            else
            {//some renderer for handling preview
                renderer.render(w_p, cachedConfig.get(renderer.getClass().getCanonicalName()), this);
            }
        }
        else
        {
            w_p.write("No object to handle");
        }
        super.onRender(w_p);
    }

    /**
     * Will implicitly load a default handler if no explicit viewer/renderer configuration is available.
     * @param mimeTypes List of MIME type String's
     * @return OwPreviewRenderer, or null in case no matching renderer was found
     * @throws OwException 
     */
    @SuppressWarnings("unchecked")
    protected OwPreviewRenderer getPreviewRenderer(List<String> mimeTypes) throws OwException
    {
        OwPreviewRenderer prevRenderer = null;
        for (String mime : mimeTypes)
        {
            prevRenderer = this.cachedRenderer.get(mime);
        }
        if (prevRenderer == null)
        {
            OwXMLUtil rendererConfig = findMatchingRendererConfig(mimeTypes);
            Map<String, String> parameters = new HashMap<String, String>();
            if (rendererConfig != null)
            {
                prevRenderer = createPreviewRenderer(rendererConfig.getSafeStringAttributeValue(AT_CLASS, null));
                List<OwXMLUtil> parameterUtils = rendererConfig.getSafeUtilList(EL_CONFIGURATION, EL_PARAMERTER);
                for (OwXMLUtil parameter : parameterUtils)
                {
                    String name = parameter.getSafeStringAttributeValue(AT_NAME, null);
                    String value = parameter.getSafeStringAttributeValue(AT_VALUE, null);
                    if (name != null && value != null)
                    {
                        parameters.put(name, value);
                    }
                    else
                    {
                        throw new OwConfigurationException("Renderer configuration problem, parameter name [" + name + "] and/or value [" + value + "].");
                    }
                }
            }
            else
            {
                boolean hasConfiguration = hasPreviewConfiguration(mimeTypes);

                if (!hasConfiguration)
                {
                    prevRenderer = getSimpleConfigurationPreviewRanderer(mimeTypes, parameters);
                }

                else
                {
                    prevRenderer = null;
                }
            }

            if (prevRenderer != null)
            {
                cachedConfig.put(prevRenderer.getClass().getCanonicalName(), parameters);
                for (String mime : mimeTypes)
                {
                    cachedRenderer.put(mime, prevRenderer);
                }
            }
        }
        return prevRenderer;
    }

    /**
     * Find matching rendering configuration.
     * @param mimeType_p List of MIME types
     * @return OwXMLUtil or null if none available
     * @throws OwConfigurationException
     */
    @SuppressWarnings("unchecked")
    protected OwXMLUtil findMatchingRendererConfig(List<String> mimeType_p) throws OwConfigurationException
    {
        List<OwXMLUtil> viewers = getConfiguration().getSafeUtilList(EL_VIEWER);
        OwXMLUtil anyContentViewer = null;
        for (OwXMLUtil viewer : viewers)
        {
            List<OwXMLUtil> filterMimes = viewer.getSafeUtilList(EL_FILTER, EL_MIME);
            for (OwXMLUtil filter : filterMimes)
            {
                String mimeType = filter.getSafeStringAttributeValue(AT_TYPE, null);
                if (mimeType != null)
                {
                    if (ANY_CONTENT_TYPE.equals(mimeType))
                    {
                        anyContentViewer = viewer;
                    }
                    if (mimeType_p.contains(mimeType))
                    {
                        return viewer;
                    }
                }
            }
        }
        return anyContentViewer;
    }

    @SuppressWarnings("unchecked")
    protected OwPreviewRenderer createPreviewRenderer(String rendererClass) throws OwException
    {
        OwPreviewRenderer newInstance;
        try
        {
            Class<OwPreviewRenderer> clazz = (Class<OwPreviewRenderer>) Class.forName(rendererClass);
            newInstance = clazz.newInstance();
            return newInstance;
        }
        catch (ClassNotFoundException e)
        {
            throw new OwConfigurationException("Could not find configured class: " + rendererClass, e);
        }
        catch (InstantiationException e)
        {
            throw new OwConfigurationException("Cannot instantiate configured class: " + rendererClass, e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwServerException("Failed to access defined class: " + rendererClass, e);
        }
    }

    /**
     * Returns an instance of {@link OwPreviewRenderer} which will handle
     * the case that no preview renderer is configured/available.
     * @return OwPreviewRenderer
     */
    protected OwPreviewRenderer getNoPreviewRenderer()
    {
        return OwNoPreviewRenderer.INSTANCE;
    }

    /**
     * Return the provider which can retrieve a OwRenditionService for current object. 
     * @return OwRenditionServiceProvider
     */
    protected OwRenditionServiceProvider getRenditionServiceProvider()
    {
        return getContext().getRegisteredInterface(OwRenditionServiceProvider.class);
    }

    @Override
    public void setConfiguration(OwXMLUtil configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void setObject(OwObject object)
    {
        this.object = object;
    }

    @Override
    public OwObject getObject()
    {
        return this.object;
    }

    protected OwXMLUtil getConfiguration()
    {
        return this.configuration;
    }

    @Override
    public String getRenditionType()
    {
        return renditionType;
    }

    @Override
    public OwAppContext getAppContext()
    {
        return getContext();
    }

    /**
     * This method verifies if <code>Preview</code> has any view configured
     * @param mimeType_p
     * @return <code>true</code>  if there are any views configured or<br/>
     *         <code>false</code> otherwise 
     * @throws OwConfigurationException
     */
    @SuppressWarnings("unchecked")
    private boolean hasPreviewConfiguration(List<String> mimeType_p) throws OwConfigurationException
    {
        List<OwXMLUtil> viewers = getConfiguration().getSafeUtilList(EL_VIEWER);
        boolean hasConfiguration = false;
        if (viewers.size() > 0)
        {
            hasConfiguration = true;
        }
        return hasConfiguration;
    }

    /**
     * This method returns the default PreviewRanderer
     * @param mimeTypes
     * @param parameters
     * @return the default PreviewRanderer
     * @throws OwException
     */
    protected OwPreviewRenderer getSimpleConfigurationPreviewRanderer(List<String> mimeTypes, Map<String, String> parameters) throws OwException
    {
        OwPreviewRenderer prevRenderer;
        boolean imageRenderer = false;
        for (String mime : mimeTypes)
        {
            if (mime.startsWith("image") && !(mime.endsWith("/tiff") || mime.endsWith("/tif")))
            {
                imageRenderer = true;
                break;
            }
        }
        if (imageRenderer)
        {
            prevRenderer = createPreviewRenderer(OwImagePreviewRenderer.class.getCanonicalName());
            parameters.put("url", "{baseurl}/rendition?dmsid={dmsid}&rendition=" + renditionType);

        }
        else
        {

            prevRenderer = createPreviewRenderer(OwFlashPreviewRenderer.class.getCanonicalName());
            parameters.put("width", "100%");
            parameters.put("height", "600px");
            parameters.put("paging", "true");
            parameters.put("quality", "high");
            parameters.put("show_fullscreen_button", fullscreen);
            parameters.put("allowfullscreen", fullscreen);
            parameters.put("disable_i18n_input_fix", "false");
            parameters.put("url", "{baseurl}/rendition?dmsid={dmsid}&rendition=" + renditionType);

        }
        return prevRenderer;
    }
}
