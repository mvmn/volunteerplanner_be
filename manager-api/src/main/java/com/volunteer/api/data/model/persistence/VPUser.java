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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
public class VPUser implements Serializable {

  private static final long serialVersionUID = 6222614224950722720L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
  @SequenceGenerator(name = "user_generator", sequenceName = "user_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Version
  @Column(name = "version_num")
  private Integer version;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "password")
  private String password;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "role_id")
  private Role role;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "email")
  private String email;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "address_id")
  private Address address;

  @Column(name = "phone_number_verified")
  private boolean phoneNumberVerified;

  @Column(name = "user_verified")
  private boolean userVerified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_verified_by")
  private VPUser userVerifiedBy;

  @Column(name = "user_verified_by", insertable = false, updatable = false)
  private Integer userVerifiedByUserId;

  @Column(name = "user_verified_at")
  private ZonedDateTime userVerifiedAt;

  @Column(name = "locked")
  private boolean locked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locked_by")
  private VPUser lockedBy;

  @Column(name = "locked_by", insertable = false, updatable = false)
  private Integer lockedByUserId;

  @Column(name = "locked_at")
  private ZonedDateTime lockedAt;

}
