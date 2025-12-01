package com.example.holidaykeeper.domain.history;

import java.util.List;
import java.util.Optional;

public interface DataSyncHistoryRepository {

    Optional<DataSyncHistory> save(DataSyncHistory history);
    List<DataSyncHistory> saveAll(List<DataSyncHistory> histories);
}
