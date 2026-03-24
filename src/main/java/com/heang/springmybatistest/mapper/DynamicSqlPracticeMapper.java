package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.model.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Practice Mapper for learning MyBatis Dynamic SQL
 * <p>
 * This mapper demonstrates all major Dynamic SQL features:
 * 1. <if> - conditional inclusion
 * 2. <choose><when><otherwise> - switch-like conditions.html
 * 3. <where> - smart WHERE clause handling
 * 4. <set> - smart SET clause for updates
 * 5. <foreach> - iteration over collections
 * 6. <trim> - custom trimming
 * 7. <sql><include> - reusable SQL fragments
 */
@Mapper
public interface DynamicSqlPracticeMapper {

    // =====================================================
    // PRACTICE 1: <if> tag - Conditional Search
    // =====================================================
    /**
     * Search users with optional filters
     * Demonstrates: <if>, <where>
     */
    List<Users> searchUsersWithIf(UserSearchRequest request);

    // =====================================================
    // PRACTICE 2: <choose><when><otherwise> - Switch Logic
    // =====================================================
    /**
     * Search with dynamic sorting
     * Demonstrates: <choose>, <when>, <otherwise>
     */
    List<Users> searchUsersWithChoose(UserSearchRequest request);

    // =====================================================
    // PRACTICE 3: <foreach> - Collection Iteration
    // =====================================================
    /**
     * Find users by list of IDs
     * Demonstrates: <foreach> with IN clause
     */
    List<Users> findUsersByIds(@Param("ids") List<Long> ids);

    /**
     * Find users by multiple statuses
     * Demonstrates: <foreach> for multiple conditions.html
     */
    List<Users> findUsersByStatuses(@Param("statuses") List<String> statuses);

    /**
     * Batch insert users
     * Demonstrates: <foreach> for INSERT
     */
    int batchInsertUsers(@Param("users") List<UserRequest> users);

    /**
     * Batch update status
     * Demonstrates: <foreach> for UPDATE
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                          @Param("status") String status);

    /**
     * Batch delete users
     * Demonstrates: <foreach> for DELETE
     */
    int batchDeleteUsers(@Param("ids") List<Long> ids);

    // =====================================================
    // PRACTICE 4: <set> - Dynamic Update
    // =====================================================
    /**
     * Update user with only non-null fields
     * Demonstrates: <set>, <if>
     */
    int dynamicUpdate(@Param("id") Long id,
                      @Param("req") UserRequest request);

    // =====================================================
    // PRACTICE 5: <trim> - Custom Trimming
    // =====================================================
    /**
     * Search with trim (alternative to <where>)
     * Demonstrates: <trim>
     */
    List<Users> searchUsersWithTrim(UserSearchRequest request);

    // =====================================================
    // PRACTICE 6: <sql><include> - Reusable Fragments
    // =====================================================
    /**
     * Uses SQL fragment for common columns
     * Demonstrates: <sql>, <include>
     */
    List<Users> findAllWithSqlFragment();

    // =====================================================
    // PRACTICE 7: Combined - Advanced Search
    // =====================================================
    /**
     * Full-featured search combining all Dynamic SQL features
     * Demonstrates: Everything together!
     */
    List<Users> advancedSearch(UserSearchRequest request);

    /**
     * Count for pagination
     */
    int countAdvancedSearch(UserSearchRequest request);

    // =====================================================
    // PRACTICE 8: LIKE Search Patterns
    // =====================================================
    /**
     * Search with LIKE using different patterns
     * Demonstrates: LIKE with <bind> or CONCAT
     */
    List<Users> searchByKeyword(@Param("keyword") String keyword);
}
