package com.example.holidaykeeper.infra.holiday;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHolidayTypeEntity is a Querydsl query type for HolidayTypeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHolidayTypeEntity extends EntityPathBase<HolidayTypeEntity> {

    private static final long serialVersionUID = -1444284276L;

    public static final QHolidayTypeEntity holidayTypeEntity = new QHolidayTypeEntity("holidayTypeEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> holidayId = createNumber("holidayId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<com.example.holidaykeeper.domain.holiday.HolidayTypeEnum> type = createEnum("type", com.example.holidaykeeper.domain.holiday.HolidayTypeEnum.class);

    public QHolidayTypeEntity(String variable) {
        super(HolidayTypeEntity.class, forVariable(variable));
    }

    public QHolidayTypeEntity(Path<? extends HolidayTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHolidayTypeEntity(PathMetadata metadata) {
        super(HolidayTypeEntity.class, metadata);
    }

}

