DROP TABLE IF EXISTS awards_given;
DROP TABLE IF exists awards;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS alliances;

CREATE TABLE alliances (
  alliance_tag varchar(10) NOT NULL,
  alliance_name varchar(30) NOT NULL,
  num_members int NOT NULL,
  alliance_leader varchar(30) NOT NULL,
  alliance_power bigint NOT NULL,
  alliance_updated DATE,
  UNIQUE KEY (alliance_tag),
  PRIMARY KEY (alliance_tag)
  );

CREATE TABLE teams (
  team_id int NOT NULL AUTO_INCREMENT,
  team_name varchar(15) NOT NULL,
  team_refine varchar(15) NOT NULL,
  two_system_defense varchar(10) NOT NULL,
  three_system_defense varchar(10) NOT NULL,
  PRIMARY KEY (team_id)
  );

CREATE TABLE players (
  player_id int NOT NULL AUTO_INCREMENT,
  alliance_tag varchar(4) NOT NULL,
  player_name varchar(30) NOT NULL,
  team_id int,
  pwr int,
  resources_mined bigint,
  pvp_total int,
  pvp_damage bigint,
  kd_ratio decimal(4, 2),
  pve int,
  pve_damage varchar(30),
  lvl int,
  player_updated DATE,
  act boolean,
  PRIMARY KEY (player_id),
  UNIQUE KEY (player_id),
  FOREIGN KEY (alliance_tag) REFERENCES alliances (alliance_tag),
  FOREIGN KEY (team_id) REFERENCES teams (team_id)
);

CREATE TABLE awards (
  award_id int NOT NULL AUTO_INCREMENT,
  award_name varchar(25) NOT null,
  PRIMARY KEY (award_id)
);
CREATE TABLE awards_given (
  award_id int NOT NULL,
  player_id int NOT NULL,
  FOREIGN KEY (award_id) REFERENCES awards (award_id) ON DELETE CASCADE,
  FOREIGN KEY (player_id) REFERENCES players (player_id)
  );