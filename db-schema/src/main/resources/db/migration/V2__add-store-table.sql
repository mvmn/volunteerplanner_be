CREATE TABLE store (
	id serial NOT NULL,
	"name" varchar(250) NOT NULL,
	address_id integer NOT NULL,
	recipient_address_id integer NULL,
	contact_person varchar(250) NULL,
	note varchar(2000) NULL,
	CONSTRAINT pk_store PRIMARY KEY (id),
	CONSTRAINT fk_store_address_id FOREIGN KEY (address_id) REFERENCES public.address(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_store_recipient_address_id FOREIGN KEY (recipient_address_id) REFERENCES public.address(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE task DROP CONSTRAINT fk_task_store_address_id;
ALTER TABLE task DROP COLUMN store_address_id;
ALTER TABLE task ADD store_id integer NOT NULL;
ALTER TABLE task ADD CONSTRAINT fk_task_store_id FOREIGN KEY (store_id) REFERENCES public.store(id) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE task DROP CONSTRAINT fk_task_customer_address_id;
ALTER TABLE task DROP COLUMN customer_address_id;
