DROP DATABASE IF EXISTS test1;
DROP DATABASE IF EXISTS test2;
CREATE DATABASE test1 charset=utf8mb4;
CREATE DATABASE test2 charset=utf8mb4;
USE test1;
DROP TABLE IF EXISTS test1;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `pass_word` varchar(255) DEFAULT NULL,
  `user_sex` varchar(2) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

delimiter #
drop procedure if exists insertusers;
create procedure insertusers()
  begin
    declare i int;
    set i = 1;
    while i < 11 do
      insert into users(user_name, pass_word, user_sex, nick_name)
      values(concat('test1_cd',i),concat('test1_password',i), round(rand()*1),concat('test1_nick_name',i));
      set i = i + 1;
    end while;
  end #
delimiter ;

CALL insertusers();

USE test2;
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `pass_word` varchar(255) DEFAULT NULL,
  `user_sex` varchar(2) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

delimiter #
drop procedure if exists insertusers;
create procedure insertusers()
  begin
    declare i int;
    set i = 1;
    while i < 11 do
      insert into users(user_name, pass_word, user_sex, nick_name)
      values(concat('test2_cd',i),concat('test2_password',i), round(rand()*1),concat('test2_nick_name',i));
      set i = i + 1;
    end while;
  end #
delimiter ;

CALL insertusers();