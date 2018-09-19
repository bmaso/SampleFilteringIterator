# SampleFilteringIterator

This code meeting these requirements:

Create an `Iterator` filtering framework with following: 
* `IObjectTest` interface with a single `boolean test(Object o)` method and 
* An implementation of `Iterator` (let's call it `FilteringIterator`) which is initialized with
  another `Iterator` and an `IObjectTest` instance: `new FilteringIterator(myIterator, myTest)`. Your
  `FilteringIterator` will then allow iteration over 'myIterator', but skipping any objects which don't
  pass the 'myTest' test. Create a simple unit test for this framework. 

Execute the unit tests that prove this test meets these requirements with the following bash commands (assuming you
have `git` and `mvn` installed):

```
git clone https://www.github.come/bmaso/SampleFilteringIterator && \
cd SampleFilteringIterator && \
mvn test
```
