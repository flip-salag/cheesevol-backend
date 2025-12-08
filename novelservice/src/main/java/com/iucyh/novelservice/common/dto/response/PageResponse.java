package com.iucyh.novelservice.common.dto.response;

import java.util.List;

public record PageResponse<T>(

        long total,
        Object next,
        List<T> items
) {}
