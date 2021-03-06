<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import="com.objectfrontier.insta.workflow.util.ConversionUtil" %>
<nested:form action="/submitDayEndReport">
<div id="printReady">
<%
String title = " DayEnd Reports- Outward <BR> As On ";
title +=ConversionUtil.getCurrentDateTime();
String imagePath = imageBase + "/print.jpg";
%>
<% java.util.List list = new java.util.ArrayList(); %>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <!-- RBC CMD 1.0 -->
    <TR>
      <TD vAlign=top height="21" >
      
        <table  class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
            
            <tr class="row-label th-bc">
                <td align="middle"><nested:message key="label.serialno"/></td>
                <td align="middle"><nested:message key="label.branchname"/></td>
                <td align="middle"><nested:message key="label.receiver.ifsc"/></td>
                <td align="middle"><nested:message key="label.amount"/></td>
                <td align="middle"><nested:message key="label.rtgsbaltxn.utrnumber"/></td>
            </tr> 
            
            <nested:notEmpty property = "branchDTOs">
            <%
              			boolean k= false;
            %>	
            <nested:iterate indexId = "index" id ="newReportDto" property="branchDTOs"> 
            
                <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td align="left">
                IFSC >> <nested:write property = "ifsc"/>
                </td>
                <td align="left">
                <nested:write property = "branchName"/>
                </td>
                <td align="right">
                <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                <%
                    out.println(FormatAmount.formatINRAmount(txnAmt));
                %>
                </td>
                <td align="left">
                <nested:write property = "utrNo"/>
                </td>
               </tr>
            </nested:iterate>
            </nested:notEmpty>
            <nested:empty property = "branchDTOs">
               		<tr class="row-value tr1-bc">
               			<td colspan ="5" align="center">
               				No Records Found...
               			</td>
               		</tr>
            </nested:empty>
            <tr class="row-label th-bc">
           <td align="right" colspan="3">
            <b><nested:message key="label.report.total"/></b>
            </td>
            <nested:define id="netAmt" property="netAmount" type="java.lang.String"/>
                <td align="right">
                    <b> <%
                            out.println(FormatAmount.formatINRAmount(netAmt));
                        %></b>
                </td>
                <td></td>
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