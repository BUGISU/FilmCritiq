package com.example.FilmCritiq.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Movies extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;
  private Long cno;
  private String title;
  private String content;

  private String releaseDate; //개봉일
  private String screeningTime; //상영시간
  private String audienceAge; //관람층

  public void changeTitle(String title) {
    this.title = title;
  }
  public void changeContent(String content) {this.content = content;}

}