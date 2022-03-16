ALTER TABLE subtask RENAME COLUMN order_id TO task_id;

ALTER TABLE subtask RENAME CONSTRAINT fk_subtask_order_id TO fk_subtask_task_id;

