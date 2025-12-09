package com.iucyh.novelservice.common.response;

import java.util.List;

public record PageResponse<T>(

        long total,
        Object next,
        List<T> items
) {}
