package com.example.holidaykeeper.infra.holiday;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCountyEntity is a Querydsl query type for CountyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCountyEntity extends EntityPathBase<CountyEntity> {

    private static final long serialVersionUID = 1831678230L;

    public static final QCountyEntity countyEntity = new QCountyEntity("countyEntity");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> holidayId = createNumber("holidayId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath name = createString("name");

    public QCountyEntity(String variable) {
        super(CountyEntity.class, forVariable(variable));
    }

    public QCountyEntity(Path<? extends CountyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCountyEntity(PathMetadata metadata) {
        super(CountyEntity.class, metadata);
    }

}

