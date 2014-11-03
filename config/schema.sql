/**********************/
/* Table for 'User'   */
DROP TABLE IF EXISTS users;
CREATE TABLE users(
  id INT NOT NULL auto_increment PRIMARY KEY,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  pass VARCHAR(20) NOT NULL,
  email VARCHAR(60) NOT NULL unique,
  address VARCHAR(40) NOT NULL,
  city_id INT NOT NULL
);


/******************************/
/* Table for 'Administrator'   */
DROP TABLE IF EXISTS administrators;
CREATE TABLE administrators(
  id INT NOT NULL auto_increment PRIMARY KEY,
  pass VARCHAR(20) NOT NULL,
  email VARCHAR(60) NOT NULL unique
);


/* This admin must be created automatically */
/******************************/
/* Creator the 'Webmaster'   */
INSERT INTO administrators (pass,email) VALUES ('admin','admin');


/**********************/
/* Table for 'City'   */
DROP TABLE IF EXISTS cities;
CREATE TABLE cities (
  id INT NOT NULL auto_increment PRIMARY KEY,
  postcode INT NOT NULL unique,
  name VARCHAR(20) NOT NULL,
  state VARCHAR(20) NOT NULL,
  country VARCHAR(20) NOT NULL
);


/**********************/
/* Table for 'Phone'  */
DROP TABLE IF EXISTS phones;
CREATE TABLE phones (
  id INT NOT NULL auto_increment PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  num VARCHAR(30) NOT NULL,
  user_id int NOT NULL
);


/***********************/
/* Table for 'Vehicle' */
DROP TABLE IF EXISTS vehicles;
CREATE TABLE vehicles (
  id int NOT NULL auto_increment PRIMARY KEY,
  plate VARCHAR(12) NOT NULL unique,
  user_id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  brand VARCHAR(45) NOT NULL,
  year INT(4) NOT NULL,
  type VARCHAR(20) NOT NULL,
  passengers INT,
  displacement INT,
  max_load INT,
  doors INT
);



/**********************/
/* Table for 'Post' */
DROP TABLE IF EXISTS posts;
CREATE TABLE posts (
  id int NOT NULL auto_increment PRIMARY KEY,
  user_id INT NOT NULL,
  vehicle_id INT NOT NULL,
  price INT NOT NULL,
  text VARCHAR(255) NOT NULL
);


/**********************/
/* Table for 'Rate'   */
DROP TABLE IF EXISTS rates;
CREATE TABLE rates (
  id int NOT NULL auto_increment PRIMARY KEY,
  post_id INT NOT NULL,
  user_id int NOT NULL,
  rate INT NOT NULL
);


/************************/
/* Table for 'Question' */
DROP TABLE IF EXISTS questions;
CREATE TABLE questions (
  id int NOT NULL auto_increment PRIMARY KEY,
  post_id INT NOT NULL,
  user_id int NOT NULL,
  question VARCHAR(80) NOT NULL
);


/**********************/
/* Table for 'Answer' */
DROP TABLE IF EXISTS answers;
CREATE TABLE answers (
  id int NOT NULL auto_increment PRIMARY KEY,
  question_id INT NOT NULL,
  answer VARCHAR(80) NOT NULL
);
