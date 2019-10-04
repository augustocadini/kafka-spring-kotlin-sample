package com.kafkakotlinsample.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class KafkaProducerController(val kafkaTemplate: KafkaTemplate<String, String>) {

    val topicPerson = "topic-kotlin-person"
    val topicCallback = "topic-kotlin-callback"

    /*
        Simple topic publish
     */
    @GetMapping("/person/{name}")
    fun getPersonName(@PathVariable name : String) : ResponseEntity<String> {
        val person = Optional.of(name)
        return if (person.isPresent) {
            kafkaTemplate.send(topicPerson, "GET /person/name OK > $name")
            ResponseEntity.ok(person.get())
        } else {
            kafkaTemplate.send(topicPerson, "GET /person/name BadRequest > $name")
            ResponseEntity.badRequest().body("Error")
        }
    }

    /*
        Simple topic publish with callback's result send
     */
    @GetMapping("/send_topic_callback/{message}")
    fun sendTopicCallback(@PathVariable message : String) : ResponseEntity<String> {
        val msg = Optional.of(message)
        return if (msg.isPresent) {
            kafkaTemplate.send(topicCallback, message).addCallback({
                println("Sent message=[" + message +
                        "] with offset=[" + it!!.recordMetadata.offset() + "]")
            }, {
                println("Unable to send message=["
                        + message + "] due to : " + it.message)
            })
            ResponseEntity.ok(msg.get())
        } else {
            kafkaTemplate.send(topicCallback, "GET /send_topic_callback/message BadRequest > $message")
            ResponseEntity.badRequest().body("Bad request!")
        }
    }

}