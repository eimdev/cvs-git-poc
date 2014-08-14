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
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/neftBatchwiseAggregateReport">
<nested:define id="_reportType" property="reportDto.reportType" type="java.lang.String"/>
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
    String title = "";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    
    <tr class="row-value">
        <td align="center" class="row-value">
            	<nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
         <font size="2.0">
        	<b>Batchwise Aggregate <nested:write property="reportDto.reportType"/> Report for  <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
         </font>	 
        </b></td>
    </tr>
    <tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
            <tr class="row-value">
              <td colspan="1" align="left">
                  <b>
                    Branch
                  </b>
              </td>
              <td colspan="6" align="left">
                  <b>
                    <% String displayValue = "ALL Branches"; %>
                    <nested:notEmpty property="hostBranchList">
                	<nested:define id="_hostBranchList" property="hostBranchList" type="java.util.List"/>
                	<nested:define id="_ifscId" property="reportDto.ifscId" type="java.lang.Long"/>
                	<% 
                       for(Iterator iter = _hostBranchList.iterator(); iter.hasNext();) {

                       	HostIFSCMasterDTO _dto = (HostIFSCMasterDTO)iter.next();
                       	long ifscId = Long.parseLong(_dto.getHostIFSCMasterVO().getIfscId());
                        if(_ifscId.longValue() == ifscId) {
                            displayValue = _dto.getHostIFSCMasterVO().getIfscCode() + "-"+ _dto.getHostIFSCMasterVO().getBranchName();
                        	break;
                        }
                       }
                   %>
                   </nested:notEmpty>
                   <%=displayValue%>
                  </b>
              </td>
            </tr>
            <tr class="row-value">
              <td colspan="1" align="left">
                  <b>
                    Batch Time
                  </b>
              </td>
              <td colspan="6" align="left">
                  <b>
                    <nested:write property="reportDto.batchTime"/>
                  </b>
              </td>
            </tr>
              <tr class="row-value">
                 <td colspan="8" align="right">
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
			<nested:notEmpty property="aggregateMap">
			<nested:define id="_aggregateMap" property="aggregateMap" type="java.util.Map"/>
			<%
				boolean k= false;
				if(_reportType.equalsIgnoreCase("Detailed")) {
				    
             		
           			int grandCreditCount = 0;
        			int grandDebitCount = 0;
        			BigDecimal grandTotCreditAmount = BigDecimal.ZERO;
        			BigDecimal grandTotDebitAmount = BigDecimal.ZERO;
           			List result = null;
            		for(Iterator i = _aggregateMap.entrySet().iterator(); i.hasNext(); ) {
            			Map.Entry entry = (Map.Entry)i.next();
            			result = (List)entry.getValue();
		        		if(result != null && result.size() > 0) { %>
		        <tr class="row-label th-bc">
            		<td align="left" width="30%" colspan="8">
            	    <% Object obj = entry.getKey(); %>
            	    <% if(obj != null) { %>
            			<b>Batch :  <%=(String)obj%></b>
            		<% } %>	
            		</td>
            	</tr>
		        <tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Branch Ifsc Code</td>
                <td width="15%" align="center" >No. of credits</td>
                <td width="20%" align="center" >Credit Amount (Rs.)</td>
                <td width="15%" align="center" >No. of debits</td>
                <td width="20%" align="center" >Debit Amount (Rs.)</td>
                <td width="30%" align="center" >Aggregate Amount (Credit-Debit)(Rs.)</td>
                </tr>	
		        <% int z = 0; %>
		        <% int creditCount = 0; %>
		        <% int debitCount = 0; %>
		        <% BigDecimal totCreditAmount = BigDecimal.ZERO; %>
		        <% BigDecimal totDebitAmount = BigDecimal.ZERO; %>
		        <% for(Iterator j = result.iterator(); j.hasNext(); ) { %>
		        <% BatchwiseAggregateDTO dto = (BatchwiseAggregateDTO)j.next(); %>
		        <% String credit = dto.getCreditAmount(); %>
		        <% if(credit == null) { dto.setCreditAmount("0.00"); }%>
		        <% String debit = dto.getDebitAmount(); %>
		        <% if(debit == null) { dto.setDebitAmount("0.00"); }%>
		        
		        <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <%  z  += 1; %>
                	<td align="center" width="5%"><%=z%></td>
               		<td align="center" nowrap>&nbsp;
               			<%=dto.getIfsc()%>
                	</td>
                	<td align="center">
                	    <% creditCount += dto.getNoOfCredits(); %>
                	    <%=dto.getNoOfCredits()%>
                	</td>    
                	<td align="right">
                		<% BigDecimal creditDecimal = new BigDecimal(dto.getCreditAmount()); %>
                		<% creditDecimal = creditDecimal.setScale(2); %>
                		<% totCreditAmount = totCreditAmount.add(creditDecimal); %>
                		<%=creditDecimal.toString()%>
                	</td>     
                	<td align="center">
                		<% debitCount += dto.getNoOfDebits(); %>
                		<%=dto.getNoOfDebits()%>
                	</td>    
                	<td align="right">
                		<% BigDecimal debitDecimal = new BigDecimal(dto.getDebitAmount()); %>
                		<% debitDecimal = debitDecimal.setScale(2); %>
                		<% totDebitAmount = totDebitAmount.add(debitDecimal); %>
                		<%=debitDecimal.toString()%>
                	</td>
                 	<% BigDecimal creditAmount = new BigDecimal(dto.getCreditAmount()); 
                	   BigDecimal debitAmount = new BigDecimal(dto.getDebitAmount());
                	   BigDecimal totalAmt = BigDecimal.ZERO;
                	   totalAmt = creditAmount.subtract(debitAmount);
                	   %>
                	 <td align="right">
                	 	<%=totalAmt.toString()%>
                	 </td>  
                <% } %>
                <% k = k ? false : true;
              	if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
		        	
                	<td width="5%" align="center">Total</td>
                	<td width="15%" align="center" >&nbsp;</td>
                	<td width="15%" align="center" ><%=creditCount%></td>
                	<td width="20%" align="right" ><%=totCreditAmount.toString()%></td>
                	<td width="15%" align="center" ><%=debitCount%></td>
                	<td width="20%" align="right" ><%=totDebitAmount.toString()%></td>
                	<% BigDecimal totalAggregate = BigDecimal.ZERO; %>
                	<% totalAggregate =  totCreditAmount.subtract(totDebitAmount);%>
                	<td width="30%" align="right" ><%=totalAggregate.toString()%></td>
                	</tr> 
                	<tr class="row-value tr2-bc">
						<td colspan="11">&nbsp;</td>
		        	</tr>
		        	<% grandTotCreditAmount = grandTotCreditAmount.add(totCreditAmount); %>
		    		<% grandDebitCount += debitCount; %>
		    		<% grandCreditCount += creditCount; %>
		    		<% grandTotDebitAmount = grandTotDebitAmount.add(totDebitAmount); %>
		        <% } else  { %>
        		 <tr class="row-value tr2-bc">
			  		 <td colspan="11" align="center">No Records Found..</td>
    	    	</tr>
    	 	 <% }  %>
            <% } %>
            <% if(result.size() > 0) { %>
		    	<tr class="row-value tr2-bc">
           			<td width="5%" align="center">Grand Total</td>
            		<td width="15%" align="center" >&nbsp;</td>
            		<td width="15%" align="center" ><%=grandCreditCount%></td>
            		<td width="20%" align="right" ><%=grandTotCreditAmount.toString()%></td>
            		<td width="15%" align="center" ><%=grandDebitCount%></td>
            		<td width="20%" align="right" ><%=grandTotDebitAmount.toString()%></td>
            		<% BigDecimal grandTotalAggregate = BigDecimal.ZERO; %>
            		<% grandTotalAggregate =  grandTotCreditAmount.subtract(grandTotDebitAmount);%>
            		<td width="30%" align="right" ><%=grandTotalAggregate.toString()%></td>
          		</tr>
          <% } %>	
          
          <% } else { %>
          		<tr class="row-label th-bc">
                <td width="15%" align="center" >Batch Time</td>
                <td width="15%" align="center" >No. of credits</td>
                <td width="20%" align="center" >Credit Amount (Rs.)</td>
                <td width="15%" align="center" >No. of debits</td>
                <td width="20%" align="center" >debit Amount (Rs.)</td>
                <td width="30%" align="center" >Aggregate Amount (Credit-Debit)(Rs.)</td>
                </tr>	
          <%
          		BatchwiseAggregateDTO summaryDTO = null;
          		for(Iterator j = _aggregateMap.entrySet().iterator(); j.hasNext(); ) {
           			Map.Entry entry = (Map.Entry)j.next();
           			summaryDTO = (BatchwiseAggregateDTO)entry.getValue();
           			k = k ? false : true;
              		if (k) {  %>
              			<tr class="row-value tr1-bc">
				<% 	} else { %>
						<tr class="row-value tr2-bc">
		        <% 	} %>
            	<td width="15%" align="center" ><%=summaryDTO.getBatchTime() %></td>
                <td width="15%" align="center" ><%=summaryDTO.getNoOfCredits() %></td>
                <td width="20%" align="center" ><%=new BigDecimal(summaryDTO.getCreditAmount()).setScale(2)%></td>
                <td width="15%" align="center" ><%=summaryDTO.getNoOfDebits() %></td>
                <td width="20%" align="center" ><%=new BigDecimal(summaryDTO.getDebitAmount()).setScale(2)%></td>
                <% 	BigDecimal total = BigDecimal.ZERO;
                   	total = new BigDecimal(summaryDTO.getCreditAmount()).subtract(new BigDecimal(summaryDTO.getDebitAmount())).setScale(2);
                %>
                <td width="30%" align="center" ><%=total%></td>		
                <%}
         	} %>		
          </nested:notEmpty>
          
          <nested:empty property="aggregateMap">
            <tr class="row-value tr2-bc">
                <td colspan="8" align="center"><b>	No Records Found.   </b></td>
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
