package com.example.holidaykeeper.infra.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSyncHistoryJpaRepository extends JpaRepository<DataSyncHistoryEntity, Long> {
}
