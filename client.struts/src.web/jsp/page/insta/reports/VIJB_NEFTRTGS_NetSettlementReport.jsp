<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Iterator" %>
<%@page import="com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="com.objectfrontier.neft.report.dto.NEFT_RTGSNetSettlementDTO"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/neftRtgsNetSettlement">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
    String title = "NEFT Reconciliation with Net Positions received from RTGS";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr class="row-value">
		 <td align="left"><b>Net Settlements received from RTGS</b></td><td>&nbsp;</td>
	</tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
		 
            <tr class="row-value">
              <td colspan="2" align="left">
                  <b>
                    Report Date
                  </b>
              </td>
              <td colspan="6" align="left" class="row-value">
                        <!-- Here all the input details should be here -->
                  <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
                  <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
                  <b>
                  	<%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>
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
                BigDecimal totAmt = BigDecimal.ZERO;
                BigDecimal tempAmt = new BigDecimal("-1");
           		boolean k= false;
            %>
            <nested:define id="code" property="NEFTRTGS_NetSettleKeyword" type="java.lang.String"/>
            <nested:define id="_NEFTRTGS_settlementList" property="NEFTRTGS_settlementList" type="java.util.List"/>

            	<tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Msg Type</td>
                <td width="20%" align="center" >Ordering Institution</td>
                <td width="20%" align="center" >Code</td>
                <td width="20%" align="center" >Info</td>
                <td width="20%" align="center" >Additional Info</td>
                <td width="20%" align="center" >Amount</td>
                </tr>
                <% if(_NEFTRTGS_settlementList != null && _NEFTRTGS_settlementList.size() > 0) { %>
                	<% int z = 0; %>
                	<%	for(Iterator iter = _NEFTRTGS_settlementList.iterator(); iter.hasNext();) {
                	    NEFT_RTGSNetSettlementDTO dto = (NEFT_RTGSNetSettlementDTO) iter.next(); %>
	                	<% k = k ? false : true;
	              			if (k) {%><tr class="row-value tr1-bc">
						<% 	} else { %><tr class="row-value tr2-bc">
			        	<% 	} %>
	                	<%  z  += 1; %>
	                	<td align="center" width="5%"><%=z%></td>
	                	<td width="15%" align="center" ><%=dto.getMsgType()%><%=dto.getMsgSubType()%></td>
	                	<td width="20%" align="center" ><%=dto.getOrderingInstitution()%></td>
	                	<td width="20%" align="right" ><%=code%></td>
	                	<td width="20%" align="center" ><%=dto.getInfo()%></td>
	                	<td width="20%" align="right" ><%=dto.getAdditionalInfo()%></td>
	                	<td width="20%" align="center" ><%=FormatAmount.formatINRAmount(dto.getAmount())%></td>
						<% totAmt = totAmt.add(new BigDecimal(dto.getAmount()));
						   totAmt = totAmt.setScale(2);
                		%> 
	                	
                	<% } } else { %>
                	<tr class="row-value tr2-bc">
                		<td colspan="12" align="center"><b>	No Records Found...   </b></td>
            		</tr>
                <% } %>

                <tr class="row-value tr2-bc">
                		<td colspan="2" align="center"><b>	&nbsp; </b></td>
            	</tr>
				<tr class="row-value">
            		<td align="left" width="30%" colspan="2">
            			<b>LMS NEFT Aggregate</b>
            		</td>
            		<td align="left" width="5%" colspan="2">&nbsp;
            		</td>
            	</tr>
            	
            	<tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Batch Time</td>
                <td width="20%" align="center" >Aggregate Amount <br>(Total Credit-Total Debit)</td>
                </tr>
              	<% int z = 0;  %>
                <nested:notEmpty property="NEFTRTGS_settlementMap">
            		<nested:iterate id="_settlementMap" property="NEFTRTGS_settlementMap">
	 	             	<nested:define id="_batchTime" name="_settlementMap" property="key" type="java.lang.String" />
	    	            <nested:define id="_totalAmount" name="_settlementMap" property="value" type="java.lang.String" />

                  		<%if (z%2 == 0) {%><tr class="row-value tr1-bc">
						<% 	} else { %><tr class="row-value tr2-bc">
			        	<% 	} 
                  		z  += 1; %>
	                	<td align="center" width="5%"><%=z%></td>
	                	<td width="15%" align="center" ><%=_batchTime%></td>
	                	<% if(new BigDecimal(_totalAmount).compareTo(BigDecimal.ZERO) < 0){
	                	    
	                	     tempAmt = tempAmt.multiply(new BigDecimal(_totalAmount));
	                	    _totalAmount = tempAmt.toString();
	                	   }   
	                	%>
	                	<td width="20%" align="center" ><%=FormatAmount.formatINRAmount(_totalAmount)%></td>
	                	</tr>
                	</nested:iterate>
                	</nested:notEmpty>
                	<nested:empty property="NEFTRTGS_settlementMap">
                	<tr class="row-value tr2-bc">
                		<td colspan="12" align="center"><b>	No Records Found... </b></td>
            		</tr>
            		</nested:empty>
              
		     </TABLE> 
    </td>
	</tr>	
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function printScript() {

        var title = '<%=title%>';
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
    
//-->

</script>
</div>
</nested:form>
