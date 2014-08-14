<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<script language="JavaScript" src="<%=scriptBase%>/calendar.js"></script>
<nested:form action="/graduatedReport">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td class="error" height="10"><nested:errors/></td>
	</tr>
	<tr>
	 <TD>
		<table width="80%" align="center" border="0" cellspacing="1" cellpadding="4">
			<tr>
          		<TD vAlign=top height="21" width="257" colspan="2">&nbsp;</TD>
	        </tr>
          	<tr>
	      		<TD vAlign=top height="21" width="257" colspan="2">&nbsp;</TD>
          	</tr>
          <TR>
            <TD class=column-label width="25%" nowrap>Transaction Date :</TD>
            <TD class=row-value width="75%"> 
           		<nested:text property="reportDto.valueDate" maxlength="20" styleClass="textbox" style="width:180" 
	      			readonly="true" onclick="fPopCalFuture(this);"/>
	      		<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand"
	      			 onClick="setDate(document.forms[0].elements['reportDto.valueDate']);">
            </TD>
          </TR>
		</table>
	</td>
	</tr>
</table>
</nested:form>
<script>
<!--
function setDate(e) {

	fPopCalFuture(e);
	return false;
}
function generateGraduatedPaymentReport() {

    document.forms[0].action='/insta/reports/graduatedReport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}
//-->
</script>
