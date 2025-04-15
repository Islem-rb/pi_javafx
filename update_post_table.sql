-- Backup existing table
DROP TABLE IF EXISTS post_backup;
CREATE TABLE post_backup SELECT * FROM post;

-- Drop existing post table
DROP TABLE IF EXISTS post;

-- Create user table if not exists
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(180) NOT NULL,
  `roles` longtext NOT NULL COMMENT '(DC2Type:json)',
  `password` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_8D93D649E7927C74` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Create new post table
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` longtext DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_5A8A6C8DA76ED395` (`user_id`),
  CONSTRAINT `FK_5A8A6C8DA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Insert sample data
INSERT INTO `post` (`id`, `user_id`, `title`, `description`, `image`, `created_at`, `updated_at`) VALUES
(13, 1, 'testtttt', 'rzzzzzzzzzzzzzzzzzzzaannnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn', 'C:\\Users\\Tassnim Missaoui\\Downloads\\ChatGPT Image Apr 3, 2025, 02_03_06 AM.png', '2025-04-03 23:49:37', '2025-04-03 23:50:34'),
(14, 1, 'Agricultura', 'Te explicamos qué es la agricultura, cuál es su historia y qué tipos hay. Además, cuáles son las etapas del ciclo agrícola y importancia de la agricultura.', 'C:\\Users\\Tassnim Missaoui\\Downloads\\agricultura-e1551193452226.jpg', '2025-04-04 00:28:41', NULL),
(15, 1, 'beauty of nature', 'I\'m a biologist. To show the beauty of nature is my goal. \"es gibt überall Blumen für den, der sie sehen will\" Henri Matisse (\"Il y a des fleurs partout pour qui veut bien les voir.\")', 'C:\\Users\\Tassnim Missaoui\\Downloads\\strawberries-8177601_1280.jpg', '2025-04-04 00:29:59', NULL),
(16, 1, 'Welcome to VietNam', 'Welcome to Vietnam vous invite à découvrir un pays aux paysages époustouflants, une cuisine savoureuse et une riche histoire. De la baie d\'Ha Long aux rizières de Sapa, en passant par Hanoï et Hô Chi Minh-Ville, le Vietnam mêle tradition et modernité pour une aventure inoubliable.', 'C:\\Users\\Tassnim Missaoui\\Downloads\\countryside-6700296_1280.jpg', '2025-04-04 00:32:19', NULL);

-- Insert sample user if not exists
INSERT INTO `user` (`id`, `email`, `roles`, `password`, `nom`, `prenom`, `role`) VALUES
(1, 'admin@example.com', '[]', '$2y$13$IMalnQpo7xfZD5FJGbEadOcqyj2mi/NQbQiI8v2wBXfjZ4PYKzXCy', 'Admin', 'User', 'medecin');

-- Reset auto increment
ALTER TABLE `post` AUTO_INCREMENT = 17; 