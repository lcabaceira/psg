package com.wewebu.ow.server.plug.owrecordext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.OwSimplePropertySubregion;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog;
import com.wewebu.ow.server.dmsdialogs.OwFormPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwStandardPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.efilekey.generator.OwEFileKeyGenerator;
import com.wewebu.ow.server.plug.efilekey.parser.ParseException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Creates a view for properties involved in eFile key generation.
 * In this view, the user has the possibility to set values for the ECM properties involved 
 * in eFile key generation.
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
 *@since 3.1.0.0
 */
public class OwCreateContractDialog extends OwCreateObjectDialog
{
    private static final String DEFAULT_EMPTY_STRING = "";
    private static final String DIALOG_HANDLER_ELEMENT_NAME = "DialogHandler";
    /** the property view */
    protected OwPropertyViewBridge m_keyPatternPropertyBridge;
    /** configuration flag, if <code>true</code> the key section is configured*/
    private boolean m_useKeyPropertyView;
    /** property list configuration with {@link org.alfresco.wd.ui.conf.prop.OwPropertyInfo} objects*/
    private OwPropertyListConfiguration m_keysPropertiesInfos;

    private int m_keyPatternViewTabIndex;

    /**class logger*/
    private static final Logger LOG = OwLog.getLogger(OwCreateContractDialog.class);

    /** a map between property names and pattern based value generators*/
    protected Map<String, OwEFileKeyGenerator> m_propertyPatternConfiguration;

    /**
     * Constructor.
     * @param folderObject_p
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param fOpenObject_p
     * @deprecated will be replaced by {@link #OwCreateContractDialog(OwObject, OwObjectClassSelectionCfg, boolean)}
     */
    public OwCreateContractDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean fOpenObject_p)
    {
        super(folderObject_p, strClassName_p, strObjectClassParent_p, fOpenObject_p);
        m_propertyPatternConfiguration = new HashMap<String, OwEFileKeyGenerator>();
    }

    /**
     * Constructor.
     * @param folderObject_p
     * @param classSelectionCfg
     * @param fOpenObject_p
     * @since 4.1.0.0
     */
    public OwCreateContractDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p)
    {
        super(folderObject_p, classSelectionCfg, fOpenObject_p);
        m_propertyPatternConfiguration = new HashMap<String, OwEFileKeyGenerator>();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog#createDocument()
     */
    protected OwDocument createDocument()
    {
        return new OwContractDocument();
    }

    /**
     * Create the map between property names and associated generators.
     * @param node_p - the configuration node
     * @param context_p - the application context
     * @throws Exception
     */
    protected void initPropertyPatternConfiguration(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        OwXMLUtil dialogHandlerNode = node_p.getSubUtil(DIALOG_HANDLER_ELEMENT_NAME);
        if (dialogHandlerNode != null)
        {
            OwXMLUtil keyGeneratorsSection = dialogHandlerNode.getSubUtil("KeyGenerators");
            if (keyGeneratorsSection != null)
            {
                List<?> keyNodeList = keyGeneratorsSection.getSafeUtilList("Key");
                Iterator<?> keyNodesIterator = keyNodeList.iterator();
                while (keyNodesIterator.hasNext())
                {
                    OwXMLUtil keyConfiguration = (OwXMLUtil) keyNodesIterator.next();
                    String pattern = keyConfiguration.getSafeTextValue(DEFAULT_EMPTY_STRING);
                    try
                    {
                        OwEFileKeyGenerator generator = new OwEFileKeyGenerator(context_p.getNetwork(), pattern);
                        m_propertyPatternConfiguration.put(keyConfiguration.getSafeStringAttributeValue("name", DEFAULT_EMPTY_STRING), generator);
                    }
                    catch (ParseException pex)
                    {
                        OwString message = new OwString("plug.owadddocument.OwAddObjectRecordFunction.keyGeneratorParseError", "Could not parse key generator pattern.");
                        throw new OwConfigurationException(message, pex);
                    }
                }
            }
        }
        getDocument().setKeyPropertyConfiguration(m_propertyPatternConfiguration);
    }

    /**
     * Create properties info for EditPropertiesView
     * @param isReadOnly_p - represent the property info editable status.
     * @return a {@link List} object with properties information
     * @throws Exception
     */
    protected Set<OwPropertyInfo> getPatternBasedPropInfos(boolean showKeys_p, boolean isReadOnly_p) throws Exception
    {
        Set<OwPropertyInfo> propertyInfos = new LinkedHashSet<OwPropertyInfo>();
        Iterator<String> keys = m_propertyPatternConfiguration.keySet().iterator();
        while (keys.hasNext())
        {
            String keyPropName = keys.next();
            //set read only the generated key
            if (showKeys_p)
            {
                propertyInfos.add(new OwPropertyInfo(keyPropName, true));
            }
            OwEFileKeyGenerator keyGenerator = m_propertyPatternConfiguration.get(keyPropName);
            String[] involvedProperties = keyGenerator.getECMPropertyNames();
            for (int i = 0; i < involvedProperties.length; i++)
            {
                propertyInfos.add(new OwPropertyInfo(involvedProperties[i], isReadOnly_p));
            }
        }
        return propertyInfos;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog#init()
     */
    protected void init() throws Exception
    {
        // read configuration
        if (m_configNode == null)
        {
            LOG.error("OwCreateContractDialog.init(): the configuration node is missing.");
            throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.missingConfigNode", "Configuration node is missing."));
        }
        initPropertyPatternConfiguration(m_configNode, (OwMainAppContext) getContext());
        m_useKeyPropertyView = false;
        for (OwEFileKeyGenerator gen : m_propertyPatternConfiguration.values())
        {
            String[] ecmProps = gen.getECMPropertyNames();
            if (ecmProps != null && ecmProps.length != 0)
            {
                m_useKeyPropertyView = true;
                break;
            }
        }
        setKeysPropertyInfos(new LinkedList<OwPropertyInfo>(getPatternBasedPropInfos(false, false)));
        (getDocument()).setAdditionalPropertiesInfo(getPatternBasedPropInfos(true, true));
        super.init();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initTabOrder() throws Exception
    {
        super.initTabOrder();
        if (m_useKeyPropertyView)
        {
            m_keyPatternPropertyBridge = createKeyPatternPropertyBridge();
            // attach view to navigation
            m_keyPatternViewTabIndex = m_SubNavigation.addView(m_keyPatternPropertyBridge.getView(), getContext().localize("owaddrecord.OwCreateContractDialog.key_properties", "Properties for ID generation"), null, getContext().getDesignURL()
                    + "/images/plug/owdocprops/properties_key.png", null, null);
            enableTabView(m_keyPatternViewTabIndex, false);

            ((OwMultipanel) m_keyPatternPropertyBridge.getView()).setNextActivateView(propertiesTab.getView());

            int idx = m_SubNavigation.getTabList().indexOf(propertiesTab);
            OwTabInfo key = (OwTabInfo) m_SubNavigation.getTabList().get(m_keyPatternViewTabIndex);
            m_SubNavigation.getTabList().remove(m_keyPatternViewTabIndex);
            m_SubNavigation.getTabList().add(idx, key);
            m_keyPatternViewTabIndex = idx;
            if (m_classView != null)
            {
                if (getAccessRightsView() != null)
                {
                    getAccessRightsView().setNextActivateView(m_keyPatternPropertyBridge.getView());
                }
                else
                {
                    m_classView.setNextActivateView(m_keyPatternPropertyBridge.getView());
                }
            }
            else
            {
                if (getAccessRightsView() != null)
                {
                    getAccessRightsView().setNextActivateView(m_keyPatternPropertyBridge.getView());
                }
            }
        }
    }

    /**
     * Create the bridge responsible to render the view with properties involved in key generation.
     * @return the bridge responsible to render the view with properties involved in key generation.
     * @throws Exception
     */
    protected OwPropertyViewBridge createKeyPatternPropertyBridge() throws Exception
    {
        OwPropertyViewBridge bridge = null;
        OwXMLUtil dialogHanlder = m_configNode.getSubUtil(DIALOG_HANDLER_ELEMENT_NAME);
        String jspKey = dialogHanlder.getSafeTextValue("JspFormKeyPattern", DEFAULT_EMPTY_STRING);
        if (jspKey != DEFAULT_EMPTY_STRING)
        {
            bridge = new OwFormPropertyViewBridge(createJspKeyPatternPropertyView(jspKey));
        }
        else
        {
            bridge = new OwStandardPropertyViewBridge(createKeyPatternPropertyView());
        }
        return bridge;
    }

    /**
     * Create the JSP based view for rendering properties involved in key generation.
     * @param jspFile_p - the path to JSP file
     * @return - the JSP based view for rendering properties involved in key generation.
     */
    protected OwObjectPropertyFormularView createJspKeyPatternPropertyView(String jspFile_p) throws Exception
    {
        OwEFileKeyPropertiesFormView view = new OwEFileKeyPropertiesFormView();
        view.setJspFormular(jspFile_p);
        return view;
    }

    /** 
     * Create the property view for properties involved in key generation
     * @return the newly created {@link OwEFileKeyPropertiesView} object
     */
    protected OwEFileKeyPropertiesView createKeyPatternPropertyView()
    {
        return new OwEFileKeyPropertiesView();
    }

    /** the object class for the folder was changed, update the skeleton
     */
    protected void updateObjectClass() throws Exception
    {
        super.updateObjectClass();

        if (m_useKeyPropertyView)
        {
            enableTabView(m_keyPatternViewTabIndex, true);
            m_keyPatternPropertyBridge.setObjectRef(m_sceletonObject, false);

            m_keyPatternPropertyBridge.setPropertyListConfiguration(m_keysPropertiesInfos);
        }
        else
        {
            if (!m_propertyPatternConfiguration.isEmpty())
            {
                getDocument().saveGeneratedProperties(m_sceletonObject, new OwStandardPropertyCollection());
            }
        }
    }

    /**
     * Set the properties information involved in key generation.
     * The keys involved in key generation are read-only.
     * @param propertiesInfos_p
     */
    protected void setKeysPropertyInfos(Collection<OwPropertyInfo> propertiesInfos_p)
    {
        if (propertiesInfos_p == null || propertiesInfos_p.isEmpty())
        {
            m_keysPropertiesInfos = null;
        }
        else
        {
            m_keysPropertiesInfos = new OwPropertyListConfiguration();
            OwSimplePropertySubregion<OwPropertyInfo> keyRegion = new OwSimplePropertySubregion<OwPropertyInfo>(new LinkedList<OwPropertyInfo>(propertiesInfos_p));
            m_keysPropertiesInfos.addRegion(keyRegion);
        }

    }

    /** overridable to create properties view
     * 
     * @return {@link OwObjectPropertyView}
     */
    protected OwObjectPropertyView createObjectPropertyView() throws Exception
    {
        OwContractPropertiesView owContractPropertiesView = new OwContractPropertiesView(m_propertyPatternConfiguration);

        return owContractPropertiesView;
    }

    /**
     * Activate / deactivate navigation tab.
     * @param tabIndex_p
     */
    private void enableTabView(int tabIndex_p, boolean enabledStatus_p)
    {
        if (tabIndex_p >= 0 && tabIndex_p < m_SubNavigation.getTabList().size())
        {
            ((OwTabInfo) m_SubNavigation.getTabList().get(tabIndex_p)).setDisabled(!enabledStatus_p);
        }
    }

    /**
     * Create an {@link OwContractPropertyFormView} object which has a special handling.
     * @return the newly created contract bridge.
     */
    protected OwFormPropertyViewBridge createFormViewBridge()
    {
        OwObjectPropertyFormularView view = new OwContractPropertyFormView();
        view.setJspConfigurator(getJspConfigurator());
        return new OwFormPropertyViewBridge(view);
    }

    @Override
    public OwContractDocument getDocument()
    {
        return (OwContractDocument) super.getDocument();
    }
}
