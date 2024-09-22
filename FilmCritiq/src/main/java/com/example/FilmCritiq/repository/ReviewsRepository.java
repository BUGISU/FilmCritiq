package com.example.FilmCritiq.repository;

import com.example.FilmCritiq.entity.ClubMember;
import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Reviews;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    @EntityGraph(attributePaths = {"clubMember"},
            type = EntityGraph.EntityGraphType.FETCH)
    List<Reviews> findByMovies(Movies movies);

    // 쿼리메서드나, deleteById등은 한건씩 진행을 한다.
    // @Query를 사용해서 delete, update를 할 경우 Bulk연산을 함
    // 그래서 트랜잭션을 복수개 할 것을 한번에 처리하기 때문에
    // 복수의 트랜잭션을 한번에 처리하기 위해 @Modifying
    @Modifying
    @Query("delete from Reviews r where r.clubMember = :clubMember")
    void deleteByClubMember(ClubMember clubMember);

    @Modifying //update, deltet할 때 항상 표기
    @Query("delete from Reviews r where r.movies.mno=:mno")
    void deleteByFno(@Param("mno") Long mno);
}
