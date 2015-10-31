import java.util.Scanner;

/**
 * A class to model polynomial expressions in a linked list data structure.
 * 
 * @author Matthew Staehely
 * @version CSC 143 Winter 15
 */
public class Polynomial{
    
    private Node front, last; // last node stored for convenience
    private int terms; // tracks TERMS in polynomial, not DEGREE
    
    /**
     * Constructor of class Polynomial.
     * 
     * @param s String which represents the polynomial expression. Must be
     * composed of double/integer or integer/integer pairs.
     * 
     * @throws IllegalArgumentException if non-numbers are passed to constructor.
     */
    public Polynomial(String s){
        Scanner reader = new Scanner(s);
        double check; // used to ensure non-negative integers passed for exponent
        Node node;
        terms = 0;
        if(s == ""){
            front = new Node(1, 0);
            last = front;
            front.coefficient = 0;
        } else {
            // This loop makes sure that the string is both real numbers only as well
            // as contains no negative integers.
            while(reader.hasNext()){
                if(!reader.hasNextDouble())
                throw new IllegalArgumentException("Real numbers only.");
                reader.nextDouble();
            }
            
            // Builds the polynomial.
            reader = new Scanner(s);
            front = null;
            last = front;
            //front = new Node(reader.nextDouble(), reader.nextInt());
            //last = front;
            //this.terms++;
            while(reader.hasNext()){
                this.addTerm(reader.nextDouble(), reader.nextInt());
            }
        }   
    }
    
    /**
     * Constructor for class Polynomial. Creates a deep copy of a polynomial
     * expression, does not modify original.
     * 
     * @param p the polynomial object to be copied.
     * @throws NullPointerException if passed Polynomial is null.
     */
    public Polynomial(Polynomial p){
        this("");
        if(p == null)throw new NullPointerException();
        int[] exp = new int[p.terms()];
        double[] coeff = new double[p.terms()];
        int testTerms = 0;
        for(int i = 0, j = 0; testTerms < p.terms(); i++){
            if(p.getCoefficient(i) != 0){
                exp[j] = i;
                coeff[j] = p.getCoefficient(i);
                j++;
                testTerms++;
            }
        }
        for(int i = 0; i < exp.length; i++){
            this.addTerm(coeff[i], exp[i]);
        }
    }
    
    /**
     * Returns the number of terms in this expression.
     * 
     * @return the number of terms in this expression.
     */
    public int terms(){
        return terms;
    }
    
    /**
     * Adds a term to the polynomial expression.
     * 
     * @param coef the coefficient of the new term.
     * @param exp the exponent of the new term.
     * @throws IllegalArgumentException if a negative exponent value is passed.
     */
    public void addTerm(double coef, int exp){
        if(exp < 0) throw new IllegalArgumentException();
        Node node = new Node(coef, exp);
        // First checks to see if polynomial is empty (but not null).
        if(front == null || node.exponent > front.exponent){
            node.next = this.front;
            this.front = node;
            this.terms++;
            // Now verifies end of polynomial and breaks link with coefficients of
            // 0.
            if(node.next != null && node.next.coefficient == 0){
                node.next = null;
                this.last = node;
            }
        } else if(node.exponent == front.exponent){
            // This clears out an empty polynomial of its "0.0 0" node.
            if(front.coefficient == 0){
                terms++;
            }
            front.coefficient += node.coefficient;
            // Polynomial addition may be subtraction. Gets rid of a node which
            // has a modified coefficient of 0.
            if(front.coefficient == 0){
                front = front.next;
                terms--;
            }
        } else {
            addTerm(front, node, front.next);
        }
    }
    
    private void addTerm(Node prev, Node add, Node current){
        // Recurses through linked list to add terms to their proper location.
        if(current == null){
            prev.next = add;
            last = add;
            terms++;
        } else if(current.exponent == add.exponent){
            current.coefficient += add.coefficient;
            if(current.coefficient == 0){
                prev.next = current.next;
                if(prev.next == null){
                    last = prev;
                }
                terms--;
            }
        } else if(prev.exponent -1 >= add.exponent 
                  && add.exponent > current.exponent){
            // This loop checks to make sure a new polynomial term is being added
            // in the appropriate spot to maintain conventional expression.
            prev.next = add;
            add.next = current;
            terms++;
        } else {
            // Recurse
            addTerm(current, add, current.next);
        }            
    }
    
    /**
     * Deletes a term from the polynomial expression and returns that term's 
     * coefficient.
     * 
     * @param the exponent of the term to be deleted.
     * @return the coefficient of the deleted term, 0.0 if that term does not exist.
     */
    public double deleteTerm(int exp){
        if(front == null){
            return 0.0;
        } else {
            Node node = new Node (1.0, exp);
            if(front.exponent == node.exponent){
                node = front;
                this.front = front.next;
                terms--;
                return node.coefficient;
            }
            return deleteTerm(front, node, front.next);
        }
    }
    
    private double deleteTerm(Node previous, Node check, Node current){
        // Recurses through the linked list to find and delete the specified term.
        if(current == null){
            return 0.0;
        }
        if(check.exponent == current.exponent){
            previous.next = current.next;
            this.terms--;
            if(check.exponent == 0){
                last = previous;
            }
            return current.coefficient;
        }
        // Recurse
        return deleteTerm(current, check, current.next);
    }
    
    /**
     * Returns the coefficient value of the specified term.
     * 
     * @param exp the exponent of the term being searched for.
     * @return the coefficient of the specified term.
     */
    public double getCoefficient(int exp){
        if(front == null){
            return 0.0;
        }
        if(front.exponent == exp){
            return front.coefficient;
        }
        return getCoefficient(front.next, exp);
    }
    
    private double getCoefficient(Node node, int exp){
        // Recurses through the list while searching.
        if(node == null){
            return 0.0;
        }
        if(node.exponent == exp){
            return node.coefficient;
        }
        // Recurse
        return getCoefficient(node.next, exp);
    }
    
    /**
     * Plugs in a value for x and evaluates the expression.
     * 
     * @param x the x value.
     * @return the evaluated expression's numerical value.
     */
    public double evaluate(double x){
        if(front == null){
            return 0.0;
        }
        return evaluate(front, x);
    }
    
    private double evaluate(Node node, double x){
        // Recurses through the list to evaluate the expression.
        if(node == null){
            return 0.0;
        }
        // Recurse
        return Math.pow(x, node.exponent)*node.coefficient + 
               (evaluate(node.next, x));
    }
    
    /**
     * Checks for content equality.
     * 
     * @param o the object to be compared for equality.
     * @return true if the two polynomials are equal.
     */
    public boolean equals(Object o){
        if(front == null){
            return o == null;
        }
        if(this == o){
            return true;
        }
        // If the object isn't a polynomial object it will immediately fail this
        // check and hit the false return;
        if((o instanceof Polynomial)){
           Polynomial test = (Polynomial)o;
           if(terms() == test.terms()){
               if(this.front.coefficient == test.front.coefficient &&
                  this.front.exponent == test.front.exponent){
                   return equals(front.next, test.front.next);
                }
            }
        }
        return false;    
    }
    
    private boolean equals(Node main, Node test){
        // Recurses through the list to continue comparing terms.
        if(main == null && test == null){
            return true;
        }
        if(main.coefficient == test.coefficient &&
           main.exponent == test.exponent){
               // Recurse
               return equals(main.next, test.next);
        }
        return false;
    }
    
    /**
     * Calculates the derivative of the polynomial.
     * 
     * @return the derived polynomial expression.
     * @throws IllegalStateException if a null polynomial is attempted to be derived.
     */
    public Polynomial derivative(){
        if(front == null) throw new IllegalStateException();
        String term = "" + front.coefficient * front.exponent + " " + --front.exponent;
        Polynomial newPoly = new Polynomial(term);
        derive(this.front.next, newPoly);
        return newPoly;
    }
    
    private void derive(Node node, Polynomial poly){
        // Recursive function to calculate derivative.
        if(node == null){
            return;
        }
        if(node.exponent - 1 >= 0){
            poly.addTerm(node.coefficient * node.exponent, --node.exponent);
            // Recurse
            derive(node.next, poly);
        }
    }
    
    /**
     * Returns a String representation of this polynomial.
     * 
     * @return a string representation of this polynomial.
     */
    public String toString(){
        String string = "";
        if(terms == 0){
            string = "0.0";
            return string;
        }
        if(front.exponent > 0){
            if(front.coefficient == 0){
                string += front.coefficient;
            } else if(front.coefficient == 1){
                string += "x";
            } else if(front.coefficient == -1){
                string += "-x";
            } else {
                string += front.coefficient + "x";
            }
            string += "^" + front.exponent;
        } else {
            string += front.coefficient;
            return string;
        }
        return toString(string, front.next);
    }
    
    private String toString(String string, Node node){
        // Recurses to build String.
        if(node == null){
            return string;
        }
        if(node.coefficient == 0){
            string += "";
        } else if(node.coefficient < -1){
            string += " - " + -1*node.coefficient;
        } else if(node.coefficient == 1){
            string += " + ";
            if(node.exponent == 0){
                string += node.coefficient;
            }
        } else if(node.coefficient == -1){
            string += " - x";
        } else {
            string += " + " + node.coefficient;
        }
        if(node.exponent > 1){
            string += "x^" + node.exponent;
        } else if(node.exponent == 1){
            string += "x";
        }
            
        // Recurse.
        return toString(string, node.next);
    }
    
    /**
     * Returns a String representation of this polynomial. The format is in
     * ascending order of degree, beginning with constant term if such exists.
     * 
     * @return a String representation of this polynomial.
     */
    public String description(){
        String string = "";
        if(front.coefficient == 0){
            string += "0.0";
            return string;
        }
        if(front.exponent > 0){
            string += "exponent " + front.exponent + ", coefficient " 
                      + front.coefficient;
        }
        return description(string, front.next);
    }
    
    private String description(String string, Node node){
        // Recurses through list to build description.
        if(node == null){
            return string;
        }
        if(node.exponent == 0){
            string = "constant term " + node.coefficient + "\n" + string;
        } else {
            string = "exponent " + node.exponent + ", coefficient " 
                     + node.coefficient + "\n" + string;
        }
        // Recurse
        return description(string, node.next);
    }
    
    /**
     * Static method which will take two polynomials and find their sum.
     * 
     * @param a the first polynomial.
     * @param b the second polynomial.
     * @return the new polynomial representing the sum of the other two.
     * 
     * @throws NullPointerException if either polynomial is null.
     */
    public static Polynomial sum(Polynomial a, Polynomial b){
        if(a == null || b == null) throw new NullPointerException();
        Polynomial sum = new Polynomial(a);
        Polynomial sum2 = new Polynomial(b);
        for(int i = 0; sum2.terms > 0; i++){
            if(sum2.getCoefficient(i) != 0){
                sum.addTerm(sum2.deleteTerm(i), i);
            }
        }
        return sum;
    }
    
    /**
     * Static method which will take two polynomials and find their product.
     * 
     * @param a the first polynomial.
     * @param b the second polynomial.
     * @return the new polynomial representing the product of the other two.
     * 
     * @throws NullPointerException if either polynomial is null.
     */
    public static Polynomial product(Polynomial a, Polynomial b){
        if(a == null || b == null) throw new NullPointerException();
        Polynomial prod = new Polynomial ("");
        Node polyANode = a.front;
        Node polyBNode = b.front;
        while(polyANode != null){
            while(polyBNode != null){
                prod.addTerm(polyANode.coefficient * polyBNode.coefficient, 
                             polyANode.exponent + polyBNode.exponent);
                polyBNode = polyBNode.next;
            }
            polyANode = polyANode.next;
            polyBNode = b.front;
        }
        return prod;
    }
    
    /**
     * Private inner class which constructs Node objects to create the linked list
     * data structure. The fields of these nodes may be accessed directly by other
     * objects of the Polynomial class, no accessor is required.
     */
    private class Node{
        double coefficient;
        int exponent;
        Node next;
        
        /**
         * Constructs a new Node.
         * 
         * @param coefficient the coefficient of the new term being created.
         * @param exponent the exponent of the new term being created.
         * 
         * @throws IllegalArgumentException if the coefficient passed is 0.
         */
        public Node(double coefficient, int exponent){
            if(coefficient == 0) throw new IllegalArgumentException();
            this.coefficient = coefficient;
            this.exponent = exponent;
            this.next = null;
        }
    }
}