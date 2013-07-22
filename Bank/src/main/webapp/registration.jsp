
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
  <head>
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css" >
     <%--<script type="text/javascript">--%>
           <%--function validateRegistration(){--%>
             <%--var fields = document.forms[0].getElementsByTagName("input");--%>
             <%--var labels = document.getElementsByTagName("label");--%>
               <%--for(var i=0;i<fields.length;i++){--%>
                   <%--if(fields[i].value==""){--%>
                      <%--labels[i].style.visibility="visible";--%>
                   <%--}--%>
                   <%--else{--%>
                       <%--labels[i].style.visibility="hidden";--%>
                   <%--}--%>
               <%--}--%>

               <%--if(fields[0].value!="" && fields[1].value!=""){--%>
                   <%--document.forms[0].submit();--%>
               <%--}--%>
           <%--}--%>
     <%--</script>--%>

  </head>
  <body>

      <h1>Registration</h1>
      <form method="POST" action="RegisterServlet" id="frm">
          Username: <input type="text" name="usernameBox">
          Password: <input type="password" name="passwordBox">
          <input type="submit" value="Register">
          <br/><label style="color:red;">${labelMessage}</label>
      </form>
      <%--<button id="btn" onclick="validateRegistration()">Register</button>--%>

  </body>
</html>