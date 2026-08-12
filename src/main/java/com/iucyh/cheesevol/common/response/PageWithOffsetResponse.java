package com.iucyh.cheesevol.common.response;

import java.util.List;

public record PageWithOffsetResponse<T>(

        int page,
        int size,
        int totalPages,
        long totalCount,
        List<T> items
) {}
