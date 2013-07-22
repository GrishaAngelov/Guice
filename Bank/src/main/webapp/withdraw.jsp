
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Withdraw</title>
    <link rel="stylesheet" type="text/css" href="css/withdraw.css" >
      <%--<script type="text/javascript">--%>
          <%--function validateWithdraw(){--%>
              <%--var depositValue = document.forms[0]["withdraw"].value;--%>
              <%--var isValid = /^[0-9]+$|^[0-9]+[.][0-9]{2}$/i.test(depositValue);--%>
              <%--if(isValid){--%>
                  <%--document.forms[0].submit();--%>
              <%--}--%>
          <%--}--%>
      <%--</script>--%>

  </head>
  <body>
      <h1>Withdraw</h1>
      <form method="post" action="WithdrawOperationServlet">
          <input type="text" name="withdrawAmount">
          <input type="submit" value="Withdraw">
      </form>
      <a href="index.jsp">Go back</a>
      <%--<h2>${status}</h2>--%>
      <h2><%=session.getAttribute("operationStatus")%></h2>
      <%
          session.setAttribute("operationStatus","");
      %>
  </body>
</html>