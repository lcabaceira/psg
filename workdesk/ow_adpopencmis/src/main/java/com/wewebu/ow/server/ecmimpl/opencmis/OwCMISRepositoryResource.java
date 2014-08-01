package com.wewebu.ow.server.ecmimpl.opencmis;

import java.util.Locale;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Simple {@link RepositoryInfo} based resource.
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
public class OwCMISRepositoryResource implements OwCMISResource
{
    private Repository repository;
    private Map<String, ?> parameters;
    private OwCMISNetwork network;
    private OwCMISDMSIDDecoder dmsidDecoder = new OwCMISSimpleDMSIDDecoder();

    public OwCMISRepositoryResource(Repository repository, Map<String, ?> parameters_p, OwCMISNetwork network)
    {
        this.repository = repository;
        this.network = network;
        this.parameters = parameters_p;
    }

    public String getDisplayName(Locale locale_p)
    {
        return repository.getName();
    }

    public String getDescription(Locale locale_p)
    {
        return repository.getDescription();
    }

    public String getID()
    {
        return repository.getId();
    }

    public Repository getRepository()
    {
        return repository;
    }

    @Override
    public String getName()
    {
        return repository.getName();
    }

    @Override
    public OwCMISSession createSession() throws OwException
    {
        return new OwCMISRepositorySession(this, parameters, this.network);
    }

    @Override
    public OwCMISDMSIDDecoder getDMSIDecoder()
    {
        return dmsidDecoder;
    }
}
