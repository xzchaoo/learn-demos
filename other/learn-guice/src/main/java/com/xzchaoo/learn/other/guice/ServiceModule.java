package com.xzchaoo.learn.other.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.ProvisionListener;

public class ServiceModule extends AbstractModule {
	@Override
	protected void configure() {
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.init();
		this.bindListener(new AbstractMatcher<Binding<?>>() {
			@Override
			public boolean matches(Binding<?> binding) {
				System.out.println(binding.getKey());
				return binding.getKey().getTypeLiteral().getType() == UserDaoImpl.class;
//				System.out.println("matches " + binding);
//				return true;
			}
		}, new ProvisionListener() {
			@Override
			public <T> void onProvision(ProvisionInvocation<T> provision) {
				System.out.println("onProvision");
			}
		});
		bind(UserDao.class).toInstance(userDao);
		//bind(UserService.class);
	}
}
