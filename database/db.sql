CREATE TABLE IF NOT EXISTS `amigo_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(150) NOT NULL DEFAULT '',
  `password` varchar(255) DEFAULT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `balance` decimal(10,2) DEFAULT '0.00',
  `created_at` int(11) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_ticket` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `author_id` int(11) NOT NULL,
  `department` varchar(150) NOT NULL,
  `priority` varchar(50) NOT NULL,
  `heading` varchar(150) NOT NULL,
  `message` text NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_ticket_msg` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `created_at` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `amigo_session` (
  `token` varchar(15) NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `user_agent` varchar(100) DEFAULT NULL,
  `ip_address` varchar(100) DEFAULT NULL,
  `created_at` int(11) unsigned NOT NULL,
  `last_action` int(11) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `amigo_log_system` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `type` varchar(10) NOT NULL,
 `heading_block` varchar(150) NOT NULL,
 `message_block` varchar(150) NOT NULL,
 `created_at` int(11) unsigned NOT NULL,
 `active` tinyint(1) NOT NULL DEFAULT '1',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `amigo_log_user` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `user_id` int(11) NOT NULL,
 `type` varchar(10) NOT NULL,
 `heading_block` varchar(150) NOT NULL,
 `message_block` varchar(150) NOT NULL,
 `created_at` int(11) unsigned NOT NULL,
 `active` tinyint(1) NOT NULL DEFAULT '1',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `amigo_text_block` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(150) NOT NULL,
 `lang` varchar(5) NOT NULL,
 `text` varchar(250) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_cart` (
  `id` varchar(72) NOT NULL,
  `created_at` int(11) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_cart_item` (
  `id` int(11) NOT NULL,
  `cart_id` varchar(72) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_product` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `name_id` varchar(150) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `combi_product_id` int(11) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amigo_transaction` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `tax` decimal(10,2) NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `paypal_order_id` varchar(100) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `transaction_type` varchar(50) NOT NULL,
  `paid` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` int(11) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;