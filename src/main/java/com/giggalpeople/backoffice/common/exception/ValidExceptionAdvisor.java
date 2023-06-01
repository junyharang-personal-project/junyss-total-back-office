package com.giggalpeople.backoffice.common.exception;

import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_SUGGEST;

@Slf4j
@ControllerAdvice
@RestController
public class ValidExceptionAdvisor {

    /**
     * <b>유효성 검사에 실패할 경우 MethodArgumentNotValidException이 발동하게 되는데, 이를 Custom하게 처리하기 위한 Method</b>
     * @param exception 유효성 검사 실패 시 발동되는 Exception
     * @return ResponseEntity<DefaultResponse<String>>
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse<String>> processValidationException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder stringBuffer = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuffer.append("[");
            stringBuffer.append(fieldError.getField());
            stringBuffer.append("](은)는 ");
            stringBuffer.append(fieldError.getDefaultMessage()).append(".");
            stringBuffer.append(" 입력된 값 : [");
            stringBuffer.append(fieldError.getRejectedValue());
            stringBuffer.append("] 입니다.");
        }

        log.error("Request Value Validation failure 요청 값에 대한 유효성 검사 실패 하였습니다.");
        return new ResponseEntity<>(DefaultResponse.error(NOT_EXIST_SUGGEST, NOT_EXIST_SUGGEST.getMessage(), stringBuffer.toString()), HttpStatus.BAD_REQUEST);
    }
}
