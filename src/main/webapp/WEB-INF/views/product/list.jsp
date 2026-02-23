<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Store Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div class="container">
    <!-- Header -->
    <header class="header">
        <div class="brand">
            <div class="brand-mark">SA</div>
            <div class="brand-text">
                <h1>Store Admin</h1>
                <span class="brand-subtitle">Product Management</span>
            </div>
        </div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">Dashboard</a>
            <a href="${pageContext.request.contextPath}/store/category" class="nav-link">Category</a>
            <a href="${pageContext.request.contextPath}/store/product" class="nav-link active">Product</a>
            <a href="${pageContext.request.contextPath}/" class="nav-link">Users</a>

        </nav>
    </header>

    <!-- Main Content -->
    <main class="main">
        <div class="page-header">
            <div class="page-title">
                <h2>${pageTitle}</h2>
                <p class="page-hint">Manage pricing, stock, and category assignments.</p>
            </div>
            <button type="button" id="btnAdd" class="btn btn-primary">+ Add Product</button>
        </div>

        <!-- Search Filter -->
        <div class="search-box">
            <div class="search-row">
                <div class="search-item">
                    <label for="searchKeyword">Product Name</label>
                    <input type="text" id="searchKeyword" class="form-control" placeholder="Search product">
                </div>
                <div class="search-item">
                    <label for="searchCategory">Category</label>
                    <select id="searchCategory" class="form-control">
                        <option value="">All</option>
                        <jsp:useBean id="categories" scope="request" type="java.util.List"/>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="search-item">
                    <label for="searchMinPrice">Min Price</label>
                    <input type="number" id="searchMinPrice" class="form-control" placeholder="0">
                </div>
                <div class="search-item">
                    <label for="searchMaxPrice">Max Price</label>
                    <input type="number" id="searchMaxPrice" class="form-control" placeholder="999999">
                </div>
                <div class="search-item">
                    <label for="searchSortBy">Sort By</label>
                    <select id="searchSortBy" class="form-control">
                        <option value="">Newest</option>
                        <option value="name">Name</option>
                        <option value="price">Price</option>
                        <option value="stock">Stock</option>
                        <option value="createdAt">Created At</option>
                    </select>
                </div>
                <div class="search-item">
                    <label for="searchSortOrder">Order</label>
                    <select id="searchSortOrder" class="form-control">
                        <option value="DESC">DESC</option>
                        <option value="ASC">ASC</option>
                    </select>
                </div>
                <div class="search-item search-actions">
                    <button type="button" id="btnSearch" class="btn btn-primary">Search</button>
                    <button type="button" id="btnReset" class="btn btn-secondary">Reset</button>
                </div>
            </div>
        </div>

        <!-- Product List Table -->
        <div class="table-container">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="productTableBody">
                <!-- Data loaded via AJAX -->
                <tr>
                    <td colspan="7" class="text-center">Loading data...</td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        <div class="pagination" id="pagination">
        </div>
    </main>
</div>

<!-- Product Create/Edit Modal -->
<div id="productModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 id="modalTitle">Add Product</h3>
            <button type="button" class="close-btn" onclick="closeModal()">&times;</button>
        </div>
        <form id="productForm">
            <input type="hidden" id="productId">
            <div class="form-group">
                <label for="productName">Product Name <span class="required">*</span></label>
                <input type="text" id="productName" name="name" class="form-control"
                       placeholder="Enter product name" required>
            </div>
            <div class="form-group">
                <label for="productCategory">Category <span class="required">*</span></label>
                <select id="productCategory" name="categoryId" class="form-control" required>
                    <option value="">Select</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}">${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="productPrice">Price <span class="required">*</span></label>
                <input type="number" id="productPrice" name="price" class="form-control"
                       placeholder="Enter price" min="0" required>
            </div>
            <div class="form-group">
                <label for="productStock">Stock <span class="required">*</span></label>
                <input type="number" id="productStock" name="stock" class="form-control"
                       placeholder="Enter stock" min="0" required>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/product.js"></script>
</body>
</html>
