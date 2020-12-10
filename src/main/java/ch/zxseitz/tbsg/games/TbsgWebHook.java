package ch.zxseitz.tbsg.games;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TbsgWebHook {
    enum Method {
        POST,
        GET,
        PUT,
        DELETE
    }

    String url();
    Method method();
}
