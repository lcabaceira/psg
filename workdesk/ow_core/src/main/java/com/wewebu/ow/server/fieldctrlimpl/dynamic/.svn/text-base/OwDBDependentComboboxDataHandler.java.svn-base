package com.wewebu.ow.server.fieldctrlimpl.dynamic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwDataSourceUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implements a DB based OwDependentComboboxDataHandler.
 * Will query data from a DB, and provide the result as OwComboItem.<br />
 * <p>
 * Allows to configure independent DataSource using own JNDI name, if local Session DB connection should not be used.<br />
 * <code>
 * &lt;DataSource&gt;JNDI_NAME&lt;/DataSource&gt;
 * </code>
 * </p>
 * <p>
 * Concept of SQL-Query definition is based on standard query for Select- and From-clause<br />
 * <code>&lt;Query selectAll=&quot;false&quot;&gt;Select my,fields FROM table&lt;/Query&gt;</code><br />
 * where the attribute <code>selectAll</code> is controlling if everything should be retrieved 
 * in case dependent fields are empty, and additional dynamic/dependent WHERE Property definitions:
 * <ul><li>
 * <code>&lt;Property operator="="&gt;ECM_FIELD_ID&lt;Property&gt;</code><br />
 * Defines a where property, where DB column name and ECM field id is the same.
 * </li><li>
 * <code>&lt;Property queryName="DBClumnName" operator="="&gt;ECM_FIELD_ID&lt;Property&gt;</code><br />
 * Specific mapping of ECM field id to DB column name, with equals operator.
 * </li><li>
 * <code>&lt;Property queryName="DBClumnName"&gt;ECM_FIELD_ID&lt;Property&gt;</code><br />
 * Mapping of ECM field id and DB column name, the operator will be assumed as &quot;equals&quot; (=).
 * </li><li>
 * <code>&lt;Property queryName="DBClumnName" operator="!%s=?"&gt;ECM_FIELD_ID&lt;Property&gt;</code><br />
 * Special definition of SQL-operator, which can be used if the order of column name and operator is 
 * different or should be negated. If operator contains a percent sign (%s), the %s will be replaced by
 * queryName value and added to query, else the construction of where clause is done like:<br />
 * <code> <b>queryName</b> (space) <b>operator</b> (space) <b>?</b> <code><br />
 * The constructed Query is provided as QueryString for prepared Statement and should not contain value definitions. 
 * </li>
 * </ul>
 * </p>
 * Attention all WHERE property definition will be concatenated with AND, and
 * then attached to the Query parameter value (WHERE value should not be part of Query value).
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
 *@since 4.2.0.0
 */
public class OwDBDependentComboboxDataHandler implements OwDependentComboboxDataHandler
{
    private static final Logger LOG = OwLogCore.getLogger(OwDBDependentComboboxDataHandler.class);
    protected static final String EL_DATA_SOURCE = "DataSource";
    protected static final String AT_SELECT_ALL = "selectAll";
    protected static final String EL_QUERY = "Query";
    protected static final String EL_PROPERTY = "Property";
    protected static final String AT_PROP_QUERY_NAME = "queryName";
    protected static final String AT_PROP_OPERATOR = "operator";

    private OwXMLUtil conf;
    private JdbcTemplate template;
    private String query;
    private Map<String, OwWherePropDefinition> dependentData;
    private OwAppContext context;
    private boolean selectAll;

    @Override
    public void init(OwAppContext context, OwXMLUtil configuration) throws OwException
    {
        this.setConf(configuration);
        this.context = context;
        String dataSourceName = getConf().getSafeTextValue(EL_DATA_SOURCE, null);
        if (dataSourceName != null)
        {
            DataSource dataSource = OwDataSourceUtil.retrieveDataSource(dataSourceName);
            setTemplate(OwDataSourceUtil.createJdbcTemplate(dataSource));
        }
        else
        {
            OwRepositoryContext repoCtx = context.getRegisteredInterface(OwRepositoryContext.class);
            if (repoCtx != null)
            {
                setTemplate(repoCtx.getJDBCTemplate());
            }
        }
        if (getTemplate() == null)
        {
            throw new OwConfigurationException("OwDBDependentComboboxDataHandler.init: No JDBC connection available, check config node = " + EL_DATA_SOURCE);
        }

        OwXMLUtil queryConf;
        try
        {
            queryConf = getConf().getSubUtil(EL_QUERY);
            this.query = getConf().getSafeTextValue(EL_QUERY, null);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("OwDBDependentComboboxDataHandler.init: Missing query definition, check configuration of node = " + EL_QUERY, e);
        }

        if (query == null)
        {
            throw new OwConfigurationException("OwDBDependentComboboxDataHandler.init: Missing query definition, check configuration of node = " + EL_QUERY);
        }
        this.selectAll = queryConf.getSafeBooleanAttributeValue(AT_SELECT_ALL, false);
        Iterator<?> propIt = getConf().getSafeUtilList(EL_PROPERTY).iterator();
        this.dependentData = new LinkedHashMap<String, OwWherePropDefinition>();
        while (propIt.hasNext())
        {
            OwXMLUtil propUtil = (OwXMLUtil) propIt.next();
            String fieldMapping = propUtil.getSafeTextValue(null);
            if (fieldMapping != null)
            {
                String queryName = propUtil.getSafeStringAttributeValue(AT_PROP_QUERY_NAME, fieldMapping);
                String operator = propUtil.getSafeStringAttributeValue(AT_PROP_OPERATOR, "=");
                this.dependentData.put(fieldMapping, new OwWherePropDefinition(fieldMapping, operator, queryName));
            }
            else
            {
                LOG.info("OwDBDependentComboboxDataHandler.init: Could not read whereProp configuration");
            }
        }
    }

    @Override
    public List<OwComboItem> getData(OwFieldProvider fieldProvider) throws OwException
    {
        LinkedList<OwComboItem> result = new LinkedList<OwComboItem>();
        Map<String, OwWherePropDefinition> whereDef = new LinkedHashMap<String, OwWherePropDefinition>();
        for (Entry<String, OwWherePropDefinition> prop : dependentData.entrySet())
        {
            try
            {
                OwField field = fieldProvider.getField(prop.getKey());
                Object value = field.getValue();
                if (value != null)
                {
                    OwWherePropDefinition copy = (OwWherePropDefinition) prop.getValue().clone();
                    copy.setValue(value);
                    whereDef.put(prop.getKey(), copy);
                }
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.warn("Could not retrieve from provider field with id = " + prop, e);
                }
                else
                {
                    LOG.warn("OwDBDependentComboboxDataHandler.getData: Could not retrieve from provider field with id = " + prop);
                }
            }
        }
        if (!whereDef.isEmpty() || selectAll())
        {
            String fullQuery = createFullQuery(this.query, whereDef);
            processRequest(fullQuery, whereDef, result);
        }
        return result;
    }

    /**
     * Process the query and add the query-results into the provided results collection,
     * wrapped as OwComoboItem interface. 
     * @param query String the created query
     * @param whereProps Map of ECM field name and corresponding SQL where definition
     * @param result List of OwComboItem's
     * @throws OwException
     */
    protected void processRequest(final String query, Map<String, OwWherePropDefinition> whereProps, List<OwComboItem> result) throws OwException
    {
        ArrayList<Object> values = new ArrayList<Object>();
        for (OwWherePropDefinition whereProp : whereProps.values())
        {
            values.add(whereProp.getValue());
        }
        SqlRowSet rowSet = getTemplate().queryForRowSet(query, values.toArray());
        if (rowSet.first())
        {
            SqlRowSetMetaData rowInfo = rowSet.getMetaData();
            int valueIdx = 1, labelIdx = 1;//index value and index display/label
            if (rowInfo.getColumnCount() > 1)
            {
                labelIdx = valueIdx + 1;
                LOG.info("OwDBDependentComboboxDataHandler.processRequest: contains more than two columns, only first two will be used for value and label representation");
            }
            try
            {
                do
                {
                    String value = rowSet.getString(valueIdx);
                    String label = getContext().localize(OwString.LABEL_PREFIX + value, rowSet.getString(labelIdx));
                    OwDefaultComboItem item = new OwDefaultComboItem(value, label);
                    result.add(item);
                } while (rowSet.next());
            }
            catch (InvalidResultSetAccessException accessEx)
            {
                LOG.error("Cannot process result set", accessEx);
                throw new OwServerException("Could not process resulting set", accessEx);
            }
        }
    }

    /**
     * Create the String which represents the full SQL query to be fired for new results.
     * By default creates an AND concatenated WHERE clause for provided properties,
     * using the query as predefined information.
     * @param query String the base Query
     * @param whereProps Map of WHERE props to handle
     * @return String representing the full query string
     * @throws OwException
     */
    protected String createFullQuery(final String query, Map<String, OwWherePropDefinition> whereProps) throws OwException
    {
        StringBuilder fullQuery = new StringBuilder(query);
        if (!whereProps.isEmpty())
        {
            fullQuery.append(" WHERE ");
            Iterator<Entry<String, OwWherePropDefinition>> it = whereProps.entrySet().iterator();
            while (it.hasNext())
            {
                OwWherePropDefinition field = it.next().getValue();
                fullQuery.append("( ");
                if (field.getOperator().contains("%s"))
                {
                    fullQuery.append(String.format(field.getOperator(), field.getQueryName()));
                }
                else
                {
                    fullQuery.append(field.getQueryName()).append(" ").append(field.getOperator());
                    fullQuery.append(" ?");
                }
                fullQuery.append(" )");
                if (it.hasNext())
                {
                    fullQuery.append(" AND ");
                }
            }
        }
        return fullQuery.toString();
    }

    protected OwAppContext getContext()
    {
        return this.context;
    }

    protected JdbcTemplate getTemplate()
    {
        return template;
    }

    protected void setTemplate(JdbcTemplate template)
    {
        this.template = template;
    }

    protected OwXMLUtil getConf()
    {
        return conf;
    }

    protected void setConf(OwXMLUtil conf)
    {
        this.conf = conf;
    }

    @Override
    public List<String> getDependentFieldIds()
    {
        return new LinkedList<String>(this.dependentData.keySet());
    }

    public boolean selectAll()
    {
        return this.selectAll;
    }

    /**
     *<p>
     * POJO containing configuration options,
     * as simplified data objects. 
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
    public static class OwWherePropDefinition implements Cloneable
    {
        private String queryName, operator, fieldMapping;
        private Object value;

        public OwWherePropDefinition(String fieldName, String operator, String queryName)
        {
            super();
            this.queryName = queryName;
            this.operator = operator;
            this.fieldMapping = fieldName;
        }

        public String getQueryName()
        {
            return this.queryName;
        }

        public String getOperator()
        {
            return this.operator;
        }

        public String getFieldMapping()
        {
            return this.fieldMapping;
        }

        public Object getValue()
        {
            return this.value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }

        public Object clone()
        {
            return new OwWherePropDefinition(getFieldMapping(), getOperator(), getQueryName());
        }
    }
}
