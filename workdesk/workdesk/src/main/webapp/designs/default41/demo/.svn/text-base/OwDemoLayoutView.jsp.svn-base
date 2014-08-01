<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.util.*,com.wewebu.ow.server.plug.owdemo.owmain.*"
	autoFlush="true"
	pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"
	language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwMainContent">
	<table bgcolor="<%=((OwDemoLayoutView) m_View).getBGColor()%>">
		<tr>
			<td colspan="4">
				<h1>
					Class: OwDemoView
				</h1>
			</td>
		</tr>
		<tr>
			<td colspan="3" width="85%">
				<table>
					<tr>
						<td colspan="3">
							<h2>
								Class: OwDemoLayoutView
							</h2>
						</td>
					</tr>
					<tr>
						<td width="33%">
							<%
							    m_View.renderRegion(out, OwDemoLayoutView.MENU_REGION);
							%>
						</td>
						<td width="33%">
							<%
							    m_View.renderRegion(out, OwDemoLayoutView.DISPLAY_REGION);
							%>
						</td>
						<td width="33%">
							<%
							    m_View.renderRegion(out, OwDemoLayoutView.LABEL_REGION);
							%>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<table bgcolor="#B9D8D2">
								<tr>
									<td width="33%" class="align-center">
										<img src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/arrow_down.gif" alt="<%=m_View.getContext().localize("image.arrow_down.gif","Image: Arrow down")%>" title="<%=m_View.getContext().localize("image.arrow_down.gif","Image: Arrow down")%>"/>
										<br />
										Set color value:
										<br />
										<strong>setColor()</strong>
									</td>
									<td width="33%" class="align-center">
										<img src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/arrow_up.gif" alt="<%=m_View.getContext().localize("image.arrow_up.gif","Image: Arrow up")%>" title="<%=m_View.getContext().localize("image.arrow_up.gif","Image: Arrow up")%>"/>
										<br />
										Get color value:
										<br />
										<strong>getColor()</strong>
									</td>

									<td width="33%">
										&nbsp;
									</td>

								</tr>
							</table>

						</td>

					</tr>

					<tr>
						<td colspan="3">
							<table bgcolor="#FFCC00" bordercolor="#FFCC00">
								<tr>
									<td>
										Class:
										<strong>OwDemoDocument</strong>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>

			</td>

			<td width="15%">
				Update event:
				<br />
				<strong>setColor()</strong>
				<br />
				<div>
					<img src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/arrow_left.gif" alt="<%=m_View.getContext().localize("image.arrow_left.gif","Image: Arrow left")%>" title="<%=m_View.getContext().localize("image.arrow_left.gif","Image: Arrow left")%>" width="110" height="130" >
				</div>
			</td>

		</tr>
	</table>
</div>

