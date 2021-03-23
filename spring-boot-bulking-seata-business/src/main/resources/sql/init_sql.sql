
数据库：db_seata_1

create table account (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) NOT NULL ,
`money` int(11)  DEFAULT 0 ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 ;


insert into account(id,user_id,money) value (1,"101",500);
insert into account(id,user_id,money) value (2,"102",500);


数据库：db_seata_2

create table `order`(
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) NOT NULL ,
`commodity_code` varchar(255) ,
`count` int(11) default 0,
`money` int(11) default  0,
PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 ;



数据库：db_seata_3

create table storage(
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`commodity_code` varchar(255) default  null,
`count` int(11) default 0,
PRIMARY KEY (`id`),
unique key (`commodity_code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 ;


insert into storage(id,commodity_code,count) value (1,'6666',1000)


CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



