package com.xzchaoo.learn.db.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BIMapper {
	@Insert({"insert into bi(k1) values(#{k1})"})
	@Options(useGeneratedKeys = true)
	void insert(@Param("k1") String k1);

	@Select("select count(*) from bi")
	int count();
}
