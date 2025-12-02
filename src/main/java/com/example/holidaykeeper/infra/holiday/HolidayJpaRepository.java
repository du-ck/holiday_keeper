package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {

    @Modifying
    @Query("update HolidayEntity h set h.isDeleted = true , h.deletedAt = CURRENT_TIMESTAMP ")
    int updateAllIsDeletedTrue();
}
