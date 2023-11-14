CREATE SCHEMA IF NOT EXISTS `saranghaengbok_soap` DEFAULT CHARACTER SET utf8 ;
USE saranghaengbok_soap;

CREATE TABLE IF NOT EXISTS `saranghaengbok_soap`.`log`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `ip` VARCHAR(50) NOT NULL,
    `endpoint` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `saranghaengbok_soap`.`transaction`(
    `transaction_id` INT NOT NULL AUTO_INCREMENT,
    `buyer_username` VARCHAR(45) NOT NULL,
    `seller_username` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `saranghaengbok_soap`.`transaction_items`(
    `transaction_id` INT NOT NULL, 
    `item_id` INT NOT NULL, 
    `quantity` INT NOT NULL,
    CONSTRAINT `fk_transaction`
        FOREIGN KEY (`transaction_id`)
        REFERENCES `saranghaengbok_soap`.`transaction` (`transaction_id`)
        ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
