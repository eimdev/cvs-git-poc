<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/r41inwardreport">
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr class="row-value">
    	<td align="center" class="row-value">
    	 <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
         <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
            <b>
            	<font size="2.0">
                	Reconciliation Report on <br><%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
                </font>
            </b>
    	</td>
    </tr>
    <tr class="row-value">
        <td align="right">
           <nested:define id = "printDate" property="currentReportPrintTime" type="java.lang.String"/>
	           	<% String dateForm = printDate.substring(0,11);
	              String time = printDate.substring(11);
	              String printDat = InstaReportUtil.getDateInSpecificFormat(format,dateForm)+time;
	            %>
	         <b>
            	Report Printed on <%=printDat%>
          	</b>
        </td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td width="5%" align="center">SNo</td>
                <td width="15%" align="center" >Value Date</td>
                <td width="30%" align="center" >Message Type</td><!-- MessageType-MessageSubType -->
                <td width="30%" align="center" >Transaction Type</td>
                <td width="25%" align="center" >Total Txn</td>
                <td width="20%" align="center" >Total TXN Amount</td>
           </tr>
              	<% int i=1; %>
				<nested:notEmpty property="reportDTOs">
				   <nested:iterate property="reportDTOs">
        			<% if(i%2==0) { %>
        			<tr class="row-value tr2-bc">
        			<% } else { %>
        			<tr class="row-value tr1-bc">
        			<% } %>
						<td align="center" width="30"><%=i%></td>
						 <nested:define id = "date" property="valueDate" type="java.lang.String"/>
						<td align="center" nowrap>&nbsp;<%=InstaReportUtil.getDateInSpecificFormat(format,date)%></td>
                       	<td align="center" >&nbsp;<nested:write property="msgType"/></td>
	                    <td align="center" >&nbsp;<nested:write property="tranType"/></td>
                    	<td align="center" nowrap>&nbsp;<nested:write property="count"/></td>
                    	<nested:define id="txnAmt" property="amt" type="java.lang.String"/>
	                    <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
               		</tr>
        			<% i++; %>
                        </nested:iterate>
				  </nested:notEmpty>
				   <nested:empty property="reportDTOs">
        			<tr class="row-value tr2-bc">
						<td colspan=6 align="center">	No Data Found.   </td>
               		</tr>
            	</nested:empty>
	  </table>
    </td>
	</tr>
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function printScript() {
    
        var title = new String("");	
		
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->

</script>
</div>
</nested:form>
