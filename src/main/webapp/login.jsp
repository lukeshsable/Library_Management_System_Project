<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login | Library System</title>
  <link rel="stylesheet" href="index.css"> <!-- Reuse same CSS as register -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
  <div class="wrapper">
    <form action="LoginServlet" method="post">
      <h2>Login</h2>

      <div class="input-field">
        <input type="text" name="username" required>
        <label>Enter your username</label>
      </div>

      <div class="input-field">
        <input type="password" name="password" required>
        <label>Enter your password</label>
      </div>

      <button type="submit">Sign In</button>

      <div class="register">
        <p>Don't have an account? <a href="index.jsp">Register</a></p>
      </div>
    </form>
  </div>
  <script src="index.js"></script>
</body>
</html>