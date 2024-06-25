package csen1002.main.task8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;


/**
 * Write your info here
 * 
 * @name Mohammad Sherif Elwan
 * @id 49-1865
 * @labNumber 13
 */

public class CfgLl1Parser {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG, the First sets of
	 *            each right-hand side, and the Follow sets of each variable. The
	 *            string representation follows the one in the task description
	 */


	String[] variables;
	String[] terminals;
	HashMap<String, List<String>> rules;
	HashMap<String, List<String>> firsts;
	HashMap<String, List<String>> follows;
	HashMap<String, HashMap<String, List<String>>> predTable = new HashMap<>();
	Stack<String> pars;


	public CfgLl1Parser(String input) {
		// TODO Auto-generated constructor stub

		String[] parts=input.split("#");
		variables=parts[0].split(";");
		terminals=parts[1].split(";");

		rules= new HashMap<>();
		firsts= new HashMap<>();
		follows= new HashMap<>();



		String[]vr= parts[2].split(";");
		String[]fir=parts[3].split(";");
		String[]fol=parts[4].split(";");

		for(String v:vr) 
		{
			String[] vv=v.split("/");
			String var=vv[0];
			String[] rule=vv[1].split(",");

			List<String> varRule= new ArrayList<>();

			for(String r:rule) 
			{
				varRule.add(r);
			}
			rules.put(var, varRule);
		}

		for(String v:fir) 
		{
			String[] vfir=v.split("/");
			String var=vfir[0];
			String[] first=vfir[1].split(",");

			List<String> varFirst= new ArrayList<>();

			for(String r:first) 
			{
				varFirst.add(r);
			}
			firsts.put(var, varFirst);
		}

		for(String v:fol) 
		{
			String[] vfol=v.split("/");
			String var=vfol[0];
			String[] follow=vfol[1].split(",");

			List<String> varFollow= new ArrayList<>();

			for(String r:follow) 
			{
				varFollow.add(r);
			}
			follows.put(var, varFollow);
		}


		for(String v:variables) 
		{
			HashMap<String, List<String>> ter= new HashMap<>();

			for(String t:terminals) 
			{
				ter.put(t, new ArrayList<>());
			}
			ter.put("$", new ArrayList<>());
			predTable.put(v, ter);
		}


		for(String v:variables) 
		{
			List<String> vRul=rules.get(v);
			List<String> vFi=firsts.get(v);
			List<String> vFo=follows.get(v);

			for (int i = 0; i < vRul.size(); i++) {
				String q = vRul.get(i);
				if (!q.isEmpty() && !q.equals("e")) 
				{
					String fi= vFi.get(i);
					for(int k=0;k<fi.length();k++) 
					{
						char l=fi.charAt(k);
						String ss= Character.toString(l);
						predTable.get(v).get(ss).add(q);
					}
				}
				else if(!q.isEmpty() && q.equals("e")) 
				{
					for(String fo:vFo) 
					{

						for(int k=0;k<fo.length();k++) 
						{
							char l=fo.charAt(k);
							String ss= Character.toString(l);
							predTable.get(v).get(ss).add(q);
						}
					}
				}
			}


		}

	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		// TODO Auto-generated method stub
		input += "$";
		pars=new Stack<>();
		List<String> res= new ArrayList<>();
		pars.push("$");
		pars.push(variables[0]);
		res.add(variables[0]);
		String derive = "S;";
		String parsed ="";
		int i=0;
		while(i<input.length()) 
		{
			parsed = input.substring(0,i);
			char c=input.charAt(i);

			String x=pars.peek();
			char y=x.charAt(0);

			if(Character.isUpperCase(y)) 
			{		
				List<String> q= predTable.get(x).get(String.valueOf(c));
				if(q.isEmpty())
				{
					derive += "ERROR" + ";";
					break;
				}
				if(q.contains("e"))
				{
					pars.pop();
				}
				else
				{
					pars.pop();
					for(int k = q.get(0).length() - 1; k >= 0; k--) 
					{
						String l = String.valueOf(q.get(0).charAt(k));
						pars.push(l);
					}
				}
				res.clear();
				res.addAll(pars);
				derive += parsed;
				for(int y1 = res.size()-1; y1 >=0; y1--)
				{
					if(!res.get(y1).equals("$"))
						derive += res.get(y1);
				}
				derive += ";";
			}

			else if(Character.isLowerCase(y)) 
			{
				if(y==c) 
				{
					pars.pop();
					i++;
				}
				else
				{
					derive += "ERROR" + ";";
					break;
				}
			}
			
			if(y=='$' && c =='$') 
			{
				pars.pop();
				i++;
			}

		}


		derive = derive.substring(0,derive.length()-1);

		return derive;
	}



	public static void main(String[] args) {

		String input="S;Y;W;T;R#j;m;o;r;u;v;z#S/u,vS,oTuS,z;Y/vRo,jY,e;W/z,m,rSu,e;T/vR,rW,e;R/j,mSSY,z#S/u,v,o,z;Y/v,j,e;W/z,m,r,e;T/v,r,e;R/j,m,z#S/$ouvz;Y/ou;W/u;T/u;R/ou";
		CfgLl1Parser CfgLl1= new CfgLl1Parser(input);
		System.out.print(CfgLl1.parse("vorrvvo"));



	}


}
