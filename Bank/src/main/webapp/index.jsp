<%@ page import="com.clouway.bank.LoggedUsersCounter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" type="text/css" href="css/index.css" >

    <script type="text/javascript">
        function checkToRemoveLinks(){
            if(document.getElementById("greet").innerHTML != ""){
                document.getElementById("loginLink").style.display="none";
                document.getElementById("regLink").style.display="none";
                document.getElementById("menuList").style.width="30%";
                document.getElementById("menuList").style.marginLeft="35%";
                document.getElementsByTagName("h3")[0].style.marginTop="-4%";
            }
        }
    </script>

</head>
<body onload="checkToRemoveLinks()">

<h1>Home Page</h1>

<div id="menuList">
    <a id="regLink" href="registration.jsp">registration</a>
    <a href="BalanceServlet">balance</a>
    <a href="DepositServlet">deposit</a>
    <a href="WithdrawServlet">withdraw</a>
    <a id="loginLink" href="login.jsp">login</a>
</div>

<%
    Cookie[] cookies = request.getCookies();
    String cookieValue = "";

    if(cookies!=null){
        Cookie greetCookie=null;
        for (int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals("loginGreet")){
                greetCookie = cookies[i];
                break;
            }
        }
        cookieValue = greetCookie.getValue();
        cookieValue+="<br/><a style='color:white;' href='logout.jsp'>logout</a>";
    }
%>
<div id="greet"><%=cookieValue%></div><br/>


<h3>Logged Users: <%=LoggedUsersCounter.getCount()%></h3>

</body>
</html>