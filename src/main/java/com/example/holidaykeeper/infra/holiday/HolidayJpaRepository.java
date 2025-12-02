package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {

    @Modifying
    @Query("update HolidayEntity h set h.isDeleted = true , h.deletedAt = CURRENT_TIMESTAMP ")
    int updateAllIsDeletedTrue();

    @Modifying
    @Query("update HolidayEntity h set h.isDeleted = true , h.deletedAt = CURRENT_TIMESTAMP " +
            "where h.id IN :ids")
    int updateIsDeletedTrue(@Param("ids") List<Long> ids);

    List<HolidayEntity> findByYearAndCountryCode(int year, String countryCode);
}
