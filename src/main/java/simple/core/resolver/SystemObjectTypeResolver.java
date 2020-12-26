package simple.core.resolver;


import simple.core.description.TypeDescription;
import simple.core.description.UnAvailableTypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

public class SystemObjectTypeResolver extends ReferenceTypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && isSystem(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new UnAvailableTypeDescription();
    }

    private static boolean isSystem(ResolvedType type) {
        String id = type.asReferenceType().getId();
        return id.startsWith("java") || id.startsWith("sun");
    }
}
