package Translator;

import Translator.Translate;
import org.armedbear.lisp.*;            // Interpreter

public class SLAManager {
    private String theCode = null;
    private Function interpreterFunc = null;

    public SLAManager(String LispCode) throws Exception {
	theCode = LispCode;
	initialiseSLAManager();
    }

    public String treatGTViolation(String GTName) {
	JavaObject param = new JavaObject(GTName);
	assert (null != param)
	    : "Failed to create a new Lisp parameter";
	System.err.println(";;;; Interpreter: Executing - Call Request!\n");
	/*
	 * Run the actions that should be triggered by this
	 * violation, if any.
	 */
	LispObject result =
	    this.interpreterFunc.execute(param);
	// System.err.println("Lisp code is: " + lispCode);

	/*
	 * Return result (TO WHOM?) XXX
	 */
	String res = result.printObject();
	// System.err.print("#| The result was :\n" + res + "\n#|\n");
	System.err.println(";;;; Interpreter: Executing - Call Response!\n");
	return res;
    }
    
    private void initialiseSLAManager() throws Exception{
	/*
	 * Evaluate the lisp code
	 */
        Interpreter interpreter = Interpreter.getInstance();
        if (interpreter == null) {
            interpreter = Interpreter.createInstance();
        }
        System.err.println(";;;; Interpreter: Starting\n");

        LispObject interpreterResult =
            interpreter.eval(theCode);
        assert (null != interpreterResult)
            : "Failed to eval the code";
        System.err.println(";;;; Interpreter: Eval'ed the code!\n");

	/*
	 * Start executing the SLA Manager
	 */
        // The function is not in a separate package, thus the correct
        // package is CL-USER. Symbol names are upper case. Package
        // needs the prefix, because java also has a class named
        // Package.
        org.armedbear.lisp.Package defaultPackage =
            Packages.findPackage("CL-USER");
        assert (null != defaultPackage)
            : "Failed to find cl-user package";
        Symbol interpreterSym =
            // defaultPackage.findAccessibleSymbol("LIFECYCLE-LOOP");
            defaultPackage.findAccessibleSymbol("TREAT-GT-VIOLATION");
        // System.err.print(lispCode);
        assert (null != interpreterSym)
            : "Failed to find symbol TREAT-GT-VIOLATION";
	// : "Failed to find symbol LIFECYCLE-LOOP";
        this.interpreterFunc =
            (Function) interpreterSym.getSymbolFunction();
        assert (null != this.interpreterFunc)
            : "Failed to get function from symbol LIFECYCLE-LOOP";
    }

    public static void main(String[] args) throws Exception {
	String target = "Cms/AgreementOffer_Demo-christos.xml";
	if (args.length == 1) // Read the argument instead of a hard-coded file
	    target = args[0];

	java.io.FileInputStream file = new java.io.FileInputStream(target);
	Translator.Translate tr = new Translator.Translate();
        try {
	    String[] results = tr.translateSLAtoPrismAndLisp(file);

	    // Only start the SLA Manager if everything went fine with
	    // the translation.
	    SLAManager theMan = new SLAManager(results[1]);

	    while (true) {
		String res = theMan.treatGTViolation("AvailabilityGT1");
		System.err
		    .println("#| The result was :\n"
			     + res.replaceAll("@", "\"").replaceAll("\\$", "\"")
			     + "\n#|\n");
	    }
	    /*
	     * XXX
	     *
	     * If you want to get the JSON output, run:
	     *
	     * result.replaceAll("@ ", "\"").replaceAll(" $", "\"");
	     */
	} catch (java.io.IOException e) {
	    System.err.println(e.getMessage());
	}
    }

}
