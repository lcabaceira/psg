package com.wewebu.ow.server.util;

import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 *<p>
 * Utility interface for structured configuration data access.
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
@SuppressWarnings("rawtypes")
public interface OwXMLUtil
{
    // === new methods to prevent direct DOM access, see Ticket 794

    /**
     * Return the name (tag-name) of this instance
     * @return String (can return null)
     * @since 4.2.0.0
     */
    String getName();

    /** get a sub util node with the given name
     * 
     * @param nodeName_p the subnode name
     * @return OwXMLUtil or null if not found
     * @throws Exception
     */
    public abstract OwXMLUtil getSubUtil(String nodeName_p) throws Exception;

    /** get a list with OwXMLUtil's with the given subname
     * 
     * @param nodeName_p the subnode name
     * @param itemName_p the name of the item's or null to retrieve all items
     * @return a {@link List}
     */
    public abstract List getSafeUtilList(String nodeName_p, String itemName_p);

    /** get a list with OwXMLUtil's
     * 
     * @param itemName_p the name of the item's or null to retrieve all items
     * 
     * @return a {@link List}
     */
    public abstract List getSafeUtilList(String itemName_p);

    // === deprecated methods to prevent direct DOM access, see Ticket 794

    /** return the wrapped DOM Node reference
     * @return Node
     */
    public abstract Node getNode();

    /** get the subnode with the given tagname
     * @param strNodeName_p tag name of requested node
     * @return org.w3c.dom.Node
     */
    public abstract Node getSubNode(String strNodeName_p) throws Exception;

    /** retrieve a node list in a subnode 
    *
    * @return List of DOM Nodes
    */
    public abstract List getSafeNodeList();

    /** retrieve a node list in a subnode 
    *
    * @param strNodeName_p String name of the subnode with the node list
    * @return List of DOM Nodes
    */
    public abstract List getSafeNodeList(String strNodeName_p);

    // === existing methods
    /** write configuration as HTML to a writer object
     * @param w_p a {@link Writer} 
     */
    public abstract void writeHtmlDump(Writer w_p) throws Exception;

    /** retrieve a string list in the node 
    *
    * @return List of Strings
    */
    public abstract List getSafeStringList();

    /** retrieve a CDATA string list in the node 
    *
    * @return List of Strings
    */
    public abstract List getSafeCDATAList(String strNodeName_p);

    /** retrieve a CDATA string list in a subnode 
    *
    * @return List of Strings
    */
    public abstract List getSafeCDATAList();

    /** retrieve a string list in a subnode 
    *
    * @param strNodeName_p String name of the subnode with the string list
    * @return List of Strings
    */
    public abstract List getSafeStringList(String strNodeName_p);

    /** retrieve a string list in a subnode 
    *
    * @param strNodeName_p String name of the subnode with the string list
    * @return List of Strings
    */
    public abstract Set getSafeStringSet(String strNodeName_p);

    /** get the value of a string attribute, catch exception 
     * @param strAttributeName_p name of the String attribute
     * @param strDefault_p Default string in case the attribute could not be found
     * @return string value of attribute or strDefault_p on failure
     */
    public abstract String getSafeStringAttributeValue(String strAttributeName_p, String strDefault_p);

    /** get the value of a string attribute, catch exception 
     * @param strAttributeName_p name of the String attribute
     * @param iDefault_p Default int in case the attribute could not be found
     * 
     * @return int value of attribute or iDefault_p on failure
     */
    public abstract int getSafeIntegerAttributeValue(String strAttributeName_p, int iDefault_p);

    /** get the value of a boolean attribute [true | false], catch exception 
     * @param strAttributeName_p name of the String attribute
     * @param fDefault_p Default string in case the attribute could not be found
     * @return boolean value of attribute or fDefault_p on failure
     */
    public abstract boolean getSafeBooleanAttributeValue(String strAttributeName_p, boolean fDefault_p);

    /** get the value of a sub boolean node [true | false], catch exception 
     * @param strNodeName_p name of the subnode
     * @param fDefault_p Default value in case the node could not be found
     * @return boolean value of node or fDefault_p on failure
     */
    public abstract boolean getSafeBooleanValue(String strNodeName_p, boolean fDefault_p);

    /** get the value of a sub Integer node, catch exception 
     * @param strNodeName_p name of the subnode
     * @param fDefault_p Default value in case the node could not be found
     * @return int value of node or fDefault_p on failure
     */
    public abstract int getSafeIntegerValue(String strNodeName_p, int fDefault_p);

    /** get the value of a sub text node, catch exception 
     *
     * @param strNodeName_p name of the text subnode
     * @param strDefault_p Default string in case the node could not be found
     * @return string value of subnode or strDefault_p on failure
     */
    public abstract String getSafeTextValue(String strNodeName_p, String strDefault_p);

    /**
     * get the value of THE node, catch exception
     * 
     * @param strDefault_p
     *            Default string in case the node could not be found
     * 
     * @return string value of subnode or strDefault_p on failure
     */
    public abstract String getSafeTextValue(String strDefault_p);

    /**
     * Helper method to create an URL from given configuration node.
     * @param nodeName_p String name of child node, where to extract the URL
     * @return java.net.URL
     * @throws MalformedURLException if the extracted text is not URL conform string
     * @since 4.0.0.0 
     */
    public abstract URL getURLFromNode(String nodeName_p) throws MalformedURLException;

    /** write configuration as HTML to a writer object and filter xml tags 
     * @param htmlWriter_p a {@link Writer} 
     * @param hiddenTags {@link java.util.Map}
     */
    public abstract void writeHtmlDumpFiltered(Writer htmlWriter_p, Map<String, String> hiddenTags) throws Exception;

}