package com.heang.springmybatistest.service;

import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.mapper.BoardFileMapper;
import com.heang.springmybatistest.vo.BoardFileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * BoardFileServiceImpl — File attachment service implementation (첨부파일 서비스 구현체)
 *
 * Key pattern:
 *   1. Save file to disk (파일 저장)
 *   2. Insert record to DB (DB 등록)
 *
 * This is called from BoardServiceImpl.insertWithFiles() which is @Transactional.
 * If step 2 throws → Spring rolls back the board INSERT + all file INSERTs.
 * The disk files are already saved but since the DB has no record they are orphaned
 * (acceptable for a learning project; production systems add a cleanup job).
 */
@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    private final BoardFileMapper boardFileMapper;

    // Same upload folder used by FileUploadService (제품 이미지와 동일한 폴더 사용)
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    /**
     * Save each file to disk and insert a record to board_file table.
     * Skips empty files (브라우저가 빈 input 보낼 경우 skip).
     */
    @Override
    public void saveFiles(Long boardSn, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        // Create uploads folder if it does not exist (폴더 없으면 생성)
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory: " + e.getMessage());
        }

        for (MultipartFile file : files) {
            // Skip empty file input (사용자가 파일 첨부 안 했을 때 skip)
            if (file == null || file.isEmpty()) continue;

            String origName  = file.getOriginalFilename();    // "report.pdf"
            String extension = "";
            if (origName != null && origName.contains(".")) {
                extension = origName.substring(origName.lastIndexOf(".")); // ".pdf"
            }
            String savedName = UUID.randomUUID().toString() + extension;   // "a1b2c3.pdf"

            // ── 1. Save file to disk ──────────────────────────────────
            try {
                Path filePath = uploadPath.resolve(savedName);
                Files.copy(file.getInputStream(), filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + origName + " — " + e.getMessage());
            }

            // ── 2. Insert record to DB ────────────────────────────────
            // If this throws (DB error), Spring rolls back the entire transaction
            BoardFileVO vo = new BoardFileVO();
            vo.setBoardSn(boardSn);
            vo.setOrigName(origName);
            vo.setSavedName(savedName);
            vo.setFileSize(file.getSize());

            boardFileMapper.insert(vo);
        }
    }

    @Override
    public List<BoardFileVO> findByBoardSn(Long boardSn) {
        return boardFileMapper.findByBoardSn(boardSn);
    }

    @Override
    public BoardFileVO findById(Long fileSn) {
        BoardFileVO file = boardFileMapper.findById(fileSn);
        if (file == null) {
            throw new NotFoundException("File not found: " + fileSn);
        }
        return file;
    }
}
