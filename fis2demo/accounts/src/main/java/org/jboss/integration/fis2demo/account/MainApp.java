package org.jboss.integration.fis2demo.account;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.undertow.WARArchive;

public class MainApp {

	public static void main(String[] args) throws Exception {
		Swarm swarm = new Swarm();
		swarm.start();

		WARArchive deployment = ShrinkWrap.create(WARArchive.class);
		deployment.addPackage("org.jboss.integration.fis2demo.account.route");
		deployment.staticContent();

		swarm.deploy(deployment);
	}
}