package com.example.FilmCritiq.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMovies is a Querydsl query type for Movies
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovies extends EntityPathBase<Movies> {

    private static final long serialVersionUID = -1785555465L;

    public static final QMovies movies = new QMovies("movies");

    public final QBasicEntity _super = new QBasicEntity(this);

    public final StringPath audienceAge = createString("audienceAge");

    public final NumberPath<Long> cno = createNumber("cno", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> mno = createNumber("mno", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath releaseDate = createString("releaseDate");

    public final StringPath screeningTime = createString("screeningTime");

    public final StringPath title = createString("title");

    public QMovies(String variable) {
        super(Movies.class, forVariable(variable));
    }

    public QMovies(Path<? extends Movies> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovies(PathMetadata metadata) {
        super(Movies.class, metadata);
    }

}

