/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar4;

/**
 *
 * @author Sridhar
 */

/*
    Tests the Grammar
*/
public class TestGrammar {
     public  void test(CheckGrammar cfg) {
	System.out.println(cfg);
	System.out.println("# of variables = "+ cfg.leftVars.size());
	System.out.println("# of rules = "+ cfg.prod.size());
	System.out.println("Has epsilons? "+cfg.hasEps());
	System.out.println("Has no epsilons,  <START> --> eps? "
			   +cfg.hasNoEpsilons() );
	System.out.println("Contains unit transitions? "+cfg.hasUnitTrans());
    }
}
