# SampleFilteringIterator

This code meeting these requirements:

Create an `Iterator` filtering framework with following: 
* `IObjectTest` interface with a single `boolean test(Object o)` method and 
* An implementation of `Iterator` (let's call it `FilteringIterator`) which is initialized with
  another `Iterator` and an `IObjectTest` instance: `new FilteringIterator(myIterator, myTest)`. Your
  `FilteringIterator` will then allow iteration over 'myIterator', but skipping any objects which don't
  pass the 'myTest' test. Create a simple unit test for this framework. 
