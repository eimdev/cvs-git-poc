<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<%@page import="java.util.Date"%>
<nested:form action="/neftGraduatedPaymentReport">
<nested:equal property="isDateWiseGraduated" value = "0">
<meta http-equiv="refresh" content="60;url=/insta/reports/neftGraduatedPaymentReport.do?mode=view&amp;module=reports&amp;report=GrduatedPaymentReport" />
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10">This page will be automatically refreshed for every 1 minute</td>
    </tr>
</table>
</nested:equal>    
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	 <tr class="row-value">
        <td align="center" class="row-value">
        	<font size="2.0">
        	<nested:define id ="format" property="dateFormat" type="java.lang.String"/>
            <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
        	<b>
        		NEFT Datewise Graduated Payment Report for <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
            </b>
            </font>
        </td>
     </tr>
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
    <!-- Here Report date removed by priyak for user requirement -->
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
                <td width="5%" align="center">S.No</td>
                <td width="10%" align="center" >UTR No</td>
                <td width="10%" align="center" >Msg Type</td>
                <td width="10%" align="center" >Tran Type</td>
                <td width="15%" align="center" >Sender Address</td>
                <td width="10%" align="center" >Receiver Address</td>
                <!-- <td width="10%" align="center" >Amount(Rs.)</td>-->
               	<td width="10%" align="center" >Credit Amount</td>
               	<td width="10%" align="center" >Debit Amount</td>
               	 <!-- Column added by priyak for user requirement. -->
                <td width="10%" align="center" >NEFT Balance</td>
                <td width="10%" align="center" >Batch Time</td>
               	<td width="10%" align="center" >Rescheduled Date</td>
                <td width="10%" align="center" >Rescheduled Batch Time</td>
                <td width="10%" align="center" >Rejected Date</td>
                <td width="10%" align="center" >Rejected Batch Time</td>
           </tr>     
              	<% int i=1; %>
				<nested:notEmpty property="graduadtedPayments">
				   <nested:iterate property="graduadtedPayments">
        			<% if(i%2==0) { %>  
        			<tr class="row-value tr2-bc">
        			<% } else { %>
        			<tr class="row-value tr1-bc">
        			<% } %>
						<td align="center" width="30"><%=i%></td>
	                   	<td align="center" nowrap><nested:write property="utrNo"/></td>
	                   	<td align="center" ><nested:write property="msgType"/></td>
                       	<td align="center" ><nested:write property="tranType"/></td>
                       	<td align="center" ><nested:write property="senderAddress"/></td>
	                    <td align="center" ><nested:write property="receiverAddress"/></td>
	                    <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
						<nested:equal property="debitCredit"  value="Credit">
		                    <td align="right" nowrap><%=FormatAmount.formatINRAmount(txnAmt)%></td>
		                    <td align="center"></td> <!-- Zero values should not be displayed.Have done by priyak. -->
		                </nested:equal>
		                <nested:equal property="debitCredit"  value="Debit">
		                    <td align="center" ></td> <!-- Zero values should not be displayed.Have done by priyak. -->
		                    <td align="right" nowrap><%=FormatAmount.formatINRAmount(txnAmt)%></td>
		                </nested:equal>
	                   	<!-- <td align="right" nowrap><nested:write property="amt"/></td>
	                    <td align="center" ><nested:write property="debitCredit"/></td>-->
	                    <td align="right" nowrap></td>
	                    <td align="center" ><nested:write property="batchTime"/></td>
	                    <nested:notEmpty property="reshDate">
	                     <nested:define id = "reshDate" property="reshDate" type="java.lang.String"/>
	                    <td align="center" nowrap><%=InstaReportUtil.getDateInSpecificFormat(format,reshDate)%></td>
	                    </nested:notEmpty>
	                    <nested:empty property="reshDate">
	                    <td>&nbsp;</td>
	                    </nested:empty>
	                    <td align="center" ><nested:write property="reshBatchTime"/></td>
	                    <nested:notEmpty property="rejDate">
	                    <nested:define id = "rejDate" property="rejDate" type="java.lang.String"/>
	                    <td align="center" nowrap><%=InstaReportUtil.getDateInSpecificFormat(format,rejDate)%></td>
	                    </nested:notEmpty>
	                    <nested:empty property="rejDate">
	                    <td>&nbsp;</td>
	                    </nested:empty>
                    	<td align="center" ><nested:write property="rejBatchTime"/></td>
               		</tr>
        			<% i++; %>	       
                        </nested:iterate>
				  </nested:notEmpty>
				   <nested:empty property="graduadtedPayments">
        			<tr class="row-value tr2-bc">
						<td colspan=6 align="center">&nbsp;No Data Found.</td>          		   		        		   		
               		</tr>
            	</nested:empty>
	  </table>
    </td>
	</tr>
</table>

<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>

<script>
<!--
   // if(document.forms[0].appDate.value == document.forms[0].reportDate.value){
     //   milliSec = 1*1000*60; 
       // setTimeout("location.reload()", milliSec) //
    // }
    
    function printScript() {
    
    	var title = new String("");
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->
</script>
</div>
</nested:form>
