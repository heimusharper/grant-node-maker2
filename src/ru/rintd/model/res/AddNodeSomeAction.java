package ru.rintd.model.res;

import ru.rintd.json2grid.Node;
/**
 * действие - добавление узла
 * @author sheihar
 *
 */
public class AddNodeSomeAction extends SomeAction implements SomeActionI{
	private Node node;
	private int level;

	
	
	public AddNodeSomeAction(Node node, int level) {
		super();
		this.node = node;
		this.level = level;
	}

	@Override
	public void commit(Model m) {
		m.setNode(node, level);
	}

	@Override
	public void rollback(Model m) {
		m.removeNode(node, level);
	}
	
}
