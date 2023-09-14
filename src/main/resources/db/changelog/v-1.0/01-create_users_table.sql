CREATE TABLE users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    username character varying(16) NOT NULL,
    age smallint NOT NULL,
    telephone character varying NOT NULL,
    email character varying NOT NULL,
    status smallint NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_username_key UNIQUE (username),
    CONSTRAINT users_age_check CHECK (age > 14 AND age < 120)
)