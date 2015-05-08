package ru.rintd.model.res;

import java.util.Stack;

public class SomeActionS {
	private Stack<SomeAction> actions = new Stack<SomeAction>();
	private Stack<SomeAction> backedActions = new Stack<SomeAction>();
	private Model m;
	
	public SomeActionS(Model m){
		this.m = m;
	}
	
	public void push(SomeAction action){
		action.commit(m);
		actions.push(action);
	}
	
	public void back(){
		if (!actions.empty()){
			SomeAction a = actions.pop();
			a.rollback(m);
			backedActions.push(a);
		}
		
	}
	
	public void forvard(){
		if (!backedActions.empty()){
			SomeAction a = backedActions.pop();
			a.commit(m);
			push(a);
		}
	}
}
