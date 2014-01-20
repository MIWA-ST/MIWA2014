
-- -----------------------------------------------------
-- Table Client
-- -----------------------------------------------------
DROP TABLE Client_has_Segmentation;
DROP TABLE StatisticSale;
DROP TABLE StatisticPayment ;
DROP TABLE Product_has_DetailSale;
DROP TABLE Promotion ;
DROP TABLE StatisticStock ;
DROP TABLE Stock ;
DROP TABLE Product ;
DROP TABLE Sale ;
DROP TABLE DetailSale;
DROP TABLE Client ;

CREATE TABLE Client (
  numero INT NOT NULL,
  title VARCHAR(10) NOT NULL,
  birthDate TIMESTAMP NOT NULL,
  zipcode INT NOT NULL,
  maritalStatus VARCHAR(45) NOT NULL,
  children BOOLEAN NOT NULL,
  loyaltyType INT NOT NULL,
  PRIMARY KEY (numero));

-- -----------------------------------------------------
-- Table Product
-- -----------------------------------------------------


CREATE TABLE Product (
  reference VARCHAR(45) NOT NULL,
  buyingPrice VARCHAR(45) NOT NULL,
  sellingPrice VARCHAR(45) NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  margin VARCHAR(45) NOT NULL,
  PRIMARY KEY (reference));


-- -----------------------------------------------------
-- Table Promotion
-- -----------------------------------------------------


CREATE TABLE Promotion (
  id SERIAL NOT NULL,
  productReference VARCHAR(45) NOT NULL,
  beginDate DATE NULL,
  endDate DATE NULL,
  percentage INT NULL,
  store VARCHAR(45) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_Promotion_Product
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table Stock
-- -----------------------------------------------------


CREATE TABLE Stock (
  datetime TIMESTAMP NOT NULL,
  productReference VARCHAR(45) NOT NULL,
  ordered BOOLEAN NULL,
  stockQty INT NOT NULL,
  maxQty INT NOT NULL,
  store VARCHAR(45) NULL,
  PRIMARY KEY (datetime, productreference),
  CONSTRAINT fk_Stock_Product1
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table Sale
-- -----------------------------------------------------


CREATE TABLE Sale (
  id SERIAL NOT NULL,
  dateTime TIMESTAMP NOT NULL,
  store VARCHAR(45) NOT NULL,
  soldQty VARCHAR(45) NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  supplierTotal INT NULL,
  salesTotal INT NULL,
  source VARCHAR(10) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table DetailSale
-- -----------------------------------------------------


CREATE TABLE DetailSale (
  id SERIAL NOT NULL,
  paymentMean VARCHAR(45) NOT NULL,
  datetime TIMESTAMP NOT NULL,
  total INT NULL,
  store VARCHAR(45) NULL,
  clientNumero INT NOT NULL,
  source VARCHAR(10) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_DetailSale_Client1
    FOREIGN KEY (clientNumero)
    REFERENCES Client (numero)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


-- -----------------------------------------------------
-- Table Product_has_DetailSale
-- -----------------------------------------------------


CREATE TABLE Product_has_DetailSale (
  productReference VARCHAR(45) NOT NULL,
  detailSaleId INT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (productReference, detailSaleId),
  CONSTRAINT fk_Product_has_DetailSale_Product1
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_Product_has_DetailSale_DetailSale1
    FOREIGN KEY (detailSaleId)
    REFERENCES DetailSale (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

-- -----------------------------------------------------
-- Table StatisticStock
-- -----------------------------------------------------


CREATE TABLE StatisticStock (
  dateTime TIMESTAMP NOT NULL,
  isWide BOOLEAN NOT NULL,
  isEmpty BOOLEAN NOT NULL,
  ordered BOOLEAN NOT NULL,
  store VARCHAR(45) NULL,
  productReference VARCHAR(45) NOT NULL,
  PRIMARY KEY (dateTime),
  CONSTRAINT fk_StatisticStock_Product1
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

-- -----------------------------------------------------
-- Table StatisticSale
-- -----------------------------------------------------

CREATE TABLE StatisticSale (
  dateTime TIMESTAMP NOT NULL,
  percentageCA VARCHAR(45) NOT NULL,
  evolution VARCHAR(45) NOT NULL,
  ca VARCHAR(45) NOT NULL,
  soldQty INT NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  source VARCHAR(10) NOT NULL,
  PRIMARY KEY (dateTime, categoryName));


-- -----------------------------------------------------
-- Table StatisticPayment
-- -----------------------------------------------------

CREATE TABLE StatisticPayment (
  dateTime TIMESTAMP NOT NULL,
  paymentMean VARCHAR(45) NOT NULL,
  percentageCA VARCHAR(45) NOT NULL,
  ca INT NOT NULL,
  PRIMARY KEY (dateTime, paymentMean));

-- -----------------------------------------------------
-- Table Client_has_Segmentation
-- -----------------------------------------------------

CREATE TABLE Client_has_Segmentation (
  dateTime TIMESTAMP NOT NULL,
  clientNumero INT NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (dateTime, clientNumero, categoryName),
  CONSTRAINT fk_Segmentation_Client1
    FOREIGN KEY (clientNumero)
    REFERENCES Client (numero)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
