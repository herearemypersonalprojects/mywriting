import java.util.Iterator;

//import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;

import AbstractDataCourtier.CompteLiquidites;
import AbstractDataCourtier.CompteTitres;
import AbstractDataCourtier.Courtier;
import AbstractDataCourtier.Ordre;
import AbstractDataCourtier.Particulier;
import AbstractDataCourtier.SocieteCotee;
import Fonctions.Euro;
import SQL.*;

public class BUILD_COURTIER {

	public static void main(String[] args) {
		// id e, r cup rer tous les noms de fichiers pr sents dans
		// /DDL/SYSTEM-COURTIER
		/*
		 * Chers membres du groupe 3, => uniquement   partir de l' cole,
		 * connexion distante refus e...
		 * 
		 * Voici les informations relatives au serveur Oracle9i (Release
		 * 9.0.1.0.0) sur sunset...
		 * 
		 * host : sunset.info.fundp.ac.be port : 1521 SID : sid1
		 * 
		 * Ainsi que les informations relatives aux deux comptes qui vous ont
		 *  t  attribu s...
		 * 
		 * login : mdl3a mot de passe : mdp3a
		 * 
		 * login : mdl3b mot de passe : mdp3b
		 */
		SQLUser usr = new SQLUser("mdl3a", "mdp3a", new SQLDB("sunset.info.fundp.ac.be",
				"1521", "sid1"));
		// SQLUser usr = new SQLUser("mdl3a", "mdp3a", new
		// SQLDB("sunset.info.fundp.ac.be","1521","sid1"));

		boolean destroy = true;
		boolean build = true;
		boolean dosmthg = true;

		/*
		 * boolean destroy = false; boolean build = false; boolean dosmthg =
		 * false;
		 */
		String prefix = "COU_";

		if (destroy) {
			DDL.SYSTEMCOURTIER.Destroy todo1 = new DDL.SYSTEMCOURTIER.Destroy(
					usr);
			if (todo1.make(prefix))
				System.out.println("Bien d truite");
			else
				System.out.println("Erreur dans la destruction");
		}

		if (build) {
			DDL.SYSTEMCOURTIER.Build todo2 = new DDL.SYSTEMCOURTIER.Build(usr);
			if (todo2.make(prefix))

				System.out.println("Bien construite");

			else
				System.out.println("Erreur dans la construction");
		}

		if (dosmthg) {
			SQL.SQLQuery ajout = Fonctions.SQLRequestGenerator
					.COUCreateCourtier(prefix, "Daniel", "Clarisse",
							"logbourse", "passbourse");
			System.out.println(ajout.getQuery());

			if (usr.execBatchQuery(ajout, true)) {
				System.out.println("courtier ajout  !");
			} else {
				System.out.println("courtier pas ajout  !!!");
			}

			ajout = Fonctions.SQLRequestGenerator.COUCreatePolitique(prefix,
					"EMISSION", new Euro("-1000,0"), new Euro("5,9"), "Daniel",
					"20070905");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("ok politique");
			else
				System.out.println("ko politique !");

			for (int j = 0; j < 10; j += 1) {
				ajout = Fonctions.SQLRequestGenerator.COUCreateParticulier(
						prefix, "Clarisse" + j, "violette", "009-12345323",
						"Daniel");
				if (usr.execBatchQuery(ajout, true))
					System.out.println("particulier ajout  !");
				else
					System.out.println("probl me ajout particulier !");
			}

			ajout = Fonctions.SQLRequestGenerator.COUCreateParticulier(prefix,
					"Audrey", "mdp", "Compère", "Audrey", "Daniel");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("ok audrey");
			else
				System.out.println("prob audrey");

			/**/
			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteLiquidites(
					prefix, new Euro("13,11"), "354-8557-659", "Audrey");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de liquidites cree !");
			else
				System.out.println("probl...");

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteLiquidites(
					prefix, new Euro("3636"), "253-4978-4897", "Audrey");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de liquidites cree !");
			else
				System.out.println("probl...");

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteTitres(prefix,
					"569-titres-5245", "354-8557-659");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de titres cree !");
			else
				System.out.println("prol !");

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteTitres(prefix,
					"2685-titres2-4723", "354-8557-659");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de titres cree !");
			else
				System.out.println("prol !");

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteTitres(prefix,
					"2015-titres3-9896", "253-4978-4897");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de titres cree !");
			else
				System.out.println("prol !");
			/**/

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteLiquidites(
					prefix, new Euro("77,77"), "azrety", "Clarisse8");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de liquidites cree !");
			else
				System.out.println("probl...");

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteLiquidites(
					prefix, new Euro("0"), "azert2", "Clarisse8");
			usr.execBatchQuery(ajout, true);

			ajout = Fonctions.SQLRequestGenerator.COUCreateCompteTitres(prefix,
					"02134", "azrety");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("compte de titres cree !");
			else
				System.out.println("prol !");

			ajout = Fonctions.SQLRequestGenerator.COUCreateSocieteCotee(prefix,
					"Nintendo");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("societe cree !");
			else
				System.out.println("priob nintendo");

			for (int i = 0; i < 11; i += 1) {
				ajout = Fonctions.SQLRequestGenerator.COUCreateTitre(prefix,
						"Nintendo", "Titre " + i, "02134");
				if (usr.execBatchQuery(ajout, true))
					System.out.println("titre ajoute !");
				else
					System.out.println("prob ajout titre");
			}

			ajout = Fonctions.SQLRequestGenerator.COUCreateOrdreVente(prefix,
					10, "20070912", "20071250", new Euro("7,01"), "Nintendo",
					"02134", "ordre1");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("ordre cree");
			else
				System.out.println("ordre pas cree");

			ajout = Fonctions.SQLRequestGenerator
					.COUCreateReceptionTransaction(prefix, "20071150",
							"essai1", new Euro("4,09"), 5, "Nintendo", "ordre1");
			if (usr.execBatchQuery(ajout, true))
				System.out.println("ok transaction");
			else
				System.out.println("aie transaction !");
			ajout = Fonctions.SQLRequestGenerator
					.COUCreateReceptionTransaction(prefix, "20051010",
							"essai2", new Euro("6,99"), 3, "Nintendo", "ordre1");
			usr.execBatchQuery(ajout, true);

		}

		/*
		 * Particulier part = new Particulier ("Clarisse8","violette", prefix,
		 * usr); Particulier part2 = new Particulier ("Audrey","compere",
		 * prefix, usr); part.seConnecter(); part2.seConnecter();
		 * 
		 * System.out.println("login : " + part.getLogin());
		 * System.out.println("motdepasse : " + part.getMotDePasse());
		 * System.out.println("bloque : " + part.getBloque());
		 * System.out.println("nom : " + part.getNom());
		 * System.out.println("prenom : " + part.getPrenom());
		 * System.out.println("pers.morale : " +
		 * part.getEstUnePersonneMorale()); System.out.println("nom. ent. : " +
		 * part.getNomEntreprise()); System.out.println("TVA : " +
		 * part.getNumeroTVA());
		 */
		/* CONNEXION DU COURTIER -> indispensable pour réussir les opérations */
		Courtier Daniel = new Courtier("Daniel", "Clarisse", prefix, usr);
		Daniel.seConnecter();
		/* FIN CONNEXION */

		/* DEBUT Afficher liste des cliens (Stété) */

		System.out
				.println("************** AFFICHAGE DE TOUS LES LOGINS DE CLIENTS **************");

		Iterator iter = Daniel.getParticuliers().iterator();
		while (iter.hasNext()) {
			Particulier parti = (Particulier) iter.next();
			System.out.println(parti.getLogin());
		}
		/* FIN Afficher liste des cliens (Stété) */

		/* DEBUT Afficher liste des comptes de tous les clients (Stété) */

		System.out
				.println("************** AFFICHAGE DES COMPTES DE TOUS LES CLIENTS **************");

		Iterator iter2 = Daniel.getParticuliers().iterator();
		while (iter2.hasNext()) {
			Particulier parti = (Particulier) iter2.next();
			System.out.println("________________   LOGIN CLIENT : "
					+ parti.getLogin() + "   ________________");

			Iterator iter3 = parti.getComptesLiquidites().iterator();
			if (iter3.hasNext())
				System.out.println("> CompteLiquidité");
			while (iter3.hasNext()) {
				CompteLiquidites cl = (CompteLiquidites) iter3.next();
				System.out.println("> Numéro : " + cl.getNumCompteLiquidites());
				/*
				 * System.out.println("> Solde Courant :
				 * "+cl.getSoldeCourant()); System.out.println("> Solde Dispo :
				 * "+cl.getSoldeDisponible());
				 */

				Iterator iter4 = cl.getComptesTitres().iterator();
				if (iter4.hasNext())
					System.out.println(">>>>> CompteTitres");
				while (iter4.hasNext()) {

					CompteTitres ct = (CompteTitres) iter4.next();
					System.out.println(">>>>> Numéro : "
							+ ct.getNumCompteTitres());
				}

			}

			System.out
					.println("___________________________________________________________________________");
			System.out.println();
		}

		System.out
				.println("************** AFFICHAGE DES INFOS D'UN CLIENT **************");

		Iterator iter5 = Daniel.getParticuliers().iterator();
		while (iter5.hasNext()) {
			Particulier parti = (Particulier) iter5.next();
			if (parti.getLogin().equals("Audrey")) {
				System.out.println("Nom : "+parti.getNom());
				System.out.println("Prénom : "+parti.getPrenom());
				System.out.print("Statut : ");
				if (parti.getBloque().equals("TRUE")) {
					System.out.println("Bloqué");
				} else {
					System.out.println("Non bloqué");
				}
				;
			}
			// System.out.println("________________ NOM DU CLIENT : "+
			// parti.getLogin()+" ________________");

		}

		/* FIN */
		/*
		 * Iterator it = part.getComptesLiquidites().iterator(); while
		 * (it.hasNext()) { CompteLiquidites compte = (CompteLiquidites)
		 * it.next(); System.out.println(compte.getNumCompteLiquidites() + "
		 * disponible : " + compte.getSoldeDisponible() + " courant : " +
		 * compte.getSoldeCourant() );
		 * 
		 * Iterator it2 = compte.getComptesTitres().iterator(); if
		 * (it2.hasNext()) System.out.println("a comme compte de titres: ");
		 * while (it2.hasNext()) { CompteTitres comptetit = (CompteTitres)
		 * it2.next();
		 * 
		 * comptetit.createOrdre("TRUE", "Nintendo", "1", new Euro
		 * ("10,00").toString(), "20070000", "20090000");
		 * //comptetit.createOrdre("TRUE", "Nintendo", "1", new Euro
		 * ("2000aoiezru,00").toString(), "20070000", "20090000");
		 * 
		 * 
		 * System.out.println(comptetit.getNumCompteTitres()); Iterator it3 =
		 * comptetit.getOrdres().iterator(); if (it3.hasNext())
		 * System.out.println("il y a des ordres"); while (it3.hasNext()) {
		 * Ordre ord = (Ordre) it3.next();
		 * System.out.println(ord.getNumOrdre()); } System.out.println("nbre de
		 * titres nintendo disp : " +
		 * comptetit.getNbTitresDisponible("Nintendo"));
		 * System.out.println("nbre de titres nintendo courant : " +
		 * comptetit.getNbTitresCourant("Nintendo")); } }
		 * 
		 * it = part.getComptesLiquidites().iterator(); while (it.hasNext()) {
		 * CompteLiquidites compte = (CompteLiquidites) it.next();
		 * System.out.println(compte.getNumCompteLiquidites() + " disponible : " +
		 * compte.getSoldeDisponible() + " courant : " +
		 * compte.getSoldeCourant() ); } it =
		 * part.getSocietesCotees().iterator(); while (it.hasNext()) {
		 * SocieteCotee soc = (SocieteCotee) it.next();
		 * System.out.println(soc.getNumSocieteCotee()); }
		 * 
		 */

	}
}
