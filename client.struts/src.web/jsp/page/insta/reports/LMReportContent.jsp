<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import="com.objectfrontier.insta.workflow.util.ConversionUtil" %>
<nested:form action="/submitLMReport">
<div id="printReady">
<%
String title = "Liquidity Management Reports <BR> As On ";
title +=ConversionUtil.getCurrentDateTime();
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <TR>
      <TD vAlign=top height="21" >
    <table  class="inner-table table-border" cellSpacing="0" cellPadding="2" width="100%" border="0">
    <br> <br> <br>
    </table>
    <table  class="inner-table table-border" cellSpacing="0" cellPadding="2" width="100%" border="0">
        
       <tr class="row-value tr1-bc">
       <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.credit.sett"/>&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="creditSettAmt" property="creditAmt" type="java.lang.String"/>
                <% out.println("Credut Settlement Amt >> "); %>
                <% out.println(FormatAmount.formatINRAmount(creditSettAmt)); %>
            </td>
        </tr>
    
        <tr class="row-value tr1-bc">
            <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.inward.cpr"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="inwCpr" property="inwCprTotal" type="java.lang.String"/>
                <% out.println(FormatAmount.formatINRAmount(inwCpr)); %>
            </td>
        </tr>
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.inward.ipr"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="inwIpr" property="inwIprTotal" type="java.lang.String"/>
                <% out.println(FormatAmount.formatINRAmount(inwIpr)); %>
            </td>
        </tr>
        
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
            <nested:message key="label.sub"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="credit" property="creditTotal" type="java.lang.String"/>
                <b><% out.println(FormatAmount.formatINRAmount(credit)); %></b>
            </td>
        </tr>   
        
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.outward.cpr"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="outCpr" property="outCprTotal" type="java.lang.String"/>
                <% out.println(FormatAmount.formatINRAmount(outCpr)); %>
            </td>
        </tr>
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.outward.ipr"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="outIpr" property="outIprTotal" type="java.lang.String"/>
                <% out.println(FormatAmount.formatINRAmount(outIpr)); %>
            </td>
        </tr>
         <tr class="row-value tr1-bc">
         <td width = "10%"></td>
             <td align ="left">
                <nested:message key="label.debit.sett"/> &nbsp;&nbsp;&nbsp;
             </td>
             <td align ="right"><nested:message key="label.equal"/></td>
             <td align = "right">
                <nested:define id="debitSettAmt" property="debitAmt" type="java.lang.String"/>
                <% out.println(FormatAmount.formatINRAmount(debitSettAmt)); %>
             </td>
        </tr>
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
            <nested:message key="label.sub1"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="debit" property="debitTotal" type="java.lang.String"/>
                <b> <% out.println(FormatAmount.formatINRAmount(debit)); %> </b>
            </td>
        </tr> 
        <tr class="row-value tr1-bc">
        <td width = "10%"></td>
            <td align ="left">
                <nested:message key="label.liquidity"/>
            </td>
            <td align ="right"><nested:message key="label.equal"/></td>
            <td align = "right">
                <nested:define id="liqudity" property="liquidityamt" type="java.lang.String"/>
               <b> <% out.println(FormatAmount.formatINRAmount(liqudity)); %></b>
            </td>
        </tr>
</table>
    </td>
    </tr>
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function printScript() {
    
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