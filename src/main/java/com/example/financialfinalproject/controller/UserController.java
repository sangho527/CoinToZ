package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.request.UserJoinRequest;
import com.example.financialfinalproject.domain.request.UserLoginRequest;
import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.domain.response.UserJoinResponse;
import com.example.financialfinalproject.domain.response.UserLoginResponse;
import com.example.financialfinalproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

//    private final AlarmService alarmService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Response<UserJoinResponse>> joinUser(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);
        return ResponseEntity.ok().body(Response.success(userJoinResponse));
    }

    @ApiOperation(value = "로그인", notes = "jwt 반환")
    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());
        return ResponseEntity.ok().body(Response.success(new UserLoginResponse(token)));
    }

//    @ApiOperation("알림 목록 조회")
//    @GetMapping("/alarm")
//    public ResponseEntity<Response<Page<AlarmDto>>> getAlarms(Authentication authentication, @PageableDefault(size = 20) @SortDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<AlarmDto> alarmDtos = alarmService.getAlarms(authentication.getName(), pageable);
//        return ResponseEntity.ok().body(Response.success(alarmDtos));
//    }

}