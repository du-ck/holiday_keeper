package com.example.holidaykeeper.domain.history;

import com.example.holidaykeeper.domain.holiday.Holiday;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    private final DataSyncHistoryRepository dataSyncHistoryRepository;

    public List<DataSyncHistory> saveSyncHistories(List<DataSyncHistory> histories) {
        return dataSyncHistoryRepository.saveAll(histories);
    }


}
