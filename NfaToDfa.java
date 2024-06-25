package csen1002.main.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;


/**
 * Write your info here
 * 
 * @name
 * @id
 * @labNumber
 */

public class NfaToDfa {

	/**
	 * Constructs a DFA corresponding to an NFA
	 * 
	 * @param input A formatted string representation of the NFA for which an
	 *              equivalent DFA is to be constructed. The string representation
	 *              follows the one in the task description
	 */
	
	private int[] nfaStates;
	private String[] alphabet;
	private String alphab;
	//private ArrayList<Transition> transitions;
	private int initState;
	private int[] accState;
	Hashtable<Integer,ArrayList<Integer>> epsilonClosure;
	ArrayList<ArrayList<Integer>> dfaStates;
	ArrayList<ArrayList<Integer>> acceptStates;
	ArrayList<Transition> dfaTransitions;
	ArrayList<ArrayList<Integer>> checkStates;
	String[] trans;
	ArrayList<Integer>[] epsilonTransitions;
	
	
	public NfaToDfa(String input) {
		// TODO Auto-generated constructor stub
		
		String[] parts = input.split("#", 5);
		alphab = parts[1];
		String[] state = parts[0].split(";");
		String[] alpha = parts[1].split(";");
		trans = parts[2].split(";");
		initState = Integer.parseInt(parts[3]);
		String[] acc= parts[4].split(";");
		
		dfaStates=new ArrayList<>();
		acceptStates= new ArrayList<>();
        
		nfaStates=new int[state.length];
		for(int i=0;i<state.length;i++) 
		{
			nfaStates[i]=Integer.parseInt(state[i]);
		}
		
        epsilonTransitions = new ArrayList[nfaStates.length];
        for (int i = 0; i < nfaStates.length; i++) {
            epsilonTransitions[i] = new ArrayList<>();
        }

        populateEtranstions() ;
		alphabet=new String[alpha.length];
		for(int i=0;i<alpha.length;i++) 
		{
			alphabet[i]=alpha[i];
		}
		
		
		accState=new int[acc.length];
		for(int i=0;i<acc.length;i++) 
		{
			accState[i]=Integer.parseInt(acc[i]);
		}
		
		epsilonClosure = new Hashtable<Integer,ArrayList<Integer>>(); 
		
		for(int i = 0 ; i<nfaStates.length;i++)
		{
			ArrayList<Integer> epsClosure = epsilonClosuree(nfaStates[i]);
			epsilonClosure.put(nfaStates[i], epsClosure);
		}
		
		ArrayList<Integer> initEps=epsilonClosure.get(initState);
		//dfaStates.add(initEps);
		
		
		ArrayList<ArrayList<Integer>> checkStack = new ArrayList<>();
		checkStack.add(initEps);
		
		checkStates = new ArrayList<>();
		Set<ArrayList<Integer>> seenStates = new HashSet<>(); 
		
		dfaTransitions = new ArrayList<Transition>();
		boolean found = false;
		
		while(!checkStack.isEmpty()) 
		{
			ArrayList<Integer> curr=checkStack.remove(0);
			Set<Integer> curState = new HashSet<>(curr);
			curr.clear();
			curr.addAll(curState);
			
		    if (seenStates.contains(curr)) 
		    { 
		        continue;
		    }
		    seenStates.add(curr);
		    checkStates.add(curr);
			for(int i=0;i<alphabet.length;i++) 
			{
				char symbol=alphabet[i].charAt(0);
				ArrayList<Integer>nxt=new ArrayList<>();

				for(int j=0;j<curr.size();j++) 
				{
					int nfaState=curr.get(j);

					for(int i1=0;i1<trans.length;i1++) 
					{
						String[] transition = trans[i1].split(",");
						int from = Integer.parseInt(transition[0]);
						char with = transition[1].charAt(0);
						int to = Integer.parseInt(transition[2]);
						if(from==nfaState && with==symbol) 
						{
							ArrayList<Integer> nxtEps = epsilonClosure.get(to);
							nxt.addAll(nxtEps);
							found = true;
						}
					}

				}
				if(nxt.isEmpty())
				{
					nxt.add(-1);
				}
				if(!containsState(checkStates,nxt) && !containsState(checkStack,nxt) && !equalStates(nxt,curr) )
				{
					checkStack.add(nxt);
				}
				Transition x;
				if(found == true)
					 x = new Transition(curr,symbol,nxt);
				else
				{
					ArrayList<Integer> none = new ArrayList<Integer>();
					none.add(-1);
					x = new Transition(curr,symbol,none);
				}
				dfaTransitions.add(x);
			}
		}
		checkStates.sort(new compareStates());

		for(int i=0;i<checkStates.size();i++) 
		{
			ArrayList<Integer> dfaState=checkStates.get(i);
			
			for(int j=0;j<accState.length;j++) 
			{
				int acpt= accState[j];
				if(dfaState.contains(acpt) && !containsState(acceptStates,dfaState)) 
				{
					acceptStates.add(dfaState);
				}
			}
		}
		acceptStates.sort(new compareStates());
		
		Collections.sort(dfaTransitions);
		
	}
	
    public static boolean containsState(ArrayList<ArrayList<Integer>> states, ArrayList<Integer> targetState) 
    {
        for (int i = 0; i < states.size(); i++) {
            ArrayList<Integer> list = states.get(i);
            if (list.size() == targetState.size()) {
                boolean isEqual = true;
                for (int j = 0; j < list.size(); j++) {
                    if (!list.get(j).equals(targetState.get(j))) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void populateEtranstions() 
    {
    	for (int i = 0; i < trans.length; i++) 
    	{
    	    String transition = trans[i];
    	    String[] partss = transition.split(",");
    	    int from = Integer.parseInt(partss[0]);
    	    char symbol = partss[1].charAt(0);
    	    int to = Integer.parseInt(partss[2]);

    	    if (symbol == 'e') {
    	        epsilonTransitions[from].add(to);
    	    }
    	}
    }
    public ArrayList<Integer> epsilonClosuree(int nfaState) 
    {
        ArrayList<Integer> closure = new ArrayList<>();
        boolean[] visited = new boolean[nfaStates.length];
        Stack<Integer> stack = new Stack<>();
        
        stack.push(nfaState);
        closure.add(nfaState);
        visited[nfaState] = true;

        while (!stack.isEmpty()) 
        {
            int curr = stack.pop();
            ArrayList<Integer> transitions = epsilonTransitions[curr];

            if (transitions != null) {
            	for (int i = 0; i < transitions.size(); i++) 
            	{
            	    int nextState = transitions.get(i);
            	    if (!visited[nextState]) {
            	        stack.push(nextState);
            	        closure.add(nextState);
            	        visited[nextState] = true;
            	    }
            	}
            }
        }

        Collections.sort(closure);
        return closure;
    }

	
	public boolean equalStates(ArrayList<Integer> state1,ArrayList<Integer> state2)
	{
		if(state1.size() != state2.size())
			return false;
		for(int i = 0 ; i < state1.size() ; i++)
		{
			if(state1.get(i) != state2.get(i))
				return false;
		}
		return true;
	}
	
	static class Transition implements Comparable<Transition>
	{
		ArrayList<Integer> currState;
		char symbol;
		ArrayList<Integer> nxtState;
		
		public Transition(ArrayList<Integer> currState,char symbol,ArrayList<Integer> nxtState) 
		{
			this.currState=currState;
			this.symbol=symbol;
			this.nxtState=nxtState;
		}
		
		public String toString()
		{
			String f = "";
			for(int i = 0 ; i < currState.size();i++)
			{
				if(i < currState.size() - 1)
					f+=currState.get(i) + "/";
				else
					f+=currState.get(i) + ",";
			}
			f += symbol + ",";
			for(int i = 0 ; i < nxtState.size();i++)
			{
				if(i < nxtState.size() - 1)
					f+=nxtState.get(i) + "/";
				else
					f+=nxtState.get(i);
			}
			return f;
		}

		@Override
		public int compareTo(Transition o) 
		{
			
			for (int i = 0; i < this.currState.size() && i < o.currState.size(); i++) 
			{
		        int compareCurr = Integer.compare(this.currState.get(i), o.currState.get(i));
		        if (compareCurr != 0) 
		        {
		            return compareCurr;
		        }
		    }
		    if (this.currState.size() != o.currState.size()) 
		    {
		        return Integer.compare(this.currState.size(), o.currState.size());
		    }
		    return Integer.compare(this.currState.size(), o.currState.size());
		}
		
	
	}

	/**
	 * @return Returns a formatted string representation of the DFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		
		String outStates="";
		
		for(int i=0;i<checkStates.size();i++) 
		{
			ArrayList<Integer> state = checkStates.get(i);
			for(int j = 0; j < state.size(); j++)
			{
				if(j < state.size() - 1)
					outStates += state.get(j) + "/";
				else if( i < checkStates.size() - 1)
					outStates += state.get(j) + ";";
				else
					outStates += state.get(j) + "#";
			}
		}
		
		outStates += alphab + "#";
		
		for(int i=0;i<dfaTransitions.size();i++) 
		{
			Transition transition = dfaTransitions.get(i);
			if(i < dfaTransitions.size() - 1)
				outStates += transition.toString() + ";";
			else
				outStates += transition.toString() + "#";
		}
		
		ArrayList<Integer> startEps = epsilonClosure.get(initState);
		for(int i = 0 ; i < startEps.size() ; i++)
		{
			if(i < startEps.size() - 1)
				outStates += startEps.get(i) + "/";
			else
				outStates += startEps.get(i) + "#";
		}
		
		
		for(int i = 0 ; i < acceptStates.size() ; i++)
		{
			ArrayList<Integer> state = acceptStates.get(i);
			for(int j = 0; j < state.size(); j++)
			{
				if(j < state.size() - 1)
					outStates += state.get(j) + "/";
				else if( i < acceptStates.size() - 1)
					outStates += state.get(j) + ";";
				else
					outStates += state.get(j);
			}
		}
		
		return outStates;
	}
	
	public class compareStates implements Comparator<ArrayList<Integer>>
	{

		@Override
		public int compare(ArrayList<Integer> state1, ArrayList<Integer> state2) 
		{
			// TODO Auto-generated method stub
			
			int min=0;
			if(state1.size()<state2.size()) 
			{
				 min =state1.size();
			}
			else 
			{
				min =state2.size();
			}
			
			for(int i =0;i<min;i++) 
			{
				int comp=Integer.compare(state1.get(i), state2.get(i));
				
				if(comp!=0) 
				{
					return comp;
				}
			}
			
			
			return Integer.compare(state1.size(), state2.size());
		}
		
	}
	
	public static void main(String[] args) {
	    // Example input string
	    String input="0;1;2;3;4;5;6;7;8;9;10#j;m#0,e,1;0,e,5;0,j,1;0,j,3;0,j,5;0,j,7;0,j,8;0,j,9;0,j,10;0,m,0;0,m,1;0,m,2;0,m,3;0,m,6;1,e,8;1,e,10;1,j,0;1,j,5;1,j,6;1,j,7;1,j,8;1,m,0;1,m,1;1,m,3;1,m,4;1,m,5;1,m,8;2,j,0;2,j,2;2,j,3;2,j,6;2,j,7;2,j,8;2,m,2;2,m,4;2,m,5;2,m,6;2,m,8;2,m,9;3,j,0;3,j,5;3,j,6;3,j,8;3,j,9;3,j,10;3,m,1;3,m,4;3,m,5;3,m,9;4,e,7;4,j,0;4,j,2;4,j,3;4,j,5;4,j,6;4,j,8;4,j,9;4,m,0;4,m,1;4,m,5;4,m,8;4,m,10;5,e,4;5,j,2;5,j,6;5,j,7;5,j,9;5,m,2;5,m,3;5,m,5;5,m,6;6,e,8;6,j,0;6,j,2;6,j,3;6,j,4;6,j,6;6,j,7;6,m,2;6,m,4;6,m,5;6,m,6;6,m,7;7,e,0;7,e,4;7,j,0;7,j,1;7,j,2;7,j,3;7,j,10;7,m,0;7,m,1;7,m,4;7,m,6;7,m,7;7,m,9;7,m,10;8,j,0;8,j,1;8,j,2;8,j,7;8,j,8;8,j,9;8,m,1;8,m,2;8,m,3;8,m,4;8,m,5;8,m,7;8,m,10;9,j,1;9,j,2;9,j,3;9,j,4;9,j,5;9,j,10;9,m,5;9,m,7;9,m,8;9,m,10;10,j,3;10,j,4;10,j,6;10,j,7;10,m,1;10,m,4;10,m,5;10,m,7#0#0;2;6;8;10";
	    

	    NfaToDfa nfa = new NfaToDfa(input);
	    System.out.print(nfa.toString());
	}

}


