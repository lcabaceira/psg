package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.bindings.spi.StandardAuthenticationProvider;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class LocaleTest extends TestCase
{
    private static final String DEFAULT_ALFRESCO_ATOM_URL = "http://abs-alfone.alfresco.com:8080/alfresco/cmisatom";
    private static final String DEFAULT_ALFRESCO_SOAP_URL = "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/";

    String usr = "fav";
    String pwd = "wewebu2011";

    public void testAtomLocale()
    {
        System.out.println("-------------ATOM-ENGLISH------------------");
        Session session = getSession(BindingType.ATOMPUB, usr, pwd, Locale.ENGLISH);

        ObjectType type = session.getTypeDefinition("cmis:document");

        Map<String, PropertyDefinition<?>> props = type.getPropertyDefinitions();
        for (Entry<String, PropertyDefinition<?>> prop : props.entrySet())
        {
            System.out.println(prop.getKey() + " " + prop.getValue().getDisplayName());
        }

        System.out.println("-------------ATOM-GERMAN------------------");
        session = getSession(BindingType.ATOMPUB, usr, pwd, Locale.GERMAN);

        type = session.getTypeDefinition("cmis:document");

        props = type.getPropertyDefinitions();
        for (Entry<String, PropertyDefinition<?>> prop : props.entrySet())
        {
            System.out.println(prop.getKey() + " " + prop.getValue().getDisplayName());
        }

    }

    public void testSOAPLocale()
    {
        Session session;
        System.out.println("-------------SOAP-GERMAN------------------");
        session = getSession(BindingType.WEBSERVICES, usr, pwd, Locale.GERMAN);
        ObjectType type = session.getTypeDefinition("cmis:document");

        Map<String, PropertyDefinition<?>> props = type.getPropertyDefinitions();
        for (Entry<String, PropertyDefinition<?>> prop : props.entrySet())
        {
            System.out.println(prop.getKey() + " " + prop.getValue().getDisplayName());
        }

        System.out.println("-------------SOAP-ENGLISH------------------");
        session = getSession(BindingType.WEBSERVICES, usr, pwd, Locale.ENGLISH);
        type = session.getTypeDefinition("cmis:document");

        props = type.getPropertyDefinitions();
        for (Entry<String, PropertyDefinition<?>> prop : props.entrySet())
        {
            System.out.println(prop.getKey() + " " + prop.getValue().getDisplayName());
        }

    }

    protected Session getSession(BindingType type, String user, String pwd, Locale locale)
    {
        Map<String, String> params = getConfiguration(type, user, pwd);
        SessionFactoryImpl factory = SessionFactoryImpl.newInstance();
        RepositoryInfo repo = factory.getRepositories(params).get(0);
        params.put(SessionParameter.REPOSITORY_ID, repo.getId());
        params.put(SessionParameter.LOCALE_ISO3166_COUNTRY, locale.getCountry());
        params.put(SessionParameter.LOCALE_ISO639_LANGUAGE, locale.getLanguage());
        params.put(SessionParameter.LOCALE_VARIANT, locale.getVariant());

        ProxyLocalAuthenticationProvider authProv = new ProxyLocalAuthenticationProvider(new StandardAuthenticationProvider(), locale);
        return factory.createSession(params, null, authProv, null);
    }

    protected Map<String, String> getConfiguration(BindingType type, String user, String pwd)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put(SessionParameter.BINDING_TYPE, type.value());
        params.put(SessionParameter.USER, user);
        params.put(SessionParameter.PASSWORD, pwd);
        switch (type)
        {
            case WEBSERVICES:
            {
                //                String url = DEFAULT_ALFRESCO_SOAP_URL;
                //                params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url + "RepositoryService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, url + "NavigationService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, url + "ObjectService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, url + "VersioningService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, url + "DiscoveryService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, url + "RelationshipService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, url + "MultifilingService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, url + "PolicyService?wsdl");
                //                params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, url + "AclService?wsdl");

                params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
                params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl");
            }
                break;
            case ATOMPUB:
            {
                /*DEFAULT_ALFRESCO_ATOM_URL*/
                params.put(SessionParameter.ATOMPUB_URL, "http://abs-fncm52.alfresco.com:9080/fncmis/resources/Service");
            }
                break;
            default:
                System.err.println("HEEEEEEEEEE?????");
        }

        return params;
    }
}
