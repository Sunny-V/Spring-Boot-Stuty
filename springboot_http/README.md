# SpringBoot接口Http协议开发

<a name="98URf"></a>
### ![](https://cdn.nlark.com/yuque/0/2019/jpeg/408886/1565887863724-106a61d9-bdb4-4141-b470-34dca9604c9b.jpeg#align=left&display=inline&height=366&originHeight=366&originWidth=500&size=0&status=done&width=500)
<a name="YfVtV"></a>
### 主要注解
1.@RestController and @RequestMapping是SpringMvc的注解，并不是SpringBoot特有的<br />2.@RestControler =  @Controller+ResponseBody<br />3.SpringBootApplication = @Configuration+@EnableAutoConfiguration+@ComponentScan

<a name="UAGFO"></a>
### 测试请求返回数据

```java
//测试http协议的get请求
@RestController
public class GetController {

    private Map<String,Object> params = new HashMap<>();

    /**
     * 功能描述：测试restful协议，从路径中获取字段
     * @param name
     * @param age
     * @return
     */
    @RequestMapping(path = "/{name}/{age_v}", method = RequestMethod.GET)
    public Object findUser(@PathVariable String name, @PathVariable("age_v") String age){
        params.clear();

        params.put("name", name);
        params.put("age", age);

        return params;

    }

}
```

<a name="WWcQJ"></a>
### 利用接口测试工具postman测试

![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565874084420-f7e4b8ac-1dc9-4161-9658-a3ee4cf56a0b.png#align=left&display=inline&height=610&name=image.png&originHeight=762&originWidth=1204&size=67717&status=done&width=963.2)<br />**模拟请求测试返回数据成功** 

```java
/**
	 * 功能描述：测试GetMapping
	 * @param from
	 * @param size
	 * @return
	 */
	@GetMapping(value="/v1/page_user1")
	public Object pageUser(int  from, int size ){
		params.clear();
		params.put("from", from);
		params.put("size", size);
		
		return params;
		
	}
```


<a name="IfCLA"></a>
### 使用@GetMapping或PostMapping限定提交的方式
![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565874640415-6a26581e-fe17-4964-935a-99fc997860f0.png#align=left&display=inline&height=528&name=image.png&originHeight=660&originWidth=1200&size=60497&status=done&width=960)<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565874735207-4a93619c-85c9-4118-a7a5-1f16df9aa6d3.png#align=left&display=inline&height=218&name=image.png&originHeight=273&originWidth=719&size=21542&status=done&width=575.2)<br />**限定了get提交方式就不允许post的提交方式了**<br />**<br />

<a name="iec4J"></a>
### 使用@RequestParam注解，里面提供了三个属性，defaultValue，name和required，分别表示默认值，别名，和是否必须

```java
/**
 * 功能描述：默认值，是否必须的参数
 * @param from
 * @param size
 * @return
 */
	@GetMapping(value="/v1/page_user2")
	public Object pageUserV2(@RequestParam(defaultValue="0",name="page") int  from, int size ){
 
 params.clear();
 params.put("from", from);
 params.put("size", size);
 
 return params;
 
	}
```
**<br />**![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565875442210-f5dca49e-26db-47b2-9346-647975b33f5a.png#align=left&display=inline&height=126&name=image.png&originHeight=158&originWidth=562&size=5281&status=done&width=449.6)**<br />**
<a name="z0IU4"></a>
### 创建User类，生成字段属性，通过请求体映射实体类，传输数据
```java
public class User {

	private int age;
	
	@JsonIgnore
	private String pwd;
	
	@JsonProperty("account")
	@JsonInclude(Include.NON_NULL)
	private String phone;
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User() {
		super();
	}

	public User(int age, String pwd, String phone, Date createTime) {
		super();
		this.age = age;
		this.pwd = pwd;
		this.createTime = createTime;
	}
}



/**
     * 功能描述：bean对象传参
     * 注意：1、注意需要指定http头为 content-type为application/json
     * 		2、使用body传输数据
     * @param user
     * @return
     */
    @RequestMapping("/v1/save_user")
    public Object saveUser(@RequestBody User user){
        params.clear();
        params.put("user", user);
        return params;
    }
```


<a name="y3HqC"></a>
### 使用@RequestHeader获取请求头信息

```java
/**
     * 功能描述：测试获取http头信息
     * @param accessToken
     * @param id
     * @return
     */
    @GetMapping("/v1/get_header")
    public Object getHeader(@RequestHeader("access_token") String accessToken, String id){
        params.clear();
        params.put("access_token", accessToken);
        params.put("id", id);
        return params;
    }
```

<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565879531335-f4873889-beaf-4c4d-b68c-f324d70ab95c.png#align=left&display=inline&height=412&name=image.png&originHeight=515&originWidth=1083&size=52078&status=done&width=866.4)<br />

<a name="WlRV2"></a>
### 测试通过HttpSeRequestrvletRequest对象获取请求参数
```java
 /**
     * 功能描述：测试通过HttpSeRequestrvletRequest对象获取请求参数
     * @param request
     * @return
     */
    @GetMapping("/v1/test_request")
    public Object testRequest(HttpServletRequest request){
        params.clear();
        String id = request.getParameter("id");
        params.put("id", id);
        return params;
    }
```
                           <br />         
<a name="Dld7Z"></a>
### 其他几种常用的提交方式

```java
//测试http协议的post,del,put请求
@RestController
public class OtherHttpController {

	private Map<String,Object> params = new HashMap<>();
	
	
	/**
	 * 功能描述：测试PostMapping
	 * @param accessToken
	 * @param id
	 * @return
	 */
	@PostMapping("/v1/login")
	public Object login(String id, String pwd){
		params.clear();
		params.put("id", id);
		params.put("pwd", pwd);
		return params;	
	}
	
	
	@PutMapping("/v1/put")
	public Object put(String id){
		params.clear();
		params.put("id", id);
		return params;	
	}
	

	@DeleteMapping("/v1/del")
	public Object del(String id){
		params.clear();
		params.put("id", id);
		return params;	
	}
	
}

```

<a name="EU4YI"></a>
### 常用json框架介绍和Jackson返回结果处理
1.常用框架 阿里 fastjson,谷歌gson等<br /> JavaBean序列化为Json，性能：Jackson > FastJson > Gson > Json-lib 同个结构<br /> Jackson、FastJson、Gson类库各有优点，各有自己的专长<br /> 空间换时间，时间换空间<br />	2.jackson处理相关自动<br /> 指定字段不返回：@JsonIgnore<br /> 指定日期格式：@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")<br /> 空字段不返回：@JsonInclude(Include.NON_NUll)<br /> 指定别名：@JsonProperty

```java
//使用几种json处理注解修饰属性 
    @JsonIgnore
	private String pwd;
	
	@JsonProperty("account")
	@JsonInclude(Include.NON_NULL)
	private String phone;
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")
	private Date createTime;

//重载的带参构造方法
public User(int age, String pwd, String phone, Date createTime) {
		super();
		this.age = age;
		this.pwd = pwd;
		this.createTime = createTime;
	}


public class JsonConvertController {
    @GetMapping("/testjson")
    public Object testjson(){

        return new User(111, "abc123", "10001000", new Date());
    }
}
```

![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565882673507-8793f9dc-980c-41b2-87d8-536bb253aae7.png#align=left&display=inline&height=82&name=image.png&originHeight=103&originWidth=468&size=5586&status=done&width=374.4)<br />**测试结果**<br />**<br />**