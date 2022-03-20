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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "city")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_generator")
  @SequenceGenerator(name = "city_generator", sequenceName = "city_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "region_id")
  private Region region;

  @Column(name = "name", nullable = false)
  private String name;

}
