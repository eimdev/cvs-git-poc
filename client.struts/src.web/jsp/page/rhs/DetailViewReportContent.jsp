<%@ include file="RHSStandard.jsp" %>

<%@page import="com.objectfrontier.insta.reports.server.util.FormatAmount"%>
<%@page import="com.objectfrontier.insta.rtgs.reports.bean.ReportBean"%>
<%@page import="com.objectfrontier.insta.reports.constants.ReportConstants"%>
<%@page import="com.objectfrontier.rtgs.client.jsp.util.ConversionUtils"%>
<nested:form action="/report">
<div id="printReady">
<%
 String title = ReportBean.title;
 title += ConversionUtils.getCurrentDateTime();
 
 String imagePath = imageBase + "/print.jpg";
%>  
<%!
    public String convert(String displayFieldValue) {
    
        if (displayFieldValue != null 
                && displayFieldValue.indexOf("\r\n") > 0 ) {

            displayFieldValue = ConversionUtils.transformString(displayFieldValue, "\r\n", "<br>"); 
        } else if (displayFieldValue != null 
                && displayFieldValue.indexOf("\n") > 0 ) {

            displayFieldValue = ConversionUtils.transformString(displayFieldValue, "\n", "<br>");
        }
        return displayFieldValue;
    }
%>        
<table border="0" cellspacing="1" cellpadding="1" width="100%" valign="top">
	<tr>
	   <td class="error" colspan="2" height="10"><nested:errors/></td>
	</tr>
    <tr><td class="error" valign="middle" height="20"><br><nested:write property="message"/></td></tr>        
    <% boolean displayFieldNo = false; boolean utrIsEmpty = false; String utrWise_a= null;
    %>
    
   <nested:notEmpty property="utrWise">   
   	<nested:define id="utrWise_t" property="utrWise" type="java.lang.String"/>
   	<%
   		utrIsEmpty = true;
   		utrWise_a = utrWise_t;
   	%>
   </nested:notEmpty>
   
   
   <%if ( !utrIsEmpty || (utrIsEmpty && !utrWise_a.equalsIgnoreCase("utrNumberwise"))) { %>
   	
    <tr>
        <TD class=column-label width="25%" height="10">
                <nested:message key="label.fromdate"/> : 
        </TD>
        <TD class=row-value width="25%" height="10"> 
                <nested:write property="fromDate" format="dd-MM-yyyy"/>
        </TD>
        <TD class=column-label width="25%" height="10">
                <nested:message key="label.todate"/> :  
        </TD>
        <TD class=row-value width="25%" height="10"> 
                <nested:write property="toDate" format="dd-MM-yyyy"/>
        </TD>
    </tr>
    <%} %>
    
    
    
    
<nested:define id="tStatus" property="detailedReportDTO.txnStatus" type="java.lang.String"/>

<nested:notEmpty property="detailedReportDTO.detailedMessages">    
    <nested:define id="curMode" property="currentMode" type="java.lang.String"/>
    <% if (curMode != null && curMode.equalsIgnoreCase(ReportConstants.BRANCH)) { %>
        <tr>
            <td colspan="4">
                <font class="side-heading"><b><nested:write property="detailedReportDTO.branchName"/></b></font>
            </td>
        </tr>
    <% } %>
    
<nested:iterate id="dateElement" property="detailedReportDTO.detailedMessages" >  
    <tr>
        <td colspan="4">
        <table border="0" cellspacing="1" cellpadding="1" width="100%">
        <tr>
            <td>
                <font class="side-heading"><b><nested:message key="label.report.date"/> : <nested:write name="dateElement" property="key"/></b></font>
            </td>
        </tr>
<nested:iterate id="statusElement" name="dateElement" property="value">        
        <tr>
            <td>
                <table border="0" cellspacing="1" cellpadding="1" width="100%">
                <% if (curMode != null && curMode.equalsIgnoreCase(ReportConstants.CONTROLLER)) { %>
                <tr>
                    <td class="row-value th-bc">
                        <font class="side-heading"><b><nested:write name="statusElement" property="key"/></b></font>
                    </td>
                </tr>
                <% } %>       
                
<nested:iterate id="statusElement2" name="statusElement" property="value">
                <tr>
                    <td class="row-value td-bc">
                        <font class="side-heading"><b><nested:write name="statusElement2" property="key"/></b></font>
                    </td>
                </tr>

<nested:iterate id="detailedMsgDTO" name="statusElement2" property="value">
                <tr>
                    <td>
                    <table border="0" cellspacing="1" cellpadding="1" width="100%">
                    <tr>
                     <td>
                        <table border="0" cellspacing="1" cellpadding="1" width="98%" align="center">
                            <tr>
                                <td width="50%" class="column-label">
                                    <nested:message key = "label.listmessages.utrnumber"/>
                                </td>                        
                                
                                <td class="row-value" align="left" colspan="3">
                                    <nested:write property="utrNumber"/> 
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" class="column-label">
                                    <nested:message key="label.report.txnType"/>
                                </td>                        
                                                                
                                <td class="row-value" align="left" colspan="3">
                                    <nested:write property="tranType"/>
                                </td>
                            </tr>                                
                            <tr>               
                                <td width="50%" class="column-label">
                                <nested:message key="message.messagetype"/>
                                </td>
                                        
                                <td class="row-value">
                                <nested:write property="msgType"/> / <nested:write property="msgSubType"/> - <nested:write property="typeName"/>
                                </td>
                            </tr>
                
                            <!-- <nested:equal property="tranType" value="<%=RHSUIJSPConstants.INWARDTRANSACTIONTYPE%>">
                            <tr>
                                <td class="column-label">
                                <nested:message key="message.debittxnid" /> 
                                </td>
                                    
                                <td class="row-value">
                                <nested:write property="creditTxnId" />           
                                </td>
                            </tr>
                            </nested:equal>
                    
                            <nested:notEqual property="tranType" value="<%=RHSUIJSPConstants.INWARDTRANSACTIONTYPE%>">
                            <tr>
                                <td class="column-label">
                                <nested:message key="message.credittxnid" /> 
                                </td>
                                    
                                <td class="row-value" >
                                <nested:write property="creditTxnId" />           
                                </td>
                            </tr> 
                            </nested:notEqual> -->
        
                            <!-- <tr>
                                <td class="column-label">
                                <nested:message key="message.lotid" /> 
                                </td>
                                    
                                <td class="row-value">
                                <nested:write property="lotId" />
                                </td>
                            </tr> --->
                            
                            <%
                              if (tStatus.equalsIgnoreCase(ReportConstants.ALL)
                              || (tStatus.equalsIgnoreCase(ReportConstants.SUCCESSFUL))) {
                                  
                            %>
                                <tr>
                                    <td class="column-label">
                                        <nested:message key="label.report.transactionStatus"/>
                                    </td>
                                    
                                    <td class="row-value">
                                        <nested:write property="txnStatus"/>
                                    </td>
                                </tr>
                            <%
                             }
                            %>    
                            
                         </table>  
                        </td>
                        </tr>
                          
                        <tr>
                        <td>
                            <table border="0" cellspacing="1" cellpadding="1" width="99%" align="center">
                            <tr>
                                <td align="center" colspan="2">
                                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                <!------------- Message Block Start --------------->      
                                    <nested:notEmpty property="msgfields">
                                    <nested:iterate property="msgfields" indexId="index">
                                    <%  if (index.intValue() % 2 == 0) {%>
                                    <tr class="row-value tr1-bc-report" >
                                    <% } else { 
                                    %>
                                    <tr class="row-value tr2-bc-report">
                                    <% } %>
        
                                <!------------- Default Field Type Start ---------->
                
                                    <td class="column-label" width="50%">
                                    <nested:write property  ="fieldName"/> 
                             
                                    <% if (displayFieldNo) { %>
                                        
                                        (<nested:write property ="fieldNo"/>)
                                    <% } %>
                                    </td>
                                    
                                    <td class="column-label" width="50%">
                                        <nested:empty property = "displayValue">
                                            <nested:write property = "value"/>
                                        </nested:empty>
                                        
                                        <nested:notEmpty property = "displayValue">
                                          <nested:define id = "dispValue" property = "displayValue" type = "java.lang.String"/>
                                          <%= convert(dispValue) %>
                                        </nested:notEmpty>          
                                    </td>
                                    </tr>  
                                    </nested:iterate> 
                                    </nested:notEmpty> 
                                    </table>
                                  </td>
                            </tr>
                            </table>
                           </td>
                          </tr>

                    <!------- PI Response Message Field Type Start-------------> 
                    
                    <nested:present property="piMessage">
                    <tr>
                        <td align="center" colspan="2">
                            <table border="0" width="98%" cellspacing="0" cellpadding="0" align="center">        
                            <tr class="row-label">
                                <td width="100%" align="left" colspan="2">
                                <nested:message key="label.listmessages.piresponse"/>
                                </td>       
                            </tr>
    
                             <!-----PI Response Message Block Start ----------->

                             <nested:notEmpty property="piMessage.msgfields">
                             <nested:iterate property="piMessage.msgfields" indexId="index">
                             <%  if (index.intValue() % 2 == 0) {%>
                                 
                                    <tr class="row-value tr1-bc-report">
                             <% } else { %>
                                 
                                    <tr class="row-value tr2-bc-report">
                             <% } %>

                             <!--------- PI Response Field Type Start---------->
                            
                                <td class="column-label" width="50%">
                                <nested:write property  ="fieldName"/>  
                                    <% if (displayFieldNo) { %>
                                        
                                        (<nested:write property ="fieldNo"/>)
                                    <% } %>
                                </td>

                                <td class="column-label" width="50%">
                                    <nested:empty property = "displayValue">
                                        <nested:write property = "value"/>
                                    </nested:empty>

                                    <nested:notEmpty property = "displayValue">
                                        <nested:define id = "dispValue" property = "displayValue" type = "java.lang.String"/>
                                        <%= convert(dispValue) %>
                                    </nested:notEmpty>  
                                </td>
                             </tr>
                             </nested:iterate>
                             </nested:notEmpty>
                           </table>
                        </td>
                    </tr>  
                    </nested:present>

                    <!------ SSN Response Message Field Type Start ------------>
                    <nested:present property="ssnMessage">
                          
                    <tr>
                        <td align="center" colspan="2">
                            <table border="0" width="98%" cellspacing="0" cellpadding="0" align="center"> 
                            <tr class="row-label">
                                <td width="100%" align="left" colspan="2">
                                <nested:message key="label.listmessages.ssnresponse"/>
                                </td>
                            </tr>   

                        <!-----SSN Response Message Block Start --------------->

                            <nested:notEmpty property="ssnMessage.msgfields">
                            <nested:iterate property="ssnMessage.msgfields" indexId="index">
                            <%  if (index.intValue() % 2 == 0) {%>
                            <tr class="row-value tr1-bc-report">
                            <% } else { 
                            %>
                            <tr class="row-value tr2-bc-report">
                            <% } %>

                        <!---- SSN Response Field Type Start------------------->
                            
                                <td class="column-label" width="50%">
                                <nested:write property  ="fieldName"/>  
                                <% if (displayFieldNo) { %>
                                    (<nested:write property ="fieldNo"/>)
                                <% } %>
                                </td>

                                <td class="column-label" width="50%">
                                    <nested:empty property = "displayValue">
                                        <nested:write property = "value"/>
                                    </nested:empty>

                                    <nested:notEmpty property = "displayValue">
                                        <nested:define id = "dispValue" property = "displayValue" type = "java.lang.String"/>                                    
                                        <%= convert(dispValue) %>
                                    </nested:notEmpty>  
                                </td>
                              </tr>
                              </nested:iterate>
                              </nested:notEmpty>
                              </table>
                          </td>
                    </tr>    
                    </nested:present>
                    <tr>
                        <td colspan="2">
                        <hr align="center"></hr>
                        </td>
                    </tr>
                 </table>
                </td>
              </tr>
</nested:iterate>                
</nested:iterate>     
          </table>
      </td>
    </tr>  
</nested:iterate>
    </table> 
  </td>
 </tr>
 </nested:iterate>
</nested:notEmpty>

<nested:empty property="detailedReportDTO.detailedMessages">
  <tr>
    <td class="row-value" height="5" colspan="4" align="center">
        <nested:message key="label.report.noReport"/>
    </td>
  </tr>
  </nested:empty>
</table>        

<script language="javascript" src="<%=scriptBase%>/selected_print.js"></script>
  <script>
  <!--
      function customscript() {
          var title = new String("<%=title %>");
          var imagePath = new String("<%=imagePath %>");
          void(printSpecial(title,imagePath));//for ink jet or laser print
      }

//  -->

  </script>
  </div>
  </nested:form>
