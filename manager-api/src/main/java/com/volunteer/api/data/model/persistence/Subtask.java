package com.volunteer.api.data.model.persistence;

import com.volunteer.api.data.model.SubtaskStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subtask")
public class Subtask {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subtask_generator")
  @SequenceGenerator(name = "subtask_generator", sequenceName = "subtask_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id")
  private Task task;

  @Column(name = "task_id", insertable = false, updatable = false)
  private Integer taskId;

  @Column(name = "quantity", columnDefinition = "NUMERIC", nullable = false)
  private BigDecimal quantity;

  @Column(name = "status_id", nullable = false)
  private SubtaskStatus status;

  @Column(name = "note")
  private String note;

  @Column(name = "transport_required")
  private boolean transportRequired;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by")
  private VPUser createdBy;

  @Column(name = "created_by", insertable = false, updatable = false)
  private Integer createdByUserId;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "closed_by")
  private VPUser closedBy;

  @Column(name = "closed_by", insertable = false, updatable = false)
  private Integer closedByUserId;

  @Column(name = "closed_at")
  private ZonedDateTime closedAt;

}
