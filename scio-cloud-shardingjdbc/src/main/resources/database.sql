CREATE DATABASE ds0;
CREATE DATABASE ds1;
USE ds0;
CREATE TABLE `user_info` (
  `id` varchar(26) NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locked` bit(1) DEFAULT NULL,
  `mobile` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `order0` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iyd3du1hn587q5c1gjfw9ksgq` (`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



CREATE TABLE `order1` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iyd3du1hn587q5c1gjfw9ksgq` (`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


USE ds1;
CREATE TABLE `user_info` (
  `id` varchar(26) NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locked` bit(1) DEFAULT NULL,
  `mobile` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `order0` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iyd3du1hn587q5c1gjfw9ksgq` (`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



CREATE TABLE `order1` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iyd3du1hn587q5c1gjfw9ksgq` (`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


