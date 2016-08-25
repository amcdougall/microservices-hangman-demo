drop table T_WORD if exists;
drop table T_GAME if exists;


create table T_WORD (ID bigint identity primary key, WORD varchar(50));
create table T_GAME (ID binary(100), WORD varchar(50), NUM_LETTERS int, GUESSES int, LETTERPOSITIONS VARCHAR(100) );

                        

