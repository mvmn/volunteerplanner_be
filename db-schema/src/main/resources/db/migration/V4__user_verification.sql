ALTER TABLE "user"
DROP COLUMN IF EXISTS verified;

ALTER TABLE "user"
ADD COLUMN version_num integer NOT NULL DEFAULT 0,
ADD COLUMN phone_number_verified bool NOT NULL DEFAULT false,
ADD COLUMN user_verified bool NOT NULL DEFAULT false,
ADD COLUMN user_verified_by integer NULL,
ADD COLUMN user_verified_at timestamp NULL,
ADD COLUMN locked bool NOT NULL DEFAULT false,
ADD COLUMN locked_by integer NULL,
ADD COLUMN locked_at timestamp NULL,

ADD CONSTRAINT fk_user_user_verified_by FOREIGN KEY (user_verified_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
ADD CONSTRAINT fk_user_locked_by FOREIGN KEY (locked_by) REFERENCES "user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE INDEX idx_user_phone_number_verified ON public."user" USING btree (phone_number_verified);
CREATE INDEX idx_user_user_verified ON public."user" USING btree (user_verified);
CREATE INDEX idx_user_locked ON public."user" USING btree (locked);