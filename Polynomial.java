import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Polynomial {

	double[] coef;
	int[] exp;
	
	// No-argument constructor
	public Polynomial() {
		coef = new double[1];
		exp = new int[1];
		for (int i = 0; i < coef.length; i++) {
			coef[i] = 0.0;
			exp[i] = 0;
		}
	}
	
	// Constructor with array of coefficients
	public Polynomial(double[] coef, int[] exp) {
		this.coef = coef;
		this.exp = exp;
	}
	
	// Constructor that takes a File object
	public Polynomial(File file) {
		try {
			Scanner scanner = new Scanner(file);
			String polynomial = scanner.nextLine();
			scanner.close();
			
			List<Double> coefficients = new ArrayList<>();
			List<Integer> exponents = new ArrayList<>();
			
			polynomial = polynomial.replace("-", "+-"); // replace '-' with '+-' to split properly
			String[] terms = polynomial.split("\\+");
			
			for (String term : terms) {
				String[] parts = term.split("x");
				
				double coef;
				if (parts[0].isEmpty()) {
					coef = 1.0;
				}
				else {
					coef = Double.parseDouble(parts[0]);
				}
				
				int exp;
				if (parts.length > 1) {
					if (parts[1].isEmpty()) {
						exp = 1;
					} else {
						exp = Integer.parseInt(parts[1]);
					}
				} else {
					exp = 0;
				}
				
				coefficients.add(coef);
				exponents.add(exp);
			}
			
			this.coef = coefficients.stream().mapToDouble(d->d).toArray();
			this.exp = exponents.stream().mapToInt(i->i).toArray();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Save the polynomial to a file
	public void saveToFile(String filename) {
		try {
			PrintWriter writer = new PrintWriter(filename);
			
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < coef.length; i++) {
				if (coef[i] < 0) {
					sb.append(coef[i] + "x" + exp[i]);
				} else {
					sb.append("+" + coef[i] + "x" + exp[i]);
				}
			}
			
			// Remove the leading '+' if it exists
			if (sb.charAt(0) == '+') {
				sb.deleteCharAt(0);
			}
			
			writer.println(sb.toString());
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Polynomial multiply(Polynomial poly) {
		int maxExp = this.exp[this.exp.length - 1] + poly.exp[poly.exp.length - 1];
		double[] newCoef = new double[maxExp + 1];
		
		for (int i = 0; i < this.coef.length; i++) {
			for (int j = 0; j < poly.coef.length; j++) {
				newCoef[this.exp[i] + poly.exp[j]] += this.coef[i] * poly.coef[j];
			}
		}
		
		int count = 0;
		for (double c : newCoef) {
			if (c != 0) {
				count++;
			}
		}
		
		double[] finalCoef = new double[count];
		int[] finalExp = new int[count];
		int index = 0;
		
		for (int i = 0; i < newCoef.length; i++) {
			if (newCoef[i] != 0) {
				finalCoef[index] = newCoef[i];
				finalExp[index] = i;
				index++;
			}
		}
		
		return new Polynomial(finalCoef, finalExp);
	}
	
	public Polynomial add(Polynomial poly) {
		int maxExp = Math.max(this.exp[this.exp.length - 1], poly.exp[poly.exp.length - 1]);
		double[] newCoef = new double[maxExp + 1];
		
		for (int i = 0; i < this.coef.length; i++) {
			newCoef[this.exp[i]] += this.coef[i];
		}
		
		for (int i = 0; i < poly.coef.length; i++) {
			newCoef[poly.exp[i]] += poly.coef[i];
		}
		
		int count = 0;
		for (double c : newCoef) {
			if (c != 0) {
				count++;
			}
		}
		
		double[] finalCoef = new double[count];
		int[] finalExp = new int[count];
		int index = 0;
		
		for (int i = 0; i < newCoef.length; i++) {
			if (newCoef[i] != 0) {
				finalCoef[index] = newCoef[i];
				finalExp[index] = i;
				index++;
			}
		}
		
		return new Polynomial(finalCoef, finalExp);
	}
	
	public double evaluate(double x) {
		double result = 0.0;
		
		for (int i = 0; i < coef.length; i++) {
			result += coef[i] * Math.pow(x, exp[i]);
		}
		
		return result;
	}
	
	public boolean hasRoot(double x) {
		return evaluate(x) == 0.0;
	}
	
}