package com.custom.validation.domain;

import com.custom.validation.domain.user.User;
import com.custom.validation.domain.user.UserRepository;
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

    private <T> ResponseEntity<T> postApi(Object request, Class<T> response) {
        return template.postForEntity("/api/v1/user", request, response);
    }
}