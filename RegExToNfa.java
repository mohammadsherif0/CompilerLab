package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

 
/**
 * Write your info here
 * 
 * @name Mohammad Sherif Elwan
 * @id 49-1865
 * @labNumber 13
 */

public class RegExToNfa { 


	/**
	 * Constructs an NFA corresponding to a regular expression based on Thompson's
	 * construction
	 * 
	 * @param input The alphabet and the regular expression in postfix notation for
	 *              which the NFA is to be constructed
	 */
	String alphabet = "";
	ArrayList<Integer> states = new ArrayList<Integer>();
	private Stack<Transition[]> nfaStack;
	private ArrayList<Transition> t;
	
	public RegExToNfa(String input) {
		// TODO Auto-generated constructor stub
		
		
		
		String[] st=input.split("#",2);
		String alpha=st[0];
		alphabet = alpha;
		String pstFix=st[1];
		
		
		nfaStack=new Stack<Transition[]>();
		t=new ArrayList();
		int len=pstFix.length();
		int currentState=0;
		
		for(int i=0; i<len;i++) 
		{
			char sym= pstFix.charAt(i);
			if(Character.isLetter(sym)) 
			{
				Transition stateTransition=new Transition(currentState,sym,currentState+1);
				Transition AStransition = new Transition(currentState,'$',currentState+1);
				Transition[] TA = new Transition[] {stateTransition,AStransition };
				nfaStack.push(TA);				
				t.add(new Transition(currentState,sym,currentState+1));
				states.add(currentState);
				states.add(currentState+1);
				currentState+=2;
			}
			
			else if(isOperator(sym))
			{
				if(sym=='.') 
				{
					Transition[] oper2 = nfaStack.pop();					
					Transition[] oper1= nfaStack.pop();
					int StartOper1 = 0;
					int StartOper2 = 0;
					int AcceptOper1 = 0;
					int AcceptOper2 = 0;
					Transition newTrans = null;
					
					ArrayList<Transition> tran= new ArrayList<>();
					for(int w=0;w<oper1.length;w++) 
					{
						
						if(oper1[w].symbol == '$')
						{
							StartOper1 = oper1[w].currState;
							AcceptOper1 = oper1[w].nxtState;

						}
						else
							tran.add(oper1[w]);
							
					}
					for(int w=0;w<oper2.length;w++) 
					{
						
						if(oper2[w].symbol == '$')
						{
							StartOper2 = oper2[w].currState;
							AcceptOper2 = oper2[w].nxtState;
						}
						else
							tran.add(oper2[w]);
							
					}
					
					for(int i1 = 0 ; i1<states.size();i1++)
					{
						if(states.get(i1) == StartOper2)
							states.remove(i1);
					}
			
					for(int j=0;j<tran.size();j++)
					{
						Transition trans=tran.get(j);
						if(StartOper2==trans.currState) 
						{
							newTrans=new Transition(AcceptOper1,trans.getSymbol(),trans.getNxtState());
							tran.remove(j);
							tran.add(newTrans);
							j--;
						}
					}
					Transition AStransition = new Transition(StartOper1,'$',AcceptOper2);
					tran.add(AStransition);
					Transition[] transitionsArray = tran.toArray(new Transition[tran.size()]);
					//Transition concat=new Transition(oper1.getNxtState(),oper2.getSymbol(),oper2.getNxtState());
					//t.add(concat);
					nfaStack.push(transitionsArray);

				}
				else if(sym=='|') 
				{
					Transition[] oper2 = nfaStack.pop();
					Transition[] oper1 = nfaStack.pop();
					
					int StartOper1 = 0;
					int StartOper2 = 0;
					int AcceptOper1 = 0;
					int AcceptOper2 = 0;
					
					ArrayList<Transition> tran= new ArrayList<>();
					
					for(int w=0;w<oper1.length;w++) 
					{
						
						if(oper1[w].symbol == '$')
						{
							StartOper1 = oper1[w].currState;
							AcceptOper1 = oper1[w].nxtState;
						}
						else
							tran.add(oper1[w]);
							
					}
					for(int w=0;w<oper2.length;w++) 
					{
						
						if(oper2[w].symbol == '$')
						{
							StartOper2 = oper2[w].currState;
							AcceptOper2 = oper2[w].nxtState;
						}
						else
							tran.add(oper2[w]);
							
					}
					states.add(currentState);
					states.add(currentState+1);
					int initState=currentState++;
					int endState=currentState++;
					Transition union1=new Transition(initState,'e',StartOper1);
					Transition union2=new Transition(initState,'e',StartOper2);
					Transition union3=new Transition(AcceptOper1,'e',endState);
					Transition union4=new Transition(AcceptOper2,'e',endState);
					Transition union5=new Transition(initState,'$',endState);
					t.add(union1);
					t.add(union2);
					t.add(union3);
					t.add(union4);
					Transition[] unionArray = new Transition[] { union1, union2, union3, union4};
					ArrayList<Transition> unionA = new ArrayList<>(Arrays.asList(unionArray));
					unionA.addAll(tran);
					unionA.add(union5);
					Transition[] uArray = unionA.toArray(new Transition[unionA.size()]);
					nfaStack.push(uArray);

					
					
				}
				else if(sym=='*') 
				{
					Transition[] oper=nfaStack.pop();
					
					int StartOper=0;
					int AccOper=0;
					
					ArrayList<Transition> tran= new ArrayList<>();
					
					for(int w=0;w<oper.length;w++) 
					{
						if(oper[w].symbol=='$') 
						{
							StartOper=oper[w].currState;
							AccOper=oper[w].nxtState;
							
						}
						else
							tran.add(oper[w]);
					}										
					states.add(currentState);
					states.add(currentState+1);
					int newStartState=currentState++;
					int newAccState=currentState++;					
					Transition startToOldStart=new Transition(newStartState,'e',StartOper);
					Transition startToGoal=new Transition(newStartState,'e',newAccState);
					Transition oldGoalToGoal=new Transition(AccOper,'e',newAccState);
					Transition oldGoalToOldStart=new Transition(AccOper,'e',StartOper);
					
					tran.add(oldGoalToOldStart);
					tran.add(oldGoalToGoal);
					tran.add(startToGoal);
					tran.add(startToOldStart);
					tran.add(new Transition(newStartState,'$',newAccState));
					
					Transition[] starArray=tran.toArray(new Transition[tran.size()]);
					
					nfaStack.push(starArray);

				}

			Collections.sort(t);
			}
			
		}

	}
	
	private boolean isOperator(char op){
		if(op=='.'|| op=='|'|| op=='*') 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}  
	//1,')',3
	 static class Transition implements Comparable<Transition>{
		int currState;
		char symbol;
		int nxtState;
		
		public Transition(int currState,char symbol, int nxtState) {
			this.currState=currState;
			this.symbol=symbol;
			this.nxtState=nxtState;
		}
		public int getCurrState() {
		        return currState;
		}

		public char getSymbol() {
		        return symbol;
		}

		public int getNxtState() {
		        return nxtState;
		}

		public void setCurrState(int currState) {
		        this.currState = currState;
		}

		public void setSymbol(char symbol) {
		        this.symbol = symbol;
		}

		public void setNxtState(int nxtState) {
		        this.nxtState = nxtState;
		}
		
		public String toString()
		{
			return currState + "," + symbol + "," + nxtState;
		}
		@Override
		public int compareTo(Transition o) {
			// TODO Auto-generated method stub
			int x = Integer.compare(this.currState, o.currState);
			if(o.symbol == '$')
				return 1;
			if(this.symbol == '$')
				return -1;
			if(x==0) 
			{
				if(this.symbol == o.symbol)
					return Integer.compare(this.nxtState, o.nxtState);
				else if(this.symbol=='e')
				{
					return -1;
				}
				else if(o.symbol=='e') 
				{
					return 1;
				}

				x= Character.compare(this.symbol, o.symbol);
			}
			return x;
		}

	}

	 
	/**
	 * @return Returns a formatted string representation of the NFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		int StartState = 0;
		int AcceptState = 0;
		String trans = "";
		Collections.sort(states);
		for(int j = 0 ; j<states.size();j++)
		{
			if(j != states.size()-1)
				trans += states.get(j)+";";
			else
				trans += states.get(j)+"#";
		}
		trans += alphabet + "#";
		Arrays.sort(nfaStack.peek());
		for(int i=0;i<nfaStack.peek().length;i++) 
		{
			Transition q=(Transition) nfaStack.peek()[i];
			if(q.symbol != '$')
			{
				if(i != nfaStack.peek().length - 1)
					trans += q.toString()+";";
				else
					trans += q.toString()+"#";
			}
			else
			{
				StartState = nfaStack.peek()[i].currState;
				AcceptState = nfaStack.peek()[i].nxtState;
			}
		}
		trans += StartState + "#";
		trans += AcceptState;
		return trans;
	}
	
	
	
	
	public static void main(String[] args) {
	    // Example input string
	    String input="a;i;q;v#iaq.q.va|.|"; // Example input string "A#R"
	    
	    // Create the RegExToNfa object
	    RegExToNfa nfa = new RegExToNfa(input);
	    System.out.print(nfa.toString());
	}
}
