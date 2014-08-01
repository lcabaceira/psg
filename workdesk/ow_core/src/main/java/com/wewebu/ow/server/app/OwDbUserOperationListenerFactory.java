package com.wewebu.ow.server.app;

import com.wewebu.ow.server.util.OwTableSpec;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Factory that creates a operation listener that log user events using a logger (can be a db logger too).
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
 * @since 3.1.0.3
 */
public class OwDbUserOperationListenerFactory extends OwContextBasedUOListenerFactory
{
    /** constructor*/
    public OwDbUserOperationListenerFactory(OwMainAppContext context_p)
    {
        super(context_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwUserOperationListenerFactory#createListener()
     */
    public OwUserOperationListener createListener() throws Exception
    {
        OwXMLUtil bootstrapConfiguration = context.getConfiguration().getBootstrapConfiguration();
        OwXMLUtil ecmAdapterUtil = bootstrapConfiguration.getSubUtil("EcmAdapter");

        OwXMLUtil attributeBagTableXML = ecmAdapterUtil.getSubUtil("DbAttributeBagTableName");
        OwTableSpec DB_ATTRIBUTE_BAG_TABLE;
        if (null != attributeBagTableXML)
        {
            DB_ATTRIBUTE_BAG_TABLE = OwTableSpec.fromXML(attributeBagTableXML);

        }
        else
        {
            DB_ATTRIBUTE_BAG_TABLE = new OwTableSpec();
        }

        OwAttrBagBasedUserOperationListener listener = new OwAttrBagBasedUserOperationListener(context.getJDBCTemplate(), DB_ATTRIBUTE_BAG_TABLE);
        return listener;

    }

}
