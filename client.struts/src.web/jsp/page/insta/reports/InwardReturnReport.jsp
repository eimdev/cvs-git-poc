<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>

<%@page import="java.util.Date"%>
<nested:form action="/inwardreturnreport">

<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr class="row-value">
        <td align="right">
           <b>
             Report Printed on <nested:write property="currentReportPrintTime"/>
           </b>
        </td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td width="5%" align="center">SNo</td>
                <td width="15%" align="center" >UTR No</td>
                <td width="15%" align="center" >Value Date</td>
                <td width="25%" align="center" >Sender Address</td>
                <td width="25%" align="center" >Receiver Address</td>
                <td width="20%" align="center" >Amount</td>
           </tr>     
              	<% int i=1; %>
				<nested:notEmpty property="reportDTOs">
				   <nested:iterate property="reportDTOs">
        			<% if(i%2==0) { %>  
        			<tr class="row-value tr2-bc">
        			<% } else { %>
        			<tr class="row-value tr1-bc">
        			<% } %>
						<td align="center" width="30"><%=i%></td>
	                   	<td align="center" nowrap>&nbsp;<nested:write property="utrNo"/></td>
						<td align="center" nowrap>&nbsp;<nested:write property="valueDate"/></td>                        
                       	<td align="center" >&nbsp;<nested:write property="senderAddress"/></td>
	                    <td align="center" >&nbsp;<nested:write property="receiverAddress"/></td>
                    	<nested:define id="txnAmt" property="amt" type="java.lang.String"/>
	                    <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
               		</tr>
        			<% i++; %>	       
                        </nested:iterate>
				  </nested:notEmpty>
				   <nested:empty property="reportDTOs">
        			<tr class="row-value tr2-bc">
						<td colspan=6 align="center">	No Data Found.   </td>          		   		        		   		
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
    
    	var title = new String("Inward Messages Returned");

		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->

</script>
</div>
</nested:form>
