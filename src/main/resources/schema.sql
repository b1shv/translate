CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  ip VARCHAR(15) NOT NULL,
  date_time TIMESTAMP NOT NULL,
  words VARCHAR NOT NULL,
  words_translated VARCHAR NOT NULL
);