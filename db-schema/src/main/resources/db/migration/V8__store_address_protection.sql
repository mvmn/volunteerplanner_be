ALTER TABLE store
DROP COLUMN address_id,
DROP COLUMN contact_person,

ADD COLUMN city_id integer NOT NULL,
ADD COLUMN address varchar(2000) NULL,
ADD COLUMN confidential bool NOT NULL DEFAULT false,

ALTER COLUMN note TYPE TEXT,

ADD CONSTRAINT fk_store_city FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX idx_store_name ON public.store USING btree (name);
CREATE INDEX idx_store_confidential ON public.store USING btree (confidential);

DROP TABLE address;