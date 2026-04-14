/**
 * Category Management JavaScript
 * Category CRUD AJAX handling (카테고리 CRUD AJAX 처리)
 */

// API base URL (API 기본 URL)
const API_BASE = '/api/categories';

// ===================================
// Initialization (초기화)
// ===================================
$(document).ready(function () {
    // Load category list (카테고리 목록 로드)
    loadCategories();

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
    $('#categoryForm').on('submit', function (e) {
        e.preventDefault();
        saveCategory();
    });

    // Close modal on outside click (모달 외부 클릭 시 닫기)
    $('#categoryModal').on('click', function (e) {
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
// Load Categories (SELECT) - 카테고리 목록 조회
// ===================================
function loadCategories() {
    $.ajax({
        url: API_BASE,
        method: 'GET',
        success: function (response) {
            console.log('Category API Response:', response);
            if (response.status && response.status.code === 200) {
                renderCategoryTable(response.data);
            } else if (response.data) {
                // Fallback: if status check fails but data exists
                renderCategoryTable(response.data);
            } else {
                showError(response.status ? response.status.message : 'Unknown error');
            }
        },
        error: function (xhr) {
            console.error('Category API Error:', xhr);
            showError('Failed to load category list (카테고리 목록 로드 실패)');
        }
    });
}

// ===================================
// Render Table (테이블 렌더링)
// ===================================
function renderCategoryTable(categories) {
    const tbody = $('#categoryTableBody');
    tbody.empty();

    if (!categories || categories.length === 0) {
        tbody.append(`
            <tr>
                <td colspan="4" class="text-center empty-state">
                    No categories found (등록된 카테고리 없음)
                </td>
            </tr>
        `);
        return;
    }

    categories.forEach(function (category) {
        // Format date (날짜 포맷팅)
        const createdAt = formatDateTime(category.createdAt);

        tbody.append(`
            <tr>
                <td>${category.id}</td>
                <td>${escapeHtml(category.name)}</td>
                <td>${createdAt}</td>
                <td>
                    <button type="button" class="btn btn-sm btn-secondary"
                            onclick="openEditModal(${category.id}, '${escapeHtml(category.name)}')">
                        Edit
                    </button>
                    <button type="button" class="btn btn-sm btn-danger"
                            onclick="deleteCategory(${category.id}, '${escapeHtml(category.name)}')">
                        Delete
                    </button>
                </td>
            </tr>
        `);
    });
}

// ===================================
// Save Category (INSERT / UPDATE) - 카테고리 저장
// ===================================
function saveCategory() {
    const id = $('#categoryId').val();
    const name = $('#categoryName').val().trim();

    if (!name) {
        alert('Please enter category name (카테고리명을 입력해주세요)');
        $('#categoryName').focus();
        return;
    }

    const data = {name: name};

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
                loadCategories();
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
// Delete Category (DELETE) - 카테고리 삭제
// ===================================
function deleteCategory(id, name) {
    if (!confirm(`Delete "${name}" category?\n\n* Cannot delete if products exist in this category.\n(해당 카테고리에 상품이 있으면 삭제 불가)`)) {
        return;
    }

    $.ajax({
        url: `${API_BASE}/${id}`,
        method: 'DELETE',
        success: function (response) {
            if (response.status && response.status.code === 200) {
                alert('Deleted successfully (삭제 완료)');
                loadCategories();
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
    $('#modalTitle').text('Add Category');
    $('#categoryId').val('');
    $('#categoryName').val('');
    $('#categoryModal').addClass('show');
    $('#categoryName').focus();
}

function openEditModal(id, name) {
    $('#modalTitle').text('Edit Category');
    $('#categoryId').val(id);
    $('#categoryName').val(name);
    $('#categoryModal').addClass('show');
    $('#categoryName').focus();
}

function closeModal() {
    $('#categoryModal').removeClass('show');
    $('#categoryForm')[0].reset();
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
