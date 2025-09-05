<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register | Library System</title>
  <link rel="stylesheet" href="index.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
  <div class="wrapper">
    <form action="<%=request.getContextPath()%>/RegisterServlet" method="post">
      <h2>Register</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
      <% } %>

      <div class="input-field">
        <input type="text" name="username" required>
        <label>Enter your Username</label>
      </div>

      <div class="input-field">
        <input type="email" name="email" required>
        <label>Enter your email</label>
      </div>

      <div class="input-field">
        <input type="password" name="password" required>
        <label>Create password</label>
      </div>

      <div class="select-field">
        <select name="role" required>
          <option value="" disabled selected hidden></option>
          <option value="user">User</option>
          <option value="librarian">Librarian</option>
        </select>
        <label>Select Role</label>
      </div>

      <button type="submit">Sign Up</button>

      <div class="register">
        <p>Already have an account? <a href="login.jsp">Login</a></p>
      </div>
    </form>
  </div>
  <script src="index.js"></script>
</body>
</html>