CREATE SCHEMA IF NOT EXISTS sniper DEFAULT CHARACTER SET UTF8;
USE sniper;

CREATE TABLE IF NOT EXISTS platform (
  id INT AUTO_INCREMENT PRIMARY KEY,
	code VARCHAR(10) NOT NULL,

	created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uc_platform_code UNIQUE (code),

  INDEX ix_platform_created_by (created_by),
  INDEX ix_platform_created_on (created_on)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS platform_metadata (
  id INT AUTO_INCREMENT PRIMARY KEY,
  platform_id INT NOT NULL,

	full_name VARCHAR(50) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,

	perm_id VARCHAR(24),
  primary_ric VARCHAR(10),

	created_by VARCHAR(255) NOT NULL,
  valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valid_to TIMESTAMP NULL,

  CONSTRAINT fk_platform_metadata_platform_id FOREIGN KEY(platform_id) REFERENCES platform(id) ON DELETE RESTRICT,

  INDEX ix_platform_full_name (full_name),
  INDEX ix_platform_active (active),

  INDEX ix_platform_perm_id (perm_id),
  INDEX ix_platform_primary_ric (primary_ric),

  INDEX ix_platform_created_by (created_by),
  INDEX ix_platform_valid_from (valid_from),
  INDEX ix_platform_valid_to (valid_to)
)  ENGINE=INNODB;

CREATE OR REPLACE VIEW vw_platform
AS SELECT
  p.id,
  p.code,
  pm.full_name,
  pm.active,
  pm.perm_id,
  pm.primary_ric,
  p.created_by AS created_by,
  p.created_on AS created_on,
  pm.created_by AS updated_by,
  pm.valid_from AS updated_on
FROM platform p
  INNER JOIN platform_metadata pm ON pm.platform_id = p.id AND pm.valid_to IS NULL;

CREATE TABLE IF NOT EXISTS account (
  id INT AUTO_INCREMENT PRIMARY KEY,

  platform_id INT NOT NULL,
  platform_account_id VARCHAR(255) NOT NULL,

	created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT fk_account_platform_id FOREIGN KEY(platform_id) REFERENCES platform(id) ON DELETE RESTRICT,
	CONSTRAINT uc_account_account_id_platform_id UNIQUE(platform_account_id, platform_id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS account_metadata (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_id INT NOT NULL,

	account_handle VARCHAR(255) NOT NULL,
  display_name VARCHAR(255),

  active TINYINT(1) NOT NULL DEFAULT 1,

	created_by VARCHAR(255) NOT NULL,
  valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valid_to TIMESTAMP NULL,

  CONSTRAINT fk_account_metadata_account_id FOREIGN KEY(account_id) REFERENCES account(id) ON DELETE RESTRICT,

  INDEX ix_account_metadata_account_handle (account_handle),
  INDEX ix_account_metadata_display_mame (display_name),

  INDEX ix_account_metadata_active (active),

  INDEX ix_account_metadata_created_by (created_by),
  INDEX ix_account_metadata_valid_from (valid_from),
  INDEX ix_account_metadata_valid_to (valid_to)
)  ENGINE=INNODB;

CREATE OR REPLACE VIEW vw_account
AS SELECT
	ac.id AS id,
  ac.platform_id,
  ac.platform_account_id,
  acm.account_handle,
  acm.display_name,
  acm.active,
  ac.created_by AS created_by,
  ac.created_on AS created_on,
  acm.created_by AS updated_by,
  acm.valid_from AS updated_on
FROM account ac
  INNER JOIN account_metadata acm on acm.account_id = ac.id AND acm.valid_to IS NULL;

CREATE TABLE IF NOT EXISTS entity_category (
  id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,

  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uc_entity_category_name UNIQUE (name)
)  ENGINE=INNODB;

INSERT INTO entity_category(id, name)
VALUES
  (1, "person"),
  (2, "organization");

CREATE TABLE IF NOT EXISTS entity (
  id INT AUTO_INCREMENT PRIMARY KEY,

  created_by VARCHAR(255) NOT NULL,
  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  INDEX ix_entity_created_by (created_by),
  INDEX ix_entity_created_on (created_on)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS entity_metadata (
  id INT AUTO_INCREMENT PRIMARY KEY,

  sme_id INT NOT NULL,
  category_id INT,

	full_name VARCHAR(255) NOT NULL,
	primary_email VARCHAR(255),

  active TINYINT(1) NOT NULL DEFAULT 1,

	perm_id VARCHAR(24),
  primary_ric VARCHAR(10),

	created_by VARCHAR(255) NOT NULL,
  valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valid_to TIMESTAMP NULL,

	CONSTRAINT fk_entity_metadata_sme_id FOREIGN KEY(sme_id) REFERENCES entity(id) ON DELETE RESTRICT,
  CONSTRAINT fk_entity_metadata_category_id FOREIGN KEY(category_id) REFERENCES entity_category(id) ON DELETE RESTRICT,

  INDEX ix_entity_metadata_full_name (full_name),
  INDEX ix_entity_metadata_active (active),

  INDEX ix_entity_metadata_perm_id (perm_id),
  INDEX ix_entity_metadata_primary_ric (primary_ric),

  INDEX ix_entity_metadata_created_by (created_by),
  INDEX ix_entity_metadata_valid_from (valid_from),
  INDEX ix_entity_metadata_valid_to (valid_to)
)  ENGINE=INNODB;

CREATE OR REPLACE VIEW vw_entity
AS SELECT
  e.id,
  em.category_id,
  em.full_name,
  em.primary_email,
  em.active,
  em.perm_id,
  em.primary_ric,
  e.created_by AS created_by,
  e.created_on AS created_on,
  em.created_by AS updated_by,
  em.valid_from AS updated_on
FROM entity e
    INNER JOIN entity_metadata em ON em.sme_id = e.id AND em.valid_to IS NULL;

CREATE TABLE IF NOT EXISTS entity_account (
  id INT AUTO_INCREMENT PRIMARY KEY,

  sme_id INT NOT NULL,
  account_id INT NOT NULL,

  active TINYINT(1) NOT NULL DEFAULT 1,

	created_by VARCHAR(255) NOT NULL,
  valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valid_to TIMESTAMP NULL,

	CONSTRAINT fk_entity_account_sme_id FOREIGN KEY(sme_id) REFERENCES entity(id) ON DELETE RESTRICT,
	CONSTRAINT fk_entity_account_account_id FOREIGN KEY(account_id) REFERENCES account(id) ON DELETE RESTRICT,

  INDEX ix_entity_account_active (active),

  INDEX ix_entity_account_created_by (created_by),
  INDEX ix_entity_account_valid_from (valid_from),
  INDEX ix_entity_account_valid_to (valid_to)
)  ENGINE=INNODB;

CREATE OR REPLACE VIEW vw_entity_account
AS SELECT
  ea.id,
  ea.sme_id,
  ea.account_id,
  ea.created_by,
  ea.valid_from AS created_on
FROM entity_account ea
WHERE ea.valid_to IS NULL AND ea.active = 1;