package com.example.holidaykeeper.infra.history;

import com.example.holidaykeeper.domain.history.CallApiHistory;
import com.example.holidaykeeper.domain.history.CallApiHistoryRepository;
import com.example.holidaykeeper.domain.history.DataSyncHistory;
import com.example.holidaykeeper.domain.history.DataSyncHistoryRepository;
import com.example.holidaykeeper.infra.holiday.CountyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DataSyncHistoryRepositoryImpl implements DataSyncHistoryRepository {

    private final DataSyncHistoryJpaRepository jpaRepository;

    @Override
    public Optional<DataSyncHistory> save(DataSyncHistory history) {
        Optional<DataSyncHistoryEntity> result = Optional.of(jpaRepository.save(DataSyncHistoryEntity.toEntity(history)));
        if (result.isPresent()) {
            return Optional.of(DataSyncHistoryEntity.toDomain(result.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<DataSyncHistory> saveAll(List<DataSyncHistory> histories) {
        List<DataSyncHistoryEntity> savedEntities = jpaRepository.saveAll(DataSyncHistoryEntity.toEntityList(histories));
        return DataSyncHistoryEntity.toDomainList(savedEntities);
    }
}
