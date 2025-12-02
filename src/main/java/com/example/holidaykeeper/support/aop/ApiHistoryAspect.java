package com.example.holidaykeeper.support.aop;

import com.example.holidaykeeper.domain.history.CallApiHistory;
import com.example.holidaykeeper.domain.history.CallApiHistoryRepository;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import com.example.holidaykeeper.support.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiHistoryAspect {

    private final CallApiHistoryRepository apiHistoryRepository;
    private final ObjectMapper objectMapper;

    // 별도 서비스로 분리
    @Service
    @RequiredArgsConstructor
    public static class ApiHistoryService {

        private final CallApiHistoryRepository repository;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void saveHistory(CallApiHistory history) {
            repository.save(history);
        }
    }

    private final ApiHistoryService apiHistoryService;

    // 모든 @RestController 클래스의 public 메서드
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    // @Around 어드바이스: 컨트롤러 메서드의 실행 전후를 감싸서 로깅
    @Around("controllerMethods()")
    public Object logIncomingApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 요청 정보 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        CallApiHistory.CallApiHistoryBuilder historyBuilder = CallApiHistory.builder()
                .endpoint(request.getRequestURI())
                .httpMethod(request.getMethod());

        // 요청 매개변수 JSON 문자열로 변환
        try {
            // Controller 메서드의 인수
            String requestParamsJson = objectMapper.writeValueAsString(joinPoint.getArgs());
            historyBuilder.requestParams(requestParamsJson);
        } catch (Exception e) {
            historyBuilder.requestParams("Json으로 변환 실패 : " + e.getMessage());
        }

        Object result = null;
        Throwable exception = null;

        try {
            // 컨트롤러 메서드 실행
            result = joinPoint.proceed();

            historyBuilder.isSuccess(true)
                    .responseStatus(200);

            // 응답 본문 처리 (500 제한)
            String responseBody = objectMapper.writeValueAsString(result);
            historyBuilder.responseBody(truncateBody(responseBody, 500)); // 500자 제한 예시

        } catch (Exception e) {
            exception = e;

            int status = 500;
            String errorMsg = e.getMessage();

            if (exception instanceof ResourceNotFoundException) {
                status = 404;
                errorMsg = "해당하는 데이터가 없습니다.";
            } else if (exception instanceof ApiCallFailedException) {
                status = 503;
            } else if (exception instanceof NoResourceFoundException) {
                status = 404;
                errorMsg = "요청한 경로가 존재하지 않습니다.";
            } else if (exception instanceof DataIntegrityViolationException) {
                status = 400;
                errorMsg = "데이터 제약 조건을 위반했습니다. 이미 존재하는 값이거나 형식이 올바르지 않습니다.";
            } else if (exception instanceof MethodArgumentNotValidException) {
                status = 400;
                errorMsg = "요청 정보가 올바르지 않습니다.";
            }

            historyBuilder.isSuccess(false)
                    .responseStatus(status)
                    .errorMessage(truncateBody(errorMsg, 255));

            throw e; // 예외를 다시 던져서 @ControllerAdvice로 흐르게 함

        } finally {
            // 최종 히스토리 저장
            if (historyBuilder != null) {
                historyBuilder.responseTimeMs((int) (System.currentTimeMillis() - startTime));
                //새로운 트랜잭션으로 저장 (부모 트랜잭션과 독립)
                try {
                    apiHistoryService.saveHistory(historyBuilder.build());
                } catch (Exception e) {
                    log.error("Api History 저장 실패", e);
                }
            }
        }

        return result;
    }

    private String truncateBody(String body, int maxLength) {
        if (body == null) return null;
        return body.length() > maxLength ? body.substring(0, maxLength) + "..." : body;
    }
}
