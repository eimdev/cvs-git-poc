<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="com.objectfrontier.insta.util.AmountInWordsConverter"%>
<%@page import="com.objectfrontier.insta.workflow.util.FormatAmount"%>
<%@page import="com.objectfrontier.insta.workflow.util.InstaUtil"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/utrNoWiseReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr>
        <td align="center" class="row-value">
        <!-- Here all the input details should be here -->
        <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        <b>
        <font size="2.5">
            <nested:define id="utrNo" property="reportDto.utrNo" type="java.lang.String"/>
            Report on UTR Number : <%=utrNo.toUpperCase() %> for RTGS
        </font>
        </b>
        </td>
    </tr>
    <tr class="row-value">
        <td align="right" width="90%">
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
    	int len = 0;
    %>
    
    <nested:notEmpty property="messageDTO">
    <!-- To get the Sender and receiver address for IFSC Name and IFSC code display -->
	<nested:define id="senderAddressF6" property="messageDTO.senderAddress" type="java.lang.String"/>
	<nested:define id="receiverAddressF7" property="messageDTO.receiverAddress" type="java.lang.String"/>
    <tr>
	    <td>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
   <td width="90%" align="center">
	  <table width="100%" border="0" align="left" cellspacing="0" class="inner-table">
    	<tr height="22">
         <td colspan="5" align="left" class="column-label"><b>Message Details</b></td>
        </tr>
		<tr height="22" class="row-value tr1-bc">
		 <td width="150" align="left" class="column-label">Message Type &nbsp;</td>
         <td width="100" align="left" class="row-value"><nested:write property="messageDTO.msgType"/>/<nested:write property="messageDTO.msgSubType"/></td>
		</tr> 
        <tr height="10">
         <td colspan="5" align="center">&nbsp;</td>
        </tr>
      </table>
    </td>
   </tr>
    <nested:size id="_len" property="messageDTO.msgFieldList"/>
    <%
        len=_len;
    %>
   <TR>
   	<td colspan="5" align="left" class="column-label"><b>Message Field Details</b>
		  </td>
   </TR>
   <tr>
    <td>
      
		<table id="msgFieldTab" width="100%" border="0" align="center" cellspacing="1" >
		 <tr height="10">
		  <td colspan="5">
		  </td>
		 </tr>
		 <%boolean k = false; %>
		 <nested:notEmpty property="messageDTO.msgFieldList">
		 <nested:iterate property="messageDTO.msgFieldList">
		 <nested:define id="isDisplay" property="isDisplay" type="java.lang.Boolean"/>
		 <% if(isDisplay.booleanValue()) { %>
		 <% k = k ? false : true;
			if (k) {%><tr height="22" class="row-value tr1-bc">
		 <%	} else {%><tr height="22" class="row-value tr2-bc">
         <%	} %>
         	<nested:empty property="altFieldTypeList">
         		<td class="column-label"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)
         		<nested:equal property="isMandatory" value="true">
				<font color="red"><b>*</b></font></nested:equal></td>
         	</nested:empty>
         	<% String wholeFieldData = ""; %>
		   		 <nested:notEmpty property="altFieldTypeList">
		   		 	<nested:define id="fieldNo_2" property="msgFieldNumber" type="java.lang.String"/>
		   		 		<% if(wholeFieldData.length() == 0)  %>
		   		 			<% wholeFieldData = fieldNo_2; %> 
		   		 	<nested:iterate property="altFieldTypeList">
		   		 			<nested:define id="field_no_1" property="msgFieldNumber" type="java.lang.String"/>
		   		 			<% wholeFieldData += "," + field_no_1; %>
		   		    </nested:iterate>
		   		 </nested:notEmpty>	
         	<% String showFields = "showFields(this,'"+wholeFieldData+"');";%>
         	<nested:notEmpty property="altFieldTypeList">
		   	<td>
		   		<table border="0">
		   		<tr>	
		   			<nested:define id="fieldNo_1" property="msgFieldNumber" type="java.lang.String"/>
		   			<% String fieldAlt = fieldNo_1 + "_1"; %>
		   			<td class="column-label" id="<%=fieldAlt%>" style="display:none"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)
         				<nested:equal property="isMandatory" value="true">
						<font color="red"><b>*</b></font></nested:equal></td>
					<nested:iterate property="altFieldTypeList">
						<nested:define id="fieldNo_alt_1" property="msgFieldNumber" type="java.lang.String"/>
						<% String fieldAlt1 = fieldNo_alt_1 + "_1"; %>
		   				<td class="column-label" id="<%=fieldAlt1%>" style="display:none"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)
         				<nested:equal property="isMandatory" value="true">
						<font color="red"><b>*</b></font></nested:equal></td>
					</nested:iterate>	
		   			<td style="display:none">
				   		<nested:select property="selectedField" styleClass="select" onchange="<%=showFields%>" disabled="true">
				   			<nested:define id="fieldNo" property="msgFieldNumber" type="java.lang.String"/>
				   		 	<html:option value="<%=fieldNo%>"><%=fieldNo%></html:option>
				   		 	<nested:iterate property="altFieldTypeList">
				   		 		<nested:define id="field_no" property="msgFieldNumber" type="java.lang.String"/>
				   		 		<html:option value="<%=field_no%>"><%=field_no%></html:option>
				   		 	</nested:iterate>
				   		 </nested:select>
				   	</td>	 
			   	 </tr>	 
		   		 </table>
		   	</td> 
		   	</nested:notEmpty>
		   	<td>
		   		<table border="0">
		   			<tr>
		   	<nested:empty property="altFieldTypeList">
		   	<nested:define id="compound" property="isCompoundField" type="java.lang.Boolean"/>
		   	<% if(!compound.booleanValue()) { %> 
		   		<td>
		   			<table border="0">
		   				<tr>
         						<td align="center" class="row-value" nowrap="nowrap">
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<nested:define id="fieldNumber" property="msgFieldNumber" type="java.lang.String"/>
								 			<!-- Added to display Ifsc code with Ifsc name -->
	         							<%if((fieldNumber.equals("F6"))||(fieldNumber.equals("F7"))) { %> <!-- Checking for sender or receiver address -->
	         						
	         								<%if(fieldNumber.equals("F6")){%>
	         								<%=senderAddressF6%>
	         							<%} else if(fieldNumber.equals("F7")){%>
	         								<%=receiverAddressF7%>
	         							<%}%>
	         							 <%} else {%>	
	         					 			<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
	         								<%=tempMsgFieldValue%>
	         							 <%} %>			 
         							</nested:notEmpty>
         						</td>
						</tr>
				</table>
				</td>	
			<% } else { %>
				<td>
				<% boolean j = false; %>
				<nested:notEmpty property="msgFieldDTOList">
				<table border="0">
				<nested:iterate property="msgFieldDTOList">
					<nested:define id="isDisplay_compound_1" property="isDisplay" type="java.lang.Boolean"/>
					<% if (isDisplay_compound_1.booleanValue()) { %>
						<% j = j ? false : true;
							if (j) {%><tr height="22" class="row-value tr1-bc">
						<%	} else {%><tr height="22" class="row-value tr2-bc">
				   		<%	} %>
				   			<td class="column-label"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)</td>
				   			<nested:define id="fieldNumber" property="msgFieldNumber" type="java.lang.String"/> 
				   			<% if(fieldNumber.equalsIgnoreCase("D4488")) { %>
				   				<nested:define id="dateValue" property="msgFieldValue" type="java.lang.String"/>
				   				<%if(dateValue.trim().length() > 0) { 
				   				 String valueDate = InstaUtil.parseDateString(dateValue);
				   				%>
				   				<td align="center" class="row-value"><%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%>&nbsp; </td>
				   				<%} else {%>
				   				<td align="center" class="row-value">&nbsp; </td>
				   				<%} %>

				   			<% } else if(fieldNumber.equalsIgnoreCase("A4488")) { %>
				   				<nested:define id="amountValue" property="msgFieldValue" type="java.lang.String"/>
				   				<td align="center" class="row-value"><%=FormatAmount.formatINRAmount(Double.parseDouble(amountValue))%>&nbsp
				   				<br>
				   				(  <%=AmountInWordsConverter.convertIntoRupeeString(amountValue)%> ) 
				   				</td>
				   			<% } else { %>
         						<td align="center" class="row-value" nowrap="nowrap">
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
	         								<%=tempMsgFieldValue%>
         							</nested:notEmpty>
         						</td>
         					<% } %>	
						</tr>
					<% } %>	
				</nested:iterate>
				</table>
         		</nested:notEmpty>
         		</td>
         <% } %>
         </nested:empty>
         <nested:notEmpty property="altFieldTypeList">
         	<nested:define id="compound" property="isCompoundField" type="java.lang.Boolean"/>
		   	<% if(!compound.booleanValue()) { %> 
		   		<nested:define id="field_alt_no" property="msgFieldNumber" type="java.lang.String"/>
		   		<td id="<%=field_alt_no%>" style="display:none">
		   			<table border="0">
		   				<tr>
          						<td align="center" class="row-value" nowrap="nowrap">
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
	         								<%=tempMsgFieldValue%>
         							</nested:notEmpty>
         						</td>	
					   </tr>
				</table>
				</td>	
			<% } else { %>
			    <% boolean z = false; %>
				<nested:notEmpty property="msgFieldDTOList">
				<nested:define id="field_alt_no_com" property="msgFieldNumber" type="java.lang.String"/>
				<td id="<%=field_alt_no_com%>" style="display:none">
				<table border="0">
				<nested:iterate property="msgFieldDTOList">
					<nested:define id="isDisplay_compound_1" property="isDisplay" type="java.lang.Boolean"/>
					<% if (isDisplay_compound_1.booleanValue()) { %>
						<% z = z ? false : true;
							if (z) {%><tr height="22" class="row-value tr1-bc">
						<%	} else {%><tr height="22" class="row-value tr2-bc">
				   		<%	} %>
							<td class="column-label"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)</td>
          						<td align="center" class="row-value" nowrap="nowrap">
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
	         								<%=tempMsgFieldValue%>
         							</nested:notEmpty>
         						</td>
						</tr>
					<% } %>	
				</nested:iterate>
				</table>
				</td>
         		</nested:notEmpty>
         <% } %>
         <nested:iterate property="altFieldTypeList">
         	<nested:define id="compound_alt_it" property="isCompoundField" type="java.lang.Boolean"/>
		   	<% if(!compound_alt_it.booleanValue()) { %> 
		   		<nested:define id="field_alt_no_it" property="msgFieldNumber" type="java.lang.String"/>
		   		<td id="<%=field_alt_no_it%>" style="display:none">
		   			<table border="0">
		   				<tr>
		   	   			         						<td align="left" class="row-value" nowrap="nowrap">
		   	   			         						
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
         									
	         								<%=tempMsgFieldValue%>
         							</nested:notEmpty>
         						</td>
				    	</tr>
				</table>
				</td>	
			<% } else { %>
			    <% boolean x = false; %>
				<nested:notEmpty property="msgFieldDTOList">
				<nested:define id="field_alt_no_com1" property="msgFieldNumber" type="java.lang.String"/>
				<td id="<%=field_alt_no_com1%>" style="display:none">
				<table border="0">
				<nested:iterate property="msgFieldDTOList">
					<nested:define id="isDisplay_compound_2" property="isDisplay" type="java.lang.Boolean"/>
					<% if (isDisplay_compound_2.booleanValue()) { %>
						<% x = x ? false : true;
							if (x) {%><tr height="22" class="row-value tr1-bc">
						<%	} else {%><tr height="22" class="row-value tr2-bc">
				   		<%	} %>
							<td class="column-label"><nested:write property="msgFieldName"/> (<nested:write property="msgFieldNumber"/>)</td>
         					         						<td align="left" class="row-value" nowrap="nowrap">
                            		<nested:notEmpty property="msgFieldValue">
	         							<nested:define id="tempMsgFieldValue" property="msgFieldValue" type="java.lang.String" />
         									<% tempMsgFieldValue = InstaUtil.removeNewLineCharacter(tempMsgFieldValue);%>
	         								<%=tempMsgFieldValue%>
         							</nested:notEmpty>
         						</td>
						</tr>
					<% } %>	
				</nested:iterate>
				</table>
				</td>
         		</nested:notEmpty>
         <% } %>
         </nested:iterate>	
         </nested:notEmpty>
         		</tr>
         	</table>
         </td>	
 	   	 </tr>    
 	   	 <% } %>
		</nested:iterate>
		</nested:notEmpty>
	  </table>
	  <table width="100%" border="0">
	  <% k = k ? false : true;
			if (k) {%><tr height="22" class="row-value tr1-bc">
		 <%	} else {%><tr height="22" class="row-value tr2-bc">
         <%	} %>
			<td align="left" colspan="2" >
		   	   <table width="100%" border="0" align="left">
		   	    <!-- Added for LMS, Vinoth.M.N.A, 20100806 -->
		   	   <tr align="left">
		   		 <td width="10%" align="left" class="column-label">&nbsp;&nbsp;</td>	
		   		 <td width="70%" align="left" class="column-label">Status</td>
		   		 <td width="20%" align="left" class="column-label">&nbsp;&nbsp;</td>
		   		 <td width="10%" align="left" class="column-label">&nbsp;&nbsp;</td>	
		   		</tr>
		   		<tr align="left">
		   		 <td width="10%" align="left" class="column-label">&nbsp;&nbsp;</td>	
		   		 <td width="70%" align="left" class="column-label">Remarks</td>
		   		 <td width="20%" align="left" class="column-label">&nbsp;&nbsp;</td>
		   		</tr>
		   	  </table>
		   	 </td>
		   	 <td width="50%" align="right">
		   		<table align="center" border="0">
		   		<!-- Added for LMS, 20100806 -->
		   		 <tr align="left">
	   				<td width="100%" align="left" class="row-value">&nbsp;&nbsp;
	   				<nested:write property="messageDTO.statusName"/>
		   			<td width="10%" align="left" class="column-label">&nbsp;&nbsp;</td>	
		    	 </tr>
		   	  	 <tr align="center">
	   				<td width="100%" align="center" class="row-value">
	   				<nested:notEmpty property="messageDTO.remarks">
	         			<nested:define id="remarks" property="messageDTO.remarks" type="java.lang.String" />
	         			<% String tempRemarks = InstaUtil.arrangeRemarksField(remarks, "\\r\\n", 35, 4); %>
	         			<% tempRemarks = InstaUtil.removeNewLineCharacter(tempRemarks);%>
	         			<%=tempRemarks%>
         			</nested:notEmpty>&nbsp;</td>
		   			<td></td>
		    	 </tr>
		   		</table>
		   	 </td>
		</tr>
		</table>
		<nested:define id="errRemarks" property="messageDTO.errorRemarks" type="java.lang.String"/>
		<% if(errRemarks != null && errRemarks.length() > 0) { %>
			<table width="100%" border="0">
		  <% k = k ? false : true;
				if (k) {%><tr height="22" class="row-value tr1-bc">
			 <%	} else {%><tr height="22" class="row-value tr2-bc">
	         <%	} %>
				<td align="left" colspan="2" >
			   	   <table width="100%" border="0" align="left">
			   		<tr align="left">
			   		 <td width="10%" align="left" class="column-label">&nbsp;&nbsp;</td>	
			   		 <td width="70%" align="left" class="column-label">Error Remarks</td>
			   		 <td width="20%" align="left" class="column-label">&nbsp;&nbsp;</td>
			   		</tr>
			   	  </table>
			   	 </td>
			   	 <td width="50%" align="right">
			   		<table align="center" border="0">
			   	  	 <tr align="center">
			   			<td>&nbsp;</td>
			   			<td width="100%" align="center" class="row-value"><nested:write property="messageDTO.errorRemarks"/></td>
			   			<td></td>
			    	 </tr>
			   		</table>
			   	 </td>
			</tr>
			</table>
		<% } %>	

   </td>
 </tr>
 <tr>
 <td>
 </td>
 </tr>
  
</table>

    </td>
	</tr>
    </nested:notEmpty>
    <nested:empty property="messageDTO">
	<tr>
    <td>
    </td>
	</tr>
    <tr align="center">
        <td class="error" height="10">
            <b>No record found for the UTR No: <nested:write property="reportDto.utrNo"/></b>
        </td>
    </tr>    
    </nested:empty>
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
	len = <%=len%>;
	function init() {
    
	    for(var i = 0; i < len; i++) {
	    
	    	if(document.forms[0].elements['messageDTO.msgFieldList['+i+'].selectedField'] != null) {
	    		var selectedId = document.forms[0].elements['messageDTO.msgFieldList['+i+'].selectedField'].value;
	    		
	    		var fieldNo = selectedId;
				var fieldNo_Alt = fieldNo + "_1";
		
				document.getElementById(fieldNo_Alt).style.display = "block";
				document.getElementById(fieldNo).style.display = "block";
	    	}
	    }
	}

    function printScript() {

        var title = new String("");
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }

    function goBack() {

        document.forms[0].action='/insta/reports/reportInput.do?module=reports&mode=input&report=UTRNumberwiseReport';
        document.forms[0].submit();
    }
//-->

</script>
</div>
</nested:form>
