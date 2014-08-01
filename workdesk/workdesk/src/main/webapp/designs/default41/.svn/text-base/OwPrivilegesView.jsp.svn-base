<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.dmsdialogs.views.OwPrivilegesDocument,
    java.util.Iterator,java.util.*,
    com.wewebu.ow.server.ui.*,
    com.wewebu.ow.server.ecm.*,
    com.wewebu.ow.server.app.*,
    com.wewebu.ow.server.conf.*,
    com.wewebu.ow.server.dmsdialogs.views.OwPrivilegesView"
     autoFlush="true"
%><%
    // get a reference to the calling view
    OwPrivilegesView m_View = (OwPrivilegesView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwPrivilegesDocument document = m_View.getDocument();
%>
<script type="text/javascript">
    function openAddDialog() {
            navigateHREF(window,'<%=m_View.getEventURL("OpenAddDialog", null)%>');
    
    }
</script>
<div class="OwObjectPropertyView">
    <table>
        <thead>
            <tr>
                <th width="80%"/>
                <th width="5%"/>
                <th width="15%"/>
            </tr>
        </thead>
        <tr>
            <td>
                <table class="OwObjectPropertyView_PropList">
                    <thead>
                        <tr class="OwObjectPropertyView_Header">
                            <th class="OwRequired"><img src="<%=m_View.getContext().getDesignURL() + "/images/OwObjectPropertyView/notrequired.gif"%>" alt=""></th>
                            <th class="OwPropertyName"><%=m_View.getContext().localize("jsp.OwPrivilegesView.principal","Principal")%></th>
                            <th class="OwPropertyName"><%=m_View.getContext().localize("jsp.OwPrivilegesView.privileges","Privileges")%></th>
                            <th class="OwRequired">&nbsp;</th>
                        </tr>
                    </thead>
<%
                        Collection<OwPrivilegeSet> privilegeSets = document.getAppliedPrivilegeSets();
                        boolean evenRow = false;
                        for (OwPrivilegeSet privilegeSet : privilegeSets)
                        {
                            evenRow = !evenRow;
                            OwUserInfo principal = privilegeSet.getPrincipal();
                            Collection<OwPrivilege> privileges = privilegeSet.getPrivileges();
%>
                    <tr class="<%=evenRow ? "OwObjectPropertyView_EvenRow" : "OwObjectPropertyView_OddRow"%>">
                        <td/>
                        <td><%=principal.getUserDisplayName()%></td>
                        <td>
                            <table style="width:100%">
<%
                                    StringBuilder removeParameter = new StringBuilder();
                                        for (OwPrivilege privilege : privileges)
                                        {

                                            String displayName = document.displayNameOf(privilege);
                                            String guid = document.privilegeToGuid(privilege);

                                            removeParameter.append(guid);
                                            removeParameter.append("_");
%>
                                <tr>
                                    <td style="width:100%;padding:1px 0px"><%=displayName%></td>
                                    
                                </tr>
<%
                                    }
                                        removeParameter.append("P");
                                        removeParameter.append(principal.getUserName());
%>
                            </table>
                        </td>
<%                        if (!m_View.isReadOnly()) {%>
                        <td><input type="checkbox"
                            name="<%=OwPrivilegesView.PRIVILEGES_PARAM%>"
                            value="<%=removeParameter.toString()%>" /></td>
<%                        }%>
                    </tr>
<%
                        }
%>
                </table>
            </td>
            <td/>
            <td>
<%                if (!m_View.isReadOnly()) {%>
                <table>
                    <tr>
                        <td><input type="button" style="width: 100%"
                            name="addprivilege" value="<%=m_View.getContext().localize("jsp.OwPrivilegesView.add","Add")%>" onClick="openAddDialog()"
                            readonly /></td>
                    </tr>
                    <tr>
                        <td><input type="button" style="width: 100%"
                            name="removeprivilege" value="<%=m_View.getContext().localize("jsp.OwPrivilegesView.remove","Remove")%>" onClick="<%=m_View.getFormEventFunction("RemovePrivileges", null)%>"
                            readonly /></td>
                    </tr>
                </table>
<%                }%>
            </td>
        </tr>
    </table>
</div>