<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Iterator" %>
<%@page import="com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="com.objectfrontier.neft.report.dto.BatchwiseAggregateDTO"%>
<%@page import="com.objectfrontier.neft.report.dto.NEFTN04DetailsDTO"%>
<%@page import="com.objectfrontier.neft.report.dto.BatchwiseReconcillationDTO"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/neftBatchwiseReconcillationReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
	//Commented and modified by priyak for spelling correction.
    //String title = "Batchwise - Reconcilliation Report";
    String title = "Batchwise - Reconciliation Report";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
            <tr class="row-value">
              <td colspan="3" align="left">
                  <b>
                    Report Date :
                  </b>
              </td>
              
              <td colspan="2" align="left" class="row-value">
                        <!-- Here all the input details should be here -->
                 <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        		 <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
                  <b>
                  	 <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
                  </b>
              </td>
              <td colspan="2" align="left">
                  <b>
                    Batch Time :
                  </b>
              </td>
              <td colspan="6" align="left">
                  <b>
                    <nested:write property="reportDto.batchTime"/>
                  </b>
              </td>
              </tr>
           <!-- Here transction type is removed by priyak for client requirement -->
            <tr class="row-value">
            </tr>
              <tr class="row-value">
                 <td colspan="11" align="right">
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
            <%
                BigDecimal totAmt = BigDecimal.ZERO;
            %>
            <nested:notEmpty property="reconcillationMap">
            <%
              			boolean k= false;
            %>
            <nested:define id="_reconcillationMap" property="reconcillationMap" type="java.util.Map"/>
            	<tr class="row-label th-bc">
            		<td align="left" width="30%" colspan="12">
            			<b>As Per N04</b>
            		</td>
            	</tr>
            	<tr class="row-label th-bc">
            		<td align="center" width="30%" colspan="8">
            			<b>Outward Transactions</b>
            		</td>
            		<td align="center" width="30%" colspan="4">
            			<b>Inward Transactions</b>
            		</td>
            	</tr>
            	<tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Batch Time</td>
                <td width="20%" align="center" >Total no.of Txns Sent</td>
                <td width="20%" align="center" >Total Amount Sent</td>
                <td width="20%" align="center" >Total no.of Txns Accepted</td>
                <td width="20%" align="center" >Total Amount Accepted</td>
                <td width="20%" align="center" >Total no.of Txns Rejected</td>
                <td width="20%" align="center" >Total Amount Rejected</td>
                <td width="20%" align="center" >Total no.of Txns Received</td>
                <td width="20%" align="center" >Total Amount Received</td>
                <td width="20%" align="center" >Total no.of Txns Returned</td>
                <td width="20%" align="center" >Total Amount Returned</td>
                </tr>
                <% List N04List = (List)_reconcillationMap.get("N04"); %>
                <% if(N04List != null && N04List.size() > 0) { %>
                	<% int z = 0; %>
                	<%	for(Iterator iter = N04List.iterator(); iter.hasNext();) {
                	    NEFTN04DetailsDTO dto = (NEFTN04DetailsDTO) iter.next(); %>
	                	<% k = k ? false : true;
	              			if (k) {%><tr class="row-value tr1-bc">
						<% 	} else { %><tr class="row-value tr2-bc">
			        	<% 	} %>
	                	<%  z  += 1; %>
	                	<td align="center" width="5%"><%=z%></td>
	                	<td width="15%" align="center" ><%=dto.getField3535()%></td>
	                	<td width="20%" align="center" ><%=dto.getField5175()%></td>
	                	<td width="20%" align="right" ><%=dto.getField4105()%></td>
	                	<td width="20%" align="center" ><%=dto.getField5180()%></td>
	                	<td width="20%" align="right" ><%=dto.getField4110()%></td>
	                	<td width="20%" align="center" ><%=dto.getField5185()%></td>
	                	<td width="20%" align="right" ><%=dto.getField4115()%></td>
	                	<td width="20%" align="center" ><%=dto.getField5267()%></td>
	                	<td width="20%" align="right" ><%=dto.getField4410()%></td>
	                	<td width="20%" align="center" ><%=dto.getField5047()%></td>
	                	<% BigDecimal tot = new BigDecimal(dto.getField4460());
                           tot = tot.setScale(2);%>
                           
	                	<td width="20%" align="right" ><%=tot%></td>
	                	</tr>
                	<%	} %>
                <% } else { %>
                	<tr class="row-value tr2-bc">
                		<td colspan="12" align="center"><b>	No Records Found...   </b></td>
            		</tr>
                <% } %>
                <tr class="row-value tr2-bc">
                		<td colspan="12" align="center"><b>	&nbsp; </b></td>
            	</tr>
				<tr class="row-label th-bc">
            		<td align="left" width="30%" colspan="12">
            			<b>As Per LMS</b>
            		</td>
            	</tr>
            	<tr class="row-label th-bc">
            		<td align="center" width="30%" colspan="8">
            			<b>Outward Transactions</b>
            		</td>
            		<td align="center" width="30%" colspan="4">
            			<b>Inward Transactions</b>
            		</td>
            	</tr>
            	<tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Batch Time</td>
                <td width="20%" align="center" >Outward Total no.of txns Sent</td>
                <td width="20%" align="center" >Outward Total Amount</td>
                <td width="20%" align="center" >Total no.of  txns Settled, Rescheduled</td>
                <td width="20%" align="center" >Total Amount Settled, Rescheduled</td>
                <td width="20%" align="center" >Total no.of txns Unsuccessful</td>
                <td width="20%" align="center" >Total Amount Unsuccessful</td>
                <td width="20%" align="center" >Inward Total no.of Txns Received</td>
                <td width="20%" align="center" >Inward Total Amount Received</td>
                <td width="20%" align="center" >Total no.of Txns Returned</td>
                <td width="20%" align="center" >Total Amount Returned</td>
                </tr>
                <% List subTypeList = (List)_reconcillationMap.get("SUBTYPE"); %>
                <% if(subTypeList != null && subTypeList.size() > 0) { %>
                	<% int z = 0; %>
                	<%	for(Iterator iter = subTypeList.iterator(); iter.hasNext();) {
                	    BatchwiseReconcillationDTO dto = (BatchwiseReconcillationDTO) iter.next(); %>
	                	<% k = k ? false : true;
	              			if (k) {%><tr class="row-value tr1-bc">
						<% 	} else { %><tr class="row-value tr2-bc">
			        	<% 	} %>
	                	<%  z  += 1; %>
	                	<td align="center" width="5%"><%=z%></td>
	                	<% long owTxnSent = 0; %>
	                	<% owTxnSent = dto.getOwTxnAccepted() + dto.getOwTxnRejected(); %>
	                	<% BigDecimal owTxnSentAccepted = new BigDecimal(dto.getOwTxnAccepted()); %>
	                	<% BigDecimal owTxnSentAmt = BigDecimal.ZERO; %>
	                	<% BigDecimal owTxnSentAcceptedAmt = new BigDecimal(dto.getOwTxnAmtAccepted()); %>
	                	<% BigDecimal owTxnSentRejctedAmt = new BigDecimal(dto.getOwTxnAmtRejected()); %>
	                	<% owTxnSentAmt = owTxnSentAcceptedAmt.add(owTxnSentRejctedAmt); %>
	                	<% BigDecimal iwTxnAmt = BigDecimal.ZERO; %>
	                	<% BigDecimal iwTxnReceivedAmt = new BigDecimal(dto.getIwTxnAmtReceived()); %>
	                	<% BigDecimal iwTxnReturnedAmt = new BigDecimal(dto.getIwTxnAmtReturned()); %>
	                	<% iwTxnAmt = iwTxnReceivedAmt.add(iwTxnReturnedAmt); %>
	                	<td width="15%" align="center" ><%=dto.getBatchTime()%></td>
	                	<td width="20%" align="center" ><%=owTxnSent%></td>
	                	<td width="20%" align="right" ><%=owTxnSentAmt%></td>
	                	<td width="20%" align="center" ><%=dto.getOwTxnAccepted()%></td>
	                	<% BigDecimal owTxnAmtAccepted = new BigDecimal(dto.getOwTxnAmtAccepted()); %>
	                	<% owTxnAmtAccepted = owTxnAmtAccepted.setScale(2); %>
	                	<td width="20%" align="right" ><%=owTxnAmtAccepted%></td>
	                	<td width="20%" align="center" ><%=dto.getOwTxnRejected()%></td>
	                	<% BigDecimal owTxnAmtRejected = new BigDecimal(dto.getOwTxnAmtRejected()); %>
	                	<% owTxnAmtRejected = owTxnAmtRejected.setScale(2); %>
	                	<td width="20%" align="right" ><%=owTxnAmtRejected%></td>
	                	<td width="20%" align="center" ><%=dto.getIwTxnReceived() + dto.getIwTxnReturned()%></td>
	                	<td width="20%" align="right" ><%=iwTxnAmt%></td>
	                	<td width="20%" align="center" ><%=dto.getIwTxnReturned()%></td>
	                	<% BigDecimal iwTxnAmtReturned = new BigDecimal(dto.getIwTxnAmtReturned()); %>
	                	<% iwTxnAmtReturned = iwTxnAmtReturned.setScale(2); %>
	                	<td width="20%" align="right" ><%=iwTxnAmtReturned%></td>
	                	</tr>
                	<%	} %>
                <% } else { %>
                	<tr class="row-value tr2-bc">
                		<td colspan="12" align="center"><b>	No Records Found... </b></td>
            		</tr>
                <% } %>	                
          	</nested:notEmpty>
           <nested:empty property="reconcillationMap">
            <tr class="row-value tr2-bc">
                <td colspan="8" align="center"><b>	No Records Found...   </b></td>
            </tr>
          </nested:empty>
	     </TABLE> 
    </td>
	</tr>	
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function printScript() {

        var title = '<%=title%>';
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
    
//-->

</script>
</div>
</nested:form>
