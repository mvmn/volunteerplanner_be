package com.volunteer.api.utils;

import java.util.Map;
import com.volunteer.api.data.model.SubtaskStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserRatingUtils {

  private static final Map<SubtaskStatus, Integer> SUBTASK_STATUS_SCORE = Map.of(
      SubtaskStatus.COMPLETED, 1,
      SubtaskStatus.REJECTED, -10
  );

  public static int calculateDelta(final SubtaskStatus status, final int count) {
    final int score = SUBTASK_STATUS_SCORE.getOrDefault(status, 0);
    return score * count;
  }
}
