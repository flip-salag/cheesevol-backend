package com.iucyh.cheesevol.common.exception;

import com.iucyh.cheesevol.base.exception.BusinessException;
import com.iucyh.cheesevol.common.exception.errorcode.GeneralErrorCode;

public class DataNotFound extends BusinessException {

    /**
     * @param id 해당 리소스를 조회했을 당시 사용한 식별자
     */
    public DataNotFound(String id) {
        super(GeneralErrorCode.DATA_NOT_FOUND);
        addToCauses("id", id);
    }

    /**
     * <p>식별자를 노출하고 싶지 않을 때 사용</p>
     */
    public DataNotFound() {
        super(GeneralErrorCode.DATA_NOT_FOUND);
    }
}
