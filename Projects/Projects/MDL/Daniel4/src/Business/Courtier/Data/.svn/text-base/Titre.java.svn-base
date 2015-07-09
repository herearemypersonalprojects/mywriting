package Business.Courtier.Data;

import Utils.ID;

public class Titre {

	private ID numtitre;
	private SocieteCotee societe;
	private Ordre numordre = null;
	private Transaction numtransaction = null;
	private CompteTitres numcomptetitres = null;

	public Titre(ID numtitre, SocieteCotee cotee) {
		// TODO Auto-generated constructor stub
		this.numtitre = numtitre;
		this.societe = cotee;
	}

	public String getNumeroTitre()
	{
		//retourne le numéro du titre
		return this.numtitre.getID();
	}
	
	public SocieteCotee getSociete()
	{
		// retourne la societe du titre
		return this.societe;
	}
	
	public Transaction getTransaction()
	{
		// retourne la transaction lié qui lie ce titre ;
		return this.numtransaction;
	}
	
	public Ordre getOrdre()
	{
		// retourne l'ordre lié à ce titre ;
		return this.numordre;
	}
	
	public CompteTitres getCompteTitres()
	{
		// retourne le comte de titres lié à ce titre;
		return this.numcomptetitres;
	}
	
	public void bindOrdre(Ordre ord)
	{
		// permet de lier (uniquement point de vue données)
		// un ordre au titre
		this.numordre = ord;
	}
	
	public void bindTransaction(Transaction tra)
	{
		// permet de lier (point de vue données) une transaction au titre
		this.numtransaction = tra;
	}
	
	public void bindCompteTitres(CompteTitres tit)
	{
		// permet de lier point de vue données, une compte de titres au titre
		this.numcomptetitres = tit;
	}
	
	public void setCompteTitres(CompteTitres co)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumCompteTitres='" + co.getNumCompteTitres()+ "' where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindCompteTitres(co);
	}
	public void setTransaction(Transaction tr)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumTransaction='" + tr.getNumeroTransaction()+ "' where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindTransaction(tr);
	}
	public void setOrdre(Ordre or)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumOrdre='" + or.getNumeroOrdre()+ "' where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindOrdre(or);
	}
	
	public void unsetCompteTitres(CompteTitres co)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumCompteTitres=null where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindCompteTitres(null);
	}
	public void unsetTransaction(Transaction tr)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumTransaction=null where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindTransaction(null);
	}
	public void unsetOrdre(Ordre or)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_titre set NumOrdre=null where NumTitre='" + this.getNumeroTitre() + "' and NumSociete='" + this.getSociete().getNumeroSociete() + "'");
		
		this.bindOrdre(null);
	}
}
