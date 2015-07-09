package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.ID;
import Utils.MoneyCurrency;
import Utils.Quantite;

public class Transaction {

	private Ordre ordre;
	private ID numTransaction;
	private Date datede;
	private MoneyCurrency montant;
	private Quantite quantiterealisee;
	private SocieteCotee societe;
	private ArrayList titresdelatransaction;

	public Transaction(Ordre ordre, ID numtransaction, Date datedea, MoneyCurrency montanta, Quantite querealisea, SocieteCotee lasociete, ArrayList titresdelatransaction) {
		// TODO Auto-generated constructor stub
		this.ordre = ordre;
		this.numTransaction = numtransaction;
		this.datede = datedea;
		this.montant = montanta;
		this.quantiterealisee = querealisea;
		this.societe = lasociete;
		this.titresdelatransaction = titresdelatransaction;
		// en gros on bind les titres ici !
		Iterator tit = this.titresdelatransaction.iterator();
		while (tit.hasNext())
		{
			Titre ti = (Titre) tit.next();
			ti.bindTransaction(this);
		}
	}
	
	public Ordre getOrdre()
	{
		return this.ordre;
	}

	public String getNumeroTransaction()
	{
		return this.numTransaction.getID();
	}
	public String getMontant()
	{
		return this.montant.getAmount();
	}
	public String getQuantiteRealisee()
	{
		return this.quantiterealisee.getQuantite();
	}
	public SocieteCotee getSociete()
	{
		return this.societe;
	}
	public Iterator getTitres()
	{
		return this.titresdelatransaction.iterator();
	}
	public String getDate()
	{
		return this.datede.getDate();
	}
	// ˆ la transaction on peut associer des titres !
	public void addTitre (Titre tit)
	{
		// on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitres();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (!found)
		{
			// il faut setter le titre vers ce compte de titres
			tit.setTransaction(this);
			this.titresdelatransaction.add(tit);
		}
	}
	
	public void removeTitre(Titre tit)
	{
//		 on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitres();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (found)
		{
			// il faut unsetter le titre vers ce compte de titres
			tit.unsetTransaction(this);
			this.titresdelatransaction.remove(tit);
		}
	}
}
