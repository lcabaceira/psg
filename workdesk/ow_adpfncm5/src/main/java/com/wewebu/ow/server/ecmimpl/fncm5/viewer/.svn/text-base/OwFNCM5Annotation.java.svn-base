package com.wewebu.ow.server.ecmimpl.fncm5.viewer;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.constants.SecurityPrincipalType;
import com.filenet.api.core.Factory;
import com.filenet.api.security.Permission;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Utility Class that represents a Daeja Annotation.
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
public class OwFNCM5Annotation
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Annotation.class);

    protected static final String F_ANNOTATEDID = "F_ANNOTATEDID";
    protected static final String F_ID = "F_ID";
    protected static final String MOD_CONTENT = "mod_content";
    protected static final String MOD_PROPS = "mod_props";
    protected static final String NAME = "name";
    protected static final String OWNER_CTRL = "owner_ctrl";
    protected static final String PERMISSION = "permission";
    protected static final String PERMISSIONS = "permissions";
    protected static final String PROMOTE_VER = "promote_ver";
    protected static final String PROPDESC = "PropDesc";

    protected static final String STATE = "STATE";
    protected static final String TYPE = "type";
    protected static final String VIEW_CONTENT = "view_content";
    protected static final String VIEW_PROPS = "view_props";
    protected static final String ALLOW = "allow";

    protected static final String ANNOTATION = "annotation";
    protected static final String CE = "ce";
    protected static final String CHANGE = "change";
    protected static final String CLIENTPERMISSION = "clientpermission";
    protected static final String DELETE = "delete";
    protected static final String EDIT = "edit";
    protected static final String ENCODING = "Encoding";
    protected static final String F_TEXT = "F_TEXT";
    protected static final String ID = "id";
    protected static final String LEVEL = "level";
    protected static final String NONE = "none";
    protected static final String OBJECTID = "objectid";
    protected static final String OBJECTTYPE = "objecttype";
    protected static final String ONE = "one";
    protected static final String SECURITY = "security";
    protected static final String SECURITYOBJECT = "securityobject";
    protected static final String SYSTEMTYPE = "systemtype";
    protected static final String UNICODE = "unicode";
    protected static final String USER = "user";
    protected static final String VIEW = "view";

    protected static final int FULL_CONTROL_ANNOTATION_AS_INT = AccessRight.DELETE_AS_INT | AccessRight.WRITE_AS_INT | AccessRight.WRITE_ACL_AS_INT | AccessRight.READ_ACL_AS_INT | AccessRight.VIEW_CONTENT_AS_INT | AccessRight.WRITE_OWNER_AS_INT
            | AccessRight.CREATE_INSTANCE_AS_INT | AccessRight.READ_AS_INT;

    /** P8 permission list of the annotation */
    private AccessPermissionList permissions;
    /** state of the annotation - whether this annotation has to be added, modified, deleted */
    private String m_state;

    /** root element of the annotation doc */
    private Element m_rootElement;
    /** unique identifier of the annotation */
    private String m_id;

    /**
     * constructor called when saving annotations
     * @param root_p
     * @param context_p 
     */
    public OwFNCM5Annotation(Element root_p, OwNetworkContext context_p)
    {
        try
        {
            readPermissions(root_p);

            m_rootElement = root_p;

            // initialize the only data that uniquely identifies this
            // annotation - the F_ANNOTATEDID
            readAnnotationID();

        }
        catch (Exception e)
        {
            LOG.error("Cannot create the annotation object...", e);
        }
    }

    /**
     * constructor Called when reading Annotations.
     * @param xmlData_p
     * @param mask_p
     * @param userName_p
     * @param context_p 
     */
    public OwFNCM5Annotation(Document xmlData_p, int mask_p, String userName_p, OwNetworkContext context_p)
    {
        try
        {
            // get content
            m_rootElement = xmlData_p.getDocumentElement();

            // construct up-to-date security XML which can be read by the INI parser
            createCESecurityXML(mask_p, userName_p);

            // convert the encoding from UTF16 to something Daeja can understand (UTF-8).
            // This method updates the xmlContent
            fixTextEncoding();
        }
        catch (Exception e)
        {
            LOG.error("Cannot create the annotation object...", e);
        }
    }

    /**
     * 
     * @param mask_p
     * @param userName_p
     */
    private void createCESecurityXML(int mask_p, String userName_p)
    {
        // Generate the XML
        // create security node
        Element secNode = OwFNCM5AnnotationXMLUtil.createNodeNamed(m_rootElement, SECURITY);

        // Adding security object node
        Element secObjNode = OwFNCM5AnnotationXMLUtil.createNodeNamed(secNode, SECURITYOBJECT);
        secObjNode.setAttribute(SYSTEMTYPE, CE);
        secObjNode.setAttribute(OBJECTID, ONE);
        secObjNode.setAttribute(OBJECTTYPE, ANNOTATION);
        secObjNode.setAttribute(CLIENTPERMISSION, CHANGE);

        // create permission node
        Element perm1 = OwFNCM5AnnotationXMLUtil.createNodeNamed(secObjNode, PERMISSION);
        perm1.setAttribute(ID, "1");
        perm1.setAttribute(NAME, userName_p);
        perm1.setAttribute(TYPE, USER);

        // now set the permission attributes
        boolean canDelete = (mask_p & FULL_CONTROL_ANNOTATION_AS_INT) == FULL_CONTROL_ANNOTATION_AS_INT;
        //AccessRight.MINOR_VERSION_AS_INT | AccessRight.MAJOR_VERSION_AS_INT
        boolean canEdit = (mask_p & AccessRight.MINOR_VERSION_AS_INT) == AccessRight.MINOR_VERSION_AS_INT;
        //AccessRight.VIEW_CONTENT_AS_INT | AccessLevel.VIEW_AS_INT
        boolean canView = (mask_p & AccessRight.VIEW_CONTENT_AS_INT) == AccessRight.VIEW_CONTENT_AS_INT;

        if (canDelete)
        {
            perm1.setAttribute(LEVEL, DELETE);
        }
        else if (canEdit)
        {
            perm1.setAttribute(LEVEL, EDIT);
        }
        else if (canView)
        {
            perm1.setAttribute(LEVEL, VIEW);
        }
        else
        {
            perm1.setAttribute(LEVEL, NONE);
        }
    }

    /**
     * read the permissions from the document
     * @param xmlContent_p
     */
    @SuppressWarnings("unchecked")
    private void readPermissions(Element xmlContent_p)
    {
        // read the status of the annotation (add, delete, modify)
        m_state = xmlContent_p.getAttribute(STATE);

        // get a permissions empty object from P8  
        permissions = Factory.AccessPermission.createList();

        // now read the permissions in the permission node
        Element permissionsElement = (Element) xmlContent_p.getElementsByTagName(PERMISSIONS).item(0);
        if (permissionsElement == null)
        {
            return;
        }
        NodeList permisionNodes = permissionsElement.getElementsByTagName(PERMISSION);

        for (int i = 0; i < permisionNodes.getLength(); i++)
        {
            Element permissionElement = (Element) permisionNodes.item(i);

            int access = 0;

            // get the permission level
            //int permType = Integer.parseInt(OwXMLDOMUtil.getChildElementText(permissionElement, TYPE));//TODO invest what kind of information is permType
            String permName = OwXMLDOMUtil.getChildElementText(permissionElement, NAME);
            String owner_ctrl = OwXMLDOMUtil.getChildElementText(permissionElement, OWNER_CTRL);
            String promote_ver = OwXMLDOMUtil.getChildElementText(permissionElement, PROMOTE_VER);
            String mod_content = OwXMLDOMUtil.getChildElementText(permissionElement, MOD_CONTENT);
            String mod_props = OwXMLDOMUtil.getChildElementText(permissionElement, MOD_PROPS);
            String view_content = OwXMLDOMUtil.getChildElementText(permissionElement, VIEW_CONTENT);
            String view_props = OwXMLDOMUtil.getChildElementText(permissionElement, VIEW_PROPS);

            if (owner_ctrl.equals(ALLOW))
            {
                access = FULL_CONTROL_ANNOTATION_AS_INT;
            }
            else if (promote_ver.equals(ALLOW))
            {
                access = AccessRight.MINOR_VERSION_AS_INT | AccessRight.MAJOR_VERSION_AS_INT;
            }
            else if (mod_content.equals(ALLOW))
            {
                access = AccessRight.MINOR_VERSION_AS_INT;
            }
            else if (mod_props.equals(ALLOW))
            {
                access = AccessRight.WRITE_AS_INT | AccessRight.READ_ACL_AS_INT | AccessRight.CREATE_INSTANCE_AS_INT | AccessRight.READ_AS_INT;
            }
            else if (view_content.equals(ALLOW))
            {
                access = AccessRight.VIEW_CONTENT_AS_INT;
            }
            else if (view_props.equals(ALLOW))
            {
                access = AccessRight.READ_AS_INT;
            }

            // set the permission to the permissions object
            if (access != 0)
            {
                //                Permission permObj = ObjectFactory.getPermission(access, Permission.TYPE_ALLOW, permName, permType);
                Permission perm = OwFNCM5PermissionHelper.createAccessPermission(permName, AccessType.ALLOW, Integer.valueOf(access), Integer.valueOf(0), PermissionSource.SOURCE_DIRECT, SecurityPrincipalType.UNKNOWN);
                permissions.add(perm);
            }
        }

        // Now remove the permissions nodes
        Node permParent = permissionsElement.getParentNode();
        permParent.removeChild(permissionsElement);
    }

    /**
     * read the annotation ID from XML 
     *
     */
    private void readAnnotationID()
    {
        Element pDescNode = (Element) OwFNCM5AnnotationXMLUtil.getNodeNamed(m_rootElement, PROPDESC);

        m_id = pDescNode.getAttribute(F_ID);

        if (m_id == null || m_id.length() != 38)
        {
            m_id = pDescNode.getAttribute(F_ANNOTATEDID);
        }
    }

    /**
     * getter method for state 
     * @return Returns the state.
     */
    public String getState()
    {
        return m_state;
    }

    /**
     * getter method for the ID 
     * @return the ID 
     */
    public String getID()
    {
        return m_id;
    }

    /**
     * getter method for permissions 
     * @return Returns the permissions.
     */
    public AccessPermissionList getPermissions()
    {
        return permissions;
    }

    /**
     * get content as String
     * @return the content as <code>String</code>
     * @throws TransformerException 
     */
    public String getContent() throws TransformerException
    {
        return OwFNCM5AnnotationXMLUtil.toString(m_rootElement);
    }

    /**
     * fix the encoding
     *
     */
    private void fixTextEncoding()
    {

        Node pDescNode = OwFNCM5AnnotationXMLUtil.getNodeNamed(m_rootElement, PROPDESC);
        Node textNode = OwFNCM5AnnotationXMLUtil.getNodeNamed(pDescNode, F_TEXT);

        if (textNode != null)
        {
            String encoding = OwFNCM5AnnotationXMLUtil.getAttributeNamed(textNode, ENCODING);
            Node firstChild = textNode.getFirstChild();
            if (firstChild != null)
            {
                if ((encoding != null) && (encoding.length() > 0))
                {
                    if (encoding.equalsIgnoreCase(UNICODE))
                    {
                        // The annotation string contains the characters
                        // as hex encoded in unicoded big endian.
                        String sHexEncUniBE = firstChild.getNodeValue();
                        // Convert to a char array of characters in
                        // unicoded big endian.
                        // Each annotation character is 4 characters in
                        // the annotation string.
                        char[] caUniBE = new char[sHexEncUniBE.length() / 4];
                        int count = 0;
                        for (int i = 0; i < sHexEncUniBE.length(); i = i + 4)
                        {
                            String sTemp = sHexEncUniBE.substring(i, i + 4);
                            char cTemp = (char) Integer.parseInt(sTemp, 16);
                            caUniBE[count++] = cTemp;
                        }

                        // Assign the char array with unicode big endian
                        // data to a string.
                        String sUniBE = new String(caUniBE);

                        // Remove the encoding.
                        NamedNodeMap al = textNode.getAttributes();
                        if (al != null)
                        {
                            al.removeNamedItem(ENCODING);
                        }

                        // Save the unicode big endian string to the
                        // node.
                        firstChild.setNodeValue(sUniBE);

                    } // if unicode
                } // if encoding and val are not null
            } // if firstChild is not null
        } // if text node is not null

    }

    /**
     * getter method for rootElement 
     * @return Returns the rootElement.
     */
    public Element getRootElement()
    {
        return m_rootElement;
    }

    /**
     * Needed during creation process of new Annotations, 
     * the content will be extended with new information
     * and also some protocol information will be stripped.
     * @param fID String id of the new created Annotation
     */
    public void prepareContent(String fID)
    {
        if (!getID().equalsIgnoreCase(fID))
        {
            Node propDesc = OwFNCM5AnnotationXMLUtil.getNodeNamed(getRootElement(), PROPDESC);
            NamedNodeMap atts = propDesc.getAttributes();
            Node att = atts.getNamedItem(F_ID);
            att.setNodeValue(fID);
            att = atts.getNamedItem(F_ANNOTATEDID);
            att.setNodeValue(fID);
        }
        if (getRootElement().getAttribute(STATE) != null)
        {
            getRootElement().removeAttribute(STATE);
        }
    }

}
