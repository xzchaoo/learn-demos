package com.xzchaoo.learn.fastxmljackson.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by Administrator on 2017/6/15.
 */
//@JsonSerialize(using = SecurityUserSerializer.class)
@JsonDeserialize(using = SecurityUserDeserializer.class)
public abstract class SecurityUserMixin2 {

}
