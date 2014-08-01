<%@ page 
    import="com.wewebu.ow.server.ui.*,java.util.*" 

    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<html>
<head>

    <%-- import standard script --%>
    <script src='/js/common.js' type='text/javascript'></script>

	<style>
	
	.User
	{
	font-size:8pt;
	background:#00ff00;
	height:12px;
	padding: 0;
	border: 0;
	}
	
	.Session
	{
	font-size:8pt;
	background:#ff0000;
	height:12px;
	padding: 0;
	border: 0;
	}
	
	</style>
</head>

<body>

<h3>Statistic usage information for Alfresco Workdesk</h3>

Entries: <%=OwUsageCounter.getStatistics().size()%>
<%
	// get max
	Iterator it = OwUsageCounter.getStatistics().iterator();
	int iMaxSessionCount = 0;
	int iMaxUserCount = 0;
	while ( it.hasNext() )
	{
		OwUsageCounter.OwUsageState state = (OwUsageCounter.OwUsageState)it.next();
		
		if ( iMaxSessionCount < state.getSessionCount() )
			iMaxSessionCount = state.getSessionCount();
			
		if ( iMaxUserCount < state.getUserCount() )
			iMaxUserCount = state.getUserCount();
	}
	
	int iMaxWidth = 500;
%>

<table cellspacing="1" cellpadding="0" border="0" bgcolor="#000000">

	<tr bgcolor="#ffffff" style="font-size:8pt;">
		<td>Date</td>
		<td>Sessions</td>
		<td>Users</td>
		<td>Graph</td>
	</tr>
<%	
	
	// draw graph	
	it = OwUsageCounter.getStatistics().iterator();
	while ( it.hasNext() )
	{
		OwUsageCounter.OwUsageState state = (OwUsageCounter.OwUsageState)it.next();
		
		int iSessionWidh		= (iMaxWidth * state.getSessionCount()) / iMaxSessionCount;
		int iUserWidh			= (iMaxWidth * state.getUserCount()) / iMaxSessionCount;
		int iSessionRestWidh	= iSessionWidh - iUserWidh;
%>

		<tr bgcolor="#ffffff" style="font-size:8pt;">
		
			<td nowrap><%=state.getDate().toString()%></td width="<%=iMaxWidth%>">
			
			<td align="center" nowrap><%=state.getSessionCount()%></td width="<%=iMaxWidth%>">

			<td align="center" nowrap><%=state.getUserCount()%></td width="<%=iMaxWidth%>">

			<td>
				<%--<div style="font-size:8pt;background:#ff0000;height:12px;width:<%=iSessionWidh%>;"><%=state.getSessionCount()%>&nbsp;</div>--%>
				<span class="User" style="padding-right:<%=iUserWidh%>;"></span><span class="Session" style="padding-right:<%=iSessionRestWidh%>;"></span>
			</td>
			
		</tr>
		
<%
	}
%>

</table>

</body>
</html>
