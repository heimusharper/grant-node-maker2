package ru.rintd.controller.network;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.rintd.g14.data.Nodes;
import org.rintd.g14.util.Commands;

public class Connector extends Commands{

	public Connector(String[] args, String addr) throws InvalidName,
			AdapterInactive, ServantNotActive, WrongPolicy {
		super(args, addr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Notify_fire_condition(Nodes node) {
		System.out.print("Fire: "+node.name);
		
	}

	@Override
	public void Notify_node_changed(Nodes node) {
		System.out.print("Changed: "+node.name);
		
	}

	@Override
	public void Notify_node_added(Nodes node) {
		System.out.print("Added: "+node.name);
		
	}

}
