
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
DROP TABLE StatisticCategory ;
DROP TABLE Category ;
DROP TABLE DetailSale;
DROP TABLE Client ;

CREATE TABLE Client (
  numero SERIAL NOT NULL,
  title VARCHAR(10) NOT NULL,
  birthDate TIMESTAMP NOT NULL,
  zipcode INT NOT NULL,
  maritalStatus VARCHAR(45) NOT NULL,
  childrenNb INT NOT NULL,
  loyaltyType VARCHAR(45) NOT NULL,
  PRIMARY KEY (numero));


-- -----------------------------------------------------
-- Table Category
-- -----------------------------------------------------


CREATE TABLE Category (
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (name));


-- -----------------------------------------------------
-- Table Product
-- -----------------------------------------------------


CREATE TABLE Product (
  reference VARCHAR(45) NOT NULL,
  buyingPrice INT NOT NULL,
  sellingPrice VARCHAR(45) NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  margin INT NULL,
  PRIMARY KEY (reference),
  CONSTRAINT fk_Product_Category1
    FOREIGN KEY (categoryName)
    REFERENCES Category (name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


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
  id INT NOT NULL,
  productReference VARCHAR(45) NOT NULL,
  ordered BOOLEAN NULL,
  stockQty INT NOT NULL,
  maxQty INT NOT NULL,
  store VARCHAR(45) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_Stock_Product1
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table Sale
-- -----------------------------------------------------


CREATE TABLE Sale (
  id INT NOT NULL,
  dateTime TIMESTAMP NOT NULL,
  store VARCHAR(45) NOT NULL,
  soldQty VARCHAR(45) NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  supplierTotal INT NULL,
  salesTotal INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_VentesGlobales_Category
    FOREIGN KEY (categoryName)
    REFERENCES Category (name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table DetailSale
-- -----------------------------------------------------


CREATE TABLE DetailSale (
  id SERIAL NOT NULL,
  paymentMean VARCHAR(45) NOT NULL,
  dateTime TIMESTAMP NULL,
  total INT NULL,
  store VARCHAR(45) NULL,
  clientNumero INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_DetailSale_Client1
    FOREIGN KEY (clientNumero)
    REFERENCES Client (numero)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table Product_has_DetailSale
-- -----------------------------------------------------


CREATE TABLE Produit_has_DetailSale (
  productReference VARCHAR(45) NOT NULL,
  detailSaleId INT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (productReference, detailSaleId),
  CONSTRAINT fk_Product_has_DetailSale_Product1
    FOREIGN KEY (productReference)
    REFERENCES Product (reference)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Product_has_DetailSale_DetailSale1
    FOREIGN KEY (detailSaleId)
    REFERENCES DetailSale (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table StatisticStock
-- -----------------------------------------------------


CREATE TABLE StatisticStock (
  dateTime TIMESTAMP NOT NULL,
  isWide BOOLEAN NOT NULL,
  isEmpty BOOLEAN NOT NULL,
  ordered INT NOT NULL,
  store VARCHAR(45) NULL,
  PRIMARY KEY (dateTime),
  CONSTRAINT fk_StatisticStock_Stock1
    FOREIGN KEY (ordered)
    REFERENCES Stock (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table StatisticCategory
-- -----------------------------------------------------


CREATE TABLE StatisticCategory (
  dateTime TIMESTAMP NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (dateTime, categoryName),
  CONSTRAINT fk_StatisticCategory_Category1
    FOREIGN KEY (categoryName)
    REFERENCES Category (name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table StatisticSale
-- -----------------------------------------------------

CREATE TABLE StatisticSale (
  dateTime TIMESTAMP NOT NULL,
  percentageCA DECIMAL(2) NOT NULL,
  evolution DECIMAL(2) NOT NULL,
  ca INT NOT NULL,
  soldQty INT NOT NULL,
  categoryName VARCHAR(45) NOT NULL,
  PRIMARY KEY (dateTime, categoryName),
  CONSTRAINT fk_StatisticSale_Category1
    FOREIGN KEY (categoryName)
    REFERENCES Category (name)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table StatisticPayment
-- -----------------------------------------------------

CREATE TABLE StatisticPayment (
  dateTime TIMESTAMP NOT NULL,
  paymentMean VARCHAR(45) NOT NULL,
  percentageCA DECIMAL(2) NOT NULL,
  ca INT NOT NULL,
  PRIMARY KEY (dateTime, paymentMean));

-- -----------------------------------------------------
-- Table Client_has_Segmentation
-- -----------------------------------------------------

CREATE TABLE Client_has_Segmentation (
  dateTime TIMESTAMP NOT NULL,
  clientNumero VARCHAR(45) NOT NULL,
  categoryStatisticName VARCHAR(45) NOT NULL,
  categoryStatisticDateTime TIMESTAMP NOT NULL,
  PRIMARY KEY (dateTime, clientNumero, categoryStatisticName, categoryStatisticDateTime),
  CONSTRAINT fk_Segmentation_Client1
    FOREIGN KEY (categoryStatisticDateTime, categoryStatisticName)
    REFERENCES StatisticCategory (dateTime, categoryName)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
