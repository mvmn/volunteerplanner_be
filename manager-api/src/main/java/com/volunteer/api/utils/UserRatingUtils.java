package com.volunteer.api.utils;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.VPUser;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserRatingUtils {

  private static final int DISASTER_RATING = -50;

  private static final Map<SubtaskStatus, Integer> SUBTASK_STATUS_SCORE = Map.of(
      SubtaskStatus.COMPLETED, 1,
      SubtaskStatus.REJECTED, -10
  );

  public static int calculateDelta(final SubtaskStatus status, final int count) {
    final int score = SUBTASK_STATUS_SCORE.getOrDefault(status, 0);
    return score * count;
  }

  public static boolean hasDisasterRating(final VPUser user) {
    return user.getRating() <= DISASTER_RATING;
  }

}
