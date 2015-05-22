package ru.rintd.model.res;
/**
 * 
 * @author sheihar
 *
 */
public interface SomeActionI {
	public void commit(Model m);

	public void rollback(Model m);
}
