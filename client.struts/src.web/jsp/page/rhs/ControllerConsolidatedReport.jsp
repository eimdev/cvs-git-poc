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
<% int i = 0; %>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td class="error" height="10"><nested:errors/></td>
	</tr>
	<tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
	 <TR>
      <TD vAlign=top height="21" >
      
       <table  class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
        <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
        <tr class="row-label th-bc">
            <td>
                <nested:message key="label.fromdate"/> 
            </td>
            <td>
                <nested:write property="reportDTO.fromDate" format="dd-MM-yyyy"/>
            </td>
            <td></td><td></td><td></td><td></td>
            </tr>
        <tr class="row-label th-bc">
                <td>
                    <nested:message key="label.todate"/> 
                </td>
                <td>
                    <nested:write property="reportDTO.toDate" format="dd-MM-yyyy"/>
                </td>
                <td></td><td></td><td></td><td></td>
        </tr>
        
        </nested:notEqual>
        <nested:notEmpty property="utrWise">
        <nested:define property="utrWise" id="utr"/>
        </nested:notEmpty>
          
        <tr class="row-label th-bc">
            <td align="middle"><nested:message key="label.branch.ifsc"/></td>
            <td align="middle"><nested:message key="label.serialno"/></td>
            <td align="middle"><nested:message key="label.txntype"/></td>
            <td align="middle"><nested:message key="label.subtype"/></td>
            <td align="middle"><nested:message key="label.valuedate"/></td>
            <td align="middle"><nested:message key="label.amount"/></td>
        </tr>
        <nested:notEmpty property = "ifscMap">
        <nested:define id="_ifscMap" property="ifscMap"/>
        <nested:iterate indexId = "index" id ="element" name="_ifscMap">
        
                    
        <nested:define id ="_dateMap" name="element" property="value" type = "java.util.TreeMap"/>
        <tr class="row-value tr1-bc">
        <td>
            <nested:write name="element" property="key"/>
        </td>
        
        <nested:iterate indexId = "index1" id ="element1" name="_dateMap">
        
        <nested:define id ="_reportDTO" name="element1" property="value"/>
        <nested:define id = "cprInAmt" name = "_reportDTO" property = "dateCprInAmt" type="java.lang.String"/>
        <nested:define id = "iprInAmt" name = "_reportDTO" property = "dateIprInAmt" type="java.lang.String"/>
        <nested:define id = "cprOutAmt" name = "_reportDTO" property = "dateCprOutAmt" type="java.lang.String"/>
        <nested:define id = "iprOutAmt" name = "_reportDTO" property = "dateIprOutAmt" type="java.lang.String"/>
        <nested:define id = "grantAmt" name = "_reportDTO" property = "dateGrantTolal" type="java.lang.String"/>
        <!--nested:define id = "dto" name = "element" property = "ifsc"/>-->
          
        <% 
         BigDecimal cprAmtIn = new BigDecimal(cprInAmt);
         BigDecimal iprAmtIn = new BigDecimal(iprInAmt);
         BigDecimal cprAmtOut = new BigDecimal(cprOutAmt);
         BigDecimal iprAmtOut = new BigDecimal(iprAmtOut);
         
        %>
         <% int sno1  = index1.intValue()+1; %>
         <% if(sno1 == 1) {%>
         
        <td align="center" width="5%"><%=sno1%></td>
            <td>
                <% out.write("Outward"); %>
            </td>
            <td>
                <% out.write("R41"); %>
            </td>
            <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
            </td>
            <td align="right">
                <%out.println(FormatAmount.formatINRAmount(cprOutAmt));%>
            </td>
            <tr class="row-value tr1-bc-report">
                <td></td><td></td>
            <td>
                <%out.write("Outward");%>
            </td>
            <td>
                <%out.write("R42");%>
            </td>
            <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
            </td>
            <td align="right">
                <%out.println(FormatAmount.formatINRAmount(iprOutAmt));%>
            </td>
            </tr>
            <tr class="row-value tr1-bc-report">
                <td></td><td></td>
                <td><%out.write("Inward");%></td>
                <td>
                <%out.write("R41");%>
                </td>
                <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
                </td>
                <td align="right">
                <%out.println(FormatAmount.formatINRAmount(cprInAmt));%>
                </td>
            </tr>
             <tr class="row-value tr1-bc-report">
                <td></td><td></td>
                <td><%out.write("Inward");%></td>
                <td>
                <%out.write("R42");%>
                </td>
                <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
                </td>
                <td align="right">
                <%out.println(FormatAmount.formatINRAmount(iprInAmt));%>
                </td>
            </tr>   
        <tr class="row-value tr1-bc-report">
            <td align="right" colspan="5">
                <b><nested:message key = "label.report.grandTotal"/></b>
            </td>
            <td align="right">
             <% out.println(FormatAmount.formatINRAmount(grantAmt)); %>
            </td>
        </tr>
        <% } %>
        
        <% if(sno1 > 1) {%>
        <tr class="row-value tr1-bc-report">
        <td></td>
        <td align="center" width="5%"><%=sno1%></td>
            <td>
                <% out.write("Outward"); %>
            </td>
            <td>
                <% out.write("R41"); %>
            </td>
            <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
            </td>
            <td align="right">
                <%out.println(FormatAmount.formatINRAmount(cprOutAmt));%>
            </td>
            <tr class="row-value tr1-bc-report">
                <td></td><td></td>
            <td>
                <%out.write("Outward");%>
            </td>
            <td>
                <%out.write("R42");%>
            </td>
            <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
            </td>
            <td align="right">
                <%out.println(FormatAmount.formatINRAmount(iprOutAmt));%>
            </td>
            </tr>
            <tr class="row-value tr1-bc-report">
                <td></td><td></td>
                <td><%out.write("Inward");%></td>
                <td>
                <%out.write("R41");%>
                </td>
                <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
                </td>
                <td align="right">
                <%out.println(FormatAmount.formatINRAmount(cprInAmt));%>
                </td>
            </tr>
             <tr class="row-value tr1-bc-report">
                <td></td><td></td>
                <td><%out.write("Inward");%></td>
                <td>
                <%out.write("R42");%>
                </td>
                <td align = "left">        
                   <b><nested:write name="_reportDTO" property="valueDate" format="dd-MMM-yyyy"/></b>
                </td>
                <td align="right">
                <%out.println(FormatAmount.formatINRAmount(iprInAmt));%>
                </td>
            </tr>  
            <tr>            
        <tr class="row-value tr1-bc-report">
            <td align="right" colspan="5">
                <b><nested:message key = "label.report.grandTotal"/></b>
            </td>
            <td align="right">
                <% out.println(FormatAmount.formatINRAmount(grantAmt)); %>
            </td>
        </tr>
        
        <% } %>
        </nested:iterate> 
    </nested:iterate> 
    <tr class="row-value tr1-bc-report">
        <td align="right" colspan="5"><b><nested:message key ="label.netAmount"/></b></td>
        <nested:define id="netAmt" property="netAmount" type="java.lang.Double"/>
            <td align="right">
                <b> <%
                        out.println(FormatAmount.formatINRAmount(netAmt));
                    %></b>
            </td>
    </tr>
        </nested:notEmpty>
        
        <nested:empty property = "ifscMap">
        <tr class=""row-value tr1-bc""> 
          <td class="row-value tr1-bc" height="5" colspan="9" align="center">
                    <nested:message key="label.report.noReport"/>
              </td></tr>
        </nested:empty>
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
