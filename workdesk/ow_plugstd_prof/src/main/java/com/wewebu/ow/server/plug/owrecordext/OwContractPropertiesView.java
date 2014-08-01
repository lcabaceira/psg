package com.wewebu.ow.server.plug.owrecordext;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;

/**
 *<p>
 * Property view for an eFile object.
 * Properties involved in creation of the unique key must be read-only in this view.
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwContractPropertiesView extends OwObjectPropertyView
{
    /**Class logger*/
    private static Logger LOG = OwLog.getLogger(OwContractPropertiesView.class);
    /** keep a collection of modified properties in previous view */
    private OwPropertyCollection m_previousProperties;
    /** configuration key name - key generator*/
    private Map<String, ? extends Object> m_propertyGeneratorConfiguration;

    /**
     * Constructor
     * @param propertyGeneratorConfiguration_p 
     */
    public OwContractPropertiesView(Map<String, ? extends Object> propertyGeneratorConfiguration_p)
    {
        m_previousProperties = new OwStandardPropertyCollection();
        m_propertyGeneratorConfiguration = propertyGeneratorConfiguration_p;
    }

    /**
     * Set the value for properties changed in previous view.
     * @param changedProperties_p 
     * throws Exception if cannot get current properties using {@link #getFilteredClonedProperties()}
     */
    public void mergeProperties(OwPropertyCollection changedProperties_p) throws Exception
    {
        m_previousProperties = changedProperties_p;
        if (changedProperties_p != null)
        {
            getFilteredClonedProperties().putAll(changedProperties_p);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#setObjectRefEx(com.wewebu.ow.server.ecm.OwObject, boolean, java.util.Collection)
     */
    @Deprecated
    public void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, Collection propertyInfos_p) throws Exception
    {
        LinkedHashSet<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> mergedPropertiesInfo = new LinkedHashSet<org.alfresco.wd.ui.conf.prop.OwPropertyInfo>();
        if (propertyInfos_p == null || propertyInfos_p.isEmpty())
        {
            OwPropertyCollection allProps = objectRef_p.getClonedProperties(null);
            Iterator entryIt = allProps.entrySet().iterator();
            while (entryIt.hasNext())
            {
                Entry entry = (Entry) entryIt.next();
                String propName = (String) entry.getKey();
                OwProperty prop = (OwProperty) entry.getValue();
                if (prop != null && !prop.isHidden(m_ReadOnlyContext))
                {
                    mergedPropertiesInfo.add(new org.alfresco.wd.ui.conf.prop.OwPropertyInfo(propName, false));
                }
            }
        }
        else
        {
            mergedPropertiesInfo.addAll(propertyInfos_p);//filter props first, and overwrite afterwards
        }
        mergedPropertiesInfo.addAll(getDocument().getAdditionalReadOnlyKeys());//always add key props
        super.setObjectRefEx(objectRef_p, showSystemProperties_p, mergedPropertiesInfo);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#update(javax.servlet.http.HttpServletRequest)
     */
    protected OwPropertyCollection update(HttpServletRequest request_p) throws Exception
    {
        OwPropertyCollection result = new OwStandardPropertyCollection();
        if (m_previousProperties != null)
        {
            result.putAll(m_previousProperties);
        }
        OwPropertyCollection update = super.update(request_p);
        if (update != null)
        {
            result.putAll(update);
        }
        //not all mandatory fields are updated!
        if (!m_theFieldManager.getUpdateStatus())
        {
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#informUserOnSuccess()
     */
    protected void informUserOnSuccess()
    {
        Map<String, Object> localizedNameValues = new LinkedHashMap<String, Object>();
        String localizedMessage = "";
        if (m_propertyGeneratorConfiguration != null && !m_propertyGeneratorConfiguration.isEmpty())
        {
            Iterator<String> keysIterator = m_propertyGeneratorConfiguration.keySet().iterator();
            StringBuilder generatedKeysBuffer = new StringBuilder(" ");
            OwPropertyCollection props = new OwStandardPropertyCollection();
            try
            {
                props = getFilteredClonedProperties();
            }
            catch (Exception e1)
            {
                LOG.error("Unable to access current properties", e1);
            }
            while (keysIterator.hasNext())
            {
                String propName = keysIterator.next();
                OwProperty prop = (OwProperty) props.get(propName);
                if (prop != null)
                {
                    Object value = "";
                    if (generatedKeysBuffer.length() > 1)
                    {
                        generatedKeysBuffer.append(", ");
                    }
                    try
                    {
                        value = prop.getValue();
                        if (value == null)
                        {
                            value = "";
                        }
                        try
                        {
                            String dspName = prop.getFieldDefinition().getDisplayName(getContext().getLocale());
                            localizedNameValues.put(dspName, value);
                            generatedKeysBuffer.append(dspName);
                        }
                        catch (Exception e)
                        {
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("Cannot resolve property with name " + propName);
                            }

                            localizedNameValues.put(propName, value);
                            generatedKeysBuffer.append(propName);
                        }
                    }
                    catch (Exception e)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Cannot get value for property with name " + propName + ". Empty string will be used.");
                        }

                        localizedNameValues.put(propName, "");
                        generatedKeysBuffer.append(propName);
                    }
                    generatedKeysBuffer.append(" - ");
                    generatedKeysBuffer.append(value);
                }
            }
            if (localizedNameValues.size() > 1)
            {
                localizedMessage = getContext().localize1("plugin.com.wewebu.ow.server.plug.owrecordext.OwContractPropertiesView.contractsaved.properties", "Contract successfully saved. Generated keys are: %1", generatedKeysBuffer.toString());
            }
            else
            {
                localizedMessage = getContext().localize1("plugin.com.wewebu.ow.server.plug.owrecordext.OwContractPropertiesView.contractsaved.property", "Contract successfully saved. Generated key is: %1", generatedKeysBuffer.toString());
            }
        }
        else
        {
            localizedMessage = getContext().localize("app.OwObjectPropertyView.saved", "Changes have been saved.");
        }
        ((OwMainAppContext) getContext()).postMessage(localizedMessage);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        mergeProperties(getDocument().getGeneratedProperties());
    }

    @Override
    public OwContractDocument getDocument()
    {
        return (OwContractDocument) super.getDocument();
    }

    @Override
    protected boolean isPropertyReadonly(OwProperty property_p) throws Exception
    {
        Set<org.alfresco.wd.ui.conf.prop.OwPropertyInfo> keyProps = getDocument().getAdditionalReadOnlyKeys();
        if (keyProps != null && !keyProps.isEmpty())
        {
            for (org.alfresco.wd.ui.conf.prop.OwPropertyInfo propInfo : keyProps)
            {
                if (propInfo.getPropertyName().equals(property_p.getPropertyClass().getClassName()))
                {
                    return true;
                }
            }
        }
        return super.isPropertyReadonly(property_p);
    }
}
