<%@ include file="RHSStandard.jsp" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils" %>
<%@page import="com.objectfrontier.insta.rtgs.reports.bean.ReportBean"%>
<%@page import="com.objectfrontier.insta.reports.server.util.FormatAmount"%>
<%@page import="java.math.BigDecimal"%>
<nested:form action="/submitControllerReport">
<div id="printReady">
<%
String title = ReportBean.title;
title += ConversionUtils.getCurrentDateTime();
 
String imagePath = imageBase + "/print.jpg";
%>
<% java.util.List list = new java.util.ArrayList(); %>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
    <!-- RBC CMD 1.0 -->
    <TR>
      <TD vAlign=top height="21">
      
        <table  class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
        
        <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
            <tr class="row-label th-bc">
            <td>
                <nested:message key="label.fromdate"/> 
            </td>
            <td>
                <nested:write property="reportDTO.fromDate" format="dd-MM-yyyy"/>
            </td>
            <td></td><td></td><td></td><td></td><td></td><td></td>
            <nested:equal property = "tranType" value = "inward">
                <td></td><td></td>
            </nested:equal>
            </tr>
            <tr class="row-label th-bc">
                <td>
                    <nested:message key="label.todate"/> 
                </td>
                <td>
                    <nested:write property="reportDTO.toDate" format="dd-MM-yyyy"/>
                </td>
                <td></td><td></td><td></td><td></td><td></td><td></td>
                <nested:equal property = "tranType" value = "inward">
                <td></td><td></td>
                </nested:equal>
            </tr>
        
        </nested:notEqual>
        <nested:notEmpty property="utrWise">
        <nested:define property="utrWise" id="utr"/>
        </nested:notEmpty>

        
            <tr class="row-label th-bc">
            <td align="middle"><nested:message key="label.branch.ifsc"/></td>
            <td align="middle"><nested:message key="label.subtype"/></td>
            <td align="middle"><nested:message key="label.serialno"/></td>
            <td align="middle"><nested:message key="label.rtgsbaltxn.utrnumber"/></td>
            <td align="middle"><nested:message key="label.receiver.ifsc"/></td>
            <td align="middle"><nested:message key="label.valuedate"/></td>
            <td align="middle"><nested:message key="label.amount"/></td>
            <nested:equal property = "tranType" value = "inward">
                <td align="middle"><nested:message key="label.accno"/></td>
                <td align="middle"><nested:message key="label.remitter.info"/></td>
            </nested:equal>
            <td align="middle"><nested:message key="label.listmessages.status"/></td>
            </tr>            
            
            <nested:iterate indexId = "index2" id ="reportDtos" property="branchDTOs">  
                       
                <nested:notEmpty property="msgTypes"> 
                <nested:iterate indexId ="index1" property="msgTypes">
                <tr class="row-value tr1-bc-report">  
                    <td align = "left">
                    <nested:define id = "brCode" property="branchCode"/>
                     <% boolean check = list.contains(brCode); 
                    if(!check) {
                        list.add(brCode); %>
                        <b><nested:write property="branchCode"/></b>
                   <% } %>
                    </td> 
                    <td align = "center">               
                        <nested:write property="msgSubType"/>
                    </td>       
                <nested:notEmpty property="messages">
                <nested:iterate indexId="index" property="messages">
                <nested:define id = "txntype" property = "txnType"/>
                    <% int sno  = index.intValue()+1; %>
                    <%if(sno == 1) {%>
                    <td align="center" width="5%"><%=sno%></td>
                    <td align="middle"><nested:write property="utrNo"/></td>
                    <td align="left"><nested:write property="otherBank"/></td>
                    <td align = "center" nowrap>
                         <nested:write property ="valueDate" format="dd-MMM-yyyy"/> 
                    </td>
                    <td align="right">
                    <nested:notEqual property="amountDTO.outAmount" value="0">
                    <nested:define id="outAmt" property="amountDTO.outAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(outAmt));
                        %>
                    </nested:notEqual>
                    <nested:notEqual property="amountDTO.inAmount" value="0">
                        <nested:define id="inAmt" property="amountDTO.inAmount" type="java.lang.String"/>
                            <%
                                 out.println(FormatAmount.formatINRAmount(inAmt));
                            %>
                     </nested:notEqual>
                    </td>
                    <% if (txntype.equals("In")) {%>
                    <td align = "center">
                         <nested:write property ="accNo"/> 
                    </td>
                    <td align = "center">
                         <nested:write property ="orderingCustomer"/> 
                    </td>
                    <%}%>
                    <td align="left">
                        <nested:write property="status"/>
                    </td>
                   <% } %>
                   <% if(sno > 1) { %>
                   <tr class="row-value tr1-bc-report">
                   <td><td>
                   <td align="center" width="5%"><%=sno%></td>
                    <td align="middle"><nested:write property="utrNo"/></td>
                    <td align="left"><nested:write property="otherBank"/></td>
                    <td align = "center">
                         <nested:write property ="entDate" format="dd-MMM-yyyy"/> 
                    </td>
                    <td align="right">
                    <nested:notEqual property="amountDTO.outAmount" value="0">
                    <nested:define id="outAmt" property="amountDTO.outAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(outAmt));
                        %>
                    </nested:notEqual>
                    <nested:notEqual property="amountDTO.inAmount" value="0">
                        <nested:define id="inAmt" property="amountDTO.inAmount" type="java.lang.String"/>
                            <%
                                 out.println(FormatAmount.formatINRAmount(inAmt));
                            %>
                     </nested:notEqual>
                    </td>
                    <%if (txntype.equals("In")) {%>
                    <td align = "center">
                         <nested:write property ="accNo"/> 
                    </td>
                    <td align = "center">
                         <nested:write property ="orderingCustomer"/> 
                    </td>
                    <%}%>
                    <td align="left">
                        <nested:write property="status"/>
                    </td>
                   </tr>
                   <% } %>
                </nested:iterate>
                </nested:notEmpty>
               
                <nested:equal property = "msgSubType" value = "R41">
                 <tr class="row-value tr1-bc-report">
                    <td align="right" colspan="6"><nested:message key="label.report.total"/></td>
                    <td align="right"><b>
                    <nested:notEqual property = "grandTotalDTO.iprOutAmount" value ="0">
                     <nested:define id="grtIprOutAmt" property="grandTotalDTO.iprOutAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(grtIprOutAmt));
                        %>
                    </nested:notEqual>
                    <nested:notEqual property = "grandTotalDTO.iprInAmount" value ="0">
                    <nested:define id="grtIprInAmt" property="grandTotalDTO.iprInAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(grtIprInAmt));
                        %>
                    </nested:notEqual>
                        </b>
                    </td>   
                    <td align="center">&nbsp;
                    </td>
                    <td></td>
                    <td></td>
                  </tr>
                </nested:equal>
                    
                <nested:equal property = "msgSubType" value = "R42">
                    <tr class="row-value tr1-bc-report">
                    <td align="right" colspan="6"><nested:message key="label.report.total"/></td>
                    <td align="right"><b>
                    <nested:notEqual property = "grandTotalDTO.cprOutAmount" value ="0">
                     <nested:define id="grtCprOutAmt" property="grandTotalDTO.cprOutAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(grtCprOutAmt));
                        %>
                    </nested:notEqual>
                    <nested:notEqual property = "grandTotalDTO.cprInAmount" value ="0">
                    <nested:define id="grtCprInAmt" property="grandTotalDTO.cprInAmount" type="java.lang.String"/>
                        <%
                             out.println(FormatAmount.formatINRAmount(grtCprInAmt));
                        %>
                    </nested:notEqual>
                        </b>
                    </td>   
                    <td align="center">&nbsp;
                    </td>
                    <td></td>
                    <td></td>
                  </tr>
                </nested:equal>  
                </nested:iterate>
                </nested:notEmpty>
                    <tr class="row-value tr1-bc-report">
                    <td align="right" colspan="6"><nested:message key="label.report.grandTotal"/></td>
                    <td align="right"><b>
                    <nested:notEqual property = "grandTotalDTO.outAmount" value ="0">
                    <nested:define id="grtOutAmt" property="grandTotalDTO.outAmount" type="java.lang.String"/>
                        <%
                            out.println(FormatAmount.formatINRAmount(grtOutAmt));
                        %>
                    </nested:notEqual>
                    <nested:notEqual property = "grandTotalDTO.inAmount" value ="0">
                    <nested:define id="grtInAmt" property="grandTotalDTO.inAmount" type="java.lang.String"/>
                        <%
                            out.println(FormatAmount.formatINRAmount(grtInAmt));
                        %>
                    </nested:notEqual>
                            </b>
                    </td> 
                    <td align="center">&nbsp;</td>
                    <td></td>
                    <td></td>
               </tr>
            </nested:iterate>
            <tr class="row-value tr1-bc-report">
                <td align="right" colspan="4">
                <b><nested:message key ="label.totalnooftxns"/></b>
                <td align="left"><b><nested:write property = "totalTxns"/></b></td>
                <td align="right"><b><nested:message key ="label.netAmount"/></b></td>
                <nested:define id="netAmt" property="netAmount" type="java.lang.String"/>
                <td align="right">
                    <b> <%
                            out.println(FormatAmount.formatINRAmount(netAmt));
                        %></b></td>
                <td align="center">&nbsp;</td>
                <nested:equal property = "tranType" value = "inward">
                <td></td><td></td>
                </nested:equal>
            </tr>
      </table>
     </td>
    </tr>
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function customscript() {
        var title = new String("<%= title %>");
        var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
    
    function showExcel() {
		window.open("D:/rtgsdemo/webapps/rhs/rpt.xls");
    }
    
//-->

</script>
</div>
</nested:form>
