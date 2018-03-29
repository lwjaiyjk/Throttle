# Throttle
限流幂等框架
设计和使用文档：https://www.jianshu.com/p/224da86fa9ff
限流和幂等框架设计和使用说明
1.	背景介绍
1.1	限流
在早期的计算机领域，限流技术(time limiting)被用作控制网络接口收发通信数据的速率。 可以用来优化性能，减少延迟和提高带宽等。 现在在互联网领域，也借鉴了这个概念， 用来为服务控制请求的速率， 如果双十一的限流， 12306的抢票等。限流就是限制流量，就像你宽带包了1个G的流量，用完了就没了。限流的目的是通过对并发访问/请求进行限速或者一个时间窗口内的的请求进行限速来保护系统，一旦达到限制速率则可以拒绝服务（定向到错误页或告知资源没有了）、排队或等待（比如秒杀、评论、下单）、降级（返回托底数据或默认数据，如商品详情页库存默认有货），通过限流，我们可以很好地控制系统的qps，从而达到保护系统的目的。常见的限流算法有令牌桶、漏桶，计数器也可以进行粗暴限流实现。
1.2	幂等
在编程中.一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同。幂等函数，或幂等方法，是指可以使用相同参数重复执行，并能获得相同结果的函数。这些函数不会影响系统状态，也不用担心重复执行会对系统造成改变。例如“setTrue()”函数就是一个幂等函数,无论多次执行，其结果都是一样的.更复杂的操作幂等保证是利用唯一交易号(流水号)实现。
                   f(f(x)) = f(x)
 x被函数f作用一次和作用无限次的结果是一样的。幂等性应用在软件系统中，我把它简单定义为：某个函数或者某个接口使用相同参数调用一次或者无限次，其造成的后果是一样的，在实际应用中一般针对于接口进行幂等性设计。举个栗子，在系统中，调用方A调用系统B的接口进行用户的扣费操作时，由于网络不稳定，A重试了N次该请求，那么不管B是否接收到多少次请求，都应该保证只会扣除该用户一次费用。
2.	架构说明
限流和幂等框架的设计主要是通过注解和拦截器aop的方式来实现，框架本身实现分为两个主要部分：限流规则解析和请求对应的限流规则使用，对应的如下图所示：
 

下面先介绍框架的限流规则解析部分。
2.1	限流规则解析
在详细介绍限流规则解析之前，先整体看一下这部分对于类图结构，如下图所示：
 
	从上图可以看出限流规则解析整体分成3块：注解领域模型，注解解析器和限流规则对应的领域模型，并且这3块的关系从上图也能清晰的看出，通过注解解析器从对应的注解中解析出来对应的限流规则，而且注解领域模型和限流规则对应的领域模型实际上一一对象的，下面对这3块进行详细介绍【注意限流仅仅限制对应的Public方法】：
（1）	注解领域模型：分别是限流策略，限流组规则，限流规则和限流条件，失效限流策略，失效限流规则，异常自定义处理方法别名注解，如下图所示：
 

（2）	注解到规则转换的解析器：对于注解领域模型中的每一个注解类型定义了一个解析器，用于将对应的注解领域转换为对应的规则领域模型。
 

（3）	规则领域模型：和（1）注解领域模型基本是一样的。
 

2.2	限流规则使用
在详解介绍限流规则的使用前，先来看一下请求处理对应的类图：
 

	首先拦截器会拦截所有标记有@EnableThrottlingPolicy注解的类的方法，在aroundAdvice方法中根据方法进行限流和幂等拦截，如果访问到达了接口限流的上限，则异常，需要调用者自己进行异常处理。
2.3注解含义使用说明
（1）限流策略注解：
属性	类型	说明	默认值
rules	ThrottlingRule[]	规则列表，这个规则列表会按照限流规则对应的规则类型对应的权重进行降序排序（之所以进行排序是因为保证幂等类型的规则是最后一个进行处理的）	无
policyPersistBeanName	String	策略持久化对应的Spring容器中bean名称	guvvaRateLimitThrottlePersistService（使用guvva对应的限流策略进行限流）
excepHandleMethodAlias	String	异常个性化处理方法别名，在策略上设置默认这个类对应的规则都继承这个属性值，但是这个值会被方法上组规则覆盖，方法上组规则又会被对应的规则上的覆盖	“”（就是不指定异常处理方法别名）

限流策略注解包含了限流规则注解列表，使用这个限流策略注解的类中的方法要满足限流策略对应的所有的限流规则列表中定义的限制，注意限流策略注解具有继承性，也即是在一个类上增加这个注解，则这个类及其子类都会继承对应的限流策略，当然子类也可以自己进行重写。
（2）限流组规则注解：
属性	类型	说明	默认值
rules	ThrottlingRule[]	限流规则列表	
excepHandleMethodAlias	String	异常个性化处理方法别名	“”（就是不指定异常处理方法别名）
限流组规则可以定义在方法上，增加一个对应限流策略的组规则，这样这个方法对应的组规则就是类型上定义的限流策略中的规则列表和方法自己定义的规则列表之和（相同名称的组规则以方法上的为准），要注意一点的是如果两个规则对应的名称一样，则后面的规则会覆盖前面的规则。 
（3）限流规则注解：
属性	类型	说明	默认值
conditions	ThrottlingCondition[]	限流规则对应的限流条件列表	
ruleName	String	限流规名称	默认值为空
ruleAcquirerBeanName	String	规则对应的entryValue值得获得器，比如按照IP进行过滤等，则需要自己实现对应的ThrottleValueAcquirer接口	默认值为空表示限流的是接口
ruleWhiteNames	String[]	对应entryvalue的白名单列表，如果在白名单内则不适用限流规则	默认值为空
rulePersistBeanName	String	规则对应的持久化策略，会覆盖策略上对应的持久化策略	默认值为空
ruleType	ThrottleRuleTypeEnum	规则类型有两种，限流和幂等	限流
excepHandleMethodAlias	String	异常个性化处理方法别名	“”（就是不指定异常处理方法别名）
（4）限流条件注解：
属性	类型	说明	默认值
conditionType	ThrottleConditionTypeEnum	限流条件类型：对应的限流的时间单位	ThrottleConditionTypeEnum.SECOND
maxAccessTimes	Long	单位限流时间内的最大访问次数	
cndAcquirerBeanName	String	限流条件对应的entryValue值得获取器	
cndWhiteNames	String[]	限流条件对应的白名单	默认值为空
（5）关闭限流策略注解：
@DisableThrottlingPolicy注解是一个标记注解，可以标记在类型和方法中，标记在类型上则这个类对应的方法都不会应用限流规则，应用在方法上则对应的该方法不会应用限流规则。
（6）关闭限流规则注解：
属性	类型	说明	默认值
ruleNames	String	需要关闭限流规则的名称	

（7）限流异常处理注解
属性	类型	说明	默认值
handleMethodAliasName	String	定义异常处理方法对应的别名	

	@ThrottlingExceptionRule 限流异常处理规则，其有一个属性是handleMethodAliasName，这个属性是对应的异常处理函数对应的别名，这个别名主要是为了在对应的策略注解，组规则注解以及对应的规则注解中使用，其他使用如下图所示：
 
注意这个对应的异常处理函数的参数类型和顺序一定是（ProceedingJoinPoint,ThrottlePolicy,ThrottleException），且对应的方法访问属性是Public。如果对应的限流方法上规则没有增加对应的限流自定义异常处理，则按照原来默认的处理，即是限流条件不通过则抛异常；幂等则直接返回原来的结果。
使用的时候，只要在注解@EnableThrottlingPolicy或者@ThrottlingGroupRule或者@ThrottlingRule对应的属性excepHandleMethodAlias设置对应的@ThrottlingExceptionRule中定义的别名即可。
3.	用法说明
3.1框架引入Pom依赖
<dependency>
<groupId>com.ctim.framework</groupId>
<artifactId>throttle</artifactId>
<version>0.0.3-SNAPSHOT</version>
</dependency>
3.2 增加spring对应的组件扫描包路径
增加spring对应的组件扫描包路径 “com.ctim.framework.throttle”，将对应的限流框架引入。例如使用@CompoentScan(“com.ctim.framework.throttle”)
3.3 自定义实现接口ThrottleValueAcquirer
自定义类实现接口ThrottleValueAcquirer用于获得对应的限流条件对应的白名单中的值，其中接口ThrottleValueAcquirer对应的方法acquire(Object... objects)中的参数和要限流的方法的参数完成一样，下面是一个自己实现接口ThrottleValueAcquirer的例子：
 
3.4 自定义实现接口ThrottlePersistenceService
接口ThrottlePersistenceService定义了一些比较和更新限流策略对应的限流值方法，具体说明如下：
方法	说明
public ThrottlePersistControlResult selectByKey(ThrottlePersistQueryRequest throttlePersistQueryRequest)	根据限流对应的查询请求参数进行查询，如果存在则说明这个请求已经已经访问过，并同时判断对应返回结果的successFlag标记，如果successFlag为false，则抛异常说明限流规则没有通过，否则则更新对应的限流规则中的当前访问值。
public boolean save(ThrottlePersistQueryRequest throttlePersistQueryRequest,ThrottlePersistControlResult throttlePersistControlResult);	保存对应的限流策略到指定的存储中，这个方法会在第一次访问的时候被调用，因为根据selectByKey进行查询返回为null的规则类型为限流的时候会被调用。
public Object saveWithCallbackForIdempoent(
			List<ThrottlePersistQueryRequest> throttlePersistQueryRequests,
			ProceedingJoinPoint pjp);	这个方法是在这个方法第一次被访问的时候且规则类型为幂等的情况下会被调用，因为是幂等所以要开启事物来保证，所以这部分需要使用者自己实现逻辑。
public boolean update(ThrottlePersistQueryRequest throttlePersistQueryRequest,
		ThrottlePersistControlResult throttlePersistControlResult)	更新对应的限流规则对应的当前已经访问的值（不断更新对应的访问策略中的当前已经访问的值），这个方法在使用selectByKey方法返回对象对应的successFlag=true的情况下被调用
public ThrottlePersistControlResult deleteByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest);	这个方法目前框架没有调用，主要是考虑drm推送的时候使用，暂时没有实现
3.5 在类型和方法上增加注解
在需要进行限流或幂等的接口上增加相应的开启限流策略的注解，这样这个类型及其子类型都已经开启了限流规则，如下图所示：
（1）在类型上定义的限流策略：
在类型定义了一个限流策略，这个限流策略包含限流规则列表，这个规则列表中的规则使用的规则entryValue获取器对应的beanName为myThrottleValueAcquirer，持久化服务对象使用的组规则的默认值guvva的ratelimiter来实现的，这个规则包含了两个限流条件：一个是每秒访问5次，一个是每分钟访问200次的限流条件，且这个两个限流条件对应的白名单中值得获取器使用的都是myThrottleValueAcquirer来获取的（这个要和限流条件中的白名单中的值对应的）。
 

（2）在类型上定义限流策略，在方法上定义对应的组规则
 

这种定义的方式和（1）实现同样的功能。
（3）在类型上定义幂等规则策略
 

幂等规则和限流规则唯一差异就是规则类型不一样。
（4）类型和方法定义不同规则名称的规则
 

	类型和方法都定义规则但是对应的规则名称不一样，则方法test就对应了两个规则。
（5）类型和方法上定义相同名称规则
 
	类型和方法上对应的规则名称相同，则方法上的规则会覆盖类型上定义的规则。
（6）类型上同时定义限流和幂等组规则
 
在类型上定义了一个限流规则和幂等规则。
（7）类型上使用DisableThrottlingPolicy注解
注解@DisableThrottlingPolicy不具有继承性，父类关闭限流，子类不会关闭限流策略。
 

在类型上加入关闭限流策略的注解，则这个类中的所有方法都将关闭限流策略。
（8）方法上使用DisableThrottlingPolicy注解
在方法上使用关闭限流策略，则仅仅只对该方法关闭限流策略，类型中的其他方法不受影响。
 

方法test上加了关闭限流策略的注解，则方法test将不受限流限制，而方法test1仍然受限流策略的限制。
（9）失效限流的特定规则
 

失效限流规则test1，其他限流规则test2的仍然有效
（10）根据特定规则进行限流
根据特定规则，比如根据访问ip，userId等进行限流和防刷，则需要自己定义对应的规则注解中的ruleAcquirerBeanName，这个bean需要自己实现，按照什么规则进行限流和防刷。
（11）自定义对应限流异常处理流程
先定义对应的异常处理方法的别名，如下图所示：
 
在限流策略中指定对应的限流异常处理方法别名，如下图所示：
 
	或者在限流组规则中指定对应的限流异常处理方法别名，如下图所示：
 
或者在限流规则中指定对应的限流异常处理方法别名，如下图所示：
 
注意自定义异常处理方法指定的覆盖策略：规则》组规则》策略
