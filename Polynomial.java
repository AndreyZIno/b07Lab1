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
	
	// Constructor for the Polynomial class. 
	// The constructor takes a File object as input which it uses to construct the polynomial.
	public Polynomial(File file) {
		try {
			// Initialize a Scanner to read from the file
			Scanner scanner = new Scanner(file);
			
			// Read the first line of the file into a string, expecting it to be a polynomial
			String polynomial = scanner.nextLine();
			
			// Close the scanner to free resources
			scanner.close();
			
			// Initialize lists to hold the coefficients and exponents of the polynomial
			List<Double> coefficients = new ArrayList<>();
			List<Integer> exponents = new ArrayList<>();
			
			// Replace '-' characters with '+-' in the polynomial string to aid in splitting the terms correctly
			polynomial = polynomial.replace("-", "+-");
			
			// Split the polynomial into individual terms based on the '+' character
			String[] terms = polynomial.split("\\+");
			
			// For each term in the polynomial
			for (String term : terms) {
				// Split the term into its coefficient and exponent parts based on the 'x' character
				String[] parts = term.split("x");
				
				// Declare and initialize the coefficient
				double coef;
				if (parts[0].isEmpty()) {
					coef = 1.0; // If the part is empty, it means there's an implicit 1 coefficient
				}
				else {
					coef = Double.parseDouble(parts[0]); // Otherwise, parse the coefficient part as a double
				}
				
				// Declare and initialize the exponent
				int exp;
				if (parts.length > 1) {
					if (parts[1].isEmpty()) {
						exp = 1; // If there is a second part but it's empty, it means there's an implicit 1 exponent
					} else {
						exp = Integer.parseInt(parts[1]); // Otherwise, parse the exponent part as an integer
					}
				} else {
					exp = 0; // If there's no second part, it means the term doesn't have 'x', so the exponent is 0
				}
				
				// Add the coefficient and exponent to their respective lists
				coefficients.add(coef);
				exponents.add(exp);
			}
			
			// Convert the coefficient and exponent lists into arrays and assign them to this object's corresponding fields
			this.coef = coefficients.stream().mapToDouble(d->d).toArray();
			this.exp = exponents.stream().mapToInt(i->i).toArray();
			
		} catch (FileNotFoundException e) {
			// Print the stack trace if the file is not found
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