# JAsync

JAsync provides `async` and `await` functions in Java (8+) based on `CompletableFuture`.

The idea is very basic, `async` and `await` are wrappers around `CompletableFuture`'s methods to give a convenient way to
call multiple long-running processes and wait for execution at the end.

>NOTE: It is different from Javascript async/await where in JS the code is suspended until the result is returned, but here a thread is handling the call(s) and then we block and wait for the result at the end.

>NOTE: The API of jasync is influenced by the [Task API of Elixir](https://hexdocs.pm/elixir/Task.html)

It integrates with the spring framework (but doesn't require it). so if you need to run the async tasks on a
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

1. Invoke functions and doesn't expect results:

```java
Promise<?> p1 = Task.async(this::callLongRunningFunction1);
Promise<?> p2 = Task.async(this::callLongRunningFunction2);

// ... other code here ...

// and at the last:
Task.await(p1, p2);
```

2. Invoke functions and expects result:

```java
Promise<String> p1 = Task.async(this::callLongRunningFunction1);
Promise<String> p2 = Task.async(this::callLongRunningFunction2);

// ... other code here ...

// and at the last:
String resutl1 = Task.await(p1);
String resutl2 = Task.await(p2);
```

> NOTE: It is different from Javascript async await. it when multiple async operations cascaded, you need to wait for the first before invoking the second.


#### 🎖 Special Thanks

Special thanks to [Rashad Saif](https://github.com/rashadsaif) and [Hamada Elnoby](https://github.com/hamadaelnopy) for helping in the design, inspiring with ideas, and for doing the code review.  
