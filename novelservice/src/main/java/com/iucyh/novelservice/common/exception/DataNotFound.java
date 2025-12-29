package com.iucyh.novelservice.common.exception;

import com.iucyh.novelservice.common.exception.errorcode.CommonErrorCode;

import java.util.Map;

public class DataNotFound extends ServiceException {

    /**
     * @param publicId 해당 리소스를 조회했을 당시 사용한 식별자
     */
    public DataNotFound(String publicId) {
        super(
                CommonErrorCode.DATA_NOT_FOUND,
                Map.of("id", publicId)
        );
    }

    /**
     * <p>식별자를 노출하고 싶지 않을 때 사용</p>
     */
    public DataNotFound() {
        super(
                CommonErrorCode.DATA_NOT_FOUND,
                "Resource not found"
        );
    }
}
