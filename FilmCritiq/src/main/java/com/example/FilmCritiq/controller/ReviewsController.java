package com.example.FilmCritiq.controller;
import com.example.FilmCritiq.dto.ReviewsDTO;
import com.example.FilmCritiq.entity.ClubMember;
import com.example.FilmCritiq.repository.ClubMemberRepository;
import com.example.FilmCritiq.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
  private final ReviewsService reviewsService;
  private final ClubMemberRepository clubMemberRepository;
  @GetMapping(value = "/{mno}/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReviewsDTO>> getList(@PathVariable("mno") Long mno) {
    log.info("mno: " + mno);
    List<ReviewsDTO> reviewsDTOList = reviewsService.getListOfMovies(mno);
    return new ResponseEntity<>(reviewsDTOList, HttpStatus.OK);
  }

  @PostMapping("/{mno}")
  public ResponseEntity<Long> register(@RequestBody ReviewsDTO reviewsDTO, Principal principal) {
    log.info("Entering register method with reviewsDTO: {}", reviewsDTO);

    // 1. 로그인한 사용자의 이메일 가져오기
    String loggedInEmail = principal.getName();
    log.info("Logged in email: {}", loggedInEmail);

    // 2. ClubMember 조회
    ClubMember clubMember = clubMemberRepository.findByEmail(loggedInEmail)
        .orElseThrow(() -> new IllegalArgumentException("Invalid member email: " + loggedInEmail));
    log.info("ClubMember found: {}", clubMember);

    // 3. 리뷰 DTO에 사용자 정보 설정
    reviewsDTO.setNickname(clubMember.getName());
    reviewsDTO.setEmail(clubMember.getEmail());
    log.info("Updated reviewsDTO with ClubMember info: {}", reviewsDTO);

    // 4. 리뷰 등록 및 결과 반환
    Long reviewsnum = reviewsService.register(reviewsDTO);
    log.info("Review registered with ID: {}", reviewsnum);

    return new ResponseEntity<>(reviewsnum, HttpStatus.OK);
  }



  @PutMapping("/{mno}/{reviewsnum}")
  public ResponseEntity<Long> modify(@RequestBody ReviewsDTO reviewsDTO) {
    log.info(">>modify " + reviewsDTO);
    reviewsService.modify(reviewsDTO);
    return new ResponseEntity<>(reviewsDTO.getReviewsnum(), HttpStatus.OK);
  }

  @DeleteMapping("/{mno}/{reviewsnum}")
  public ResponseEntity<Long> delete(@PathVariable Long reviewsnum) {
    log.info(">>delete" + reviewsnum);
    reviewsService.remove(reviewsnum);
    return new ResponseEntity<>(reviewsnum, HttpStatus.OK);
  }

}
