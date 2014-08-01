package com.wewebu.ow.server.ui.viewer;

/**
 *<p>
 * Registry to retrieve OwInfoProvider.
 * Interface which must be implement by classes,
 * which should be used as registry for information
 * provider.
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
 *@since 3.1.0.0
 */
public interface OwInfoProviderRegistry
{
    /**
     * prefix used to request annotation provider
     * for a given DMS prefix.
     */
    public static final String PREFIX_ANNOT = "annot_";

    /**
     * Method to retrieve a provider regarding the given context.
     * <p>Attention this can return null if the context
     * is null or no provider exist for this context</p>
     * @param context_p String context to be handled
     * @return OwInformationProvider or null
     */
    OwInfoProvider getInfoProvider(String context_p);

    /**
     * Method called to register a provider for specific context.
     * @param context_p String representing the context
     * @param pro_p OwInfoProvider to register
     */
    void registerProvider(String context_p, OwInfoProvider pro_p);

    /**
     * Remove a provider from registry for given context.
     * @param context_p String to unregister
     * @return OwInfoProvider which was unregistered, or null if nothing was registered 
     */
    OwInfoProvider unregisterProvider(String context_p);
}