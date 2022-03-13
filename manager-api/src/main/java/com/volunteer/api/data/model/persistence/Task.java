package com.volunteer.api.data.model.persistence;

import java.io.Serializable;
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
import javax.persistence.Version;
import com.volunteer.api.data.model.TaskStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class Task implements Serializable {
  private static final long serialVersionUID = -8389647643192380321L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
  @SequenceGenerator(name = "task_generator", sequenceName = "task_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Version
  @Column(name = "version_num")
  private Integer version;

  @Column(name = "customer", length = 255, nullable = false)
  private String customer;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "volunteer_store_id")
  private Store volunteerStore;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_store_id")
  private Store customerStore;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "quantity", columnDefinition = "NUMERIC", nullable = false)
  private Integer quantity;

  @Column(name = "product_measure", nullable = false, length = 100)
  private String productMeasure;

  @Column(name = "priority", columnDefinition = "NUMERIC", nullable = false)
  private Integer priority;

  @Column(name = "deadline_date", nullable = false)
  private ZonedDateTime deadlineDate;

  @Column(name = "note", nullable = true, length = 2000)
  private String note;

  @Column(name = "status_id", nullable = false)
  private TaskStatus status;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by")
  private VPUser createdBy;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "verified_by")
  private VPUser verifiedBy;

  @Column(name = "verified_at", nullable = true)
  private ZonedDateTime verifiedAt;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "closed_by")
  private VPUser closedBy;

  @Column(name = "closed_at", nullable = true)
  private ZonedDateTime closedAt;

  @Column(name = "quantity_left", columnDefinition = "NUMERIC", nullable = false)
  private Integer quantityLeft;
}
