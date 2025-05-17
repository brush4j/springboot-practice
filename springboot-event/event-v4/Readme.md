官方推荐的优雅的泛型事件解决方案

实现接口ResolvableTypeProvider
```java
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }
```