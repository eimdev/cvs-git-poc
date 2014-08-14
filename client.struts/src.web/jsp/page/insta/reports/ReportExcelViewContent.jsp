<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import="com.objectfrontier.insta.workflow.util.ConversionUtil" %>
<%@page import="java.math.BigDecimal"%>
<nested:form action="/submitExcelReport">
<div id="printReady">
<%
String title = " ExcelSheet Reports <BR> As On ";
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
            <nested:equal property = "tranType" value = "outward">
            <tr class="row-label th-bc">
                <td align="middle"><nested:message key="label.serialno"/></td>
                <td align="middle"><nested:message key="label.branchname"/></td>
                <td align="middle"><nested:message key="label.receiver.ifsc"/></td>
                <td align="middle"><nested:message key="label.amount"/></td>
                <td align="middle"><nested:message key="label.rtgsbaltxn.utrnumber"/></td>
                <td align="middle"><nested:message key="label.listmessages.status"/></td>
            </tr> 
            </nested:equal>
            <nested:equal property = "tranType" value = "inward">
            <tr class="row-label th-bc">
                <td align="middle"><nested:message key="label.serialno"/></td>
                <td align="middle"><nested:message key="label.bene.name"/></td>
                <td align="middle"><nested:message key="label.amount"/></td>
                <td align="middle"><nested:message key="label.accno"/></td>
                <td align="middle"><nested:message key="label.rtgsbaltxn.utrnumber"/></td>
                <td align="middle"><nested:message key="label.remitter.name"/></td>
                <td align="middle"><nested:message key="label.listmessages.status"/></td>
            </tr> 
            </nested:equal>
            
            <nested:equal property = "tranType" value = "outward">
            <nested:notEmpty property = "branchDTOs">
            <%
              			boolean k= false;
            %>
            <nested:iterate indexId = "index" id ="reportDtos" property="branchDTOs"> 
            
                <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td align="left">
                <nested:write property = "ifsc"/>
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
                <td align="left">
                <nested:write property = "status"/>
                </td>
               </tr>
            </nested:iterate>
             </nested:notEmpty>
             <nested:empty property = "branchDTOs">
             	<tr class="row-value tr1-bc">
             		<td align="center" colspan="6">
             			No Records Found
             		</td>
             	</tr>
             </nested:empty>
            </nested:equal>
            <nested:equal property = "tranType" value = "inward">
            <nested:notEmpty property = "branchDTOs">
            <%
              			boolean j= false;
            %>
            <nested:iterate indexId = "index" id ="reportDtos" property="branchDTOs"> 
                <% j = j ? false : true;
              		if (j) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td align="left">
                <nested:write property = "beneficiaryName"/>
                </td>
                <td align="right">
                <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                <%
                    out.println(FormatAmount.formatINRAmount(txnAmt));
                %>
                </td>
                <td align="left">
                <nested:define id="_accNo" property="accNo" type="java.lang.String"/>
                <% if(_accNo != null && _accNo.length() > 0) { 
                    if(_accNo.length() > 25) {
                        String value = "";
                        for(int k1 = 0;k1< _accNo.length();k1++){ 
						       char a = _accNo.charAt(k1);
						       value += a;
						       if(k1 > 1 && k1%25 == 0) { %>
						          <% value += " "; %>
						       <% } %>
					  <%  } %> 
					  <%=value%>
                 <% } else { %>
                    <%=_accNo%>  
                 <% } %>
                 <% } %>
                </td>
                <td align="left">
                <nested:write property = "utrNo"/>
                </td>
                <td align="left">
                <nested:write property = "remitterName"/>
                </td>
                <td align="left">
                <nested:write property = "status"/>
                </td>
               </tr>
               </nested:iterate>
               </nested:notEmpty>
               <nested:empty property = "branchDTOs">
             	<tr class="row-value tr1-bc">
             		<td align="center" colspan="7">
             			No Records Found
             		</td>
             	</tr>
             </nested:empty>
            </nested:equal>
            
            <tr class="row-label th-bc">
            <nested:equal property = "tranType" value = "outward">
            <td align="right" colspan="3">
            </nested:equal>
            <nested:equal property = "tranType" value = "inward">
            <td align="right" colspan="2">
            </nested:equal>
            <b><nested:message key="label.report.total"/></b>
            </td>
            <nested:define id="netAmt" property="netAmount" type="java.lang.String"/>
                <td align="right">
                    <b> <%
                            out.println(FormatAmount.formatINRAmount(netAmt));
                        %></b>
                </td>
                <nested:equal property = "tranType" value = "outward">
                <td></td><td></td>
                </nested:equal>
                <nested:equal property = "tranType" value = "inward">
                <td></td><td></td><td></td><td></td>
                </nested:equal>
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
//-->

function resetValue() {
		
		document.forms[0].action="/insta/reports/submitExcelReport.do?mode=controller&action=reset&module=reports";
		document.forms[0].submit();
	}

</script>
</div>
</nested:form>