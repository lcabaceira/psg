package com.wewebu.ow.server.app;

/**
 *<p>
 * Abstract class for user operation listener factory. Subclasses of this class 
 * will be used in <code>owbootstrap.xml</code> configuration file for dynamic creation 
 * of user operation listeners.
 * Used to decouple the listeners from {@link OwMainAppContext}, to provide an easier 
 * usage of listeners in another applications than OWD. 
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

public abstract class OwContextBasedUOListenerFactory implements OwUserOperationListenerFactory
{
    /** the context*/
    protected OwMainAppContext context;

    /** constructor */
    public OwContextBasedUOListenerFactory(OwMainAppContext context_p)
    {
        this.context = context_p;
    }
}