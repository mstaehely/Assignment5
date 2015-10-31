import junit.framework.TestCase;

public class JUnitTestClass extends TestCase{
    private static final String string = "3 5 4 4 5 3 6 2 1 1 9 0";
    private static final Polynomial master = new Polynomial(string);
    
    private Polynomial poly;
    
    private Polynomial getPolyCopy(String string){
        Polynomial poly = new Polynomial(string);
        return poly;
    }
    
    protected void setUp() {
        poly = getPolyCopy(string);
    }
    
    public void testPolynomial(){
        Polynomial copy = new Polynomial(poly);
        assertTrue(copy.equals(poly));
        assertFalse(copy == poly);
        String test = copy.toString();
        assertTrue(test.equals("3.0x^5 + 4.0x^4 + 5.0x^3 + 6.0x^2 + x + 9.0"));
        try{
            copy = null;
            Polynomial check = new Polynomial(copy);
            fail("Expected NPE not thrown.");
        } catch (NullPointerException e){
        } catch (Exception e){
            fail("Unexpected exception.");
        }
    }
    
    public void testTerms(){
        assertTrue(poly.terms() == 6);
    }
    
    public void testAddTerm(){
        Polynomial test = new Polynomial("3 5 3 2 1 0");
        test.addTerm(-4, 3);
        String check = test.toString();
        assertTrue(check.equals("3.0x^5 - 4.0x^3 + 3.0x^2 + 1.0"));
        poly.addTerm(1.5, 6);
        assertTrue(poly.getCoefficient(6) == 1.5);
        assertTrue(poly.terms() == 7);
        poly.addTerm(3, 3);
        assertTrue(poly.getCoefficient(3) == 8);
        assertTrue(poly.terms() == 7);
        poly.addTerm(5, 0);
        assertTrue(poly.getCoefficient(0) == 14);
        assertTrue(poly.terms() == 7);
        poly.addTerm(-14, 0);
        assertTrue(poly.getCoefficient(0) == 0);
        assertTrue(poly.terms() == 6);
        
        try {
            poly.addTerm(1.5, -1);
            fail("Expected IAE not thrown.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("Unexpected exception.");
        }
    }
    
    public void testDeleteTerm(){
        assertTrue(poly.deleteTerm(5) == 3);
        assertTrue(poly.terms() == 5);
        assertTrue(poly.deleteTerm(6) == 0.0);
        assertTrue(poly.terms() == 5);
        assertTrue(poly.deleteTerm(0) == 9);
        assertTrue(poly.terms() == 4);
    }
    
    public void testGetCoefficient(){
        assertTrue(poly.getCoefficient(5) == 3);
        assertTrue(poly.getCoefficient(0) == 9);
        assertTrue(poly.getCoefficient(3) == 5);
        assertTrue(poly.getCoefficient(6) == 0);
            
    }
    
    public void testEvaluate(){
        assertTrue(poly.evaluate(1) == 28);
        assertTrue(poly.evaluate(2) == 235);
    }
    
    public void testEquals(){
        assertTrue(poly.equals(master));
        poly.deleteTerm(5);
        assertFalse(poly.equals(master));
    }
    
    public void testDerivative(){
        String test = "15 4 16 3 15 2 12 1 1 0";
        Polynomial pn = new Polynomial(test);
        assertTrue(poly.derivative().equals(pn));
    }
    
    public void testToString(){
        String check = "" + poly;
        assertTrue("String is: " + poly, check.equals("3.0x^5 + 4.0x^4 + 5.0x^3 + 6.0x^2 + x + 9.0"));
        check = "-1 5 -3 4 -2 3 1 2 9 0";
        Polynomial test = new Polynomial(check);
        check = "" + test;
        assertTrue("String is : " + test, check.equals("-x^5 - 3.0x^4 - 2.0x^3 + x^2 + 9.0"));
        Polynomial empty = new Polynomial("");
        check = empty.toString();
        assertTrue("" + check, check.equals("0.0"));
    }
    
    public void testDescription(){
        String check = poly.description();
        assertTrue("" + check, check.equals("constant term 9.0\nexponent 1, coefficient 1.0" + 
                   "\nexponent 2, coefficient 6.0\nexponent 3, coefficient 5.0"+
                   "\nexponent 4, coefficient 4.0\nexponent 5, coefficient 3.0"));
        Polynomial empty = new Polynomial("");
        check = empty.description();
        assertTrue("" + check, check.equals("0.0"));
    }
    
    public void testSum(){
        Polynomial copy = new Polynomial(poly);
        Polynomial test = Polynomial.sum(copy, copy);
        String check = test.toString();
        assertTrue("" + test, check.equals("6.0x^5 + 8.0x^4 + 10.0x^3 + 12.0x^2 + 2.0x + 18.0"));
        assertTrue(copy.equals(poly));
        copy = null;
        try{
            Polynomial.sum(copy, poly);
            fail("Expected NPE not thrown.");
        } catch (NullPointerException e){
        } catch (Exception e){
            fail("Unexpected exception.");
        }
    }
    
    public void testProduct(){
        Polynomial copy = new Polynomial(poly);
        Polynomial test = new Polynomial(poly);
        Polynomial product = Polynomial.product(copy, test);
        String check = product.toString();
        assertTrue(copy.equals(poly));
        assertTrue(test.equals(poly));
        assertTrue(check.equals("9.0x^10 + 24.0x^9 + 46.0x^8 + 76.0x^7"
                                + " + 79.0x^6 + 122.0x^5 + 118.0x^4 + "
                                + "102.0x^3 + 109.0x^2 + 18.0x + 81.0"));
        copy = null;
        try{
            Polynomial.sum(copy, poly);
            fail("Expected NPE not thrown.");
        } catch (NullPointerException e){
        } catch (Exception e){
            fail("Unexpected exception.");
        }
    }
}