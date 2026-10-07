CREATE TABLE outbox_events (
 id UUID PRIMARY KEY,
 aggregate_id UUID NOT NULL,
 event_type VARCHAR(120) NOT NULL,
 payload TEXT NOT NULL,
 published BOOLEAN NOT NULL DEFAULT FALSE,
 created_at TIMESTAMPTZ NOT NULL,
 published_at TIMESTAMPTZ
);
CREATE INDEX idx_outbox_pending ON outbox_events(published,created_at);