-- create db users
CREATE ROLE cs_user1 LOGIN PASSWORD 'cs_pass1';
CREATE ROLE cs_user2 LOGIN PASSWORD 'cs_pass2';

CREATE ROLE gs_user1 LOGIN PASSWORD 'gs_pass1';
CREATE ROLE gs_user2 LOGIN PASSWORD 'gs_pass2';

-- make cs_user1 the owner of message_service db
ALTER DATABASE message_service OWNER TO cs_user1;
-- make gs_user1 the owner of message_service db
ALTER DATABASE message_service OWNER TO gs_user1;

-- create schema cs with cs_user1 owner
CREATE SCHEMA cs AUTHORIZATION cs_user1;
-- create schema gs with gs_user1 owner
CREATE SCHEMA gs AUTHORIZATION gs_user1;

-- grant usage to cs_user2
GRANT USAGE ON SCHEMA cs TO cs_user2;
-- grant usage to gs_user2
GRANT USAGE ON SCHEMA gs TO gs_user2;

--
ALTER DEFAULT PRIVILEGES FOR USER cs_user1 IN SCHEMA cs GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE ON TABLES TO cs_user2;
ALTER DEFAULT PRIVILEGES FOR USER cs_user1 IN SCHEMA cs GRANT USAGE ON SEQUENCES TO cs_user2;
ALTER DEFAULT PRIVILEGES FOR USER cs_user1 IN SCHEMA cs GRANT EXECUTE ON FUNCTIONS TO cs_user2;
--
ALTER DEFAULT PRIVILEGES FOR USER gs_user1 IN SCHEMA gs GRANT SELECT,INSERT,UPDATE,DELETE,TRUNCATE ON TABLES TO gs_user2;
ALTER DEFAULT PRIVILEGES FOR USER gs_user1 IN SCHEMA gs GRANT USAGE ON SEQUENCES TO gs_user2;
ALTER DEFAULT PRIVILEGES FOR USER gs_user1 IN SCHEMA gs GRANT EXECUTE ON FUNCTIONS TO gs_user2;

ALTER USER cs_user2 SET search_path = 'cs';
ALTER USER gs_user2 SET search_path = 'gs';