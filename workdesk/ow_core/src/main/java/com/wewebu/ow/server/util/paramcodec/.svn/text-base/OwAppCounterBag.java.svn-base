package com.wewebu.ow.server.util.paramcodec;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * This class represent an application counter generator<br>
 * A bag is used for the unique index storage. <br/> 
 * The index name uniqueness is guaranteed per virtual machine.<br/>
 * Attribute bag access is also synchronized per virtual machine.<br/>
 * This class is not suitable for cluster based usage.<br/>
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
public class OwAppCounterBag extends OwAttributeBagCodec
{
    /** The name of the bag where current counter is stored */
    private static final String APP_COUNTER_MASTER_BAG = "APP_COUNTER_MASTER_BAG";
    /** The generic user name used to store attributes. */
    private static final String APP_COUNTER_USER = "APP_COUNTER_USER";
    /** The network object */
    private OwNetwork m_network;

    /**
     * Constructor
     */
    public OwAppCounterBag(OwNetwork network_p)
    {
        //no prefixes are needed
        //no expiration time is needed
        //no cookie parameter name is needed
        super("", 0, "", "");
        m_network = network_p;
    }

    /**
     * Get the bag from the network
     * @param bagName_p -  the name of the bag
     */
    protected OwAttributeBagWriteable getBag(String bagName_p, boolean create_p) throws Exception
    {
        OwAttributeBagWriteable attributeBagWriteable = (OwAttributeBagWriteable) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, bagName_p, APP_COUNTER_USER, true, create_p);
        return attributeBagWriteable;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.util.paramcodec.OwAttributeBagCodec#getInitialValueForIndex()
     */
    protected long getInitialValueForIndex()
    {
        return 0;
    }

    /**
     * Get the name for the master bag.
     * @return the master bag name  
     */
    protected String masterBagName()
    {
        return APP_COUNTER_MASTER_BAG;
    }

}
