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
<nested:form action="/eventAlertReport">
<nested:hidden property="reportEvent"/>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <nested:define id="_haveBranchField" property="haveBranchField" type="java.lang.Boolean"/>
    <nested:define id="_haveDateField" property="haveDateField" type="java.lang.Boolean"/>
    <nested:define id="_haveTranTypeField" property="haveTranTypeField" type="java.lang.Boolean"/>
    <nested:define id="_haveUserField" property="haveUserField" type="java.lang.Boolean"/>
        <nested:define id="_haveChannelField" property="haveChannelField" type="java.lang.Boolean"/>
    <nested:define id="_haveMsgSubTypeField" property="haveMsgSubTypeField" type="java.lang.Boolean"/>
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
          <TR>
            <TD>
                <%if(_haveChannelField.booleanValue()) {%>
                <TR>
                         <TD align="left" class=column-label width="25%">
                             Channel&nbsp;<font color="red"><b>*</b></font>
                         </TD>
                         <TD class=row-value width="75%"> 
             				<nested:select property="reportDto.channel" size="1" styleClass="select" onchange = "setMsgType();">
                                <html:option value="">--Select--</html:option>
                                <html:options property="channelList" />
                            </nested:select>
                         </TD>
                       </TR>
                <%} %>
                  <%if (_haveTranTypeField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Tran Type&nbsp;<font color="red"><b>*</b></font>
                    </TD>
                    <TD class=row-value width="75%"> 
        				<nested:select property="reportDto.transactionType" size="1" style="width:180" styleClass="select" onchange = "setMsgType();">
                           <html:option value="">--Select--</html:option>
                            <html:option value="inward">Inward</html:option>
                            <html:option value="outward">Outward</html:option>
                    	</nested:select>
                    </TD>
                  </TR>
                  <%}%>
                  <%if(_haveMsgSubTypeField.booleanValue()) {%>
                <TR>
                         <TD align="left" class=column-label width="25%">
                             Payment Type&nbsp;<font color="red"><b>*</b></font>
                         </TD>
                         <TD class=row-value width="75%"> 
             				<nested:select property="reportDto.paymentType" size="1" style="width:180" styleClass="select">
                            <html:option value="">--Select--</html:option>
                            <!--<html:option value="ALL">ALL Payments</html:option>-->
                            <nested:notEmpty property="subTypeList">
                		   		<nested:define id="_subTypeList" property="subTypeList"/>
                				<html:options collection="_subTypeList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                         </TD>
                       </TR>
                <% }%>
                  <%if (_haveBranchField.booleanValue()) {%>
                  <TR>
                      <TD align="left" class=column-label width="25%">
                      Branch
                  </TD>
                  <TD class=row-value width="75%"> 
                  
      				<nested:select property="reportDto.ifscId" size="1" style="width:250" styleClass="select" onchange = "setUserList();">
      					<html:option value="ALL">ALL Branches</html:option>
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
          			
       				</nested:select>
                 </TD>
                </TR>
                <% }%>
                <%if (_haveUserField.booleanValue()) {%>
                       <TR>
                         <TD align="left" class=column-label width="25%">
                             User&nbsp;<font color="red"><b>*</b></font>
                         </TD>
                         <TD class=row-value width="75%"> 
             				<nested:select property="reportDto.userId" size="1" styleClass="select">
                                <html:option value="">--Select--</html:option>
                                <html:options property="userIdList"/>
                            </nested:select>
                         </TD>
                       </TR>
                  <%} %>
                  
                 
          <%if (_haveDateField.booleanValue()) {%>
          <TR>
      		<TD vAlign=top height="21" width="257" colspan="2" >
				<table width="100%" align="center" border="0" cellspacing="1" cellpadding="4">
    				<tr><TD align="left" class=column-label width="25%" nowrap>
        	            From Date 
            	    </TD>
                	<TD class=row-value width="70%">
               			<nested:text property="reportDto.valueDate" maxlength="20" styleClass="textbox" style="width:120" 
    	      			readonly="true" onclick="fPopCalFuture(this);"/>
                    </td>
                    <td>
    	      			<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand"
    	      			 onClick="setDate(document.forms[0].elements['reportDto.valueDate']);">
                	</TD>
                	<TD class=column-label width="25%" nowrap>
                    	To Date
                	</TD>
                	<TD class=row-value width="70%"> 
               		<nested:text property="reportDto.toDate" maxlength="20" styleClass="textbox" style="width:120" 
    	      			readonly="true" onclick="fPopCalFuture(this);"/>
                    </td>
                    <td>
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
function setDate(e) {

	fPopCalFuture(e);
	return false;
}

function setMsgType() {

   document.forms[0].action='/insta/reports/eventAlertReport.do?mode=setValue';
   document.forms[0].submit();
}

function isValid() {
    var isValid = true;
    if(document.forms[0].reportEvent.value == 'Email') {
    
        if (document.forms[0].elements['reportDto.channel'].value =='') {
            alert('select any Channel');
            isValid = false;
        }
        if (document.forms[0].elements['reportDto.paymentType'].value == '') {
            alert('select any Payment Type');
            isValid = false;
        }
        if (document.forms[0].elements['reportDto.transactionType'].value == '') {
            alert('select any Tranaction Type');
            isValid = false;
        }
    } 
    if(document.forms[0].reportEvent.value == 'AuditLog') {
        if (document.forms[0].elements['reportDto.userId'].value =='') {
            alert('select any User');
            isValid = false;
        }
    }
    return isValid;
        
	}
    
 function showReport() {
    if (isValid()) {
        if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
            document.forms[0].action='/insta/reports/eventAlertReport.do?mode=viewreport';
            document.forms[0].submit();
        }
    }
 }

function setUserList() {
    
    if(document.forms[0].reportEvent.value == 'AuditLog') {
        document.forms[0].action='/insta/reports/eventAlertReport.do?mode=refresh';
        document.forms[0].submit();
    } else {
    return;
    }
}

function backToSelectPage() {
    document.forms[0].action='/insta/reports/eventAlertReport.do?mode=select';
    document.forms[0].submit();
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