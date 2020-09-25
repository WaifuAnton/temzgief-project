-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shopdb
-- -----------------------------------------------------
-- temzgief-project for EPAM
DROP SCHEMA IF EXISTS `shopdb` ;

-- -----------------------------------------------------
-- Schema shopdb
--
-- temzgief-project for EPAM
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shopdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `shopdb` ;

-- -----------------------------------------------------
-- Table `shopdb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`users` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`users` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(256) NOT NULL,
  `password_hash` BLOB NOT NULL,
  `salt` VARCHAR(16) NOT NULL,
  `role` VARCHAR(16) NOT NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`categories` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`categories` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  `picture` VARCHAR(128) NOT NULL,
  `parent_id` BIGINT(10) UNSIGNED NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_categories_categories1_idx` (`parent_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) INVISIBLE,
  CONSTRAINT `fk_categories_categories1`
    FOREIGN KEY (`parent_id`)
    REFERENCES `shopdb`.`categories` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`products` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`products` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  `picture` VARCHAR(128) NOT NULL,
  `color` VARCHAR(16) NOT NULL,
  `manufacture_date` DATE NOT NULL,
  `description` VARCHAR(4096) NULL,
  `price` DOUBLE UNSIGNED NOT NULL,
  `amount` INT UNSIGNED NOT NULL DEFAULT 0,
  `category_id` BIGINT(10) UNSIGNED NOT NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_products_categories1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_categories1`
    FOREIGN KEY (`category_id`)
    REFERENCES `shopdb`.`categories` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`orders` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`orders` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(10) UNSIGNED NOT NULL,
  `total` DOUBLE UNSIGNED NOT NULL DEFAULT 0,
  `status` VARCHAR(16) NOT NULL DEFAULT 'REGISTERED',
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `shopdb`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`order_has_product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`order_has_product` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`order_has_product` (
  `order_id` BIGINT(10) UNSIGNED NOT NULL,
  `product_id` BIGINT(10) UNSIGNED NOT NULL,
  `count` INT UNSIGNED NOT NULL,
  `price` DOUBLE UNSIGNED NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX `fk_cart_has_products_products1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_cart_has_products_cart1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_has_products_cart1`
    FOREIGN KEY (`order_id`)
    REFERENCES `shopdb`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_has_products_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `shopdb`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`addresses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`addresses` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`addresses` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(128) NOT NULL,
  `city` VARCHAR(128) NOT NULL,
  `building` VARCHAR(16) NOT NULL,
  `apartment` INT UNSIGNED NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `country_city_building__apartment_UNIQUE` (`country` ASC, `city` ASC, `building` ASC, `apartment` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`deliveries`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`deliveries` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`deliveries` (
  `order_id` BIGINT(10) UNSIGNED NOT NULL,
  `begin_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `details` VARCHAR(2048) NULL,
  `address_id` BIGINT(10) UNSIGNED NOT NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  INDEX `fk_deliveries_addresses1_idx` (`address_id` ASC) VISIBLE,
  INDEX `fk_deliveries_orders1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_deliveries_addresses1`
    FOREIGN KEY (`address_id`)
    REFERENCES `shopdb`.`addresses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_deliveries_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `shopdb`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopdb`.`blocked_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopdb`.`blocked_users` ;

CREATE TABLE IF NOT EXISTS `shopdb`.`blocked_users` (
  `user_id` BIGINT(10) UNSIGNED NOT NULL,
  `begin_date` DATE NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_blocked_users_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `shopdb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `shopdb`;

DELIMITER $$

USE `shopdb`$$
DROP TRIGGER IF EXISTS `shopdb`.`order_has_product_BEFORE_INSERT` $$
USE `shopdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `shopdb`.`order_has_product_BEFORE_INSERT` BEFORE INSERT ON `order_has_product` FOR EACH ROW
BEGIN
	DECLARE new_amount INT;
    SET NEW.price = (SELECT price FROM products WHERE NEW.product_id = id);
    SET new_amount = (SELECT amount FROM products WHERE id = NEW.product_id) - NEW.count;
    IF (new_amount < 0) THEN
		SIGNAL SQLSTATE '45001' SET message_text = 'Not enough products';
	END IF;
    UPDATE products SET amount = new_amount WHERE id = NEW.product_id;
    UPDATE orders SET total = total + NEW.price * NEW.count WHERE id = NEW.order_id;
END$$


USE `shopdb`$$
DROP TRIGGER IF EXISTS `shopdb`.`order_has_product_BEFORE_UPDATE` $$
USE `shopdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `shopdb`.`order_has_product_BEFORE_UPDATE` BEFORE UPDATE ON `order_has_product` FOR EACH ROW
BEGIN
	DECLARE delta INT;
    SET delta = NEW.count - count;
    IF (delta < 0) THEN
		UPDATE products SET amount = (SELECT amount FROM products WHERE id = product_id) + delta;
    ELSE
		UPDATE products SET amount = (SELECT amount FROM products WHERE id = product_id) - delta;
    END IF;
END$$


USE `shopdb`$$
DROP TRIGGER IF EXISTS `shopdb`.`order_has_product_AFTER_DELETE` $$
USE `shopdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `shopdb`.`order_has_product_AFTER_DELETE` AFTER DELETE ON `order_has_product` FOR EACH ROW
BEGIN
	UPDATE products SET amount = amount + OLD.count WHERE id = OLD.product_id;
    UPDATE orders SET total = total - OLD.price * OLD.count WHERE id = OLD.order_id;
END$$


USE `shopdb`$$
DROP TRIGGER IF EXISTS `shopdb`.`blocked_users_BEFORE_INSERT` $$
USE `shopdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `shopdb`.`blocked_users_BEFORE_INSERT` BEFORE INSERT ON `blocked_users` FOR EACH ROW
BEGIN
	
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `shopdb`.`categories`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopdb`;
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'cars', 'pictures/cars/main.jpg', NULL, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'clothes', 'pictures/clothes/main.png', NULL, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'men', 'pictures/clothes/men.jpg', 2, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'women', 'pictures/clothes/women.jpg', 2, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'big', 'pictures/cars/big.png', 1, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`categories` (`id`, `name`, `picture`, `parent_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'small', 'pictures/cars/small.png', 1, DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `shopdb`.`products`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopdb`;
INSERT INTO `shopdb`.`products` (`id`, `name`, `picture`, `color`, `manufacture_date`, `description`, `price`, `amount`, `category_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'Mango 77072888-08', 'pictures/clothes/Mango77072888-08.jpg', 'WHITE', '2019-02-01', NULL, 1299, 100, 4, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`products` (`id`, `name`, `picture`, `color`, `manufacture_date`, `description`, `price`, `amount`, `category_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'Mango 77055933-TO', 'pictures/clothes/Mango77055933-TO.jpg', 'BLUE', '2019-02-01', NULL, 1599, 200, 4, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`products` (`id`, `name`, `picture`, `color`, `manufacture_date`, `description`, `price`, `amount`, `category_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'Tom Tailor tom04500014', 'pictures/clothes/tom_tailor19243430107.jpg', 'WHITE', '2020-05-01', NULL, 1549, 400, 4, DEFAULT, DEFAULT);
INSERT INTO `shopdb`.`products` (`id`, `name`, `picture`, `color`, `manufacture_date`, `description`, `price`, `amount`, `category_id`, `create_date`, `last_update`) VALUES (DEFAULT, 'Leo Pride KR3111', 'pictures/clothes/leo_pride_2000660001481.jpg', 'BLACK', '2018-08-01', NULL, 1199, 89, 4, DEFAULT, DEFAULT);

COMMIT;

