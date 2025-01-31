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