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

    private final Map<String, Safelist> safelistMap;

    public SafelistRegistry(List<SafelistProvider> providers) {
        validate(providers);
        // 중복 키 존재 시 IllegalStateException 예외가 발생하므로 빈 등록 후에는 중복 문제에서 안전
        this.safelistMap = providers
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                SafelistProvider::getKey,
                                SafelistProvider::getSafelistPolicy
                        )
                );
    }

    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }
        return safelistMap.containsKey(key);
    }

    /**
     * <p>key에 해당하는 Safelist를 반환</p>
     * @return 매칭된 {@code Safelist}, 매칭되는 {@code Safelist}가 없다면 {@code null}
     */
    public Safelist getSafelist(String key) {
        if (key == null) {
            return null;
        }
        return safelistMap.get(key);
    }

    /**
     * <p>각 {@code SafelistProvider}들이 반환하는 key, safelist가 모두 올바른 상태인지 검증 (중복 검증은 제외)</p>
     * @param providers 주입받은 {@code SafelistProvider} 목록
     * @throws IllegalStateException 올바른 상태가 아닐 때 (key가 null이거나 비어있는 등)
     */
    private void validate(List<SafelistProvider> providers) throws IllegalStateException {
        providers.forEach(p -> {
            String key = p.getKey();
            Safelist safelist = p.getSafelistPolicy();

            boolean isInvalidKey = key == null || key.isBlank();
            boolean isInvalidSafelist = safelist == null;
            if (isInvalidKey || isInvalidSafelist) {
                throw new IllegalStateException("Key or safelist cannot be null or blank in SafelistProvider: %s".formatted(p.getClass().getName()));
            }
        });
    }
}
