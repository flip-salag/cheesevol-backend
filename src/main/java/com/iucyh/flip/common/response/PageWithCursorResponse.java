package com.iucyh.flip.common.response;

import java.util.List;

public record PageWithCursorResponse<T>(

        int size,
        Object next,
        List<T> items
) {}
