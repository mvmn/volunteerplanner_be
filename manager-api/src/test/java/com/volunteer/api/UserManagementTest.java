package com.volunteer.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.UserService;
import com.volunteer.api.utils.JsonTestUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class UserManagementTest {

  private static final ParameterizedTypeReference<JsonNode> JSON_NODE_TYPE_REFERENCE =
      new ParameterizedTypeReference<>() {
      };

  private static final String TEST_CASES_FILE_PATH = "data/user-mgmt-e2e-tests.json";
  private static final Pattern URI_USERNAME_PATTERN = Pattern.compile("\\{(.*?)\\}");

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserService userService;

  @ParameterizedTest()
  @MethodSource("loadClassPassTestCases")
  public void endpointTest(final String testName, final JsonNode given, final JsonNode expected) {
    final ResponseEntity<JsonNode> actual = executeRequest(given);

    assertEquals(expected.get("status").asInt(), actual.getStatusCodeValue(),
        String.format("[%s] response status code", testName));

    final JsonNode expectedBody = expected.path("body");
    if (JsonTestUtils.isNull(expectedBody)) {
      assertNull(actual.getBody(), String.format("[%s] response body", testName));
    } else {
      Assertions.assertTrue(JsonTestUtils.equals(expectedBody, actual.getBody(),
              Set.of("id", "userVerifiedByUserId", "userVerifiedAt", "lockedByUserId", "lockedAt")),
          String.format("[%s] response body. \nExpected: '%s' \nActual:   '%s'", testName,
              JsonTestUtils.toJsonString(expectedBody),
              JsonTestUtils.toJsonString(actual.getBody())));
    }
  }

  private ResponseEntity<JsonNode> executeRequest(final JsonNode given) {
    final Optional<String> token = authenticate(given.path("auth"));

    final JsonNode request = given.get("request");
    return executeRequest(parseEndpointUri(request.get("endpoint").asText()),
        request.get("method").asText(), token, request.path("body"));
  }

  private String parseEndpointUri(final String source) {
    final StringBuilder result = new StringBuilder();
    final Matcher matcher = URI_USERNAME_PATTERN.matcher(source);

    int lastIndex = 0;
    while (matcher.find()) {
      final String userName = matcher.group(1);
      final VPUser user = userService.get(userName)
          .orElseThrow(() -> new RuntimeException(String.format(
              "'%s' username is missed", userName)));

      result.append(source, lastIndex, matcher.start()).append(user.getId());
      lastIndex = matcher.end();
    }

    if (lastIndex < source.length()) {
      result.append(source, lastIndex, source.length());
    }

    return result.toString();
  }

  private Optional<String> authenticate(final JsonNode request) {
    if (JsonTestUtils.isNull(request)) {
      return Optional.empty();
    }

    final ResponseEntity<JsonNode> response = executeRequest("/authenticate",
        HttpMethod.POST.name(), Optional.empty(), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    return Optional.of(response.getBody().path("accessToken").textValue());
  }

  private ResponseEntity<JsonNode> executeRequest(final String endpointUri, final String method,
      final Optional<String> token, final JsonNode body) {

    return restTemplate.exchange(endpointUri, HttpMethod.valueOf(method),
        createHttpEntity(token, body), JSON_NODE_TYPE_REFERENCE);
  }

  private HttpEntity<JsonNode> createHttpEntity(final Optional<String> token, final JsonNode body) {
    final HttpHeaders headers = new HttpHeaders();
    token.ifPresent(value -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + value));

    if (Objects.isNull(body) || body.isMissingNode()) {
      return new HttpEntity<>(null, headers);
    } else {
      headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
      return new HttpEntity<>(body, headers);
    }
  }

  private static Stream<Arguments> loadClassPassTestCases() {
    final List<JsonNode> testCases = JsonTestUtils.loadClassPathJson(TEST_CASES_FILE_PATH,
        new TypeReference<>() {
        });

    return testCases.stream()
        .map(testCase -> Arguments.of(
            testCase.path("name").asText(),
            testCase.path("given"),
            testCase.path("expected"))
        );
  }

}