<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<!-- <%@ include file="NEFTRHSStandard.jsp"%>
<%@ page import="com.objectfrontier.insta.reports.server.util.FormatAmount" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils" %>
<%@ page import="com.objectfrontier.insta.reports.constants.ReportConstants"%>
<%@ page import="java.util.Iterator, java.util.List, java.util.ArrayList"%>
<%@ page import="com.objectfrontier.neft.report.dto.SummaryInfo.SummaryInfoElement"%>
<%@ page import="com.objectfrontier.neft.report.dto.TransactionInfo"%>
<%@ page import="com.objectfrontier.insta.neft.reports.bean.InstaNEFTReportBean"%>
<%@ page import="com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<!-- <link rel="stylesheet" type="text/css" href="<%=styleBase%>/ofs_report.css">-->
<%@page import="com.objectfrontier.insta.reports.InstaReportUtil"%>
<%@page import="java.math.BigDecimal"%>
<nested:form action="/neftOutwardTxnDetailsReport">
<nested:define id="_valueDate" property="reportDto.valueDate" type="java.lang.String" />
<nested:define id="_toDate" property="reportDto.toDate" type="java.lang.String"/>
<div id="printReady">
<%
//Title changed by priyak for user requirement.
 String title = "";
 String imagePath = imageBase + "/print.jpg";
%>
<nested:define id="brCode" property="reportDto.branchCode" type="java.lang.String"/>
<!-- Table 1 Starts Here -->
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td align=left class=column-label width="600" height="9">
            <br><b><nested:message key="label.report.branch"/></b>:
    			 <nested:notEmpty property="hostBranchList">
  					 <nested:define id="_hostBranchList" property="hostBranchList" type="java.util.List"/>
					 <!-- To display the ifscId with branch name  done by priyak-->
					<%
   						String code = "VIJB000"+brCode;
   	
   						if (brCode.equals("ALL")) {
   			 				out.println("ALL Branches");
   						} else {
   			
	   						for(Iterator iter = _hostBranchList.iterator(); iter.hasNext();) {
     	 					HostIFSCMasterDTO _dto = (HostIFSCMasterDTO)iter.next();

      						if(code.equals(_dto.getHostIFSCMasterVO().getIfscCode())){
          				
	      				    	String ifscId = _dto.getHostIFSCMasterVO().getIfscId();
          						String displayValue = _dto.getHostIFSCMasterVO().getIfscCode() + "-"+ _dto.getHostIFSCMasterVO().getBranchName();
      	  						out.println(displayValue);
      	    				}
	   					}	
   					}		
   				%>
			</nested:notEmpty>
			 &nbsp;&nbsp;
			 <b><nested:message key="label.listmessages.batchtime"/></b>:
             <nested:write property="reportDto.batchTime" format="hhmm"/> 
             
              &nbsp;&nbsp;
             <b><nested:message key="label.generatedby"/></b>: 
 	       	 <nested:write property="neftRepDTO.reportRunBy"/> 
               
        </td>
    </tr>
    <!-- Here batch date removed by priyak for client requirement -->
    <!-- Here report type removed by priyak for user requirement -->
    <tr>
        <td class=column-label width="300" height="9">
    	   	       
 	        &nbsp;
 	        <b><nested:message key="label.status"/></b>: 
 	       <nested:write property="reportDto.statusValue"/>         
        </td>
    </tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
     <tr class="row-value">
    	<td align="center" class="row-value">
    	<nested:define id ="format" property="dateFormat" type="java.lang.String"/>
            <b>
            	<font size="2.0">
                	Outward Txn Detailed Report from <%=InstaReportUtil.getDateInSpecificFormat(format,_valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,_toDate)%>
                </font>
            </b>
    	</td>
    </tr>
    <tr class="row-value">
                 <td colspan="" align="right">
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
    <tr>
   <tr>
	<td vAlign=top>
	    <nested:define id="reportType" property="reportDto.reportType" type="java.lang.String"/>

		<nested:equal name="reportType" value="Detailed">
		<!-- Inner Table 2 Starts Here -->
		<TABLE width="75%" border="0">

		<!-- Get Data's From Map -->
		<% int flag = 0;%>
		<nested:define id="outwardDataMap" property="neftRepDTO.outwardMap"/>
        <tr></tr><tr></tr>
        <TR> 
		<TD vAlign=top height="21" >
		<!-- Inner Table 3 Starts here -->
		<table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
                <td rowspan=2 width="5%" align="center"><nested:message key="label.serialno"/></td>
				<td rowspan=2 width="5%" align="center"><nested:message key="label.batch.time"/></td>
				<td rowspan=2 width="5%" align="center" border="2"><nested:message key="label.valuedate"/></td>
				<!--  <td rowspan=2 align="center">&nbsp;<nested:message key="label.senderifsc"/></td>
				<td rowspan=2 align="center">&nbsp;<nested:message key="label.beneficiaryifsc"/></td>  -->       
                                <td rowspan=2 align="center"><nested:message key="label.transaction.ref"/><br>Message Type</td>
                <td rowspan=2 align="center"><nested:message key="label.rtgsbaltxn.txnamount"/></td>
                <td colspan=4 align="center"><nested:message key="label.sender"/></td>
                <td colspan=4 align="center"><nested:message key="label.beneficiary"/></td>
                <td colspan=2 align="center"><nested:message key="label.reschedul"/></td>
                <td rowspan=2 align="center"><nested:message key="label.messageStatus"/></td>                
                
           </tr>
			    <tr class="row-label th-bc">
			        <td align="center"><nested:message key="label.senderifsc"/></td>
                	<td align="center"><nested:message key="label.sender.account.type"/></td>
	                <td align="center"><nested:message key="label.sender.account.number"/></td>
    	            <td align="center"><nested:message key="label.sender.account.name"/></td>
    	            <td align="center"><nested:message key="label.beneficiaryifsc"/></td>                
                    <td align="center"><nested:message key="label.beneficiary.acctype"/></td>
                	<td align="center"><nested:message key="label.beneficiary.no"/></td>
                	<td align="center"><nested:message key="label.beneficiary.name"/></td>
                	<td align="center"><nested:message key="label.date"/></td>
                	<td align="center"><nested:message key="label.batch"/></td>
                	
                	
				</tr>
                <% BigDecimal totAmt = BigDecimal.ZERO;%>
		<nested:iterate id ="element" name="outwardDataMap">
			<nested:define id="elementKey" name="element" property="key"/>                        
			<nested:define id="elementValue" name="element" property="value"/>
		
		<!--<TR> 

			<TH align="center" class="row-label s"><%out.println((String)elementKey); %> 
			</TH> 
		</TR>-->
		
		
			

	    <%
				
			List detailList = (ArrayList)elementValue;
            
			
				
				int i=1; 
		        
				for (int count1=0; count1 < detailList.size(); count1++) { 
					TransactionInfo transactionInfo = (TransactionInfo) (detailList.get(count1)); 
                    flag++;
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
						<td align="center" >&nbsp;<%= ""%></td>
					<% } else { %>
                   
                    <% String formatDate = InstaReportUtil.formatDate(transactionInfo.valueDate);
                    String date = InstaReportUtil.formatDateString(formatDate);%>
                        <td align="center" nowrap>&nbsp;<%=InstaReportUtil.getDateInSpecificFormat(format,date)%></td>
					<% } %>                    

                    
                    <% if (transactionInfo.utrNo == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.utrNo%>
						<%=transactionInfo.msgType%></td>
					<% } %>
					<% totAmt = totAmt.add(transactionInfo.amount);%>
					<td align="right">&nbsp;<%= FormatAmount.formatINRAmount(transactionInfo.amount)%></td>
                                        <% if (transactionInfo.senderInfo.accIfsc== null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.senderInfo.accIfsc%></td>
					<% } %>
                    
					<% if (transactionInfo.senderInfo.accType == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.senderInfo.accType%></td>
					<% } %>
                    
                    <% if (transactionInfo.senderInfo.accNo == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.senderInfo.accNo%></td>
					<% } %>
                    
                    <% if ( transactionInfo.senderInfo.accName== null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%=  transactionInfo.senderInfo.accName%></td>
					<% } %>
                    <% if (transactionInfo.beneficiaryInfo.accIfsc == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%=transactionInfo.beneficiaryInfo.accIfsc%></td>
					<% } %>
                    <% if (transactionInfo.beneficiaryInfo.accType == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.beneficiaryInfo.accType%></td>  
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accNo == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.beneficiaryInfo.accNo%></td>  
					<% } %>
                    
                    <% if (transactionInfo.beneficiaryInfo.accName == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.beneficiaryInfo.accName%></td>  
					<% } %>
					<% if (transactionInfo.rescheduleDate == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						
						<%
							//Added for formatting date from yyyy-mm-dd format into dd-mm-yyyy format.
							String inputDate = InstaReportUtil.convertFormat(transactionInfo.rescheduleDate.toString());
				            String date = InstaReportUtil.formatDateString(inputDate);
						%>
						<td align="center" nowrap>&nbsp;<%=InstaReportUtil.getDateInSpecificFormat(format,date)%></td>
						  
					<% } %>
                     <% if (transactionInfo.rescheduleBatch == null) {%>
						<td align="center">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="center">&nbsp;<%= transactionInfo.rescheduleBatch%></td>  
					<% } %>
					
                    <% if (transactionInfo.currentStatus == null) {%>
						<td align="left">&nbsp;<%= ""%></td>
					<% } else { %>
						<td align="left">&nbsp;<%= transactionInfo.currentStatus%></td>  
					<% } %>
					
				</tr>
				<% i++; %>
				<%}%>
                </nested:iterate>
                 <tr class="row-value tr2-bc">
                    <td></td> <td></td><td></td>
                    <td align="center"><b>Total</b></td>
                    <td align="right"><b><%=FormatAmount.formatINRAmount(totAmt)%></b></td>
                    </tr>
                
		<!-- Inner Table 3 Ends Here -->
		<% if (flag > 0) { %>		
		
	         </table>
	         </TD>
	         </TR>
		 <% } %>	
			<% if (flag == 0) { %>	
				<tr class="row-value tr2-bc">
				<td align="center" colspan=50>
				<b><nested:message key="label.report.noReport"/></b> </td>
				</tr>
			   </table>
				</TD>
				</TR>
            <% } %>
			
        


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
  
    <tr><td><b><div align="right" id = "printDate"  > 
    
	           	<% String currDate = ConversionUtils.getCurrentDateTime();
	           	String dateValue = currDate.substring(0,11);
	              String time1 = currDate.substring(11);
	              String printData = InstaReportUtil.getDateInSpecificFormat(format,dateValue)+time1;
	            %>
	            Generated On <%=printData%></div></td></tr></b>
      
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

    function goBack() {

        document.forms[0].action='/insta/reports/neftReportInput.do?module=reports&amp;mode=input&amp;report=NeftOutwardTxnDetailsReport';
        alert(document.forms[0].action);
        document.forms[0].submit();
    }

//-->

</script>


</nested:form>