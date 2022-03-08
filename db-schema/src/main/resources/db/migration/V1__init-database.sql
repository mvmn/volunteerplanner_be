-- public.address definition

-- Drop table

-- DROP TABLE address;

CREATE TABLE address (
	id serial,
	region varchar(100) NOT NULL,
	city varchar(100) NOT NULL,
	address varchar(2000) NULL,
	CONSTRAINT pk_address PRIMARY KEY (id)
);

-- public.user_role definition

-- Drop table

-- DROP TABLE user_role;

CREATE TABLE user_role (
	id serial,
	"name" varchar(250) NOT NULL,
	CONSTRAINT pk_user_role PRIMARY KEY (id)
);

-- public."user" definition

-- Drop table

-- DROP TABLE "user";

CREATE TABLE "user" (
	id serial,
	user_name varchar(100) NOT NULL,
	phone_number varchar(100) NOT NULL,
	password varchar(250) NOT NULL,
	role_id integer NOT NULL,
	verified bool NOT NULL DEFAULT false,
	address_id integer NOT NULL,
	full_name varchar(250) NULL,
	email varchar(250) NULL,
	CONSTRAINT pk_user PRIMARY KEY (id),
	CONSTRAINT un_user_user_name UNIQUE (user_name),
	CONSTRAINT un_user_phone_number UNIQUE (phone_number),
	CONSTRAINT fk_user_address_id FOREIGN KEY (address_id) REFERENCES address(id),
	CONSTRAINT fk_user_role_id FOREIGN KEY (role_id) REFERENCES user_role(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- public.category definition

-- Drop table

-- DROP TABLE category;

CREATE TABLE category (
	id serial,
	parent_id integer NULL,
	"name" varchar(50) NOT NULL,
	note varchar(2000) NULL,
	"path" varchar(2000) NOT NULL,
	CONSTRAINT pk_category PRIMARY KEY (id),
	CONSTRAINT un_category UNIQUE (name, parent_id),
	CONSTRAINT fk_category_parent_id FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE ON UPDATE RESTRICT
);

-- public.product definition

-- Drop table

-- DROP TABLE product;

CREATE TABLE product (
	id serial,
	category_id integer NOT NULL,
	"name" varchar(250) NOT NULL,
	note varchar(2000) NULL,
	CONSTRAINT pk_product PRIMARY KEY (id),
	CONSTRAINT fk_product_category_id FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- public.order_status definition

-- Drop table

-- DROP TABLE order_status;

CREATE TABLE task_status (
	id serial,
	"name" varchar(50) NOT NULL,
	CONSTRAINT pk_task_status PRIMARY KEY (id)
);

-- public."task" definition

-- Drop table

-- DROP TABLE "task";

CREATE TABLE task (
	id serial,
	customer varchar(250) NOT NULL,
	store_address_id integer NOT NULL,
	customer_address_id integer NOT NULL,
	product_id integer NOT NULL,
	quantity numeric(20, 6) NOT NULL,
	product_measure varchar(100) NOT NULL,
	priority integer NOT NULL DEFAULT 0,
	deadline_date timestamp NOT NULL,
	note varchar(2000) NULL,
	status_id integer NOT NULL DEFAULT 1,
	created_by integer NOT NULL,
	created_at timestamp NOT NULL,
	verified_by integer NULL,
	verified_at timestamp NULL,
	closed_by integer NULL,
	closed_at timestamp NULL,
	quantity_left numeric(20, 6) NOT NULL,
	CONSTRAINT pk_task PRIMARY KEY (id),
	CONSTRAINT fk_task_store_address_id FOREIGN KEY (store_address_id) REFERENCES address(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_task_customer_address_id FOREIGN KEY (customer_address_id) REFERENCES address(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_task_product_id FOREIGN KEY (product_id) REFERENCES product(id),
	CONSTRAINT fk_task_status_id FOREIGN KEY (status_id) REFERENCES task_status(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_task_created_by FOREIGN KEY (created_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_task_verified_by FOREIGN KEY (verified_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_task_closed_by FOREIGN KEY (closed_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX idx_task_customer_id ON public.task USING btree (customer);
CREATE INDEX idx_task_product_name ON public.task USING btree (product_id);
CREATE INDEX idx_task_status ON public.task USING btree (status_id);

-- public.subtask_status definition

-- Drop table

-- DROP TABLE subtask_status;

CREATE TABLE subtask_status (
	id serial,
	"name" varchar(50) NOT NULL,
	CONSTRAINT pk_subtask_status PRIMARY KEY (id)
);

-- public.subtask definition

-- Drop table

-- DROP TABLE subtask;

CREATE TABLE subtask (
	id serial,
	product_id integer NOT NULL,
	quantity numeric(20, 6) NOT NULL,
	status_id integer NOT NULL DEFAULT 1,
	note varchar(2000) NULL,
	transport_required bool NOT NULL DEFAULT false,
	volunteer_id integer NOT NULL,
	order_id integer NULL,
	CONSTRAINT pk_subtask PRIMARY KEY (id),
	CONSTRAINT fk_subtask_order_id FOREIGN KEY (order_id) REFERENCES task(id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT fk_subtask_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_subtask_status_id FOREIGN KEY (status_id) REFERENCES subtask_status(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_subtask_volunteer_id FOREIGN KEY (volunteer_id) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO user_role ("name") VALUES('root');
INSERT INTO user_role ("name") VALUES('operator');
INSERT INTO user_role ("name") VALUES('volunteer');

INSERT INTO task_status ("name") VALUES('new');
INSERT INTO task_status ("name") VALUES('verified');
INSERT INTO task_status ("name") VALUES('completed');
INSERT INTO task_status ("name") VALUES('rejected');

INSERT INTO subtask_status ("name") VALUES('in_progress');
INSERT INTO subtask_status ("name") VALUES('completed');
INSERT INTO subtask_status ("name") VALUES('rejected');