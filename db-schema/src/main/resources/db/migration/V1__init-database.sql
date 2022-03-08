-- public.address definition

-- Drop table

-- DROP TABLE IF EXISTS address;

CREATE TABLE address (
	id SERIAL,
	region varchar(100) NOT NULL,
	city varchar(100) NOT NULL,
	address varchar(2000) NULL,
	CONSTRAINT address_pk PRIMARY KEY (id)
);



-- public."user" definition

-- Drop table

-- DROP TABLE "user";

CREATE TABLE "user" (
	id varchar(50) NOT NULL,
	nickname varchar(100) NOT NULL,
	"role" varchar(100) NOT NULL,
	verified bool NOT NULL DEFAULT false,
	phonenumber varchar(100) NOT NULL,
	fullname varchar(250) NULL,
	email varchar(250) NULL,
	address_id varchar(50) NOT NULL,
	CONSTRAINT user_email_un UNIQUE (email),
	CONSTRAINT user_nickname_un UNIQUE (nickname),
	CONSTRAINT user_phonenumber_un UNIQUE (phonenumber),
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_fk FOREIGN KEY (address_id) REFERENCES address(id)
);


-- public.category definition

-- Drop table

-- DROP TABLE category;

CREATE TABLE category (
	id varchar(50) NOT NULL,
	parent_id varchar(50) NULL,
	"name" varchar(2000) NOT NULL,
	note varchar(2000) NULL,
	"path" varchar(2000) NOT NULL,
	CONSTRAINT category_pk PRIMARY KEY (id),
	CONSTRAINT category_un UNIQUE (name, parent_id),
	CONSTRAINT category_fk FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE ON UPDATE RESTRICT
);


-- public.product definition

-- Drop table

-- DROP TABLE product;

CREATE TABLE product (
	id varchar(50) NOT NULL,
	"name" varchar(2000) NOT NULL,
	category_id varchar(50) NULL,
	CONSTRAINT product_pk PRIMARY KEY (id),
	CONSTRAINT product_category_fk FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public.task definition

-- Drop table

-- DROP TABLE task;

CREATE TABLE task (
	id varchar(50) NOT NULL,
	customer varchar(2000) NOT NULL,
	store_address_id varchar(50) NOT NULL,
	customer_address_id varchar(50) NOT NULL,
	product_id varchar(50) NOT NULL,
	quantity numeric(20, 6) NOT NULL,
	product_measure varchar(100) NOT NULL,
	priority int4 NOT NULL DEFAULT 0,
	deadline_date timestamp NOT NULL,
	note varchar(2000) NULL,
	status varchar(50) NOT NULL DEFAULT 'new'::character varying,
	created_by varchar(50) NOT NULL,
	created_at timestamp NOT NULL,
	verified_by varchar(50) NULL,
	verified_at timestamp NULL,
	closed_by varchar(50) NULL,
	closed_at timestamp NULL,
	quantity_left numeric(20, 6) NOT NULL,
	CONSTRAINT task_pk PRIMARY KEY (id),
	CONSTRAINT task_created_by_fk FOREIGN KEY (created_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT task_customer_address_fk FOREIGN KEY (customer_address_id) REFERENCES address(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT task_product_fk FOREIGN KEY (product_id) REFERENCES product(id),
	CONSTRAINT task_store_address_fk FOREIGN KEY (store_address_id) REFERENCES address(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT task_user_closed_fk FOREIGN KEY (closed_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT task_verified_by_fk FOREIGN KEY (verified_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX request_customer_idx ON public.task USING btree (customer);
CREATE INDEX request_product_name_idx ON public.task USING btree (product_id);
CREATE INDEX request_status_idx ON public.task USING btree (status);


-- public.subtask definition

-- Drop table

-- DROP TABLE subtask;

CREATE TABLE subtask (
	id varchar(50) NOT NULL,
	product_id varchar(50) NOT NULL,
	quantity numeric(20, 6) NOT NULL,
	product_measure varchar(100) NOT NULL,
	status varchar(50) NOT NULL DEFAULT 'in_progress'::character varying,
	"comment" varchar(2000) NULL,
	transport_help_needed bool NOT NULL DEFAULT false,
	transport_required
	volunteer_id varchar(50) NOT NULL,
	task_id varchar(50) NULL,
	CONSTRAINT subtask_pk PRIMARY KEY (id),
	CONSTRAINT subtask_fk FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT subtask_task_fk FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT subtask_volunteer_fk FOREIGN KEY (volunteer_id) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX subtask_product_name_idx ON public.subtask USING btree (product_id);