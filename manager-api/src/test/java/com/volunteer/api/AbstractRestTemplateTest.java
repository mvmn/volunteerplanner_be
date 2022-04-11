package com.volunteer.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.volunteer.api.AbstractRestTemplateTest.TestConfig;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.utils.JsonTestUtils;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
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
@Import(TestConfig.class)
public class AbstractRestTemplateTest extends AbstractTestWithPersistence {

  private static final ParameterizedTypeReference<JsonNode> JSON_NODE_TYPE_REFERENCE =
      new ParameterizedTypeReference<>() {
      };

  @TestConfiguration
  public static class TestConfig {

    public static final String VERIFICATION_CODE = "123456";

    @Bean
    @Primary
    public VerificationCodeGenerator verificationCodeGenerator() {
      return () -> VERIFICATION_CODE;
    }

  }

  @Autowired
  protected TestRestTemplate restTemplate;

  public void endpointTest(final String testName, final JsonNode given, final JsonNode expected,
      final Set<String> assertIgnoreProperties) {
    final ResponseEntity<JsonNode> actual = executeRequest(given);

    assertEquals(expected.get("status").asInt(), actual.getStatusCodeValue(),
        String.format("[%s] response status code. Body: '%s'", testName,
            JsonTestUtils.toJsonString(actual.getBody())));

    final JsonNode expectedBody = expected.path("body");
    if (JsonTestUtils.isNull(expectedBody)) {
      assertNull(actual.getBody(), String.format("[%s] response body", testName));
    } else {
      Assertions.assertTrue(JsonTestUtils.equals(
              expectedBody, actual.getBody(), assertIgnoreProperties),
          String.format("[%s] response body. \nExpected: '%s' \nActual:   '%s'", testName,
              JsonTestUtils.toJsonString(expectedBody),
              JsonTestUtils.toJsonString(actual.getBody())));
    }
  }

  private ResponseEntity<JsonNode> executeRequest(final JsonNode given) {
    final Optional<String> token = authenticate(given.path("auth"));

    final JsonNode request = given.get("request");
    return executeRequest(request.get("endpoint").asText(),
        request.get("method").asText(), token, request.path("body"));
  }

  private Optional<String> authenticate(final JsonNode request) {
    if (JsonTestUtils.isNull(request)) {
      return Optional.empty();
    }

    final ResponseEntity<JsonNode> response = executeRequest("/api/v1/authenticate",
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

    if (JsonTestUtils.isNull(body)) {
      return new HttpEntity<>(null, headers);
    }

    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    return new HttpEntity<>(body, headers);
  }

  public static Stream<Arguments> loadTestCases(final String resourceFilePath) {
    final List<JsonNode> testCases = JsonTestUtils.loadClassPathJson(resourceFilePath,
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
