package fracCalc;

import java.util.Scanner;

public class FracCalc {

    public static void main(String[] args) 
    {
        // TODO: Read the input from the user and call produceAnswer with an equation
    	Scanner console = new Scanner(System.in);
    	System.out.print("Input (quit to end): ");
    	String line = console.nextLine();
    	String answer = produceAnswer(line);
    	System.out.println("answer: " + answer);

    }
    
    // ** IMPORTANT ** DO NOT DELETE THIS FUNCTION.  This function will be used to test your code
    // This function takes a String 'input' and produces the result
    //
    // input is a fraction string that needs to be evaluated.  For your program, this will be the user input.
    //      e.g. input ==> "1/2 + 3/4"
    //        
    // The function should return the result of the fraction after it has been calculated
    //      e.g. return ==> "1_1/4"
    public static String produceAnswer(String input){ 
        // TODO: Implement this function to produce the solution to the input
        Scanner inputScanner = new Scanner(input);
        String O1 = inputScanner.next();
        String operator = inputScanner.next();
        String O2 = inputScanner.next();
        
        int[] P1 = parseOperand(O1);
        int[] P2 = parseOperand(O2);
        
        int[] t1 = improper(P1);
        int[] t2 = improper(P2);

        int[] test = doMath(t1, t2, operator);
        int[] fin = simplify(test);
        
    	
    	inputScanner.close();
    
    	if(fin[2]==1) {
    		return ""+fin[0];
    	}else if (fin[0]==0) {
    		return fin[1]+"/"+fin[2];
    	}
    	return fin[0]+"_" + fin[1]+"/"+fin[2];
    }

    /**
     * Breaks an operand string into the Whole, Numerator and Denominator components
     * @param operand - String containing an operand. 
     *     Can be something like "6_5/8" or "1/7" or "4" or "-2_1/5"
     * @return - Array of 3 integers: [Whole, Numerator, Denominator]
     */
    public static int[] parseOperand(String operand) {
        int[] parts = new int[3];
        
        // parse an operand like "6_5/8" into the three numbers:
        // parts[0] = 6; parts[1] = 5; parts[2] = 8
        // Find the positions in the string where the Numerator and Denominator are starting
        int iNumS = -1;
        int iNumF = -1;
        int iDenS = -1;
        int iDenF = -1;
        if(operand.indexOf("/")!=-1) {	
        	if(operand.indexOf("_")!=-1) {
        		iNumS = operand.indexOf("_")+1;
        		iNumF = operand.indexOf("/");
        		iDenS = operand.indexOf('/') + 1;
        		iDenF = operand.length();
        	}else {
        		iNumS = 0;
        		iNumF = operand.indexOf("/");
        		iDenS = operand.indexOf('/') + 1;
        		iDenF = operand.length();
        	}
        }
        
        // In a fraction either the Whole or the Numerator & Denominator may be missing!
        // The iNum and iDen indices will then be like this:
        // "6_5/8" => iNum=2; iDen=4
        // "6"     => iNum=0; iDen=0
        // "5/8"   => iNum=0; iDen=2
        
        if (iNumS > 0) {
            // if we have iNum>0 we definitely have a Whole component
            String whole = operand.substring(0, iNumS-1);
            parts[0] = Integer.parseInt(whole);
        }
        
        if (iNumS == iDenS) {
            // if iNum and iDen are same, we do not have a Numerator or Denominator
            // the entire operand is just the Whole number
            // TODO: fill in the Whole portion of parts
        	String whole = operand.substring(0);	
        	parts[0] = Integer.parseInt(whole);
        	parts[2] = 1;
        } else {
            // if iNum and iDen are different, we definitely have Numerator and Denominator
            String numerator = operand.substring(iNumS, iNumF);
            String denominator = operand.substring(iDenS);
            parts[1] = Integer.parseInt(numerator);
            parts[2] = Integer.parseInt(denominator);
        }
        
        // TODO: Special case! Negative numbers! For an operand like "-2_3/4"
        // TODO: The mathematical value will be -2.75, which is -2-3/4
        // TODO: So if Whole is negative, we need to make the Numerator negative as well!
        if(operand.substring(0,1).equals("-")) {
        	if(iNumS>0) {
        		String numerator = operand.substring(iNumS, iNumF);
        		parts[1] = Integer.parseInt(numerator)*-1;
        	}
        }
          
        return parts;
    }

    // TODO: Fill in the space below with any helper methods that you think you will need
    
    //this works 
    public static int[] improper(int[] input){
    	int[] imp = new int[2];
    	int whole1 = input[0];
    	int num1 = input[1];
    	int den1 = input[2];
    	//using vars
    	if(num1 >= 0 && whole1 >= 0){
    		num1 = whole1 * den1 + num1;	
    	} else {
    		num1 = Math.abs(whole1) * den1 + Math.abs(num1);
    		num1 *= -1;
    	}
    	imp[0] = num1;
    	imp[1] = den1;
    	return imp;
    }
    
    public static int[] doMath(int[] one, int[] two, String operator) {
    	int[] result = new int[2];
    	int n1 = one[0];
    	int d1 = one[1];
    	int n2 = two[0];
    	int d2 = two[1];
    	
    	int fD = d1 * d2;
    	int fN = 0;

    	if(operator.equals("+")) {
    		n1 = n1*d2;
    		n2 = n2*d1;
    		fN = n1+n2;
    	}else if(operator.equals("-")) {
    		n1 = n1*d2;
    		n2 = n2*d1;
    		fN = n1-n2;
    	}else if(operator.equals("*")) {
    		fN = n1*n2;
    	}else{
    		fN = n1*d2;
    		fD = d1*n2;
    	}
    
    	result[0] = fN;
    	result[1] = fD;
    	return result;
    }
    
    public static int[] simplify (int[] input) {
    	int[] result = new int[3];
    	
    	int a = input[0];
    	int b = input[1];
    	while(b != 0){
    		int temp = a % b;
    		a = b;
    		b = temp;
    	}
    	 int gcd = a;
    	 input[0] = input[0]/gcd;
    	 input[1] = input[1]/gcd;
    	
    	int whole = input[0]/input[1];
    	int num = input[0]%input[1];
    	int den = input[1];
    	result[0] = whole;
    	result[1] = num;
    	result[2] = den;
     	
    	if(result[0]<0) {
    		result[1]= Math.abs(result[1]);
    		result[2]= Math.abs(result[2]);
    	}
    	
    	return result;
    }

    
}
