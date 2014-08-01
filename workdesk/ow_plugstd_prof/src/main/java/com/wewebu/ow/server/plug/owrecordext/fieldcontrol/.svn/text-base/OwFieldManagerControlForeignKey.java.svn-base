package com.wewebu.ow.server.plug.owrecordext.fieldcontrol;

import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.plug.owrecordext.OwKeyReferenceResolver;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the field control for foreign key.
 * For {@link OwObject} objects added in virtual folders, the value of the foreign key is read-only and is filled by virtual folder mechanism.
 * For {@link OwObject} objects added in physical folders, the value must for the foreign key be filled automatically by the Add Document plugin,
 * using <ParameterMapping> configuration mechanism.
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
public class OwFieldManagerControlForeignKey extends OwFieldManagerControl
{
    /**configuration property key: its value represent the name of the foreign key property*/
    private static final String FOREIGN_KEY_PROPERTY_ATTRIBUTE_NAME = "key";
    /**configuration property object class: its value represent the name of class for the object where foreign key is stored*/
    private static final String OBJECTCLASS_ATTRIBUTE_NAME = "objectclass";
    /**configuration property: element name for foreign key definition {@value #FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME}*/
    private static final String FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME = "ForeignObjectDefinition";
    /**optional configuration property to create links based on a context
     * <p>By default the context will be called ForeignObjectDefinition</p>*/
    protected static final String MIMECONTEXT_ATTRIBUTE_NAME = "mimecontext";
    /**optional configuration for handling of null/empty value of the defined property/foreign key*/
    protected static final String EMPTYHANDLING_ATTRIBUTE_NAME = "emptyHandling";
    /**Constant for the empty value handling editable control*/
    protected static final String EMPTYHANDLING_EDITABLE = "editable";
    /**Constant for the empty value handling read only control*/
    protected static final String EMPTYHANDLING_READ_ONLY = "readonly";

    /**class logger*/
    private static final Logger LOG = OwLog.getLogger(OwFieldManagerControlForeignKey.class);
    /**the object class value*/
    private String m_objectClass;
    /**the foreign key name property*/
    private String m_property;
    /**value which should be used as MIME context*/
    private String m_mimeContext;
    /**configuration of the empty value handling*/
    private String m_emptyHandling;
    /**the network object*/
    private OwNetwork m_network;
    /**the main application context*/
    private OwMainAppContext m_context;

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#init(com.wewebu.ow.server.app.OwFieldManager, org.w3c.dom.Node)
     */
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        Locale locale = fieldmanager_p.getContext().getLocale();
        OwXMLUtil nodeUtil = new OwStandardXMLUtil(configNode_p);
        OwXMLUtil objectDefinitionNode = nodeUtil.getSubUtil(FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME);
        if (objectDefinitionNode == null)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("This node should have a child element named: " + FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME);
            }

            throw new OwConfigurationException(fieldmanager_p.getContext().localize1("foreignkeycontrol.configerror.nosubnode", "This node should have a child element named", FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME));
        }
        m_context = (OwMainAppContext) fieldmanager_p.getContext();
        m_network = m_context.getNetwork();

        m_objectClass = getConfigAttribute(locale, objectDefinitionNode, OBJECTCLASS_ATTRIBUTE_NAME);
        m_property = getConfigAttribute(locale, objectDefinitionNode, FOREIGN_KEY_PROPERTY_ATTRIBUTE_NAME);
        m_mimeContext = objectDefinitionNode.getSafeStringAttributeValue(MIMECONTEXT_ATTRIBUTE_NAME, FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME);
        m_emptyHandling = objectDefinitionNode.getSafeStringAttributeValue(EMPTYHANDLING_ATTRIBUTE_NAME, null);
    }

    /**
     * Utility method to get the value of an attribute, from configuration.
     * @param locale_p
     * @param objectDefinitionNode_p
     * @param attributeName_p
     * @return a {@link String} object, representing the value for the attribute.
     * @throws OwConfigurationException
     */
    private String getConfigAttribute(Locale locale_p, OwXMLUtil objectDefinitionNode_p, String attributeName_p) throws OwConfigurationException
    {
        String attributeValue = objectDefinitionNode_p.getSafeStringAttributeValue(attributeName_p, null);
        if (attributeValue == null)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFieldManagerControlForeignKey.getConfigAttribute: This node should have an attribute named = " + attributeName_p);
            }
            throw new OwConfigurationException(OwString.localize1(locale_p, "foreignkeycontrol.configerror.noattribute", "This node should have an attribute named:", attributeName_p));
        }
        return attributeValue;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        OwFieldProvider fieldProvider = getFieldManager().getFieldProvider();
        if (fieldProvider != null)
        {
            if (!getFieldManager().isFieldProviderType(OwFieldProvider.TYPE_SEARCH) && field_p.getValue() != null && !field_p.getValue().equals(""))
            {
                insertReadOnlyField(w_p, fieldDef_p, field_p.getValue());
            }
            else
            {
                if (fieldProvider.getFieldProviderType() == OwFieldProvider.TYPE_SEARCH)
                {//we're currently in the searchtemplate rendering, render input control
                    renderInputControl(w_p, fieldDef_p, field_p, strID_p);
                }
                else
                {
                    if (EMPTYHANDLING_READ_ONLY.equals(getEmptyHandling()))
                    {
                        insertReadOnlyField(w_p, fieldDef_p, field_p.getValue());
                    }
                    else
                    {
                        if (EMPTYHANDLING_EDITABLE.equals(getEmptyHandling()))
                        {
                            renderInputControl(w_p, fieldDef_p, field_p, strID_p);
                        }
                        else
                        {
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("OwFieldManagerControlForeignKey.insertEditField: "
                                        + "The value for foreign key must be not null. The value is either automatically set from virtual folder properties, or, in case of physical folders, this value must be configured"
                                        + " in Add Document plugin, ParameterMapping section. ");
                            }
                            throw new OwConfigurationException(getContext().localize("app.OwFieldManagerControlForeignKey.invalidValue", "The value of the foreign key may not be empty. Please check your plugin configuration file."));
                        }
                    }
                }
            }
        }
    }

    /**
     * Method which will render a single input control, to allow the user to change or create a value.
     * @param w_p Writer used for rendering
     * @param fieldDef_p OwFieldDefinition which is used for additional information retrieval
     * @param field_p OwField where to request the value to be set
     * @param strID_p String representing the ID of the input control
     * @throws Exception if fail to render or request information
     */
    protected void renderInputControl(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        OwFieldProvider fieldProvider = getFieldManager().getFieldProvider();
        // get the max string length from definition max value
        int iMaxSize = 32; // default
        try
        {
            if (fieldDef_p.getMaxValue() != null)
            {
                iMaxSize = ((Integer) fieldDef_p.getMaxValue()).intValue();
            }
        }
        catch (Exception e)
        { /* ignore */
        }
        int iVisibleSize = (iMaxSize > 32) ? 32 : iMaxSize;

        w_p.write("<input id=\"");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("\" MAXLENGTH=\"");
        OwHTMLHelper.writeSecureHTML(w_p, String.valueOf(iMaxSize));
        w_p.write("\" SIZE=\"");
        OwHTMLHelper.writeSecureHTML(w_p, String.valueOf(iVisibleSize));
        w_p.write("\" title=\"");
        OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
        /*create the CSS classes for this input control*/
        w_p.write("\" class=\"OwInputControl OwInputControlString OwInputControl_");
        w_p.write(fieldDef_p.getClassName());
        w_p.write("\" name=\"");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("\" type=\"text\" value=\"");
        OwHTMLHelper.writeSecureHTML(w_p, field_p.getValue() != null ? field_p.getValue().toString() : "");
        w_p.write("\" onblur=\"onFieldManagerFieldExit('");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
        w_p.write("','");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
        w_p.write("','");
        if (fieldProvider != null)
        {
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(fieldProvider.getFieldProviderType())));
            w_p.write("','");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldProvider.getFieldProviderName()));
            w_p.write("','");
        }
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(strID_p));
        w_p.write("',this.value)\">");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertReadOnlyField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object)
     */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (!getFieldManager().isFieldProviderType(OwFieldProvider.TYPE_CREATE_OBJECT) && value_p != null && !"".equals(value_p))
        {
            OwKeyReferenceResolver resolver = new OwKeyReferenceResolver(m_objectClass, m_property);

            OwObjectCollection result = null;
            // perform search
            try
            {
                result = resolver.resovle(value_p, 10, m_network, getResource());
            }
            catch (OwInvalidOperationException e)
            {
                throw e;
            }
            catch (OwServerException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Search for efile failed: ", e);
                }
            }

            //verify the search result
            if (result == null || result.size() == 0)
            {
                handleNoResults(w_p, fieldDef_p, value_p);
            }
            else if (result.size() == 1)
            {
                handleExactOneResult(w_p, fieldDef_p, value_p, result);
            }
            else
            {
                handleManyResults(w_p, fieldDef_p, value_p, result);
            }
        }
        else
        {
            w_p.write(value_p != null ? value_p.toString() : "");
        }
    }

    /**
     * Try to retrieve the current OwResource from the OwFieldProvider or OwFieldProviderSource.
     * If the resource cannot be requested from OwFieldProvider or OwFieldProviderSource the resource
     * will be identified by iteration over all OwResource-IDs searching for the configured object class.
     * @return OwResource which should be used for search
     * @throws Exception if the resource could not be retrieved or is still null when should be returned.
     * @see OwFieldProvider
     * @see #getResourceByIdIteration()
     */
    protected OwResource getResource() throws Exception
    {
        OwResource retResource = null;

        if (getFieldManager().getFieldProvider() != null)
        {
            if (getFieldManager().getFieldProvider() instanceof OwObject)
            {//here we are retrieving direct the resource from provider
                retResource = ((OwObject) getFieldManager().getFieldProvider()).getResource();
            }
            else
            {
                if (getFieldManager().getFieldProvider().getFieldProviderSource() instanceof OwObject)
                {//here we are retrieving direct the resource from "native" object
                    retResource = ((OwObject) getFieldManager().getFieldProvider().getFieldProviderSource()).getResource();
                }
            }
        }

        if (retResource == null)
        {//fallback handling: iterate over the known resource IDs and check if the configured object class exist
            retResource = getResourceByIdIteration();
        }

        //post condition, we need a resource to execute the search
        if (retResource == null)
        {
            String msg = "Could not retrieve resource for search.";
            LOG.error("OwFieldManagerControlForeignKey.getResource: " + msg);
            throw new OwConfigurationException(getContext().localize("app.OwFieldManagerControlForeignKey.resource", "Could not retrieve resource for search."));
        }

        return retResource;
    }

    /**
     * Default handling of resource retrieval, by iteration through
     * the Resource-ID list of network.
     * @return OwResource or null if could not be found.
     */
    protected OwResource getResourceByIdIteration()
    {
        OwResource retResource = null;
        try
        {
            Iterator resourceIdsIterator = m_network.getResourceIDs();
            while (resourceIdsIterator.hasNext())
            {
                String id = (String) resourceIdsIterator.next();
                try
                {
                    OwResource resource = m_network.getResource(id);
                    OwObjectClass clazz = m_network.getObjectClass(getConfiguredObjectClassName(), resource);
                    clazz.getPropertyClass(getConfiguredPropertyName());
                    //found resource, has correct object class and property
                    retResource = resource;
                    break;
                }
                catch (OwObjectNotFoundException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwFieldManagerControlForeignKey.getResourceByIdIteration: Class or Property not found in this resource, using next one.", e);
                    }
                }
                catch (Exception ex)
                {
                    LOG.warn("OwFieldManagerControlForeignKey.getResourceByIdIteration: Unknown Problem during retrieving of objectstore/repository.", ex);
                }
            }
        }
        catch (Exception itEx)
        {
            LOG.error("OwFieldManagerControlForeignKey.getResourceByIdIteration: Cannot iterate over resource ID's.", itEx);
        }
        return retResource;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object result = value_p;
        String valueFromRequest = request_p.getParameter(strID_p);
        if (valueFromRequest != null)
        {
            result = valueFromRequest.trim();
        }
        return result;
    }

    /**
     * Return the configured object class name which
     * is defined in XML config file.
     * <p>Can return null if {@link #init(OwFieldManager, Node)} was not
     * processed</p>
     * @return String representing the object class name
     */
    public String getConfiguredObjectClassName()
    {
        return this.m_objectClass;
    }

    /**
     * Return the property name which is configured in
     * XML config file, to be used for search.
     * <p>Can return null if {@link #init(OwFieldManager, Node)}
     * was not processed</p>
     * @return String representing the property name
     */
    public String getConfiguredPropertyName()
    {
        return this.m_property;
    }

    /**
     * Return a String which should be used as
     * MIME context for link creation.
     * <p>If there was explicitly definition of
     * a MIME context, the value returned will be
     * <b>{@link #FOREIGN_OBJECT_DEFINITION_ELEMENT_NAME}</b></p>
     * @return String representing the MIME context
     */
    public String getMimeContext()
    {
        return this.m_mimeContext;
    }

    /**
     * Return the configured empty handling value.
     * @return String or null if no handling defined.
     */
    public String getEmptyHandling()
    {
        return this.m_emptyHandling;
    }

    /**(overridable)
     * Called if the search cannot find any matching object for current value.
     * <p>By default this method will render the value surround by a span with
     * class &quot;OwFieldManagerControlForeignKey_error&quot;.</p>
     * @param w_p Writer to be used for rendering
     * @param fieldDef_p OwFieldDefinition of current field
     * @param value_p Object value used for search
     * @throws Exception can be thrown if no results are invalid, or rendering problems
     */
    protected void handleNoResults(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwFieldManagerControlForeignKey.renderNoResults: The search for an object (" + getConfiguredObjectClassName() + ") with a property with name '" + getConfiguredPropertyName() + "' and value '" + value_p
                    + "' returned no results.");
        }
        w_p.append("<span class=\"OwFieldManagerControlForeignKey_error\">");
        w_p.append(value_p.toString());
        w_p.append("</span>");
        //        String message = getContext().localize2("app.OwFieldManagerControlForeignKey.searchnoresult", "The search for an eFile folder with a property with name '%1' and value '%2' returned no results.", m_property, value_p.toString());
        //        throw new OwInvalidOperationException(message);
    }

    /**(overridable)
     * Called if exact one result was returned.
     * <p>By default this method render a MIME link, using the configured MIME context ({@link #getMimeContext()}).</p>
     * @param w_p Writer to be used for rendering
     * @param fieldDef_p OwFieldDefinition of the field
     * @param value_p Object value used for Search
     * @param result_p OwObjectList which was returned by search
     * @throws Exception if any issue occurs with rendering, or one result not allowed
     */
    protected void handleExactOneResult(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, OwObjectCollection result_p) throws Exception
    {
        OwObject efile = (OwObject) result_p.get(0);
        OwMimeManager mimeManager = new OwMimeManager();

        mimeManager.attach(getContext(), null);
        mimeManager.reset();

        mimeManager.setMimeTypeContext(getMimeContext());
        mimeManager.insertTextLink(w_p, value_p.toString(), efile);
    }

    /**(overridable)
     * This is called if the search with given value returned more than one result.
     * <p>By default this will throw an OwInvalidOperationException, because only one
     * result is allowed.</p>
     * @param w_p Writer to be used for rendering
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object value used for search
     * @param result_p OwObjectCollection returned by the executed search
     * @throws Exception if problem with rendering the results, or many results not allowed
     */
    protected void handleManyResults(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, OwObjectCollection result_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwFieldManagerControlForeignKey.insertReadOnlyField: The search for an efile folder with a property with name '" + m_property + "' and value '" + value_p + "' returned too many results. (" + result_p.size() + ")");
        }
        String message = getContext().localize3("app.OwFieldManagerControlForeignKey.toomanyresults", "The search for an eFile folder with a property with name '%1' and value '%2' returned too many results (%3).", m_property, value_p.toString(),
                "" + result_p.size());
        throw new OwInvalidOperationException(message);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertLabel(java.io.Writer, boolean, java.lang.String, com.wewebu.ow.server.field.OwField, java.lang.String, boolean)
     */
    @Override
    public void insertLabel(Writer w_p, String suffix_p, OwField field, String strID_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDef_p = field.getFieldDefinition();
        w_p.write("<span>");
        w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
        if (suffix_p != null)
        {
            w_p.write(suffix_p);
        }
        w_p.write("</span>");
    }
}