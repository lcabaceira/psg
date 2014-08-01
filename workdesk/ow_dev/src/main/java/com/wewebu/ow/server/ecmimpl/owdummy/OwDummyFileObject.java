package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwCaseObject;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardEnum;
import com.wewebu.ow.server.field.OwStandardEnumCollection;
import com.wewebu.ow.server.field.OwStandardLocalizeableEnum;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implements OwFileObject for dummy personal files on the file system.
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
public class OwDummyFileObject extends OwFileObject implements OwVersionSeries, OwVersion, OwCaseObject
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDummyFileObject.class);

    /**
     *<p>
     * OwOperatorsSingleton.
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
    public static class OwOperatorsSingleton
    {
        /** collection of selectable operators for numbers and dates */
        public Collection<Integer> m_datenumoperators = new LinkedList<Integer>();
        public Collection<Integer> m_otheroperators = new LinkedList<Integer>();

        public OwOperatorsSingleton()
        {
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
            m_datenumoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));

            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
            m_otheroperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
        }
    }

    protected static final OwOperatorsSingleton m_operators = new OwOperatorsSingleton();

    /**
     *<p>
     * Property class definition for the OwStandardProperty properties.
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
    protected static class OwDummyFilePropertyClass extends OwFilePropertyClass
    {
        public OwDummyFilePropertyClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, Collection operators_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p);
            m_operators = operators_p;
        }

        public OwDummyFilePropertyClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, Collection operators_p, boolean required_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, required_p);
            m_operators = operators_p;
        }
    }

    /**
     *<p>
     * Overwritten file property class for arrays.
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
    public static class OwFilePropertyArrayClass extends OwDummyFilePropertyClass
    {
        public OwFilePropertyArrayClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, Object defaultObject_p, Collection operators_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, operators_p);

            m_fArray = true;
            m_Default = defaultObject_p;
        }

        public OwFilePropertyArrayClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, Object defaultObject_p, Collection operators_p, boolean required_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, operators_p, required_p);

            m_fArray = true;
            m_Default = defaultObject_p;
        }
    }

    /**
     *<p>
     * Overwritten file property class for enums.
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
    public static class OwFilePropertyEnumClass extends OwDummyFilePropertyClass
    {
        public OwFilePropertyEnumClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, OwEnumCollection enums_p, Collection operators_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, operators_p);

            this.m_Enums = enums_p;
        }
    }

    /**
     *<p>
     * Property class definition for the OwStandardProperty properties.
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
    protected static class OwFilePropertyClassMaxLen extends OwDummyFilePropertyClass
    {
        public OwFilePropertyClassMaxLen(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, int iMaxLen_p, Collection operators_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, operators_p);

            // set max string len
            if (iMaxLen_p > 0)
            {
                this.m_MaxValue = Integer.valueOf(iMaxLen_p);
            }
        }
    }

    /**
     *<p>
     * Property class definition for the OwStandardProperty properties.
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
    protected static class OwFilePropertyClassMinMax extends OwDummyFilePropertyClass
    {
        public OwFilePropertyClassMinMax(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, Object minValue_p, Object maxValue_p, Collection operators_p)
        {
            super(strClassName_p, strJavaClassName_p, displayName_p, fSystem_p, fReadOnly_p, fName_p, operators_p);

            m_MaxValue = maxValue_p;
            m_MinValue = minValue_p;
        }
    }

    /** File documents class definition for the OwFileDocument Object */
    public static class OwDummyFileObjectClass extends OwFileObjectClass
    {
        /** class name of the notes array property */
        public static final String NOTES_PROPERTY = "Notes";
        /** class name of the image property */
        public static final String IMAGE_PROPERTY = "Image";
        /** class name of a complex property */
        public static final String COMPLEX_PERSON_PROPERTY = "ComplexPerson";
        /** class name of a complex property array */
        public static final String COMPLEX_PERSON_ARRAY_PROPERTY = "ComplexPersonArray";
        /** class name of the ID property */
        public static final String ID_PROPERTY = "ID";
        /** class name of the evaluation points property */
        public static final String EVALUATION_POINTS_PROPERTY = "EvaluationPoints";
        /** class name of the first name property */
        public static final String FIRST_NAME_PROPERTY = "FirstName";
        /** class name of the last name property */
        public static final String LAST_NAME_PROPERTY = "LastName";
        /** class name of the address property */
        public static final String ADDRESS_PROPERTY = "Address";
        /** class name of the gender property */
        public static final String MALE_PROPERTY = "Gender";
        /** class name of the department property */
        public static final String DEPT_PROPERTY = "Department";
        /** class name of the date of birth property */
        public static final String DATE_OF_BIRTH_PROPERTY = "DateOfBirth";
        /** class name of the date of birth property */
        public static final String TEL_PROPERTY = "TelNumber";
        /** class name of the date of birth property */
        public static final String FAX_PROPERTY = "FaxNumber";
        /** class name of the status flag */
        public static final String STATUS_PROPERTY = "Status";
        /** class name of the comment flag */
        public static final String COMMENT_PROPERTY = "Comment";
        /** class name of the nationality */
        public static final String NATIONALITY_PROPERTY = "Nationality";
        /** class name of the BirthPlace */
        public static final String BIRTHPLACE_PROPERTY = "BirthPlace";
        /** class name of the Personal number */
        public static final String PERSONALNUMMER_PROPERTY = "EmployeeNumber";
        /** class name of the Image Control */
        public static final String IMAGE_CONTROL_PROPERTY = "ImageControl";

        private boolean m_fRecordRoot;

        /** construct PropertyClass Object and set Property classes */
        public OwDummyFileObjectClass(boolean fDirectory_p, boolean fRecordRoot_p)
        {
            // === create default file property classes
            super(fDirectory_p);

            m_fRecordRoot = fRecordRoot_p;

            // === create additional dummy property classes

            // complex person
            m_PropertyClassesMap.put(COMPLEX_PERSON_PROPERTY, new OwDummyComplexPersonPropertyClass(COMPLEX_PERSON_PROPERTY, new OwString("owdummy.OwDummyFileObject.complexperson", "Person"), false));
            // complex person array
            m_PropertyClassesMap.put(COMPLEX_PERSON_ARRAY_PROPERTY, new OwDummyComplexPersonPropertyClass(COMPLEX_PERSON_ARRAY_PROPERTY, new OwString("owdummy.OwDummyFileObject.complexpersonarray", "Persons"), true));

            // notes array
            m_PropertyClassesMap
                    .put(NOTES_PROPERTY, new OwFilePropertyArrayClass(NOTES_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.notes", "Notes"), false, false, false, new String[] { "Note 1", "Note 2", "Note 3" }, null));
            // image path
            m_PropertyClassesMap.put(IMAGE_PROPERTY, new OwDummyFilePropertyClass(IMAGE_PROPERTY, "com.wewebu.ow.server.ecm.OwObject", new OwString("owdummy.OwDummyFileObject.image", "Picture"), false, true, false, null));
            // Customer number
            m_PropertyClassesMap.put(ID_PROPERTY, new OwFilePropertyClassMinMax(ID_PROPERTY, "java.lang.Integer", new OwString("owdummy.OwDummyFileObject.id", "ID"), false, false, false, Integer.valueOf(10), Integer.valueOf(10000),
                    m_operators.m_datenumoperators));
            // Evaluation points
            m_PropertyClassesMap.put(EVALUATION_POINTS_PROPERTY, new OwFilePropertyArrayClass(EVALUATION_POINTS_PROPERTY, "java.lang.Double", new OwString("owdummy.OwDummyFileObject.evaluationpoints", "Evaluation points"), false, false, false,
                    new Double[] { new Double(0.0) }, null, true));
            // Persons first name
            m_PropertyClassesMap.put(FIRST_NAME_PROPERTY, new OwDummyFilePropertyClass(FIRST_NAME_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.firstname", "First Name"), false, true, false, m_operators.m_otheroperators));
            // Persons last name
            m_PropertyClassesMap.put(LAST_NAME_PROPERTY, new OwDummyFilePropertyClass(LAST_NAME_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.lastname", "Last Name"), false, true, false, null));
            // Persons address
            m_PropertyClassesMap.put(ADDRESS_PROPERTY, new OwDummyFilePropertyClass(ADDRESS_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.address", "Address"), false, false, false, null));
            // Persons gender
            m_PropertyClassesMap.put(MALE_PROPERTY, new OwDummyFilePropertyClass(MALE_PROPERTY, "java.lang.Boolean", new OwString("owdummy.OwDummyFileObject.gender", "Sex"), false, true, false, null));
            // Persons department
            m_PropertyClassesMap.put(DEPT_PROPERTY, new OwDummyFilePropertyClass(DEPT_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.department", "Department"), false, true, false, null));
            // Persons birthday
            m_PropertyClassesMap.put(DATE_OF_BIRTH_PROPERTY, new OwDummyFilePropertyClass(DATE_OF_BIRTH_PROPERTY, "java.util.Date", new OwString("owdummy.OwDummyFileObject.birthday", "Birthday"), false, false, false, m_operators.m_datenumoperators));
            // Persons fax
            m_PropertyClassesMap.put(TEL_PROPERTY, new OwDummyFilePropertyClass(TEL_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.tel", "Phone Number"), false, true, false, null));
            // Persons tel
            m_PropertyClassesMap.put(FAX_PROPERTY, new OwDummyFilePropertyClass(FAX_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.fax", "Fax Number"), false, true, false, null));
            // Intern flag
            m_PropertyClassesMap.put(STATUS_PROPERTY, new OwDummyFilePropertyClass(STATUS_PROPERTY, "java.lang.Boolean", new OwString("owdummy.OwDummyFileObject.Status", "Status"), false, false, false, null));

            // Image Control
            OwEnumCollection imagectrl = new OwStandardEnumCollection();
            imagectrl.add(new OwStandardEnum("New", "10"));
            imagectrl.add(new OwStandardEnum("2", "20"));
            imagectrl.add(new OwStandardEnum("3", "40"));
            imagectrl.add(new OwStandardEnum("4", "60"));
            imagectrl.add(new OwStandardEnum("5", "80"));
            imagectrl.add(new OwStandardEnum("Completed", "100"));
            m_PropertyClassesMap.put(IMAGE_CONTROL_PROPERTY, new OwFilePropertyEnumClass(IMAGE_CONTROL_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.ImageFieldcontrol", "Image Fieldcontrol"), false, false, false, imagectrl,
                    null));

            // Comment flag
            m_PropertyClassesMap.put(COMMENT_PROPERTY, new OwFilePropertyClassMaxLen(COMMENT_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.Comment", "Comment"), false, false, false, 512, null));
            // Nationality
            OwEnumCollection natnums = new OwStandardEnumCollection();

            natnums.add(new OwStandardEnum("de", "Deutsch"));
            natnums.add(new OwStandardEnum("en", "Englisch"));
            natnums.add(new OwStandardEnum("fr", "Französisch"));
            natnums.add(new OwStandardEnum("it", "Italienisch"));
            natnums.add(new OwStandardEnum("gr", "Griechenland"));
            natnums.add(new OwStandardEnum("gb", "Großbritanien"));
            natnums.add(new OwStandardEnum("gc", "Gran Canaria"));
            natnums.add(new OwStandardEnum("gh", "Ghana"));
            natnums.add(new OwStandardEnum("gr", "Grafschaft"));

            m_PropertyClassesMap.put(NATIONALITY_PROPERTY, new OwFilePropertyEnumClass(NATIONALITY_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.Nationality", "Nationality"), false, false, false, natnums, null));
            // BirthPlace
            m_PropertyClassesMap.put(BIRTHPLACE_PROPERTY, new OwDummyFilePropertyClass(BIRTHPLACE_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.BirthPlace", "Place of birth"), false, true, false, null));
            // Personal number
            m_PropertyClassesMap.put(PERSONALNUMMER_PROPERTY, new OwDummyFilePropertyClass(PERSONALNUMMER_PROPERTY, "java.lang.String", new OwString("owdummy.OwDummyFileObject.PersonalNumber", "Personnel Number"), false, false, false, null, true));
            // standard name property
            m_PropertyClassesMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), OwResource.m_ObjectNamePropertyClass);
        }

        /**
         * Get the available modes for set property operations
         * @return Collection of OwEnum objects, or null if no modes are defined
         * @throws Exception
         */
        public List getSetPropertyModes() throws Exception
        {
            List modes = new LinkedList();

            modes.add(new OwStandardLocalizeableEnum(Boolean.FALSE, new OwString("owdummy.OwDummyFileObject.setpropertymodenormal", "Normal")));
            modes.add(new OwStandardLocalizeableEnum(Boolean.TRUE, new OwString("owdummy.OwDummyFileObject.setpropertymodenewversion", "Create new version")));

            return modes;
        }

        /** get the name of the class
         * @return class name
         */
        public String getClassName()
        {
            if (m_fRecordRoot)
            {
                return "OwFileSystemRecordRootDirectory";
            }
            else
            {
                return super.getClassName();
            }
        }

        public List getModes(int operation_p) throws Exception
        {
            switch (operation_p)
            {
                case OwObjectClass.OPERATION_TYPE_CREATE_NEW_OBJECT:
                case OwObjectClass.OPERATION_TYPE_CHECKIN:
                {
                    List modes = new LinkedList();

                    modes.add(new OwStandardLocalizeableEnum(Boolean.FALSE, new OwString("owdummy.OwDummyFileObject.checkinmodenormal", "Normal")));
                    modes.add(new OwStandardLocalizeableEnum(Boolean.TRUE, new OwString("owdummy.OwDummyFileObject.checkinmodeautoclassify", "Auto-classify")));

                    return modes;
                }
                case OwObjectClass.OPERATION_TYPE_CHECKOUT:
                {
                    List modes = new LinkedList();

                    modes.add(new OwStandardLocalizeableEnum(Integer.valueOf(0), new OwString("owdummy.OwDummyFileObject.checkoutmodedefault", "as set")));
                    modes.add(new OwStandardLocalizeableEnum(Integer.valueOf(1), new OwString("owdummy.OwDummyFileObject.checkoutmodecollaborative", "Collaborative")));

                    return modes;
                }
                case OwObjectClass.OPERATION_TYPE_SET_PROPERTIES:
                    return getSetPropertyModes();
                default:
                    return null;
            }
        }
    }

    /** DMSID prefix to distinguish OwFileObjects from other objects */
    public static final String DMS_PREFIX = "owfi";

    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_FileClassDescription = new OwDummyFileObjectClass(false, false);
    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_DirectoryClassDescription = new OwDummyFileObjectClass(true, false);
    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_RecordRootDirectoryClassDescription = new OwDummyFileObjectClass(true, true);

    /** get the static class description
     * 
     * @return the {@link OwObjectClass} static description
     */
    public static OwObjectClass getStaticObjectClass()
    {
        return m_FileClassDescription;
    }

    /** get the class description of the object, the class descriptions are defined by the DMS System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        if (m_File.isFile())
        {
            return m_FileClassDescription;
        }
        else
        {
            if ((m_File.getParent() != null) && (m_File.getParent().endsWith("dummyarchiv")))
            {
                return m_RecordRootDirectoryClassDescription;
            }
            else
            {
                return m_DirectoryClassDescription;
            }
        }
    }

    /** get the DMS specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the DMS Adaptor is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the DMS Adaptor,
     *  but would usually be made up like the following:
     *
     */
    public String getDMSID() throws Exception
    {
        // return the directory path to the object as a DMSID
        return getDMSID(((OwDummyNetwork) getNetwork()).getArchiveBaseDir(), m_File);
    }

    /** filter properties singleton */
    private static Set m_filterpropertiesSet = new HashSet();
    static
    {
        m_filterpropertiesSet.add(OwDummyFileObjectClass.ID_PROPERTY);
        m_filterpropertiesSet.add(OwDummyFileObjectClass.FIRST_NAME_PROPERTY);
        m_filterpropertiesSet.add(OwDummyFileObjectClass.DATE_OF_BIRTH_PROPERTY);
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        if (getType() == OBJECT_TYPE_FOLDER)
        {
            // === file folder allows some filter properties (simulation for test purpose)
            LinkedList filterproperties = new LinkedList();

            if (null == propertynames_p)
            {
                propertynames_p = getObjectClass().getPropertyClassNames();
            }

            Iterator it = propertynames_p.iterator();
            while (it.hasNext())
            {
                OwPropertyClass propclass = null;
                try
                {
                    propclass = getObjectClass().getPropertyClass(it.next().toString());
                }
                catch (OwObjectNotFoundException e)
                {
                    // ignore, might happen in federated szenarios
                    continue;
                }

                if (m_filterpropertiesSet.contains(propclass.getClassName()))
                {
                    filterproperties.add(propclass);
                }
            }

            return filterproperties;
        }
        else
        {
            return null;
        }
    }

    /** get the DMS specific ID of the Object. 
     *
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the DMS Adaptor is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the DMS Adaptor,
     *  but would usually be made up like the following:
     *
     * @param basedir_p base dir of the archive without ending backslash
     * @param file_p File to create a DMSID for
     *
     */
    public static String getDMSID(String basedir_p, File file_p) throws Exception
    {
        String relativePath = OwString.replaceAll(file_p.getPath(), "\\", "/");

        // try to get the relative path, which is more robust.
        // The DMSID supports both, the relative and the absolute path
        if (relativePath.startsWith(basedir_p))
        {
            relativePath = relativePath.substring(basedir_p.length());
        }

        // return the directory path to the object as a DMSID
        return OwDummyNetwork.DMS_PREFIX + "," + DMS_PREFIX + "," + relativePath;
    }

    /** construct File Object
     * @param network_p reference to the network adaptor
     * @param file_p reference to the file, the object is working on.
     */
    public OwDummyFileObject(OwNetwork network_p, java.io.File file_p) throws Exception
    {
        super(network_p, file_p);
    }

    boolean m_fPropertiesLoaded = false;

    /** load all properties of the file. There are so little, we just load all at once
     */
    protected void loadProperties() throws Exception
    {
        if (m_fPropertiesLoaded)
        {
            return;
        }

        m_fPropertiesLoaded = true;

        // === load defaults
        super.loadProperties();

        // === load additional dummy properties
        // notes array property
        m_PropertyMap.put(OwDummyFileObjectClass.NOTES_PROPERTY, new OwStandardProperty(new String[] { "Note 1", "Note 2", "Note 3" }, getObjectClass().getPropertyClass(OwDummyFileObjectClass.NOTES_PROPERTY)));

        // complex person property
        m_PropertyMap.put(OwDummyFileObjectClass.COMPLEX_PERSON_PROPERTY, new OwStandardProperty(OwStandardObjectClass.createInitialNullValue(getObjectClass().getPropertyClass(OwDummyFileObjectClass.COMPLEX_PERSON_PROPERTY), false), getObjectClass()
                .getPropertyClass(OwDummyFileObjectClass.COMPLEX_PERSON_PROPERTY)));
        // complex person array property
        m_PropertyMap.put(OwDummyFileObjectClass.COMPLEX_PERSON_ARRAY_PROPERTY, new OwStandardProperty(OwStandardObjectClass.createInitialNullValue(getObjectClass().getPropertyClass(OwDummyFileObjectClass.COMPLEX_PERSON_ARRAY_PROPERTY), false),
                getObjectClass().getPropertyClass(OwDummyFileObjectClass.COMPLEX_PERSON_ARRAY_PROPERTY)));

        // Image Property
        m_PropertyMap.put(OwDummyFileObjectClass.IMAGE_PROPERTY, new OwStandardProperty(new OwDummyImageFileObject(getNetwork(), "jens.jpg"), getObjectClass().getPropertyClass(OwDummyFileObjectClass.IMAGE_PROPERTY)));

        // ID property
        m_PropertyMap.put(OwDummyFileObjectClass.ID_PROPERTY, new OwStandardProperty(Integer.valueOf(100), getObjectClass().getPropertyClass(OwDummyFileObjectClass.ID_PROPERTY)));

        // evaluation points property
        m_PropertyMap.put(OwDummyFileObjectClass.EVALUATION_POINTS_PROPERTY, new OwStandardProperty(null, getObjectClass().getPropertyClass(OwDummyFileObjectClass.EVALUATION_POINTS_PROPERTY)));

        // first name property
        m_PropertyMap.put(OwDummyFileObjectClass.FIRST_NAME_PROPERTY, new OwStandardProperty("Jens", getObjectClass().getPropertyClass(OwDummyFileObjectClass.FIRST_NAME_PROPERTY)));

        // last name property
        m_PropertyMap.put(OwDummyFileObjectClass.LAST_NAME_PROPERTY, new OwStandardProperty("Dahl", getObjectClass().getPropertyClass(OwDummyFileObjectClass.LAST_NAME_PROPERTY)));
        // address property
        m_PropertyMap.put(OwDummyFileObjectClass.ADDRESS_PROPERTY, new OwStandardProperty("Hauptstraße 14, 91074 Herzogenaurach", getObjectClass().getPropertyClass(OwDummyFileObjectClass.ADDRESS_PROPERTY)));
        // gender property
        m_PropertyMap.put(OwDummyFileObjectClass.MALE_PROPERTY, new OwStandardProperty(Boolean.TRUE, getObjectClass().getPropertyClass(OwDummyFileObjectClass.MALE_PROPERTY)));
        // department property
        m_PropertyMap.put(OwDummyFileObjectClass.DEPT_PROPERTY, new OwStandardProperty("Management", getObjectClass().getPropertyClass(OwDummyFileObjectClass.DEPT_PROPERTY)));
        // birthday property
        m_PropertyMap.put(OwDummyFileObjectClass.DATE_OF_BIRTH_PROPERTY, new OwStandardProperty(new Date(), getObjectClass().getPropertyClass(OwDummyFileObjectClass.DATE_OF_BIRTH_PROPERTY)));
        // fax property
        m_PropertyMap.put(OwDummyFileObjectClass.TEL_PROPERTY, new OwStandardProperty("09132 745 9754", getObjectClass().getPropertyClass(OwDummyFileObjectClass.TEL_PROPERTY)));
        // tel property
        m_PropertyMap.put(OwDummyFileObjectClass.FAX_PROPERTY, new OwStandardProperty("09132 7362 70", getObjectClass().getPropertyClass(OwDummyFileObjectClass.FAX_PROPERTY)));
        // intern property
        m_PropertyMap.put(OwDummyFileObjectClass.STATUS_PROPERTY, new OwStandardProperty(Boolean.FALSE, getObjectClass().getPropertyClass(OwDummyFileObjectClass.STATUS_PROPERTY)));
        //image control
        m_PropertyMap.put(OwDummyFileObjectClass.IMAGE_CONTROL_PROPERTY, new OwStandardProperty("New", getObjectClass().getPropertyClass(OwDummyFileObjectClass.IMAGE_CONTROL_PROPERTY)));
        // Comment property
        m_PropertyMap.put(OwDummyFileObjectClass.COMMENT_PROPERTY, new OwStandardProperty("Comment...", getObjectClass().getPropertyClass(OwDummyFileObjectClass.COMMENT_PROPERTY)));
        // Nationality property
        m_PropertyMap.put(OwDummyFileObjectClass.NATIONALITY_PROPERTY, new OwStandardProperty("de", getObjectClass().getPropertyClass(OwDummyFileObjectClass.NATIONALITY_PROPERTY)));
        // BirthPlace property
        m_PropertyMap.put(OwDummyFileObjectClass.BIRTHPLACE_PROPERTY, new OwStandardProperty("Erlangen", getObjectClass().getPropertyClass(OwDummyFileObjectClass.BIRTHPLACE_PROPERTY)));
        // Personal number property
        m_PropertyMap.put(OwDummyFileObjectClass.PERSONALNUMMER_PROPERTY, new OwStandardProperty("F12345", getObjectClass().getPropertyClass(OwDummyFileObjectClass.PERSONALNUMMER_PROPERTY)));

        // standard name property
        m_PropertyMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), new OwStandardProperty(getName(), OwResource.m_ObjectNamePropertyClass));

        // m_PropertyMap.put(OwBREditDocumentProperties.PROPERTY_LIEGENSCHAFTSORT,new OwStandardProperty("Liegenschaftsort 1",getObjectClass().getPropertyClass(OwBREditDocumentProperties.PROPERTY_LIEGENSCHAFTSORT)));
        // m_PropertyMap.put(OwBREditDocumentProperties.PROPERTY_LIEGENSCHAFT,new OwStandardProperty("Liegenschaft 1",getObjectClass().getPropertyClass(OwBREditDocumentProperties.PROPERTY_LIEGENSCHAFT)));
        // m_PropertyMap.put(OwBREditDocumentProperties.PROPERTY_GEBAEUDE,new OwStandardProperty("Gebäude 1",getObjectClass().getPropertyClass(OwBREditDocumentProperties.PROPERTY_GEBAEUDE)));
        // m_PropertyMap.put(OwBREditDocumentProperties.PROPERTY_MASSNAHME,new OwStandardProperty("Massnahme 1",getObjectClass().getPropertyClass(OwBREditDocumentProperties.PROPERTY_MASSNAHME)));

    }

    /** get the children of the object, does NOT cache the returned object
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from DMS system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        boolean fDirectory = false;
        boolean fFile = false;
        boolean fLink = false;
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                    fFile = true;
                    break;

                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                    fDirectory = true;
                    break;

                case OwObjectReference.OBJECT_TYPE_LINK:
                    fLink = true;
                    break;
            }
        }

        OwStandardObjectCollection children = new OwStandardObjectCollection();
        Set<String> uniqueNames = new HashSet<String>();
        if (fFile || fDirectory)
        {
            // get containees if not already done
            File[] files = m_File.listFiles();

            // create Object for each file entry
            // create Object for each file entry
            int len = files.length;
            if (files.length > iMaxSize_p)
            {
                len = iMaxSize_p;
                // signal overflow
                children.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
            }

            for (int i = 0; i < files.length; i++)
            {
                // filter for requested type
                if ((files[i].isDirectory() && fDirectory) || (files[i].isFile() && fFile))
                {
                    if (len-- == 0)
                    {
                        break;
                    }

                    // wrap new File object around file
                    OwDummyFileObject FileOb = (OwDummyFileObject) createFileObject(files[i]);

                    // add it to the return list
                    children.add(FileOb);
                    uniqueNames.add(FileOb.getName());
                }
            }
        }

        if (fLink)
        {
            OwObjectCollection parents = getParents();

            Iterator parentsIt = parents.iterator();
            while (parentsIt.hasNext())
            {
                OwObject parent = (OwObject) parentsIt.next();
                OwObjectLink parentLink = OwDummyObjectFactory.getInstance().create(getNetwork(), OwDummyObjectLinkClass.getNamedInstance("Parent"), "Parent of " + getName(), this, parent);
                OwObjectLink parentChildLink = OwDummyObjectFactory.getInstance().create(getNetwork(), OwDummyObjectLinkClass.getNamedInstance("Child"), "Child of " + parent.getName(), parent, this);
                children.add(parentLink);
                children.add(parentChildLink);
                uniqueNames.add(parentChildLink.getName());
                uniqueNames.add(parentLink.getName());
            }

            if (hasChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER }, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                OwObjectCollection linkedChildren = getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, -1, 0, null);
                Iterator linkedChildrenIt = linkedChildren.iterator();
                while (linkedChildrenIt.hasNext())
                {
                    OwObject child = (OwObject) linkedChildrenIt.next();
                    OwObjectLink childLink = OwDummyObjectFactory.getInstance().create(getNetwork(), OwDummyObjectLinkClass.getNamedInstance("Child"), "Child of " + getName(), this, child);
                    OwObjectLink childParentLink = OwDummyObjectFactory.getInstance().create(getNetwork(), OwDummyObjectLinkClass.getNamedInstance("Parent"), "Parent of " + child.getName(), child, this);
                    children.add(childLink);
                    children.add(childParentLink);
                }
            }

            OwObjectLink self = OwDummyObjectFactory.getInstance().create(getNetwork(), OwDummyObjectLinkClass.getNamedInstance("Self"), getName() + " itself", this, this);
            children.add(self);
            uniqueNames.add(self.getName());

        }

        if (fFile)
        {
            //simulate no-content objects
            Random random = new Random(System.currentTimeMillis());

            int objectCount = random.nextInt(10);

            for (int i = 0; i < objectCount; i++)
            {
                String name = "" + random.nextInt(Integer.MAX_VALUE);
                while (uniqueNames.contains(name))
                {
                    name = "" + random.nextInt(Integer.MAX_VALUE);
                }

                File file = new File(m_File, name);
                OwFileObject fileObject = createFileObject(file);
                children.add(fileObject);
                uniqueNames.add(fileObject.getName());
            }

        }

        // set size attribute
        children.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(children.size()));

        return children;
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        boolean hasChilds = super.hasChilds(iObjectTypes_p, iContext_p);

        if (!hasChilds)
        {
            for (int i = 0; i < iObjectTypes_p.length; i++)
            {
                if (OwObjectReference.OBJECT_TYPE_LINK == iObjectTypes_p[i])
                {
                    hasChilds = true;
                    break;
                }

            }
        }

        return hasChilds;

    }

    /** overridable factory method
     * 
     * @param file_p
     * @return the newly created {@link OwFileObject}
     * @throws Exception 
     */
    protected OwFileObject createFileObject(File file_p) throws Exception
    {
        return OwDummyObjectFactory.getInstance().create(getNetwork(), file_p);
    }

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getVersion()
     */
    public OwVersion getVersion() throws Exception
    {
        if (hasVersionSeries())
        {
            return this;
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getVersionSeries()
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        if (hasVersionSeries())
        {
            return this;
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#canChangeClass()
     */
    public boolean canChangeClass() throws Exception
    {
        // simulate change class only for text files
        return getMIMEType().equalsIgnoreCase("text/plain");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#changeClass(java.lang.String, com.wewebu.ow.server.ecm.OwPropertyCollection, com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDummyFileObject.changeClass: strNewClassName_p: " + strNewClassName_p);
        }

        Iterator it = properties_p.values().iterator();
        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDummyFileObject.changeClass: OwProperty: " + prop.getPropertyClass().getClassName() + ", getValue: " + prop.getValue());
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // version/series interface implementation
    private int m_major = 1;
    private int m_minor = 0;
    private boolean m_fCheckedOut = false;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#hasVersionSeries()
     */
    public boolean hasVersionSeries() throws Exception
    {
        return m_File.isFile();
    }

    public OwVersion getLatest() throws Exception
    {
        return this;
    }

    public OwObject getObject(OwVersion version_p) throws Exception
    {
        return this;
    }

    public OwVersion getReleased() throws Exception
    {
        return this;
    }

    public OwVersion getReservation() throws Exception
    {
        return this;
    }

    public Collection getVersions(Collection properties_p, OwSort sort_p, int iMaxSize_p) throws Exception
    {
        if (hasVersionSeries())
        {
            Collection ret = new LinkedList();
            ret.add(this);

            return ret;
        }
        else
        {
            return null;
        }
    }

    public String getId()
    {
        return "dummyVersionSeries";
    }

    public boolean canCancelcheckout(int context_p) throws Exception
    {
        return m_fCheckedOut;
    }

    public boolean canCheckin(int context_p) throws Exception
    {
        return m_fCheckedOut;
    }

    public boolean canCheckout(int context_p) throws Exception
    {
        return (!m_fCheckedOut);
    }

    public boolean canDemote(int context_p) throws Exception
    {
        return m_minor == 0;
    }

    public boolean canPromote(int context_p) throws Exception
    {
        return m_minor > 0;
    }

    public boolean canSave(int context_p) throws Exception
    {
        return m_fCheckedOut;
    }

    public void cancelcheckout() throws Exception
    {
        m_fCheckedOut = false;
    }

    public void checkin(boolean fPromote_p, Object mode_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, boolean fOverwriteContent_p, String strMimeType_p,
            String strMimeParameter_p) throws Exception
    {
        m_fCheckedOut = false;

        if (fPromote_p)
        {
            m_major++;
            m_minor = 0;
        }
        else
        {
            m_minor++;
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDummyFileObject.checkin: strMimeType_p: " + strMimeType_p + ", fPromote_p: " + fPromote_p + ", strObjectClassName_p: " + strObjectClassName_p + ", fOverwriteContent_p: " + fOverwriteContent_p + ", mode_p: " + mode_p);
        }

        Iterator it = properties_p.values().iterator();
        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDummyFileObject.checkin: OwProperty: " + prop.getPropertyClass().getClassName() + ", getValue: " + prop.getValue());
            }
        }

        it = content_p.getContentTypes().iterator();
        while (it.hasNext())
        {
            Integer ctype = (Integer) it.next();
            for (int i = 0; i < content_p.getPageCount(); i++)
            {
                OwContentElement celem = content_p.getContentElement(ctype.intValue(), 0);
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwDummyFileObject.checkin: OwContentCollection getContentRepresentation: " + celem.getContentRepresentation() + ", celem.getMIMEType: " + celem.getMIMEType() + ", celem.getContentFilePath: "
                            + celem.getContentFilePath());
                }
            }
        }

    }

    public void checkout(Object mode_p) throws Exception
    {
        m_fCheckedOut = true;
    }

    public void demote() throws Exception
    {
        m_minor = 1;

        if (m_major > 0)
        {
            m_major--;
        }
    }

    public boolean equals(OwVersion version_p) throws Exception
    {
        return version_p == this;
    }

    public String getVersionInfo() throws Exception
    {
        return String.valueOf(m_major) + "." + String.valueOf(m_minor);
    }

    public int[] getVersionNumber() throws Exception
    {
        return new int[] { m_major, m_minor };
    }

    public boolean isCheckedOut(int context_p) throws Exception
    {
        return m_fCheckedOut;
    }

    public boolean isLatest(int context_p) throws Exception
    {
        return true;
    }

    public boolean isMajor(int context_p) throws Exception
    {
        return (m_minor == 0);
    }

    public boolean isReleased(int context_p) throws Exception
    {
        return (m_minor == 0);
    }

    public void promote() throws Exception
    {
        if (m_minor > 0)
        {
            m_major++;
        }

        m_minor = 0;
    }

    public void save(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDummyFileObject.save: strMimeType_p: " + strMimeType_p);
        }

        Iterator it = content_p.getContentTypes().iterator();
        while (it.hasNext())
        {
            Integer ctype = (Integer) it.next();

            OwContentElement celem = content_p.getContentElement(ctype.intValue(), 1);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDummyFileObject.save: OwContentCollection getContentRepresentation: " + celem.getContentRepresentation() + ", celem.getMIMEType: " + celem.getMIMEType() + ", celem.getContentFilePath: " + celem.getContentFilePath());
            }
        }
    }

    /** just a dummy work item reference to simulate case objects
     */
    private class OwDummyWorkitemObjectReference implements OwObjectReference
    {

        /** get a instance from this reference
         * 
         * @return OwObject or throws OwObjectNotFoundException
         * @throws Exception, OwObjectNotFoundException
         */
        public OwObject getInstance() throws Exception
        {
            throw new OwObjectNotFoundException("OwDummyFileObject$OwDummyWorkitemObjectReference.getInstance: Could not create instance for DMSID = " + getDMSID());
        }

        /** get the ID / name identifying the resource the object belongs to
         * 
         * @return String ID of resource or throws OwObjectNotFoundException
         * @throws Exception, OwObjectNotFoundException
         * @see OwResource
         */
        public String getResourceID() throws Exception
        {
            throw new OwObjectNotFoundException("OwDummyFileObject.getResourceID: Resource Id not found");
        }

        public String getDMSID() throws Exception
        {
            return OwDummyWorkitemRepository.DMS_PREFIX;
        }

        public String getID()
        {
            return null;
        }

        public String getMIMEParameter() throws Exception
        {
            return null;
        }

        public String getMIMEType() throws Exception
        {
            return "ow_workitem/item";
        }

        public String getName()
        {
            return "CASE Object";
        }

        public int getPageCount() throws Exception
        {
            return 0;
        }

        public int getType()
        {
            return OwObjectReference.OBJECT_TYPE_WORKITEM;
        }

        public boolean hasContent(int iContext_p) throws Exception
        {
            return false;
        }

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        LOG.info("OwDummyFileObject.setProperties: mode_p = " + mode_p);

        super.setProperties(properties_p, mode_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        LOG.info("OwDummyFileObject.setProperties: no mode");
        super.setProperties(properties_p);
    }

    public OwObjectReference getWorkitem(int iIndex_p) throws Exception
    {
        return new OwDummyWorkitemObjectReference();
    }

    public int getWorkitemCount() throws Exception
    {
        return 1;
    }

    /**
     * @throws OwStatusContextException if not implemented 
     * 
     * @see com.wewebu.ow.server.ecm.OwVersion#getCheckedOutUserID(int)
     */
    public String getCheckedOutUserID(int context_p) throws Exception
    {
        throw new OwStatusContextException("OwDummyFileObject.getCheckOutUserID: Not implemented or Not supported.");
    }

    /**
     * @throws OwStatusContextException if not implemented
     * 
     * @see com.wewebu.ow.server.ecm.OwVersion#isMyCheckedOut(int)
     */
    public boolean isMyCheckedOut(int context_p) throws Exception
    {
        throw new OwStatusContextException("OwDummyFileObject.isMyCheckedOut: Not implemented or Not supported.");
    }

    public int hashCode()
    {

        return m_File.hashCode();
    }

    public boolean equals(Object o_p)
    {
        if (o_p == this)
        {
            return true;
        }
        if (!(o_p instanceof OwDummyFileObject))
        {
            return false;
        }
        OwDummyFileObject o2 = (OwDummyFileObject) o_p;

        return m_File != null ? m_File.equals(o2.m_File) : false;

    }

    @Override
    public boolean exists()
    {
        //simulate no-coontent file : the object exists without content if the parent exists 
        return super.exists() || m_File.getParentFile().exists();
    }
}