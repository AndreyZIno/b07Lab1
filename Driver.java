import java.io.File;

public class Driver {
	public static void main(String [] args) {
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		double [] c1 = {1, 1};
		int [] e1 = {0, 1};
		Polynomial p1 = new Polynomial(c1, e1);
		double [] c2 = {3, 1};
		int [] e2 = {0, 2};
		Polynomial p2 = new Polynomial(c2, e2);
		
		Polynomial s = p1.add(p2);
		Polynomial m = p1.multiply(p2);
		
		for (int i = 0; i < m.coef.length; i++) {
			System.out.println(m.coef[i]);
		}
		for (int i = 0; i < m.exp.length; i++) {
			System.out.println(m.exp[i]);
		}
		System.out.println("s(2) = " + m.evaluate(2));
		if(s.hasRoot(1))
		System.out.println("1 is a root of s");
		else
		System.out.println("1 is not a root of s");
		
		System.out.println("_________Reading File__________");
		File file = new File("C:\\Users\\andre\\b07lab1\\poly.txt");
		Polynomial f = new Polynomial(file);
		for (int i = 0; i < f.coef.length; i++) {
			System.out.println(f.coef[i]);
		}
		for (int i = 0; i < f.exp.length; i++) {
			System.out.println(f.exp[i]);
		}
		
		System.out.println("_________Writting File__________");
		p1.saveToFile("C:\\Users\\andre\\b07lab1\\poly.txt");
		System.out.println("Done");
	}
}