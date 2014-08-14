<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="java.util.Iterator, java.util.List, java.util.ArrayList"%>
<%@ page import="com.objectfrontier.insta.reports.constants.ReportConstants"%>
<%@ page import="com.objectfrontier.insta.reports.server.util.ConversionUtil"%>
<%@ page import="com.objectfrontier.neft.report.dto.SummaryInfo"%>
<%@ page import="com.objectfrontier.insta.reports.server.util.FormatAmount" %>
<%@ page import="com.objectfrontier.neft.report.dto.SummaryInfo.SummaryInfoElement"%>
<%@ page import="com.objectfrontier.neft.report.dto.TransactionInfo"%>
<%@ page import="com.objectfrontier.insta.reports.InstaReportUtil"%>
<%@ page import="java.math.BigDecimal" %>
 
<nested:form action="/neftInwardTxnsReport">
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<TABLE border="0" width="100%" cellspacing="1" cellpadding="2">
   <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
    <TR>
        <td vAlign=top height="21" >
            <%
              			boolean k= false;
            %>
            <table cellSpacing="1" cellPadding="2" width="100%" border="0">
            <tr class="row-value">
              <td colspan="11" align="center">
               <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        		 <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
              	 <nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>      
                  <b>
                  <font size="2.0">
                    NEFT Inward Transactions - Detailed Report - from <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,toDate)%>
                    </font>
                  </b>
             </td>
             </tr>
            <tr class="row-value">
              <td colspan="11" align="center">
                  <b>
                  <font size="2.0">
                    Status:<nested:write property="reportDto.statusValue"/>&nbsp;&nbsp;   Batch Time:  <nested:write property="reportDto.batchTime"/>
                    </font>
                  </b>
              </td>
            </tr>
            <nested:define id="toAmt" property="reportDto.toAmount" type="java.lang.String" />
            <nested:define id="fromAmt" property="reportDto.fromAmount" type="java.lang.String" />
            <%if( (new BigDecimal(toAmt).compareTo(BigDecimal.ZERO)) >0){%>
            <tr class="row-value">
              <td colspan="11" align="center">
                  <b>
                    Amount Range from <%=FormatAmount.formatINRAmount(fromAmt)%> to <%=FormatAmount.formatINRAmount(toAmt)%>
                  </b>
              </td>
            </tr>
            <%}%>
            <tr class="row-value">
              <td colspan="11" align="center">
                  <b>
                  <font size="2.0">
                    Branch:<nested:write property="reportDto.branchCode"/>
                    </font>
                  </b>
              </td>
            </tr>
            <tr class="row-value">
                 <td colspan="15" align="right">
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
                <nested:notEmpty property="inwardTxns">
                <nested:define id = "_reportList" property="inwardTxns" type ="java.util.List"/>
                <TR><TD>&nbsp;</TD></TR>
                    <tr class="row-label th-bc">
                        <td rowspan=2 width="5%" align="center"><nested:message key="label.serialno"/></td>
                        <td rowspan=2 align="center"><nested:message key="label.batch.time"/></td>
                        <td rowspan=2 align="center"><nested:message key="label.transaction.ref"/></td>
                        <td rowspan=2 align="center"><nested:message key="label.txns.amount"/></td>                        
                        <td colspan=4 align="center"><nested:message key="label.beneficiary.details"/></td> 
                        <td colspan=4 align="center"><nested:message key="label.sender.details"/></td>
                        <td rowspan=2 align="center"><nested:message key="label.report.transactionStatus"/></td>
                    </tr>
                    <tr class="row-label th-bc">
                    	<td align="center"><nested:message key="label.beneficiaryifsc"/></td>
                        <td align="center"><nested:message key="label.beneficiary.name"/></td>	
                        <td align="center"><nested:message key="label.beneficiary.acctype"/></td>	
                        <td align="center"><nested:message key="label.beneficiary.no"/></td>
                        <td align="center"><nested:message key="label.senderifsc"/></td>
                        <td align="center"><nested:message key="label.sender.account.name"/></td>
                        <td align="center"><nested:message key="label.sender.account.type"/></td>	
                        <td align="center"><nested:message key="label.sender.account.number"/></td>	
                    </tr>                    
               <%
                    int i=1;
               		BigDecimal inwTotAmt = BigDecimal.ZERO;
               		BigDecimal outTotAmt = BigDecimal.ZERO;
                    int size = _reportList.size();
                    for(Iterator iter = _reportList.iterator(); iter.hasNext();) {

                        TransactionInfo info = (TransactionInfo)iter.next();
                %>
                    <% if(i%2==0) { %>  
                    <tr class="row-value tr2-bc">
                    <% } else { %>
                    <tr class="row-value tr1-bc">
                    <% } %>
                      <td align="center" width="30"><%=i%></td>     
                      <%if(info.batchTime==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                      <%} else {%>
                             <td nowrap align="center">&nbsp;<%= info.batchTime %></td>
                      <%}%>   
                        <%if(info.utrNo==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= info.utrNo %></td>
                        <%}%>
                        <%inwTotAmt = inwTotAmt.add(info.amount);%>
                        <!--For amount field no need to check null because if nothing then automatically it will return 0-->
                        <td nowrap align="right">&nbsp;<%= FormatAmount.formatINRAmount(info.amount) %></td>
                        
                        <%if(info.beneficiaryInfo.accIfsc==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= info.beneficiaryInfo.accIfsc %></td>
                        <%}%>
                         <%if(info.beneficiaryInfo.accName==null){%>
                            <td align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td align="left">&nbsp;<%= info.beneficiaryInfo.accName %></td>
                        <%}%>
                       
                        <%if(info.beneficiaryInfo.accType==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= info.beneficiaryInfo.accType %></td>
                        <%}%>
                        
                        <%if(info.beneficiaryInfo.accNo==null){%>
                            <td nowrap align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="left">&nbsp;<%= info.beneficiaryInfo.accNo %></td>
                        <%}%>
                        <%if(info.senderInfo.accIfsc==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= info.senderInfo.accIfsc %></td>
                        <%}%>
                        <%if(info.senderInfo.accName==null){%>
                            <td align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td align="left">&nbsp;<%= info.senderInfo.accName %></td>
                        <%}%>
                        
                        <%if(info.senderInfo.accType==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                            <td nowrap align="center">&nbsp;<%= info.senderInfo.accType %></td>
                        <%}%>
                        
                        <%if(info.senderInfo.accNo==null){%>
                            <td nowrap align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                            <td nowrap align="left">&nbsp;<%= info.senderInfo.accNo %></td>
                        <%}%>
                        
                       <td nowrap align="center">&nbsp;<%= info.statusShortDesc%></td> 
                       
                       <% i++; %>
                <% }%>                      
                    </tr>
                    <tr class="row-value tr2-bc">
                    <td></td> <td></td>
                    <td align="center"><b>Total</b></td>
                    <td align="right"><b><%=inwTotAmt%></b></td>
                    </tr>
            </nested:notEmpty>
            <nested:empty property="inwardTxns">
           <tr class="row-value tr2-bc">
            <td align="center" colspan=50>
            <b><nested:message key="label.report.noReport"/></b> </td>
            </tr>
            </nested:empty>
        </table>
    </td>
    </tr>
</TABLE>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
</div>
<script>
<!--
    function printScript() {
           
        var title = new String("");	//Title commented by priyak for client requirement.
		
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->
</script>
</nested:form>