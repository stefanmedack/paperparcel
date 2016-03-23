package nz.bradcampbell.paperparcel.internal.properties;

import static nz.bradcampbell.paperparcel.internal.utils.PropertyUtils.getRawTypeName;
import static nz.bradcampbell.paperparcel.internal.utils.PropertyUtils.literal;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import nz.bradcampbell.paperparcel.internal.Property;
import org.jetbrains.annotations.Nullable;

public class ParcelableProperty extends Property {
  public ParcelableProperty(boolean isNullable, TypeName typeName, boolean isInterface, String name,
                            @Nullable String accessorMethodName) {

    super(isNullable, typeName, isInterface, name, accessorMethodName);
  }

  @Override
  protected CodeBlock readFromParcelInner(CodeBlock.Builder block, ParameterSpec in, @Nullable FieldSpec classLoader) {
    TypeName rawTypeName = getRawTypeName(getTypeName());
    return literal("$T.CREATOR.createFromParcel($N)", rawTypeName, in);
  }

  @Override protected void writeToParcelInner(CodeBlock.Builder block, ParameterSpec dest, CodeBlock sourceLiteral) {
    block.addStatement("$L.writeToParcel($N, 0)", sourceLiteral, dest);
  }
}
