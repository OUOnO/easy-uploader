CREATE TABLE IF NOT EXISTS FILE_META
(
  ID           BIGINT PRIMARY KEY NOT NULL,
  PATH         VARCHAR(200),
  CODE         VARCHAR(32),
  DOWNLOAD_URL VARCHAR(200),
  FILE_NAME    VARCHAR(200),
  CREATE_DATE  TIMESTAMP
);