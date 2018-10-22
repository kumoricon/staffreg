CREATE DATABASE IF NOT EXISTS staffreg CHARACTER SET utf8mb4;

CREATE USER 'staffreg'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON staffreg.* to 'staffreg'@'localhost';
