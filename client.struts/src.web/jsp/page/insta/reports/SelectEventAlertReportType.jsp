<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<script language="JavaScript" src="<%=scriptBase%>/calendar.js"></script>
<nested:form action="/eventAlertReport">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr height = "30">
    &nbsp;
    </tr>
    <tr>
        <td class="column-label" align="center" height="10" width="60%">Select Event Type &nbsp;
            <nested:select property="reportEvent" styleClass="select">
                <html:option value="AuditLog">User Level Events</html:option>
                <html:option value="SystemEvent">System Level Events</html:option>
                <html:option value="Email">Email Info</html:option>
                <html:option value="IDL">IDL Utilization</html:option>
            </nested:select>
        </td>
    </tr>
</table>
</nested:form>

<script>
<!--
function selectInputPage() {
	document.forms[0].action='/insta/reports/eventAlertReport.do?mode=input&module=reports';
    document.forms[0].submit();
}
//-->
</script>