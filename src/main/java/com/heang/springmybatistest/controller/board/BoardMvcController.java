package com.heang.springmybatistest.controller.board;

import com.heang.springmybatistest.service.BoardService;
import com.heang.springmybatistest.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardMVCController — Traditional MVC Controller (게시판 MVC 컨트롤러)
 * <p>
 * Korean enterprise / eGovFrame style with .do URLs
 * 한국 기업 / 전자정부 표준프레임워크 스타일 (.do URL)
 *
 * @Controller          → returns VIEW NAME (JSP) — NOT JSON
 * @RestController      → returns JSON data directly
 * <p>
 * PRG Pattern (Post-Redirect-Get):
 *   GET → return "viewName" (forward to JSP)
 *   POST → return "redirect:/board/list.do" (redirect after action)
 */
@Controller
@RequestMapping("/board")          // base URL — all methods start with /board
@RequiredArgsConstructor
public class BoardMvcController {

    private final BoardService boardService; // ← interface, not Impl

    /**
     * GET /board/list.do
     * Show board list page (게시글 목록 페이지)
     * <p>
     * ModelMap — like a bag to pass data to JSP
     * model.addAttribute("key", value) → available as ${key} in JSP
     */
    @GetMapping("/list.do")
    public String list(ModelMap model) {
        List<BoardVO> boards = boardService.findAll();

        // Put data into ModelMap — JSP accesses via ${boards}
        model.addAttribute("boards", boards);
        model.addAttribute("totalCount", boards.size());

        return "board/list";  // → /WEB-INF/views/board/list.jsp
    }

    /**
     * GET /board/detail.do?boardSn=1
     * Show board detail page (게시글 상세 페이지)
     *
     * @RequestParam → reads ?boardSn=1 from URL query string
     * If not found → Service throws NotFoundException → 404
     */
    @GetMapping("/detail.do")
    public String detail(
            @RequestParam Long boardSn,
            ModelMap model
    ) {
        BoardVO board = boardService.findById(boardSn);

        // Put a single board into ModelMap — JSP accesses via ${board}
        model.addAttribute("board", board);

        return "board/detail"; // → /WEB-INF/views/board/detail.jsp
    }

    /**
     * GET /board/insertForm.do
     * Show empty form for new board (게시글 등록 폼 페이지)
     */
    @GetMapping("/insertForm.do")
    public String insertForm() {
        return "board/insertForm"; // → /WEB-INF/views/board/insertForm.jsp
    }

    /**
     * POST /board/insert.do
     * Save a new board post (게시글 등록 처리)
     *
     * @ModelAttribute → Spring automatically maps form fields to BoardVO
     *   form: boardTitle=Hello&boardCn=Content&user=Y
     *     ↓ auto mapping
     *   boardVO.boardTitle = "Hello"
     *   boardVO.boardCn = "Content"
     *   boardVO.useYn = "Y"
     *
     * PRG Pattern: after POST → redirect to GET (prevent duplicate submit)
     */
    @PostMapping("/insert.do")
    public String insert(@ModelAttribute BoardVO boardVO) {
        boardService.insert(boardVO);
        return "redirect:/board/list.do"; // PRG: redirect after POST ✅
    }

    /**
     * GET /board/updateForm.do?boardSn=1
     * Show an update form with existing data (게시글 수정 폼 페이지)
     * <p>
     * Load existing data first → pre-fill the form
     */
    @GetMapping("/updateForm.do")
    public String updateForm(
            @RequestParam Long boardSn,
            ModelMap model
    ) {
        BoardVO board = boardService.findById(boardSn);
        model.addAttribute("board", board); // pre-fill form with existing data

        return "board/updateForm"; // → /WEB-INF/views/board/updateForm.jsp
    }

    /**
     * POST /board/update.do
     * Save updated board post (게시글 수정 처리)
     *
     * @ModelAttribute → maps form fields to BoardVO automatically
     * PRG Pattern: redirect after POST
     */
    @PostMapping("/update.do")
    public String update(@ModelAttribute BoardVO boardVO) {
        boardService.update(boardVO);
        return "redirect:/board/list.do"; // PRG: redirect after POST ✅
    }

    /**
     * POST /board/delete.do
     * Soft delete board post (게시글 삭제 처리)
     * <p>
     * Why doesn't POST DELETE?
     * Traditional HTML forms only support GET and POST
     * Korean enterprise uses POST for delete with a hidden field
     * <p>
     * HTML: <input type="hidden" name="boardSn" value="1">
     * <p>
     * PRG Pattern: redirect after POST
     */
    @PostMapping("/delete.do")
    public String delete(@RequestParam Long boardSn) {
        boardService.delete(boardSn);
        return "redirect:/board/list.do"; // PRG: redirect after POST ✅
    }
}
