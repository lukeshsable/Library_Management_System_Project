<%@ page import="java.util.*, java.sql.*" %>
<%@ page import="dao.AdminBookDAO, dao.UserDAO" %>
<%@ page import="model.AdminBook,model.User" %>
<%@ page import="java.util.List,dao.BorrowedBookDAO,model.BorrowedBook" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    AdminBookDAO bookDAO = new AdminBookDAO();
    UserDAO userDAO = new UserDAO();

    int totalBooks = 0, borrowedBooks = 0, overdueBooks = 0;
    List<AdminBook> bookList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    BorrowedBookDAO borrowedBooksDAO = new BorrowedBookDAO();
    List<BorrowedBook> pendingRequests = new ArrayList<>();

    try {
        totalBooks = bookDAO.getTotalBooks();
        borrowedBooks = bookDAO.getBorrowedCount();
        overdueBooks = bookDAO.getOverdueCount();
        bookList = bookDAO.getAllBooks();
        userList = userDAO.getAllUsers();
        pendingRequests = borrowedBooksDAO.getPendingRequests();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<html>
<head>
    <title>Library Admin Dashboard</title>

    <!-- Bootstrap CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans&family=Pragati+Narrow:wght@700&display=swap" rel="stylesheet">

    <style>
        body {
            margin: 0;
            font-family: 'DM Sans', sans-serif;
            background-color: #f5f6fa;
            color: #2f3640;
        }

        h1, h2, h4 {
            font-family: 'Pragati Narrow', sans-serif;
            font-weight: 700;
            color: #273c75;
        }

        /* Custom logout button hover effect */
        .btn-logout:hover {
            background-color: white !important;
            color: #dc3545 !important;
            border: 1px solid #dc3545;
        }

        .dashboard {
            display: flex;
            justify-content: center;
            gap: 15px;
            flex-wrap: wrap;
            margin: 20px auto;
            padding: 0 10px;
        }

.card {
    background: #fff;
    padding: 16px;
    border-radius: 10px;
    width: 220px;
    text-align: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    transition: 0.2s ease;
    font-family: 'DM Sans', sans-serif;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.card h3 {
    margin-bottom: 10px;
    font-size: 18px;
    font-weight: bold;
}

.card p {
    font-size: 24px;
    font-weight: bold;
    margin: 0;
    color: #0984e3; 
}

        .card:hover {
            transform: scale(1.02);
        }

        .book-cards {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 20px;
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }

        .book-card {
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
            text-align: center;
            transition: transform 0.3s ease-in-out;
        }

        .book-card:hover {
            transform: scale(0.96);
        }

        .book-card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
            transition: transform 0.3s ease-in-out;
        }

        .book-card:hover img {
            transform: scale(1.1);
        }

        .book-card h4 {
            margin: 10px 0 5px;
            font-size: 16px;
        }

        .book-card p {
            margin: 0 0 10px;
            font-size: 13px;
            color: #555;
        }

        .action-buttons {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
            margin: 30px auto 10px;
            padding: 0 10px;
        }

        .action-buttons button {
            background-color: #0984e3;
            color: white;
            border: none;
            border-radius: 6px;
            padding: 10px 16px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .action-buttons button:hover {
            background-color: #0652dd;
        }

        .response-section {
            display: none;
            background: #fff;
            margin: 20px auto;
            padding: 20px;
            border-radius: 10px;
            max-width: 1100px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .response-section h2 {
            margin-bottom: 20px;
            font-size: 24px;
            color: #2c3e50;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            font-size: 14px;
        }

        th, td {
            border: 1px solid #e0e0e0;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f1f2f6;
            color: #333;
        }

        form input, form select, form button {
            width: 100%;
            padding: 8px;
            margin: 6px 0;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        form button {
            background-color: #44bd32;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        form button:hover {
            background-color: #2ecc71;
        }

        .approve-reject-btns form {
            display: inline;
        }

        .approve-reject-btns button {
            padding: 6px 10px;
            font-size: 13px;
            margin: 0 2px;
            border-radius: 4px;
        }

        .approve-reject-btns button[name="action"][value="approve"] {
            background-color: #00b894;
            color: white;
        }

        .approve-reject-btns button[name="action"][value="reject"] {
            background-color: #d63031;
            color: white;
        }

        .approve-reject-btns button:hover {
            opacity: 0.85;
        }
    </style>
</head>
<body>

<!-- Bootstrap Navbar Header -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
    <a class="navbar-brand fw-bold fs-4" style="color: #00cec9;" href="#">📚 Library Admin</a>
    <div class="ms-auto">
        <a href="logout.jsp" class="btn btn-danger btn-logout">Logout</a>
    </div>
</nav>

<div class="dashboard">
    <div class="card">
        <h3>Total Books</h3>
        <p><strong><%= totalBooks %></strong></p>
    </div>
    <div class="card">
        <h3>Borrowed Books</h3>
        <p><strong><%= borrowedBooks %></strong></p>
    </div>
    <div class="card">
        <h3>Overdue Books</h3>
        <p><strong><%= overdueBooks %></strong></p>
    </div>
</div>

<div class="book-cards">
    <% for (AdminBook book : bookList) { %>
    <div class="book-card">
        <img src="adminActionServlet?imageId=<%= book.getId() %>" alt="Book Image">
        <h4><%= book.getTitle() %></h4>
        <p><%= book.getAuthor() %></p>
        <p><strong>Quantity:</strong> <%= book.getQuantity() %></p>
    </div>
    <% } %>
</div>

<div class="action-buttons">
    <button onclick="showSection('manageBookInventory')">Manage Book Inventory</button>
    <button onclick="showSection('viewUsers')">View Registered Users</button>
    <button onclick="showSection('approveRequests')">Approve/Reject Requests</button>
    <button onclick="showSection('viewTransactions')">View Transaction History</button>
</div>

<div id="manageBookInventory" class="response-section">
    <h2>Manage Book Inventory</h2>
    <form action="adminActionServlet" method="POST" enctype="multipart/form-data">
        <input type="text" name="bookTitle" placeholder="Book Title" required>
        <input type="text" name="author" placeholder="Author" required>
        <input type="text" name="isbn" placeholder="ISBN" required>
        <input type="number" name="quantity" placeholder="Quantity" required>
        <input type="file" name="bookImage" accept=".jpg" required>
        <button type="submit" name="action" value="add">Add Book</button>
        <button type="button" onclick="goToUpdatePage()">Update Book</button>
        <button type="button" onclick="goToDeletePage()">Delete Book</button>
    </form>
</div>

<div id="viewUsers" class="response-section">
    <h2>Registered Users</h2>
    <table>
        <tr><th>User ID</th><th>Username</th><th>Email</th></tr>
        <% for (User u : userList) { %>
        <tr>
            <td><%= u.getUserId() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= u.getEmail() %></td>
        </tr>
        <% } %>
    </table>
</div>

<div id="approveRequests" class="response-section">
    <h2>Approve/Reject Borrowing Requests</h2>
    <table>
        <tr><th>Transaction ID</th><th>User ID</th><th>Book ID</th><th>Status</th><th>Actions</th></tr>
        <% if (pendingRequests != null && !pendingRequests.isEmpty()) {
               for (BorrowedBook req : pendingRequests) { %>
        <tr>
            <td><%= req.getTransactionId() %></td>
            <td><%= req.getUserId() %></td>
            <td><%= req.getBookId() %></td>
            <td><%= req.getStatus() %></td>
            <td class="approve-reject-btns">
                <form action="adminActionServlet" method="POST">
                    <input type="hidden" name="transactionId" value="<%= req.getTransactionId() %>">
                    <button type="submit" name="action" value="approve">Approve</button>
                </form>
                <form action="adminActionServlet" method="POST">
                    <input type="hidden" name="transactionId" value="<%= req.getTransactionId() %>">
                    <button type="submit" name="action" value="reject">Reject</button>
                </form>
            </td>
        </tr>
        <% } } else { %>
        <tr><td colspan="5" class="text-center">No pending requests found.</td></tr>
        <% } %>
    </table>
</div>

<div id="viewTransactions" class="response-section">
    <h2>Transaction History</h2>
    <table>
        <tr><th>Transaction ID</th><th>User ID</th><th>Book Title</th><th>Status</th></tr>
        <!-- Transaction rows go here -->
    </table>
</div>

<script>
    function showSection(sectionId) {
        document.querySelectorAll('.response-section').forEach(sec => sec.style.display = 'none');
        const section = document.getElementById(sectionId);
        section.style.display = 'block';
        section.scrollIntoView({ behavior: 'smooth' });
    }

    function goToUpdatePage() {
        window.location.href = 'updatebook.jsp';
    }

    function goToDeletePage() {
        window.location.href = 'deletebook.jsp';
    }
</script>

</body>
</html>
