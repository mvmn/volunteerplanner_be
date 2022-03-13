package com.volunteer.api.data.model.persistence;

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
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
  @SequenceGenerator(name = "task_generator", sequenceName = "task_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(name = "name", length = 250, nullable = false)
  private String name;

  @Column(name = "note", length = 2000, nullable = true)
  private String note;
}
