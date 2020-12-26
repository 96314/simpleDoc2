package simple.core.resolver;

import simple.core.description.TypeDescription;
import com.github.javaparser.ast.type.Type;

public interface NameResolver {

    boolean accept(String id);

    TypeDescription resolve(Type type);

}
