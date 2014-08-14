<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.workflow.util.InstaUtil" %>
<%@ page import="com.objectfrontier.insta.workflow.util.FormatAmount" %>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Iterator" %>
<%@page import="com.objectfrontier.insta.reports.dto.ReportDTO"%>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/owReturnedReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
            <tr>
              <td colspan="8" align="center" class="row-value">
                        <!-- Here all the input details should be here -->
                 <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        		 <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
              	 <nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>   
                  <b>
                  <font size="2.0">
                  	Outward Returned Report From <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,toDate)%>
                    </font>
                  </b>
              </td>
              </tr>
            <nested:define id="toAmt" property="reportDto.toAmount" type="java.lang.String" />
            <nested:define id="fromAmt" property="reportDto.fromAmount" type="java.lang.String" />
            <%if( new BigDecimal(toAmt).compareTo(BigDecimal.ZERO) >0){%>
            <tr class="row-value">
              <td colspan="11" align="center">
                  <b>
                    Amount Range from <%=FormatAmount.formatINRAmount(fromAmt)%> to <%=FormatAmount.formatINRAmount(toAmt)%>
                  </b>
              </td>
            </tr>
            <%}%>
              <tr class="row-value">
                 <td colspan="8" align="right">
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
                BigDecimal totAmt = BigDecimal.ZERO;
            %>
            <nested:notEmpty property="reportDTOs">
            <%
              			boolean k= false;
            %>
            <tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="5%" align="center" >Value Date</td>
                <td width="15%" align="center" >Sender Address</td>
                <td width="15%" align="center" >Receiver Address</td>
                <td width="10%" align="center" >UTR Number</td>
                <td width="10%" align="center" >Original UTR Number</td>
                <td width="15%" align="center" >Info</td>
                <td width="15%" align="center" >Additional Info</td>
                <td width="10%" align="center" >Amount</td>
            </tr> 
            <nested:iterate indexId = "index" id ="reportDto" property="reportDTOs"> 
              <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td width="5%" align="center" nowrap>&nbsp;
                <nested:define id = "date" property="valueDate" type="java.lang.String"/>
                <%=InstaReportUtil.getDateInSpecificFormat(format,date)%>
                </td>
                <td width="15%" align="left">
                	<nested:write property = "senderAddress"/>
                </td>    
                <td width="15%" align="left">
                	<nested:write property = "receiverAddress"/>
                </td>     
                <td width="10%" align="left">
                	<nested:write property = "utrNo"/>
                </td> 
                <td width="10%" align="left">
                	<nested:write property = "outUTRNo"/>
                </td>
                <td width="15%" align="left" wrap>
                <nested:write property = "fieldI7495"/>
                </td>
                <td width="15%" align="left" wrap>
                <nested:write property = "fieldA7495"/>
                </td>
                <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                 <% totAmt = totAmt.add(new BigDecimal(txnAmt));%>
                 <td width="10%" align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
               </tr>
            </nested:iterate>
           <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <td colspan="11" align="right"><b>	Total  Amount : &nbsp; <%=FormatAmount.formatINRAmount(totAmt)%></b></td>
            </tr>
            
          </nested:notEmpty>
          <nested:empty property="reportDTOs">
            <tr class="row-value tr2-bc">
                <td colspan="8" align="center"><b>	No Data Found.   </b></td>
            </tr>
          </nested:empty>
	     </TABLE> 
    </td>
	</tr>	
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function printScript() {

        var title = new String("");
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }

    function goBack() {

        document.forms[0].action='/insta/reports/reportInput.do?module=reports&mode=input&report=OwReturnedReport';
        document.forms[0].submit();
    }
//-->

</script>
</div>
</nested:form>
