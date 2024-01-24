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
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //멤버 관련
    _EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER_001", "존재하지 않는 사용자입니다."),
    _WRONG_LOGINID_PASSWORD(HttpStatus.CONFLICT, "MEMBER_002", "아이디 또는 비밀번호가 일치하지 않습니다."),
    _DUPLICATE_LOGINID(HttpStatus.CONFLICT, "MEMBER_003", "중복된 ID입니다."),

    //암장 관련
    _EMPTY_CLIMBING_GYM(HttpStatus.CONFLICT, "CLIMBING_GYM_001", "존재하지 않는 암장입니다."),
    _EMPTY_MANAGER_GYM(HttpStatus.CONFLICT, "CLIMBING_GYM_002", "관리자가 존재하지 않는 암장입니다"),
    _DUPLICATE_GYM_MANAGER(HttpStatus.CONFLICT, "CLIMBING_GYM_003", "이미 관리자가 등록된 암장입니다."),

    //벽면 관련
    _EMPTY_SECTOR(HttpStatus.CONFLICT, "SECTOR_001", "존재하지 않는 벽면입니다."),
    _DUPLICATE_SECTOR_NAME(HttpStatus.CONFLICT, "SECTOR_002", "섹터 이름이 중복됩니다."),

    //루트 관련
    _EMPTY_ROUTE(HttpStatus.CONFLICT, "ROUTE_001", "존재하지 않는 루트입니다."),
    _EMPTY_ROUTE_LIST(HttpStatus.CONFLICT, "ROUTE_002", "암장의 루트 정보를 찾을 수 없습니다."),
    _DUPLICATE_ROUTE_NAME(HttpStatus.CONFLICT, "ROUTE_004", "루트 이름이 중복됩니다."),

    //인증 관련
    _EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH_001", "JWT가 존재하지 않습니다."),
    _INVALID_JWT(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 JWT입니다."),

    _EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "AUTH_003", "JWT가 만료되었습니다."),

    //회원가입 중복 관련
    _DUPLICATE_SIGN_IN(HttpStatus.CONFLICT, "AUTH_004", "이미 가입 된 유저입니다."),


    //파일 업로드 관련
    _FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_001", "파일 업로드에 실패했습니다."),

    //운동 기록 관련
    _EMPTY_CLIMBING_RECORD(HttpStatus.CONFLICT, "CLIMBING_RECORD_001", "존재하지 않는 암장운동기록입니다."),
    _INVALID_DATE_RANGE(HttpStatus.CONFLICT, "CLIMBING_RECORD_002",
        "시점상 시작 날짜는 종료 날짜보다 같거나 앞서야 합니다."),

    //루트 기록 관련
    _EMPTY_ROUTE_RECORD(HttpStatus.CONFLICT, "ROUTE_RECORD_001", "존재하지 않는 루트운동기록입니다."),

    //숏츠 관련
    _EMPTY_SHORTS(HttpStatus.CONFLICT, "SHORTS_001", "존재하지 않는 쇼츠입니다."),

    //암장 리뷰 관련
    _CONTENT_TOO_LARGE(HttpStatus.CONFLICT, "REVIEW_001", "리뷰 최대 입력 길이를 초과했습니다."),
    _RATING_OUT_OF_RANGE(HttpStatus.CONFLICT, "REVIEW_002", "rating의 범위가 올바르지 않습니다."),
    _REVIEW_EXIST(HttpStatus.CONFLICT, "REVIEW_003", "유저가 이미 해당 암장에 대한 리뷰를 남겼습니다."),

    //유저 관련
    _EMPTY_USER(HttpStatus.CONFLICT, "USER_001", "존재하지 않는 유저입니다."),

    //숏츠 댓글 관련
    _EMPTY_SHORTS_COMMENT(HttpStatus.CONFLICT, "SHORTS_COMMENT_001" , "존재하지 않는 쇼츠 댓글입니다.");

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