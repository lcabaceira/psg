package com.wewebu.ow.server.settingsimpl;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Settings Property for OwObjectReference Values.
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
public class OwSettingsPropertyObject extends OwSettingsPropertyBaseImpl
{
    /**
     *<p>
     * Class defining a single object reference instance.
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
    public static class OwObjectEntry implements OwObjectReference
    {
        /** get Object name property string
         * @return the name property string of the object
         */
        public String getName()
        {
            return m_strName;
        }

        /** get a instance from this reference
         * 
         * @return OwObject or throws OwObjectNotFoundException
         * @throws Exception, OwObjectNotFoundException
         */
        public OwObject getInstance() throws Exception
        {
            throw new OwObjectNotFoundException("OwSettingsPropertyObject$OwObjectEntry.getInstance: Could not create instance for DMSID = " + getDMSID());
        }

        /** get the ID / name identifying the resource the object belongs to
         * 
         * @return String ID of resource or throws OwObjectNotFoundException
         * @throws Exception, OwObjectNotFoundException
         * @see OwResource
         */
        public String getResourceID() throws Exception
        {
            throw new OwObjectNotFoundException("OwSettingsPropertyObject$OwObjectEntry.getResourceID: Could not get resource Id for DMSID = " + getDMSID());
        }

        /** get Object symbolic name of the object which is unique among its siblings
         *  used for path construction
         *
         * @return the symbolic name of the object which is unique among its siblings
         */
        public String getID()
        {
            return m_strName;
        }

        /** get Object type
         * @return the type of the object
         */
        public int getType()
        {
            return m_iType;
        }

        /** get the ECM specific ID of the Object. 
         *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
         *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
         *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
         *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
         *
         *  The syntax of the ID is up to the ECM Adapter,
         *  but would usually be made up like the following:
         *
         */
        public String getDMSID() throws Exception
        {
            return m_strDmsID;
        }

        /** retrieve the number of pages in the objects
         * @return number of pages
         */
        public int getPageCount() throws Exception
        {
            // Page count unknown
            return 0;
        }

        /** get the MIME Type of the Object
         * @return MIME Type as String
         */
        public String getMIMEType() throws Exception
        {
            return m_strMimetype;
        }

        /** get the additional MIME Parameter of the Object
         * @return MIME Parameter as String
         */
        public String getMIMEParameter() throws Exception
        {
            return "";
        }

        /** check if the object contains a content, which can be retrieved using getContentCollection 
         *
         * @param iContext_p OwStatusContextDefinitions
         *
         * @return boolean true = object contains content, false = object has no content
         */
        public boolean hasContent(int iContext_p) throws Exception
        {
            // unknown
            return false;
        }

        /** the name that should be displayed for the object */
        protected String m_strName;

        /** object type */
        protected int m_iType;

        /** the DMSID of the object */
        protected String m_strDmsID;

        /** object mimetype */
        protected String m_strMimetype;
    };

    /** overridable to create a default value for list properties
    *
    * @return Object with default value for a new list item
    */
    protected Object getDefaultListItemValue()
    {
        return null;
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p)
    {
        // UI Not implemented yet
        return null;
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return oSingleValue_p;
    }

    /** overridable to create a single value for the given node
     * @return Object with value
     */
    protected Object getSingleValue(Node valueNode_p)
    {
        try
        {
            // get entry from XML 
            OwObjectEntry object = new OwObjectEntry();

            OwXMLUtil objectReferenceNode = new OwStandardXMLUtil(valueNode_p);

            object.m_iType = objectReferenceNode.getSafeIntegerValue("type", OwObjectReference.OBJECT_TYPE_UNDEFINED);
            object.m_strDmsID = objectReferenceNode.getSafeTextValue("dmsid", "");
            object.m_strMimetype = objectReferenceNode.getSafeTextValue("mimetype", "");
            object.m_strName = objectReferenceNode.getSafeTextValue("name", "");

            return object;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** create a text subnode for the given node
    * @param valueRootNode_p root Node of the property
    * @param strNodeName_p String node name of new node
    * @param value_p String to append as DOM Node
    * 
     * */
    protected void addSubNode(Node valueRootNode_p, String strNodeName_p, String value_p)
    {
        Element newNode = valueRootNode_p.getOwnerDocument().createElement(strNodeName_p);

        valueRootNode_p.appendChild(newNode);

        newNode.appendChild(valueRootNode_p.getOwnerDocument().createTextNode(value_p));
    }

    /** overridable, return the given value as a DOM Node for serialization
    *
    * @param valueRootNode_p root Node of the property
    * @param value_p Object to append as DOM Node
    *
    */
    protected void appendSingleValueNode(Node valueRootNode_p, Object value_p)
    {
        try
        {
            OwObjectReference object = (OwObjectReference) value_p;

            addSubNode(valueRootNode_p, "type", String.valueOf(object.getType()));
            addSubNode(valueRootNode_p, "dmsid", object.getDMSID());
            addSubNode(valueRootNode_p, "mimetype", object.getMIMEType());
            addSubNode(valueRootNode_p, "name", object.getName());
        }
        catch (Exception e)
        {
            // ignore
        }
    }
}
