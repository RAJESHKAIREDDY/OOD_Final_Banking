CREATE TABLE `safebankdb`.`users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` bigint NOT NULL,
  `credit_score` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `safebankdb`.`savings_accounts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` VARCHAR(45) NOT NULL,
  `account_number` BIGINT NOT NULL,
  `account_balance` DOUBLE NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `account_id_UNIQUE` (`account_id` ASC) VISIBLE);
  
  CREATE TABLE `safebankdb`.`credit_cards` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `credit_card_id` VARCHAR(100) NOT NULL,
  `card_number` BIGINT NOT NULL,
  `security_code` INT NOT NULL,
  `pin_number` INT NOT NULL,
  `total_credit_limit` DOUBLE NOT NULL,
  `remaining_credit_limit` DOUBLE NOT NULL,
  `card_category` varchar(45) NOT NULL,
  `card_provider` varchar(45) NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `valid_thru` timestamp NOT NULL,
  `last_due_date` timestamp NULL DEFAULT NULL,
  `last_payment_date` timestamp NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `credit_card_id_UNIQUE` (`credit_card_id` ASC) VISIBLE);
  
  CREATE TABLE `safebankdb`.`transactions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `transaction_id` VARCHAR(100) NOT NULL,
  `transaction_name` VARCHAR(45) NOT NULL,
  `transaction_category` VARCHAR(45) NOT NULL,
  `transaction_type` VARCHAR(45) NOT NULL,
  `transaction_mode` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `transaction_id_UNIQUE` (`transaction_id` ASC) VISIBLE);
  
  CREATE TABLE `safebankdb`.`beneficiary_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `beneficiary_user_id` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `beneficiary_user_id_UNIQUE` (`beneficiary_user_id`)
  );