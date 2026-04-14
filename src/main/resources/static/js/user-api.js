/* ============================================
   USER API SERVICE (jQuery AJAX Version)
   Handles all API calls for User operations

   이 파일은 jQuery AJAX를 사용하여 백엔드 API와 통신합니다.
   (This file uses jQuery AJAX to communicate with backend API)

   HOW IT WORKS:
   1. JSP page calls these functions (e.g., UserAPI.getAll())
   2. jQuery $.ajax() sends HTTP request to Spring Backend
   3. Backend returns JSON response
   4. Success/Error callbacks handle the response

   REQUIRES: jQuery library must be loaded before this file
   ============================================ */

const UserAPI = {

    // Base URL for user endpoints (maps to UserController)
    baseUrl: (window.APP_CONTEXT || '') + '/users',

    /* ----------------------------------------
       GET ALL USERS (모든 사용자 조회)
       HTTP: GET /users
       Backend: UserController.getUserList()
       ---------------------------------------- */
    getAll: function (params, successCallback, errorCallback) {
        if (typeof params === 'function') {
            errorCallback = successCallback;
            successCallback = params;
            params = {};
        }
        $.ajax({
            url: this.baseUrl,          // Request URL
            type: 'GET',                // HTTP Method
            data: params,
            dataType: 'json',           // Expected response type
            success: function (result) {
                console.log('API Response:', result);
                if (successCallback) {
                    successCallback(result);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error fetching users:', error);
                if (errorCallback) {
                    errorCallback(xhr, status, error);
                }
            }
        });
    },

    /* ----------------------------------------
       GET SINGLE USER BY ID (단일 사용자 조회)
       HTTP: GET /users/{id}
       Backend: UserController.getUserById()
       ---------------------------------------- */
    getById: function (id, successCallback, errorCallback) {
        $.ajax({
            url: this.baseUrl + '/' + id,
            type: 'GET',
            dataType: 'json',
            success: function (result) {
                console.log('User Detail:', result);
                if (successCallback) {
                    successCallback(result);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error fetching user:', error);
                if (errorCallback) {
                    errorCallback(xhr, status, error);
                }
            }
        });
    },

    /* ----------------------------------------
       CREATE NEW USER (새 사용자 생성)
       HTTP: POST /users
       Backend: UserController.createUser()
       Body: { username, email, status }
       ---------------------------------------- */
    create: function (userData, successCallback, errorCallback) {
        $.ajax({
            url: this.baseUrl,
            type: 'POST',
            contentType: 'application/json',    // Tell the server we're sending JSON
            data: JSON.stringify(userData),      // Convert object to JSON string
            dataType: 'json',
            success: function (result) {
                console.log('Create Response:', result);
                if (successCallback) {
                    successCallback(result);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error creating user:', error);
                if (errorCallback) {
                    errorCallback(xhr, status, error);
                }
            }
        });
    },

    /* ----------------------------------------
       UPDATE EXISTING USER (사용자 정보 수정)
       HTTP: PUT /users/{id}
       Backend: UserController.updateUser()
       Body: { username, email, status }
       ---------------------------------------- */
    update: function (id, userData, successCallback, errorCallback) {
        $.ajax({
            url: this.baseUrl + '/' + id,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            dataType: 'json',
            success: function (result) {
                console.log('Update Response:', result);
                if (successCallback) {
                    successCallback(result);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error updating user:', error);
                if (errorCallback) {
                    errorCallback(xhr, status, error);
                }
            }
        });
    },

    /* ----------------------------------------
       DELETE USER (사용자 삭제)
       HTTP: DELETE /users/{id}
       Backend: UserController.deleteUser()
       ---------------------------------------- */
    delete: function (id, successCallback, errorCallback) {
        $.ajax({
            url: this.baseUrl + '/' + id,
            type: 'DELETE',
            dataType: 'json',
            success: function (result) {
                console.log('Delete Response:', result);
                if (successCallback) {
                    successCallback(result);
                }
            },
            error: function (xhr, status, error) {
                console.error('Error deleting user:', error);
                if (errorCallback) {
                    errorCallback(xhr, status, error);
                }
            }
        });
    }
};


/* ============================================
   UI HELPER FUNCTIONS (UI 헬퍼 함수)
   Common UI utility functions
   ============================================ */

const UIHelper = {

    // Show message banner (메시지 배너 표시)
    showMessage: function (elementId, text, isSuccess) {
        const $element = $('#' + elementId);
        if ($element.length) {
            $element.text(text)
                .removeClass('success error')
                .addClass('message ' + (isSuccess ? 'success' : 'error'))
                .show();

            // Auto hide after 5 seconds
            setTimeout(function () {
                $element.fadeOut();
            }, 5000);
        }
    },

    // Show modal (모달 표시)
    showModal: function (modalId) {
        $('#' + modalId).css('display', 'flex');
    },

    // Close modal (모달 닫기)
    closeModal: function (modalId) {
        $('#' + modalId).hide();
    },

    // Get status badge HTML (상태 배지 HTML 생성)
    getStatusBadge: function (status) {
        if (!status) return '<span class="badge">N/A</span>';
        let badgeClass = '';
        const statusLower = status.toLowerCase();
        if (statusLower === 'active') badgeClass = 'badge-active';
        else if (statusLower === 'inactive') badgeClass = 'badge-inactive';
        else if (statusLower === 'pending') badgeClass = 'badge-pending';
        return '<span class="badge ' + badgeClass + '">' + status + '</span>';
    },

    // Escape HTML to prevent XSS (XSS 방지를 위한 HTML 이스케이프)
    escapeHtml: function (text) {
        if (!text) return '';
        return $('<div>').text(text).html();
    }
};


/* ============================================
   MODAL HELPER (모달 헬퍼)
   Setup modal close on outside click
   ============================================ */

function setupModalCloseOnOutsideClick(modalId) {
    $('#' + modalId).on('click', function(e) {
        if (e.target === this) {
            UIHelper.closeModal(modalId);
        }
    });
}


/* ============================================
   DOCUMENT READY HELPER (문서 준비 헬퍼)
   Common initialization when page loads

   Usage in JSP:
   $(document).ready(function() {
       // Your code here
   });
   ============================================ */
