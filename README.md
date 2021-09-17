# RandomK

Based on [Marcin Moskala](https://github.com/MarcinMoskala) from [Kt Academy](https://kt.academy/)'s article:

- [Creating a random instance of any class in Kotlin](https://blog.kotlin-academy.com/creating-a-random-instance-of-any-class-in-kotlin-b6168655b64a)

## How to use

### Add the dependency

#### Create a GitHub Personal Access Token

To use the packages hosted on the GPR (GitHub Package Registry) you need your username/token.
Add the GPR as a repository inside your [build.gradle.kts](Example/build.gradle.kts):

```kotlin
maven {
    url = uri("https://maven.pkg.github.com/sylhare/RandomK")
    credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
    }

}
```

You can set those variables using:

```bash
export USERNAME="my-github-username"
export TOKEN="my-secret-personal-access-token-for-github"
```

Then add the randomK dependency:

```kotlin
dependencies {
    implementation("com.github.sylhare:randomk:$randomKVersion")
}
```

With the latest version available, check out the [_package_](https://github.com/sylhare/RandomK/packages/978387) section 😉 
Find the full example [here](Example).

### In your code

Since we're using `typeOf<T>()` fom kotlin-reflect to detect the retention type (introduced in kotlin v1.3), 
the library is under the `@ExperimentalStdlibApi`.

You can create a random instance with `randomK` like:

```kotlin
private val example: Example = randomK()
private val otherExample = randomK<OtherExample>()
```

It will create a random instance.
