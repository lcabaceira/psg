package com.wewebu.ow.server.app;

/**
 *<p>
 * Factory that creates a operation listener that log user events in attribute bag. 
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
public class OwLogUserOperationListenerFactory extends OwContextBasedUOListenerFactory
{
    /** constructor*/
    public OwLogUserOperationListenerFactory(OwMainAppContext context_p)
    {
        super(context_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwUserOperationListenerFactory#createListener()
     */
    public OwUserOperationListener createListener() throws Exception
    {
        return new OwLogBasedUserOperationListener();
    }

}
