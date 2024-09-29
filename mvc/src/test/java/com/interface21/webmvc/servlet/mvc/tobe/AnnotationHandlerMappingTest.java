package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("해당 uri와 method get에 매핑되는 핸들러를 찾아 실행한다.")
    @Test
    void should_handleHandler_when_getRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("해당 uri와 method post에 매핑되는 핸들러를 찾아 실행한다.")
    @Test
    void should_handleHandler_when_postRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("해당 uri와 method에 매핑되는 핸들러가 없는 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_givenInvalidRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 요청에 대응하는 핸들러가 없습니다: POST /invalid");
    }

    @DisplayName("핸들러에 http method가 선언되지 않은 경우 모든 method가 매핑되도록 한다.")
    @ParameterizedTest
    @EnumSource(value = RequestMethod.class)
    void should_mapAllMethods_when_declaredMethodEmpty(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/all-test");
        when(request.getMethod()).thenReturn(method.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
