All essential lombok knowledge

---

docs: https://projectlombok.org/features/

---

Delombok

Replace all lombok annotations with actual implementation.

![delombok](images/delombok.png)

---

[val](https://projectlombok.org/features/val)

Will be replaced with exact value type. If value will be null than the type will be `Object`.

You can also find [var](https://projectlombok.org/features/var) that works similar, but use java's `var` since `Java 10`

```java
# with lombok
public class ValExample {
  public String example() {
    val example = new ArrayList<String>();
    example.add("Hello, World!");
    val foo = example.get(0);
    return foo.toLowerCase();
  }
}

# vanilla java
public class ValExample {
  public String example() {
    ArrayList<String> example = new ArrayList<String>();
    example.add("Hello, World!");
    String foo = example.get(0);
    return foo.toLowerCase();
  }
}

# config
lombok.val.flagUsage=[warning,error]
lombok.var.flagUsage=[warning,error]
# lombok will flag any usage of those as error or warning
# default - not set
```

---

[@NonNull](https://projectlombok.org/features/NonNull)

You can use on any param of method or constructor. Lombok will add null check at the top of the method. On primitive parameters will generate warnings.

```java
# with lombok
public class NonNullExample extends Something {
  private String name;
  
  public NonNullExample(@NonNull Person person) {
    super("Hello");
    this.name = person.getName();
  }
}

# vanilla java
public class NonNullExample extends Something {
  private String name;
  
  public NonNullExample(@NonNull Person person) {
    super("Hello");
    if (person == null) {
      throw new NullPointerException(
      "person is marked non-null but is null"
      );
    }
    this.name = person.getName();
  }
}

# config
lombok.nonNull.exceptionType=[NullPointerException, IllegalArgumentException,JDK,GUAVA,Assertion]
# which exception should be thrown / assert will be generated / JDK or Guava will instead of vanilla x == null will use functions from those frameworks
# default - NullPointerException

lombok.nonNull.flagUsage=[warning,error]
# if used than lombok will flag it as an error
# default - not set
```

---

[@Cleanup](https://projectlombok.org/features/Cleanup)

Will generate `try finally` block and call `.close()` in `finally` block. Also, you can specify which method should be called f.e. `@Cleanup("thismethod")`.

```java
# with lombok
public class CleanupExample {
  public static void main(String[] args) throws IOException {
    @Cleanup InputStream in = new FileInputStream(args[0]);
    byte[] b = new byte[10000];
    while (true) {
      int r = in.read(b);
      if (r == -1) break;
    }
  }
}

# vanilla java
public class CleanupExample {
    public CleanupExample() {
    }

    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream(args[0]);

        try {
            byte[] b = new byte[10000];

            int r;
            do {
                r = in.read(b);
            } while(r != -1);
        } finally {
            if (Collections.singletonList(in).get(0) != null) {
                in.close();
            }

        }

    }
}

# config
lombok.cleanup.flagUsage=[warning,error]
# lombok will flag any usage as waning/error
# default - not set
```

---

[@Getter and @Setter](https://projectlombok.org/features/GetterSetter)

You can put those annotations on class level or above class variable. Will generate getters and setters. You can specify accessLevel with `AccessLevel.` and set it to `PUBLIC, PROTECTED, PACKAGE, PRIVATE, NONE`. `NONE` means that getter/setter won't get generated. `@Getter` can be used on enums. Every generated method will be annotated with [@Generated](https://projectlombok.org/api/lombok/Generated)

`@Getter(lazy=true)` allows to create field that will store and cache method call (and also it will be thread safe). To accessed that value always use created by lombok getter.

```java
# with lombok
public class GetterSetterExample {
  @Getter @Setter private int age = 10;
  
  @Setter(AccessLevel.PROTECTED) private String name;

	@Getter(lazy=true) private final double[] cached = expensive();
  
  private double[] expensive() {
    double[] result = new double[1000000];
    for (int i = 0; i < result.length; i++) {
      result[i] = Math.asin(i);
    }
    return result;
  }
}

# vanilla java
public class GetterSetterExample {
	private int age = 10;
    private String name;
    private final AtomicReference<Object> cached = new AtomicReference();

    public GetterSetterExample() {
    }

    private double[] expensive() {
        double[] result = new double[1000000];

        for(int i = 0; i < result.length; ++i) {
            result[i] = Math.asin(i);
        }

        return result;
    }

    @Generated
    public int getAge() {
        return this.age;
    }

    @Generated
    public void setAge(int age) {
        this.age = age;
    }

    @Generated
    protected void setName(String name) {
        this.name = name;
    }

    @Generated
    public double[] getCached() {
        Object $value = this.cached.get();
        if ($value == null) {
            synchronized(this.cached) {
                $value = this.cached.get();
                if ($value == null) {
                    double[] actualValue = this.expensive();
                    $value = actualValue == null ? this.cached : actualValue;
                    this.cached.set($value);
                }
            }
        }

        return (double[])($value == this.cached ? null : $value);
    }
}

# config
lombok.accessors.chain =[true,false]
# default - false
# if set to true, than setters will return this

lombok.accessors.fluent=[true,false]
# default - false
# if set to true, than methods will not be prefixed with 'get', 'set', 'is', it will use just field names

lombok.accessors.prefix += [list]
lombok.accessors.prefix -= [list]
# will add or remove prefixes from all fields, f.e. if we do -= [meet]
and we will have field named meetName than getters and setters will look like 'getName' and 'setName'

lombok.getter.noIsPrefix=[true,false]
# default - false
# disables 'is' prefix for getters, will use 'get' instead

lombok.accessors.capitalization=[basic,beanspec]
# default - basic
# describeas how uShaped fields should be handled, f.e. iName
with 'basic' will look like 'getIName', but with beanspec will look like 'getiName'

lombok.setter.flagUsage=[warning,error]
lombok.getter.flagUsage=[warning,error]
lombok.getter.lazy.flagUsage=[warning,error]
# default - is no set
# will flag any usage of those as waning/error


```

---

[@ToString](https://projectlombok.org/features/ToString)

Will generate `.toString()` method.

By default, all non-static methods will be included.

`@ToString.Exlude` to exclude fields
`@ToString.Include` to include fields

`@ToString(callSuper=true)` and `@ToString(onlyExplicitlyIncluded=true)` just like in `#config` section.

You can also include non-static methods that takes no arguments. Use `@ToString.Include` to accomplish that.

Change name of field with `@ToString.Include(name="new name")`.

Change the order of fields printed with `@ToString.Include(rank= 1)`. Higher number == printed first.

If we have method that has the same name as field, and we will include that field than value of that field will be replaced with method call.

All fields that name starts with `$` will be by default excluded.

Can be used on enum.

```java
# with lombok
@ToString
public class ToStringExample {
  private static final int STATIC_VAR = 10;
  private String name;
  private Shape shape = new Square(5, 10);
  private String[] tags;
  @ToString.Exclude private int id;
  
  public String getName() {
    return this.name;
  }
  
  @ToString(callSuper=true, includeFieldNames=true)
  public static class Square extends Shape {
    private final int width, height;
    
    public Square(int width, int height) {
      this.width = width;
      this.height = height;
    }
  }
}

# vanilla java
public class ToStringExample {
	private static final int STATIC_VAR = 10;
    private String name;
    private Shape shape = new Square(5, 10);
    private String[] tags;
    private int id;

    public ToStringExample() {
    }

    public String getName() {
        return this.name;
    }

    @Generated
    public String toString() {
        String var10000 = this.getName();
        return "ToStringExample(name=" + var10000 + ", shape=" + String.valueOf(this.shape) + ", tags=" + Arrays.deepToString(this.tags) + ")";
    }

    public static class Square extends Shape {
        private final int width;
        private final int height;

        public Square(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Generated
        public String toString() {
            String var10000 = super.toString();
            return "ToStringExample.Square(super=" + var10000 + ", width=" + this.width + ", height=" + this.height + ")";
        }
    }
}

# config
lombok.toString.includeFieldNames=[true,false]
# default = true
# will skip fieldNames in printed text

lombok.toString.doNotUseGetters=[true,false]
# default = false
# if true, lombok will use fields directly and skip usage of getters

lombok.toString.callSuper=[call,skip.warn]
# default - skip
# call - will call superclass implementation of .toString()
# warn - will skip, but also will warn about that

lombok.toString.onlyExplicitlyIncluded=[true,false]
# default - false
# only fields with @ToString.Included will get included

lombok.toString.flagUsage=[warning,error]
# default - unset
# lombok will flah any usage
```

---

[@EqualsAndHashCode](https://projectlombok.org/features/EqualsAndHashCode)

Generates `.equals()` and `.hashcode()`.

Similar to `@ToString`:
- `@EqualsAndHashCode.Exclude`
- `@EqualsAndHashCode.Include`
- `@EqualsAndHashCode(onlyExplicitlyIncluded = true)`

All static fields and [[transient]] fields will be skipped.

If method with the same name as variable exists than it will get skipped.

Variables that starts with `$` will be excluded automatically.

```java
# with lombok
@EqualsAndHashCode
public class EqualsAndHashCodeExample {
  private transient int transientVar = 10;
  private String name;
  private double score;
  
  @EqualsAndHashCode.Exclude
  private Shape shape = new Square(5, 10);
  
  private String[] tags;
  
  @EqualsAndHashCode.Exclude
  private int id;
  
  public String getName() {
    return this.name;
  }
}

# vanilla java
public class EqualsAndHashCodeExample {
    private transient int transientVar = 10;
    private String name;
    private double score;
    private Shape shape = new ToStringExample.Square(5, 10);
    private String[] tags;
    private int id;

    public EqualsAndHashCodeExample() {
    }

    public String getName() {
        return this.name;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EqualsAndHashCodeExample)) {
            return false;
        } else {
            EqualsAndHashCodeExample other = (EqualsAndHashCodeExample)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (Double.compare(this.score, other.score) != 0) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                if (!Arrays.deepEquals(this.tags, other.tags)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof EqualsAndHashCodeExample;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $score = Double.doubleToLongBits(this.score);
        result = result * 59 + (int)($score >>> 32 ^ $score);
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.tags);
        return result;
    }
}

# config - 
lombok.equalsAndHashCode.doNotUseGetters=[true,false]
# default - false
# if set to true, than lombok won't use getters

lombok.equalsAndHashCode.callSuper=[call,skip,warn]
# default - warn
# use `.hashcode()` from parent class, skip it or warn on every usage on class that inherits something

lombok.equalsAndHashCode.flagUsage=[warning,error]
# default - not set
# flag any usage of this annotation
```

---

[@NoArgsConstructor, @RequiredArgsConstructor, @AllArgsConstructor](https://projectlombok.org/features/constructor)

Will generate constructors.

`@RequiredArgsConstructor` - will generate constructor for all `final` and `@NonNull` fields.

If we write exactly the same constructor as lombok will generate than error will occur.

`@ConstructorProperties` - will be used for constructor with all fields.

`@NoArgsConstructor` will always set all non-set fields to default (0, false, null)/

`lombok.*<constructor>.flagUsage` won't get triggered for all `@Data` and `@Value`.

All of these annotations can be also used on enums. All constructors will be private (because enums cannot have public constructors).

```java
# with lombok
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConstructorExample<T> {
  private int x, y;
  @NonNull private T description;
  private final String finalNote;
}

# vanilla java
public class ConstructorExample<T> {
    private int x;
    private int y;
    private @NonNull T description;
    private final String finalNote;

    @Generated
    public ConstructorExample(@NonNull T description) {
        if (description == null) {
            throw new NullPointerException("description is marked non-null but is null");
        } else {
            this.description = description;
            this.finalNote = finalNote;
        }
    }

    @Generated
    protected ConstructorExample(int x, int y, @NonNull T description) {
        if (description == null) {
            throw new NullPointerException("description is marked non-null but is null");
        } else {
            this.x = x;
            this.y = y;
            this.description = description;
            this.finalNote = finalNote;
        }
    }
}

# config
lombok.anyConstructor.addConstructorProperties=[true,false]
# default - false
# if set to true tan @ConstructorProperties will be used for all constructors

lombok.[allArgsConstructor|requiredArgsConstructor|noArgsConstructor|anyConstructor].flagUsage=[warning,error]
# default - not set
# if annotation used than show error

lombok.copyableAnnotations=[list]
# default - empty list
# if field has any annotations from list than they will be copied to constructor params, setter params and getter
# f.e. if field has @NonNull and @NonNull will be on the list than it will be copied from field into constructor param, setter param and getter method

lombok.noArgsConstructor.extraPrivate=[true,false]
# default - false
# if true, than lombok will generate private no-args constructor for all @Value or @Data class
```

---

[@Data](https://projectlombok.org/features/Data)

A shortcut for `@ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor`.

Used to create [[POJO]]s from classes

Can be combined with `@ToString` etc. if you want to set some specific configuration for specific annotation.

Things like `AccessLevel.NONE` ofc works even if you are using `@Data`.

`@Data(staticConstructor="of")` - will generate new static constructor so you will be able to create object like `Foo.of(1)`.

```java
# with lombok
@Data 
public class DataExample {
  private final String name;
  @Setter(AccessLevel.PACKAGE) private int age;
  private double score;
  private String[] tags;
}

# vanilla java
public class DataExample {
  private final String name;
  private int age;
  private double score;
  private String[] tags;
  
  public DataExample(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
  void setAge(int age) {
    this.age = age;
  }
  
  public int getAge() {
    return this.age;
  }
  
  public void setScore(double score) {
    this.score = score;
  }
  
  public double getScore() {
    return this.score;
  }
  
  public String[] getTags() {
    return this.tags;
  }
  
  public void setTags(String[] tags) {
    this.tags = tags;
  }
  
  @Override public String toString() {
    return "DataExample(" + this.getName() + ", " + this.getAge() + ", " + this.getScore() + ", " + Arrays.deepToString(this.getTags()) + ")";
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof DataExample;
  }
  
  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof DataExample)) return false;
    DataExample other = (DataExample) o;
    if (!other.canEqual((Object)this)) return false;
    if (this.getName() == null ? other.getName() != null : !this.getName().equals(other.getName())) return false;
    if (this.getAge() != other.getAge()) return false;
    if (Double.compare(this.getScore(), other.getScore()) != 0) return false;
    if (!Arrays.deepEquals(this.getTags(), other.getTags())) return false;
    return true;
  }
  
  @Override 
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long temp1 = Double.doubleToLongBits(this.getScore());
    result = (result*PRIME) + (this.getName() == null ? 43 : this.getName().hashCode());
    result = (result*PRIME) + this.getAge();
    result = (result*PRIME) + (int)(temp1 ^ (temp1 >>> 32));
    result = (result*PRIME) + Arrays.deepHashCode(this.getTags());
    return result;
  }
}

# config
lombok.data.flagUsage=[warning,error]
# default - not set
# will throw if set

lombok.noArgsConstructor.extraPrivate=[true,false]
# default - false
# if true, than will create additional noArgsConstructor that will set all fields to default values
```

---

[@Value](https://projectlombok.org/features/Value)

Don't get confused with [[@Value in Spring]].

Works like `@Data` but everything is `final` and there are no setters.
So:
- class is final
- `@ToString @EqualsAndHashCode @AllArgsConstructor @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE) @Getter`
- if you will any additional annotation like `@Builder` (which forces as to have @AllArgsConstructor without final fields), that other annotation is always stronger so in that case constructor will be generated BUT you cannot override behavior of `@FieldDefaults` by putting another `@FieldDefaults`. You can use `@NonFinal or @PackagePrivate` on class or field to change `@Value` behavior for that.

Make a notice that `@NonFinal` is experimental. Will make a field non-final.

```java
# with lombok
@Value
public class ValueExample {
  String name;
  
  @With(AccessLevel.PACKAGE)
  @NonFinal int age;
  
  double score;
  
  protected String[] tags;
}

# vanilla java
public final class ValueExample {
  private final String name;
  private int age;
  private final double score;
  protected final String[] tags;
  
  @java.beans.ConstructorProperties({"name", "age", "score", "tags"})
  public ValueExample(String name, int age, double score, String[] tags) {
    this.name = name;
    this.age = age;
    this.score = score;
    this.tags = tags;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getAge() {
    return this.age;
  }
  
  public double getScore() {
    return this.score;
  }
  
  public String[] getTags() {
    return this.tags;
  }
  
  @java.lang.Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ValueExample)) return false;
    final ValueExample other = (ValueExample)o;
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    if (this.getAge() != other.getAge()) return false;
    if (Double.compare(this.getScore(), other.getScore()) != 0) return false;
    if (!Arrays.deepEquals(this.getTags(), other.getTags())) return false;
    return true;
  }
  
  @java.lang.Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    result = result * PRIME + this.getAge();
    final long $score = Double.doubleToLongBits(this.getScore());
    result = result * PRIME + (int)($score >>> 32 ^ $score);
    result = result * PRIME + Arrays.deepHashCode(this.getTags());
    return result;
  }
  
  @java.lang.Override
  public String toString() {
    return "ValueExample(name=" + getName() + ", age=" + getAge() + ", score=" + getScore() + ", tags=" + Arrays.deepToString(getTags()) + ")";
  }
  
  ValueExample withAge(int age) {
    return this.age == age ? this : new ValueExample(name, age, score, tags);
  }
}

# config
lombok.value.flagUsage=[warning,error]
# default - not set
# if set than will generate error on occurance

lombok.noArgsConstructor.extraPrivate=[true,false]
# default - false
# generate extra noArgsConstructor that will set all fields to its default values
```

---

[@Builder](https://projectlombok.org/features/Builder)

API for building objects in more readable way. It will generate method `.builder()` which will return `<ClassName>Builder` and it will be the beginning of the object creation.  It will generate method for each class variable. Methods will be setters for that fields and will have names like class fields. All methods will return `<ClassName>Builder`. To create object you have to use `.build()` at the end.

If method exists it will get skipped, so you can override builder methods.

If you have filed like `List<String> names` you can add annotation `@Singular` on it and create object with it like:
`Example.builder()
	.name("123")
	.name("321")
	.build();`
You can specify name of singular thing with `@Singular("WILL_BE_THE_NAME")`. If lombok cannot create the name automatically than it will raise an error.
@Singular(ignoreNullCollections = true) - will make lombok do not throw exceptions on passing null as collections. Code snippet: [for @Singular]([https://projectlombok.org/features/BuilderSingular](for @Singular)).

You can build that object one by one.

You can put builder annotation on class, constructor or method (but the last one has different behavior).

`@Builder`needs `@AllArgsConstructor`. If annotation / constructor is not provided than `@Builder` will generate it by itself.

`@Builder.ObtainVia(method = "calculateFoo")` - you can put that on field and then builder will use that method to obtain value (that method can take argument that you will pass during object creation).

`@Builder.Default` - if you don't specify field value during object creation it will get default value. But if you add this annotation than you can specify value for that field.

Config:
`@Builder(builderClassName = "HelloWorldBuilder", buildMethodName = "execute", builderMethodName = "helloWorld", toBuilder = true, access = AccessLevel.PRIVATE, setterPrefix = "set")`
- The _builder's class name_ (default: return type + 'Builder')
- The _build()_ method's name (default: `"build"`)
- The _builder()_ method's name (default: `"builder"`)
- If you want `toBuilder()` (default: no)
- The access level of all generated elements (default: `public`).
- (discouraged) If you want your builder's 'set' methods to have a prefix, i.e. `Person.builder().setName("Jane").build()` instead of `Person.builder().name("Jane").build()` and what it should be.

```java
# with lombok
@Builder
public class BuilderExample {
  @Builder.Default 
  private long created = System.currentTimeMillis();
  
  private String name;
  
  private int age;
  
  @Singular
  private Set<String> occupations;
}

# vanilla java
public class BuilderExample {
  private long created;
  private String name;
  private int age;
  private Set<String> occupations;
  
  BuilderExample(String name, int age, Set<String> occupations) {
    this.name = name;
    this.age = age;
    this.occupations = occupations;
  }
  
  private static long $default$created() {
    return System.currentTimeMillis();
  }
  
  public static BuilderExampleBuilder builder() {
    return new BuilderExampleBuilder();
  }
  
  public static class BuilderExampleBuilder {
    private long created;
    private boolean created$set;
    private String name;
    private int age;
    private java.util.ArrayList<String> occupations;
    
    BuilderExampleBuilder() {
    }
    
    public BuilderExampleBuilder created(long created) {
      this.created = created;
      this.created$set = true;
      return this;
    }
    
    public BuilderExampleBuilder name(String name) {
      this.name = name;
      return this;
    }
    
    public BuilderExampleBuilder age(int age) {
      this.age = age;
      return this;
    }
    
    public BuilderExampleBuilder occupation(String occupation) {
      if (this.occupations == null) {
        this.occupations = new java.util.ArrayList<String>();
      }
      
      this.occupations.add(occupation);
      return this;
    }
    
    public BuilderExampleBuilder occupations(Collection<? extends String> occupations) {
      if (this.occupations == null) {
        this.occupations = new java.util.ArrayList<String>();
      }

      this.occupations.addAll(occupations);
      return this;
    }
    
    public BuilderExampleBuilder clearOccupations() {
      if (this.occupations != null) {
        this.occupations.clear();
      }
      
      return this;
    }

    public BuilderExample build() {
      // complicated switch statement to produce a compact properly sized immutable set omitted.
      Set<String> occupations = ...;
      return new BuilderExample(created$set ? created : BuilderExample.$default$created(), name, age, occupations);
    }
    
    @java.lang.Override
    public String toString() {
      return "BuilderExample.BuilderExampleBuilder(created = " + this.created + ", name = " + this.name + ", age = " + this.age + ", occupations = " + this.occupations + ")";
    }
  }
}

# config
lombok.builder.className=[name]
# default - *Builder
# enables change of a <Classname>Builder

lombok.builder.flagUsage=[warning,error]
# default - not set
# if used than error

lombok.singular.useGuava=[true,false]
# default - false
# will use builders and immutable collections from guava

lombok.singular.auto=[true,false]
# default - true
# if false than you have to always specify name of singular thing when using @Singular
```

---

[@SneakyThrows](https://projectlombok.org/features/SneakyThrows)

Lombok will fake out the compiler so you don't have to handle exception that cannot happen. You can pass exceptions to the `@SneakyThrows([here])` so it will only get silent those.

It will raise an error if you try to declare a `checked exception` as sneakily thrown when you don't call any methods that throw this `exception`.

You cannot put it on constructor of a class that inherits from other class, because super call has to be the first in this constructor (irrelevant since [[java 23]])

```java
# with lombok
public class SneakyThrowsExample implements Runnable {
  @SneakyThrows(UnsupportedEncodingException.class)
  public String utf8ToString(byte[] bytes) {
    return new String(bytes, "UTF-8");
  }
  
  @SneakyThrows
  public void run() {
    throw new Throwable();
  }
}

# vanilla java
public class SneakyThrowsExample implements Runnable {
    public SneakyThrowsExample() {
    }

    public String utf8ToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException $ex) {
            throw $ex;
        }
    }

    public void run() {
        try {
            throw new Throwable();
        } catch (Throwable $ex) {
            throw $ex;
        }
    }
}

# config
lombok.sneakyThrows.flagUsage=[warning,error]
# default - not set
# flag any usage
```

---

[@Synchronized](https://projectlombok.org/features/Synchronized)

Safer way to use [[synchronized]].
Can be used on methods and static methods.
You can specify field name in `@Synchornized(here)` that lock will be working on.

It's safer than blocking on `this` because something can also lock on the same object in the other part of the program so it could create some side effects.

For using with virtual threads recommended is [[#@Locked]].

Lock is initialized as `Object[]`, because `new Object()` is not serializable, but empty array is.

```java
# with lombok
public class SynchronizedExample {
  private final Object readLock = new Object();
  
  @Synchronized
  public static void hello() {
    System.out.println("world");
  }
  
  @Synchronized
  public int answerToLife() {
    return 42;
  }
  
  @Synchronized("readLock")
  public void foo() {
    System.out.println("bar");
  }
}

# vanilla java
public class SynchronizedExample {
  private static final Object $LOCK = new Object[0];
  private final Object $lock = new Object[0];
  private final Object readLock = new Object();
  
  public static void hello() {
    synchronized($LOCK) {
      System.out.println("world");
    }
  }
  
  public int answerToLife() {
    synchronized($lock) {
      return 42;
    }
  }
  
  public void foo() {
    synchronized(readLock) {
      System.out.println("bar");
    }
  }
}

# config
lombok.synchronized.flagUsage=[warning,error]
# default - not set
# throw if appears
```

---

## @Locked
[@Locked](https://projectlombok.org/features/Locked)

Similar to `@Synchronized` but uses `ReentrantLock` - used with virtual threads (so java 20+). `@Locked.Read` used when blocking on read operations. `@Locked.Write` used when blocking on write operations. You cannot use `@Locked.Read` and `@Locked.Write` on the same field, because it blocks on different lock.

```java
# with lombok
public class LockedExample {
  private int value = 0;
  
  @Locked.Read
  public int getValue() {
    return value;
  }
  
  @Locked.Write
  public void setValue(int newValue) {
    value = newValue;
  }
  
  @Locked("baseLock")
  public void foo() {
    System.out.println("bar");
  }
}

# vanilla java
public class LockedExample {
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock baseLock = new ReentrantLock();
  private int value = 0;
  
  public int getValue() {
    this.lock.readLock().lock();
    try {
      return value;
    } finally {
      this.lock.readLock().unlock();
    }
  }
  
  public void setValue(int newValue) {
    this.lock.writeLock().lock();
    try {
      value = newValue;
    } finally {
      this.lock.writeLock().unlock();
    }
  }
  
  public void foo() {
    this.baseLock.lock();
    try {
      System.out.println("bar");
    } finally {
      this.baseLock.unlock();
    }
  }
}

# config
lombok.locked.flagUsage=[warning,error]
# default - not set
# flag any usage of annotation
```

---

[@With](https://projectlombok.org/features/With)

Create copy of the object with one field changed / get the object if passed value is the same. Requires all args constructor. Cannot be used for static fields.

```java
# with lombok
public class WithExample {
  @With(AccessLevel.PROTECTED) @NonNull private final String name;
  @With private final int age;
  
  public WithExample(@NonNull String name, int age) {
    this.name = name;
    this.age = age;
  }
}

# vanilla java
public class WithExample {
  private @NonNull final String name;
  private final int age;

  public WithExample(String name, int age) {
    if (name == null) throw new NullPointerException();
    this.name = name;
    this.age = age;
  }

  protected WithExample withName(@NonNull String name) {
    if (name == null) throw new java.lang.NullPointerException("name");
    return this.name == name ? this : new WithExample(name, age);
  }

  public WithExample withAge(int age) {
    return this.age == age ? this : new WithExample(name, age);
  }
}

# config
lombok.accessors.prefix+=[fieldPrefix]
lombok.accessors.prefix-=[fieldPrefix]
# default - not set
# define common prefix / delete common prefix

lombok.accessors.capitalization=[basic,beanspec]
# default - basic
# field - uShape, basic - withUShape, beanspec - withuSpec

lombok.with.flagUsage=[warning,error]
# default - not set
# flag all usages
```

---

[@Log](https://projectlombok.org/features/log)

Adds static field `log` that enables you to use logger framework.

`@CommonsLog` org.apache.commons.logging
`@Flogger` com.google.common.flogger
`@JBossLog` org.jboss.logging
`@Log` java.util.logging
`@Log4j` org.apache.log4j
`@Log4j2` org.apache.logging.log4j
`@Slf4j` org.slf4j
`@XSlf4j` org.slf4j.ext
`@CustomLog`
This option _requires_ that you add a configuration to your `lombok.config` file to specify what `@CustomLog` should do.

```java
# with lombok
@Log
public class LogExample {
  
  public static void main(String... args) {
    log.severe("Something's wrong here");
  }
}

@Slf4j
public class LogExampleOther {
  
  public static void main(String... args) {
    log.error("Something else is wrong here");
  }
}

@CommonsLog(topic="CounterLog")
public class LogExampleCategory {

  public static void main(String... args) {
    log.error("Calling the 'CounterLog' with a message");
  }
}

# vanilla java
public class LogExample {
  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogExample.class.getName());
  
  public static void main(String... args) {
    log.severe("Something's wrong here");
  }
}

public class LogExampleOther {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExampleOther.class);
  
  public static void main(String... args) {
    log.error("Something else is wrong here");
  }
}

public class LogExampleCategory {
  private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog("CounterLog");

  public static void main(String... args) {
    log.error("Calling the 'CounterLog' with a message");
  }
}

# config
lombok.log.flagUsage=[warning,error]
lombok.log.[custom, apacheCommons, flogger, jbosslog, javaUtilLogging, log4j, log4j2, slf4j, xslf4j].flagUsage=[warning,error]
# default - not set
# flag usage as warning/error

lombok.log.fieldName=[customName]
# default - log
# set static field name

lombok.log.fieldIsStatic=[true,false]
# default - true
# if false than every log field will be set per instance

lombok.log.custom.declaration=[]
# default - not set
# used for custom logger setting
```

---

**EXPERIMENTAL**

Worth to mention is that lombok has bunch of experimental features, but:
- they are not as robust as standard features
- they are not as well tested
- may disappear in future releases or may get completely changed

Link: [HERE](https://projectlombok.org/features/experimental/)
Experimental features:
- @Accessors
- @ExtensionMethod
- @FieldDefaults
- @Delegate
- @UtilityClass
- @Helper
- @FieldNameConstants
- @SuperBuilder
- @Tolerate
- @Jacksonized
- @StandardException

Previously experimental, but now they are standard features:
- @Value
- @Builder
- @With (previously named as @Wither)
- var (since Java 10)