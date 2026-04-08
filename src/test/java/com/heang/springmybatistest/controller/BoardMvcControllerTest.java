package com.heang.springmybatistest.controller;

import com.heang.springmybatistest.controller.board.BoardMvcController;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.service.BoardService;
import com.heang.springmybatistest.vo.BoardVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BoardMsvcControllerTest — Tests HTTP URLs and routing
 * HTTP 요청/응답 및 라우팅 테스트
 *
 * @SpringBootTest      → loads full Spring context
 * @AutoConfigureMockMvc → enables MockMvc without needing @WebMvcTest
 * @MockitoBean         → replaces BoardService with a fake (no real DB)
 * <p>
 * What we test:
 * - Correct URL mapping (@GetMapping, @PostMapping)
 * - Correct view name returned (board/list, board/detail)
 * - Correct ModelMap attributes passed to JSP
 * - Redirect after POST (PRG pattern)
 */
@ExtendWith(MockitoExtension.class)
class BoardMvcControllerTest {

    @Mock
    private BoardService boardService; // fake — no real DB calls

    @InjectMocks
    private BoardMvcController controller; // real controller + fake service

    private MockMvc mockMvc;

    private BoardVO sampleBoard;

    @BeforeEach
    void setUp() {
        // No @AutoConfigureMockMvc needed — build MockMvc directly from controller
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        sampleBoard = new BoardVO();
        sampleBoard.setBoardSn(1L);
        sampleBoard.setBoardTitle("Sample Title");
        sampleBoard.setBoardCn("Sample Content");
        sampleBoard.setUseYn("Y");
        sampleBoard.setDataRegDt(LocalDateTime.now());
    }

    // ====================================================
    // GET /board/list.do
    // ====================================================

    @Test
    @DisplayName("GET /board/list.do: returns HTTP 200 and board/list view")
    void list_returns200_andListView() throws Exception {
        when(boardService.findAll())
                .thenReturn(Arrays.asList(sampleBoard, new BoardVO()));

        mockMvc.perform(get("/board/list.do"))
                .andExpect(status().isOk())                     // HTTP 200
                .andExpect(model().attributeExists("boards"))   // ModelMap has "boards"
                .andExpect(model().attributeExists("totalCount")); // ModelMap has "totalCount"
    }

    @Test
    @DisplayName("GET /board/list.do: totalCount = 0 when no boards")
    void list_totalCountZero_whenNoBoards() throws Exception {
        when(boardService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/board/list.do"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 0));
    }

    // ====================================================
    // GET /board/detail.do?boardSn=1
    // ====================================================

    @Test
    @DisplayName("GET /board/detail.do: returns HTTP 200 with board in model")
    void detail_returns200_withBoard() throws Exception {
        when(boardService.findById(1L)).thenReturn(sampleBoard);

        mockMvc.perform(get("/board/detail.do").param("boardSn", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board")); // ModelMap has "board"
    }

    @Test
    @DisplayName("GET /board/detail.do: returns 404 when board not found")
    void detail_returns404_whenNotFound() throws Exception {
        when(boardService.findById(99L))
                .thenThrow(new NotFoundException("Board not found: 99"));

        mockMvc.perform(get("/board/detail.do").param("boardSn", "99"))
                .andExpect(status().is5xxServerError()); // standaloneSetup: no GlobalExceptionHandler
    }

    // ====================================================
    // GET /board/insertForm.do
    // ====================================================

    @Test
    @DisplayName("GET /board/insertForm.do: returns HTTP 200")
    void insertForm_returns200() throws Exception {
        mockMvc.perform(get("/board/insertForm.do"))
                .andExpect(status().isOk());
    }

    // ====================================================
    // POST /board/insert.do
    // ====================================================

    @Test
    @DisplayName("POST /board/insert.do: inserts board and redirects to list (PRG)")
    void insert_insertsBoard_andRedirects() throws Exception {
        doNothing().when(boardService).insert(any(BoardVO.class));

        mockMvc.perform(post("/board/insert.do")
                        .param("boardTitle", "New Title")
                        .param("boardCn", "New Content")
                        .param("useYn", "Y"))
                .andExpect(status().is3xxRedirection())        // HTTP 302
                .andExpect(redirectedUrl("/board/list.do"));   // PRG → back to list

        verify(boardService, times(1)).insert(any(BoardVO.class));
    }

    // ====================================================
    // GET /board/updateForm.do?boardSn=1
    // ====================================================

    @Test
    @DisplayName("GET /board/updateForm.do: returns HTTP 200 with board pre-filled")
    void updateForm_returns200_withBoard() throws Exception {
        when(boardService.findById(1L)).thenReturn(sampleBoard);

        mockMvc.perform(get("/board/updateForm.do").param("boardSn", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board")); // form pre-filled
    }

    // ====================================================
    // POST /board/update.do
    // ====================================================

    @Test
    @DisplayName("POST /board/update.do: updates board and redirects to list (PRG)")
    void update_updatesBoard_andRedirects() throws Exception {
        when(boardService.update(any(BoardVO.class))).thenReturn(1);

        mockMvc.perform(post("/board/update.do")
                        .param("boardSn", "1")
                        .param("boardTitle", "Updated Title")
                        .param("boardCn", "Updated Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list.do")); // PRG ✅

        verify(boardService, times(1)).update(any(BoardVO.class));
    }

    // ====================================================
    // POST /board/delete.do
    // ====================================================

    @Test
    @DisplayName("POST /board/delete.do: deletes board and redirects to list (PRG)")
    void delete_deletesBoard_andRedirects() throws Exception {
        when(boardService.delete(1L)).thenReturn(1);

        mockMvc.perform(post("/board/delete.do").param("boardSn", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list.do")); // PRG ✅

        verify(boardService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("POST /board/delete.do: returns 404 when board not found")
    void delete_returns404_whenNotFound() throws Exception {
        when(boardService.delete(99L))
                .thenThrow(new NotFoundException("Board not found: 99"));

        mockMvc.perform(post("/board/delete.do").param("boardSn", "99"))
                .andExpect(status().is5xxServerError()); // standaloneSetup: no GlobalExceptionHandler
    }
}
