package com.climeet.climeet_backend.global.response.code.status;


import com.climeet.climeet_backend.global.response.code.BaseErrorCode;
import com.climeet.climeet_backend.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //멤버 관련
    _EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER_001", "존재하지 않는 사용자입니다."),

    //암장 관련
    _EMPTY_CLIMBING_GYM(HttpStatus.CONFLICT, "CLIMBING_GYM_001", "존재하지 않는 암장입니다."),

    //벽면 관련
    _EMPTY_SECTOR(HttpStatus.CONFLICT, "SECTOR_001", "존재하지 않는 벽면입니다."),
    _GYM_ID_MISMATCH(HttpStatus.CONFLICT, "SECTOR_002", "벽면과 암장 정보가 일치하지 않습니다."),

    //루트 관련
    _EMPTY_ROUTE(HttpStatus.CONFLICT, "ROUTE_001", "존재하지 않는 루트입니다."),
    _EMPTY_ROUTE_LIST(HttpStatus.CONFLICT, "Route_002", "암장의 루트 정보를 찾을 수 없습니다."),

    //인증 관련
    _EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH_001", "JWT가 존재하지 않습니다."),
    _INVALID_JWT(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 JWT입니다."),

    //파일 업로드 관련
    _FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_001", "파일 업로드에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}