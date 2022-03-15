package com.volunteer.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;

public final class JsonTestUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static ObjectMapper getObjectMapper() {
    return JsonTestUtils.MAPPER;
  }

  public static String toJsonString(final Object data) {
    try {
      return MAPPER.writeValueAsString(data);
    } catch (final IOException ex) {
      throw new RuntimeException(
          String.format("while converting '%s' to JSON String", data), ex);
    }
  }

  public static JsonNode loadClassPathJson(final String path) {
    try (final InputStream input = new ClassPathResource(path).getInputStream()) {
      return MAPPER.readValue(input, JsonNode.class);
    } catch (final Exception exception) {
      throw new RuntimeException(String.format("Failed to load json from resource '%s'", path));
    }
  }

  public static <T> T loadClassPathJson(final String path, final TypeReference<T> typeReference) {
    try (final InputStream input = new ClassPathResource(path).getInputStream()) {
      return MAPPER.readValue(input, typeReference);
    } catch (final Exception exception) {
      throw new RuntimeException(String.format("Failed to load json from resource '%s'", path));
    }
  }

  public static boolean isNull(final JsonNode node) {
    return Objects.isNull(node) || node.isNull() || node.isMissingNode();
  }

  public static boolean equals(final JsonNode first, final JsonNode second,
      final Set<String> ignoreProperties) {
    if (first == second) {
      return true;
    }

    if (Objects.isNull(first)) {
      return false;
    }

    if (first.getClass() != second.getClass()) {
      return false;
    }

    // value node comparison
    if (first.isValueNode()) {
      return first.equals(second);
    }

    if (first.size() != second.size()) {
      return false;
    }

    // array node comparison
    if (first.isArray()) {
      for (int index = 0; index < first.size(); index++) {
        final JsonNode firstValue = first.get(index);
        final JsonNode secondValue = second.get(index);

        if (!JsonTestUtils.equals(firstValue, secondValue, ignoreProperties)) {
          return false;
        }
      }

      return true;
    }

    // object node comparison
    final Iterator<Entry<String, JsonNode>> children = first.fields();
    while (children.hasNext()) {
      final Entry<String, JsonNode> entry = children.next();

      final String key = entry.getKey();
      if (ignoreProperties.contains(key)) {
        continue;
      }

      final JsonNode firstValue = entry.getValue();
      final JsonNode secondValue = second.get(key);
      if (secondValue == null || !JsonTestUtils.equals(firstValue, secondValue, ignoreProperties)) {
        return false;
      }

    }

    return true;
  }

  private JsonTestUtils() {
  }

}
