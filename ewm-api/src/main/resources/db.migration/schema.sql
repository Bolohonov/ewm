CREATE TABLE IF NOT EXISTS users (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 name VARCHAR(255) NOT NULL,
 firstname VARCHAR(255) NOT NULL,
 lastname VARCHAR(255) NOT NULL,
 email VARCHAR(512) NOT NULL,
 password_hash VARCHAR(255) NOT NULL,
 CONSTRAINT pk_user PRIMARY KEY (id),
 CONSTRAINT UQ_USER_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS user_roles (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT pk_user_role PRIMARY KEY (id),
    CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE SPRING_SESSION(
    PRIMARY_ID CHAR(36) NOT NULL PRIMARY KEY,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100) DEFAULT NULL
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES(
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

CREATE TABLE tokens (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token_value VARCHAR(255) NOT NULL,
    token_validity BOOLEAN,
    user_id BIGINT,
    CONSTRAINT pk_token PRIMARY KEY (id),
    CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_NAME UNIQUE (name)
);

create type event_state as enum ('PENDING', 'PUBLISHED', 'CANCELED');

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(2000) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_requests BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(4000),
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    paid BOOLEAN,
    participant_limit INT,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state event_state,
    views BIGINT,
    loc_lat float4 NOT NULL,
    loc_lon float4 NOT NULL,
    rating BIGINT,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT FK_EVENT_ON_INITIATOR
      FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE CAST (CHARACTER VARYING as event_state) WITH INOUT AS IMPLICIT;

CREATE TYPE request_status AS ENUM ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status request_status,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT FK_REQUEST_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_REQUEST_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE CAST (CHARACTER VARYING as request_status) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(2000) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_in_compilations (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_events_in_compilations PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT FK_events_in_compilations_to_events
      FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT FK_events_in_compilations_to_compilations
      FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feedbacks (
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    is_like BOOLEAN NOT NULL,
    CONSTRAINT pk_feedbacks PRIMARY KEY (user_id, event_id),
    CONSTRAINT FK_feedbacks_to_users
     FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_feedbacks_to_events
     FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);