package com.wewebu.ow.server.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *<p>
 * Base interface for a single settings property used in the OwSettings for the plugins.<br/>
 * Unlike configuration node entries, the settings are stored for each user or for a site, 
 * where the configuration via getConfigNode is only set during startup. <br/><br/>
 * You can retrieve settings by calling getSafeSetting form within your plugin function, document or view class.
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
public interface OwSettingsProperty
{
    /** node name of a list item for list properties */
    public static final String ITEM_VALUE_NODE = "property";

    /** set the default value
     */
    public abstract void setDefault();

    /** return the property value as a DOM Node for serialization
     *
     * @param doc_p DOM Document to add to
     *
     * @return Node
     */
    public abstract Node getValueNode(Document doc_p);

    /** get flag indicating if property can be edited
     * @return boolean true = editable
     */
    public abstract boolean isUser();

    /** get flag indicating if property can be edited
     * @return boolean true = editable
     */
    public abstract boolean isEditable();

    /** get ID of property
     * @return String with ID 
     */
    public abstract String getName();

    /** set current value of property, to be overridden
     *
     * @param propertyDefinitionNode_p the node which defines the property in the plugin descriptors setting
     * @param valueNode_p the node with the current value
     * @param strSetName_p name of the property set for which the property is created
     */
    public abstract void init(Node propertyDefinitionNode_p, Node valueNode_p, String strSetName_p) throws Exception;

    /** get current value of property
     * @return Object if isList() is true, Object otherwise
     */
    public abstract Object getValue();

    /** set current value of property, to be overridden
     *
     * @param value_p Object
     */
    public abstract void setValue(Object value_p);
}