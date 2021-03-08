// <Client>
// * 로그인을 통해 얻어진 토큰을 얻는다.
// * 그리고 얻은 토큰을 가지고 인가를 받는다.
// Authentication -> 로그인 (인증)
// Authorization <- Token (인가)
//
// <Server>
// * 로그인할때도 서버는 인증을 해야하고
// * 다시 토큰을 들고 왔을때도 인증을 해야 한다.
// Authentication = 로그인 (인증)
// Token -> Authentication (인증)
// User -> Role -> Authorization (인가)

package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private AuthenticationService authenticationService;

    public AuthenticationInterceptor(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        return filterWithPathAndMethod(request) ||
                doAuthentication(request, response);
    }

    private boolean filterWithPathAndMethod(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!path.equals("/products")) {
            return true;
        }

        if (method.equals("get")) {
            return true;
        }

        return false;
    }

    private boolean doAuthentication(HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }


        String accessToken = authorization.substring("Bearer ".length());
        authenticationService.parseToken(accessToken);
        return false;
    }
}
