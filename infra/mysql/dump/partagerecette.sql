create database if not exists partagerecette;

use partagerecette;

    drop table if exists User;
    drop table if exists Recipe;

/*------------------------------------------*/
/* TABLE : User							*/
/*------------------------------------------*/
create table if not exists User
(
    Use_ID 				int not null auto_increment,
    Use_email 		    varchar(40) not null,
    Use_fullname 		varchar(40) not null,
    Use_password		varchar(100) not null,
    primary key (Use_ID)
    );

/*------------------------------------------*/
/* TABLE : Recipe							*/
/*------------------------------------------*/
create table if not exists Recipe
(
    Rec_ID 				    int not null auto_increment,
    Rec_created_at 		    varchar(40) not null,
    Rec_description 		varchar(40) not null,
    Rec_image		        varchar(100) not null,
    Rec_title               varchar(40) not null,
    Rec_vagetarian          boolean,
    Use_ID                  int not null,
        primary key (Rec_ID),
            constraint
        foreign key (Use_ID) references User(Use_ID) on delete cascade on update restrict
    );