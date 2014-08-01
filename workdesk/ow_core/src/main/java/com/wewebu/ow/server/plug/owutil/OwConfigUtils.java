package com.wewebu.ow.server.plug.owutil;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Static class with utility functions to create simple parameter from plugin descriptor.
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
public class OwConfigUtils
{

    /** helper function to get the view mask bit from the config node
     * use this function to create mask bits out of XML descriptions to easily
     * configure your dialogs and views.<br>
     * This method assumes false as default value.
     * 
     * @param configNode_p OwXMLUtil plugin descriptor
     * @param strNodeName_p String XML node name in plugin descriptor which has true or false
     * @param iMaskBit_p int a bit value to associate with in case of true
     *
     * @return int mask bit if node name is set otherwise 0
     *
     */
    public static int computeViewMaskFromConfig(OwXMLUtil configNode_p, String strNodeName_p, int iMaskBit_p)
    {
        // delegate to same method with default value 'false'
        return computeViewMaskFromConfig(configNode_p, strNodeName_p, iMaskBit_p, false);
    }

    /** helper function to get the view mask bit from the config node
     * use this function to create mask bits out of XML descriptions to easily
     * configure your dialogs and views.<br>
     * The default value of this config option can be configured.
     * 
     * @param configNode_p OwXMLUtil plugin descriptor
     * @param strNodeName_p String XML node name in plugin descriptor which has true or false
     * @param iMaskBit_p int a bit value to associate with in case of true
     * @param fDefault_p the default value for this config option
     *
     * @return int mask bit if node name is set otherwise 0
     *
     */
    public static int computeViewMaskFromConfig(OwXMLUtil configNode_p, String strNodeName_p, int iMaskBit_p, boolean fDefault_p)
    {
        if (configNode_p.getSafeBooleanValue(strNodeName_p, fDefault_p))
        {
            return iMaskBit_p;
        }
        else
        {
            return 0;
        }
    }

    /** helper function to get the view mask bit from the config node's show flag attribute
     * use this function to create mask bits out of XML descriptions to easily
     * configure your dialogs and views.<br>
     * The default value of this config option can be configured.
     * 
     * @param configNode OwXMLUtil plugin descriptor
     * @param nodeName String XML node name in plugin descriptor which has true or false
     * @param showFlagAttribute show falg boolean attribute name  
     * @param maskBit int a bit value to associate with in case of true
     * @param defaultShowValue the default value for this config option
     *
     * @return int mask bit if node name is set otherwise 0
     * @throws OwException 
     * @since 4.1.1.0
     */
    public static int computeViewMaskFromConfig(OwXMLUtil configNode, String nodeName, String showFlagAttribute, int maskBit, boolean defaultShowValue) throws OwException
    {

        try
        {
            OwXMLUtil subUtil = configNode.getSubUtil(nodeName);

            if (subUtil != null)
            {
                if (subUtil.getSafeBooleanAttributeValue(showFlagAttribute, defaultShowValue))
                {
                    return maskBit;
                }
            }

            return 0;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Invalid configuration of node " + nodeName, e);
        }
    }

    /** get the view read-only mask bit from the config node for the given config node, only if read-only attribute is set
     *
     * @param configNode_p OwXMLUtil plugin descriptor
     * @param strNodeName_p String XML node name in plugin descriptor which has true or false
     * @param iMaskBit_p int a bit value to associate with in case of true
     *
     * @return int mask bit if node name is set otherwise 0
     *
     *
    */
    public static int computeReadOnlyViewMaskFromConfig(OwXMLUtil configNode_p, String strNodeName_p, int iMaskBit_p)
    {
        try
        {
            if (OwXMLDOMUtil.getSafeBooleanAttributeValue(configNode_p.getSubNode(strNodeName_p), "readonly", false))
            {
                return iMaskBit_p;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            return 0;
        }
    }

}
