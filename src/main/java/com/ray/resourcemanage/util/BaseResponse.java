package com.ray.resourcemanage.util;

import com.ray.resourcemanage.constant.ConstResponse;

public class BaseResponse {

    private Object data;

    private boolean result;

    private Integer errorCode;

    private String errorMessage;

    public BaseResponse() {
        result = true;
        errorCode = ConstResponse.NO_ERROR_CODE;
        errorMessage = ConstResponse.SUCCESS;
    }

    public BaseResponse(Object data) {
        this.data = data;
        result = true;
        errorCode = ConstResponse.NO_ERROR_CODE;
        errorMessage = ConstResponse.SUCCESS;
    }

    public BaseResponse(boolean result, Integer errorCode, String errorMessage) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
