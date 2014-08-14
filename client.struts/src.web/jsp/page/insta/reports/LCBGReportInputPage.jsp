<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.objectfrontier.insta.message.client.dto.BankMasterDTO" %>
<%@ page import="com.objectfrontier.insta.message.client.dto.HostIFSCMasterDTO" %>
<%@ page import="com.objectfrontier.insta.message.client.dto.IFSCMasterDTO" %>
<%@ page import="com.objectfrontier.insta.message.client.vo.BankMasterValueObject" %>
<%@ page import="com.objectfrontier.insta.message.client.vo.HostIFSCMasterVO" %>
<%@ page import="com.objectfrontier.insta.message.client.vo.IFSCMasterVO" %>

<script language="JavaScript" src="<%=scriptBase%>/calendar.js"></script>
<nested:form action="/lcbgReportInput">
<%! boolean typeField = false;
    boolean statusField = false;%>
    <nested:hidden property ="report"/>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <nested:define id="_haveBranchField" property="haveBranchField" type="java.lang.Boolean"/>
    <nested:define id="_haveDateField" property="haveDateField" type="java.lang.Boolean"/>
    <nested:define id="_haveTranTypeField" property="haveTranTypeField" type="java.lang.Boolean"/>
    <nested:define id="_haveMsgSubTypeField" property="haveMsgSubTypeField" type="java.lang.Boolean"/>
    <% typeField = _haveMsgSubTypeField.booleanValue(); %>
    <nested:define id="_haveHostTypeField" property="haveHostTypeField" type="java.lang.Boolean"/>
    <nested:define id="_isInwardSpecific" property="isInwardSpecific" type="java.lang.Boolean"/>
    <nested:define id="_isOutwardSpecific" property="isOutwardSpecific" type="java.lang.Boolean"/>
    <nested:define id="_haveUTRNoField" property="haveUTRNoField" type="java.lang.Boolean"/>
    <nested:define id="_haveAmountField" property="haveAmountField" type="java.lang.Boolean"/>
    <nested:define id="_haveStatusField" property="haveStatusField" type="java.lang.Boolean"/>
    <% statusField = _haveStatusField.booleanValue(); %>
    <nested:define id="_haveCounterPartyFld" property="haveCounterPartyFld" type="java.lang.Boolean"/>
    <nested:define id="_haveUserField" property="haveUserField" type="java.lang.Boolean"/>
    <nested:define id="_userIfscCode" property="userIfscCode" type="java.lang.String"/>
    <nested:define id="_userIfscId" property="userIfscId" type="java.lang.Long"/>
    <nested:define id="_isCOUser" property="isCOUser" type="java.lang.Integer"/>
    <nested:define id="_branchDisplay" property="disableBranchDisplay" type="java.lang.Integer"/>
    <nested:define id="_haveBatchTime" property="haveBatchTimeField" type="java.lang.Boolean"/>
    <nested:define id="_haveValueDate" property="haveValueDateField" type="java.lang.Boolean"/>
    <nested:define id="_haveReportType" property="haveReportTypeField" type="java.lang.Boolean"/>
    <nested:define id="_haveInwardType" property="haveInwardTypeField" type="java.lang.Boolean"/>
	<nested:define id="_report" property="report" type="java.lang.String"/>
    <nested:define id="_haveFutureDateTxnStatus" property="haveFutureDateTxnStatus" type="java.lang.Boolean"/>
	<nested:define id="_haveBankListField" property="haveBankListField" type="java.lang.Boolean"/>
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
            </td>
          </tr>
			<!--TR>
				<TD align="left" class=column-label width="25%">
					IFN Series :
				</TD>
				<TD class=row-value width="75%">
					<nested:select property="ifnSeries" size="1" styleClass="select">
                        <html:option value="700">700</html:option>
                        <html:option value="700">701</html:option>
						<html:option value="700">705</html:option>
						<html:option value="700">707</html:option>
						<html:option value="700">710</html:option>
						<html:option value="700">711</html:option>
						<html:option value="700">720</html:option>
						<html:option value="700">721</html:option>
						<html:option value="700">730</html:option>
					</nested:select>
				</TD>
			</TR-->

          <%if (_haveDateField.booleanValue()) {%>
          <TR>
       		<TD vAlign=top height="21" width="257" colspan="2" >
				<table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
    				<tr><TD align="left" class=column-label width="25%" nowrap>
        	            From Date :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	    </TD>
                	<TD class=row-value width="70%">
               			<nested:text property="reportDto.valueDate" maxlength="20" styleClass="textbox" style="width:120"
    	      			readonly="true" onclick="fPopCalFuture(this);"/>
                    </td>
                    <td>&nbsp;
    	      			<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand"
    	      			 onClick="setDate(document.forms[0].elements['reportDto.valueDate']);">
                	</TD>
					</TR>
					<TR>
                	<TD align="left" class=column-label width="25%" nowrap>
                    	To Date :&nbsp;
                	</TD>
                	<TD class=row-value width="70%">
               		<nested:text property="reportDto.toDate" maxlength="20" styleClass="textbox" style="width:120"
    	      			readonly="true" onclick="fPopCalFuture(this);"/>
                    </td>
                    <td>&nbsp;
    	      		<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand"
    	      			 onClick="setDate(document.forms[0].elements['reportDto.toDate']);">
    	            </TD></tr>
            	</table>
  	 		</TD>
          </TR>
          <%}%>
		</table>
	</td>
	</tr>
</table>
</nested:form>
<script>
<!--
var msgTypeField = <%=typeField%>;
var statusField = <%=statusField%>;
function init() {

	//changeTranType(document.forms[0].elements['reportDto.transactionType']);
}

// generateNEFTInwLcbgSummaryReport
function generateLCBGInwSummaryReport() {

	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		document.forms[0].action='/insta/reports/lcbgInwardSummaryReport.do?mode=inwardSummaryReportView&module=reports';
		document.forms[0].submit();
	}
}

//generateNEFTOutwLcbgSummaryReport,
function generateNEFTOutwLcbgSummaryReport() {

	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		document.forms[0].action='/insta/reports/lcbgOutwardSummaryReport.do?mode=outwardSummaryReportView&module=reports';
		document.forms[0].submit();
	}
}

function setDate(e) {

	fPopCalFuture(e);
	return false;
}

function setDate1(e) {

	fPopCal(e);
	return false;
}

function dateValidation(fromDate, toDate) {

    var fYr, tYr, fMon, tMon, fDt, tDt;
    var gMonths = new Array("","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    var i = 0;

    fYr = fromDate.substring(7,11);
    fMon = fromDate.substring(3,6);
    fDt = fromDate.substring(0,2);
    tYr = toDate.substring(7,11);
    tMon = toDate.substring(3,6);
    tDt = toDate.substring(0,2);

    while(i<gMonths.length){

        if(gMonths[i]==fMon){
            fMon = i;
        }
        if(gMonths[i]==tMon){
            tMon = i;
        }
        i++;
    }

    if(fYr < tYr){
        return(true);
    }else if(fYr > tYr){
        alert("From Date cannot be greater than To Date.");
        return(false);
    }else if(fYr == tYr){
        if(fMon < tMon){
            return(true);
        }else if(fMon > tMon){
            alert("From Date cannot be greater than To Date.");
            return(false);
        }else if(fMon == tMon){
            if(fDt < tDt){
                return(true);
            }else if(fDt > tDt){
                alert("From Date cannot be greater than To Date.");
                return(false);
            }else{
                return(true);
            }
        }
    }
}

//-->
</script>
