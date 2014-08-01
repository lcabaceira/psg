package com.wewebu.ow.server.ecmimpl.opencmis.conf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwManagedSemiVirtualRecordConfiguration;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSessionParameter;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISAuthenticationInterceptor;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISIDDMSIDConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwNetworkConfiguration;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Helper to wrap the CMIS specific owbootstrap.xml configuration.
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
public class OwCMISNetworkCfg extends OwNetworkConfiguration
{
    private static final Logger LOG = OwLog.getLogger(OwCMISNetworkCfg.class);
    /**Configuration node for AtomPub binding*/
    public static final String EL_ATOM_PUB = "AtomPub";
    /**Configuration node name for RepositoryService URL*/
    public static final String EL_WSDL_REPOSITORY = "WSDLRepository";
    /**Configuration node name for NavigationService URL*/
    public static final String EL_WSDL_NAVIGATION = "WSDLNavigation";
    /**Configuration node name for ObjectService URL*/
    public static final String EL_WSDL_OBJECT = "WSDLObject";
    /**Configuration node name for MultifilingService URL*/
    public static final String EL_WSDL_MULTIFILING = "WSDLMultifiling";
    /**Configuration node name for DiscoveryService (search service) URL*/
    public static final String EL_WSDL_DISCOVERY = "WSDLDiscovery";
    /**Configuration node name for VersioningService URL*/
    public static final String EL_WSDL_VERSIONING = "WSDLVersioning";
    /**Configuration node name for RelationshipService URL*/
    public static final String EL_WSDL_RELATIONSHIP = "WSDLRelationship";
    /**Configuration node name for PolicyService URL*/
    public static final String EL_WSDL_POLICY = "WSDLPolicy";
    /**Configuration node name for ACL-Service URL*/
    public static final String EL_WSDL_ACL = "WSDLACL";

    public static final String EL_DEFAULTVERSIONHANDLING = "DefaultVersionHandling";

    public static final String EL_PROPERTY_ORDER = "PreferedPropertyOrder";

    public static final String EL_ID_DMSID_CONVERTER_CLASS = "IDDMSIDConverterClass";
    /**Mandatory Element to define the default object store/repository for Network*/
    public static final String EL_DEFAULT_REPOSITORY = "DefaultObjectStore";
    /**(optional) configuration node for AuthInterceptor definition*/
    public static final String EL_AUTH_INTERCEPTOR = "AuthInterceptor";

    public static final String AT_CLASS_NAME = "className";

    /**Node for extended the adapter session configuration configuration*/
    public static final String EL_ADAPTER_SESSION_CONF = "adapterSessionConf";
    /**Node for extended OpenCmis configuration*/
    public static final String EL_OPEN_CMIS_CONF = "openCmisConf";
    /**Sub node of OpenCMIS configuration*/
    public static final String EL_OPEN_CMIS_ENTRY = "entry";
    /**Name of entry key-attribute*/
    public static final String AT_ENTRY_KEY = "key";

    /**Configuration node to define Properties of type Id, which should be handled as String*/
    public static final String EL_CONF_NONSERIALIZED_ID = "NonSerializedId";

    private static final String FOLDERCHILDREN_CONTEXT = "FolderChildren";
    private static final String NORMALIZE_VERSION = "normalize";
    private static final String PRESERVE_VERSION = "preserve";

    private static final List<String> NATIVE_ID_PROPERTIES = Arrays.asList(new String[] { PropertyIds.OBJECT_ID, PropertyIds.OBJECT_TYPE_ID, PropertyIds.BASE_TYPE_ID, PropertyIds.VERSION_SERIES_ID, PropertyIds.PARENT_ID,
            PropertyIds.CONTENT_STREAM_ID, PropertyIds.ALLOWED_CHILD_OBJECT_TYPE_IDS, "IndexationId" });

    private Map<String, String> versionHandling;
    private Boolean preservVersion;
    private OwCMISPreferredPropertyTypeCfg preferredPropertyTypeCfg;

    private OwCMISDMSIDDecoder dmsidDecoder;
    /**Handler for semi-virtual folder configuration*/
    private OwManagedSemiVirtualRecordConfiguration semiVirtualConfiguration;

    private Map<String, OwCMISIDDMSIDConverter> idDMSIDConverters;

    private Collection<String> nonSerializedPropIds;

    public OwCMISNetworkCfg(OwXMLUtil config_p)
    {
        super(config_p);
        idDMSIDConverters = new HashMap<String, OwCMISIDDMSIDConverter>();
    }

    /**
     * @return a {@link Map} of String context names mapped to version handling modes (see CMIS bootstrap documentation) 
     */
    @SuppressWarnings("unchecked")
    public synchronized Map<String, String> getDefaultVersionHandling()
    {
        if (versionHandling == null)
        {
            OwXMLUtil configNode = getConfigNode();
            versionHandling = new HashMap<String, String>();

            List<Node> contexts = configNode.getSafeNodeList(EL_DEFAULTVERSIONHANDLING);
            for (Node context : contexts)
            {
                versionHandling.put(context.getNodeName(), context.getTextContent());
            }
        }

        return versionHandling;
    }

    /**
     * 
     * @param context_p
     * @return a String version handling mode for the given context
     */
    public String getDefaultVersionHandling(String context_p)
    {
        Map<String, String> versionHandligMap = getDefaultVersionHandling();
        return versionHandligMap.get(context_p);
    }

    /**
     * Is network configured to preserve version information of object.
     * @return true if version handling set to &quot;preserve&quot;
     */
    public boolean isPreservedVersion()
    {
        if (preservVersion == null)
        {
            String folderChildrenVersionHandling = getDefaultVersionHandling(FOLDERCHILDREN_CONTEXT);
            if (LOG.isDebugEnabled())
            {
                if (folderChildrenVersionHandling == null)
                {
                    LOG.debug("No default version hanling configured for " + FOLDERCHILDREN_CONTEXT);
                }
                else if (!NORMALIZE_VERSION.equalsIgnoreCase(folderChildrenVersionHandling) && !PRESERVE_VERSION.equals(folderChildrenVersionHandling))
                {
                    LOG.debug("Invalid version hanling configured for " + FOLDERCHILDREN_CONTEXT + " : " + folderChildrenVersionHandling);
                }
            }
            preservVersion = NORMALIZE_VERSION.equalsIgnoreCase(folderChildrenVersionHandling) ? Boolean.FALSE : Boolean.TRUE;
        }
        return preservVersion.booleanValue();
    }

    public OwXMLUtil getBpmNode() throws OwConfigurationException
    {
        OwXMLUtil result = null;
        try
        {
            result = getConfigNode().getSubUtil("BPM");
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not get the BPM configuration!", e);
        }

        if (null == result)
        {
            throw new OwConfigurationException("Could not get the BPM configuration!");
        }
        return result;
    }

    public OwCMISPreferredPropertyTypeCfg getPreferredPropertyTypeCfg()
    {
        if (null == this.preferredPropertyTypeCfg)
        {
            this.preferredPropertyTypeCfg = new OwCMISPreferredPropertyTypeCfg(getConfigNode());
        }

        return this.preferredPropertyTypeCfg;
    }

    /**
     * get a list of property names which represents the preferred order (sequence) of the properties
     * @return a {@link List} of property names
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public List<String> getPreferedPropertyOrder() throws OwException
    {
        Node preferedOrderNode = null;
        try
        {
            preferedOrderNode = getConfigNode().getSubNode(EL_PROPERTY_ORDER);
        }
        catch (Exception e)
        {
            LOG.warn("OwCMISNetworkConfig.getPerferedPropertyOrder: Configuration \"PreferedPropertyOrder\" could not be read.");
        }
        if (preferedOrderNode == null)
        {
            return new LinkedList<String>();
        }
        else
        {
            try
            {
                return (new OwStandardXMLUtil(preferedOrderNode)).getSafeStringList();
            }
            catch (Exception e)
            {
                LOG.error("Parsing of PreferedPropertyOrder node failed", e);
                throw new OwServerException("Could not parse configuration of PreferedPropertyOrder", e);
            }
        }
    }

    public synchronized OwCMISDMSIDDecoder getDMSIDDecoder()
    {
        if (dmsidDecoder == null)
        {
            dmsidDecoder = new OwCMISSimpleDMSIDDecoder();
        }
        return dmsidDecoder;
    }

    /**(overridable)
     * Map of session parameters string values.
     * @see OwCMISSessionParameter
     * 
     * @return Map of strings
     * @throws OwConfigurationException 
     */
    public Map<String, String> getSessionParametersConfig() throws OwConfigurationException
    {
        return addConfigurationProperties(EL_ADAPTER_SESSION_CONF, EL_OPEN_CMIS_ENTRY, AT_ENTRY_KEY, new HashMap<String, String>());
    }

    /**(overridable)
     * Get a binding handler class.
     * <p>Can be extended to use other binding handler.</p>
     * @return Map of strings
     * @throws OwConfigurationException 
     */
    public Map<String, String> getBindingConfig() throws OwConfigurationException
    {
        String url = getConfigNode().getSafeTextValue(EL_ATOM_PUB, null);
        Map<String, String> connection = new HashMap<String, String>();
        if (url != null)
        {
            connection.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            connection.put(SessionParameter.ATOMPUB_URL, url);
            if (LOG.isInfoEnabled())
            {
                LOG.info("OwCMISNetworkCfg.getBindingConfig: AtomPub binding is configured.");
            }
        }
        else
        {
            url = getConfigNode().getSafeTextValue(EL_WSDL_REPOSITORY, null);
            if (url != null)
            {
                connection.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());

                connection.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url);
                connection.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_NAVIGATION, null));
                connection.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_OBJECT, null));
                connection.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_VERSIONING, null));
                connection.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_DISCOVERY, null));
                connection.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_RELATIONSHIP, null));
                connection.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_MULTIFILING, null));
                connection.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_POLICY, null));
                connection.put(SessionParameter.WEBSERVICES_ACL_SERVICE, getConfigNode().getSafeTextValue(EL_WSDL_ACL, null));
                if (LOG.isInfoEnabled())
                {
                    LOG.info("OwCMISNetworkCfg.getBindingConfig: WebService (SOAP) binding is configured.");
                }
            }
            else
            {
                connection.put(SessionParameter.BINDING_TYPE, BindingType.LOCAL.value());
                if (LOG.isInfoEnabled())
                {
                    /* This binding is only valid with OpenCMIS, and has the restriction 
                     * to be run on/in the same JVM.
                     * Additionally a local factor must be defined! */
                    LOG.info("OwCMISNetworkCfg.getBindingConfig: LOCAL binding is configured.");
                }
            }
        }

        return setAdditionalConnectionInformation(connection);
    }

    /**
     * Handling of additional configuration of OpenCMIS. 
     * @param map Map where to put additional configuration
     * @return map with extended configuration (if such configuration exist)
     */
    protected Map<String, String> setAdditionalConnectionInformation(Map<String, String> map)
    {
        return addConfigurationProperties(EL_OPEN_CMIS_CONF, EL_OPEN_CMIS_ENTRY, AT_ENTRY_KEY, map);
    }

    /**
     * Adds key value configuration information to the given map.
     * The key value pairs are retrieved from a list with the given name and item name.
     *  
     * @see OwXMLUtil#getSafeUtilList(String, String)
     * @param map Map where to put additional configuration
     * @return map with extended configuration (if such configuration exist)
     */
    protected Map<String, String> addConfigurationProperties(String listNodeName, String itemName, String keyAttributeName, Map<String, String> map)
    {
        List<?> extConfLst = getConfigNode().getSafeUtilList(listNodeName, itemName);
        if (extConfLst != null && !extConfLst.isEmpty())
        {
            Iterator<?> it = extConfLst.iterator();
            while (it.hasNext())
            {
                OwXMLUtil util = (OwXMLUtil) it.next();
                String key = util.getSafeStringAttributeValue(keyAttributeName, null);
                String val = util.getSafeTextValue(null);
                if (key != null && val != null)
                {
                    map.put(key, val);
                }
                else
                {
                    LOG.warn("Invalid extended OpenCMIS found @key = " + key + " value = " + val);
                }
            }
        }

        return map;
    }

    /**
     * Return a String which is defined in &lt;DefaultObjectStore&gt; and
     * represents an id or name of a repository object. 
     * @return String name or id of Repository, or null if configuration was not found/defined
     */
    public String getDefaultRepository()
    {
        return getConfigNode().getSafeTextValue(EL_DEFAULT_REPOSITORY, null);
    }

    /**
     * Returns a class which provides a handler for semi-virtual configuration.
     * @return OwManagedSemiVirtualRecordConfiguration
     * @throws OwConfigurationException
     */
    public OwManagedSemiVirtualRecordConfiguration getSemiVirtualConfiguration() throws OwConfigurationException
    {
        if (semiVirtualConfiguration == null)
        {
            semiVirtualConfiguration = new OwManagedSemiVirtualRecordConfiguration();
        }
        return semiVirtualConfiguration;
    }

    /**
     * Get configured authentication intercepter, if any was defined.
     * Can return null if definition is not available.
     * @param createArgs Object array of arguments to create interceptor
     * @return OwCMISAuthenticationInterceptor (can return null)
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public OwCMISAuthenticationInterceptor getAuthenticationInterceptor(Object... createArgs) throws OwException
    {
        OwXMLUtil conf;
        String className = "com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator";
        try
        {
            conf = getConfigNode().getSubUtil(EL_AUTH_INTERCEPTOR);
            if (conf != null)
            {
                className = conf.getSafeStringAttributeValue(AT_CLASS_NAME, className);
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not read \"AuthInterceptor\" configuration", e);
            throw new OwConfigurationException("Could not read configuration", e);
        }

        OwCMISAuthenticationInterceptor interceptor = null;
        try
        {
            Class<OwCMISAuthenticationInterceptor> clazz = (Class<OwCMISAuthenticationInterceptor>) Class.forName(className);
            Constructor<OwCMISAuthenticationInterceptor> cons = null;
            if (createArgs != null && createArgs.length != 0)
            {
                Class<?>[] paramTypes = new Class[createArgs.length];
                int i = 0;
                for (Object arg : createArgs)
                {
                    paramTypes[i++] = arg.getClass();
                }
                Constructor<OwCMISAuthenticationInterceptor>[] constructors = (Constructor<OwCMISAuthenticationInterceptor>[]) clazz.getConstructors();
                for (Constructor<OwCMISAuthenticationInterceptor> constructor : constructors)
                {
                    Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
                    if (constructorParameterTypes.length == paramTypes.length)
                    {
                        cons = constructor;
                        for (int j = 0; j < constructorParameterTypes.length; j++)
                        {
                            if (!constructorParameterTypes[j].isAssignableFrom(paramTypes[j]))
                            {
                                cons = null;
                                break;
                            }
                        }
                        if (cons != null)
                        {
                            break;
                        }
                    }
                }
                if (cons == null)
                {
                    throw new NoSuchMethodException("No " + OwCMISAuthenticationInterceptor.class + " constructor found for parameter types " + paramTypes);
                }
            }
            else
            {
                cons = clazz.getConstructor();
            }
            if (cons.getParameterTypes().length == 0)
            {
                interceptor = cons.newInstance();
            }
            else
            {
                interceptor = cons.newInstance(createArgs);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new OwConfigurationException("Invalid class of type = " + className, e);
        }
        catch (SecurityException e)
        {
            throw new OwServerException("Environment Restriction to access class for \"AuthInterceptor\".", e);
        }
        catch (NoSuchMethodException e)
        {
            throw new OwServerException("Missing default constructor for \"AuthInterceptor\".", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new OwServerException("Invalid Argument for AuthInterceptor instantiation", e);
        }
        catch (InstantiationException e)
        {
            throw new OwServerException("Could not create instance for defined AuthInterceptor", e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwConfigurationException("Access to default consturctor is illegal, class loading problem!", e);
        }
        catch (InvocationTargetException e)
        {
            throw new OwServerException("Could not invoke default constructor for AuthInterceptor", e);
        }
        interceptor.init(conf);

        return interceptor;
    }

    @SuppressWarnings("unchecked")
    public synchronized OwCMISValueConverter<String, String> getIdDMSIDConverter(String resourceID_p) throws OwException
    {
        OwCMISIDDMSIDConverter idDMSIDConverter = idDMSIDConverters.get(resourceID_p);
        if (idDMSIDConverter == null)
        {
            try
            {
                String javaClassName = getConfigNode().getSafeTextValue(EL_ID_DMSID_CONVERTER_CLASS, OwCMISIDDMSIDConverter.class.getName());

                Class<OwCMISIDDMSIDConverter> javaClass = (Class<OwCMISIDDMSIDConverter>) Class.forName(javaClassName);

                Constructor<OwCMISIDDMSIDConverter> constructor = javaClass.getConstructor(new Class[] { OwCMISDMSIDDecoder.class, String.class });
                idDMSIDConverter = constructor.newInstance(new Object[] { getDMSIDDecoder(), resourceID_p });

                idDMSIDConverters.put(resourceID_p, idDMSIDConverter);
            }
            catch (Exception e)
            {
                throw new OwConfigurationException("Error accessing the ID to DMSID converter configuration!", e);
            }
        }

        return idDMSIDConverter;
    }

    /**
     * Return a collection of property Id (non-qualified), which should be not serialized and values should be treated as String.
     * <p>If nothing is configured a default set of property Id's is returned, which should not be deserialized. 
     * @return Collection of property Id's
     * @throws OwException if cannot access the configuration
     */
    @SuppressWarnings("unchecked")
    public Collection<String> getNonSerializedPropertyIds() throws OwException
    {
        if (this.nonSerializedPropIds == null)
        {
            try
            {
                List<String> nonSerLst = getConfigNode().getSafeStringList(EL_CONF_NONSERIALIZED_ID);
                if (nonSerLst != null && !nonSerLst.isEmpty())
                {
                    this.nonSerializedPropIds = Collections.unmodifiableCollection(nonSerLst);
                }
                else
                {
                    this.nonSerializedPropIds = Collections.unmodifiableCollection(NATIVE_ID_PROPERTIES);
                }
            }
            catch (Exception e)
            {
                throw new OwConfigurationException("Error accessing node " + EL_CONF_NONSERIALIZED_ID + " from configuration!", e);
            }
        }
        return this.nonSerializedPropIds;
    }
}
