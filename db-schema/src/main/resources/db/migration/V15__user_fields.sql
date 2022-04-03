ALTER TABLE "user"

DROP COLUMN user_name,
DROP COLUMN full_name,
DROP COLUMN email,

ADD COLUMN display_name varchar(100) NOT NULL,
ADD COLUMN organization varchar(100),
ADD COLUMN rating integer NOT NULL default 0,

ADD CONSTRAINT un_user_display_name UNIQUE (display_name);

CREATE INDEX idx_user_rating ON public."user" USING btree (rating);

INSERT INTO user_role ("name") VALUES('requestor');