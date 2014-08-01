package com.wewebu.ow.server.ecmimpl.opencmis.wd.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRendition;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.service.rendition.impl.OwAbstractRenditionService;

/**
 *<p>
 * CMIS Implementation of Rendition service handling. 
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
public class OwCMISRenditionService extends OwAbstractRenditionService
{
    private static final Logger LOG = OwLog.getLogger(OwCMISRenditionService.class);

    @Override
    public InputStream getRenditionStream(OwObject obj, String type) throws IOException, OwException
    {
        Set<String> parsedFilter = parseFilter(type);
        List<OwCMISRendition> lst = getRenditions(obj, parsedFilter);
        if (type != null)
        {
            for (OwCMISRendition rendition : lst)
            {
                for (String filterPart : parsedFilter)
                {
                    if (filterPart.equals(rendition.getType()) || filterPart.equals(rendition.getContentMIMEType()) || filterPart.matches(rendition.getType()))
                    {
                        return rendition.getContentStream();
                    }
                }
            }
        }
        return lst.get(0).getContentStream();
    }

    @Override
    public List<String> getRenditionMimeType(OwObject obj, String type) throws OwException
    {
        List<OwCMISRendition> lst = getRenditions(obj, type);
        List<String> result = new LinkedList<String>();
        for (OwCMISRendition rendition : lst)
        {
            result.add(rendition.getContentMIMEType());
        }
        return result;
    }

    @Override
    public boolean hasRendition(OwObject obj, String type)
    {
        try
        {
            return !getRenditions(obj, type).isEmpty();
        }
        catch (OwException e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.warn("Failed to retrieve Renditions for object", e);
            }
            else
            {
                LOG.warn("Failed to retrieve Renditions for object, return false (no renditions).");
            }
            return false;
        }
    }

    private List<OwCMISRendition> getRenditions(OwObject obj, String filter) throws OwException
    {
        return getRenditions(obj, parseFilter(filter));
    }

    private List<OwCMISRendition> getRenditions(OwObject obj, Set<String> filter) throws OwException
    {
        OwCMISNativeObject<?> cmisObject = (OwCMISNativeObject<?>) obj;
        return cmisObject.retrieveRenditions(filter, false);
    }

    /**
     * Parse filter string based on CMIS specification:
     * <pre>
     * The Rendition Filter grammar is defined as follows:
     * &lt;renditionInclusion&gt; ::= &lt;none&gt; | &lt;wildcard&gt; | &lt;termlist&gt;
     * &lt;termlist&gt; ::= &lt;term&gt; | &lt;term&gt; ',' &lt;termlist&gt;
     * &lt;term&gt; ::= &lt;kind&gt; | &lt;mimetype&gt;
     * &lt;kind&gt; ::= &lt;text&gt;
     * &lt;mimetype&gt; ::= &lt;type&gt; '/' &lt;subtype&gt;
     * &lt;type&gt; ::= &lt;text&gt;
     * &lt;subtype&gt; ::= &lt;text&gt; | &lt;wildcard&gt;
     * &lt;text&gt; ::= /&#42; any char except whitespace &#42;/
     * &lt;wildcard&gt; ::= '&#42;'
     * &lt;none&gt; ::= 'cmis:none'
     * </pre>
     * @param filter String representing CMIS specific filter
     * @return Set of explicit filter definitions
     */
    protected Set<String> parseFilter(String filter)
    {
        if (filter == null)
        {
            filter = "*";
        }
        HashSet<String> thumbnailRenditions = new HashSet<String>();
        String[] arrFilter = filter.split(",");
        for (String inFilter : arrFilter)
        {
            thumbnailRenditions.add(inFilter);
        }

        return thumbnailRenditions;
    }
}
