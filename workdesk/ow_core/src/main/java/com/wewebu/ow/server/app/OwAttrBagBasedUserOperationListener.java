package com.wewebu.ow.server.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardDBAttributeBagWriteableFactory;
import com.wewebu.ow.server.util.OwTableSpec;

/**
 *<p>
 * Listener for user operation.
 * Details about user operation are saved in attribute bag. 
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
public class OwAttrBagBasedUserOperationListener implements OwUserOperationListener
{
    /**
     * the bag name
     * @since 3.2.0.0
     */
    public static final String USEROPERATION_BAG_NAME = "useroperation";
    /** class logger*/
    private static Logger LOG = OwLogCore.getLogger(OwAttrBagBasedUserOperationListener.class);
    /** 
     * the attribute bag
     * @deprecated 
     */
    private String bagTableName = null;
    private OwTableSpec attributeBagTable;

    /** the jdbc template*/
    private JdbcTemplate jdbc = null;

    private static final List<OwUserOperationType> TARGET_OPERATIONS = Arrays.asList(new OwUserOperationType[] { OwUserOperationType.LOGIN, OwUserOperationType.LOGOUT });

    /** converter class for attribute bag value*/
    public static class OwDateValue
    {
        /** empty event source (null) representation*/
        private static final String EMPTY_SOURCE = "-";
        /** the date format*/
        private static String FORMAT = "dd/MM/yyyy HH:mm:ss";
        /** the formatter*/
        private static SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        /** storage separator*/
        private static String SEPARATOR = "|";
        /** the date*/
        private Date theDate;
        /** the source*/
        private String source;

        /** constructor*/
        private OwDateValue()
        {

        }

        /**
         * Setter for date.
         * @param date_p
         */
        private void setDate(Date date_p)
        {
            theDate = date_p;
        }

        /**
         * Getter for date.
         * @return the date.
         */
        public Date getDate()
        {
            return theDate;
        }

        /**
         * Getter for application source.
         * Can be <code>null</code>
         * @return the application source. 
         */
        public String getSource()
        {
            return source;
        }

        /**
         * Restore {@link OwDateValue} object from a source.
         * Use this method when reading from attribute bag.
         * @param dateValueSource_p
         * @return {@link OwDateValue} object
         */
        public static synchronized OwDateValue fromString(String dateValueSource_p)
        {

            OwDateValue result = new OwDateValue();
            if (dateValueSource_p != null)
            {
                String[] splittedSource = dateValueSource_p.trim().split("\\" + SEPARATOR);
                try
                {
                    Date theDate = formatter.parse(splittedSource[0]);
                    result.setDate(theDate);
                }
                catch (ParseException e)
                {
                    LOG.error("Cannot restore");
                }
                if (splittedSource.length == 2)
                {
                    if (splittedSource[1].equals(EMPTY_SOURCE))
                    {
                        result.setSource(null);
                    }
                    else
                    {
                        result.setSource(splittedSource[1]);
                    }
                }
            }
            return result;
        }

        @Override
        public String toString()
        {
            StringBuilder build = new StringBuilder();
            synchronized (formatter)
            {
                build.append(formatter.format(theDate));
            }
            build.append(SEPARATOR);
            build.append((source == null ? EMPTY_SOURCE : source));
            return build.toString();
        }

        /** 
         * Create a {@link OwDateValue} object from a {@link OwUserOperationEvent} object.
         * @return the created object
         */
        public static synchronized OwDateValue fromEvent(OwUserOperationEvent event_p)
        {
            OwDateValue owDateValue = new OwDateValue();
            owDateValue.setDate(event_p.getCreationTime());
            owDateValue.setSource(event_p.getEventSource());
            return owDateValue;
        }

        /**
         * Set the event source
         */
        private void setSource(String eventSource_p)
        {
            this.source = eventSource_p;
        }

    }

    /**
     * Constructor.
     * @param jdbc_p - the JDBC accessor.
     * @param bagTableName_p - the table name where bags are stored.
     * @deprecated Will be replaced by {@link #OwAttrBagBasedUserOperationListener(JdbcTemplate , OwTableSpec)} .
     */
    public OwAttrBagBasedUserOperationListener(JdbcTemplate jdbc_p, String bagTableName_p)
    {
        this.jdbc = jdbc_p;
        this.bagTableName = bagTableName_p;
        this.attributeBagTable = new OwTableSpec(null, null, bagTableName_p);
    }

    /**
     * Constructor.
     * @param jdbcTemplate the JDBC accessor.
     * @param attributeBagTable the table where bags are stored.
     * @since 4.2.0.0
     */
    public OwAttrBagBasedUserOperationListener(JdbcTemplate jdbcTemplate, OwTableSpec attributeBagTable)
    {
        this.jdbc = jdbcTemplate;
        this.bagTableName = attributeBagTable.getTableName();
        this.attributeBagTable = attributeBagTable;
    }

    /**
     * Obtain corresponding attribute bag to this user.
     * @param userName_p
     * @param bagName_p
     * @param tableName_p
     * @return the attribute bag corresponding to this user.
     * @throws Exception
     * @deprecated
     */
    protected OwAttributeBagWriteable getAttributeBag(String userName_p, String bagName_p, String tableName_p) throws Exception
    {
        OwStandardDBAttributeBagWriteableFactory owStandardDBAttributeBagWriteableFactory = new OwStandardDBAttributeBagWriteableFactory(jdbc, userName_p, tableName_p);
        owStandardDBAttributeBagWriteableFactory.init();
        return owStandardDBAttributeBagWriteableFactory.getBag(bagName_p);
    }

    /**
     * Obtain corresponding attribute bag to this user.
     * @param userName_p
     * @param bagName_p
     * @param attributeBagTable
     * @return the attribute bag corresponding to this user.
     * @since 4.2.0.0
     */
    private OwAttributeBagWriteable getAttributeBag(String userName_p, String bagName_p, OwTableSpec attributeBagTable) throws Exception
    {
        OwStandardDBAttributeBagWriteableFactory owStandardDBAttributeBagWriteableFactory = new OwStandardDBAttributeBagWriteableFactory(jdbc, userName_p, attributeBagTable);
        owStandardDBAttributeBagWriteableFactory.init();
        return owStandardDBAttributeBagWriteableFactory.getBag(bagName_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwUserOperationListener#operationPerformed(com.wewebu.ow.server.app.OwUserOperationEvent)
     */
    public void operationPerformed(OwUserOperationEvent event_p)
    {
        if (TARGET_OPERATIONS.contains(event_p.getType()))
        {
            try
            {
                OwAttributeBagWriteable bag = getAttributeBag(event_p.getUserInfo().getUserID(), USEROPERATION_BAG_NAME, this.attributeBagTable);
                bag.setAttribute(event_p.getType().toString(), OwDateValue.fromEvent(event_p));
                bag.save();
            }
            catch (Exception e)
            {
                LOG.error("Cannot save user operation timestamp", e);
            }
        }

    }
}
