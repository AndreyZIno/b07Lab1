import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

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
	
	// Constructor for the Polynomial class. 
	// The constructor takes a File object as input which it uses to construct the polynomial.
	public Polynomial(File file) {
		try (BufferedReader input = new BufferedReader(new FileReader(file))) {
			String line = input.readLine();
			
			String[] polyTerms = line.split("(?=[+-])");
			int polyLength = polyTerms.length;
			
			double[] coefs = new double[polyLength];
			int[] exps = new int[polyLength];
			
			for (int i = 0; i < polyLength; i++) {
				String term = polyTerms[i];
				
				// Find the index of 'x' in the term
				int indexOfX = term.indexOf('x');
				
				if (indexOfX == -1) {
					// Term does not contain 'x'
					coefs[i] = Double.parseDouble(term);
					exps[i] = 0;
				} else {
					// Term contains 'x'
					if (term.charAt(0) == '-') {
						// Negative coefficient
						coefs[i] = -Double.parseDouble(term.substring(1, indexOfX));
					} else {
						// Positive coefficient
						coefs[i] = Double.parseDouble(term.substring(0, indexOfX));
					}
					
					exps[i] = Integer.parseInt(term.substring(indexOfX + 1));
				}
			}
			
			this.coef = coefs;
			this.exp = exps;
			
			for (int i = 0; i < polyLength; i++) {
				System.out.println(this.coef[i] + "x^" + this.exp[i]);
			}
		} catch (IOException e) {
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
					// Add special case for exponent 0
					if (exp[i] == 0) {
						sb.append(coef[i]);
					} else {
						sb.append(coef[i] + "x" + exp[i]);
					}
				} else {
					// Add special case for exponent 0
					if (exp[i] == 0) {
						sb.append("+" + coef[i]);
					} else {
						sb.append("+" + coef[i] + "x" + exp[i]);
					}
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
	
	// This function multiplies two polynomials and returns the product as a new Polynomial.
	public Polynomial multiply(Polynomial poly) {
		// Calculate the maximum possible exponent by adding the highest exponents of both polynomials.
		int maxExp = this.exp[this.exp.length - 1] + poly.exp[poly.exp.length - 1];
		
		// Create a new array for coefficients, the size of which corresponds to the maximum possible exponent plus 1.
		double[] newCoef = new double[maxExp + 1];
		
		// Multiply each term of the first polynomial with each term of the second polynomial.
		for (int i = 0; i < this.coef.length; i++) {
			for (int j = 0; j < poly.coef.length; j++) {
				// The exponent of the product term is the sum of the exponents of the multiplied terms,
				// and the coefficient is the product of the coefficients of the multiplied terms.
				// If the product term already exists, add the new coefficient to the existing one.
				newCoef[this.exp[i] + poly.exp[j]] += this.coef[i] * poly.coef[j];
			}
		}
		
		// Count the number of non-zero coefficients in the product polynomial.
		int count = 0;
		for (double c : newCoef) {
			if (c != 0) {
				count++;
			}
		}
		
		// Create final coefficient and exponent arrays of the correct size.
		double[] finalCoef = new double[count];
		int[] finalExp = new int[count];
		int index = 0;
		
		// Populate the final coefficient and exponent arrays, 
		// excluding terms with a coefficient of 0 since they don't contribute to the polynomial.
		for (int i = 0; i < newCoef.length; i++) {
			if (newCoef[i] != 0) {
				finalCoef[index] = newCoef[i];
				finalExp[index] = i;
				index++;
			}
		}
		
		// Return the product polynomial, built from the final coefficient and exponent arrays.
		return new Polynomial(finalCoef, finalExp);
	}
	
	// This function adds two polynomials and returns the sum as a new Polynomial.
	public Polynomial add(Polynomial poly) {
		// The maximum possible exponent is the highest exponent out of both polynomials.
		int maxExp = Math.max(this.exp[this.exp.length - 1], poly.exp[poly.exp.length - 1]);
		
		// Create a new array for coefficients, the size of which corresponds to the maximum possible exponent plus 1.
		double[] newCoef = new double[maxExp + 1];
		
		// For each term in the first polynomial, add its coefficient to the correct place in the new coefficient array.
		for (int i = 0; i < this.coef.length; i++) {
			newCoef[this.exp[i]] += this.coef[i];
		}
		
		// Repeat the above step for the second polynomial.
		for (int i = 0; i < poly.coef.length; i++) {
			newCoef[poly.exp[i]] += poly.coef[i];
		}
		
		// Count the number of non-zero coefficients in the sum polynomial.
		int count = 0;
		for (double c : newCoef) {
			if (c != 0) {
				count++;
			}
		}
		
		// Create final coefficient and exponent arrays of the correct size.
		double[] finalCoef = new double[count];
		int[] finalExp = new int[count];
		int index = 0;
		
		// Populate the final coefficient and exponent arrays, 
		// excluding terms with a coefficient of 0 since they don't contribute to the polynomial.
		for (int i = 0; i < newCoef.length; i++) {
			if (newCoef[i] != 0) {
				finalCoef[index] = newCoef[i];
				finalExp[index] = i;
				index++;
			}
		}
		
		// Return the sum polynomial, built from the final coefficient and exponent arrays.
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