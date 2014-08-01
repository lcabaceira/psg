package com.wewebu.ow.server.ui.preview;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * ShockWave Flash based preview renderer. 
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
public class OwFlashPreviewRenderer implements OwPreviewRenderer
{
    public static final String REQ_ATT_CFG = "FlashPreviewRenderer_cfg";

    private static final String ALLOW_FULLSCREEN = "allowfullscreen";
    private static final String ALLOW_SCRIPTACCESS = "allowScriptAccess";
    private static final String ALLOW_NETWORKING = "allowNetworking";
    private static final String QUALITY = "high";

    private static final String FILE_NAME = "fileName";
    private static final String PAGING = "paging";
    private static final String URL = "url";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String WMODE = "wmode";
    private static final List<String> NON_FLASHVARS_EXTENSION = Arrays.asList(FILE_NAME, PAGING, URL, WIDTH, HEIGHT, WMODE);

    @Override
    public void render(Writer writer, Map<String, String> configuration, OwPreviewContext prevContext) throws OwException, IOException
    {
        try
        {
            final String fileName = createFlashParameter(FILE_NAME, configuration, prevContext, prevContext.getObject().getName());
            final String paging = createFlashParameter(PAGING, configuration, prevContext, true);

            OwAppContext appContext = prevContext.getAppContext();
            final String url = createFlashParameter(URL, configuration, prevContext, appContext.getBaseURL() + "/swfpreview?dmsid=" + OwAppContext.encodeURL(prevContext.getObject().getDMSID()));
            final String otherFlashVars = createOtherFlashParameters(NON_FLASHVARS_EXTENSION, configuration, prevContext);
            final String i18n_actualSize = "i18n_actualSize=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_actualSize", "Actual Size"));
            final String i18n_fitPage = "i18n_fitPage=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fitPage", "Fit Page"));
            final String i18n_fitWidth = "i18n_fitWidth=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fitWidth", "Fit Width"));
            final String i18n_fitHeight = "i18n_fitHeight=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fitHeight", "Fit Height"));
            final String i18n_fullscreen = "i18n_fullscreen=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fullscreen", "Full Screen"));
            final String i18n_fullwindow = "i18n_fullwindow=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fullwindow", "Maximize"));
            final String i18n_fullwindow_escape = "i18n_fullwindow_escape=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_fullwindow_escape", "Press Esc to exit full window mode."));
            final String i18n_page = "i18n_page=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_page", "Page"));
            final String i18n_pageOf = "i18n_pageOf=" + OwAppContext.encodeURL(appContext.localize("OwFlashPreviewRenderer.i18n_pageOf", "of"));

            final String flashvars = "\"" + createFlashVarsValue(fileName, paging, url, i18n_actualSize, i18n_fitPage, i18n_fitWidth, i18n_fitHeight, i18n_fullscreen, i18n_fullwindow, i18n_fullwindow_escape, i18n_page, i18n_pageOf, otherFlashVars)
                    + "\"";

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("flashvars", flashvars);

            final String width = createValue(WIDTH, configuration, prevContext, "100%", null);
            map.put("width", width);

            final String height = createValue(HEIGHT, configuration, prevContext, "600px", null);
            map.put("height", height);

            String quality = createValue(QUALITY, configuration, prevContext, "high", null);
            map.put("quality", quality);

            String allowNetworking = createValue(ALLOW_NETWORKING, configuration, prevContext, "all", null);
            map.put("allowNetworking", allowNetworking);

            String allowScriptAccess = createValue(ALLOW_SCRIPTACCESS, configuration, prevContext, "sameDomain", null);
            map.put("allowScriptAccess", allowScriptAccess);

            String allowFullScreen = createValue(ALLOW_FULLSCREEN, configuration, prevContext, "true", null);
            map.put("allowFullScreen", allowFullScreen);

            String wmode = createValue(WMODE, configuration, prevContext, "transparent", null);
            map.put("wmode", wmode);

            HttpServletRequest httpRequest = appContext.getHttpRequest();
            httpRequest.setAttribute(REQ_ATT_CFG, map);

            appContext.serverSideInclude("/viewer/preview/FlashPreviewRenderer.jsp", writer);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Preview rendering error.", e);
        }
    }

    private String createFlashParameter(String name, Map<String, String> configuration, OwPreviewContext prevContext, Object defaultValue) throws OwInvalidOperationException
    {
        String value = createValue(name, configuration, prevContext, defaultValue, null);
        return name + "=" + value;
    }

    private String createHTMLParameter(String name, Map<String, String> configuration, OwPreviewContext prevContext, Object defaultValue) throws OwInvalidOperationException
    {
        String value = createValue(name, configuration, prevContext, defaultValue, "\"");
        return name + "=" + value;
    }

    private String createValue(String name, Map<String, String> configuration, OwPreviewContext prevContext, Object defaultValue, String quote) throws OwInvalidOperationException
    {
        try
        {
            StringBuilder value = new StringBuilder((configuration.containsKey(name) ? configuration.get(name) : defaultValue.toString()));
            OwString.replaceAll(value, "{baseurl}", prevContext.getAppContext().getBaseURL());
            OwString.replaceAll(value, "{dmsid}", prevContext.getObject().getDMSID());
            value = new StringBuilder(OwAppContext.encodeURL(value.toString()));
            if (quote != null)
            {
                return quote + value + quote;
            }
            else
            {
                return value.toString();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwInvalidOperationException("Error creating parameter.", e);
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Error creating parameter.", e);
        }

    }

    private String createOtherFlashParameters(List<String> omitNames, Map<String, String> configuration, OwPreviewContext prevContext) throws OwInvalidOperationException
    {
        Set<String> parameters = configuration.keySet();
        StringBuilder flashParameters = new StringBuilder();
        for (String paramerer : parameters)
        {
            if (!omitNames.contains(paramerer))
            {
                if (flashParameters.length() > 0)
                {
                    flashParameters.append("&");
                }
                flashParameters.append(createFlashParameter(paramerer, configuration, prevContext, null));
            }
        }
        return flashParameters.toString();
    }

    private String createFlashVarsValue(String... vars)
    {

        StringBuilder flashVars = new StringBuilder();
        if (vars != null)
        {
            if (vars.length > 0)
            {
                flashVars.append(vars[0]);
            }
            for (int i = 1; i < vars.length; i++)
            {
                flashVars.append("&");
                flashVars.append(vars[i]);
            }
        }

        return flashVars.toString();
    }
}
