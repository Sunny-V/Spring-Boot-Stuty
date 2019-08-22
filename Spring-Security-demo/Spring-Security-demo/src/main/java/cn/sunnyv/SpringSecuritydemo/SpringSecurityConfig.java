package cn.sunnyv.SpringSecuritydemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 功能描述：
 *
 * @author lihao
 * @create 2019-08-22 9:54
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //可以设置内存指定的登录的账号密码,指定角色
        //不加.passwordEncoder(new MyPasswordEncoder())
        //就不是以明文的方式进行匹配，会报错
        //.passwordEncoder(new MyPasswordEncoder())。
        //这样，页面提交时候，密码以明文的方式进行匹配。
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder()).withUser("admin").password("123456").roles("ADMIN");
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder()).withUser("xiaoli").password("111111").roles("USER");
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
