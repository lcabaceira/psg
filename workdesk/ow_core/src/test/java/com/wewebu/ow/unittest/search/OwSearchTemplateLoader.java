package com.wewebu.ow.unittest.search;

import java.io.InputStream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * OwSearchTemplateLoader. 
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
public class OwSearchTemplateLoader
{
    private Class<?> resourceClass;

    public OwSearchTemplateLoader(Class<?> resourceClass)
    {
        this.resourceClass = resourceClass;
    }

    public OwSearchTemplate loadTestSearchTemplate(InputStream templateInputStream_p, OwFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
    {
        org.w3c.dom.Document doc = null;

        // === parse the XML Content of the OwObject to create a OwSearchTemplate.
        doc = OwXMLDOMUtil.getDocumentFromInputStream(templateInputStream_p);

        NodeList rootList = doc.getElementsByTagName("searchclauses");
        Node root = rootList.item(0);

        OwSearchTemplate template = createSearchTemplate(root);
        template.init(fieldDefinitionProvider_p);
        return template;

    }

    public OwSearchTemplate createSearchTemplate(Node root_p) throws Exception
    {
        return new OwStandardSearchTemplate(new OwSearchTemplateTestContext(), root_p, "Test Template", null);
    }

    public OwSearchTemplate loadLocalClassPathTemplate(String resourceName_p, OwFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
    {
        InputStream is = resourceClass.getResourceAsStream(resourceName_p);
        if (is == null)
        {
            throw new IllegalArgumentException(resourceName_p + " does not exists in local class path of " + this.getClass());
        }
        return loadTestSearchTemplate(is, fieldDefinitionProvider_p);
    }
}
