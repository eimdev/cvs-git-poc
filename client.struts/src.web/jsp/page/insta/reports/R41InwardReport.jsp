<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>

<nested:form action="/r41inwardreport">
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
    <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
    
    <tr class="row-value">
    	<td align="center" class="row-value">
            <b>
            	<font size="2.0">
                	Customer Payments Received - Grouped By Sender Address on <br><%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
                </font>
            </b>
    	</td>
    </tr>
     
    <tr class="row-value">
        <td align="right">
           <b>
           <nested:define id = "printDate" property="currentReportPrintTime" type="java.lang.String"/>
           <% String dateForm = printDate.substring(0,11);
              String time = printDate.substring(11);
              String printDat = InstaReportUtil.getDateInSpecificFormat(format,dateForm)+time;
              %>
             Report Printed on <%=printDat%>
           </b>
        </td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="25%" align="center" >Sender Address</td>
                <td width="15%" align="center" >Value Date</td>
                <td width="10%" align="center" >No.of Txns</td>
                <td width="20%" align="center" >Total Txn Amount(Rs.)</td>
           </tr>     
              	<% int i=1; 
              	   BigDecimal totAmt = BigDecimal.ZERO; %>
				<nested:notEmpty property="reportDTOs">
				   <nested:iterate property="reportDTOs">
        			<% if(i%2==0) { %>  
        			<tr class="row-value tr2-bc">
        			<% } else { %>
        			<tr class="row-value tr1-bc">
        			<% } %>
						<td align="center" width="30"><%=i%></td>
                       	<td align="left" >&nbsp;<nested:write property="senderAddress"/></td>
                       	<nested:define id = "date" property="valueDate" type="java.lang.String"/>
						<td align="center" nowrap>&nbsp;<%=InstaReportUtil.getDateInSpecificFormat(format,date)%></td>                        
                    	<td align="center" nowrap>&nbsp;<nested:write property="count"/></td>
                    	<nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                    	<% totAmt = totAmt.add(new BigDecimal(txnAmt)); %> 
	                    <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
               		</tr>
        			<% i++; %>	       
                   </nested:iterate>
                   <tr class="row-value tr1-bc">
                   		<td colspan=4 align="right"><b>	Total  : &nbsp;&nbsp;</b></td>
                   		<td align="right" nowrap>&nbsp;<b><%=FormatAmount.formatINRAmount(totAmt)%></b></td>
                   </tr>		 
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
