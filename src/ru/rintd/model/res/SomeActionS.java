package ru.rintd.model.res;

import java.util.Stack;
/**
 * система событий для отмены и возврата изменений
 * @author sheihar
 *
 */
public class SomeActionS {
	private Stack<SomeAction> actions = new Stack<SomeAction>();
	private Stack<SomeAction> backedActions = new Stack<SomeAction>();
	private Model m;
	
	/**
	 * передать модель данных
	 * @param m
	 */
	public SomeActionS(Model m){
		this.m = m;
	}
	
	/**
	 * добавить действие
	 * @param action
	 */
	public void push(SomeAction action){
		action.commit(m);
		actions.push(action);
	}
	/**
	 * отменить действие
	 */
	public void back(){
		if (!actions.empty()){
			SomeAction a = actions.pop();
			a.rollback(m);
			backedActions.push(a);
		}
		
	}
	/**
	 * вернуть отмененное
	 */
	public void forvard(){
		if (!backedActions.empty()){
			SomeAction a = backedActions.pop();
			a.commit(m);
			push(a);
		}
	}
}
