
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
  <head>
    <title>Withdraw</title>
    <link rel="stylesheet" type="text/css" href="css/withdraw.css" >

  </head>
  <body>
      <h1>Withdraw</h1>
      <form method="post" action="WithdrawOperationServlet">
          <input type="text" name="withdrawAmount">
          <input type="submit" value="Withdraw">
      </form>
      <a href="index.jsp">Go back</a>

    <%
        String message = (String) request.getAttribute("operationStatus");
        if(message == null){
            message = "";
        }
    %>

    <h2><%=message%></h2>
  </body>
</html>