ALTER TABLE subtask
DROP COLUMN product_id,
DROP COLUMN volunteer_id,

ADD COLUMN created_by integer NOT NULL,
ADD COLUMN created_at timestamp NOT NULL,

ADD COLUMN closed_by integer,
ADD COLUMN closed_at timestamp,

ADD CONSTRAINT fk_subtask_created_by FOREIGN KEY (created_by) REFERENCES public."user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
ADD CONSTRAINT fk_subtask_closed_by FOREIGN KEY (closed_by) REFERENCES public."user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT;