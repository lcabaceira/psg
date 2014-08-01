package com.wewebu.ow.server.plug.owrecordext;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString2;
import com.wewebu.ow.server.util.OwString3;

/**
 *<p>
 * Foreign key referred objects retrieval utility.
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
public class OwKeyReferenceResolver
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwKeyReferenceResolver.class);

    private String referenceClass;
    private String referenceProperty;

    /**
     * Constructor 
     * @param referenceClass_p referred object class 
     * @param referenceProperty_p referred object property to be matched against foreign key value
     */
    public OwKeyReferenceResolver(String referenceClass_p, String referenceProperty_p)
    {
        super();

        this.referenceClass = referenceClass_p;
        this.referenceProperty = referenceProperty_p;
    }

    public String getReferenceProperty()
    {
        return this.referenceProperty;
    }

    public String getReferenceClass()
    {
        return this.referenceClass;
    }

    /**
     * Referred objects search and retrieve method.
     * @param foreignKeyValue_p value of the foreign key
     * @param maxSize_p mximum number of referred objects to retrieve 
     * @param network_p 
     * @param resource_p
     * @return a collection of maximum <b>maxSize_p</b> objects  of class {@link #referenceClass} 
     *         having the property designated by {@link #referenceProperty} equal to the value of <b>foreignKeyValue_p</b>
     * @throws OwServerException if the search operation failed
     * @throws OwInvalidOperationException if the resolver was miss-configured or the configured entities (classes and properties)
     *                                     can not be found  
     */
    public OwObjectCollection resovle(Object foreignKeyValue_p, int maxSize_p, OwNetwork network_p, OwResource resource_p) throws OwServerException, OwInvalidOperationException
    {
        String stringValue = foreignKeyValue_p == null ? "" : foreignKeyValue_p.toString();

        //the setup create a document named: junit_Document Title
        OwEcmUtil.OwSimpleSearchClause searchClause = new OwEcmUtil.OwSimpleSearchClause(this.referenceProperty, OwSearchOperator.CRIT_OP_EQUAL, stringValue);

        // create array of search criteria
        OwEcmUtil.OwSimpleSearchClause[] searchClauses = new OwEcmUtil.OwSimpleSearchClause[] { searchClause };

        OwObjectClass referringClazz = null;
        //request object class which will be used for search
        try
        {
            referringClazz = network_p.getObjectClass(this.referenceClass, resource_p);

        }
        catch (Exception e)
        {
            LOG.error("OwForeignKeyResolver.resovle: Could not retrieve objectclass " + this.referenceProperty + " and from resource " + resource_p.getDisplayName(network_p.getLocale()) + " returned no results.", e);
            OwString2 message = new OwString2("app.OwForeignKeyResolver.invalidObjectClass", "The configured objectclass '%1' cannot be retrieved from repository '%2'.", this.referenceClass, resource_p.getDisplayName(network_p.getLocale()));
            throw new OwInvalidOperationException(message, e);
        }

        // perform search
        try
        {
            OwSearchObjectStore searchObjectStore = OwEcmUtil.createSearchStore(resource_p.getID(), null);
            String searchClass = referringClazz.getClassName();
            if ("ibmcm".equals(network_p.getDMSPrefix()))
            {//do workaround for ibmcm network
                searchClass = searchClass.substring(0, searchClass.indexOf('!'));
            }

            OwSearchNode search = OwEcmUtil.createSimpleSearchNode(referringClazz.getType(), searchObjectStore, searchClass, resource_p.getID(), "/", searchClauses, network_p, true);

            return network_p.doSearch(search, null, null, maxSize_p, OwSearchTemplate.VERSION_SELECT_DEFAULT);
        }
        catch (Exception e)
        {
            LOG.error("OwForeignKeyResolver.resovle(): The search for foreign key reference of class " + this.referenceClass + " with a property named " + this.referenceProperty + "=" + stringValue + " has failed!", e);
            OwString3 message = new OwString3("app.OwForeignKeyResolver.search.error", "The search for an object reference of class '%1' with a property named '%2' and value '%3' has failed.", this.referenceClass, this.referenceProperty, stringValue);
            throw new OwServerException(message, e);
        }

    }

}
