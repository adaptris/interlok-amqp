# interlok-artemis-amqp

Interlok with AMQP 1.0 + Apache Artemis

!Note that error messages can be quite confusing.

For example, during testing we forgot to create the queue on the host broker and received the following when attempting to publish

```
SMF AD ack response error [condition = amqp:not-allowed]
```