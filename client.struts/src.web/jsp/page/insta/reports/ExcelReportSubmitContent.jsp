<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>

<% try {%>
<nested:form action="/submitExcelReport">
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
            <TR>
            <TD class=column-label width="25%">
                <nested:message key="label.report.transactionType"/>
            </TD>
           <TD class=row-value width="75%"> 
			   		<nested:select property="newReportDto.tranType" size="1" style="width:220" styleClass="select">
                            <nested:define id="col" property="listOfTranType"/>
						<nested:options name="col"/> 
         			</nested:select>
            </TD> 
            
         </TR> 
         <TR>
            <TD class=column-label width="25%">
                <nested:message key="label.listmessages.messagesubtype"/>
            </TD>
             <TD class=row-value width="75%"> 
			   		<nested:select property="newReportDto.msgSubType" size="1"  style="width:220" styleClass="select">
			   			<nested:define id="col" property="subTypeMap"/>
						<nested:options collection="col" property="value" labelProperty="key"/> 
					</nested:select>
            </TD>          
         </TR>
         </table>
	</td>
	</tr>	
	
</table>
<nested:hidden property="newReportDto.branchId"/> 
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

	if (document.forms[0].elements['newReportDto.branchCode']) {
		setList(document.forms[0]);
	} 
}

function executeRHS(form, action) {

	document.forms[0].action = "<%=appBase%>" + action;
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
    form.elements['newReportDto.branchId'].value = getOptionName(form.elements['newReportDto.branchCode'],form.elements['newReportDto.branchCode'].value);
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
function generateExcelSheetReport() {

    document.forms[0].action='/insta/reports/submitExcelReport.do?mode=controller&action=submit&module=reports';
    document.forms[0].submit();
}
	



//-->
</script>

</nested:form>
<%	} catch(Exception e) {
		e.printStackTrace();
	}
%>
