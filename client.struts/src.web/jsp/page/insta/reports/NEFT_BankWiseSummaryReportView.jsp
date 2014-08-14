<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@page import="com.objectfrontier.insta.reports.dto.ReportDTO"%>
<%@ page import="com.objectfrontier.neft.report.dto.TransactionInfo"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/neftInwardBankWiseSummaryReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
           
		      <tr class="row-value">
              <td colspan="11" align="center">
			  
			  <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
			  <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
			  <nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>
			  <nested:define id="_reportTitle" property="reportTitle" type="java.lang.String" />
                <b>
                  <font size="2.5">
				
                  	<%=_reportTitle%><br>
                  	  from <%=valueDate%> to <%=toDate%>
                  	  <br>
                    </font>
                  </b>
              </td>
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
		   <% int head_i = 1; %>
            <nested:notEmpty property="returnedList">
              <nested:define id="_returnList" property="returnedList" type="java.util.List" />
              
			  <tr>
              <td  colspan="11" align="center">
                <TABLE>
					<tr class="row-label th-bc">
                        <td rowspan=2 width="3%" align="center"><nested:message key="label.serialno"/></td>						
						<td rowspan=2 width="5%" align="center"><nested:message key="label.bank.ifsc"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.count"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.amount"/></td>						
						
						
					<% if (_reportTitle.equalsIgnoreCase("NEFT Inward Summary Report")) { %>
					
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.incompletedcount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.incompletedamount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.inrtncount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.inrtnamount"/></td>
						
					<% } else {%>
					
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.outcompletedcount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.outcompletedamount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.outrtncount"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.txn.outrtnamount"/></td>
					<% } %>
						
                    </tr>
					<tr> </tr>
					<%  
					int size = _returnList.size();					
                    if (size==0) {
                    %> 
                        <tr class="row-value tr2-bc">
                            <td colspan=9 align="center"><b>	No Data Found.  </b> </td>
                        </tr>
                    <% } else {
						int i=1;
						BigDecimal inwTotAmt = BigDecimal.ZERO;
						BigDecimal outTotAmt = BigDecimal.ZERO;					
						for(Iterator iter = _returnList.iterator(); iter.hasNext();) {
							ReportDTO _reportDTO = (ReportDTO)iter.next();
                    %>

						<% if(i%2==0) { %>  
						<tr class="row-value tr2-bc">
						<% } else { %>
						<tr class="row-value tr1-bc">
						<% } %>

						<td align="center" width="30"><%=i%></td>   

						<%if(_reportDTO.getSenderAddress() == null){%>
								<td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="left">&nbsp;<%= _reportDTO.getSenderAddress() %></td>
                        <%}%>

						<%if(_reportDTO.getCount() == 0){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.getCount() %></td>
                        <%}%>
						
						<%if(_reportDTO.amt == null || (_reportDTO.amt.equals("0"))){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.amt %></td>
                        <%}%>

						<%if(_reportDTO.completedTxnCount == 0){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.completedTxnCount %></td>
                        <%}%>
						
						
						<%if(_reportDTO.completedTxnAmount == null || (_reportDTO.completedTxnAmount.equals("0"))){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.completedTxnAmount %></td>
                        <%}%>
						
						<%if(_reportDTO.rtnTxnCount == 0){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.rtnTxnCount %></td>
                        <%}%>
						
						<%if(_reportDTO.rtnTxnAmount == null || (_reportDTO.rtnTxnAmount.equals("0"))){%>
								<td nowrap align="right">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="right">&nbsp;<%= _reportDTO.rtnTxnAmount %></td>
                        <%}%>
						
						<% i++; head_i++; %>
					<% } 
					} %>
                </TABLE>    
              </td></tr>
          </nested:notEmpty>
          <nested:empty property="returnedList">
            <tr class="row-value tr2-bc">
                <td colspan="7" align="center"><b>	No Data Found.   </b></td>
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

    function goBack() {

        document.forms[0].action='/insta/reports/neftReportInput.do?module=reports&amp;mode=input&amp;report=InwardBankDetailedReport';
        document.forms[0].submit();
    }
//-->

</script>
</div>
</nested:form>
