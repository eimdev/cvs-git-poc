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
<nested:form action="/brOutwardBankwiseReport">
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
              <td colspan="7" align="center" class="row-value">
                        <!-- Here all the input details should be here -->
                 <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
        		 <nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
              	 <nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>
                  <b>
                  <font size="2.5">
                    Branch Report - Outward - Bank Wise from <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,toDate)%>
                    </font>
                  </b>
              </td>
            </tr>
            <nested:define id="toAmt" property="reportDto.toAmount" type="java.lang.String" />
            <nested:define id="fromAmt" property="reportDto.fromAmount" type="java.lang.String" />
            <%if(new BigDecimal(toAmt).compareTo(BigDecimal.ZERO) >0){%>
            <tr>
              <td colspan="11" align="center" class="row-value">
                  <b>
                    Amount Range from <%=FormatAmount.formatINRAmount(fromAmt)%> to <%=FormatAmount.formatINRAmount(toAmt)%>
                  </b>
              </td>
            </tr>
            <%}%>
            <tr>
                 <td colspan="7" align="right" class="row-value">
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
                BigDecimal bankTotAmt = BigDecimal.ZERO;
                String bank = "";
            %>
            <nested:notEmpty property="reportMap">
            <nested:iterate id="_reportMap" property="reportMap">
              <nested:define id="_bank" name="_reportMap" property="key" type="java.lang.String" />
              <nested:define id="_reportList" name="_reportMap" property="value" type="java.util.List" />
              <tr>
              <td  colspan="7" align="center">
                <TABLE>
                    <tr class="row-value">
                    <%
                        bank = _bank;
                    %>
						<td colspan=7 align="left"><b> Bank : <%=bank%> </b> </td>
               		</tr>
                    <tr class="row-label th-bc">
                        <td width="5%" align="center">S.No</td>
                        <td width="15%" align="center" >Value Date</td>
                        <td width="15%" align="center" >Msg Type</td>
                        <td width="15%" align="center" >UTR Number</td>
                        <td width="25%" align="center" >Sender Address</td>
                        <td width="25%" align="center" >Receiver Address</td>
                        <td width="20%" align="center" >Amount(Rs.)</td>
                    </tr>
                    <%
                        int i=1;
                        BigDecimal subTotAmt = BigDecimal.ZERO;
                        int size = _reportList.size();
                        for(Iterator iter = _reportList.iterator(); iter.hasNext();) {

                            ReportDTO _repDto = (ReportDTO)iter.next();
                    %>
                			<% if(i%2==0) { %>  
                			<tr class="row-value tr2-bc">
                			<% } else { %>
                			<tr class="row-value tr1-bc">
                			<% } %>
        						<td align="center" width="30"><%=i%></td>
                                <td align="center" nowrap>&nbsp;<%=InstaReportUtil.getDateInSpecificFormat(format,_repDto.getValueDate())%></td>
                                <td align="center" nowrap>&nbsp;<%=_repDto.getMsgType()%></td>
                                <td align="center" nowrap>&nbsp;<%=_repDto.getUtrNo()%></td>
                                <td align="center" >&nbsp;<%=_repDto.getSenderAddress()%></td>
                               	<td align="center" >&nbsp;<%=_repDto.getReceiverAddress()%></td>
                                <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(_repDto.getAmt())%></td>
                       		</tr>
                			<%
                            subTotAmt = subTotAmt.add(new BigDecimal(_repDto.getAmt()));
                            i++; 
                        }
                            %>

                   <% if(i%2==0) { %>  
                        <tr class="row-value tr2-bc">
                   <% } else { %>
                        <tr class="row-value tr1-bc">
                   <% } 

                            bankTotAmt = bankTotAmt.add(subTotAmt);
                   %>
                            <td colspan="6" align="right"><b>	Total (BankWise : <%=bank%> )&nbsp;&nbsp;</b></td>
                            <td align="right" nowrap>&nbsp;<b><%=FormatAmount.formatINRAmount(subTotAmt)%></b></td>
                        </tr>
                    <%  

                    if (size==0) {
                    %>
                        <tr class="row-value tr2-bc">
                            <td colspan="7" align="center"><b>	No Data Found.  </b> </td>
                        </tr>
                    <%
                    }
                    %>
                </TABLE>    
              </td></tr>
            </nested:iterate>
            <tr class="row-value tr2-bc">
                <td colspan="7" align="right"><b>	Total Amount : &nbsp; <%=FormatAmount.formatINRAmount(bankTotAmt)%></b></td>
            </tr>
          </nested:notEmpty>
          <nested:empty property="reportMap">
            <tr class="row-value tr2-bc">
                <td colspan="7" align="center"><b>	No Data Found.   </b></td>
            </tr>
          </nested:empty>
	  </table>
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

        document.forms[0].action='/insta/reports/reportInput.do?module=reports&mode=input&report=OutwardBankwiseReport';
        document.forms[0].submit();
    }
//-->

</script>
</div>
</nested:form>
