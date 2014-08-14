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
			  <nested:define id="_tranType" property="tranType" type="java.lang.String" />
                <b>
                  <font size="2.5">
					<%=_reportTitle%>
					<br>
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
            <nested:notEmpty property="reportMap">
            <nested:iterate id="_reportMap" property="reportMap">
              <nested:define id="_key" name="_reportMap" property="key" type="java.lang.String" />
			  <%
				String bankCode = _key.substring(0,4);
				String txnAmount = _key.substring(5);
			  %>
              <nested:define id="_reportList" name="_reportMap" property="value" type="java.util.List" />
				<tr class="row-value">
				    <td colspan="11" align="center">
						<TABLE>
							<tr> <td align="right"><b><font size="2.5"> 
							<% if (_tranType.equals("inward")) { %>
								Sender Bank 
							<%} else { %>
								Receiver Bank 
							<% } %>
							</td> <td><b><font size="2.5"> : </font></b> </td> <td> <b><font size="2.5"><%=bankCode%> </td></font></b></tr>
							<tr> <td align="right"><b><font size="2.5"> Txn Count </font></b></td> <td><b><font size="2.5"> : </font></b></td> <td><b><font size="2.5"> <%=_reportList.size()%> </td></font></b></tr>
							<tr> <td align="right"><b><font size="2.5"> Sum of Txn Amount </font></b></td> <td><b><font size="2.5"> : </font></b></td> <td><b><font size="2.5">Rs. <%=txnAmount%> </td></font></b></tr>
						</TABLE>
					</td>
				</tr>
			  <tr>
              <td  colspan="11" align="center">
                <TABLE>
					<tr class="row-label th-bc">
                        <td rowspan=2 width="5%" align="center"><nested:message key="label.serialno"/></td>
						<td rowspan=2 width="5%" align="center"><nested:message key="label.date"/></td>
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
					int size = _reportList.size();
					
                    if (size==0) {
                    %> 
                        <tr class="row-value tr2-bc">
                            <td colspan=11 align="center"><b>	No Data Found.  </b> </td>
                        </tr>
                    <%
                    } else {
						int i=1;
						BigDecimal inwTotAmt = BigDecimal.ZERO;
						BigDecimal outTotAmt = BigDecimal.ZERO;					
						for(Iterator iter = _reportList.iterator(); iter.hasNext();) {
							TransactionInfo _ti = (TransactionInfo)iter.next();
                    %>
						
						<% if(i%2==0) { %>  
						<tr class="row-value tr2-bc">
						<% } else { %>
						<tr class="row-value tr1-bc">
						<% } %>
						
							<td align="center" width="30"><%=i%></td>     

						<%if(_ti.valueDate==null){%>
								<td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= _ti.valueDate %></td>
                        <%}%>
						
						<%if(_ti.utrNo==null){%>
								<td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= _ti.utrNo %></td>
                        <%}%>
						
						<%inwTotAmt = inwTotAmt.add(_ti.amount);%>
                        <!--For amount field no need to check null because if nothing then automatically it will return 0-->
                        <td nowrap align="right">&nbsp;<%= FormatAmount.formatINRAmount(_ti.amount) %></td>
                        
                        <%if(_ti.beneficiaryInfo.accIfsc==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= _ti.beneficiaryInfo.accIfsc %></td>
                        <%}%>
						
						
						<%if(_ti.beneficiaryInfo.accName==null){%>
                            <td align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td align="left">&nbsp;<%= _ti.beneficiaryInfo.accName %></td>
                        <%}%>
                       
                        <%if(_ti.beneficiaryInfo.accType==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= _ti.beneficiaryInfo.accType %></td>
                        <%}%>
						
						
						<%if(_ti.beneficiaryInfo.accNo==null){%>
                            <td nowrap align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="left">&nbsp;<%= _ti.beneficiaryInfo.accNo %></td>
                        <%}%>
                        <%if(_ti.senderInfo.accIfsc==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td nowrap align="center">&nbsp;<%= _ti.senderInfo.accIfsc %></td>							 
                        <%}%>
                        <%if(_ti.senderInfo.accName==null){%>
                            <td align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                             <td align="left">&nbsp;<%= _ti.senderInfo.accName %></td>
                        <%}%>
                        
                        <%if(_ti.senderInfo.accType==null){%>
                            <td nowrap align="center">&nbsp;<%=""%></td>
                        <%} else {%>
                            <td nowrap align="center">&nbsp;<%= _ti.senderInfo.accType %></td>
                        <%}%>

						<%if(_ti.senderInfo.accNo==null){%>
                            <td nowrap align="left">&nbsp;<%=""%></td>
                        <%} else {%>
                            <td nowrap align="left">&nbsp;<%= _ti.senderInfo.accNo %></td>
                        <%}%>
                        
                       <td nowrap align="center">&nbsp;<%= _ti.statusShortDesc%></td> 
                       
                       <% i++; head_i++; %>
					   
					<%  }
					} %>
                </TABLE>    
              </td></tr>
            </nested:iterate>
          </nested:notEmpty>
          <nested:empty property="reportMap">
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
