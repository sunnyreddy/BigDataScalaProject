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

CREATE TABLE users (
 id				INT IDENTITY (1, 1) PRIMARY KEY,
 userName		VARCHAR(100) NOT NULL,
 protfolioID	INT NOT NULL,
 FOREIGN KEY (protfolioID) REFERENCES protfolios(protfolioID)