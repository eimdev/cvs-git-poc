<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>

<%@page import="com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO"%>
<%@page import="java.util.Iterator"%>
<script language="JavaScript" src="<%=scriptBase%>/calendar.js"></script>
<nested:form action="/r41inwardreport">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td class="error" height="10"><nested:errors/></td>
	</tr>
	<tr>
	 <TD>
		<table width="80%" align="center" border="0" cellspacing="1" cellpadding="4">
			<tr>
          		<TD vAlign=top height="21" width="257" colspan="2">
          		</TD>
	        </tr>
          	<tr>
	      		<TD vAlign=top height="21" width="257" colspan="2" >
     	 		</TD>
          	</tr>
            <tr>
            <td class=column-label width="25%">
            Select Date Format :
            </td>
            <td class=row-value width="75%"> 
            <nested:select property="dateFormat" size="1" styleClass="select">
                <option value="">-- Select --</option>
                <option value="dd-mmm-yy">dd-mmm-yy</option>
                <option value="dd-mm-yyyy">dd-mm-yyyy</option>
                <option value="mm/dd/yy">mm/dd/yy</option>
                <option value="mm/dd/yyyy">mm/dd/yyyy</option>
            </nested:select>
            </td>
            </tr>
          <nested:define id="_isOnlyDate" property="isDateOnly" type="java.lang.Integer"/>
          <%if (_isOnlyDate.intValue()==0) { %>
          <TR>
            <TD class=column-label width="25%">
                Branch Code :
            </TD>
            <TD class=row-value width="75%"> 
   				<nested:define id="_userIfscCode" property="userIfscCode" type="java.lang.String"/>
   				<nested:define id="_userIfscId" property="userIfscId" type="java.lang.Long"/>
   				<nested:define id="_isCOUser" property="isCOUser" type="java.lang.Integer"/>

				<nested:select property="reportDto.ifscId" size="1" style="width:250" styleClass="select">
				<%if(_isCOUser.intValue()==1) { %>
    				<html:option value="0">ALL Branches</html:option>
     				<nested:notEmpty property="hostBranchList">
		   				<nested:define id="_hostBranchList" property="hostBranchList" type="java.util.List"/>
		   				<%
                        	for(Iterator iter = _hostBranchList.iterator(); iter.hasNext();) {

                            	HostIFSCMasterDTO _dto = (HostIFSCMasterDTO)iter.next();
                                String ifscId = _dto.getHostIFSCMasterVO().getIfscId();
                                String displayValue = _dto.getHostIFSCMasterVO().getIfscCode() + "-"+ _dto.getHostIFSCMasterVO().getBranchName();
                    	%>
                    		<html:option value="<%=ifscId%>"><%=displayValue%></html:option>
                    	<%  }
                    	%>
					</nested:notEmpty>
    			<%} else { %>
					<html:option value="<%=String.valueOf(_userIfscId.longValue())%>"><%=_userIfscCode%></html:option>
				<%} %>
 				</nested:select>
            </TD>
          </TR>
	      <%} %>
	      <TR>
            <TD class=column-label width="25%" nowrap>
                Report Date :
            </TD>
            <TD class=row-value width="75%"> 
           		<nested:text property="reportDto.valueDate" maxlength="20" styleClass="textbox" style="width:180" 
	      			readonly="true" onclick="fPopCalFuture(this);"/>
	      		<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand"
	      			 onClick="setDate(document.forms[0].elements['reportDto.valueDate']);">
            </TD>
          </TR>
<!-- 
          <TR>
            <TD class=column-label width="25%">
            	Payment Type :
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select property="reportDto.paymentType" size="1" style="width:180" styleClass="select">
                    <html:option value="R41">Customer Payment</html:option>
                    <html:option value="R42">InterBank Payment</html:option>
 				</nested:select>
            </TD>
          </TR>
          <TR>
            <TD class=column-label width="25%">
            	Transaction Type :
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select property="reportDto.transactionType" size="1" style="width:180" styleClass="select">
                    <html:option value="inward">Inward Transaction</html:option>
                    <html:option value="outward">Outward Transaction</html:option>
 				</nested:select>
            </TD>
          </TR>
          <TR>
            <TD class=column-label width="25%">
            	Group By :
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select property="reportDto.groupBy" size="1" style="width:180" styleClass="select">
                    <html:option value="sender_address">Sender Address</html:option>
                    <html:option value="receiver_address">Receiver Address</html:option>
 				</nested:select>
            </TD>
          </TR>
 -->
		</table>
	</td>
	</tr>
</table>
</nested:form>
<script>
<!--
function setDate(e) {

	fPopCalFuture(e);
	return false;
}

function generateR41OutwardReport() {

    document.forms[0].action='/insta/reports/r41outwardreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateR42OutwardReport() {

    document.forms[0].action='/insta/reports/r42outwardreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateR41InwardReport() {

    document.forms[0].action='/insta/reports/r41inwardreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateR42InwardReport() {

    document.forms[0].action='/insta/reports/r42inwardreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateReconcillationReport() {

    document.forms[0].action='/insta/reports/reconcillationreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateInwardReturnReport() {

    document.forms[0].action='/insta/reports/inwardreturnreport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}

function generateNEFTReconcillationReport() {

	document.forms[0].action='/insta/reports/neftReconcilliation.do?mode=viewreport&module=reports';
    document.forms[0].submit();
}
//-->
</script>
