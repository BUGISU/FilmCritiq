package com.example.FilmCritiq.repository;

import com.example.FilmCritiq.repository.search.SearchRepository;
import com.example.FilmCritiq.entity.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoviesRepository extends JpaRepository<Movies,Long>, SearchRepository {
//  select m.mno, avg(coalesce(r.grade,0)), count(r.reviewnum)
//  from movie m left outer join review r on m.mno = r.movie_mno
//  group by m.mno;

    // 영화에 대한 리뷰의 평점과 댓글 갯수를 출력
    @Query("select f, count(distinct r) " +
            "from Movies f left outer join Reviews r on r.movies=f group by f ")
    Page<Object[]> getListPage(Pageable pageable);

    // 아래와 같은 경우 mi를 찾기 위해서 review 카운트 만큼 반복횟수도 늘어나는 문제점
    // mi의 inum이 가장 낮은 이미지 번호가 출력된다.
    // 영화와 영화이미지,리뷰의 평점과 댓글 갯수 출력
    @Query("select f, pt, count(distinct r) from Movies f " +
            "left outer join Photos pt on pt.movies = f " +
            "left outer join Reviews   r  on r.movies  = f group by f ")
    Page<Object[]> getListPageImg(Pageable pageable);

    // spring 3.x에서는 실행 안됨.
    @Query("select f,max(pt),count(distinct r) from Movies f " +
            "left outer join Photos pt on pt.movies = f " +
            "left outer join Reviews   r  on r.movies  = f group by f ")
    Page<Object[]> getListPageMaxImg(Pageable pageable);

    // Native Query = SQL
    @Query(value = "select f.mno, pt.pnum, pt.img_name, " +
            "count(r.reviewsnum) " +
            "from db7.photos pt left outer join db7.movies f on f.mno=pt.movies_mno " +
            "left outer join db7.reviews r on f.mno=r.movies_mno " +
            "where pt.pnum = " +
            "(select max(pnum) from db7.photos pt2 where pt2.movies_mno=f.mno) " +
            "group by f.mno ", nativeQuery = true)
    Page<Object[]> getListPageImgNative(Pageable pageable);

    // JPQL
    @Query("select f, pt,count(distinct r) from Movies f " +
            "left outer join Photos pt on pt.movies = f " +
            "left outer join Reviews     r  on r.movies  = f " +
            "where pnum = (select max(pt2.pnum) from Photos pt2 where pt2.movies=f) " +
            "group by f ")
    Page<Object[]> getListPageImgJPQL(Pageable pageable);

    @Query("select movies, max(pt.pnum) from Photos pt group by movies")
    Page<Object[]> getMaxQuery(Pageable pageable);

    @Query("select f, pt, count(r) " +
            "from Movies f left outer join Photos pt on pt.movies=f " +
            "left outer join Reviews r on r.movies = f " +
            "where f.mno = :mno group by pt ")
    List<Object[]> getMoviesWithAll(Long mno); //특정 영화 조회
}
