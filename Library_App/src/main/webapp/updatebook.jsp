<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.AdminBookDAO,model.AdminBook" %>

<%
    AdminBookDAO bookDAO = new AdminBookDAO();
    List<AdminBook> books = bookDAO.getAllBooks();
%>

<html>
<head>
    <title>Manage Books</title>
   <style>
    @import url('https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&family=Geo:ital@0;1&family=Pragati+Narrow:wght@400;700&family=Ubuntu:ital,wght@0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&display=swap');

    body {
        font-family: 'DM Sans', sans-serif;
        background: #f4f4f4;
        margin: 0;
        padding: 20px;
    }

    h1 {
        text-align: center;
        font-family: 'Pragati Narrow', sans-serif;
        color: #273c75;
    }

    .book-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 20px;
        margin-top: 20px;
    }

    .card {
        background: white;
        width: 220px;
        padding: 15px;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        text-align: center;
        font-family: 'DM Sans', sans-serif;
        transition: transform 0.3s ease;
        overflow: hidden;
    }

    .card:hover {
        transform: scale(0.96);
    }

    .card img {
        width: 100%;
        height: 180px;
        object-fit: cover;
        border-radius: 8px;
        transition: transform 0.3s ease-in-out;
    }

    .card:hover img {
        transform: scale(1.1);
    }

    .card .details {
        font-size: 14px;
        margin: 10px 0;
        color: #2f3640;
    }

    .card button,
    .card input[type="submit"] {
        margin: 5px;
        padding: 10px;
        border: none;
        border-radius: 5px;
        background-color: #007bff;
        color: white;
        cursor: pointer;
        font-size: 14px;
        font-family: 'Ubuntu', sans-serif;
        transition: background-color 0.3s ease;
    }

    .card button:hover,
    .card input[type="submit"]:hover {
        background-color: #0056b3;
    }
</style>

</head>
<body>

<h1>Manage Books</h1>

<div class="book-container">
    <% for (AdminBook book : books) { %>
        <div class="card">
            <img src="adminActionServlet?imageId=<%= book.getId() %>" alt="Book Image">
            <div class="details">
                <strong><%= book.getTitle() %></strong><br>
                <i>by <%= book.getAuthor() %></i><br>
                ISBN: <%= book.getIsbn() %>
            </div>

            <!-- Redirect to editbook.jsp with bookId -->
            <form method="get" action="editbook.jsp" style="display:inline;">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <button type="submit">Update</button>
            </form>

            <!-- Delete action -->
            <form action="adminActionServlet" method="post" style="display:inline;">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <input type="submit" name="action" value="delete" onclick="return confirm('Are you sure you want to delete this book?');">
            </form>
        </div>
    <% } %>
</div>

</body>
</html>
