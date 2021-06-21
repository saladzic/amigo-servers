INSERT INTO `amigo_user` (`id`, `email`, `password`, `admin`, `balance`,created_at)
VALUES
	(1, 'angelo@example.com', 'ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff', 1, 50.10, UNIX_TIMESTAMP());

INSERT INTO `amigo_ticket` (`id`, `author_id`, `heading`, `message`, `created_at`) VALUES (NULL, '1', 'Ein Test-Ticket', 'Message des Test-Tickets', '1624025504');

INSERT INTO `amigo_ticket_msg` (`id`, `ticket_id`, `user_id`, `msg`, `created_at`) VALUES
(1, 1, 1, 'Test-Message 1', 1624025504),
(2, 1, 1, 'Test-Message 2', 1624025530);

INSERT INTO `amigo_product` (`id`, `name`, `name_id`, `quantity`, `price`, `combi_product_id`) VALUES
(1, 'CPU vCore', 'cpu', 10, '1.50', 1),
(2, 'RAM GB', 'ram', 30, '1.50', 1),
(3, 'Dedicated Server #585\r\nAMD Ryzen 3700X\r\n32 GB RAM', '', 1, '25.00', 0);