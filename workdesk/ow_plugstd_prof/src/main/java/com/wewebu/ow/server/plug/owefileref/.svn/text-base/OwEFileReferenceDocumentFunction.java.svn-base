package com.wewebu.ow.server.plug.owefileref;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.wewebu.expression.language.OwExprBooleanValue;
import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExpressionType;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprType;
import com.wewebu.expression.language.OwExprValue;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;
import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMessageBox;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwObjectScope;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Document function for opening associated eFile for a given Object.
 * Opens an eFile, which is directly associated with the initial source object, such as a task object.
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
public class OwEFileReferenceDocumentFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEFileReferenceDocumentFunction.class);

    private static final String TEMPLATENAME_CONF_ATT_NAME = "templatename";
    private static final String SUBPATH_CONF_ATT_NAME = "subpath";
    private static final String OBJECTPROPERTY_CONF_ATT_NAME = "objectproperty";
    private static final String EFILEPROPERTY_CONF_ATT_NAME = "efileproperty";
    private static final String OBJECTCLASSES_CONF_TAG_NAME = "objectclasses";
    private static final String PROPERTY_MAPPING_CONF_TAG_NAME = "PropertyMapping";
    private static final String NAME_CONF_TAG_NAME = "name";
    private static final String EFILE_CONF_TAG_NAME = "EFile";
    private static final String EFILES_CONF_TAG_NAME = "EFiles";
    private static final String SEARCH_CONF_TAG_NAME = "Search";
    private static final String REFINEMENT_CONF_TAG = "Refinement";
    private static final String EXPRESSION_CONF_TAG = "expression";

    /** How many object should be displayed in the multiple-references message box */
    public int TOTAL_ELEMENTS_DISPLAY = 10;

    /**Configuration  &lt;Search&gt; element bean.*/
    private class OwEFileSearchConfiguration
    {
        private String searchTemplateName;
        private Map<String, String> propertyMappings = new HashMap<String, String>();

        public OwEFileSearchConfiguration(String searchTemplateName_p)
        {
            super();
            this.searchTemplateName = searchTemplateName_p;
        }

        public String getSearchTemplateName()
        {
            return this.searchTemplateName;
        }

        public void addPropertyMapping(String eFileProperty_p, String objectProperty_p)
        {
            this.propertyMappings.put(eFileProperty_p, objectProperty_p);
        }

        public Map<String, String> getPropertyMappings()
        {
            return this.propertyMappings;
        }
    }

    /**{@link OwEFileReferenceDocumentFunction}'s configuration  &lt;Refinement&gt; element bean.*/
    private class OwEFileRefinementConfiguration
    {
        private String subPath;
        private List<String> objectClassNames = new LinkedList<String>();
        private OwExprExpression expression;

        public OwEFileRefinementConfiguration(String subPath_p, List<String> objectClassNames_p, OwExprExpression expression_p)
        {
            super();
            this.subPath = subPath_p;
            this.objectClassNames.addAll(objectClassNames_p);
            this.expression = expression_p;
        }

        public String getSubPath()
        {
            return this.subPath;
        }

        public List<String> getObjectClassNames()
        {
            return this.objectClassNames;
        }

        /**
         * 
         * @param object_p {@link OwObject} to check the expression against
         * @return <code>true</code> if the given OwObject matches the refinement expression <br>
         *         <code>false</code> otherwise 
         * @throws OwInvalidOperationException
         */
        public boolean matches(OwObject object_p) throws OwInvalidOperationException
        {
            OwObjectScope objectScope = new OwObjectScope("object", object_p);
            try
            {
                OwExprExternalScope[] priorityScopes = new OwExprExternalScope[] { objectScope };
                if (this.expression.symbolsVisibleInScopes(priorityScopes))
                {
                    OwExprValue value = this.expression.evaluate(priorityScopes);
                    return OwExprBooleanValue.TRUE.equals(value);
                }
                else
                {
                    return false;
                }
            }
            catch (OwExprEvaluationException e)
            {
                LOG.error("OwEFileRefinementConfiguration.matches(): could not evaluate refinement expression - " + this.expression.toString(), e);
                throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);

            }

        }

    }

    /**{@link OwEFileReferenceDocumentFunction}'s configuration  &lt;Refinement&gt; element bean.*/
    private class OwEFileConfiguration
    {
        private List<String> objectClassNames = new LinkedList<String>();
        private List<OwEFileSearchConfiguration> searchConfigurations = new LinkedList<OwEFileSearchConfiguration>();
        private Map<String, List<OwEFileRefinementConfiguration>> refinemementConfigurations = new HashMap<String, List<OwEFileRefinementConfiguration>>();

        public OwEFileConfiguration(List<String> objectClassNames_p)
        {
            super();
            this.objectClassNames.addAll(objectClassNames_p);
        }

        public List<OwEFileSearchConfiguration> getSearchConfigurations()
        {
            return this.searchConfigurations;
        }

        public List<String> getObjectClassNames()
        {
            return this.objectClassNames;
        }

        public String getObjectClassNameString()
        {
            return this.objectClassNames.toString();
        }

        public void add(OwEFileSearchConfiguration searchConfiguration_p)
        {
            this.searchConfigurations.add(searchConfiguration_p);
        }

        /**
         * 
         * @param className_p
         * @return a {@link List} of {@link OwEFileReferenceDocumentFunction.OwEFileRefinementConfiguration}s for the given class name
         */
        public synchronized List<OwEFileRefinementConfiguration> getRefinements(String className_p)
        {
            List<OwEFileRefinementConfiguration> classRefinments = this.refinemementConfigurations.get(className_p);
            if (classRefinments == null)
            {
                classRefinments = new LinkedList<OwEFileRefinementConfiguration>();
                this.refinemementConfigurations.put(className_p, classRefinments);
            }
            return classRefinments;
        }

        public synchronized void add(OwEFileRefinementConfiguration refinementConfiguration_p)
        {
            List<String> objectClasses = refinementConfiguration_p.getObjectClassNames();
            for (String objectClass : objectClasses)
            {
                List<OwEFileRefinementConfiguration> refinements = getRefinements(objectClass);
                refinements.add(refinementConfiguration_p);
            }
        }

    }

    /**{@link Map} of String class names mapped to {@link OwEFileConfiguration}s */
    private Map<String, OwEFileConfiguration> eFileConfigurations;

    public OwEFileReferenceDocumentFunction()
    {
        super();
        eFileConfigurations = new HashMap<String, OwEFileConfiguration>();
    }

    private List<String> createConfigurationObjectClasses(OwXMLUtil objectClassesUtil_p) throws OwConfigurationException
    {
        List<?> nameUtilList = objectClassesUtil_p.getSafeUtilList(NAME_CONF_TAG_NAME);
        List<String> objectClasseNames = new LinkedList<String>();
        for (Iterator<?> j = nameUtilList.iterator(); j.hasNext();)
        {
            OwXMLUtil nameUtil = (OwXMLUtil) j.next();
            String objectClassName = nameUtil.getSafeTextValue("");
            if (objectClassName.length() == 0)
            {
                LOG.error("OwEFileReferenceDocumentFunction.createConfigurationObjectClasses():Invalid configuration - object class  name is missing in EFile configuration ");
                throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
            }
            objectClasseNames.add(objectClassName);
        }

        return objectClasseNames;
    }

    /**
     * 
     * @param objectClasseNames_p
     * @param refinementSubpath_p
     * @return a String error message part used for locating an efile/refinement definition in the configuration
     */
    private String createRefinementErrorLocation(List objectClasseNames_p, String refinementSubpath_p)
    {
        return "refinement with subpath=" + refinementSubpath_p + " of efile for target object classes " + objectClasseNames_p + " located in document function with id=" + getPluginID();
    }

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        OwXMLUtil configNode = getConfigNode();
        OwXMLUtil eFiles = configNode.getSubUtil(EFILES_CONF_TAG_NAME);
        List<?> eFileUtilList = eFiles.getSafeUtilList(EFILE_CONF_TAG_NAME);
        for (Iterator<?> i = eFileUtilList.iterator(); i.hasNext();)
        {
            OwXMLUtil eFileUtil = (OwXMLUtil) i.next();
            OwXMLUtil objectClassesUtil = eFileUtil.getSubUtil(OBJECTCLASSES_CONF_TAG_NAME);
            List<String> objectClasseNames = createConfigurationObjectClasses(objectClassesUtil);

            OwEFileConfiguration eFileConfiguration = new OwEFileConfiguration(objectClasseNames);
            List<?> searchUtilList = eFileUtil.getSafeUtilList(SEARCH_CONF_TAG_NAME);
            for (Iterator<?> j = searchUtilList.iterator(); j.hasNext();)
            {
                OwXMLUtil searchUtil = (OwXMLUtil) j.next();

                String templateName = searchUtil.getSafeStringAttributeValue(TEMPLATENAME_CONF_ATT_NAME, "");
                if (templateName.length() == 0)
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - search template name is missing in EFile for object class " + eFileConfiguration.getObjectClassNameString() + " located in document function with id="
                            + getPluginID());
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                }

                OwEFileSearchConfiguration searchConfiguration = new OwEFileSearchConfiguration(templateName);

                List<?> mappingUtils = searchUtil.getSafeUtilList(PROPERTY_MAPPING_CONF_TAG_NAME);
                for (Iterator<?> k = mappingUtils.iterator(); k.hasNext();)
                {
                    OwXMLUtil mappingUtil = (OwXMLUtil) k.next();
                    String eFileProperty = mappingUtil.getSafeStringAttributeValue(EFILEPROPERTY_CONF_ATT_NAME, "");
                    String objectProperty = mappingUtil.getSafeStringAttributeValue(OBJECTPROPERTY_CONF_ATT_NAME, "");
                    if (eFileProperty.length() == 0 || objectProperty.length() == 0)
                    {
                        LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - invalid property mapping in EFile for object class " + eFileConfiguration.getObjectClassNameString() + " and search template "
                                + searchConfiguration.getSearchTemplateName() + " located in document function with id=" + getPluginID());
                        throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                    }
                    searchConfiguration.addPropertyMapping(eFileProperty, objectProperty);
                }

                eFileConfiguration.add(searchConfiguration);

            }

            List<?> refinementUtilList = eFileUtil.getSafeUtilList(REFINEMENT_CONF_TAG);
            for (Iterator<?> j = refinementUtilList.iterator(); j.hasNext();)
            {
                OwXMLUtil refinementUtil = (OwXMLUtil) j.next();
                String subPath = refinementUtil.getSafeStringAttributeValue(SUBPATH_CONF_ATT_NAME, "");
                if (subPath.length() == 0)
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - subpath missing in refinement");
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                }
                OwXMLUtil refinmentClassesUtil = refinementUtil.getSubUtil(OBJECTCLASSES_CONF_TAG_NAME);
                List<String> refinementClasseNames = createConfigurationObjectClasses(refinmentClassesUtil);
                List<String> cdataExpressions = refinementUtil.getSafeCDATAList(EXPRESSION_CONF_TAG);
                if (cdataExpressions.isEmpty())
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - missing expression in " + createRefinementErrorLocation(objectClasseNames, subPath));
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                }
                if (cdataExpressions.size() > 1)
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - only one expression.refinement is accepted " + createRefinementErrorLocation(objectClasseNames, subPath));
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                }
                String expressionString = cdataExpressions.get(0);
                OwExprExpression expression = null;
                try
                {
                    expression = OwExprParser.parse(expressionString);
                    if (expression.hasErrors())
                    {
                        LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - error in refinement expression" + expressionString + " > " + expression.getErrorTable() + " in "
                                + createRefinementErrorLocation(objectClasseNames, subPath));
                        throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                    }
                    OwExprExpressionType expressionType = expression.type();
                    if (!expressionType.canInfer(OwExprType.BOOLEAN))
                    {
                        LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - non-boolean refinement expression " + expressionString + " in " + createRefinementErrorLocation(objectClasseNames, subPath));
                        throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                    }
                }
                catch (ParseException e)
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - error parsing expression " + expressionString + " in  " + createRefinementErrorLocation(objectClasseNames, subPath), e);
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."), e);
                }
                OwEFileRefinementConfiguration refinementConfiguration = new OwEFileRefinementConfiguration(subPath, refinementClasseNames, expression);
                eFileConfiguration.add(refinementConfiguration);
            }

            List<String> configuredClassNames = eFileConfiguration.getObjectClassNames();
            for (String className : configuredClassNames)
            {
                if (this.eFileConfigurations.containsKey(className))
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - multiple EFile configurations for object class  " + className + " located in document function with id=" + getPluginID());
                    throw new OwConfigurationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.config.error", "Invalid EFile-Reference-Document-Function configuration."));
                }
                else
                {
                    this.eFileConfigurations.put(className, eFileConfiguration);
                }
            }
        }
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owefileref/efileref.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owefileref/efileref_24.png");
    }

    @SuppressWarnings("unchecked")
    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_BEGIN);
        OwObjectClass objectClass = object_p.getObjectClass();
        String className = objectClass.getClassName();
        OwEFileConfiguration eFileConfiguration = this.eFileConfigurations.get(className);
        if (eFileConfiguration != null)
        {
            OwRoleManagerContext roleCtx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
            OwNetwork<OwObject> network = roleCtx.getNetwork();
            OwObjectCollection semiVirtualEFiles = new OwStandardObjectCollection();
            List<OwEFileSearchConfiguration> searchConfigurations = eFileConfiguration.getSearchConfigurations();

            for (OwEFileSearchConfiguration searchConfiguration : searchConfigurations)
            {
                String searchTemplateName = searchConfiguration.getSearchTemplateName();
                OwSearchTemplate searchTemplate = roleCtx.getUnmanagedAOProvider().getApplicationObject(OwAOConstants.AO_SEARCHTEMPLATE, searchTemplateName, false, false);
                searchTemplate.init(network);
                OwSearchNode searchNode = searchTemplate.getSearch(true);

                List<OwEFileObjectCollectionFilter> eFileFilters = enforcePropertyMappings(searchConfiguration, searchNode, object_p);
                OwObjectCollection searchEFiles = network.doSearch(searchNode, null, null, 2, 0);
                List<OwObject> allowedEFiles = filterAll(searchEFiles, eFileFilters);
                semiVirtualEFiles.addAll(allowedEFiles);
            }

            if (semiVirtualEFiles.size() == 1)
            {
                List<String> subPaths = new LinkedList<String>();
                List<OwEFileRefinementConfiguration> refinements = eFileConfiguration.getRefinements(className);
                for (OwEFileRefinementConfiguration refinement : refinements)
                {
                    if (refinement != null)
                    {
                        if (refinement.matches(object_p))
                        {
                            subPaths.add(refinement.getSubPath());
                        }
                    }
                }
                OwObject singleEFileObject = (OwObject) semiVirtualEFiles.get(0);

                if (subPaths.size() <= 1)
                {
                    String subPath = null;
                    if (subPaths.size() == 1)
                    {
                        subPath = subPaths.get(0);
                    }

                    //                    OwMimeManager mimeManager = new OwMimeManager();//FIXME: Not used anymore?
                    //                    mimeManager.attach(getContext(), null);

                    // get the handler plugin
                    OwMasterDocument masterPlugin = OwMimeManager.getHandlerMasterPlugin(getContext(), singleEFileObject);

                    String label = roleCtx.localizeLabel(subPath);

                    // open with plugin
                    masterPlugin.dispatch(OwDispatchCodes.OPEN_OBJECT, singleEFileObject, label);
                }
                else
                {
                    processMultipleVirtualReferences(singleEFileObject, subPaths);
                }
            }
            else if (semiVirtualEFiles.size() > 1)
            {
                processMultipleObjectReferences(semiVirtualEFiles);
            }
            else
            {
                addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
                LOG.debug("OwEFileReferenceDocumentFunction.createMultipleReferencesMessageBox(): eFile reference error - no reference found");
                throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.no.reference.error", "No eFile reference found!"));
            }
        }
        else
        {
            addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            LOG.error("OwEFileReferenceDocumentFunction.createMultipleReferencesMessageBox(): eFile reference error - no eFile configurations found");
            throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.no.configuration.error", "No eFile configurations found!"));
        }

        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Handles multiple matching virtual eFile references for a given object by
     * opening a message box that informs the user on the multiple matches.  
     * @param semiVirtualEFile_p the matching semi-virtual-entry
     * @param virtualSubpaths_p {@link List} of multiple String subpaths  
     * @throws OwException
     */
    private void processMultipleVirtualReferences(OwObject semiVirtualEFile_p, List<String> virtualSubpaths_p) throws OwException
    {
        try
        {
            List<String> references = new LinkedList<String>();
            String semiVirtualPath = semiVirtualEFile_p.getPath();
            for (String virtualSubPath : virtualSubpaths_p)
            {
                String reference = OwHTMLHelper.encodeToSecureHTML(semiVirtualPath + "/" + virtualSubPath);
                references.add(reference);
            }
            openMultipleReferencesMessageBox(references);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            OwMainAppContext context = getContext();
            LOG.error("OwEFileReferenceDocumentFunction.processMultipleVirtualReferences(): eFile reference error - could not create multiple refereces message box", e);
            throw new OwInvalidOperationException(context.localize("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }

    /**
     * Handles multiple matching semi-virtual eFile references for a given object by
     * opening a message box that informs the user on the multiple matches.  
     * @param semiVirtualEFiles_p multiple matching {@link OwObject}-eFile s
     * @throws OwException
     */
    private void processMultipleObjectReferences(OwObjectCollection semiVirtualEFiles_p) throws OwException
    {
        try
        {
            List<String> references = new LinkedList<String>();
            for (Iterator<?> i = semiVirtualEFiles_p.iterator(); i.hasNext();)
            {
                OwObject eFile = (OwObject) i.next();
                String reference = OwHTMLHelper.encodeToSecureHTML(eFile.getPath());
                references.add(reference);
            }
            openMultipleReferencesMessageBox(references);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            OwMainAppContext context = getContext();
            LOG.error("OwEFileReferenceDocumentFunction.processMultipleObjectReferences(): eFile reference error - could not create multiple refereces message box", e);
            throw new OwInvalidOperationException(context.localize("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }

    /**
     * Handles multiple matching File references for a given object by
     * opening a message box that informs the user on the multiple matches.  
     * @param references_p {@link List} of String eFile paths
     * @throws OwException
     */
    private void openMultipleReferencesMessageBox(List<String> references_p) throws OwException
    {
        OwMainAppContext context = getContext();
        try
        {
            // === create a string with the first TOTAL_ELEMENTS_DISPLAY objects
            StringBuffer dialogContent = new StringBuffer();
            //create description of what happens
            dialogContent.append(context.localize("owefileref.OwEFileReferenceDocumentFunction.multiple.references.found", "Multiple eFile references were found : "));

            dialogContent.append("<ul class=\"OwMessageBoxText\">");

            Iterator<String> it = references_p.iterator();

            //create list of objects which are processed
            while (it.hasNext())
            {
                String reference = it.next();

                dialogContent.append("<li>");
                dialogContent.append(reference);
                dialogContent.append("</li>");
                if ((0 == --TOTAL_ELEMENTS_DISPLAY) && it.hasNext())
                {
                    // there are more than TOTAL_ELEMENTS_DISPLAY objects, add ellipses and break
                    dialogContent.append("<li>");
                    dialogContent.append("...");
                    dialogContent.append("</li>");
                    break;
                }
            }

            dialogContent.append("</ul><br />");
            String title = context.localize("owefileref.OwEFileReferenceDocumentFunction.multiple.references.found.title", "The followings eFile references found!");
            OwMessageBox messageBox = new OwMessageBox(OwMessageBox.TYPE_CANCEL, OwMessageBox.ACTION_STYLE_BUTTON, OwMessageBox.ICON_TYPE_EXCLAMATION, title, dialogContent.toString());

            // open the dialog
            getContext().openDialog(messageBox, null);
        }
        catch (Exception e)
        {
            LOG.error("OwEFileReferenceDocumentFunction.createMultipleReferencesMessageBox(): eFile reference error - could not create multiple refereces message box", e);
            throw new OwInvalidOperationException(context.localize("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }

    /**
     * 
     * @param objects_p an {@link OwObject} collection
     * @param filters_p a {@link List} of {@link OwEFileFilter}s 
     * @return a {@link List} containing a the allowed-objects subset of objects_p 
     * @see OwEFileFilter#allows(OwObject)
     * @throws OwException
     */
    private List<OwObject> filterAll(OwObjectCollection objects_p, List<? extends OwEFileFilter> filters_p) throws OwException
    {
        List<OwObject> allowedObjects = new LinkedList<OwObject>();
        for (Iterator<?> i = objects_p.iterator(); i.hasNext();)
        {
            OwObject object = (OwObject) i.next();
            if (object != null)
            {
                boolean passed = true;
                for (OwEFileFilter filter : filters_p)
                {
                    if (!filter.allows(object))
                    {
                        passed = false;
                        break;
                    }
                }
                if (passed)
                {
                    allowedObjects.add(object);
                }
            }
        }
        return allowedObjects;
    }

    /**
     * Enforces the property mapping configuration from the given {@link OwEFileSearchConfiguration}
     * onto the given {@link OwSearchNode} (searchNode_p)  using property values from the given 
     * {@link OwObject} (object_p). 
     * @param searchConfiguration_p OwEFileSearchConfiguration
     * @param searchNode_p OwSearchNode
     * @param object_p OwObject
     * @return a {@link List} of {@link OwEFileObjectCollectionFilter}'s for the configured property mappings that can not be enforced
     *         on {@link OwSearchNode}s.
     * @throws OwException
     */
    protected List<OwEFileObjectCollectionFilter> enforcePropertyMappings(OwEFileSearchConfiguration searchConfiguration_p, OwSearchNode searchNode_p, OwObject object_p) throws OwException
    {
        try
        {
            List<OwEFileObjectCollectionFilter> eFileFilters = new LinkedList<OwEFileObjectCollectionFilter>();
            Map<?, ?> searchCriteriaMap = searchNode_p.getCriteriaMap(OwSearchNode.FILTER_NONE);
            Map<String, String> propertyMappings = searchConfiguration_p.getPropertyMappings();

            // get object and the corresponding EFile property
            for (Entry<String, String> entry : propertyMappings.entrySet())
            {
                OwProperty theProperty = null;

                if (OwSearchPathField.CLASS_NAME.equals(entry.getValue()))
                {
                    OwObjectCollection parents = object_p.getParents();
                    eFileFilters.add(new OwEFileObjectCollectionFilter(parents));
                }
                else if (searchCriteriaMap.containsKey(entry.getKey()))
                {
                    if (entry.getValue() != null)
                    {
                        theProperty = object_p.getProperty(entry.getValue());

                        OwSearchCriteria searchCricteria = (OwSearchCriteria) searchCriteriaMap.get(entry.getKey());
                        Object theValue = convertPropertyValue(searchCricteria, theProperty);
                        searchCricteria.setValue(theValue);
                    }
                }
            }

            return eFileFilters;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwEFileReferenceDocumentFunction.enforcePropertyMappings(): error on eFile property mapping", e);
            throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }

    /** (overridable)
     * Convert the given property value to a value compatible with given search criteria.
     * The default implementation returns the same value if java classes are the same in
     * the property and the search criteria (see {@link OwSearchCriteria#getJavaClassName()} and {@link OwFieldDefinition#getJavaClassName()}). 
     * If the two classes don't match search a search-criteria based string conversion is performed    
     * on the properties value string representation.
     * 
     * @param searchCriteria_p
     * @param objectProperty_p
     * @return converted Object value  
     * @throws OwException
     */
    protected Object convertPropertyValue(OwSearchCriteria searchCriteria_p, OwProperty objectProperty_p) throws OwException
    {
        try
        {

            String criteriaJavaClass = searchCriteria_p.getJavaClassName();
            OwFieldDefinition propertyFieldDefinition = objectProperty_p.getFieldDefinition();
            String propertyJavaClass = propertyFieldDefinition.getJavaClassName();

            if (criteriaJavaClass.equals(propertyJavaClass))
            {
                return objectProperty_p.getValue();
            }
            else
            {
                // convert data type
                Object value = objectProperty_p.getValue();
                if (null == value)
                {
                    return null;
                }

                return searchCriteria_p.getValueFromString(value.toString());
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwEFileReferenceDocumentFunction.convertPropertyValue(): error on eFile property value cobversion ", e);
            throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }
}
