package faang.school.postservice.controller.post;

import faang.school.postservice.model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        POSTGRESQL_CONTAINER.start();

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testToCreatePostByUser() throws Exception {
        mockMvc.perform(post("/post/create")
                        .header("x-project-id", "1")
                        .contentType("application/json")
                        .content("{\"content\": \"content\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsersDrafts() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/post/getAllUsersDrafts")
                        .header("x-project-id", "1")
                        .header("x-user-id", "1"))
                .andExpect(status().isOk());

        ObjectMapper objectMapper = new ObjectMapper();
        List<Post> posts = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
        Assertions.assertEquals(1, posts.size());
    }
}