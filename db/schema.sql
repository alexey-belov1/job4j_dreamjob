CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE city (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   photo_id integer,
   city_id integer references city(id)
);

CREATE TABLE photo (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name TEXT,
   email TEXT UNIQUE,
   password TEXT
);
