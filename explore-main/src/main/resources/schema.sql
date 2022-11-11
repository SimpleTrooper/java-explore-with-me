CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY(user_id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY(category_id)
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(100) NOT NULL,
    annotation VARCHAR (250),
    description TEXT,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INT,
    request_moderation BOOLEAN NOT NULL,
    created TIMESTAMP NOT NULL,
    event_state VARCHAR(30) NOT NULL,
    published TIMESTAMP,
    CONSTRAINT pk_events PRIMARY KEY(event_id),
    CONSTRAINT fk_events_categories FOREIGN KEY(category_id) REFERENCES categories(category_id),
    CONSTRAINT fk_events_users FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    request_state VARCHAR(100) NOT NULL,
    created TIMESTAMP,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY(request_id),
    CONSTRAINT fk_requests_events FOREIGN KEY(event_id) REFERENCES events(event_id),
    CONSTRAINT fk_requests_users FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(100) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilations PRIMARY KEY(compilation_id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT pk_compilations_events PRIMARY KEY(compilation_id, event_id),
    CONSTRAINT fk_compilations_events_events FOREIGN KEY(event_id) REFERENCES events(event_id),
    CONSTRAINT fk_compilations_events_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(compilation_id)
);