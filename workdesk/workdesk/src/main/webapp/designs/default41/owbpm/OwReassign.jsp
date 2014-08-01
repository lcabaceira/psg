<%@page
	import="com.wewebu.ow.server.plug.owbpm.plug.*,com.wewebu.ow.server.ui.*,java.util.List,java.util.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.ecm.OwUserInfo,com.wewebu.ow.server.util.OwString"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwBPMReassignDialog m_dialog = (OwBPMReassignDialog) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwPropertyBlockLayout" id="OwMainContent">

	<div class="OwPropertyBlock OwPropertyControl">
		<div class="OwPropertyLabel"><%=m_dialog.getContext().localize("plug.owbpm.OwReassign.reassignitems", "Reassign Items")%>:</div>
		<div class="OwPropertyValue">
<%
				    if (m_dialog.isRegion(OwBPMReassignDialog.OWN_INBOX_REGION))
				    {
%>
						<div class="owInlineControls">
							<input type="radio" id="radio"
								name="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_WEB_KEY)%>"
								value="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_OWN_INBOX_VALUE)%>"
								<%=m_dialog.getCheckedString(OwBPMReassignDialog.DESTINATION_OWN_INBOX_VALUE)%>>
							 <label for="radio"><%=m_dialog.getContext().localize("plug.owbpm.OwReassign.toinbox", "to my inbox")%></label>
						 </div>
<%
				    }
%>
                <div class="owInlineControls">
<%
				    if (m_dialog.isRegion(OwBPMReassignDialog.OTHER_INBOX_REGION))
				    {
%>
					<input type="radio" id="radio1"
							name="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_WEB_KEY)%>"
							value="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_OTHER_INBOX_VALUE)%>"
							<%=m_dialog.getCheckedString(OwBPMReassignDialog.DESTINATION_OTHER_INBOX_VALUE)%>>
						<label for="radio1"><%=m_dialog.getContext().localize("plug.owbpm.OwReassign.touser", "to the inbox of user")%>:</label>
<%
                           List defaultUsers=m_dialog.getDefaultUsers();
                           if (m_dialog.isUserDefaultEnabled() && defaultUsers!=null)
                           {
%>
                        <script type="text/javascript">
                        	function onUserListClick() {
                            	var radio = document.getElementById('radio1');
                        		radio.checked = true;
                        	}
                        </script>
<%
                        List items = new LinkedList();
                        OwUserInfo receiver=m_dialog.getReceiverUser();
					    String receiverId=receiver!=null?receiver.getUserID():null;
 		              	for (Iterator i = defaultUsers.iterator(); i.hasNext();)
                        {
                             OwUserInfo info = (OwUserInfo) i.next();
                             String infoId=info.getUserID();
                             OwComboItem item = new OwDefaultComboItem(infoId,info.getUserLongName());
                             items.add(item);
                        }
 		              	OwComboModel model = new OwDefaultComboModel(false,false,receiverId,items);
 		              	OwComboboxRenderer renderer = ((OwMainAppContext)m_dialog.getContext()).createComboboxRenderer(model,OwBPMReassignDialog.DEFAULT_USER_SELECTION_WEB_KEY,null,null,null);
 		              	renderer.addEvent("onclick","onUserListClick()");
 		              	renderer.addEvent("onfocus","onUserListClick()");
 		              	renderer.addStyleClass("OwInputfield_long",true);
 		              	OwString description=new OwString("plug.owbpm.OwReassign.user.select.title","User name");
 		              	renderer.setFieldDescription(description);
 		              	String userDisplayName=description.getString(m_dialog.getContext().getLocale());
 		              	OwInsertLabelHelper.insertLabelValue(out, userDisplayName, OwBPMReassignDialog.DEFAULT_USER_SELECTION_WEB_KEY);
 		              	renderer.renderCombo(out);

                    } else {
%>
						<input type="text" value="<%=m_dialog.getReceiverUserName()%>" class="OwInputfield_long" readonly>
<%
                    }
%> 
						<input type="button" value="..." onclick="radio1.checked = true;<%=m_dialog.getFormEventFunction("OpenUserDialog", null)%>;">
<%
				    }
%>
                </div>
<%
				    if (m_dialog.isRegion(OwBPMReassignDialog.OTHER_PUBLICBOX_REGION))
				    {
%>
						<div class="owInlineControls">
						<input id="radio2" type="radio"
							name="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_WEB_KEY)%>"
							value="<%=String.valueOf(OwBPMReassignDialog.DESTINATION_OTHER_PUBLICBOX_VALUE)%>"
							<%=m_dialog.getCheckedString(OwBPMReassignDialog.DESTINATION_OTHER_PUBLICBOX_VALUE)%>/>
						 <label for="radio2"><%=m_dialog.getContext().localize("plug.owbpm.OwReassign.togroupbox", "to the group inbox")%>:</label>
					     <script type="text/javascript">
                        	function onQueueListClick() {
                            	var radio = document.getElementById('radio2');
                        		radio.checked = true;
                        	}
                        </script>
<%
						    java.util.Collection publicdestinatios = m_dialog.getPublicDestinations();
						        if (publicdestinatios != null)
						        {
						            List queueItems = new LinkedList();
								    java.util.Iterator it = publicdestinatios.iterator();
						            while (it.hasNext())
						            {
						                String sID = (String) it.next();
						                String sDisplayName = m_dialog.getPublicDestinationDisplayName(sID);
						                OwComboItem item = new OwDefaultComboItem(sID,sDisplayName);
						                queueItems.add(item);
						            }
									OwComboModel queueComboModel = new OwDefaultComboModel(false,false,null,queueItems);
									OwComboboxRenderer queueRenderer = ((OwMainAppContext)m_dialog.getContext()).createComboboxRenderer(queueComboModel,OwBPMReassignDialog.PUBLIC_CONTAINER_NAME_WEB_KEY,null,null,null);
									queueRenderer.addEvent("onclick","onQueueListClick()");
									queueRenderer.addEvent("onfocus","onQueueListClick()");
									OwString queueDescription=new OwString("plug.owbpm.OwReassign.queue.select.title","Queue name");
									queueRenderer.setFieldDescription(queueDescription);
									String queueDisplayName=queueDescription.getString(m_dialog.getContext().getLocale());
									OwInsertLabelHelper.insertLabelValue(out, queueDisplayName, OwBPMReassignDialog.PUBLIC_CONTAINER_NAME_WEB_KEY);
									queueRenderer.renderCombo(out);
							    }
						        else
						        {
%>
						<input onclick="radio2.checked = true;" type="text" value="" name="<%=OwBPMReassignDialog.PUBLIC_CONTAINER_NAME_WEB_KEY%>" class="wInputfield_long">
<%
             				    }
%>
					</div>
<%
				    }
%>
		  </div>
		</div>
		<div class="OwPropertyBlock OwPropertyControl">
			<div class="OwPropertyLabel"><%=m_dialog.getContext().localize("plug.owbpm.OwBPMInsertNote.newNote", "New Note")%>:</div>
			<div class="OwPropertyValue">
<%				m_dialog.renderNote(out); %>
			</div>
		</div>
</div>
