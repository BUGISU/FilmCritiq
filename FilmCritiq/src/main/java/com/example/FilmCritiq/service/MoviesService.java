package com.example.FilmCritiq.service;

import com.example.FilmCritiq.dto.MoviesDTO;
import com.example.FilmCritiq.dto.PageRequestDTO;
import com.example.FilmCritiq.dto.PageResultDTO;
import com.example.FilmCritiq.dto.PhotosDTO;
import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface MoviesService {
  Long register(MoviesDTO moviesDTO);

  PageResultDTO<MoviesDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  MoviesDTO getMovies(Long mno);


  void modify(MoviesDTO moviesDTO);

  List<String> removeWithReviewsAndPhotos(Long mno);

  void removeUuid(String uuid);


  default Map<String, Object> dtoToEntity(MoviesDTO moviesDTO) {
    Map<String, Object> entityMap = new HashMap<>();
    Movies movies = Movies.builder().mno(moviesDTO.getMno())
            .title(moviesDTO.getTitle())
            .content(moviesDTO.getContent())
            .director(moviesDTO.getDirector())
            .actor(moviesDTO.getActor())
            .genre(moviesDTO.getGenre())
            .releaseDate(moviesDTO.getReleaseDate())
            .screeningTime(moviesDTO.getScreeningTime())
            .audienceAge(moviesDTO.getAudienceAge())
            .build();
    entityMap.put("movies", movies);
    List<PhotosDTO> photosDTOList = moviesDTO.getPhotosDTOList();
    if (photosDTOList != null && photosDTOList.size() > 0) {
      List<Photos> photosList = photosDTOList.stream().map(
          new Function<PhotosDTO, Photos>() {
            @Override
            public Photos apply(PhotosDTO photosDTO) {
              Photos photos = Photos.builder()
                  .path(photosDTO.getPath())
                  .imgName(photosDTO.getImgName())
                  .uuid(photosDTO.getUuid())
                  .movies(movies)
                  .build();
              return photos;
            }
          }
      ).collect(Collectors.toList());
      entityMap.put("photosList", photosList);
    }
    return entityMap;
  }

  default MoviesDTO entityToDto(Movies movies, List<Photos> photosList
      , Long reviewsCnt) {
    MoviesDTO moviesDTO = MoviesDTO.builder()
            .mno(movies.getMno())
            .title(movies.getTitle())
            .content(movies.getContent())
            .releaseDate(movies.getReleaseDate())
            .screeningTime(movies.getScreeningTime())
            .audienceAge(movies.getAudienceAge()  )
            .regDate(movies.getRegDate())
            .modDate(movies.getModDate())
            .build();
    List<PhotosDTO> photosDTOList = new ArrayList<>();
    if(photosList.toArray().length > 0 && photosList.toArray()[0] != null) {
      photosDTOList = photosList.stream().map(
          photos -> {
            PhotosDTO photosDTO = PhotosDTO.builder()
                .imgName(photos.getImgName())
                .path(photos.getPath())
                .uuid(photos.getUuid())
                .build();
            return photosDTO;
          }
      ).collect(Collectors.toList());
    }
    moviesDTO.setPhotosDTOList(photosDTOList);
    moviesDTO.setReviewsCnt(reviewsCnt);
    return moviesDTO;
  }
}
