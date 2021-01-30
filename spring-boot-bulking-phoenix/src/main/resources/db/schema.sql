

CREATE TABLE IF NOT EXISTS user (
  id  BIGINT  primary key,
  user_name VARCHAR ,
  age BIGINT ,
  address varchar(128)
) ;

UPSERT INTO user (id, user_name, age, address) VALUES (1, 'tom', 32, '合肥');


