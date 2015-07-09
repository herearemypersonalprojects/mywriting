import java.util.Iterator;

import AbstractDataBourse.LigneFeuilleMarche;
import AbstractDataBourse.SocieteCotee;
	import Fonctions.Euro;
import SQL.*;



public class BUILD_BOURSE {

	
		public static void main(String[] args) {
			// idée, récupérer tous les noms de fichiers présents dans /DDL/SYSTEM-COURTIER
			/*		
			Chers membres du groupe 3, => uniquement à partir de l'école, connexion distante refusée...

			Voici les informations relatives au serveur Oracle9i (Release 9.0.1.0.0) sur sunset...

			host : sunset.info.fundp.ac.be
			port : 1521
			SID  : sid1

			Ainsi que les informations relatives aux deux comptes qui vous ont été attribués...

			login : mdl3a
			mot de passe : mdp3a

			login : mdl3b
			mot de passe : mdp3b
	*/
			//SQLUser usr = new SQLUser("system", "nema19", new SQLDB("138.48.207.225","1521","mydev"));
			SQLUser usr = new SQLUser("mdl3a", "mdp3a", new SQLDB("sunset.info.fundp.ac.be","1521","sid1"));
			
			boolean destroy = true;
			boolean build = true;
			boolean dosmthg = true;
			
			String prefix = "BOU";
			
			if (destroy)
			{
			DDL.SYSTEMBOURSIER.Destroy todo1 = new DDL.SYSTEMBOURSIER.Destroy(usr);
			if(todo1.make(prefix))
				System.out.println("Bien détruite");
			else
				System.out.println("Erreur dans la destruction");
			}
			
			if (build)
			{
			DDL.SYSTEMBOURSIER.Build todo2 = new DDL.SYSTEMBOURSIER.Build(usr);
			if(todo2.make(prefix))
			
				System.out.println("Bien construite");
				
			
			else
				System.out.println("Erreur dans la construction");
			}
			
			if (dosmthg)
			{
				
			
			SQL.SQLQuery ajout = Fonctions.SQLRequestGenerator
			.BOUCreateCourtier(prefix, "logbourse", "passbourse");
	
			if (usr.execBatchQuery(ajout, true)) 
			{
				System.out.println("courtier ajout  !");
			} 
			else 
			{
			System.out.println("courtier pas ajout  !!!");
			}
	
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateSocieteCotee(prefix, "Nintendo");
		
			if (usr.execBatchQuery(ajout, true))
			{
				System.out.println("societe cotee ajoute");
			}
			else
			{
				System.out.println("prob soci");
			}
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "TRUE", "ordre1", "10", "0000000", "0",new Euro("69,69"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok1");
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "TRUE", "ordre2", "9", "0000000", "0",new Euro("69,68"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok2");
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "TRUE", "ordre3", "8", "0000000", "0",new Euro("69,67"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok3");
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "TRUE", "ordre5", "5", "0000000", "0",new Euro("69,67"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok5");
			
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "TRUE", "ordre4", "7", "0000000", "0",new Euro("69,66"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok4");
			
			ajout = Fonctions.SQLRequestGenerator.BOUCreateOrdre(prefix, "FALSE", "FALSE", "ordre6", "4", "00000000", "0", new Euro("69,67"), "logbourse", "Nintendo");
			if(usr.execBatchQuery(ajout, true)) System.out.println("ok6");
			
			
			}
			System.out.println("something");
		
		
			SocieteCotee cot = new SocieteCotee(prefix, usr, "Nintendo");
			System.out.println(cot.getBloqueeAutomatiquement());
			System.out.println(cot.getBloqueeParAutorite());
			System.out.println(cot.getNomSociete());
			System.out.println(cot.getNumeroDeSociete());
			Iterator it = cot.getLigneFeuilleMarche().iterator();
			System.out.println("-------- feuille de marche ------------");
			System.out.println(cot.getNumeroDeSociete());
			System.out.println("Demande\t\tMontant\tOffres\t\tminimum");
			while (it.hasNext())
			{
				LigneFeuilleMarche lig = (LigneFeuilleMarche) it.next();
				System.out.println(lig.getQuantiteAchat() + "\t" + lig.getQuantiteCumuleeAchat() + "\t" + lig.getMontant() + "\t" + lig.getQuantiteVente() + "\t" + lig.getQuantiteCumuleeVente() + "\t" + lig.getQuantiteCumuleeMin());
			}
			System.out.println("---------------------------------------");
			
		} // fin du main

	
}
