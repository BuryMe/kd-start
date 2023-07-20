# kd-start
公司业务需要对接了金蝶云星空，对接过程中发现其官方提供的java版本SDK实在是版本老旧，不兼容现在的主流的java架构体系，便看了下源码，再结合我司实际的业务需求，
封装了一套自己的SDK。

功能接口在com.cc.kingdeestart.core.IRequestClient，包含部分功能接口，也可以根据实际要求添加。

项目主依赖

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/>
    </parent>


使用方式：

1.clone项目，编译，将jar丢到公司私服活着本地仓库，添加maven依赖

2.在业务主项目的配置文件中，添加prefix=kingdee的金蝶服务配置信息，具体看com.cc.kingdeestart.core.KingDeeProperty

3.用@Resource注解注入IRequestClient。







