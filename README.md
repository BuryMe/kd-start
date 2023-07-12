# kd-start
公司业务需要对接了金蝶云星空，对接过程中发现其官方提供的java版本SDK实在是版本老旧，不兼容现在的主流的SpringBoot体系，随即便看了下源码，再结合我司实际的业务需求，
封装了一套自己的SDK。
核心目的是为了方便对接使用，其次也借这个机会，练练手，实践了下Spring动态注册机制，自定义start组件，Sring SPI等。

功能接口在com.cc.kingdeestart.core.IRequestClient，包含部分功能接口，也可以根据实际要求添加。

推荐使用方式，clone项目，编译，将jar丢到公司私服活着本地仓库，添加maven依赖，在实际调用时，注入IRequestClient即可。





