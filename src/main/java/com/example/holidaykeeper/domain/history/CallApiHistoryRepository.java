package com.example.holidaykeeper.domain.history;

import com.example.holidaykeeper.domain.holiday.Country;

import java.util.List;
import java.util.Optional;

public interface CallApiHistoryRepository {

    Optional<CallApiHistory> save(CallApiHistory history);
}
