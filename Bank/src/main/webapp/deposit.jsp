
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Deposit</title>
    <link rel="stylesheet" type="text/css" href="css/deposit.css" >
    <%--<script type="text/javascript">--%>
        <%--function validateDeposit(){--%>
            <%--var depositValue = document.forms[0]["deposit"].value;--%>
            <%--if(depositValue!=""){--%>
                <%--var isValid = /^[0-9]+$|^[0-9]+[.][0-9]{2}$/i.test(depositValue);--%>
                <%--if(isValid){--%>
                    <%--document.forms[0].submit();--%>
                <%--}--%>
            <%--}--%>
        <%--}--%>
    <%--</script>--%>

      <script type="text/javascript">
          var maxAmount = "99999999";
          function validateAmount(){
            if(document.forms[0]["depositAmount"].value.length > maxAmount.length){
                alert("Your amount is too big!")
            }
              else{
                document.forms[0].submit();
            }
          }
      </script>

  </head>
  <body>
      <h1>Deposit</h1>
      <form method="post" action="DepositOperationServlet">
          <input type="text" name="deposit" id="depositAmount">
          <%--<input type="submit" value="Deposit">--%>
          <button type="button" onclick="validateAmount()">Deposit</button>
      </form>
      <a href="index.jsp">Go back</a>
      <%--<h2>${status}</h2>--%>
      <h2><%=session.getAttribute("operationStatus")%></h2>
      <%
          session.setAttribute("operationStatus","");
      %>
  </body>
</html>