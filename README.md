# Doist detekt rules

This repository contains custom detekt rules based on Doist internal coding conventions.

## How to use it

First add [detekt](https://github.com/detekt/detekt) to your repository. Then follow
[adding more rule sets](https://github.com/detekt/detekt#adding-more-rule-sets) instructions and add this
detekt plugin in your gradle file:
```
dependencies {
    detektPlugins("com.doist.detekt:detekt-rules:[version]")
}
```

## Release

To release, just push a tag with the new version number:
```
git tag vX.Y.Z
git push --tags
```
After that GitHub actions will automatically create new release and publish it.

## Rules

### NoBlankNewLineAfterClassHeader

This rule reports every class that has an empty line between a class header and its body.

### ConsistentWhenEntries

This rule reports when statements that have some entries single line and some multiline. 

### SingleLineWhenEntryExpressionsAreWrapped

This rule reports every when entry expression that is on a separate line and is not wrapped with 
brackets.

For example this is incorrect:
```kotlin
val a = when {
    c == b ->
        true
    else ->
        false
}
```
Instead it should be:
```kotlin
val a = when {
    c == b -> true
    else -> false
}
// or
val a = when {
    c == b -> {
        true
    }
    else -> {
        false
    }
}
```

### MutableObservablePropertyIsPrivate

This rule reports exposed `MutableLive...` and `MutableStateFlow` properties. They should be 
private.

### NoNotNullOperator

This rule reports `!!` usage. `requireNotNull` should be used instead.

### TodoPattern

Reports when TODO comment does not match a pattern. Default pattern is `// TODO(.+): .*`.


### NewLineAfterSuperCall

This rule reports if an override function does not have a new line after the super call.

For example this is incorrect:
```kotlin
 override fun foo() {
    super.foo()
    bar.bazinga()
}
```
Instead it should be:
```kotlin
 override fun foo() {
    super.foo()
    
    bar.bazinga()
}
```
