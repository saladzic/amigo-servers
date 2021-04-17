CREATE TABLE IF NOT EXISTS `amigo_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(25) NOT NULL DEFAULT '',
  `password` varchar(255) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT '0.00',
  `email` varchar(150) DEFAULT NULL,
  `created_at` int(11) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
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