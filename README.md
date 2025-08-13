# Intro

keewebx project is opinioned web framework with tightly bindings on libraries of:

1. vertx for http handling and json object as data context format
2. htmx for protocol
   - keewebx offers an utility class implementation mentioned in ebook ['Unveil HTMX'(HTMX揭秘)](https://store.afoo.me/l/htmx)
3. jte for templating




# 关于Jte配置

首先， pom.xml里的jte maven plugin配置是为了把jte模板class跟程序的class一起打包到发布的jar包中：

```xml
            <plugin>
                <groupId>gg.jte</groupId>
                <artifactId>jte-maven-plugin</artifactId>
                <version>${jte.version}</version>
                <configuration>
                    <!-- This is the directory where your .jte files are located. -->
                    <sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
                    <!-- This is the directory where compiled templates are located. -->
                    <!--                    这种方式会编译到target/jte-classes下面，不会打包到jar，部署到时候可以rsync到服务器-->
                    <!--                    <targetDirectory>${project.build.directory}/jte-classes</targetDirectory>-->
                    <!--                    直接编译到target/classes目录下，会跟程序一起打包到jar里-->
                    <targetDirectory>${project.build.outputDirectory}</targetDirectory>
                    <contentType>Html</contentType>
                </configuration>
                <executions>
                    <execution>
                        <phase>process-classes</phase>
                        <goals>
                            <goal>precompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

这块儿跟Jte工具类的createPrecompiled方法配合，也就是生产环境下我们把模板跟一起打包到jar了：

```scala
  templateEngine.set(if (isProductionEnv()) {
    logger.info(s"profile=production, create Precompiled TemplateEngine for Jte.")
    val te = TemplateEngine.createPrecompiled(ContentType.Html)
    te.setBinaryStaticContent(true) // if we don't use Utf8ByteOutput, then the byte array will rollback to string for StringOutput
    te
  } else {
    logger.info(s"profile is not production, so create hot-reloadable TemplateEngine for Jte in Development phase.")
    val codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte")) // ResourceCodeResolver
    TemplateEngine.create(codeResolver, Path.of("jte-classes"), ContentType.Html)
  })
```

但是，如果我们不用`TemplateEngine.create(codeResolver, Path.of("jte-classes"), ContentType.Html)`这个重载方法， 那么，开发期间到jte模板热加载就失效了，因为它终究是要从classpath加载模板。

所以， TemplateEngine.create方法的第二个参数在dev期间就是必须的， 有了它，热加载的模板编译就可以输出到另一个地方（而不是按照pom.xml里的配置直接编译到target/classes下，从而dev和生产用的模板class相互覆盖）。

现在：

- 生产环境： 直接jar包的classpath加载
- dev环境： src/resources/jte下的模板会被动态编译到jte-classes目录，从而跟target/classes输出目录不一样，也避免了冲突。



# License

MIT