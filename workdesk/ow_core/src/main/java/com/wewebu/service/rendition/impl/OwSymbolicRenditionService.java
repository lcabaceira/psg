package com.wewebu.service.rendition.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwMimeTypes;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.service.rendition.OwConfigurableRenditionService;

/**
 *<p>
 * Symbolic rendition service representation.
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
public class OwSymbolicRenditionService implements OwConfigurableRenditionService
{
    private static final String IMAGE_ATTRIBUTE = "image";

    private static final String MIME_ATTRIBUTE = "mime";

    private static final String UNKNOWN = "unknown";

    private static final Logger LOG = OwLogCore.getLogger(OwSymbolicRenditionService.class);

    private static final String EL_THUMBNAIL = "thumbnail";
    private static final String EL_THUMBNAILS = "thumbnails";
    private Map<String, String> configuredThumbnails = new HashMap<String, String>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    @Override
    public InputStream getRenditionStream(OwObject obj, String type) throws IOException, OwException
    {
        readLock.lock();
        try
        {
            String path;
            try
            {
                path = configuredThumbnails.get(obj.getMIMEType());
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not retrieve thumbnail.", e);
            }

            return getClass().getResourceAsStream(path);
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public List<String> getRenditionMimeType(OwObject obj, String type) throws OwException
    {
        try
        {
            readLock.lock();
            List<String> result = new LinkedList<String>();
            String path;
            try
            {
                path = configuredThumbnails.get(obj.getMIMEType());
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not retrieve thumbnail.", e);
            }

            String mime = OwMimeTypes.getMimeTypeFromPath(path);

            if (mime == null)
            {
                return result;
            }
            else
            {
                result.add(mime);
                return result;
            }
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public boolean hasRendition(OwObject obj, String type)
    {
        readLock.lock();
        try
        {
            if (!obj.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                return false;
            }
            else
            {
                String mimeType = obj.getMIMEType();
                return configuredThumbnails.get(mimeType) != null;
            }
        }
        catch (Exception e)
        {
            LOG.error("Could not retrive MIME type.", e);
            return false;
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public void createRendition(OwObject obj, String type) throws OwException
    {
        writeLock.lock();
        try
        {
            if (!obj.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                throw new OwInvalidOperationException("Can not create thumbnail for no-content objects.");
            }

            String mime = obj.getMIMEType();
            if (!configuredThumbnails.containsKey(mime))
            {
                if (configuredThumbnails.containsKey(UNKNOWN))
                {
                    configuredThumbnails.put(mime, configuredThumbnails.get(UNKNOWN));
                }
                else
                {
                    throw new OwConfigurationException("Can not create new-unknown MIME thumbs.Please add " + UNKNOWN + " thumb configuration.");
                }
            }
            else if (configuredThumbnails.get(mime) == null)
            {
                throw new OwConfigurationException("Create error simmulation : for development purposes only. Generated by missing image attribute in thumbnails configuration for MIME " + mime);
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not retrieve thumbnail.", e);
        }
        finally
        {
            writeLock.unlock();
        }
    }

    @Override
    public boolean canCreateRendition(OwObject obj, String type) throws OwException
    {
        try
        {
            return obj.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not check content.", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(OwXMLUtil serviceXMLUtil) throws OwException
    {
        List<OwXMLUtil> thumbnails = serviceXMLUtil.getSafeUtilList(EL_THUMBNAILS, EL_THUMBNAIL);
        for (OwXMLUtil thumbnail : thumbnails)
        {
            String mime = thumbnail.getSafeStringAttributeValue(MIME_ATTRIBUTE, null);
            String image = thumbnail.getSafeStringAttributeValue(IMAGE_ATTRIBUTE, null);
            if (mime != null && image != null)
            {
                configuredThumbnails.put(mime, image);
            }
            else if (image == null && mime != null)
            {
                configuredThumbnails.put(mime, null);
            }
        }
    }

}
