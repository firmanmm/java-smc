# Java Simple Message Codec
This is an implementation of Simple Message Codec in Java.

## Compability
This implementation should be compatible with `universal` version of other simple message codec that I wrote. 

## Conversion Table
| Type | Implementation | Converted |
| :------------- | :------------- | :---------- |
| boolean | Any | boolean |
| byte[] | Any | byte[] |
| String | Any | String |
| byte, short, int, long | Any | long |
| float, double | Any | double |
| List<Any> | Any | List<Object> |
| Map<Any, Any> | Any | Map<Object, Object>

## Example 
Below are example of using Simple Message Codec
```java
HashMap<Object, Object> original = new HashMap<>();
original.put(1L, 1123.312D);
original.put("Not A Number", 13123L);
original.put(-1L, "11111");
original.put(-2L, -2L);
original.put("ww", "www");
HashMap<String, String> nested = new HashMap<>();
nested.put("A", "Yes a string");
nested.put("Java is good", "you know");
nested.put("You can do all of this", "Just because of java");
original.put("nested", nested);

SimpleMessageCodec simpleMessageCodec = new SimpleMessageCodec();
byte[] encoded = simpleMessageCodec.encode(original);
Object decoded = simpleMessageCodec.decode(encoded);
```
Or maybe you prefer manual method, so you can encode the things that you like

```java

/* This is how DummyClass structure look like, just for reference
class DummyClass {
    String name;
    byte[] fingerPrint;
    int age;
    boolean active;
    double weight;
    List<String> relative;
}
*/
SimpleMessageCodec simpleMessageCodec = new SimpleMessageCodec();
DummyClass original = getDummyData();
byte[] encoded = simpleMessageCodec.manualEncode(original, (data, writer) -> {
    DummyClass dummyData = (DummyClass)data;
    writer.write(dummyData.name);
    writer.write(dummyData.age);
    writer.write(dummyData.fingerPrint);
    writer.write(dummyData.active);
    writer.write(dummyData.weight);
    writer.write(dummyData.relative);
});

DummyClass decoded = (DummyClass)simpleMessageCodec.manualDecode(encoded, reader -> {
    DummyClass dummyData = new DummyClass();
    dummyData.name = (String) reader.read();
    dummyData.age = (int)(long)reader.read();
    dummyData.fingerPrint = (byte[]) reader.read();
    dummyData.active = (boolean)reader.read();
    dummyData.weight = (double)reader.read();
    dummyData.relative = (List<String>)reader.read();
    return dummyData;
});
```