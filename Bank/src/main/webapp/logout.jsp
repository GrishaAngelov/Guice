
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
  <head>
    <title>Logout</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css" >

  </head>
  <body onload="javascript:document.forms[0].submit();">

      <form method="post" action="LogoutServlet" id="frm">
          <input type="hidden" name="logout" value="out">
      </form>

  </body>
</html>