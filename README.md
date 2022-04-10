# JAsync

JAsync provides `async` and `await` functions in Java (8+) based on `CompletableFuture`.

The idea is very basic, `async` and `await` are wrappers on `CompletableFuture`'s methods to give a convenient way to
call multiple long-running processes and wait for execution at the end.

It integrates with spring framework (but don't require it). so if you need to run the async tasks on a
Spring `TaskExecutor`, you need to define a bean of type `TaskExecutor` with the name `jasyncTaskExecutor`

## Install
```xml
<dependency>
  <groupId>com.github.mhewedy</groupId>
  <artifactId>jasync</artifactId>
  <version>0.0.4</version>
</dependency>
```

## Example

1. Invoke functions and doesn't expect result:

```java
Promise<?> p1 = async(this::callLongRunningFunction1);
Promise<?> p2 = async(this::callLongRunningFunction2);

// ... other code here ...

// and at the last:
await(p1, p2);
```

2. Invoke functions and expects result:

```java
Promise<String> p1 = async(this::callLongRunningFunction1);
Promise<String> p2 = async(this::callLongRunningFunction2);

// ... other code here ...

// and at the last:
String resutl1 = await(p1);
String resutl2 = await(p2);
```

> NOTE: It is different from Javascript async await. it when multiple async operation cascaded, you need to wait for the first before invoke the second.


#### 🎖 Special Thanks

Special thanks to [Rashad Saif](https://github.com/rashadsaif) and [Hamada Elnoby](https://github.com/hamadaelnopy) for helping in the design, inspring with ideas, and for doing code review.  
