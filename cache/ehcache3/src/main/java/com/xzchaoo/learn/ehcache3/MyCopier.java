package com.xzchaoo.learn.ehcache3;

import org.ehcache.spi.copy.Copier;

public class MyCopier implements Copier<Long> {
	@Override
	public Long copyForRead(Long obj) {
		System.out.println("copyForRead");
		return obj;
	}

	@Override
	public Long copyForWrite(Long obj) {
		System.out.println("copyForWrite");
		return obj;
	}
}
