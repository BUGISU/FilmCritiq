package com.example.FilmCritiq.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviews is a Querydsl query type for Reviews
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviews extends EntityPathBase<Reviews> {

    private static final long serialVersionUID = 338615271L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviews reviews = new QReviews("reviews");

    public final QBasicEntity _super = new QBasicEntity(this);

    public final QClubMember clubMember;

    public final QFeeds feeds;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final NumberPath<Long> reviewsnum = createNumber("reviewsnum", Long.class);

    public final StringPath text = createString("text");

    public QReviews(String variable) {
        this(Reviews.class, forVariable(variable), INITS);
    }

    public QReviews(Path<? extends Reviews> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviews(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviews(PathMetadata metadata, PathInits inits) {
        this(Reviews.class, metadata, inits);
    }

    public QReviews(Class<? extends Reviews> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubMember = inits.isInitialized("clubMember") ? new QClubMember(forProperty("clubMember")) : null;
        this.feeds = inits.isInitialized("feeds") ? new QFeeds(forProperty("feeds")) : null;
    }

}

