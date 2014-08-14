<%@ include file="RHSStandard.jsp" %>
<%@ page import="com.objectfrontier.insta.reports.server.util.FormatAmount" %>
<%@ page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils" %>
<%@page import="com.objectfrontier.insta.rtgs.reports.bean.ReportBean"%>
<%@page import="com.objectfrontier.insta.workflow.util.InstaUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.math.BigDecimal"%>

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
    <!-- RBC CMD 1.0 -->
    
    <nested:notEqual property="utrWise" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
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
                <nested:message key="label.todate"/> :  
            </TD>
            <TD class=row-value width="217" height="9"> 
                <nested:write property="reportDTO.toDate" format="dd-MM-yyyy"/>
            </TD>
          </tr>
          <!-- RBC CMD 1.0 -->
    	  <nested:notEqual property="reportDTO.txnStatus" value="<%=RHSJSPConstants.TIMED_OUT%>"> 
          
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
          </nested:notEqual>
		</table>
	</td>
	</tr>
	</nested:notEqual>
	
	<nested:notEmpty property="utrWise">
		<nested:define property="utrWise" id="utr"/>
	</nested:notEmpty>
	
    <TR>
      <TD vAlign=top height="21" >
      <table  class="inner-table table-border" cellSpacing="1" cellPadding="2" width="100%" border="0">
          <tr class="row-label th-bc">
            <td align="middle" width="5%"><nested:message key="label.serialno"/></td>
            <td align="middle"><nested:message key="label.rtgsbaltxn.utrnumber"/></td>
            <td align="middle"><nested:message key="label.rtgsbaltxn.otherbank"/></td>
          
          <!-- For Report Filtering  
            <td align="middle"><nested:message key="label.report.inout"/> </td>
          -->
            
            <td align="middle"><nested:message key="label.report.inAmount"/></td>
            <td align="middle"><nested:message key="label.report.outAmount"/></td>
            <nested:equal property="reportDTO.txnStatus" value="<%=RHSJSPConstants.ALL_TRANSACTION%>">
                <td align="middle">
                    <nested:message key="label.listmessages.status"/>
                </td>
            </nested:equal>
            
            <nested:equal property="reportDTO.txnStatus" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
                <td align="middle">
                    Value Date
                </td>
                <td align="middle">
                    <nested:message key="label.listmessages.status"/>
                </td>
            </nested:equal>
            
            <!-- <td align="middle"><nested:message key="label.rtgsbaltxn.time"/></td> -->
            <!-- <td align="middle"><nested:message key="label.report.date"/></td> -->
            <!--td noWrap align="middle"></td-->
          </tr>
            
            <nested:define id="tStatus" property="reportDTO.txnStatus" type="java.lang.String"/>

	<tr>   	
		<td>
			<nested:define id="_datesMap" property="datesMap"/>
				<nested:iterate id ="element" name="_datesMap">

				<nested:define id ="_reportDTO" name="element" property="value"/>
					<!-- <nested:write name="element" property="key" format="dd-MMM-yyyy"/> -->
					<!-- <nested:write name="_reportDTO" property="branchwiseDTOs"/> -->
					
					<nested:iterate name="_reportDTO" property="branchwiseDTOs"> 
					
			 			<nested:notEqual name="utr" value="<%=RHSJSPConstants.UTR_NO_WISE%>">
				            <tr class=""row-value tr1-bc"">
				            <%
				            // For an alignment
				              if (tStatus.equalsIgnoreCase(RHSJSPConstants.ALL_TRANSACTION) || (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) {
				            %>
				                  <td class="page-title-sub" align="left" colSpan="7">
				            <% } else { %>
				                <td class="page-title-sub" align="left" colSpan="6">
				            <% } %>
				                   <b><nested:write name="element" property="key" format="dd-MMM-yyyy"/></b>
				                 </td>
							</tr>
						</nested:notEqual>
			
			           	 <tr class="page-title-sub tr-bc-report">
					            <%
					            // For an alignment
					              if (tStatus.equalsIgnoreCase(RHSJSPConstants.ALL_TRANSACTION) || (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) {
					            %>
					                  <td class="page-title-sub" align="left" colSpan="7">
					            <% } else { %>
					                <td class="page-title-sub" align="left" colSpan="6">
					            <% } %>
			                   <b><nested:write property="branchName"/></b>
			                 </td>
			              </tr>
			              
			              
			              
			              
			              
			              
			              
			              
			              
			              <nested:notEmpty property="msgTypes"> 
			              	<nested:iterate property="msgTypes">
			                  <tr class="page-title-sub tr-bc-report">
			                    <%
			                      if (tStatus.equalsIgnoreCase(RHSJSPConstants.ALL_TRANSACTION) || (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) {
			                    %>
			                         <td class="page-title-sub-sub" align="left" colSpan="7">
			                    
			                    <% } else { %>
			                        
			                         <td class="page-title-sub-sub" align="left" colSpan="6">
			                         
			                    <% } %>
			  
			                        <nested:write property="msgType"/> / 
			                        <nested:write property="msgSubType"/> -
			                        <nested:write property="msgTypeName"/>
			                     </td>
			                  </tr>
			                  
			                  
			                  
			                  
					                  <nested:notEmpty property="messages"> 
					                  <nested:iterate indexId="index" property="messages">
					                    <% if(index.intValue()%2==0) { %>  
					                    <tr class="row-value tr2-bc-report">
					                    <% } else { %>
					                    <tr class="row-value tr1-bc-report">
					                    <% } %>
					                        <td align="center" width="5%"><%=index.intValue()+1%></td>
					                        <td align="middle"><nested:write property="utrNo"/></td>
					                        <td align="left"><nested:write property="otherBank"/></td>
					              <!-- For Report Filtering                        
					                        <td align="middle"><nested:write property="txnType"/></td>
					              -->
					                        
					                        <td align="right">
					                            <nested:notEqual property="amountDTO.inAmount" value="0">
					                            <nested:define id="inAmt" property="amountDTO.inAmount" type="java.lang.String"/>
					                                <%
					                                     out.println(FormatAmount.formatINRAmount(inAmt));
					                                %>
					                            </nested:notEqual>
					                            </td>
					                        <td align="right">
					                            <nested:notEqual property="amountDTO.outAmount" value="0">
					                            <nested:define id="outAmt" property="amountDTO.outAmount" type="java.lang.String"/>
					                                <%
					                                     out.println(FormatAmount.formatINRAmount(outAmt));
					                                %>
					                            </nested:notEqual>
					                        </td>
					                        <% if((RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) { %>
					                        	<td align="middle">
					                        		<nested:notEqual property="valueDate" value="">
					                                <nested:define id="_valueDate" property="valueDate"/>
					                                <%
					                                     out.println(InstaUtil.formatDateString(InstaUtil.formatDate((Timestamp)_valueDate)));
					                                %>
					                                </nested:notEqual>
					                            </td>
					                        <% } %>
					                        <% if((RHSJSPConstants.ALL_TRANSACTION).equalsIgnoreCase(tStatus) || (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) { %>
					                        	<td align="middle">
					                                <nested:write property="status"/>
					                            </td>
					                        <% } %>
					                        <!-- <td align="left" nowrap><nested:write property="entDate" format="dd-MM-yyyy"/></td> -->
					                        <!--td nowrap align="center"><input type="radio" value="V1" checked name="R1"></td-->
					                      </tr>
					                  </nested:iterate>
					                  </nested:notEmpty>
					                  
					                  
					                  
					                  
			                  <tr class="row-value tr1-bc-report">
			                  	<td align="right" colspan="3">
			                    <nested:message key="label.report.total"/></td>
			                    <td align="right"><b>
			                    <nested:notEqual property="grandTotalDTO.inAmount" value="0">                   
			                     <nested:define id="grtInAmt" property="grandTotalDTO.inAmount" type="java.lang.String"/>
			                        <%
			                             out.println(FormatAmount.formatINRAmount(grtInAmt));
			                        %>
			                        </b>
			                    </nested:notEqual>                  
			                    </td>
			                    <td align="right"><b>
			                    <nested:notEqual property="grandTotalDTO.outAmount" value="0">                  
			                     <nested:define id="grtOutAmt" property="grandTotalDTO.outAmount" type="java.lang.String"/>
			                        <%
			                             out.println(FormatAmount.formatINRAmount(grtOutAmt));
			                        %>
			                        </b>
			                    </nested:notEqual>                  
			                    </td>   
			                    <%
			                      if (tStatus.equalsIgnoreCase(RHSJSPConstants.ALL_TRANSACTION)|| (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) {
			                    %>
			                            <td align="center">&nbsp;</td>
			                            <td align="center">&nbsp;</td>            
			                    <% } else { %>
			                    <% } %>                    
			                  </tr>
			              </nested:iterate>
			              </nested:notEmpty>
			              
			              
              <tr class="row-value tr1-bc-report">
                <td align="right" colspan="3">
                <nested:message key="label.report.grandTotal"/></td>
                <td align="right"><b>
                <nested:notEqual property="grandTotalDTO.inAmount" value="0">                   
                 <nested:define id="grossGrtInAmt" property="grandTotalDTO.inAmount" type="java.lang.String"/>
                    <%
                         out.println(FormatAmount.formatINRAmount(grossGrtInAmt));
                    %>
                    </b>
                </nested:notEqual>
                </td>
                <td align="right"><b>
                <nested:notEqual property="grandTotalDTO.outAmount" value="0">     
                 <nested:define id="grossGrtOutAmt" property="grandTotalDTO.outAmount" type="java.lang.String"/>
                    <%
                         out.println(FormatAmount.formatINRAmount(grossGrtOutAmt));
                    %>
                    </b>
                </nested:notEqual>
                </td> 
                    <%
                      if (tStatus.equalsIgnoreCase(RHSJSPConstants.ALL_TRANSACTION)|| (RHSJSPConstants.UTR_NO_WISE).equalsIgnoreCase(tStatus)) {
                    %>
                            <td align="center">&nbsp;</td>
                            <td align="center">&nbsp;</td>            
                    <% } else { %>
                    <% } %>                    
              </tr>
            </nested:iterate>

				<nested:empty name="_reportDTO" property="branchwiseDTOs">  
	              <tr class=""row-value tr1-bc""> 
	              		<td class="page-title-sub" height="5" colspan="10" align="left">
	                    <b><nested:write name="element" property="key" format="dd-MMM-yyyy"/></b>
	              </td></tr>
	              <tr><td class="row-value tr1-bc-report" height="5" colspan="9" align="center">
	                    <nested:message key="label.report.noReport"/>
	              </td></tr>
	            </nested:empty>
			</nested:iterate>
			
			
			
			
			
			
			
			
			
			
			
			
			

           <nested:equal property="currentMode" value="<%=RHSJSPConstants.CONTROLLER%>">
             <nested:notEmpty property="reportDTO.branchwiseDTOs">  
               <nested:size id="beanSize" property="reportDTO.branchwiseDTOs"/>
                  <nested:notEqual name="beanSize" value= "1">
                    <tr class="row-value tr1-bc-report">

                    <td align="right" colspan="4"><nested:message key="label.report.allBranchGrandTotal"/>
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
            
            <!-- <nested:empty property="reportDTO.branchwiseDTOs">  
              <tr><td class="row-value tr1-bc" height="5" colspan="9" align="center">
                    <nested:message key="label.report.noReport"/>
              </td></tr>
            </nested:empty> -->
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
