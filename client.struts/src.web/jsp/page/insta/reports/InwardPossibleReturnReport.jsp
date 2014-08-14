<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<!-- <script type="text/javascript">
	milliSec = 1*1000*60; 
	setTimeout("location.reload()", milliSec) //
</script>-->
<nested:form action="/rtgsInwardPossibleReturnedReport">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
    </tr>
</table>    
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <!-- <tr>
        <td class="row-value">Value Date : <nested:write property="reportDto.valueDate"/></td>
    </tr> -->
    <tr class="row-value">
    	<td align="center" class="row-value">
    	<nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
            <b>
            	<font size="2.0">
                	RTGS Inward Possible Return Report on <br><%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
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
                <td width="5%" align="center">S.No</td>
                <td width="10%" align="center" nowrap>UTR No</td>
                <td width="10%" align="center" nowrap>Tran Type</td>
                <td width="10%" align="center" nowrap>Sender Address</td>
                <td width="10%" align="center" nowrap>Receiver Address</td>
                <td width="10%" align="center" nowrap>Amount(Rs.)</td>
                <td width="10%" align="center" nowrap>BusinessDate</td>
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
	                   	<td align="center" nowrap><nested:write property="utrNo"/></td>
                       	<td align="center" nowrap><nested:write property="tranType"/></td>
                       	<td align="center"><nested:write property="senderAddress"/></td>
	                    <td align="center"><nested:write property="receiverAddress"/></td>
	                   	<nested:define id="txnAmt" property="amt" type="java.lang.String"/>
	                   	<td align="right" nowrap><%=FormatAmount.formatINRAmount(txnAmt)%></td>
	                    <td align="center"> 
	                    <nested:define id = "date" property="valueDate" type="java.lang.String"/>
                		<%=InstaReportUtil.getDateInSpecificFormat(format,date)%>
                		</td>
               		</tr>
        			<% i++; %>	       
                        </nested:iterate>
				  </nested:notEmpty>
				   <nested:empty property="reportDTOs">
        			<tr class="row-value tr2-bc">
						<td colspan="10" align="center">&nbsp;No Data Found.</td>          		   		        		   		
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
    
    	var title = "";
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->
</script>
</div>
</nested:form>
