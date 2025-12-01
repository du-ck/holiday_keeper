package com.example.holidaykeeper.infra.history;

import com.example.holidaykeeper.domain.history.CallApiHistory;
import com.example.holidaykeeper.domain.history.CallApiHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CallApiHistoryRepositoryImpl implements CallApiHistoryRepository {

    private final CallApiHistoryJpaRepository jpaRepository;


    @Override
    public Optional<CallApiHistory> save(CallApiHistory history) {
        Optional<CallApiHistoryEntity> result = Optional.of(jpaRepository.save(CallApiHistoryEntity.toEntity(history)));
        if (result.isPresent()) {
            return Optional.of(CallApiHistoryEntity.toDomain(result.get()));
        }
        return Optional.empty();
    }
}
