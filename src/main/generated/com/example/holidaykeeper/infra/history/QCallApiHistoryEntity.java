package com.example.holidaykeeper.infra.history;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCallApiHistoryEntity is a Querydsl query type for CallApiHistoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCallApiHistoryEntity extends EntityPathBase<CallApiHistoryEntity> {

    private static final long serialVersionUID = -709558360L;

    public static final QCallApiHistoryEntity callApiHistoryEntity = new QCallApiHistoryEntity("callApiHistoryEntity");

    public final StringPath countryCode = createString("countryCode");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath endpoint = createString("endpoint");

    public final StringPath errorMessage = createString("errorMessage");

    public final StringPath httpMethod = createString("httpMethod");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSuccess = createBoolean("isSuccess");

    public final StringPath requestParams = createString("requestParams");

    public final NumberPath<Integer> requestYear = createNumber("requestYear", Integer.class);

    public final StringPath responseBody = createString("responseBody");

    public final NumberPath<Integer> responseStatus = createNumber("responseStatus", Integer.class);

    public final NumberPath<Integer> responseTimeMs = createNumber("responseTimeMs", Integer.class);

    public QCallApiHistoryEntity(String variable) {
        super(CallApiHistoryEntity.class, forVariable(variable));
    }

    public QCallApiHistoryEntity(Path<? extends CallApiHistoryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCallApiHistoryEntity(PathMetadata metadata) {
        super(CallApiHistoryEntity.class, metadata);
    }

}

