package simple.core.resolver;


import simple.core.Apigcc;
import com.github.javaparser.resolution.types.ResolvedType;

public abstract class ReferenceTypeResolver implements TypeResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType() &&
                !Apigcc.getInstance().getContext().hasCodeTypeDeclaration(type.asReferenceType().getId());
    }
}
