
数据库：ds0

CREATE TABLE `user_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int(11) NOT NULL  COMMENT '年龄',
  `address` varchar(128) COMMENT '地址',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `user_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int(11) NOT NULL  COMMENT '年龄',
  `address` varchar(128) COMMENT '地址',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


delete  from user_0;
delete  from user_2;


数据库：ds1

CREATE TABLE `user_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int(11) NOT NULL  COMMENT '年龄',
  `address` varchar(128) COMMENT '地址',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `user_3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int(11) NOT NULL  COMMENT '年龄',
  `address` varchar(128) COMMENT '地址',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


delete  from user_1;
delete  from user_3;



