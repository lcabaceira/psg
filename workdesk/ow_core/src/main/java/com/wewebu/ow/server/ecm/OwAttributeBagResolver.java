package com.wewebu.ow.server.ecm;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardDBAttributeBagWriteableFactory;
import com.wewebu.ow.server.util.OwStandardDBInvertedAttributeBag;
import com.wewebu.ow.server.util.OwTableSpec;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implements a helper class that resolves attribute bags for the getAttributeBagWriteable methods in the OwNetwork adapters.
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
public class OwAttributeBagResolver implements OwAttributeBagsSupport
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwAttributeBagResolver.class);

    /** standard table for writable attribute bags, this table must exist in order to persist writable attribute bags */
    public static OwTableSpec DB_ATTRIBUTE_BAG_TABLE = new OwTableSpec();

    /** factory for pooled attribute bags */
    protected OwStandardDBAttributeBagWriteableFactory m_writeablebagfactory;

    /**
     * Returns an {@link OwAttributeBagResolver} instance  if such
     * an attribute bag support is usable in the given context. Fails with {@link OwNotSupportedException}
     * otherwise.
     * @param networkContext_p
     * @return  an {@link OwAttributeBagResolver} instance
     * @throws OwNotSupportedException if the DB attribute bags can not be used (egg. no DB connection is defined
     * @since 3.1.0.0
     */
    public static OwAttributeBagResolver createAndCheckResolver(OwNetworkContext networkContext_p) throws OwNotSupportedException
    {
        OwAttributeBagResolver resolver = new OwAttributeBagResolver();
        try
        {
            resolver.getUserKeyAttributeBagWriteable(networkContext_p, "testBag", "testUser");
            return resolver;
        }
        catch (Exception e)
        {
            LOG.debug("DB AttributeBags are not supported (configuration/initialisation error or not configured)!", e);
            throw new OwNotSupportedException(networkContext_p.localize("app.OwAttributeBagResolver.db.bags.not.supported", "DB bags are not supported!"), e);
        }
    }

    /** get a inverted writable attribute bag based on a attribute name key
     *  i.e.: the attributenames of the bag represent the users
     *  @see OwNetwork#APPLICATION_OBJECT_TYPE_INVERTED_ATTRIBUTE_BAG
     *
     * @param context_p
     * @param bagname_p
     * @param attributename_p
     * @return an {@link OwAttributeBag}
     * @throws Exception
     */
    public OwAttributeBag getNameKeyAttributeBag(OwNetworkContext context_p, String bagname_p, String attributename_p) throws Exception
    {
        // === get read only bag for a specific attribute name
        JdbcTemplate jdbc = context_p.getJDBCTemplate();
        if (jdbc == null)
        {
            String msg = "OwAttributeBagResolver.getAttributeBagWriteable: No DataSource was specified, so DB-AttributeBag is not available.";
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
        initTableName(context_p);
        return new OwStandardDBInvertedAttributeBag(jdbc, bagname_p, attributename_p, DB_ATTRIBUTE_BAG_TABLE);
    }

    /**
     * Initialize the attribute bag table name from bootstrap configuration.
     * @param context_p - the context;
     * @since 2.5.3.0
     */
    private static void initTableName(OwNetworkContext context_p)
    {
        try
        {
            OwXMLUtil bootstrapConfiguration = ((OwMainAppContext) context_p).getConfiguration().getBootstrapConfiguration();
            OwXMLUtil ecmAdapterUtil = bootstrapConfiguration.getSubUtil("EcmAdapter");

            OwXMLUtil attributeBagTableXML = ecmAdapterUtil.getSubUtil("DbAttributeBagTableName");

            if (null != attributeBagTableXML)
            {
                DB_ATTRIBUTE_BAG_TABLE = OwTableSpec.fromXML(attributeBagTableXML);
            }

        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Cannot detect the name of the ATTRIBUTE_BAG table. Using default name: " + DB_ATTRIBUTE_BAG_TABLE.getTableName(), e);
            }
            else
            {
                LOG.warn("OwAttributeBagResolver.initTableName: Cannot detect the name of the ATTRIBUTE_BAG table. Using default name: " + DB_ATTRIBUTE_BAG_TABLE.getTableName());
            }
        }
    }

    /** get a writable attribute bag based an a user key
     *  i.e.: the attributenames of the bag represent the attribute names
     *  @see OwNetwork#APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE
     *
     * @param context_p
     * @param bagname_p
     * @param username_p
     * @return an {@link OwAttributeBagWriteable}
     * @throws Exception
     */
    public OwAttributeBagWriteable getUserKeyAttributeBagWriteable(OwNetworkContext context_p, String bagname_p, String username_p) throws Exception
    {
        JdbcTemplate jdbc = context_p.getJDBCTemplate();
        if (jdbc == null)
        {
            String msg = "OwAttributeBagResolver.getAttributeBagWriteable: No DataSource was specified, so DB-AttributeBag is not available.";
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        initTableName(context_p);

        if (null != username_p)
        {
            // === get bags for a specific user
            // no pooling needed here
            OwStandardDBAttributeBagWriteableFactory owStandardDBAttributeBagWriteableFactory = new OwStandardDBAttributeBagWriteableFactory(jdbc, username_p, DB_ATTRIBUTE_BAG_TABLE);
            owStandardDBAttributeBagWriteableFactory.init();
            return owStandardDBAttributeBagWriteableFactory.getBag(bagname_p);
        }
        else
        {
            // === get bags with the current user, use pooled factory for high performance access
            try
            {
                if (null == m_writeablebagfactory)
                {
                    m_writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(jdbc, context_p.getCurrentUser().getUserID(), DB_ATTRIBUTE_BAG_TABLE);
                    m_writeablebagfactory.init();
                }

                return m_writeablebagfactory.getBag(bagname_p);
            }
            catch (Exception e)
            {
                String msg = "Error getting application object, name = " + bagname_p;
                LOG.error(msg, e);
                throw new OwObjectNotFoundException(msg, e);
            }
        }
    }
}