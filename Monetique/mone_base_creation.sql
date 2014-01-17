/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     17/01/2014 16:16:53                          */
/*==============================================================*/


drop table if exists FIDELITY_CREDIT;

drop table if exists FIDELITY_CREDIT_ACCOUNT;

drop table if exists LOYALTY_CARD_TYPE;

/*==============================================================*/
/* Table: FIDELITY_CREDIT                                       */
/*==============================================================*/
create table FIDELITY_CREDIT
(
   ID_FIDELITY_CREDIT   int not null auto_increment,
   ID_FIDELITY_CREDIT_ACCOUNT int not null,
   FIDELITY_CREDIT_DATE datetime,
   AMOUNT               numeric(8,0),
   REPAID_AMOUNT        numeric(8,0),
   IS_REPAID            bool,
   ECHELON_NB           numeric(8,0),
   primary key (ID_FIDELITY_CREDIT)
);

/*==============================================================*/
/* Table: FIDELITY_CREDIT_ACCOUNT                               */
/*==============================================================*/
create table FIDELITY_CREDIT_ACCOUNT
(
   ID_FIDELITY_CREDIT_ACCOUNT int not null auto_increment,
   ID_LOYALTY_CARD_TYPE int not null,
   CUSTOMER_CODE        varchar(255),
   IS_BLACKLISTED       bool,
   BLAKLISTED_DATE      datetime,
   TOTAL_CREDIT_AMOUNT  numeric(8,0),
   TOTAL_REPAID_CREDIT__AMOUNT numeric(8,0),
   primary key (ID_FIDELITY_CREDIT_ACCOUNT),
   key AK_CUSTOMER_CODE (CUSTOMER_CODE)
);

/*==============================================================*/
/* Table: LOYALTY_CARD_TYPE                                     */
/*==============================================================*/
create table LOYALTY_CARD_TYPE
(
   ID_LOYALTY_CARD_TYPE int not null auto_increment,
   CARD_TYPE_CODE       varchar(255),
   MONTLY_CREDIT_LIMIT  numeric(8,0),
   TOTAL_CREDIT_LIMIT   numeric(8,0),
   ECHELON_NB           numeric(8,0),
   primary key (ID_LOYALTY_CARD_TYPE),
   key AK_CARD_TYPE_CODE (CARD_TYPE_CODE)
);

alter table FIDELITY_CREDIT add constraint FK_FIDELITY_CREDIT_ACCOUNT_HAS_FIDELITY_CREDITS foreign key (ID_FIDELITY_CREDIT_ACCOUNT)
      references FIDELITY_CREDIT_ACCOUNT (ID_FIDELITY_CREDIT_ACCOUNT) on delete restrict on update restrict;

alter table FIDELITY_CREDIT_ACCOUNT add constraint FK_FIDELITY_CREDIT_ACCOUNT_HAS_LOYALTY_CARD_TYPE foreign key (ID_LOYALTY_CARD_TYPE)
      references LOYALTY_CARD_TYPE (ID_LOYALTY_CARD_TYPE) on delete restrict on update restrict;

