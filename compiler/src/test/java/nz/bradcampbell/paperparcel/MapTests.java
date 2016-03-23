package nz.bradcampbell.paperparcel;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

public class MapTests {

  @Test public void mapOfParcelableTypesTest() throws Exception {
    JavaFileObject dataClass = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "@PaperParcel",
        "public final class Test {",
        "private final Map<Integer, Integer> child;",
        "public Test(Map<Integer, Integer> child) {",
        "this.child = child;",
        "}",
        "public Map<Integer, Integer> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject testParcel = JavaFileObjects.forSourceString("test/TestParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class TestParcel implements TypedParcelable<Test> {",
        "public static final Parcelable.Creator<TestParcel> CREATOR = new Parcelable.Creator<TestParcel>() {",
        "@Override public TestParcel createFromParcel(Parcel in) {",
        "Map<Integer, Integer> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Integer, Integer> child = new HashMap<Integer, Integer>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "Integer outChildValue = null;",
        "if (in.readInt() == 0) {",
        "outChildValue = in.readInt();",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Test data = new Test(outChild);",
        "return new TestParcel(data);",
        "}",
        "@Override public TestParcel[] newArray(int size) {",
        "return new TestParcel[size];",
        "}",
        "};",
        "public final Test data;",
        "public TestParcel(Test data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Integer, Integer> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, Integer> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue());",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSource()).that(dataClass)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(testParcel);
  }

  @Test public void mapWithDataTypeAsKeyTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<Child, Integer> child;",
        "public Root(Map<Child, Integer> child) {",
        "this.child = child;",
        "}",
        "public Map<Child, Integer> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<Child, Integer> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Child, Integer> child = new HashMap<Child, Integer>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Child outChildKey = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildKeyChild = null;",
        "if (in.readInt() == 0) {",
        "outChildKeyChild = in.readInt();",
        "}",
        "outChildKey = new Child(outChildKeyChild);",
        "}",
        "Integer outChildValue = null;",
        "if (in.readInt() == 0) {",
        "outChildValue = in.readInt();",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Child, Integer> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Child, Integer> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childEntry.getKey().getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey().getChild());",
        "}",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue());",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClassRoot, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void mapWithDataTypeAsValueTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<Integer, Child> child;",
        "public Root(Map<Integer, Child> child) {",
        "this.child = child;",
        "}",
        "public Map<Integer, Child> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<Integer, Child> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Integer, Child> child = new HashMap<Integer, Child>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "Child outChildValue = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildValueChild = null;",
        "if (in.readInt() == 0) {",
        "outChildValueChild = in.readInt();",
        "}",
        "outChildValue = new Child(outChildValueChild);",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Integer, Child> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, Child> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childEntry.getValue().getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue().getChild());",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClassRoot, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void mapWithDataTypeAsKeyAndValueTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<Child, Child> child;",
        "public Root(Map<Child, Child> child) {",
        "this.child = child;",
        "}",
        "public Map<Child, Child> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<Child, Child> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Child, Child> child = new HashMap<Child, Child>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Child outChildKey = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildKeyChild = null;",
        "if (in.readInt() == 0) {",
        "outChildKeyChild = in.readInt();",
        "}",
        "outChildKey = new Child(outChildKeyChild);",
        "}",
        "Child outChildValue = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildValueChild = null;",
        "if (in.readInt() == 0) {",
        "outChildValueChild = in.readInt();",
        "}",
        "outChildValue = new Child(outChildValueChild);",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Child, Child> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Child, Child> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childEntry.getKey().getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey().getChild());",
        "}",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childEntry.getValue().getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue().getChild());",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClassRoot, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void mapWithParcelableListAsValueTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.lang.Integer;",
        "import java.util.Map;",
        "import java.util.List;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<Integer, List<Integer>> child;",
        "public Root(Map<Integer, List<Integer>> child) {",
        "this.child = child;",
        "}",
        "public Map<Integer, List<Integer>> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.ArrayList;",
        "import java.util.HashMap;",
        "import java.util.List;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<Integer, List<Integer>> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Integer, List<Integer>> child = new HashMap<Integer, List<Integer>>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "List<Integer> outChildValue = null;",
        "if (in.readInt() == 0) {",
        "int childValueSize = in.readInt();",
        "List<Integer> childValue = new ArrayList<Integer>(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Integer outChildValueItem = null;",
        "if (in.readInt() == 0) {",
        "outChildValueItem = in.readInt();",
        "}",
        "childValue.add(outChildValueItem);",
        "}",
        "outChildValue = childValue;",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Integer, List<Integer>> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, List<Integer>> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "int childValueSize = childEntry.getValue().size();",
        "dest.writeInt(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Integer childValueItem = childEntry.getValue().get(childValueIndex);",
        "if (childValueItem == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childValueItem);",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSource()).that(dataClassRoot)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void mapWithNonParcelableListAsValueTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "import java.util.List;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<Integer, List<Child>> child;",
        "public Root(Map<Integer, List<Child>> child) {",
        "this.child = child;",
        "}",
        "public Map<Integer, List<Child>> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.ArrayList;",
        "import java.util.HashMap;",
        "import java.util.List;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<Integer, List<Child>> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<Integer, List<Child>> child = new HashMap<Integer, List<Child>>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "List<Child> outChildValue = null;",
        "if (in.readInt() == 0) {",
        "int childValueSize = in.readInt();",
        "List<Child> childValue = new ArrayList<Child>(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Child outChildValueItem = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildValueItemChild = null;",
        "if (in.readInt() == 0) {",
        "outChildValueItemChild = in.readInt();",
        "}",
        "outChildValueItem = new Child(outChildValueItemChild);",
        "}",
        "childValue.add(outChildValueItem);",
        "}",
        "outChildValue = childValue;",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<Integer, List<Child>> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, List<Child>> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "int childValueSize = childEntry.getValue().size();",
        "dest.writeInt(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Child childValueItem = childEntry.getValue().get(childValueIndex);",
        "if (childValueItem == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childValueItem.getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childValueItem.getChild());",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClassRoot, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void mapWithParcelableListAsKeyAndNonParcelableListAsValueTest() throws Exception {
    JavaFileObject dataClassRoot = JavaFileObjects.forSourceString("test.Root", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.Map;",
        "import java.util.List;",
        "@PaperParcel",
        "public final class Root {",
        "private final Map<List<Integer>, List<Child>> child;",
        "public Root(Map<List<Integer>, List<Child>> child) {",
        "this.child = child;",
        "}",
        "public Map<List<Integer>, List<Child>> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject rootParcel = JavaFileObjects.forSourceString("test/RootParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.ArrayList;",
        "import java.util.HashMap;",
        "import java.util.List;",
        "import java.util.Map;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class RootParcel implements TypedParcelable<Root> {",
        "public static final Parcelable.Creator<RootParcel> CREATOR = new Parcelable.Creator<RootParcel>() {",
        "@Override public RootParcel createFromParcel(Parcel in) {",
        "Map<List<Integer>, List<Child>> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "Map<List<Integer>, List<Child>> child = new HashMap<List<Integer>, List<Child>>(childSize);",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "List<Integer> outChildKey = null;",
        "if (in.readInt() == 0) {",
        "int childKeySize = in.readInt();",
        "List<Integer> childKey = new ArrayList<Integer>(childKeySize);",
        "for (int childKeyIndex = 0; childKeyIndex < childKeySize; childKeyIndex++) {",
        "Integer outChildKeyItem = null;",
        "if (in.readInt() == 0) {",
        "outChildKeyItem = in.readInt();",
        "}",
        "childKey.add(outChildKeyItem);",
        "}",
        "outChildKey = childKey;",
        "}",
        "List<Child> outChildValue = null;",
        "if (in.readInt() == 0) {",
        "int childValueSize = in.readInt();",
        "List<Child> childValue = new ArrayList<Child>(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Child outChildValueItem = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildValueItemChild = null;",
        "if (in.readInt() == 0) {",
        "outChildValueItemChild = in.readInt();",
        "}",
        "outChildValueItem = new Child(outChildValueItemChild);",
        "}",
        "childValue.add(outChildValueItem);",
        "}",
        "outChildValue = childValue;",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Root data = new Root(outChild);",
        "return new RootParcel(data);",
        "}",
        "@Override public RootParcel[] newArray(int size) {",
        "return new RootParcel[size];",
        "}",
        "};",
        "public final Root data;",
        "public RootParcel(Root data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "Map<List<Integer>, List<Child>> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<List<Integer>, List<Child>> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "int childKeySize = childEntry.getKey().size();",
        "dest.writeInt(childKeySize);",
        "for (int childKeyIndex = 0; childKeyIndex < childKeySize; childKeyIndex++) {",
        "Integer childKeyItem = childEntry.getKey().get(childKeyIndex);",
        "if (childKeyItem == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childKeyItem);",
        "}",
        "}",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "int childValueSize = childEntry.getValue().size();",
        "dest.writeInt(childValueSize);",
        "for (int childValueIndex = 0; childValueIndex < childValueSize; childValueIndex++) {",
        "Child childValueItem = childEntry.getValue().get(childValueIndex);",
        "if (childValueItem == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childValueItem.getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childValueItem.getChild());",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClassRoot, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(rootParcel);
  }

  @Test public void treeMapOfParcelableTypesTest() throws Exception {
    JavaFileObject dataClass = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.TreeMap;",
        "@PaperParcel",
        "public final class Test {",
        "private final TreeMap<Integer, Integer> child;",
        "public Test(TreeMap<Integer, Integer> child) {",
        "this.child = child;",
        "}",
        "public TreeMap<Integer, Integer> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject testParcel = JavaFileObjects.forSourceString("test/TestParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.Map;",
        "import java.util.TreeMap;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class TestParcel implements TypedParcelable<Test> {",
        "public static final Parcelable.Creator<TestParcel> CREATOR = new Parcelable.Creator<TestParcel>() {",
        "@Override public TestParcel createFromParcel(Parcel in) {",
        "TreeMap<Integer, Integer> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "TreeMap<Integer, Integer> child = new TreeMap<Integer, Integer>();",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "Integer outChildValue = null;",
        "if (in.readInt() == 0) {",
        "outChildValue = in.readInt();",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Test data = new Test(outChild);",
        "return new TestParcel(data);",
        "}",
        "@Override public TestParcel[] newArray(int size) {",
        "return new TestParcel[size];",
        "}",
        "};",
        "public final Test data;",
        "public TestParcel(Test data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "TreeMap<Integer, Integer> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, Integer> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue());",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSource()).that(dataClass)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(testParcel);
  }

  @Test public void treeMapOfNonParcelableValueTypesTest() throws Exception {
    JavaFileObject dataClass = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "import java.util.TreeMap;",
        "@PaperParcel",
        "public final class Test {",
        "private final TreeMap<Integer, Child> child;",
        "public Test(TreeMap<Integer, Child> child) {",
        "this.child = child;",
        "}",
        "public TreeMap<Integer, Child> getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject dataClassChild = JavaFileObjects.forSourceString("test.Child", Joiner.on('\n').join(
        "package test;",
        "public final class Child {",
        "private final Integer child;",
        "public Child(Integer child) {",
        "this.child = child;",
        "}",
        "public Integer getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject testParcel = JavaFileObjects.forSourceString("test/TestParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Integer;",
        "import java.lang.Override;",
        "import java.util.Map;",
        "import java.util.TreeMap;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class TestParcel implements TypedParcelable<Test> {",
        "public static final Parcelable.Creator<TestParcel> CREATOR = new Parcelable.Creator<TestParcel>() {",
        "@Override public TestParcel createFromParcel(Parcel in) {",
        "TreeMap<Integer, Child> outChild = null;",
        "if (in.readInt() == 0) {",
        "int childSize = in.readInt();",
        "TreeMap<Integer, Child> child = new TreeMap<Integer, Child>();",
        "for (int childIndex = 0; childIndex < childSize; childIndex++) {",
        "Integer outChildKey = null;",
        "if (in.readInt() == 0) {",
        "outChildKey = in.readInt();",
        "}",
        "Child outChildValue = null;",
        "if (in.readInt() == 0) {",
        "Integer outChildValueChild = null;",
        "if (in.readInt() == 0) {",
        "outChildValueChild = in.readInt();",
        "}",
        "outChildValue = new Child(outChildValueChild);",
        "}",
        "child.put(outChildKey, outChildValue);",
        "}",
        "outChild = child;",
        "}",
        "Test data = new Test(outChild);",
        "return new TestParcel(data);",
        "}",
        "@Override public TestParcel[] newArray(int size) {",
        "return new TestParcel[size];",
        "}",
        "};",
        "public final Test data;",
        "public TestParcel(Test data) {",
        "this.data = data;",
        "}",
        "@Override public int describeContents() {",
        "return 0;",
        "}",
        "@Override public void writeToParcel(Parcel dest, int flags) {",
        "TreeMap<Integer, Child> child = data.getChild();",
        "if (child == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(child.size());",
        "for (Map.Entry<Integer, Child> childEntry : child.entrySet()) {",
        "if (childEntry.getKey() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getKey());",
        "}",
        "if (childEntry.getValue() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "if (childEntry.getValue().getChild() == null) {",
        "dest.writeInt(1);",
        "} else {",
        "dest.writeInt(0);",
        "dest.writeInt(childEntry.getValue().getChild());",
        "}",
        "}",
        "}",
        "}",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(asList(dataClass, dataClassChild))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(testParcel);
  }
}
