<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.RHSJSPConstants" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.RHSUIJSPConstants" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>

<%!
	String rhsBase = null;
%>

<%
	rhsBase = appBase;
%>
<script>
<!--
function executeRHS(form, action) {
	form.action = "<%=rhsBase%>" + action;
	form.submit();
}
//-->
</script>