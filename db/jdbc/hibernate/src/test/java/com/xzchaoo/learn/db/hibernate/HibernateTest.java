//package com.xzchaoo.learn.db.hibernate;
//
//import com.xzchaoo.learn.db.hibernate.entity.Address;
//import com.xzchaoo.learn.db.hibernate.entity.Book;
//import com.xzchaoo.learn.db.hibernate.entity.FooRecord;
//import com.xzchaoo.learn.db.hibernate.entity.Gender;
//import com.xzchaoo.learn.db.hibernate.entity.Phone;
//import com.xzchaoo.learn.db.hibernate.entity.PhoneType;
//import com.xzchaoo.learn.db.hibernate.entity.User;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.Metadata;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.AvailableSettings;
//import org.hibernate.tool.schema.Action;
//import org.junit.Test;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.JoinType;
//import javax.persistence.criteria.Root;
//
///**
// * @author zcxu
// * @date 2018/1/11
// */
//public class HibernateTest {
//	@Test
//	public void test1() {
//		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//			.applySetting(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver")
//			.applySetting(AvailableSettings.URL, "jdbc:mysql://localhost:3306/test")
//			.applySetting(AvailableSettings.USER, "root")
//			.applySetting(AvailableSettings.PASS, "70862045")
//			.applySetting(AvailableSettings.SHOW_SQL, true)
//			.applySetting(AvailableSettings.FORMAT_SQL, true)
//			.applySetting(AvailableSettings.HBM2DDL_AUTO, Action.UPDATE)
//			.build();
//		Metadata metadata = new MetadataSources(registry)
//			.addAnnotatedClass(User.class)
//			.addAnnotatedClass(Phone.class)
//			.addAnnotatedClass(Book.class)
//			.getMetadataBuilder()
//			.build();
//
//		SessionFactory sf = metadata.getSessionFactoryBuilder()
//			.build();
//
//		sf.close();
//	}
//
//	@Test
//	public void test2() {
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("CRM", new HashMap());
//
//		EntityManager em = emf.createEntityManager();
//
//		List<User> users = em.createQuery("from User as u", User.class).getResultList();
//		User u = new User();
//		u.setGender(Gender.MALE);
//		u.setUsername("xzc");
//		u.setPassword("70862045");
//		u.setRegisterTime(LocalDateTime.now());
//		u.setLastLoginTime(LocalDateTime.now());
//		Address address = new Address();
//		address.setProvince("ShangHai");
//		address.setCity("MinHang");
//		u.setAddress(address);
//		List<Phone> phones = new ArrayList<>();
//		Phone p1 = new Phone();
//		p1.setType(PhoneType.HOME);
//		p1.setPhoneNumber("123");
//		phones.add(p1);
//		u.setPhones(phones);
//		em.getTransaction().begin();
//		em.persist(u);
//		em.getTransaction().commit();
//		System.out.println(users);
//		em.close();
//
//		em = emf.createEntityManager();
//		em.getTransaction().begin();
//		User user = em.find(User.class, 1L);
//		Book b = new Book();
//		b.setTitle("hah");
//		b.setAuthor(user);
//		em.persist(b);
//		em.getTransaction().commit();
//		Long bid = b.getId();
////		System.out.println(user);
////		System.out.println(user.getPhones().getClass().getName());
////		System.out.println(user.getPhones());
//
//		System.out.println(em.find(FooRecord.class, 1L));
//
//		b = em.find(Book.class, bid);
//		System.out.println("book is");
//		System.out.println(b);
//		b.setTitle("hah");
//		em.createQuery("select fr from FooRecord as fr", FooRecord.class).getResultList();
//		em.close();
//	}
//
//	public void test_criteriaBuilder() {
//		EntityManager em = null;
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<User> q = cb.createQuery(User.class);
//		Root<User> root = q.from(User.class);
//		root.fetch("projects", JoinType.LEFT);
//		q.select(root).where(
//			cb.and(
//				cb.equal(root.get("username"), "username"),
//				cb.equal(root.get("password"), "haha")
//			)
//		);
//	}
//}
