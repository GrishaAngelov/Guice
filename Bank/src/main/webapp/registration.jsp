
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
  <head>
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css" >

  </head>
  <body>

      <h1>Registration</h1>
      <form method="POST" action="RegisterServlet" id="frm">
          Username: <input type="text" name="usernameBox">
          Password: <input type="password" name="passwordBox">
          <input type="submit" value="Register">
          <br/><label style="color:red;">${labelMessage}</label>
      </form>

  </body>
</html>