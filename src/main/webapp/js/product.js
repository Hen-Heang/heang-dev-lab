/**
 * Product Management JavaScript
 * Product CRUD AJAX handling (상품 CRUD AJAX 처리)
 */

// API base URL (API 기본 URL)
const API_BASE = '/api/products';
const CATEGORY_API = '/api/categories';
const PAGE_SIZE = 10;
let currentPage = 1;

// ===================================
// Initialization (초기화)
// ===================================
$(document).ready(function () {
    // Load product list (상품 목록 로드)
    loadProducts();

    // Bind events (이벤트 바인딩)
    bindEvents();
});

// ===================================
// Event Binding (이벤트 바인딩)
// ===================================
function bindEvents() {
    // Add button click (추가 버튼 클릭)
    $('#btnAdd').on('click', function () {
        openAddModal();
    });

    // Form submit (폼 제출)
    $('#productForm').on('submit', function (e) {
        e.preventDefault();
        saveProduct();
    });

    // Search button (검색 버튼)
    $('#btnSearch').on('click', function () {
        loadProducts(1);
    });

    $('#searchKeyword, #searchCategory, #searchMinPrice, #searchMaxPrice, #searchSortBy, #searchSortOrder').on('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            loadProducts(1);
        }
    });

    $('#searchSortBy, #searchSortOrder').on('change', function () {
        loadProducts(1);
    });

    // Reset button (초기화 버튼)
    $('#btnReset').on('click', function () {
        $('#searchKeyword').val('');
        $('#searchCategory').val('');
        $('#searchMinPrice').val('');
        $('#searchMaxPrice').val('');
        $('#searchSortBy').val('');
        $('#searchSortOrder').val('DESC');
        loadProducts(1);
    });

    // Close modal on outside click (모달 외부 클릭 시 닫기)
    $('#productModal').on('click', function (e) {
        if (e.target === this) {
            closeModal();
        }
    });

    // Close modal with ESC key (ESC 키로 모달 닫기)
    $(document).on('keydown', function (e) {
        if (e.key === 'Escape') {
            closeModal();
        }
    });
}

// ===================================
// Load Products (SELECT) - 상품 목록 조회
// ===================================
function loadProducts(page) {
    currentPage = page || currentPage || 1;
    const keyword = $('#searchKeyword').val().trim();
    const categoryId = $('#searchCategory').val();
    const minPrice = $('#searchMinPrice').val();
    const maxPrice = $('#searchMaxPrice').val();
    const sortBy = $('#searchSortBy').val();
    const sortOrder = $('#searchSortOrder').val();

    const params = {};
    params.page = currentPage;
    params.size = PAGE_SIZE;
    if (keyword) params.keyword = keyword;
    if (categoryId) params.categoryId = categoryId;
    if (minPrice !== '') params.minPrice = minPrice;
    if (maxPrice !== '') params.maxPrice = maxPrice;
    if (sortBy) params.sortBy = sortBy;
    if (sortOrder) params.sortOrder = sortOrder;

    $.ajax({
        url: `${API_BASE}/paged`,
        method: 'GET',
        data: params,
        success: function (response) {
            console.log('Product API Response:', response);
            if (response.status && response.status.code === 200) {
                const data = response.data || {};
                const products = data.products || [];
                renderProductTable(products);
                renderPagination(data.page || currentPage, data.totalPages || 0, data.total || 0);
            } else if (response.data) {
                const data = response.data || {};
                const products = data.products || [];
                renderProductTable(products);
                renderPagination(data.page || currentPage, data.totalPages || 0, data.total || 0);
            } else {
                showError(response.status ? response.status.message : 'Unknown error');
            }
        },
        error: function (xhr) {
            console.error('Product API Error:', xhr);
            showError('Failed to load product list (상품 목록 로드 실패)');
        }
    });
}

function renderPagination(page, totalPages, total) {
    const pagination = $('#pagination');
    pagination.empty();

    if (!totalPages || totalPages < 1) {
        totalPages = 1;
    }
    if (!page || page < 1) {
        page = 1;
    }
    if (page > totalPages) {
        page = totalPages;
    }

    const prevDisabled = page <= 1 ? 'disabled' : '';
    pagination.append(`<button type="button" class="btn btn-sm btn-secondary ${prevDisabled}" data-page="${page - 1}">Prev</button>`);

    const start = Math.max(1, page - 2);
    const end = Math.min(totalPages, page + 2);

    for (let i = start; i <= end; i++) {
        const cls = i === page ? 'btn btn-sm btn-primary' : 'btn btn-sm btn-secondary';
        pagination.append(`<button type="button" class="${cls}" data-page="${i}">${i}</button>`);
    }

    const nextDisabled = page >= totalPages ? 'disabled' : '';
    pagination.append(`<button type="button" class="btn btn-sm btn-secondary ${nextDisabled}" data-page="${page + 1}">Next</button>`);

    pagination.find('button').not('.disabled').on('click', function () {
        const targetPage = parseInt($(this).data('page'), 10);
        if (Number.isInteger(targetPage) && targetPage >= 1 && targetPage <= totalPages) {
            loadProducts(targetPage);
        }
    });
}

// ===================================
// Render Table (테이블 렌더링)
// ===================================
function renderProductTable(products) {
    const tbody = $('#productTableBody');
    tbody.empty();

    if (!products || products.length === 0) {
        tbody.append(`
            <tr>
                <td colspan="7" class="text-center empty-state">
                    No products found (등록된 상품 없음)
                </td>
            </tr>
        `);
        return;
    }

    products.forEach(function (product) {
        // Format date (날짜 포맷팅)
        const createdAt = formatDateTime(product.createdAt);
        // Format price (가격 포맷팅)
        const priceFormatted = formatNumber(product.price);

        tbody.append(`
            <tr>
                <td>${product.id}</td>
                <td>${escapeHtml(product.name)}</td>
                <td>${escapeHtml(product.categoryName || '-')}</td>
                <td>${priceFormatted}</td>
                <td>${product.stock}</td>
                <td>${createdAt}</td>
                <td>
                    <button type="button" class="btn btn-sm btn-secondary"
                            onclick="openEditModal(${product.id})">
                        Edit
                    </button>
                    <button type="button" class="btn btn-sm btn-danger"
                            onclick="deleteProduct(${product.id}, '${escapeHtml(product.name)}')">
                        Delete
                    </button>
                </td>
            </tr>
        `);
    });
}

// ===================================
// Save Product (INSERT / UPDATE) - 상품 저장
// ===================================
function saveProduct() {
    const id = $('#productId').val();
    const name = $('#productName').val().trim();
    const categoryId = $('#productCategory').val();
    const price = $('#productPrice').val();
    const stock = $('#productStock').val();

    if (!name) {
        alert('Please enter product name (상품명을 입력해주세요)');
        $('#productName').focus();
        return;
    }

    if (!categoryId) {
        alert('Please select category (카테고리를 선택해주세요)');
        $('#productCategory').focus();
        return;
    }

    const data = {
        name: name,
        categoryId: parseInt(categoryId),
        price: parseInt(price) || 0,
        stock: parseInt(stock) || 0
    };

    // If ID exists = update, else = create (ID 있으면 수정, 없으면 등록)
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/${id}` : API_BASE;

    $.ajax({
        url: url,
        method: method,
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            if (response.status && response.status.code === 200) {
                alert('Saved successfully (저장 완료)');
                closeModal();
                loadProducts();
            } else {
                alert(response.status ? response.status.message : 'Save failed');
            }
        },
        error: function (xhr) {
            const response = xhr.responseJSON;
            alert(response?.status?.message || 'Save failed (저장 실패)');
        }
    });
}

// ===================================
// Delete Product (DELETE) - 상품 삭제
// ===================================
function deleteProduct(id, name) {
    if (!confirm(`Delete "${name}" product?\n(상품을 삭제하시겠습니까?)`)) {
        return;
    }

    $.ajax({
        url: `${API_BASE}/${id}`,
        method: 'DELETE',
        success: function (response) {
            if (response.status && response.status.code === 200) {
                alert('Deleted successfully (삭제 완료)');
                loadProducts();
            } else {
                alert(response.status ? response.status.message : 'Delete failed');
            }
        },
        error: function (xhr) {
            const response = xhr.responseJSON;
            alert(response?.status?.message || 'Delete failed (삭제 실패)');
        }
    });
}

// ===================================
// Modal Open/Close (모달 열기/닫기)
// ===================================
function openAddModal() {
    $('#modalTitle').text('Add Product');
    $('#productId').val('');
    $('#productName').val('');
    $('#productCategory').val('');
    $('#productPrice').val('');
    $('#productStock').val('');
    $('#productModal').addClass('show');
    $('#productName').focus();
}

function openEditModal(id) {
    // Get product data (상품 데이터 조회)
    $.ajax({
        url: `${API_BASE}/${id}`,
        method: 'GET',
        success: function (response) {
            if (response.status && response.status.code === 200) {
                const product = response.data;
                $('#modalTitle').text('Edit Product');
                $('#productId').val(product.id);
                $('#productName').val(product.name);
                $('#productCategory').val(product.categoryId);
                $('#productPrice').val(product.price);
                $('#productStock').val(product.stock);
                $('#productModal').addClass('show');
                $('#productName').focus();
            } else {
                alert('Failed to load product data (상품 데이터 로드 실패)');
            }
        },
        error: function (xhr) {
            alert('Failed to load product data (상품 데이터 로드 실패)');
        }
    });
}

function closeModal() {
    $('#productModal').removeClass('show');
    $('#productForm')[0].reset();
}

// ===================================
// Utility Functions (유틸리티 함수)
// ===================================
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '-';

    const date = new Date(dateTimeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function formatNumber(num) {
    if (num === null || num === undefined) return '0';
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function showError(message) {
    alert(message);
}
