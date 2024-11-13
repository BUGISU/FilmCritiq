package com.example.FilmCritiq.service;

import com.example.FilmCritiq.dto.ReviewsDTO;
import com.example.FilmCritiq.entity.ClubMember;
import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Reviews;
import com.example.FilmCritiq.repository.ClubMemberRepository;
import com.example.FilmCritiq.repository.MoviesRepository;
import com.example.FilmCritiq.repository.ReviewsRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReviewsServiceImpl implements ReviewsService {
  private final ReviewsRepository reviewsRepository;
  private final MoviesRepository moviesRepository;
  private final ClubMemberRepository clubMemberRepository;

  @Override
  public List<ReviewsDTO> getListOfMovies(Long mno) {
    List<Reviews> result = reviewsRepository.findByMovies(
        Movies.builder().mno(mno).build());
    return result.stream().map(this::entityToDto).collect(Collectors.toList());
  }

  @Override
  public Long register(ReviewsDTO reviewsDTO) {
    log.info("reviewsDTO >> {}", reviewsDTO);

    // Movies와 ClubMember 엔티티를 데이터베이스에서 조회
    Movies movie = moviesRepository.findById(reviewsDTO.getMno())
        .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + reviewsDTO.getMno()));
    ClubMember member = clubMemberRepository.findByEmail(reviewsDTO.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid club member email: " + reviewsDTO.getEmail()));

    // 리뷰 엔티티 생성 및 저장
    Reviews review = Reviews.builder()
        .movies(movie)
        .clubMember(member)
        .text(reviewsDTO.getText())
        .build();

    // 리뷰를 저장하고 생성된 reviewsnum 반환
    Reviews savedReview = reviewsRepository.save(review);
    return savedReview.getReviewsnum(); // 저장된 엔티티의 ID를 반환
  }



  @Override
  public void modify(ReviewsDTO reviewsDTO) {
    Optional<Reviews> result = reviewsRepository.findById(reviewsDTO.getReviewsnum());
    if (result.isPresent()) {
      Reviews reviews = result.get();
      reviews.changeText(reviewsDTO.getText());
      reviewsRepository.save(reviews);
    }
  }

  @Override
  public void remove(Long reviewsnum) {
    log.info("remove reviewnum >> {}", reviewsnum);
    reviewsRepository.deleteById(reviewsnum);
  }
}
