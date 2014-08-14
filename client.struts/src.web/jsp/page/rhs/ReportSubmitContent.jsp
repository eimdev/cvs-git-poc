<%@ include file="RHSStandard.jsp" %>

<%@page import="com.objectfrontier.insta.reports.constants.ReportConstants"%>


<script language="JavaScript" src="<%=scriptBase%>/select_date.js"></script>

<nested:form action="/submitReport">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td class="error" height="10"><nested:errors/></td>
	</tr>
<tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
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
		<nested:equal property="currentMode" value="<%=ReportConstants.BRANCH%>">
          <TR>
            <TD class=column-label width="25%">
                <nested:message key="label.branchmaster.branchcode"/>
            </TD>
            <TD class=row-value width="75%"> 
            <nested:write property="reportDTO.branchCode"/>
            </TD>
         </TR>
        </nested:equal>  
        
        <!--  RBC  -->
        <nested:equal property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
        	<TR>
            <TD class=column-label width="25%">
                <nested:message key="label.listmessages.utrnumber"/>
            </TD>
            <TD class=row-value width="75%"> 
            <nested:text property="reportDTO.utrNumber" styleClass="textbox" maxlength="16"/>
            </TD>
        
        </nested:equal>
        
        <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
        
        	
           
		  <TR>
            <TD class=column-label width="25%">
                <nested:message key="label.report.transactionType"/>
            </TD>
            <TD class=row-value width="75%"> 
			   		<nested:select property="reportDTO.tranType" size="1"  style="width:220" styleClass="select">
			   			<nested:define id="col" property="tranTypeList"/>
						<nested:options name="col"/> 
					</nested:select>
            </TD>
         </TR> 
          
         <TR>
            <TD class=column-label width="25%">
                <nested:message key="label.listmessages.messagesubtype"/>
            </TD>
            <TD class=row-value width="75%"> 
			   		<nested:select property="reportDTO.msgSubType" size="1"  style="width:220" styleClass="select">
			   			<nested:define id="col" property="subTypeMap"/>
						<nested:options collection="col" property="value" labelProperty="key"/> 
					</nested:select>
            </TD>           
         </TR>
         <nested:notEqual property="reportDTO.txnStatus" value="<%=RHSJSPConstants.TIMED_OUT%>"> 
      <!-- RBC CMD 1.0   <TR>
            <TD class=column-label width="25%">
                <nested:message key ="label.report.sender.receiver.ifsc"/> <nested:message key="label.mandatory.indicator"/>
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select property="reportDTO.branchCode" size="1" style="width:180" styleClass="select">
    				<html:option value="All Branches"><nested:message key="label.report.allBranches"/></html:option>
					<nested:define id="_branches" property="branches"/>
					<html:options collection="_branches" property="branchCode" labelProperty="ifsc"/>
 				</nested:select>
            </TD>
          </TR>
          
       -->   
         </nested:notEqual>
		</nested:notEqual>
		
		<nested:equal property="currentMode" value="<%=ReportConstants.CONTROLLER%>">
        <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
		  

          <TR>
            <TD class=column-label width="25%">
                <nested:message key ="label.report.branch"/> <nested:message key="label.mandatory.indicator"/>
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select styleId = "branchCode" property="reportDTO.branchCode" size="1" style="width:180" styleClass="select" onchange = "setList(this.form);">
    				<html:option value="All Branches"><nested:message key="label.report.allBranches"/></html:option>
					<nested:define id="_branches" property="branches"/>
					<html:options collection="_branches" property="branchCode" labelProperty="ifsc"/>
 				</nested:select>
            </TD>
          </TR>
         </nested:notEqual> 
        </nested:equal>

          <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
          <nested:notEqual property="reportDTO.txnStatus" value="<%=RHSJSPConstants.TIMED_OUT%>"> 
          <TR>
            <TD class=column-label width="25%">
            	<nested:message key="label.fromamount"/> 
            </TD>
            <TD class=row-value width="75%"> 
   	      		<nested:text styleId = "fromAmount" property="reportDTO.fromAmount" maxlength="20" 
				    onkeydown = "checkAmt(this)"  styleClass="textbox" />
	      		
            </TD>
          </TR>
          
          <TR>
            <TD class=column-label width="25%">
            	<nested:message key="label.toamount"/>   
            </TD>
            <TD class=row-value width="75%"> 
   	      		<nested:text styleId = "toAmount" property="reportDTO.toAmount" maxlength="20" 
				    onkeydown = "checkAmt(this)"  styleClass="textbox" />
	      		
             </TD>
          </TR>
       	<nested:notEqual property="reportDTO.reportType" value="<%=RHSJSPConstants.DETAILED_VIEW%>">   
        <tr>          
	    	<td width="20%" class="column-label" nowrap align="left">
		    	<nested:message key="label.branchreports.user"/>&nbsp;&nbsp;
		    </td>
	      	<td width="40%" class="row-value" align="left" colspan="3"> 
				
		        <nested:select property="reportDTO.userId" size="1" styleClass="select">
					<html:option value="">--Select--</html:option>
					<html:options property="userIdList"/>
				</nested:select>
			</td>
		</tr>
		</nested:notEqual>
		</nested:notEqual>
		<TR>
            <TD class=column-label width="25%">
            	<nested:message key="label.fromdate"/> <nested:message key="label.mandatory.indicator"/>
            </TD>
            <TD class=row-value width="75%"> 
   	      		<nested:text property="fromDate" maxlength="20" styleClass="textbox" readonly="true" onclick="setToDate(document.forms[0].elements['fromDate'])"/>
	      		<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand" onclick="setToDate(document.forms[0].elements['fromDate'])">
            </TD>
          </TR>
          
          <TR>
            <TD class=column-label width="25%">
            	<nested:message key="label.todate"/> <nested:message key="label.mandatory.indicator"/>  
            </TD>
            <TD class=row-value width="75%"> 
   	      		<nested:text property="toDate" maxlength="20" styleClass="textbox" readonly="true" onclick="fPopCal(document.forms[0].elements['toDate']);return false"/>
	      		<img src="<%=imageBase%>/cal.gif" border="0" alt="Select Date" style="vertical-align:top;cursor:hand" onclick="fPopCal(document.forms[0].elements['toDate']); return false">
            </TD>
          </TR>

          <!-- <TR>
            <TD class=column-label width="25%">
                <nested:message key ="label.bankmasterlist.sol"/>
            </TD>
            <TD class=row-value width="75%"> 
				<nested:select property="SOLCode" size="1" style="width:80" styleClass="select">
				<html:option value="">--Select--</html:option>
					<nested:iterate property="SOLList">
					    <nested:define id = "code" property = "SOLCode" type="java.lang.String"/>
					    <html:option  value = "<%=code%>"/>
					</nested:iterate>
 				</nested:select>
            </TD>
          </TR> -->
		</nested:notEqual>
		</table>
	</td>
	</tr>	
	
</table>
<nested:hidden property="reportDTO.branchId"/>
<script>
<!--



 var obj = document.getElementById("branchType"); 

 if (obj != null) { // only for controller reports

	if ((!document.forms[0].branchType[1].checked) && (!document.forms[0].branchType[2].checked)) {
		document.forms[0].branchType[0].checked = true;
	}

	if ((document.forms[0].branchType[1].checked) || (document.forms[0].branchType[2].checked)) {

		document.forms[0].branchCode.remove(0);
	} 

 }
function init() {

	if (document.forms[0].elements['reportDTO.branchCode']) {
		setList(document.forms[0]);
	} 
}

function executeRHS(form, action) {

	document.forms[0].action = "<%=rhsBase%>" + action;
	document.forms[0].submit();
}

function setToDate(el) 
{
	fPopCal(el);
	if (document.forms[0].elements['fromDate'].value != "") 
	{
		document.forms[0].elements['toDate'].value = document.forms[0].elements['fromDate'].value
	}
	return false;
}

function checkAmt(obj) {

    if (event.shiftKey){
        event.returnValue=false;
	}

    if (event.keyCode !=13) {

        if ((event.keyCode > 47 && event.keyCode < 58) ||
            (event.keyCode > 95 && event.keyCode < 106) ||
            (event.keyCode == 8) || (event.keyCode == 46) ||
            (event.keyCode == 110) || (event.keyCode == 190) ||
		    (event.keyCode == 37) || (event.keyCode == 39 ) ||
		    (event.keyCode == 9) )
            {
                    var n = obj.value;
                    if(n.indexOf(".") != -1 &&
                          (event.keyCode == 110 || event.keyCode == 190 )){
                         event.returnValue=false;
                    }
			}
           else
           {
			   event.returnValue=false;
           }
	}
}

 function fmtAmt(obj1) //This function is used to format amount  like 100000 as 100,000.000
    {
        alert('inside function . value is '+ obj1.value);
        var n = obj1.value;

        var myNum = n.toString(),fmat = new Array();
        var len = 0, deci = ".000";

        if(myNum.length == 0)
               myNum = "0";

        if(myNum.indexOf(".") != -1){
	        l = myNum.indexOf(".");
            deci = myNum.substring(l);
            myNum = myNum.substring(0,l);
            if(myNum.length == 0)
		        myNum = "0";
        }

        for(var i = 1; i < myNum.length + 1; i++)
                fmat[i] = myNum.charAt(i-1);

        if(deci.length > 0){
	        if(deci.indexOf(',') != -1 ){
		        alert("Please Check the entered figure, Comma not allowed in decimal part..");
                obj1.focus();
                }
        }

        if(deci.length > 4)
                deci = deci.substring(0,3);

            var val = fmat.join('');
			alert(val);
            val += deci;
 //           obj1.value=val;
       alert('finally obj1 value is ' + obj1.value);
	}

function setBranchList() {
	
	executeRHS(this.form, "/submitReport.do?mode=controller&action=input");

}

<!-- BoR CMD 1.0 -->
function setList(form) {
    form.elements['reportDTO.branchId'].value = getOptionName(form.elements['reportDTO.branchCode'],form.elements['reportDTO.branchCode'].value);
}

function getOptionName(optionElement, value)
{
	for (var i = optionElement.options.length-1; i >= 0; i--) 
	{
		if (value == optionElement.options[i].value)
		{
			return optionElement.options[i].text;
		}
	}
}	



//-->
</script>

</nested:form>
