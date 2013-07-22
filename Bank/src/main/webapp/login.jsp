
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page isELIgnored="false"%>
<html>
  <head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css" >

  </head>
  <body>

      <h1>Log In</h1>
      <form method="post" action="LoginServlet" id="frm">
          Username: <input type="text" name="usernameBox">
          Password: <input type="password" name="passwordBox">
          <input type="submit" value="Login">
          <br/><label style="color: red;">${labelMessage}</label>
      </form>

  </body>
</html>