<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.AdminBookDAO,model.AdminBook" %>

<%

    String bookIdParam = request.getParameter("bookId");
    System.out.println("Received bookId = " + bookIdParam); // debugging

    AdminBook selectedBook = null;

    if (bookIdParam != null && !bookIdParam.isEmpty()) {
        try {
            int bookId = Integer.parseInt(bookIdParam);
            AdminBookDAO bookDAO = new AdminBookDAO();
            selectedBook = bookDAO.getBookById(bookId);
            if (selectedBook == null) {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (Exception e) {
            System.out.println("Error parsing bookId or fetching book: " + e.getMessage());
        }
    } else {
        System.out.println("bookIdParam is null or empty.");
    }
%>
<html>
<head>
    <title>Edit Book</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f0f0f0;
            padding: 20px;
        }
        .form-container {
            max-width: 500px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
        }
        label {
            display: block;
            margin-top: 15px;
        }
        input[type="text"], input[type="file"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        input[type="submit"] {
            margin-top: 20px;
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 6px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .error {
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>

<% if (selectedBook != null) { %>
    <div class="form-container">
        <h2>Update Book</h2>
        <form action="adminActionServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="bookId" value="<%= selectedBook.getId() %>">

            <label for="bookTitle">Book Title</label>
            <input type="text" name="bookTitle" value="<%= selectedBook.getTitle() %>" required>

            <label for="author">Author</label>
            <input type="text" name="author" value="<%= selectedBook.getAuthor() %>" required>

            <label for="isbn">ISBN</label>
            <input type="text" name="isbn" value="<%= selectedBook.getIsbn() %>" required>
            
            <label for="quantity">Quantity</label>
			<input type="text" name="quantity" value="<%= selectedBook.getQuantity() %>" required>
			

            <label for="bookImage">Book Image</label>
            <input type="file" name="bookImage" accept=".jpg,.jpeg,.png">

            <input type="submit" name="action" value="update">
        </form>
    </div>
<% } else { %>
    <p class="error">Error: Book not found or invalid book ID.</p>
<% } %>

</body>
</html>
