CREATE DATABASE `pama`;

CREATE TABLE `pama`.`patient` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `old_id` SMALLINT,
    `image_name` VARCHAR(70),
    `name` VARCHAR(70) NOT NULL,
    
    `address` VARCHAR(150),
	`birth_day` DATE NOT NULL,
	`is_fermale` tinyint(1) NOT NULL,
	`cell_phone` VARCHAR(12),
	`phone` VARCHAR(15),
	`email` VARCHAR(70),
	`career` VARCHAR(70),	
	`last_visit` DATE,
	`last_surgery` DATE,
	`next_appointment` DATE,
	`patient_level` tinyint,
	`note` VARCHAR(700),
	`medical_personal_info` VARCHAR(15000),
    
    PRIMARY KEY  (`id`) 
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE `pama`.`catagory` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `old_id` SMALLINT,
    `name` VARCHAR(70) NOT NULL,
    `symbol` VARCHAR(70),
    `cat_desc` VARCHAR(200),    
    `cat_type` VARCHAR(150),
    `ref_ids` TEXT,
	`other_data` VARCHAR(2000),
    
    PRIMARY KEY  (`id`) 
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_unicode_ci;
	
CREATE TABLE `pama`.`appointment` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `patient_id` SMALLINT NOT NULL,
    
	`appointment_date` DATE NOT NULL,
	`resolved` tinyint(1) NOT NULL,
	`appointment_type` SMALLINT,
	
    `note` VARCHAR(500),
    `pa_info` VARCHAR(500),
    
    PRIMARY KEY  (`id`) 
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_unicode_ci;