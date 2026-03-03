package com.iucyh.novelservice.core.json.deserializer.html.registry;

import jakarta.annotation.PostConstruct;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>도메인별 Safelist 정책을 조회할 수 있는 Registry</p>
 */
@Component
public class SafelistRegistry {

    private final Map<String, SafelistProvider> safelistProviderMap;

    public SafelistRegistry(List<SafelistProvider> providers) {
        // 중복 키 존재 시 IllegalStateException 예외가 발생하므로 빈 등록 후에는 중복 문제에서 안전
        this.safelistProviderMap = providers
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                SafelistProvider::getKey,
                                Function.identity()
                        )
                );
    }

    @PostConstruct
    private void validate() { // 등록된 provider들과 key가 전부 올바른 상태인지 검증
        safelistProviderMap.forEach((key, provider) -> {
            boolean isInvalidKey = key == null || key.isBlank();
            boolean isInvalidSafelist = provider.getSafelistPolicy() == null;

            if (isInvalidKey || isInvalidSafelist) {
                throw new IllegalStateException("Key or safelist cannot be null or blank in SafelistProvider: %s".formatted(provider.getClass().getName()));
            }
        });
    }

    public boolean containsKey(String key) {
        return safelistProviderMap.containsKey(key);
    }

    /**
     * <p>key에 해당하는 Safelist를 반환</p>
     * @return 매칭된 {@code Safelist}, 매칭되는 {@code Safelist}가 없다면 {@code null}
     */
    public Safelist getSafelist(String key) {
        return safelistProviderMap.get(key).getSafelistPolicy();
    }
}
