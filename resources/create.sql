#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: user
#------------------------------------------------------------

CREATE TABLE user
(
    id              Int Auto_increment NOT NULL,
    password        Varchar(300)       NOT NULL,
    username        Varchar(100)       NOT NULL,
    account_balance Double NOT NULL,
    is_enabled      Bool   NOT NULL,
    email           Varchar(150)       NOT NULL,
    CONSTRAINT user_AK UNIQUE (email),
    CONSTRAINT user_PK PRIMARY KEY (id)
) ENGINE = InnoDB;


#------------------------------------------------------------
# Table: intern_transaction
#------------------------------------------------------------

CREATE TABLE intern_transaction
(
    id           Int Auto_increment NOT NULL,
    date         Date               NOT NULL,
    is_completed Bool               NOT NULL,
    amount Double NOT NULL,
    taxe   Double NOT NULL,
    id_friend    Int                NOT NULL,
    label        Varchar(400)       NOT NULL,
    id_user      Int                NOT NULL,
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
    amount Double NOT NULL,
    taxe   Double NOT NULL,
    id_user Int                NOT NULL,
    CONSTRAINT extern_transaction_PK PRIMARY KEY (id),
    CONSTRAINT extern_transaction_user_FK FOREIGN KEY (id_user) REFERENCES user (id)
) ENGINE = InnoDB;


#------------------------------------------------------------
# Table: friendship
#------------------------------------------------------------

CREATE TABLE friendship
(
    id        Int Auto_increment NOT NULL,
    is_active Bool               NOT NULL,
    id_friend Int                NOT NULL,
    id_user   Int                NOT NULL,
    CONSTRAINT friendship_PK PRIMARY KEY (id),
    CONSTRAINT friendship_user_FK FOREIGN KEY (id_user) REFERENCES user (id)
) ENGINE = InnoDB;

