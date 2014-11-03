INSERT INTO `carsapp_development`.`cities` (`postcode`, `name`, `state`, `country`) 
    VALUES ('5800', 'Rio IV', 'Cordoba', 'Argentina');

INSERT INTO `carsapp_development`.`cities` (`postcode`, `name`, `state`, `country`) 
    VALUES ('5700', 'Rio III', 'Cordoba', 'Argentina');

INSERT INTO `carsapp_development`.`users` (`first_name`, `last_name`, `pass`, `email`, `address`, `city_id`) 
    VALUES ('Lucho', 'Rocha', '123456', 'lrocha@gmail.com', 'Peru 725', '1');
    
INSERT INTO `carsapp_development`.`users` (`first_name`, `last_name`, `pass`, `email`, `address`, `city_id`) 
    VALUES ('Juan', 'Baez', '123456', 'jbaez@gmail.com', 'Constitucion 1292', '1');
    
INSERT INTO `carsapp_development`.`users` (`first_name`, `last_name`, `pass`, `email`, `address`, `city_id`) 
    VALUES ('Mery', 'Anelo', '123456', 'manelo@gmail.com', 'Velez Sarfield 214', '1');
    
INSERT INTO `carsapp_development`.`users` (`first_name`, `last_name`, `pass`, `email`, `address`, `city_id`) 
    VALUES ('Jorge', 'Ortolano', '123456', 'jortolano@gmail.com', 'Buenos Aires 1287', '1');
    
INSERT INTO `carsapp_development`.`phones` (`type`, `num`, `user_id`) 
    VALUES ('home', '4628083', '1');

INSERT INTO `carsapp_development`.`phones` (`type`, `num`, `user_id`) 
    VALUES ('home', '4621457', '2');

INSERT INTO `carsapp_development`.`phones` (`type`, `num`, `user_id`) 
    VALUES ('home', '4600212', '3');

INSERT INTO `carsapp_development`.`phones` (`type`, `num`, `user_id`) 
    VALUES ('home', '4629213', '4');

INSERT INTO `carsapp_development`.`vehicles` (`plate`, `user_id`, `name`, `brand`, `year`, `type`, `passengers`) 
    VALUES ('GDQ202', '1', 'Ka', 'Ford', '2007', 'car', '4');

INSERT INTO `carsapp_development`.`vehicles` (`plate`, `user_id`, `name`, `brand`, `year`, `type`, `passengers`) 
    VALUES ('AMY471', '2', 'Clio', 'Renault', '2002', 'car', '4');

INSERT INTO `carsapp_development`.`posts` (`user_id`, `vehicle_id`, `price`, `text`) 
    VALUES ('2', '2', '45000', 'Hola, vendo mi Clio... Es modelo 2002 y esta en excelente estado.');

INSERT INTO `carsapp_development`.`posts` (`user_id`, `vehicle_id`, `price`, `text`) 
    VALUES ('1', '1', '45000', 'Hola, vendo mi Ford Ka... Es modelo 2007 y esta en muy buen estado.');

