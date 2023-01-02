package org.egov.ifix.exception;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvise {

    /**
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<?> exceptionHandler(HttpServletRequest request, Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        List<Error> errorList = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            if (exception instanceof BindException) {
                BindException bindException = (BindException) exception;
                errorResponse.setErrors(getBindingErrors(bindException.getBindingResult()));
            } else if (exception instanceof HttpCustomException) {
                HttpCustomException customException = (HttpCustomException) exception;
                Map<String, String> errorMap = customException.getErrors();

                if (errorMap != null && !errorMap.isEmpty()) {
                    for (Map.Entry entry : errorMap.entrySet()) {
                        Error error = new Error();
                        error.setCode((String) entry.getKey());
                        error.setMessage((String) entry.getValue());
                        errorList.add(error);
                    }
                } else {
                    Error error = new Error();
                    error.setCode(customException.getCode());
                    error.setMessage(customException.getMessage());
                    error.setDescription(customException.getDescription());
                    errorList.add(error);
                }
                errorResponse.setErrors(errorList);
                httpStatus = customException.getHttpStatus();
            }else {
                errorResponse.setErrors(new ArrayList(Collections.singletonList(
                        new Error(HttpStatus.INTERNAL_SERVER_ERROR.name(), exception.getMessage(),
                                exception.getLocalizedMessage()))));
            }
        } catch (Exception e) {
            errorResponse.setErrors(new ArrayList(Collections.singletonList(
                    new Error(HttpStatus.INTERNAL_SERVER_ERROR.name(), "An unhandled exception occurred",
                            "Exception occur while binding exceptions"))));
        }

        return new ResponseEntity(errorResponse, httpStatus);
    }


    /**
     * @param bindingResult
     * @return
     */
    private List<Error> getBindingErrors(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<>();
        List<ObjectError> objectErrors = bindingResult.getAllErrors();

        for (ObjectError objectError : objectErrors) {
            Error error = new Error();
            String[] codes = objectError.getCodes();
            error.setCode(codes[0]);
            error.setMessage(objectError.getDefaultMessage());
            errors.add(error);
        }
        return errors;
    }
}
