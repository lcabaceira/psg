<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	autoFlush="false" errorPage="adaptorError.jsp" language="java"%>
 
<jsp:useBean id="theApp" scope="request"
	class="com.wewebu.ow.server.ui.OwWebApplication">
</jsp:useBean>
<jsp:useBean id="adaptor" scope="session"
	class="com.wewebu.ow.server.ui.OwAdaptor">
</jsp:useBean>

<%
    adaptor.initialize(request, theApp);
%>

<%
    adaptor.processRequest(request, response, theApp);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="js/extjs/resources/css/ext-all.css"
			type="text/css" />
	    <link rel="stylesheet" href="js/extjs/resources/css/xtheme-gray.css"
            type="text/css" />
		<link rel="stylesheet" href="designs/default41/css/ow.css"
			type="text/css" />

		<script src="js/extjs/adapter/ext/ext-base.js" type="text/javascript">
</script>

		<script src="js/extjs/ext-all.js" type="text/javascript">
</script>

		<script src="js/owadaptor.js" type="text/javascript">
</script>
		<script type="text/javascript">
		Ext.onReady(configJS);
		
		function configJS() {
			OwAdaptor.config={
						m_baseURL:'<%=adaptor.getBaseURL(request)%>',
						m_adaptorsData:<%=adaptor.getAvailableAdators()%>,
						m_selectedAdaptor:'<%=adaptor.getSelectedAdaptor()%>',
						m_editableAdaptors:'<%=adaptor.getEditableAdaptors()%>',
						m_editNodes:<%=adaptor.getEditNodes(adaptor.getSelectedAdaptor(),null)%>
					};
			OwAdaptor.init();		
		}

		
	</script>

		<title>Alfresco Workdesk - Adaptor Select</title>

	</head>
	<body class='owBody' >

		<div id='OwMainLayout'>

			<div id='OwMainLayout_HEADER'>
				<BR />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<img src='designs/default41/images/OwGuiLayout/OW_Logo1.png' alt='Workdesk Logo' title='Workdesk Logo' >
			</div>

			<form id="selectionFormId" name="adaptor_selection" method="post" action="adaptor.jsp">
				<div class='OwAdaptorSelection'>

					<div class="OwAdaptorSelectionTitle">
						<div id="mainPanel"></div>
					</div>
				</div>
			</form>
		</div>


	</body>
</html>