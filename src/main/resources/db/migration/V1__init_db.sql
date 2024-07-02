CREATE TABLE IF NOT EXISTS crawlers
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255),
    enabled         BOOLEAN   NOT NULL DEFAULT FALSE,
    last_crawled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_result     JSON,
    next_crawl_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items
(
    id         UUID PRIMARY KEY,
    store      INT       NOT NULL,
    vendor     VARCHAR   NOT NULL,
    title      VARCHAR   NOT NULL,
    type       INT       NOT NULL,
    source_url VARCHAR,
    image_url  VARCHAR,
    price      VARCHAR,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)
