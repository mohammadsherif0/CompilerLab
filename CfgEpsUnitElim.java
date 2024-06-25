package csen1002.main.task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Write your info here
 * 
 * @name Mohammad Sherif Elwan
 * @id 49-1865
 * @labNumber 13
 */

public class CfgEpsUnitElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *             representation follows the one in the task description
	 */
	
	String[] variables;
	String[] terminals;
	static Hashtable <String,ArrayList<String>> rules;

	
	public CfgEpsUnitElim(String cfg) {
		// TODO Auto-generated constructor stub
		
		String[] parts= cfg.split("#");
		
		variables=parts[0].split(";");
		terminals=parts[1].split(";");
		rules=new Hashtable<>();
		
		String[] rul=parts[2].split(";");
		for(int i=0;i<rul.length;i++) 
		{
			String[] q=rul[i].split("/");
			String[] q1=q[1].split(",");
			ArrayList<String> st= new ArrayList<>();
			for(int j=0;j<q1.length;j++) 
			{
				st.add(q1[j].trim());
			}
			rules.put(q[0], st);
		}
		
		
		
		
	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		String f= "";
		for(int i=0;i<variables.length;i++) 
		{
			f += variables[i]+";";
		}
		f = f.substring(0, f.length()-1);
		f += "#";
		for(int i=0;i<terminals.length;i++) 
		{
			f += terminals[i]+";";
		}
		f = f.substring(0, f.length()-1);
		f += "#";
		for(int i=0;i<variables.length;i++)
		{
			ArrayList<String> gArray = new ArrayList<>();
			gArray = rules.get(variables[i]);
			f += variables[i] + "/";
			Collections.sort(gArray);
			for(int j=0;j<gArray.size();j++)
			{
				f += gArray.get(j) + ",";
			}
			f = f.substring(0, f.length()-1);
			f += ";";
		}
		f = f.substring(0, f.length() - 1);
		return f;
	}

	/**
	 * Eliminates Epsilon Rules from the grammar
	 */
	public void eliminateEpsilonRules() {
		// TODO Auto-generated method stub
		
		List<String> epsVar= new ArrayList<>();
		ArrayList<String> handledVars = new ArrayList<>();

		for(int i=0;i<variables.length;i++) 
		{
			String v=variables[i];
			ArrayList<String> varRules=rules.get(v);
			for(int j=0;j<varRules.size();j++) 
			{
				if(varRules.get(j).equals("e")) 
				{
					epsVar.add(v);
					varRules.remove(j);
					handledVars.add(v);
					j--;
					i=0;
				
					for(int k=0;k<variables.length;k++) 
					{
						String var=variables[k];
						
						
						if(true) 
						{
							ArrayList<String> rule=rules.get(var.trim());
							ArrayList<String> newRules = new ArrayList<>();
							for(int q=0;q<rule.size();q++) 
							{
								String vr= rule.get(q);
								if(vr.contains(v)) 
								{
									
									
									if(vr.length()==1 && !handledVars.contains(var)) 
									{
										vr="e";
										newRules.add(vr.trim());

									}
									else if(vr.length()>1)
									{										
										for(int m=0;m<vr.length();m++) 
										{
											if(vr.charAt(m)==v.charAt(0)) 
											{
												
												String vv= vr.substring(0, m)+vr.substring(m+1);
												if(!rule.contains(vv) && !newRules.contains(vv)) 
												{
													newRules.add(vv.trim());
												}
											}
										}
									}																
									
								}
								
								rule.addAll(newRules);								
								newRules = new ArrayList<>();
							}
							rules.put(var.trim(), rule);
						}
					}
				}
			}	
			
			
		}
		
		
	}

	/**
	 * Eliminates Unit Rules from the grammar
	 */
	public void eliminateUnitRules() {
		
		// TODO Auto-generated method stub
		while(true)
		{
			boolean change = false;
			for(int i=0;i<variables.length;i++) 
			{
				String v=variables[i];
				ArrayList<String> varRules=rules.get(v);
				Set<String> newRule= new HashSet<>();

				for(int j=0;j<varRules.size();j++) 
				{
					String s= varRules.get(j);
					if(!s.equals(v) && ruleExists(s) && Character.isUpperCase(s.charAt(0)))
					{
						change = true;
						newRule.addAll(rules.get(s));
						varRules.remove(s);
						
					}
					else if(s.equals(v))
					{
						varRules.remove(s);
						change = true;
					}
				}
				newRule.addAll(rules.get(v));
				newRule.remove(v);
				rules.get(v).clear();
				rules.get(v).addAll(newRule);

			}
			if(change==false) 
			{
				break;
			}
		}

	}
	
	public boolean ruleExists(String s) {
		for(int i=0;i<variables.length;i++) 
		{
			String v=variables[i];
			if(v.equals(s))
				return true;
		}
		return false;
	}


	
	 public static void main(String[] args) {
		 
	        String cfg ="S;Y;X;J;P#k;l;p#S/SpXkS,Y,pJkS;Y/PpJ,S,Y,lPXYp,lX;X/J,JJp,Y,e,lPXpY,lPpX,pJp;J/Jl,e,lXSk,p,pYJ;P/kPS,pJJl";
	        
	        CfgEpsUnitElim cfgEpsUnitElim = new CfgEpsUnitElim(cfg);
	        cfgEpsUnitElim.eliminateEpsilonRules();
	        cfgEpsUnitElim.eliminateUnitRules();
	        //System.out.println(rules.toString());      

	        System.out.println(cfgEpsUnitElim.toString());
	    }
}
