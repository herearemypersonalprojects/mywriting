package Business.Courtier.Data;

import Utils.Date;
import Utils.ID;
import Utils.MoneyCurrency;
import Utils.Quantite;
import Utils.Time;

public class LigneJournal {

	private ID numerodeligne;
	private Time eureevenement;
	private Date ateevenement;
	private MoneyCurrency ontant;
	private Quantite antite;
	private boolean ejaconsulteparcourtier;
	private boolean ejaconsutleparparticulier;
	private String typeoperation;
	private String surtypeobjet;
	private String commentaire;
	private Object lienrapide;

	public LigneJournal(ID numerodeligne, Time eureevenement, Date ateevenement, MoneyCurrency ontant, Quantite antite, boolean ejaconsulteparparticulier, boolean ejaconsulteparcourtier, String typeoperation, String surtypeobjet, String commentaire, Object lienrapide) {
		this.numerodeligne = numerodeligne;
		this.eureevenement = eureevenement;
		this.ateevenement = ateevenement;
		this.ontant = ontant;
		this.antite = antite;
		this.ejaconsulteparcourtier = ejaconsulteparcourtier;
		this.ejaconsutleparparticulier = ejaconsulteparparticulier;
		this.typeoperation = typeoperation;
		this.surtypeobjet = surtypeobjet;
		this.commentaire = commentaire;
		this.lienrapide = lienrapide;
	}
	
	public String getNumeroDeLigne()
	{
		// retourne le numéro de ligne du journal;
		return this.numerodeligne.getID();
	}

	public String getHeureEvenement()
	{
		// retourne l'heure de l'évènement considéré;
		return this.eureevenement.getTime();
	}
	public String getDateEvenement()
	{
		// retourne la date de l'évènement considéré;
		return this.ateevenement.getDate();
	}
	
	public String getMontant()
	{
		// retourne la valeur du montant ;
		return this.ontant.getAmount();
	}
	
	public String getQuantite()
	{
		// retourne la quantite spécifiée;
		return this.antite.getQuantite();
	}
	
	public boolean getDejaConsulteParParticulier()
	{
		// retourne si le particulier a déjà consulté;
		return this.ejaconsutleparparticulier;
	}
	
	public boolean getDejaConsulteParCourtier()
	{
		// retourne si le courtier a déjà consulté
		return this.ejaconsulteparcourtier;
	}
	public String getTypeOperation()
	{
		// retourne le type d'opération effectué
		return this.typeoperation;
	}
	public String getSurTypeObjet()
	{
		// retourne le type d'objet concerné
		return this.surtypeobjet;
	}
	public String getCommentaire()
	{
		// retourne le commentaire !
		return this.commentaire;
	}
	public Object getObjetLienRapide()
	{
		// retourne l'objet visé par le lien rapide !
		return this.lienrapide;
	}
	
	public void setDejaConsulteParParticulier(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_lignejournal set DejaConsulteParParticulier='" + new String(value + "").toUpperCase() + "' where numeroligne='" + this.getNumeroDeLigne() + "'");
		this.ejaconsutleparparticulier = value;
	}
	
	public void setDejaConsulteParCourtier(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_lignejournal set DejaConsulteParCourtier='" + new String(value + "").toUpperCase() + "' where numeroligne='" + this.getNumeroDeLigne() + "'");
		this.ejaconsulteparcourtier = value;
	}
	
	// Les set considérés portent uniquement sur dejaconsulteparcourtier, dejaconsulteparparticulier
}
