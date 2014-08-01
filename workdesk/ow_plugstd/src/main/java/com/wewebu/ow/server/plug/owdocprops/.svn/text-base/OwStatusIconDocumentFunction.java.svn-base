package com.wewebu.ow.server.plug.owdocprops;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.plug.owdocprops.iconrule.OwStatusIconExpressionRule;
import com.wewebu.ow.server.plug.owdocprops.iconrule.OwStatusIconRule;
import com.wewebu.ow.server.plug.owdocprops.iconrule.OwStatusIconSimpleRule;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A document function that displays a status icon dependent on document property states.
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
public class OwStatusIconDocumentFunction extends OwDocumentFunction
{
    /** package logger for the class */
    //    private static final Logger LOG = OwLog.getLogger(OwStatusIconDocumentFunction.class);

    /** the applicable icon rules */
    private List<OwStatusIconRule> m_rules;

    // === members
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        m_rules = new LinkedList<OwStatusIconRule>();

        // load rules
        Iterator<?> it = node_p.getSubUtil("IconRules").getSafeUtilList(null).iterator();
        while (it.hasNext())
        {
            OwXMLUtil n = (OwXMLUtil) it.next();
            OwStatusIconRule rule = createStatusIconRule(n, context_p);
            m_rules.add(rule);
        }
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owstatus/hasminorversions.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owstatus/hasminorversions_24.png");
    }

    /** get property from the XML plugin config node */
    public boolean getObjectInstance()
    {
        // always true
        return true;
    }

    /** get property from the XML plugin config node */
    public boolean getContextMenu()
    {
        // always false
        return false;
    }

    /** get property from the XML plugin config node */
    public boolean getMultiselect()
    {
        // always false
        return false;
    }

    /** check if the plugin handles events with function calls, 
     *  or if it is just used to display an icon.
     *  The default is false, i.e. plugin handles events.
     *
     *  @return boolean true = handles events, false = does not handle events
     */
    public boolean getNoEvent()
    {
        // always true, just show state, do not handle click
        return true;
    }

    /** get the HTML code for the small (16x16 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getIconHTML(OwObject oObject_p, OwObject oParent_p) throws Exception
    {
        this.addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        // apply rules first match count's
        for (OwStatusIconRule rule : m_rules)
        {
            if (rule.canApply(oObject_p, oParent_p))
            {
                StringBuilder result = new StringBuilder("<img class=\"OwFunctionIcon\" ");

                if (rule.getDescription() != null)
                {
                    String tooltip = getDisplayName(getContext().getLocale(), rule.getDescription());
                    result.append("alt=\"");
                    result.append(tooltip);
                    result.append("\" title=\"");
                    result.append(tooltip);
                    result.append("\" ");
                }

                result.append("src=\"");
                result.append(getContext().getDesignURL());
                result.append(rule.getIcon());
                result.append("\" />");
                return result.toString();
            }
        }

        // don't show anything, no rule matched
        return "";
    }

    public String getDisplayName(Locale locale_p, String description)
    {
        return OwString.localizeLabel(locale_p, description);
    }

    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // ignore
    }

    /**
     * Getter of configured rules list.
     * @return List of OwStatusIconRule
     * @since 4.2.0.0
     */
    protected List<OwStatusIconRule> getRules()
    {
        return this.m_rules;
    }

    /**
     * Factory method for OwStatusIconRule instances from provided configuration
     * @param config OwXMLUtil defined configuration of OwStatusIconRule
     * @param context OwAppContext current Application context
     * @return OwStatusIconRule
     * @throws OwException if unsupported or incorrect configured
     */
    protected OwStatusIconRule createStatusIconRule(OwXMLUtil config, OwAppContext context) throws OwException
    {
        String nodeName = config.getNode().getNodeName();
        if ("rule".equals(nodeName))
        {
            return new OwStatusIconSimpleRule(config, context);
        }
        else if ("xrule".equals(nodeName))
        {
            return new OwStatusIconExpressionRule(config, context);
        }
        else
        {
            throw new OwConfigurationException(context.localize1("OwStatusIconDcoumentFunction.createStatusIconRule.invalidDef", "Unsupported definition %1 found in IconRules configuration.", nodeName));
        }
    }

}