#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: user
#------------------------------------------------------------

CREATE TABLE user
(
    id              Int Auto_increment NOT NULL,
    password        Varchar(20)        NOT NULL,
    username        Varchar(100)       NOT NULL,
    account_balance Float              NOT NULL,
    email           Varchar(150)       NOT NULL,
    CONSTRAINT user_AK UNIQUE (email),
    CONSTRAINT user_PK PRIMARY KEY (id)
) ENGINE = InnoDB;


#------------------------------------------------------------
# Table: intern_transaction
#------------------------------------------------------------

CREATE TABLE intern_transaction
(
    id        Int Auto_increment NOT NULL,
    date      Date               NOT NULL,
    status    Varchar(2)         NOT NULL,
    amount    Decimal(5, 2)      NOT NULL,
    taxe      Decimal(3)         NOT NULL,
    id_sender Int                NOT NULL,
    id_user   Int                NOT NULL,
    CONSTRAINT intern_transaction_PK PRIMARY KEY (id),
    CONSTRAINT intern_transaction_user_FK FOREIGN KEY (id_user) REFERENCES user (id)
) ENGINE = InnoDB;


#------------------------------------------------------------
# Table: extern_transaction
#------------------------------------------------------------

CREATE TABLE extern_transaction
(
    id      Int Auto_increment NOT NULL,
    account Varchar(50)        NOT NULL,
    date    Date               NOT NULL,
    type    Char(1)            NOT NULL,
    amount  Float              NOT NULL,
    taxe    Float              NOT NULL,
    id_user Int                NOT NULL,
    CONSTRAINT extern_transaction_PK PRIMARY KEY (id),
    CONSTRAINT extern_transaction_user_FK FOREIGN KEY (id_user) REFERENCES user (id)
) ENGINE = InnoDB;


#------------------------------------------------------------
# Table: friendship
#------------------------------------------------------------

CREATE TABLE friendship
(
    id       Int Auto_increment NOT NULL,
    status   Varchar(3)         NOT NULL,
    friendId Int                NOT NULL,
    id_user  Int                NOT NULL,
    CONSTRAINT friendship_PK PRIMARY KEY (id),
    CONSTRAINT friendship_user_FK FOREIGN KEY (id_user) REFERENCES user (id)
) ENGINE = InnoDB;


