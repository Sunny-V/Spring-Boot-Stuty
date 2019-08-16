# SpringBoot实现文件上传

<a name="1"></a>
## MultipartFile介绍
- MultipartFile是一个接口并继承了InputStreamSource接口
- MockMultipartFile、CommonsMultipartFile实现MultipartFile接口
<a name="2"></a>
## MockMultipartFile方法介绍

- getOriginalFilename()在客户端的文件系统中返回原始文件名
- getName()返回多部分表单中参数的名称。
- getInputStream()返回一个InputStream以从中读取文件的内容。通过此方法就可以获取到流

<a name="n2qbk"></a>
## springboot文件上传 MultipartFile file，源自SpringMVC
     <br />注意点：如果想要直接访问html页面，则需要把html放在springboot默认加载的文件夹下面<br />MultipartFile 对象的transferTo方法，用于文件保存（效率和操作比原先用FileOutStream方便和高效）

1.准备实体，保存上传成功返回数据

```java
public class JsonData implements Serializable {
	private static final long serialVersionUID = 1L;

	//状态码,0表示成功，-1表示失败
	private int code;
	
	//结果
	private Object data;

	//错误描述
	private String msg;
	
	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public JsonData(int code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}

	public JsonData(int code, String msg,Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}	
}
```

2.新建一个Controller和一个html页面，来实现上传文件的模拟操作

```java
/**
 * 功能描述：文件测试
 * @author lihao
 * @create 2019-08-16 22:22
 */
public class FileUploadController {

    //图片存放路径
    private static final String filePath = "/Users/jack/Desktop/person/springboot/xdclass_springboot/src/main/resources/static/images/";


    @RequestMapping(value = "upload")
    @ResponseBody
    public JsonData upload(@RequestParam("head_img") MultipartFile file, HttpServletRequest request) {

        //file.isEmpty(); 判断图片是否为空
        //file.getSize(); 图片大小进行判断

        String name = request.getParameter("name");
        System.out.println("用户名："+name);

        // 获取文件名
        String fileName = file.getOriginalFilename();
        System.out.println("上传的文件名为：" + fileName);

        // 获取文件的后缀名,比如图片的jpeg,png
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);

        // 文件上传后的路径
        fileName = UUID.randomUUID() + suffixName;
        System.out.println("转换后的名称:"+fileName);

        File dest = new File(filePath + fileName);

        try {
            file.transferTo(dest);

            return new JsonData(0, fileName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new JsonData(-1, "fail to save ", null);
    }

}

```

<a name="Fo3Fp"></a>
## 接下来测试功能
1.打开上传图片的页面，选择图片上传

![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565987480254-11fee65f-cafe-4c9c-956f-2a3509976941.png#align=left&display=inline&height=159&name=image.png&originHeight=199&originWidth=822&size=22714&status=done&width=657.6)<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565987547270-986ae393-fb89-4c82-ba54-923515c98791.png#align=left&display=inline&height=152&name=image.png&originHeight=190&originWidth=653&size=15580&status=done&width=522.4)<br />
<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/408886/1565987586200-a4bd2aba-ebb9-4deb-ab90-d9530bbf7194.png#align=left&display=inline&height=221&name=image.png&originHeight=276&originWidth=474&size=25194&status=done&width=379.2)

注意：一般开发的话路径是需要写在配置文件里的。而且一般的网站也都不是这样放置图片的，有专门的图片ftp服务器或者为了加载速度而利用cdn来做图片内容分发。

```java
web.upload-path=D:/projects/springboot/springboot_fileupload
spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/,classpath:/test/,file:${web.upload-path} 
```

在配置类里面加入下面的类限制文件上传的大小限制
```java
@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//单个文件最大  
		factory.setMaxFileSize("10240KB"); //KB,MB  
		/// 设置总上传数据总大小  
		factory.setMaxRequestSize("1024000KB");
		return factory.createMultipartConfig();
	}
```

通常生产环境中的项目是以打成jar包的方式运行，打包成jar包，需要增加maven依赖

```java
<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			</plugins>
		</build>
```

注意：如果没加相关依赖，执行maven打包，运行后会报错:no main manifest attribute, in XXX.jar


