USE test

CREATE TABLE stocks_daily_adjusted (
 stockID			VARCHAR(100) PRIMARY KEY,
 PricetimeStamp		date NOT NULL,
 openPrice			DECIMAL NOT NULL,
 highPrice			DECIMAL NOT NULL,
 lowPrice			DECIMAL NOT NULL,
 closePrice			DECIMAL NOT NULL,
 adjusted_close		DECIMAL NOT NULL,
 volume				INT NOT NULL,
 divident_amount	DECIMAL NOT NULL,
 split_coefficient	INT NOT NULL
)

CREATE TABLE protfolios (
 protfolioID    INT IDENTITY (1, 1) PRIMARY KEY,
 StockID          VARCHAR(100) NOT NULL,
 unit           INT NOT NULL,
 FOREIGN KEY (stockID) REFERENCES stocks_daily_adjusted(stockID)
 )

CREATE TABLE USERS (
 username  VARCHAR(100) NOT NULL,
 password VARCHAR(100) NOT NULL,
 name  VARCHAR(100) NOT NULL,
 email  VARCHAR(100) NOT NULL,
 portfolioID  VARCHAR(100) NOT NULL,
 availableFund  Float NOT NULL,
 PRIMARY KEY (username)
)