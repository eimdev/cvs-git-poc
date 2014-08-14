<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/neftReconcilliation">
<div id="printReady">
<%
String imagePath = imageBase + "/print.jpg";
%>

<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr>
    <tr class="row-value">
    	<td align="center" class="row-value">
    	<nested:define id ="format" property="dateFormat" type="java.lang.String"/>
         <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
            <b>
            	<font size="2.0">
                	NEFT Reconciliation Report on <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
                </font>
            </b>
    	</td>
    </tr>
     <TD vAlign=top>
        <table width="100%" border="0" cellspacing="1" cellpadding="2">
          <tr class="row-value">
        	<td align="right">
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
		</table>
	</td>
	</tr>

	<tr>
	    <td>
		          <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td width="5%" align="center">&nbsp;</td>
                <td align="center"><nested:message key="label.batch.time"/></td>
                <td align="center"><nested:message key="label.gross.outward"/></td>
                <td align="center"><nested:message key="label.prev.batch.rescheduled"/></td>
                <td align="center"><nested:message key="label.next.batch.rescheduled"/></td>
                <td align="center"><nested:message key="label.rejected"/></td>
                <td align="center"><nested:message key="label.net.outward"/></td>
                <td align="center"><nested:message key="label.net.Inward"/></td>
                <td align="center"><nested:message key="label.aggregate"/></td>
                	<% int i=1; %>
					<nested:notEmpty property="newReportDto.reconcileReportDTOs">
				   <nested:iterate property="newReportDto.reconcileReportDTOs">
        			<% if(i%2==0) { %>  
        			<tr class="row-value tr2-bc">
        			<% } else { %>
        			<tr class="row-value tr1-bc">
        			<% } %>
						<nested:empty property="heading">
	 						<td align="center" width="30"><%=i%></td>						
                        </nested:empty>

						<nested:notEmpty property="heading">
							<td nowrap><b><nested:write property="heading"/></b></td>
                        </nested:notEmpty>
                        
						<td align="right" nowrap>&nbsp;<nested:write property="batchTime"/></td>
                        <td align="right" nowrap>&nbsp;<nested:write property="grossOutwardAmount" format="0.00"/></td>
                        <td align="right" nowrap>&nbsp;<nested:write property="rescheduledPrevBatchAmt" format="0.00"/></td>
                        <td align="right" nowrap>&nbsp;<nested:write property="rescheduledNextBatchAmt" format="0.00"/></td>
   	       		   		<td align="right" nowrap>&nbsp;<nested:write property="rejectedAmt" format="0.00"/></td>
                        <td align="right" nowrap>&nbsp;<nested:write property="netOutwardAmt" format="0.00"/></td>
   	       		   		<td align="right" nowrap>&nbsp;<nested:write property="netInwardAmt" format="0.00"/></td>
   	       		   		<td align="right" nowrap>&nbsp;<nested:write property="aggregateAmt" format="0.00"/></td>
               		</tr>
        			<% i++; %>	       
                        </nested:iterate>
				  </nested:notEmpty>
				   <nested:empty property="newReportDto.reconcileReportDTOs">
        			<tr class="row-value tr2-bc">
								<td colspan=50>	<nested:message key="label.report.noReport"/>   </td>          		   		        		   		
               		</tr>
            	</nested:empty>

	  </table>
    </td>
	</tr>
	
	<tr>
	   <td> &nbsp; </td>
	</tr>
	    <tr>
			<td align="center" class="row-value"><b><font size="2.0"><nested:message key="label.consolidated.report"/></font></b></td>
	    </tr>		 
    <TR>
      <TD vAlign=top height="21" >
      <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td align="center">&nbsp;&nbsp;</td>
                <td align="center"><nested:message key="label.rbi.acc"/></td>
                <td align="center"><nested:message key="label.neft.acc"/></td>
    		  <nested:notEmpty property = "newReportDto.consolidatedReportDTO.consolidatedReportDTOs"> 
				   <nested:iterate property="newReportDto.consolidatedReportDTO.consolidatedReportDTOs">
        			    <tr class="row-value tr1-bc">
						    <td nowrap align="center" ><nested:write property="heading"/></td>
                        	<td align="right" nowrap>&nbsp;<nested:write property="RBIAccountAmt" format="0.00"/></td>
	                        <td align="right" nowrap>&nbsp;<nested:write property="NEFTAccountAmt" format="0.00"/></td>
               		</tr>
                  </nested:iterate>
			  </nested:notEmpty>
      </table>
     </td>
    </tr>

      <TR><TD>&nbsp;</TD></TR>
      <TR><TD>&nbsp;</TD></TR>
      <TR><TD>&nbsp;</TD></TR>
    
    <tr><td><b><div align="right" id = "printDate"  ></div></td></tr></b>
    
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
</div>

<script>
<!--
    function printScript() {
    
        var title = new String("");	
		
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
//-->

</script>
</nested:form>
