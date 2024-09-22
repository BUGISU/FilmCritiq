package com.example.FilmCritiq.repository;

import com.example.FilmCritiq.entity.ClubMember;
import com.example.FilmCritiq.entity.Movies;
import com.example.FilmCritiq.entity.Reviews;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class ReviewsRepositoryTests {
    @Autowired
    ReviewsRepository reviewsRepository;
    @Autowired
    ClubMemberRepository clubMemberRepository;

    @Test
    public void insertReviews() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            Long mno = (long) (Math.random() * 100) + 1;
            Long cno = (long) (Math.random() * 100) + 1;
            Reviews review = Reviews.builder()
                    .clubMember(ClubMember.builder().cno(cno).build())
                    .movies(Movies.builder().mno(mno).build())
                    .text("이 피드는.....")
                    .build();
            reviewsRepository.save(review);
        });
    }

    @Test
    public void testFindByMovies() {
        List<Reviews> result = reviewsRepository.findByMovies(
                Movies.builder().mno(100L).build()
        );
        result.forEach(review -> {
            System.out.println(review.getReviewsnum());
            System.out.println(review.getText());
            System.out.println(review.getClubMember().getEmail());
            System.out.println();
        });
    }

    @Test
    @Transactional
    @Commit
    public void testDeleteByClubMemeber() {
        Long cno = 1L;
        ClubMember clubMember = ClubMember.builder().cno(cno).build();
        reviewsRepository.deleteByClubMember(clubMember);
        clubMemberRepository.deleteById(cno);
    }
}
