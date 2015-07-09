import Structure.Utils;


public class demoquinconce {

	public static void main(String[] args) {
		int taille = 69;
		
		for (int i=0; i< taille * 2; i+=1)
		{
			System.out.println("A Element : " + i + " dans une table de taille : " + taille + " a pour pos : " + Utils.quinconceA(i,taille));
			System.out.println("B Element : " + i + " dans une table de taille : " + taille + " a pour pos : " + Utils.quinconceB(i,taille));
			
		}
	}
	
}
