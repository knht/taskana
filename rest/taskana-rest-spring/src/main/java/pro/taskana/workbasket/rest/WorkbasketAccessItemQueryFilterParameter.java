package pro.taskana.workbasket.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.ConstructorProperties;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import pro.taskana.common.rest.QueryParameter;
import pro.taskana.workbasket.api.WorkbasketAccessItemQuery;

public class WorkbasketAccessItemQueryFilterParameter
    implements QueryParameter<WorkbasketAccessItemQuery, Void> {

  /** Filter by the key of the workbasket. This is an exact match. */
  @JsonProperty("workbasket-key")
  private final String[] workbasketKey;

  /**
   * Filter by the key of the workbasket. This results in a substring search.. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("workbasket-key-like")
  private final String[] workbasketKeyLike;

  /** Filter by the name of the access id. This is an exact match. */
  @JsonProperty("access-id")
  private final String[] accessId;

  /**
   * Filter by the name of the access id. This results in a substring search.. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("access-id-like")
  private final String[] accessIdLike;

  @ConstructorProperties({"workbasket-key", "workbasket-key-like", "access-id", "access-id-like"})
  public WorkbasketAccessItemQueryFilterParameter(
      String[] workbasketKey,
      String[] workbasketKeyLike,
      String[] accessId,
      String[] accessIdLike) {
    this.workbasketKey = workbasketKey;
    this.workbasketKeyLike = workbasketKeyLike;
    this.accessId =
        Arrays.stream(accessId)
            .map(
                accessIdItem -> {
                  try {
                    return URLDecoder.decode(accessIdItem, StandardCharsets.UTF_8.toString());
                  } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                  }
                  return accessIdItem;
                })
            .toArray(String[]::new);
    this.accessIdLike = accessIdLike;
  }

  public String[] getAccessId() {
    return accessId;
  }

  @Override
  public Void apply(WorkbasketAccessItemQuery query) {
    Optional.ofNullable(workbasketKey).ifPresent(query::workbasketKeyIn);
    Optional.ofNullable(workbasketKeyLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::workbasketKeyLike);
    Optional.ofNullable(accessId).ifPresent(query::accessIdIn);
    Optional.ofNullable(accessIdLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::accessIdLike);
    return null;
  }

  @Override
  public String toString() {
    return "WorkbasketAccessItemQueryFilterParameter [workbasketKey="
        + Arrays.toString(workbasketKey)
        + ", workbasketKeyLike="
        + Arrays.toString(workbasketKeyLike)
        + ", accessId="
        + Arrays.toString(accessId)
        + ", accessIdLike="
        + Arrays.toString(accessIdLike)
        + "]";
  }
}
