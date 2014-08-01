package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

/**
 *<p>
 * Abstract native
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
public abstract class AbstractNativeTest extends TestCase
{
    private static final String DEFAULT_ALFRESCO_SOAP_URL = "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/";
    private static final String DEFAULT_ALFRESCO_ATOM_URL = "http://abs-alfone.alfresco.com:8080/alfresco/cmisatom";
    protected static final String JUNIT_TEST = "JUnitTest";
    protected static final String JUNIT_TEST_PATH = "/" + JUNIT_TEST;
    private static final String DEFAULT_USER = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private Session session;
    protected boolean atom = true;
    private String objectFactoryClassName;
    private String mainServicerUrl;
    protected Folder junitFolder;

    public AbstractNativeTest()
    {
        this(true, null);
    }

    public AbstractNativeTest(boolean atomBinding)
    {
        this(atomBinding, null);
    }

    public AbstractNativeTest(boolean atomBinding, String objectFactoryClassName)
    {
        this(atomBinding, objectFactoryClassName, null);
    }

    public AbstractNativeTest(boolean atomBinding, String objectFactoryClassName, String serviceUrl)
    {
        atom = atomBinding;
        this.objectFactoryClassName = objectFactoryClassName;
        this.mainServicerUrl = serviceUrl;
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        session = createSession(atom, objectFactoryClassName, mainServicerUrl, null, null);
        junitFolder = (Folder) session.getObjectByPath(JUNIT_TEST_PATH);
    }

    protected Session createSession(boolean atom, String objectFactoryClassName)
    {
        return createSession(atom, objectFactoryClassName, null, null, null);
    }

    protected Session createSession(boolean atom, String objectFactoryClassName, String serviceUrl, String userName, String plainPassword)
    {
        Map<String, String> params = new HashMap<String, String>();
        final String user = userName == null ? DEFAULT_USER : userName;
        final String password = userName == null ? DEFAULT_PASSWORD : plainPassword;
        params.put(SessionParameter.USER, user);
        params.put(SessionParameter.PASSWORD, password);
        if (atom)
        {
            final String url = serviceUrl == null ? DEFAULT_ALFRESCO_ATOM_URL : serviceUrl;
            System.out.println("Binding ATOMPUB @ " + url);

            params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            params.put(SessionParameter.ATOMPUB_URL, url);
        }
        else
        {
            String url = serviceUrl == null ? DEFAULT_ALFRESCO_SOAP_URL : serviceUrl;
            System.out.println("Binding WEBSERVICES @ " + url);

            boolean bang = false;
            url = url.trim();
            if (url.endsWith("!"))
            {

                url = url.substring(0, url.length() - 1);
                bang = true;
            }

            params.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());

            if (bang)
            {
                params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, url);
                params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, url);
            }
            else
            {
                params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url + "RepositoryService?wsdl");
                params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, url + "NavigationService?wsdl");
                params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, url + "ObjectService?wsdl");
                params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, url + "VersioningService?wsdl");
                params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, url + "DiscoveryService?wsdl");
                params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, url + "RelationshipService?wsdl");
                params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, url + "MultifilingService?wsdl");
                params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, url + "PolicyService?wsdl");
                params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, url + "AclService?wsdl");
            }
        }

        if (objectFactoryClassName != null)
        {
            params.put(SessionParameter.OBJECT_FACTORY_CLASS, objectFactoryClassName);
        }
        SessionFactory factory = SessionFactoryImpl.newInstance();
        RepositoryInfo repo = factory.getRepositories(params).get(0);
        params.put(SessionParameter.REPOSITORY_ID, repo.getId());

        return factory.createSession(params);
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (session != null)
        {
            session.clear();
            session.getBinding().close();
        }
        super.tearDown();
    }

    protected Session getSession()
    {
        return this.session;
    }

    protected void traceType(ObjectType type)
    {
        System.out.println("id=" + type.getId());
        System.out.println("displayName= " + type.getDisplayName());
        System.out.println("queryName=" + type.getQueryName());
        System.out.println("propertyDefinitions = {" + type.getQueryName());
        Map<String, PropertyDefinition<?>> propertyDefinitionsMap = type.getPropertyDefinitions();
        Collection<PropertyDefinition<?>> propertyDefinitions = propertyDefinitionsMap.values();
        for (PropertyDefinition<?> propertyDefinition : propertyDefinitions)
        {
            System.out.println("\t" + propertyDefinition.getId() + "," + propertyDefinition.getPropertyType().value());
        }
        System.out.println("}");
    }

    public static void traceExtensions(List<CmisExtensionElement> extensions, String tab)
    {

        if (tab == null)
        {
            tab = "";
        }
        if (extensions != null)
        {
            for (CmisExtensionElement ex : extensions)
            {
                Map<String, String> attributes = ex.getAttributes();

                String value = ex.getValue();
                Class valueClass = value != null ? value.getClass() : null;
                System.out.println(tab + ex.getName() + "[" + attributes + "] = " + valueClass + " = " + value);

                traceExtensions(ex.getChildren(), tab + "\t");
            }
        }
        else
        {
            System.out.println("<null extensions>");
        }
    }
}
