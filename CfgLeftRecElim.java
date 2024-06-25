package csen1002.main.task5;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Write your info here
 * 
 * @name Mohammad Sherif Elwan
 * @id 49-1865
 * @labNumber 13
 */

public class CfgLeftRecElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */


	String[] variables;
	String[] terminals;
	String variablesStr;
	String terminalsStr;
	Hashtable<String, List<String>> rules;
	List<String> newVariables= new ArrayList<>();

	public CfgLeftRecElim(String cfg) {
		// TODO Auto-generated constructor stub

		String[] parts= cfg.split("#");
		variablesStr = parts[0];
		terminalsStr = parts[1];
		variables=parts[0].split(";");
		terminals=parts[1].split(";");

		rules=new Hashtable<>();
		String[] varRules=parts[2].split(";");

		for(int i=0;i<varRules.length;i++) 
		{
			String[] pair= varRules[i].split("/");
			String variable = pair[0];
			String[] rule= pair[1].split(",");
			List<String> rulesList= new ArrayList<>();
			for(int j=0;j<rule.length;j++) 
			{
				rulesList.add(rule[j]);
			}

			rules.put(variable, rulesList);
		}

	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
	    String output = "";
	    output += variablesStr + ";";

	    for (int j=0;j<newVariables.size();j++) 
	    {
	        String v = newVariables.get(j);
	        output += v + ";";
	    }

	    output = output.substring(0, output.length() - 1);
	    output += "#";

	    output += terminalsStr + "#";

	    for (int j=0;j<variables.length;j++) 
	    {
	    	String x= variables[j];
	        output += x + "/";
	        List<String> Rule = rules.get(x);
	        for (int i=0;i<Rule.size();i++) 
	        {
	            output += Rule.get(i);
	            if (i<Rule.size()-1) 
	            {
	                output += ",";
	            }
	        }
	        output += ";";
	    }

	    for (int j=0;j<newVariables.size();j++) 
	    {
	    	String x = newVariables.get(j);
	        output += x + "/";
	        List<String> Rule = rules.get(x);
	        for (int i=0;i<Rule.size();i++) 
	        {
	            output += Rule.get(i);
	            if (i<Rule.size() - 1) 
	            {
	                output += ",";
	            }
	        }
	        output += ";";
	    }

	    return output.substring(0, output.length() - 1);
	}


	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
	    for (int i=0;i<variables.length;i++) 
	    {
	        String v = variables[i];
	        List<String> vRules = rules.get(v);
	        List<String> Left = new ArrayList<>();
	        List<String> noLeft = new ArrayList<>();
	        List<String> newNoLeft = new ArrayList<>();
	        List<String> newLeft = new ArrayList<>();
	        List<String> finalNoLeft = new ArrayList<>();

	        boolean b = true;

	        while (b) 
	        {
	            b = false;
	            for (int j=0;j<vRules.size();j++) 
	            {
	            	String s= vRules.get(j);
	                char c = s.charAt(0);
	                if (Character.isUpperCase(c)) 
	                {
	                    boolean startsWithVar = false;
	                    for (int k=0; k<i; k++) 
	                    {
	                        String var = variables[k];
	                        if (s.startsWith(var)) 
	                        {
	                        	if(newNoLeft.contains(s)) 
	                        	{
	                        		newNoLeft.remove(s);
	                        	}
	                            startsWithVar = true;
	                            b = true;
	                            List<String> prodRules = rules.get(var);
	                            for (int n=0;n<prodRules.size();n++) 
	                            {
	                                String x = prodRules.get(n);
	                                String y = x + s.substring(var.length());
	                                newNoLeft.add(y);
	                            }

	                        }
	                    }
	                    if (!startsWithVar) 
	                    {
	                        newNoLeft.add(s);
	                    }
	                } 
	                else 
	                {
	                    newNoLeft.add(s);
	                }
	            }
	            vRules = newNoLeft;
	            newNoLeft = new ArrayList<>(); 
	        }

	        for (int q=0;q<vRules.size();q++) 
	        {
	        	if (vRules.get(q).startsWith(v)) 
	        	{
	        		Left.add(vRules.get(q));
	        	} 
	        	else 
	        	{
	        		noLeft.add(vRules.get(q));
	        	}
	        	
	        }
	        if (!Left.isEmpty()) 
	        {
	            String newVar = v + "'";
	            for (int q=0;q<Left.size();q++) 
	            {
	                String d = Left.get(q);
	                String l = d.substring(1) + v + "'";
	                newLeft.add(l);
	            }

	            newLeft.add("e");
	            rules.put(newVar, newLeft);
	        }
	        

	        if (!noLeft.isEmpty() && !Left.isEmpty()) 
	        {
	            String newVar = v + "'";
	            newVariables.add(newVar);
	            for (int q=0;q<noLeft.size();q++) 
	            {
	                String d = noLeft.get(q);
	                String l = d + newVar;
	                finalNoLeft.add(l);
	            }

	            rules.put(v, finalNoLeft);
	        }
	        else 
	        {
	        	rules.put(v, vRules);	        	
	        }
	    }
	}



	public static void main(String[] args) {

		String cfg="S;I;D;W;K#c;g;n;t#S/nKS,gDKS,nIg,ScWSW;I/WSWK,DcWn,IcSgS,InS;D/IIcWS,gKWS,g;W/DK,WWnI,t,cS,DtIt,WKtD;K/gStSI,WIg,Sn";
		CfgLeftRecElim cfgg= new CfgLeftRecElim(cfg);
		cfgg.eliminateLeftRecursion();
		System.out.print(cfgg.toString());
	}

}
