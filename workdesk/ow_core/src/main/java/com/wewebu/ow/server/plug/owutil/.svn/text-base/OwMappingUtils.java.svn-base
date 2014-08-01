package com.wewebu.ow.server.plug.owutil;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprValue;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwObjectScope;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Static class with utility functions to create mappings and read mappings from XML.
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
public class OwMappingUtils
{
    /**
     * Helper class to hold the parsed parameter structure.
     * 
     */
    private static class OwParamMappingNameStructure
    {
        /** destination property name*/
        public final String destinationParameterName;

        /** source expression*/
        public final String sourceExpression;

        public OwParamMappingNameStructure(String destinationParameterName_p, String sourceExpression_p)
        {
            super();
            this.destinationParameterName = destinationParameterName_p;
            this.sourceExpression = sourceExpression_p;
        }

    }

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMappingUtils.class);

    private static Object evaluate(String expression_p, OwObject object_p) throws ParseException, OwExprEvaluationException
    {
        final String objectScopeName = "object";
        OwExprParser parser = new OwExprParser(new StringReader(objectScopeName + "." + expression_p));

        OwExprExpression expression = parser.ExprExpression();

        OwObjectScope objectScope = new OwObjectScope(objectScopeName, object_p);

        OwExprValue evalResult = expression.evaluate(new OwExprExternalScope[] { objectScope });

        return evalResult.toJavaObject(evalResult.getJavaType());
    }

    /** iterate over the given folder object until the property is resolved
     *
     * @param confignode_p OwXMLUtil plugin descriptor
     * @param rootObject_p OwObject record root
     * @param obj_p OwObject current visited folder
     * @param expression_p String property expression to look for
     *
     * @return Object value of property
     */
    private static Object findParameterValueInRecord(OwXMLUtil confignode_p, OwObject rootObject_p, OwObject obj_p, String expression_p) throws Exception
    {
        // try to resolve the property in the given object
        try
        {
            return evaluate(expression_p, obj_p);
        }
        catch (OwExprEvaluationException e)
        {
            LOG.debug("Could not find parameter expression value. Trying root object.", e);
        }
        catch (ParseException e)
        {
            LOG.error("Could not find parameter value.", e);
            throw new OwConfigurationException("Could not find parameter value : " + " " + expression_p + " (" + confignode_p.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "unknown") + ")", e);
        }

        // === could not find the property look in the parent
        // but only if not the root object
        if (obj_p.getDMSID().equals(rootObject_p.getDMSID()))
        {
            return null;
        }

        List parents = obj_p.getParents();

        if ((parents == null) || (parents.size() == 0))
        {
            return null;
        }

        OwObject parent = (OwObject) parents.get(0);
        return findParameterValueInRecord(confignode_p, rootObject_p, parent, expression_p);
    }

    /**
    * Get the property value from the source object.
    * @param confignode_p OwXMLUtil plugin descriptor
    * @param sourceObject_p the {@link OwObject} source
    * @param expression_p String property expression to look for
    *
    * @return Object value of property
    */
    private static Object findParameterValueInObject(OwXMLUtil confignode_p, OwObject sourceObject_p, String expression_p) throws Exception
    {
        // try to resolve the property in the given object
        try
        {
            return evaluate(expression_p, sourceObject_p);
        }
        catch (Exception e)
        {
            LOG.error("Could not find parameter value.", e);
            throw new OwConfigurationException("Could not find parameter value : " + " " + expression_p + " (" + confignode_p.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "unknown") + ")", e);
        }

    }

    /** 
     * Compute a Map of values derived from the parent and to be set in advance in the new object,
     *reads the ParameterMapping section in the plugin descriptor.
     *
     * Each Parameter from the Parent Object is set to the parameter in the new Object<br/>
     * Example: <br/> 
     *   Instruction: Name=Customer.Name <br/>
     * The Parameter <I>Name</I> in the new object gets the value of the Parameter <I>Customer.Name</I> in the Parent object<br/> 
     * where <I>Customer</I> is a Object Reference Property and <I>Name</I> is the property of that reference.
     *
     * @param confignode_p OwXMLUtil plugin configuration node
     * @param rootObject_p the Parent OwObject where to create the object and derive the properties from
     * @param folderObject_p the folder to look up the properties if not found look up in rootObject_p
     *
     * @return Map key is the property name of the new object, value is the value to set
     */
    public static Map getParameterMapValuesFromRecord(OwXMLUtil confignode_p, OwObject rootObject_p, OwObject folderObject_p) throws Exception
    {
        if (null == folderObject_p)
        {
            return null;
        }

        Map valuesMap = new HashMap();
        List mappingConfiguration = confignode_p.getSafeStringList("ParameterMapping");

        // === Iterate over the instructions
        Iterator instructions = mappingConfiguration.iterator();
        while (instructions.hasNext())
        {
            String strInstruction = (String) instructions.next();

            OwParamMappingNameStructure paramStructure = extractParameterNames(confignode_p, strInstruction);

            // === Create mapping entry
            Object parentValue = findParameterValueInRecord(confignode_p, rootObject_p, folderObject_p, paramStructure.sourceExpression);

            if (null != parentValue)
            {
                valuesMap.put(paramStructure.destinationParameterName, parentValue);
            }
        }

        if (valuesMap.size() == 0)
        {
            return null;
        }
        else
        {
            return valuesMap;
        }
    }

    /** Compute a Map of values derived from the parent and to be set in advance in the new object
    *
    * reads the ParameterMapping section in the plugin descriptor
    *
    * Each Parameter from the Parent Object is set to the parameter in the new Object<br/>
    * Example: <br/> 
    *   Instruction: Name=Customer.Name <br/>
    * The Parameter <I>Name</I> in the new object gets the value of the Parameter <I>Customer.Name</I> in the Parent object<br/> 
    * where <I>Customer</I> is a Object Reference Property and <I>Name</I> is the property of that reference.
    *
    * @param confignode_p OwXMLUtil plugin configuration node
    * @param sourceObject_p the folder to look up the properties if not found look up in rootObject_p
    *
    * @return Map key is the property name of the new object, value is the value to set
    */
    public static Map getParameterMapValuesFromObject(OwXMLUtil confignode_p, OwObject sourceObject_p, String mappingElementName_p) throws Exception
    {
        if (null == sourceObject_p)
        {
            return null;
        }

        Map valuesMap = new HashMap();
        List mappingConfiguration = confignode_p.getSafeStringList(mappingElementName_p);

        // === Iterate over the instructions
        Iterator instructions = mappingConfiguration.iterator();
        while (instructions.hasNext())
        {
            String strInstruction = (String) instructions.next();

            OwParamMappingNameStructure paramStructure = extractParameterNames(confignode_p, strInstruction);

            // === Create mapping entry
            Object sourceValue = findParameterValueInObject(confignode_p, sourceObject_p, paramStructure.sourceExpression);

            if (null != sourceValue)
            {
                valuesMap.put(paramStructure.destinationParameterName, sourceValue);
            }
        }

        if (valuesMap.size() == 0)
        {
            return null;
        }
        else
        {
            return valuesMap;
        }
    }

    /**
     * Helper method, to extract the parameter names form a give structure string
     * @param confignode_p
     * @param strInstruction_p
     * @return the {@link OwParamMappingNameStructure} object
     * @throws OwConfigurationException
     */
    private static OwParamMappingNameStructure extractParameterNames(OwXMLUtil confignode_p, String strInstruction_p) throws OwConfigurationException
    {
        // === Split instruction
        int iSplit = strInstruction_p.indexOf('=');
        if (-1 == iSplit)
        {
            String msg = "OwMappingUtils.getParameterMapValuesFromRecord: Incorrect ParameterMapping format used in plugin descriptor of = " + confignode_p.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "unknown");
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        // Split equality & Remove white space

        String destinationParameterName = strInstruction_p.substring(0, iSplit).trim();
        String sourceParameterName = strInstruction_p.substring(iSplit + 1).trim();

        return new OwParamMappingNameStructure(destinationParameterName, sourceParameterName);
    }

    /** direction attribute used in getObjectClassMap */
    public static final int EMPTY_MAPPING = 0;
    /** direction attribute used in getObjectClassMap */
    public static final int AUTO_SELECT_CLASS = 1;
    /** direction attribute used in getObjectClassMap */
    public static final int AUTO_SELECT_FOLDER = 2;

    /** Compute a Map of object class - subfolder mappings
     *
     * reads the ObjectClassMapping section in the plugin descriptor
     *
     * List of object class mappings to be used.
     * Each object class is mapped to a subfolder path.
     * 
     * Where the direction attribute will specify the following:
     * 
     *      "autoselectclass":	The selected subfolder will select 
     *                          a object class to be used.
     * 
     *
     *      "autoselectfolder":	The selected class will select
     *                          a subfolder to be used to store the new object.
     *
     * @param confignode_p OwXMLUtil plugin configuration node
     * @param objectclassmap_p Map which will be filled with object class mappings
     *          depending on directionflag (see return value) 
     *          keys and values are:
     *
     *              AUTO_SELECT_CLASS:  Key is the subfolder path 
     *                                  and value is the object class.
     *
     *              AUTO_SELECT_FOLDER: Key is the objectclass
     *                                  and value is the subfolderpath.
     *
     * @return int directionflag which is either AUTO_SELECT_CLASS or AUTO_SELECT_FOLDER
     *
     */
    public static int getObjectClassMap(OwXMLUtil confignode_p, Map objectclassmap_p) throws Exception
    {
        // === read mapping descriptor
        // mappings
        List mappingConfiguration = confignode_p.getSafeStringList("ObjectClassMapping");

        if (mappingConfiguration.size() == 0)
        {
            return EMPTY_MAPPING;
        }

        // direction attribute
        int iDirection = AUTO_SELECT_CLASS;
        if (OwXMLDOMUtil.getSafeStringAttributeValue(confignode_p.getSubNode("ObjectClassMapping"), "direction", "autoselectclass").equalsIgnoreCase("autoselectclass"))
        {
            iDirection = AUTO_SELECT_CLASS;
        }
        else
        {
            iDirection = AUTO_SELECT_FOLDER;
        }

        // === Iterate over the instructions
        Iterator instructions = mappingConfiguration.iterator();
        while (instructions.hasNext())
        {
            String strInstruction = (String) instructions.next();

            // === Split instruction
            int iSplit = strInstruction.indexOf('=');
            if (-1 == iSplit)
            {
                String msg = "OwMappingUtils.getObjectClassMap: Incorrect ObjectClassMapping format used in plugin descriptor of = " + confignode_p.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "unknown");
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            String strSubFolderName = strInstruction.substring(0, iSplit);
            String strObjectClassName = strInstruction.substring(iSplit + 1);

            // Remove white space
            strSubFolderName = strSubFolderName.trim();
            strObjectClassName = strObjectClassName.trim();

            // === Create mapping entry
            switch (iDirection)
            {
                case AUTO_SELECT_CLASS:
                    objectclassmap_p.put(strSubFolderName, strObjectClassName);
                    break;

                case AUTO_SELECT_FOLDER:
                    objectclassmap_p.put(strObjectClassName, strSubFolderName);
                    break;
            }
        }

        return iDirection;
    }
}
