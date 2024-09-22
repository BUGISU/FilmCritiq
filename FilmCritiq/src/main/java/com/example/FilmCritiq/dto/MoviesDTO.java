package com.example.FilmCritiq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoviesDTO {
  private Long mno; //pst 번호
  private Long  cno; //작성자 번호

  private String title; //영화제목 내용
  private String content; //줄거리 내용

  private String releaseDate; //개봉일
  private String screeningTime; //상영시간
  private String audienceAge; //관람층

  @Builder.Default
  private List<PhotosDTO> photosDTOList = new ArrayList<>();
  private Long reviewsCnt;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}
