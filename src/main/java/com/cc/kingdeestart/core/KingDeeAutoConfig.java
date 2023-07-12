package com.cc.kingdeestart.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author seven up
 * @date 2023年07月12日 2:37 PM
 */
@Configuration
@EnableConfigurationProperties(KingDeeProperty.class)
@ConditionalOnProperty(prefix = "kingdee", value = "enabled", matchIfMissing = true)
@Import({ApiClientRegister.class})
public class KingDeeAutoConfig {


}
