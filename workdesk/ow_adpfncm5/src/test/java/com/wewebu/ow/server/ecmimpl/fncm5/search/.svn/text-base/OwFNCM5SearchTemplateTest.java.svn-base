package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5TestFileObject;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * OwFNCM5SearchTemplateTest.
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
public abstract class OwFNCM5SearchTemplateTest
{

    protected OwSearchTemplate loadTestSearchTemplate(File templateFile_p, OwSearchTestFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
    {
        OwFileObject fileObject = new OwFNCM5TestFileObject(Locale.US, null, templateFile_p, templateFile_p.getAbsolutePath());
        OwSearchTemplate template = createSearchTemplate(fileObject);
        template.init(fieldDefinitionProvider_p);
        return template;
    }

    protected OwSearchTemplate loadTestSearchTemplate(InputStream templateInputStream_p, OwSearchTestFieldDefinitionProvider fieldDefinitionProvider_p, boolean useSearchPaths_p) throws Exception
    {
        org.w3c.dom.Document doc = null;

        // === parse the XML Content of the OwObject to create a OwSearchTemplate.
        doc = OwXMLDOMUtil.getDocumentFromInputStream(templateInputStream_p);

        NodeList rootList = doc.getElementsByTagName("searchclause");
        Node root = rootList.item(0);

        OwSearchTemplate template = createSearchTemplate(root);
        template.init(fieldDefinitionProvider_p);
        return template;
    }

    protected OwSearchTemplate createSearchTemplate(Node root_p) throws Exception
    {
        return new OwFNCM5SearchTemplate(new OwSearchTemplateTestContext(), root_p, "Test Template", null);
    }

    protected OwSearchTemplate createSearchTemplate(OwObject templateObject_p) throws Exception
    {
        return new OwFNCM5SearchTemplate(new OwSearchTemplateTestContext(), templateObject_p);
    }

    protected OwSearchTemplate loadLocalClassPathTemplate(String resourceName_p, OwSearchTestFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
    {
        URL resourceURL = this.getClass().getResource(resourceName_p);
        File resourceFile = null;
        try
        {
            resourceFile = new File(resourceURL.toURI());
        }
        catch (URISyntaxException e)
        {
            resourceFile = new File(resourceURL.getPath());
        }

        if (!resourceFile.exists())
        {
            throw new IllegalArgumentException(resourceName_p + " does not exists in local class path of " + this.getClass());
        }

        return loadTestSearchTemplate(resourceFile, fieldDefinitionProvider_p);
    }
}
