package com.heang.springmybatistest.controller;

import com.heang.springmybatistest.common.api.ApiResponse;
import com.heang.springmybatistest.service.BoardService;
import com.heang.springmybatistest.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * BoardApiController — REST API for board data (JSON)
 * Used by dashboard to fetch board count via fetch()
 * <p>
 * Separate from BoardMVCController (.do style JSP)
 * BoardMVCController → JSP pages
 * BoardApiController → JSON for AJAX/dashboard
 */
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // GET /api/board → returns all boards as JSON
    // Used by dashboard to show board count
    
    @GetMapping
    public ApiResponse<List<BoardVO>> findAll() {
        return ApiResponse.success(boardService.findAll());
    }
}