package com.iucyh.novelservice.core.json.deserializer.html.registry;

import org.jsoup.safety.Safelist;

/**
 * <p>레지스트리에 등록될 각 도메인별 Safelist 정책을 정의</p>
 */
public interface SafelistProvider {

    /**
     * <p>레지스트리에 등록시 사용할 키</p>
     * <p>각 도메인이 상수로 관리하는 것을 권장</p>
     * <b>Key 이름은 꼭 {도메인}.{Entity의 필드명} 규칙으로 명명해야 합니다.</b>
     */
    String getKey();

    /**
     * <p>각 도메인별 Safelist 정책</p>
     */
    Safelist getSafelistPolicy();
}
