package com.heang.springmybatistest.controller.practice;

import com.heang.springmybatistest.common.api.ApiResponse;
import com.heang.springmybatistest.controller.BaseController;
import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.service.DynamicSqlPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for practicing Dynamic SQL operations
 *
 * Test these endpoints with Postman or any REST client
 */
@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DynamicSqlPracticeController extends BaseController {

    private final DynamicSqlPracticeService practiceService;

    // =========================================================
    // PRACTICE 1: <if> tag - Conditional filters
    // =========================================================
    @GetMapping("/search/if")
    public ApiResponse<List<UserResponse>> searchWithIf(UserSearchRequest request) {
        List<UserResponse> users = practiceService.searchWithIf(request);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 2: <choose><when><otherwise> - Dynamic sorting
    // =========================================================
    @GetMapping("/search/choose")
    public ApiResponse<List<UserResponse>> searchWithChoose(UserSearchRequest request) {
        List<UserResponse> users = practiceService.searchWithChoose(request);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 3: <trim> tag
    // =========================================================
    @GetMapping("/search/trim")
    public ApiResponse<List<UserResponse>> searchWithTrim(UserSearchRequest request) {
        List<UserResponse> users = practiceService.searchWithTrim(request);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 4: LIKE search with <bind>
    // =========================================================
    @GetMapping("/search/keyword")
    public ApiResponse<List<UserResponse>> searchByKeyword(@RequestParam String keyword) {
        List<UserResponse> users = practiceService.searchByKeyword(keyword);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 5: <foreach> - Find by multiple IDs
    // =========================================================
    @GetMapping("/users/by-ids")
    public ApiResponse<List<UserResponse>> findByIds(@RequestParam List<Long> ids) {
        List<UserResponse> users = practiceService.findByIds(ids);
        return ApiResponse.success(users);
    }

    @PostMapping("/users/by-ids")
    public ApiResponse<List<UserResponse>> findByIdsPost(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        List<UserResponse> users = practiceService.findByIds(ids);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 6: <foreach> - Find by multiple statuses
    // =========================================================
    @GetMapping("/users/by-statuses")
    public ApiResponse<List<UserResponse>> findByStatuses(@RequestParam List<String> statuses) {
        List<UserResponse> users = practiceService.findByStatuses(statuses);
        return ApiResponse.success(users);
    }

    // =========================================================
    // PRACTICE 7: <foreach> - Batch INSERT
    // =========================================================
    @PostMapping("/users/batch")
    public ApiResponse<Integer> batchInsert(@RequestBody List<UserRequest> users) {
        int count = practiceService.batchInsert(users);
        return ApiResponse.success(count);
    }

    // =========================================================
    // PRACTICE 8: <foreach> - Batch UPDATE status
    // =========================================================
    @PutMapping("/users/batch-status")
    public ApiResponse<Integer> batchUpdateStatus(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Integer>) body.get("ids"))
                .stream().map(Long::valueOf).toList();
        String status = (String) body.get("status");
        int count = practiceService.batchUpdateStatus(ids, status);
        return ApiResponse.success(count);
    }

    // =========================================================
    // PRACTICE 9: <foreach> - Batch DELETE
    // =========================================================
    @DeleteMapping("/users/batch")
    public ApiResponse<Integer> batchDelete(@RequestParam List<Long> ids) {
        int count = practiceService.batchDelete(ids);
        return ApiResponse.success(count);
    }

    // =========================================================
    // PRACTICE 10: <set> - Dynamic UPDATE
    // =========================================================
    @PatchMapping("/users/{id}")
    public ApiResponse<UserResponse> dynamicUpdate(
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        UserResponse user = practiceService.dynamicUpdate(id, request);
        return ApiResponse.success(user);
    }

    // =========================================================
    // PRACTICE 11: ADVANCED SEARCH - Everything combined
    // =========================================================
    @PostMapping("/search/advanced")
    public ApiResponse<Map<String, Object>> advancedSearch(@RequestBody UserSearchRequest request) {
        List<UserResponse> users = practiceService.advancedSearch(request);
        int total = practiceService.countAdvancedSearch(request);
        return ApiResponse.success(Map.of(
                "users", users,
                "total", total,
                "page", request.getPage() != null ? request.getPage() : 1,
                "size", request.getSize() != null ? request.getSize() : users.size()
        ));
    }

    @GetMapping("/search/advanced")
    public ApiResponse<Map<String, Object>> advancedSearchGet(UserSearchRequest request) {
        return advancedSearch(request);
    }
}
