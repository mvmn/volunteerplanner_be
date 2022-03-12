package com.volunteer.api.data.user.model.persistence;

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
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
  @SequenceGenerator(name = "category_generator", sequenceName = "category_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @Column(name = "name")
  private String name;

  @Column(name = "note")
  private String note;

  @Column(name = "path")
  private String path;

}
