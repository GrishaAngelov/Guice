
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

  <head>
    <title>Balance</title>
    <link rel="stylesheet" type="text/css" href="css/status.css" >

  </head>

  <body>
      <h1>Current Financial Status</h1>
      <h2><%=request.getAttribute("balance")%></h2>
      <a href="index.jsp">Go back</a>
  </body>
</html>