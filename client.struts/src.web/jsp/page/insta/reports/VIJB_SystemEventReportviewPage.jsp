<%@ taglib uri="/tlds/struts-html" prefix="html" %>
<%@ taglib uri="/tlds/struts-bean" prefix="bean" %>
<%@ taglib uri="/tlds/struts-nested" prefix="nested" %>
<%@ taglib uri="/tlds/struts-tiles" prefix="tiles" %>
<%@ include file="../../../component/standard/LFStandard.jsp" %>
<%@ include file="../../../component/standard/AppStandard.jsp" %>
<%@ page import="java.util.Iterator" %>
<%@ page import ="com.objectfrontier.insta.reports.InstaReportUtil" %>
<nested:form action="/eventAlertReport">
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
                   <nested:define id ="format" property="dateFormat" type="java.lang.String"/>
         		<nested:define id = "valueDate" property="reportDto.valueDate" type="java.lang.String"/>
          		<nested:define id = "toDate" property="reportDto.toDate" type="java.lang.String"/>   
                  <b>
                  <font size="2.0">
                  System Level Events Report 
                  From <%=InstaReportUtil.getDateInSpecificFormat(format,valueDate)%> to <%=InstaReportUtil.getDateInSpecificFormat(format,toDate)%>
					</font>
                  </b>
              </td>
              </tr>
               <nested:notEmpty property="reportDTOs">
            <%
              			boolean k= false;
            %>
            <tr class="row-label th-bc">
            <td width="5%" align="center">S.No</td>
                <td width="15%" align="center" >DateTime</td>
                <td width="10%" align="center">User</td>
                <td width="25%" align="center" >Info</td>
            </tr> 
           
            <nested:iterate indexId = "index" id ="reportDto" property="reportDTOs"> 
              <% k = k ? false : true;
              		if (k) {%><tr class="row-value tr1-bc">
				<% 	} else { %><tr class="row-value tr2-bc">
		        <% 	} %>
                <% int sno  = index.intValue()+1; %>
                <td align="center" width="5%"><%=sno%></td>
                <td align="left">
                <nested:write property="valueDate"/>
	           	</td>   
                <td align="center" nowrap>&nbsp;
                <nested:write property = "userId"/>
                </td>
                <td align="left">
                <nested:write property = "auditInfo"/>
                </td> 
                </tr>
                </nested:iterate>
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

        var title = '<%=title%>';
		var imagePath = new String("<%=imagePath %>");
        void(printSpecial(title,imagePath));//for ink jet or laser print
    }
    
    function backToSystemEventInputPage() {
        document.forms[0].action='/insta/reports/eventAlertReport.do?mode=input';
        document.forms[0].submit();
    }
    
//-->

</script>
</div>
</nested:form>
          
          
          