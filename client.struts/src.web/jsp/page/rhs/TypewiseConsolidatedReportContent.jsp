<%@ include file="RHSStandard.jsp" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils" %>
<%@page import="com.objectfrontier.insta.rtgs.reports.bean.ReportBean"%>
<%@page import="com.objectfrontier.insta.reports.server.util.FormatAmount"%>

<nested:form action="/report">
<div id="printReady">
<%
String title = ReportBean.title;
title += ConversionUtils.getCurrentDateTime();
 
String imagePath = imageBase + "/print.jpg";
%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">

	<tr>
		<td class="error" height="10"><nested:errors/></td>
	</tr>
	<tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>
	<tr>
	 <TD vAlign=top>
		<table width="100%" border="0" cellspacing="1" cellpadding="2">
          <tr>
		    <TD class=column-label width="244" height="9">
                <nested:message key="label.fromdate"/> : 
		    </TD>
            <TD class=row-value width="217" height="9"> 
	            <nested:write property="reportDTO.fromDate" format="dd-MM-yyyy"/>
            </TD>
            <TD class=column-label width="244" height="9">
                <nested:message key="label.todate"/>  :  
            </TD>
            <TD class=row-value width="217" height="9"> 
	            <nested:write property="reportDTO.toDate" format="dd-MM-yyyy"/>
            </TD>
          </tr>
          <tr>
            <TD class=column-label width="244" height="9">
                <nested:message key="label.fromamount"/> : 
            </TD>
            <TD class=row-value width="217" height="9"> 
                <nested:write property="reportDTO.fromAmount" />
            </TD>
            <TD class=column-label width="244" height="9">
                <nested:message key="label.toamount"/> :  
            </TD>
            <TD class=row-value width="217" height="9"> 
                <nested:write property="reportDTO.toAmount" />
            </TD>
          </tr>
           <tr>
            <TD class=column-label width="244" height="9">
                <nested:message key="label.userid"/> : 
            </TD>
            <TD class=row-value width="217" height="9"> 
                <nested:write property="reportDTO.userId" />
            </TD>
            <tr>
		</table>
	</td>
	</tr>
    <TR>
	  <TD vAlign=top height="21" >
      <table class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc" >
            <td align="middle" width="5%"><nested:message key="label.serialno"/></td>
            <td align="middle"><nested:message key="label.transactiontype"/></td>
            <td align="middle"><nested:message key="label.report.inAmount"/></td>
            <td align="middle"><nested:message key="label.report.outAmount"/></td>
          </tr>
          
			<nested:define id="_datesMap" property="datesMap"/>
			<nested:iterate id ="element" name="_datesMap">
			<nested:define id ="_reportDTO" name="element" property="value"/>
			<!-- <nested:write name="element" property="key" format="dd-MMM-yyyy"/> -->
			<!-- <nested:write name="_reportDTO" property="typewiseDTOs"/> -->
			<nested:iterate name="_reportDTO" property="typewiseDTOs">      	  	   
              <tr class=""row-value tr1-bc"">
                <td class="page-title-sub" align="left" colspan="4">
                	<nested:write name="element" property="key" format="dd-MMM-yyyy"/>
                 </td>
              </tr>
              <nested:define property="msgType" id="type"/>
              <nested:define property="msgSubType" id="subType"/>
              <nested:define property="msgTypeName" id="typeName"/>
              
              <nested:notEmpty property="branches">	
	    	  <nested:iterate indexId="index" property="branches">
	    	  <tr class="page-title-sub tr-bc-report">
	    	  <td class="page-title-sub" align="left" colSpan="4">
	                	<nested:write property="branchName"/>
                	</td>
	    	  </tr>
    			<% if(index.intValue()%2==0) { %>  
    			<tr class="row-value tr2-bc-report">
    			<% } else { %>
    			<tr class="row-value tr1-bc-report">
    			<% } %>
                    <td align="center" width="5%"><%=index.intValue()+1%></td>
                   <td class="page-title-sub" align="left">
                   <nested:write name="type"/>/<nested:write name="subType"/>-<nested:write name="typeName"/>
                   
	                <!-- 	<nested:write property="branchName"/>-->
                	</td>
                	
		            <td align="right">
					<nested:notEqual property="grandTotalDTO.inAmount" value="0">	                
		    	     <nested:define id="grtInAmt" property="grandTotalDTO.inAmount" type="java.lang.String"/>
		        		<%
		        			 out.println(FormatAmount.formatINRAmount(grtInAmt));
		        		%>
		           	</nested:notEqual>
		            </td>
		            <td align="right">
		           	<nested:notEqual property="grandTotalDTO.outAmount" value="0">	   
		    	     <nested:define id="grtOutAmt" property="grandTotalDTO.outAmount" type="java.lang.String"/>
		        		<%
		        			 out.println(FormatAmount.formatINRAmount(grtOutAmt));
		        		%>
		           	</nested:notEqual>
		            </td>
              </tr>
          </nested:iterate>
          </nested:notEmpty>
          <tr class="row-value tr1-bc-report">
            <td align="right" colspan="2"><nested:message key="label.report.grandTotal"/></td>
            <td align="right"><b>
			<nested:notEqual property="grandTotalDTO.inAmount" value="0">	                
    	     <nested:define id="grossGrtInAmt" property="grandTotalDTO.inAmount" type="java.lang.String"/>
        		<%
        			 out.println(FormatAmount.formatINRAmount(grossGrtInAmt));
        		%></b>
           	</nested:notEqual>
            </td>
            <td align="right"><b>
           	<nested:notEqual property="grandTotalDTO.outAmount" value="0">	   
    	     <nested:define id="grossGrtOutAmt" property="grandTotalDTO.outAmount" type="java.lang.String"/>
        		<%
        			 out.println(FormatAmount.formatINRAmount(grossGrtOutAmt));
        		%></b>
           	</nested:notEqual>
            </td>
          </tr>
          </nested:iterate>

			<nested:empty name="_reportDTO" property="typewiseDTOs">  
              <tr class="row-value tr1-bc-report"> 
              		<td class="page-title-sub" height="5" colspan="4" align="left">
                    <b><nested:write name="element" property="key" format="dd-MMM-yyyy"/></b>
              </td></tr>
              <tr><td class="row-value tr1-bc-report" height="5" colspan="4" align="center">
                    <nested:message key="label.report.noReport"/>
              </td></tr>
            </nested:empty>
          </nested:iterate>
            <nested:equal property="currentMode" value="<%=RHSJSPConstants.CONTROLLER%>">
                <nested:notEmpty property="reportDTO.typewiseDTOs">    
                    <nested:size id="beanSize" property="reportDTO.typewiseDTOs"/>
                        <nested:notEqual name="beanSize" value= "1">
                            <tr class="row-value tr1-bc">
                            
                            <td align="right" colspan="2"><nested:message key="label.report.allBranchGrandTotal"/>
                            </td>
                            
                            <td align="right"><b> 
                            <nested:notEqual property="reportDTO.branchesGrandTotalDTO.inAmount" value="0.0">
                                <nested:define id="branchesgrtInAmt" property="reportDTO.branchesGrandTotalDTO.inAmount" type="java.lang.String"/>
                                    <%
                                        out.println(FormatAmount.formatINRAmount(branchesgrtInAmt));
                                    %></b>
                            </nested:notEqual> 
                            </td>
            
                            <td align="right"><b>
                            <nested:notEqual property="reportDTO.branchesGrandTotalDTO.outAmount" value="0.0">
                                <nested:define id="branchesgrtOutAmt" property="reportDTO.branchesGrandTotalDTO.outAmount" type="java.lang.String"/>
                                    <%
                                        out.println(FormatAmount.formatINRAmount(branchesgrtOutAmt));
                                    %></b>
                            </nested:notEqual>
                            </td>
                            </tr>
                       </nested:notEqual>
               </nested:notEmpty>
            </nested:equal>
                      
		<!--	<nested:empty property="reportDTO.typewiseDTOs">	
	          <tr><td class="row-value tr1-bc" height="5" align="center" colspan="4">
                    <nested:message key="label.report.noReport"/>
	          </td></tr>
			</nested:empty>	-->
      </table>
     </td>
	</tr>
</table>
<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
<script>
<!--
    function customscript() {
        var title = new String("<%= title %>");
        var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }

    function showExcel() {
				window.open("D:/rtgsdemo/webapps/rhs/rpt.xls");
    }

//-->

</script>
</div>
</nested:form>
