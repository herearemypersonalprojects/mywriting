
public class EnglishDecimalFormat {
	  private static final String[] majorNames = {
		    "",
		    " thousand",
		    " million",
		    " billion",
		    " trillion",
		    " quadrillion",
		    " quintillion"
		    };

		  private static final String[] tensNames = {
		    "",
		    " ten",
		    " twenty",
		    " thirty",
		    " fourty",
		    " fifty",
		    " sixty",
		    " seventy",
		    " eighty",
		    " ninety"
		    };

		  private static final String[] numNames = {
		    "",
		    " one",
		    " two",
		    " three",
		    " four",
		    " five",
		    " six",
		    " seven",
		    " eight",
		    " nine",
		    " ten",
		    " eleven",
		    " twelve",
		    " thirteen",
		    " fourteen",
		    " fifteen",
		    " sixteen",
		    " seventeen",
		    " eighteen",
		    " nineteen"
		    };

		 private String convertLessThanOneThousand(int number) {
		    String soFar;

		    if (number % 100 < 20){
		        soFar = numNames[number % 100];
		        number /= 100;
		       }
		    else {
		        soFar = numNames[number % 10];
		        number /= 10;

		        soFar = tensNames[number % 10] + soFar;
		        number /= 10;
		       }
		    if (number == 0) return soFar;
		    return numNames[number] + " hundred" + soFar;
		}

		public String convert(int number) {
		    /* special case */
		    if (number == 0) { return "zero"; }

		    String prefix = "";

		    if (number < 0) {
		        number = -number;
		        prefix = "negative";
		      }

		    String soFar = "";
		    int place = 0;

		    do {
		      int n = number % 1000;
		      if (n != 0){
		         String s = convertLessThanOneThousand(n);
		         soFar = s + majorNames[place] + soFar;
		        }
		      place++;
		      number /= 1000;
		      } while (number > 0);

		    return (prefix + soFar).trim();
		}

		public static void main(String[] args) {
		    EnglishDecimalFormat f = new EnglishDecimalFormat();
		    System.out.println("*** " + f.convert(0));
		    System.out.println("*** " + f.convert(1));
		    System.out.println("*** " + f.convert(16));
		    System.out.println("*** " + f.convert(100));
		    System.out.println("*** " + f.convert(118));
		    System.out.println("*** " + f.convert(200));
		    System.out.println("*** " + f.convert(219));
		    System.out.println("*** " + f.convert(800));
		    System.out.println("*** " + f.convert(801));
		    System.out.println("*** " + f.convert(1316));
		    System.out.println("*** " + f.convert(1000000));
		    System.out.println("*** " + f.convert(2000000));
		    System.out.println("*** " + f.convert(3000200));
		    System.out.println("*** " + f.convert(700000));
		    System.out.println("*** " + f.convert(9000000));
		    System.out.println("*** " + f.convert(123456789));
		    System.out.println("*** " + f.convert(-45));
		    /*
		    *** zero
		    *** one
		    *** sixteen
		    *** one hundred
		    *** one hundred eighteen
		    *** two hundred
		    *** two hundred nineteen
		    *** eight hundred
		    *** eight hundred one
		    *** one thousand three hundred sixteen
		    *** one million
		    *** two million
		    *** three million two hundred
		    *** seven hundred thousand
		    *** nine million
		    *** one hundred twenty three million four hundred 
		    **                fifty six thousand seven hundred eighty nine
		    *** negative fourty five
		    */
		    }		
}
