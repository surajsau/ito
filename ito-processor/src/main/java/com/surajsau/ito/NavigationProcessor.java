package com.surajsau.ito;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class NavigationProcessor extends AbstractProcessor {

    private static final String NAVIGATOR_CLASS = "Ito";

    private static final ClassName intentClass = ClassName.get("android.content", "Intent");
    private static final ClassName contextClass = ClassName.get("android.content", "Context");
    private static final ClassName bundleClass = ClassName.get("android.os", "Bundle");

    private static final String QUALIFIER_STRING = "java.lang.String";

    private Messager mMessager;
    private Filer mFiler;

    private Map<String, String> qualifiedNames;
    private Map<String, TypeElement> bundleQualifiedNames;
    private Map<String, List<Element>> params;

    private Types mTypeUtils;
    private Elements mElementUtils;

    private List<MethodSpec> methodSpecs;

    private String packageName;

    private boolean isPackageNameFound;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mTypeUtils = processingEnvironment.getTypeUtils();
        mElementUtils = processingEnvironment.getElementUtils();

        qualifiedNames = new HashMap<>();
        bundleQualifiedNames = new HashMap<>();
        params = new HashMap<>();

        methodSpecs = new ArrayList<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        try {
            for(Element element : roundEnvironment.getElementsAnnotatedWith(IntentVar.class)) {
                if(element.getKind() != ElementKind.FIELD) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "Can only be applied to Field variables");
                    return false;
                }

                if(!isPackageNameFound)
                    processPackageName(element);

                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                String parentName = enclosingElement.getSimpleName().toString();

                if(!params.containsKey(parentName)) {
                    params.put(parentName, new ArrayList<Element>());
                }

                params.get(parentName).add(element);

                qualifiedNames.put(parentName, enclosingElement.getQualifiedName().toString());
            }

            for(Element element : roundEnvironment.getElementsAnnotatedWith(BundleVar.class)) {
                if(element.getKind() != ElementKind.FIELD) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "Can only be applied to Field variables");
                    return false;
                }

                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                String parentName = enclosingElement.getSimpleName().toString();

                if(!params.containsKey(parentName)) {
                    params.put(parentName, new ArrayList<Element>());
                }

                params.get(parentName).add(element);

                bundleQualifiedNames.put(parentName, enclosingElement);
            }

            if(roundEnvironment.processingOver()) {

                generateBundleLauncherMethods();
                generateIntentLauncherMethods();

                generateNavigator();

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            return false;
        }
        return false;
    }

    private void processPackageName(Element element) {
        String elementPackage = mElementUtils.getPackageOf(element).getQualifiedName().toString();
        String[] parts = elementPackage.split("\\.");

        if(parts.length < 3) {

            packageName = elementPackage.substring(0, elementPackage.lastIndexOf("\\."));

        } else {

            packageName = parts[0] + "." + parts[1] + "." + parts[2];

        }

        isPackageNameFound = true;
    }

    private void generateBundleLauncherMethods() {
        for(String fragmentName: bundleQualifiedNames.keySet()) {
            TypeElement fragmentElement = bundleQualifiedNames.get(fragmentName);
            ClassName fragment = ClassName.get(fragmentElement);

            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(String.format("create%sInstance", fragmentName))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addStatement("$T fragment = new $T()", fragment, fragment)
                    .addStatement("$T bundle = new $T()", bundleClass, bundleClass);

            for(Element element : params.get(fragmentName)) {
                String key = element.getAnnotation(BundleVar.class).value();
                String elementName = element.getSimpleName().toString();

                TypeMirror typeMirror = element.asType();

                methodSpec.addParameter(TypeName.get(typeMirror), elementName);
                switch (typeMirror.getKind()) {
                    case INT:
                        methodSpec.addStatement("bundle.putInt($S, $L)", key, elementName);
                        break;

                    case LONG:
                        methodSpec.addStatement("bundle.putLong($S, $L)", key, elementName);
                        break;

                    case FLOAT:
                        methodSpec.addStatement("bundle.putFloat($S, $L)", key, elementName);
                        break;

                    case BOOLEAN:
                        methodSpec.addStatement("bundle.putBoolean($S, $L)", key, elementName);
                        break;

                    case BYTE:
                        methodSpec.addStatement("bundle.putByte($S, $L)", key, elementName);
                        break;

                    case CHAR:
                        methodSpec.addStatement("bundle.putChar($S, $L)", key, elementName);
                        break;

                    case DOUBLE:
                        methodSpec.addStatement("bundle.putDouble($S, $L)", key, elementName);
                        break;

                    case SHORT:
                        methodSpec.addStatement("bundle.putShort($S, $L)", key, elementName);
                        break;

                    default:
                        TypeMirror string = mElementUtils.getTypeElement(QUALIFIER_STRING).asType();
                        if(QUALIFIER_STRING.equalsIgnoreCase(typeMirror.toString()))
                            methodSpec.addStatement("bundle.putString($S, $L)", key, elementName);
                        break;
                }
            }

            methodSpec.addStatement("fragment.setArguments(bundle)");
            methodSpec.addStatement("return fragment");
            methodSpec.returns(ClassName.get(bundleQualifiedNames.get(fragmentName)));

            methodSpecs.add(methodSpec.build());
        }
    }

    private void generateIntentLauncherMethods() {
        for(String activityName: qualifiedNames.keySet()) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(String.format("create%sIntent", activityName))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(contextClass, "context")
                    .addStatement("$T intent = new $T($L, $L)", intentClass, intentClass, "context", qualifiedNames.get(activityName) + ".class");

            for(Element element : params.get(activityName)) {
                String key = element.getAnnotation(IntentVar.class).value();
                String elementName = element.getSimpleName().toString();
                methodSpec.addParameter(TypeName.get(element.asType()), elementName);
                methodSpec.addStatement("intent.putExtra($S, $L)", key, elementName);
            }

            methodSpec.addStatement("return intent");
            methodSpec.returns(intentClass);

            methodSpecs.add(methodSpec.build());
        }
    }

    private void generateNavigator() throws IOException {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(NAVIGATOR_CLASS)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        classBuilder.addMethods(methodSpecs);

        JavaFile.builder(packageName, classBuilder.build())
                .build()
                .writeTo(mFiler);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(IntentVar.class.getCanonicalName());
        return annotations;
    }

}