package com.volunteer.api.data.model.persistence;

import java.math.BigDecimal;
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
import com.volunteer.api.data.model.SubtaskStatus;
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "quantity", columnDefinition = "NUMERIC", nullable = false)
  private BigDecimal quantity;

  @Column(name = "status_id", nullable = false)
  private SubtaskStatus status;

  @Column(name = "note")
  private String note;

  @Column(name = "transport_required")
  private boolean transportRequired;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "volunteer_id")
  private VPUser volunteer;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "task_id")
  private Task task;
}
