package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for object class descriptions.
 * Class descriptions are defined by the ECM System, they contain information about
 * the object type and properties.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwObjectClass
{
    /** operation mode @see {@link OwObjectClass#getModes(int)} */
    public static final int OPERATION_TYPE_UNDEF = 0;

    /** operation mode @see {@link OwObjectClass#getModes(int)} */
    public static final int OPERATION_TYPE_SET_PROPERTIES = 1;

    /** operation mode @see {@link OwObjectClass#getModes(int)} */
    public static final int OPERATION_TYPE_CHECKIN = 2;

    /** operation mode @see {@link OwObjectClass#getModes(int)} */
    public static final int OPERATION_TYPE_CHECKOUT = 3;

    /** operation mode @see {@link OwObjectClass#getModes(int)} */
    public static final int OPERATION_TYPE_CREATE_NEW_OBJECT = 4;

    /** get type of associated object
     * @return the type of the object
     * @see OwObjectReference
     */
    public abstract int getType();

    /** get the child classes of this class if we deal with a class tree
     *
     * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     *
     * @return List of child classes, or null if no class tree is supported
     */
    public abstract java.util.List getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception;

    /** get the child classes of this class if we deal with a class tree
    *
     * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
    * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
    *
    * @return Map of child class symbolic name keys, mapped to display names, or null if no class tree is supported
    */
    public abstract java.util.Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception;

    /** check if children are available
    *
    * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
    * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
    * @param context_p OwStatusContextDefinitions
    * 
    * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
    */
    public abstract boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception;

    /** get the symbolic name of the class
     *
     * @return class name
     */
    public abstract String getClassName();

    /** get the displayable name of the type as defined by the ECM System
     *
     * @param locale_p Local to use
     * @return type displayable name of property
     */
    public abstract String getDisplayName(java.util.Locale locale_p);

    /** get a map of the available property class descriptions 
     *
     * @param strClassName_p Name of class
     *
     * @return OwPropertyClass instance
     */
    public abstract OwPropertyClass getPropertyClass(String strClassName_p) throws Exception;

    /** get a list of the available property class descriptions names
     *
     * @return string Collection of String Names
     */
    public abstract java.util.Collection getPropertyClassNames() throws Exception;

    /** get the name of the name property
     * @return String name of the name property
     */
    public abstract String getNamePropertyName() throws Exception;

    /** check, if new object instances can be created for this class
     *
     * @return true, if object can be created
     */
    public abstract boolean canCreateNewObject() throws Exception;

    /** check if a version series object class is available, i.e. the object is versionable
     *
     * @return true if object class is versionable
     */
    public abstract boolean hasVersionSeries() throws Exception;

    /** get the available modes for operations like checkin, createNewObject, setProperties
    * 
    * @param operation_p int as defined with OPERATION_TYPE_...
    *
    * @return List of OwEnum objects, or null if no modes are defined for the given operation
    */
    public abstract java.util.List getModes(int operation_p) throws Exception;

    /** retrieve a description of the object class
     *
     * @param locale_p Local to use
     * @return String Description of the object class
     */
    public abstract String getDescription(java.util.Locale locale_p);

    /** check if class is visible to the user
     *
     * @return true if property is visible to the user
     */
    public abstract boolean isHidden() throws Exception;

    /** get the parent class of this class 
     *
     * @return OwObjectClass parent or null if topmost class
     */
    public abstract OwObjectClass getParent() throws Exception;
}