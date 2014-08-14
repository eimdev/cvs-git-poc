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
<nested:form action="/neftReportInput">
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
			<TR>
				<TD align="left" class=column-label width="25%">
					Bank :
				</TD>
				<TD class=row-value width="75%">
					<nested:select property="reportDto.selectedBank" multiple="true" size="6" style="width:250" styleClass="textbox" >
                        <html:option value="ALL">All Banks</html:option>
                            <nested:notEmpty property="bankList">
								<nested:define id="_bankList" property="bankList" type="java.util.List"/>
								<nested:options collection="_bankList"
									property="bankMasterVO.id" labelProperty="bankMasterVO.name" />
               				</nested:notEmpty>
               			</nested:select>
				</TD>
			</TR>

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
<!--      <TR>
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
var msgTypeField = <%=typeField%>;
var statusField = <%=statusField%>;
function init() {

	//changeTranType(document.forms[0].elements['reportDto.transactionType']);
}

// neftInwardBankWiseSummaryReport
function generateNEFTInwBankWiseSummaryReport() {
	
	var all = false;
    var length = document.forms[0].elements['reportDto.selectedBank'].length; 
    if (document.forms[0].elements['reportDto.selectedBank'][0].selected) {
        all = true;       
    }

    for (var i = 1; i < length; i++) {
        var selectedValue = document.forms[0].elements['reportDto.selectedBank'][i].selected;
            if (all) {
                document.forms[0].elements['reportDto.selectedBank'][0].selected = false;
            }
     }

	//This condition added for NEFT branch wise summary report -amount and date checking by Eswaripriyak
	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		
		document.forms[0].action='/insta/reports/neftInwardBankWiseSummaryReport.do?mode=inwardSummaryReportView&module=reports';
    	document.forms[0].submit();
	}    
}

//neftOutwardBankwiseSummaryReport, 20110803
function generateNEFTOutwBankSummaryReport() {

var all = false;
    var length = document.forms[0].elements['reportDto.selectedBank'].length; 
    if (document.forms[0].elements['reportDto.selectedBank'][0].selected) {
        all = true;       
    }

    for (var i = 1; i < length; i++) {
        var selectedValue = document.forms[0].elements['reportDto.selectedBank'][i].selected;
            if (all) {
                document.forms[0].elements['reportDto.selectedBank'][0].selected = false;
            }
     }

	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		
		document.forms[0].action='/insta/reports/neftInwardBankWiseSummaryReport.do?mode=outwardSummaryReportView&module=reports';
		document.forms[0].submit();
	}
}

// neftInwardBankWiseDetailedReport
function generateNEFTInwBankWiseDetailedReport() {
	
	var all = false;
    var length = document.forms[0].elements['reportDto.selectedBank'].length; 
    if (document.forms[0].elements['reportDto.selectedBank'][0].selected) {
        all = true;       
    }

    for (var i = 1; i < length; i++) {
        var selectedValue = document.forms[0].elements['reportDto.selectedBank'][i].selected;
            if (all) {
                document.forms[0].elements['reportDto.selectedBank'][0].selected = false;
            }
     }

	//This condition added for NEFT branch wise summary report -amount and date checking by Eswaripriyak
	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		
		document.forms[0].action='/insta/reports/neftInwardBankWiseSummaryReport.do?mode=inwardDetailedviewreport&module=reports';
    	document.forms[0].submit();
	}    
}

//neftOutwardBankwiseDetailedReport
function generateNEFTOutwBankDetailedReport() {
	
	var all = false;
    var length = document.forms[0].elements['reportDto.selectedBank'].length; 
    if (document.forms[0].elements['reportDto.selectedBank'][0].selected) {
        all = true;       
    }

    for (var i = 1; i < length; i++) {
        var selectedValue = document.forms[0].elements['reportDto.selectedBank'][i].selected;
            if (all) {
                document.forms[0].elements['reportDto.selectedBank'][0].selected = false;
            }
     }

	if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
		
		document.forms[0].action='/insta/reports/neftInwardBankWiseSummaryReport.do?mode=outwardDetailedViewReport&module=reports';
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
