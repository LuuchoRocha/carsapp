#!/bin/bash

mysql -uroot -e "drop database if exists carsapp_test; drop database if exists carsapp_development; create database carsapp_development; create database carsapp_test; use carsapp_development; source ./config/schema.sql; source ./config/loadExamples.sql; use carsapp_test; source ./config/schema.sql;"

