package com.cc.kingdeestart.core;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KingDeeApiClient {

    String name();


}
