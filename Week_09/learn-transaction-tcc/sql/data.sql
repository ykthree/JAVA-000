USE `cny_account`;
TRUNCATE table `cny_account`.`cny_account`;
INSERT INTO `cny_account`.`cny_account` (`id`, `user_id`, `balance`, `decrease_freeze`, `increase_freeze`, `create_time`) VALUES ('1', '1', '0', '0', '0', now());
INSERT INTO `cny_account`.`cny_account` (`id`, `user_id`, `balance`, `decrease_freeze`, `increase_freeze`, `create_time`) VALUES ('2', '2', '7', '0', '0', now());
COMMIT;

USE `dollar_account`;
TRUNCATE table `dollar_account`.`dollar_account`;
INSERT INTO `dollar_account`.`dollar_account` (`id`, `user_id`, `balance`, `decrease_freeze`, `increase_freeze`, `create_time`) VALUES ('1', '1', '1', '0', '0', now());
INSERT INTO `dollar_account`.`dollar_account` (`id`, `user_id`, `balance`, `decrease_freeze`, `increase_freeze`, `create_time`) VALUES ('2', '2', '0', '0', '0', now());
COMMIT;