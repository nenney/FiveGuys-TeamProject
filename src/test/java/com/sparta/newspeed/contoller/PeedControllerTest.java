package com.sparta.newspeed.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newspeed.controller.CommentController;
import com.sparta.newspeed.controller.PeedController;
import com.sparta.newspeed.dto.PeedRequestDto;
import com.sparta.newspeed.dto.PeedResponseDto;
import com.sparta.newspeed.entity.Peed;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.security.WebSecurityConfig;
import com.sparta.newspeed.service.CommentService;
import com.sparta.newspeed.service.PeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {PeedController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

public class PeedControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @MockBean
    PeedService peedService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreatePeed() throws Exception {
        // given
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Test content");
        PeedResponseDto responseDto = new PeedResponseDto(new Peed(requestDto));

        // peedService.createPeed 메서드가 호출될 때 responseDto를 반환하도록 설정
        when(peedService.createPeed(any(), (UserDetailsImpl) any(Principal.class))).thenReturn(responseDto);

        // when
        // POST /api/peed 엔드포인트에 requestDto를 JSON으로 전송
        mvc.perform(post("/api/peed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk()) // HTTP 상태코드 200 OK를 기대
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto))); // responseDto와 일치하는 JSON 응답을 기대
    }

    @Test
    public void testGetPeeds() throws Exception {
        // given
        PeedResponseDto responseDto = new PeedResponseDto(new Peed(new PeedRequestDto("testuser", "Test content")));
        Page<PeedResponseDto> pagedResponseDto = new PageImpl<>(Collections.singletonList(responseDto), PageRequest.of(0, 1), 1);

        // peedService.getAllPeeds 메서드가 호출될 때 pagedResponseDto를 반환하도록 설정
        when(peedService.getAllPeeds(any(Integer.class), any(Integer.class), any(String.class), any(Boolean.class))).thenReturn(pagedResponseDto);

        // when
        // GET /api/peed 엔드포인트에 쿼리 파라미터를 사용하여 요청을 전송
        mvc.perform(get("/api/peed")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sortBy", "id")
                        .param("isAsc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk()) // HTTP 상태코드 200 OK를 기대
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponseDto))); // pagedResponseDto와 일치하는 JSON 응답을 기대
    }

    @Test
    public void testCreatePeedWithInvalidData() throws Exception {
        // given
        PeedRequestDto requestDto = new PeedRequestDto("", ""); // 잘못된 데이터

        // when
        // POST /api/peed 엔드포인트에 requestDto를 JSON으로 전송
        mvc.perform(post("/api/peed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isBadRequest()); // HTTP 상태코드 400 Bad Request를 기대
    }

}