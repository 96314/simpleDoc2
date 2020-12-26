package simple.core.resolver;


import simple.core.Apigcc;
import simple.core.description.ArrayTypeDescription;
import simple.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

public class ArrayTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return type.isArray();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new ArrayTypeDescription(Apigcc.getInstance().getTypeResolvers().resolve(type.asArrayType().getComponentType()));
    }

}
