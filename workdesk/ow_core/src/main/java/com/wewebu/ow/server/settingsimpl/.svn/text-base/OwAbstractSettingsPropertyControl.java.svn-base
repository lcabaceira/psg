package com.wewebu.ow.server.settingsimpl;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSettingsPropertyControl;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Default abstract implementation of OwSettingsPropertyControl.
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
public abstract class OwAbstractSettingsPropertyControl extends OwSettingsPropertyControl
{

    /** the defining node from the plugin descriptors setting set 
     * @deprecated since 4.2.0.0 use {@link #getPropertyDefinitionNode()}*/
    protected Node m_propertyDefinitionNode;

    private Node propertyDefinitionNode;

    /** set name */
    protected String m_strSetName;

    /** external form view overrides internal form */
    protected OwEventTarget m_externalFormEventTarget;

    @Override
    public void init(Node propertyDefinitionNode_p, Node valueNode_p, String strSetName_p) throws Exception
    {
        this.m_propertyDefinitionNode = propertyDefinitionNode_p;//backwards compatibility
        this.propertyDefinitionNode = propertyDefinitionNode_p;
        this.m_strSetName = strSetName_p;
    }

    @Override
    public String getHelpPath()
    {
        return OwXMLDOMUtil.getSafeStringAttributeValue(getPropertyDefinitionNode(), "helppath", null);
    }

    @Override
    public boolean isUser()
    {
        return OwXMLDOMUtil.getSafeStringAttributeValue(getPropertyDefinitionNode(), OwBaseConfiguration.PLUGIN_SETATTR_SCOPE, "").equals("user");
    }

    @Override
    public boolean isEditable()
    {
        return OwXMLDOMUtil.getSafeBooleanAttributeValue(getPropertyDefinitionNode(), OwBaseConfiguration.PLUGIN_SETATTR_EDIT, false);
    }

    @Override
    public String getDisplayName()
    {
        return ((OwMainAppContext) getContext()).getConfiguration().getLocalizedPluginSettingTitle(getPropertyDefinitionNode(), m_strSetName);
    }

    /** get ID of property
     * @return String which is basically the NodeName 
     */
    @Override
    public String getName()
    {
        return getPropertyDefinitionNode().getNodeName();
    }

    @Override
    protected void init() throws Exception
    {
    }

    /**
     * get a reference to the property definition node 
     * @return Node (or null if not initialized yet)
     */
    protected Node getPropertyDefinitionNode()
    {
        //For backwards compatibility check old reference, super.init call may be misssing
        return propertyDefinitionNode == null ? m_propertyDefinitionNode : propertyDefinitionNode;
    }

    /** get the form used for the edit fields
    *
    * @return String form name
    */
    public String getFormName()
    {
        if (this == m_externalFormEventTarget)
        {
            return super.getFormName(); // we do not render a own form, must be provided externally
        }
        else
        {
            return m_externalFormEventTarget.getFormName();
        }
    }

    @Override
    public void setExternalFormTarget(OwEventTarget eventTarget_p) throws Exception
    {
        m_externalFormEventTarget = eventTarget_p;
    }

    public OwEventTarget getFormTarget()
    {
        return m_externalFormEventTarget;
    }

    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        ret.append("\r\n");
        ret.append(getClass().getSimpleName());
        ret.append(": [");
        ret.append("getDisplayName() = ").append(getDisplayName());
        ret.append(", getValue() = ").append(getValue());
        ret.append(", getName() = ").append(getName());
        ret.append(", isUserScope() = ").append(isUser());
        ret.append(", isEditable() = ").append(isEditable());
        ret.append("]");

        return ret.toString();
    }
}
