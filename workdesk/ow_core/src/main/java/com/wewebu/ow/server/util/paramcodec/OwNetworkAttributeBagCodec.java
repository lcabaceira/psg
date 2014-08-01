package com.wewebu.ow.server.util.paramcodec;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * {@link OwNetwork} based implementation of the {@link OwAttributeBagCodec}.<br>
 * Uses an instance of {@link OwNetwork} to access attribute bags.  
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
 *@see OwAttributeBag
 *@see OwAttributeBagWriteable
 *@since 3.0.0.0
 */
public class OwNetworkAttributeBagCodec extends OwAttributeBagCodec
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwNetworkAttributeBagCodec.class);

    public static final String DEFAULT_CODEC_BAGS_PSEUDO_USER = "att_bag_codec";

    private OwNetwork m_network;
    private String m_codecBagsUser = DEFAULT_CODEC_BAGS_PSEUDO_USER;

    /**
     * Creates a new {@link OwNetworkAttributeBagCodec} based on the bootstrap configuration that is <br>
     * accessed through the given {@link OwMainAppContext}.<br>
     * The expected  XML structure of the URLParameterCodec tag element is the following : <br>
     *<br>
     *   URLLifeTime - tag<br>
     *       Configures the amount of time an encoded URL parameter map is valid.<br>
     *       After that the parameter map is considered expired and links that relay<br>
     *       on are considered out of date / expired and will not produce the desired response.<br>
     *       <br>
     *       Attributes :<br>
     *          days    = days until the parameter map expires considering the URL parameter map encoding time<br>  
     *          hours   = hours until the parameter map expires considering the URL parameter map encoding time<br>
     *          minutes = minutes until the parameter map expires considering the URL parameter map encoding time<br>                             
     *          seconds = seconds until the parameter map expires considering the URL parameter map encoding time<br>
     * <br>   
     * <br> 
     *   AttributeBags - tag<br>
     *       Configures the attribute bag storage name for the parameters maps.<br>
     *       In order to prevent name conflicts in attribute bags usage unique bag names prefixes should <br>
     *       be specified here.<br>
     * <br>      
     *       Attributes :<br>
     *           codecprefix     = a string prefix to be used in codec internal bag names<br>
     *           cookieprefix    = a string prefix to be used in codec internal bag names that will store<br>
     *                               cookie based parameter maps<br>
     *           user            = user to be used when storing codec attribute bags<br>
     *    <br>    
     *  CookieParameter - tag  <br>
     *       The parameter map codec encodes a parameter map into another parameter map containing<br>
     *       one cookie defining parameter.  <br>
     *       <br>
     *      Attributes :<br>
     *           name = string name of the URL parameter<br>
     * @param context_p
     * @return the configured codec
     * @throws OwInvalidOperationException
     */
    public static OwNetworkAttributeBagCodec createConfiguredCodec(OwMainAppContext context_p) throws OwInvalidOperationException
    {
        try
        {
            OwNetwork network_p = context_p.getNetwork();
            OwConfiguration configuration = context_p.getConfiguration();
            OwXMLUtil bootstrapConfiguration = configuration.getBootstrapConfiguration();
            OwXMLUtil codecConfiguration = bootstrapConfiguration.getSubUtil(CONFIGURATION_BOOTSTRAP_ELEMENT_NAME);

            long urlLifeTime = DEFAULT_CONFIG_URL_LIFE_TIME;
            String codecBagsPrefix = DEFAULT_CODEC_BAGS_PREFIX;
            String cookieBagsPrefix = DEFAULT_COOKIE_BAGS_PREFIX;
            String user = DEFAULT_CONFIG_BAG_USER_NAME;
            String cookieParameter = DEFAULT_URL_PARAMETER_NAME;

            if (codecConfiguration == null)
            {
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): configuration is missing ! Proceeding with the following default values:");
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): default URL life time  	        :" + urlLifeTime + " ms");
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): default codec bags prefix        :" + codecBagsPrefix);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): default cookie bags prefix       :" + cookieBagsPrefix);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): default bags user name           :" + user);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): default URL parameter name    :" + cookieParameter);
            }
            else
            {
                urlLifeTime = loadUrlLifeTimeConfig(codecConfiguration);
                cookieParameter = loadUrlParameterConfig(codecConfiguration);
                String[] attributeBagsConfig = loadAttributeBagsConfiguation(codecConfiguration);
                codecBagsPrefix = attributeBagsConfig[0];
                cookieBagsPrefix = attributeBagsConfig[1];
                user = attributeBagsConfig[2];
            }

            if (codecConfiguration != null)
            {
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): Creating codec with the following loaded configuration :");
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): URL life time          :" + urlLifeTime + " ms");
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): codec bags prefix        :" + codecBagsPrefix);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): cookie bags prefix       :" + cookieBagsPrefix);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): bags user name           :" + user);
                LOG.debug("OwNetworkAttributeBagCodec.createConfiguredCodec(): URL parameter name    :" + cookieParameter);
            }

            return new OwNetworkAttributeBagCodec(network_p, cookieParameter, urlLifeTime, codecBagsPrefix, cookieBagsPrefix, user);
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not load the configured OwNetworkAttributeBagCodec!", e);
        }
    }

    /**
     * 
     * @param netowrk_p the {@link OwNetwork} instance used to access attribute bags 
     * @param cookieParameterName_p
     * @param namesExpirationTime_p
     * @param codecBagsPrefix_p
     * @param cookieBagsPrefix_p
     * @param codecBagsUser_p
     */
    public OwNetworkAttributeBagCodec(OwNetwork netowrk_p, String cookieParameterName_p, long namesExpirationTime_p, String codecBagsPrefix_p, String cookieBagsPrefix_p, String codecBagsUser_p)
    {
        super(cookieParameterName_p, namesExpirationTime_p, codecBagsPrefix_p, cookieBagsPrefix_p);
        this.m_network = netowrk_p;
        this.m_codecBagsUser = codecBagsUser_p;
    }

    protected OwAttributeBagWriteable getBag(String bagName_p, boolean create_p) throws Exception
    {
        OwAttributeBagWriteable attributeBagWriteable = (OwAttributeBagWriteable) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, bagName_p, m_codecBagsUser, true, create_p);
        return attributeBagWriteable;
    }

}