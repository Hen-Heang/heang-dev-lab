package com.heang.springmybatistest.controller.board;

import com.heang.springmybatistest.service.BoardFileService;
import com.heang.springmybatistest.service.BoardService;
import com.heang.springmybatistest.vo.BoardFileVO;
import com.heang.springmybatistest.vo.BoardSearchV0;
import com.heang.springmybatistest.vo.BoardVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * BoardMVCController — Traditional MVC Controller (게시판 MVC 컨트롤러)
 * <p>
 * Korean enterprise / eGovFrame style with .do URLs
 * 한국 기업 / 전자정부 표준프레임워크 스타일 (.do URL)
 *
 * @Controller → returns VIEW NAME (JSP) — NOT JSON
 * @RestController → returns JSON data directly
 * <p>
 * PRG Pattern (Post-Redirect-Get):
 * GET → return "viewName" (forward to JSP)
 * POST → return "redirect:/board/list.do" (redirect after action)
 */
@Controller
@RequestMapping("/board")          // base URL — all methods start with /board
@RequiredArgsConstructor
public class BoardMvcController {

    private final BoardService     boardService;
    private final BoardFileService boardFileService;

    // Must match UPLOAD_DIR in BoardFileServiceImpl (업로드 경로 일치)
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    /**
     * GET /board/list.do
     * Show board list page (게시글 목록 페이지)
     * <p>
     * ModelMap — like a bag to pass data to JSP
     * model.addAttribute("key", value) → available as ${key} in JSP
     */

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
        model.addAttribute("board", board);

        // Load attached files for this board (첨부파일 목록 조회)
        List<BoardFileVO> files = boardFileService.findByBoardSn(boardSn);
        model.addAttribute("files", files);

        return "board/detail";
    }

    /**
     * GET /board/download.do?fileSn=1
     * Stream file from disk to browser as a download (파일 다운로드)
     *
     * Content-Disposition: attachment → forces browser to download, not display
     * Content-Type: application/octet-stream → generic binary stream
     */
    @GetMapping("/download.do")
    public void download(
            @RequestParam Long fileSn,
            HttpServletResponse response
    ) throws IOException {
        BoardFileVO file = boardFileService.findById(fileSn);

        Path filePath = Paths.get(UPLOAD_DIR).resolve(file.getSavedName());
        if (!Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on disk");
            return;
        }

        // Tell browser to download with the original filename (원본 파일명으로 다운로드)
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + file.getOrigName() + "\"");
        response.setContentLengthLong(file.getFileSize());

        Files.copy(filePath, response.getOutputStream());
        response.getOutputStream().flush();
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
     * form: boardTitle=Hello&boardCn=Content&user=Y
     * ↓ auto mapping
     * boardVO.boardTitle = "Hello"
     * boardVO.boardCn = "Content"
     * boardVO.useYn = "Y"
     * <p>
     * PRG Pattern: after POST → redirect to GET (prevent duplicate submit)
     */
    /**
     * POST /board/insert.do
     * Insert board + files in one transaction (게시글+첨부파일 트랜잭션 등록)
     *
     * enctype="multipart/form-data" on the form is required for file upload.
     * @RequestParam(required=false) — still works if user submits with no files.
     */
    @PostMapping("/insert.do")
    public String insert(
            @ModelAttribute BoardVO boardVO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        boardService.insertWithFiles(boardVO, files); // @Transactional inside
        return "redirect:/board/list.do";
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

    @GetMapping("/list.do")
    public String list(BoardSearchV0 searchV0, ModelMap model) {
        int total = boardService.countBySearch(searchV0);
        int totalPages = (int) Math.ceil((double) total / searchV0.getPageSize());
        model.addAttribute("boards", boardService.findBySearch(searchV0));
        model.addAttribute("search", searchV0);
        model.addAttribute("totalCount", total);
        model.addAttribute("totalPages", totalPages);

        return "board/list";
    }
}
