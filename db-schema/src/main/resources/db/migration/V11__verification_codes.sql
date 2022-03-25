CREATE TABLE verification_code (
	id serial,
	created_at bigint NOT NULL,
	user_id integer NOT NULL,
	code varchar(16) NOT NULL,
	code_type varchar(64) NOT NULL,

	CONSTRAINT pk_verification_code PRIMARY KEY (id),
	CONSTRAINT fk_verification_code_user_id FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT uq_verification_code_user_type UNIQUE (code_type, user_id)
);
