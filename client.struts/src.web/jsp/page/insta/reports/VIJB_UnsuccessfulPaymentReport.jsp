<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Iterator" %>
<%@page import="com.objectfrontier.insta.reports.dto.ReportDTO"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/unsuccessfulPaymentReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
                        <!-- Here all the input details should be here -->
            <tr class="row-value">
              <td colspan="11" align="center">
               <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        	  <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
              <nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>
              <nested:define id = "_response" property="reportDto.response" type="java.lang.String"/>
                  <b>
                  <font size="2.5">
                  <nested:define id ="subType" property="reportDto.paymentType" type="java.lang.String"/>
                  
                   RTGS Unsuccessful Payments Report 
                     from <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,toDate)%>
                     with  Negative <% if (_response.equalsIgnoreCase("R90")) { %>
                        PI
                    <%} else {%>
                        SSN
                    <%}%> Response
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
              <%
                String status = "";
                BigDecimal dateWiseAmt = BigDecimal.ZERO;
              %>
              <%
                BigDecimal totAmt = BigDecimal.ZERO;
            %> 
            <nested:notEmpty property="reportDTOs">
            <%
              			boolean k= false;
            %>
            <tr>
              <td  colspan="11" align="center">
                
                    <tr class="row-label th-bc">
                        <td width="5%" align="center">S.No</td>
                        <td width="15%" align="center" >Value Date</td>
                        <td width="15%" align="center" >Msg Type</td>
                        <td width="15%" align="center" >UTR Number</td>
                        <td width="25%" align="center" >Sender Address</td>
                        <td width="25%" align="center" >Receiver Address</td>
                        <td width="15%" align="center">Response Type</td>
                        <td width="25%" align="center">Remarks</td>
                        <td width="15%" align="center">Amount</td>
                     </tr>     
                    <nested:iterate indexId = "index" id ="reportDto" property="reportDTOs"> 
	              	<% k = k ? false : true;
	              		if (k) {%><tr class="row-value tr1-bc">
					<% 	} else { %><tr class="row-value tr2-bc">
			        <% 	} %>
	                <% int sno  = index.intValue()+1; %>
                	<td align="center" width="5%"><%=sno%></td>
                	
                     <nested:define id = "date" property="valueDate" type="java.lang.String"/>
                     <td nowrap>
                    <%=InstaReportUtil.getDateInSpecificFormat(format,date)%>
                        </td>
                    <td align="center" nowrap><nested:write property = "msgSubType"/></td>
                    <td align="center" nowrap>&nbsp;<nested:write property = "utrNo"/></td>
                    <td align="center" wrap>&nbsp;<nested:write property = "senderAddress"/></td>
                   	<td align="center" wrap>&nbsp;<nested:write property = "receiverAddress"/></td>
                    <td align="center" wrap>&nbsp;<nested:write property = "responseType"/></td>
                    <td align="center" wrap>&nbsp;<nested:write property = "remarks"/></td>
                      <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                      <% totAmt = totAmt.add(new BigDecimal(txnAmt));%>
                    <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
                        
                 </tr>
                
              </td></tr>
            </nested:iterate>
            <tr class="row-value tr2-bc">
                <td colspan="9" align="right"><b>	Total Amount : &nbsp; <%=FormatAmount.formatINRAmount(totAmt)%></b></td>
            </tr>
          </nested:notEmpty>
          <nested:empty property="reportDTOs">
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

        document.forms[0].action='/insta/reports/reportInput.do?module=reports&mode=input&report=unsuccessfulPayment';
        document.forms[0].submit();
    }
//-->

</script>
</div>
</nested:form>
