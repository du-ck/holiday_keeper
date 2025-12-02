package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountyJpaRepository extends JpaRepository<CountyEntity, Long> {
    List<CountyEntity> findByHolidayIdIn(List<Long> holidayIds);

    @Modifying
    @Query("update CountyEntity c set c.isDeleted = true , c.deletedAt = CURRENT_TIMESTAMP ")
    int updateAllIsDeletedTrue();

    @Modifying
    @Query("update CountyEntity c set c.isDeleted = true , c.deletedAt = CURRENT_TIMESTAMP " +
            "where c.id IN :ids")
    int updateIsDeletedTrue(@Param("ids") List<Long> ids);
}
