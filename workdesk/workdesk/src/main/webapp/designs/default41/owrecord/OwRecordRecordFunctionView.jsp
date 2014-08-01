<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true" 
    import="com.wewebu.ow.server.plug.owrecord.*,
            com.wewebu.ow.server.app.OwMainAppContext,
            com.wewebu.ow.server.app.OwRecordFunction,
            com.wewebu.ow.server.ecm.OwObject,
            com.wewebu.ow.server.ui.OwView,
            com.wewebu.ow.server.util.OwHTMLHelper,
            com.wewebu.ow.server.app.OwDocumentFunction"

%><%
    // get a reference to the calling view
    OwRecordRecordFunctionView m_View = (OwRecordRecordFunctionView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<ul class="OwRecordRecordFunctionView">
<%
       OwMainAppContext m_context = (OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);
       boolean useExtJsForDragAndDrop =m_context.getConfiguration().isUseExtJsForDragAndDrop();

        OwObject folderObject = m_View.getCurrentSubFolderObject();
        OwObject rootObject = m_View.getCurrentRootFolder();

        if (m_View.getRecordFuntionPlugins() != null)
        {
                for (int i=0;i < m_View.getRecordFuntionPlugins().size();i++)
                {
                    OwRecordFunction recordFunctionPlugin = (OwRecordFunction)m_View.getRecordFuntionPlugins().get(i);

                    boolean fEndabled = m_View.getIsPluginEnabled(recordFunctionPlugin);

                    String strEventURL = m_View.getRecordFunctionEventURL(i);
                    String escapedToolTip = OwHTMLHelper.encodeToSecureHTML(recordFunctionPlugin.getTooltip());
                    // render applet, if record function is drop target
                    if(recordFunctionPlugin.isDragDropTarget())
                    {// Skip it
                        continue;
                    }

                    // render active record function, if enabled
                    // render simple icon-link-area, if record function is not drop target
                    if (fEndabled)
                    {
                        %><li><%
                        // icon
                        if(recordFunctionPlugin.getNoEvent())
                        {
                            %><%=recordFunctionPlugin.getBigIconHTML(rootObject,folderObject)%><%
                        }
                        else
                        {
                            %><a title="<%=escapedToolTip%>" class="OwMainMenuItem" href="<%=strEventURL%>" target="_top"><%=recordFunctionPlugin.getBigIconHTML(rootObject,folderObject)%><%
                        }

                        // link
                        if(recordFunctionPlugin.getNoEvent())
                        {
                            %><span title="<%=escapedToolTip%>"><%=recordFunctionPlugin.getLabel(rootObject,folderObject)%></span><%
                        }
                        else
                        {
                            %><span><%=recordFunctionPlugin.getLabel(rootObject,folderObject)%></span></a><%
                        }

                        // register with keyboard as well
                        if(!recordFunctionPlugin.getNoEvent())
                        {
                            ((OwMainAppContext) m_View.getContext()).registerPluginKeyEvent(
                                    recordFunctionPlugin.getPluginID(), strEventURL,
                                    null, recordFunctionPlugin.getDefaultLabel());
                        }
                        %></li><%
                    }
                    else
                    {
                    // === record function is disabled
                        if ( m_View.showDisabledRecordFunctions() )
                        {
                            %><li><%
                            // icon
                            %><%=recordFunctionPlugin.getBigIconHTML(rootObject, folderObject)%><%
                            // link
                            %><span title="<%=escapedToolTip%>"><%=recordFunctionPlugin.getLabel(rootObject, folderObject)%></span><%
                          %></li><%
                        }
                    }    
                }
        }

        // === draw links for each document function
            OwObject docFunctionWorkObject = m_View.getDocumentFunctionWorkobject();

            if ( (m_View.getDocumentFuntionPlugins() != null) && (docFunctionWorkObject != null) )
            {
                out.write("<!-- document functions -->\n");
                for (int i=0;i < m_View.getDocumentFuntionPlugins().size();i++)
                {
                    OwDocumentFunction docFunctionPlugin = (OwDocumentFunction)m_View.getDocumentFuntionPlugins().get(i);
                    
                    if ( ! m_View.getIsPluginEnabled(docFunctionPlugin) )
                        continue;
                    %><li><%
                    %><a title="<%=OwHTMLHelper.encodeToSecureHTML(docFunctionPlugin.getTooltip())%>" class="OwMainMenuItem" href="<%=m_View.getDocumentFunctionEventURL(i)%>" target="_top"><%=docFunctionPlugin.getBigIconHTML(docFunctionWorkObject,null)%><%
                    %><span><%=docFunctionPlugin.getLabel(docFunctionWorkObject,null)%></span></a><%
                    %></li><%
                }
            }
 %></ul>