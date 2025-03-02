# AEdept
Java console program for hospital registration - Presented as a single Java file to conform to the assignment submission rules.

## Data structure applied
### List
A List is an ordered collection (interface) in Java that allows duplicate elements. It extends the Collection interface and provides indexed access to elements.

### TreeMap
A TreeMap is a part of the Java Collections Framework and implements the NavigableMap interface, which extends SortedMap. It is a red-black tree-based implementation that keeps the keys sorted in ascending order by default.

### HashMap
A HashMap is a part of the Java Collections Framework that implements the Map interface. It stores key-value pairs and uses a hashing mechanism to provide efficient access to elements.


## Sorting algorithm applied

QuickSort, MergeSort, and InsertionSort are popular sorting algorithms, each suited for different scenarios. QuickSort is a divide-and-conquer algorithm that selects a pivot, partitions the array around it, and recursively sorts the subarrays, offering an average O(n log n) time complexity but can degrade to O(n²) in the worst case. MergeSort, also a divide-and-conquer approach, splits the array into halves, sorts them recursively, and merges the sorted halves, maintaining a stable O(n log n) complexity even in the worst case, making it ideal for linked lists and large datasets. InsertionSort, a simple and adaptive algorithm, builds a sorted list by inserting elements at their correct positions, performing best (O(n)) for nearly sorted data but inefficient (O(n²)) for large unsorted inputs. While QuickSort is often the fastest in practice, MergeSort is reliable for consistent performance, and InsertionSort excels for small or nearly sorted datasets.


## Input validation 
In Java, the Pattern and Matcher classes from the java.util.regex package are used for regular expression (regex) processing. Pattern is a compiled representation of a regex and is created using Pattern.compile(), improving efficiency for repeated use. Matcher is used to perform operations like searching, matching, and replacing text based on the compiled Pattern. The matcher.find() method finds occurrences of a pattern, while matcher.matches() checks if the entire string matches the regex. This approach is powerful for validating inputs (e.g., emails, phone numbers), text parsing, and complex string manipulations.

