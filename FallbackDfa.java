package csen1002.main.task3;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Write your info here
 * 
 * @name 
 * @id 49-
 * @labNumber 13
 */

public class FallbackDfa {

	/**
	 * Constructs a Fallback DFA
	 * 
	 * @param fdfa A formatted string representation of the Fallback DFA. The string
	 *             representation follows the one in the task description
	 */
	
	private int[] states;
	private String[] alpha;
	ArrayList<Transition> transitions;
	private int initial;
	private int[] acptStates;
	
	
	Stack<Integer> stateStack = new Stack<>();
	
	public FallbackDfa(String fdfa) {
		// TODO Auto-generated constructor stub
		
		String []parts=fdfa.split("#",5);
		String[] state = parts[0].split(";");
		alpha=parts[1].split(";");
		String [] tran=parts[2].split(";");
		initial=Integer.parseInt(parts[3]);
		String [] accepted=parts[4].split(";");
		
		states=new int[state.length];
		for(int i=0;i<state.length;i++) 
		{
			states[i]=Integer.parseInt(state[i]);
		}
		
		transitions=new ArrayList<>();
		for(int i=0;i<tran.length;i++) 
		{
			String[] t=tran[i].split(",");
			int currState=Integer.parseInt(t[0]);
			char symbol=t[1].charAt(0);
			int nxtState=Integer.parseInt(t[2]);
			transitions.add(new Transition(currState,symbol,nxtState));
		}
		
		acptStates=new int[accepted.length];
		for(int i=0;i<accepted.length;i++) 
		{
			acptStates[i]= Integer.parseInt(accepted[i]);
		}
		
	}
	
	
	
	
	 static class Transition{
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

	 }

	/**
	 * @param input The string to simulate by the FDFA.
	 * 
	 * @return Returns a formatted string representation of the list of tokens. The
	 *         string representation follows the one in the task description
	 */
	public String run(String input) {
		// TODO Auto-generated method stub
		
		int curr=initial;
		stateStack.push(curr);
		int i=0;
		int k =0;
		int r = 0;
		String out="";
		String ans = "";
		while(r<input.length()) 
		{

			for(int y = k ; y < input.length();y++)
			{
				char with=input.charAt(y);
				for(int j=0;j<transitions.size();j++) 
				{
					Transition tr= transitions.get(j);
					
					if(tr.getCurrState()==curr && tr.getSymbol()==with) 
					{
						curr =tr.getNxtState();
						stateStack.push(curr);
						break;
					}
				}
			}
			String noAcc ="";
			i = input.length();
			boolean flag = false;
			while(true) 
			{
				curr=stateStack.pop();
				if(i == input.length())
				{
					noAcc = input.substring(k, i) + "," + curr +";" ;
				}
				for(int q=0;q<acptStates.length;q++) 
				{
					if(curr==acptStates[q]) 
					{
						out=input.substring(k, i) + "," + curr +";" ;
						ans += out;
						stateStack.clear();
						curr = initial;
						stateStack.push(curr);
						k = i;
						r = k;
						flag = true;
					}
				}
				if(flag)
					break;
				if(stateStack.isEmpty())
				{
					ans += noAcc;
					stateStack.clear();
					curr = initial;
					stateStack.push(curr);
					k = input.length();
					r = input.length();
					break;	
				}
				
				i=i-1;
			}
		 
		}
		
		return ans.substring(0, ans.length()-1);
	}
	
	
	public static void main(String[] args) {
		FallbackDfa f = new FallbackDfa("0;1;2;3#a;b#0,a,0;0,b,1;1,a,2;1,b,1;2,a,0;2,b,3;3,a,3;3,b,3#0#1;2");
		System.out.println(f.run("baababb"));
		
	}
	

	
}
