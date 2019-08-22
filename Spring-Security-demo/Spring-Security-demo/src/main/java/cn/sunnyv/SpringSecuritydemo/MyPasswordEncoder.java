package cn.sunnyv.SpringSecuritydemo;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 功能描述：
 *
 * @author lihao
 * @create 2019-08-22 11:31
 */
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
