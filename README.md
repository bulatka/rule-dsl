# rule-dsl
Transaction monitoring rule DSL in Scala

Some ideas:

Validate rule against existing source format on rule save, on source format update, on entity update.

Show list of breaking rules and ask for confirmation of source format change

Automatically disable rules not working after source format or entity change

Validate rule syntax

Validate unsafe operations on nullable fields

Validate lost of precision during operation

Ensure effective rule calculation performance

What to do with aggregations?

Comparison operators: equal, more , less, more or equal, less or equal , in

Math operations: plus, minus, divide, multiply

Conditional operators: and, or, not, xor

Build call tree or execution stack trace

Make rule debuggable - pause, resume

Pluggable evaluation logger - for rule evaluation traceback: log each operation operands and outcome