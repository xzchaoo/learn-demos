package com.xzchaoo.learn.db.hibernate.versiontest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.tool.schema.Action;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author zcxu
 * @date 2018/1/12
 */
public class VersionTest {

	private SessionFactory sf;

	@Before
	public void before() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.applySetting(AvailableSettings.DRIVER, "org.h2.Driver")
			.applySetting(AvailableSettings.URL, "jdbc:h2:mem:;")
			.applySetting(AvailableSettings.USER, "sa")
			.applySetting(AvailableSettings.PASS, "")
			.applySetting(AvailableSettings.SHOW_SQL, true)
			.applySetting(AvailableSettings.FORMAT_SQL, true)
			.applySetting(AvailableSettings.HBM2DDL_AUTO, Action.UPDATE)
			.build();

		Metadata metadata = new MetadataSources(registry)
			.addAnnotatedClass(VTUser.class)
			.getMetadataBuilder()
			.build();

		sf = metadata.getSessionFactoryBuilder()
			.build();
	}

	@After
	public void after() {
		sf.close();
	}

	@Test
	public void test() {
		Session s0 = sf.openSession();
		s0.getTransaction().begin();
		VTUser u = new VTUser();
		u.setUsername("a");
		u.setPassword("b");
		u.setLastLoginTime(LocalDateTime.now());
		u.setRegisterTime(LocalDateTime.now());
		s0.persist(u);
		s0.getTransaction().commit();
		s0.close();

		Session s1 = sf.openSession();
		//Session s2 = sf.openSession();
		s1.beginTransaction();
		//s2.beginTransaction();
		VTUser u1 = s1.find(VTUser.class, 1L);
		//VTUser u2 = s2.find(VTUser.class, 1L);
		u1.setPassword("c");
		//u2.setPassword("d");
		System.out.println(u1);
		//System.out.println(u2);
		s1.getTransaction().commit();
		//s2.getTransaction().commit();
		s1.close();
		//s2.close();
	}
}
