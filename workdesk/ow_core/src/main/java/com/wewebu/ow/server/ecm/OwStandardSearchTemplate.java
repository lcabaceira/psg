package com.wewebu.ow.server.ecm;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.field.OwHeaderFieldColumnInfoDecorator;
import com.wewebu.ow.server.field.OwPriorityRule;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Standard implementation of the search template for FileNet P8 Designer Compatible XML templates.
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
public class OwStandardSearchTemplate implements OwSearchTemplate
{
    private static final String CUSTOMOBJECT_TYPE = "customobject";
    private static final String FOLDER_TYPE = "folder";
    private static final String DOCUMENT_TYPE = "document";

    /** XML attribute name */
    private static final String ATTRIBUTE_ALIGNMENT = "alignment";
    /** XML attribute name */
    private static final String ATTRIBUTE_ALLOWWILDCARD = "allowwildcard";
    /** XML attribute name: choiceId */
    private static final String ATTRIBUTE_CHOICEID = "choiceId";
    /** XML attribute name */
    private static final String ATTRIBUTE_DATE_MODE = "datemode";
    /** XML attribute value */
    private static final String ATTRIBUTE_DATE_MODE_IGNORE_DATE = "ignoredate";
    /** XML attribute value */
    private static final String ATTRIBUTE_DATE_MODE_IGNORE_TIME = "ignoretime";
    /** XML attribute value */
    private static final String ATTRIBUTE_DATE_MODE_USE_TIME = "usetime";
    /** XML attribute name */
    private static final String ATTRIBUTE_EDIT_MODE = "editproperty";
    /** XML attribute value */
    private static final String ATTRIBUTE_EDIT_MODE_EDITABLE = "editable";
    /** XML attribute value */
    private static final String ATTRIBUTE_EDIT_MODE_HIDDEN = "hidden";
    /** XML attribute value */
    private static final String ATTRIBUTE_EDIT_MODE_READONLY = "readonly";
    /** XML attribute value */
    private static final String ATTRIBUTE_EDIT_MODE_REQUIRED = "required";

    /** XML attribute name: haschoices */
    private static final String ATTRIBUTE_HASCHOICES = "haschoices";

    /** XML attribute name */
    private static final String ATTRIBUTE_INCLUDESUBCLASSES = "includesubclasses";

    /** XML attribute name */
    private static final String ATTRIBUTE_INSTRUCTION = "instruction";

    /** XML attribute name */
    private static final String ATTRIBUTE_LITERAL_RANGE_1 = "value_1";
    /** XML attribute name */
    private static final String ATTRIBUTE_LITERAL_RANGE_2 = "value_2";
    /** XML attribute name */
    private static final String ATTRIBUTE_MAX_VALUE = "maxvalue";
    /** XML attribute name */
    private static final String ATTRIBUTE_MIN_VALUE = "minvalue";
    /** XML attribute name */
    private static final String ATTRIBUTE_OBJECTTYPE = "objecttype";
    /** XML attribute name */
    private static final String ATTRIBUTE_OVERRIDE_DATATYPE = "overridedatatype";
    /** XML attribute name */
    private static final String ATTRIBUTE_SORTLEVEL = "sortlevel";
    /** XML attribute name */
    private static final String ATTRIBUTE_SORTORDER = "sortorder";

    /** XML attribute name */
    private static final String ATTRIBUTE_SYMNAME = "symname";
    /** XML attribute name */
    private static final String ATTRIBUTE_UNIQUENAME = "uniquename";

    /** XML attribute name */
    private static final String ATTRIBUTE_VIEW_MODE = "view";

    /** Serialization constant: criteria group element name */
    private static final String CRITERIA_GROUP_ELEMENT_NAME = "c-g";

    /** Serialization constant: criterion element name */
    private static final String CRITERION_ELEMENT_NAME = "c";

    /** XML element name: literal */
    private static final String ELEMENT_LITERAL = "literal";

    /** XML element name: propdesc */
    private static final String ELEMENT_PROPDESC = "propdesc";

    /** XML element name: whereprop */
    private static final String ELEMENT_WHEREPROP = "whereprop";

    /** left delimiter of placeholder token */
    public static final String LITERAL_PLACEHOLDER_LEFT_DELIMITER = "{";

    /** right delimiter of placeholder token */
    public static final String LITERAL_PLACEHOLDER_RIGHT_DELIMITER = "}";
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardSearchTemplate.class);

    /** SQL operators singleton  */
    protected static final SqlOperatorSingleton m_SqlOperators = new SqlOperatorSingleton();

    /** Serialization constant: criterion attribute name */
    private static final String NAME_ATTR_NAME = "name";

    /** Serialization constant OwClass constant: base class name attribute position */
    private static final int OW_CLASS_BASECLASSNAME = 3;

    /** Serialization constant OwClass constant: class type attribute position */
    private static final int OW_CLASS_TYPE = 1;
    /** Serialization constant OwClass constant: enable attribute position */
    private static final int OW_CLASS_CLASSNAME = 2;
    /** Serialization constant OwClass constant: include subclasses attribute position */
    private static final int OW_CLASS_ENABLED = 5;
    /** Serialization constant OwClass constant: class name attribute position */
    private static final int OW_CLASS_INCLUDE_SUBCLASSES = 6;
    /** Serialization constant OwClass constant: resource name attribute position */
    private static final int OW_CLASS_RESOURCENAME = 4;

    /** Serialization constant OwClass constant: serialized structure of OwClass */
    private static final int[] OW_CLASS_STRUCTURE = new int[] { OW_CLASS_TYPE, OW_CLASS_CLASSNAME, OW_CLASS_BASECLASSNAME, OW_CLASS_RESOURCENAME, OW_CLASS_ENABLED };

    /** Serialization constant: second criterion element name */
    private static final String SECOND_CRITERION_ELEMENT_NAME = "s-c";

    /** Serialization constant: value element name */
    private static final String VALUE_ELEMENT_NAME = "v";

    /**
     * Returns the attribute from given node, the method handles the
     * requested attributes as &quot;required&quot; and throws exception.
     * @param node_p org.w3c.dom.Node where to retrieve attribute, (non-null)
     * @param attributeName_p String attribute name to retrieve (non-null)
     * @return org.w3c.dom.Node representing the attribute 
     * @throws OwException if node_p or attributeName_p is <code>null</code>, or attribute not exist
     * @since 2.5.2.0
     */
    protected static Node getAttributeByName(Node node_p, String attributeName_p) throws OwException
    {
        if (node_p == null || attributeName_p == null)
        {
            throw new OwInvalidOperationException("OwStandardSearchTemplate.getAttributeByName: One of the parameter (node or attributeName) is a null reference...");
        }

        Node att = node_p.getAttributes().getNamedItem(attributeName_p);
        if (att == null)
        {
            String msg = "OwStandardSearchTemplate.getAttributeByName: Searchtemplate failure, the attribute=\"" + attributeName_p + "\" could not be found in node=" + node_p.getNodeName();
            LOG.error(msg);
            throw new OwConfigurationException(msg);
        }

        return att;
    }

    /** check if given text is a doc ID 
     * 
     * @param text_p
     * @return boolean
     */
    public static boolean isDocId(String text_p)
    {
        if (text_p.length() != 38)
        {
            return false;
        }

        if (text_p.charAt(9) != '-')
        {
            return false;
        }

        if (text_p.charAt(14) != '-')
        {
            return false;
        }

        if (text_p.charAt(19) != '-')
        {
            return false;
        }

        if (text_p.charAt(24) != '-')
        {
            return false;
        }

        return true;
    }

    /** the cached search the template is working on */
    private OwSearchNode m_cashedSearch;

    /** the list of the column info tuple, which describe the result view */
    protected List m_ColumnInfoList;

    /** DOM Node with the column info */
    protected Node m_ColumnInfoNode;

    /** the context reference */
    private OwNetworkContext m_context;

    /** the field definition provider to resolve the fields  */
    protected OwFieldDefinitionProvider m_fieldDefinitionProvider;

    /** field provider from search */
    protected OwFieldProvider m_fieldProvider = null;

    private Node m_folders;

    /** the default value for the max size of results */
    private int m_iDefaultMaxSize;

    /** flag indicating if the search can be stored in an attribute bag */
    protected boolean m_isWrittableAttributeBagAvailable;
    /** types of versions to search for as defined with OwSearchTemplate.VERSION_SELECT_... */
    protected int m_iVersionSelection = VERSION_SELECT_RELEASED;

    private Locale m_local;
    // members that are used to initialize. After init these members are not longer needed,
    // because all information has been scanned to m_cashedSearch (OwSearchNode)
    protected OwObject m_object;
    private Node m_objectstores;

    /** the already created on the fly classes */
    private Map m_OnTheFlyClassDescriptions;

    /** List of resource IDs the fields should be resolved from, or null to use the default resource */
    protected Collection m_resourceNames;

    private String m_savedSearch;

    /** the Search created out of the template */
    protected OwSearchNode m_Search;

    /** optional HTML layout */
    protected String m_strHtmlLayout;

    /** optional JSP layout page */
    protected String m_strJspLayoutPage;

    /** template name */
    protected String m_strName;

    private Node m_XMLSearchTemplateNode;

    /** Creates a new search template with the given DOM node
    *
    * @param context_p
    * @param xmlSearchTemplateNode_p XML Search Template DOM Node to be wrapped
    * @param strName_p Name of the search
    * @param strResourceName_p name of the resource where the search template origins
    */
    public OwStandardSearchTemplate(OwNetworkContext context_p, Node xmlSearchTemplateNode_p, String strName_p, String strResourceName_p) throws Exception
    {
        m_context = context_p;
        m_local = context_p.getLocale();

        m_strName = strName_p;
        m_XMLSearchTemplateNode = xmlSearchTemplateNode_p;
        m_folders = null;
        m_objectstores = null;

        // call custom handler for additional custom information in overridden search templates
        scanCustomInfo(xmlSearchTemplateNode_p);
    }

    @Deprecated
    /**
     * Constructor is deprecated use {@link #OwStandardSearchTemplate(OwNetworkContext, Node, String, String)} instead
     * @param context_p
     * @param xmlSearchTemplateNode_p
     * @param strName_p
     * @param strResourceName_p
     * @param useSearchPaths_p if true the {@link OwSearchPathField} criteria will be used for search paths<br>
     *                         if false the OwStandardResourceSelectPath criteria will be used for search paths 
     * @throws Exception
     * @deprecated since 4.0.0.0 use {@link #OwStandardSearchTemplate(OwNetworkContext, Node, String, String)} constructor
     */
    public OwStandardSearchTemplate(OwNetworkContext context_p, Node xmlSearchTemplateNode_p, String strName_p, String strResourceName_p, boolean useSearchPaths_p) throws Exception
    {
        this(context_p, xmlSearchTemplateNode_p, strName_p, strResourceName_p);
        if (!useSearchPaths_p)
        {
            throw new OwNotSupportedException("The support of OwStandardResourceSelectPath was deprecated in 3.0.0.0 and is no more supported since 4.0.0.0");
        }
    }

    /** Creates a new search template with the given object
     * 
     *  NOTE:   The syntax of the XML Template is compatible with FileNet P8 Search designer.
     *          I.e. this function can read FileNet P8 SearchDesigner created templates.
     *          
     * @param context_p
     * @param obj_p
     * @throws Exception
     */
    public OwStandardSearchTemplate(OwNetworkContext context_p, OwObject obj_p) throws Exception
    {
        // just store the given OwObject in a member. We retrieve the XML later in init, this saves a lot of runtime during login.
        // otherwise all searchtemplates, even those the user has not role for, would be loaded during login.
        m_context = context_p;
        m_local = m_context.getLocale();
        m_object = obj_p;
        m_strName = obj_p.getName();
    }

    /**
     * 
     * @param context_p
     * @param obj_p
     * @param useSearchPaths_p if true the {@link OwSearchPathField} criteria will be used for search paths<br>
     *                         if false the OwStandardResourceSelectPath criteria will be used for search paths
     * @throws Exception
     * @deprecated since 4.0.0.0 use {@link #OwStandardSearchTemplate(OwNetworkContext, OwObject)} constructor
     */
    public OwStandardSearchTemplate(OwNetworkContext context_p, OwObject obj_p, boolean useSearchPaths_p) throws Exception
    {
        this(context_p, obj_p);
        if (!useSearchPaths_p)
        {
            throw new OwNotSupportedException("The support of OwStandardResourceSelectPath was deprecated in 3.0.0.0 and is no more supported since 4.0.0.0");
        }
    }

    /** check if saved searches can be deleted
     * 
     */
    public boolean canDeleteSearch()
    {

        return m_isWrittableAttributeBagAvailable;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwSearchTemplate#canSaveSearch()
     */
    public boolean canSaveSearch()
    {
        return m_isWrittableAttributeBagAvailable;
    }

    /** check if saved searches can be updated
     * 
     */
    public boolean canUpdateSearch()
    {
        return m_isWrittableAttributeBagAvailable;
    }

    /**
     * Operator conversion utility
     * @param strOperator_p the search template defined operator (egg. "in", "eq").
     * @return the int code for the give operator
     * @throws OwConfigurationException if the operator can not be converted
     */
    protected int convertOperator(String strOperator_p) throws OwConfigurationException
    {
        try
        {
            Integer operator = (Integer) m_SqlOperators.m_OperatorMap.get(strOperator_p);
            return operator.intValue();
        }
        catch (NullPointerException e)
        {
            LOG.error("OwStandardSearchTemplate.convertOperator: Invalid operator = '" + strOperator_p + "' in searchtemplate = " + getName(), e);
            throw new OwConfigurationException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.invalidOperator", "Invalid operator '%1' in search template '%2'", strOperator_p, getName()), e);
        }
    }

    /**  retrieve Column information for the result view from the ColumnInfoNode
     * @param columnInfoNode_p XML DOM Node with column info compatible with FileNet P8 SearchDesigner.
     * @return List of OwFieldColumnInfo
     */
    private List createColumnInfo(Node columnInfoNode_p) throws Exception
    {
        // === retrieve Column information for the result view from the ColumnInfoNode
        List ColumnInfoList = new ArrayList();

        for (Node n = columnInfoNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (!n.hasAttributes())
            {
                continue;
            }

            // get the class name of the search property
            String strClassName = getAttributeByName(n, ATTRIBUTE_SYMNAME).getNodeValue();

            // get the alignment
            int iAlignment = OwFieldColumnInfo.ALIGNMENT_DEFAULT;
            try
            {
                String strAlignment = n.getAttributes().getNamedItem(ATTRIBUTE_ALIGNMENT).getNodeValue();
                if (strAlignment.equalsIgnoreCase("left"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_LEFT;
                }
                if (strAlignment.equalsIgnoreCase("center"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_CENTER;
                }
                if (strAlignment.equalsIgnoreCase("right"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_RIGHT;
                }
                if (strAlignment.equalsIgnoreCase("leftnowrap"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_LEFT_NOWRAP;
                }
                if (strAlignment.equalsIgnoreCase("centernowrap"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_CENTER_NOWRAP;
                }
                if (strAlignment.equalsIgnoreCase("rightnowrap"))
                {
                    iAlignment = OwFieldColumnInfo.ALIGNMENT_RIGHT_NOWRAP;
                }
            }
            catch (NullPointerException e)
            {
                /* ignore, node was not defined */
            }

            // we get the display name from the property class, so display name changes will reflect automatically in search template
            //String strDisplayName = n.getAttributes().getNamedItem("name").getNodeValue();
            OwFieldDefinition propClass = getPropertyClassInternal(strClassName, new OwStandardXMLUtil(n));
            if (propClass != null)
            {
                OwStandardFieldColumnInfo info = new OwStandardFieldColumnInfo(propClass, iAlignment);
                ColumnInfoList.add(new OwHeaderFieldColumnInfoDecorator(info, getName()));
            }
            else
            {
                // === criteria not defined as property
                // ...do special treatment of special criteria here
                LOG.debug("OwStandardSearchTemplate.createColumnInfo: Search Template parser error, undefined criteria property = " + strClassName);
                throw new OwObjectNotFoundException(OwString.localize1(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedCriteriaProperty", "Search Template parser error, undefined criteria property: %1", strClassName));
            }
        }

        return ColumnInfoList;
    }

    /** (overridable) factory method
     * 
     * @param iOp_p combination operator which should be applied to the child OwSearchNode elements as defined in OwSearchNode
     * @param iNodeType_p the type of the branch can be one of the NODE_TYPE_... definition
     */
    protected OwSearchNode createSearchNode(int iOp_p, int iNodeType_p)
    {
        return new OwSearchNode(iOp_p, iNodeType_p);
    }

    /** (overridable) factory method
     *
     *  @param field_p OwField criteria as field
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     *  @param strUniqueName_p String a unique name that identifies this criteria 
     *  @param strInstruction_p String instruction to be displayed, can be null
     *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     */
    protected OwSearchNode createSearchNode(OwField field_p, int iOp_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p) throws Exception
    {
        return new OwSearchNode(field_p, iOp_p, iAttributes_p, strUniqueName_p, strInstruction_p, wildcarddefinitions_p);
    }

    /** (overridable) factory method
     * 
    *  @param fieldDefinition_p OwFieldDefinition of criteria
    *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
    *  @param oInitialAndDefaultValue_p initial and default value, for range operators it is a two field array, first field for first range criteria, second field for second range criteria
    *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
    *  @param strUniqueName_p String a unique name that identifies this criteria 
    *  @param strInstruction_p String instruction to be displayed, can be null
    *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
    */
    protected OwSearchNode createSearchNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p)
    {
        return new OwSearchNode(fieldDefinition_p, iOp_p, oInitialAndDefaultValue_p, iAttributes_p, strUniqueName_p, strInstruction_p, wildcarddefinitions_p);
    }

    /** (overridable) factory method
     * 
     *  @param fieldDefinition_p OwFieldDefinition
     *  @param iOp_p criteria operator which should be applied to the value as defined in OwSearchCriteria
     *  @param oInitialAndDefaultValue_p initial and default value, for range operators it is a two field array, first field for first range criteria, second field for second range criteria
     *  @param iAttributes_p int attributes as defined with ATTRIBUTE_...
     *  @param strUniqueName_p String a unique name that identifies this criteria 
     *  @param strInstruction_p String instruction to be displayed, can be null
     *  @param wildcarddefinitions_p Collection of OwWildCardDefinition, or null if no wildcards are allowed
     *  
     *  @param minValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param maxValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param defaultValue_p Object value to override the inherited FieldDefinition with custom values
     *  @param sJavaClassName_p String value to override the inherited FieldDefinition with custom values
     */
    protected OwSearchNode createSearchNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p, Object minValue_p,
            Object maxValue_p, Object defaultValue_p, String sJavaClassName_p) throws Exception
    {
        return new OwSearchNode(fieldDefinition_p, iOp_p, oInitialAndDefaultValue_p, iAttributes_p, strUniqueName_p, strInstruction_p, wildcarddefinitions_p, minValue_p, maxValue_p, defaultValue_p, sJavaClassName_p);
    }

    /** delete the saved search
     * 
     * @param name_p
     */
    public void deleteSavedSearch(String name_p) throws Exception
    {
        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
        String userId = network.getCredentials().getUserInfo().getUserID();
        OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_strName, userId, true, false);
        Collection names = attrBag.getAttributeNames();
        if (name_p != null && names.contains(name_p))
        {
            attrBag.remove(name_p);
            attrBag.save();
        }
        this.m_savedSearch = null;
    }

    /** get the cached list of the column info tuple, which describe the result view 
     * @return Collection of OwFieldColumnInfo items
     */
    public Collection getColumnInfoList() throws OwObjectNotFoundException, OwConfigurationException
    {
        // === retrieve Column information for the result view from the ColumnInfoNode
        if (m_ColumnInfoList == null)
        {
            if (null == m_ColumnInfoNode)
            {
                return new ArrayList();
                //throw new OwConfigurationException(OwString.localize(m_local,"server.ecm.OwStandardSearchTemplate.nocolinfodef","You have not selected any columns for the result list in the search template. ") + getName());
            }

            try
            {
                m_ColumnInfoList = createColumnInfo(m_ColumnInfoNode);
            }
            catch (OwObjectNotFoundException e)
            {
                // empty column info
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Error found in the search template = " + getName(), e);
                throw new OwConfigurationException(OwString.localize1(m_local, "server.ecm.OwStandardSearchTemplate.ErrorInSearchtemplate", "Error found in search template: %1", getName()), e);
            }
        }

        return m_ColumnInfoList;
    }

    /** get a reference to the network context
     * 
     * @return an {@link OwNetworkContext}
     */
    protected OwNetworkContext getContext()
    {
        return m_context;
    }

    /** get the default value for the  maximum size of results or 0 if not defined
     * 
     * @return int
     */
    public int getDefaultMaxSize()
    {
        return m_iDefaultMaxSize;
    }

    /** get the template name, MUST be available before calling init
     * @param locale_p Locale to use
     * @return displayname of the template
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, getName());
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        if (m_fieldProvider == null)
        {
            m_fieldProvider = getSearch(false).getFieldProvider();
        }

        return m_fieldProvider.getField(strFieldClassName_p);
    }

    /**
     * (overridable) method for field definition provider based definition creation  
     * @param strFieldDefinitionName_p field definition name like in {@link OwFieldDefinitionProvider#getFieldDefinition(String, String)}
     * @param strResourceName_p resource name like in {@link OwFieldDefinitionProvider#getFieldDefinition(String, String)}
     * @return the field definition provider based result
     * @throws OwObjectNotFoundException
     * @throws Exception
     */
    protected OwFieldDefinition getFieldDefinitionProviderField(String strFieldDefinitionName_p, String strResourceName_p) throws OwObjectNotFoundException, Exception
    {
        return m_fieldDefinitionProvider.getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return TYPE_SEARCH;
    }

    /** singleton for placeholders */
    /*
    private static class OwLiteralPlaceholderSingleton
    {
        private static OwLiteralPlaceholderSingleton m_instance = new OwLiteralPlaceholderSingleton(); 
        
    	private Map m_placeholders = new HashMap();
    	
    	public OwLiteralPlaceholderSingleton()
    	{
    		m_placeholders.put(LITERAL_PLACEHOLDER_TODAY,new Date());
    	}
    	
    	public static Map getPlaceholderMap()
    	{
    		return m_instance.m_placeholders;
    	}
    }
    */

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN | OwSearchNode.FILTER_NONPROPERTY);
    }

    /** get the optional HTML layout associated with this template
     * @return String containing a HTML fragment with place holders for the search parameters of the form {#<searchparametername>#}, or null if not available
     */
    public String getHtmlLayout()
    {
        return m_strHtmlLayout;
    }

    /** get a optional icon to be displayed with the search template, MUST be available before calling init
     * @return String icon path relative to /<design dir>/, or null if no icon is defined
     */
    public String getIcon()
    {
        return null;
    }

    /** comput the default value/s from the given literal node
     * 
     * @param defaultValueNode_p
     * @param propClass_p
     * @param iOP_p
     * @return an {@link Object}
     * @throws Exception 
     */
    private Object getInitialAndDefaultValueFromNode(Node defaultValueNode_p, OwFieldDefinition propClass_p, int iOP_p) throws Exception
    {
        Object initialanddefaultvalue = null;

        if (defaultValueNode_p != null)
        {
            if (OwSearchOperator.isCriteriaOperatorRange(iOP_p))
            {
                // === range with two values
                Object[] defaultvalues = new Object[2];

                for (Node nDefaultValue = defaultValueNode_p.getFirstChild(); nDefaultValue != null; nDefaultValue = nDefaultValue.getNextSibling())
                {
                    if (nDefaultValue.getNodeName().equals(ATTRIBUTE_LITERAL_RANGE_1))
                    {
                        defaultvalues[0] = resolveLiteralValue(getName(), propClass_p, nDefaultValue.getFirstChild());
                    }

                    if (nDefaultValue.getNodeName().equals(ATTRIBUTE_LITERAL_RANGE_2))
                    {
                        defaultvalues[1] = resolveLiteralValue(getName(), propClass_p, nDefaultValue.getFirstChild());
                    }
                }

                initialanddefaultvalue = defaultvalues;
            }
            else
            {
                // single value
                initialanddefaultvalue = resolveLiteralValue(getName(), propClass_p, defaultValueNode_p.getFirstChild());
            }
        }

        return initialanddefaultvalue;
    }

    /** get the optional JSP layout page associated with this template
     * @return String containing a JSP page name, or null if it is not available
     */
    public String getJspLayoutPage()
    {
        return m_strJspLayoutPage;
    }

    /** get the template name, MUST be available before calling init
     * @return name of the template
     */
    public String getName()
    {
        return m_strName;
    }

    /** compute the OwObject object type from the given class name
     */
    private int getObjectTypeFromClassName(String strClassName_p)
    {
        if (strClassName_p != null)
        {
            if (strClassName_p.equals(DOCUMENT_TYPE))
            {
                return OwObjectReference.OBJECT_TYPE_DOCUMENT;
            }

            if (strClassName_p.equals(FOLDER_TYPE))
            {
                return OwObjectReference.OBJECT_TYPE_FOLDER;
            }

            if (strClassName_p.equals(CUSTOMOBJECT_TYPE))
            {
                return OwObjectReference.OBJECT_TYPE_CUSTOM;
            }
        }
        return OwObjectReference.OBJECT_TYPE_UNDEFINED;
    }

    /** get the already created on the fly classes
     * @return Map containing OwFieldDefinition's
     */
    protected Map getOnTheFlyClassDescriptions()
    {
        if (m_OnTheFlyClassDescriptions == null)
        {
            m_OnTheFlyClassDescriptions = new HashMap();
        }

        return m_OnTheFlyClassDescriptions;
    }

    /** get a priority rule for priority 
     * @return OwPriorityRule or null if undefined
     * */
    public OwPriorityRule getPriorityRule()
    {
        return null;
    }

    /** resolve the requested search field definition or create a on the fly class description
     *
     * @param strClassName_p String requested class name of field definition
     * @param node_p OwXMLUtil Node with class attributes for on the fly creation
     *
     * @return OwFieldDefinition
     */
    protected OwFieldDefinition getPropertyClassInternal(String strClassName_p, OwXMLUtil node_p) throws Exception
    {
        // === first check if a on the fly attribute has been defined
        String strJavaClassName = node_p.getSafeStringAttributeValue("datatype", null);

        if (strJavaClassName != null)
        {
            // === on the fly property is defined
            // === resolve class via stored on-the-fly classes
            OwFieldDefinition propClass = null;
            propClass = (OwFieldDefinition) getOnTheFlyClassDescriptions().get(strClassName_p);

            // === try to create the class on the fly with the given node attributes
            if (propClass == null)
            {
                String strDisplayName = node_p.getSafeStringAttributeValue("name", null);
                if (strDisplayName != null)
                {
                    propClass = new OwOnTheFlySearchFieldDescription(strClassName_p, new OwString(strClassName_p, strDisplayName, true), strJavaClassName);
                    getOnTheFlyClassDescriptions().put(strClassName_p, propClass);
                }
                else
                {
                    String msg = "OwStandardSearchTemplate.getPropertyClassInternal: Error in searchtemplate = " + getName() + ", on the fly property could not be resolved. Please specify datatype and name for property = " + strClassName_p;
                    LOG.error(msg);
                    throw new OwObjectNotFoundException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedField", "Error in searchtemplate '%1': could not resolve property '%2'", getName(), strClassName_p));
                }
            }

            return propClass;
        }

        // === resolve class via derived class (ECM Adapter), iterate over all resources specified in the search template
        OwFieldDefinition fieldDef = null;
        if (null != m_resourceNames)
        {
            Iterator it = m_resourceNames.iterator();
            while ((fieldDef == null) && it.hasNext())
            {
                // === try to resolve with this resource
                try
                {
                    fieldDef = getFieldDefinitionProviderField(strClassName_p, (String) it.next());
                }
                catch (OwObjectNotFoundException e)
                {
                    // ignore. we will continue with the next resource.
                }
            }
        }
        else
        {
            // === resolve with the default resource (null)
            fieldDef = getFieldDefinitionProviderField(strClassName_p, null);
        }
        if (fieldDef == null)
        {
            LOG.error("OwStandardSearchTemplate.getPropertyClassInternal: Searchtemplate = " + getName() + ", could not resolve property = " + strClassName_p);
            throw new OwObjectNotFoundException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedField", "Error in searchtemplate '%1': could not resolve property '%2'", getName(), strClassName_p));
        }

        // === exchange the enum collection if requested in the XML
        OwXMLUtil propdescNode = node_p.getSubUtil(ELEMENT_PROPDESC);
        if (propdescNode != null)
        {
            String hasChoices = propdescNode.getSafeStringAttributeValue(ATTRIBUTE_HASCHOICES, null);
            String choiceId = propdescNode.getSafeStringAttributeValue(ATTRIBUTE_CHOICEID, null);
            if ((hasChoices != null) && (choiceId != null) && hasChoices.equalsIgnoreCase("true"))
            {
                // try to cast the field definition provider to an OwNetwork
                OwNetwork network = null;
                try
                {
                    network = (OwNetwork) m_fieldDefinitionProvider;
                }
                catch (ClassCastException cce)
                {
                    LOG.error("OwStandardSearchTemplate.getPropertyClassInternal: Searchtemplate = " + getName() + ", property = " + strClassName_p + ", can not cast the field definition provider to an OwNetwork to retrieve the choice list...", cce);
                    throw new OwObjectNotFoundException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedField", "Error in searchtemplate '%1': could not resolve property '%2'", getName(), strClassName_p), cce);
                }
                // retrieve the choice list as application object
                OwEnumCollection newEnumCollection = null;
                try
                {
                    newEnumCollection = (OwEnumCollection) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ENUM_COLLECTION, choiceId, false, false);
                }
                catch (Exception e)
                {
                    LOG.error("OwStandardSearchTemplate.getPropertyClassInternal: Searchtemplate = " + getName() + ", property = " + strClassName_p + ", can not read the enum collection, choiceId = '" + choiceId + "' from the network...", e);
                    throw new OwObjectNotFoundException(OwString.localize3(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedChoiceList", "Error in searchtemplate \"%1\": cannot read the enum collection with ID '%2' for property '%3'",
                            getName(), choiceId, strClassName_p), e);
                }
                // exchange the enum collection
                fieldDef = new OwFieldDefinitionEnumExchangeWrapper(fieldDef, newEnumCollection);
            }
        }

        return fieldDef;
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            OwField field = getField(sName_p);
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get the name of the current set search, or null if no saved search is set
     * 
     */
    public String getSavedSearch() throws Exception
    {
        return m_savedSearch;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwSearchTemplate#getSavedSearches()
     */
    public Collection getSavedSearches() throws Exception
    {
        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
        String userId = network.getCredentials().getUserInfo().getUserID();
        OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_strName, userId, true, false);
        Collection result = attrBag.getAttributeNames();
        if (result != null)
        {
            result = new LinkedList(result);
            Collections.sort((List) result);
        }
        return result;
    }

    /** get the cached search tree created out of the template
     * @param fRefresh_p true = reload search from template, false = get cached search
     * @return OwSearchNode the search tree created out of the template
     * 
     */
    public OwSearchNode getSearch(boolean fRefresh_p) throws Exception
    {
        // === retrieve the search from the search node
        if (m_cashedSearch == null || fRefresh_p)
        {
            try
            {
                // create new search
                m_cashedSearch = (OwSearchNode) m_Search.clone();
                // reset field map for getFieldsByName function
                m_fieldProvider = null;
            }
            catch (Exception e)
            {
                LOG.error("OwStandardSearchTemplate.getSearch: Search execution error.", e);
                throw new OwInvalidOperationException(OwString.localize(m_local, "server.ecm.OwStandardSearchTemplate.searchExecutionError", "Search execution error."), e);
            }
        }
        return m_cashedSearch;
    }

    /** compute the criteria attributes from a p8 XML node
     * 
     * @param node_p
     * @param iDefaultEditAttribute_p int default value for the edit attribute
     * @return an int
     * @throws OwConfigurationException 
     */
    protected int getSearchAttributeFromNode(Node node_p, int iDefaultEditAttribute_p) throws OwConfigurationException
    {
        int iRet = 0;

        // get edit attribute
        String sEditAttribute = OwXMLDOMUtil.getSafeStringAttributeValue(node_p, ATTRIBUTE_EDIT_MODE, null);
        if (sEditAttribute == null)
        {
            sEditAttribute = OwXMLDOMUtil.getSafeStringAttributeValue(node_p, ATTRIBUTE_VIEW_MODE, "");
        }

        if (sEditAttribute.equals(ATTRIBUTE_EDIT_MODE_EDITABLE))
        {
            iRet |= OwSearchCriteria.ATTRIBUTE_NONE;
        }
        else if (sEditAttribute.equals(ATTRIBUTE_EDIT_MODE_READONLY))
        {
            iRet |= OwSearchCriteria.ATTRIBUTE_READONLY;
        }
        else if (sEditAttribute.equals(ATTRIBUTE_EDIT_MODE_HIDDEN))
        {
            iRet |= (OwSearchCriteria.ATTRIBUTE_HIDDEN + OwSearchCriteria.ATTRIBUTE_READONLY);
        }
        else if (sEditAttribute.equals(ATTRIBUTE_EDIT_MODE_REQUIRED))
        {
            iRet |= OwSearchCriteria.ATTRIBUTE_REQUIRED;
        }
        else
        {
            iRet |= iDefaultEditAttribute_p;
        }

        // get date mode attribute
        String sDateModeAttriute = OwXMLDOMUtil.getSafeStringAttributeValue(node_p, ATTRIBUTE_DATE_MODE, ATTRIBUTE_DATE_MODE_IGNORE_TIME);

        if (sDateModeAttriute.equals(ATTRIBUTE_DATE_MODE_IGNORE_TIME))
        { // === datemode: ignore time part
            iRet |= OwSearchCriteria.ATTRIBUTE_IGNORE_TIME;
        }
        else if (sDateModeAttriute.equals(ATTRIBUTE_DATE_MODE_IGNORE_DATE))
        { // === datemode: ignore date part
            iRet |= OwSearchCriteria.ATTRIBUTE_IGNORE_DATE;
        }
        else if (!sDateModeAttriute.endsWith(ATTRIBUTE_DATE_MODE_USE_TIME))
        { // ===  datemode: undefined
            StringBuilder msg = new StringBuilder();
            msg.append("OwStandardSearchTemplate.getSearchAttributeFromNode: Undefined date mode = ");
            msg.append(sDateModeAttriute);
            msg.append(", please define correct ");
            msg.append(ATTRIBUTE_DATE_MODE);
            msg.append(" tag. One of: ");
            msg.append(ATTRIBUTE_DATE_MODE_IGNORE_TIME);
            msg.append(", ");
            msg.append(ATTRIBUTE_DATE_MODE_IGNORE_DATE);
            msg.append(", ");
            msg.append(ATTRIBUTE_DATE_MODE_USE_TIME);

            LOG.error(msg.toString());
            throw new OwConfigurationException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedDateMode", "Please define a corect '%1'. It must be one of the following: '%2'", ATTRIBUTE_DATE_MODE,
                    ATTRIBUTE_DATE_MODE_IGNORE_TIME + ", " + ATTRIBUTE_DATE_MODE_IGNORE_DATE + ", " + ATTRIBUTE_DATE_MODE_USE_TIME));
        }
        // === datemode: use time part
        // requires no OwSearchCriteria flag

        return iRet;
    }

    /** get the sort to use for the result list
     *
     * @param iMinSortCriteria_p int min number of sort criteria that the returned sort should support
     *
     * @return OwSort the number of max sort criteria can be higher than iMinSortCriteria_p but not less
     */
    public OwSort getSort(int iMinSortCriteria_p)
    {
        if (m_ColumnInfoNode == null)
        {
            return new OwSort(iMinSortCriteria_p, true);
        }

        Map sorts = new HashMap();

        int iMaxLevel = 0;
        for (Node n = m_ColumnInfoNode.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (!n.hasAttributes())
            {
                continue;
            }

            // get the class name of the search property
            String strClassName;
            try
            {
                strClassName = getAttributeByName(n, ATTRIBUTE_SYMNAME).getNodeValue();
            }
            catch (OwException confEx)
            {
                LOG.warn("OwStandardSearchTemplate.getSort: error during attribute request", confEx);
                //do not proceed try next element/node
                continue;
            }

            try
            {
                // get sort
                Integer sortlevel = Integer.valueOf(n.getAttributes().getNamedItem(ATTRIBUTE_SORTLEVEL).getNodeValue());
                String strSortorder = n.getAttributes().getNamedItem(ATTRIBUTE_SORTORDER).getNodeValue();

                boolean fAsc = strSortorder.equalsIgnoreCase("ascending");

                OwSort.OwSortCriteria crit = new OwSort.OwSortCriteria(strClassName, fAsc);

                // store with sortlevel so we can than put it into order when returning
                if (sortlevel.intValue() > iMaxLevel)
                {
                    iMaxLevel = sortlevel.intValue();
                }

                // only sort for level > 0
                if (sortlevel.intValue() > 0)
                {
                    sorts.put(sortlevel, crit);
                }
            }
            catch (NullPointerException e)
            {/* ignore, node was not defined */
            }
        }

        if (sorts.size() > iMinSortCriteria_p)
        {
            iMinSortCriteria_p = sorts.size();
        }

        OwSort sort = new OwSort(iMinSortCriteria_p, true);

        // now return sorts in the order of their level, last one has highest priority and the lowest P8 level
        // also ignore level = 0 i.e. no sort
        for (int i = iMaxLevel; i > 0; i--)
        {
            OwSort.OwSortCriteria crit = (OwSort.OwSortCriteria) sorts.get(Integer.valueOf(i));
            if (crit != null)
            {
                sort.addCriteria(crit);
            }
        }

        return sort;
    }

    /** get the version selection type
     *
     * @return int types of versions to search for as defined with VERSION_SELECT_...
     */
    public int getVersionSelection()
    {
        return m_iVersionSelection;
    }

    /** get the zones from a verity node */
    private void getZones(Node node_p, List zones_p)
    {
        // === recurse the node until a zone node is found
        for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals("verityzonename"))
            {
                // === add zone name    
                zones_p.add(n.getFirstChild().getNodeValue());
            }
            else
            {
                getZones(n, zones_p);
            }
        }
    }

    /** check if the optional HTML layout is available
     * @return true if a HTML layout is available via getHtmlLayout
     */
    public boolean hasHtmlLayout()
    {
        return (m_strHtmlLayout != null);
    }

    /** check if the optional JSP layout page is available
     * @return true if a JSP layout page is available via getJspLayoutPage
     */
    public boolean hasJspLayoutPage()
    {
        return (m_strJspLayoutPage != null);
    }

    /** init the search template so that the specified fields can be resolved
     *
     * @param fieldDefinitionProvider_p OwFieldDefinitionProvider to resolve fields
     */
    public void init(OwFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception
    {

        m_isWrittableAttributeBagAvailable = isWrittableAttributeBagAvailable();

        if (m_object != null)
        {
            loadXMLTemplateFromObject(m_object);

            // garbage collection, save memory
            m_object = null;
        }

        m_fieldDefinitionProvider = fieldDefinitionProvider_p;

        /** DOM Node with the search info */
        Node whereSearchNode = null;
        /** content based retrieval node */
        Node cbrNode = null;
        /** from node (class) */
        Node fromNodeNode = null;
        /** node for the selected subclasses */
        Node subClassesNode = null;

        // === retrieve the version selection flag
        parseVersionSelection(OwXMLDOMUtil.getSafeStringAttributeValue(m_XMLSearchTemplateNode, "versionselection", null));

        // === find the column info node and the searchspec node from XML compatible with FileNet P8 Searchdesigner.
        for (Node n = m_XMLSearchTemplateNode.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeType() != Node.ELEMENT_NODE)
            {//skip non-element nodes
                continue;
            }

            // === retrieve the column node
            if (n.getNodeName().equalsIgnoreCase("select"))
            {
                // === found column info node, get subnode
                for (Node col = n.getFirstChild(); col != null; col = col.getNextSibling())
                {
                    if (col.getNodeName().equalsIgnoreCase("selectprops"))
                    {
                        // === found the column info node    
                        m_ColumnInfoNode = col;
                        break;
                    }
                }
                continue;
            }

            // === retrieve the form clause (requested class)
            if (n.getNodeName().equalsIgnoreCase("from"))
            {
                // === found the from node
                fromNodeNode = n;
                continue;
            }

            // === retrieve the form clause (requested class)
            if (n.getNodeName().equalsIgnoreCase("subclasses"))
            {
                // === found the from node
                subClassesNode = n;
                continue;
            }

            // === retrieve the where clause node
            if (n.getNodeName().equalsIgnoreCase("where"))
            {
                // === found the search node
                whereSearchNode = n;
                continue;
            }

            // === retrieve the HTML layout node
            if (n.getNodeName().equalsIgnoreCase("layout"))
            {
                // === found optional layout node
                if (n.getFirstChild() != null)
                {
                    m_strHtmlLayout = n.getFirstChild().getNodeValue();
                }
                continue;
            }

            // === retrieve the jasp page node
            if (n.getNodeName().equalsIgnoreCase("jsppage"))
            {
                // === found optional layout node
                if (n.getFirstChild() != null)
                {
                    m_strJspLayoutPage = n.getFirstChild().getNodeValue();
                }
                continue;
            }

            // === retrieve the folder node
            if (n.getNodeName().equalsIgnoreCase("folders"))
            {
                m_folders = n;
                continue;
            }

            // === retrieve the resource node
            if (n.getNodeName().equalsIgnoreCase("objectstores"))
            {
                m_objectstores = n;
                continue;
            }

            // === retrieve the content based retrieval node
            if (n.getNodeName().equalsIgnoreCase("veritycontent"))
            {
                cbrNode = n;
            }
        }

        // create root node
        m_Search = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_COMBINATION);

        OwSearchNode newSpecialSearchNode = null;
        // === scan folders and ObjectStore node and add folder properties to search
        // Scan it before all other, since we need the resource name
        if ((m_folders != null) || (m_objectstores != null))
        {
            newSpecialSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);

            m_Search.add(newSpecialSearchNode);

            scanResourceNodeEx(newSpecialSearchNode, m_folders, m_objectstores);
        }

        // === scan search clause for property based search
        if (whereSearchNode != null)
        {
            // === scan where clause
            OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
            m_Search.add(newSearchNode);
            // traverse of sub DOM nodes
            scanSearchNode(whereSearchNode, newSearchNode);
        }

        // === scan from node
        if (fromNodeNode != null)
        {
            if (newSpecialSearchNode == null)
            {
                newSpecialSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
                m_Search.add(newSpecialSearchNode);
            }

            scanClasses(newSpecialSearchNode, fromNodeNode, subClassesNode);
        }

        // === scan content based retrieval node and add CBR properties to search
        if (null != cbrNode)
        {
            OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_CBR);
            m_Search.add(newSearchNode);
            scanCBRNode(newSearchNode, cbrNode);
        }

        // garbage collection, save memory
        m_XMLSearchTemplateNode = null;
        m_folders = null;
        m_objectstores = null;
    }

    /** check if search template is already initialized
     * @return boolean true = template is initialized already, false = init was not called yet. 
     * */
    public boolean isInitalized()
    {
        return m_Search != null;
    }

    /**
     * 
     * @param strOperator_p
     * @return <code>true</code> if the given String is an operator that can be converted using {@link #convertOperator(String)}, <code>false</code> otherwise 
     * @throws OwConfigurationException
     */
    protected boolean isOperator(String strOperator_p) throws OwConfigurationException
    {
        return m_SqlOperators.m_OperatorMap.containsKey(strOperator_p);
    }

    /**
     * Check if there exists the possibility to save writable attribute bags.
     * @return <code>true</code> if attribute bags can be saved
     * @since 3.0.0.0
     */
    private boolean isWrittableAttributeBagAvailable()
    {
        boolean result = false;
        try
        {
            OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
            String userId = network.getCredentials().getUserInfo().getUserID();
            OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_strName, userId, true, false);
            if (attrBag != null)
            {
                result = true;
            }
        }
        catch (Exception e)
        {

        }
        return result;
    }

    /** load the XML from the given OwObject 
     * @throws Exception 
     * @throws IOException 
     * @throws SAXException */
    private void loadXMLTemplateFromObject(OwObject obj_p) throws SAXException, IOException, Exception
    {
        /** folders node */
        Node foldersNode = null;
        /** resource (ObjectStores)  node */
        Node objectstoresNode = null;

        org.w3c.dom.Document doc = null;
        try
        {
            // === parse the XML Content of the OwObject to create a OwSearchTemplate.
            doc = OwXMLDOMUtil.getDocumentFromInputStream(obj_p.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null));
        }
        catch (Exception e)
        {
            String msg = "Exception loading XML Search-Template from Object";
            if (obj_p != null)
            {
                msg += ", name = " + obj_p.getName();
            }
            LOG.error(msg, e);
            throw e;
        }

        // === retrieve the options node
        NodeList options = doc.getElementsByTagName("options");
        if (options.getLength() > 0)
        {
            Node option = options.item(0);
            m_iDefaultMaxSize = Integer.parseInt(OwXMLDOMUtil.getSafeStringAttributeValue(option, "maxrecords", "0"));
        }

        // === retrieve the folder node
        // NOTE: In FileNet P8 V2.0 the folders node is within the searchclause node and retrieved in the later init method
        NodeList folders = doc.getElementsByTagName("folders");
        if (folders.getLength() > 0)
        {
            foldersNode = folders.item(0);
        }

        // === retrieve the version selection flag
        NodeList rootNodes = doc.getElementsByTagName("searchspec");
        if (rootNodes.getLength() > 0)
        {
            Node rootNode = rootNodes.item(0);
            parseVersionSelection(OwXMLDOMUtil.getSafeStringAttributeValue(rootNode, "versionselection", null));
        }

        // === retrieve the resource node
        // NOTE: In FileNet P8 V2.0 the resource (ObjectStore) node is within the searchclause node and retrieved in the later init method
        NodeList objectstores = doc.getElementsByTagName("objectstores");
        if (objectstores.getLength() > 0)
        {
            objectstoresNode = objectstores.item(0);
        }

        // === retrieve the only node with search specs
        // FileNet V3.0
        org.w3c.dom.NodeList searchNodes = doc.getElementsByTagName("searchclause");
        // or try FileNet V2.0
        if ((searchNodes == null) || (searchNodes.getLength() == 0))
        {
            searchNodes = doc.getElementsByTagName("searchclauses");
        }

        if (searchNodes.getLength() > 0)
        {
            m_XMLSearchTemplateNode = searchNodes.item(0);
            m_folders = foldersNode;
            m_objectstores = objectstoresNode;
        }
        else
        {
            LOG.error("OwStandardSearchTemplate.loadXMLTemplateFromObject: Can not load XML from object, searchNodes.getLength()=0, object getName = " + obj_p.getName());
            throw new OwInvalidOperationException(OwString.localize1(m_local, "server.ecm.OwStandardSearchTemplate.cannotLoadXmlFromObject", "Cannot load XML from object with name: '%1'", obj_p.getName()));
        }

        // call custom handler for additional custom information in overridden search templates
        scanCustomInfo(doc);
    }

    /** get the OwSearchTemplate.VERSION_SELECT_... definition from the p8 attribute */
    private void parseVersionSelection(String strVersionSelectionAttribute_p)
    {
        if (strVersionSelectionAttribute_p == null)
        {
            return;
        }

        // ow defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("minorversions"))
        {
            m_iVersionSelection = VERSION_SELECT_MINORS;
            return;
        }

        // ow defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("majorversions"))
        {
            m_iVersionSelection = VERSION_SELECT_MAJORS;
            return;
        }

        // ow defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("inprocessversion"))
        {
            m_iVersionSelection = VERSION_SELECT_IN_PROCESS;
            return;
        }

        // ow defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("checkedoutversion"))
        {
            m_iVersionSelection = OwSearchTemplate.VERSION_SELECT_CHECKED_OUT;
            return;
        }

        // FileNet P8 defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("releasedversion"))
        {
            m_iVersionSelection = VERSION_SELECT_RELEASED;
            return;
        }

        // FileNet P8 defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("allversions"))
        {
            m_iVersionSelection = VERSION_SELECT_ALL;
            return;
        }

        // FileNet P8 defined
        if (strVersionSelectionAttribute_p.equalsIgnoreCase("currentversion"))
        {
            m_iVersionSelection = VERSION_SELECT_CURRENT;
            return;
        }

        m_iVersionSelection = VERSION_SELECT_RELEASED;
    }

    /** (overridable) resolve the given literal placeholder name to a property / criteria value
     * 
     * @param contextname_p String name of calling context, e.g. searchtemplate name
     * @param placeholdername_p String name of placeholder to retrieve value for
     * @return an {@link Object}
     * @throws Exception
     */
    protected Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception
    {
        return getContext().resolveLiteralPlaceholder(contextname_p, placeholdername_p);
    }

    /** resolve the given literal node to a property / criteria value
     * 
     * @param contextname_p
     * @param propClass_p
     * @param literal_p
     * @return an {@link Object}
     * @throws Exception
     */
    protected Object resolveLiteralValue(String contextname_p, OwFieldDefinition propClass_p, Node literal_p) throws Exception
    {
        if (literal_p == null)
        {
            return null;
        }

        // check if given literal is a placeholder
        String literal = literal_p.getNodeValue();

        if (null == literal)
        {
            return propClass_p.getValueFromNode(literal_p);
        }

        // Bug 1003: check for escaped parenthesis
        if (literal.startsWith("\\" + LITERAL_PLACEHOLDER_LEFT_DELIMITER))
        {
            return propClass_p.getValueFromString(literal.substring(1));
        }

        if (literal.startsWith(LITERAL_PLACEHOLDER_LEFT_DELIMITER) && literal.endsWith(LITERAL_PLACEHOLDER_RIGHT_DELIMITER))
        {
            // Bug 1003: check for doc id's like {AE901EAC-BF45-449A-9572-B09F0466066A}
            if (isDocId(literal))
            {
                return propClass_p.getValueFromNode(literal_p);
            }
            else
            {
                // === resolve placeholder
                return resolveLiteralPlaceholder(contextname_p, literal.substring(1, literal.length() - 1));
            }
        }
        else
        {
            return propClass_p.getValueFromNode(literal_p);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwSearchTemplate#saveSearch(java.lang.String)
     */
    public void saveSearch(String name_p) throws Exception
    {
        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
        String userId = network.getCredentials().getUserInfo().getUserID();
        OwSearchNode currentSearch = m_cashedSearch == null ? m_Search : m_cashedSearch;
        if (currentSearch != null && name_p != null)
        {
            SearchNodeXMLSerializer serializer = new SearchNodeXMLSerializer(currentSearch);
            String encodedSearchCriteria = serializer.serializeToString();
            OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_strName, userId, true, true);

            attrBag.setAttribute(name_p, encodedSearchCriteria);
            attrBag.save();

            if (LOG.isDebugEnabled())
            {
                LOG.debug("Encoded search template:\n" + encodedSearchCriteria + "\n");
            }
        }
    }

    /** scan the content based retrieval node
     */
    private void scanCBRNode(OwSearchNode search_p, Node cbrNode_p) throws Exception
    {
        // === iterate over the verity items, which define cbr criteria
        for (Node n = cbrNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equals("verityitem"))
            {
                // === found a verity item    
                // predefined value
                String strLiteral = "";
                // get parameters
                for (Node parameter = n.getFirstChild(); parameter != null; parameter = parameter.getNextSibling())
                {
                    if (parameter.getNodeName().equals("usertext"))
                    {
                        // === found literal
                        if (parameter.getFirstChild() != null)
                        {
                            strLiteral = parameter.getFirstChild().getNodeValue();
                        }
                    }
                }

                List zones = new ArrayList();
                // get zones
                getZones(n, zones);

                // create new value
                OwStandardContentBasedRetrivalObject cbrValue = new OwStandardContentBasedRetrivalObject(strLiteral, zones);

                // get operator
                String strOperator = OwXMLDOMUtil.getSafeStringAttributeValue(n, "groupaction", null);
                if (null == strOperator)
                {
                    String msg = "OwStandardSearchTemplate.scanCBRNode: CBR Operator not defined, operator = 'groupaction' in searchtemplate = " + getName();
                    LOG.error(msg);
                    throw new OwInvalidOperationException(OwString.localize2(m_local, "server.ecm.OwStandardSearchTemplate.invalidOperator", "Invalid operator '%1' in search template '%2'", "groupaction", getName()));
                }

                int iOP = convertOperator("cbr_" + strOperator);
                // get optional instruction
                String strInstruction = OwXMLDOMUtil.getSafeStringAttributeValue(n, ATTRIBUTE_INSTRUCTION, null);

                // add cbr field to search tree
                search_p.add(createSearchNode(cbrValue, iOP, getSearchAttributeFromNode(n, 0), OwStandardContentBasedRetrivalObject.CLASS_NAME, strInstruction, null));
            }

            if (n.getNodeName().equals("verityand"))
            {
                // === found a verity and operator 
                // recurse
                OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_CBR);
                search_p.add(newSearchNode);
                scanCBRNode(newSearchNode, n);
            }

            if (n.getNodeName().equals("verityor"))
            {
                // === found a verity or operator    
                // recurse
                OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_OR, OwSearchNode.NODE_TYPE_CBR);
                search_p.add(newSearchNode);
                scanCBRNode(newSearchNode, n);
            }
        }
    }

    /** scan the from node, look for class names to search for, add as property to the given search
     *
     * @param search_p OwSearchNode
     * @param fromNode_p XML node with object type 
     * @param classesNode_p XML node with subclasses
     *
     */
    private void scanClasses(OwSearchNode search_p, Node fromNode_p, Node classesNode_p) throws Exception
    {
        // === create a value for the objectclass field
        // using the form: <ObjectType>,[<ClassName>];<ObjectType>,[<ClassName>];...

        String strInstruction = null;

        OwStandardClassSelectObject from = OwStandardClassSelectObject.CLASS_FROM.newObject();

        List<OwXMLUtil> fromClassNodes = new OwStandardXMLUtil(fromNode_p).getSafeUtilList("class");
        for (OwXMLUtil fromClassNode : fromClassNodes)
        {
            String symName = fromClassNode.getSafeStringAttributeValue(ATTRIBUTE_SYMNAME, null);
            String objectType = fromClassNode.getSafeStringAttributeValue(ATTRIBUTE_OBJECTTYPE, null);

            int iObjectType = getObjectTypeFromClassName(objectType);
            from.addClass(iObjectType, symName, objectType, null, true);
        }

        // add from field to search tree
        search_p.add(createSearchNode(from, OwSearchOperator.CRIT_OP_EQUAL, OwSearchCriteria.ATTRIBUTE_HIDDEN, OwStandardClassSelectObject.FROM_NAME, strInstruction, null));

        OwStandardClassSelectObject subclasses = OwStandardClassSelectObject.CLASS_SUBCLASSES.newObject();

        OwStandardClassSelectObject classes = OwStandardClassSelectObject.CLASS_CLASS_NAME.newObject();

        int hiddenClassesFlag = OwSearchCriteria.ATTRIBUTE_HIDDEN;

        if (classesNode_p != null)
        {
            OwXMLUtil classesNodeWrapper = new OwStandardXMLUtil(classesNode_p);
            List subClasses = classesNodeWrapper.getSafeNodeList();

            // get instruction
            strInstruction = classesNodeWrapper.getSafeStringAttributeValue(ATTRIBUTE_INSTRUCTION, null);

            if (subClasses.size() > 0)
            {
                // === create class info from the subclasses node
                for (int i = 0; i < subClasses.size(); i++)
                {
                    Node subclass = (Node) subClasses.get(i);

                    // get editable flag
                    int hiddenClass = getSearchAttributeFromNode(subclass, OwSearchCriteria.ATTRIBUTE_HIDDEN);

                    // bitwise AND : enables the classes if at least one class is editable
                    hiddenClassesFlag = hiddenClassesFlag & hiddenClass;

                    // get the objecttype name
                    String strBaseClaseName = getAttributeByName(subclass, ATTRIBUTE_OBJECTTYPE).getNodeValue();

                    // get the classname
                    String strClassName = getAttributeByName(subclass, ATTRIBUTE_SYMNAME).getNodeValue();

                    // get includesubclasses
                    boolean includesubclasses = OwXMLDOMUtil.getSafeBooleanAttributeValue(subclass, ATTRIBUTE_INCLUDESUBCLASSES, false);
                    // get the objecttype
                    int iObjectType = getObjectTypeFromClassName(strBaseClaseName);
                    classes.addClass(iObjectType, strClassName, strBaseClaseName, null, includesubclasses);
                    subclasses.addClass(iObjectType, strClassName, strBaseClaseName, null, includesubclasses);
                }

                // get max value if defined
                String maxvalue = classesNodeWrapper.getSafeStringAttributeValue(ATTRIBUTE_MAX_VALUE, null);
                // get max value if defined
                String minvalue = classesNodeWrapper.getSafeStringAttributeValue(ATTRIBUTE_MIN_VALUE, null);

                // add objectclass field to search tree
                search_p.add(createSubclassNode(classes.getFieldDefinition(), OwSearchOperator.CRIT_OP_EQUAL, classes.getValue(), hiddenClassesFlag, OwStandardClassSelectObject.CLASS_NAME, strInstruction, null, minvalue, maxvalue, null, null));

                search_p.add(createSubclassNode(subclasses.getFieldDefinition(), OwSearchOperator.CRIT_OP_EQUAL, classes.getValue(), hiddenClassesFlag, OwStandardClassSelectObject.SUBCLASSES_NAME, strInstruction, null, minvalue, maxvalue, null, null));

                return;
            }
        }
        //default - no or 0 sublclasses

        // add subclasses field to search tree
        search_p.add(createSearchNode(subclasses, OwSearchOperator.CRIT_OP_EQUAL, OwSearchCriteria.ATTRIBUTE_HIDDEN, OwStandardClassSelectObject.SUBCLASSES_NAME, strInstruction, null));

        // === create class info from the class node
        // get the objecttype name
        Node nodeClass = new OwStandardXMLUtil(fromNode_p).getSubNode("class");

        if (nodeClass == null)
        {
            throw new OwConfigurationException("OwStandardSearchTemplate.scanClass: failed, no \"class\" subnode could be found");
        }

        String strBaseClassName = getAttributeByName(nodeClass, ATTRIBUTE_SYMNAME).getNodeValue();

        // get the objecttype
        int iObjectType = getObjectTypeFromClassName(strBaseClassName);
        classes.addClass(iObjectType, strBaseClassName, strBaseClassName, null, true);

        // add objectclass field to search tree
        search_p.add(createSearchNode(classes, OwSearchOperator.CRIT_OP_EQUAL, hiddenClassesFlag, OwStandardClassSelectObject.CLASS_NAME, strInstruction, null));
    }

    /**(overridable)
     * 
     * @param fieldDefinition_p
     * @param iOp_p
     * @param oInitialAndDefaultValue_p
     * @param iAttributes_p
     * @param strUniqueName_p
     * @param strInstruction_p
     * @param wildcarddefinitions_p
     * @param minValue_p
     * @param maxValue_p
     * @param defaultValue_p
     * @param sJavaClassName_p
     * @return search node for &lt;subclasses&gt; search template element
     * @throws Exception
     * @since 3.2.0.3
     */
    protected OwSearchNode createSubclassNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p, Object minValue_p,
            Object maxValue_p, Object defaultValue_p, String sJavaClassName_p) throws Exception
    {
        if (OwStandardClassSelectObject.CLASS_SUBCLASSES.getClassName().equals(fieldDefinition_p.getClassName()))
        {
            iAttributes_p = OwSearchCriteria.ATTRIBUTE_HIDDEN;
        }
        return createSearchNode(fieldDefinition_p, iOp_p, oInitialAndDefaultValue_p, iAttributes_p, strUniqueName_p, strInstruction_p, wildcarddefinitions_p, minValue_p, maxValue_p, defaultValue_p, sJavaClassName_p);
    }

    /** (overridable) scan search template for additional custom info
     * 
     * @param searchTemplateNode_p Node
     */
    protected void scanCustomInfo(Node searchTemplateNode_p)
    {
        // default does nothing, override in custom search template class
    }

    /** Scan the folders and ObjectStore node and 
     *  add resource criteria search nodes to the given search node 
     *  as defined by AWD 3.0.0.0 search concepts specification.
     *  The new nodes are based on {@link OwSearchPathField} , {@link OwSearchPath} and {@link OwSearchObjectStore} 
     *  object data. 
     *
     * @param search_p OwSearchNode to add to
     * @param foldersNode_p folder Node to traverse
     * @param objectstoresNode_p objectstore Node to traverse
     * @since 3.0.0.0
     */
    protected void scanResourceNodeEx(OwSearchNode search_p, Node foldersNode_p, Node objectstoresNode_p) throws Exception
    {

        Map osByIds = new HashMap();
        Map osByNames = new HashMap();

        final String pathNodeName = "searchPath";
        int mergeOperator = OwSearchOperator.MERGE_NONE;

        // === scan ObjectStores node
        // NOTE:    this is a FileNet P8 specialty, actually a folders node would be enough to describe the resources to search for,
        //          but for FileNet P8 compatibility we need to mind the ObjectStores in addition to the folders node
        //          we will also collect the meregeOperator (only one merge option will be used)
        if (objectstoresNode_p != null)
        {
            String strMergeOption = OwXMLDOMUtil.getSafeStringAttributeValue(objectstoresNode_p, "mergeoption", "");

            if (strMergeOption.equals("union"))
            {
                mergeOperator = OwSearchOperator.MERGE_UNION;
            }

            if (strMergeOption.equals("intersection"))
            {
                mergeOperator = OwSearchOperator.MERGE_INTERSECT;
            }

            for (Node n = objectstoresNode_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() != Node.ELEMENT_NODE || !n.getNodeName().equals("objectstore"))
                {
                    continue;
                }

                //retrieve the correct id and name
                String objectStoreId = OwXMLDOMUtil.getSafeStringAttributeValue(n, "id", null);
                String objectStoreName = OwXMLDOMUtil.getSafeStringAttributeValue(n, "name", null);

                //the OwSearchObjectStore constructor will throw an exception if both name and id are null
                OwSearchObjectStore objectStore = new OwSearchObjectStore(objectStoreId, objectStoreName);
                //map the new object store accordingly by name and id 
                if (objectStoreId != null)
                {
                    osByIds.put(objectStoreId, objectStore);
                }
                if (objectStoreName != null)
                {
                    osByNames.put(objectStoreName, objectStore);
                }
            }
        }

        //the pathDefinedStores will hold all ObjectStores referred through folder elements 
        List pathDefinedStores = new LinkedList();
        final String defaultInstruction = OwString.localize(m_local, "ecm.OwStandardSearchTemplate.resourcepathinstr", "File path with object store, .../* also browses subfolders.");
        if (foldersNode_p != null)
        {
            for (Node n = foldersNode_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() != Node.ELEMENT_NODE || !n.getNodeName().equals("folder"))
                {
                    continue;
                }

                int attributes = getSearchAttributeFromNode(n, OwSearchCriteria.ATTRIBUTE_HIDDEN);

                String instruction = OwXMLDOMUtil.getSafeStringAttributeValue(n, ATTRIBUTE_INSTRUCTION, null);
                if (null == instruction)
                {
                    instruction = defaultInstruction;
                }

                String objectStoreId = null;
                String objectStoreName = null;
                for (Node objectStoreNode = n.getFirstChild(); objectStoreNode != null; objectStoreNode = objectStoreNode.getNextSibling())
                {
                    if (n.getNodeType() != Node.ELEMENT_NODE || objectStoreNode.getNodeName().equals("objectstore"))
                    {
                        objectStoreId = OwXMLDOMUtil.getSafeStringAttributeValue(objectStoreNode, "id", null);
                        objectStoreName = OwXMLDOMUtil.getSafeStringAttributeValue(objectStoreNode, "name", null);
                        break;
                    }
                }

                //the OwSearchObjectStore constructor will throw an exception if both name and id are null
                OwSearchObjectStore objectStore = new OwSearchObjectStore(objectStoreId, objectStoreName);

                if (objectStoreId != null)
                {
                    //an id defined ObjectStore must be mapped or unified (they should have the same name) 
                    //with an already existing ObjectStore with the same id 
                    OwSearchObjectStore osById = (OwSearchObjectStore) osByIds.get(objectStoreId);
                    if (osById != null)
                    {
                        objectStore = osById.unify(objectStore);
                        //we will replace the already existing ObjectStore
                        osByIds.put(objectStoreId, objectStore);
                    }
                    else
                    {
                        osByIds.put(objectStoreId, objectStore);
                    }
                }

                //object store name might have been unified by id we must refresh the name 
                objectStoreName = objectStore.getName();

                if (objectStoreName != null)
                {
                    //an name defined ObjectStore must be mapped or unified (they should have the same id) 
                    //with an already existing ObjectStore with the same name 
                    OwSearchObjectStore osByName = (OwSearchObjectStore) osByNames.get(objectStoreName);
                    if (osByName != null)
                    {
                        objectStore = osByName.unify(objectStore);
                        // we will replace the already existing ObjectStore 
                        // both in the name and id map if necessary 
                        if (objectStore.getId() != null)
                        {
                            osByIds.put(objectStore.getId(), objectStore);
                        }

                        osByNames.put(objectStoreName, objectStore);
                    }
                    else
                    {
                        osByNames.put(objectStoreName, objectStore);
                    }
                }

                pathDefinedStores.add(objectStore);
                String pathName = OwXMLDOMUtil.getSafeStringAttributeValue(n, "pathname", null);
                String pathId = null;

                boolean searchSubfolders = OwXMLDOMUtil.getSafeBooleanAttributeValue(n, "searchsubfolders", false);

                OwSearchPath searchPath = new OwSearchPath(pathId, pathName, searchSubfolders, objectStore);
                OwSearchPathField field = new OwSearchPathField(searchPath);

                // add resource path field to search tree
                search_p.add(createSearchNode(field.getFieldDefinition(), mergeOperator, searchPath, attributes, pathNodeName, instruction, null));

            }
        }
        //all referred resource names (ObjectStore names) should be filled in 
        m_resourceNames = new LinkedList();
        for (Iterator i = pathDefinedStores.iterator(); i.hasNext();)
        {
            OwSearchObjectStore pathStore = (OwSearchObjectStore) i.next();
            String id = pathStore.getId();
            String name = pathStore.getName();
            if (name == null)
            {
                String msg = "All objectstores must have names ! Found objecstore with id=" + id + " that has no name!";
                LOG.error("OwStandardSearchTemplate.scanResourceNodeEx() : " + msg + ".The current OwStandardSearchTemplate implementation does not support nameless objectstores!");
                throw new OwInvalidOperationException(msg);
            }
            m_resourceNames.add(name);
            //we clear the maps so that they will only hold non folder referred ObjectStores
            if (id != null)
            {
                osByIds.remove(id);
            }
            osByNames.remove(name);

        }

        //this list will hold non folder referred 
        List objectStoreRootPaths = new LinkedList();
        objectStoreRootPaths.addAll(osByIds.values());
        //next we remove name based duplicates  of the id based ObjectStores 
        for (Iterator i = objectStoreRootPaths.iterator(); i.hasNext();)
        {
            OwSearchObjectStore store = (OwSearchObjectStore) i.next();
            String name = store.getName();
            if (name != null)
            {
                osByNames.remove(name);
            }
        }

        //the remaining name mapped objectstores are non folder referred name defined objecstores
        objectStoreRootPaths.addAll(osByNames.values());

        //we create objectstore reference paths if any
        for (Iterator i = objectStoreRootPaths.iterator(); i.hasNext();)
        {
            OwSearchObjectStore store = (OwSearchObjectStore) i.next();
            OwSearchPath osReference = new OwSearchPath(store);
            String name = store.getName();
            if (name == null)
            {
                String msg = "All objectstores must have names ! Found objecstore with id=" + store.getId() + " that has no name!";
                LOG.error("OwStandardSearchTemplate.scanResourceNodeEx() : " + msg + ".The current OwStandardSearchTemplate implementation does not support nameless objectstores!");
                throw new OwInvalidOperationException(msg);
            }
            m_resourceNames.add(name);

            OwSearchPathField field = new OwSearchPathField(osReference);
            search_p.add(createSearchNode(field.getFieldDefinition(), mergeOperator, osReference, OwSearchCriteria.ATTRIBUTE_HIDDEN, pathNodeName, defaultInstruction, null));
        }
    }

    /** recursively traverse the DOM nodes to create the search
     *  NOTE: The syntax of the DOM Node is compatible with FileNet P8 Search Designer
     *
     * @param searchDOMNode_p node to traverse further
     * @param search_p Search to be created
     */
    private void scanSearchNode(Node searchDOMNode_p, OwSearchNode search_p) throws Exception
    {
        for (Node n = searchDOMNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            // and combination node
            if (n.getNodeName().equalsIgnoreCase("and"))
            {
                // add combination node
                OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
                search_p.add(newSearchNode);
                // traverse sub DOM nodes
                scanSearchNode(n, newSearchNode);
            }
            // or combination node
            else if (n.getNodeName().equalsIgnoreCase("or"))
            {
                // add combination node
                OwSearchNode newSearchNode = createSearchNode(OwSearchNode.SEARCH_OP_OR, OwSearchNode.NODE_TYPE_PROPERTY);
                search_p.add(newSearchNode);
                // traverse of sub DOM nodes
                scanSearchNode(n, newSearchNode);
            }
            // criteria node
            else
            {
                // nodename acts as operator
                String strOperator = n.getNodeName();
                // get the int operator form string
                int iOP = convertOperator(strOperator);

                // get criteria subnode ATTRIBUTE_PROP for class and display name of property and the object value node
                OwXMLUtil criteriaNode = null; // ATTRIBUTE_PROP node
                Node defaultValueNode = null; // ATTRIBUTE_LITERAL node
                for (Node node = n.getFirstChild(); node != null; node = node.getNextSibling())
                {
                    if (n.getNodeType() != Node.ELEMENT_NODE)
                    {
                        continue;
                    }

                    if (node.getNodeName().equalsIgnoreCase(ELEMENT_WHEREPROP))
                    {
                        criteriaNode = new OwStandardXMLUtil(node);
                    }
                    else
                    {
                        if (node.getNodeName().equalsIgnoreCase(ELEMENT_LITERAL))
                        {
                            if (node.hasChildNodes())
                            {
                                defaultValueNode = node;
                            }
                        }
                    }
                }

                // === get attributes
                // get class attribute
                String strClassName = criteriaNode.getSafeStringAttributeValue(ATTRIBUTE_SYMNAME, null);

                // get unique name attribute
                String strUniqueName = criteriaNode.getSafeStringAttributeValue(ATTRIBUTE_UNIQUENAME, strClassName);

                // get allowwildcardflag
                boolean fAllowWildCard = criteriaNode.getSafeBooleanAttributeValue(ATTRIBUTE_ALLOWWILDCARD, false);

                // get instruction
                String strInstruction = criteriaNode.getSafeStringAttributeValue(ATTRIBUTE_INSTRUCTION, null);

                // get attributes (P8 Designer compatible)
                int iAttributes = getSearchAttributeFromNode(criteriaNode.getNode(), 0);

                // get override data type, i.e. force the given java class name to be used
                String sOverrideDatatype = criteriaNode.getSafeStringAttributeValue(ATTRIBUTE_OVERRIDE_DATATYPE, null);

                // === try to get enums from property class description if available for this criteria
                OwFieldDefinition PropClass = getPropertyClassInternal(strClassName, criteriaNode);
                if (PropClass != null)
                {
                    // === get the value of the property if set
                    Object initialanddefaultvalue = getInitialAndDefaultValueFromNode(defaultValueNode, PropClass, iOP);

                    // do we support wildcard characters for this field ?
                    if (fAllowWildCard)
                    {
                        // === set attribute for criteria
                        iAttributes += OwSearchCriteria.ATTRIBUTE_ALLOWWILDCARD;
                    }

                    // === add criteria search node
                    if (null == sOverrideDatatype)
                    {
                        // === create standard node
                        search_p.add(createSearchNode(PropClass, iOP, initialanddefaultvalue, iAttributes, strUniqueName, strInstruction, m_fieldDefinitionProvider.getWildCardDefinitions(PropClass.getClassName(), null, iOP)));
                    }
                    else
                    {
                        // === a override data type was given use it as the javaclassname in the decorator pattern of OwSearchNode
                        search_p.add(createSearchNode(PropClass, iOP, initialanddefaultvalue, iAttributes, strUniqueName, strInstruction, m_fieldDefinitionProvider.getWildCardDefinitions(PropClass.getClassName(), null, iOP), PropClass.getMinValue(),
                                PropClass.getMaxValue(), PropClass.getDefaultValue(), sOverrideDatatype));
                    }
                }
                else
                {
                    // === criteria not defined as property
                    // ...do special treatment of special criteria here
                    LOG.error("OwStandardSearchTemplate.scanSearchNode: Search Template parser error, undefined criteria property = " + strClassName);
                    throw new OwInvalidOperationException(OwString.localize1(m_local, "server.ecm.OwStandardSearchTemplate.UndefinedCriteriaProperty", "Search Template parser error, undefined criteria property: %1", strClassName));
                }
            }
        }
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getField(sName_p).setValue(value_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwSearchTemplate#setSavedSearch(java.lang.String)
     */
    public void setSavedSearch(String name_p) throws Exception
    {
        this.m_savedSearch = name_p;
        if (this.m_savedSearch != null)
        {
            OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
            String userId = network.getCredentials().getUserInfo().getUserID();

            OwSearchNode currentSearch = getSearch(true);
            if (currentSearch != null)
            {
                OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_strName, userId, true, false);
                String attributeValue = (String) attrBag.getAttribute(name_p);
                SearchNodeXMLDeserializer deserializer = new SearchNodeXMLDeserializer(attributeValue);
                deserializer.deserialize(currentSearch);
            }
        }
    }

    public static class OwFieldDefinitionEnumExchangeWrapper implements OwFieldDefinition
    {

        /** the new OwEnumCollection that is returned on getEnums() instead of the OwEnumCollection of the wrapped OwFieldDefinition */
        protected OwEnumCollection m_exchangedEnum;

        /** the wrapped OwFieldDefinition */
        protected OwFieldDefinition m_fieldDefinition;

        public OwFieldDefinitionEnumExchangeWrapper(OwFieldDefinition fieldDefinition_p, OwEnumCollection exchangedEnum_p)
        {
            m_fieldDefinition = fieldDefinition_p;
            m_exchangedEnum = exchangedEnum_p;
        }

        public String getClassName()
        {
            return m_fieldDefinition.getClassName();
        }

        public List getComplexChildClasses() throws Exception
        {
            return m_fieldDefinition.getComplexChildClasses();
        }

        public Object getDefaultValue() throws Exception
        {
            return m_fieldDefinition.getDefaultValue();
        }

        public String getDescription(Locale locale_p)
        {
            return m_fieldDefinition.getDescription(locale_p);
        }

        public String getDisplayName(Locale locale_p)
        {
            return m_fieldDefinition.getDisplayName(locale_p);
        }

        public OwEnumCollection getEnums() throws Exception
        {
            return m_exchangedEnum;
        }

        public OwFormat getFormat()
        {
            return m_fieldDefinition.getFormat();
        }

        public String getJavaClassName()
        {
            return m_fieldDefinition.getJavaClassName();
        }

        public Object getMaxValue() throws Exception
        {
            return m_fieldDefinition.getMaxValue();
        }

        public Object getMinValue() throws Exception
        {
            return m_fieldDefinition.getMinValue();
        }

        public Object getNativeType() throws Exception
        {
            return m_fieldDefinition.getNativeType();
        }

        public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
        {
            return m_fieldDefinition.getNodeFromValue(value_p, doc_p);
        }

        public Collection getOperators() throws Exception
        {
            return m_fieldDefinition.getOperators();
        }

        public Object getValueFromNode(Node node_p) throws Exception
        {
            return m_fieldDefinition.getValueFromNode(node_p);
        }

        public Object getValueFromString(String text_p) throws Exception
        {
            return m_fieldDefinition.getValueFromString(text_p);
        }

        public boolean isArray() throws Exception
        {
            return m_fieldDefinition.isArray();
        }

        public boolean isComplex()
        {
            return m_fieldDefinition.isComplex();
        }

        public boolean isEnum() throws Exception
        {
            return true; // m_fieldDefinition.isEnum();
        }

        public boolean isRequired() throws Exception
        {
            return m_fieldDefinition.isRequired();
        }
    }

    /** class description for on the fly created search fields
     */
    public static class OwOnTheFlySearchFieldDescription extends OwStandardPropertyClass
    {
        public OwOnTheFlySearchFieldDescription(String strClassName_p, OwString displayName_p, String strJavaClassName_p)
        {

            m_strClassName = strClassName_p;
            m_DisplayName = displayName_p;
            m_strJavaClassName = strJavaClassName_p;

            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = false;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = false;
            m_fName = false;

            m_fRequired = false;
            m_DMSType = null;

        }
    }

    /**
     * Utility class for criteria deserialize.
     * @since 3.0.0.0 
     */
    public class SearchNodeXMLDeserializer
    {
        /**the document*/
        private Document m_document;

        /**constructor*/
        public SearchNodeXMLDeserializer(String xmlAsString_p) throws Exception
        {
            try
            {
                this.m_document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlAsString_p)));
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Cannot parse serialized xml: \n" + xmlAsString_p, e);
                }
                throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.cannotParseXml", "Cannot parse the serialized xml."), e);
            }
        }

        /**
         * Decode criteria from XML.
         * @param element_p - the {@link Element} object
         * @param criteria_p - the {@link OwSearchCriteria} object 
         * @throws Exception - thrown when the criteria cannot be deserialize.
         */
        private void decodeCriteria(Element element_p, OwSearchCriteria criteria_p) throws Exception
        {
            NodeList valueList = element_p.getElementsByTagName(VALUE_ELEMENT_NAME);
            if (valueList != null)
            {
                String criteriaClassName = criteria_p.getJavaClassName();
                Class criteriaClass = Class.forName(criteriaClassName);

                if (criteria_p.isArray())
                {
                    int size = valueList.getLength();
                    Object array = Array.newInstance(criteriaClass, size);
                    for (int i = 0; i < valueList.getLength(); i++)
                    {
                        Node valueNode = valueList.item(i);
                        String valueNodeAsString = null;
                        if (valueNode.hasChildNodes())
                        {
                            valueNodeAsString = valueNode.getFirstChild().getNodeValue();
                        }
                        Object value = decodeValue(criteriaClass, criteria_p, valueList, valueNodeAsString);
                        ((Object[]) array)[i] = value;
                    }
                    criteria_p.setValue(array);
                }
                else
                {
                    Node valueNode = valueList.item(0);
                    String valueNodeAsString = null;
                    if (valueNode.hasChildNodes())
                    {
                        valueNodeAsString = valueNode.getFirstChild().getNodeValue();
                    }

                    Object value = decodeValue(criteriaClass, criteria_p, valueList, valueNodeAsString);
                    criteria_p.setValue(value);
                }
            }
        }

        /**
         * Decode second criteria element,
         * @param element_p - the {@link Element} object
         * @param criteria_p - the {@link OwSearchCriteria} object 
         * @throws Exception - thrown when the criteria cannot be deserialize.
         */
        private void decodeSecondCriteria(Element element_p, OwSearchCriteria criteria_p) throws Exception
        {
            OwSearchCriteria secondCriteria = criteria_p.getSecondRangeCriteria();
            if (secondCriteria != null)
            {
                if (!(secondCriteria.isHidden() || secondCriteria.isReadonly()))
                {
                    //read values for second criteria
                    NodeList secondCriteriaElements = element_p.getElementsByTagName(SECOND_CRITERION_ELEMENT_NAME);
                    if (secondCriteriaElements.getLength() != 1)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwStandardSearchTemplate$SeachNodeXMLDeserializer.decodeSecondCriteria: Problem reading the second criteria elements");
                        }
                        throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.invalidXmlOrSearchNode",
                                "The serialized XML structure doesn't match the search node structure. It's possible to have an old xml."));
                    }
                    Element secondCriteriaElement = (Element) secondCriteriaElements.item(0);
                    decodeCriteria(secondCriteriaElement, secondCriteria);
                }
            }
        }

        /**
         * Translate the string value into a specific criteria value object.
         * @param criteriaClass_p - the {@link Class} associated with criteria object
         * @param criteria_p - the {@link OwSearchCriteria} object
         * @param valueList_p - the {@link NodeList} object.
         * @param valueNodeAsString_p - node value
         * @return the {@link Object} value transformed from string.
         * @throws Exception thrown when something went wrong.
         */
        private Object decodeValue(Class criteriaClass_p, OwSearchCriteria criteria_p, NodeList valueList_p, String valueNodeAsString_p) throws Exception
        {
            Object result = null;
            if (valueNodeAsString_p != null)
            {
                if (OwObject.class.isAssignableFrom(criteriaClass_p))
                {
                    OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
                    result = network.getObjectFromDMSID(valueNodeAsString_p, true);
                }
                else if (OwClass.class.isAssignableFrom(criteriaClass_p))
                {
                    //getObjectType() + "|" + getClassName() + "|" + getBaseClassName() + "|" + getResourceName() + "|" + isEnabled() + "|" +isIncludeSubclasses();
                    StringTokenizer tokenizer = new StringTokenizer(valueNodeAsString_p, "|");
                    if (tokenizer.countTokens() != 6)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwStandardSearchTemplate$SeachNodeXMLDeserializer.decodeValue: Cannot deserialize OWClass criteria!");
                        }
                        throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.cannotDeserialiseOwClass", "Cannot deserialize OWClass criteria."));
                    }
                    int objectType = -1;
                    String className = "", baseClassName = "", resourceName = "";
                    boolean enabled = false, includeSubclasses = false;

                    for (int i = 0; i < OW_CLASS_STRUCTURE.length; i++)
                    {
                        String token = tokenizer.nextToken();

                        switch (OW_CLASS_STRUCTURE[i])
                        {
                            case OW_CLASS_TYPE:
                                try
                                {
                                    objectType = Integer.parseInt(token);
                                }
                                catch (Exception e)
                                {
                                    if (LOG.isDebugEnabled())
                                    {
                                        LOG.debug("OwStandardSearchTemplate$SeachNodeXMLDeserializer.decodeValue: Cannot deserialize OWClass criteria.", e);
                                    }
                                    throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.cannotDeserialiseOwClass", "Cannot deserialize OWClass criteria."), e);

                                }
                                break;
                            case OW_CLASS_CLASSNAME:
                                className = token;
                                break;
                            case OW_CLASS_BASECLASSNAME:
                                baseClassName = token;
                                break;
                            case OW_CLASS_RESOURCENAME:
                                resourceName = token;
                                break;
                            case OW_CLASS_ENABLED:
                                enabled = Boolean.valueOf(token).booleanValue();
                                break;
                            case OW_CLASS_INCLUDE_SUBCLASSES:
                                includeSubclasses = Boolean.valueOf(token).booleanValue();
                                break;
                            default:
                                break;
                        }
                    }
                    result = new OwClass(objectType, className, baseClassName, resourceName, enabled, includeSubclasses);
                }
                else if (criteria_p.isDateType())
                {
                    SimpleDateFormat df = new SimpleDateFormat(OwStandardPropertyClass.EXACT_DATE_TEXT_FORMAT);
                    Date theDate = df.parse(valueNodeAsString_p);
                    long timestamp = theDate.getTime();
                    if (Date.class.isAssignableFrom(criteriaClass_p))
                    {
                        try
                        {
                            java.lang.reflect.Constructor constr = criteriaClass_p.getConstructor(new Class[] { Long.TYPE });
                            result = constr.newInstance(new Object[] { Long.valueOf(timestamp) });
                        }
                        catch (Exception e)
                        {
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("Cannot create search criteria value from string: " + valueNodeAsString_p, e);
                            }
                            throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.cannotCreateValue", "Cannot create the search criteria value."), e);
                        }
                    }
                }
                else if (Boolean.class.getName().equals(criteriaClass_p.getName()))
                {
                    result = Boolean.valueOf(valueNodeAsString_p);
                }
                else
                {
                    try
                    {
                        java.lang.reflect.Constructor constr = criteriaClass_p.getConstructor(new Class[] { java.lang.String.class });

                        result = constr.newInstance(new Object[] { valueNodeAsString_p });
                    }
                    catch (Exception e)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Cannot create search criteria value from string: " + valueNodeAsString_p, e);
                        }
                        throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.cannotCreateValue", "Cannot create the search criteria value."), e);

                    }
                }
            }
            return result;
        }

        /**
         * Extracts the relevant information from given @{link {@link Element} object and fills the given search object.  
         * @param element_p - the {@link Element} object
         * @param searchNode_p - the @{link {@link OwSearchNode} object
         * @throws Exception - thrown when something went wrong.
         */
        private void deserialize(Element element_p, OwSearchNode searchNode_p) throws Exception
        {
            if (searchNode_p.isCriteriaNode())
            {
                OwSearchCriteria criteria = searchNode_p.getCriteria();
                if (!(criteria.isReadonly() || criteria.isHidden()))
                {
                    decodeCriteria(element_p, criteria);
                    if (criteria.isCriteriaOperatorRange())
                    {
                        decodeSecondCriteria(element_p, criteria);
                    }
                }
                else
                {
                    if (criteria.isCriteriaOperatorRange())
                    {
                        decodeSecondCriteria(element_p, criteria);
                    }
                }
            }
            else
            {
                NodeList xmlNodes = element_p.getChildNodes();
                if (xmlNodes.getLength() > 0) //hidden and read-only criteria are not saved
                {
                    List searchNodes = searchNode_p.getChilds();
                    if (xmlNodes.getLength() != searchNodes.size())
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwStandardSearchTemplate$SeachNodeXMLDeserializer.deserialize: The serialized XML structure doesn't match the search node structure. It's possible to have an old xml.");
                        }
                        throw new OwInvalidOperationException(getContext().localize("server.ecm.OwStandardSearchTemplate.serialization.invalidXmlOrSearchNode",
                                "The serialized XML structure doesn't match the search node structure. It's possible to have an old xml."));
                    }
                    for (int i = 0; i < searchNodes.size(); i++)
                    {
                        OwSearchNode searchNode = (OwSearchNode) searchNodes.get(i);
                        deserialize((Element) xmlNodes.item(i), searchNode);
                    }
                }
            }

        }

        /**
         * Fills the given {@link OwSearchNode} with the criteria corresponding values from XML.
         * @param searchNode_p - the {@link OwSearchNode} object. 
         * @throws Exception - thrown when deserialize went wrong.
         */
        public void deserialize(OwSearchNode searchNode_p) throws Exception
        {
            Element rootElement = m_document.getDocumentElement();
            deserialize((Element) rootElement.getFirstChild(), searchNode_p);
        }
    }

    /**
     * Serialize a search node in a XML structure.
     * @since 3.0.0.0
     */
    public class SearchNodeXMLSerializer
    {
        /** DOM document*/
        private Document m_document;
        /** current search node*/
        private OwSearchNode m_searchNode;

        /**
         * Constructor
         * @param searchNode_p - search node.
         */
        public SearchNodeXMLSerializer(OwSearchNode searchNode_p)
        {
            this.m_searchNode = searchNode_p;
        }

        /**
         * Append a criteria element, or a criteria group element, 
         * depending of the type of the given node parameter.
         * @param element_p - {@link Element} object.
         * @param node_p - {@link OwSearchNode} object.
         * @throws Exception - thrown when the node cannot be serialized.
         */
        private void appendCriterion(Element element_p, OwSearchNode node_p) throws Exception
        {
            if (node_p.isCriteriaNode())
            {
                OwSearchCriteria criteria = node_p.getCriteria();
                Element theNewElement = m_document.createElement(CRITERION_ELEMENT_NAME);
                element_p.appendChild(theNewElement);
                theNewElement.setAttribute(NAME_ATTR_NAME, criteria.getUniqueName());
                if (!(criteria.isHidden() || criteria.isReadonly()))
                {
                    encodeValue(theNewElement, criteria);
                    if (criteria.isCriteriaOperatorRange())
                    {
                        encodeSecondRangeCriteria(criteria, theNewElement);
                    }
                }
                else
                {
                    if (criteria.isCriteriaOperatorRange())
                    {
                        encodeSecondRangeCriteria(criteria, theNewElement);
                    }
                }
            }
            else
            {
                Element criteriaGroup = m_document.createElement(CRITERIA_GROUP_ELEMENT_NAME);
                element_p.appendChild(criteriaGroup);
                Iterator it = node_p.getChilds().iterator();
                while (it.hasNext())
                {
                    appendCriterion(criteriaGroup, (OwSearchNode) it.next());
                }
            }
        }

        /**
         * Special method for encoding compound criteria.
         * @param criteria_p - the compound criteria
         * @param theNewElement_p - the {@link Element} object
         * @throws Exception - thrown when the encoding fails.
         */
        private void encodeSecondRangeCriteria(OwSearchCriteria criteria_p, Element theNewElement_p) throws Exception
        {
            OwSearchCriteria secondRangeCriteria = criteria_p.getSecondRangeCriteria();
            if (secondRangeCriteria != null)
            {
                Element secondCriterionElement = m_document.createElement(SECOND_CRITERION_ELEMENT_NAME);
                theNewElement_p.appendChild(secondCriterionElement);
                if (!(criteria_p.isHidden() || criteria_p.isReadonly()))
                {
                    encodeValue(secondCriterionElement, secondRangeCriteria);
                }
            }
        }

        /**
         * Convert the criteria value into a string.
         * @param criteria_p - the {@link OwSearchCriteria} object
         * @param value_p - the value of criteria.
         * @return a {@link String} object, representing the encoded value.
         * @throws Exception - thrown when encoding fail.
         */
        private String encodeSimpleCriteriaValue(OwSearchCriteria criteria_p, Object value_p) throws Exception
        {
            StringBuilder encodedValue = new StringBuilder();
            if (value_p != null)
            {
                try
                {
                    Class javaClass = Class.forName(criteria_p.getJavaClassName());
                    if (OwObject.class.isAssignableFrom(javaClass))
                    {
                        OwObject objectValue = (OwObject) value_p;
                        encodedValue.append(objectValue.getDMSID());
                    }
                    else if (OwClass.class.isAssignableFrom(javaClass))
                    {
                        OwClass classValue = (OwClass) value_p;
                        encodedValue.append(classValue.getObjectType()).append("|").append(classValue.getClassName()).append("|").append(classValue.getBaseClassName()).append("|").append(classValue.getResourceName()).append("|")
                                .append(classValue.isEnabled()).append("|").append(classValue.isIncludeSubclasses());
                    }
                    else if (criteria_p.isDateType())
                    {
                        Date dateValue = (Date) value_p;
                        SimpleDateFormat formatter = new SimpleDateFormat(OwStandardPropertyClass.EXACT_DATE_TEXT_FORMAT);
                        encodedValue.append(formatter.format(dateValue));
                    }
                    else
                    {
                        encodedValue.append(value_p);
                    }
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.error("Cannot find the corresponding JavaClass for this criteria " + criteria_p.getUniqueName(), e);
                    }
                    throw new OwObjectNotFoundException(getContext().localize1("", "Cannot find the corresponding JavaClass for this criteria %1", criteria_p.getUniqueName()));
                }

            }
            return encodedValue.toString();
        }

        /**
         * Encode the criteria value. If criteria type is array, multiple elements are created. 
         * @param criteriaElement_p - the {@link Element} object.
         * @param criteria_p - the {@link OwSearchCriteria} object.
         * @throws Exception - thrown when operation fail.
         */
        private void encodeValue(Element criteriaElement_p, OwSearchCriteria criteria_p) throws Exception
        {
            Object value = criteria_p.getValue();
            if (criteria_p.isArray())
            {
                Object[] values = (Object[]) value;
                for (int i = 0; i < values.length; i++)
                {
                    Element valueElement = m_document.createElement(VALUE_ELEMENT_NAME);
                    Text textNode = m_document.createTextNode(encodeSimpleCriteriaValue(criteria_p, values[i]));
                    valueElement.appendChild(textNode);
                    criteriaElement_p.appendChild(valueElement);
                }
            }
            else
            {
                Element valueElement = m_document.createElement(VALUE_ELEMENT_NAME);
                Text textNode = m_document.createTextNode(encodeSimpleCriteriaValue(criteria_p, value));
                valueElement.appendChild(textNode);
                criteriaElement_p.appendChild(valueElement);
            }
        }

        /**
         * Serialize the criteria from the current search node into a {@link Document} object
         * @return a @{link {@link Document} object.
         * @throws Exception - thrown when the serialization fails.
         */
        public Document serialize() throws Exception
        {
            m_document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = m_document.createElement("criteria");
            m_document.appendChild(root);
            appendCriterion(root, m_searchNode);
            return m_document;
        }

        /**
         * Serialize the criteria from the current search node into a {@link String} object
         * @return a {@link String} object (XML structure)
         * @throws Exception - thrown when serialization fails.
         */
        public String serializeToString() throws Exception
        {
            Document doc = serialize();
            return OwXMLDOMUtil.toString(doc.getDocumentElement());
        }
    }

    /**
     *<p>
     * SQL operators singleton.
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
    public static class SqlOperatorSingleton
    {
        Map m_OperatorMap = new HashMap();

        public SqlOperatorSingleton()
        {
            // init operator map according to FileNet P8 designer search template DOM Documents
            m_OperatorMap.put("eq", Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
            m_OperatorMap.put("noteq", Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL));
            m_OperatorMap.put("neq", Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL));
            m_OperatorMap.put("like", Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE));
            m_OperatorMap.put("notlike", Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_LIKE));
            m_OperatorMap.put("gt", Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
            m_OperatorMap.put("lt", Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
            m_OperatorMap.put("gte", Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
            m_OperatorMap.put("lte", Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
            m_OperatorMap.put("isnotnull", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NOT_NULL));
            m_OperatorMap.put("isnull", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NULL));
            m_OperatorMap.put("isin", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_IN));
            m_OperatorMap.put("isnotin", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NOT_IN));
            m_OperatorMap.put("in", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_ARRAY_IN));
            m_OperatorMap.put("notin", Integer.valueOf(OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN));

            m_OperatorMap.put("between", Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
            m_OperatorMap.put("notbetween", Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_BETWEEN));

            // cbr operators
            m_OperatorMap.put("cbr_none", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_NONE));
            m_OperatorMap.put("cbr_all", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_ALL));
            m_OperatorMap.put("cbr_any", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_ANY));
            m_OperatorMap.put("cbr_in", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_IN));
            m_OperatorMap.put("cbr_zone", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_ZONE));
            m_OperatorMap.put("cbr_near", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_NEAR));
            m_OperatorMap.put("cbr_paragraph", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_PARAGRAPH));
            m_OperatorMap.put("cbr_sentence", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_SENTENCE));
            m_OperatorMap.put("cbr_vql", Integer.valueOf(OwSearchOperator.CRIT_OP_CBR_VQL));
        }
    }
}
