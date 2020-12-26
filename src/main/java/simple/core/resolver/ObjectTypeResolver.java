package simple.core.resolver;

import simple.core.Apigcc;
import simple.core.description.ObjectTypeDescription;
import simple.core.description.TypeDescription;

import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import simple.core.common.helper.*;

/**
 * 普通类型解析
 */
public class ObjectTypeResolver implements TypeResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        ObjectTypeDescription typeDescription = new ObjectTypeDescription();

        ResolvedReferenceType referenceType = type.asReferenceType();
        typeDescription.setType(referenceType.getTypeDeclaration().getName());
        if (!ReferenceContext.getInstance().push(referenceType.describe())) {
            //类型解析缓冲池，防止循环引用
            return typeDescription;
        }

        //解析父类属性，并合并至当前
        for (ResolvedReferenceType directAncestor : referenceType.getDirectAncestors()) {
            TypeDescription ancestorDescription = Apigcc.getInstance().getTypeResolvers().resolve(directAncestor);
            if (ancestorDescription.isAvailable() && ancestorDescription.isObject()) {
                typeDescription.merge(ancestorDescription.asObject());
            }
        }

        //TODO fix use access method
        for (ResolvedFieldDeclaration declaredField : referenceType.getTypeDeclaration().getDeclaredFields()) {
            if (declaredField.isStatic()) {
                continue;
            }
            ResolvedType fieldType = FieldHelper.getActuallyType(referenceType, declaredField);

            TypeDescription fieldDescription = Apigcc.getInstance().getTypeResolvers().resolve(fieldType);
            fieldDescription.setKey(declaredField.getName());

            JsonPropertyHelper.getJsonName(declaredField).ifPresent(fieldDescription::setKey);
            CommentHelper.getComment(declaredField).ifPresent(fieldDescription::accept);
            fieldDescription.addConditions(ValidationHelper.getValidations(declaredField));
            FieldHelper.getInitializerValue(declaredField).ifPresent(fieldDescription::setDefaultValue);

            typeDescription.add(fieldDescription);
        }

        ReferenceContext.getInstance().remove(referenceType.describe());
        return typeDescription;
    }

}