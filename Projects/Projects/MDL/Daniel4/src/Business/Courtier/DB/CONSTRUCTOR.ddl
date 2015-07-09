-- *********************************************

-- * Standard SQL generation                   *

-- *-------------------------------------------*

-- * Generator date:  Mar  5 2007              *

-- * Generation date: Fri Nov 16 00:19:44 2007 *

-- ********************************************* 





-- Database Section

-- ________________ 



create database SystèmeCourtier;





-- DBSpace Section

-- _______________





-- Domains Section

-- ______________ 



create domain TYPE_HEURE as numeric(6);



create domain TYPE_AMOUNT as numeric(30,2);



create domain TYPE_QUANTITY as numeric(30);



create domain TYPE_DATE as numeric(8);



create domain TYPE_STRING as varchar(100);



create domain TYPE_ID as varchar(100);





-- Tables Section

-- _____________ 



create table COU_CompteLiquidites (

     SoldeDisponible TYPE_AMOUNT not null,

     NumCompteLiquidites TYPE_ID not null,

     SoldeCourant TYPE_AMOUNT not null,

     Login TYPE_ID not null,

     constraint ID_COU_CompteLiquidites_ID primary key (NumCompteLiquidites));



create table COU_CompteTitres (

     NumCompteTitres TYPE_ID not null,

     NumCompteLiquidites TYPE_ID not null,

     constraint ID_COU_CompteTitres_ID primary key (NumCompteTitres));



create table COU_CoursTitre (

     NumSociete TYPE_ID not null,

     Heure TYPE_HEURE not null,

     Valeur char(1) not null,

     DateDe TYPE_DATE not null,

     constraint ID_COU_CoursTitre_ID primary key (DateDe, Heure, NumSociete));



create table COU_Courtier (

     MotDePasseBourse TYPE_STRING not null,

     LoginBourse TYPE_STRING not null,

     Bloque TYPE_STRING not null,

     MotDePasse TYPE_STRING not null,

     Login TYPE_ID not null,

     constraint ID_COU_Courtier_ID primary key (Login));



create table COU_InfoBourse (

     EtatCourant TYPE_STRING not null,

     DateCourante TYPE_DATE not null,

     HeureCourante TYPE_HEURE not null);



create table COU_LigneJournal (

     HeureEvenement TYPE_HEURE not null,

     Montant TYPE_AMOUNT,

     Quantite TYPE_QUANTITY,

     TypeOperation TYPE_STRING not null,

     SurTypeObjet TYPE_STRING not null,

     DateEvenement TYPE_DATE not null,

     DejaConsulteParParticulier TYPE_STRING not null,

     DejaConsulteParCourtier TYPE_STRING not null,

     Commentaire TYPE_STRING not null,

     LienRapide TYPE_ID,

     NumeroLigne TYPE_ID not null,

     constraint ID_COU_LigneJournal_ID primary key (NumeroLigne));



create table COU_Liquidation (

     EstEffectue TYPE_STRING not null,

     DateDe TYPE_DATE not null,

     Montant TYPE_AMOUNT not null,

     AutreCourtier TYPE_ID not null,

     Login TYPE_ID not null,

     constraint ID_COU_Liquidation_ID primary key (DateDe, AutreCourtier));



create table COU_Ordre (

     DatePaymentFrais TYPE_DATE,

     EstAToutPrix TYPE_STRING not null,

     EstUnAchat TYPE_STRING not null,

     NumOrdreEnBourse TYPE_ID,

     Etat TYPE_STRING not null,

     NumOrdre TYPE_ID not null,

     QuantiteDesiree TYPE_QUANTITY not null,

     QuantiteRealisee TYPE_QUANTITY not null,

     DateDebut TYPE_DATE not null,

     DateButoir TYPE_DATE not null,

     MontantLimite TYPE_AMOUNT not null,

     NumSociete TYPE_ID not null,

     NumCompteTitres TYPE_ID not null,

     constraint ID_COU_Ordre_ID primary key (NumOrdre));



create table COU_Particulier (

     NomEntreprise TYPE_STRING,

     NumeroTVA TYPE_STRING,

     EstUnePersonneMorale TYPE_STRING not null,

     Bloque TYPE_STRING not null,

     MotDePasse TYPE_STRING not null,

     Login TYPE_ID not null,

     Prenom TYPE_STRING,

     Nom TYPE_STRING,

     S_a_Login TYPE_ID not null,

     constraint ID_COU_Particulier_ID primary key (Login));



create table COU_Politique (

     RegleFacturation TYPE_STRING not null,

     SeuilLimiteLiquidites TYPE_STRING not null,

     FraisEmissionOrdre TYPE_STRING not null,

     DateDe TYPE_ID not null,

     Login TYPE_ID not null,

     constraint ID_COU_Politique_ID primary key (DateDe));



create table COU_SocieteCotee (

     Bloquee TYPE_STRING not null,

     NomSociete TYPE_STRING,

     NumSociete TYPE_ID not null,

     constraint ID_COU_SocieteCotee_ID primary key (NumSociete));



create table COU_Titre (

     NumSociete TYPE_ID not null,

     NumTitre TYPE_ID not null,

     NumOrdre TYPE_ID,

     NumCompteTitres TYPE_ID,

     NumTransaction TYPE_ID,

     constraint ID_COU_Titre_ID primary key (NumTitre, NumSociete));



create table COU_Transaction (

     DateDe TYPE_DATE not null,

     NumTransaction TYPE_ID not null,

     Montant TYPE_AMOUNT not null,

     QuantiteRealisee TYPE_QUANTITY not null,

     NumSociete TYPE_ID not null,

     NumOrdre TYPE_ID not null,

     constraint ID_COU_Transaction_ID primary key (NumTransaction));





-- Constraints Section

-- ___________________ 



alter table COU_CompteLiquidites add constraint FKpossedeEnTresorerie_FK

     foreign key (Login)

     references COU_Particulier;



alter table COU_CompteTitres add constraint FKlieA_FK

     foreign key (NumCompteLiquidites)

     references COU_CompteLiquidites;



alter table COU_CoursTitre add constraint FKhistorise_FK

     foreign key (NumSociete)

     references COU_SocieteCotee;



alter table COU_Liquidation add constraint FKfait_FK

     foreign key (Login)

     references COU_Courtier;



alter table COU_Ordre add constraint FKplaceSurFeuille_FK

     foreign key (NumSociete)

     references COU_SocieteCotee;



alter table COU_Ordre add constraint FKconstitueLesTitres_FK

     foreign key (NumCompteTitres)

     references COU_CompteTitres;



alter table COU_Particulier add constraint FKS_adresseA_FK

     foreign key (S_a_Login)

     references COU_Courtier;



alter table COU_Politique add constraint FKsuit_FK

     foreign key (Login)

     references COU_Courtier;



alter table COU_Titre add constraint FKlibere_par_FK

     foreign key (NumSociete)

     references COU_SocieteCotee;



alter table COU_Titre add constraint FKglobalise_FK

     foreign key (NumOrdre)

     references COU_Ordre;



alter table COU_Titre add constraint FKcontient_FK

     foreign key (NumCompteTitres)

     references COU_CompteTitres;



alter table COU_Titre add constraint FKcompose_FK

     foreign key (NumTransaction)

     references COU_Transaction;



alter table COU_Transaction add constraint FKs_effectue_FK

     foreign key (NumSociete)

     references COU_SocieteCotee;



alter table COU_Transaction add constraint FKengendre_FK

     foreign key (NumOrdre)

     references COU_Ordre;





-- Index Section

-- _____________ 



create unique index ID_COU_CompteLiquidites_IND

     on COU_CompteLiquidites (NumCompteLiquidites);



create index FKpossedeEnTresorerie_IND

     on COU_CompteLiquidites (Login);



create unique index ID_COU_CompteTitres_IND

     on COU_CompteTitres (NumCompteTitres);



create index FKlieA_IND

     on COU_CompteTitres (NumCompteLiquidites);



create unique index ID_COU_CoursTitre_IND

     on COU_CoursTitre (DateDe, Heure, NumSociete);



create index FKhistorise_IND

     on COU_CoursTitre (NumSociete);



create unique index ID_COU_Courtier_IND

     on COU_Courtier (Login);



create unique index ID_COU_LigneJournal_IND

     on COU_LigneJournal (NumeroLigne);



create unique index ID_COU_Liquidation_IND

     on COU_Liquidation (DateDe, AutreCourtier);



create index FKfait_IND

     on COU_Liquidation (Login);



create unique index ID_COU_Ordre_IND

     on COU_Ordre (NumOrdre);



create index FKplaceSurFeuille_IND

     on COU_Ordre (NumSociete);



create index FKconstitueLesTitres_IND

     on COU_Ordre (NumCompteTitres);



create unique index ID_COU_Particulier_IND

     on COU_Particulier (Login);



create index FKS_adresseA_IND

     on COU_Particulier (S_a_Login);



create unique index ID_COU_Politique_IND

     on COU_Politique (DateDe);



create index FKsuit_IND

     on COU_Politique (Login);



create unique index ID_COU_SocieteCotee_IND

     on COU_SocieteCotee (NumSociete);



create unique index ID_COU_Titre_IND

     on COU_Titre (NumTitre, NumSociete);



create index FKlibere_par_IND

     on COU_Titre (NumSociete);



create index FKglobalise_IND

     on COU_Titre (NumOrdre);



create index FKcontient_IND

     on COU_Titre (NumCompteTitres);



create index FKcompose_IND

     on COU_Titre (NumTransaction);



create unique index ID_COU_Transaction_IND

     on COU_Transaction (NumTransaction);



create index FKs_effectue_IND

     on COU_Transaction (NumSociete);



create index FKengendre_IND

     on COU_Transaction (NumOrdre);



