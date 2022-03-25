package com.volunteer.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class ManagementE2ETest extends AbstractRestTemplateTest {

  private static final String ADDRESS_CONTROLLER_TEST_CASES_FLP = "data/address-controller-e2e-tests.json";
  private static final String USERS_CONTROLLER_TEST_CASES_FLP = "data/users-controller-e2e-tests.json";
  private static final String CATEGORIES_CONTROLLER_TEST_CASES_FLP = "data/categories-controller-e2e-tests.json";
  private static final String PRODUCTS_CONTROLLER_TEST_CASES_FLP = "data/products-controller-e2e-tests.json";

//  @ParameterizedTest()
//  @MethodSource("loadAddressControllerTestCases")
//  @Order(1)
//  @DisplayName("address-controller")
//  public void addressControllerTest(final String testName, final JsonNode given,
//      final JsonNode expected) {
//    endpointTest(testName, given, expected, Collections.emptySet());
//  }

  @ParameterizedTest()
  @MethodSource("loadUsersControllerTestCases")
  @Order(2)
  @DisplayName("users-controller")
  public void usersControllerTest(final String testName, final JsonNode given,
      final JsonNode expected) {
    endpointTest(testName, given, expected, Set.of("userVerifiedAt", "lockedAt"));
  }

  @ParameterizedTest()
  @MethodSource("loadCategoriesControllerTestCases")
  @Order(3)
  @DisplayName("categories-controller")
  public void categoriesControllerTest(final String testName, final JsonNode given,
      final JsonNode expected) {
    endpointTest(testName, given, expected, Collections.emptySet());
  }

  @ParameterizedTest()
  @MethodSource("loadProductsControllerTestCases")
  @Order(3)
  @DisplayName("products-controller")
  public void productsControllerTest(final String testName, final JsonNode given,
      final JsonNode expected) {
    endpointTest(testName, given, expected, Collections.emptySet());
  }

  private static Stream<Arguments> loadAddressControllerTestCases() {
    return loadTestCases(ADDRESS_CONTROLLER_TEST_CASES_FLP);
  }

  private static Stream<Arguments> loadUsersControllerTestCases() {
    return loadTestCases(USERS_CONTROLLER_TEST_CASES_FLP);
  }

  private static Stream<Arguments> loadCategoriesControllerTestCases() {
    return loadTestCases(CATEGORIES_CONTROLLER_TEST_CASES_FLP);
  }

  private static Stream<Arguments> loadProductsControllerTestCases() {
    return loadTestCases(PRODUCTS_CONTROLLER_TEST_CASES_FLP);
  }

}