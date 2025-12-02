package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HolidayTypeJpaRepository extends JpaRepository<HolidayTypeEntity, Long> {
    List<HolidayTypeEntity> findByHolidayIdIn(List<Long> holidayIds);

    @Modifying
    @Query("update HolidayTypeEntity ht set ht.isDeleted = true , ht.deletedAt = CURRENT_TIMESTAMP ")
    int updateAllIsDeletedTrue();
}
