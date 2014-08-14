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
<nested:form action="/reportInput">
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
    <nested:define id="_haveFutureDateTxnStatus" property="haveFutureDateTxnStatus" type="java.lang.Boolean"/>
    <nested:define id="_haveResponeType" property="haveResponeType" type="java.lang.Boolean"/>
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
            <TD>
                <table width="100%" align="center" border="0" cellspacing="1" cellpadding="4">
                <tr>
		            <td class=column-label width="25%">
		            Select Date Format 
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
                  <%if (_haveTranTypeField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Tran Type
                    </TD>
                    <TD class=row-value width="75%"> 
        				<nested:select property="reportDto.transactionType" size="1" style="width:180" styleClass="select" onchange="changeTranType(this);">
                            <!--<html:option value="ALL">Both</html:option>-->
                            <nested:notEmpty property="tranTypeList">
                		   		<nested:define id="_tranTypeList" property="tranTypeList"/>
                				<html:options collection="_tranTypeList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                    </TD>
                  </TR>
                  <%}%>
                  <%if (_haveMsgSubTypeField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Payment Type
                    </TD>
                    <TD id="bothType" class=row-value width="75%" style="display:none"> 
        				<nested:select property="reportDto.paymentType" size="1" style="width:180" styleClass="select">
                            <!--<html:option value="ALL">ALL Payments</html:option>-->
                            <nested:notEmpty property="subTypeList">
                		   		<nested:define id="_subTypeList" property="subTypeList"/>
                				<html:options collection="_subTypeList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                    </TD>
                    <TD id="iwType" class=row-value width="75%" style="display:none"> 
        				<nested:select property="inwardType" size="1" style="width:180" styleClass="select" onchange="setPaymentType(this);">
                            <!--<html:option value="ALL">ALL Payments</html:option>-->
                            <nested:notEmpty property="inwardTypeList">
                		   		<nested:define id="_iwTypeList" property="inwardTypeList"/>
                				<html:options collection="_iwTypeList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                    </TD>
                    <TD id="owType" class=row-value width="75%" style="display:none"> 
        				<nested:select property="outwardType" size="1" style="width:180" styleClass="select" onchange="setPaymentType(this);">
                            <!--<html:option value="ALL">ALL Payments</html:option>-->
                            <nested:notEmpty property="outwardTypeList">
                		   		<nested:define id="_owTypeList" property="outwardTypeList"/>
                				<html:options collection="_owTypeList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                    </TD>
                  </TR>
                  <%}%>
                  <%if (_haveResponeType.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Response Type
                   </TD>
                    <TD class=row-value width="75%"> 
        				<nested:select property="reportDto.response" size="1" style="width:180" styleClass="select">
                            <html:option value="R90">PI</html:option>
                            <html:option value="R09">SSN</html:option>
                        </nested:select>
                    </TD>
                  </TR>
                  <% } %>
                  <%if (_haveHostTypeField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Host Type
                    </TD>
                    <TD class=row-value width="75%"> 
        				<nested:select property="reportDto.hostType" size="1" style="width:180" styleClass="select">
                            <html:option value="ALL">ALL Hosts</html:option>
                            <nested:notEmpty property="hostList">
                		   		<nested:define id="_hostList" property="hostList"/>
                				<html:options collection="_hostList" property="value" labelProperty="displayValue"/>
                    		</nested:notEmpty>
         				</nested:select>
                    </TD>
                  </TR>
                  <%}%>
                  <%if (_haveBranchField.booleanValue()) {%>
                    <%if (_isInwardSpecific.booleanValue()) {%>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Receiver Branch
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.receiverIfscId" size="1" style="width:250" styleClass="select">
                				<%if(_isCOUser.intValue()==1) { %>
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
                    			<%} else { %>
                					<html:option value="<%=String.valueOf(_userIfscId.longValue())%>"><%=_userIfscCode%></html:option>
                				<%} %>
                 				</nested:select>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Sender Bank
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.senderBank" size="1" style="width:250" styleClass="select" onchange="getBranchList();">
                                    <html:option value="ALL">ALL Banks</html:option>
                                    <nested:notEmpty property="bankList">
                		   				<nested:define id="_bankList" property="bankList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _bankList.iterator(); iter.hasNext();) {

                                                BankMasterDTO _dto = (BankMasterDTO)iter.next();
                                                String bankCode = _dto.getBankMasterVO().getCode();
                                                String displayValue = _dto.getBankMasterVO().getCode() + "-"+ _dto.getBankMasterVO().getName();
                                        %>
                                                <html:option value="<%=bankCode%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                    				</nested:notEmpty>
                 				</nested:select>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Sender Branch
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.senderIfscId" size="1" style="width:250" styleClass="select">
                                    <html:option value="ALL">ALL Branches</html:option>
                                    <nested:notEmpty property="nonHostBranchList">
                		   				<nested:define id="_nonHostBranchList" property="nonHostBranchList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _nonHostBranchList.iterator(); iter.hasNext();) {

                                                IFSCMasterDTO _dto = (IFSCMasterDTO)iter.next();
                                                String ifscId = _dto.getIfscMasterVO().getIfscId();
                                                String displayValue = _dto.getIfscMasterVO().getIfscCode() + "-"+ _dto.getIfscMasterVO().getBranchName();
                                        %>
                                                <html:option value="<%=ifscId%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                    				</nested:notEmpty>
                 				</nested:select>
                            </TD>
                         </TR>
                    <%} else if (_isOutwardSpecific.booleanValue()) {%>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Sender Branch
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.senderIfscId" size="1" style="width:250" styleClass="select">
                				<%if(_isCOUser.intValue()==1) { %>
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
                    			<%} else { %>
                					<html:option value="<%=String.valueOf(_userIfscId.longValue())%>"><%=_userIfscCode%></html:option>
                				<%} %>
                 				</nested:select>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Receiver Bank
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.receiverBank" size="1" style="width:250" styleClass="select" onchange="getBranchList();">
                                    <html:option value="ALL">ALL Banks</html:option>
                                    <nested:notEmpty property="bankList">
                		   				<nested:define id="_bankList" property="bankList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _bankList.iterator(); iter.hasNext();) {

                                                BankMasterDTO _dto = (BankMasterDTO)iter.next();
                                                String bankCode = _dto.getBankMasterVO().getCode();
                                                String displayValue = _dto.getBankMasterVO().getCode() + "-"+ _dto.getBankMasterVO().getName();
                                        %>
                                                <html:option value="<%=bankCode%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                    				</nested:notEmpty>
                 				</nested:select>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Receiver Branch
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.receiverIfscId" size="1" style="width:250" styleClass="select">
                                    <html:option value="ALL">ALL Branches</html:option>
                                    <nested:notEmpty property="nonHostBranchList">
                		   				<nested:define id="_nonHostBranchList" property="nonHostBranchList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _nonHostBranchList.iterator(); iter.hasNext();) {

                                                IFSCMasterDTO _dto = (IFSCMasterDTO)iter.next();
                                                String ifscId = _dto.getIfscMasterVO().getIfscId();
                                                String displayValue = _dto.getIfscMasterVO().getIfscCode() + "-"+ _dto.getIfscMasterVO().getBranchName();
                                        %>
                                                <html:option value="<%=ifscId%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                    				</nested:notEmpty>
                 				</nested:select>
                            </TD>
                         </TR>
                    <%} else {%>
                          <TR>
                            <TD align="left" class=column-label width="25%">
                                Branch
                            </TD>
                            <TD class=row-value width="75%"> 
                            <%if(_branchDisplay.intValue()==0) { %>
                				<nested:select property="reportDto.ifscId" size="1" style="width:250" styleClass="select" onchange = "setBranchList();">
                				<%if(_isCOUser.intValue()==1) { %>
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
                    			<%} else { %>
                					<html:option value="<%=String.valueOf(_userIfscId.longValue())%>"><%=_userIfscCode%></html:option>
                				<%} %>
                 				</nested:select>
                            <%} else { %>
                                <%=_userIfscCode%>
                            <%} %>
                            </TD>
                          </TR>
                    <%} %>
                  <%}%>
                  <%if (_haveCounterPartyFld.booleanValue()) {%>
                        <TR>
                            <TD align="left" class=column-label width="25%">
                                Counter Party Bank
                            </TD>
                            <TD class=row-value width="75%"> 
                				<nested:select property="reportDto.counterPartyBank" size="1" style="width:250" styleClass="select" onchange="getBranchList();">
                                    <html:option value="ALL">ALL Banks</html:option>
                                    <nested:notEmpty property="bankList">
                		   				<nested:define id="_bankList" property="bankList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _bankList.iterator(); iter.hasNext();) {

                                                BankMasterDTO _dto = (BankMasterDTO)iter.next();
                                                String bankCode = _dto.getBankMasterVO().getCode();
                                                String displayValue = _dto.getBankMasterVO().getCode() + "-"+ _dto.getBankMasterVO().getName();
                                        %>
                                                <html:option value="<%=bankCode%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                    				</nested:notEmpty>
                 				</nested:select>
                            </TD>
                        </TR>
                       <TR>
                         <TD align="left" class=column-label width="25%">
                             Counter Party Branch
                         </TD>
                         <TD class=row-value width="75%"> 
             				<nested:select property="reportDto.counterPartyIfscId" size="1" style="width:250" styleClass="select">
                                <html:option value="ALL">ALL</html:option>
                                <nested:notEmpty property="nonHostBranchList">
                                    <nested:define id="_nonHostBranchList" property="nonHostBranchList" type="java.util.List"/>
                                        <%
                                            for(Iterator iter = _nonHostBranchList.iterator(); iter.hasNext();) {

                                                IFSCMasterDTO _dto = (IFSCMasterDTO)iter.next();
                                                String ifscId = _dto.getIfscMasterVO().getIfscId();
                                                String displayValue = _dto.getIfscMasterVO().getIfscCode() + "-"+ _dto.getIfscMasterVO().getBranchName();
                                        %>
                                                <html:option value="<%=ifscId%>"><%=displayValue%></html:option>
                                        <%  }
                                        %>
                                </nested:notEmpty>
                            </nested:select>
                         </TD>
                       </TR>
                  <%} %>
                  <%if (_haveUTRNoField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                        UTR Number
                    </TD>
                    <TD class=row-value width="75%"> 
                   		<nested:text property="reportDto.utrNo" maxlength="16" styleClass="textbox" style="width:180" onkeypress="suppressCode(this);"/>
                    </TD>
                  </TR>
                  <%}%>
                  
                  <%if (_haveUserField.booleanValue()) {%>
                       <TR>
                         <TD align="left" class=column-label width="25%">
                             User
                         </TD>
                         <TD class=row-value width="75%"> 
             				<nested:select property="reportDto.userId" size="1" styleClass="select">
                                <html:option value="">--Select--</html:option>
                                <html:options property="userIdList"/>
                            </nested:select>
                         </TD>
                       </TR>
                  <%} %>
                  <%if(_haveFutureDateTxnStatus.booleanValue()) { %>
                    <tr>
                        <TD align="left" class=column-label width="25%">
                                    Status
                        </TD>
                        <td>
                        <nested:select property="reportDto.status" size="1" style="width:180" styleClass="select">
                            <html:option value="2000">Active</html:option>
                            <html:option value="2050">Cancelled</html:option>
                        </nested:select>
                        </td>
                    </tr>
                  <% }%>
                  <%if (_haveStatusField.booleanValue()) {%>
                  <TR>
                    <TD align="left" class=column-label width="25%">
                    	Status
                    </TD>
                    <TD id="bothStatus" class=row-value width="75%" style="display:none"> 
        				<nested:select property="reportDto.status" size="1" style="width:180" styleClass="select">
                            <html:option value="ALL">ALL Status</html:option>
                            <nested:notEmpty property="statusList">
                                <nested:define id="_statusList" property="statusList"/>
                                <html:options collection="_statusList" property="value" labelProperty="displayValue"/>
                            </nested:notEmpty>
         				</nested:select>
                    </TD>
                    <TD id="iwStatus" class=row-value width="75%" style="display:none"> 
        				<nested:select property="inwardStatus" size="1" style="width:180" styleClass="select" onchange="setStatus(this);">
                            <html:option value="ALL">ALL Status</html:option>
                            <nested:notEmpty property="inwardStatusList">
                                <nested:define id="_iwStatusList" property="inwardStatusList"/>
                                <html:options collection="_iwStatusList" property="value" labelProperty="displayValue"/>
                            </nested:notEmpty>
         				</nested:select>
                    </TD>
                    <TD id="owStatus" class=row-value width="75%" style="display:none"> 
        				<nested:select property="outwardStatus" size="1" style="width:180" styleClass="select" onchange="setStatus(this);">
                            <html:option value="ALL">ALL Status</html:option>
                            <nested:notEmpty property="outwardStatusList">
                                <nested:define id="_owStatusList" property="outwardStatusList"/>
                                <html:options collection="_owStatusList" property="value" labelProperty="displayValue"/>
                            </nested:notEmpty>
         				</nested:select>
                    </TD>
                  </TR>
                  <%}%>
              </table>
            </td>
          </tr>
          <%if (_haveAmountField.booleanValue()) {%>
          <TR>
            <TD vAlign=top height="21" width="257" colspan="2" >
                <table width="80%" align="center" border="0" cellspacing="1" cellpadding="4">
                    <tr><TD align="left" class=column-label width="25%" nowrap>
                        From Amount
                    </TD>
                    <TD class=row-value width="75%"> 
                        <nested:text property="reportDto.fromAmount" maxlength="19" styleClass="textbox" style="width:120" onkeyup="isValidateNumber(this);"/>
                    </TD>
                    <TD class=column-label width="25%" nowrap>
                        To Amount
                    </TD>
                    <TD class=row-value width="75%"> 
                        <nested:text property="reportDto.toAmount" maxlength="19" styleClass="textbox" style="width:120" onkeyup="isValidateNumber(this);"/>
                    </TD></tr>
                </table>
            </TD>
          </TR>
          <%}%>
          
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

	changeTranType(document.forms[0].elements['reportDto.transactionType']);
}

function changeTranType(e1) {
		
		
		if(msgTypeField) {
		
			var trantype = document.forms[0].elements['reportDto.transactionType'];
			if(trantype != null) {
				if(trantype.value != null ||
				   trantype.value != '') {
				   
					if(trantype.value == 'Both' || trantype.value == 'All') {
						
						document.getElementById('bothType').style.display = '';
						document.getElementById('iwType').style.display = 'none';
						document.getElementById('owType').style.display = 'none';
					}
					if(trantype.value == 'Inward' || trantype.value == 'inward') {
					
						document.getElementById('bothType').style.display = 'none';
						document.getElementById('iwType').style.display = '';
						document.getElementById('owType').style.display = 'none';
					}
					if(trantype.value == 'Outward' || trantype.value == 'outward') {
						
						document.getElementById('bothType').style.display = 'none';
						document.getElementById('iwType').style.display = 'none';
						document.getElementById('owType').style.display = '';
					}
				}
			} else {
				
				document.getElementById('bothType').style.display = '';
			    document.getElementById('iwType').style.display = 'none';
				document.getElementById('owType').style.display = 'none';
			}
		}
		
		if(statusField) {
		
			var trantype = document.forms[0].elements['reportDto.transactionType'];
			if(trantype != null) {
				if(trantype.value != null ||
				   trantype.value != '') {
				   
					if(trantype.value == 'Both' || trantype.value == 'All') {
						
						document.getElementById('bothStatus').style.display = '';
			    		document.getElementById('iwStatus').style.display = 'none';
						document.getElementById('owStatus').style.display = 'none';
					}
					if(trantype.value == 'Inward' || trantype.value == 'inward') {
					
						document.getElementById('bothStatus').style.display = 'none';
						document.getElementById('iwStatus').style.display = '';
						document.getElementById('owStatus').style.display = 'none';
					}
					if(trantype.value == 'Outward' || trantype.value == 'outward') {
						
						document.getElementById('bothStatus').style.display = 'none';
						document.getElementById('iwStatus').style.display = 'none';
						document.getElementById('owStatus').style.display = '';
					}
				}
			} else {
				
				document.getElementById('bothStatus').style.display = '';
			    document.getElementById('iwStatus').style.display = 'none';
				document.getElementById('owStatus').style.display = 'none';
			}
		}
}
function setDate(e) {

	fPopCalFuture(e);
	return false;
}

function isValidateNumber(e) {

    var charpos = e.value.search("[^0-9.]");
    if(e.value.length > 0 &&  charpos >= 0) {

        e.value = e.value.substring(0, charpos);
        alert('Only numeric value is allowed in this field.');
        return false;
    }
    var dotCharpos = e.value.search("[.]");
    if(e.value.length > 0 &&  dotCharpos >= 0) {

        var dotValue = e.value.substring(dotCharpos+1, e.value.length);
        var dotCharpos1 = dotValue.search("[.]");
        if (dotCharpos1 >= 0) {
            e.value = e.value.substring(0, dotCharpos+1 + dotCharpos1);
            return false;
        }

        if (dotValue.length>2) {

            e.value = e.value.substring(0, dotCharpos+1 + 2);
        }
    }
    return true;
}

function validateAmount() {

    var fromAmt = document.forms[0].elements['reportDto.fromAmount'].value;
    var toAmt = document.forms[0].elements['reportDto.toAmount'].value;
    if (Number(fromAmt) > Number(toAmt)) {

        alert("FromAmount cannot be greater than ToAmount.");
        return false;
    }
    return true;
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

function getBranchList() {

    document.forms[0].action='/insta/reports/reportInput.do?mode=branchList';
    document.forms[0].submit();
}

function generateTranDetailsReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {

        document.forms[0].action='/insta/reports/tranDetailsReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrIndividualReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {

        document.forms[0].action='/insta/reports/brIndividualReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrSummaryReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brSummaryReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrInwReturnedReport() {

    if (dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)) {
        document.forms[0].action='/insta/reports/brInwardReturnedReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrInwBankwiseReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brInwardBankwiseReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrOutBankWiseReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brOutwardBankwiseReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateUTRNoWiseReport() {

    if (document.forms[0].elements['reportDto.utrNo']==null || trim(document.forms[0].elements['reportDto.utrNo'].value)=='') {
        alert("Please enter value for the UTR Number field.");
        return false;
    } 
    //else if ((trim(document.forms[0].elements['reportDto.utrNo'].value)).length<16) {
    //	alert("Please enter valid UTR Number.");
    //    return false;
    //}
    
    //if((trim(document.forms[0].elements['reportDto.utrNo'].value)).length==16) {
    //    if(!isValidAlphaNumeric(document.forms[0].elements['reportDto.utrNo'].value)) {
    //        alert('Only AlphaNumeric values are allowed');
    //        return false;
    //    }
    //}
    
    document.forms[0].action='/insta/reports/utrNoWiseReport.do?mode=viewreport&module=reports';
    document.forms[0].submit();

}

function generateCPWiseReconcilliationReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/cpWiseReconcilliationReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrWiseReconcilliationReport() {

    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brWiseReconcilliationReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrSubtypeIndividual() {

	if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brSubTypeIndividualReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateBrInwRejectedByUser() {
    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/brInwRejectedByUserReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateOutwardReturned() {
    if(validateAmount() && dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/owReturnedReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateFutureDatedTxnsReport() {
     if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/futureDatedTxnsReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateExceptionReport() {
    if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/exceptionReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateUnsuccessPaymentReport() {
    if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/unsuccessfulPaymentReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function generateReturnPaymentRejectedReport() {
    if (document.forms[0].elements['reportDto.userId'].value == '') {
        alert("Please select any UserId");
        return false;
    }
    if(dateValidation(document.forms[0].elements['reportDto.valueDate'].value, document.forms[0].elements['reportDto.toDate'].value)){

        document.forms[0].action='/insta/reports/returnPaymentRejectedReport.do?mode=viewreport&module=reports';
        document.forms[0].submit();
    }
}

function setBranchList() {
    if(document.forms[0].report.value=='InwardRejectedByUser') {
    
       document.forms[0].action='/insta/reports/brInwRejectedByUserReport.do?mode=input';
       document.forms[0].submit();
    } else if(document.forms[0].report.value=='returnPaymentRejectedReport') {
    
       document.forms[0].action='/insta/reports/returnPaymentRejectedReport.do?mode=input';
       document.forms[0].submit();
    }
    
}

function suppressCode(obj) {
	
	if(window.event.keyCode == 13) {
    
	if (document.forms[0].elements['reportDto.utrNo']==null || trim(document.forms[0].elements['reportDto.utrNo'].value)=='') {
        alert("Please enter value for the UTR Number field.");
        window.event.keyCode = 0;
        return false;
    } 
    //else if ((trim(document.forms[0].elements['reportDto.utrNo'].value)).length<16) {
    //	alert("Please enter valid UTR Number.");
    //	window.event.keyCode = 0;
    //    return false;
    //}
    
    if((trim(document.forms[0].elements['reportDto.utrNo'].value)).length==16) {
    
    	//Commented the below code, for the Issue in onsite while searching the UTR Numberwise report.
    	// NEFT Special charecter UTR search. The text box should allow special charecters to enter.
        //if(!isValidAlphaNumeric(document.forms[0].elements['reportDto.utrNo'].value)) {
        //    alert('Only AlphaNumeric values are allowed');
        //    window.event.keyCode = 0;
        //    return false;
        //}
    }
    
    document.forms[0].action='/insta/reports/utrNoWiseReport.do?mode=viewreport&module=reports';
    document.forms[0].submit();
	}
}

function setStatus(e1) {
	document.forms[0].elements['reportDto.status'].value = e1.value
}

function setPaymentType(e1) {
	document.forms[0].elements['reportDto.paymentType'].value = e1.value
}
	
//-->
</script>
