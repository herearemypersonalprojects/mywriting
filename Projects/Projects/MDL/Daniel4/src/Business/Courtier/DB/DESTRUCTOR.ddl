-- DESTRUCTOR.ddl
-- Le dimanche 4 novembre 2007
-- Script permettant l'effacement complet de la base de données Coutier

-- Suppression des tables, de leurs contraintes et de leurs triggers

drop table COU_courtier cascade constraints;
drop table COU_particulier cascade constraints;
drop table COU_ordre cascade constraints;
drop table COU_societecotee cascade constraints;
drop table COU_transaction cascade constraints;
drop table COU_comptetitres cascade constraints;
drop table COU_compteliquidites cascade constraints;
drop table COU_lignejournal cascade constraints;
drop table COU_titre cascade constraints;
drop table COU_politique cascade constraints;
drop table COU_courstitre cascade constraints;
drop table COU_liquidation cascade constraints;
drop table COU_infobourse cascade constraints;