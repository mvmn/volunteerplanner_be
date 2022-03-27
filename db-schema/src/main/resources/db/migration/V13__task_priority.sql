ALTER TABLE task
DROP COLUMN priority,
ADD COLUMN priority_id integer NOT NULL DEFAULT 3;

CREATE INDEX idx_task_priority ON public.task USING btree (priority_id);