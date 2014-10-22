package org.alfresco.psgextension;

import java.util.Collection;

import org.alfresco.psgutil.Checker;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.*;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.util.*;
import com.wewebu.ow.server.ecm.*;
import com.wewebu.ow.server.log.OwLogCore;

public class OwTakeOwnershipDocumentFunction extends OwDocumentFunction
{
	private static final Logger LOG = OwLogCore.getLogger(OwTakeOwnershipDocumentFunction.class);
	
// === versioning    
    /** minor version number of the used Interface */
    public static int INTERFACE_MINOR_VERSION           = 0;
    /** major version number of the used Interface */
    public static int INTERFACE_MAJOR_VERSION           = 1;
    /** update version number of the implementation */
    public static int IMPLEMENTAION_UPDATE_VERSION      = 0;
    /** minor version number of the implementation */
    public static int IMPLEMENTAION_MINOR_VERSION       = 0;
    /** major version number of the implementation */
    public static int IMPLEMENTAION_MAJOR_VERSION       = 1;
    
    /** set the plugin description node 
     *
     * @param Node_p OwXMLUtil wrapped DOM Node containing the plugindescritption
     * @param Context_p OwMainAppContext
     */
    public void init(OwXMLUtil Node_p,OwMainAppContext Context_p) throws Exception
    {
        super.init(Node_p,Context_p);

    }
    
    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface
     *  for the function plugins to signal refresh events to clients,
     *  can be null if no refresh is needed
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception 
    {
       
        boolean hasLinks = Checker.hasLinks(oObject_p);
        OwProperty assignee = oObject_p.getProperty("D:custom:document.custom:docCode");
        OwBaseUserInfo userInfo = getContext().getCurrentUser();
        if (null != assignee && null != userInfo) {
        	assignee.setValue(userInfo.getUserDisplayName());
        	OwPropertyCollection props = new OwStandardPropertyCollection();
        	props.put("D:custom:document.custom:docCode", assignee);
        	oObject_p.setProperties(props);
        }
        
        if (null != refreshCtx_p) {
        	refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_PROPERTY, null);
        	
        	getContext().postMessage("You are now the owner..." + hasLinks);
        	if (oObject_p != null && oParent_p != null)
        		LOG.debug("Ownership has changed for document "+ oObject_p.getDMSID() + " " + oParent_p.getProperty("OW_ObjectPath"));
        	else
        		LOG.debug("Ownership has changed for document");
        }

    }
    
    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
   public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
   {
	   for (Object oObject_p : objects_p) {
		   if (oObject_p instanceof OwObject)
			   this.onClickEvent((OwObject)oObject_p, oParent_p, refreshCtx_p);
	   }
   }
}
