drop procedure if exists doWhile;
DELIMITER //
CREATE PROCEDURE doWhile()
BEGIN
DECLARE i INT DEFAULT 0;
WHILE (i <= 1500) DO
    INSERT INTO `users` (username, password, name, email, portfolioID, availableFund)
    values (i, 'a', 'a b','a@a.com', 1, 100000);
    SET i = i+1;
END WHILE;
END;
//

CALL doWhile();

SET SQL_SAFE_UPDATES = 0;
UPDATE users SET username=CONCAT('user',username) WHERE username NOT LIKE 'user%'
SET SQL_SAFE_UPDATES = 0;
UPDATE users SET portfolioID=CONCAT(username,portfolioID) WHERE portfolioID NOT LIKE 'user%'