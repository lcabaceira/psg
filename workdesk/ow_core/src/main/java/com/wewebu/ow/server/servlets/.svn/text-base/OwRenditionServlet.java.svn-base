package com.wewebu.ow.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwStreamUtil;
import com.wewebu.service.rendition.OwRenditionService;
import com.wewebu.service.rendition.OwRenditionServiceProvider;

/**
 *<p>
 * OwRenditionServlet for retrieval of renditions from objects.
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
public class OwRenditionServlet extends HttpServlet
{
    private static final Logger LOG = OwLogCore.getLogger(OwRenditionServlet.class);

    /** Default serial Version UID */
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_PARAM_RENDITION = "rendition";
    public static final String DEFAULT_PARAM_FALLBACK = "fallback";

    private static final String INIT_PARAM_FOLDER_IMAGE = "folderImage";
    private static final String INIT_PARAM_ERROR_IMAGE = "errorImage";
    private static final String INIT_PARAM_NO_CONTENT_IMAGE = "noContentImage";

    private String paramType;
    private String folderImagePath;
    private String noContentImagePath;
    private String errorImagePath;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        paramType = config.getInitParameter(DEFAULT_PARAM_RENDITION);
        if (paramType == null)
        {
            paramType = DEFAULT_PARAM_RENDITION;
        }
        folderImagePath = getConfiguredImage(config, INIT_PARAM_FOLDER_IMAGE);
        if (folderImagePath == null)
        {
            throw new ServletException("No init param " + INIT_PARAM_FOLDER_IMAGE + " configured");
        }
        noContentImagePath = getConfiguredImage(config, INIT_PARAM_NO_CONTENT_IMAGE);
        if (noContentImagePath == null)
        {
            throw new ServletException("No init param " + INIT_PARAM_NO_CONTENT_IMAGE + " configured");
        }
        errorImagePath = getConfiguredImage(config, INIT_PARAM_ERROR_IMAGE);
        if (errorImagePath == null)
        {
            throw new ServletException("No init param " + INIT_PARAM_ERROR_IMAGE + " configured");
        }
    }

    /**
     * Helper to get configured path to images. 
     * @param config ServletConfig
     * @param initParam String
     * @return String or null if no configuration found.
     */
    protected String getConfiguredImage(ServletConfig config, String initParam)
    {
        String confImg = config.getInitParameter(initParam);
        if (confImg != null && !confImg.startsWith("/"))
        {
            confImg = "/" + confImg;
        }
        return confImg;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession ses = req.getSession(false);
        if (ses != null)
        {
            String type = req.getParameter(paramType);
            OwAppContext appCtx = OwWebApplication.getContext(ses);
            try
            {
                if (appCtx != null && appCtx.isLogin())
                {
                    boolean processed = false;
                    OwRenditionServiceProvider prov = appCtx.getRegisteredInterface(OwRenditionServiceProvider.class);
                    OwObject obj = getRequestedObject(req, appCtx);
                    OwRenditionService service = prov.getRendtionService(obj);
                    if (service != null)
                    {
                        boolean hasRendition = service.hasRendition(obj, type);
                        if (hasRendition || service.canCreateRendition(obj, type))
                        {
                            try
                            {
                                if (!hasRendition)
                                {//try to create the requested rendition
                                    service.createRendition(obj, type);
                                }

                                if (service.hasRendition(obj, type))//check again if rendition is available
                                {
                                    processed = sendRendition(req, resp, prov, service, obj, type);
                                }
                                else
                                {// rendition seems to be created asynchronous, do default handling
                                    handleNoRendition(req, resp, obj, prov, type);
                                }
                            }
                            catch (OwException e)
                            {
                                handleNoRendition(req, resp, obj, prov, type);
                            }
                        }
                        else
                        {
                            handleNoRendition(req, resp, obj, prov, type);
                        }
                    }
                    else
                    {
                        handleNoRendition(req, resp, obj, prov, type);
                    }

                    if (LOG.isDebugEnabled())
                    {
                        if (processed)
                        {
                            LOG.debug("OwRenditionServlet.process: Rendition processed successfully");
                        }
                        else
                        {
                            LOG.debug("OwRenditionServlet.process: No rendition");
                        }
                    }
                }
                else
                {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    LOG.debug("OwRenditionServlet.process: No authentication found, sending forbidden response");
                }
            }
            catch (IOException e)
            {
                LOG.error("IO error during process of rendition request", e);
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Failed to process rendition request", e);
                showStaticImage(resp, errorImagePath);
            }
        }
        else
        {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**(overridable)
     * Called in case no Rendition service could be found.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param obj
     * @param prov
     * @throws IOException 
     */
    protected void handleNoRendition(HttpServletRequest req, HttpServletResponse resp, OwObject obj, OwRenditionServiceProvider prov, String type) throws OwException, IOException
    {
        String strFallback = req.getParameter(DEFAULT_PARAM_FALLBACK);
        boolean fallback = false;
        if (null != strFallback)
        {
            fallback = Boolean.parseBoolean(strFallback);
        }

        //Try the fall-back rendition service
        OwRenditionService fallbackService = prov.getFallbackRendtionService();
        if (fallback && null != fallbackService)
        {
            boolean hasRendition = fallbackService.hasRendition(obj, type);
            if (hasRendition || fallbackService.canCreateRendition(obj, type))
            {
                if (!hasRendition)
                {
                    fallbackService.createRendition(obj, type);
                }
                boolean handled = sendRendition(req, resp, prov, fallbackService, obj, type);
                if (!handled)
                {
                    sendDefaultStaticRendition(req, resp, obj);
                }
            }
            else
            {
                sendDefaultStaticRendition(req, resp, obj);
            }
        }
        else
        {
            sendDefaultStaticRendition(req, resp, obj);
        }
    }

    private void sendDefaultStaticRendition(HttpServletRequest req, HttpServletResponse resp, OwObject obj) throws IOException
    {
        if ((obj.getType() == OwObjectReference.OBJECT_TYPE_FOLDER) || (obj.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER))
        {
            showStaticImage(resp, folderImagePath);
        }
        else
        {
            showStaticImage(resp, noContentImagePath);
        }
    }

    /**
     * Show a static image from provided path.
     * @param resp HttpServletResponse
     * @param path String path to image
     * @throws IOException
     */
    protected void showStaticImage(HttpServletResponse resp, String path) throws IOException
    {
        InputStream staticImage = this.getClass().getResourceAsStream(path);
        if (staticImage != null)
        {
            OwStreamUtil.upload(staticImage, resp.getOutputStream(), true, 8192);
        }
        else
        {
            LOG.warn("OwRenditionServlet.showStaticImage: could not find static source under path = " + path);
        }
    }

    /**
     * Retrieve the object from request.
     * @param req HttpServletRequest
     * @param context OwAppContext
     * @return OwObject
     * @throws OwException if object cannot be retrieved from provided request
     */
    protected OwObject getRequestedObject(HttpServletRequest req, OwAppContext context) throws OwException
    {
        OwRoleManagerContext roleCtx = context.getRegisteredInterface(OwRoleManagerContext.class);
        String dmsid = req.getParameter("dmsid");
        try
        {
            return roleCtx.getNetwork().getObjectFromDMSID(dmsid, true);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.warn("Could not get Object by DMSID = " + dmsid, e);
            throw new OwServerException("Faild to retrieve Object from DMSID", e);
        }
    }

    protected boolean sendRendition(HttpServletRequest req, HttpServletResponse resp, OwRenditionServiceProvider prov, OwRenditionService service, OwObject obj, String renditionType) throws IOException, OwException
    {
        List<String> mimetypes = service.getRenditionMimeType(obj, renditionType);
        if (!mimetypes.isEmpty())
        {
            InputStream stream = service.getRenditionStream(obj, renditionType);
            if (stream != null)
            {
                resp.setContentType(mimetypes.get(0));
                OwStreamUtil.upload(stream, resp.getOutputStream(), true, 8192);
            }
            else
            {
                return false;
            }
            return true;
        }
        return false;
    }
}
