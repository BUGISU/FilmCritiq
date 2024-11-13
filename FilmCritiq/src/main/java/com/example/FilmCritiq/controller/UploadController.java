package com.example.FilmCritiq.controller;

import com.example.FilmCritiq.repository.PhotosRepository;
import com.example.FilmCritiq.dto.UploadResultDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {
  private final PhotosRepository photosRepository;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> upload(MultipartFile[] uploadFiles) {
    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    for (MultipartFile multipartFile : uploadFiles) {
      String originalName = multipartFile.getOriginalFilename();
      String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
      log.info("fileName: " + fileName);

      String folderPath = makeFolder(); // yyyy/MM/dd 형식 폴더 생성
      String uuid = UUID.randomUUID().toString(); // 고유 ID 생성
      String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
      Path savePath = Paths.get(saveName);

      try {
        multipartFile.transferTo(savePath); // 원본 파일 저장
        resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath)); // 업로드 결과에 추가
      } catch (IOException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
  }

  @GetMapping("/display")
  public ResponseEntity<byte[]> getImageFile(String fileName) {
    try {
      String searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", Files.probeContentType(file.toPath()));
      return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  @PostMapping("/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName, String uuid) {
    log.info("remove fileName: " + fileName);
    boolean originalFileDeleted = false;

    try {
      // 파일 이름을 UTF-8로 디코딩
      String decodedFileName = URLDecoder.decode(fileName, "UTF-8");

      // 원본 파일 삭제
      File originalFile = new File(uploadPath + File.separator + decodedFileName);
      originalFileDeleted = originalFile.delete();
      log.info("Original file deleted: " + originalFileDeleted);

      // DB에서 uuid에 해당하는 레코드 삭제
      if (uuid != null) {
        photosRepository.deleteByUuid(uuid);
      }

      // 삭제 결과 반환
      return new ResponseEntity<>(originalFileDeleted, originalFileDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);

    } catch (Exception e) {
      log.error("Error deleting file: ", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String folderPath = str.replace("/", File.separator);
    File uploadPathFolder = new File(uploadPath, folderPath);
    if (!uploadPathFolder.exists()) uploadPathFolder.mkdirs();
    return folderPath;
  }
}
