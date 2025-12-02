package com.example.holidaykeeper.infra.history;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDataSyncHistoryEntity is a Querydsl query type for DataSyncHistoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDataSyncHistoryEntity extends EntityPathBase<DataSyncHistoryEntity> {

    private static final long serialVersionUID = 529551941L;

    public static final QDataSyncHistoryEntity dataSyncHistoryEntity = new QDataSyncHistoryEntity("dataSyncHistoryEntity");

    public final DateTimePath<java.time.LocalDateTime> completedAt = createDateTime("completedAt", java.time.LocalDateTime.class);

    public final StringPath countryCode = createString("countryCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.holidaykeeper.domain.history.OperationTypeEnum> operationType = createEnum("operationType", com.example.holidaykeeper.domain.history.OperationTypeEnum.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> syncYear = createNumber("syncYear", Integer.class);

    public QDataSyncHistoryEntity(String variable) {
        super(DataSyncHistoryEntity.class, forVariable(variable));
    }

    public QDataSyncHistoryEntity(Path<? extends DataSyncHistoryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDataSyncHistoryEntity(PathMetadata metadata) {
        super(DataSyncHistoryEntity.class, metadata);
    }

}

