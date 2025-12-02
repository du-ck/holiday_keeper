package com.example.holidaykeeper.infra.holiday;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHolidayEntity is a Querydsl query type for HolidayEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHolidayEntity extends EntityPathBase<HolidayEntity> {

    private static final long serialVersionUID = 1453920306L;

    public static final QHolidayEntity holidayEntity = new QHolidayEntity("holidayEntity");

    public final StringPath countryCode = createString("countryCode");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath engName = createString("engName");

    public final DatePath<java.time.LocalDate> holidayDate = createDate("holidayDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isGlobal = createBoolean("isGlobal");

    public final NumberPath<Integer> launchYear = createNumber("launchYear", Integer.class);

    public final StringPath localName = createString("localName");

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QHolidayEntity(String variable) {
        super(HolidayEntity.class, forVariable(variable));
    }

    public QHolidayEntity(Path<? extends HolidayEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHolidayEntity(PathMetadata metadata) {
        super(HolidayEntity.class, metadata);
    }

}

