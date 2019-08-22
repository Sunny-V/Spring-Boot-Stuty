Spring Security

<a name="SK74z"></a>
## 为什么需要权限管理
- 安全性：误操作，人为破坏，数据泄露
- 数据隔离：不同权限能看到操作不同的数据
- 明确职责：运营，客服不同角色，leader和dev等不同级别
<a name="GU1Vf"></a>
## 权限管理核心

- 用户-权限：人员少，功能固定，或者特别简单的系统

**例如Mysql下可以对某个用户服务器权限和某一个数据库某一张表的权限授予操作**

![image.png](https://i.loli.net/2019/08/22/b8Gj1LFJNQyEiYt.png)
![image.png](https://i.loli.net/2019/08/22/dwQU94ZmtEYfupM.png)
- RBAC模型(Role-Based-Access Control)：用户-角色-权限，都适用

<a name="eTeV8"></a>
## 理想中的权限管理

- 能实现角色级权限：RBAC
- 能实现功能级，数据级权限
- 简单，易操作，能够应用各种需求
<a name="nfFT9"></a>
## 相关权限界面

- 权限管理界面，角色管理界面，用户管理界面
- 角色和权限关系维护界面，用户和角色关系维护界面
<a name="Z0JHj"></a>
## 目前比较流行的开源权限管理项目
1.Spring Security<br />2.Apache Shiro

<a name="6RYbg"></a>
## Spring Security介绍
![image.png](https://i.loli.net/2019/08/22/AsCHOeEK6iIwxvn.png)
权限管理实际包括两部分，一是身份认证，二是权限控制。<br />常见的认证方式:<br />basic：账号和密码（Base64编码）明文传输<br />Digest ：基于Hash函数，避免密码明文传输，但传输过程中如被截获，则资源可被访问，通常服务端进一步增加时效性验证。<br />X.509：数字证书认证，传输加密，有效避免被截获。<br />LDAP：轻量级访问协议，通常用于企业内部。<br />Form：Form表单认证，Web系统最常用的方式，为增强安全性，通常追加动态验证码。

<a name="TpQSx"></a>
### Spring Security常用拦截器
![image.png](https://i.loli.net/2019/08/22/LcoqYe5lyVkK47a.png)![image.png](https://i.loli.net/2019/08/22/5NqSpwsAIm29MPa.png)<br />**权限控制分两段，首先进行身份认证，然后进行访问权限验证，如都通过，则允许访问资源。**<br /><br />**UserDetailsService是一个接口，仅有一个方法，返回类型为UserDetails**<br />
![image.png](https://i.loli.net/2019/08/22/RL43aACHgOjdrx7.png)<br /><br />**Authentication对象才是SpringSecurity框架进行权限控制的安全对象，来源于登录表单或cookie，有个是否认证通过的属性isAuthenticated，与安全源UserDetails匹配后，将相关信息（如权限集合）拷贝到Authentication对象。**<br />
![image.png](https://i.loli.net/2019/08/22/uvkLacKd27grMxX.png)
<a name="avih3"></a>
## Spring Security Case
在pom.xml里面引入Spring Security的依赖
```java
<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
</dependency>
```
创建一个SpringSecSpurityConfig的配置类,继承WebSecurityConfigurerAdapter
```java
@Configuration
@EnableWebSecurity
public class SpringSecSpurityConfig extends WebSecurityConfigurerAdapter {}
```
WebSecurityConfigurerAdapter提供了一些拦截类型的方法

```java
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("111111").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("afu").password("111111").roles("AFU");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //主路径放行
                .antMatchers("/").permitAll()
                //其他请求全部经过验证
                .anyRequest().authenticated()
                .and()
                //注销允许访问
                .logout().permitAll()
                .and()
                //支持表单登录
                .formLogin();
        //关闭csrf认证
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略不被拦截的文件
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }
}
```

配置好角色的权限，写一个方法来进行访问，注意：限定某一个角色的方法必须在方法上@PreAuthorize("hasRole('ROLE_xxx')")，<br />在Controller上必须加上@EnableGlobalMethodSecurity(prePostEnabled = true)
```java
@SpringBootApplication
@Controller
@ResponseBody
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "hello spring boot";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("roleAuth")
	public String role() {
		return "admin auth";
	}

}
```
测试报错：java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null" <br />原因：这是因为Spring boot 2.0.3引用的security 依赖是 spring security 5.X版本，此版本需要提供一个PasswordEncorder的实例，在使用认证的时候用MyPasswordEncoder去校验密码,否则后台汇报错误。

```java
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}
```
用user角色登录,访问roleAuth被拒绝，因为权限已经限定了admin<br />![image.png](https://i.loli.net/2019/08/22/av6CONFERKVJ8IM.png)<br />![image.png](https://i.loli.net/2019/08/22/Shtz1UbqpJTfmuO.png)<br />用admin就能够登录成功了<br />![image.png](https://i.loli.net/2019/08/22/DEzotOGh9QFUgik.png)

<a name="ndLMv"></a>
## Spring Security的优缺点

- 优点1.提供了一套安全框架，而且这个框架是可用的

2.提供了很多用户认证的功能，实现相关接口即可，节约大量开发工作<br />3.基于Spring，易于集成到Spring项目中，且封装了许多方法

- 缺点1.配置文件多，角色被"编码"到配置文件和源文件中，RBAC不明显

2.对于系统中用户，角色，权限之间的关系，没有可操作的界面<br />3.大数据量情况下，几乎不可用

