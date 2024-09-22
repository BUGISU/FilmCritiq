package com.example.FilmCritiq.service;
import com.example.FilmCritiq.dto.ReviewsDTO;
import com.example.FilmCritiq.entity.ClubMember;
import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Reviews;

import java.util.List;

public interface ReviewsService {
  List<ReviewsDTO> getListOfMovies(Long mno);
  Long register(ReviewsDTO reviewsDTO);

  void modify(ReviewsDTO reviewsDTO);

  void remove(Long revieswnum);

  public default Reviews dtoToEntity(ReviewsDTO reviewsDTO) {
    Reviews review = Reviews.builder()
        .reviewsnum(reviewsDTO.getReviewsnum())
        .movies(Movies.builder().mno(reviewsDTO.getMno()).build())
        .clubMember(ClubMember.builder().cno(reviewsDTO.getCno()).build())
        .text(reviewsDTO.getText())
        .build();
    return review;
  }

  default ReviewsDTO entityToDto(Reviews reviews) {
    ReviewsDTO reviewDTO = ReviewsDTO.builder()
        .reviewsnum(reviews.getReviewsnum())
        .mno(reviews.getMovies().getMno())
        .cno(reviews.getClubMember().getCno())
        .nickname(reviews.getClubMember().getName())
        .email(reviews.getClubMember().getEmail())
        .text(reviews.getText())
        .regDate(reviews.getRegDate())
        .modDate(reviews.getModDate())
        .build();
    return reviewDTO;
  }
}
