package csen1002.main.task6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

public class CfgFirstFollow {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	
	String[] variables;
	String[] terminals;
	Hashtable<String, List<String>> rules;
	HashMap<String, Set<String>> firsts;
	HashMap<String, Set<String>> follows;


	
	public CfgFirstFollow(String cfg) {
		// TODO Auto-generated constructor stub
		
		String[] parts=cfg.split("#");
		variables=parts[0].split(";");
		terminals=parts[1].split(";");
		
		rules=new Hashtable<>();
		String[] vRules=parts[2].split(";");
		
		for(int i=0;i<vRules.length;i++) 
		{
			String[] r=vRules[i].split("/");
			String v=r[0];
			String[]vr=r[1].split(",");
			
			List<String> varRule= new ArrayList<>();
			
			for(int j=0;j<vr.length;j++) 
			{
				varRule.add(vr[j]);
			}
			rules.put(v, varRule);
		}				
		
	}

	/**
	 * Calculates the First Set of each variable in the CFG.
	 * 
	 * @return A string representation of the First of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	
	public String first() {
		// TODO Auto-generated method stub
		
		firsts= new HashMap<>();
		String res="";
		for(int i=0;i<variables.length;i++) 
		{
			String var= variables[i];
			firsts.put(var, new HashSet<>());
		}
		boolean change=true;
		while(change) 
		{
			change=false;
			for(int i=0; i<variables.length;i++) 
			{
				String var= variables[i];
				List<String> vRules= rules.get(var);

				for(int j=0;j<vRules.size();j++) 
				{
					String rule=vRules.get(j);					
					char s=rule.charAt(0);
					boolean checker=firsts.get(var).contains(String.valueOf(s));
					if((!Character.isUpperCase(s) || s=='e') && checker==false)
					{
						firsts.get(var).add(String.valueOf(s));
						change=true;
					}
					else if(Character.isUpperCase(s))
					{
						for (int r = 0; r < rule.length(); r++) 
						{
						    char curr = rule.charAt(r);
						    if (Character.isLowerCase(curr)) 
						    {
						        if (!firsts.get(var).contains(String.valueOf(curr))) 
						        {
						            firsts.get(var).add(String.valueOf(curr));
						            change = true;
						        }
						        break;
						    } 
						    else if (Character.isUpperCase(curr)) 
						    {
						        Set<String> ff = firsts.get(String.valueOf(curr));
						        boolean eps = false;
						        for (int k = 0; k < ff.size(); k++) 
						        {
						            String f=(String)ff.toArray()[k];
						            if (!f.equals("e")) 
						            {
						                if (!firsts.get(var).contains(f)) 
						                {
						                    firsts.get(var).add(f);
						                    change = true;
						                }
						            } 
						            else 
						            {
						                eps = true;
						            }
						        }

						        if (!eps) 
						        {
						        	break;						        	
						        }
						    }
						    if (r==rule.length()-1 && !firsts.get(var).contains("e")) 
						    {
						        firsts.get(var).add("e");
						        change = true;
						    }
						}						
					}		
				}
			}			
		}
		for (int i=0;i<variables.length;i++) 
		{
		    String var = variables[i];
		    res += var + "/";
		    Set<String> f = firsts.get(var);
		    List<String> newF = new ArrayList<>(f);
		    Collections.sort(newF);
		    for (int j=0;j<newF.size();j++) 
		    {
		        res += newF.get(j); 
		    }
		    res += ";";
		}		
		res=res.substring(0, res.length() - 1);						
		return res.toString();
	}
	


	/**
	 * Calculates the Follow Set of each variable in the CFG.
	 * 
	 * @return A string representation of the Follow of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	
	public String follow() {
		// TODO Auto-generated method stub
		first();
		follows= new HashMap<>();
		String res="";
		List<String> vars=new ArrayList<>();	
		boolean changed= true;
		
		for(int i=0;i<variables.length;i++) 
		{
			String var=variables[i];
			follows.put(var, new HashSet<>());
			vars.add(var);			
		}
		follows.get(variables[0]).add("$");
		while(changed) 
		{
			changed=false;
			
			for(int i=0;i<variables.length;i++) 
			{
				String v= variables[i];
				List<String> rule= rules.get(v);
				
				for(int j=0;j<rule.size();j++) 
				{
					String s = rule.get(j);
					for(int k=0;k<s.length();k++) 
					{
						char ss= s.charAt(k);	
						String l= String.valueOf(ss);
						
						if(Character.isUpperCase(ss) && vars.contains(l)) 
						{
							if((k+1)<s.length()) 
							{								
								String q= String.valueOf(s.charAt(k+1));
								if(!q.isEmpty() && Character.isLowerCase(s.charAt(k+1)) && !follows.get(l).contains(q)) 
								{
									follows.get(l).add(q);
									changed=true;
								}
								else if(!q.isEmpty() && Character.isUpperCase(s.charAt(k+1))) 
								{
									Set<String> vFirsts=new HashSet<>(firsts.get(q));
									//List<String> vRules=rules.get(s.charAt(k+1));
									Set<String> firstNext = new HashSet<>(firsts.get(q));
									vFirsts.remove("e");
									for(int n=0;n<vFirsts.size();n++) 
									{
										String m=(String) vFirsts.toArray()[n];
										
										if(!follows.get(l).contains(m)) 
										{
											follows.get(l).addAll(vFirsts);
											changed=true;																						
										}
										if(firstNext.contains("e")) 
										{
											if((k+2)<s.length()) 
											{
												String qq= String.valueOf(s.charAt(k+2));
												if(!qq.isEmpty() && Character.isUpperCase(s.charAt(k+2))) 
												{
													Set<String> qRule=new HashSet<>(firsts.get(qq));
													for(int p=0;p<qRule.size();p++) 
													{
														String pp=(String) qRule.toArray()[p];
														
														if(!follows.get(l).contains(pp)) 
														{
															follows.get(l).addAll(qRule);
															follows.get(l).remove("e");
														}

													}
												}
											}

										}

									}
									
								}
							}
							else if(ss==s.charAt(s.length()-1)) 
							{
								Set<String> vFollows=follows.get(v);								
								List<String> vRules=rules.get(l);
								Set<String> firstNext = new HashSet<>(firsts.get(String.valueOf(ss)));
								for(int n=0;n<vFollows.size();n++) 
								{
									String m=(String) vFollows.toArray()[n];
									
									if(!follows.get(l).contains(m)) 
									{
										follows.get(l).add(m);
										changed=true;																				
									}
								}
								if(vRules.contains("e") || firstNext.contains("e")) 
								{
									Set<String> q= follows.get(v);
									if(k>=1 && follows.get(String.valueOf(s.charAt(k-1))) != null)
										follows.get(String.valueOf(s.charAt(k-1))).addAll(q);
								}
							}
						}
					}
				}
				
			}
		}

		
		for (int i=0;i<variables.length;i++) 
		{
		    String var = variables[i];
		    res += var + "/";
		    Set<String> f = follows.get(var);
		    List<String> newF = new ArrayList<>(f);
		    Collections.sort(newF);
		    for (int j=0;j<newF.size();j++) 
		    {
		        res += newF.get(j); 
		    }
		    res += ";";
		}		
		res=res.substring(0, res.length() - 1);						
		return res.toString();
	}
	
	public static void main(String[] args) {

		String cfg="S;R;L;W;V;B;K#a;b;d;l;q;z#S/dWq,V;R/lLa,lBL,bSKSR,VRWR;L/zBKK,zWqKW,l,zLS,lWl,dVdSq,e;W/zLaS,VV,V,S;V/dWRz,aWd,e,S,V;B/VKd,KVdL;K/LBa,VV,BbL";
		//String cfg="S;T;L#a;b;c;d;i#S/ScT,T;T/aSb,iaLb,e;L/SdL,S";
		CfgFirstFollow cfgg= new CfgFirstFollow(cfg);
		
//////		HashMap<String, Set<String>> res=cfgg.firsts;
		System.out.println(cfgg.first());
		System.out.println(cfgg.follow());

	}

}


