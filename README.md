# objectMotherCreator

##At last the feature that none was asking for!!!

Moreover: it does not work quite well: I have problems with the event of exiting edition and pushing 'ok' or 'cancel'.

¯\_(ツ)_/¯

Seriously, if it is too annoying to you feel free to make a PR :P

Plugin for Intellij to generate ObjectMother for java classes

You can find it here:

https://plugins.jetbrains.com/plugin/14774-object-mother-creator

And install it by searching 

PR are welcome :)

## So, what does it do?

Creates object mothers for selected class on right-clicking over it.

Supports Kotlin, Java and Groovy classes To use it, select the file containing the class or over the editor tab of the opened file, right click -> Create object mother for current class

Generated code uses by default, to generate random Strings, Javafaker library.

You can add it (it generates random data for your tests, very usefully) to your project using maven or gradle, as example;

```
dependencies {
    testImplementation "com.github.javafaker:javafaker:1.0.2"
}
```

If you don't want to use javafaker library, you can configure what to use in setting page.

From 1.6.0.0 mappings are esxposed and custom mappings can be added, meaning that if when founding or having any class that is not mapped you can add it.

As example.

```
package my.package

class MyClass {
    ...
    public static from(String something)
    ...
}
```

And you use it in another one:
```
class Other {
    ....
    public Other(MyClass myClass)
    ....
}
```
You can add in configuration:

Class: Myclass
Comma separated imports: my.package
Code to generate random object: MyClass.from(faker.ancient().titan())

And it will generate de Objectmother:

```
class OtherObjectMother {
    public Other randomOther() {
        Faker faker = new Faekr();
        return new Other(Myclas.from(faker.ancient().titan()));
    }
}
```


Anyway: Although not configuring it this plugin will do the TEDIOUS work of creating, recursively, the packages, the ObjectMother.java files, and a 'randomClassName' static methods with required parameters, and you only would have to change those parameters.

https://martinfowler.com/bliki/ObjectMother.html


Last changes include:
```
1.0 - Initial release. Creates ObjectMother for selected java classes
1.0.1.2 - Increased support until 203.*
1.1.0 - Autodetect test source directory if exists and unique
1.1.0.1 - Remove support until label and refactor
1.1.0.2 - Fix multiple constructors format and the generation of random integer
1.2.0.0 - Add groovy support
1.2.0.1 - Fix documentation
1.2.0.2 - Add support until 211.*
1.2.0.3 - Remove a bug
1.3.0.0 - Add list and map support, opening it to typed classes
1.3.0.1 - Add support until 293.*
1.3.0.2 - Code refactors
1.4.0.0 - Add Kotlin support
1.4.1.0 - Identify UUID and Instant, fix errors
1.4.2.0 - Identify Boolean and Timestamp, refactor to improve agility adding more types
1.5.0.0 - Add a config page to customize Faker library
1.5.0.1 - Add default button to get back to default values and fix an annoying bug
1.6.0.0 - Add custom mappings in config page
```

## What's next?

The next thing to do is to add all the info needed to make the template of the objectMothers in the config page, as, per example:

* ObjectMother class suffix
* Creation of each class (lets say that you want to create UUIDs with Faker.randomUUID() instead of UUID.random() for whatever reason)
* ...

The idea is to let user, step by step, to fully configure the plugin.
