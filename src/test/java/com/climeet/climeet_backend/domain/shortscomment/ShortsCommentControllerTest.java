package com.climeet.climeet_backend.domain.shortscomment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class ShortsCommentControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    void beforeAll() {
        //추후 Get, Patch 테스트에 사용
    }

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
    }

    @AfterEach
    public void AfterEach() {
        //추후 Get, Patch 테스트에 사용
    }

    @DisplayName("숏츠 댓글 생성에 성공한다")
    @Test
    void createShortsComment() throws Exception {
        //given
        Long shortsId = 1L;
        Long shortsCommentId = 1L;

        CreateShortsCommentRequest createShortsCommentRequest = new CreateShortsCommentRequest();
        Field field = createShortsCommentRequest.getClass().getDeclaredField("content");
        field.setAccessible(true);
        field.set(createShortsCommentRequest, "댓글 내용");

        String content = objectMapper.writeValueAsString(createShortsCommentRequest);
        String url = "/shorts/" + shortsId + "/shortsComments";

        //when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .param("parentCommentId",
                    shortsCommentId != null ? shortsCommentId.toString() : null));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("댓글 작성에 성공했습니다."));
    }
}