package com.custom.validation.domain;

import com.custom.validation.domain.user.User;
import com.custom.validation.domain.user.UserRepository;
import com.custom.validation.exception.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private static String API_URL = "/api/v1/user";

    @Autowired
    TestRestTemplate template;

    @Autowired
    UserRepository userRepository;


    @Test
    void 사용자_저장_사용자이름_null() {
        User user = User.builder()
                .username(null)
                .password("1234")
                .build();

        ResponseEntity<Object> response = postApi(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 사용자_저장_비밀번호_null() {
        User user = User.builder()
                .username("박병길")
                .password(null)
                .build();

        ResponseEntity<Object> response = postApi(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 사용자_저장_짧은_이름() {
        User user = User.builder()
                .username("박병길")
                .password(null)
                .build();

        ResponseEntity<Object> response = postApi(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void API_에러() {
        User user = new User();
        ResponseEntity<ApiError> response = postApi(user, ApiError.class);
        System.out.println(response);
        assertThat(response.getBody().getUrl()).isEqualTo(API_URL);
        assertThat(response.getBody().getValidationError().get("username")).isEqualTo("널이어서는 안됩니다");
        assertThat(response.getBody().getValidationError().get("password")).isEqualTo("널이어서는 안됩니다");
    }

    private <T> ResponseEntity<T> postApi(Object request, Class<T> response) {
        return template.postForEntity(API_URL, request, response);
    }
}