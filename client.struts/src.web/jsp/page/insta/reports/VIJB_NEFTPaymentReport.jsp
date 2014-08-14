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
<nested:form action="/neftPaymentReport">
<div id="printReady">
<%
    String imagePath = imageBase + "/print.jpg";
    String title = "";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td class="error" height="10"><nested:errors/></td>
    </tr>
      <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
    <tr>
	    <td>
		 <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
            <tr>
              <td colspan="8" align="center" class="row-value">
                        <!-- Here all the input details should be here -->
                  <b>
                  <font size="2.0">
                  <nested:define id="paymentType" property="reportDto.paymentType" type="java.lang.String" />
                  <nested:define id="transactionType" property="reportDto.transactionType" type="java.lang.String" />
                    <% if(transactionType.equals("inward")) {
					    title = "Payment Received Report"; %> Payment Received Report<% 
					} else if(transactionType.equals("outward")) {
					    title = "Payment Submitted Report"; %> Payment Submitted Report<%
					}
					if(paymentType.equals("ALL")) {
					    %> ALL Payments<% 
					} else if(paymentType.equals("N02")) {
					    %> N02<%
					} else if(paymentType.equals("N06")) {
					    %> N06<%
					} else if(paymentType.equals("N07")) {
					    %> N07<%
					}
					%> From <nested:write property="reportDto.valueDate"/> to <nested:write property="reportDto.toDate"/>
					<BR>with status  <nested:write property="reportDto.statusValue"/>
                    </font>
                  </b>
              </td>
              </tr>
            <nested:define id="toAmt" property="reportDto.toAmount" type="java.lang.String" />
            <nested:define id="fromAmt" property="reportDto.fromAmount" type="java.lang.String" />
            <%if(new BigDecimal(toAmt).compareTo(BigDecimal.ZERO)>0){%>
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
              	   <b>
              	     Report Printed on <nested:write property="currentReportPrintTime"/>
              	   </b>
                 </td>
              </tr>
            <%
                BigDecimal totAmt = BigDecimal.ZERO;
            %>
            <nested:notEmpty property="detailReportDTOs">
            <%
              			boolean k= false;
            %>
            <tr class="row-label th-bc">
                <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >Value Date</td>
                <td width="25%" align="center" >Sender Address</td>
                <td width="25%" align="center" >Receiver Address</td>
                <td width="15%" align="center" >UTR Number</td>
                <% if(paymentType.equals("N02")) { %>
					<td width="15%" align="center" >Sender Account Number</td>
                	<td width="15%" align="center" >Sender Details</td>
                <% } else if(paymentType.equals("N06") || paymentType.equals("N07") || paymentType.equalsIgnoreCase("ALL")) {%>
                	<td width="15%" align="center" >Beneficery Account Number</td>
                	<td width="15%" align="center" >Beneficery Details</td>
                <% } %>	
                <td width="20%" align="center" > Amount(Rs.)</td>
                 <td width="20%" align="center"> Status</td>
            </tr> 
            <nested:define id ="subType" property="reportDto.paymentType" type="java.lang.String"/>
            <nested:iterate indexId = "index" id ="reportDto" property="detailReportDTOs"> 
              <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td align="center" nowrap>&nbsp;
                <nested:write property = "valueDate"/>
                </td>
                <td align="left">
                <nested:write property = "senderAddress"/>
                </td>    
                <td align="left">
                <nested:write property = "receiverAddress"/>
                </td>     
                <td align="left">
                <nested:write property = "utrNo"/>
                </td> 
                <nested:define id ="msgSubType" property="msgSubType" type="java.lang.String"/>
                <% if(subType.equalsIgnoreCase("ALL")) {%>
                	<% if(msgSubType.equalsIgnoreCase("N06") || msgSubType.equalsIgnoreCase("N07")) { %>
		            	<td align="left" wrap>
		                <nested:write property = "field6061"/>
		                </td>
		                <td align="left" wrap>
		                <nested:write property = "field6081"/> - <nested:write property = "field5565"/> 
		                </td>
		            <% }  %>    
                <% } else { %>
                	<% if(subType.equalsIgnoreCase("N02")) { %>
                		<td align="left" wrap>
		                <nested:write property = "field6021"/>
		                </td>
		                <td align="left" wrap>
		                <nested:write property = "field6091"/>
		                </td>
                	<% } else if(subType.equalsIgnoreCase("N06") || subType.equalsIgnoreCase("N07")) {%>
                		<td align="left" wrap>
		                <nested:write property = "field6061"/>
		                </td>
		                <td align="left" wrap>
		                <nested:write property = "field6081"/> - <nested:write property = "field5565"/> 
		                </td>
                	<% } %>
                <% } %>
                 <nested:define id="txnAmt" property="amt" type="java.lang.String"/>
                 <% totAmt = totAmt.add( new BigDecimal(txnAmt));%>
                 <td align="right" nowrap>&nbsp;<%=FormatAmount.formatINRAmount(txnAmt)%></td>
                 <td align="center" wrap>
		           <nested:write property="status"/> 
		        </td>
               </tr>
            </nested:iterate>
           <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <td colspan="11" align="right"><b>	Total  Amount : &nbsp; <%=FormatAmount.formatINRAmount(totAmt)%></b></td>
            </tr>
            
          </nested:notEmpty>
          <nested:empty property="detailReportDTOs">
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

        var title = '<%=title%>';
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
    
//-->

</script>
</div>
</nested:form>
