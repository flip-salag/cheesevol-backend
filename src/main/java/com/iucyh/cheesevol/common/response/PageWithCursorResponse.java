package com.iucyh.cheesevol.common.response;

import java.util.List;

public record PageWithCursorResponse<T>(

        int size,
        Object next,
        List<T> items
) {}
