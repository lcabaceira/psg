package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Java model of the &lt;SemiVirtualRecordClassName&gt; XML configuration node in owbootstrap.xml.
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
 * @since 4.0.0.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwSemiVirtualRecordClass
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSemiVirtualRecordClass.class);

    public static final String SEMIVIRTUALRECORDCLASSES_ELEMENT = "SemiVirtualRecordClasses";
    public static final String SEMIVIRTUALRECORDCLASSNAME_ELEMENT = "SemiVirtualRecordClassName";

    public static final String INCLUDEPHYSICALCHILDS_ATTRIBUTE = "includephysicalchilds";
    public static final String FOLDERCLASSNAME_ATTRIBUTE = "folderclassname";
    public static final String VIRTUALFOLDER_ATTRIBUTE = "virtualfolder";
    public static final String SEARCHSUBSTRUCTURE_ATTRIBUTE = "searchSubstructure";

    public static final Map<String, OwSemiVirtualRecordClass> createVirtualRecordClasses(OwXMLUtil configuration_p) throws OwConfigurationException
    {
        return createVirtualRecordClasses(configuration_p, null);
    }

    /**
     * 
     * @param configuration_p bootstrap configuration node 
     * @return a {@link Map} of {@link OwSemiVirtualRecordClass} mapped by their folder class name 
     *         as defined in  SemiVirtualRecordClasses configuration node in bootstrap configuration
     * @throws OwConfigurationException 
     */
    public static final Map<String, OwSemiVirtualRecordClass> createVirtualRecordClasses(OwXMLUtil configuration_p, Collection<String> selectiveConfigurationIds_p) throws OwConfigurationException
    {

        String optionId = null;

        if (selectiveConfigurationIds_p != null)
        {
            try
            {
                for (String id : selectiveConfigurationIds_p)
                {
                    int i = id.lastIndexOf('.');
                    String key = id.substring(0, i);
                    if (SEMIVIRTUALRECORDCLASSES_ELEMENT.equals(key))
                    {
                        optionId = id.substring(i + 1, id.length());
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                throw new OwConfigurationException("Could not create virtual record classes.", e);
            }
        }

        List<OwXMLUtil> list = configuration_p.getSafeUtilList(SEMIVIRTUALRECORDCLASSES_ELEMENT);

        Map<String, OwSemiVirtualRecordClass> semivirtualfolderMap = new HashMap<String, OwSemiVirtualRecordClass>();

        for (OwXMLUtil declaration : list)
        {
            String declarationOptionId = declaration.getSafeStringAttributeValue("optionid", null);
            if ((optionId == null && declarationOptionId == null) || declarationOptionId == null || (declarationOptionId.equals(optionId)))
            {
                List<Node> recordClassDeclarations = declaration.getSafeNodeList();
                for (Node recordClassDeclaration : recordClassDeclarations)
                {
                    OwSemiVirtualRecordClass entry = new OwSemiVirtualRecordClass(recordClassDeclaration);
                    semivirtualfolderMap.put(entry.getFolderClassName(), entry);
                }

            }
        }

        return semivirtualfolderMap;
    }

    private String virtualFolder;
    private String folderClassName;
    private boolean includePhysicalChilds;
    private boolean searchSubstructure;

    private Map propertyMapping;

    public OwSemiVirtualRecordClass(Node n_p) throws OwConfigurationException
    {
        virtualFolder = OwXMLDOMUtil.getSafeStringAttributeValue(n_p, VIRTUALFOLDER_ATTRIBUTE, null);
        if (null == virtualFolder)
        {
            String msg = "OwSemiVirtualRecordClass: Please define \"virtualfolder\" attribute for SemiVirtualRecordClassName node in the owbootstrap.xml.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        folderClassName = OwXMLDOMUtil.getSafeStringAttributeValue(n_p, FOLDERCLASSNAME_ATTRIBUTE, null);
        if (null == folderClassName)
        {
            String msg = "OwSemiVirtualRecordClass: Please define \"folderclassname\" attribute for SemiVirtualRecordClassName node in the owbootstrap.xml.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        includePhysicalChilds = OwXMLDOMUtil.getSafeBooleanAttributeValue(n_p, INCLUDEPHYSICALCHILDS_ATTRIBUTE, false);
        searchSubstructure = OwXMLDOMUtil.getSafeBooleanAttributeValue(n_p, SEARCHSUBSTRUCTURE_ATTRIBUTE, false);

        // create map of property mappings keyed by the virtual folder property
        propertyMapping = new HashMap();

        Collection<?> propertymappings = OwXMLDOMUtil.getSafeNodeList(n_p);

        Iterator<?> it = propertymappings.iterator();
        while (it.hasNext())
        {
            Node vfProperty = (Node) it.next();

            // add to map
            if (null != vfProperty.getFirstChild())
            {
                propertyMapping.put(vfProperty.getFirstChild().getNodeValue(), OwXMLDOMUtil.getSafeStringAttributeValue(vfProperty, "folderproperty", null));
            }
        }

    }

    /**
     * Return the folder class value of semi-virtual structure definition.
     * Definition of object-type which is used to map virtual structure.
     * @return String
     */
    public String getFolderClassName()
    {
        return folderClassName;
    }

    /**
     * Return template name which is
     * used for semi-virtual structure.
     * @return String
     */
    public String getVirtualFolder()
    {
        return virtualFolder;
    }

    /**
     * Should physical children be returned
     * also during folder retrieval.
     * @return boolean, by default false
     */
    public boolean isIncludePhysicalChilds()
    {
        return includePhysicalChilds;
    }

    /**
     * Get property mapping which was configured for current
     * semi-virtual structure.
     * @return Map
     */
    public Map getPropertyMapping()
    {
        return propertyMapping;
    }

    /**
     * Should search be extended to semi-virtual root.
     * @return boolean, by default false
     */
    public boolean isSearchSubstructure()
    {
        return searchSubstructure;
    }
}
