# Stinkpot

Stinkpot is a minimalist library that supports parsing N-Triples and Turtle into an immutable object model for processing with JVM applications.

The goals of this project are as follows:
* Support the N-Triples (http://www.w3.org/TR/n-triples/) and Turtle (https://www.w3.org/TR/turtle/) specifications
* Have an immutable object model that represents concepts from N-Triples (Triple, Subject, etc.) and Turtle (Statement, PredicateList, etc.)
* Have a single runtime dependency on the latest version of Kotlin (Stinkpot is implemented in Kotlin)
* Have extensive example based tests written in Spock
* Support OSGi
* Excrete a foul-smelling musk from the underside of the carapace when provoked
