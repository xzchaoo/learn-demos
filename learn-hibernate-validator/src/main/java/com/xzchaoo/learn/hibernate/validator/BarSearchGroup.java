package com.xzchaoo.learn.hibernate.validator;

import javax.validation.groups.Default;

//bar继承了2个组 因此这两个组都会生效
public interface BarSearchGroup extends Default, FooSearchGroup {
}
