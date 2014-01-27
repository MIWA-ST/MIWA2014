/*==============================================================*/
/* Nom de SGBD :  PostgreSQL 8                                  */
/* Date de création :  17/01/2014 05:25:06                      */
/*==============================================================*/


drop index ARTICLE_PK;

drop table ARTICLE CASCADE;

drop index BUY2_FK;

drop index BUY_FK;

drop index BUY_PK;

drop table BUY CASCADE;

drop index DELIVERY_PK;

drop table DELIVERY CASCADE;

drop index DEMANDE2_FK;

drop index DEMANDE_FK;

drop index DEMANDE_PK;

drop table DEMANDE CASCADE;

drop index EST_COMPOSEE_DE2_FK;

drop index EST_COMPOSEE_DE_FK;

drop index EST_COMPOSEE_DE_PK;

drop table EST_COMPOSEE_DE CASCADE;

drop index PREVOIE2_FK;

drop index PREVOIE_FK;

drop index PREVOIE_PK;

drop table PREVOIE CASCADE;

drop index APPLIQUE_FK;

drop index PROMOTION_PK;

drop table PROMOTION CASCADE;

drop index RESTOCK_REQUEST_PK;

drop table RESTOCK_REQUEST CASCADE;

drop index RESTOCK_REQUEST_RECEPTION_PK;

drop table RESTOCK_REQUEST_RECEPTION CASCADE;

drop index SALE_PK;

drop table SALE CASCADE;

/*==============================================================*/
/* Table : ARTICLE                                              */
/*==============================================================*/
create table ARTICLE (
   ID_ARTICLE           INT4                 not null,
   REFERENCE            VARCHAR(256)         null,
   NAME_ARTICLE         VARCHAR(256)         null,
   CATEGORY_ARTICLE     VARCHAR(256)         null,
   QUANTITY             VARCHAR(256)         null,
   PROVIDER_PRICE       VARCHAR(256)         null,
   SALES_PRICE          VARCHAR(256)         null,
   EAN                  VARCHAR(256)         null,
   DESCRIPTION          TEXT                 null,
   constraint PK_ARTICLE primary key (ID_ARTICLE)
);


ALTER TABLE public.article OWNER TO postgres;

CREATE SEQUENCE "ARTICLE_ID_ARTICLE_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."ARTICLE_articleid_seq" OWNER TO postgres;

ALTER SEQUENCE "ARTICLE_articleid_seq" OWNED BY article.ID_ARTICLE;

ALTER TABLE ONLY article ALTER COLUMN ID_ARTICLE SET DEFAULT nextval('"ARTICLE_ID_ARTICLE_seq"'::regclass);


/*==============================================================*/
/* Index : ARTICLE_PK                                           */
/*==============================================================*/
create unique index ARTICLE_PK on ARTICLE (
ID_ARTICLE
);

/*==============================================================*/
/* Table : BUY                                                  */
/*==============================================================*/
create table BUY (
   ID_SALE              INT4                 not null,
   REFERENCE            VARCHAR(256)         not null,
   constraint PK_BUY primary key (ID_SALE, REFERENCE)
);

/*==============================================================*/
/* Index : BUY_PK                                               */
/*==============================================================*/
create unique index BUY_PK on BUY (
ID_SALE,
REFERENCE
);

/*==============================================================*/
/* Index : BUY_FK                                               */
/*==============================================================*/
create  index BUY_FK on BUY (
ID_SALE
);

/*==============================================================*/
/* Index : BUY2_FK                                              */
/*==============================================================*/
create  index BUY2_FK on BUY (
REFERENCE
);

/*==============================================================*/
/* Table : DELIVERY                                             */
/*==============================================================*/
create table DELIVERY (
   ID_DELIVERY          INT4                 not null,
   NUMBER               VARCHAR(256)         null,
   SHOP_REFERENCE       VARCHAR(256)         null,
   ORDER_DATE           DATE                 null,
   DELIVERY_DATE        DATE                 null,
   constraint PK_DELIVERY primary key (ID_DELIVERY)
);

/*==============================================================*/
/* Index : DELIVERY_PK                                          */
/*==============================================================*/
create unique index DELIVERY_PK on DELIVERY (
ID_DELIVERY
);

/*==============================================================*/
/* Table : DEMANDE                                              */
/*==============================================================*/
create table DEMANDE (
   ID_RESTOCK_REQUEST   INT4                 not null,
   REFERENCE            VARCHAR(256)         not null,
   constraint PK_DEMANDE primary key (ID_RESTOCK_REQUEST, REFERENCE)
);

/*==============================================================*/
/* Index : DEMANDE_PK                                           */
/*==============================================================*/
create unique index DEMANDE_PK on DEMANDE (
ID_RESTOCK_REQUEST,
REFERENCE
);

/*==============================================================*/
/* Index : DEMANDE_FK                                           */
/*==============================================================*/
create  index DEMANDE_FK on DEMANDE (
ID_RESTOCK_REQUEST
);

/*==============================================================*/
/* Index : DEMANDE2_FK                                          */
/*==============================================================*/
create  index DEMANDE2_FK on DEMANDE (
REFERENCE
);

/*==============================================================*/
/* Table : EST_COMPOSEE_DE                                      */
/*==============================================================*/
create table EST_COMPOSEE_DE (
   ID_DELIVERY          INT4                 not null,
   REFERENCE            VARCHAR(256)         not null,
   constraint PK_EST_COMPOSEE_DE primary key (ID_DELIVERY, REFERENCE)
);

/*==============================================================*/
/* Index : EST_COMPOSEE_DE_PK                                   */
/*==============================================================*/
create unique index EST_COMPOSEE_DE_PK on EST_COMPOSEE_DE (
ID_DELIVERY,
REFERENCE
);

/*==============================================================*/
/* Index : EST_COMPOSEE_DE_FK                                   */
/*==============================================================*/
create  index EST_COMPOSEE_DE_FK on EST_COMPOSEE_DE (
ID_DELIVERY
);

/*==============================================================*/
/* Index : EST_COMPOSEE_DE2_FK                                  */
/*==============================================================*/
create  index EST_COMPOSEE_DE2_FK on EST_COMPOSEE_DE (
REFERENCE
);

/*==============================================================*/
/* Table : PREVOIE                                              */
/*==============================================================*/
create table PREVOIE (
   ID_RESTOCK_REQUEST_RECEPTION INT4                 not null,
   REFERENCE            VARCHAR(256)         not null,
   constraint PK_PREVOIE primary key (ID_RESTOCK_REQUEST_RECEPTION, REFERENCE)
);

/*==============================================================*/
/* Index : PREVOIE_PK                                           */
/*==============================================================*/
create unique index PREVOIE_PK on PREVOIE (
ID_RESTOCK_REQUEST_RECEPTION,
REFERENCE
);

/*==============================================================*/
/* Index : PREVOIE_FK                                           */
/*==============================================================*/
create  index PREVOIE_FK on PREVOIE (
ID_RESTOCK_REQUEST_RECEPTION
);

/*==============================================================*/
/* Index : PREVOIE2_FK                                          */
/*==============================================================*/
create  index PREVOIE2_FK on PREVOIE (
REFERENCE
);

/*==============================================================*/
/* Table : PROMOTION                                            */
/*==============================================================*/
create table PROMOTION (
   ID_PROMOTION         INT4                 not null,
   REFERENCE            VARCHAR(256)         not null,
   PERCENT              INT4                 null,
   BEGIN                DATE                 null,
   "END"                DATE                 null,
   constraint PK_PROMOTION primary key (ID_PROMOTION)
);

/*==============================================================*/
/* Index : PROMOTION_PK                                         */
/*==============================================================*/
create unique index PROMOTION_PK on PROMOTION (
ID_PROMOTION
);

/*==============================================================*/
/* Index : APPLIQUE_FK                                          */
/*==============================================================*/
create  index APPLIQUE_FK on PROMOTION (
REFERENCE
);

/*==============================================================*/
/* Table : RESTOCK_REQUEST                                      */
/*==============================================================*/
create table RESTOCK_REQUEST (
   ID_RESTOCK_REQUEST   INT4                 not null,
   NUMBER               VARCHAR(256)         null,
   BO_REFERENCE         VARCHAR(256)         null,
   BO_ADRESS            VARCHAR(256)         null,
   BO_PHONE             VARCHAR(256)         null,
   DATE_REQUEST         DATE                 null,
   constraint PK_RESTOCK_REQUEST primary key (ID_RESTOCK_REQUEST)
);

/*==============================================================*/
/* Index : RESTOCK_REQUEST_PK                                   */
/*==============================================================*/
create unique index RESTOCK_REQUEST_PK on RESTOCK_REQUEST (
ID_RESTOCK_REQUEST
);

/*==============================================================*/
/* Table : RESTOCK_REQUEST_RECEPTION                            */
/*==============================================================*/
create table RESTOCK_REQUEST_RECEPTION (
   ID_RESTOCK_REQUEST_RECEPTION INT4                 not null,
   ORDER_NUMBER         INT4                 null,
   STATUS               VARCHAR(256)         null,
   COMMENT              TEXT                 null,
   DATE_DELIVERY        DATE                 null,
   constraint PK_RESTOCK_REQUEST_RECEPTION primary key (ID_RESTOCK_REQUEST_RECEPTION)
);

/*==============================================================*/
/* Index : RESTOCK_REQUEST_RECEPTION_PK                         */
/*==============================================================*/
create unique index RESTOCK_REQUEST_RECEPTION_PK on RESTOCK_REQUEST_RECEPTION (
ID_RESTOCK_REQUEST_RECEPTION
);

/*==============================================================*/
/* Table : SALE                                                 */
/*==============================================================*/
create table SALE (
   ID_SALE              INT4                 not null,
   CUSTOMER             VARCHAR(256)         null,
   CUSTOMERNUMBER       VARCHAR(256)         null,
   PAYMENT_MEANS        VARCHAR(256)         null,
   TOTAL                VARCHAR(256)         null,
   DATE_AND_TIME        DATE                 null,
   PAYMENT              VARCHAR(256)         null,
   constraint PK_SALE primary key (ID_SALE)
);

/*==============================================================*/
/* Index : SALE_PK                                              */
/*==============================================================*/
create unique index SALE_PK on SALE (
ID_SALE
);

alter table BUY
   add constraint FK_BUY_BUY_SALE foreign key (ID_SALE)
      references SALE (ID_SALE)
      on delete restrict on update restrict;

alter table BUY
   add constraint FK_BUY_BUY2_ARTICLE foreign key (ID_ARTICLE)
      references ARTICLE (ID_ARTICLE)
      on delete restrict on update restrict;

alter table DEMANDE
   add constraint FK_DEMANDE_DEMANDE_RESTOCK_ foreign key (ID_RESTOCK_REQUEST)
      references RESTOCK_REQUEST (ID_RESTOCK_REQUEST)
      on delete restrict on update restrict;

alter table DEMANDE
   add constraint FK_DEMANDE_DEMANDE2_ARTICLE foreign key (ID_ARTICLE)
      references ARTICLE (ID_ARTICLE)
      on delete restrict on update restrict;

alter table EST_COMPOSEE_DE
   add constraint FK_EST_COMP_EST_COMPO_DELIVERY foreign key (ID_DELIVERY)
      references DELIVERY (ID_DELIVERY)
      on delete restrict on update restrict;

alter table EST_COMPOSEE_DE
   add constraint FK_EST_COMP_EST_COMPO_ARTICLE foreign key (ID_ARTICLE)
      references ARTICLE (ID_ARTICLE)
      on delete restrict on update restrict;

alter table PREVOIE
   add constraint FK_PREVOIE_PREVOIE_RESTOCK_ foreign key (ID_RESTOCK_REQUEST_RECEPTION)
      references RESTOCK_REQUEST_RECEPTION (ID_RESTOCK_REQUEST_RECEPTION)
      on delete restrict on update restrict;

alter table PREVOIE
   add constraint FK_PREVOIE_PREVOIE2_ARTICLE foreign key (ID_ARTICLE)
      references ARTICLE (ID_ARTICLE)
      on delete restrict on update restrict;

alter table PROMOTION
   add constraint FK_PROMOTIO_APPLIQUE_ARTICLE foreign key (ID_ARTICLE)
      references ARTICLE (ID_ARTICLE)
      on delete restrict on update restrict;

