-- Run this as postgres superuser
CREATE DATABASE securebank;
CREATE USER securebank WITH ENCRYPTED PASSWORD 'securebank123';
GRANT ALL PRIVILEGES ON DATABASE securebank TO securebank;

-- Connect to securebank database before running 02_schema.sql
