package com.example.FilmCritiq.repository;

import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Photos;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
class MoviesRepositoryTests {
    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    PhotosRepository photosRepository;

    @Transactional
    @Commit
    @Test
    public void insertMovies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movies movies = Movies.builder()
                    .title("Movies..." + i)
                    .content("Content..." + i)
                    .releaseDate("2022-01-01")
                    .audienceAge("15")
                    .screeningTime("120")
                .build();
            moviesRepository.save(movies);
            System.out.println("----------------------------");
            int cnt = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < cnt; j++) {
                Photos photos = Photos.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movies(movies)
                        .imgName("Photos" + j + ".jpg")
                        .build();
                photosRepository.save(photos);
            }
        });
    }

    @Test
    public void testListPage() {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
        Page<Object[]> result = moviesRepository.getListPage(pageRequest);
        for (Object[] objArr : result.getContent()) {
            System.out.println(Arrays.toString(objArr));
        }
    }
    @Test
    public void testListPageImg() {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
        Page<Object[]> result = moviesRepository.getListPageImg(pageRequest);
        for (Object[] objArr : result.getContent()) {
            System.out.println(Arrays.toString(objArr));
        }
    }
    @Test
    public void testGetListPageImgNative() {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movies_mno"));
        Page<Object[]> result = moviesRepository.getListPageImgNative(pageRequest);
        for (Object[] objArr : result.getContent()) {
            System.out.println(Arrays.toString(objArr));
        }
    }

    @Test
    public void testGetListPageImgJPQL() {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
        Page<Object[]> result = moviesRepository.getListPageImgJPQL(pageRequest);
        for (Object[] objArr : result.getContent()) {
            System.out.println(Arrays.toString(objArr));
        }
    }

    @Test
    public void testGetMaxQuery() {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movies"));
        Page<Object[]> result = moviesRepository.getMaxQuery(pageRequest);
        for (Object[] objArr : result.getContent()) {
            System.out.println(Arrays.toString(objArr));
        }
    }

    @Test
    public void testGetMoviesWithAll() {
        List<Object[]> result = moviesRepository.getMoviesWithAll(101L);
        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

//  @Test
//  public void testSearch1() {
//    movieRepository.search1();
//  }

    @Test
    public void testSearchPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending().and(Sort.by("title").ascending()));
        Page<Object[]> result = moviesRepository.searchPage("t", "1", pageable);
    }
}