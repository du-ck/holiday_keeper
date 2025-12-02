package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.domain.holiday.HolidayDetail;
import com.example.holidaykeeper.domain.holiday.HolidayRepository;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Q-Type Import (QueryDSL이 생성한 Q 클래스)
import static com.example.holidaykeeper.infra.holiday.QHolidayEntity.holidayEntity;
import static com.example.holidaykeeper.infra.holiday.QCountryEntity.countryEntity;
import static com.example.holidaykeeper.infra.holiday.QCountyEntity.countyEntity;
import static com.example.holidaykeeper.infra.holiday.QHolidayTypeEntity.holidayTypeEntity;

@Repository
@RequiredArgsConstructor
public class HolidayRepositoryImpl implements HolidayRepository {

    private final HolidayJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Holiday> saveAll(List<Holiday> holidays) {
        List<HolidayEntity> savedEntities = jpaRepository.saveAll(HolidayEntity.toEntityList(holidays));
        return HolidayEntity.toDomainList(savedEntities);
    }

    @Override
    public Page<HolidayDetail> searchHolidays(SearchHolidayDomain.Request req, Pageable pageable) {
        // 동적 조건 생성 (BooleanBuilder)
        BooleanBuilder builder = new BooleanBuilder();

        // 기본 조건: is_deleted = false
        builder.and(holidayEntity.isDeleted.isFalse());

        // 동적 검색 조건
        builder.and(yearEq(req.getYear()));
        builder.and(monthEq(req.getMonth()));
        builder.and(countryCodeEq(req.getCountryCode()));
        builder.and(dateBetween(req.getFromDate(), req.getToDate()));

        // types 조건은 서브쿼리로 처리
        builder.and(hasAllTypes(req.getTypes()));

        // JPAQuery의 타입을 DTO로 변경
        JPAQuery<HolidayDetail> contentQuery = queryFactory
                // select(Projections.constructor)를 사용하여 DTO로 Projection
                // DTO 생성자의 인자 순서와 Q-클래스 필드 나열 순서 똑같이해야함.
                .select(Projections.constructor(HolidayDetail.class,
                        holidayEntity.id,
                        holidayEntity.localName,
                        holidayEntity.engName,
                        holidayEntity.holidayDate,
                        holidayEntity.year,
                        holidayEntity.month,
                        holidayEntity.countryCode,
                        holidayEntity.isGlobal,
                        holidayEntity.launchYear,
                        countryEntity.name.as("countryName"),
                        holidayEntity.createdAt,
                        holidayEntity.updatedAt,
                        holidayEntity.deletedAt,
                        holidayEntity.isDeleted
                ))
                .from(holidayEntity) // from 절은 그대로 유지
                .leftJoin(countryEntity).on(holidayEntity.countryCode.eq(countryEntity.code))
                .leftJoin(countyEntity).on(holidayEntity.id.eq(countyEntity.holidayId))
                .leftJoin(holidayTypeEntity).on(holidayEntity.id.eq(holidayTypeEntity.holidayId))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct();

        // 데이터 조회 및 페이징 객체 생성
        List<HolidayDetail> content = contentQuery.fetch();

        // Count 쿼리 (페이징을 위해 전체 개수 필요)
        long totalCount = queryFactory
                .selectFrom(holidayEntity)
                .leftJoin(countryEntity).on(holidayEntity.countryCode.eq(countryEntity.code))
                .leftJoin(countyEntity).on(holidayEntity.id.eq(countyEntity.holidayId))
                .leftJoin(holidayTypeEntity).on(holidayEntity.id.eq(holidayTypeEntity.holidayId))
                .where(builder)
                .distinct() // Count 쿼리에도 DISTINCT 적용
                .fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
    }

    /**
     * 대량이라 JPQL로 처리
     */
    @Override
    public boolean deleteAll() {
        jpaRepository.updateAllIsDeletedTrue();
        entityManager.clear();
        return true;
    }

    @Override
    public int delete(List<Long> ids) {
        int result = jpaRepository.updateIsDeletedTrue(ids);
        return result;
    }

    @Override
    public List<Holiday> searchHolidayIds(int year, String countryCode) {
        return HolidayEntity.toDomainList(jpaRepository.findByYearAndCountryCode(year, countryCode));
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ?  holidayEntity.year.eq(year) : null;
    }

    private BooleanExpression globalEq(boolean isGlobal) {
        return holidayEntity.isGlobal.eq(isGlobal);
    }

    private BooleanExpression monthEq(Integer month) {
        return month != null ? holidayEntity.month.eq(month) : null;
    }

    private BooleanExpression countryCodeEq(String countryCode) {
        return countryCode != null ? holidayEntity.countryCode.eq(countryCode) : null;
    }

    private BooleanExpression dateBetween(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            return holidayEntity.holidayDate.between(fromDate, toDate);
        }
        if (fromDate != null) {
            return holidayEntity.holidayDate.goe(fromDate);
        }
        if (toDate != null) {
            return holidayEntity.holidayDate.loe(toDate);
        }
        return null;
    }

    /**
     *  조건의 타입을 가진 Holiday만 조회 (AND 조건)
     *
     * 예시: types = [PUBLIC, BANK]인 경우
     * - holiday_type 테이블에서 같은 holiday_id로
     * - PUBLIC row 1개 + BANK row 1개 = 총 2개의 row를 가진 holiday만 조회
     */
    private BooleanExpression hasAllTypes(List<HolidayTypeEnum> types) {
        if (types == null || types.isEmpty()) {
            return null;
        }

        // 서브쿼리: 요청된 모든 타입을 가진 holiday_id만 반환
        // holiday_type 테이블의 별칭을 subHolidayType으로 생성
        QHolidayTypeEntity subHolidayType = new QHolidayTypeEntity("subHolidayType");

        return holidayEntity.id.in(
                JPAExpressions
                        .select(subHolidayType.holidayId)
                        .from(subHolidayType)
                        .where(subHolidayType.type.in(types))
                        .groupBy(subHolidayType.holidayId)
                        .having(subHolidayType.type.countDistinct().eq((long) types.size()))
        );
    }
}
