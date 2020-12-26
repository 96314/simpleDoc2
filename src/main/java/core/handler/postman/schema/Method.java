package core.handler.postman.schema;

import core.http.HttpRequestMethod;
import core.markup.asciidoc.Color;
import core.util.loging.Logger;
import core.util.loging.LoggerFactory;

/**
 * 支持的http method
 */
public enum Method {


    GET,POST,PUT,DELETE,
    OPTIONS,PATCH,COPY,HEAD,LINK,UNLINK,PURGE,LOCK,UNLOCK,PROPFIND,VIEW;

    static Logger log = LoggerFactory.getLogger(Method.class);

    public static Method of(HttpRequestMethod method) {
        return valueOf(method.name());
    }

    public static Color getColor(String method){
        try {
            switch (valueOf(method)){
                case GET:
                    return Color.GREEN;
                case POST:
                    return Color.YELLOW;
                case PUT:
                    return Color.BLUE;
                case DELETE:
                    return Color.RED;
                default:
            }
        }catch (Exception e){
            log.debug("parse method error:{}",method);
        }
        return Color.BLACK;
    }

}
