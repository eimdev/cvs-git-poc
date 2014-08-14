<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="NEFTRHSStandard.jsp"%>
<%@ page import="com.objectfrontier.insta.reports.server.util.FormatAmount" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils" %>
<%@ page import="com.objectfrontier.insta.reports.constants.ReportConstants"%>
<%@ page import="java.util.Iterator, java.util.List, java.util.ArrayList"%>
<%@ page import="com.objectfrontier.neft.report.dto.SummaryInfo.SummaryInfoElement"%>
<%@ page import="com.objectfrontier.neft.report.dto.TransactionInfo"%>
<%@ page import="com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean"%>

<nested:form action="/neftOutwardSummaryReport">
<div id="printReady">
<%
 String title = ReportConstants.title;
 String imagePath = imageBase + "/print.jpg";
%>
<!-- Table 1 Starts Here -->
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td align=left class=column-label width="244" height="9">
            <b><nested:message key="label.branchmaster.branchcode"/></b>:
               <nested:write property="reportDto.branchCode"/>               
        </td>
    </tr>
     <tr>
        <td align=left class=column-label width="244" height="9">
            <b><nested:message key="label.batch.date"/></b>:
            <nested:write property="reportDto.valueDate" /> 
            <nested:define id="rDate" property="reportDto.valueDate" />
        </td>
    </tr>
    <tr>
        <td align=left class=column-label width="244" height="9">
            <b><nested:message key="label.listmessages.batchtime"/></b>:
               <nested:write property="reportDto.batchTime" format="hhmm"/>               
        </td>
    </tr>
    <tr>
        <td align=left class=column-label width="244" height="9">
            <b><nested:message key="label.report.reportType"/></b>:
               <nested:write property="reportDto.reportType"/>               
        </td>
    </tr>
    
    <tr>
        <td class=column-label width="244" height="9">
    	    <b><nested:message key="label.generatedby"/></b>: 
 	       <nested:write property="neftRepDTO.reportRunBy"/>     
        </td>
    </tr>
    
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    
   <tr>
	<td vAlign=top>
	    <nested:define id="reportType" property="reportDto.reportType" type="java.lang.String"/>
		<nested:equal name="reportType" value="Detailed">
		<!-- Inner Table 2 Starts Here -->
		<TABLE width="75%" border="0">

		<!-- Get Data's From Map -->
		
		<nested:define id="outwardDataMap" property="neftRepDTO.outwardMap"/>
        
		<nested:iterate id ="element" name="outwardDataMap">
			<nested:define id="elementKey" name="element" property="key"/>                        
			<nested:define id="elementValue" name="element" property="value"/>
		<tr></tr><tr></tr>
		<TR> 

			<TH align="center" class="row-label s"><%out.println((String)elementKey); %> 
			</TH> 
		</TR>
		
		<TR>
		<TD vAlign=top height="21" >
		<!-- Inner Table 3 Starts here -->
		<table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td rowspan=2 width="5%" align="center"><nested:message key="label.serialno"/></td>
				<td rowspan=2 width="5%" align="center"><nested:message key="label.batch.time"/></td>
				<td rowspan=2 width="5%" align="center"><nested:message key="label.valuedate"/></td>
				<td rowspan=2 align="center">&nbsp;<nested:message key="label.senderifsc"/></td>
				<td rowspan=2 align="center">&nbsp;<nested:message key="label.beneficiaryifsc"/></td>         
                <td rowspan=2 align="center"><nested:message key="label.transaction.ref"/></td>
                <td rowspan=2 align="center"><nested:message key="label.rtgsbaltxn.txnamount"/></td>
                <td colspan=3 align="center"><nested:message key="label.sender"/></td>
                <td colspan=3 align="center"><nested:message key="label.beneficiary"/></td>
                <td rowspan=2 align="center"><nested:message key="label.messageStatus"/></td>
           </tr>
			    <tr class="row-label th-bc">
                	<td align="center"><nested:message key="label.sender.account.type"/></td>
	                <td align="center"><nested:message key="label.sender.account.number"/></td>
    	            <td align="center"><nested:message key="label.sender.account.name"/></td>                
                    <td align="center"><nested:message key="label.beneficiary.acctype"/></td>
                	<td align="center"><nested:message key="label.beneficiary.no"/></td>
                	<td align="center"><nested:message key="label.beneficiary.name"/></td>
				</tr>
			

	    <%
				
			List detailList = (ArrayList)elementValue;

			if (detailList != null && detailList.size() >= 1 ) { 
				
				int i=1; 
		
				for (int count1=0; count1 < detailList.size(); count1++) { 
					TransactionInfo transactionInfo = (TransactionInfo) (detailList.get(count1)); 

					if(i%2==0) { %>  
					<tr class="row-value tr2-bc">
					<% } else { %>
					<tr class="row-value tr1-bc">
					<% } %>
					<td align="center" width="30"><%=i%></td>
                    
                    <% if (transactionInfo.batchTime == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.batchTime%></td>
					<% } %>
                    
                    <% if (transactionInfo.valueDate == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.valueDate%></td>
					<% } %>
                    
                    <% if (transactionInfo.senderInfo.accIfsc== null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.senderInfo.accIfsc%></td>
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accIfsc == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.beneficiaryInfo.accIfsc%></td>
					<% } %>
                    
                    <% if (transactionInfo.utrNo == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.utrNo%></td>
					<% } %>
					
					<td align="right">&nbsp;<%= FormatAmount.formatINRAmount(transactionInfo.amount)%></td>
                    
					<% if (transactionInfo.senderInfo.accType == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.senderInfo.accType%></td>
					<% } %>
                    
                    <% if (transactionInfo.senderInfo.accNo == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.senderInfo.accNo%></td>
					<% } %>
                    
                    <% if ( transactionInfo.senderInfo.accName== null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=  transactionInfo.senderInfo.accName%></td>
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accType == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.beneficiaryInfo.accType%></td>  
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accNo == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.beneficiaryInfo.accNo%></td>  
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accName == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.beneficiaryInfo.accName%></td>  
					<% } %>
                    
                    <% if (transactionInfo.currentStatus == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.currentStatus%></td>  
					<% } %>
					
				</tr>
				<% i++; %>
				<%}%>
		<!-- Inner Table 3 Ends Here -->
	</table>
	  </TD>
	  </TR>
			<% } else { %>
					
				<tr class="row-value tr2-bc">
				<td align="center" colspan=50>
				<b><nested:message key="label.report.noReport"/></b> </td>
				</tr>
			   </table>
				</TD>
				</TR>

			<%	} %>
        </nested:iterate>


	  </TD>
	  </TR>
	<!--  Inner Table 2 (Detailed) Ends Here -->
	</TABLE>	
	</nested:equal>


	<nested:equal name="reportType" value="Summary">
		
	<!-- Same Inner 2 Table (Summary) As Per report Type Either This Table Or The Previous Inner Table Will Execute-->
	<TABLE width="75%" border="0">
		
		<nested:define id="outwardDataMap1" property="neftRepDTO.outwardMap"/>
		
		<nested:notEmpty property="neftRepDTO.outwardMap">

		<nested:iterate id ="element" name="outwardDataMap1">
		
			<nested:define id="elementKey1" name="element" property="key"/>                        
			<nested:define id="elementValue1" name="element" property="value"/>
 			

		<TR> <TD> &nbsp;</TD></TR> 	      <TR> <TD> &nbsp;</TD></TR>
		<TR> <TH align="center"> <nested:message key="title.summary.txns"/> </TH> </TR>
		<TR>
		<TD vAlign=top height="21" >
			<!-- Inner Table 3 Starts Here -->
			<table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
				<tr class="row-label th-bc">
					<td align="center">&nbsp</td>
					<td align="center"><nested:message key="label.transactions"/></td>                
					<td align="right"><nested:message key="label.rtgsbaltxn.txnamount"/></td>
				</tr>
	    <%
				
			List summaryList = (ArrayList)elementValue1;

			if (summaryList != null && summaryList.size() >= 1 ) { 
				
				int k=1; 
		
				for (int count2=0; count2 < summaryList.size(); count2++) {
					SummaryInfoElement summaryInfoElement = (SummaryInfoElement)summaryList.get(count2);
			
				if(k%2==0) { %>  
				<tr class="row-value tr2-bc">
				<% } else { %>
				<tr class="row-value tr1-bc">
				<% } %>
					<td align="center">&nbsp;<b><%= summaryInfoElement.heading %></b></td> 
					<td align="center">&nbsp;<%= summaryInfoElement.count %></td>  
					<td align="right">&nbsp;<%= FormatAmount.formatINRAmount(summaryInfoElement.amount) %></b></td>          		   		
				</tr>
			<% k++; %>
			<% } %>
			
			<% } else { %>
					
				<tr class="row-value tr2-bc">
				<td align="center" colspan=50>
				<b><nested:message key="label.report.noReport"/></b> </td>
				</tr>
			<%	} %>

			</nested:iterate>
 		 </nested:notEmpty>
    <!-- Inner Table 3 Ends Here -->
	  </table>
	</TD></TR>

	<!-- End For Inner Table 2 (Summary) --> 
	</TABLE>	
	<!-- Equals Summary -->
</nested:equal>


	</td>
	</tr>
    <tr> <td> &nbsp; </td> </tr> <tr> <td> &nbsp; </td> </tr>

    <TR><TD>&nbsp;</TD></TR>
    <TR><TD>&nbsp;</TD></TR>
    <TR><TD>&nbsp;</TD></TR>
  
    <tr><td><b><div align="right" id = "printDate"  > Generated On <%=ConversionUtils.getCurrentDateTime()%></div></td></tr></b>
      
<!-- Final Table Ends Here -->
</TABLE>

<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
</div>
<script>
<!--
document.getElementById("printDate").style.display = "none";
    function printScript() { 
    	document.getElementById("printDate").style.display = "";
        var title = new String("<%=title%>");
        var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
        document.getElementById("printDate").style.display = "none";
    }

//-->

</script>


</nested:form>